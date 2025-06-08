package com.honda.galc.service.gts.pbs2;

import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsProduct;

/**
 * The main priority on this rule is to feed released, non-repair cars to AF as quickly as possible.
 * The requirement for lane 9 to be empty has been removed.
 * Repair cars will not be moved automatically. Users will have to move them manually.
 * This is to reduce the half-gap situations that production has been experiencing since ramping up to 650 per day. 
 * @author IS08925
 *
 */
public class GtsPbs2MoveDecisionRuleC3 extends GtsPbs2MoveDecisionRuleBase{

	public GtsPbs2MoveDecisionRuleC3() {
		super();
	}
	
	public GtsPbs2TrackingServiceImpl getHandler() {
		return (GtsPbs2TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        if(!isMoveInPossible(LANE_8)) return;
        
        GtsProduct currentProduct = getTailProduct(LANE_8);
        if(currentProduct == null) currentProduct = getTailProduct(LANE_9);
        
        String[] lanes = new String[] {LANE_12D, LANE_3, LANE_4};
        
        String moveOutLane = null;
        String oldestLot = null;
        
        for(String laneId : lanes) {
        
	        if(isMoveInPossible(laneId, LANE_8)) {
	        	GtsLaneCarrier lc = area.findLane(laneId).getHeadCarrier();
	        	GtsProduct product = lc.getProduct();
	        	if(isProductReleased(product)) {
	        		if(currentProduct != null && product.getLotNumber().equalsIgnoreCase(currentProduct.getLotNumber())) {
	        			moveOutLane = laneId;
	        			oldestLot = null;
	        			break;
	        		}else {
	        			if(oldestLot == null || product.getLotNumber().compareTo(oldestLot) < 0) {
	        				oldestLot = product.getLotNumber();
	        				moveOutLane = laneId;
	        			}
	        		}
	        	}
	        }
        }
        
        if(moveOutLane != null) issueMoveRequest(moveOutLane, LANE_8);
        
    }  	

}
