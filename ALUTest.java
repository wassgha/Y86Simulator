

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class ALUTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class ALUTest
{
    /**
     * Default constructor for test class ALUTest
     */
    public ALUTest()
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
    public void testAddEmpty()
    {
        Register a = new Register(4);
        Register c = new Register(4);
        Bus bus = new Bus(4);
        Flags flags = new Flags();
        ALU aLU1 = new ALU(a, c, bus, flags, 65536);
        aLU1.add(false);
        assertEquals(0, c.readInt());
    }
    
    @Test
    public void testAddEmptyAndBus()
    {
        Register a = new Register(4);
        Register c = new Register(4);
        Bus bus = new Bus(4);
        Flags flags = new Flags();
        ALU aLU1 = new ALU(a, c, bus, flags, 65536);
        bus.writeInt(5);
        aLU1.add(false);
        assertEquals(5, c.readInt());
    }
    
    @Test
    public void testAddAandBus()
    {
        Register a = new Register(4);
        Register c = new Register(4);
        Bus bus = new Bus(4);
        Flags flags = new Flags();
        ALU aLU1 = new ALU(a, c, bus, flags, 65536);
        a.writeInt(2);
        bus.writeInt(5);
        aLU1.add(false);
        assertEquals(7, c.readInt());
    }
    
    @Test
    public void testAddAandBusBig()
    {
        Register a = new Register(4);
        Register c = new Register(4);
        Bus bus = new Bus(4);
        Flags flags = new Flags();
        ALU aLU1 = new ALU(a, c, bus, flags, 65536);
        a.writeInt(3002010);
        bus.writeInt(102);
        aLU1.add(false);
        assertEquals(3002112, c.readInt());
    }
    
    @Test
    public void testIncrementA()
    {
        Register a = new Register(4);
        Register c = new Register(4);
        Bus bus = new Bus(4);
        Flags flags = new Flags();
        ALU aLU1 = new ALU(a, c, bus, flags, 65536);
        bus.writeInt(5);
        aLU1.add(6, false);
        assertEquals(11, c.readInt());
    }

    // Flags tested in FlagTest
    
}

