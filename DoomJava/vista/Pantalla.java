package vista;

import java.util.ArrayList;
import java.awt.Color;
import modelo.Camara;
import modelo.Textura;

public class Pantalla {
    public int[][] mapa;
    public int anchoMapa=0, 
               altoMapa=0, 
               ancho=0, 
               alto=0;

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
            double disDeltaX = Math.sqrt(1 + (dirRayoY*dirRayoY) / (dirRayoX*dirRayoX));
            double disDeltaY = Math.sqrt(1 + (dirRayoX*dirRayoX) / (dirRayoY*dirRayoY));
            double distMuroPerp;
            
            //"direccion a la que ir de x e y"
            int pasoX, pasoY;
            boolean colision = false; //"fue una colision contra el muro"
            int lado=0; //"fue el muro vertical u horizontal"
            
            //"Figura la direccion paso y distancia inicial a un lado"
            if (dirRayoX < 0) {
                pasoX = -1;
                disLadoX = (camara.posX - posXMapa) * disDeltaX;
            }
            else {
                pasoX = 1;
                disLadoX = (posXMapa + 1.0 - camara.posX) * disDeltaX;
            }
            if (dirRayoY < 0) {
                pasoY = -1;
                disLadoY = (camara.posY - posYMapa) * disDeltaY;
            }
            else {
                pasoY = 1;
                disLadoY = (posYMapa + 1.0 - camara.posY) * disDeltaY;
            }
            
            //"Bucle para encontrar donde intersecta el rayo con el muro"
            while(!colision) {
                
                //"Salta al siguiente cuadrado"
                if (disLadoX < disLadoY)
                {
                    disLadoX += disDeltaX;
                    posXMapa += pasoX;
                    lado = 0;
                }
                else
                {
                    disLadoY += disDeltaY;
                    posYMapa += pasoY;
                    lado = 1;
                }
                
                //Check if ray has hit a wall
                //System.out.println(mapX + ", " + mapY + ", " + mapa[mapX][mapY]);
                if(mapa[posXMapa][posYMapa] > 0) {
                    colision = true;
                }
            }
            //Calculate distance to the point of impact
            if(lado==0) {
                distMuroPerp = Math.abs((posXMapa - camara.posX + (1 - pasoX) / 2) / dirRayoX);
            }
            else {
                distMuroPerp = Math.abs((posYMapa - camara.posY + (1 - pasoY) / 2) / dirRayoY);	
            }

            //Now calculate the alto of the wall based on the distance from the camera
            int alturaLinea;
            if(distMuroPerp > 0) {
                alturaLinea = Math.abs((int)(alto / distMuroPerp));
            }
            else {
                alturaLinea = alto;
            }
            //calculate lowest and highest pixel to fill in current stripe
            int inicioDibujo = -alturaLinea/2+ alto/2;
            if(inicioDibujo < 0) {
                inicioDibujo = 0;
            }
            int finDibujo = alturaLinea/2 + alto/2;
            if(finDibujo >= alto) {
                finDibujo = alto - 1;
            }
            
            //add a texture
            int numTex = mapa[posXMapa][posYMapa] - 1;
            double muroX;//Exact position of where wall was hit
            if(lado==1) {//If its a y-axis wall
                muroX = (camara.posX + ((posYMapa - camara.posY + (1 - pasoY) / 2) / dirRayoY) * dirRayoX);
            } 
            else {//X-axis wall
                muroX = (camara.posY + ((posXMapa - camara.posX + (1 - pasoX) / 2) / dirRayoX) * dirRayoY);
            }
            muroX-=Math.floor(muroX);
            //x coordinate on the texture
            int xText = (int)(muroX * (texturas.get(numTex).tamañoTextura));
            if(lado == 0 && dirRayoX > 0) {
                xText = texturas.get(numTex).tamañoTextura - xText - 1;
            }
            else if(lado == 1 && dirRayoY < 0) {
                xText = texturas.get(numTex).tamañoTextura - xText - 1;
            }
            
            //calculate y coordinate on texture
            for(int y=inicioDibujo; y<finDibujo; y++) {
                int texY = (((y*2 - alto + alturaLinea) << 6) / alturaLinea) / 2;
                int color=0;
                
                if(lado==0) {
                    color = texturas.get(numTex).pixeles[xText + (texY * texturas.get(numTex).tamañoTextura)];
                }
                else {
                    color = (texturas.get(numTex).pixeles[xText + (texY * texturas.get(numTex).tamañoTextura)]>>1) & 8355711; //"Oscurece los lados y"
                }
                
                pixeles[x + y*(ancho)] = color;
            }
        }
        return pixeles;
    }
}
