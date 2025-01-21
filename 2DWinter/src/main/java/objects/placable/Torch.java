
package objects.placable;

import java.awt.Color;
import java.awt.Graphics2D;
import objects.RenderableObject;
import ui.MainPanel;

public class Torch extends RenderableObject{
    
    private int[] pos;
    private int fuel;
    private final int MAX_FUEL = 1000;
    
    public Torch(int initX, int initY, int initFuel) {
        pos = new int[] {initX,initY};
        fuel = initFuel;
        if(fuel > MAX_FUEL)
            fuel = MAX_FUEL;
        else if(fuel < 0)
            fuel = 0;
    }
    
    public Torch(int[] initPos, int initFuel) {
        pos = initPos.clone();
        fuel = initFuel;
        if(fuel > MAX_FUEL)
            fuel = MAX_FUEL;
        else if(fuel < 0)
            fuel = 0;
    }
    
    public Torch(double initX,double initY, int initFuel) {
        pos = new int[] {(int)initX,(int)initY};
        fuel = initFuel;
        if(fuel > MAX_FUEL)
            fuel = MAX_FUEL;
        else if(fuel < 0)
            fuel = 0;
    }
    
    public Torch(double[] initPos, int initFuel) {
        pos = new int[] {(int)initPos[0],(int)initPos[1]};
        if(fuel > MAX_FUEL)
            fuel = MAX_FUEL;
        else if(fuel < 0)
            fuel = 0;
    }
    
    // CLASS METHODS
    
    // GETTER-SETTER
    
    public void setX(int nX) {
        pos[0] = nX;
    } 
    
    public void setX(double nX) {
        pos[0] = (int)nX;
    }
    
    public int getX() {
        return pos[0];
    }
    
    public double getXasDouble() {
        return (double)pos[0];
    }
    
    public void setY(int nY) {
        pos[1] = nY;
    }
    
    public void setY(double nY) {
        pos[1] = (int)nY;
    }
    
    public int getY() {
        return pos[0];
    }
    
    public double getYasDouble() {
        return (double)pos[1];
    }
    
    public void setPos(int[] nPos) {
        pos = nPos.clone();
    }
    
    public void setPos(double[] nPos) {
        pos = new int[] {(int)nPos[0],(int)nPos[1]};
    }
    
    public void setFuel(int nFuel) {
        fuel = nFuel;
        if(fuel > MAX_FUEL)
            fuel = MAX_FUEL;
        else if(fuel < 0) 
            fuel = 0;
    }
    
    public void addFuel(int fuelIncr) {
        fuel += fuelIncr;
        if(fuel > MAX_FUEL) {
            fuel = MAX_FUEL;
        }
        else if(fuel < 0) 
            fuel = 0;
    }
    
    public int getFuel() {
        return fuel;
    }


    @Override
    public void renderObject(Graphics2D g2d, double xPos, double yPos) {
        
        g2d.setColor(Color.PINK);
        g2d.fillRect(50, 50, 300, 300);
        
    }
    
}
