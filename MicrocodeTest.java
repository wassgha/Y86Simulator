

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class MicrocodeTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class MicrocodeTest
{
    /**
     * Default constructor for test class MicrocodeTest
     */
    public MicrocodeTest()
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
    public void testHLT()
    {
        Machine machine1 = new Machine("config.properties", "rtn.txt");
        Microcode microcod1 = new Microcode(machine1);
        Flags flags1 = machine1.flags;
        assertEquals("AOK", flags1.getStatus());
        microcod1.executeRTNOperations(new byte[]{0x0,0x0,0x0,0x0}, false);
        assertEquals("HLT", flags1.getStatus());
    }

    @Test
    public void testIRMOVQ()
    {
        Machine machine1 = new Machine("config.properties", "rtn.txt");
        Microcode microcod1 = new Microcode(machine1);
        Register register[] = machine1.register;
        assertEquals(0, register[4].readInt());
        // irmovq B to register %r4
        microcod1.executeRTNOperations(new byte[]{0x30,0x04,0x0,0xB}, false);
        // 0xB = 11
        assertEquals(11, register[4].readInt());
    }
    
    @Test
    public void testRRMOVQ()
    {
        Machine machine1 = new Machine("config.properties", "rtn.txt");
        Microcode microcod1 = new Microcode(machine1);
        Register register[] = machine1.register;
        assertEquals(0, register[2].readInt());
        assertEquals(0, register[3].readInt());
        // irmovq 9 to register %r2
        microcod1.executeRTNOperations(new byte[]{0x30,0x02,0x0,0x09}, false);
        assertEquals(9, register[2].readInt());
        assertEquals(0, register[3].readInt());
        microcod1.executeRTNOperations(new byte[]{0x20,0x23,0x0,0x0}, false);
        assertEquals(9, register[2].readInt());
        assertEquals(9, register[3].readInt());
    }

    @Test
    public void testRMMOVQ()
    {
        Machine machine1 = new Machine("config.properties", "rtn.txt");
        Microcode microcod1 = new Microcode(machine1);
        Register register[] = machine1.register;
        MainMem mainMem = machine1.mainMem;
        assertEquals(0, register[2].readInt());
        assertEquals(0, mainMem.readInt(5)); // make sure location 5 is initially empty
        // irmovq 9 to register %r2
        microcod1.executeRTNOperations(new byte[]{0x30,0x02,0x0,0x09}, false);
        assertEquals(9, register[2].readInt());
        // irmovq 5 to register %r3
        microcod1.executeRTNOperations(new byte[]{0x30,0x03,0x0,0x05}, false);
        assertEquals(5, register[3].readInt());
        // make sure memory is still empty
        assertEquals(0, mainMem.readInt(5));
        // rmmovq %r2, (%r3)
        microcod1.executeRTNOperations(new byte[]{0x40,0x23,0x0,0x0}, false);
        assertEquals(9, register[2].readInt());
        assertEquals(5, register[3].readInt());
        assertEquals(9, mainMem.readInt(5));
    }

    @Test
    public void testMRMOVQ()
    {
        Machine machine1 = new Machine("config.properties", "rtn.txt");
        Microcode microcod1 = new Microcode(machine1);
        Register register[] = machine1.register;
        MainMem mainMem = machine1.mainMem;
        assertEquals(0, register[2].readInt());
        assertEquals(0, mainMem.readInt(5));
       // irmovq 5 to register %r3
        microcod1.executeRTNOperations(new byte[]{0x30,0x03,0x0,0x05}, false);
        assertEquals(5, register[3].readInt());
       // irmovq 9 to register %r4
        microcod1.executeRTNOperations(new byte[]{0x30,0x04,0x0,0x09}, false);
        assertEquals(9, register[4].readInt());
        // rmmovq : move 9 (=%r4) to memory at location 5 (= %r3)
        microcod1.executeRTNOperations(new byte[]{0x40,0x43,0x0,0x00}, false);
        assertEquals(9, mainMem.readInt(5));
        // make sure dest register is empty
        assertEquals(0, register[2].readInt());
        // mrmovq (%r3), %r2
        microcod1.executeRTNOperations(new byte[]{0x50,0x32,0x0,0x0}, false);
        assertEquals(9, register[2].readInt());
        assertEquals(5, register[3].readInt());
        assertEquals(9, register[4].readInt());
        assertEquals(9, mainMem.readInt(5));
    }
    
    @Test
    public void testADDQ()
    {
        Machine machine1 = new Machine("config.properties", "rtn.txt");
        Microcode microcod1 = new Microcode(machine1);
        Register register[] = machine1.register;
        MainMem mainMem = machine1.mainMem;
        assertEquals(0, register[2].readInt());
        assertEquals(0, mainMem.readInt(5));
       // irmovq 5 to register %r3
        microcod1.executeRTNOperations(new byte[]{0x30,0x03,0x0,0x05}, false);
        assertEquals(5, register[3].readInt());
       // irmovq 9 to register %r4
        microcod1.executeRTNOperations(new byte[]{0x30,0x04,0x0,0x09}, false);
        assertEquals(9, register[4].readInt());
        // addq %r4, %r3
        microcod1.executeRTNOperations(new byte[]{0x60,0x43,0x0,0x00}, false);
        assertEquals(14, register[3].readInt());
    }
    
    @Test
    public void testSUBQ()
    {
        Machine machine1 = new Machine("config.properties", "rtn.txt");
        Microcode microcod1 = new Microcode(machine1);
        Register register[] = machine1.register;
        MainMem mainMem = machine1.mainMem;
        assertEquals(0, register[2].readInt());
        assertEquals(0, mainMem.readInt(5));
       // irmovq 5 to register %r3
        microcod1.executeRTNOperations(new byte[]{0x30,0x03,0x0,0x05}, false);
        assertEquals(5, register[3].readInt());
       // irmovq 9 to register %r4
        microcod1.executeRTNOperations(new byte[]{0x30,0x04,0x0,0x09}, false);
        assertEquals(9, register[4].readInt());
        // addq %r3, %r4
        microcod1.executeRTNOperations(new byte[]{0x61,0x43,0x0,0x00}, false);
        assertEquals(-4, register[3].readInt());
    }
    
    @Test
    public void testJMP()
    {
        Machine machine1 = new Machine("config.properties", "rtn.txt");
        Microcode microcod1 = new Microcode(machine1);
        assertEquals(0, machine1.pc.readInt());
       // jmp 0x05
        microcod1.executeRTNOperations(new byte[]{0x70,0x00,0x0,0x05}, false);
        assertEquals(5, machine1.pc.readInt());
    }

    @Test
    public void testJLE()
    {
        Machine machine1 = new Machine("config.properties", "rtn.txt");
        Microcode microcod1 = new Microcode(machine1);
        Register register[] = machine1.register;
        assertEquals(0, machine1.pc.readInt());
        // irmovq 5 to register %r3
        microcod1.executeRTNOperations(new byte[]{0x30,0x03,0x0,0x05}, false);
        assertEquals(5, register[3].readInt());
       // irmovq 9 to register %r4
        microcod1.executeRTNOperations(new byte[]{0x30,0x04,0x0,0x09}, false);
        assertEquals(9, register[4].readInt());
        // subq %r3, %r4
        microcod1.executeRTNOperations(new byte[]{0x61,0x43,0x0,0x00}, false);
        assertEquals(-4, register[3].readInt());
        // jle 0x05
        microcod1.executeRTNOperations(new byte[]{0x71,0x00,0x0,0x05}, false);
        assertEquals(5, machine1.pc.readInt());
    }

    @Test
    public void testJL()
    {
        Machine machine1 = new Machine("config.properties", "rtn.txt");
        Microcode microcod1 = new Microcode(machine1);
        Register register[] = machine1.register;
        assertEquals(0, machine1.pc.readInt());
        // irmovq 5 to register %r3
        microcod1.executeRTNOperations(new byte[]{0x30,0x03,0x0,0x05}, false);
        assertEquals(5, register[3].readInt());
       // irmovq 9 to register %r4
        microcod1.executeRTNOperations(new byte[]{0x30,0x04,0x0,0x09}, false);
        assertEquals(9, register[4].readInt());
        // subq %r3, %r4
        microcod1.executeRTNOperations(new byte[]{0x61,0x43,0x0,0x00}, false);
        assertEquals(-4, register[3].readInt());
        // jle 0x05
        microcod1.executeRTNOperations(new byte[]{0x72,0x00,0x0,0x05}, false);
        assertEquals(5, machine1.pc.readInt());
    }
    
    @Test
    public void testJG()
    {
        Machine machine1 = new Machine("config.properties", "rtn.txt");
        Microcode microcod1 = new Microcode(machine1);
        Register register[] = machine1.register;
        assertEquals(0, machine1.pc.readInt());
        // irmovq 5 to register %r3
        microcod1.executeRTNOperations(new byte[]{0x30,0x03,0x0,0x05}, false);
        assertEquals(5, register[3].readInt());
       // irmovq 9 to register %r4
        microcod1.executeRTNOperations(new byte[]{0x30,0x04,0x0,0x09}, false);
        assertEquals(9, register[4].readInt());
        // subq %r3, %r4
        microcod1.executeRTNOperations(new byte[]{0x61,0x34,0x0,0x00}, false);
        assertEquals(4, register[4].readInt());
        // je 0x05
        microcod1.executeRTNOperations(new byte[]{0x76,0x00,0x0,0x05}, false);
        assertEquals(5, machine1.pc.readInt());
    }
    
    @Test
    public void testJNE()
    {
        Machine machine1 = new Machine("config.properties", "rtn.txt");
        Microcode microcod1 = new Microcode(machine1);
        Register register[] = machine1.register;
        assertEquals(0, machine1.pc.readInt());
        // irmovq 5 to register %r3
        microcod1.executeRTNOperations(new byte[]{0x30,0x03,0x0,0x05}, false);
        assertEquals(5, register[3].readInt());
       // irmovq 9 to register %r4
        microcod1.executeRTNOperations(new byte[]{0x30,0x04,0x0,0x09}, false);
        assertEquals(9, register[4].readInt());
        // subq %r3, %r4
        microcod1.executeRTNOperations(new byte[]{0x61,0x34,0x0,0x00}, false);
        assertEquals(4, register[4].readInt());
        // je 0x05
        microcod1.executeRTNOperations(new byte[]{0x74,0x00,0x0,0x05}, false);
        assertEquals(5, machine1.pc.readInt());
    }


    @Test
    public void testJE()
    {
        Machine machine1 = new Machine("config.properties", "rtn.txt");
        Microcode microcod1 = new Microcode(machine1);
        Register register[] = machine1.register;
        assertEquals(0, machine1.pc.readInt());
        // irmovq 5 to register %r3
        microcod1.executeRTNOperations(new byte[]{0x30,0x03,0x0,0x05}, false);
        assertEquals(5, register[3].readInt());
       // irmovq 9 to register %r4
        microcod1.executeRTNOperations(new byte[]{0x30,0x04,0x0,0x09}, false);
        assertEquals(9, register[4].readInt());
        // subq %r3, %r4
        microcod1.executeRTNOperations(new byte[]{0x61,0x43,0x0,0x00}, false);
        assertEquals(-4, register[3].readInt());
        // je 0x05
        microcod1.executeRTNOperations(new byte[]{0x73,0x00,0x0,0x05}, false);
        assertEquals(0, machine1.pc.readInt());
        // irmovq 5 to register %r4
        microcod1.executeRTNOperations(new byte[]{0x30,0x04,0x0,0x05}, false);
        assertEquals(5, register[4].readInt());
        // irmovq 5 to register %r3
        microcod1.executeRTNOperations(new byte[]{0x30,0x03,0x0,0x05}, false);
        assertEquals(5, register[3].readInt());
        // subq %r3, %r4
        microcod1.executeRTNOperations(new byte[]{0x61,0x43,0x0,0x00}, false);
        assertEquals(0, register[3].readInt());
        // je 0x05
        microcod1.executeRTNOperations(new byte[]{0x73,0x00,0x0,0x05}, false);
        assertEquals(5, machine1.pc.readInt());
    }
}

