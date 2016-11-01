# Y86 ISA Simulator in Java

**This Java program tries to simulate a simplified version of the [Y86 Instruction Set Architechture](http://cs.slu.edu/~fritts/CSCI224_S12/schedule/chap4-intro-Y86.pdf) in a simple graphical interface.** 

![Demo Program](http://i.imgur.com/U1DasZv.png)

  
## Running the simulation
To run the simulation, open a terminal window and make sure you are in the project's directory then use ```java Simulation``` to open the GUI. Refer to `README.TXT` for more instructions on configuring the simulation.

You are welcome to try it and if there is a problem, please contact me or [make a new issue](https://github.com/wassgha/Y86Simulator/issues/new). 

## Notes
* This software should not have any dependencies other than the standard Java libraries and Swing.
* Please review the `README` files for further instructions
* To visualize class relationships please use BlueJ

## Provided Demo Program
### While Loop
The current demo program is a primitive while loop that increments a counter and writes it to a memory location. The demo program demonstrates primitive Y86 instructions such as `mrmovq`, `rrmovq`, `irmovq`, `addq` and `jmp`. The program is hard-coded in Simulation.java and is written to memory through byte arrays.

![Demo Program](http://i.imgur.com/6gQMYYb.png)

```java
public void writeDemoProgram() {
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
```

## CONTACT
**Email:** wassgha(AT)gmail.com  
