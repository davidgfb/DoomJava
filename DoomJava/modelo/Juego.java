package modelo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import javax.swing.JFrame;

public class Juego extends JFrame implements Runnable{
	
    private static final long serialVersionUID = 1L;
    public int mapWidth = 15,
               mapHeight = 15,
            
               ancho=640,
               alto=480;
    
    private Thread hilo=new Thread(this);
    private boolean abierto;
    private BufferedImage imagen= new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);;
    public int[] pixeles = ((DataBufferInt)imagen.getRaster().getDataBuffer()).getData();
    public ArrayList<Textura> texturas = new ArrayList<Textura>();
    public Camara camera= new Camara(4.5, 4.5, 1, 0, 0, -0.66);
    public Pantalla screen = new Pantalla(map, mapWidth, mapHeight, texturas, ancho, alto);;
    public static int[][] map = {{1,1,1,1,1,1,1,1,2,2,2,2,2,2,2},
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
        this.setSize(ancho, alto);
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
        } catch(InterruptedException error) {
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
        
        Graphics graficos = estraBuffer.getDrawGraphics();
        graficos.drawImage(imagen, 0, 0, imagen.getWidth(), imagen.getHeight(), null);
        estraBuffer.show();
    }
    
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0; //60 times per second
        double delta = 0;
        
        this.requestFocus();
        
        while(abierto) {
            long now = System.nanoTime();
            delta += (now-lastTime) / ns;
            lastTime = now;
            
            while (delta >= 1) //Make sure update is only happening 60 times a second
            {
                //handles all of the logic restricted time
                this.screen.update(camera, pixeles);
                this.camera.update(map);
                delta--;
            }
            this.renderiza();//displays to the screen unrestricted time
        }
    }
    
    public static void main(String [] args) {
        Juego juego = new Juego();
    }
}
