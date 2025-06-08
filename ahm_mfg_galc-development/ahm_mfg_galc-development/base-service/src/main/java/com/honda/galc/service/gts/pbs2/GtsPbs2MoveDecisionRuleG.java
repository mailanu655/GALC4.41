package com.honda.galc.service.gts.pbs2;

import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsProduct;

/**
 * 
 * 
 * <h3>GtsPbs1MoveDecisionRule01A Class description</h3>
 * <p> GtsPbs1MoveDecisionRule01A description </p>
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
 * Oct.26, 2018
 *
 *
 */
public class GtsPbs2MoveDecisionRuleG extends GtsPbs2MoveDecisionRuleBase{

	public GtsPbs2MoveDecisionRuleG() {
		super();
	}
	
	public GtsPbs2TrackingServiceImpl getHandler() {
		return (GtsPbs2TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        boolean movePossible_21E_23A = isMovePossible(LANE_21E, LANE_23A);
        boolean movePossible_22_23A = isMovePossible(LANE_22, LANE_23A);
        boolean movePossible_22A_23A = isMovePossible(LANE_22A, LANE_23A);
        
        GtsLaneCarrier lc_21E = getHeadOfLaneCarrier(LANE_21E);
        GtsLaneCarrier lc_22 = getHeadOfLaneCarrier(LANE_22);
        GtsLaneCarrier lc_22A = getHeadOfLaneCarrier(LANE_22A);
        
        if(movePossible_21E_23A && isHeadOfLaneBadProduct(lc_21E) || movePossible_22_23A && isHeadOfLaneBadProduct(lc_22) || movePossible_22A_23A && isHeadOfLaneBadProduct(lc_22A) ) {
        		getHandler().logEmergencyAndNotify("Empty or UNKNOWN body at Lane 21E, 22 or 22A!\n Please check tracking at the lane, and issue move request manually to move it to lane 23A");			
        }
        
        movePossible_21E_23A = movePossible_21E_23A && isGoodProductNoPauseNoWeld(lc_21E);
        movePossible_22_23A = movePossible_22_23A && isGoodProductNoPauseNoWeld(lc_22);
        movePossible_22A_23A = movePossible_22A_23A && isGoodProductNoPauseNoWeld(lc_22A);
              
        if(!movePossible_21E_23A && !movePossible_22_23A && !movePossible_22A_23A) return;
        
        // get current lot at 23A , 23B
 /*       GtsProduct product = getTailProduct(LANE_23A);
        if(product == null) product = getTailProduct(LANE_23B);
        */
        
        GtsProduct product = getTailProduct(LANE_23A);
        if(product == null) product = getTailProduct(LANE_23B);
              
        String currentLot = product == null ? "ZZZ" : product.getLotNumber(); 

        if(movePossible_22_23A && lc_22.getProductionLot().equals(currentLot) ) {
        	issueMoveRequest(LANE_22, LANE_23A);
        	return;
        }
               	
        if(movePossible_21E_23A && lc_21E.getProductionLot().equals(currentLot) ) {
        	issueMoveRequest(LANE_21E, LANE_23A);
        	return;
        }
        
        if(movePossible_22A_23A && lc_22A.getProductionLot().equals(currentLot) ) {
        	issueMoveRequest(LANE_22A, LANE_23A);
        	return;
        }
        
   	// current lot at lane head of lane but not physically possible (not consider carrier present, move request and move in progress)
    	
        boolean movePhysicallyPossible_22_23A = isMoveToBeReady(LANE_22, LANE_23A);        		
        boolean movePhysicallyPossible_21E_23A = isMoveToBeReady(LANE_21E, LANE_23A);
        boolean movePhysicallyPossible_22A_23A = isMoveToBeReady(LANE_22A, LANE_23A);
        
        boolean movePhysicallyPossible_30A_22 = isMoveToBeReady(LANE_30A, LANE_22);
        boolean movePhysicallyPossible_30B_22 = isMoveToBeReady(LANE_30B, LANE_22);
        boolean movePhysicallyPossible_30C_22 = isMoveToBeReady(LANE_30C, LANE_22);
        
        boolean movePhysicallyPossible_21C_21E = isMoveToBeReady(LANE_21C, LANE_21E);
    	
    	boolean movePhysicallyPossible_21B_30A = isMoveToBeReady(LANE_21B, LANE_30A);
    	boolean movePhysicallyPossible_21B_30B = isMoveToBeReady(LANE_21B, LANE_30B);
    	boolean movePhysicallyPossible_21B_30C = isMoveToBeReady(LANE_21B, LANE_30C);
    	boolean movePhysicallyPossible_21B_21C = isMoveToBeReady(LANE_21B, LANE_21C);
    	
    	boolean movePhysicallyPossible_20D_21B = isMoveToBeReady(LANE_20D, LANE_21B);
    	boolean movePhysicallyPossible_20C_20D = isMoveToBeReady(LANE_20C, LANE_20D);
    	boolean movePhysicallyPossible_20C_22A = isMoveToBeReady(LANE_20C, LANE_22A);
    	boolean movePhysicallyPossible_20D_22  = isMoveToBeReady(LANE_20D, LANE_22) &&  (!hasExitGate(LANE_20S) || isExitGateOpen(LANE_20S));
    	
    	boolean movePhysicallyPossible_30A_23A = movePhysicallyPossible_22_23A && isLaneEmpty(LANE_22) && movePhysicallyPossible_30A_22;
    	boolean movePhysicallyPossible_30B_23A = movePhysicallyPossible_22_23A && isLaneEmpty(LANE_22) && movePhysicallyPossible_30B_22;
    	boolean movePhysicallyPossible_30C_23A = movePhysicallyPossible_22_23A && isLaneEmpty(LANE_22) && movePhysicallyPossible_30C_22;
   	
    	boolean movePhysicallyPossible_21C_23A = movePhysicallyPossible_21E_23A && isLaneEmpty(LANE_21E) && movePhysicallyPossible_21C_21E;
    	
    	boolean movePhysicallyPossible_21B_23A = ((movePhysicallyPossible_30A_23A && isLaneEmpty(LANE_30A) && movePhysicallyPossible_21B_30A) ||
                (movePhysicallyPossible_30B_23A && isLaneEmpty(LANE_30B) && movePhysicallyPossible_21B_30B) ||
                (movePhysicallyPossible_30C_23A && isLaneEmpty(LANE_30C) && movePhysicallyPossible_21B_30C) ||
    			(movePhysicallyPossible_21C_23A && isLaneEmpty(LANE_21C) && movePhysicallyPossible_21B_21C));

    	boolean movePhysicallyPossible_20D_23A = (movePhysicallyPossible_21B_23A && isLaneEmpty(LANE_21B)&& movePhysicallyPossible_20D_21B)||
    			(movePhysicallyPossible_22_23A && isLaneEmpty(LANE_22) && movePhysicallyPossible_20D_22);

    	boolean movePhysicallyPossible_20C_23A = (movePhysicallyPossible_20D_23A && isLaneEmpty(LANE_20D)&& movePhysicallyPossible_20C_20D) ||
    			(movePhysicallyPossible_22A_23A && isLaneEmpty(LANE_22A)&& movePhysicallyPossible_20C_22A);

    	String lowestLot = "ZZZ";
    	
    	String tmpLot = checkLowestLotNoPauseNoWeld(movePhysicallyPossible_30A_23A,LANE_30A,currentLot,lowestLot);
    	if(currentLot.equals(tmpLot)) return;
    	else if(tmpLot != null) lowestLot = tmpLot;
    	
    	tmpLot = checkLowestLotNoPauseNoWeld(movePhysicallyPossible_30B_23A,LANE_30B,currentLot,lowestLot);
    	if(currentLot.equals(tmpLot)) return;
    	else if(tmpLot != null) lowestLot = tmpLot;

    	tmpLot = checkLowestLotNoPauseNoWeld(movePhysicallyPossible_30C_23A,LANE_30C,currentLot,lowestLot);
    	if(currentLot.equals(tmpLot)) return;
    	else if(tmpLot != null) lowestLot = tmpLot;
    	
    	tmpLot = checkLowestLotNoPauseNoWeld(movePhysicallyPossible_21C_23A,LANE_21C,currentLot,lowestLot);
    	if(currentLot.equals(tmpLot)) return;
    	else if(tmpLot != null) lowestLot = tmpLot;
    	
      	tmpLot = checkLowestLotNoPauseNoWeld(movePhysicallyPossible_21B_23A,LANE_21B,currentLot,lowestLot);
    	if(currentLot.equals(tmpLot)) return;
    	else if(tmpLot != null) lowestLot = tmpLot;

      	tmpLot = checkLowestLotNoPauseNoWeld(movePhysicallyPossible_20D_23A,LANE_20D,currentLot,lowestLot);
    	if(currentLot.equals(tmpLot)) return;
    	else if(tmpLot != null) lowestLot = tmpLot;

      	tmpLot = checkLowestLotNoPauseNoWeld(movePhysicallyPossible_20C_23A,LANE_20C,currentLot,lowestLot);
    	if(currentLot.equals(tmpLot)) return;
    	else if(tmpLot != null) lowestLot = tmpLot;
    	
     	tmpLot = checkLowestLotNoPauseNoWeld(movePhysicallyPossible_21E_23A,LANE_21E,currentLot,lowestLot);
    	if(currentLot.equals(tmpLot)) return;
    	else if(tmpLot != null) lowestLot = tmpLot;

     	tmpLot = checkLowestLotNoPauseNoWeld(movePhysicallyPossible_22A_23A,LANE_22A,currentLot,lowestLot);
    	if(currentLot.equals(tmpLot)) return;
    	else if(tmpLot != null) lowestLot = tmpLot;    	
//    	tmpLot = checkLowestLot(movePhysicallyPossible_22_23A,LANE_22,currentLot,lowestLot);
    	tmpLot = checkLowestLotNoPauseNoWeld(movePhysicallyPossible_22_23A,LANE_22,currentLot,lowestLot);
    	if(currentLot.equals(tmpLot)) return;
    	else if(tmpLot != null) lowestLot = tmpLot;

        //lowest lot lane go
    	if(movePhysicallyPossible_22_23A && isCarrierPresent(LANE_22)&& isSameLotGoodHeadProduct(LANE_22,lowestLot)) {
        	issueMoveRequest(LANE_22, LANE_23A);
        	return;
        }
        
        if(movePhysicallyPossible_21E_23A && isCarrierPresent(LANE_21E)&& isSameLotGoodHeadProduct(LANE_21E,lowestLot)) {
        	issueMoveRequest(LANE_21E, LANE_23A);
        	return;
        }

        if(movePhysicallyPossible_22A_23A && isCarrierPresent(LANE_22A)&& isSameLotGoodHeadProduct(LANE_22A,lowestLot)) {
        	issueMoveRequest(LANE_22A, LANE_23A);
        	return;
        }
        
    }
	
}
