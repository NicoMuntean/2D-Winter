package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import map.MidpointDisplacement;
import objects.Moon;
import objects.Player;
import objects.Sun;
import objects.placable.Torch;
import ui.objects.Camera;


public class MainPanel extends JPanel implements MouseListener,MouseMotionListener,KeyListener{
    
    private int panelW;
    private int panelH;
    private int[][] tileMap;
    
    private boolean wPressed = false;
    private boolean aPressed = false;
    private boolean sPressed = false;
    private boolean dPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    
    private boolean leftMousePressed = false;
    private boolean rightMousePressed = false;
    
    private boolean paintDone = false;
    private boolean updateDone = false;

    private final long TARGET_FPS = 60;
    private long elapsedTime;
    private long totalElapsed = 0;
    private long milliCnt = 0;
    private long startTime;
    
    private BufferedImage bgImg;
    
    private Camera c;
    private Player p;
    private Timecycle tc;
    private Sun sun;
    private Moon moon;
    
    int mX = 0;
    int mY = 0;
    
    Torch torch = new Torch(100,100,100);
    
    public MainPanel(int w, int h) {
        
        panelW = w;
        panelH = h;
        tileMap = new MidpointDisplacement(h+1,w+1,8,new int[] {200,400}).getMap();
        tc = new Timecycle();
        sun = new Sun(tc);
        moon = new Moon(tc);
        
        for(int i = 0; i < panelH; i++) {
            if(tileMap[50][i] != 0) {
                p = new Player(50*16,(i-2)*16);
                break;
            }
        }
        
        c = new Camera(p);
        try {
            bgImg = ImageIO.read(new File("src/main/java/resources/bg_img_alpha.png"));
        } catch(IOException e) {
            System.out.println("LOADING OF BACKGROUND IMAGE FAILED!");
        }
        
        addMouseMotionListener(this);
        addKeyListener(this);
        addMouseListener(this);
        setSize(h,w);
        setFocusable(true);
        requestFocus(true);
        setVisible(true);
        repaint();
        
       
        
        new Thread() {
            @Override
            public void run() {
                int updateCnt = 0;
                long referenceTime = System.currentTimeMillis();
                startTime = System.currentTimeMillis();

                while (true) {

                    elapsedTime = System.currentTimeMillis() - startTime;

                    totalElapsed = System.currentTimeMillis() - referenceTime;
                    milliCnt += 1;
                    
                    if( updateCnt % 10 == 0) {
                        tc.updateTime();
                        sun.updateSunPos(tc.getTime());
                        moon.updateMoonPos(tc.getTime());
                        updateCnt++;
                        if(updateCnt > 10)
                            updateCnt = 1;
                    }
                    else updateCnt++;
                    
                    updateDone = false;
                    
                    checkPresses();
                    p.addFallVelocity(tileMap);
                    p.updatePos(w, h, tileMap, c);
                    
                    updateDone = true;
                    
                    //if(updateDone)
                    repaint();

                    if (elapsedTime < 1000 / TARGET_FPS) {

                        try {

                            this.sleep(1000 / TARGET_FPS - elapsedTime);
                            startTime = System.currentTimeMillis();

                        } catch (Exception e) {

                        }
                    }

                    if (totalElapsed >= 1000) {

                        referenceTime = System.currentTimeMillis();
                        System.out.println("FPS: " + milliCnt);
                        milliCnt = 0;
                        

                    }

                }
            }
        }.start();
        
    }
    
    @Override
    public void paintComponent(Graphics g) {
        
        paintDone = false;
        
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        
        int[] renderPos = c.getPosAsInteger();
        double[][] pIntersecP = p.getHitpoints();
        
        int wMultRenderIncr = 512;
        int wTotalRenderSize = 546;
        int hMultRenderIncr = 256;
        int hTotalRenderSize = 272;
        
        double time = tc.getTime();
        
        g2d.setColor(tc.getBGColor());
        g2d.fillRect(0, 0, panelW, panelH);
        
        sun.renderObject(g2d, sun.getXasInt(), sun.getYasInt());
        moon.renderObject(g2d, moon.getXasInt(), moon.getYasInt());
        
        g2d.drawImage(bgImg,0,0,this);
        
        g2d.setColor(tc.getDarknessMist());
        g2d.fillRect(0, 0, panelW, panelH);
        
        for(int i = renderPos[0]-wTotalRenderSize; i < renderPos[0]+wTotalRenderSize; i+=1) {
            for(int j = renderPos[1]-hTotalRenderSize; j < renderPos[1]+hTotalRenderSize; j+=1) {
                
                if(i > 0 && i < panelW*16 && j > 0 && j < panelH*16 && i%16==0 && j%16==0) {
                    
                    double distToPlayer = Math.sqrt(Math.pow((p.getX()/16)-(i/16),2)+Math.pow((p.getY()/16)-(j/16),2));
                    
                    if(tileMap[(int)(i/16)][(int)(j/16)] == 0) {
                    g2d.setColor(Color.CYAN);
                    }
                    else if(tileMap[(int)(i/16)][(int)(j/16)]==1){
                        g2d.setColor(Color.DARK_GRAY);
                    }
                    else if(tileMap[(int)(i/16)][(int)(j/16)]==3) {
                        g2d.setColor(new Color(66,40,14));
                    }
                    else
                        g2d.setColor(new Color(0,80,0));
                    
                    if(tileMap[(int)(i/16)][(int)(j/16)] != 0)
                        g2d.fillRect((i - renderPos[0]+wMultRenderIncr),(j - renderPos[1]+hMultRenderIncr),16,16);
                    
                    if(tileMap[(int)(i/16)][(int)(j/16)] != 0 && (time < 1500 || time > 2600)) {
                        
                        float alphaCoeff = 1.0f;
                        
                        if(time > 1000 && time < 1500) {
                            alphaCoeff = 1.0f - (float)(time-1000)/500;
                        }
                        else if(time > 2600 && time < 3100) {
                            alphaCoeff = (float)(time-2600)/500;
                        }
                        
                        if(distToPlayer > 10)
                            g2d.setColor(new Color(0f,0f,0f,0.75f*alphaCoeff));
                        else
                            g2d.setColor(new Color(0f,0f,0f,(float)(((0.75*(distToPlayer/10))))*alphaCoeff));
                        g2d.fillRect((i - renderPos[0]+wMultRenderIncr),(j - renderPos[1]+hMultRenderIncr),16,16);
                    }
                    
                    boolean furtherShading = true;
                    double minDis = 10000;
                    
                    for(int k = -5; k < 5; k++) {
                        for(int l = -5; l < 5; l++) {
                            
                            if((i/16)+k > 0 && (i/16) + k < panelW && (j/16)+l > 0 && (j/16)+l < panelH) {
                                if(tileMap[(int)((i)/16)+k][(int)((j)/16)+l] == 0) {
                                    furtherShading = false;
                                    double dis = Math.sqrt(Math.pow(k,2)+Math.pow(l,2));
                                    if(minDis > dis)
                                        minDis = dis;
                                }
                            }
                        }
                        
                    }
                    
                    if(furtherShading) {
                        
                        if(distToPlayer > 10)
                            g2d.setColor(new Color(0f,0f,0f,0.75f));
                        else
                            g2d.setColor(new Color(0f,0f,0f,(float)(((0.75*(distToPlayer/10))))));
                        g2d.fillRect((i - renderPos[0]+wMultRenderIncr),(j - renderPos[1]+hMultRenderIncr),16,16);
                    }
                    else if(!furtherShading && tileMap[(int)((i)/16)][(int)((j)/16)] != 0) {
                        
                        if(distToPlayer > 10)
                            g2d.setColor(new Color(0f,0f,0f,0.75f*(float)(minDis/Math.sqrt(50))));
                        else
                            g2d.setColor(new Color(0f,0f,0f,(float)(((0.75*(float)(minDis/Math.sqrt(50))*(distToPlayer/10))))));
                        g2d.fillRect((i - renderPos[0]+wMultRenderIncr),(j - renderPos[1]+hMultRenderIncr),16,16);
                    }
                    
                    
                }
                
            }
            
          
        }
        
        for(int k = 0; k < 9; k++) {
            
            if(p.getPlayerInventory().getSelectedSlot() == k)
                g2d.setColor(Color.RED);
            else
                g2d.setColor(Color.WHITE);
                
            g2d.drawRect(80 + 100*k, 20, 50, 50);
            int itemID = p.getPlayerInventory().getItemIDAtIndex(k);
            
            if(itemID != -1) {
                
                if(itemID==1){
                    g2d.setColor(Color.DARK_GRAY);
                }
                else if(itemID == 2) {
                    g2d.setColor(new Color(0,50,0));
                }
                else if(itemID==3) {
                    g2d.setColor(new Color(66,40,14));
                }
                
                g2d.fillRect(90 + 100*k,30,30,30);
                
                g2d.setColor(Color.WHITE);
                
                g2d.drawString(Integer.toString(p.getPlayerInventory().getItemQuantityAtIndex(k)), 97 + 100*k,50);
                
            }
            
            
        }
        
        p.renderObject(g2d, ((int)p.getX() - renderPos[0]+wMultRenderIncr), ((int)p.getY() - renderPos[1]+hMultRenderIncr));
        
        paintDone = true;
        
        
    }
    
    private void checkPresses() {

        if (wPressed && paintDone) {
            p.initiateJump(tileMap);
            //if(c.getY() > 16.1)
            //    c.setY(c.getY() - 1);
        }
                    
        if (aPressed && paintDone) {
            p.alterXvelByIncr(-1);
            //if(c.getX() > 32.1)
            //    c.setX(c.getX() - 1);
        }
        /*
        if (sPressed && paintDone) {
            p.alterYvelByIncr(4);
            //if(c.getY() < 496.1)
            //    c.setY(c.getY() + 1);
        }
        */
        if (dPressed && paintDone) {
            p.alterXvelByIncr(1);
            //if(c.getX() < 992.1)
            //    c.setX(c.getX() + 1);
        }
        
        if(!aPressed && !dPressed)
            p.velocityDecay(0);
        //if(!wPressed && !sPressed)
        //    p.velocityDecay(1);
        
        if(leftMousePressed) {
            int[] renderPos = c.getPosAsInteger();
            int wTotalRenderSize = 512;
            int hTotalRenderSize = 256;
            
            int i = renderPos[0]-wTotalRenderSize+mX;
            int j = renderPos[1]-hTotalRenderSize+mY;
            
            if(tileMap[(int)(i/16)][(int)(j/16)] != 0) {
                
                p.getPlayerInventory().addItem(tileMap[(int)(i/16)][(int)(j/16)], 1);
                tileMap[(int)(i/16)][(int)(j/16)] = 0;
                
            }
            
        }
        
        if(rightMousePressed) {
            int[] renderPos = c.getPosAsInteger();
            int wTotalRenderSize = 512;
            int hTotalRenderSize = 256;
            
            int i = renderPos[0]-wTotalRenderSize+mX;
            int j = renderPos[1]-hTotalRenderSize+mY;
            
            int itemID = p.getPlayerInventory().getItemIDAtIndex(p.getPlayerInventory().getSelectedSlot());
            
            if(tileMap[(int)(i/16)][(int)(j/16)] == 0 && itemID != -1) {
                
                tileMap[(int)(i/16)][(int)(j/16)] = itemID;
                p.getPlayerInventory().removeItem(itemID);
                
            }
            
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int button = e.getButton();
        
        if(button == 1) {
            leftMousePressed=true;
        }
        if(button == 3) {
            rightMousePressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int button = e.getButton();
        if(button == 1) {
            leftMousePressed=false;
        }
        if(button == 3) {
            rightMousePressed = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mX = e.getX();
        mY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mX = e.getX();
        mY = e.getY();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_SPACE) {
            wPressed = true;
        }
        if(key == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if(key == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if(key == KeyEvent.VK_D) {
            dPressed = true;
        }
        if(key == KeyEvent.VK_S) {
            sPressed = true;
        }
        if(key == KeyEvent.VK_A) {
            aPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        System.out.println(key);
        if(key == KeyEvent.VK_SPACE) {
            wPressed = false;
        }
        if(key == KeyEvent.VK_D) {
            dPressed = false;
        }
        if(key == KeyEvent.VK_S) {
            sPressed = false;
        }
        if(key == KeyEvent.VK_RIGHT) {
            rightPressed = false;
            p.getPlayerInventory().pivotSelectedSlot(0);
        }
        if(key == KeyEvent.VK_LEFT) {
            leftPressed = false;
            p.getPlayerInventory().pivotSelectedSlot(1);
        }
        if(key == KeyEvent.VK_A) {
            aPressed = false;
        }
    }
    
}
