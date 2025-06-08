package com.honda.galc.service.gts.pbs2;

import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsProduct;

/*
 * LANE_9 to LANE_20A, LANE_19 to LANE_20A
 * 
 * Current Lot from LANE_9, LANE_19 moves to LANE_20A first.
 * Otherwise Oldest lot moves to LANE_20A.
 * 
 */
public class GtsPbs2MoveDecisionRuleE extends GtsPbs2MoveDecisionRuleBase{

	public GtsPbs2MoveDecisionRuleE() {
		super();
	}
	
	public GtsPbs2TrackingServiceImpl getHandler() {
		return (GtsPbs2TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        boolean movePossible_19_20A = isMovePossible(LANE_19, LANE_20A);
        boolean movePossible_9_20A = isMovePossible(LANE_9, LANE_20A);
        if (!movePossible_9_20A && !movePossible_19_20A) return;
                
        if(movePossible_9_20A && movePossible_19_20A) {        	
            GtsProduct currentProduct = null;
            String currentLotNumber = "                    ";
            GtsLaneCarrier currentLaneCarrier =null;
       
            String[] toLanes = new String[] {LANE_20A, LANE_20C};   
            
            for (String toLaneId: toLanes) {
            	
            	currentLaneCarrier = getTailOfLaneCarrier(toLaneId);
            	
            	if (currentLaneCarrier != null) {
            		currentProduct = currentLaneCarrier.getProduct();
            		if (currentProduct != null)
            		currentLotNumber= currentProduct.getLotNumber();
            		break;
            	}   	
            }
                        
            String[] lanes = new String[] {LANE_19, LANE_9};
            
            String moveOutLane = null;
            String oldestLot = null;
            
            for(String laneId : lanes) {
            
	        	GtsLaneCarrier lc = area.findLane(laneId).getHeadCarrier();
	        	GtsProduct product = lc.getProduct();
	        	String headLotNumber = "                    ";
	        	
	        	if (product != null) headLotNumber=product.getLotNumber();
	        	
	        	if(headLotNumber.equalsIgnoreCase(currentLotNumber)) {	
	        			moveOutLane = laneId;
	        			oldestLot = null;
	        			break;
	        	}else {
	        			if(oldestLot == null || headLotNumber.compareTo(oldestLot) < 0) {
	        				oldestLot = headLotNumber;
	        				moveOutLane = laneId;
	        			}
	        		}
	        	} 
            
            if(moveOutLane != null) issueMoveRequest(moveOutLane, LANE_20A);
            return;
        }else if(movePossible_19_20A) {
        	issueMoveRequest(LANE_19, LANE_20A);
        	return;
        }else if(movePossible_9_20A) {
        	issueMoveRequest(LANE_9, LANE_20A);
        } 
    }  	
}
