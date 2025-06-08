package com.honda.galc.service.gts.pbs;

import com.honda.galc.entity.enumtype.GtsDefectStatus;
import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsProduct;

/**
 * 
 * 
 * <h3>GtsPbs1MoveDecisionRule18A Class description</h3>
 * <p> GtsPbs1MoveDecisionRule18A description </p>
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
  * @author hcm_adm_008925<br>
 * Sep 24, 2018
 *
 *
 */
public class GtsPbs1MoveDecisionRule18A extends GtsPbs1MoveDecisionRuleBase{

	public GtsPbs1MoveDecisionRule18A() {
		super();
	}
	
	public GtsPbs1MoveDecisionRule18A(GtsPbs1TrackingServiceImpl handler) {
		super(handler);
	}
	
	public GtsPbs1TrackingServiceImpl getHandler() {
		return (GtsPbs1TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        boolean movePossible_tX_tF= isMovePossible(LANE_tX, LANE_tF);
        boolean movePossible_tX_wQ = isMovePossible(LANE_tX, LANE_wQ);
        
        if (!movePossible_tX_tF && !movePossible_tX_wQ)
        	return;
        
        GtsProduct headProduct_tX = area.findLane(LANE_tX).getHeadCarrier().getProduct();
        
        if (headProduct_tX != null) {
        	 GtsDefectStatus headDefectStatus_tX = headProduct_tX.getDefectStatus();             
             if (headDefectStatus_tX == GtsDefectStatus.OUTSTANDING || headDefectStatus_tX == GtsDefectStatus.SCRAP ) { 
             	if(movePossible_tX_wQ) issueMoveRequest(LANE_tX, LANE_wQ);
             	return;
                }    	
        }
         
        if(movePossible_tX_tF) {
        	issueMoveRequest(LANE_tX, LANE_tF);
        	return;
        }       

        if(movePossible_tX_wQ ) {	   	
        	issueMoveRequest(LANE_tX, LANE_wQ);
     		return;
        	}
    }       
}

