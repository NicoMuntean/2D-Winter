package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import objects.playerObjects.Inventory;
import ui.MainPanel;
import ui.objects.Camera;


public class Player extends RenderableObject{
    
    private double[] pos;
    private double[] vel;
    private double maxXvel = 4.0;
    private double maxYvel = 15.0;
    private double[][] hitpoints;
    private double[][] gravHitpoints;
    private Inventory pInv = new Inventory();
    
    public Player(double[] initPos) {
        
        pos = initPos;
        vel = new double[] {0,0};
        
        rearangeHitdetection();
    }
    
    public Player(double xPos, double yPos) {
        
        pos = new double[] {xPos,yPos};
        vel = new double[] {0,0};
        
        hitpoints = new double [6][2];
        gravHitpoints = new double[2][2];
        pInv.addItem(1, 30);
        pInv.addItem(2,58);
        pInv.addItem(3,45);
        rearangeHitdetection();
    }
    
    private void rearangeHitdetection() {
        hitpoints[0] = new double[] {pos[0],pos[1]};
        hitpoints[1] = new double[] {pos[0] + 15,pos[1]};
        hitpoints[2] = new double[] {pos[0],pos[1] + 1*31};
        hitpoints[3] = new double[] {pos[0] + 15,pos[1] + 31};
        hitpoints[4] = new double[] {pos[0],pos[1]+16};
        hitpoints[5] = new double[] {pos[0] + 15,pos[1]+16};
        gravHitpoints[0] = new double[] {pos[0]+1,pos[1] + 32};
        gravHitpoints[1] = new double[] {pos[0]+15,pos[1] + 32};
    }
    
    private boolean intersectsWithSolid(int[][] map,int mode) {
        
        boolean hitcheck = false;
            
        if(mode == 0) {
            for(double[] i : hitpoints) {
                if(map[(int)((i[0]+vel[0])/16)][(int)(i[1]/16)] != 0 && vel[0] != 0) {
                    hitcheck = true;
                }
            }
            
        }
        else if(mode == 1) {
            
            if(map[(int)((gravHitpoints[0][0])/16)][(int)((gravHitpoints[0][1])/16)] != 0 || map[(int)((gravHitpoints[1][0])/16)][(int)((gravHitpoints[1][1])/16)] != 0 ) {
                    hitcheck = true;
            }
        }
        return hitcheck;
    }
    
    public void updatePos(int w,int h,int[][] map, Camera c) {
        
        if(pos[0] + vel[0] < (w-1)*16 && pos[0] + vel[0] > 1*16) {
            
            boolean hitcheck = intersectsWithSolid(map,0);
            //if(map[(int)((pos[0]+vel[0])/16)][(int)(pos[1]/16)]==0) {
            if(!hitcheck) {
                pos[0] += vel[0];
                rearangeHitdetection();
            }
            else {
                if(vel[0] > 0) {
                    pos[0] = (pos[0]+vel[0])-((pos[0]+vel[0])%16);
                }
                else {
                    pos[0] = pos[0]-pos[0]%16;
                }
                rearangeHitdetection();
                vel[0] = 0;
            }
        }
        else if(pos[0] + vel[0] > (w-1)*16) {
            vel[0] = 0;
            pos[0] = (w-1)*16;
            rearangeHitdetection();
        }
        else if(pos[0] + vel[0] < 1*16) {
            vel[0] = 0;
            pos[0] = 1*16;
            rearangeHitdetection();
        }
        
        if(pos[1] + vel[1] < (h-1)*16 && pos[1] + vel[1] > 1*16) {
            
            boolean hitcheck = false;
            
            for(double[] i : hitpoints) {
                if(map[(int)((i[0])/16)][(int)((i[1]+vel[1])/16)] != 0) {
                    hitcheck = true;
                }
            }
            
            //if(map[(int)(pos[0]/16)][(int)((pos[1]+vel[1])/16)]==0) {
            if(!hitcheck) {
                pos[1] += vel[1];
                rearangeHitdetection();
            }
            else {
                if(vel[1] > 0) {
                    pos[1] = (pos[1]+vel[1])-((pos[1]+vel[1])%16);
                }
                else {
                    pos[1] = pos[1]-pos[1]%16; 
                }
                rearangeHitdetection();
                vel[1] = 0;
            }
        }
        else if(pos[1] + vel[1] > (h-1)*16) {
            vel[1] = 0;
            pos[1] = (h-1)*16;
            rearangeHitdetection();
        }
        else if(pos[1] + vel[1] < 1*16) {
            vel[1] = 0;
            pos[1] = 1*16;
            rearangeHitdetection();
        }
        
        c.updateCamPos(pos, w, h);
        
    }
    
    public double[] getPos() {
        return pos;
    }
    
    public double getX() {
        return pos[0];
    }
    
    public double getY() {
        return pos[1];
    }
    
    public int[] getPosAsInteger() {
        return new int[] {(int)pos[0],(int)pos[1]};
    }
        
    public void alterVelByIncr(double[] velIncr) {
        vel[0] += velIncr[0];
        vel[1] += velIncr[1];
    }
    
    public void setVel(double[] nVel) {
        vel = nVel;
    }
    
    public void alterXvelByIncr(double xVelIncr) {
        if(vel[0] + xVelIncr > maxXvel)
            vel[0] = maxXvel;
        else if (vel[0] + xVelIncr < -maxXvel) 
            vel[0] = -maxXvel;
        else
            vel[0] += xVelIncr;
    }
    
    public void alterYvelByIncr(double yVelIncr) {
        if(vel[1] + yVelIncr > maxYvel)
            vel[1] = maxYvel;
        else if (vel[1] + yVelIncr < -maxYvel) 
            vel[1] = -maxYvel;
        else
            vel[1] += yVelIncr;
    }
    
    public void velocityDecay(int index) {
        if(vel[index] != 0) {
            if(vel[index] > 2.0)
                vel[index] -= (vel[index]/(2*vel[index]));
            else if(vel[index] < -2.0)
                vel[index] += (vel[index]/(2*vel[index]));
            else
                vel[index] = 0;
        
        }
          
    }
    
    public void initiateJump(int[][] map) {
        boolean hitcheck = intersectsWithSolid(map,1);
        
        if(hitcheck) {
            vel[1] = -maxYvel;
        
        }
    }
    
    public void addFallVelocity(int[][] map) {
        boolean hitcheck = intersectsWithSolid(map,1);
        
        if(!hitcheck) {
            vel[1] += 1;
            if(vel[1] > maxYvel)
                vel[1] = maxYvel;
        
        }
        
        //System.out.println(vel[1]);
                
    }
    
    public Inventory getPlayerInventory() {
        return pInv;
    }
    
    public void setXvel(double nxVel) {
        vel[0] = nxVel;
    }
    
    public void setYvel(double nyVel) {
        vel[1] = nyVel;
    }
    
    public double[] getVel() {
        return vel;
    }
    
    public double getXvel() {
        return vel[0];
    }
    
    public double getYvel() {
        return vel[1];
    }
    
    public double[][] getHitpoints() {
        return hitpoints;
    }

    @Override
    public void renderObject(Graphics2D g2d, double xPos, double yPos) {
        g2d.setColor(Color.BLUE);
        g2d.fillRect((int)xPos, (int)yPos, 16, 32);
    }
}
