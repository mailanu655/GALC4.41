package com.honda.galc.client.gts.view;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;

public class PolygonButton extends JButton {
    
    
    private static final long serialVersionUID = 1L;
    
    private Color color1 = new Color(0,100,0);
    private Color color2 = Color.green;
    
    public PolygonButton(String label){
        super(label);
    }
    
    
    
    public void paint(Graphics g) {
        
        super.paint(g);
        
        // Take advantage of Graphics2D to position string

        Graphics2D g2d = (Graphics2D)g;
        RenderingHints rh = g2d.getRenderingHints ();
        rh.put (RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints (rh);
 
        // fill the gradient color into the polygon
        GradientPaint gradient = new GradientPaint(20, 0, color1, 20, (int)getSize().getHeight()/2, color2, true);
        g2d.setPaint(gradient);
        Polygon polygon  = createPolygon();
        
        g2d.fillPolygon(polygon);
        
        // Finding size of text so can position in center.
        FontRenderContext frc = new FontRenderContext(null, false, false);
        Rectangle2D r = getFont().getStringBounds(getText(), frc);
 
        float xMargin = (float)(getWidth()-r.getWidth())/2;
        float yMargin = (float)(getHeight()-getFont().getSize())/2;
 
        // Draw the text in the center
        g2d.setColor(Color.BLACK);

        g2d.drawPolygon(polygon);

        g2d.drawString(getText(), xMargin, (float)getFont().getSize() + yMargin);
    }
    
    /**
     * set the fill color of the polygon to
     * @param aFlag
     */
    
    public void setComStatus(boolean aFlag) {
       
        if(aFlag) {
            color1 = new Color(0,100,0);
            color2 = Color.green;
        }else{
            color1 = new Color(190,0,0);
            color2 = Color.red;
        }
        
    }
    
    private Polygon createPolygon(){
        double dx = getSize().getWidth()/2 - getSize().getHeight();
        Polygon poly = new Polygon();
        poly.addPoint((int)dx, getSize().height /2);
        poly.addPoint((int)(dx + getSize().height /2) , 0);
        poly.addPoint((int)(dx + (getSize().height *1.5)) , 0 );
        poly.addPoint((int)(dx + (getSize().height *2.0)), getSize().height /2);
        poly.addPoint((int)(dx + (getSize().height *1.5)), getSize().height);
        poly.addPoint((int)(dx + getSize().height /2), getSize().height);
        
        return poly;
    }

   

    public boolean contains(int x, int y) {
      // If the button has changed size, 
      // make a new shape object.
     
      Polygon polygon  = createPolygon();
      return polygon.contains(x, y);
    }

    
}
