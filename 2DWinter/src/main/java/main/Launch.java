package main;

import javax.swing.JFrame;
import ui.MainPanel;


public class Launch {
    
    public static void main(String[] args) {
        
        JFrame mainFrame = new JFrame();
        mainFrame.setSize(1008,500);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setTitle("Winter PreBuilt");
        mainFrame.add(new MainPanel(1024,512));
        mainFrame.setVisible(true);
        
    }
    
}
