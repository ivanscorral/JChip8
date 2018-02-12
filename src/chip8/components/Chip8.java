package chip8.components;

import java.io.File;

public class Chip8 {
	
	/*
	 * Registers:
	 * 
	 * 1.- 16 general purpose 8-bit registers (V0-VF).
	 * 2.- 16-bit register (I) -> used to store memory addresses, only the lowest 12 bits are usually used.
	 * 3.- VF register should not be used by programs, it is used as Flag Register by some instructions.
	 * 4.- 2 special purpose 8-bit registers (delay and sound timers, DT and ST). When != 0 decremented at 60Hz.
	 * 5.- Program counter (PC) should be 16-bit and the Stack Pointer can be 8-bit.
	 * 6.- The stack is an array of 16 16-bit values.
	 * 
	 */
		
	private int opcode;
	
	private Memory memory = new Memory(Memory.MEMORY_4K);
	private File program = new File("resources/TETRIS.c8");
	
	//Registers
	
	private int V[] = new int[16];
	private int stack[] = new int[16];
	private short sp;
	private int I;
	private int pc;
	
	private short dt;
	private short st;
	
	
	public Chip8() {		
		this.initialize();
	}
	
	public void initialize(){
		//Initialize registers and memory
		pc = 0x200;
		opcode = 0;
		I = 0;
		sp = 0;
		
		
		memory.set(pc, 0x80);
		memory.set(pc+1, 0x16);
		
		V[0] = 0;
		V[1] = 6;



		//memory.loadProgram(program);
	}
	
	public void emulateCycle() {
		//Fetch, decode, execute opcode
				
		//Fetch opcode from pc and pc+1
		
		opcode = memory.get(pc) << 8 | memory.get(pc+1);
		System.out.println(Integer.toHexString(opcode));
		
		executeOpcode(opcode);
						
		//Update timers
	}
	
	public void registerDump() {
		System.out.println("PC: 0x" + Integer.toHexString(pc));
		System.out.println("I: 0x" + Integer.toHexString(I));
		System.out.println("SP: 0x" + Integer.toHexString(sp));
		for(int i = 0; i < V.length; i++) {
			System.out.println("V"+ Integer.toHexString(i).toUpperCase() + ": " + Integer.toBinaryString(V[i]));
		}
	}
	
	private void executeOpcode(int opcode) {
		int decodedOp = opcode & 0xF000;
		int x, y, n, nn, nnn; 
		switch (decodedOp) {
		case 0x0000:
			switch(opcode & 0xFF) {
			case 0xE0:
				//TODO Implement 00E0
				break;
			case 0xEE:
				//TODO Implement 00EE
				break;
			}
			break;
		case 0x1000:
			nnn = opcode & 0x0FFF;
			pc = nnn;
			break;
		case 0x2000:
			//TODO Implement 2NNN
			break;
		case 0x3000:
			x = (opcode & 0x0F00) >> 8;
			nn = opcode & 0x00FF;
			if(V[x] == nn) {
				//Skip
				pc = pc + 2;
			}
			break;
		case 0x4000:
			x = (opcode & 0x0F00) >> 8;
			nn = opcode & 0x00FF;
			if(V[x] != nn) {
				//Skip
				pc = pc + 2;
			}
			break;
		case 0x5000:
			x = (opcode & 0x0F00) >> 8;
			y = (opcode & 0x00F0) >> 4;
			if(V[x] == y) {
				//Skip
				pc = pc + 2;
			}
			break;
		case 0x6000:
			x = (opcode & 0x0F00) >> 8;
			nn = opcode & 0x00FF;
			V[x] = nn;
			break;
		case 0x7000:
			x = (opcode & 0x0F00) >> 8;
			nn = opcode & 0x00FF;
			V[x] += nn;
			break;
		case 0x8000:
			x = (opcode & 0x0F00) >> 8;
			y = (opcode & 0x00F0) >> 4;
						
			switch (opcode & 0x000F) {			
			//8XY0, 8XY1...
			case 0x0000:
				V[x] = V[y];
				break;
			case 0x0001:
				V[x] |= V[y];
				break;
			case 0x0002:
				V[x] &= V[y];
				break;
			case 0x0003:
				V[x] ^= V[y];
				break;
			case 0x0004:
				if((V[x] + V[y]) < 255) {
					V[x] += V[y];
					V[0xF] = 0;
				}else {
					V[x] = (V[x] + V[y]) & 0xFF;
					V[0xF] = 1;
				}
				break;
			case 0x0005:				
				if(V[x] > V[y]){
					V[0xF] = 1;
				}else {
					V[0xF] = 0;
				}
				V[x] -= V[y];
				break;
			case 0x0006:
				V[0xF] = (V[y] & 1);
				V[x] = (V[y] >> 1);
				break;
			case 0x0007:
				if(V[y] > V[x]) {
					V[0xF] = 0;
				}else {
					V[0xF] = 1;
				}
				V[x] = V[y] - V[x];
				break;
			case 0x000E:
				V[0xF] = V[y] >> 7;
				V[x] = (V[y] << 1);
				break;
			}
			break;
		case 0x9000:
			x = (opcode & 0x0F00) >> 8;
			y = (opcode & 0x00F0) >> 4;
			
			if(V[x] != V[y]) {
				//Skip
				pc = pc + 2;
			}
			break;
		case 0xA000:
			nnn = opcode & 0x0FFF;
			I = nnn;
			break;
		case 0xB000:
			//TODO Implement BNNN
			break;
		case 0xC000:
			//TODO Implement CXNN
			break;
		case 0xD000:
			//TODO Implement CXNN
			break;
		case 0xE000:
			//TODO Implement CXNN
			break;
		case 0xF000:
			//TODO Implement CXNN
			break;
		}
		//increment program counter by 2
		pc = pc + 2;
	}
	
	
	
}
