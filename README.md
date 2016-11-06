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
The built-in demo program consists of the implementation of for loop that increments a counter and writes it to a memory location (0x7C) until the counter is equal to 5. The  program demonstrates most implemented Y86 instructions such as `mrmovq`, `rrmovq`, `irmovq`, `addq`, `jmp` and `jge` (conditional jump). The demo program also makes use of the `call`, return (`ret`) and `halt` instructions to demonstrate the memory stack implementation. The program is hard-coded in Simulation.java and is written to memory through byte arrays.

<p align="center">
  <img src ="http://i.imgur.com/BM2B9ey.png" />
</p>

```java
public void writeDemoProgram() {
  // pos 0
  // irmovq 0xCC, %rbp
  machine.mainMem.write(0, new byte[]{0x30, 0x01, 0x00, (byte) 0xCC});
  // rrmovq %rbp, %rsp
  machine.mainMem.write(4, new byte[]{0x20, 0x10, 0x00, (byte) 0x00});
  // call 0x14
  machine.mainMem.write(8, new byte[]{(byte)0x80, 0x00, 0x00, (byte) 0x14});
  // halt
  machine.mainMem.write(16, new byte[]{0x00, 0x00, 0x00, 0x00});
  // irmovq 0x01, %r2
  machine.mainMem.write(20, new byte[]{0x30, 0x02, (byte) 0x00, (byte) 0x01});
  // rrmovq %r2, %r3
  machine.mainMem.write(24, new byte[]{0x20, 0x23, 0x00, 0x00});
  // addq %r2, %r3
  machine.mainMem.write(28, new byte[]{0x60, 0x23, 0x00, 0x00});
  // irmovq 0x7C, %r4
  machine.mainMem.write(32, new byte[]{0x30, 0x04, 0x00, (byte) 0x7C});
  // rmmovq %r3, (%r4)
  machine.mainMem.write(36, new byte[]{0x40, 0x34, 0x00, 0x00});
  // irmovq 0xA, %r5
  machine.mainMem.write(40, new byte[]{0x30, 0x05, 0x00, (byte) 0x05});
  // subq %r3, %r5
  machine.mainMem.write(44, new byte[]{0x61, 0x35, 0x00, 0x00});
  // jge 0x38
  machine.mainMem.write(48, new byte[]{0x75, 0x00, 0x00, 0x38});
  // jmp 0x08
  machine.mainMem.write(52, new byte[]{0x70, 0x00, 0x00, 0x1C});
  // ret
  machine.mainMem.write(56, new byte[]{(byte)0x90, 0x00, 0x00, 0x00});
}
```

## CONTACT
**Email:** wassgha(AT)gmail.com  
