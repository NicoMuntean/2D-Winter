
package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import ui.MainPanel;
import ui.Timecycle;

public class Moon extends RenderableObject{
    
    private double xPos;
    private double yPos;
    
    public Moon(Timecycle tc) {
        xPos = -2000+3000*(tc.getTime()/4000);
        yPos = 300-(280*Math.sin(Math.PI*(tc.getTime()/4000)));
    }
    
    public void updateMoonPos(double time) {
        if(time > 3000 || time < 1000) {
            if(time > 3000) {
                xPos = -50+560*((time-3000)/1000);
                yPos = 80-(60*Math.sin(Math.PI/2*((time-3000)/1000)));
            }
            else {
                xPos = 510+560*(time/1000);
                yPos = 20+(60-60*Math.sin(Math.PI/2+Math.PI/2*(time/1000)));
            }
        }
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
        g2d.setColor(Color.WHITE);
        g2d.fillOval((int)xPos, (int)yPos, 40, 40);
    }
}
