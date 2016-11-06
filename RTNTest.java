

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class RTNTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class RTNTest
{
    /**
     * Default constructor for test class RTNTest
     */
    public RTNTest()
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
    public void testFETCHOp()
    {
        RTN rTN1 = new RTN("rtn.txt");
        java.util.ArrayList<Operands> fetchOperands = rTN1.getFetchRTN();
        assertTrue(fetchOperands.size()>0);
    }

    @Test
    public void testrrmovqOpParsed()
    {
        RTN rTN1 = new RTN("rtn.txt");
        java.util.ArrayList<Operands> rrmovqOperands = rTN1.getRTN((byte) 0x20);
        assertTrue(rrmovqOperands.size()>0);
    }


    @Test
    public void testjmpOpParsed()
    {
        RTN rTN1 = new RTN("rtn.txt");
        java.util.ArrayList<Operands> jmpOperands = rTN1.getRTN((byte) 0x70);
        assertTrue(jmpOperands.size()>0);
    }
}

