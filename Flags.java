
/**
 * Write a description of class Flags here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Flags
{
    private boolean z, s, o;

    public Flags()
    {
        z = false;
        s = false;
        o = false;
    }
    
    public void setZ(boolean z)
    {
        this.z = z;
    }

    public void setS(boolean s)
    {
        this.s = s;
    }
    
    public void setO(boolean o)
    {
        this.o = o;
    }

    public boolean getZ()
    {
        return this.z;
    }

    public boolean getS()
    {
        return this.s;
    }
    
    public boolean getO()
    {
        return this.o;
    }
}
