package modelo;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Textura {
    public int[] pixeles;
    private String ruta;
    public int tamañoTextura = 64; 
    
    //***************************** INICIO TEXTURAS **************************************** //instanciacion!
    public static Textura madera = new Textura("/texturas/madera.png", 64),
                          ladrillo = new Textura("/texturas/ladrilloRojo.png", 64),
                          piedraAzul = new Textura("/texturas/piedraAzul.png", 64),
                          piedra = new Textura("/texturas/piedraGris.png", 64);
    //****************************  FIN TEXTURAS    ****************************************

    public Textura(String ruta, int tamaño) {
        this.setRuta(ruta);
        this.setTamañoTextura(tamaño);
        int TAMAÑO=this.getTAMAÑO();
        this.setPixeles(new int[TAMAÑO * TAMAÑO]);
        this.carga();
    }
    
    
    void setRuta(String RUTA) {
        this.ruta=RUTA;
    }
    
    
    void setTamañoTextura(int TAMAÑO) {
        this.tamañoTextura=TAMAÑO;
    }
    
    
    void setPixeles(int[] PIXELES) {
        this.pixeles=PIXELES;
    }
    
    int getTAMAÑO() {
        return this.tamañoTextura;
    }
    
    
    private void carga() {
        try {
            BufferedImage imagen= ImageIO.read(getClass().getResource(ruta));
            int x0=0,
                y0=0,
                ancho = imagen.getWidth(),
                alto = imagen.getHeight(),
                desplaz=0;
            
            imagen.getRGB(x0, y0, ancho, alto, pixeles, desplaz, ancho);
        } 
        
        catch (IOException ex) {
            Logger.getLogger(Textura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
