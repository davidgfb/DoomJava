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
        this.loc=location;
        this.SIZE=size;
        //this.setTamaño(size);
        this.pixels=new int[SIZE * SIZE];
        //this.setPixeles(new int[SIZE * SIZE]);
        this.carga();
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
    
    private void carga() {
        try {
            BufferedImage imagen = ImageIO.read(getClass().getResource(loc));
            int ancho = imagen.getWidth(),
                alto = imagen.getHeight();
            imagen.getRGB(0, 0, ancho, alto, pixels, 0, ancho);
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
