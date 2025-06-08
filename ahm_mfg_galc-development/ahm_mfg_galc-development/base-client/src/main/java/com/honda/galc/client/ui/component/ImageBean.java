package com.honda.galc.client.ui.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.entity.qics.ImageSection;
/**
 * <h3>Class description</h3>
 *  <h4>Description</h4>
 *  <p>It is used by ImageSectionMaintenance.
 *  <br> 
 *  </p>
 *  <h4>Usage and Example</h4>
 *  <h4>Special Notes</h4> 
 *  <h4>Change History</h4>
 *  <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 *  <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 *  <TH>Updated by</TH>
 *  <TH>Update date</TH>
 *  <TH>Version</TH>
 *  <TH>Mark of Update</TH>
 *  <TH>Reason</TH>
 *  </TR>
 *  <TR>
 *  <TD>K.Ishibe</TD>
 *  <TD>(2001/01/13 16:52:02)</TD>
 *  <TD>0.1</TD>
 *  <TD>(none)</TD>
 *  <TD>Initial Creation</TD>
 *  </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Aug 12, 2004</TD>
 * <TD>1.0</TD>
 * <TD>JM006</TD>
 * <TD>SystemException constructor change</TD>
 * </TR>
 * </TABLE>
 *  @see
 *  @ver 0.1
 *  @Tuan Truong Anh
 */
public class ImageBean extends javax.swing.JPanel {
	
	private static final long serialVersionUID = 1L;
	private	java.awt.image.BufferedImage	image;
	private java.awt.image.BufferedImage	orgImage;
	private List<PartPolygon>				polygons ;
	private	List<Point>						lines;
	private	List<Point>						nudges;
	private	PartPolygon						currentPolygon			=	new PartPolygon();
	int polygonIx = 0;
	private int								mouseClickedX	=	0;
	private int								mouseClickedY	=	0;
	private float							zoomScale		=	1;
	private int								orgX;
	private int								orgY;
	/**
	It is a fixed value meaning pointer mode.
	*/
	public static final int					POINTER_MODE	=	1;
	/**
	It is a fixed value meaning move back mode.
	*/
	public static final int					MOVEBACK_MODE	=	6;
	/**
	It is a fixed value meaning zoom mode.
	*/
	public static final int					ZOOM_MODE		=	5;
	/**
	It is a fixed value meaning pan mode.
	*/
	public static final int					PAN_MODE		=	4;
	/**
	It is a fixed value meaning nudge mode.
	*/
	public static final int					NUDGE_MODE		=	3;
	/**
	It is a fixed value meaning shape mode.
	*/
	public static final int					SHAPE_MODE		=	2;
	/**
	It is a fixed value meaning all polygon.
	*/
	public static final int					ALL_POLYGON		=	7;
	/**
	The mode performed now is stored.
	*/
	private int								ActionMode		=	POINTER_MODE;
	/**
	Inner Class.
	*/
	private PartPolygon						newPolygon		=	new PartPolygon();
	private int								stepNudge		=	0;
	private int								selectedLine1;
	private int								selectedLine2;
	private int								selectedPoint1;
	private int								selectedPoint2;
	/**
	The mode performed now is stored.
	*/
	private int								functionMode	=	POINTER_MODE;
	
	// part mode
	public final static short				ONEPART			=	0;
	public final static short				TWOPART			=	1;
	private short							partMode		= ONEPART;
	
	public final static int					DELETE_STATE	=	2;
	public final static int					EDITDESCID_STATE=	4;
	public final static int					EDITOVERLAY_STATE = 5;
	public final static int					EDITPOLYGON_STATE = 3;
	public final static int					NEW_STATE		=	1;
	public final static int					ORG_STATE		=	0;
	
	public final static double 				DOT_RADIUS      =   3.5;
	
	/**
	The message outputted when loading of an image goes wrong is stored.
	*/
	private String imageLoadFailMsg = null;
	/**
	 * A newly created ImageBean object,and Initializing.
	 */
	public ImageBean() {
		super();
		initialize();
	}
	/**
	 *  A newly created ImageBean object,and Initializing.
	 * @param layout java.awt.LayoutManager
	 */
	public ImageBean(java.awt.LayoutManager layout) {
		super(layout);
	}
	/**
	 * ImageSectionPanel constructor comment.
	 * @param layout java.awt.LayoutManager
	 * @param isDoubleBuffered boolean
	 */
	public ImageBean(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}
	/**
	 *  A newly created ImageBean object,and Initializing.
	 * @param isDoubleBuffered boolean
	 */
	public ImageBean(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}
	/**
	 * This method is The section information which a classholds
	 * is updated with the received parameter.
	 * @param polygon PartPolygon
	 */
	private void addPolygon(PartPolygon polygon) {
		PartPolygon a = polygon; 
		polygons.add(a);
	}
	/**
	 * This method is based on the received parameter,
	 * a PartPolygon Object is created and the create object is
	 * passed to appPolygon method.
	 * @param point_x int[]
	 * @param point_y int[]
	 * @param info ImageSectionData
	 * @param DefectName String
	 */ 
	public void addPolygonData(int[] point_x, int[] point_y, ImageSection info, String DefectName) {
		PartPolygon p = new PartPolygon(point_x, point_y, point_x.length, info, DefectName);	
		addPolygon(p);
	}
	/**
	 * Used in Nudge mode.
	 * 
	 * @return int
	 */
	private int catchLine() {
		Polygon pa;
		double k,L;
		
		L = 3.5;
		for (int i=0; i<nudges.size(); i++) {
			Point p1;
			Point p2;
			if (i<(nudges.size()-1)) {
				p1 = scalePoint(nudges.get(i));
				p2 = scalePoint(nudges.get(i+1));
			}else {
				p1 = scalePoint(nudges.get(0));
				p2 = scalePoint(nudges.get(nudges.size()-1));
			}
			
			double k1,k2,x1,y1,x2,y2,x3,y3;
			x1 = p1.getX(); y1 = p1.getY();
			x2 = p2.getX(); y2 = p2.getY();
			x3 = 0; y3 = 0;
			k1 = Math.abs(x2-x1); k2 = Math.abs(y2-y1);
			if ((k1!=0)&&(k2!=0)) {
				k = k1/k2;
				double atan = (Math.atan(k));	
				x3 = Math.cos(atan)*L;
				y3 = Math.sin(atan)*L;
			}else if(k1==0) {
				x3 = L; y3 =0;
			}else if(k2==0) {	
				x3 = 0; y3 = L;
			}
		
			if (((x2<x1)&&(y2>y1))||((x1<x2)&&(y1>y2))) {
				int[] xp = {(int)(x1+x3),(int)(x1-x3),(int)(x2-x3),(int)(x2+x3)};
				int[] yp = {(int)(y1+y3),(int)(y1-y3),(int)(y2-y3),(int)(y2+y3)};
				pa = new Polygon(xp,yp,4);
			}else {
				int[] xp = {(int)(x1+x3),(int)(x1-x3),(int)(x2-x3),(int)(x2+x3)};
				int[] yp = {(int)(y1-y3),(int)(y1+y3),(int)(y2+y3),(int)(y2-y3)};
				pa = new Polygon(xp,yp,4);
			}
			if (pa.contains(mouseClickedX,mouseClickedY)) 
				{return i;}
		}								
		return -1;  //there are no line caught at click point
	}
	/**
	 * Used in Nudge mode.
	 * @param selectedLine int
	 * @return int
	 */
	private int catchPoint(int selectedLine) {
		int i = selectedLine;
		double L = 3.5;
		Point p1,p2;
		if (i<(nudges.size()-1)) {
			p1 = scalePoint(nudges.get(i));
			p2 = scalePoint(nudges.get(i+1));
		}else {
			p1 = scalePoint(nudges.get(i));
			p2 = scalePoint(nudges.get(0));
		} 
		if ((mouseClickedX<p1.getX()+L)&&(mouseClickedX>p1.getX()-L)&&(mouseClickedY<p1.getY()+L)&&(mouseClickedY>p1.getY()-L)){
			return i;
		}
		if ((mouseClickedX<p2.getX()+L)&&(mouseClickedX>p2.getX()-L)&&(mouseClickedY<p2.getY()+L)&&(mouseClickedY>p2.getY()-L)){
			return (i+1>nudges.size()-1?0:i+1);
		}
		return -1;
	}
	/**
	 * Current Image is clear.
	 */ 
	public void clearImage() {
		image		=	null;
		zoomScale	=	1;
		repaint();
	}
	/**
	 * This method is Delete a selected Image section.
	 */
	private void deletePolygon() {
		//find and remove current polygon in vectorPolygon
		for (PartPolygon partPolygon : polygons) {
			if (partPolygon.info.getId().equals(this.currentPolygon.info.getId())){
				int[] a = {0};
				partPolygon.xpoints = a;
				partPolygon.ypoints = a;
				partPolygon.npoints = 1;
		
				currentPolygon = new PartPolygon();	
				break;
			}	
		}
		repaint();
	}
	
	/**
	 * 
	 * @return if the polygon is closed
	 */
	private boolean drawLine() {
		
		if (lines.size()==0){
			//draw start point
			newPolygon = new PartPolygon();
			lines.add(setRealPoint(new Point(mouseClickedX,mouseClickedY)));
			repaint();
			return false;
		}	
			
		Point startPoint = scalePoint(lines.get(0));
		int sX = (int)startPoint.getX();
		int sY = (int)startPoint.getY();
	
		//check clicking on start point
		if ((mouseClickedX>(sX-DOT_RADIUS))&&(mouseClickedX<(sX+DOT_RADIUS))&&
			(mouseClickedY>(sY-DOT_RADIUS))&&(mouseClickedY<(sY+DOT_RADIUS))){		
			//draw result polygon and show dialog
			
			PartPolygon p = new PartPolygon();
			for (Point pt :lines) 
				p.addPoint((int)pt.getX(),(int)pt.getY());
			
			//set part kind flag
			//p.partKindFlag = partMode;
			
			//add to vectorPolygons
			addPolygon(p);
			//show new polygon
			newPolygon = p;
			//clear vectorLines
			lines.clear();
	
			repaint();
			//finish draw lines
			return true;
					
		} else	lines.add(setRealPoint(new Point(mouseClickedX,mouseClickedY)));
	
		repaint();
		//continuing draw lines
		return false;	
	}
	/**
	 * This method is Edit polygon.
	 */
	private boolean editPolygon() {
		stepNudge++;
		if (stepNudge==1) { // Select basement line
			selectedLine1 = catchLine();
			if (selectedLine1==-1) stepNudge--;
			
		}else if (stepNudge==2) { // Select moving line
			selectedLine2 = catchLine();
			if ((selectedLine2==-1)||(selectedLine2==selectedLine1)) stepNudge--;
			
		}else if (stepNudge==3) { // Select moving point
			selectedPoint1 = catchPoint(selectedLine2);
			if (selectedPoint1==-1) stepNudge--;
		}else if (stepNudge==4) { // Select basement point
			selectedPoint2 = catchPoint(selectedLine1);
			if ((selectedPoint2==-1)||(selectedPoint2==selectedPoint1)) {stepNudge--;
			}else {
				mergePoint();			
			}
		}else if (stepNudge==5) { // Select moving point
			selectedPoint1 = catchPoint(selectedLine2);
			if (selectedPoint1==-1) stepNudge--;
		}else if (stepNudge==6) { // Select basement point
			selectedPoint2 = catchPoint(selectedLine1);
			if (selectedPoint2==-1) {stepNudge--;
			}else {
				mergePoint();
				selectedLine1 = -1;	selectedLine2 = -1;
				stepNudge = 7;
				return	true;
			}
		}
		repaint();
		return	false;
	}
	/**
	 * A filter is covered over an image.
	 * @param op java.awt.image.BufferedImageOp
	 */
	private void filter(BufferedImageOp op) {
		image = orgImage;
		BufferedImage filteredImage = new BufferedImage((int)(image.getWidth()*zoomScale), (int)(image.getHeight()*zoomScale),image.getType());
		op.filter(image,filteredImage);
		image = filteredImage;
		repaint();
	}

	/**
	 * The function mode under present execution is returned.
	 * @return int
	 */ 
	public int getFunctionMode() {
		return functionMode;
	}
	/**
	 * This method is Getter of imageLoadFailMsg.
	 * @return java.lang.String
	 */
	public java.lang.String getImageLoadFailMsg() {
		return imageLoadFailMsg;
	}
	/**
	 * This method is return partMode.
	 * 
	 * @return short
	 */ 
	public short getPartMode() {
		return partMode;
	}
	/**
	 * This method is return Seleted polygon.
	 * 
	 * @param newPartMode int
	 */             
	
	public PartPolygon getSelectedPolygon() {
		if (currentPolygon.npoints != 0) {
			return	currentPolygon;
		} else {
			return	null;	
		}
	}
	/**
	 * This method is return value of step Nudge.
	 * 
	 * @return int
	 */
	public int getStepNudge() {
		return stepNudge;
	}
	/**
	 * This method is return value of Part Polygon.
	 * 
	 * @return int
	 */
	public PartPolygon getVectorPolygon() {
		return polygons.get(polygons.size() -1);
	}
	/**
	 * Called whenever the part throws an exception.
	 * @param exception java.lang.Throwable
	 */  
	private void handleException(java.lang.Throwable exception) {
	
		/* Uncomment the following lines to print uncaught exceptions to stdout */
		 System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		 exception.printStackTrace(System.out);
	}
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("ImageBean");
			setLayout(null);
			setSize(160, 120);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		lines = new ArrayList<Point>();
		nudges = new ArrayList<Point>();
		polygons  = new ArrayList<PartPolygon>();
		// user code end
	}
	public void insertNewPolygonData(ImageSection info) {
		int	lastOrder	=	polygons.size()-1;
		polygons.get(lastOrder).info = info;
	}
	/**
	 * It judges whether a mouse is in an image.
	 * @return boolean
	 */
	public boolean isInImage(int mouseX, int mouseY) {
		if (image != null) {
			if ((mouseX>orgX) && (mouseX<image.getWidth()+orgX) &&
				(mouseY>orgY) && (mouseY<image.getHeight()+orgY)) {
				return true;
			}else {
				return false;
			}	
		} else {
			return false;
		}
	}
	/**
	 * This method is Load Image.
	 * @param name java.lang.String
	 */
	public void loadImage(String name) throws SystemException {
	
		if(getClass().getResource(name)==null) {
			imageLoadFailMsg	=	"Image file ["+name+"] is loading failed";
			throw new SystemException("Failed in loading of image");  // @JM006
		} else {
			imageLoadFailMsg	=	null;
		}
		Image	loadedImage	=	getToolkit().createImage(getClass().getResource(name));
		MediaTracker tracker = new MediaTracker(this);
		tracker.addImage(loadedImage, 0);
		try { tracker.waitForID(0);}
		catch (InterruptedException e) {}
		
		image = new BufferedImage(loadedImage.getWidth(null),loadedImage.getHeight(null),BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		g2.drawImage(loadedImage,0,0,null);
	
		//Set origin coordinate of image to center of panel
		orgX= (getWidth()/2)-(image.getWidth(null)/2);
		orgY= (getHeight()/2)-(image.getHeight(null)/2);
	
		//set mouse clicked to center of panel
		mouseClickedX= getWidth()/2;
		mouseClickedY= getHeight()/2;
		
		//set origin Image from loaded image
		orgImage = image;	
		
		repaint();
		
	}
	
	public void loadImage(byte[] imageData) throws SystemException {
	
		Image	loadedImage	=	getToolkit().createImage(imageData);
		MediaTracker tracker = new MediaTracker(this);
		tracker.addImage(loadedImage, 0);
		try { 
			tracker.waitForID(0);
		} catch (InterruptedException e) {
		}
		
		int w = loadedImage.getWidth(null);
		int h = loadedImage.getHeight(null);
		
		image = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		g2.drawImage(loadedImage,0,0,null);
	
		//Set origin coordinate of image to center of panel
		orgX= (getWidth()/2)-(image.getWidth(null)/2);
		orgY= (getHeight()/2)-(image.getHeight(null)/2);
	
		//set mouse clicked to center of panel
		mouseClickedX= getWidth()/2;
		mouseClickedY= getHeight()/2;
		
		//set origin Image from loaded image
		orgImage = image;	
		repaint();
	}
	
	/**
	 * A point is merged.
	 */
	private void mergePoint() {
		// Change point to new value
		Point p1 = new Point(nudges.get(selectedPoint2));
		nudges.set(selectedPoint1,p1);
		
		// Update polygon
		int nx[] = new int[nudges.size()] ;	
		int ny[] = new int[nudges.size()] ;	
		for (int i=0; i<nudges.size(); i++) {
			Point pt = nudges.get(i);
			nx[i] = (int)pt.getX();
			ny[i] = (int)pt.getY();		
		}
		PartPolygon p = new PartPolygon(nx,ny,nudges.size(),currentPolygon.info,currentPolygon.defectName);
		// PartPolygon p = new PartPolygon(nx,ny,vectorNudge.size(),polygon.imageSectionID,polygon.descriptionID,polygon.partKindFlag,polygon.defectName);
		// p.imageSectionID = polygon.imageSectionID;
		
		// find current polygon in vectorPolygon
		for (int i=0; i<polygons.size(); i++) {
			PartPolygon pl = polygons.get(i);
			if (pl.info.getId()==currentPolygon.info.getId()){
				polygons.set(i,p);			
				break;
			}	
		}
		
		// reset selected points 
		selectedPoint1 = -1;
		selectedPoint2 = -1;
		
		
	}
	/**
	 * Return StartPoint of draw defectName.
	 * @param lenStr int
	 * @return Point
	 */
	private Point orgDefectName(int lenStr) {
		Point pointTmp = new Point(mouseClickedX,mouseClickedY);
		
		Rectangle boundingBox = currentPolygon.getBounds();
		
		if(mouseClickedX>=boundingBox.getCenterX()) pointTmp.x -= lenStr+3;
		if(mouseClickedY>=boundingBox.getCenterY()) pointTmp.y -=15;
		return pointTmp;
	}
	
	/**
	 * This method carries out the paint of the component.
	 * @param g java.awt.Graphics
	 */
	public void paintComponent(Graphics g) {
	
		super.paintComponent(g);
			
		drawImage(g);
	
		drawPolygons(g);
				
		if ((currentPolygon.npoints != 0)&&((ActionMode==MOVEBACK_MODE)||(ActionMode==POINTER_MODE))) 			
			drawDefectName(g);
	
		if (ActionMode==SHAPE_MODE)	drawNewPolygon(g);
	
		if (ActionMode==NUDGE_MODE) drawNudges(g);
		
	}
	
	private void drawImage(Graphics g) {
		//chack image
		if(imageLoadFailMsg!=null) {
			g.setColor(Color.black);
			g.fillRect(0,0,getWidth(),getHeight());
			g.setColor(Color.white);
			g.drawString(imageLoadFailMsg,10,getHeight()/2);
		}
		//draw image
		if (image != null) g.drawImage(image,orgX,orgY,null);	
	}
	
	private void drawDefectName(Graphics g) {
		
		if ((currentPolygon.contains(mouseClickedX,mouseClickedY))&&
			currentPolygon.isPartMode(partMode))	{
			g.setColor(Color.blue);
			g.drawPolygon(currentPolygon);

			//draw defect name of part
			//polygon.defectName = "testStringABC123";
			if (currentPolygon.defectName!=null) {
				Font f = Fonts.DIALOG_PLAIN_14;
				FontMetrics fm = g.getFontMetrics(f);
				g.setFont(f);

				//calculate coordinate of defect name string
				Point orDF = orgDefectName(fm.stringWidth(currentPolygon.defectName));
	
				//draw background
				g.setColor(Color.yellow);
				g.fillRect(orDF.x,orDF.y,fm.stringWidth(currentPolygon.defectName)+4,15 );
				//draw string
				g.setXORMode(Color.black);
				g.drawString(currentPolygon.defectName,orDF.x + 2,orDF.y + 13);
			}	
		}	
	}
	
	private void drawPolygons(Graphics g) {
		g.setColor(Color.green);
		for(PartPolygon item : polygons){
			PartPolygon pp=scalePolygon(item);
			if(pp.isPartMode(partMode))
					g.drawPolygon(pp);
		}
	}
	
	private void drawNewPolygon(Graphics g) {
		drawLines(lines,false,Color.blue,g);
		//draw new polygon
		if (newPolygon.npoints!=0) 
			g.drawPolygon(scalePolygon(newPolygon));
	}
	
	private void drawLines(List<Point> points,boolean isClosed, Color color,Graphics g) {
		if(points.isEmpty()) return;
		g.setColor(color);
		
		for (int i=0; i<points.size(); i++) {			
			Point p1 = scalePoint(points.get(i));
			fillOval(p1, g);
			Point p2;
			if(i == points.size() -1) {
				if(!isClosed)break;
				p2 = scalePoint(points.get(0));
			}else  p2 = scalePoint(points.get(i+1));
			g.drawLine((int)p1.getX(),(int)p1.getY(),(int)p2.getX(),(int)p2.getY());
		}
	}
	
	
	private void drawNudges(Graphics g) {
			drawLines(nudges,true,Color.red,g);
			drawSelectedNudges(g);
	}
	
	private void drawSelectedNudges(Graphics g) {
		
		Point p1,p2;
	 	
		g.setColor(Color.green);			
	 	if (selectedLine1 != -1){				 	
			p1 = scalePoint(nudges.get(selectedLine1));
			p2 = scalePoint(nudges.get(selectedLine1+1==nudges.size()?0:selectedLine1+1));
			g.drawLine((int)p1.getX(),(int)p1.getY(),(int)p2.getX(),(int)p2.getY());
			fillOval(p1,g);
		}
	 	if (selectedLine2 != -1){			
			p1 = scalePoint(nudges.get(selectedLine2));
			p2 = scalePoint(nudges.get(selectedLine2+1==nudges.size()?0:selectedLine2+1));
			g.drawLine((int)p1.getX(),(int)p1.getY(),(int)p2.getX(),(int)p2.getY());
			fillOval(p1,g);
		}

	}
	
	private void fillOval(Point p, Graphics g) {
		g.fillOval((int)(p.getX()-DOT_RADIUS),(int)(p.getY()-DOT_RADIUS),(int)(DOT_RADIUS * 2),(int)(DOT_RADIUS *2));
	}
	
	/**
	 * The last line is erased.
	 */
	private void removeLastLine() {
		if (lines.size()!=0) {
			lines.remove(lines.size()-1);
		}	
		repaint();
	}
	/**
	 * This method is deletePolygon method is Called.
	 */ 
	public void runDEL() {
		deletePolygon();
	}
	/**
	 * This method is setNudgeMode method is Called.
	 */ 
	public void runESCNudge() {
		setNudgeMode();
	}
	/**
	 * Called removeLastLine. 
	 */ 
	public void runESCShape() {
		removeLastLine();
	}
	/**
	 * This method is move back processing is performed.
	 */ 
	public boolean runMoveBack() {
		int k=0,p1=0;
		
		if (ActionMode!=MOVEBACK_MODE) return false;
		
		for (int i=0; i<polygons.size(); i++) {
			Polygon p = scalePolygon(polygons.get(i));
			if (p.contains(mouseClickedX,mouseClickedY)){
				k++;				
				if (k==1){
					p1 = i;
				}	
			}
		}
		
		//if there are no intersected polygons 	
		if (k==1) {
			showPolygon();
			return false;
		}	 
	
		/*there are at least two polygon
		move selected polygon to last position, push others up 1 step*/
		PartPolygon s1 = polygons.get(p1);
		for (int i=p1; i<polygons.size()-1; i++) {
			PartPolygon s2 = polygons.get(i+1);	
			polygons.set(i,s2);
		}
			
		polygons.set(polygons.size()-1,s1);
	
		//refresh screen	
		showPolygon();
		return true;
	}
	/**
	 * This method is editPolygon method is Called.
	 */ 
	public boolean runNudge() {
		return	editPolygon();
	}
	/**
	 * This method is PAN processing is performed.
	 */ 
	public void runPan() {	
		//set polygon return original point
		currentPolygon.translate(-orgX,-orgY);
		//set coordinate of clicked mouse point to center of panel
		orgX = orgX +((getWidth()/2)-mouseClickedX);
		orgY = orgY +((getHeight()/2)-mouseClickedY);
		//set polygon to new point
		currentPolygon.translate(orgX,orgY);
		//set mouse clicked to center of panel
		mouseClickedX= getWidth()/2;
		mouseClickedY= getHeight()/2;
		
		repaint();
	}
	/**
	 * This method is Restore processing is performed.
	 */ 
	public void runRestore() {
		image = orgImage;
		//polygon = orgPolygon;
	
		//set zoom scale to 1
		zoomScale = 1;
		
		//set mouse clicked to center of panel
		mouseClickedX= getWidth()/2;
		mouseClickedY= getHeight()/2;
		
		//set coordinate of origin center to center of panel
		int h = (int)(image.getHeight(null)*zoomScale);
		int w = (int)(image.getWidth(null)*zoomScale);
		orgX= (getWidth()/2)-(w/2);
		orgY= (getHeight()/2)-(h/2);
		
		mouseClickedX	=	0;
		mouseClickedY	=	0;
	
		currentPolygon.translate(orgX,orgY);	
		repaint();
	}
	/**
	 * Called drawLine. 
	 */ 
	public boolean runShape() {
		return drawLine();
	}
	/**
	 * This method is Zoom processing is performed.
	 */
	public void runZoom() {
		//set zoom scale max 300%
		if ((zoomScale<3)==true) {
			float oldZoomScale=zoomScale;
			zoomScale+=0.5;
			//set polygon return original point
			currentPolygon.translate(-orgX,-orgY);
			
			//set coordinate of clicked mouse point to center of panel
			int a = (int)(((zoomScale/oldZoomScale)-1)*(mouseClickedX-orgX));
			int b = (int)(((zoomScale/oldZoomScale)-1)*(mouseClickedY-orgY));
			orgX = orgX +((getWidth()/2)-mouseClickedX) - a;
			orgY = orgY +((getHeight()/2)-mouseClickedY) - b;
	
			//zoom image
			AffineTransform transform = AffineTransform.getScaleInstance(zoomScale,zoomScale);
			AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
	
			//zoom polygon		
			int newxpoints[]= new int[currentPolygon.npoints];
			int newypoints[]= new int[currentPolygon.npoints];
			for (int i=0; i<currentPolygon.npoints; i++) {
				newxpoints[i] = (int)(currentPolygon.xpoints[i]*zoomScale/oldZoomScale);
				newypoints[i] = (int)(currentPolygon.ypoints[i]*zoomScale/oldZoomScale);		
			}	
			PartPolygon ptmp = new PartPolygon(newxpoints, newypoints, currentPolygon.npoints,currentPolygon.info,currentPolygon.defectName);
			currentPolygon = ptmp;
			
			//set polygon to new point
			currentPolygon.translate(orgX,orgY);
			
			//set mouse clicked to center of panel
			mouseClickedX= getWidth()/2;
			mouseClickedY= getHeight()/2;
		
			filter(op);
		}	
	}								/**
	 * Insert the method's description here.
	 * 
	 * @return int
	 */ 
	/**
	 * This method the received point is changed and returned in consideration of
	 * magnification and the starting point.
	 * @return java.awt.Point
	 * @param point java.awt.Point
	 */
	private Point scalePoint(Point point) {
		Point newpoint = new Point((int)(point.getX()*zoomScale),(int)(point.getY()*zoomScale));
		newpoint.translate(orgX,orgY);	
		return newpoint;
	}
	/**
	 * The scale of a polygon is adjusted.
	 * @return com.honda.global.galc.client.qics.data.PartPolygon
	 */ 
	private PartPolygon scalePolygon(PartPolygon newpolygon) {
	
		int newxpoints[]= new int[newpolygon.npoints];
		int newypoints[]= new int[newpolygon.npoints];
		for (int i=0; i<newpolygon.npoints; i++) {
			newxpoints[i] = (int)(newpolygon.xpoints[i]*zoomScale);
			newypoints[i] = (int)(newpolygon.ypoints[i]*zoomScale);		
		}
		PartPolygon p = new PartPolygon(newxpoints, newypoints, newpolygon.npoints,newpolygon.info,newpolygon.defectName);
		//PartPolygon p = new PartPolygon(newxpoints, newypoints, newpolygon.npoints,newpolygon.imageSectionID,newpolygon.descriptionID,newpolygon.partKindFlag,newpolygon.defectName);
		//p.imageSectionID =newpolygon.imageSectionID ;
		p.translate(orgX,orgY);
		
		return p;
	}
	/**
	 * The scale of a polygon is vectorPolygon.
	 * @return java.awt.Polygon
	 * @param newPolygon java.awt.Polygon
	 */
	public void setDataPolygons(List<PartPolygon> newVPolygon) {
		polygons = newVPolygon;	
	}
	/**
	 * This method is setter of FunctionMode.
	 * @param newImageLoadFaileMsg java.lang.String
	 */
	public void setFunctionMode(int newFunctionMode) {
		functionMode = newFunctionMode;
		ActionMode = functionMode;
		if( functionMode == ImageBean.NUDGE_MODE) {
			setNudgeMode();
		} else if( functionMode == ImageBean.POINTER_MODE) {
			if (lines != null ) lines.clear();
			if (nudges != null ) nudges.clear();
			currentPolygon= new PartPolygon();
			repaint();
		}		
	}
	/**
	 * This method is setter of imageLoadFailMsg.
	 * @param newImageLoadFaileMsg java.lang.String
	 */
	public void setImageLoadFailMsg(java.lang.String newImageLoadFailMsg) {
		imageLoadFailMsg = newImageLoadFailMsg;
	}
	/**
	 * This method is set mouseClickPoint to Class variable.
	 * @param mouseX int
	 * @param mouseY int
	 */
	public void setMouseClicked(int mouseX, int mouseY) {	
		mouseClickedX=mouseX;
		mouseClickedY=mouseY;
	}
	/**
	 * It judges where the point of a mouse is from the passed point.
	 * The image of cursor is changed by the result.
	 * @param mouseX int
	 * @param mouseY int
	 */
	public void setMouseCursorMove(int mouseX, int mouseY) {
	
		if (isInImage(mouseX, mouseY)){
			setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
		}else {
			setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		}		
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return - true - a polygon is completed
	 */
	public boolean processMouseClicked(int x, int y) {
		
		setMouseClicked(x, y);
		if (!isInImage(x, y)) return false;

		if (getFunctionMode() == POINTER_MODE)
			showPolygonIterate();
		else if (getFunctionMode() == SHAPE_MODE) {
			if (runShape()) {
				setFunctionMode(ImageBean.POINTER_MODE);
				return true;
			}
		}
		return false;
			
	}
	/**
	 * Set Nudge mode to this Object.
	 */
	private void setNudgeMode() {
		nudges = new ArrayList<Point>();
		selectedLine1 = -1;
		selectedLine2 = -1;
		selectedPoint1 = -1;
		selectedPoint2 = -1;
		stepNudge = 0;
		for (int i=0; i<currentPolygon.npoints; i++) {
			Point pt = setRealPoint(new Point((int)(currentPolygon.xpoints[i]),(int)(currentPolygon.ypoints[i])));
			nudges.add(pt);
		}	
		repaint();
		return;
	}
	/**
	 * Set Part Mode to this Object.
	 * @return java.awt.Point
	 * @param point java.awt.Point
	 */
	public void setPartMode(short newPartMode) {
		partMode = newPartMode;
	}
	/**
	 * Set Real Point to this Object.
	 * @return java.awt.Point
	 * @param point java.awt.Point
	 */
	public Point setRealPoint(Point point) {
		Point newpoint = new Point(point);
		newpoint.translate(-orgX,-orgY);
		return new Point((int)(newpoint.getX()/zoomScale),
						 (int)(newpoint.getY()/zoomScale));
	}
	/**
	 * This method is show polygon.
	 */
	public void showPolygon() {
		int k = 0;
		currentPolygon = new PartPolygon();	
		for (PartPolygon p : polygons) {		 
			//PartPolygon s = (PartPolygon)vectorPolygon.elementAt(i);
			if(p.info!=null) {
				if ((p.contains(mouseClickedX,mouseClickedY))
				&&	(p.info.getPartKindFlagValue()==partMode)){
					k++;							
					if (k==1) {
						this.currentPolygon = p;
						//polygon.defectName = s.defectName;
						break;	
					}	
				}
			}
		}
		repaint();
		return;	
	}
	
	public void showPolygonIterate() {
		currentPolygon = new PartPolygon();
		int size = polygons.size();
		for (int i=0; i<size; i++) {
			if (polygonIx > (size - 1)) {
				polygonIx = 0;
			}
			PartPolygon p = scalePolygon(polygons.get(polygonIx));
			polygonIx++;
			if(p.info!=null) {
				if (p.contains(mouseClickedX,mouseClickedY) && p.isPartMode(partMode)){
						this.currentPolygon = p;
						break;	
				}
			}
		}
		repaint();
		return;	
	}
	
	/**
	 * This method is show polygon.
	 */
	public void showPolygon(int imageSectionID) {
		int k = 0;
		currentPolygon = new PartPolygon();	
		for (int i=0; i<polygons.size(); i++) {		 
			PartPolygon p = scalePolygon(polygons.get(i));
			
			if ((p.info.getId()==imageSectionID)&&
				(p.info.getPartKindFlagValue()==partMode)){
				k++;							
				if (k==1) {
					currentPolygon = p;	
					break;	
				}	
			}			
		}
		if(currentPolygon.contains(currentPolygon.xpoints[0]+1,currentPolygon.ypoints[0]+1)) {
			setMouseClicked(currentPolygon.xpoints[0]+1,currentPolygon.ypoints[0]+1);
		} else if(currentPolygon.contains(currentPolygon.xpoints[0]+1,currentPolygon.ypoints[0]-1)) {
			setMouseClicked(currentPolygon.xpoints[0]+1,currentPolygon.ypoints[0]-1);
		} else if(currentPolygon.contains(currentPolygon.xpoints[0]-1,currentPolygon.ypoints[0]+1)) {
			setMouseClicked(currentPolygon.xpoints[0]-1,currentPolygon.ypoints[0]+1);
		} else if(currentPolygon.contains(currentPolygon.xpoints[0]-1,currentPolygon.ypoints[0]-1)) {
			setMouseClicked(currentPolygon.xpoints[0]-1,currentPolygon.ypoints[0]-1);
		}
		repaint();
		return;	
	}
	/**
	 * Description ID is Update.
	 * @param imageSectionID int
	 * @param newDescriptionID int
	 */ 
	public void updateDescriptionID(int imageSectionID, int newDescriptionID) {
		currentPolygon = new PartPolygon();	
		for (PartPolygon p : polygons) {		 
			if (p.info.getId()==imageSectionID){
				p.info.setDescriptionId( newDescriptionID);
				currentPolygon = p;
				currentPolygon.info.setDescriptionId(newDescriptionID);
				break;	
			}				
		}
		repaint();
		return;	
	}
	
	/**
	It is the inner class which stores the state of a polygon.
	*/
	public class PartPolygon extends java.awt.Polygon {
		private static final long serialVersionUID = 1L;
		public java.lang.String defectName;
		public ImageSection info;
		
		public PartPolygon() {
		}
		
		public PartPolygon(int[] xpoints, int[] ypoints, int npoints, int imageSectionID, int descriptionID, short partKindFlag, String defectName) {
			super(xpoints, ypoints, npoints);
		}
		public PartPolygon(int[] xpoints, int[] ypoints, int npoints, ImageSection info, String defectName) {
			super(xpoints, ypoints, npoints);
			this.info = info;
			this.defectName = defectName;
		}
		
		public boolean isPartMode(short partMode) {
			return info!= null && info.getPartKindFlagValue() == partMode;
		}
			
	}

}
