package com.honda.galc.service.gts.pbs;

import com.honda.galc.entity.gts.GtsArea;

/**
 * 
 * 
 * <h3>GtsPbs1MoveDecisionRule13A Class description</h3>
 * <p> GtsPbs1MoveDecisionRule13A description </p>
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
public class GtsPbs1MoveDecisionRule13A extends GtsPbs1MoveDecisionRuleBase{

	public GtsPbs1MoveDecisionRule13A() {
		super();
	}
	
	public GtsPbs1MoveDecisionRule13A(GtsPbs1TrackingServiceImpl handler) {
		super(handler);
	}
	
	public GtsPbs1TrackingServiceImpl getHandler() {
		return (GtsPbs1TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        boolean movePossible_pI_pO = isMovePossible(LANE_pI, LANE_pO);
        boolean movePossible_pG_pO = isMovePossible(LANE_pG, LANE_pO);
        
        if (!movePossible_pI_pO && !movePossible_pG_pO) return;

        // Check for a current lot vehicle at the head of a storage lane
        
        String currentLot = this.getCurrentLot(LANE_pO);
       
        if(this.isSameReleasedHeadLot(movePossible_pI_pO, LANE_pI, currentLot)) {
        	issueMoveRequest(LANE_pI, LANE_pO);
     		return;
        }else if(this.isSameReleasedHeadLot(movePossible_pG_pO, LANE_pG, currentLot)) {
        	issueMoveRequest(LANE_pG, LANE_pO);
     		return;
        }
        
        if(movePossible_pI_pO) {
        	issueMoveRequest(LANE_pI, LANE_pO);
     		return;
        }
        
        if(movePossible_pG_pO) {
        	issueMoveRequest(LANE_pG, LANE_pO);
     		return;
        }
        
    }    

}
