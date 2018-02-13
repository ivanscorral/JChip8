package chip8.components;

import java.io.File;
import java.util.Arrays;
import java.util.Stack;

import chip8.utils.NumberUtils;

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
	private File program = new File("resources/particles.ch8");
	//Registers
	
	private int V[] = new int[16];
	//Correct way to do this is to assign stack to memory region 0xEA0-0xEFF 
	private Stack<Integer> stack = new Stack<Integer>();
	private int sp;
	private int I;
	private int pc;
	
	private int dt;
	private int st;
	
	
	public Chip8() {		
		this.initialize();
	}
	
	public void initialize(){
		//Initialize registers and memory
		pc = 0x200;
		opcode = 0;
		I = 0;
		sp = 0;
		
		memory.loadProgram(program);
		memory.dump();
	}
	
	public void emulateCycle() {
		//Fetch		
		opcode = NumberUtils.parseOpCode(memory.get(pc), memory.get(pc+1));
		
		//Decode and execute

		System.out.println("Executing: " + Integer.toHexString(opcode));
		executeOpcode(opcode);
						
		//Update timers TODO: MUST BE SLOWED DOWN TO 60Hz
		
		if(dt > 0) --dt;
		if(st > 0) {
			if(st == 1) {
				System.out.println("BEEP");
				--st;
			}
		}
	}
	
	public void registerDump() {
		System.out.println("PC: 0x" + Integer.toHexString(pc));
		System.out.println("I: 0x" + Integer.toHexString(I));
		System.out.println("SP: 0x" + Integer.toHexString(sp));
		for(int i = 0; i < V.length; i++) {
			System.out.println("V"+ Integer.toHexString(i).toUpperCase() + ": " + Integer.toHexString(V[i]));
		}
		System.out.println(Arrays.toString(stack.toArray()));
	}
	
	private void executeOpcode(int opcode) {
		int decodedOp = opcode & 0xF000;
		int x = (opcode & 0x0F00) >> 8;
		int y = (opcode & 0x00F0) >> 4;
		int n = opcode & 0x000F;
		int nn = opcode & 0x00FF;
		int nnn = opcode & 0x0FFF;

		switch (decodedOp) {
		case 0x0000:
			switch(opcode & 0xFF) {
			case 0xE0:
				//TODO Implement 00E0
				break;
			case 0xEE:
				if(stack.isEmpty()) {
					//TODO
				}else {
					--sp;
					pc = stack.get(sp);	
					stack.remove(sp);
					pc = pc + 2;
				}
				break;
			}
			break;
		case 0x1000:
			pc = nnn;
			break;
		case 0x2000:
			stack.add(sp, pc);
			++sp;
			pc = nnn;
			break;
		case 0x3000:
			if(V[x] == nn) {
				//Skip
				pc = pc + 4;
			}
			break;
		case 0x4000:
			if(V[x] != nn) {
				//Skip
				pc = pc + 4;
			}
			break;
		case 0x5000:
			if(V[x] == y) {
				//Skip
				pc = pc + 4;
			}
			break;
		case 0x6000:
			V[x] = nn;
			pc = pc + 2;
			break;
		case 0x7000:
			V[x] += nn;
			pc = pc + 2;
			break;
		case 0x8000:
		switch (n) {			
			//8XY0, 8XY1...
			case 0x0000:
				V[x] = V[y];
				pc = pc + 2;
				break;
			case 0x0001:
				V[x] |= V[y];
				pc = pc + 2;
				break;
			case 0x0002:
				V[x] &= V[y];
				pc = pc + 2;
				break;
			case 0x0003:
				V[x] ^= V[y];
				pc = pc + 2;
				break;
			case 0x0004:
				if((V[x] + V[y]) < 255) {
					V[x] += V[y];
					V[0xF] = 0;
				}else {
					V[x] = (V[x] + V[y]) & 0xFF;
					V[0xF] = 1;
				}
				pc = pc + 2;
				break;
			case 0x0005:				
				if(V[x] > V[y]){
					V[0xF] = 1;
				}else {
					V[0xF] = 0;
				}
				V[x] -= V[y];
				pc = pc + 2;
				break;
			case 0x0006:
				V[0xF] = (V[y] & 1);
				V[x] = (V[y] >> 1);
				pc = pc + 2;
				break;
			case 0x0007:
				if(V[y] > V[x]) {
					V[0xF] = 0;
				}else {
					V[0xF] = 1;
				}
				V[x] = V[y] - V[x];
				pc = pc + 2;
				break;
			case 0x000E:
				V[0xF] = V[y] >> 7;
				V[x] = (V[y] << 1);
				pc = pc + 2;
				break;
			}
			break;
		case 0x9000:
			if(V[x] != V[y]) {
				//Skip
				pc = pc + 4;
			}
			break;
		case 0xA000:
			I = nnn;
			pc = pc + 2;
			break;
		case 0xB000:
			pc = (nnn + V[0]) & 0xFFF;
			break;
		case 0xC000:
			int rand = NumberUtils.getRandomInRange(0, 255);
			V[x] = rand & nn; 
			pc = pc + 2;
			break;
		case 0xD000:
			/*
			 * TODO Implement DXYN
			 * 
			 * draw(VX, VY , n): Draw a sprite at coordinate VX, VY with a width of 8 pixels and height
			 * of n pixels. Each row of 8 pixels is read as bit coded from memory location I. VF is set to 1
			 * if any pixel is set from set to unset when drawn and 0 if it doesn't.
			 */
			
			System.out.println("draw(" + Integer.toHexString(V[x]) + ", " + Integer.toHexString(V[y]) + ", " + Integer.toHexString(n) + ")");
			pc = pc + 2;
			break;
		case 0xE000:
			switch (nn) {
			case 0x9E:
				//TODO Implement EX9E
				break;
			case 0xA1:
				//TODO Implement EXA1
				break;
			}
			break;
		case 0xF000:
			switch (nn) {
			case 0x0007:
				V[x] = dt;
				pc = pc + 2;
				break;
			case 0x000A:
				/*
				 * TODO Implement FX0A
				 * 
				 * Wait for key press and save key to V[x]
				 */
				break;
			case 0x0015:
				dt = V[x];
				pc = pc + 2;
				break;
			case 0x0018:
				st = V[x];
				pc = pc + 2;
				break;
			case 0x001E:
				I +=  V[x];
				pc = pc + 2;
				break;
			case 0x0029:
				I = V[x] * 0x5;
				pc = pc + 2;
				break;
			case 0x0033:
				memory.set(I, V[x] / 100);
				memory.set(I + 1, (V[x] / 10) % 10);
				memory.set(I + 1, (V[x] % 100) % 10);
				pc = pc + 2;
				break;
			case 0x0055:
				for(int i = 0; i < x; i++) {
					memory.set(I + i, V[i]);
				}
				pc = pc + 2;
				break;
			case 0x0065:
				for(int i = 0; i < x; i++) {
					V[i] = memory.get(I + i);
				}
				pc = pc + 2;
				break;
			}
			break;
			default:
				System.out.println("ERROR! Opcode not supported!");
		}
 	}
	
	
	
}
