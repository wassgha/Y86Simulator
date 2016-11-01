import java.nio.*;
/**
 * Abstract class Register - write a description of the class here
 * 
 * @author (your name here)
 * @version (version number or date here)
 */
public class Register
{
    // instance variables - replace the example below with your own
    private byte[] data;
    private int wordSize;

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y    a sample parameter for a method
     * @return        the sum of x and y 
     */
    public Register(int wordSize)
    {
        data = new byte[wordSize];
        this.wordSize = wordSize;
    }
    
    /**
     * Access register data (word)
     * 
     * @return data in register
     */
    public byte[] read()
    {
        return data;
    }
    
    /**
     * Access register data (as integer)
     * 
     * @return data in register
     */
    public int readInt()
    {
        ByteBuffer bb = ByteBuffer.allocate(4).put(data);

        return bb.getInt(0);
    }
    
    /**
     * Access register data (as hex string)
     * 
     * @return data in register
     */
    public String readHex() {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
    
    /**
     * Write to register (byte sized data)
     * 
     * @param  stream the data to write
     */
    public void writeB(byte stream)
    {
        if (data.length - 1 < 0) return;
        data[data.length - 1] = stream;
    }
    
    /**
     * Write to register (word sized data)
     * 
     * @param  stream the data to write
     */
    public void write(byte[] stream) {
        if (stream.length != wordSize) return;
        data = stream;
    }
    
    /**
     * Write to register (an integer)
     * 
     * @param  stream the data to write
     */
    public void writeInt(int stream) {
        // This method won't work if the register is less than int's size
        if (wordSize < 4) return;
        
        data = ByteBuffer.allocate(wordSize).putInt(stream).array();
    }
    
    public int wordSize() {
        return this.wordSize;
    }
}
