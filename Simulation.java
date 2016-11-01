import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


/**
 * Simulation GUI
 * 
 * @author Wassim Gharbi
 */
public class Simulation extends JFrame implements ActionListener
{
    // instance variables - replace the example below with your own
    private JTextField pc_val, ir_val, md_val, ma_val, a_val, c_val, z_val, o_val, s_val, status_val;  // Declare a TextField component 
    private JTextField register_vals[];
    private Button    load_button, run_button, stop_button, step_button, reset_button, exit_button;     // Declare a Button component

    private JFrame mainMemWindow;
    private JTable    memTable;
    private String[][] memTableData;

    private int numReg, wordSize;              // Counter's value
    private String rtnFile;
    
    private Machine machine; // Make a new CPU
    
    private RunTask runTask;
    private java.util.Timer timer;
    private boolean isRunning=false;
    
    /**
     * Constructor for objects of class CS203_GUI
     */
    public Simulation()
    {
        
        wordSize = 4; // 4 * 8 bits = 32 bit word size
        numReg = 6; // number of general registers
        rtnFile = "rtn.txt";
        
        // Create a machine simulator 
        machine = new Machine(wordSize, numReg, wordSize, rtnFile);
        
        // Length of field that displays the content of registers
        // Each register is a quad word (4 * wordSize) and each 4
        // bits are represented with 1 hex number. We also add spaces
        // between every two hex numbers
        int regTextFieldLength = 8 * wordSize + 4 * wordSize;
        
        
        getContentPane().setLayout(
            new BoxLayout(getContentPane(), BoxLayout.Y_AXIS)
        );
        
        //setBorder(BorderFactory.createEmptyBorder() ); 
        ((JComponent)getContentPane()).setBorder( 
        BorderFactory.createEmptyBorder( 10, 10, 10, 10) );

        register_vals = new JTextField[numReg];

        // "super" Frame (a Container) sets its layout to FlowLayout, which arranges
        // the components from left-to-right, and flow to next row from top-to-bottom.

        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout());
        
        p1.add(new JLabel("Control Simulation: "));
        
        load_button = new Button("Load");          // construct the Button component
        p1.add(load_button);                       // "super" Frame adds Button
        load_button.addActionListener(this);

        
        step_button = new Button("Step");          // construct the Button component
        p1.add(step_button);                       // "super" Frame adds Button
        step_button.addActionListener(this);

        run_button = new Button("Run");          // construct the Button component
        p1.add(run_button);                       // "super" Frame adds Button
        run_button.addActionListener(this);

        stop_button = new Button("Stop");          // construct the Button component
        p1.add(stop_button);                       // "super" Frame adds Button
        stop_button.addActionListener(this);

        reset_button = new Button("Reset");          // construct the Button component
        p1.add(reset_button);                       // "super" Frame adds Button
        reset_button.addActionListener(this);

        exit_button = new Button("Exit");        // construct the Button component
        p1.add(exit_button);                     // "super" Frame adds Button
        exit_button.addActionListener(this);

        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(2,2, 10, 10));

        p2.add(new JLabel("Z: ", SwingConstants.CENTER));
        z_val = new JTextField(machine.flags.getZ()?"1":"0", SwingConstants.LEFT);
        z_val.setEditable(false);
        p2.add(z_val);

        p2.add(new JLabel("S: ", SwingConstants.CENTER));
        s_val = new JTextField(machine.flags.getS()?"1":"0", SwingConstants.LEFT);
        s_val.setEditable(false);
        p2.add(s_val);

        p2.add(new JLabel("O: ", SwingConstants.CENTER));
        o_val = new JTextField(machine.flags.getO()?"1":"0", SwingConstants.LEFT);
        o_val.setEditable(false);
        p2.add(o_val);

        p2.add(new JLabel("Status: ", SwingConstants.CENTER));
        status_val = new JTextField("HLT");
        status_val.setEditable(false);
        p2.add(status_val);


        JPanel p3 = new JPanel();
        p3.setLayout(new GridLayout(3,2, 10, 10));
                
        p3.add(new JLabel("PC: ", SwingConstants.CENTER));
        pc_val = new JTextField(machine.pc.readHex(), SwingConstants.LEFT);
        pc_val.setEditable(false);
        p3.add(pc_val);

        p3.add(new JLabel("IR: ", SwingConstants.CENTER));
        ir_val = new JTextField(machine.ir.readHex(), SwingConstants.LEFT);
        ir_val.setEditable(false);
        p3.add(ir_val);
        
        p3.add(new JLabel("MA: ", SwingConstants.CENTER));
        ma_val = new JTextField(machine.ma.readHex(), SwingConstants.LEFT);
        ma_val.setEditable(false);
        p3.add(ma_val);
        
        p3.add(new JLabel("MD: ", SwingConstants.CENTER));
        md_val = new JTextField(machine.md.readHex(), SwingConstants.LEFT);
        md_val.setEditable(false);
        p3.add(md_val);
       
        p3.add(new JLabel("A: ", SwingConstants.CENTER));
        a_val = new JTextField(machine.a.readHex(), SwingConstants.LEFT);
        a_val.setEditable(false);
        p3.add(a_val);
       
        p3.add(new JLabel("C: ", SwingConstants.CENTER));
        c_val = new JTextField(machine.c.readHex(), SwingConstants.LEFT);
        c_val.setEditable(false);
        p3.add(c_val);

        JPanel p4 = new JPanel();
        p4.setLayout(new GridLayout(numReg/2,2, 10, 10));
        
        for (int i = 0; i<numReg; i++) {
            p4.add(new JLabel("%r" + i + " : ", SwingConstants.CENTER));
            register_vals[i] = new JTextField(machine.register[i].readHex(), SwingConstants.LEFT);
            register_vals[i].setEditable(false);
            p4.add(register_vals[i]);
        }
        

        setTitle("Y86 Machine Simulator (Wassim Gharbi)");  // "super" Frame sets its title
        setSize(650, 600);             // "super" Frame sets its initial window size
        //setResizable(false);
        
        // Add Simulation Control Pane
        add(p1);

        // Add Flags Pane
        JLabel flags_label = new JLabel("Flags / State");
        flags_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(20,20)));
        add(flags_label);
        add(Box.createRigidArea(new Dimension(20,20)));
        add(p2);
        
        // Add Specific Registers Pane
        JLabel sreg_label = new JLabel("Specific Registers");
        sreg_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(20,20)));
        add(sreg_label);
        add(Box.createRigidArea(new Dimension(20,20)));
        add(p3);
        
        // Add General Registers Pane
        JLabel greg_label = new JLabel("General Registers");
        greg_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(20,20)));
        add(greg_label);
        add(Box.createRigidArea(new Dimension(20,20)));
        add(p4);   
        
        setVisible(true);    
        

        memoryWindow();

    }

    // ActionEvent handler - Called back upon button-click.
    @Override
    public void actionPerformed(ActionEvent evt) {
        
        if (evt.getActionCommand().equals("Step")) {
            
            // Run one step of the program
            machine.run();
            
        } else if(evt.getActionCommand().equals("Load")) {
            
            // Writes the program to Main Memory
            writeProgram();
            
        } else if(evt.getActionCommand().equals("Reset")) {
            
            // Reset removes the old machine and makes a new one
            machine = new Machine(wordSize, numReg, wordSize, rtnFile);
            
        } else if (evt.getActionCommand().equals("Run")) {
            
            // If the program is not currently running then set up
            // a timer to run a set every second
            if (!isRunning) {
                timer = new java.util.Timer();
                timer.schedule(new RunTask(), 0, 1000);
                isRunning = true;
            }
            
        } else if (evt.getActionCommand().equals("Stop")) {
            
            // If the program is already running then
            // pause the timer (by canceling th current timer
            // then where resuming make a new one)
            if (isRunning) {
                timer.cancel();
                timer.purge();
                isRunning = false;
            }
            
        } else if (evt.getActionCommand().equals("Exit")) {
            
            // Terminate the program
            System.exit(0); 
        } else {
            
            System.out.println("Unknown action command " + evt.getActionCommand());
            
        }
        
        // Update values of all registers and memory addresses in the GUI
        updateState();
    }
    
    public void memoryWindow() {
        mainMemWindow = new JFrame("Main Memory");
        mainMemWindow.setTitle("Main Memory");  // "super" Frame sets its title
        mainMemWindow.setSize(200, 700);             // "super" Frame sets its initial window size
        mainMemWindow.setResizable(false);
        drawMemory();
    }
    
    public void drawMemory() {
        mainMemWindow.getContentPane().removeAll();
        String[] columnNames = {"Address",
                "Content"};
        
        memTableData = machine.mainMem.getContentHex();
        memTable = new JTable(memTableData, columnNames);
        
        memTable.setPreferredScrollableViewportSize(new Dimension(200, 700));
        memTable.setFillsViewportHeight(true);
        int curInstructionAddress = machine.pc.readInt()/wordSize;
        memTable.setRowSelectionInterval(curInstructionAddress, curInstructionAddress);
        
        JScrollPane js=new JScrollPane(memTable);
        js.setVisible(true);
        mainMemWindow.add(js);
        mainMemWindow.repaint();
        mainMemWindow.setVisible(true);
    }
    
    /**
     * Method updateState updates the values of all registers, flags
     * and memory contents in the GUI
     */
    public void updateState() {
        pc_val.setText(machine.pc.readHex());
        ir_val.setText(machine.ir.readHex());
        c_val.setText(machine.c.readHex());
        a_val.setText(machine.a.readHex());
        ma_val.setText(machine.ma.readHex());
        md_val.setText(machine.md.readHex());
        pc_val.setText(machine.pc.readHex());
        
        for (int i = 0; i<numReg; i++) {
            register_vals[i].setText(machine.register[i].readHex());
        }
        
        drawMemory();
    }
    
    public void writeProgram() {
        // pos 0
        machine.mainMem.write(0, new byte[]{0x0, 0x0, 0x0, 0x0});
        // irmovq 0x666, %r1
        machine.mainMem.write(4, new byte[]{0x30, 0x01, 0x00, 0x01});
        // rrmovq %r1, %r3
        machine.mainMem.write(8, new byte[]{0x20, 0x13, 0x00, 0x00});
        // addq %r1, %r3
        machine.mainMem.write(12, new byte[]{0x60, 0x13, 0x00, 0x00});
        // irmovq 0x7C, %r4
        machine.mainMem.write(16, new byte[]{0x30, 0x04, 0x00, (byte) 0x7C});
        // rmmovq %r3, (%r4)
        machine.mainMem.write(20, new byte[]{0x40, 0x34, 0x00, 0x00});
        // jmp 0x0C
        machine.mainMem.write(24, new byte[]{0x70, 0x0, 0x0, 0xC});

    }

    class RunTask extends TimerTask {
        @Override
        public void run() {
            machine.run();
            updateState();
        }
    }
}
