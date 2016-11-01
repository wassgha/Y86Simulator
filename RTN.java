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
    HashMap<String, Byte> regCode;
    HashMap<String, Byte> opCode;
    //HashMap<Byte, String> revRegCode;
    //HashMap<Byte, String> revOpCode;
    
    // A HashMap containing each parsed RTN instruction and its list of assignments
    // (stored as a list of left and right operands)
    HashMap<Byte, ArrayList<Operands>> instructionList;
    
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
            opCode.put("pushl", (byte)0xA0);        
            opCode.put("popl", (byte)0xB0);
    
            // Fill Reverse Operation Code Table
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
    
            // Fill Register Names table
            regCode = new HashMap<String, Byte>();
            regCode.put("%eax", (byte)0x0);
            regCode.put("%ecx", (byte)0x1);
            regCode.put("%edx", (byte)0x2);
            regCode.put("%ebx", (byte)0x3);
            regCode.put("%esp", (byte)0x4);
            regCode.put("%ebp", (byte)0x5);
            regCode.put("%esi", (byte)0x6);
            regCode.put("%edi", (byte)0x7);
    
            // Fill Reverse Register Names table
            //         revRegCode = new HashMap<Byte, String>();
            //         revRegCode.put((byte)0x0, "%eax");
            //         revRegCode.put((byte)0x1, "%ecx");
            //         revRegCode.put((byte)0x2, "%edx");
            //         revRegCode.put((byte)0x3, "%ebx");
            //         revRegCode.put((byte)0x4, "%esp");
            //         revRegCode.put((byte)0x5, "%ebp");
            //         revRegCode.put((byte)0x6, "%esi");
            //         revRegCode.put((byte)0x7, "%edi");
    }
    
    /**
     * Method parseInstructions reads the RTN File and stores the series of RTN instructions
     * necessary to accomplish every ISA instruction.
     *
     * @param filename The path to the RTN File
     */
    public void parseInstructions(String filename) {
        try {
            System.out.println("******************************************");
            System.out.println("Parsing RTN (" + filename + ")...");
            System.out.println("******************************************");
            // Initialize Scanner
            File file = new File(filename);
            Scanner input = new Scanner(file);
            
            // Variables to store the instruction currently being fetched
            String curInstructionName = "";
            ArrayList<Operands> curInstruction = new ArrayList<Operands>();
            
            // Begin Scanner
            while(input.hasNext()) {
                // Read line by line
                String nextLine = input.nextLine();
                
                // Regex Matching for the start of an instruction
                // declaration (in the form "instruction_name:")
                Pattern inst_name_pattern = Pattern.compile("([A-Za-z][A-Za-z0-9_]*):");
                Matcher inst_name_m = inst_name_pattern.matcher(nextLine);
                
                // Regex Matching for sub-instructions of the current
                // RTN instruction being fetched
                Pattern inst_pattern = Pattern.compile("\t(.*)←(.*)");
                Matcher inst_m = inst_pattern.matcher(nextLine);
                
                // Each line could be the start of a new ISA (Y86) instruction
                // or an RTN instruction belonging to an ISA (Y86) instruction
                // or just a blank line
                if (inst_name_m.find()) {
                    // If this is the first Y86 instruction then don't add it yet
                    if (!instructionList.isEmpty() || !curInstruction.isEmpty())
                        if(curInstructionName.equals("FETCH"))
                            fetchInstructionList = curInstruction;
                        else
                            instructionList.put(opCode.get(curInstructionName), curInstruction);
                        
                    // Get the ISA (Y86) instruction name and re-initialize the current RTN instruction array
                    curInstructionName = inst_name_m.group(1).trim();
                    curInstruction = new ArrayList<Operands>();
                    System.out.println();
                    System.out.println("INSTRUCTION : " + curInstructionName + " CODE : " + String.format("0x%08X", opCode.get(curInstructionName)));                    
                } else if (inst_m.find()) {
                    // An RTN instruction is always an assignment in the form :  variable←expression
                    String variable = inst_m.group(1).trim();
                    String expression = inst_m.group(2).trim();
                    curInstruction.add(new Operands(variable, expression));
                    System.out.println("OPERAND1 : " + variable + ", OPERAND2 : " + expression);
                }
            }
            
            // Add the last instruction (wouldn't be add inside the loop since there is no instruction after it)
            if(!curInstruction.isEmpty())
                instructionList.put(opCode.get(curInstructionName), curInstruction);
            
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
}
