
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
    public int wordSize, numReg, busSize, memSize;
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
    public Machine(int wordSize, int numReg, int busSize, String[] allowedALUOps, String RTNFile)
    {   
        this.wordSize = wordSize;
        // Maximum addressable space is 2^(wordSize - 1)
        this.memSize = (int) Math.pow(2, 7);
        this.numReg = numReg;
        this.busSize = busSize;
        this.allowedALUOps = allowedALUOps;
        
        this.RTNDefinition = new RTN(RTNFile);
        this.microcode = new Microcode(this, RTNDefinition, allowedALUOps);
        
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
        
        this.bus = new Bus(busSize);
        
        this.alu = new ALU(a, c, bus, flags);
    }
    
    public void run() {
        microcode.fetchExec();
    }
}
