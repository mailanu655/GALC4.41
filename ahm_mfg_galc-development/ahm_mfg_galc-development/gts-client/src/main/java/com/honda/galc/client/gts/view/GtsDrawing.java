package com.honda.galc.client.gts.view;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.Figure;

import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.gts.figure.CarrierFigure;
import com.honda.galc.client.gts.figure.GateFigure;
import com.honda.galc.client.gts.figure.LabelFigure;
import com.honda.galc.client.gts.figure.LaneSegmentFigure;
import com.honda.galc.client.gts.figure.ShapeFigure;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.entity.enumtype.GtsCarrierDisplayType;
import com.honda.galc.entity.gts.GtsIndicator;
import com.honda.galc.entity.gts.GtsLabel;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsLaneSegment;
import com.honda.galc.entity.gts.GtsMove;
import com.honda.galc.entity.gts.GtsNode;
import com.honda.galc.entity.gts.GtsShape;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.service.property.PropertyService;

public class GtsDrawing extends DefaultDrawing{
    
    private static final long serialVersionUID = 1L;
    
    public static final int PRODUCTION_MODE = 1;
    public static final int EDITING_MODE = 2; 
    public static int DISPLAY_MODE = PRODUCTION_MODE;
    private GtsTrackingController controller;
    private GtsTrackingModel model;
    private boolean carrierVisible = true;
    protected ClientAudioManager audioManager = null;
    private boolean alarmCondition = false;
 	
	public GtsDrawing(GtsTrackingController controller){
		super();
        this.controller = controller;
        this.model = controller.getModel();
        audioManager = new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class));
	}
	
	public void initDrawing(){
        this.clear();
		initShapes();
        initLaneSegments();
		initNodes();
		initCarriers();
        initLabels();
        initMoves();
	}
	
	public GtsTrackingController getController(){
	    return controller;
    }
	
	public GtsTrackingModel getModel() {
		return model;
	}
    
    public boolean getCarrierVisibleFlag(){
        return carrierVisible;
    }
    
    public void setCarrierVisibleFlag(boolean aFlag){
        if(carrierVisible == aFlag) return;
        toggleCarrierVisibleFlag();
    }
    
    public void toggleCarrierVisibleFlag(){
        carrierVisible = !carrierVisible;
        showCarrierFigures(carrierVisible);
    }
    
    public void toggleLabel(GtsIndicator indicator) {
       	Map<String, String> toggleLabelMap = getModel().getServerPropertyBean().getToggleLabelMap();
       	String lableName = toggleLabelMap == null ? null : toggleLabelMap.get(indicator.getIndicatorName());
       	
       	if(StringUtils.isEmpty(lableName)) return;
       	
       	List<GtsLabel> labels = getModel().findLabels(lableName);
       	
   		for(GtsLabel label : labels) {

   			LabelFigure figure = findLabelFigure(label.getId().getLabelId());

   	       	if(!indicator.isStatusOn()) {
   	       		if(figure == null) add(new LabelFigure(label));
   	       	}else {
   	       		if(figure != null) removeLabel(figure);
   	       	}
   		}	
       	
    }

    
    private void showCarrierFigures(boolean aFlag){
        for(Figure fig:getFigures()){
            if (fig instanceof CarrierFigure){
                ((CarrierFigure)fig).setVisible(aFlag);
            }
        }
    }
    
    private void initLaneSegments(){
       	for(GtsLaneSegment aLaneSegment: model.getLaneSegments()){
       		addLaneSegment(aLaneSegment);
       	}
    }
    
    public LaneSegmentFigure addLaneSegment(GtsLaneSegment laneSegment) {
    	LaneSegmentFigure lf; 
    	lf = findLaneSegmentFigure(laneSegment.getId().getLaneSegmentId());
    	
    	if(lf == null) {
    		lf = new LaneSegmentFigure(laneSegment,model.getPropertyBean().getLaneSegmentWidth());
    		add(lf);
    		lf.setInteractive(true);
    	}
    	
    	lf.setLaneSegment(laneSegment, model.getPropertyBean().getLaneSegmentWidth());
    	
    	return lf;
    }
 	
	private void initNodes(){
		for (GtsNode aNode: model.getNodes()){
            if(aNode.isVisible()){
                GateFigure gateFigure = findGateFigure(aNode.getNodeId());
                if(gateFigure == null) {
                	add(gateFigure = new GateFigure(aNode,model.getPropertyBean().getGateIconSize()));
                }
                gateFigure.setNode(aNode);
            }
		}
	}
    
    private void initMoves(){
        for(GtsIndicator indicator:model.getIndicators()){
            if(indicator.isMoveInProgress() && indicator.isStatusOn()){
                this.updateMoveStatus(new GtsMove(indicator));
            }else if(indicator.isMoveStatus()) {
            	this.processLaneStatus(indicator.getLaneName(), indicator.isStatusOn());	
            } 
        }
        
        for(GtsMove move:model.getMoves()){
            if(!move.isFinished())
            this.updateMoveStatus(move);
        }
    }
	
	public void initCarriers(){

        for (GtsLane lane:model.getLanes()){
           updateLaneCarriers(lane);
        }
        
        highlightDuplicatedCarriers();
	}
    
    public void refreshCarriers(){
        
        if (! isEditingMode()) return;
        
        // only when it is editing mode refresh the dummy lane carriers
       for(GtsLane lane : model.getLanes()) {
            lane.initializeDummyCarriers();
        }
        
        initCarriers();
        
    }
    
    public void refreshPLCIndicators() {
    	
		controller.getWindow().setWaitCursor();
     	try {
     		model.refreshPLCIndicators();
     	}catch(Exception ex) {
     		
     	}
	  	controller.getWindow().setDefaultCursor();
		    	
	}
    	
    	
  		
    
    private void initLabels(){
        
        for(GtsLabel label: model.getLabels()){
        	
        	String indicatorName = getModel().findIndicatorNameFromLabel(label.getLabelText());
        	if(!StringUtils.isEmpty(indicatorName)) {
        		GtsIndicator indicator = getModel().findIndicator(indicatorName);
        		if(indicator != null && indicator.isStatusOn()) continue;
        	}

        	LabelFigure figure = new LabelFigure(label);
            add(figure);
            figure.refresh();
        }
    }
    
    
    private void initShapes(){
        for(GtsShape shape: model.getShapes()){
            add(new ShapeFigure(shape));
        }
    }
    
    /**
     * create carrier Figure for the lane
     * @param lane
     */
    
    public void updateLaneCarriers(GtsLane lane){
        
        // remove carrier figures first
        this.removeLaneCarriers(lane.getLaneId());
        
        int position = 0;
        CarrierFigure figure;
        this.resetDuplicatedCarriers();
        for (GtsLaneCarrier carrier: lane.getLaneCarriers()){
        	carrier.setLane(lane);
            carrier.setPosition(position);
            if(GtsDrawing.isEditingMode() || carrier.isVisible()){
                add(figure = new CarrierFigure(carrier));
                figure.init();
                figure.setVisible(carrierVisible);
                figure.highlight();
            }
            position++;
            if (carrier.isDuplicateDiscrepancy() || carrier.isPhotoEyeDiscrepancy() || carrier.isUnknownCarrier()){
            	this.setAlarmCondition(true);
            }
        }
        this.highlightDuplicatedCarriers();
        this.alarm();
    }
    
    public GateFigure findGateFigure(int nodeId){
        for (Figure fig:this.getFigures()){
            if (fig instanceof GateFigure && ((GateFigure)fig).isNodeId(nodeId))
                return (GateFigure) fig;
        }
        return null;
    }
    
    public LabelFigure findLabelFigure(int id){
        for (Figure fig:this.getFigures()){
            if (fig instanceof LabelFigure && ((LabelFigure)fig).getLabel().getId().getLabelId() ==id)
                return (LabelFigure) fig;
        }
        return null;
    }
    
    public LabelFigure findLabelFigure(String label){
        for (Figure fig:this.getFigures()){
            if (fig instanceof LabelFigure && ((LabelFigure)fig).getLabel().getLabelText().equalsIgnoreCase(label))
                return (LabelFigure) fig;
        }
        return null;
    }
    
    public ShapeFigure findShapeFigure(int id){
        for (Figure fig:this.getFigures()){
            if (fig instanceof ShapeFigure && ((ShapeFigure)fig).getShape().getId().getShapeId() ==id)
                return (ShapeFigure) fig;
        }
        return null;
    }
    
    public LaneSegmentFigure findLaneSegmentFigure(int id){
        for (Figure fig:this.getFigures()){
            if (fig instanceof LaneSegmentFigure && ((LaneSegmentFigure)fig).getLaneSegment().getId().getLaneSegmentId() ==id)
                return (LaneSegmentFigure) fig;
        }
        return null;
    }
    
    public void removeLaneSegmentFigure(int id){
        LaneSegmentFigure figure = this.findLaneSegmentFigure(id);
        if(figure == null) return;
        this.remove(figure);
        GtsLaneSegment segment = figure.getLaneSegment();
        GtsNode node = segment.getInputNode();
        if (node.getInputLaneSegments().size() == 0 && node.isVisible()) removeGateFigure(node);
        node = segment.getOutputNode();
        if (node.getOutputLaneSegments().size() == 0 && node.isVisible()) removeGateFigure(node);
    }
    
    public void removeGateFigure(GtsNode node){
        GateFigure figure = this.findGateFigure(node.getId().getNodeId());
        if (figure != null) remove(figure);
    }
    
    public List<GtsLaneSegment> findLaneSegments(int nodeId){
        List<GtsLaneSegment> laneSegments = new ArrayList<GtsLaneSegment>();
        for (Figure fig:this.getFigures()){
            if (fig instanceof LaneSegmentFigure && (((LaneSegmentFigure)fig).isNodeId(nodeId)))
            	laneSegments.add(((LaneSegmentFigure)fig).getLaneSegment());
        }
        return laneSegments;
    }
    
    private LaneSegmentFigure findLaneSegmentFigure(int nodeId, boolean isInputNode){
        
        for(Figure fig: this.getFigures()){
            if (fig instanceof LaneSegmentFigure){
                LaneSegmentFigure figure = (LaneSegmentFigure) fig;
                if(isInputNode && figure.isInputNode(nodeId)) return figure;
                else if(!isInputNode && figure.isOutputNode(nodeId)) return figure;
            }
        }
        
        return null;
    }
    
    public List<LaneSegmentFigure> findInputLaneSegmentFigures(int nodeId){
        List<LaneSegmentFigure> figures = new ArrayList<LaneSegmentFigure>();
        for (Figure fig:this.getFigures()){
            if (fig instanceof LaneSegmentFigure && (((LaneSegmentFigure)fig).isOutputNode(nodeId)))
                figures.add((LaneSegmentFigure)fig);
        }
        
        return figures;
    }
    
    
    public CarrierFigure findCarrierFigure(GtsLane lane,int position){
        for (Figure fig:this.getFigures()){
            if(fig instanceof CarrierFigure){
                GtsLaneCarrier carrier = ((CarrierFigure) fig).getCarrier();
                if (carrier.getLane().getId().getLaneId().equals(lane.getId().getLaneId())
                                && carrier.getPosition() == position)
                    return (CarrierFigure) fig; 
            }
        }
        return null;
    }
    
    public LinkedList<Figure> getLaneCarriers(String laneName){
        LinkedList<Figure> carriers = new LinkedList<Figure>();
        for (Figure fig:this.getFigures()){
            if(fig instanceof CarrierFigure && ((CarrierFigure)fig).getLane().getLaneId().equals(laneName)){
            	((CarrierFigure)fig).cleanUp();
                carriers.add(fig);
            }
        }
        return carriers;
    }
    
    public int getMinIndexOfShapes(){
        for (int i = 0;i<getFigures().size();i++){
            if(getFigures().get(i) instanceof ShapeFigure) return i;
        }
        return -1;
    }
    
    public int getMaxIndexOfShapes(){
        for (int i = getFigures().size()-1;i>=0;i--){
            if(getFigures().get(i) instanceof ShapeFigure) return i;
        }
        return 0;
    }
    
    public int getMinIndexOfLabels(){
        for (int i = 0;i<getFigures().size();i++){
            if(getFigures().get(i) instanceof LabelFigure) return i;
        }
        return getMaxIndexOfShapes();
    }
    
    public int getMaxIndexOfLabels(){
        for (int i = getFigures().size()-1;i>=0;i--){
            if(getFigures().get(i) instanceof LabelFigure) return i;
        }
        return getMaxIndexOfShapes();
    }
    
    /**
     * remove all the carrier figures at this lane
     * @param laneName
     */
    
    public void removeLaneCarriers(String laneName){
        removeAll(getLaneCarriers(laneName));
    }
    
    
    public void removeShape(ShapeFigure figure){
//       getArea().removeShape(figure.getShape());
       remove(figure);
    }
    
    public void removeLabel(LabelFigure figure){
 //       getArea().removeLabel(figure.getLabel());
        remove(figure);
     }
    
    public void highlightCarriers(){
        for(Figure fig:this.getFigures()){
            if(fig instanceof CarrierFigure){
                ((CarrierFigure)fig).highlight();
            }
        }
    }
    
    /**
     * refresh the carrier display images 
     * @param displayType - indicating to display carrier,product id, mtoc,production lot etc
     */
    
    public void refreshCarriers(GtsCarrierDisplayType displayType){
        CarrierFigure.setDisplayType(displayType);
        for(Figure fig:getFigures()){
            if (fig instanceof CarrierFigure){
                ((CarrierFigure)fig).refreshText();
            }
        }
    }
    
    /**
     * refresh product status (repair,sequence,hold etc) display of all carrier figures
     *
     */
    
    public void refreshProductStatus(){
        for(Figure fig:getFigures()){
            if (fig instanceof CarrierFigure){
                ((CarrierFigure)fig).refreshStatus();
            }
        }
    }
    
    public void resetDuplicatedCarriers(){
        for(Figure fig:getFigures()){
            if (fig instanceof CarrierFigure){
                ((CarrierFigure)fig).setDuplicated(false);
            }
        }
    }
    
    public void highlightDuplicatedCarriers(){
        List<String> carriers = getModel().findDuplicatedCarriers();
        for(Figure fig:getFigures()){
            if (fig instanceof CarrierFigure && 
                carriers.contains(((CarrierFigure)fig).getCarrierNumber())){
                ((CarrierFigure)fig).setDuplicated(true);
                this.setAlarmCondition(true);
            }
        }
    }
    
    public void refreshLaneSegmentFigures(){
        for(Figure fig:getFigures()){
            if (fig instanceof LaneSegmentFigure){
                ((LaneSegmentFigure)fig).refreshChange();                
            }
        }
    }
    
    public void refreshGateFigures(){
        for(Figure fig:getFigures()){
            if (fig instanceof GateFigure){
                ((GateFigure)fig).refreshChange();                
            }
        }
    }
    
    public void refreshGateFigure(GtsNode node) {
    	for(Figure fig:getFigures()){
            if (fig instanceof GateFigure){
            	GateFigure gateFigure = (GateFigure) fig;
            	if(gateFigure.getNode().getNodeId() == node.getNodeId())
            		((GateFigure)fig).refreshChange();                
            }
        }
    }
    
    public void refreshLayout(){
        refreshLaneSegmentFigures();
        refreshGateFigures();
        initCarriers();
    }
    
    public void reInitializeLayout() {
    	getModel().loadLaneSegmentsAndGates();
    	getModel().initializeDummyCarriers();
    	initLaneSegments();
    	initNodes();
    	refreshLayout();
    }
    
    
    
    public void updateGateFigure(GtsNode node){
        GateFigure figure = findGateFigure(node.getId().getNodeId());
        if(figure == null)add(new GateFigure(node,model.getPropertyBean().getGateIconSize()));
        else this.remove(figure);
    }
    
    public void toggleGateFigures(GtsLane lane){
        
        if(lane == null) return;
        
        for (Figure fig:this.getFigures()){
            if (fig instanceof GateFigure){
                GtsNode node = ((GateFigure)fig).getNode();
                if(node.isGateOfLane(lane.getLaneId()) && node.isGateOpen()){
                    ((GateFigure)fig).toggleGate();
                }
            }
        }
    }
    
    
    public void openLane(GtsLane lane) {
    	GtsNode node = getModel().getEntryNode(lane.getLaneName());
    	if(node != null) {
    		getModel().toggleGateStatus(node,1);
    	}
    	
    	node = getModel().getExitNode(lane.getLaneName());
    	if(node != null) {
    		getModel().toggleGateStatus(node,1);
    	}
    }
    
    public void closeLane(GtsLane lane) {
    	
         
    	GtsNode node = getModel().getEntryNode(lane.getLaneName());
    	if(node != null) {
    		getModel().toggleGateStatus(node,2);
    	}
    	
    	node = getModel().getExitNode(lane.getLaneName());
    	if(node != null) {
    		getModel().toggleGateStatus(node,2);
    	}
    	
     	toggleGateFigures(lane);
     	 
    	
    }
    
    public boolean login() {
    		
    	if(LoginDialog.login() != LoginStatus.OK) return false;

		if (!getModel().isUserControllable()) {
			JOptionPane.showMessageDialog(null, "You have no access permission to execute this action.", "Error", JOptionPane.ERROR_MESSAGE);
			getController().getTrackingPanel().checkUserControllable();
			return false;
		}
		return true;
    }
    
    public void updateMoveStatus(GtsMove move){
        
        GtsLane destLane = model.findLane(move.getId().getDestinationLaneId());
        
        // update move in progress for carrier figures
        if(destLane == null) return;
        
        CarrierFigure carrierFigure = findCarrierFigure(destLane, destLane.getLaneCarriers().size()-1);
        if(carrierFigure != null) carrierFigure.updateMoveStatus(move);
        
        
        // update move for lane segments
        for(Figure figure : this.getFigures()){
            if(figure instanceof LaneSegmentFigure) {
                LaneSegmentFigure fig = (LaneSegmentFigure) figure;
                GtsLane lane = fig.getLaneSegment().getLane();
                if(lane == null) continue;
                if((fig.getLaneSegment().isSource() && lane.getLaneId().equals(move.getId().getSourceLaneId())) ||
                   (fig.getLaneSegment().isDestination() && lane.getLaneId().equals(move.getId().getDestinationLaneId()))){
                    fig.updateMoveStatus(move);
                }
            }
        }
        
        // update transit lane segment move status
        List<LaneSegmentFigure> figs = new ArrayList<LaneSegmentFigure>();
        
        //update dest lane
        if(destLane.getLaneCapacity() <=0) return;
        
        LaneSegmentFigure fig = this.findLaneSegmentFigure(destLane.getInputNode().getId().getNodeId(),true);
        for(LaneSegmentFigure figure: this.findTransitLaneSegmentFigures(figs,fig,move)) {
            figure.updateMoveStatus(move);
        }
        
    }
    
    public void processLaneStatus(String laneId, boolean status) {
    	if(model.findLane(laneId) == null) return;
    	
    	for(Figure figure : this.getFigures()){
            if(figure instanceof LaneSegmentFigure) {
            	LaneSegmentFigure fig = (LaneSegmentFigure) figure;
            	 if(fig.getLaneSegment().getLane(laneId) != null ) {
                	fig.updateLaneStatus(status);
                }
            }
    	}
    }
    
    private List<LaneSegmentFigure> findTransitLaneSegmentFigures(List<LaneSegmentFigure> figs,LaneSegmentFigure figure,GtsMove move){
        
        
        for(LaneSegmentFigure fig: this.findInputLaneSegmentFigures(figure.getLaneSegment().getInputNodeId())){
            
            if(isTransitLaneSegmentFigure(fig,move)){
                
                figs.add(fig);
                
                findTransitLaneSegmentFigures(figs,fig,move);
            }
                
        }
        
        return figs;
        
    }
    
    
    
    private boolean isTransitLaneSegmentFigure(LaneSegmentFigure figure,GtsMove move){
        
        if(figure.getLaneSegment().getLane() == null || figure.getLaneSegment().getLane().getLaneCapacity() == 0) {
            
            for(LaneSegmentFigure fig: this.findInputLaneSegmentFigures(figure.getLaneSegment().getInputNodeId())) {
                
                if(fig.getLaneSegment().getLane() == null || fig.getLaneSegment().getLane().getLaneCapacity() == 0) {
                    if(isTransitLaneSegmentFigure(fig,move)) return true;
                }else {
                    if(move.getId().getSourceLaneId().equals(fig.getLaneSegment().getLane().getLaneId())) return true;
                }
            }
            
        }

        return false;
        
        
    }
    
    public static void setEditingMode(){
        DISPLAY_MODE = EDITING_MODE;
    }
    
    public static boolean isEditingMode(){
        return DISPLAY_MODE == EDITING_MODE;
    }
   
    public static boolean isProductionMode(){
        return DISPLAY_MODE == PRODUCTION_MODE;
    }
    
    /**
     * get drawing view
     * @return
     */
    
    public static DrawingView getDrawingView(){
        Component focusOwner = KeyboardFocusManager.
        getCurrentKeyboardFocusManager().
        getPermanentFocusOwner();
        if (focusOwner != null && focusOwner instanceof DrawingView) {
            return (GtsDrawingView)focusOwner;
        }
        return null;
    }

	/**
     * @return the alarmCondition
     */
    public boolean isAlarmCondition() {
    	return alarmCondition;
    }

	/**
     * @param alarmCondition the alarmCondition to set
     */
    public void setAlarmCondition(boolean alarmCondition) {
    	this.alarmCondition = alarmCondition;
    }
    
    /**
     * trigger alarm and reset the alarm condition
     */
    public void alarm() {
    	if (getModel().getPropertyBean().isAllowAlarm() && isAlarmCondition()) {
    		audioManager.playNGSound();
    	}
    	setAlarmCondition(false);
    }
}
