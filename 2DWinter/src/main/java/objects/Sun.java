
package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import ui.MainPanel;
import ui.Timecycle;


public class Sun extends RenderableObject{
    
    private double xPos;
    private double yPos;
    
    public Sun(Timecycle tc) {
        xPos = -1000+3000*(tc.getTime()/4000);
        yPos = 300-(280*Math.sin(Math.PI*(tc.getTime()/4000)));
    }
    
    public void updateSunPos(double time) {
        xPos = -700+2400*(time/4000);
        yPos = 300-(280*Math.sin(Math.PI*(time/4000)));
        
    }
    
    public double getX() {
        return xPos;
    }
    
    public int getXasInt() {
        return (int)xPos;
    }
    
    public double getY() {
        return yPos;
    }
    
    public int getYasInt() {
        return (int)yPos;
    }

    @Override
    public void renderObject(Graphics2D g2d, double xPos, double yPos) {
        g2d.setColor(Color.YELLOW);
        g2d.fillOval((int)xPos,(int)yPos, 40, 40);
    }
    
}
