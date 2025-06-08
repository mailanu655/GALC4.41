package com.honda.galc.service.gts.pbs;

import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.util.KeyValue;

/**
 * 
 * 
 * <h3>GtsPbs1MoveDecisionRule09A Class description</h3>
 * <p> GtsPbs1MoveDecisionRule09A description </p>
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
 * Sep 24, 2018
 *
 *
 */

//*********************************************************************************
//* The basic rule for bodies entering PBS storage                                *
//* (including DP09A, DP15A and DP10A):                                           *
//* 1. empty carrier, bad label body, non_released body goes to the farthest lane *
//* 2. storage lot body goes to:                                                  *
//* 	a. lane with last body that has the same lot (pC, pD, pD, pF, pK and pL)   *
//* 	b. empty lane (pC, pD, pE, pF, pK and pL)                                  *
//* 	c. lane with lowest lot number (at the end of the lane, pC, pD, pE, pF, pK *
//*       and pL),if the lowest lot number is less than the moving lot number     *
//*    d. lane with greastest empty spaces (pC, pD, pE, pF, pK and pL)            *
//*    e. if all lanes of pC, pD, pE, pF, pK and pL are not possible for moving   *
//*       into, repeat a, b, c and d to lane pM and pN                            *
//* 3. current lot body goes to the lane with the greatest empty spaces (pC, pD,  *
//*    pE, pF, pK, pL, pM and pN)                                                 *
//* 4. delayed lot body goes to the farthest lane                                 *
//* 5. latter lot body goes to the lane with the greatest emtpy spaces (pE, pF,   *
//*    pK and pL) and (pC, pD, pM and pN)                                         *
//*********************************************************************************

//**************************************************************************
//*  This is the Decision Point for moving carriers from pB into storage   *
//*                                                                        *
//**************************************************************************

public class GtsPbs1MoveDecisionRule09A extends GtsPbs1MoveDecisionRuleBase{

	public GtsPbs1MoveDecisionRule09A() {
		super();
	}
	
	public GtsPbs1MoveDecisionRule09A(GtsPbs1TrackingServiceImpl handler) {
		super(handler);
	}
	
	public GtsPbs1TrackingServiceImpl getHandler() {
		return (GtsPbs1TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;   
       
        boolean movePossible_pB_pV = isMovePossible(LANE_pB, LANE_pV) && !isPhysicallyFull(LANE_pQ) && !isLogicallyFull(LANE_pQ);
        boolean movePossible_pB_pC = isMovePossible(LANE_pB, LANE_pC) && movePossible_pB_pV;
        boolean movePossible_pB_pD = isMovePossible(LANE_pB, LANE_pD) && movePossible_pB_pV;
       
        if(!movePossible_pB_pC && !movePossible_pB_pD && !movePossible_pB_pV) return;
        
        boolean movePossible_pV_pE = movePossible_pB_pV && isEntryGateOpen(LANE_pE) && !isPhysicallyFull(LANE_pE) && !isLogicallyFull(LANE_pE) && isExitGateOpen(LANE_pV);
        boolean movePossible_pV_pF = movePossible_pB_pV && isEntryGateOpen(LANE_pF) && !isPhysicallyFull(LANE_pF) && !isLogicallyFull(LANE_pF) && isExitGateOpen(LANE_pV);
        boolean movePossible_pV_pK = movePossible_pB_pV && isEntryGateOpen(LANE_pK) && !isPhysicallyFull(LANE_pK) && !isLogicallyFull(LANE_pK) && isExitGateOpen(LANE_pV);
        boolean movePossible_pV_pQ = movePossible_pB_pV && isExitGateOpen(LANE_pV);
        boolean movePossible_pQ_pL = movePossible_pV_pQ && isEntryGateOpen(LANE_pL) && !isPhysicallyFull(LANE_pL) && !isLogicallyFull(LANE_pL) && isExitGateOpen(LANE_pQ);
        boolean movePossible_pQ_pM = movePossible_pV_pQ && isEntryGateOpen(LANE_pM) && !isPhysicallyFull(LANE_pM) && !isLogicallyFull(LANE_pM) && isExitGateOpen(LANE_pQ);
        boolean movePossible_pQ_pN = movePossible_pV_pQ && isEntryGateOpen(LANE_pN) && !isPhysicallyFull(LANE_pN) && !isLogicallyFull(LANE_pN) && isExitGateOpen(LANE_pQ) && area.findLane(LANE_pN).getLaneCarriers().size() < 7;
        
        if(!movePossible_pB_pC && !movePossible_pB_pD 
        		&& !movePossible_pV_pE && !movePossible_pV_pF && !movePossible_pV_pK 
        		&& !movePossible_pQ_pL && !movePossible_pQ_pM && !movePossible_pQ_pN) return;
        
        GtsLane lane_pB = area.findLane(LANE_pB);
        
        GtsLaneCarrier headCarrier_pB = lane_pB.getHeadCarrier();
        
        if(headCarrier_pB == null) return;
        
        if(headCarrier_pB.getProduct() == null || !headCarrier_pB.getProduct().isReleased()) {
        	
        	// Empty carrier, bad label body and non_released body goes to the farthest lane
        	if(movePossible_pQ_pL || movePossible_pQ_pM || movePossible_pQ_pN
        	   || movePossible_pV_pE || movePossible_pV_pF || movePossible_pV_pK) {
        		issueMoveRequest(LANE_pB, LANE_pV);
        		return;
        	}else if(movePossible_pB_pD) {
         		issueMoveRequest(LANE_pB, LANE_pD);
         		return;
         	}else if(movePossible_pB_pC) {
         		issueMoveRequest(LANE_pB, LANE_pC);
         		return;     		
        	}
            
        }else {
        	String prodLot = headCarrier_pB.getProductionLot();
        	//released carriers
        	
        	// 1. lane with the last body that has the same lot (pC, pD, pE, pF, pK, pL)
          	        	          	
        	if (isSameReleasedLastLot(movePossible_pB_pC,LANE_pC,prodLot)) {
             		issueMoveRequest(LANE_pB, LANE_pC);
             		return;
             	}else if(isSameReleasedLastLot(movePossible_pB_pD,LANE_pD,prodLot)) {
             		issueMoveRequest(LANE_pB, LANE_pD);
              		return;
             	}else if(isSameReleasedLastLot(movePossible_pV_pE,LANE_pE,prodLot) ||
             			isSameReleasedLastLot(movePossible_pV_pF,LANE_pF,prodLot) ||
             			isSameReleasedLastLot(movePossible_pV_pK,LANE_pK,prodLot) ||
             			isSameReleasedLastLot(movePossible_pQ_pL,LANE_pL,prodLot) ) {
             		issueMoveRequest(LANE_pB, LANE_pV);
              		return;
             	}             	
        	
        	if(hasLotInStorage(prodLot, LANE_pC,LANE_pD,LANE_pE,LANE_pF,LANE_pK,LANE_pL,LANE_pM,LANE_pN)) {
        		
        		// 2. goes to empty lane pC,pD,pE,pF,pG,pK,pL
        		
        		if(movePossible_pB_pC && isLaneEmpty(LANE_pC)) {
        			issueMoveRequest(LANE_pB, LANE_pC);
        			return;
        		}else if(movePossible_pB_pD && isLaneEmpty(LANE_pD)) {
        			issueMoveRequest(LANE_pB, LANE_pD);
        			return;
        		};
        		
        		if(movePossible_pV_pE && isLaneEmpty(LANE_pE) ||
        		   movePossible_pV_pF && isLaneEmpty(LANE_pF) ||
        		   movePossible_pV_pK && isLaneEmpty(LANE_pK) ||
        		   movePossible_pQ_pL && isLaneEmpty(LANE_pL)) {
       				issueMoveRequest(LANE_pB, LANE_pV);
                	return;
        		}
        		
        		//3. goes the lane with lowest lot number (at the end of lane)(pC, pD, pE, pF, pK, pL in order), if the lowest lot number is less than the moving lot number	
                GtsLane currentLane = null;
                currentLane = getLowestLotInStorage(movePossible_pB_pC,LANE_pC,currentLane);
                currentLane = getLowestLotInStorage(movePossible_pB_pD,LANE_pD,currentLane);
                currentLane = getLowestLotInStorage(movePossible_pV_pE,LANE_pE,currentLane);
                currentLane = getLowestLotInStorage(movePossible_pV_pF,LANE_pF,currentLane);
                currentLane = getLowestLotInStorage(movePossible_pV_pK,LANE_pK,currentLane);
                currentLane = getLowestLotInStorage(movePossible_pQ_pL,LANE_pL,currentLane);
                if(currentLane != null && currentLane.getReleasedTailCarrier() != null 
                		&& currentLane.getReleasedTailCarrier().getProduct()!=null
                		&& currentLane.getReleasedTailCarrier().getProductionLot().compareToIgnoreCase(prodLot) < 0) {
                	if(LANE_pC.equalsIgnoreCase(currentLane.getLaneName()) || 
                	   LANE_pD.equalsIgnoreCase(currentLane.getLaneName())) {
                		issueMoveRequest(LANE_pB, currentLane.getLaneName());
                		return;
                	}else {
                		issueMoveRequest(LANE_pB, LANE_pV);
                		return;
                	}
                }
                
                //4. goes to the lane with greatest empty spaces
            	
                KeyValue<GtsLane,Integer> laneItem = null;
            	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pB_pC,LANE_pC,laneItem);
            	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pB_pD,LANE_pD,laneItem);
            	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pV_pE,LANE_pE,laneItem);
            	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pV_pF,LANE_pF,laneItem);
            	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pV_pK,LANE_pK,laneItem);
            	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pQ_pL,LANE_pL,laneItem);
            	
            	if(laneItem != null) {
            		if(LANE_pC.equalsIgnoreCase(laneItem.getKey().getLaneName()) ||
            		   LANE_pD.equalsIgnoreCase(laneItem.getKey().getLaneName())) {
            			issueMoveRequest(LANE_pB, laneItem.getKey().getLaneName());
                		return;
            		}else {
            			issueMoveRequest(LANE_pB, LANE_pV);
            			return;
            		}
            	}
            	
              	//5. Storage lot body goes to the lane pM, pN, if move is possible    
               	if(movePossible_pQ_pM || movePossible_pQ_pN) {
               		issueMoveRequest(LANE_pB, LANE_pV);
        			return;
               	}
            	
        	}
        	
         	// NOW check the current lot
           	// Current lot cars go to the lane with the fewest carriers
        	String currentLot = getCurrentLot(LANE_pG);
        	if(prodLot.equalsIgnoreCase(currentLot)) {
        		KeyValue<GtsLane,Integer> laneItem = null;
             	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pB_pC,LANE_pC,laneItem);
             	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pB_pD,LANE_pD,laneItem);
             	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pV_pE,LANE_pE,laneItem);
             	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pV_pF,LANE_pF,laneItem);
             	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pV_pK,LANE_pK,laneItem);
             	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pQ_pL,LANE_pL,laneItem);
             	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pQ_pM,LANE_pM,laneItem);
             	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pQ_pN,LANE_pN,laneItem);
             	
             	if(laneItem != null) {
            		if(LANE_pC.equalsIgnoreCase(laneItem.getKey().getLaneName()) ||
            		   LANE_pD.equalsIgnoreCase(laneItem.getKey().getLaneName())) {
            			issueMoveRequest(LANE_pB, laneItem.getKey().getLaneName());
                		return;
            		}else {
            			issueMoveRequest(LANE_pB, LANE_pV);
            			return;
            		}
            	}
        	}
        	
        	// Now check the deleyed lot 
            // Delayed lot cars go to the farthest available lane
            //
        	
        	if(prodLot.compareTo(currentLot) <0 ) {
        		if(movePossible_pV_pE || movePossible_pV_pF || movePossible_pV_pK ||
        		   movePossible_pQ_pL || movePossible_pQ_pM || movePossible_pQ_pN) {
        			issueMoveRequest(LANE_pB, LANE_pV);
           			return;   
        		}else if(movePossible_pB_pD) {
        			issueMoveRequest(LANE_pB, LANE_pD);
           			return; 
        		}else if(movePossible_pB_pC) {
        			issueMoveRequest(LANE_pB, LANE_pC);
           			return;
        		}
        	}
        	
        	// All others go to priority lanes (pE,pF,pK,pL first , pC,pD,pM,pN second) 
        	// with fewest carriers
                    	        	
        	if(movePossible_pV_pE || movePossible_pV_pF || movePossible_pV_pK || movePossible_pQ_pL ){
        		issueMoveRequest(LANE_pB, LANE_pV);
        		return;
    		}        		
         	else {
               	KeyValue<GtsLane,Integer> laneItem = null;
         		laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pB_pC,LANE_pC,laneItem);
             	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pB_pD,LANE_pD,laneItem);      
             	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pQ_pM,LANE_pM,laneItem);
             	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pQ_pN,LANE_pN,laneItem);
              	
             	if(laneItem != null) {
            		if(LANE_pC.equalsIgnoreCase(laneItem.getKey().getLaneName()) ||
            		   LANE_pD.equalsIgnoreCase(laneItem.getKey().getLaneName())) {
            			issueMoveRequest(LANE_pB, laneItem.getKey().getLaneName());
                		return;	
            			}
            		else {
            			issueMoveRequest(LANE_pB, LANE_pV);
                		return;
            		}
         	}
        	
        }
         
	}
	}
}

	
	

