
/**
 * Write a description of class Flags here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Flags
{
    private boolean z, s, o;
    private String status;

    public Flags()
    {
        z = false;
        s = false;
        o = false;
        status = "AOK";
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

    public void setStatus(String status)
    {
        this.status = status;
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
    
    public String getStatus()
    {
        return this.status;
    }
}
