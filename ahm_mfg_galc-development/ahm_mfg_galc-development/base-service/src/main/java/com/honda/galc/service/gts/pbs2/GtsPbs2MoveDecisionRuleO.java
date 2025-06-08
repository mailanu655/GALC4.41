package com.honda.galc.service.gts.pbs2;

import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsProduct;

/**
 * 1.	Empty carrier at lane 20D only can move to 21B
 * 2.	We only can issue MR_20D-22 for a good car, if Exit_20D=1, Entry_22S=1, Entry_22=1,Entry_21B=0, Exit_30A=Exit_30B=Exit_30C=0
 * 3.	If movePossible(20D, 21B), issue MR_20D-21B and at the same time try to issue MR_X-22 by following the current decision point logic below
 * @author IS08925
 *
 */
public class GtsPbs2MoveDecisionRuleO extends GtsPbs2MoveDecisionRuleBase{

	public GtsPbs2MoveDecisionRuleO() {
		super();
	}
	
	public GtsPbs2TrackingServiceImpl getHandler() {
		return (GtsPbs2TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        boolean isShortPathOpen = !isExitGateOpen(LANE_30C) && !isExitGateOpen(LANE_30B) && !isExitGateOpen(LANE_30A) &&
        						   !isEntryGateOpen(LANE_21B) && isEntryGateOpen(LANE_22) && isExitGateOpen(LANE_20S);
        
        GtsLaneCarrier lc = getHeadOfLaneCarrier(LANE_20D);
        
        // check if we can move good carrier to LANE_22 directly through short path
        
        if(isShortPathOpen && isMovePossible(LANE_20D, LANE_22) && isGoodProduct(lc)) {
        	issueMoveRequest(LANE_20D, LANE_22);
        	return;
        }
        
        // good car and empty move to 21B
        
        boolean movePossible_20D_21B = isMovePossible(LANE_20D, LANE_21B);
        if(movePossible_20D_21B && (isGoodProduct(lc) || lc.isEmptyCarrier())) {
        	issueMoveRequest(LANE_20D, LANE_21B);
        }
               
        // current lot in LANE_30A, LANE_30B, LANE_30C goes first
        
        boolean movePossible_30A_22 = isMovePossible(LANE_30A, LANE_22) && isGoodProductNoPauseNoWeld(getHeadOfLaneCarrier(LANE_30A));
        boolean movePossible_30B_22 = isMovePossible(LANE_30B, LANE_22) && isGoodProductNoPauseNoWeld(getHeadOfLaneCarrier(LANE_30B));
        boolean movePossible_30C_22 = isMovePossible(LANE_30C, LANE_22) && isGoodProductNoPauseNoWeld(getHeadOfLaneCarrier(LANE_30C));
        
        if (!movePossible_30A_22 && !movePossible_30B_22 && !movePossible_30C_22) return;
        		
        GtsProduct product = getTailNonNullProduct(LANE_23A);
        if(product == null) product = getTailNonNullProduct(LANE_23B);
        
        String currentLot = product == null ? null : product.getLotNumber(); 
        
        if(currentLot != null) {
        	
           if(movePossible_30A_22 && getHeadOfLaneCarrier(LANE_30A).getProductionLot().equals(currentLot) ) {
               		issueMoveRequest(LANE_30A, LANE_22);
            		return;
        		}
           if(movePossible_30B_22 && getHeadOfLaneCarrier(LANE_30B).getProductionLot().equals(currentLot)) {
                   	issueMoveRequest(LANE_30B, LANE_22);
                	return;
            	}
           if(movePossible_30C_22) {
                 if(getHeadOfLaneCarrier(LANE_30C).getProductionLot().equals(currentLot)) {
                    issueMoveRequest(LANE_30C, LANE_22);
                    return;
                }
        	}
         }
        	       	       	
    	// current lot at lane head of lane but not physically possible (no carrier present)
    	
    	boolean movePhysicallyPossible_30A_22 = isMoveConditionOk(LANE_30A, LANE_22) && isMoveInPossible(LANE_22) && isMoveOutPhysicallyPossible(LANE_30A);
    	boolean movePhysicallyPossible_30B_22 = isMoveConditionOk(LANE_30B, LANE_22) && isMoveInPossible(LANE_22) && isMoveOutPhysicallyPossible(LANE_30B);
    	boolean movePhysicallyPossible_30C_22 = isMoveConditionOk(LANE_30C, LANE_22) && isMoveInPossible(LANE_22) && isMoveOutPhysicallyPossible(LANE_30C);
    	
    	boolean movePhysicallyPossible_21B_30A = isMoveToBeReady(LANE_21B, LANE_30A);
    	boolean movePhysicallyPossible_21B_30B = isMoveToBeReady(LANE_21B, LANE_30B);
    	boolean movePhysicallyPossible_21B_30C = isMoveToBeReady(LANE_21B, LANE_30C);
    	
    	boolean movePhysicallyPossible_20D_21B = isMoveToBeReady(LANE_20D, LANE_21B);
    	boolean movePhysicallyPossible_20C_20D = isMoveToBeReady(LANE_20C, LANE_20D);
    	    	
       	boolean movePhysicallyPossible_21B_22 = ((movePhysicallyPossible_30A_22 && isLaneEmpty(LANE_30A) && movePhysicallyPossible_21B_30A) ||
                (movePhysicallyPossible_30B_22 && isLaneEmpty(LANE_30B) && movePhysicallyPossible_21B_30B) ||
                (movePhysicallyPossible_30C_22 && isLaneEmpty(LANE_30C) && movePhysicallyPossible_21B_30C) );
      	
    	boolean movePhysicallyPossible_20D_22 = movePhysicallyPossible_21B_22 && isLaneEmpty(LANE_21B)&& movePhysicallyPossible_20D_21B;
   
    	boolean movePhysicallyPossible_20C_22 = movePhysicallyPossible_20D_22 && isLaneEmpty(LANE_20D)&& movePhysicallyPossible_20C_20D;

    	String lowestLot = "ZZZ";
    	
    	if (currentLot == null) currentLot="ZZZ";
    	
    	String tmpLot = checkLowestLotNoPauseNoWeld(movePhysicallyPossible_30A_22,LANE_30A,currentLot,lowestLot);
    	if(currentLot.equals(tmpLot)) return;
    	else if(tmpLot != null) lowestLot = tmpLot;
  
    	tmpLot = checkLowestLotNoPauseNoWeld(movePhysicallyPossible_30B_22,LANE_30B,currentLot,lowestLot);
    	if(currentLot.equals(tmpLot)) return;
    	else if(tmpLot != null) lowestLot = tmpLot;

    	tmpLot = checkLowestLotNoPauseNoWeld(movePhysicallyPossible_30C_22,LANE_30C,currentLot,lowestLot);
    	if(currentLot.equals(tmpLot)) return;
    	else if(tmpLot != null) lowestLot = tmpLot;

      	tmpLot = checkLowestLot(movePhysicallyPossible_21B_22,LANE_21B,currentLot,lowestLot);
    	if(currentLot.equals(tmpLot)) return;
    	else if(tmpLot != null) lowestLot = tmpLot;

      	tmpLot = checkLowestLot(movePhysicallyPossible_20D_22,LANE_20D,currentLot,lowestLot);
    	if(currentLot.equals(tmpLot)) return;
    	else if(tmpLot != null) lowestLot = tmpLot;

      	tmpLot = checkLowestLot(movePhysicallyPossible_20C_22,LANE_20C,currentLot,lowestLot);
    	if(currentLot.equals(tmpLot)) return;
    	else if(tmpLot != null) lowestLot = tmpLot;
        
    	 if(movePossible_30A_22 && lowestLot.equals(getHeadProduct(LANE_30A).getLotNumber())) {
         	issueMoveRequest(LANE_30A, LANE_22);
         	return;
         }
         
         if(movePossible_30B_22 && lowestLot.equals(getHeadProduct(LANE_30B).getLotNumber())) {
         	issueMoveRequest(LANE_30B, LANE_22);
         	return;
         }
         
         if(movePossible_30C_22 && lowestLot.equals(getHeadProduct(LANE_30C).getLotNumber())) {
         	issueMoveRequest(LANE_30C, LANE_22);
         	return;
         }         	      
    }  
}
