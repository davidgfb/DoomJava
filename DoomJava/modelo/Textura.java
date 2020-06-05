package modelo;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Textura {
    public int[] pixels;
    private String loc;
    public final int SIZE;

    public static Textura wood = new Textura("/texturas/madera.png", 64),
                          brick = new Textura("/texturas/ladrilloRojo.png", 64),
                          bluestone = new Textura("/texturas/piedraAzul.png", 64),
                          stone = new Textura("/texturas/piedraGris.png", 64);

    public Textura(String location, int size) {
        //this.setRuta(loc);
        loc=location;
        SIZE=size;
        //this.setTamaño(size);
        pixels=new int[SIZE * SIZE];
        //this.setPixeles(new int[SIZE * SIZE]);
        load();
    }
    
    /*
    void setRuta(String RUTA) {
        this.loc=RUTA;
    }
    
    
    void setTamaño(int TAMAÑO) {
        //this.SIZE=TAMAÑO;
    }
    
    
    void setPixeles(int[] PIXELES) {
        this.pixels=PIXELES;
    }
    */
    
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
}
