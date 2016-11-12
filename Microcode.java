import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.lang.*;

/**
 * Microcode class, translates the RTN instructions for each Y86 instruction
 * to executable methods and executes the instruction.
 * 
 * @author Wassim Gharbi
 */
public class Microcode
{
    HashMap<String, Byte> opCode;
    Machine machine;
    
    public Microcode(Machine machine)
    {
        this.machine = machine;
    }
    
    /**
     * Method fetchExec performs the fetch-execute cycle
     */
    public void fetchExec() {
        // Fetch The Instruction
        executeRTNOperations(null, true);
        // Execute the instruction that is stored in the IR (after fetching)
        executeRTNOperations(machine.ir.read(), false);
    }
    
    /**
     * Method executeRTNOperations gets the RTN definition of the instruction then performs the RTN
     * commands one by one if the condition is satisfied
     *
     * @param instruction the instruction (array of word-sized bytes)
     * @param fetch determines whether this is the fetch operation or not (fetch operation's definition is stored separately in the RTN)
     */
    public void executeRTNOperations(byte[] instruction, boolean fetch) {
        // If instruction is halt then terminate
        if(!fetch && instruction[0] == 0x0){
            machine.flags.setStatus("HLT");
            return;
        }
        
        // else get RTN for instruction and execute it
        ArrayList<Operands> instructionOperations;
        String condition = null;
        boolean conditionSatisfied = true;

        // Get the list of RTN operations necessary to execute the Y86 instruction
        if (fetch)
            // if this is a fetch operation then get the RTN definition for the fetch operation
            instructionOperations = machine.RTNDefinition.getFetchRTN();
        else {
            // else get RTN operations for the instruction based on the first byte (instruction code)
            instructionOperations = machine.RTNDefinition.getRTN(instruction[0]);
            // get condition necessary to run the instruction if it exists
            condition = machine.RTNDefinition.getCondition(instruction[0]);
        }
            
        if (instructionOperations == null) {
            machine.flags.setStatus("INS");
            System.out.println("Could not resolve RTN definition for instruction " + String.format("0x%08X", instruction[0]));
            return;
        }
        
        // If the instruction has a condition necessary for it to run then check whether
        // it is satisfied or not
        
        if (condition != null) {
            conditionSatisfied = false;
            // Note: Conditions are currently hard-coded. 
            // This needs to change in the future (should be checked by the ALU)
            switch (condition) {
                case "ZF":
                    conditionSatisfied=machine.flags.getZ();
                    break;
                case "SF=OF":
                    conditionSatisfied=machine.flags.getS() == machine.flags.getO();
                    break;
                case "SF<>OF":
                    conditionSatisfied=machine.flags.getS() != machine.flags.getO();
                    break;
                case "(~ZF)&(SF=OF)":
                    conditionSatisfied=(!machine.flags.getZ()) && (machine.flags.getS() == machine.flags.getO());
                    break;
                case "~ZF":
                    conditionSatisfied=!(machine.flags.getZ());
                    break;
                default:
                    machine.flags.setStatus("INS");
                    System.out.println("Unsupported condition");
                    break;
            }
        }
        
        if(!conditionSatisfied)
            return;
        
        for (Operands operation : instructionOperations) {
            if (operation.left.equals("C")) {
                // Operation has to be handled by the ALU
                boolean foundOp = false;
                // Look through the allowed operations and try to match
                // one of them inside the arithmetic expression
                for (String ALUOp : machine.allowedALUOps) {
                    if (operation.right.indexOf(ALUOp) != -1) {
                        String[] arithOperands = operation.right.split("\\" + ALUOp);
                        if(arithOperands[0].equals("A")) {
                            readOperandValueToBus(arithOperands[1], instruction);
                            performALUOp(ALUOp, null, fetch);
                        } else if (arithOperands[1].equals("A")) {
                            readOperandValueToBus(arithOperands[0], instruction);
                            performALUOp(ALUOp, null, fetch);
                        } else if (arithOperands[0].matches("[0-9]+")) {
                            readOperandValueToBus(arithOperands[1], instruction);
                            performALUOp(ALUOp, Integer.parseInt(arithOperands[0]), fetch);
                        } else if (arithOperands[1].matches("[0-9]+")) {
                            readOperandValueToBus(arithOperands[0], instruction);
                            performALUOp(ALUOp, Integer.parseInt(arithOperands[1]), fetch);
                        } else {
                            machine.flags.setStatus("INS");
                            System.out.println("Error parsing arithmetic operation");
                            return;
                        }
                        foundOp = true;
                    }
                }
                if (!foundOp) {
                    machine.flags.setStatus("INS");
                    System.out.println("Unauthorized arithmetic operation (" + operation.right + "). Allowed operations are : " + Arrays.toString(machine.allowedALUOps));
                    return;
                }
           } else if (operation.right.equals("M[MA]")) {
                // A memory read operation, doesn't go through the bus
                // Directly write to MD
                machine.md.write(machine.mainMem.read(machine.ma.readInt()));
           } else if (operation.left.equals("M[MA]")) {
                // A memory write operation, only write from MD
                machine.mainMem.write(machine.ma.readInt(), machine.md.read());
           } else {
                // Transfer Operation
                readOperandValueToBus(operation.right, instruction);
                writeOperandValue(operation.left, instruction);
            }
            // byte[] value = readOperandValue(operation.right, instruction);
            // writeOperandValue(operation.left, value, instruction);
        }       
    }
    
    /**
     * Method performALUOp, translates the arithmetic operation given as a string to a method from the ALU
     * and executes it (results always stored in register C)
     *
     * @param ALUOp the arithmetic operation (string, one of "+", "-", etc)
     * @param immediateVal if the operation is an increment/decrement operation then use the value to perform the operation
     * @param fetch true if this is the fetch operation, prevents updating flags
     */
    public void performALUOp(String ALUOp, Integer immediateVal, boolean fetch) {
        switch(ALUOp) {
            case "+":
                if (immediateVal==null)
                    machine.alu.add(fetch);
                else
                    machine.alu.add(immediateVal,fetch);
                break;
            case "-":
                if (immediateVal==null)
                    machine.alu.sub(fetch);
                else
                    machine.alu.sub(immediateVal,fetch);
                break;
            case "*":
                machine.alu.multiply(fetch);
                break;
            case "&":
                machine.alu.and(fetch);
                break;
            case "^":
                machine.alu.xor(fetch);
                break;
            case "|":
                machine.alu.or(fetch);
                break;
            case "~":
                machine.alu.not(fetch);
                break;
            default :
                machine.flags.setStatus("INS");
                System.out.println("Unsupported ALU instruction (" + ALUOp + ")");
                break;
        }
    }
    
    /**
     * Method readOperandValueToBus reads the value of the register indicated by the operand
     * and writes it to the system bus
     *
     * @param operand the right operand of the assignment (either a register name or an argument of the instruction)
     * @param instruction the instruction itself (used to get the arguments)
     */
    public void readOperandValueToBus(String operand, byte[] instruction) {
        int instruction_code_1 = 0, instruction_code_2 = 0, instruction_arg_1 = 0, instruction_arg_2 = 0;
        if (instruction != null) {
            // Instruction Specifiers (First Byte)
            instruction_code_1 = (instruction[0] & 0xf0) >> 4;
            instruction_code_2 = (instruction[0] & 0xf);
            
            // Register Specifiers (Second Byte) 
            instruction_arg_1 = (instruction[1] & 0xf0) >> 4;
            instruction_arg_2 = (instruction[1] & 0xf);
        }
        
        // Result
        byte[] value = new byte[machine.wordSize];
        switch (operand) {
            case "PC":  
                value = machine.pc.read();
                break;
            case "MD":  
                value = machine.md.read();
                break;
            case "MA":  
                value = machine.ma.read();
                break;
            case "IR":  
                value = machine.ir.read();
                break;
            case "C":  
                value = machine.c.read();
                break;
            case "A":  
                value = machine.a.read();
                break;
            case "%rsp":  
                value = machine.register[0].read();
                break;
            case "%rbp":  
                value = machine.register[1].read();
                break;
            case "R[arg1]":
                value = machine.register[instruction_arg_1].read();
                break;
            case "R[arg2]":
                value = machine.register[instruction_arg_2].read();
                break;
            case "arg1":  
                if (instruction_code_1 == 7 || instruction_code_1 == 8) {
                    // if the instruction is a jump or a call then the
                    // first argument is a memory address that begins at byte 1
                    // therefore it is just the instruction without the opCode
                    value = instruction.clone();
                    value[0] = 0x0;
                } else {
                    // if not then the instruction is an immediate move therefore
                    // the first argument is an immediate value that begins at byte 2
                    // therefore it is the instruction without the opCode and register specifiers
                    value = instruction.clone();
                    value[0] = 0x0;
                    value[1] = 0x0;
                }
                break;
            case "ZF":
                value = machine.flags.getZ() ? new byte[]{0x0,0x0,0x0,0x1} : new byte[]{0x0,0x0,0x0,0x0};
                break;
            case "SF":
                value = machine.flags.getS() ? new byte[]{0x0,0x0,0x0,0x1} : new byte[]{0x0,0x0,0x0,0x0};
                break;
            case "OF":
                value = machine.flags.getO() ? new byte[]{0x0,0x0,0x0,0x1} : new byte[]{0x0,0x0,0x0,0x0};
                break;
           default: 
                machine.flags.setStatus("INS");
                System.out.println("Could not resolve value to be assigned (" + operand + ")");
                return;
        }
        
        machine.bus.write(value);
    }

    public void writeOperandValue(String operand, byte[] instruction) {
        int instruction_code_1 = 0, instruction_code_2 = 0, instruction_arg_1 = 0, instruction_arg_2 = 0;
        if (instruction != null) {
            // Instruction Specifiers (First Byte)
            instruction_code_1 = (instruction[0] & 0xf0) >> 4;
            instruction_code_2 = (instruction[0] & 0xf);
            
            // Register Specifiers (Second Byte) 
            instruction_arg_1 = (instruction[1] & 0xf0) >> 4;
            instruction_arg_2 = (instruction[1] & 0xf);
        }
        
        byte[] valueToWrite = machine.bus.read();
        
        switch (operand) {
            case "PC":  
                machine.pc.write(valueToWrite);
                break;
            case "MD":  
                machine.md.write(valueToWrite);
                break;
            case "MA":  
                machine.ma.write(valueToWrite);
                break;
            case "IR":  
                machine.ir.write(valueToWrite);
                break;
            case "A":  
                machine.a.write(valueToWrite);
                break;
            case "%rsp":  
                machine.register[0].write(valueToWrite);
                break;
            case "%rbp":  
                machine.register[1].write(valueToWrite);
                break;
            case "R[arg1]":
                machine.register[instruction_arg_1].write(valueToWrite);
                break;
            case "R[arg2]":
                machine.register[instruction_arg_2].write(valueToWrite);
                break;
           default: 
                machine.flags.setStatus("INS");
                System.out.println("Could not resolve write medium (" + operand + ")");
                return;
        }
    }
}
