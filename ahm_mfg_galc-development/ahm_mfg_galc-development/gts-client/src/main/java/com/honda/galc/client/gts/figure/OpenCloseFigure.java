package com.honda.galc.client.gts.figure;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.BezierFigure;

import com.honda.galc.entity.enumtype.GtsOrientation;

import java.awt.Color;
import java.awt.geom.*;

/**
 * 
 * 
 * <h3>OpenCloseFigure Class description</h3>
 * <p> OpenCloseFigure description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * May 11, 2015
 *
 *
 */
public class OpenCloseFigure extends BezierFigure{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static double DEFAULT_STROKE_WIDTH = 1.0;
	private static double DEFAULT_SIZE = 20.0;
	private static double DEFAULT_MARGIN = 3.0;
	private static Color DEFAULT_STROKE_COLOR = Color.blue;
	private static Color DEFAULT_OPEN_FILL_COLOR = Color.green;
	private static Color DEFAULT_CLOSE_FILL_COLOR = Color.red;
	
    /** center coordinate of the gate   */
    private Point2D.Double center;
	
    /**  flag to indicate the open/close status of the gate - open/close gates have different display image       */
    private boolean isOpen = false;
	
    /** the direction of the gate -- one of the directions EAST,SOUTH,WEST,NORTH      */
    private GtsOrientation orientation;
    
    /** the size of the gate image             */
	private double size;
	
    public OpenCloseFigure(int x,int y, boolean aFlag,GtsOrientation orientation){
		   this(x,y,aFlag,orientation,(int)DEFAULT_SIZE);
	}
	
	public OpenCloseFigure(int x,int y, boolean aFlag,GtsOrientation orientation, int size){
		   center = new Point2D.Double(x,y);
		   setOpenCloseFlag(aFlag);
		   this.orientation = orientation;
		   this.size = size;
		   setShape();
		   this.setClosed(true);
		   setAttribute(AttributeKeys.STROKE_WIDTH, DEFAULT_STROKE_WIDTH);
		   setAttribute(AttributeKeys.STROKE_COLOR, DEFAULT_STROKE_COLOR);
		   if (isOpen)
			   setAttribute(AttributeKeys.FILL_COLOR, DEFAULT_OPEN_FILL_COLOR);
		   else setAttribute(AttributeKeys.FILL_COLOR, DEFAULT_CLOSE_FILL_COLOR);
	}	
	
	public void setOpenCloseFlag(boolean aBool){
		isOpen = aBool;
	}
	
	public Point2D.Double getCenter(){
	    return center;
    }
    
	private void setShape(){
		
		if (isOpen)setOpenShape();
		else setCloseShape(); 
	}
	
	private void setOpenShape(){
		setOpenShapeBasic();
		switch(orientation){
			case EAST: transformShape(0); break;
			case SOUTH: transformShape(90);break;
			case WEST: transformShape(180);break;
			case NORTH: transformShape(270);break;
			default: transformShape(0);
		}
	}
	
	private void setCloseShape(){
		double w = (size - (DEFAULT_MARGIN * 2))/2;
		path.moveTo(center.x + w, center.y - w /2.0);
		path.lineTo(center.x - w, center.y - w /2.0);
		path.lineTo(center.x - w, center.y + w /2.0);
		path.lineTo(center.x + w, center.y + w /2.0);
	}
	
	private void setOpenShapeBasic(){
		double w = (size - (DEFAULT_MARGIN * 2))/2;
		path.moveTo(w, 0);
		path.lineTo(0, -w);
		path.lineTo(0,  - w /2.0);
		path.lineTo(- w, - w/2.0);
		path.lineTo( -w, w/2.0);
		path.lineTo(0, w/2.0);
		path.lineTo(0,w);
	}
	
	private void transformShape(int degree){
		path.transform(AffineTransform.getRotateInstance(Math.toRadians(degree)));
		path.transform(AffineTransform.getTranslateInstance(center.x, center.y));
	}
}
