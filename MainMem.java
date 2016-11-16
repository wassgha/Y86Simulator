import java.nio.*;
import java.util.*;
/**
 * The Main Memory (byte-addressed memory simulation), holds code and data in a linear data structure
 * 
 * @author Wassim Gharbi
 */
public class MainMem
{
    // array of bytes that holds the memory data
    private byte[] mem;
    private int wordSize, memSize;

    /**
     * Constructor, makes a new Main memory of size memSize that reads and writes
     * in batches of size wordSize
     */
    public MainMem(int memSize, int wordSize)
    {
        // initialise memory array
        mem = new byte[memSize];
        this.memSize = memSize;
        this.wordSize = wordSize;
    }

    /**
     * Access memory data through address
     * 
     * @param  addr the address of the bytes accessed
     * @return  data at address specified
     */
    public byte[] read(int addr)
    {
        if (addr>=memSize) return null;
        
        byte[] result = new byte[wordSize];
        System.arraycopy(mem, addr, result, 0, wordSize);   
        return result;
    }

    /**
     * Access memory data data (as integer, for testing purposes only)
     * 
     * @return data at address specified
     */
    public int readInt(int addr)
    {
        ByteBuffer bb = ByteBuffer.allocate(4).put(read(addr));

        return bb.getInt(0);
    }
    
    /**
     * Write to memory at address
     * 
     * @param  addr the address of the bytes to write to
     * @param  data the data to write
     */
    public void write(int addr, byte[] data)
    {
        if (data.length != wordSize) return;
        if (addr>=memSize) return;
        
        for(int i=0; i<wordSize; i++) {
            mem[addr+i] = data[i];
        }
    }
    
    /**
     * Method byteToHex, converts a byte array (usually word sized) to its
     * hex representation (as a string)
     *
     * @param data word-sized byte array
     * @return hex representation of the bytes
     */
    protected String byteToHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
    
    /**
     * Method getContentHex returns the addresses and contents of the memory
     * as an array of hex formatted string (for display in the GUI
     *
     * @return hex representation of the memory content
     */
    public String[][] getContentHex() {
        String[][] result = new String[this.memSize/this.wordSize][2];
        for (int i = 0; i<this.memSize; i+=this.wordSize) {
            byte[] word = new byte[this.wordSize];
            
            for (int j=i; j<i+this.wordSize; j++) {
                // if memory is size is not divisible by word size
                if (j>=this.memSize) {
                    word = null;
                    break;
                }
                // else construct the word
                word[j-i] = mem[j];
            }

            if (word != null)
                result[i/this.wordSize] = new String[]{
                    byteToHex(new byte[]{(byte) i}),
                    byteToHex(word)
                };
        }
        return result;
    }
}
