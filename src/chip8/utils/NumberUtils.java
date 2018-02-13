package chip8.utils;

import java.util.Random;

public class NumberUtils {
	
	public static int parseOpCode(int part1, int part2) {
		String s1 = Integer.toHexString(part1);
		String s2 = Integer.toHexString(part2);

		if(s1.length() >= 2) {
			s1 = s1.substring(s1.length()-2, s1.length());
		}else {
			s1 = "0"+ s1;
		}
		if(s2.length() >= 2) {
			s2 = s2.substring(s2.length()-2, s2.length());
		}else {
			s2 = "0" + s2;
		}
		
		String result = "0x" + s1 + s2;
		
		return FileIO.parseFullHex(result);
		}
	
	
	public static int getRandomInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("Max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

}
