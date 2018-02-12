package chip8.components;

public class Chip8 {
	
	/*
	 * Registers:
	 * 
	 * 1.- 16 general purpose 8-bit registers (V0-VF).
	 * 2.- 16-bit register (I) -> used to store memory addreses, only the lowest 12 bits are usually used.
	 * 3.- VF register should not be used by programs, it is used as Flag Register by some instructions.
	 * 4.- 2 special purpose 8-bit registers (delay and sound timers, DT and ST). When != 0 decremented at 60Hz.
	 * 5.- Program counter (PC) should be 16-bit and the Stack Pointer can be 8-bit.
	 * 6.- The stack is an array of 16 16-bit values.
	 * 
	 */
		
	private char opcode;
	
	private Memory memory = new Memory(Memory.MEMORY_4K);
	
	//Registers
	
	private byte V[] = new byte[16];
	private byte stack[] = new byte[16];
	private byte SP;
	private char I;
	private char PC;
	
	private byte DT;
	private byte ST;
	
	
	public Chip8() {
		//TODO
	}
	
	public void initialize(){
		//Initialize registers and memory
	}
	
	public void emulateCycle() {
		//Fetch, decode, execute opcode
		
		//Update timers
	}
	
	
	
}
