package modelo;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Camara implements KeyListener{
	public double xPos, 
                      yPos, 
                      xDir, 
                      yDir, 
                      xPlane, 
                      yPlane;
        
	public boolean left, 
                       right, 
                       forward, 
                       back;
        
	public final double MOVE_SPEED = 0.08;
	public final double ROTATION_SPEED = 0.045;
        
	public Camara(double x, double y, double xd, double yd, double xp, double yp) {
		this.setPosX(x);
		this.setPosY(y);
		this.setDirX(xd);
		this.setDirY(yd);
		this.setPlanoX(xp);
		this.setPlanoY(yp);
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
		if((key.getKeyCode() == KeyEvent.VK_LEFT))
			left = true;
		if((key.getKeyCode() == KeyEvent.VK_RIGHT))
			right = true;
		if((key.getKeyCode() == KeyEvent.VK_UP))
			forward = true;
		if((key.getKeyCode() == KeyEvent.VK_DOWN))
			back = true;
	}
        @Override
	public void keyReleased(KeyEvent key) {
		if((key.getKeyCode() == KeyEvent.VK_LEFT))
			left = false;
		if((key.getKeyCode() == KeyEvent.VK_RIGHT))
			right = false;
		if((key.getKeyCode() == KeyEvent.VK_UP))
			forward = false;
		if((key.getKeyCode() == KeyEvent.VK_DOWN))
			back = false;
	}
	public void update(int[][] map) {
		if(forward) {
			if(map[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] == 0) {
				xPos+=xDir*MOVE_SPEED;
			}
			if(map[(int)xPos][(int)(yPos + yDir * MOVE_SPEED)] ==0)
				yPos+=yDir*MOVE_SPEED;
		}
		if(back) {
			if(map[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] == 0)
				xPos-=xDir*MOVE_SPEED;
			if(map[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)]==0)
				yPos-=yDir*MOVE_SPEED;
		}
		if(right) {
			double oldxDir=xDir;
			xDir=xDir*Math.cos(-ROTATION_SPEED) - yDir*Math.sin(-ROTATION_SPEED);
			yDir=oldxDir*Math.sin(-ROTATION_SPEED) + yDir*Math.cos(-ROTATION_SPEED);
			double oldxPlane = xPlane;
			xPlane=xPlane*Math.cos(-ROTATION_SPEED) - yPlane*Math.sin(-ROTATION_SPEED);
			yPlane=oldxPlane*Math.sin(-ROTATION_SPEED) + yPlane*Math.cos(-ROTATION_SPEED);
		}
		if(left) {
			double oldxDir=xDir;
			xDir=xDir*Math.cos(ROTATION_SPEED) - yDir*Math.sin(ROTATION_SPEED);
			yDir=oldxDir*Math.sin(ROTATION_SPEED) + yDir*Math.cos(ROTATION_SPEED);
			double oldxPlane = xPlane;
			xPlane=xPlane*Math.cos(ROTATION_SPEED) - yPlane*Math.sin(ROTATION_SPEED);
			yPlane=oldxPlane*Math.sin(ROTATION_SPEED) + yPlane*Math.cos(ROTATION_SPEED);
		}
	}
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}