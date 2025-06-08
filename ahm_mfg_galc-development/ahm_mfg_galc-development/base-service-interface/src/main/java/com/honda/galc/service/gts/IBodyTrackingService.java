package com.honda.galc.service.gts;

import com.honda.galc.common.message.Message;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsMove;
import com.honda.galc.entity.gts.GtsNode;
import com.honda.galc.service.IoService;

/**
 * 
 * 
 * <h3>IBodyTrackingService Class description</h3>
 * <p> IBodyTrackingService description </p>
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
 * Jul 8, 2015
 *
 *
 */
public interface IBodyTrackingService extends IoService {
	
	public Message checkMovePossible(GtsMove move);
		
	public Message createMoveRequest(GtsMove move);
	
	public Message addCarrierByUser(String laneName,int position,String carrier);
	    
	public Message correctCarrierByUser(GtsLaneCarrier laneCarrier, String newLabel);
	
	public Message changeAssociation(String carrierId,String productId);
	 
	public Message removeCarrierByUser(GtsLaneCarrier laneCarrier,int position);
	
	public void toggleGateStatus(GtsNode node);
	
	public void refreshProductDefectStatus(String productId);
	
	public void updateProductInspectionStatus(String productId, int inspectionStatus);
	
	public Message updateCarrierType(String carrierId, int carrierType);
	
	public void refreshPLCIndictors();
		   
	
}
