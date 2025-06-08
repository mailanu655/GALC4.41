package com.honda.galc.service.gts.pbs;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.util.KeyValue;

/**
 * 
 * 
 * <h3>GtsPbs1MoveDecisionRule15A Class description</h3>
 * <p> GtsPbs1MoveDecisionRule15A description </p>
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

//**************************************************************************
//*  This is the Decision Point for moving carriers from pV to pE,pF,pK or *
//*       pL,pM,pN                                                         *
//**************************************************************************

public class GtsPbs1MoveDecisionRule15A extends GtsPbs1MoveDecisionRuleBase{

	public GtsPbs1MoveDecisionRule15A() {
		super();
	}
	
	public GtsPbs1MoveDecisionRule15A(GtsPbs1TrackingServiceImpl handler) {
		super(handler);
	}
	
	public GtsPbs1TrackingServiceImpl getHandler() {
		return (GtsPbs1TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        

        
        boolean movePossible_pV_pE = isMovePossible(LANE_pV, LANE_pE) && !isPhysicallyFull(LANE_pQ) && !isLogicallyFull(LANE_pQ);
        boolean movePossible_pV_pF = isMovePossible(LANE_pV, LANE_pF) && !isPhysicallyFull(LANE_pQ) && !isLogicallyFull(LANE_pQ);
        boolean movePossible_pV_pK = isMovePossible(LANE_pV, LANE_pK) && !isPhysicallyFull(LANE_pQ) && !isLogicallyFull(LANE_pQ);
        
        boolean movePossible_pV_pQ = isMovePossible(LANE_pV, LANE_pQ);
        
        boolean movePossible_pQ_pL = movePossible_pV_pQ && isEntryGateOpen(LANE_pL) && !isPhysicallyFull(LANE_pL) && !isLogicallyFull(LANE_pL) && isExitGateOpen(LANE_pQ);
        boolean movePossible_pQ_pM = movePossible_pV_pQ && isEntryGateOpen(LANE_pM) && !isPhysicallyFull(LANE_pM) && !isLogicallyFull(LANE_pM) && isExitGateOpen(LANE_pQ);
        boolean movePossible_pQ_pN = movePossible_pV_pQ && isEntryGateOpen(LANE_pN) && !isPhysicallyFull(LANE_pN) && !isLogicallyFull(LANE_pN) && isExitGateOpen(LANE_pQ) && area.findLane(LANE_pN).getLaneCarriers().size() < 7;       
        		
        
        if (!movePossible_pV_pE && !movePossible_pV_pF && !movePossible_pV_pK && !movePossible_pV_pQ
        		&& !movePossible_pQ_pL && !movePossible_pQ_pM && !movePossible_pQ_pN ) return;
        
        GtsLane lane_pV = area.findLane(LANE_pV);
        
        GtsLaneCarrier headCarrier_pV = lane_pV.getHeadCarrier();
        
        if(headCarrier_pV == null) return;
        
        if(headCarrier_pV.getProduct() == null || !headCarrier_pV.getProduct().isReleased()) {
        	
        	 // Empty carrier, bad label body and non-released body goes to the farthest lane 
            if(movePossible_pQ_pL || movePossible_pQ_pM || movePossible_pQ_pN) {
        		issueMoveRequest(LANE_pV, LANE_pQ);
        		return;
        	}else if(movePossible_pV_pK) {
         		issueMoveRequest(LANE_pV, LANE_pK);
         		return;
         	}else if(movePossible_pV_pF) {
         		issueMoveRequest(LANE_pV, LANE_pF);
         		return;
        	}else if(movePossible_pV_pE) {
         		issueMoveRequest(LANE_pV, LANE_pE);
         		return;
        	}

        }else {
        	String prodLot = headCarrier_pV.getProductionLot();
        	//released carriers
        	
        	// 1. lane with the last body that has the same lot ( pE, pF, pK, pL)
          	
        	if(isSameReleasedLastLot(movePossible_pV_pE,LANE_pE,prodLot)) {
        		issueMoveRequest(LANE_pV, LANE_pE);
         		return;        	   
        	}else if(isSameReleasedLastLot(movePossible_pV_pF,LANE_pF,prodLot)) {
        		issueMoveRequest(LANE_pV, LANE_pF);
         		return;
        	}else if(isSameReleasedLastLot(movePossible_pV_pK,LANE_pK,prodLot)) {
        		issueMoveRequest(LANE_pV, LANE_pK);
         		return;
        	}else if(isSameReleasedLastLot(movePossible_pQ_pL,LANE_pL,prodLot)) {
         		issueMoveRequest(LANE_pV, LANE_pQ);
         		return;
        	}
        	
        	if(hasLotInStorage(prodLot,LANE_pC,LANE_pD,LANE_pE,LANE_pF,LANE_pK,LANE_pL,LANE_pM,LANE_pN)) {
        		
        		// 2. goes to empty lane pE,pF,pG,pK,pL
        		
        		if(movePossible_pV_pE && isLaneEmpty(LANE_pE)) {
        			issueMoveRequest(LANE_pV, LANE_pE);
        			return;
        		}else if(movePossible_pV_pF && isLaneEmpty(LANE_pF)) {
        			issueMoveRequest(LANE_pV, LANE_pF);
        			return;
        		}else if(movePossible_pV_pK && isLaneEmpty(LANE_pK)) {
        			issueMoveRequest(LANE_pV, LANE_pK);
        			return;
        		}else if(movePossible_pQ_pL && isLaneEmpty(LANE_pL)) {
       				issueMoveRequest(LANE_pV, LANE_pQ);
                	return;
        		}
        		
        		//3. goes the lane with lowest lot number (at the end of lane)(pE, pF, pK, pL in order), if the lowest lot number is less than the moving lot number	
                GtsLane currentLane = null;
                currentLane = getLowestLotInStorage(movePossible_pV_pE,LANE_pE,currentLane);
                currentLane = getLowestLotInStorage(movePossible_pV_pF,LANE_pF,currentLane);
                currentLane = getLowestLotInStorage(movePossible_pV_pK,LANE_pK,currentLane);
                currentLane = getLowestLotInStorage(movePossible_pQ_pL,LANE_pL,currentLane);
                if(currentLane != null && currentLane.getReleasedTailCarrier() != null 
                					&& currentLane.getReleasedTailCarrier().getProduct()!=null
                					&& currentLane.getReleasedTailCarrier().getProductionLot().compareToIgnoreCase(prodLot) < 0) {
                	if(LANE_pE.equalsIgnoreCase(currentLane.getLaneName()) || 
                	   LANE_pF.equalsIgnoreCase(currentLane.getLaneName()) ||
                	   LANE_pK.equalsIgnoreCase(currentLane.getLaneName())) {
                		issueMoveRequest(LANE_pV, currentLane.getLaneName());
                		return;
                	}else {
                		issueMoveRequest(LANE_pV, LANE_pQ);
                		return;
                	}
                }
                
                //4. goes to the lane with greatest empty spaces
            	
                KeyValue<GtsLane,Integer> laneItem = null;
            	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pV_pE,LANE_pE,laneItem);
            	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pV_pF,LANE_pF,laneItem);
            	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pV_pK,LANE_pK,laneItem);
            	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pQ_pL,LANE_pL,laneItem);
            	
            	if(laneItem != null) {
            		if(LANE_pE.equalsIgnoreCase(laneItem.getKey().getLaneName()) ||
            		   LANE_pF.equalsIgnoreCase(laneItem.getKey().getLaneName()) ||
                 	   LANE_pK.equalsIgnoreCase(laneItem.getKey().getLaneName())) {
            			issueMoveRequest(LANE_pV, laneItem.getKey().getLaneName());
                		return;
            		}else {
            			issueMoveRequest(LANE_pV, LANE_pQ);
            			return;
            		}
            	}
            	
              	//5. Storage lot body goes to the lane pM, pN, if move is possible    
               	if(movePossible_pQ_pM || movePossible_pQ_pN) {
               		issueMoveRequest(LANE_pV, LANE_pQ);
        			return;
               	}            	
        	}
        	
         	// NOW check the current lot
           	// Current lot cars go to the lane with the fewest carriers

        	String currentLot = getCurrentLot(LANE_pG);
        	if(prodLot.equalsIgnoreCase(currentLot)) {
            	KeyValue<GtsLane,Integer> laneItem = null;
             	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pV_pE,LANE_pE,laneItem);
             	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pV_pF,LANE_pF,laneItem);
             	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pV_pK,LANE_pK,laneItem);
             	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pQ_pL,LANE_pL,laneItem);
             	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pQ_pM,LANE_pM,laneItem);
             	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pQ_pN,LANE_pN,laneItem);
             	
             	if(laneItem != null) {
            		if(LANE_pE.equalsIgnoreCase(laneItem.getKey().getLaneName()) ||
            		   LANE_pF.equalsIgnoreCase(laneItem.getKey().getLaneName()) ||
                 	   LANE_pK.equalsIgnoreCase(laneItem.getKey().getLaneName())) {
            			issueMoveRequest(LANE_pV, laneItem.getKey().getLaneName());
                		return;
            		}else {
            			issueMoveRequest(LANE_pV, LANE_pQ);
            			return;
            		}
            	}
        	}
        	
        	// Now check the deleyed lot 
            // Delayed lot cars go to the farthest available lane
            // need change Xiaofen wang
            
            if(prodLot.compareTo(currentLot) < 0) {
           
            	if(movePossible_pQ_pL || movePossible_pQ_pM || movePossible_pQ_pN) {
        			issueMoveRequest(LANE_pV, LANE_pQ);
           			return;   
        		}else if(movePossible_pV_pK) {
        			issueMoveRequest(LANE_pV, LANE_pK);
           			return; 
        		}else if(movePossible_pV_pF) {
        			issueMoveRequest(LANE_pV, LANE_pF);
           			return;
        		}else if(movePossible_pV_pE) {
        			issueMoveRequest(LANE_pV, LANE_pE);
           			return;
        		}
        	}
        	
        	// All others go to priority lanes (pE,pF,pK,pL first , pM,pN second) 
        	// with fewest carriers
            
        	KeyValue<GtsLane,Integer> laneItem = null;
         	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pV_pE,LANE_pE,laneItem);
         	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pV_pF,LANE_pF,laneItem);
         	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pV_pK,LANE_pK,laneItem);
         	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pQ_pL,LANE_pL,laneItem);
         	
         	if(laneItem != null) {
         		if(LANE_pE.equalsIgnoreCase(laneItem.getKey().getLaneName()) ||
         		   LANE_pF.equalsIgnoreCase(laneItem.getKey().getLaneName()) ||
                   LANE_pK.equalsIgnoreCase(laneItem.getKey().getLaneName())) {
             			issueMoveRequest(LANE_pV, laneItem.getKey().getLaneName());
             			return;
             	}else {
        			issueMoveRequest(LANE_pV, LANE_pQ);
        			return;
        		}
        	}
        	
         	if(movePossible_pQ_pM || movePossible_pQ_pN) {
    			issueMoveRequest(LANE_pV, LANE_pQ);
       			return;   
    		}      	
        	
        }
         
	}

}
