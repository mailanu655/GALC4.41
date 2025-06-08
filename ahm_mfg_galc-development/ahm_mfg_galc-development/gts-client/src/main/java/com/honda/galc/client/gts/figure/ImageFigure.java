

package com.honda.galc.client.gts.figure;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;

import javax.swing.Timer;

import org.jhotdraw.draw.AbstractAttributedDecoratedFigure;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.geom.*;
import static org.jhotdraw.draw.AttributeKeys.*;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ImageFigure</code> is a class which is used to display the product
 * outline image. The image can be highlighted as pink color to indicate
 * a possible tracking off<br>
 * The figure can switch between snail image to the product outline image to indicate
 * it is moving.
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Jeffray Huang</TD>
 * <TD>Mar 4, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 */

public class ImageFigure extends AbstractAttributedDecoratedFigure implements ActionListener{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * This rectangle describes the bounds into which we draw the image.
     */
    private Rectangle2D.Double rectangle;

    private BufferedImage image;
    
    private Timer timer;
    
    private boolean displaySnailFlag = false;
    
    /** Creates a new instance. */
    public ImageFigure() {
        this(0,0,0,0);
    }
    public ImageFigure(double x, double y, double width, double height) {
        rectangle = new Rectangle2D.Double(x, y, width, height);
    }
    
    public void setBufferedImage(BufferedImage image){
        this.image = image;
    }
    
    public BufferedImage getBufferedImage(){
       return image;
    }
    
    public void enableSnail(boolean aFlag){
        if(aFlag && timer == null){
            timer = new Timer(1000,this);
            timer.start();
        }else if(!aFlag && timer != null){
            timer.stop();
            timer = null;
            displaySnailFlag = false;
        }
    }
    
    // DRAWING
    protected void drawFigure(Graphics2D g) {
        if(hasAttribute(AttributeKeys.FILL_COLOR)){
            g.setColor(AttributeKeys.FILL_COLOR.get(this));
            drawFill(g);
        }
        drawImage(g);
        
        if (STROKE_COLOR.get(this) != null && STROKE_WIDTH.get(this) > 0d) {
            g.setStroke(AttributeKeys.getStroke(this));
            g.setColor(STROKE_COLOR.get(this));
            
            drawStroke(g);
        }
        if (TEXT_COLOR.get(this) != null) {
            if (TEXT_SHADOW_COLOR.get(this) != null &&
                    TEXT_SHADOW_OFFSET.get(this) != null) {
                Dimension2DDouble d = TEXT_SHADOW_OFFSET.get(this);
                g.translate(d.width, d.height);
                g.setColor(TEXT_SHADOW_COLOR.get(this));
                drawText(g);
                g.translate(-d.width,-d.height);
            }
            g.setColor(TEXT_COLOR.get(this));
            drawText(g);
        }
    }
    
    protected void drawFill(Graphics2D g) {
        Rectangle2D.Double r = (Rectangle2D.Double) rectangle.clone();
        double grow = AttributeKeys.getPerpendicularFillGrowth(this);
        Geom.grow(r, grow, grow);
        g.fill(r);
    }
    
    protected void drawImage(Graphics2D g){
        if(displaySnailFlag) drawSnail(g);
        else this.drawBufferedImage(g);
    }
    
    private void drawSnail(Graphics2D g){
        g.drawImage(ImageFactory.getInstance().getSnailImage(), (int) rectangle.x, (int) rectangle.y, (int) rectangle.width, (int) rectangle.height, null);
    }
    
    protected void drawStroke(Graphics2D g) {
        if(image == null) return;
        Rectangle2D.Double r = (Rectangle2D.Double) rectangle.clone();
        double grow = AttributeKeys.getPerpendicularDrawGrowth(this);
        Geom.grow(r, grow, grow);
        // Currently the border is disabled. We can uncomment it if the border is needed
        //       g.draw(r);
    }
    
    private void drawBufferedImage(Graphics2D g){
        if (image != null) {
            g.drawImage(image, (int) rectangle.x, (int) rectangle.y, (int) rectangle.width, (int) rectangle.height, null);
        }   
    }
    
    // SHAPE AND BOUNDS
    public Rectangle2D.Double getBounds() {
        Rectangle2D.Double bounds = (Rectangle2D.Double) rectangle.clone();
        return bounds;
    }
    
    @Override public Rectangle2D.Double getFigureDrawingArea() {
        Rectangle2D.Double r = (Rectangle2D.Double) rectangle.clone();
        double grow = AttributeKeys.getPerpendicularHitGrowth(this);
        Geom.grow(r, grow, grow);
        return r;
    }
    
    /**
     * Checks if a Point2D.Double is inside the figure.
     */
    public boolean figureContains(Point2D.Double p) {
        Rectangle2D.Double r = (Rectangle2D.Double) rectangle.clone();
        double grow = AttributeKeys.getPerpendicularHitGrowth(this) + 1d;
        Geom.grow(r, grow, grow);
        return r.contains(p);
    }
    
    public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
        rectangle.x = Math.min(anchor.x, lead.x);
        rectangle.y = Math.min(anchor.y , lead.y);
        rectangle.width = Math.max(0.1, Math.abs(lead.x - anchor.x));
        rectangle.height = Math.max(0.1, Math.abs(lead.y - anchor.y));
    }
    /**
     * Transforms the figure.
     * @param tx The transformation.
     */
    public void transform(AffineTransform tx) {
        Point2D.Double anchor = getStartPoint();
        Point2D.Double lead = getEndPoint();
        setBounds(
                (Point2D.Double) tx.transform(anchor, anchor),
                (Point2D.Double) tx.transform(lead, lead)
                );
    }
    // ATTRIBUTES
    
    
    public void restoreTransformTo(Object geometry) {
        rectangle.setRect((Rectangle2D.Double) geometry);
    }
    
    public Object getTransformRestoreData() {
        return (Rectangle2D.Double) rectangle.clone();
    }
    
    public void actionPerformed(ActionEvent e) {
        displaySnailFlag = !displaySnailFlag;
        this.changed();
    }
    
}