package com.honda.galc.client.gts.figure;



import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.Action;

import org.jhotdraw.draw.AbstractAttributedFigure;
import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.EllipseFigure;
import org.jhotdraw.draw.FigureAdapter;
import org.jhotdraw.draw.FigureEvent;
import org.jhotdraw.draw.FigureListener;
import org.jhotdraw.draw.Handle;
import org.jhotdraw.draw.LineFigure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.RoundRectangleFigure;
import org.jhotdraw.draw.TriangleFigure;
import org.jhotdraw.draw.AttributeKeys.Orientation;

import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.gts.view.action.CopyShapeAction;
import com.honda.galc.client.gts.view.action.EditShapeAction;
import com.honda.galc.client.gts.view.action.RemoveShapeAction;
import com.honda.galc.entity.enumtype.GtsLineStyle;
import com.honda.galc.entity.gts.GtsShape;

import static org.jhotdraw.draw.AttributeKeys.*;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ShapeFigure</code> class is an graphical object which
 * displays the text. This figure has rich features.
 * It can set background color,border type
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


public class ShapeFigure extends GtsAbstractFigure{

   
    private static final long serialVersionUID = 1L;
    
       
    private GtsShape shape;
    private AbstractAttributedFigure figure;
    private FigureListener figureListener;
    
    public ShapeFigure(GtsShape shape){
        setShape(shape);
    }
    
    
    
    public GtsShape getShape(){
        return shape;
    }
    
    public void setShape(GtsShape shape){
        if (figure != null){
            this.remove(figure);
        }
        if(figureListener != null) this.removeFigureListener(figureListener);
        this.shape = shape;
        setFigure();
        if(shape.getFillColor() != null) setAttribute(AttributeKeys.FILL_COLOR,shape.getFillColor());
        setAttribute(AttributeKeys.STROKE_COLOR,shape.getLineColor());
        setAttribute(AttributeKeys.STROKE_WIDTH,(double)shape.getLineWidth());
        if(shape.getLineStyle()==GtsLineStyle.SOLID){
            this.removeAttribute(AttributeKeys.STROKE_DASHES);
        }else {
            AttributeKeys.STROKE_DASHES.basicSet(this, new double[]{3.0});
        }
        this.figureListener = new ShapeFigureHandler(this);
        this.addFigureListener(figureListener);
    }
    
    private void setFigure(){
        switch(shape.getShapeType()){
            case RECTANGLE:
                figure = new RectangleFig(shape.getX(),shape.getY(),shape.getWidth(),shape.getHeight());
                break;
            case ROUND_RECTANGLE:
                figure = new RoundRectangleFig(shape.getX(),shape.getY(),shape.getWidth(),shape.getHeight());
                break;
            case ELLIPSE:
                figure = new EllipseFig(shape.getX(),shape.getY(),shape.getWidth(),shape.getHeight());
                break;
            case TRIANGLE:
                figure = new TriangleFig(shape.getX(),shape.getY(),shape.getWidth(),shape.getHeight(),
                                            Orientation.valueOf(shape.getOrientation().toString()));
                break;
            case LINE:
                figure = new LineFigure();
                figure.setBounds(shape.getBounds());
                break;
        }
        
        add(figure);
    }
    
      
    public Collection<Handle> createHandles(int detailLevel) {
        if(GtsDrawing.isEditingMode()) 
            return figure.createHandles(detailLevel); 
        else return new LinkedList<Handle>();
    }
    
    
    // EDITING
    @Override public Collection<Action> getActions(Point2D.Double p) {
        LinkedList<Action> actions = new LinkedList<Action>();
        if(GtsDrawing.isEditingMode()){
            actions.add(new EditShapeAction(this));
            actions.add(new CopyShapeAction(this));
            actions.add(new RemoveShapeAction(this));
        }
        return actions;
    }
    
    
    public void updateShape(){
       shape.setBounds(getBounds());
        if(figure instanceof TriangleFigure){
            shape.setShapeStyle(ORIENTATION.get(figure).ordinal());
        }
    }
    /**
     * Transforms the figure.
     */
    public void transform(AffineTransform tx) {
        if(GtsDrawing.isEditingMode()){
            figure.transform(tx);
        }
    }
    
    private class ShapeFigureHandler extends FigureAdapter{
        private ShapeFigure owner;
        
        public ShapeFigureHandler(ShapeFigure figure){
            this.owner = figure;
        }
        
        
        public void figureChanged(FigureEvent e) {
            owner.updateShape();
       }
    }
    
    /**
     * change the behavior of the RectangleFigure to allow it to be transparent when it is not
     * assigned the fill_color
     * @author is08925
     *
     */
    
    private class RectangleFig extends RectangleFigure {
        
        private static final long serialVersionUID = 1L;
        
        public RectangleFig(double x, double y, double width, double height){
            super(x,y,width,height);
        }
        
        public Object getAttribute(AttributeKey key) {
            if(key.getKey().equals(AttributeKeys.FILL_COLOR.getKey())){
                return hasAttribute(key) ? getAttributes().get(key) : null;
            }else return super.getAttribute(key); 
        }    
    }
    
    /**
     * change the behavior of the RoundRectangleFigure to allow it to be transparent when it is not
     * assigned the fill_color
     * @author is08925
     *
     */
    
    private class RoundRectangleFig extends RoundRectangleFigure {
        
        private static final long serialVersionUID = 1L;
        
        public RoundRectangleFig(double x, double y, double width, double height){
            super(x,y,width,height);
        }
        
        @SuppressWarnings("unchecked")
		public Object getAttribute(AttributeKey key) {
            if(key.getKey().equals(AttributeKeys.FILL_COLOR.getKey())){
                return hasAttribute(key) ? getAttributes().get(key) : null;
            }else return super.getAttribute(key); 
        }    
    }
    
    /**
     * change the behavior of the EllipseFigure to allow it to be transparent when it is not
     * assigned the fill_color
     * @author is08925
     *
     */
    
    private class EllipseFig extends EllipseFigure {
        
        private static final long serialVersionUID = 1L;
        
        public EllipseFig(double x, double y, double width, double height){
            super(x,y,width,height);
        }
        
        @SuppressWarnings("unchecked")
		public Object getAttribute(AttributeKey key) {
            if(key.getKey().equals(AttributeKeys.FILL_COLOR.getKey())){
                return hasAttribute(key) ? getAttributes().get(key) : null;
            }else return super.getAttribute(key); 
        }    
    }
    
    /**
     * change the behavior of the TriangleFigure to allow it to be transparent when it is not
     * assigned the fill_color
     * @author is08925
     *
     */
    
    private class TriangleFig extends TriangleFigure {
        
        private static final long serialVersionUID = 1L;
        
        public TriangleFig(double x, double y, double width, double height,Orientation direction){
            super(x,y,width,height,direction);
        }
        
        @SuppressWarnings("unchecked")
		public Object getAttribute(AttributeKey key) {
            if(key.getKey().equals(AttributeKeys.FILL_COLOR.getKey())){
                return hasAttribute(key) ? getAttributes().get(key) : null;
            }else return super.getAttribute(key); 
        }    
    }
    
   }
