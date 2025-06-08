package com.honda.galc.service.gts.pbs2;

import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsProduct;

/*
 * LANE_8 to LANE_9 or LANE_10
 * released vehicle to LANE_9
 * Non-released vehicle to LANE_10
 */

public class GtsPbs2MoveDecisionRuleD extends GtsPbs2MoveDecisionRuleBase{

	public GtsPbs2MoveDecisionRuleD() {
		super();
	}
	
	public GtsPbs2TrackingServiceImpl getHandler() {
		return (GtsPbs2TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        if(!isMoveOutPossible(LANE_8)) return;
        
      	GtsProduct product = getHeadProduct(LANE_8);
     	if(isProductReleased(product)) {
     		if(isMovePossible(LANE_8, LANE_9)) {
     			issueMoveRequest(LANE_8, LANE_9);
     			return;
     		}
     	}else {
     		if(isMovePossible(LANE_8, LANE_10)) {
     			issueMoveRequest(LANE_8, LANE_10);
     			return;
     		}
     	}
        
    }  	

}
