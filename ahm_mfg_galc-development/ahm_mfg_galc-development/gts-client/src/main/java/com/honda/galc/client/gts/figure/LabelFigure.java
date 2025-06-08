package com.honda.galc.client.gts.figure;

import static org.jhotdraw.draw.AttributeKeys.FILL_COLOR;
import static org.jhotdraw.draw.AttributeKeys.FONT_SIZE;
import static org.jhotdraw.draw.AttributeKeys.FONT_UNDERLINE;
import static org.jhotdraw.draw.AttributeKeys.TEXT;
import static org.jhotdraw.draw.AttributeKeys.TEXT_COLOR;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Action;

import org.jhotdraw.draw.AbstractAttributedDecoratedFigure;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.FigureAdapter;
import org.jhotdraw.draw.FigureEvent;
import org.jhotdraw.draw.FigureListener;
import org.jhotdraw.draw.Handle;

import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.gts.view.action.AddCarrierAction;
import com.honda.galc.client.gts.view.action.CloseLaneAction;
import com.honda.galc.client.gts.view.action.CopyLabelAction;
import com.honda.galc.client.gts.view.action.EditLabelAction;
import com.honda.galc.client.gts.view.action.LaneInfoAction;
import com.honda.galc.client.gts.view.action.RemoveLabelAction;
import com.honda.galc.entity.gts.GtsLabel;
import com.honda.galc.entity.gts.GtsLane;


/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>LabelFigure</code> class is an graphical object which
 * displays the text. This figure has rich features.
 * It can set font type,font size, font color, background color,border type
 * (No border, Rectangle, Round Rectangle, Ellipse etc), line color, line width
 * line type(Solid, Dash). It also clips the text inside the border shape. 
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
 * <TD>Feb 27, 2008</TD>
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

public class LabelFigure extends AbstractAttributedDecoratedFigure{
    
    
    private static final long serialVersionUID = 1L;
    
    protected Rectangle2D.Double bounds = new Rectangle2D.Double(0,0,0,0);
//  cache of the TextFigure's layout
    transient protected TextLayout textLayout;
    private GtsLabel label;
    private FigureListener figureListener;
    
    private List<Color> fillColors;
    
    
    public LabelFigure(String text){
        this(new GtsLabel(text));
    }
    
    public LabelFigure(GtsLabel label){
        setLabel(label);
    }
    
    public void setLabel(GtsLabel label){
//        if(getDrawing() != null)getDrawing().remove(this);
        this.label = label;
        if(figureListener != null) this.removeFigureListener(figureListener);
        this.setBounds(new Point2D.Double(label.getX(),label.getY()), 
                       new Point2D.Double(label.getX() + label.getWidth(),label.getY()+ label.getHeight()));
        this.setAttribute(AttributeKeys.STROKE_COLOR, label.getBorderColor());
        this.setAttribute(AttributeKeys.FONT_FACE, label.getFont());
        setTextColor(label.getTextColor());
        this.setAttribute(AttributeKeys.FILL_COLOR, getBackColor());
        setText(label.getLabelText());
        figureListener = new LabelFigureHandler(this);
        this.addFigureListener(figureListener);
    }
    
    public void refresh() {
    	this.setLabel(label);
    }
    
    private Color getBackColor() {
    	if(getDrawing() == null) return label.getBackColor();
    	
    	GtsLane lane = getDrawing().getModel().findLane(label.getLabelText());
    	if(lane != null && getDrawing().getModel().hasGates(lane.getLaneName()) && getDrawing().getModel().isLaneClosed(lane.getLaneName())) {
    		return Color.red;
    	}else return label.getBackColor();
    }
    
    public GtsLabel getLabel(){
        return label;
    }
    
    /**
     * update the figure bound information into the label object
     *
     */
    
    public void updateLabel(){
        if (label == null) return;
        label.setX((int)bounds.x);
        label.setY((int)bounds.y);
        label.setWidth((int)bounds.width);
        label.setHeight((int)bounds.height);
    }
    
    
    // DRAWING
    protected void drawStroke(java.awt.Graphics2D g) {
        
        if(label== null) return;
        double grow = AttributeKeys.getPerpendicularFillGrowth(this);
        Shape shape = label.getBorder(grow);
        if(shape != null)  g.draw(shape);
        
    }
    
    protected void drawFill(java.awt.Graphics2D g) {
        
        if(label== null) return;
 
        Shape shape = label.getBorder(0);
        if(shape == null) return;
        
        Shape oldShape = g.getClip();
        
        // current shape has to be intersected with the view clip shape
        // otherwise the text will be drawn onto the scroll bar
        Area area; 
        if(oldShape != null) {
        	area = new Area(oldShape);
        	area.intersect(new Area(shape));
        }else {
        	area = new Area(shape);
        }
        g.setClip(area);
        
        if(fillColors == null || fillColors.isEmpty())  g.fill(shape);
        else {
            
            Rectangle rect = shape.getBounds();
            
            rect.width = rect.width / fillColors.size();
            
            int i = 0;
            for(Color color:fillColors){
                
                g.setColor(color);
                
                g.fillRect(rect.x + (rect.width * i++), rect.y, rect.width, rect.height);
            }
        }
        
        if(oldShape != null) g.setClip(oldShape);
    }
   
    /**
     * draw the text within the bounding box.
     * the text is aligned at the center both horizontally and vertically
     */
    
    protected void drawText(java.awt.Graphics2D g) {
        
        if(label== null) return;
        
        Shape shape = label.getBorder(-1);
        Shape oldShape = g.getClip();
        
        // current shape has to be intersected with the view clip shape
        // otherwise the text will be drawn onto the scroll bar
        Area area; 
        if(oldShape != null) {
        	area = new Area(oldShape);
        	area.intersect(new Area(shape));
        }else {
        	area = new Area(shape);
        }
        
        g.setClip(area);
        
        // allign the start point of the text to the center
        
        if (getText() != null) {
            TextLayout layout = getTextLayout();
            layout.draw(g, (float) (bounds.x + (bounds.width  - layout.getVisibleAdvance())/2.0), 
                           (float) (bounds.y +(bounds.height + layout.getAscent() - layout.getDescent())/2.0));
        }
         if(oldShape != null) g.setClip(oldShape);
    }
    
    protected TextLayout getTextLayout() {
        
        if (textLayout == null) {
            String text = getText();
            if (text == null || text.length() == 0) {
                text = " ";
            }
            
            FontRenderContext frc = getFontRenderContext();
            HashMap<TextAttribute,Object> textAttributes = new HashMap<TextAttribute,Object>();
            textAttributes.put(TextAttribute.FONT, getFont());
            if (FONT_UNDERLINE.get(this)) {
                textAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_ONE_PIXEL);
            }
            textLayout = new TextLayout(text, textAttributes, frc);
        }
        return textLayout;
    }
    
    /**
     * Gets the drawing area without taking the decorator into account.
     */
    
    protected Rectangle2D.Double getTextDrawingArea() {
        
        if (getText() == null) {
            return getBounds();
        } else {
            TextLayout layout = getTextLayout();
            Rectangle2D.Double r = new Rectangle2D.Double(
                    bounds.x, bounds.y, layout.getAdvance(), layout.getAscent()
                    );
            Rectangle2D lBounds = layout.getBounds();
            if (! lBounds.isEmpty() && ! Double.isNaN(lBounds.getX())) {
                r.add(new Rectangle2D.Double(
                        lBounds.getX()+bounds.x,
                        (lBounds.getY()+bounds.y+layout.getAscent()),
                        lBounds.getWidth(),
                        lBounds.getHeight()
                        ));
            }
            // grow by two pixels to take antialiasing into account
       //     Geom.grow(r, 2d, 2d);
            return r;
        }
    }
    
    /**
     * pack the figure to adjust the bound to just fit the label inside the bound
     *
     */
    
    public void pack(){
       
        Rectangle2D.Double r =  getTextDrawingArea();
        
        // translate r to the center of the original bound
        r.height += 5;
        r.width += 10;
        r.x = getBounds().getCenterX() - (r.width /2.0);
        r.y = getBounds().getCenterY() - (r.height /2.0);
        this.setBounds(r);
        this.updateLabel();
    }
    
    public Font getFont() {
        return AttributeKeys.FONT_FACE.get(this);
    }
    
    public Color getTextColor() {
        return TEXT_COLOR.get(this);
    }
    
    public void setTextColor(Color color) {
        this.setAttribute(AttributeKeys.TEXT_COLOR, color);
    }
    
    public Color getFillColor() {
        return FILL_COLOR.get(this);
    }
    
    public void setFillColor(Color color) {
        this.setAttribute(AttributeKeys.FILL_COLOR, color);
    }
    
    public void setFillColors(List<Color> colors) {

        this.setFillColor(AttributeKeys.FILL_COLOR.getDefaultValue());
        this.fillColors = colors;
        
    }
    
    public void setFontStyleAndSize(int style, float size) {
        this.setAttribute(AttributeKeys.FONT_FACE,getFont().deriveFont(style,size));
    }
    
    public float getFontSize() {
        return FONT_SIZE.get(this).floatValue();
    }
    
//  SHAPE AND BOUNDS
    public void transform(AffineTransform tx) {
       
        if(GtsDrawing.isEditingMode()){
           Point2D point = tx.transform(this.getOrigin(),null);
           bounds.x = point.getX();
           bounds.y = point.getY();
       }
    }
  
    /**
     * Sets the text shown by the text figure.
     * This is a convenience method for calling willChange,
     * AttribuTEXT.basicSet, changed.
     */
    public void setText(String newText) {
        TEXT.set(this, newText);
        textLayout = null;
    }
    
    //  ATTRIBUTES
    /**
     * Gets the text shown by the text figure.
     */
    public String getText() {
        return TEXT.get(this);
    }
    
    public Collection<Handle> createHandles(int detailLevel) {
        if(GtsDrawing.isEditingMode()) return super.createHandles(detailLevel);
        else return new LinkedList<Handle>();
    }
    
    
    public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
        this.bounds = new Rectangle2D.Double(anchor.x,anchor.y,lead.x - anchor.x,lead.y - anchor.y);
        updateLabel();
    } 
    
    public Rectangle2D.Double getBounds() {
        return (Rectangle2D.Double) this.bounds.clone();
    }
    
    private Point2D.Double getOrigin(){
        return new Point2D.Double(bounds.x,bounds.y);
    }
    public Object getTransformRestoreData() {
        return getOrigin();
    }
    
    public void restoreTransformTo(Object geometry) {
        Point2D.Double p = (Point2D.Double) geometry;
        bounds.x = p.x;
        bounds.y = p.y;
    }
    
    public boolean figureContains(Point2D.Double p) {
        if (getBounds().contains(p)) {
            return true;
        }
        return false;
    }
    
    
    public GtsDrawing getDrawing(){
        return (GtsDrawing)super.getDrawing();
    }
    
    @Override 
    public Collection<Action> getActions(Point2D.Double p) {
        
        LinkedList<Action> actions = new LinkedList<Action>();
        
        if(GtsDrawing.isEditingMode()){
            actions.add(new EditLabelAction(this));
            actions.add(new CopyLabelAction(this));
            actions.add(new RemoveLabelAction(this));
        }else {
        	GtsLane lane = getDrawing().getModel().findLane(label.getLabelText());
        	if(lane != null) {
        		actions.add(new LaneInfoAction(getDrawing(),lane));
                if(getDrawing().getModel().isControllAllowed()){
                
                    // allow to add carrier only when this lane segment belongs to a lane
                    
                    actions.add(new AddCarrierAction(getDrawing(),lane,0,2));
                    actions.add(new AddCarrierAction(getDrawing(),lane,0,3));
                  
                    if(getDrawing().getModel().hasGates(lane.getLaneName()))
                    	actions.add(new CloseLaneAction(getDrawing(),lane,getDrawing().getModel().isLaneClosed(lane.getLaneName())));
                }
        	}
        }
        return actions;
    }
    
    private class LabelFigureHandler extends FigureAdapter{
        private LabelFigure owner;
        
        public LabelFigureHandler(LabelFigure figure){
            this.owner = figure;
        }
        
        
        public void figureChanged(FigureEvent e) {
            owner.updateLabel();
       }
    }
}
