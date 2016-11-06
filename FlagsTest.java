

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class FlagsTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class FlagsTest
{
    /**
     * Default constructor for test class FlagsTest
     */
    public FlagsTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }

    @Test
    public void testZeroFlagTrueAdd()
    {
        Register a = new Register(4);
        Register c = new Register(4);
        Bus bus = new Bus(4, 4);
        Flags flags = new Flags();
        ALU aLU1 = new ALU(a, c, bus, flags, 65536);
        a.writeInt(-5);
        bus.writeInt(5);
        aLU1.add(false);
        assertTrue(flags.getZ());
    }

    @Test
    public void testZeroFlagTrueSub()
    {
        Register a = new Register(4);
        Register c = new Register(4);
        Bus bus = new Bus(4, 4);
        Flags flags = new Flags();
        ALU aLU1 = new ALU(a, c, bus, flags, 65536);
        a.writeInt(5);
        bus.writeInt(5);
        aLU1.sub(false);
        assertTrue(flags.getZ());
    }

    @Test
    public void testZeroFlagFalseAdd()
    {
        Register a = new Register(4);
        Register c = new Register(4);
        Bus bus = new Bus(4, 4);
        Flags flags = new Flags();
        ALU aLU1 = new ALU(a, c, bus, flags, 65536);
        a.writeInt(5);
        bus.writeInt(4);
        aLU1.add(false);
        assertFalse(flags.getZ());
    }

    @Test
    public void testZeroFlagFalseSub()
    {
        Register a = new Register(4);
        Register c = new Register(4);
        Bus bus = new Bus(4, 4);
        Flags flags = new Flags();
        ALU aLU1 = new ALU(a, c, bus, flags, 65536);
        a.writeInt(5);
        bus.writeInt(4);
        aLU1.sub(false);
        assertFalse(flags.getZ());
    }

    @Test
    public void testSignFlagFalseAdd()
    {
        Register a = new Register(4);
        Register c = new Register(4);
        Bus bus = new Bus(4, 4);
        Flags flags = new Flags();
        ALU aLU1 = new ALU(a, c, bus, flags, 65536);
        a.writeInt(5);
        bus.writeInt(4);
        aLU1.add(false);
        assertFalse(flags.getS());
    }

    @Test
    public void testSignFlagFalseSub()
    {
        Register a = new Register(4);
        Register c = new Register(4);
        Bus bus = new Bus(4, 4);
        Flags flags = new Flags();
        ALU aLU1 = new ALU(a, c, bus, flags, 65536);
        a.writeInt(5);
        bus.writeInt(4);
        aLU1.sub(false);
        assertFalse(flags.getS());
    }

    @Test
    public void testSignFlagTrueAdd()
    {
        Register a = new Register(4);
        Register c = new Register(4);
        Bus bus = new Bus(4, 4);
        Flags flags = new Flags();
        ALU aLU1 = new ALU(a, c, bus, flags, 65536);
        a.writeInt(-5);
        bus.writeInt(4);
        aLU1.add(false);
        assertTrue(flags.getS());
    }

    @Test
    public void testSignFlagTrueSub()
    {
        Register a = new Register(4);
        Register c = new Register(4);
        Bus bus = new Bus(4, 4);
        Flags flags = new Flags();
        ALU aLU1 = new ALU(a, c, bus, flags, 65536);
        a.writeInt(2);
        bus.writeInt(4);
        aLU1.sub(false);
        assertTrue(flags.getS());
    }

    @Test
    public void testOverflowFlagTrueAdd()
    {
        Register a = new Register(4);
        Register c = new Register(4);
        Bus bus = new Bus(4, 4);
        Flags flags = new Flags();
        ALU aLU1 = new ALU(a, c, bus, flags, 65536);
        a.writeInt(65100);
        bus.writeInt(4000);
        aLU1.add(false);
        assertTrue(flags.getO());
    }

    @Test
    public void testOverflowFlagTrueSub()
    {
        Register a = new Register(4);
        Register c = new Register(4);
        Bus bus = new Bus(4, 4);
        Flags flags = new Flags();
        ALU aLU1 = new ALU(a, c, bus, flags, 65536);
        a.writeInt(-1010);
        bus.writeInt(65530);
        aLU1.sub(false);
        assertTrue(flags.getO());
    }
}
