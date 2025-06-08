package com.honda.galc.service.gts.pbs2;

import com.honda.galc.entity.enumtype.GtsInspectionStatus;
import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsProduct;

/**
 * LANE_18 to LANE_11A, LANE_11D
 * 
 * @author IS08925
 *
 */
public class GtsPbs2MoveDecisionRuleN extends GtsPbs2MoveDecisionRuleBase{

	public GtsPbs2MoveDecisionRuleN() {
		super();
	}
	
	public GtsPbs2TrackingServiceImpl getHandler() {
		return (GtsPbs2TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        boolean movePossible_18_11A = isMovePossible(LANE_18, LANE_11A);
        boolean movePossible_18_11D = isMovePossible(LANE_18, LANE_11D);
        
        if(!movePossible_18_11A && !movePossible_18_11D) return;
        
        GtsLane lane = area.findLane(LANE_18);
        if (lane.getLaneCarriers().isEmpty()) return;
        
     	GtsProduct product = lane.getLaneCarriers().get(0).getProduct();
     	
     	//Large repairs go down 11D and not into lane 11A
     	
     	if(product != null && product.getInspectionStatus().equals(GtsInspectionStatus.FAIL_L)) {
     		if(movePossible_18_11D) {
     			issueMoveRequest(LANE_18, LANE_11D);
     			return;
     		}
     	//Small or medium repair goes into lane 11A
     	}else if(product != null && (product.getInspectionStatus().equals(GtsInspectionStatus.FAIL_M)|| product.getInspectionStatus().equals(GtsInspectionStatus.FAIL_S))) {
     		if(movePossible_18_11A) {
     			issueMoveRequest(LANE_18, LANE_11A);
     			return;
     		}
     	}else {
     		if(movePossible_18_11D) {
     			issueMoveRequest(LANE_18, LANE_11D);
     		}
     	}
        

    }  	

}
