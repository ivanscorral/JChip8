package chip8.components;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Display extends JFrame{
	
	/*
	 * The Chip-8 uses a monochrome 64x32 pixel display with this format:
	 * 
	 * +-------------------+
	 * |(0,0)        (63,0)|
	 * |                   |
	 * |(0,31)      (63,31)|
	 * +-------------------+
	 * 
	 * Chip-8 draws graphics on screen through the use of sprites. 
	 * A sprite is a group of bytes which are a binary representation of the desired picture.
	 * 
	 * 
	 */
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 32462L;
	
	private int[][] frameBuffer;
	
	private BufferedImage img;
	private JPanel contentPane;
	private JLabel imageV;
	
	public Display(Chip8 parent) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 550, 450);
		setTitle("Chip-8");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setVisible(true);
		imageV = new JLabel("");
		contentPane.add(imageV, BorderLayout.CENTER);
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {

			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				parent.emulateCycle();
				parent.registerDump();				
			}
		});
		
		frameBuffer = new int[64][32];
		img = new BufferedImage(64, 32, BufferedImage.TYPE_BYTE_BINARY);
	}
	
	public void redraw() {
		for(int y = 0; y < 32; y++) {
			for(int x = 0; x < 64; x++) {
				img.setRGB(x, y, getRGB(frameBuffer[x][y]));
			}
		}
		imageV.setIcon(new ImageIcon(img));
		System.out.println("drawing...");
	}
	
	public void setPixel(int x, int y, int value) {
		this.frameBuffer[x][y] = value;
	}
	
	public int getPixel(int x, int y) {
		return this.frameBuffer[x][y];
	}
	
	private int getRGB(int color) {
		if(color == 1) {
			return 16777215; 
		}
		else {
			return 0;
		}
	}
	
	public void clear() {
		for(int y = 0; y < 32; y++) {
			for(int x = 0; x < 64; x++) {
				img.setRGB(x, y, getRGB(0));
			}
		}
	}
	

}
