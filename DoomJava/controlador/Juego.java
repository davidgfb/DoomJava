package controlador;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import javax.swing.JFrame;
import modelo.Camara;
import vista.Pantalla;
import modelo.Textura;

public class Juego extends JFrame implements Runnable{
	
    public int anchoMapa = 15,
               altoMapa = 15,
               
               anchoVen=1920,
               altoVen=1080;
    
    private final Thread hilo=new Thread(this);
    private boolean abierto=true;
    private final BufferedImage imagen= new BufferedImage(anchoVen, altoVen, BufferedImage.TYPE_INT_RGB);
    public int[] pixeles = ((DataBufferInt)imagen.getRaster().getDataBuffer()).getData();
    public ArrayList<Textura> texturas = new ArrayList<>();
    double posX=4.5, 
           posY=4.5, 
           dirX=1, 
           dirY=0, 
           planoX=0, 
           planoY=-0.66;
    public Camara camara= new Camara(posX, posY, dirX, dirY, planoX, planoY); //crea clase Camara
    public Pantalla screen = new Pantalla(mapa, anchoMapa, altoMapa, texturas, anchoVen, altoVen); //crea clase Pantalla
    public static int[][] mapa = {{1,1,1,1,1,1,1,1,2,2,2,2,2,2,2},
                                 {1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
                                 {1,0,3,3,3,3,3,0,0,0,0,0,0,0,2},
                                 {1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
                                 {1,0,3,0,0,0,3,0,2,2,2,0,2,2,2},
                                 {1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
                                 {1,0,3,3,0,3,3,0,2,0,0,0,0,0,2},
                                 {1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
                                 {1,1,1,1,1,1,1,1,4,4,4,0,4,4,4},
                                 {1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
                                 {1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
                                 {1,0,0,0,0,0,1,4,0,3,3,3,3,0,4},
                                 {1,0,0,0,0,0,1,4,0,3,3,3,3,0,4},
                                 {1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                                 {1,1,1,1,1,1,1,4,4,4,4,4,4,4,4}};
    
    //***************************** INICIO TEXTURAS **************************************** //instanciacion!
        public static Textura madera = new Textura("/texturas/madera.png", 64),
                              ladrillo = new Textura("/texturas/ladrilloRojo.png", 64),
                              piedraAzul = new Textura("/texturas/piedraAzul.png", 64),
                              piedra = new Textura("/texturas/piedraGris.png", 64);
    //****************************  FIN TEXTURAS    ****************************************
    
    public Juego() {
        //************************  AÑADE TEXTURAS  *********************** //al arraylist
        this.texturas.add(madera);
        this.texturas.add(ladrillo);
        this.texturas.add(piedraAzul);
        this.texturas.add(piedra);
        //************************  FIN AÑADE TEXTURAS  *******************

        this.addKeyListener(camara);
        this.setSize(anchoVen, altoVen);
        this.setResizable(false);
        this.setTitle("Doom Java");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.black);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        this.abierto = true;
        this.hilo.start();
    }
    
    public synchronized void para() {
        this.setAbierto(false);
        
        try {
            this.hilo.join();
        } 
        catch(InterruptedException error) {
            error.printStackTrace();
        }
    }
    
    void setAbierto(boolean ABIERTO) {
        this.abierto=ABIERTO;
    }
    
    public void renderiza() {
        BufferStrategy estraBuffer = this.getBufferStrategy();
        
        if(estraBuffer == null) {
            this.createBufferStrategy(3);
            estraBuffer = this.getBufferStrategy();
        }
        
        Graphics graficos = estraBuffer.getDrawGraphics(); //estos van en esta posicion posible excepcion puntero nulo
        int x=0,
            y=0;
        ImageObserver observaImagen=null;
        
        graficos.drawImage(imagen, x, y, imagen.getWidth(), imagen.getHeight(), observaImagen); 
        
        estraBuffer.show();
    }
    
    
    
    @Override
    public void run() {
        long ultimoTiempo = System.nanoTime();
        final double ns = 1000000000.0 / 60.0; //60 veces por segundo fps
        double delta = 0;
        
        this.requestFocus();
        
        while(abierto) {
            long ahora = System.nanoTime();
            delta += (ahora-ultimoTiempo) / ns;
            ultimoTiempo = ahora;
            
            while (delta >= 1) //Make sure actualiza is only happening 60 times a second
            {
                this.screen.actualiza(camara, pixeles); //handles all of the logic restricted time
                this.camara.actualiza(mapa);
                delta--;
            }
            this.renderiza(); //displays to the screen unrestricted time
        }
    }
    
    public static void main(String [] args) {
        Juego juego = new Juego();
    }
}
