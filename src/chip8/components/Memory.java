package chip8.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import chip8.utils.FileIO;

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
	public static int PROGRAM_START_ADDR = 0x200;
	
	private int[] memory;
	private boolean[] hasChanged;
	
	public Memory(int size) {
		memory = new int[size];
		hasChanged = new boolean[size];
		loadFontSet();
	}
	
	public void loadProgram(File f) {
		int[] programHex = FileIO.getProgram(f);
		
		for(int i = 0; i < programHex.length; i++) {
			this.set(PROGRAM_START_ADDR + i, programHex[i]);
		}
				
	}
	
	public int get(int index) {
		return memory[index];
	}
	
	public void set(int index, int value) {
		hasChanged[index] = true;
		memory[index] = value;
	}
	
	private void loadFontSet() {
		int[] fontSet = FileIO.getFontSet();
		
		for(int i = 0; i < fontSet.length; i++) {
			//double check
			if(i < 0x200) {
				memory[i] = fontSet[i];
			}
		}
	}
	
	public void dump() {
		for(int i = 0; i < memory.length; i++) {
			if(hasChanged[i]) {
				String hexString =  Integer.toHexString(memory[i]);
				/*if(hexString.length() > 2) {
					hexString = hexString.substring(hexString.length()-2, hexString.length());
				}*/
				System.out.println(Integer.toHexString(i) + " - " + hexString);
			}
		}
	}
	

}
