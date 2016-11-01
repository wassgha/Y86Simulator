import java.nio.*;
/**
 * Write a description of class Bus here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Bus
{
    private byte[] data;
    private int busSize;

    public Bus(int busSize)
    {
        this.busSize = busSize;
        data = new byte[busSize];
    }

    /**
     * Write to bus (word sized data)
     * 
     * @param  stream the data to write
     */
    public void write(byte[] stream) {
        if (stream.length != busSize) return;
        data = stream;
    }
    
    /**
     * Write to bus (an integer)
     * 
     * @param  stream the data to write
     */
    public void writeInt(int stream) {
        // This method won't work if the bus size is less than int's size
        if (busSize < 4) return;
        data = ByteBuffer.allocate(busSize).putInt(stream).array();
    }
    

    /**
     * Access bus data (word)
     * 
     * @return data in bus
     */
    public byte[] read()
    {
        return data;
    }
    
    /**
     * Access bus data (as integer)
     * 
     * @return data in bus
     */
    public int readInt()
    {
        ByteBuffer bb = ByteBuffer.allocate(4).put(data);

        return bb.getInt(0);
    }
    
    /**
     * Access bus data (as a hex string)
     * 
     * @return data in bus
     */
    public String readHex() {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}
