package com.honda.galc.client.gts.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ntp.TimeStamp;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventTopicSubscriber;

import com.honda.galc.client.gts.figure.GateFigure;
import com.honda.galc.client.gts.figure.LabelFigure;
import com.honda.galc.client.gts.figure.ShapeFigure;
import com.honda.galc.client.gts.view.action.AfonInforDialog;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.Message;
import com.honda.galc.entity.enumtype.GtsIndicatorType;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsIndicator;
import com.honda.galc.entity.gts.GtsLabel;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsLaneSegment;
import com.honda.galc.entity.gts.GtsMove;
import com.honda.galc.entity.gts.GtsNode;
import com.honda.galc.entity.gts.GtsProduct;
import com.honda.galc.entity.gts.GtsShape;
import com.honda.galc.net.NotificationRequest;
import com.honda.galc.notification.service.IGtsNotificationService;

/**
 * 
 * 
 * <h3>GtsTrackingController Class description</h3>
 * <p> GtsTrackingController description </p>
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
 * May 27, 2015
 *
 *
 */
public class GtsTrackingController implements IGtsNotificationService{
	
	private GtsTrackingModel model;
	
	private GtsTrackingPanel trackingPanel;
	
	public GtsTrackingController(GtsTrackingPanel trackingPanel,GtsTrackingModel model) {
		this.trackingPanel = trackingPanel;
		this.model = model;
		AnnotationProcessor.process(this);
	}

	public GtsTrackingModel getModel() {
		return model;
	}

	public void setModel(GtsTrackingModel model) {
		this.model = model;
	}
	
	public GtsTrackingPanel getTrackingPanel() {
		return trackingPanel;
	}
	
	public MainWindow getWindow() {
		return trackingPanel.getMainWindow();
	}

	public void setTrackingPanel(GtsTrackingPanel trackingPanel) {
		this.trackingPanel = trackingPanel;
	}

	public GtsDrawing getDrawing() {
		return getTrackingPanel().getDrawingView().getDrawing();
	}
	
	/**
	 * notification about last passed product at a process Point
	 * @param processPointId
	 * @param productId
	 */
	@EventTopicSubscriber(topic="IGtsNotificationService")
	public void onGtsTrackingNotificationEvent(String event, NotificationRequest request) {
		getLogger().info("Receiving notification - " + request);
		try {
			request.invoke(this);
		} catch (Exception e) {
			getLogger().error(e, "Could not process tracking notification event " + request);
		}
   }
	
	 /**
     * process the message of gate status changed
     * GateFigure with id "nodeId" will be changed based on "status"
     * Notification from GALC server
     * @param nodeId
     * @param status
     */
    
    public void gateStatusChanged(int nodeId,int status){
        GateFigure figure =getDrawing().findGateFigure(nodeId);
        if(figure != null) {
        	figure.setGateStatus(status);
        	
            LabelFigure labelFigure = getDrawing().findLabelFigure(figure.getNode().getLaneName());
            if(labelFigure != null) labelFigure.refresh();

        }
        
    }
    
    public void gateUpdated(GtsNode node){
        getDrawing().updateGateFigure(node);
    }
    
    /**
     * process the lane carrier changed message
     * @param laneName - lane name
     * @param carrierNumbers - carrier list
     */
    
    public void laneCarrierChanged(String laneName,List<GtsLaneCarrier> carriers){
    	GtsLane lane = getModel().findLane(laneName);
        lane.setLaneCarriers(carriers);
        getDrawing().updateLaneCarriers(lane);
        
        AfonInforDialog.reloadData(laneName);
        
        // update the Move In progress
        
        GtsIndicator indicator = getModel().findMoveInIndicator(laneName);
        if(indicator != null) {
               getDrawing().updateMoveStatus(new GtsMove(indicator));
        }
    }
    
    
    public void associationChanged(GtsCarrier carrier){
        
      	for(GtsLane lane : model.getLanes()) {
      		boolean isChanged = false;
      		for(GtsLaneCarrier lc: lane.getLaneCarriers()){
      			if(lc.getCarrierId().equalsIgnoreCase(carrier.getCarrierNumber())) {
      				isChanged = true;
      				lc.setCarrier(carrier);
      				this.getDrawing().updateLaneCarriers(lc.getLane());
      			}
      		}
      		
      		if(isChanged) {
      			model.reloadLaneCarriers(lane);
      			this.getDrawing().updateLaneCarriers(lane);
      		}
        }
        
    }
    
	
	
	public void shapeRemoved(GtsShape shape){
	  ShapeFigure figure = getDrawing().findShapeFigure(shape.getId().getShapeId());
	  if(figure != null) getDrawing().removeShape(figure);
	}
	
	public void laneInfoUpdated(GtsLane lane){
	
//	  Lane aLane = getArea().findLane(lane.getLaneName());
//	  aLane.setDescription(lane.getDescription());
//	  aLane.setCapacity(lane.getCapacity());
//	  aLane.setLaneType(lane.getLaneType());

	}

	
	public void laneSegmentMapChanged(String laneId){
	  
//	  Lane lane = getArea().findLane(laneId);
//	  
//	  if (lane == null) return;
//	  List<LaneSegmentMap> maps = this.getRequestInvoker().getLaneSegmentMap(laneId);
//	  
//	  if(maps == null) return;
//	  
//	  lane.getLaneSegments().clear();
//	  for(LaneSegmentMap map: maps) {
//	      
//	      LaneSegment segment = getArea().findLaneSegment(map.getLaneSegmentId());
//	      
//	      if(segment != null) lane.addLaneSegment(segment);
//	      
//	  }
//	  
//	  lane.calculateCapacity();
//	  
//	  
//	  getDrawing().initCarriers();
	  
	  
	}


	@Override
	public void indicatorChanged(GtsIndicator indicator) {
		
		getModel().setIndicator(indicator);
		
		if(indicator.isMoveInProgress()){
            
            getDrawing().updateMoveStatus(new GtsMove(indicator));
            
        }else if(indicator.isMoveStatus()) {
            getDrawing().processLaneStatus(indicator.getLaneName(), indicator.isStatusOn());	
        } else {
            
        	if(indicator.getIndicatorType().equals(GtsIndicatorType.TOGGLE_LABEL)) {
        		getDrawing().toggleLabel(indicator);
        	}

        }
	}
	
	@Override
	public void carrierUpdated(List<GtsCarrier> carriers) {

		for(GtsLane lane: getModel().getLanes()) {
			if(hasCarrier(lane,carriers)) {
				getModel().reloadLaneCarriers(lane);
			}
		}
		getDrawing().initCarriers();

	}
	
	private boolean hasCarrier(GtsLane lane, List<GtsCarrier> carriers) {
		for (GtsLaneCarrier lc : lane.getLaneCarriers()) {
			for (GtsCarrier carrier : carriers) {
				if(carrier.getCarrierNumber().equalsIgnoreCase(lc.getCarrierId())) return true;
			}
		}
		return false;
	}


	@Override
	public void labelCreated(GtsLabel label) {
		model.getLabels().add(label);
		getDrawing().add(new LabelFigure(label));
	}
	
	public void labelUpdated(GtsLabel label) {
		List<GtsLabel> labels = model.getLabels();
		GtsLabel oldLabel = model.findLabel(label.getId().getLabelId());
		labels.remove(oldLabel);
		labels.add(label);
		
		LabelFigure figure = getDrawing().findLabelFigure(label.getId().getLabelId());
		if(figure != null) figure.setLabel(label);		
	}
	
	public void labelRemoved(GtsLabel label){
		LabelFigure figure = getDrawing().findLabelFigure(label.getId().getLabelId());
		if(figure != null) getDrawing().removeLabel(figure);
	}


	@Override
	public void laneSegmentCreated(GtsLaneSegment segment) {
//		 model.laneSegmentAdded(segment);
//		 getDrawing().addLaneSegment(segment); 
//		 getDrawing().refreshLayout();
	}

	
	public void laneSegmentRemoved(int segmentId) {
		  model.laneSegmentRemoved(segmentId);
		  
		  getDrawing().removeLaneSegmentFigure(segmentId);
		  
		  getDrawing().refreshLayout();

	}

	@Override
	public void laneSegmentsUpdated(List<GtsLaneSegment> segments) {
			  
//		  for(GtsLaneSegment segment:segments){
//		      LaneSegmentFigure figure = getDrawing().findLaneSegmentFigure(segment.getId().getLaneSegmentId());
//		      segment.setInputNode(getNode(segment.getInputNodeId()));
//		      segment.setOutputNode(getNode(segment.getOutputNodeId()));
//		      segment.setLane(figure.getLaneSegment().getLane());
//		      if(segment.getPowerFlag() != figure.getLaneSegment().getPowerFlag()) 
//		          figure.setPoweredFlag(segment.getPowerFlag());
//		      figure.getLaneSegment().replace(segment);
//		  }
//		  
//		  getDrawing().refreshLayout();
//			  
	}

	@Override
	public void moveStatusChanged(GtsMove move) {
		getDrawing().updateMoveStatus(move);
	}

	@Override
	public void nodeReplaced(int fromId, int toId) {
	}

	@Override
	public void productStatusChanged(List<GtsProduct> products) {
		getModel().updateLaneCarriers(products);
		getDrawing().refreshProductStatus();
	}

	@Override
	public void shapeCreated(GtsShape shape) {
		model.shapeAdded(shape);
		getDrawing().add(new ShapeFigure(shape));

	}
	
	public void shapeUpdated(GtsShape shape){
		model.shapeAdded(shape);
		ShapeFigure figure = getDrawing().findShapeFigure(shape.getId().getShapeId());
		if(figure != null)getDrawing().remove(figure);
		getDrawing().add(getDrawing().getMaxIndexOfShapes(),new ShapeFigure(shape));
	}
	
	@Override
	public void message(Message message) {
		trackingPanel.addMessage(message);
	    new MessageBox(message);
	}



	@Override
	public void nodeUpdated(GtsNode node) {
		getDrawing().refreshGateFigure(node);
	}

	@Override
	public void laneSegmentRemoved(GtsLaneSegment segment) {
		
	}
	
	protected Logger getLogger() {
		return getWindow().getLogger();
	}
	
}
