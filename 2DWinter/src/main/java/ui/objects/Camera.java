
package ui.objects;

import objects.Player;

public class Camera {
    
    private double[] pos;
    
    public Camera(double initX, double initY) {
        
        pos = new double[] {initX,initY};
    }
    
    public Camera(Player p) {
        pos = new double[] {p.getX(),p.getY()};
    }
    
    public void updateCamPos(double[] playerPos, int w, int h) {
        
        if(playerPos[1] > 16.1*16 && playerPos[1] < 496.1*16)
            pos[1] = playerPos[1];
        if(playerPos[0] > 32.1*16 && playerPos[0] < 992.1*16)
            pos[0] = playerPos[0];
       
        
    }
    
    public double getX() {
        return pos[0];
    }
    
    public double getY() {
        return pos[1];
    }
    
    public double[] getPos() {
        return pos;
    }
    
    public int[] getPosAsInteger() {
        return new int[] {(int)pos[0],(int)pos[1]};
    }
    
    public void setX(double nX) {
        pos[0] = nX;
    }
    
    public void setY(double nY) {
        pos[1] = nY;
    }
    
    public void setPos(double[] nPos) {
        pos = nPos;
    }
}
