

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class BusTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class BusTest
{
    /**
     * Default constructor for test class BusTest
     */
    public BusTest()
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
    public void testBusSize4Empty()
    {
        Bus bus1 = new Bus(4, 4);
        assertEquals("00 00 00 00", bus1.readHex());
        assertEquals(0, bus1.readInt());
        assertArrayEquals(new byte[] {0x0, 0x0, 0x0, 0x0}, bus1.read());
    }

    @Test
    public void testBusSize4WriteInt()
    {
        Bus bus1 = new Bus(4, 4);
        bus1.writeInt(20);
        assertEquals(20, bus1.readInt());
        assertEquals("00 00 00 14", bus1.readHex());
    }

    @Test
    public void testBusSize4WriteWord()
    {
        Bus bus1 = new Bus(4, 4);
        bus1.write(new byte[] {(byte) 0x10, (byte) 0xCA, (byte) 0xAB, (byte) 0x00});
        assertArrayEquals(new byte[] {(byte) 0x10, (byte) 0xCA, (byte) 0xAB, (byte) 0x00}, bus1.read());
        assertEquals(281717504, bus1.readInt());
        assertEquals("10 CA AB 00", bus1.readHex());
    }

    @Test
    public void testBusSize2Empty()
    {
        Bus bus1 = new Bus(4, 4);
        assertEquals("00 00 00 00", bus1.readHex());
        assertEquals(0, bus1.readInt());
        assertArrayEquals(new byte[] {0x0, 0x0, 0x0, 0x0}, bus1.read());
    }
    
    @Test
    public void testBusSize2WriteInt()
    {
        Bus bus1 = new Bus(4, 4);
        bus1.writeInt(20);
        assertEquals(20, bus1.readInt());
        assertEquals("00 00 00 14", bus1.readHex());
    }

    @Test
    public void testBusSize2WriteWord()
    {
        Bus bus1 = new Bus(4, 4);
        bus1.write(new byte[] {(byte) 0x10, (byte) 0xCA, (byte) 0xAB, (byte) 0x00});
        assertArrayEquals(new byte[] {(byte) 0x10, (byte) 0xCA, (byte) 0xAB, (byte) 0x00}, bus1.read());
        assertEquals(281717504, bus1.readInt());
        assertEquals("10 CA AB 00", bus1.readHex());
    }

    @Test
    public void testBusSize0Empty()
    {
        Bus bus1 = new Bus(0, 4);
        assertEquals("", bus1.readHex());
        assertEquals(0, bus1.readInt());
        assertArrayEquals(new byte[] {}, bus1.read());
    }

    @Test
    public void testBusSize0WriteInt()
    {
        Bus bus1 = new Bus(0, 4);
        bus1.writeInt(20);
        assertEquals(0, bus1.readInt());
        assertEquals("", bus1.readHex());
    }

    @Test
    public void testBusSize0WriteWord()
    {
        Bus bus1 = new Bus(0, 4);
        bus1.write(new byte[] {(byte) 0x10, (byte) 0xCA, (byte) 0xAB, (byte) 0x00});
        assertArrayEquals(new byte[] {}, bus1.read());
        assertEquals(0, bus1.readInt());
        assertEquals("", bus1.readHex());
    }
}
