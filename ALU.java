import java.math.*;
import java.nio.*;
import java.util.*;

public class ALU
{
    Bus bus;
    Register a, c;
    int wordSize;
    static int longBytes = Long.SIZE / Byte.SIZE;
    
    public ALU(Register a, Register c, Bus bus, Flags flags)
    {
        this.a = a;
        this.c = c;
        this.bus = bus;
        this.wordSize = a.wordSize();
    }
    
    public void add() {
        int aData = a.readInt();
        int busData = bus.readInt();
        c.writeInt(aData + busData);
    }

    public void add(int val) {
        int busData = bus.readInt();
        c.writeInt(val + busData);
    }

    public void subtract() {
        int aData = a.readInt();
        int busData = bus.readInt();
        c.writeInt(aData - busData);
    }

    public void multiply() {
        int aData = a.readInt();
        int busData = bus.readInt();
        c.writeInt(aData * busData);
    }

    public void and() {
        int aData = a.readInt();
        int busData = bus.readInt();
        c.writeInt(aData & busData);
    }

    public void or() {
        int aData = a.readInt();
        int busData = bus.readInt();
        c.writeInt(aData | busData);
    }

    public void xor() {
        int aData = a.readInt();
        int busData = bus.readInt();
        c.writeInt(aData & busData);
    }

}
