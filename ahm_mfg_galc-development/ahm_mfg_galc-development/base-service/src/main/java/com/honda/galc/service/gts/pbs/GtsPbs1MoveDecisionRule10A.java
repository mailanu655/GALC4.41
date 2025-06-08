package com.honda.galc.service.gts.pbs;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.util.KeyValue;

/**
 * 
 * 
 * <h3>GtsPbs1MoveDecisionRule10A Class description</h3>
 * <p> GtsPbs1MoveDecisionRule10A description </p>
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

//***************************************************************************
//*                                                                         *
//*  Movement of carriers from lane P takes priority over movement from Q.  *
//*  This is an effort to ensure that AF-On does not backup due to lane P   *
//*  filling up. The logic for filling L,M, or N (from P or Q) is similar   *
//*  to that used in PAPBS04.                                               *
//*                                                                         *
//***************************************************************************

public class GtsPbs1MoveDecisionRule10A extends GtsPbs1MoveDecisionRuleBase{
		
	private final String pP1_PHOTO_EYE= "CS-PE-pP1";
	
	public GtsPbs1MoveDecisionRule10A() {
		super();
	}
	
	public GtsPbs1MoveDecisionRule10A(GtsPbs1TrackingServiceImpl handler) {
		super(handler);
	}
	
	public GtsPbs1TrackingServiceImpl getHandler() {
		return (GtsPbs1TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        boolean movePossible_pP_pM = isMovePossible(LANE_pP, LANE_pM);
        boolean movePossible_pP_pN = isMovePossible(LANE_pP, LANE_pN);
            
        if(movePossible_pP_pM && !movePossible_pP_pN) {
        	// "****** Only move pP-pM is possible, issue MP_pP-pM \n" << flush;
        	issueMoveRequest(LANE_pP, LANE_pM);
        	return;
        }if(movePossible_pP_pN && !movePossible_pP_pM) {
        	// "****** Only move pP-pN is possible, issue MP_pP-pN \n" << flush;
        	issueMoveRequest(LANE_pP, LANE_pN);
        	return;
        }else if(movePossible_pP_pM && movePossible_pP_pN) {
        	if(area.findLane(LANE_pN).getAvailableSpaces() == 1) {
        		// "****** Both move pP-pM and pN is possible and only one space is left at lane pN, issue MP_pP-pM \n" << flush;
        		issueMoveRequest(LANE_pP, LANE_pM);
            	return;  
        	}
        	
        	 GtsLane lane_pP = area.findLane(LANE_pP);
             GtsLaneCarrier headCarrier_pP = lane_pP.getHeadCarrier(); // need check stateOfBinaryIndicator("PA.P12_PE_pP1") 
             if( !isStatusOn(pP1_PHOTO_EYE) ||
            		(headCarrier_pP != null && (headCarrier_pP.getProduct() == null) || !headCarrier_pP.getProduct().isReleased())) {
            	// "****** Special non-released carrier goes to pN. Issue MR_pP-pN \n" << endl;
            	 issueMoveRequest(LANE_pP, LANE_pN);
            	 return;
             }
                         
             String prodLot = headCarrier_pP.getProductionLot();
         	
             // Now the vehicle is a RELEASE one
             // Storage lot body goes to :
             // 1. lane with last body that has the same lot (pM, pN)
             // Storage lot         
             
          	if (isSameReleasedLastLot(movePossible_pP_pM,LANE_pM,prodLot)) {
         		issueMoveRequest(LANE_pP, LANE_pM);
         		return;
         	}else if(isSameReleasedLastLot(movePossible_pP_pN,LANE_pN,prodLot)) {
         		issueMoveRequest(LANE_pP, LANE_pN);
          		return;
         	}
            
          	if(hasLotInStorage(prodLot, LANE_pC,LANE_pD,LANE_pE,LANE_pF,LANE_pK,LANE_pL,LANE_pM,LANE_pN)) {
		  		 // 2. for lane pM and pN.  goes to the empty lane.
		    	 if(movePossible_pP_pM && isLaneEmpty(LANE_pM)) {
		    		issueMoveRequest(LANE_pP, LANE_pM);
		          	return; 
		    	 }else if(movePossible_pP_pN && isLaneEmpty(LANE_pN)) {
		    		issueMoveRequest(LANE_pP, LANE_pN);
		          	return; 
		    	 }
		    	 
		    	 // 3. for lane pM and pN. goes the lane with lowest lot number (at the end of lane).
		    	 GtsLane currentLane = null;
		         currentLane = getLowestLotInStorage(movePossible_pP_pM,LANE_pM,currentLane);
		         currentLane = getLowestLotInStorage(movePossible_pP_pN,LANE_pN,currentLane);
		         
		         if(currentLane != null && currentLane.getReleasedTailCarrier() != null
		        		 && currentLane.getReleasedTailCarrier().getProduct()!=null
		        		 && currentLane.getReleasedTailCarrier().getProductionLot().compareToIgnoreCase(prodLot) < 0) {
		        	issueMoveRequest(LANE_pP, currentLane.getLaneName());
		     		return;
		         }
                 
		         //4. goes to the lane with greatest empty spaces
		         KeyValue<GtsLane,Integer> laneItem = null;
		         laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pP_pM,LANE_pM,laneItem);
		         laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pP_pN,LANE_pN,laneItem);
				
		         if(laneItem != null) {
		        	 issueMoveRequest(LANE_pP, laneItem.getKey().getLaneName());
		        	 return;
		         }
             }
            
	        // Delayed lot VIN goes to lane pN
	             
	        String currentLot = getCurrentLot(LANE_pG);
         	
            if(!StringUtils.isEmpty(currentLot) && prodLot.compareTo(currentLot) < 0) {
            	issueMoveRequest(LANE_pP, LANE_pN);
               	return; 
            }
            
            // Anything else should go the lane with the fewest carriers
            KeyValue<GtsLane,Integer> laneItem = null;
         	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pP_pM,LANE_pM,laneItem);
         	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pP_pN,LANE_pN,laneItem);
         	
         	if(laneItem != null) {
         		issueMoveRequest(LANE_pP, laneItem.getKey().getLaneName());
        		return;
         	}           	
        }
        
        boolean movePossible_pQ_pL = isMovePossible(LANE_pQ, LANE_pL);
        boolean movePossible_pQ_pM = isMovePossible(LANE_pQ, LANE_pM);
        boolean movePossible_pQ_pN = isMovePossible(LANE_pQ, LANE_pN) && area.findLane(LANE_pN).getLaneCarriers().size() < 7;
        
        if(!movePossible_pQ_pL && !movePossible_pQ_pM && !movePossible_pQ_pN) {
        	return;
        }
        
        GtsLane lane_pQ = area.findLane(LANE_pQ);
        GtsLaneCarrier headCarrier_pQ = lane_pQ.getHeadCarrier();
        
        // Empty carrier, bad label body and non-released body goes to the farthest lane
        
        if(headCarrier_pQ != null && (headCarrier_pQ.getProduct() == null || !headCarrier_pQ.getProduct().isReleased())) {
        	if(movePossible_pQ_pN) {
        		issueMoveRequest(LANE_pQ, LANE_pN);
            	return;
        	}else if(movePossible_pQ_pM) {
        		issueMoveRequest(LANE_pQ, LANE_pM);
            	return;
        	}else if(movePossible_pQ_pL) {
        		issueMoveRequest(LANE_pQ, LANE_pL);
            	return;
        	}
        }
        
        // Now the vehicle is a RELEASE one
        // Storage lot body goes to :
        // 1. Storage lot body goes to the lane with last body that has the same lot (pE, pF, pK, pL in order)
        
        String prodLot = headCarrier_pQ.getProductionLot();
     	
        // Storage lot         
        
        if(hasLotInStorage(prodLot,  LANE_pC,LANE_pD,LANE_pE,LANE_pF,LANE_pK,LANE_pL,LANE_pM,LANE_pN)) {
        	
        	// 1, 2, 3 and 4 if pL is possible, goes to pL
         	if(movePossible_pQ_pL) {
        		issueMoveRequest(LANE_pQ, LANE_pL);
            	return;
        	}
   
     		// 4.1 for pM and pN. goes to the lane with same lot number (end of lane) 
     		
     		if(isSameReleasedLastLot(movePossible_pQ_pM, LANE_pM, prodLot)) {
        		issueMoveRequest(LANE_pQ, LANE_pM);
            	return;
        	}else if(isSameReleasedLastLot(movePossible_pQ_pN, LANE_pN, prodLot)) {
        		issueMoveRequest(LANE_pQ, LANE_pN);
            	return;
        	}
        	
     		// 4.2 for lane pM and pN.  goes to the empty lane.
     		if(movePossible_pQ_pM && isLaneEmpty(LANE_pM)) {
        		issueMoveRequest(LANE_pQ, LANE_pM);
              	return; 
        	 }else if(movePossible_pQ_pN && isLaneEmpty(LANE_pN)) {
        		issueMoveRequest(LANE_pQ, LANE_pN);
              	return; 
        	 }
     		
     		// 4.3 for lane pM and pN. goes the lane with lowest lot number (at the end of lane), if the lowest lot number is less than the moving lot number
     		GtsLane currentLane = null;
            currentLane = getLowestLotInStorage(movePossible_pQ_pM,LANE_pM,currentLane);
            currentLane = getLowestLotInStorage(movePossible_pQ_pN,LANE_pN,currentLane);
            
            if(currentLane != null && currentLane.getReleasedTailCarrier() != null 
            		&& currentLane.getReleasedTailCarrier().getProduct()!=null
            		&& currentLane.getReleasedTailCarrier().getProductionLot().compareToIgnoreCase(prodLot) < 0) {
           	issueMoveRequest(LANE_pQ, currentLane.getLaneName());
        		return;
            }
            
            //4.4 goes to the lane with greatest empty spaces
            KeyValue<GtsLane,Integer> laneItem = null;
         	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pQ_pM,LANE_pM,laneItem);
         	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pQ_pN,LANE_pN,laneItem);
         	
         	if(laneItem != null) {
         		issueMoveRequest(LANE_pQ, laneItem.getKey().getLaneName());
        		return;
         	}
        }
        
        // Delayed lot VIN goes to farthest available lane
        String currentLot = getCurrentLot(LANE_pG);
        if(!StringUtils.isEmpty(currentLot) && prodLot.compareTo(currentLot) < 0) {
        	
        	//"****** Delayed Lot Logic for pQ: goes to the farthest lane \n" << endl;
            if(movePossible_pQ_pN) {
        		issueMoveRequest(LANE_pQ, LANE_pN);
            	return;
        	}else if(movePossible_pQ_pM) {
        		issueMoveRequest(LANE_pQ, LANE_pM);
            	return;
        	}else if(movePossible_pQ_pL) {
        		issueMoveRequest(LANE_pQ, LANE_pL);
            	return;
        	}
        }         
            // Anything else should go to the lane with the fewest carriers
            // (includes the current lot and the upcoming lot)
            KeyValue<GtsLane,Integer> laneItem = null;
            laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pQ_pL,LANE_pL,laneItem);
         	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pQ_pM,LANE_pM,laneItem);
         	laneItem = getGreatestEmptySpaceLaneInStorage(movePossible_pQ_pN,LANE_pN,laneItem);
         	if(laneItem != null) {
         		issueMoveRequest(LANE_pQ, laneItem.getKey().getLaneName());
        		return;
         	}
        }
       }
