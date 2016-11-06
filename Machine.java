import java.util.*;
import java.io.*;

/**
 * Write a description of class Machine here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Machine
{
    public MainMem mainMem;
    public Register[] register;
    public int wordSize, numReg, busSize, memSize, maxImmediate, maxAddress;
    public Register pc, ir, md, ma, a, c;
    public ALU alu;
    public Bus bus;
    public Flags flags;
    public RTN RTNDefinition;
    public Microcode microcode;
    public String[] allowedALUOps;

    /**
     * Constructor for objects of class Machine
     */
    public Machine(String configFile, String RTNFile)
    {   
        System.out.println("Creating new machine ...");
        this.parseConfig(configFile);
        
        // Maximum addressable space is 2^(wordSize - 1)
        this.maxAddress = (int) Math.pow(2, 8 * (this.wordSize - 1));
        this.maxImmediate = (int) Math.pow(2, 8 * (this.wordSize - 2));
        this.memSize = (int) Math.pow(2, 9);
        this.numReg = numReg;
        this.busSize = busSize;
        this.allowedALUOps = allowedALUOps;
        
        this.RTNDefinition = new RTN(RTNFile);
        this.microcode = new Microcode(this);
        
        this.register = new Register[this.numReg];
        for (int i = 0; i<numReg; i++){
            this.register[i] = new Register(this.wordSize);
        }
        
        this.mainMem = new MainMem(this.memSize, this.wordSize);
        
        this.flags = new Flags();
        
        this.pc = new Register(this.wordSize);
        this.ir = new Register(this.wordSize);
        this.md = new Register(this.wordSize);
        this.ma = new Register(this.wordSize);
        this.a = new Register(this.wordSize);
        this.c = new Register(this.wordSize);
        
        this.bus = new Bus(this.busSize, this.wordSize);
        
        this.alu = new ALU(this.a, this.c, this.bus, this.flags, this.maxImmediate);
        
        System.out.println("Finished initializing machine");
    }
    
    public void parseConfig(String configFile) {
        try {
            // Initialize Properties loader
            Properties prop = new Properties();
            prop.load(new FileInputStream(configFile));
            // Read properties and store them in the config variables
            for (Map.Entry<Object, Object> entry : prop.entrySet())
            {
                if (entry.getKey().equals("WORDSIZE"))
                    this.wordSize = Integer.parseInt((String)entry.getValue());
                else if (entry.getKey().equals("NUMREGISTERS"))
                    this.numReg = Integer.parseInt((String)entry.getValue());
                else if (entry.getKey().equals("BUSSIZE"))
                    this.busSize = Integer.parseInt((String)entry.getValue());
                else if (entry.getKey().equals("ALLOWED_OPS"))
                    this.allowedALUOps = ((String)entry.getValue()).split(" , ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void run() {
        // Fetch and execute next instruction unless status is HLT or INS or ADR
        // ADR: An addressing error has occurred.
        // INS: An illegal instruction was encountered.
        // HLT: A halt instruction was encountered
        if(flags.getStatus() == "AOK")
            microcode.fetchExec();
    }
}
