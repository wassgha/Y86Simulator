
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
    public Register pc, ir, md, ma, a, c, rsp, rbp;
    public ALU alu;
    public Bus bus;
    public Flags flags;
    public RTN RTNDefinition;
    public Microcode microcode;
    public String[] allowedALUOps;

    /**
     * Constructor for objects of class Machine
     */
    public Machine(int wordSize, int numReg, int busSize, String[] allowedALUOps, String RTNFile)
    {   
        System.out.println("Creating new machine ...");
        this.wordSize = wordSize;
        // Maximum addressable space is 2^(wordSize - 1)
        this.maxAddress = (int) Math.pow(2, 8 * (wordSize - 1));
        this.maxImmediate = (int) Math.pow(2, 8 * (wordSize - 2));
        this.memSize = (int) Math.pow(2, 9);
        this.numReg = numReg;
        this.busSize = busSize;
        this.allowedALUOps = allowedALUOps;
        
        this.RTNDefinition = new RTN(RTNFile);
        this.microcode = new Microcode(this);
        
        this.register = new Register[numReg];
        for (int i = 0; i<numReg; i++){
            this.register[i] = new Register(wordSize);
        }
        
        this.mainMem = new MainMem(memSize, wordSize);
        
        this.flags = new Flags();
        
        this.pc = new Register(wordSize);
        this.ir = new Register(wordSize);
        this.md = new Register(wordSize);
        this.ma = new Register(wordSize);
        this.a = new Register(wordSize);
        this.c = new Register(wordSize);
        this.rsp = new Register(wordSize);
        this.rbp = new Register(wordSize);
        
        this.bus = new Bus(busSize, wordSize);
        
        this.alu = new ALU(a, c, bus, flags, maxImmediate);
        
        System.out.println("Finished initializing machine");
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
