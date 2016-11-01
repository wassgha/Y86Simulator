import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * AssemblyParser class, parses assembly file line by line, converts instructions
 * and parameters to machine language and stores them in memory.
 * 
 * @author Wassim Gharbi
 */
public class AssemblyParser
{
    HashMap<String, Byte> opCode;
    HashMap<String, Byte> regCode;
    
    public AssemblyParser(String filename)
    {
        y86Init();
        try {
            File file = new File(filename);
            Scanner input = new Scanner(file);
            
            while(input.hasNext()) {
                String nextLine = input.nextLine();
                
                Pattern labl_pattern = Pattern.compile("([A-Za-z][A-Za-z0-9_]*):");
                Matcher labl_m = labl_pattern.matcher(nextLine);
                
                Pattern inst_pattern = Pattern.compile("([A-Za-z][A-Za-z0-9]*)\\s*([^#]*)");
                Matcher inst_m = inst_pattern.matcher(nextLine);
                
                if (labl_m.find()) {
                    String label = labl_m.group(1);
                    System.out.println("LABEL : " + label);                    
                } else if (inst_m.find()) {
                    String instruction = inst_m.group(1);
                    String[] args = inst_m.group(2).split(",");
                    System.out.println("INSTRUCTION : " + instruction + " (" + String.format("0x%08X", opCode.get(instruction)) + "), ARGS : " + Arrays.toString(args));
                }
            }
            
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void y86Init() {
        // Fill Operation Code Table
        opCode = new HashMap<String, Byte>();
        opCode.put("halt", (byte)0x0);
        opCode.put("nop", (byte)0x10);
        opCode.put("rrmovl", (byte)0x20);
        // cmov.. 0x21 to 0x26
        opCode.put("irmovl", (byte)0x30);        
        opCode.put("rmmovl", (byte)0x40);        
        opCode.put("mrmovl", (byte)0x50);        
        opCode.put("addl", (byte)0x60);        
        opCode.put("subl", (byte)0x61);        
        opCode.put("andl", (byte)0x62);        
        opCode.put("xorl", (byte)0x63);        
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
        // Fill Register Names table
        regCode = new HashMap<String, Byte>();
        regCode.put("%r0", (byte)0x0);
        regCode.put("%r1", (byte)0x1);
        regCode.put("%r2", (byte)0x2);
        regCode.put("%r3", (byte)0x3);
        regCode.put("%r4", (byte)0x4);
        regCode.put("%r5", (byte)0x5);
        regCode.put("%r6", (byte)0x6);
        regCode.put("%r7", (byte)0x7);
    }

}
