package com.honda.galc.service.gts.pbs2;


import java.util.List;

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
public class GtsPbs2MoveDecisionRuleC extends GtsPbs2MoveDecisionRuleBase{

	public GtsPbs2MoveDecisionRuleC() {
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
        GtsProduct currentProduct = null;
        
        String[] outLanes = new String[] {LANE_8, LANE_9, LANE_20A, LANE_20C};
        
        for (String laneId : outLanes) {
        	if (currentProduct == null)
        		currentProduct = getTailProduct(laneId);
        	else break;      	
        }
                
        String[] inLanes = new String[] {LANE_12D, LANE_3, LANE_4};
        
        String moveOutLane = null;
        String oldestLot = null;
        
        for(String laneId : inLanes) {
        
	        if(isMovePossible(laneId)) {
	        	GtsLaneCarrier lc = area.findLane(laneId).getHeadCarrier();
	        	GtsProduct product = lc == null ? null :lc.getProduct();
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
	
	// if there is a body at lane 12D, not allow move 
	private boolean isMovePossible(String fromLane){
		if(LANE_4.equals(fromLane)) {
			if(!isLaneEmpty(LANE_12D)) return false;
		}
		
		return isMovePossible(fromLane, LANE_8);

	}
}
