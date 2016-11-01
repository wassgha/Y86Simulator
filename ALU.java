import java.math.*;
import java.nio.*;
import java.util.*;

public class ALU
{
    Bus bus;
    Register a, c;
    int wordSize;
    Flags flags;
    
    /**
     * ALU Constructor
     *
     * @param a Register a (first input)
     * @param c Register c (result)
     * @param bus Bus data (second input)
     * @param flags Current processor flags, used to set Zero/Sign
     */
    public ALU(Register a, Register c, Bus bus, Flags flags)
    {
        this.a = a;
        this.c = c;
        this.bus = bus;
        this.flags = flags;
        this.wordSize = a.wordSize();
    }
    
    public void add() {
        int aData = a.readInt();
        int busData = bus.readInt();
        int result = aData + busData;
        flags.setZ(result==0);
        flags.setS(result<0);
        c.writeInt(result);
    }

    public void add(int val) {
        int busData = bus.readInt();
        int result = val + busData;
        flags.setZ(result==0);
        flags.setS(result<0);
        c.writeInt(result);
    }

    public void sub() {
        int aData = a.readInt();
        int busData = bus.readInt();
        int result = aData - busData;
        flags.setZ(result==0);
        flags.setS(result<0);
        c.writeInt(result);
    }

    public void sub(int val) {
        int busData = bus.readInt();
        int result = busData - val;
        flags.setZ(result==0);
        flags.setS(result<0);
        c.writeInt(result);
    }

    public void multiply() {
        int aData = a.readInt();
        int busData = bus.readInt();
        int result = aData * busData;
        flags.setZ(result==0);
        flags.setS(result<0);
        c.writeInt(result);
    }

    public void and() {
        int aData = a.readInt();
        int busData = bus.readInt();
        int result = aData & busData;
        flags.setZ(result==0);
        flags.setS(result<0);
        c.writeInt(result);
    }

    public void or() {
        int aData = a.readInt();
        int busData = bus.readInt();
        int result = aData | busData;
        flags.setZ(result==0);
        flags.setS(result<0);
        c.writeInt(result);
    }

    public void xor() {
        int aData = a.readInt();
        int busData = bus.readInt();
        int result = aData ^ busData;
        flags.setZ(result==0);
        flags.setS(result<0);
        c.writeInt(result);
    }

}
