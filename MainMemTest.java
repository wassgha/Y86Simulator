

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class MainMemTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class MainMemTest
{
    /**
     * Default constructor for test class MainMemTest
     */
    public MainMemTest()
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
    public void testByteToHexOneByteZero()
    {
        MainMem mainMem1 = new MainMem((int) Math.pow(2, 7), 8);
        assertEquals("00", mainMem1.byteToHex(new byte[]{(byte)0x0}));
    }

    @Test
    public void testByteToHexOneByteFF()
    {
        MainMem mainMem1 = new MainMem((int) Math.pow(2, 7), 8);
        assertEquals("FF", mainMem1.byteToHex(new byte[]{(byte)0xFF}));
    }

    @Test
    public void testByteToHexTwoBytesCC()
    {
        MainMem mainMem1 = new MainMem((int) Math.pow(2, 7), 8);
        assertEquals("CC CC", mainMem1.byteToHex(new byte[]{(byte)0xCC, (byte)0xCC}));
    }

    @Test
    public void testByteToHexFourBytes()
    {
        MainMem mainMem1 = new MainMem((int) Math.pow(2, 7), 8);
        assertEquals("55 EF AF C0", mainMem1.byteToHex(new byte[]{(byte)0x55, (byte)0xEF, (byte)0xAF, (byte)0xC0}));
    }

    @Test
    public void testMemSize128ReadWrite()
    {
        MainMem mainMem1 = new MainMem((int) Math.pow(2, 7), 4);
        java.lang.String[][] content = mainMem1.getContentHex();
        assertEquals(32, content.length);
        assertArrayEquals(new byte[] {0x0, 0x0, 0x0, 0x0}, mainMem1.read(0));
        assertArrayEquals(new byte[] {0x0, 0x0, 0x0, 0x0}, mainMem1.read(5));
        mainMem1.write(0, new byte[] {(byte)0xAA, (byte)0x2C, (byte)0x5A, (byte)0x11});
        mainMem1.write(5, new byte[] {(byte)0x55, (byte)0x43, (byte)0x22, (byte)0x1B});
        assertArrayEquals(new byte[] {(byte)0xAA, (byte)0x2C, (byte)0x5A, (byte)0x11}, mainMem1.read(0));
        assertArrayEquals(new byte[] {(byte)0x55, (byte)0x43, (byte)0x22, (byte)0x1B}, mainMem1.read(5));
    }

    @Test
    public void testMemorySize0ReadWrite()
    {
        MainMem mainMem1 = new MainMem(0, 4);
        assertNull(mainMem1.read(0));
        assertNull(mainMem1.read(5));
        mainMem1.write(0, new byte[] {(byte)0xAA, (byte)0x2C, (byte)0x5A, (byte)0x11});
        mainMem1.write(5, new byte[] {0x55, 0x43, 0x22, 0x1B});
        assertNull(mainMem1.read(0));
        assertNull(mainMem1.read(5));
    }
}




