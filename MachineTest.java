

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class MachineTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class MachineTest
{
    /**
     * Default constructor for test class MachineTest
     */
    public MachineTest()
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
    public void testConfiguration()
    {
        Machine machine1 = new Machine("config.properties", "rtn.txt");
        machine1.parseConfig("config.properties");
        assertEquals(machine1.wordSize, 4);
        assertEquals(machine1.numReg, 6);
        assertEquals(machine1.busSize, 4);
    }
}

