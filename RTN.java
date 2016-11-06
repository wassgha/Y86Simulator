import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * RTN class, parses RTN file line by line, stores them in
 * a HashMap for later translation to actions in the simulator
 * 
 * @author Wassim Gharbi
 */
public class RTN
{
    HashMap<String, Byte> opCode;
    // Reverse lookup tables for Assembly Parser (to be implemented)
    // HashMap<Byte, String> revRegCode;
    // HashMap<Byte, String> revOpCode;
    
    // A HashMap containing each parsed RTN instruction and its list of assignments
    // (stored as a list of left and right operands)
    HashMap<Byte, ArrayList<Operands>> instructionList;
    
    // A HashMap containing the condition necessary for each RTN instruction to
    // be executed (if a condition exists)
    HashMap<Byte, String> conditionList;
    
    // A special list of assignments that define the FETCH operation
    ArrayList<Operands> fetchInstructionList;
    
    public RTN(String filename)
    {
        y86Init();
        parseInstructions(filename);
    }
    
    public void y86Init() {
            // Initialize the instruction list
            instructionList = new HashMap<Byte, ArrayList<Operands>>();
            conditionList = new HashMap<Byte, String>();
            
            // Fill Operation Code Table
            opCode = new HashMap<String, Byte>();
            opCode.put("halt", (byte)0x0);
            opCode.put("nop", (byte)0x10);
            opCode.put("rrmovq", (byte)0x20);
            // cmov.. 0x21 to 0x26
            opCode.put("irmovq", (byte)0x30);        
            opCode.put("rmmovq", (byte)0x40);        
            opCode.put("mrmovq", (byte)0x50);        
            opCode.put("addq", (byte)0x60);        
            opCode.put("subq", (byte)0x61);        
            opCode.put("andq", (byte)0x62);        
            opCode.put("xorq", (byte)0x63);        
            opCode.put("jmp", (byte)0x70);        
            opCode.put("jle", (byte)0x71);        
            opCode.put("jl", (byte)0x72);        
            opCode.put("je", (byte)0x73);        
            opCode.put("jne", (byte)0x74);        
            opCode.put("jge", (byte)0x75);        
            opCode.put("jg", (byte)0x76);        
            opCode.put("call", (byte)0x80);        
            opCode.put("ret", (byte)0x90);        
            opCode.put("pushq", (byte)0xA0);        
            opCode.put("popq", (byte)0xB0);
            

            // Fill Reverse Operation Code Table  (for use with assembly parser)
            //         revOpCode = new HashMap<Byte, String>();
            //         revOpCode.put((byte)0x0, "halt");
            //         revOpCode.put((byte)0x10, "nop");
            //         revOpCode.put((byte)0x20, "rrmovq");
            //         // cmov.. 0x21 to 0x26
            //         revOpCode.put((byte)0x30, "irmovq");        
            //         revOpCode.put((byte)0x40, "rmmovq");        
            //         revOpCode.put((byte)0x50, "mrmovq");        
            //         revOpCode.put((byte)0x60, "addq");        
            //         revOpCode.put((byte)0x61, "subq");        
            //         revOpCode.put((byte)0x62, "andq");        
            //         revOpCode.put((byte)0x63, "xorq");        
            //         revOpCode.put((byte)0x70, "jmp");        
            //         revOpCode.put((byte)0x71, "jle");        
            //         revOpCode.put((byte)0x72, "jl");        
            //         revOpCode.put((byte)0x73, "je");        
            //         revOpCode.put((byte)0x74, "jne");        
            //         revOpCode.put((byte)0x75, "jge");        
            //         revOpCode.put((byte)0x76, "jg");        
            //         revOpCode.put((byte)0x80, "call");        
            //         revOpCode.put((byte)0x90, "ret");        
            //         revOpCode.put((byte)0xA0, "pushl");        
            //         revOpCode.put((byte)0xB0, "popl");
    }
    
    /**
     * Method parseInstructions reads the RTN File and stores the series of RTN instructions
     * necessary to accomplish every ISA instruction.
     *
     * @param filename The path to the RTN File
     */
    public void parseInstructions(String filename) {
        try {
            System.out.println("Parsing RTN (" + filename + ")...");
            // Initialize Scanner
            File file = new File(filename);
            Scanner input = new Scanner(file);
            
            // Variables to store the instruction currently being fetched
            String curInstructionName = "";
            ArrayList<Operands> curInstruction = new ArrayList<Operands>();
            String curInstructionCond = null;
            
            // Begin Scanner
            while(input.hasNext()) {
                // Read line by line
                String nextLine = input.nextLine();
                
                // Regex Matching for the start of an instruction
                // declaration (in the form "instruction_name:")
                Pattern inst_name_pattern = Pattern.compile("([A-Za-z][A-Za-z0-9_]*):");
                Matcher inst_name_m = inst_name_pattern.matcher(nextLine);

                // Regex Matching for the condition necessary to execute
                // the instruction (in the form "(condition)→instruction"
                // declaration (in the form "instruction_name:")
                Pattern inst_cond_pattern = Pattern.compile("\\((.*)\\)→(.*)");
                
                // Regex Matching for sub-instructions of the current
                // RTN instruction being fetched
                Pattern inst_pattern = Pattern.compile("\t*(.*)←(.*)");
                Matcher instruction_match = inst_pattern.matcher(nextLine);
                
                // Each line could be the start of a new ISA (Y86) instruction
                // or an RTN instruction belonging to an ISA (Y86) instruction
                // or just a blank line
                if (inst_name_m.find()) {
                    // If this is the first Y86 instruction then don't add it yet
                    if (!instructionList.isEmpty() || !curInstruction.isEmpty())
                        if(curInstructionName.equals("FETCH"))
                            fetchInstructionList = curInstruction;
                        else {
                            instructionList.put(opCode.get(curInstructionName), curInstruction);
                        }
                        
                    // Get the ISA (Y86) instruction name and re-initialize the current RTN instruction array
                    curInstructionName = inst_name_m.group(1).trim();
                    curInstruction = new ArrayList<Operands>();
                    //System.out.println();
                    //System.out.println("INSTRUCTION : " + curInstructionName + " CODE : " + String.format("0x%08X", opCode.get(curInstructionName)));                    
                } else if (instruction_match.find()) {
                    // An RTN instruction is always an assignment in the form :  variable←expression
                    
                    String left_hand = instruction_match.group(1).trim();
                    String right_hand = instruction_match.group(2).trim();
                    
                    // Check whether the left hand side has a condition embedded into it
                    Matcher inst_cond_m = inst_cond_pattern.matcher(left_hand);
                    if (inst_cond_m.matches()) {
                        conditionList.put(opCode.get(curInstructionName), 
                        inst_cond_m.group(1).trim());
                        left_hand = inst_cond_m.group(2).trim();
                        //System.out.println("CONDITION " + inst_cond_m.group(1).trim());
                    }
                    
                    // Add the operands to the list of instructions
                    curInstruction.add(new Operands(left_hand, right_hand));
                    //System.out.println("OPERAND1 : " + left_hand + ", OPERAND2 : " + right_hand);
                }
            }
            
            // Add the last instruction (wouldn't be add inside the loop since there is no instruction after it)
            if(!curInstruction.isEmpty())
                instructionList.put(opCode.get(curInstructionName), curInstruction);
            
            System.out.println("Finished parsing RTN.");
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<Operands> getRTN(byte instruction) {
        return instructionList.get(instruction);
    }
    
    public ArrayList<Operands> getFetchRTN() {
        return fetchInstructionList;
    }

    public String getCondition(byte instruction) {
        return conditionList.get(instruction);
    }
}
