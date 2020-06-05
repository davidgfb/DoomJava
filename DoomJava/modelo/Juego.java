package modelo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import javax.swing.JFrame;

public class Juego extends JFrame implements Runnable{
	
    public int anchoMapa = 15,
               altoMapa = 15,
            
               anchoVen=640,
               altoVen=480;
    
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
    public Camara camera= new Camara(posX, posY, dirX, dirY, planoX, planoY);
    public Pantalla screen = new Pantalla(mapa, anchoMapa, altoMapa, texturas, anchoVen, altoVen);
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
    
    public Juego() {
            
        //************************  AÑADE TEXTURAS  ***********************
        this.texturas.add(Textura.wood);
        this.texturas.add(Textura.brick);
        this.texturas.add(Textura.bluestone);
        this.texturas.add(Textura.stone);
        //************************  FIN AÑADE TEXTURAS  *******************

        this.addKeyListener(camera);
        this.setSize(anchoVen, altoVen);
        this.setResizable(false);
        this.setTitle("3D Engine");
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
        final double ns = 1000000000.0 / 60.0; //60 times per second
        double delta = 0;
        
        this.requestFocus();
        
        while(abierto) {
            long ahora = System.nanoTime();
            delta += (ahora-ultimoTiempo) / ns;
            ultimoTiempo = ahora;
            
            while (delta >= 1) //Make sure update is only happening 60 times a second
            {
                this.screen.update(camera, pixeles); //handles all of the logic restricted time
                this.camera.update(mapa);
                delta--;
            }
            this.renderiza();//displays to the screen unrestricted time
        }
    }
    
    public static void main(String [] args) {
        Juego juego = new Juego();
    }
}
