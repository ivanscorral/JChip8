package chip8;

public class Test {

	public static void main(String[] args) {
		
		int binario = 6;
		System.out.println(Integer.toBinaryString(binario) + ": " + binario);
		binario = binario >> 1;
		System.out.println(Integer.toBinaryString(binario) + ": " + binario);
	}

}
