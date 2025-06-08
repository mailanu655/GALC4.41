package com.honda.galc.client.gts.figure;



import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Action;

import org.apache.commons.lang.StringUtils;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.Handle;

import com.honda.galc.client.gts.view.CarrierInfoWindow;
import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.gts.view.HighlightCondition;
import com.honda.galc.client.gts.view.action.AddCarrierAction;
import com.honda.galc.client.gts.view.action.CarrierAssociationAction;
import com.honda.galc.client.gts.view.action.CorrectCarrierAction;
import com.honda.galc.client.gts.view.action.EditInspectionStatusAction;
import com.honda.galc.client.gts.view.action.HighlightProductAction;
import com.honda.galc.client.gts.view.action.ManualMoveAction;
import com.honda.galc.client.gts.view.action.RefreshDefectStatusAction;
import com.honda.galc.client.gts.view.action.RemoveCarrierAction;
import com.honda.galc.client.gts.view.action.SetScrapAction;
import com.honda.galc.entity.enumtype.GtsBorderType;
import com.honda.galc.entity.enumtype.GtsCarrierDisplayType;
import com.honda.galc.entity.enumtype.GtsOrientation;
import com.honda.galc.entity.gts.GtsLabel;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsMove;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>CarrierFigure</code> is a graphical display of the product <br>
 * It displays the product outline and product information bar<br>
 * The shape of the product outline is based on the product model code<br>
 * and its color is based on the product exterior color code<br>
 * information bar can display any of carrier,product lot,model code, <br>
 * YMTO, color code etc info. User can select what info to display <br>
 * User can also highlight the products by select the specific production lot<br>
 * or YMTO. The figure also displays some status flags(Repair,Scrap,Hold etc) on top of the product <br>
 * outline image. 
 * In the case of engine tracking, the status flag also shows the allocated quorum sequence
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
 * <TD>Feb 22, 2008</TD>
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

public class CarrierFigure extends GtsAbstractFigure {
		
    private static final long serialVersionUID = 1L;

    // hightlight conditions 
    private static List<HighlightCondition> highlightConditions = new ArrayList<HighlightCondition>();
    
    private static GtsCarrierDisplayType displayType = GtsCarrierDisplayType.CARRIER; // By Default Carrier number is displayed
	private LabelFigure textFigure;
    private LabelFigure statusFigure;
    private ImageFigure image;
	private GtsLaneCarrier carrier;

    
    public CarrierFigure() {
    
    }

    
    public CarrierFigure(GtsLaneCarrier carrier){
    	this.carrier = carrier;
 //   	init();
    }
    
    
    /**
     * set up all the figure components
     *
     */
    
    public void init(){
        
        Point2D.Double point = getCenter();
        initializeImage(point);
        initializeTextFigure(point);
        initializeStatusFigure(point);
        if(image != null) add(image);
        add(textFigure);
        if(statusFigure != null) add(statusFigure);
        this.changed();
    }
    
    /**
     * Calculate the center location of the current carrier.
     * If the lane segment is horizontal, place the carrier figure above the lane segment
     * If the lane segment is vertical, place the carrier figure to be centered
     * at original calculated point.
     * @return
     */
    
    private Point2D.Double getCenter(){
        Point2D.Double point = carrier.getPoint();
        if(point == null){
            System.out.println("Could not allocate the carrier.It might be the lane Capacity issue");
            System.out.println("Carrier # "  + carrier.getCarrierId() + " Lane Name = " + carrier.getLane().getId().getLaneId());
            return new Point2D.Double(50,50);
        }
        GtsOrientation dir = carrier.getDirection();
        
        if(dir == GtsOrientation.EAST || dir == GtsOrientation.WEST){
            point.y -= (( getModel().getPropertyBean().getImageHeight() + 
            		 	  getModel().getPropertyBean().getLabelHeight()) / 2) +1;
        };
        
        return point;
    }
    
    /**
     * Initialize carrier image, the image is alligned at "point" 
     * @param point
     */
    
    private void initializeImage(Point2D.Double point){

        BufferedImage bufImage =   ImageFactory.getInstance().getBufferedImage(carrier);
        
   
            image = new ImageFigure(point.x - (getModel().getPropertyBean().getImageWidth() / 2),
                            point.y - ((getModel().getPropertyBean().getImageHeight() + getModel().getPropertyBean().getLabelHeight()) / 2),
                            getModel().getPropertyBean().getImageWidth(),
                            getModel().getPropertyBean().getImageHeight());
            image.setBufferedImage(bufImage);
            image.setAttribute(AttributeKeys.STROKE_WIDTH,getModel().getPropertyBean().getCarrierBorderWidth());
            if(carrier.isDuplicateDiscrepancy() || carrier.isPhotoEyeDiscrepancy()) image.setAttribute(AttributeKeys.FILL_COLOR,Color.red);
            else if(carrier.isReaderDiscrepancy()) image.setAttribute(AttributeKeys.FILL_COLOR,Color.magenta);
            
    }
    
    /**
     * Initialize the label figure. The lable is alligned at "point"
     * @param point
     */
    
    private void initializeTextFigure(Point2D.Double point){
        
        GtsLabel label = new GtsLabel(carrier.getDisplayText(displayType));
        label.setX((int)(point.x - (getModel().getPropertyBean().getLabelWidth() / 2)));
        label.setY((int)(point.y + ((getModel().getPropertyBean().getImageHeight() - getModel().getPropertyBean().getLabelHeight()) / 2)));
        label.setWidth(getModel().getPropertyBean().getLabelWidth());
        label.setHeight(getModel().getPropertyBean().getLabelHeight());
        label.setLabelFont("Dialog-PLAIN-"+getModel().getPropertyBean().getCarrierFontSize());
        label.setBorderType(GtsBorderType.RECTANGLE);
        textFigure = new LabelFigure(label);
        
        // hightlight the label
        List<Color> colors = getHightlightColors();
        if(!colors.isEmpty()) textFigure.setFillColors(colors);
        else textFigure.setAttribute(AttributeKeys.FILL_COLOR,AttributeKeys.FILL_COLOR.getDefaultValue());
        textFigure.setAttribute(AttributeKeys.STROKE_COLOR,AttributeKeys.STROKE_COLOR.getDefaultValue());
        textFigure.setAttribute(AttributeKeys.STROKE_WIDTH,getModel().getPropertyBean().getCarrierBorderWidth());
    }
    
    /**
     * Initialize Status figure.
     * Status figure includes different kinds of status info: Repair Status , Hold Status, Engine Quorum Allocated
     * @param point
     */
    
    private void initializeStatusFigure(Point2D.Double point){
        
        String statusText = getStatusText();
        
    	// if status text is null or empty don't show 
        if(statusText == null || statusText.length() == 0 ) return;
        
        GtsLabel label = new GtsLabel(statusText);
        label.setX((int)(point.x - (getModel().getPropertyBean().getImageWidth() / 2)));
        label.setY((int)(point.y - (getModel().getPropertyBean().getImageHeight() + getModel().getPropertyBean().getLabelHeight()) / 2));
        label.setWidth(getModel().getPropertyBean().getImageWidth());
        label.setHeight(getModel().getPropertyBean().getImageHeight());
        label.setBorderType(GtsBorderType.ROUND_RECTANGLE);
        statusFigure = new LabelFigure(label);
        statusFigure.setFontStyleAndSize(Font.BOLD,getModel().getPropertyBean().getCarrierFontSize() + 8);
        statusFigure.pack();
        statusFigure.setAttribute(AttributeKeys.FILL_COLOR,AttributeKeys.FILL_COLOR.getDefaultValue());
        statusFigure.setAttribute(AttributeKeys.STROKE_COLOR,AttributeKeys.STROKE_COLOR.getDefaultValue());
        statusFigure.setAttribute(AttributeKeys.STROKE_WIDTH,getModel().getPropertyBean().getCarrierBorderWidth());

    }
    
    private String getStatusText() {
    	if(carrier.getCarrierId().equalsIgnoreCase(getModel().getServerPropertyBean().getUnknownCarrier())) {
    		return "? ? ?";
    	}else return carrier.getStatusText();
    }
    
    public GtsLaneCarrier getCarrier(){
    	return carrier;
    }
    
    public String getCarrierNumber(){
        return carrier.getCarrierId();
    }
    
    public GtsLane getLane(){
        if (carrier != null) return carrier.getLane();
        else return null;
    }
    
    /**
     * Handles a mouse double click.
     */
    @Override public boolean handleMouseClick(Point2D.Double p, MouseEvent evt, DrawingView view) {
        
        if(GtsDrawing.isEditingMode()) return false;
        
        if(carrier != null){
           
            CarrierInfoWindow carrierInfoWindow = new CarrierInfoWindow(carrier);
            carrierInfoWindow.setVisible(true);
            
        }
        return false;
    }
   
    
    
    // EDITING
    @Override public Collection<Action> getActions(Point2D.Double p) {
        
        LinkedList<Action> actions = new LinkedList<Action>();
        
        // don't display menu when in editing mode
        if(GtsDrawing.isEditingMode()) return actions;
        
        if(getDrawing().getModel().isUserControllable() && carrier.getPosition() == 0) {
            
            // head of lane 
            boolean isFirst = true;
           
            for(GtsMove move :getDrawing().getModel().getMoves()){
                if(move.getId().getSourceLaneId().equals(getLane().getId().getLaneId())){
                    if(isFirst) {
                        // add a seprator
                        actions.add(null);
                        isFirst = false;
                    }
                    
                    actions.add(new ManualMoveAction(this.getDrawing(),move));
                }    
            }
            
        }
        
        // only display add/remove carrier menu when the user has the authority
        if(getDrawing().getModel().isControllAllowed()){
            
            actions.add(new AddCarrierAction(getDrawing(),carrier.getLane(),carrier.getPosition(),0));
            actions.add(new AddCarrierAction(getDrawing(),carrier.getLane(),carrier.getPosition(),1));
            actions.add(new CorrectCarrierAction(this));
            actions.add(new RemoveCarrierAction(this,getCarrier().getPosition()));
            if(carrier.getProduct() == null) actions.add(new SetScrapAction(getDrawing(), this));
             
            if(getModel().getServerPropertyBean().isAllowGUICarrierProductAssociation()) {
            
	            // add a seprator
	            actions.add(null);
	            if(carrier.getCarrier() != null && carrier.getCarrier().isNormal() 
	            		&& !carrier.isUnknownCarrier() && !getDrawing().getModel().isEmptyCarrier(carrier.getCarrierId())) {
	                
	                // Carrier production deassociation
	                if(StringUtils.isNotEmpty(carrier.getProductId()))
	                   actions.add(new CarrierAssociationAction(this,false));
	                
	                // carrier production association
	                actions.add(new CarrierAssociationAction(this,true));
	                
	                // add a seprator
	                actions.add(null);
	            }
            }
        }
        
        if(carrier.getProduct() != null) {
        	actions.add(new RefreshDefectStatusAction(this));
            actions.add(new EditInspectionStatusAction(this));
            actions.add(new HighlightProductAction(this,HighlightCondition.Type.TYPE_PROD_LOT));
            actions.add(new HighlightProductAction(this,HighlightCondition.Type.TYPE_MTO));
        }
        
        // remove the hightlighted colors
        if(!getConditions().isEmpty())actions.add(new HighlightProductAction(this,HighlightCondition.Type.TYPE_NONE));
        
        if(getDrawing().getModel().isUserControllable() && carrier.getPosition() == 0) {
            
            // head of lane 
            boolean isFirst = true;
           
            for(GtsMove move :getDrawing().getModel().getMoves()){
                if(move.getId().getSourceLaneId().equals(getLane().getId().getLaneId())){
                    if(isFirst) {
                        // add a seprator
                        actions.add(null);
                        isFirst = false;
                    }
                    
                    actions.add(new ManualMoveAction(this.getDrawing(),move));
                }    
            }
            
        }
        
        return actions;
   
    }
    
    public Collection<Handle> createHandles(int detailLevel) {
        return new LinkedList<Handle>();
    }
    
    
    /**
     * Transforms the figure.
     */
    public void transform(AffineTransform tx) {
        
    }
    
    /**
     * Set or reset the duplicate color 
     * @param flag
     */
    
    public void setDuplicated(boolean flag){
        if(flag){
            textFigure.setAttribute(AttributeKeys.STROKE_COLOR,Color.red);
            textFigure.setAttribute(AttributeKeys.STROKE_WIDTH,
            		getModel().getPropertyBean().getCarrierBorderWidth()+0.5d);
        }else {
            textFigure.removeAttribute(AttributeKeys.STROKE_COLOR);
            textFigure.setAttribute(AttributeKeys.STROKE_WIDTH,
            		getModel().getPropertyBean().getCarrierBorderWidth());
        }
        invalidate();
    }
    
    private List<Color> getHightlightColors(){
        
        List<Color> colors = new ArrayList<Color>();
        
        for (HighlightCondition condition:getHighlightConditions()){
            switch(condition.getType()){
                case TYPE_PROD_LOT:
                    if(getCarrier().getProductionLot().endsWith(condition.getExpectedValue())){
                       colors.add(condition.getColor());
                    }
                    break;
                case TYPE_PROD_NUMBER:
                	if(getCarrier().getProductId() != null && getCarrier().getProductId().endsWith(condition.getExpectedValue())) {
                		colors.add(condition.getColor());
                	}
                    break;
                case TYPE_CARRIER:
                    String value = condition.getExpectedValue();
                    if(StringUtils.isNumeric(value)) {
                    	String carrier = getCarrier().getCarrierId();
                    	if(!StringUtils.isNumeric(carrier)) break;
                    	if(Integer.parseInt(carrier) == Integer.parseInt(value)) {
                    		colors.add(condition.getColor());
                    	}
                    }
                    else {
						String carrier = getCarrier().getCarrierId();
						if (carrier != null && carrier.equalsIgnoreCase(value))
							colors.add(condition.getColor());
					}
                    break;
                case TYPE_MTO:
                    if(getCarrier().getProductSpec().startsWith(condition.getExpectedValue())){
                        colors.add(condition.getColor());
                    }
                    break;
                case TYPE_COLOR:
                	if(getCarrier().getColorCode().contains(condition.getExpectedValue())) { 
                        colors.add(condition.getColor());
                    }
                    break;
                case TYPE_NONE:
            }
        }
        
        return colors;
    }
    
    public void removeHighlightedColors(){
        
        List<HighlightCondition> conditions = getConditions();
        
        if(!conditions.isEmpty()) highlightConditions.removeAll(conditions);
    }
    
    
    private List<HighlightCondition> getConditions(){
        
        List<HighlightCondition> conditions = new ArrayList<HighlightCondition>();
        
        for (HighlightCondition condition:getHighlightConditions()){
            switch(condition.getType()){
                case TYPE_PROD_LOT:
                    if(getCarrier().getProductionLot().equalsIgnoreCase(condition.getExpectedValue())){
                       conditions.add(condition);
                    }
                    break;
                case TYPE_PROD_NUMBER:
                    break;
                case TYPE_MTO:
                    if(getCarrier().getProductSpec().equalsIgnoreCase(condition.getExpectedValue())){
                        conditions.add(condition);
                    }
                    break;
                default:
            }
        }
        
        return conditions;
    }
    
    /**
     * update move status - start or stop carrier flash to indicate the carrier is moving 
     * @param move
     */
    
    public void updateMoveStatus(GtsMove move){
      
      if(move.isCreated() || image == null) return;
      
      image.enableSnail(move.isStarted());
      
      this.changed();
    
    }
    
    /**
     * highlight this figure with color
     * @param color
     */
    public void highlight(){
        
        textFigure.setFillColors(getHightlightColors());
        
        textFigure.invalidate();
        
    }
    
    public BufferedImage getImage() {
    	
   // 	return image.getBufferedImage();

    	Rectangle2D dr = getView().getDrawingArea();
        
	    BufferedImage img = new BufferedImage((int)dr.getWidth(),(int)dr.getHeight(), BufferedImage.TYPE_INT_RGB);
	  //  printImage(img);
	    Graphics2D g2d = img.createGraphics();
    	this.draw(g2d);
    	Rectangle2D bound = getBounds();
    	BufferedImage subImage = img.getSubimage((int)bound.getX(), (int)bound.getY(), (int)bound.getWidth(), (int)bound.getHeight());
 //   	printImage(subImage);
    	g2d.dispose();
    	return subImage;
    }
    
    public void printPixelARGB(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        System.out.println("argb: " + alpha + ", " + red + ", " + green + ", " + blue);
      }
    
    private void printImage(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        System.out.println("width, height: " + w + ", " + h);

        for (int i = 0; i < h; i++) {
          for (int j = 0; j < w; j++) {
            System.out.println("x,y: " + j + ", " + i);
            int pixel = image.getRGB(j, i);
            printPixelARGB(pixel);
            System.out.println("");
          }
        }
      }
    
    public static List<HighlightCondition> getHighlightConditions(){
        return highlightConditions;
   }
    
    public static HighlightCondition getHighlightCondition(HighlightCondition.Type type,String key){
        for(HighlightCondition condition: highlightConditions){
            if(condition.getType() == type && condition.getExpectedValue().equalsIgnoreCase(key)){
                return condition;
            }
        }
        return null;
    }
    
    public static void addHightlightCondition(HighlightCondition.Type type,String key,Color color){
        HighlightCondition condition = getHighlightCondition(type,key);
        if(condition == null)highlightConditions.add(new HighlightCondition(type,key,color));
        else condition.setColor(color);
    }
    
    
    public void refreshText(){
        String text = carrier.getDisplayText(displayType);
        if(text == null) text = "N/A";
        if(!textFigure.getText().equalsIgnoreCase(text)){
            textFigure.setText(text);
            textFigure.invalidate();
        }
    }
    
    public void refreshStatus(){
        if(statusFigure !=null){
            this.remove(statusFigure);
            statusFigure = null;
        }
        initializeStatusFigure(getCenter());
        if(statusFigure !=null) this.add(statusFigure);
    }
    
    // this is to stop timer to release resource to avoid memory leak
    public void cleanUp() {
    	if(image != null) {
    		image.enableSnail(false);
    	}
    }
    
    public static void setDisplayType(GtsCarrierDisplayType type){
        displayType = type;
    }
    
    public static GtsCarrierDisplayType getDisplayType(){
        return displayType;
    }

 }
