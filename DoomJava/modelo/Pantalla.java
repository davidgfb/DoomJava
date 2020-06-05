package modelo;

import java.util.ArrayList;
import java.awt.Color;

public class Pantalla {
    public int[][] mapa;
    public int anchoMapa, 
               altoMapa, 
               ancho, 
               alto;

    public ArrayList<Textura> texturas;

    public Pantalla(int[][] mapa, int anchoMapa, int altoMapa, ArrayList<Textura> texturas, int ancho, int alto) {
        this.setMapa(mapa);
        this.setAnchoMapa(anchoMapa);
        this.setAltoMapa(altoMapa);
        this.setTexturas(texturas);
        this.setAncho(ancho);
        this.setAlto(alto);
    }

    void setMapa(int[][] MAPA) {
        this.mapa=MAPA;
    }

    void setAnchoMapa(int ANCHOMAPA) {
        this.anchoMapa=ANCHOMAPA;
    }

    void setAltoMapa(int ALTOMAPA) {
        this.altoMapa=ALTOMAPA;
    }

    void setTexturas(ArrayList<Textura> TEXTURAS) {
        this.texturas=TEXTURAS;
    }

    void setAncho(int ANCHO) {
        this.ancho=ANCHO;
    }

    void setAlto(int ALTO) {
        this.alto=ALTO;
    }

    public int[] actualiza(Camara camara, int[] pixeles) {
        for(int n=0; n<pixeles.length/2; n++) {
            if(pixeles[n] != Color.DARK_GRAY.getRGB()) {
                pixeles[n] = Color.DARK_GRAY.getRGB();
            }
        }
        for(int i=pixeles.length/2; i<pixeles.length; i++){
            if(pixeles[i] != Color.gray.getRGB()) {
                pixeles[i] = Color.gray.getRGB();
            }
        }

        for(int x=0; x<ancho; x+=1) {
            double posXCam = 2 * x / (double)(ancho) -1; //????????????????????
            double dirRayoX = camara.dirX + camara.planoX * posXCam;
            double dirRayoY = camara.dirY + camara.planoY * posXCam;

            //Posicion mapa
            int posXMapa = (int)camara.posX;
            int posYMapa = (int)camara.posY;
            
            //"longitud del rayo desde la posicion actual al siguiente lado x o y" 
            double disLadoX;
            double disLadoY;
            
            //"longitud del rayo desde un lado al siguiente del mapa"
            double deltaDistX = Math.sqrt(1 + (dirRayoY*dirRayoY) / (dirRayoX*dirRayoX));
            double deltaDistY = Math.sqrt(1 + (dirRayoX*dirRayoX) / (dirRayoY*dirRayoY));
            double perpWallDist;
            
            //"direccion a la que ir de x e y"
            int stepX, stepY;
            boolean hit = false; //"fue una colision contra el muro"
            int side=0; //"fue el muro vertical u horizontal"
            
            //"Figura la direccion paso y distancia inicial a un lado"
            if (dirRayoX < 0) {
                stepX = -1;
                disLadoX = (camara.posX - posXMapa) * deltaDistX;
            }
            else {
                stepX = 1;
                disLadoX = (posXMapa + 1.0 - camara.posX) * deltaDistX;
            }
            if (dirRayoY < 0) {
                stepY = -1;
                disLadoY = (camara.posY - posYMapa) * deltaDistY;
            }
            else {
                stepY = 1;
                disLadoY = (posYMapa + 1.0 - camara.posY) * deltaDistY;
            }
            
            //"Bucle para encontrar donde intersecta el rayo con el muro"
            while(!hit) {
                
                //"Salta al siguiente cuadrado"
                if (disLadoX < disLadoY)
                {
                    disLadoX += deltaDistX;
                    posXMapa += stepX;
                    side = 0;
                }
                else
                {
                    disLadoY += deltaDistY;
                    posYMapa += stepY;
                    side = 1;
                }
                //Check if ray has hit a wall
                //System.out.println(mapX + ", " + mapY + ", " + mapa[mapX][mapY]);
                if(mapa[posXMapa][posYMapa] > 0) hit = true;
            }
            //Calculate distance to the point of impact
            if(side==0) {
                perpWallDist = Math.abs((posXMapa - camara.posX + (1 - stepX) / 2) / dirRayoX);
            }
            else {
                perpWallDist = Math.abs((posYMapa - camara.posY + (1 - stepY) / 2) / dirRayoY);	
            }

            //Now calculate the alto of the wall based on the distance from the camera
            int lineHeight;
            if(perpWallDist > 0) lineHeight = Math.abs((int)(alto / perpWallDist));
            else lineHeight = alto;
            //calculate lowest and highest pixel to fill in current stripe
            int drawStart = -lineHeight/2+ alto/2;
            if(drawStart < 0)
                drawStart = 0;
            int drawEnd = lineHeight/2 + alto/2;
            if(drawEnd >= alto) 
                drawEnd = alto - 1;
            //add a texture
            int texNum = mapa[posXMapa][posYMapa] - 1;
            double wallX;//Exact position of where wall was hit
            if(side==1) {//If its a y-axis wall
                wallX = (camara.posX + ((posYMapa - camara.posY + (1 - stepY) / 2) / dirRayoY) * dirRayoX);
            } else {//X-axis wall
                wallX = (camara.posY + ((posXMapa - camara.posX + (1 - stepX) / 2) / dirRayoX) * dirRayoY);
            }
            wallX-=Math.floor(wallX);
            //x coordinate on the texture
            int texX = (int)(wallX * (texturas.get(texNum).tamañoTextura));
            if(side == 0 && dirRayoX > 0) texX = texturas.get(texNum).tamañoTextura - texX - 1;
            if(side == 1 && dirRayoY < 0) texX = texturas.get(texNum).tamañoTextura - texX - 1;
            
            //calculate y coordinate on texture
            for(int y=drawStart; y<drawEnd; y++) {
                int texY = (((y*2 - alto + lineHeight) << 6) / lineHeight) / 2;
                int color;
                if(side==0) color = texturas.get(texNum).pixeles[texX + (texY * texturas.get(texNum).tamañoTextura)];
                else color = (texturas.get(texNum).pixeles[texX + (texY * texturas.get(texNum).tamañoTextura)]>>1) & 8355711;//Make y sides darker
                pixeles[x + y*(ancho)] = color;
            }
        }
        return pixeles;
    }
}
