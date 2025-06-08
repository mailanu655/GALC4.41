package com.honda.galc.service.gts.pbs2;

import com.honda.galc.entity.enumtype.GtsInspectionStatus;
import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsProduct;

/**
 * LANE_11A to LANE_11B or LANE_11C
 * 
 * @author IS08925
 *
 */
public class GtsPbs2MoveDecisionRuleL extends GtsPbs2MoveDecisionRuleBase{

	public GtsPbs2MoveDecisionRuleL() {
		super();
	}
	
	public GtsPbs2TrackingServiceImpl getHandler() {
		return (GtsPbs2TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        boolean movePossible_11A_11B = isMovePossible(LANE_11A, LANE_11B);
        boolean movePossible_11A_11C = isMovePossible(LANE_11A, LANE_11C);
        
        if(!movePossible_11A_11B && !movePossible_11A_11C) return;

        GtsLane lane = area.findLane(LANE_18);
        if (lane.getLaneCarriers().isEmpty()) return;
        
     	GtsProduct product = lane.getLaneCarriers().get(0).getProduct();

    	if(product != null && product.getInspectionStatus().equals(GtsInspectionStatus.FAIL_S)) {
    		if(movePossible_11A_11C) {
    			issueMoveRequest(LANE_11A, LANE_11C);
    			return;
    		}else if(isLogicallyAndPhysicallyFule(LANE_11A) && movePossible_11A_11B) {
    			issueMoveRequest(LANE_11A, LANE_11B);
    		}
    		return;
    	}
    	
        // Carrier must have a FAIL_L, FAIL_M, PASS or WELD inspection status vehicle or it is empty
    	
    	if(movePossible_11A_11B) {
			issueMoveRequest(LANE_11A, LANE_11B);
			return;
		}else if(isLogicallyAndPhysicallyFule(LANE_11A) && movePossible_11A_11C) {
  			issueMoveRequest(LANE_11A, LANE_11C);
		}
        
    }  	

}
