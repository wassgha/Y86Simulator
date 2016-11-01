

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class RegisterTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class RegisterTest
{
    /**
     * Default constructor for test class RegisterTest
     */
    public RegisterTest()
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
    public void testRegisterSize4Empty()
    {
        Register register1 = new Register(4);
        assertEquals("00 00 00 00", register1.readHex());
        assertEquals(0, register1.readInt());
        assertEquals(4, register1.wordSize());
        assertArrayEquals(new byte[] {0x0, 0x0, 0x0, 0x0}, register1.read());
    }
    
    
    @Test
    public void testRegisterSize4WriteByte()
    {
        Register register1 = new Register(4);
        register1.writeB((byte) 0x5A);
        assertEquals(90, register1.readInt());
        assertEquals("00 00 00 5A", register1.readHex());
    }

    @Test
    public void testRegisterSize4WriteInt()
    {
        Register register1 = new Register(4);
        register1.writeInt(20);
        assertEquals(20, register1.readInt());
        assertEquals("00 00 00 14", register1.readHex());
    }

    @Test
    public void testRegisterSize4WriteWord()
    {
        Register register1 = new Register(4);
        register1.write(new byte[] {(byte) 0x10, (byte) 0xCA, (byte) 0xAB, (byte) 0x00});
        assertArrayEquals(new byte[] {(byte) 0x10, (byte) 0xCA, (byte) 0xAB, (byte) 0x00}, register1.read());
        assertEquals(281717504, register1.readInt());
        assertEquals("10 CA AB 00", register1.readHex());
    }

    @Test
    public void testRegisterSize2Empty()
    {
        Register register1 = new Register(4);
        assertEquals("00 00 00 00", register1.readHex());
        assertEquals(0, register1.readInt());
        assertEquals(4, register1.wordSize());
        assertArrayEquals(new byte[] {0x0, 0x0, 0x0, 0x0}, register1.read());
    }
    
    
    @Test
    public void testRegisterSize2WriteByte()
    {
        Register register1 = new Register(4);
        register1.writeB((byte) 0x5A);
        assertEquals(90, register1.readInt());
        assertEquals("00 00 00 5A", register1.readHex());
    }

    @Test
    public void testRegisterSize2WriteInt()
    {
        Register register1 = new Register(4);
        register1.writeInt(20);
        assertEquals(20, register1.readInt());
        assertEquals("00 00 00 14", register1.readHex());
    }

    @Test
    public void testRegisterSize2WriteWord()
    {
        Register register1 = new Register(4);
        register1.write(new byte[] {(byte) 0x10, (byte) 0xCA, (byte) 0xAB, (byte) 0x00});
        assertArrayEquals(new byte[] {(byte) 0x10, (byte) 0xCA, (byte) 0xAB, (byte) 0x00}, register1.read());
        assertEquals(281717504, register1.readInt());
        assertEquals("10 CA AB 00", register1.readHex());
    }

    @Test
    public void testRegisterSize0Empty()
    {
        Register register1 = new Register(0);
        assertEquals("", register1.readHex());
        assertEquals(0, register1.readInt());
        assertEquals(0, register1.wordSize());
        assertArrayEquals(new byte[] {}, register1.read());
    }
    
    
    @Test
    public void testRegisterSize0WriteByte()
    {
        Register register1 = new Register(0);
        register1.writeB((byte) 0x5A);
        assertEquals(0, register1.readInt());
        assertEquals("", register1.readHex());
    }

    @Test
    public void testRegisterSize0WriteInt()
    {
        Register register1 = new Register(0);
        register1.writeInt(20);
        assertEquals(0, register1.readInt());
        assertEquals("", register1.readHex());
    }

    @Test
    public void testRegisterSize0WriteWord()
    {
        Register register1 = new Register(0);
        register1.write(new byte[] {(byte) 0x10, (byte) 0xCA, (byte) 0xAB, (byte) 0x00});
        assertArrayEquals(new byte[] {}, register1.read());
        assertEquals(0, register1.readInt());
        assertEquals("", register1.readHex());
    }
}

