package chip8;

import chip8.components.Chip8;

public class Test {

	public static void main(String[] args) {
		
		Chip8 a = new Chip8();
		a.registerDump();
		a.emulateCycle();
		a.registerDump();
	}

}
