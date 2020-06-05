package modelo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Textura {
	public int[] pixels;
	private String loc;
	public final int SIZE;
	
	public Textura(String location, int size) {
		loc = location;
		SIZE = size;
		pixels = new int[SIZE * SIZE];
		load();
	}
	
	private void load() {
		try {
			BufferedImage image = ImageIO.read(getClass().getResource(loc));
			int w = image.getWidth();
			int h = image.getHeight();
			image.getRGB(0, 0, w, h, pixels, 0, w);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Textura wood = new Textura("/texturas/madera.png", 64);
	public static Textura brick = new Textura("/texturas/ladrilloRojo.png", 64);
	public static Textura bluestone = new Textura("/texturas/piedraAzul.png", 64);
	public static Textura stone = new Textura("/texturas/piedraGris.png", 64);
}
