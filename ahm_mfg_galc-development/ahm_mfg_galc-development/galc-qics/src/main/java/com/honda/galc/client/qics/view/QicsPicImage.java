package com.honda.galc.client.qics.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JPanel;

import com.honda.galc.client.qics.data.XPolygon;
import com.honda.galc.client.qics.property.DefaultQicsPicturePropertyBean;
import com.honda.galc.client.qics.view.screen.DefectPictureInputPanel;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.entity.qics.DefectResultId;
import com.honda.galc.entity.qics.ImageSection;
import com.honda.galc.entity.qics.ImageSectionPoint;
import com.honda.galc.service.property.PropertyService;
/**
 * <h3>Class description</h3>
 *  <h4>Description</h4>
 *  This Class is a defect input screen from picture of QICS.and is a class for controlling an image.
 *  <P>
 *  <h4>Change History</h4>
 *  <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 *  <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 *  <TH>Updated by</TH>
 *  <TH>Update date</TH>
 *  <TH>Version</TH>
 *  <TH>Mark of Update</TH>
 *  <TH>Reason</TH>
 *  </TR>
 * <TR>
 * <TD>A.Fannin</TD>
 * <TD>2004/03/25</TD>
 * <TD>0.2.0</TD>
 * <TD>@OIM6</TD>
 * <TD>PropertyServices migration</TD>
 * </TR>
 *  <TR>
 *  <TD>K.Ishibe</TD>
 *  <TD>(2001/03/18 20:00:00)</TD>
 *  <TD>0.1</TD><TD>(none)</TD>
 *  <TD>Initial Release</TD>
 *  </TR>
 *  </TABLE>
 *  @see 
 *  @ver 0.1
 *  @author K.Ishibe
 */
public	class QicsPicImage extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	The image object which draws on a panel.
	*/
	private Image				image					=	null;
	
	private com.honda.galc.entity.qics.Image imageEntity;
	
	private String defectTypeName;
	
	private List<DefectDescription> defectDescriptions;
	
	private DefaultQicsPicturePropertyBean qicsPicturePropertyBean;
	
	/**
	Used in case an image is loaded.
	*/
	private MediaTracker		mediaTracker			=	null;
	/**
	The relative path name of an image file.
	*/
	private String				imgFile					=	"";
	/**
	When an image file is not found, it is set up true.
	*/
	private boolean				imageFileNotFound		=	false;
	/**
	When drawing to begin is performed, it is set up true.
	*/
	private boolean				firstPaint				=	false;

	/**
	It has the image section drawn as Vector.
	*/
	private List<XPolygon>				polygons				=	new	ArrayList<XPolygon>();
	/**
	class RepairResultData into Vector.
	*/
	private List<DefectResult>				repairResultList		=	new	ArrayList<DefectResult>();
	/**
	only one RepairResultData.
	*/
	private DefectResult	singleDefectResultData	=	null;
	/**
	A Event Handler class.
	*/
	IvjEventHandler				ivjEventHandler			=	new IvjEventHandler();
	/**
	A cursor image when a mouse goes into an image section is stored.
	*/
	private java.awt.Image		cursorImage1			=	null;
	/**
	A cursor image when a mouse comes out of an image section is stored.
	*/
	private java.awt.Image		cursorImage2			=	null;
	/**
	A mouse hot spot when a mouse goes into an image section is stored.
	*/
	/**
	A Cursor Object when a mouse goes into an image section is stored.
	*/
	private java.awt.Cursor		outCursor				=	null;
	/**
	A Cursor Object when a mouse comes out of an image section is stored.
	*/
	private java.awt.Cursor 	inCursor				=	null;
	/**
	It is a flag for judging whether an image section is drawn.
	*/
	public	boolean				dispPolygonStatus		=	true;
	/**
	The font type of X marks when clicking is stored.
	*/
	private Font				XPointFont				=	null;
	/**
	The font type of Text of Defect Name when clicking is stored.
	*/
	private Font				textFont				=	null;
	/**
	A center point is stored.
	*/
	private Point				center					=	null;

	/**
	Defect status is stored.
	*/
	private int					defectStatus 			= 	0;
	/**
	A fixed value of X marks.
	*/
	private static final int	XPOINT 					= 	0;
	/**
	A fixed value of Defect name.
	*/
	private static final int	TEXT					= 	1;
	/**
	A fixed value of border.
	*/
	private static final int	HEMMING					= 	2;
	/**
	The now present image name is stored.
	*/
	private java.lang.String	nowImage				= 	null;
	
	/**
	For calculating the size the starting point which draws font.
	*/
	private FontMetrics			fm						=	null;
	

class IvjEventHandler implements java.awt.event.MouseListener, java.awt.event.MouseMotionListener {
		public void mouseClicked(java.awt.event.MouseEvent e) {
			if (e.getSource() == QicsPicImage.this) 
				handleMousePressed(e);
		};
		public void mouseDragged(java.awt.event.MouseEvent e) {};
		public void mouseEntered(java.awt.event.MouseEvent e) {};
		public void mouseExited(java.awt.event.MouseEvent e) {};
		public void mouseMoved(java.awt.event.MouseEvent e) {
			handleMouseMoved(e);
		};
		public void mousePressed(java.awt.event.MouseEvent e) {};
		public void mouseReleased(java.awt.event.MouseEvent e) {};
	};
/**
 * QicsPicImage constructor comment.
 */
public QicsPicImage() {
	super();
	initialize();
}
/**
 * It adjusts so that the character sequence which draws may not overflow a screen.
 * @return java.awt.Point 	Adusted point
 * @param param java.awt.Point Mouse click point
 * @param defectName java.lang.String Defect name
 */
private Point calcDrawPoint(Point clickPoint, String polygonId) {

	// working storage define.
	Point	drawPoint	=	new	Point();
		
	// Set Draw Point.
	if( (clickPoint.x <= center.x) && (clickPoint.y <= center.y) ) {
		drawPoint.x	=	clickPoint.x;
		drawPoint.y	=	clickPoint.y	+	20;		
	} else if( (clickPoint.x >= center.x) && (clickPoint.y <= center.y) ) {
		drawPoint.x	=	clickPoint.x	-	fm.stringWidth(polygonId);
		drawPoint.y	=	clickPoint.y	+	20;		
	} else if( (clickPoint.x <= center.x) && (clickPoint.y >= center.y) ) {
		drawPoint.x	=	clickPoint.x;
		drawPoint.y	=	clickPoint.y	-	13;		
	} else if( (clickPoint.x >= center.x) && (clickPoint.y >= center.y) ) {
		drawPoint.x	=	clickPoint.x	-	fm.stringWidth(polygonId);
		drawPoint.y	=	clickPoint.y	-	13;		
	}	
	return drawPoint;
}
/**
 * The state of Outstanding, Repair, and a Scrap is judged and a X point a defect and the color of a border are returned.
 * @return java.awt.Color Color for defect
 * @param param int Defect status
 */
public Color checkColor(int param) {
	if(param == XPOINT) {
		switch (defectStatus) {
			case 1 : {	return	qicsPicturePropertyBean.getXPointColorRepair();		} 
			case 3 : {	return	qicsPicturePropertyBean.getDefectColorScrap();		} 
			default: {	return	qicsPicturePropertyBean.getXPointColorOS();			}
		}
	} else if(param == TEXT) {
		switch (defectStatus) {
			case 1 : {	return	qicsPicturePropertyBean.getDefectColorRepair();	} 
			case 3 : {	return	qicsPicturePropertyBean.getDefectColorScrap();	} 
			default: {	return	qicsPicturePropertyBean.getDefectColorOS();		}
		}
	} else if(param == HEMMING) {
		switch (defectStatus) {
			case 1 : {	return	qicsPicturePropertyBean.getHemmingColorRepair();		} 
			case 3 : {	return	qicsPicturePropertyBean.getHemmingColorScrap();			} 
			default: {	return	qicsPicturePropertyBean.getHemmingColorOS();			}
		}
	}
	return	qicsPicturePropertyBean.getXPointColorOS();
}
/**
 * This method is from the received mouse event, a click point is takenout.
 * it judges the inside of an image section, or outside, and if it is inside,
 * the method which draws X points will be called.
 * @param e MouseEvent Mouse event
 */
private void checkPolygon(MouseEvent e) {

	XPolygon	polygon	=	null;
	Point	point	=	new	Point(e.getX(),e.getY());

 if (getParent() instanceof DefectPictureInputPanel) {
		//kw - hcm
		this.defectStatus = ((DefectPictureInputPanel)getParent()).getDefectStatusPanel().getSelectedStatus();
		polygon = selectCoveringPolygon(point);
		if (polygon != null) {
			drawXPoint(point,polygon,getGraphics());
		} else {
			singleDefectResultData = null;
		}
	}
}

protected XPolygon selectCoveringPolygon(Point	point) {
	XPolygon coveringPolygon = null;
	List<XPolygon> coveringPolygons = new ArrayList<XPolygon>();
	
	for(XPolygon p : polygons) {
		if(p.contains(point)) {
			coveringPolygons.add(p);
		} 
	}
	if (coveringPolygons.size() == 0) {
		return coveringPolygon;
	}
	if (coveringPolygons.size() == 1) {
		return coveringPolygons.get(0);
	}
	Comparator<Polygon> comparator = new Comparator<Polygon>() {
		public int compare(Polygon o1, Polygon o2) {
			Double a1 = o1.getBounds2D().getHeight() * o1.getBounds2D().getWidth();
			Double a2 = o2.getBounds2D().getHeight() * o2.getBounds2D().getWidth();
			return a1.compareTo(a2);
		}
	};
	Collections.sort(coveringPolygons, comparator);
	coveringPolygon = coveringPolygons.get(0);
	return coveringPolygon;
}

/**
 * This method is an image is eliminated.
 */
public void clearImage() {
	this.imgFile	=	"";	
	repaint();
}
/**
 * This method an image section on display is cleared.
 */
public void clearPolygon() {
	this.polygons.clear();
	this.singleDefectResultData	=	null;
	this.defectDescriptions = null;
	repaint();
}

/**
 * The method draws image section and saves on memory.
 * @param g Graphics Graphics context
 * @param point Vector Selected points
 * @param parts String Defect names
 */
private void drawPolygon(Graphics g, ImageSection	imageSection,DefectDescription defectDescription) {

	XPolygon	polygon	=	new	XPolygon();	
	// Save Polygon.
	polygon.setPolygonId(defectTypeName);
	polygon.setImageSection(imageSection);
	polygon.setDefectDescription(defectDescription);
	polygons.add(polygon);
	
	g.setColor(Color.blue);
	for(ImageSectionPoint p : imageSection.getImageSectionPoints()) {
		polygon.addPoint(p.getPointX(),p.getPointY());
	}
	polygon.translate(getOrgX(), getOrgY());
	if(dispPolygonStatus) g.drawPolygon(polygon);
}

/**
 * This mathod draws a part name and X point based on the clicked point.
 * @param clickPoint Point Mouse click point
 * @param polygon XPolygon Image section
 * @param g Graphics Graphics context
 */
private void drawXPoint(Point clickPoint,XPolygon polygon, Graphics g) {

	// Working Strage Define.
	Point	drawPoint	=	null;
		
	// Set Font Type.
	g.setFont(XPointFont);
	g.setColor(checkColor(XPOINT));

	// Draw X Point.
	//@SR3739 - JJ
	//modified to display correctly
	//@WAS51 - JJ
	//different JDK version, use different coordinates adjustment
	String jdkVersion = (System.getProperty("java.version")).trim();
	if ((jdkVersion.substring(0, 3)).equals("1.3")) {
		g.drawString("X",clickPoint.x-5,clickPoint.y-12);
	} else {
		g.drawString("X",clickPoint.x-5,clickPoint.y+7);
	}
	
	// Set Font Type.
	g.setFont(textFont);
	
	// Set Draw Point.
	//@SR3739 - JJ
	//modified to display correctly
	Point modifiedClickPoint = null;
	if ((jdkVersion.substring(0, 3)).equals("1.3")) {
		modifiedClickPoint = new Point(clickPoint.x, clickPoint.y - 13);
	} else {
		modifiedClickPoint = new Point(clickPoint.x, clickPoint.y);
	}
	drawPoint	=	calcDrawPoint(modifiedClickPoint, polygon.getPolygonId());
	// Draw hemming.
	if(qicsPicturePropertyBean.isHemmingDraw()) {
		g.setColor(checkColor(HEMMING));
		g.drawString(polygon.getPolygonId(),drawPoint.x-1,drawPoint.y-1);
		g.drawString(polygon.getPolygonId(),drawPoint.x+1,drawPoint.y+1);
		g.drawString(polygon.getPolygonId(),drawPoint.x-1,drawPoint.y+1);
		g.drawString(polygon.getPolygonId(),drawPoint.x+1,drawPoint.y-1);
		g.drawString(polygon.getPolygonId(),drawPoint.x+1,drawPoint.y);
		g.drawString(polygon.getPolygonId(),drawPoint.x-1,drawPoint.y);
		g.drawString(polygon.getPolygonId(),drawPoint.x,drawPoint.y+1);
		g.drawString(polygon.getPolygonId(),drawPoint.x,drawPoint.y-1);
	}
	// Draw String.
	g.setColor(checkColor(TEXT));
	g.drawString(polygon.getPolygonId(),drawPoint.x,drawPoint.y);

	singleDefectResultData = createDefectResultData(polygon, clickPoint);
	
}

protected DefectResult createDefectResultData(XPolygon polygon, Point clickPoint) {
	
	DefectDescription defectDescription = polygon.getDefectDescription();
	
	DefectResult repairResultData	=	new	DefectResult();
	DefectResultId id = new DefectResultId();
	repairResultData.setId(id);
	
	id.setInspectionPartName(defectDescription.getInspectionPartName());
	id.setInspectionPartLocationName(defectDescription.getId().getInspectionPartLocationName());
	id.setTwoPartPairPart(defectDescription.getId().getTwoPartPairPart());
	id.setTwoPartPairLocation(defectDescription.getId().getTwoPartPairLocation());
	id.setDefectTypeName(defectDescription.getDefectTypeName());
	id.setSecondaryPartName(defectDescription.getSecondaryPartName());
	
	repairResultData.setImageName(nowImage);
	repairResultData.setPointX(clickPoint.x);
	repairResultData.setPointY(clickPoint.y);
	repairResultData.setTwoPartDefectFlag(polygon.getPart_Kind_Flag());
	repairResultData.setPartGroupName(defectDescription.getPartGroupName());


	repairResultData.setIqsCategoryName( defectDescription.getIqsCategoryName());//iqsCategory;
	repairResultData.setIqsItemName(defectDescription.getIqsItemName());//iqsItem;
    repairResultData.setEngineFiring(defectDescription.getEngineFiringFlag()); // engine firing
	repairResultData.setRegressionCode(defectDescription.getRegressionCode());//regressionCode;
	repairResultData.setResponsibleDept(defectDescription.getResponsibleDept());//responsibleDept;
	repairResultData.setResponsibleLine(defectDescription.getResponsibleLine());//responsibleLine;
	repairResultData.setResponsibleZone(defectDescription.getResponsibleZone());//responsibleZone;
	
	
	return repairResultData;
}

/**
 * This mathod draws a part name and X point based on the clicked point.
 * @param clickPoint Point Mouse click point
 * @param polygonId String Image section ID
 * @param g Graphics Graphics context
 */
private void drawXPoint(Point clickPoint,String polygonId,Graphics g) {

	// Working Strage Define.
	Point	drawPoint	=	null;
	
	// Set Font Type.
	g.setFont(XPointFont);
	g.setColor(checkColor(XPOINT));

	// Draw X Point.
	g.drawString("X",clickPoint.x-5,clickPoint.y+7);

	// Set Font Type.
	g.setFont(textFont);
	
	// Set Draw Point.
	drawPoint	=	calcDrawPoint(clickPoint,polygonId);

	// Draw hemming.
	if(qicsPicturePropertyBean.isHemmingDraw()) {
		g.setColor(checkColor(HEMMING));
		g.drawString(polygonId,drawPoint.x-1,drawPoint.y-1);
		g.drawString(polygonId,drawPoint.x+1,drawPoint.y+1);
		g.drawString(polygonId,drawPoint.x-1,drawPoint.y+1);
		g.drawString(polygonId,drawPoint.x+1,drawPoint.y-1);
		g.drawString(polygonId,drawPoint.x+1,drawPoint.y);
		g.drawString(polygonId,drawPoint.x-1,drawPoint.y);
		g.drawString(polygonId,drawPoint.x,drawPoint.y+1);
		g.drawString(polygonId,drawPoint.x,drawPoint.y-1);
	}
	// Draw String.
	g.setColor(checkColor(TEXT));
	g.drawString(polygonId,drawPoint.x,drawPoint.y);
}
/**
 * This method loads an image.
 */
private void firstPaint() {

	if(imageFileNotFound)	return;
	// get image.
	try {
		
		if (imageEntity != null && imageEntity.getImageData() != null) {
			image = getToolkit().createImage(imageEntity.getImageData());
		} else {
			image			=	getToolkit().createImage(getClass().getResource(imgFile));
		}
	} catch(Throwable e) {
		System.out.println("Image Load Error");
	}
	mediaTracker	=	new	MediaTracker(this);
	mediaTracker.addImage(image,0);
	// It waits until an inage is loaded completely.
	try{
		mediaTracker.waitForID(0);
	} catch(InterruptedException e) {
		return; 
	} 
	if(image.getWidth(this) < 0) {
	} else {
		imageFileNotFound	=	false;
		firstPaint			=	true;
	}
}
/**
 * The present defect status is returned.
 * @return int Defect status
 */
public int getDefectStatus() {
	return defectStatus;
}
/**
 * The name of an image on display is returned.
 * @return java.lang.String Image name
 */
public java.lang.String getNowImage() {
	return nowImage;
}
/**
 * This method returns Active RepairResultData.
 * @return com.honda.global.galc.client.qics.data.RepairResultData Current repair result data
 */
public DefectResult getDefectResultData() {
	return singleDefectResultData;
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
/**
 * Initializes connections
 * @exception java.lang.Exception The exception description.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	this.addMouseListener(ivjEventHandler);
	this.addMouseMotionListener(ivjEventHandler);
}
/**
 * Initialize the class.
 * A Properties file is acquired by this method.
 * The example of a setting of QicsPicture.properties is described how
 * <br>
 * <br> # Picture defect entry properties
 * <br> # PLAIN:0 BOLD:1 ITALIC:2
 * <br> X_POINT_FONT_STYLE = 0
 * <br> X_POINT_FONT_SIZE  = 18
 * <br> DEFECT_FONT_STYLE  = 1
 * <br> DEFECT_FONT_SIZE   = 10
 * <br> 
 * <br> # RGB        R   G   B
 * <br> # white     255,255,255
 * <br> # lightGray 192,192,192
 * <br> # gray      128,128,128
 * <br> # darkGray  064,064,064
 * <br> # black     000,000,000
 * <br> # red       255,000,000
 * <br> # pink      255,175,175
 * <br> # orange    255,200,000
 * <br> # yellow    255,255,000
 * <br> # green     000,255,000
 * <br> # magenta   255,000,255
 * <br> # cyan      000,255,255
 * <br> # blue      000,000,255
 * <br> # Coution Please Padding Spaces!
 * <br> 
 * <br> X_POINT_COLOR_OS          = 200,000,000
 * <br> X_POINT_COLOR_REPAIR      = 000,100,000
 * <br> X_POINT_COLOR_SCRAP       = 000,000,100
 * <br> 
 * <br> DEFECT_COLOR_OS           = 200,000,000
 * <br> DEFECT_COLOR_REPAIR       = 000,100,000
 * <br> DEFECT_COLOR_SCRAP        = 000,000,100
 * <br> 
 * <br> HEMMING_COLOR_OS          = 255,150,150
 * <br> HEMMING_COLOR_REPAIR      = 050,200,050
 * <br> HEMMING_COLOR_SCRAP       = 150,150,255
 * <br> 
 * <br> HEMMING_DRAW              = true
 * <br> 
 * <br> PLUS_CURSOR_NAME  = /resource/com/honda/global/galc/client/qics/view/plus.gif
 * <br> MINUS_CURSOR_NAME = /resource/com/honda/global/galc/client/qics/view/minus.gif
 * <br> 
 * <br> P_CURSOR_HOT_SPOT_X = 2
 * <br> P_CURSOR_HOT_SPOT_Y = 2
 * <br> 
 * <br> M_CURSOR_HOT_SPOT_X = 2
 * <br> M_CURSOR_HOT_SPOT_Y = 2
 * <br> 
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("QicsPicImage");
		setLayout(null);
		setSize(500, 530);
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	
	qicsPicturePropertyBean = PropertyService.getPropertyBean(DefaultQicsPicturePropertyBean.class);
	
	// 
	String	plusCursorImageFile		=	qicsPicturePropertyBean.getPlusCursorURL();
	String	minusCursorImageFile	=	qicsPicturePropertyBean.getMinusCursorURL();

	// Set Font.
	XPointFont	=	new	Font("Century",qicsPicturePropertyBean.getXPointFontStyle(),qicsPicturePropertyBean.getXPointFontSize());
	textFont	=	new	Font("Century",qicsPicturePropertyBean.getDefectFontStyle(),qicsPicturePropertyBean.getDefectFontSize());

	// Set Font Metrics.
	fm	=	getFontMetrics(textFont);
		
	center		=	new	Point(getWidth()/2,getHeight()/2);
	// image file is exist ?
	if(getClass().getResource(plusCursorImageFile)==null) {
		System.out.println("An Image file ["+plusCursorImageFile+"] is not found.");
		cursorImage1	=	null;
	} else {
		cursorImage1	=	getToolkit().createImage(getClass().getResource(plusCursorImageFile));
	}	
	if(getClass().getResource(minusCursorImageFile)==null) {
		System.out.println("An Image file ["+minusCursorImageFile+"] is not found.");
		cursorImage2	=	null;
	} else {
		cursorImage2	=	getToolkit().createImage(getClass().getResource(minusCursorImageFile));
	}
	MediaTracker	mt	=	new	MediaTracker(this);
	mt.addImage(cursorImage1,0);
	mt.addImage(cursorImage2,1);
	try{
		mt.waitForID(0);
		mt.waitForID(1);
	} catch(InterruptedException e) {
		System.out.println("An cursor image files has not been loaded."); 
	} 
	if(cursorImage1 == null) {
		inCursor	=	new	Cursor(Cursor.DEFAULT_CURSOR);
	} else {
		inCursor	=	getToolkit().createCustomCursor(cursorImage1,
				new Point(qicsPicturePropertyBean.getPCursorHotSpotX(),qicsPicturePropertyBean.getPCursorHotSpotY()),"in");
	}
	if(cursorImage2 == null) {
		outCursor	=	new	Cursor(Cursor.DEFAULT_CURSOR);
	} else {
		outCursor	=	getToolkit().createCustomCursor(cursorImage2,
				new Point(qicsPicturePropertyBean.getMCursorHotSpotX(),qicsPicturePropertyBean.getMCursorHotSpotY()),"out");
	}
	// user code end
}

/**
 * This method is override of the paint method is carried out.
 *
 * @param g java.awt.Graphics Graphics context
 */
public void paint(java.awt.Graphics g) {

	g.fillRect(0,0,getWidth(),getHeight());
	if(imgFile.trim().equals(""))	return;
	firstPaint();

	if(!this.firstPaint) {
		firstPaint();
	}	
	
	// draw Image.
	if(imageFileNotFound) {
		// Image File is Not Found.
		g.setColor(Color.white);
		g.drawString("Image File [ " + imgFile + " ] is Not Found.",20,getHeight()/2);	
	} else {
		g.drawImage(image,getOrgX(),getOrgY(),null);
	}
	
	drawImageSections(g);
	
	// X point full display mode. 
	if(repairResultList != null) {

		for(DefectResult item : repairResultList) {
			Point	p	=	new	Point(item.getPointX(),item.getPointY());
			if(nowImage.equals(item.getImageName())) {
				defectStatus	=	item.getDefectStatusValue();
					if(p.x!=0||p.y!=0) drawXPoint(p,item.getId().getDefectTypeName(),g);
			}
		}
	}


}

private int getOrgX(){
	return (getWidth()/2)-(image.getWidth(null)/2);
}

private int getOrgY(){
	return (getHeight()/2)-(image.getHeight(null)/2);
}

private void drawImageSections(Graphics g) {
	
	polygons.clear();
	
	for(ImageSection item : imageEntity.getSections()) {
		DefectDescription defectDescription = item.getDefectDescription();
		if(defectDescription != null && defectDescription.getDefectTypeName().equals(defectTypeName))	
			drawPolygon(g,item,defectDescription);
	}
	
}

private DefectDescription getDefectDescriptionById(int descriptionId) {
	
	if(defectDescriptions == null) return null;
	for(DefectDescription item : defectDescriptions) {
		
		if(item.getInspectionPartDescription() != null && item.getInspectionPartDescription().getDescriptionId() == descriptionId) return item;
		if(item.getInspectionTwoPartDescription() != null && item.getInspectionTwoPartDescription().getDescriptionId() == descriptionId) return item;

	}
	return null;
	
}
/**
 * This method is called when mouse_moved_event happends.
 * It checks whether the hot point of cursor is inside an image section,
 * or it is outside , and the image of corsor is changed.
 * @param mouseEvent MouseEvent Mouse event
 */
public void handleMouseMoved(MouseEvent mouseEvent) {

	//@SR3739 - JJ
	//set default cursor
	setCursor(outCursor);
	int x = mouseEvent.getX();
	int y = mouseEvent.getY();
	
	for(XPolygon polygon : polygons) {
		if(polygon.contains(x,y)) {
			setCursor(inCursor);
			break;
		}
	}
	return;
}
/**
 * This method is called when a mouse is pushed on this object.
 * Call checkPolygon.
 * @param mouseEvent MouseEvent Mouse event
 */
public void handleMousePressed(MouseEvent mouseEvent) {
	checkPolygon(mouseEvent);
	return;
}
/**
 * Set new Value for defect_status.
 * @param newDefectStatus int Defect status
 */
public void setDefectStatus(int newDefectStatus) {
	defectStatus = newDefectStatus;
}
/**
 * This method is the flag of a section display and not displaying is set up.
 */
public void setDispPolygonStatus(boolean isDisplay) {
	dispPolygonStatus	=	isDisplay;
}

/**
 * This method sets an image file name. 
 * @param imageFile String Image file name
 */
public void setImage(String imageFile) {

	try {
		imgFile	=	(imageFile==null) ? "" : imageFile;

		// image file is exist ?
		if(getClass().getResource(imageFile)==null) {
			imageFileNotFound	=	true;	
			System.out.println("An Image file "+imageFile+"is not found.");
		}
	} catch(Exception e) {
			imageFileNotFound	=	true;	
		System.out.println("An Image file "+imageFile+"is not found.");
	}
	if(polygons != null) polygons.clear();
	//@SR3739 - JJ 
	//call repaint() later for better performance
//	repaint();
}

public void setImage(com.honda.galc.entity.qics.Image imageEntity) {

	imageFileNotFound = false;
	imgFile	=	(imageEntity==null) ? "" : imageEntity.getBitmapFileName();
	this.imageEntity = imageEntity;

	if(polygons != null) polygons.clear();
}

/**
 * The method sets current image name.
 * @param newNowImage java.lang.String Image name
 */
public void setNowImage(java.lang.String newNowImage) {
	nowImage = newNowImage;
}


public void setDefects(List<DefectResult> defects) {
	this.repairResultList = defects;
}

public void setImageAndDefectTypeName(String defectTypeName, 
		List<DefectDescription> defectDescriptions) {

	this.defectTypeName = defectTypeName;
	this.defectDescriptions = defectDescriptions;
	
	if(polygons!=null) polygons.clear();
	
	for(ImageSection item : imageEntity.getSections()) {
		DefectDescription defectDescription = getDefectDescriptionById(item.getDescriptionId());
	    item.setDefectDescription(defectDescription);
	}
	
}

}
