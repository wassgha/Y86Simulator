import java.math.*;
import java.nio.*;
import java.util.*;

public class ALU
{
    Bus bus;
    Register a, c;
    int wordSize;
    int maxImmediate;
    Flags flags;
    
    /**
     * ALU Constructor
     *
     * @param a Register a (first input)
     * @param c Register c (result)
     * @param bus Bus data (second input)
     * @param flags Current processor flags, used to set Zero/Sign
     */
    public ALU(Register a, Register c, Bus bus, Flags flags, int maxImmediate)
    {
        this.a = a;
        this.c = c;
        this.bus = bus;
        this.flags = flags;
        this.wordSize = a.wordSize();
        this.maxImmediate = maxImmediate;
    }
    
    public void add() {
        int aData = a.readInt();
        int busData = bus.readInt();
        int result = aData + busData;
        setFlags(result);
        c.writeInt(result);
    }

    public void add(int val) {
        int busData = bus.readInt();
        int result = val + busData;
        setFlags(result);
        c.writeInt(result);
    }

    public void sub() {
        int aData = a.readInt();
        int busData = bus.readInt();
        int result = aData - busData;
        setFlags(result);
        c.writeInt(result);
    }

    public void sub(int val) {
        int busData = bus.readInt();
        int result = busData - val;
        setFlags(result);
        c.writeInt(result);
    }

    public void multiply() {
        int aData = a.readInt();
        int busData = bus.readInt();
        int result = aData * busData;
        setFlags(result);
        c.writeInt(result);
    }

    public void and() {
        int aData = a.readInt();
        int busData = bus.readInt();
        int result = aData & busData;
        setFlags(result);
        c.writeInt(result);
    }

    public void or() {
        int aData = a.readInt();
        int busData = bus.readInt();
        int result = aData | busData;
        setFlags(result);
        c.writeInt(result);
    }

    public void xor() {
        int aData = a.readInt();
        int busData = bus.readInt();
        int result = aData ^ busData;
        setFlags(result);
        c.writeInt(result);
    }
    
    public void not() {
        int busData = bus.readInt();
        int result = ~busData;
        setFlags(result);
        c.writeInt(result);
    }
    
    private void setFlags(int result) {
        System.out.println("RESULT: " + result + "MAX : " + maxImmediate);
        flags.setO(result>maxImmediate || result<(-maxImmediate));
        flags.setZ(result==0);
        flags.setS(result<0);
    }

}
