package com.honda.galc.notification.service;

import java.util.List;

import com.honda.galc.common.message.Message;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsIndicator;
import com.honda.galc.entity.gts.GtsLabel;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsLaneSegment;
import com.honda.galc.entity.gts.GtsMove;
import com.honda.galc.entity.gts.GtsNode;
import com.honda.galc.entity.gts.GtsProduct;
import com.honda.galc.entity.gts.GtsShape;

/**
 * 
 * 
 * <h3>IGtsNotificationService Class description</h3>
 * <p> IGtsNotificationService description </p>
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
 * Jun 9, 2015
 *
 *
 */
public interface IGtsNotificationService extends INotificationService{
	
	public void gateStatusChanged(int nodeId,int status);
	
	public void carrierUpdated(List<GtsCarrier> carriers);
	
	public void laneCarrierChanged(String laneName,List<GtsLaneCarrier> carriers);
	
	public void labelCreated(GtsLabel label);
	
	public void labelUpdated(GtsLabel label);
	
	public void labelRemoved(GtsLabel label);
	
	public void shapeCreated(GtsShape shape);
	
	public void shapeUpdated(GtsShape shape);
	
	public void shapeRemoved(GtsShape shape);
	
	public void laneSegmentCreated(GtsLaneSegment segment);
	
	public void laneSegmentsUpdated(List<GtsLaneSegment> segments);
	
	public void laneSegmentRemoved(GtsLaneSegment segment);
	
	public void laneSegmentMapChanged(String laneId);
	
	public void nodeUpdated(GtsNode node);
	
	public void nodeReplaced(int fromId,int toId);
	    
	public void indicatorChanged(GtsIndicator indicator);
		   
	public void moveStatusChanged(GtsMove move);
	
	public void productStatusChanged(List<GtsProduct> products);
	
	public void associationChanged(GtsCarrier carrier);
	
	public void message(Message message);
	
}
