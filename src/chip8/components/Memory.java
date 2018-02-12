package chip8.components;

public class Memory {
	
	/*
	 * Memory map:
	 * 
	 * 0x000 (0) Start of Chip-8 RAM
	 * 
	 * 0x000 to 0x1FF Reserved for interpreter
	 * 
	 * 0x200 (512) Start of most Chip-8 programs
	 * 
	 * 0x600 (1536) Start of ETI 660 Chip-8 programs
	 * 
	 * 0xFFF (4095) End of Chip-8 RAM
	 * 
	 */
	
	public static int MEMORY_4K = 4096;
	
	private int[] memory;	
	
	public Memory(int size) {
		this.memory = new int[size];		
	}
	

}
