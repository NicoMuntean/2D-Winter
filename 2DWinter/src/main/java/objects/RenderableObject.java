
package objects;

import java.awt.Graphics2D;
import ui.MainPanel;

public abstract class RenderableObject {
    
    public abstract void renderObject(Graphics2D g2d, double xPos, double yPos);
    
}
