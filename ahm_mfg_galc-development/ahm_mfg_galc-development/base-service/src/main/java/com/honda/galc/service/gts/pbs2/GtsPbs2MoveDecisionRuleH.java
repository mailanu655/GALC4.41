package com.honda.galc.service.gts.pbs2;

import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.gts.GtsProduct;
import com.honda.galc.entity.enumtype.GtsInspectionStatus;

/**
 * 
 * INPUT LANES: 10, 11D, 5B, 6, 7 
 * OUTPUT LANES: 12A, 12D
 * 1 - move released good vin from LANE_11D to LANE_12D or from LANE_10 to LANE_12D if lane 8, 9, 20A and 20C are not all empty
 * 2 - otherwise release carrier from lane 11D and lane 10 to lane 12A
 * 		at same time, release released good vin from lane 5B, 6 and 7 to 12D by lot
 *  
 * @author IS08925
 * modified by IS08054
 */

public class GtsPbs2MoveDecisionRuleH extends GtsPbs2MoveDecisionRuleBase{

	public GtsPbs2MoveDecisionRuleH() {
		super();
	}
	
	public GtsPbs2TrackingServiceImpl getHandler() {
		return (GtsPbs2TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        //Priority decision for lane goes to a current lot car at lane 11D  So try to move from 11D to 12D

        // find current lot at LANE_8 / LANE_9/ LANE_20A/LANE_20C
        
        String[] currentLotLanes = new String[] {LANE_8, LANE_9, LANE_20A, LANE_20C};
                  		       
        String currentLot = getCurrentProductLot(currentLotLanes);
        
        // if not all lane 8, 9, 20A and 20C are empty
     
        if (currentLot != null) {
        	if(isMovePossible(LANE_11D, LANE_12D) ) {
        		GtsProduct product_11D =getHeadProduct(LANE_11D);
        		if (isProductReleased(product_11D) && (product_11D.getInspectionStatus() == GtsInspectionStatus.PASS || product_11D.getInspectionStatus() == GtsInspectionStatus.WELD)
        			||getHeadOfLaneCarrier(LANE_11D).isEmptyCarrier())
        			{issueMoveRequest(LANE_11D, LANE_12D);   
        			return;   
        			}
        	}
   	
        	if(isMovePossible(LANE_10, LANE_12D) ) {
        		GtsProduct product_10 =getHeadProduct(LANE_10);
        		if (isProductReleased(product_10) && (product_10.getInspectionStatus() == GtsInspectionStatus.PASS || product_10.getInspectionStatus() == GtsInspectionStatus.WELD)
        			||getHeadOfLaneCarrier(LANE_10).isEmptyCarrier())
        			{issueMoveRequest(LANE_10, LANE_12D);   
        			return;   
        			}
        	}
        }
        //If there move possible to 12A from 11D or 10
        
        boolean movePossible_11D_12A = isMovePossible(LANE_11D, LANE_12A);
        boolean movePossible_10_12A = isMovePossible(LANE_10, LANE_12A);
        
        if(movePossible_11D_12A) {
    		issueMoveRequest(LANE_11D, LANE_12A);
        }else if(movePossible_10_12A) {
    		issueMoveRequest(LANE_10, LANE_12A);
        }
        
        //Try just issuing a move from 5B, 6 or 7 to lane 12D
        //A bit of a hassle but you need to ensure that every input lane has the proper lane status to allow a move
        //and can't use PbsLaneState::getReleasedPassedInputLanes because it wants to evaluate all input lanes in decision point
        
        // Move vehicles with current Lot 
        if(moveReleasedVehicleWithCurrentLot(currentLot, LANE_5B, LANE_12D)) return;
        if(moveReleasedVehicleWithCurrentLot(currentLot, LANE_6, LANE_12D)) return;
        if(moveReleasedVehicleWithCurrentLot(currentLot, LANE_7, LANE_12D)) return;
 
        // check oldest lot and move it to LANE_12D
        String laneName = getOldestLotLane(new String[] {LANE_5B,LANE_6,LANE_7});
        
        if(laneName != null) issueMoveRequest(laneName, LANE_12D);        
        
	}

	private String getCurrentProductLot(String[] currentLotLanes) {
		GtsCarrier currentCarrier = null;
        String currentLot = null;
        for (String laneId: currentLotLanes) {
        	if (getTailOfLaneCarrier(laneId) != null) {
        	currentCarrier = getTailOfLaneCarrier(laneId).getCarrier();
        	if(currentCarrier != null) {
        		if(currentCarrier.getProduct() != null) 
        			currentLot = currentCarrier.getProduct().getLotNumber();
        		else currentLot="";
        		break;       		
        		}
        	else {
        		currentLot="";
        		break;      		  
        	}
        	}
        }
		return currentLot;
	}
	
	private boolean moveReleasedVehicleWithCurrentLot(String currentProdLot, String fromLane, String toLane) {
		if(currentProdLot == null || currentProdLot == "") return false;
		
		if(isMovePossible(fromLane, toLane))  {
			GtsProduct product = getHeadProduct(fromLane);
	       	if(isProductReleased(product) && 
	       	    product.getLotNumber().compareTo(currentProdLot) == 0 ) {
        		issueMoveRequest(fromLane, toLane);
        		return true;
	       	}
		}   	
	    return false;
	}//
	
	private String getOldestLotLane(String[] laneIds) {
		
		String lotNumber = null; // start biggest lot number 
		String laneName = null;
		for(String laneId : laneIds) {
	        if(isMovePossible(laneId, LANE_12D)) {
	        	GtsProduct product = getHeadProduct(laneId);
	        	if(isProductReleased(product)) {
				if(lotNumber == null || product.getLotNumber().compareTo(lotNumber) < 0) {
					lotNumber = product.getLotNumber();
					laneName = laneId;
					}
	        	}
	        }
		}
		
		return laneName;
		
	}
}
