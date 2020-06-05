package modelo;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camara implements KeyListener{
    public double xPos=0, 
                  yPos=0, 
                  xDir=0, 
                  yDir=0, 
                  xPlane=0, 
                  yPlane=0;

    public boolean izquierda=false, 
                   derecha=false, 
                   delante=false, 
                   atras=false;

    public final double VELMOVIMIENTO = 0.08,
                        VELROTACION = 0.045;

    public Camara(double posX, double posY, double dirX, double dirY, double planoX, double planoY) {
        this.setPosX(posX);
        this.setPosY(posY);
        this.setDirX(dirX);
        this.setDirY(dirY);
        this.setPlanoX(planoX);
        this.setPlanoY(planoY);
    }

    void setPosX(double POSX) {
        this.xPos=POSX;
    }

    void setPosY(double POSY) {
        this.yPos=POSY;
    }

    void setDirX(double DIRX) {
        this.xDir=DIRX;
    }

    void setDirY(double DIRY) {
        this.yDir=DIRY;
    }

    void setPlanoX(double PLANOX) {
        this.xPlane=PLANOX;
    }

    void setPlanoY(double PLANOY) {
        this.yPlane=PLANOY;
    }
    
    @Override
    public void keyPressed(KeyEvent key) {
        if((key.getKeyCode() == KeyEvent.VK_W)) {
            delante = true;
        }
        if((key.getKeyCode() == KeyEvent.VK_A)) {
            izquierda = true;
        }
        if((key.getKeyCode() == KeyEvent.VK_S)) {
            atras = true;
        }
        if((key.getKeyCode() == KeyEvent.VK_D)) {
            derecha = true;
        }
    }
        
    @Override
    public void keyReleased(KeyEvent key) {
        if((key.getKeyCode() == KeyEvent.VK_W)) {
            delante = false;
        }
        if((key.getKeyCode() == KeyEvent.VK_A)) {
            izquierda = false;
        }
        if((key.getKeyCode() == KeyEvent.VK_S)) {
            atras = false;
        }
        if((key.getKeyCode() == KeyEvent.VK_D)) {
            derecha = false;
        }
    }
    
    public void update(int[][] map) {
        if(delante) {
                if(map[(int)(xPos + xDir * VELMOVIMIENTO)][(int)yPos] == 0) {
                        xPos+=xDir*VELMOVIMIENTO;
                }
                if(map[(int)xPos][(int)(yPos + yDir * VELMOVIMIENTO)] ==0)
                        yPos+=yDir*VELMOVIMIENTO;
        }
        if(atras) {
                if(map[(int)(xPos - xDir * VELMOVIMIENTO)][(int)yPos] == 0)
                        xPos-=xDir*VELMOVIMIENTO;
                if(map[(int)xPos][(int)(yPos - yDir * VELMOVIMIENTO)]==0)
                        yPos-=yDir*VELMOVIMIENTO;
        }
        if(derecha) {
                double oldxDir=xDir;
                xDir=xDir*Math.cos(-VELROTACION) - yDir*Math.sin(-VELROTACION);
                yDir=oldxDir*Math.sin(-VELROTACION) + yDir*Math.cos(-VELROTACION);
                double oldxPlane = xPlane;
                xPlane=xPlane*Math.cos(-VELROTACION) - yPlane*Math.sin(-VELROTACION);
                yPlane=oldxPlane*Math.sin(-VELROTACION) + yPlane*Math.cos(-VELROTACION);
        }
        if(izquierda) {
                double oldxDir=xDir;
                xDir=xDir*Math.cos(VELROTACION) - yDir*Math.sin(VELROTACION);
                yDir=oldxDir*Math.sin(VELROTACION) + yDir*Math.cos(VELROTACION);
                double oldxPlane = xPlane;
                xPlane=xPlane*Math.cos(VELROTACION) - yPlane*Math.sin(VELROTACION);
                yPlane=oldxPlane*Math.sin(VELROTACION) + yPlane*Math.cos(VELROTACION);
        }
    }
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }
}