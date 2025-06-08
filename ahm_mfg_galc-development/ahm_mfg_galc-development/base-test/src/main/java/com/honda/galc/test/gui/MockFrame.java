package com.honda.galc.test.gui;

import javax.swing.JFrame;

public class MockFrame {
    
    private static JFrame frame;
    
    public static void createFrame(){
        
        if(frame == null) frame = new JFrame();
    }
}
