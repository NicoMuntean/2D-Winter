
package ui;

import java.awt.Color;


public class Timecycle {
    
    //Night RGB: 0,10,0
    //Day   RGB: 135,206,235
    
    // Ticks = 2000
    
    private double time;
    private double darknessAlpha = 0;
    private Color bgColor = new Color(0,10,0);
    private Color darknessMist = new Color(0,0,0,(float)darknessAlpha);
    
    public Timecycle() {
        time = 2599;
    }
    
    public void updateTime() {
       
        time+=1;
        if(time > 4000)
            time = 0;
        updateSky();
    }
    
    private void updateSky() {
        
        if(time > 1000 && time < 1500) {
            bgColor = new Color((int)(135*(time-1000)/500),(int)(10+(206*(time-1000)/500)),(int)(235*(time-1000)/500));
            darknessAlpha = 0.5-0.5*(time-1000)/500;
            darknessMist = new Color(0,0,0,(float)darknessAlpha);
        }
        else if(time > 2600 && time < 3100) {
            bgColor = new Color((int)(135-135*(time-2600)/500),(int)(10+(206-206*(time-2600)/500)),(int)(235-235*(time-2600)/500));
            darknessAlpha = 0.5*(time-2600)/500;
            darknessMist = new Color(0,0,0,(float)darknessAlpha);
        }
    }
    
    
    
    public Color getBGColor() {
        return bgColor;
    }
    
    public double getDarknessAlpha() {
        return darknessAlpha;
    }
    
    public Color getDarknessMist() {
        return darknessMist;
    }
    
    public double getTime() {
        return time;
    }
    
}
