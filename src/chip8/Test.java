package chip8;

import chip8.components.Chip8;

public class Test {

	public static void main(String[] args) {
		
		Chip8 a = new Chip8();
		for(int i = 0; i < 100000; i++) {
			a.emulateCycle();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//TODO All roms get stuck in an infinite loop, might be related to non-implemented input, probably not.
		


	}

}
