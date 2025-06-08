package com.honda.galc.client.gts.view;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;

import org.jhotdraw.draw.AbstractTool;

public class GtsAreaTracker extends AbstractTool {
    
    int xOffs, yOffs;
    
    //  implements java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent e) {
    
        ((GtsDrawingView)getView()).dragArea(e.getX() - xOffs,e.getY() - yOffs);
        
    }

    //     implements java.awt.event.MouseListener
    public void mousePressed(MouseEvent e) {
        getView().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        xOffs = e.getX();
        yOffs = e.getY();
    }

//     implements java.awt.event.MouseListener
    public void mouseReleased(MouseEvent e) {
        getView().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    
    public void mouseMoved(MouseEvent evt) {
     
        updateCursor(editor.findView((Container) evt.getSource()), new Point(evt.getX(), evt.getY()));
    }
}
