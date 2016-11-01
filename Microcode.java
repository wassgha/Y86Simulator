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
    HashMap<String, Byte> regCode;
    Machine machine;
    
    public Microcode(Machine machine)
    {
        this.machine = machine;
    }
    
    public void fetchExec() {
        // Fetch The Instruction
        executeRTNOperations(null, true);
        // Execute the instruction that is stored in the IR (after fetching)
        executeRTNOperations(machine.ir.read(), false);
    }
    
    public void executeRTNOperations(byte[] instruction, boolean fetch) {
        // If instruction is halt then terminate
        if(instruction[0] == 0x0){
            machine.flags.setStatus("HLT");
            return;
        }
        
        // else get RTN for instruction and execute it
        ArrayList<Operands> instructionOperations;

        // Get the list of RTN operations necessary to execute the Y86 instruction
        if (fetch)
            // if this is a fetch operation then get the RTN definition for the fetch operation
            instructionOperations = machine.RTNDefinition.getFetchRTN();
        else
            // else get RTN operations for the instruction based on the first byte (instruction code)
            instructionOperations = machine.RTNDefinition.getRTN(instruction[0]);
            
        if (instructionOperations == null) {
            System.out.println("Could not resolve RTN definition for instruction " + String.format("0x%08X", instruction[0]));
            return;
        }
        
        for (Operands operation : instructionOperations) {
            if (operation.left.equals("C")) {
                // Operation has to be handled by the ALU
                boolean foundOp = false;
                for (String ALUOp : machine.allowedALUOps) {
                    if (operation.right.indexOf(ALUOp) != -1) {
                        String[] arithOperands = operation.right.split("\\" + ALUOp);
                        if(arithOperands[0].equals("A")) {
                            readOperandValueToBus(arithOperands[1], instruction);
                            performALUOp(ALUOp, null);
                        } else if (arithOperands[1].equals("A")) {
                            readOperandValueToBus(arithOperands[0], instruction);
                            performALUOp(ALUOp, null);
                        } else if (arithOperands[0].matches("[0-9]+")) {
                            readOperandValueToBus(arithOperands[1], instruction);
                            performALUOp(ALUOp, Integer.parseInt(arithOperands[0]));
                        } else if (arithOperands[1].matches("[0-9]+")) {
                            readOperandValueToBus(arithOperands[0], instruction);
                            performALUOp(ALUOp, Integer.parseInt(arithOperands[1]));
                        } else {
                            System.out.println("Error parsing arithmetic operation");
                            return;
                        }
                        foundOp = true;
                    }
                }
                if (!foundOp) {
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
    
    public void performALUOp(String ALUOp, Integer immediateVal) {
        switch(ALUOp) {
            case "+":
                if (immediateVal==null)
                    machine.alu.add();
                else
                    machine.alu.add(immediateVal);
                break;
            case "-":
                if (immediateVal==null)
                    machine.alu.sub();
                else
                    machine.alu.sub(immediateVal);
                break;
            case "*":
                machine.alu.multiply();
                break;
            case "&":
                machine.alu.and();
                break;
            case "^":
                machine.alu.xor();
                break;
            case "|":
                machine.alu.or();
                break;
            default :
                System.out.println("Unsupported ALU instruction (" + ALUOp + ")");
                break;
        }
    }
    
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
                    value = instruction.clone();
                    value[0] = 0x0;
                } else {
                    // if not then the instruction is an immediate move therefore
                    // the first argument is an immediate value that begins at byte 2
                    value = instruction.clone();
                    value[0] = 0x0;
                    value[1] = 0x0;
                }
                break;
           default: 
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
            case "R[arg1]":
                machine.register[instruction_arg_1].write(valueToWrite);
                break;
            case "R[arg2]":
                machine.register[instruction_arg_2].write(valueToWrite);
                break;
           default: 
                System.out.println("Could not resolve write medium (" + operand + ")");
                return;
        }
    }
}
