package com.honda.galc.service.gts.pbs2;

import com.honda.galc.entity.enumtype.GtsInspectionStatus;
import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsLaneCarrier;

/**
 * 
 * @author IS08925
 *
 */
public class GtsPbs2MoveDecisionRuleM extends GtsPbs2MoveDecisionRuleBase{

	public GtsPbs2MoveDecisionRuleM() {
		super();
	}
	
	public GtsPbs2TrackingServiceImpl getHandler() {
		return (GtsPbs2TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        if(!isExitGateOpen(LANE_21B)) return;
        
        int moveCount = 0;
        
        GtsLaneCarrier lc = getHeadOfLaneCarrier(LANE_21C);
        
        boolean movePossible_21B_30A = isMovePossible(LANE_21B, LANE_30A);
        boolean movePossible_21B_30B = isMovePossible(LANE_21B, LANE_30B) && lc == null;
        boolean movePossible_21B_30C = isMovePossible(LANE_21B, LANE_30C) && lc == null;
        boolean movePossible_21B_21C = isMovePossible(LANE_21B, LANE_21C);
        
        if(movePossible_21B_30A) moveCount++;
        if(movePossible_21B_30B) moveCount++;
        if(movePossible_21B_30C) moveCount++;
        if(movePossible_21B_21C) moveCount++;
                
        boolean moveInPossible_21E = movePossible_21B_21C &&
        		!this.isPhysicallyFull(LANE_21E) && !this.isLogicallyFull(LANE_21E);
        		
        if(moveCount == 0) return;
        
        GtsLaneCarrier lc_21B = getHeadOfLaneCarrier(LANE_21B);
        
        if(lc_21B == null) return;
        
        if(lc_21B.isEmptyCarrier()) {
        	if(movePossible_21B_21C) issueMoveRequest(LANE_21B, LANE_21C);
        	return;
        }
        
        if(lc_21B.isUnknownCarrier() || lc_21B.getProduct() == null || 
                lc_21B.getProduct().getInspectionStatus() == GtsInspectionStatus.STOP || 
                lc_21B.getProduct().getInspectionStatus() == GtsInspectionStatus.WELD || 
                lc_21B.getProduct().getInspectionStatus() == GtsInspectionStatus.UNKNOWN) {
        	return;
        }
        
        String lot_21B = lc_21B.getProduct().getLotNumber();
        
        // moveCount > = 1
        if(moveCount == 1 || (isLaneEmpty(LANE_30A) && isLaneEmpty(LANE_30B) && isLaneEmpty(LANE_30C) &&  isLaneEmpty(LANE_21C) &&  isLaneEmpty(LANE_21E))) {
        	if(movePossible_21B_30A) {
        		issueMoveRequest(LANE_21B, LANE_30A);
        		return;
        	}
        	
        	if(movePossible_21B_30B) {
        		issueMoveRequest(LANE_21B, LANE_30B);
        		return;
        	}
        	
        	if(movePossible_21B_30C) {
        		issueMoveRequest(LANE_21B, LANE_30C);
        		return;
        	}
        	if(movePossible_21B_21C) {
        		issueMoveRequest(LANE_21B, LANE_21C);
        		return;
        	}
        }
        
        // moveCount is > 1
        
        String biggestLot = "";
        String biggestLotLane = null;
        
        String smallestLot = "ZZZ";
        String smallestLotLane = null;

        String tmpLot = "";
               
        tmpLot = getBiggestLot(LANE_30A);
        if(!tmpLot.isEmpty() && tmpLot.compareTo(biggestLot) > 0) {
        	biggestLot = tmpLot;
        	if(movePossible_21B_30A) 
        		biggestLotLane = LANE_30A;
        }
        
        if(movePossible_21B_30A && !tmpLot.isEmpty() && tmpLot.compareTo(smallestLot) < 0) {
        	smallestLot = tmpLot;
        	smallestLotLane = LANE_30A;
        }
           
      tmpLot = getBiggestLot(LANE_30B);
        if(!tmpLot.isEmpty() ) {
        	if (tmpLot.compareTo(biggestLot) > 0) {
        	biggestLot = tmpLot;
           	if(movePossible_21B_30B )
           		biggestLotLane = LANE_30B;
           	else biggestLotLane = null;
           	}
        	if (tmpLot.compareTo(biggestLot) == 0)
        		if(movePossible_21B_30B && biggestLotLane == null )
               		biggestLotLane = LANE_30B;        		
        } 
        
        
        if(movePossible_21B_30B && !tmpLot.isEmpty() && tmpLot.compareTo(smallestLot) < 0) {
        	smallestLot = tmpLot;
        	smallestLotLane = LANE_30B;
        }
            
        tmpLot = getBiggestLot(LANE_30C);
        if(!tmpLot.isEmpty() ) {
        	if (tmpLot.compareTo(biggestLot) > 0) {
        	biggestLot = tmpLot;
           	if(movePossible_21B_30C )
           		biggestLotLane = LANE_30C;
           	else biggestLotLane = null;
           	}
        	if (tmpLot.compareTo(biggestLot) == 0)
        		if(movePossible_21B_30C && biggestLotLane == null )
               		biggestLotLane = LANE_30C;        		
        } 
        
        if(movePossible_21B_30C && !tmpLot.isEmpty() && tmpLot.compareTo(smallestLot) < 0) {
        	smallestLot = tmpLot;
        	smallestLotLane = LANE_30C;
        }
          
        tmpLot = getBiggestLot(LANE_21E);
        if(!tmpLot.isEmpty() ) {
        	if (tmpLot.compareTo(biggestLot) > 0) {
        	biggestLot = tmpLot;
           	if(movePossible_21B_21C )
           		biggestLotLane = LANE_21C;
           	else biggestLotLane = null;
           	}
        	if (tmpLot.compareTo(biggestLot) == 0)
        		if(movePossible_21B_21C && biggestLotLane == null )
               		biggestLotLane = LANE_21C;        		
        } 
        
        if(moveInPossible_21E && !tmpLot.isEmpty() && tmpLot.compareTo(smallestLot) < 0) {
        	smallestLot = tmpLot;
        	smallestLotLane = LANE_21C;
        }
        
        tmpLot = getBiggestLot(LANE_21C);
        if(!tmpLot.isEmpty() ) {
        	if (tmpLot.compareTo(biggestLot) > 0) {
        	biggestLot = tmpLot;
           	if(movePossible_21B_21C )
           		biggestLotLane = LANE_21C;
           	else biggestLotLane = null;
           	}
        	if (tmpLot.compareTo(biggestLot) == 0)
        		if(movePossible_21B_21C && biggestLotLane == null )
               		biggestLotLane = LANE_21C;        		
        } 
        
        tmpLot = getBiggestLot(LANE_22);
        if(!tmpLot.isEmpty() && tmpLot.compareTo(biggestLot) > 0) {
        	biggestLot = tmpLot;
        	biggestLotLane = null;
        }
        
        tmpLot = getBiggestLot(LANE_23A);
        if(!tmpLot.isEmpty() && tmpLot.compareTo(biggestLot) > 0) {
        	biggestLot = tmpLot;
        	biggestLotLane = null;
        }
 
        tmpLot = getBiggestLot(LANE_23B);
        if(!tmpLot.isEmpty() && tmpLot.compareTo(biggestLot) > 0) {
        	biggestLot = tmpLot;
        	biggestLotLane = null;
        }
 
        if(lot_21B.compareTo(biggestLot) == 0) {
        	// current lot , move to lane with the same lot
        	if (!(biggestLotLane == null)) {
        		issueMoveRequest(LANE_21B, biggestLotLane);
        		return;
        	}
        }else if(lot_21B.compareTo(biggestLot) > 0) {
        	// New lot, move to lane with lowest lot
        	if (!(smallestLotLane == null ) && isMoveInPossible(LANE_21B, smallestLotLane)) {
        		issueMoveRequest(LANE_21B, smallestLotLane);
        		return;
        	}
        }else if (lot_21B.compareTo(biggestLot) < 0){
        	// Delayed lot, move to lane with greatest space
        	
        	String biggestLane = null;
        	int biggest = 0; 
        	
        	if(movePossible_21B_30A)  {
	         	int tmp = area.findLane(LANE_30A).getAvailableSpaces();
	        	if(tmp > biggest ) {
	        		biggest = tmp;
	        		biggestLane = LANE_30A;
	        	}
        	}
        	
        	if(movePossible_21B_30B)  {
	         	int tmp = area.findLane(LANE_30B).getAvailableSpaces();
	        	if(tmp > biggest ) {
	        		biggest = tmp;
	        		biggestLane = LANE_30B;
	        	}
        	}	
        	
        	if(movePossible_21B_30C)  {
	         	int tmp = area.findLane(LANE_30C).getAvailableSpaces();
	        	if(tmp > biggest ) {
	        		biggest = tmp;
	        		biggestLane = LANE_30C;
	        	}
        	}	

        	if(movePossible_21B_21C)  {
	         	int tmp = area.findLane(LANE_21E).getAvailableSpaces();
	        	if(tmp > biggest ) {
	        		biggest = tmp;
	        		biggestLane = LANE_21C;
	        	}
        	}	
        	
        	if(biggestLane != null) {
        		issueMoveRequest(LANE_21B, biggestLane);
        		return;
        	}
        	
        }
        
        // other 
        
        if(movePossible_21B_30A ) {
        	issueMoveRequest(LANE_21B, LANE_30A);
        	return;
        }
        
        if(movePossible_21B_30B ) {
        	issueMoveRequest(LANE_21B, LANE_30B);
        	return;
        }
        
        if(movePossible_21B_30C ) {
        	issueMoveRequest(LANE_21B, LANE_30C);
        	return;
        }
        
        if(movePossible_21B_21C ) {
        	issueMoveRequest(LANE_21B, LANE_21C);
        	return;
        }
	}
	

}
