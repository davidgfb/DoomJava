package modelo;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camara implements KeyListener{
    public double posX=0, 
                  posY=0, 
                  dirX=0, 
                  dirY=0, 
                  planoX=0, 
                  planoY=0;

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
        this.posX=POSX;
    }

    void setPosY(double POSY) {
        this.posY=POSY;
    }

    void setDirX(double DIRX) {
        this.dirX=DIRX;
    }

    void setDirY(double DIRY) {
        this.dirY=DIRY;
    }

    void setPlanoX(double PLANOX) {
        this.planoX=PLANOX;
    }

    void setPlanoY(double PLANOY) {
        this.planoY=PLANOY;
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
    
    public void actualiza(int[][] mapa) {
        System.out.println("\n******************"+
                           "\n*posX:"+posX+
                           "\n*posY: "+posY+
                           "\n*dirX: "+dirX+
                           "\n*dirY: "+dirY+
                           "\n*planoX: "+planoX+
                           "\n*planoY: "+planoY+
                           "\n******************");
        
        if(delante) { //suma
            if(mapa[(int)(posX + dirX * VELMOVIMIENTO)][(int)posY] == 0) {
                    posX+=dirX*VELMOVIMIENTO;
            }
            if(mapa[(int)posX][(int)(posY + dirY * VELMOVIMIENTO)] ==0)
                    posY+=dirY*VELMOVIMIENTO;
        }
        if(atras) { //resta
            if(mapa[(int)(posX - dirX * VELMOVIMIENTO)][(int)posY] == 0)
                    posX-=dirX*VELMOVIMIENTO;
            if(mapa[(int)posX][(int)(posY - dirY * VELMOVIMIENTO)]==0)
                    posY-=dirY*VELMOVIMIENTO;
        }
        if(derecha) {
            double antDirX=dirX,
                   antPlanoX = planoX;
            
            dirX=dirX*Math.cos(-VELROTACION) - dirY*Math.sin(-VELROTACION); //no simplifiques
            dirY=antDirX*Math.sin(-VELROTACION) + dirY*Math.cos(-VELROTACION);
            
            planoX=planoX*Math.cos(-VELROTACION) - planoY*Math.sin(-VELROTACION);
            planoY=antPlanoX*Math.sin(-VELROTACION) + planoY*Math.cos(-VELROTACION);
        }
        if(izquierda) {
            double antDirX=dirX,
                   antPlanoX = planoX;
            
            dirX=dirX*Math.cos(VELROTACION) - dirY*Math.sin(VELROTACION);
            dirY=antDirX*Math.sin(VELROTACION) + dirY*Math.cos(VELROTACION);
            
            planoX=planoX*Math.cos(VELROTACION) - planoY*Math.sin(VELROTACION);
            planoY=antPlanoX*Math.sin(VELROTACION) + planoY*Math.cos(VELROTACION);
        }
    }
    
    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub  metodo abstracto obligatorio

    }
}