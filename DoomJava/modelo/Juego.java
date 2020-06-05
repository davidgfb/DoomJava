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
               mapHeight = 15;
    private Thread thread=new Thread(this);
    private boolean running;
    private BufferedImage image= new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);;
    public int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();;
    public ArrayList<Textura> textures = new ArrayList<Textura>();
    public Camara camera= new Camara(4.5, 4.5, 1, 0, 0, -0.66);
    public Pantalla screen = new Pantalla(map, mapWidth, mapHeight, textures, 640, 480);;
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
        this.textures.add(Textura.wood);
        this.textures.add(Textura.brick);
        this.textures.add(Textura.bluestone);
        this.textures.add(Textura.stone);
        //************************  FIN AÑADE TEXTURAS  *******************

        
        this.addKeyListener(camera);
        this.setSize(640, 480);
        this.setResizable(false);
        this.setTitle("3D Engine");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.black);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        this.running = true;
        this.thread.start();
    }
    private synchronized void start() {
            
    }
    public synchronized void stop() {
            running = false;
            try {
                    thread.join();
            } catch(InterruptedException e) {
                    e.printStackTrace();
            }
    }
    public void render() {
            BufferStrategy bs = getBufferStrategy();
            if(bs == null) {
                    createBufferStrategy(3);
                    return;
            }
            Graphics g = bs.getDrawGraphics();
            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
            bs.show();
    }
    public void run() {
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0; //60 times per second
        double delta = 0;
        requestFocus();
        while(running) {
            long now = System.nanoTime();
            delta = delta + ((now-lastTime) / ns);
            lastTime = now;
            while (delta >= 1)//Make sure update is only happening 60 times a second
            {
                //handles all of the logic restricted time
                screen.update(camera, pixels);
                camera.update(map);
                delta--;
            }
            render();//displays to the screen unrestricted time
        }
    }
    public static void main(String [] args) {
            Juego game = new Juego();
    }
}
