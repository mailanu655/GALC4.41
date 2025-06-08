package com.honda.galc.service.gts.pbs;

import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsMove;

/**
 * 
 * 
 * <h3>GtsPbs1MoveDecisionRule05A Class description</h3>
 * <p> GtsPbs1MoveDecisionRule05A description </p>
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
 * Oct.22, 2018
 *
 *
 */
public class GtsPbs1MoveDecisionRule05A extends GtsPbs1MoveDecisionRuleBase{

	public GtsPbs1MoveDecisionRule05A() {
		super();
	}
	
	public GtsPbs1MoveDecisionRule05A(GtsPbs1TrackingServiceImpl handler) {
		super(handler);
	}
	
	public GtsPbs1TrackingServiceImpl getHandler() {
		return (GtsPbs1TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        boolean movePossible_tY_tC = isMovePossible(LANE_tY, LANE_tC);
        boolean movePossible_tY_tZ = isMovePossible(LANE_tY, LANE_tZ);
        boolean movePossible_tS_tC = isMovePossible(LANE_tS, LANE_tC);
        
        if (!movePossible_tY_tC && !movePossible_tY_tZ && !movePossible_tS_tC) return;
        
        
        if(!isEntryGateOpen(LANE_tW)) {
        	if (movePossible_tY_tZ) {
        		issueMoveRequest(LANE_tY, LANE_tZ);
        	}
        	if (movePossible_tS_tC) {
        		issueMoveRequest(LANE_tS, LANE_tC);
        	}      	
        	return;	
        }
        
        if (area.findLane(LANE_tY).getLaneCarriers().size() >= 11) {
        	if(movePossible_tY_tC && !movePossible_tY_tZ) {
        		issueMoveRequest(LANE_tY, LANE_tC);
        		return;
        	}
        	else if (!movePossible_tY_tC && movePossible_tY_tZ) {
        		issueMoveRequest(LANE_tY, LANE_tZ);
        		return;
        	}
        	else if (movePossible_tY_tC && movePossible_tY_tZ) {
        		if (movePossible_tS_tC) {
        			String lotLanetY = area.findLane(LANE_tY).getHeadCarrier().getProductionLot();
            		String lotLanetS = area.findLane(LANE_tS).getHeadCarrier().getProductionLot();
            		if (lotLanetY.compareTo(lotLanetS) <= 0 ) {
            		   	issueMoveRequest(LANE_tY, LANE_tC);
            		   	return;
            		}else {
            			issueMoveRequest(LANE_tY, LANE_tZ);
            			issueMoveRequest(LANE_tS, LANE_tC);
            		   	return;
            		} 	
        		}else {
       		   		issueMoveRequest(LANE_tY, LANE_tC);
       		   		return;
        		}             	
        	}    
        }
        
       if (!movePossible_tY_tC) {
    	   if ((!movePossible_tY_tZ && !movePossible_tS_tC) || (movePossible_tY_tZ && movePossible_tS_tC))
    		   return;
    	   if ((!movePossible_tY_tZ && movePossible_tS_tC)) {
    		   if(!isMoveOutFinished(LANE_tY) || isMoveOutRequestCreated(LANE_tY)
    				   || (!isMoveInFinished(LANE_tZ) || isMoveInRequestCreated(LANE_tZ))&& isExitGateOpen(LANE_tY))
    				   return;
    		   else {
    			   issueMoveRequest(LANE_tS, LANE_tC);
    			   return;
    		   }
    	   }  
    	   
    	   if (movePossible_tY_tZ && !movePossible_tS_tC) {
    		   if(!isMoveOutFinished(LANE_tS) || isMoveOutRequestCreated(LANE_tS)
    				   || !isMoveInFinished(LANE_tC) || isMoveInRequestCreated(LANE_tC) ||
    			  ((area.findLane(LANE_tS).getLaneCarriers().size() + 
    			    area.findLane(LANE_tR).getLaneCarriers().size() + 
    			    area.findLane(LANE_tQ).getLaneCarriers().size() +
    			    area.findLane(LANE_tZ).getLaneCarriers().size()) <= 11) && isEntryGateOpen(LANE_tC))
    			   return;
    		   else {   			
  			    issueMoveRequest(LANE_tY, LANE_tZ);
    				return;    			   
    		   }
    	   }
    	   
    	   return;
       } else {
    	   	if (!movePossible_tS_tC) {
    	   		issueMoveRequest(LANE_tY, LANE_tC);
    	   		return;
    	   	}else {
			   GtsLane lane_tY = area.findLane(LANE_tY);
			   GtsLane lane_tS = area.findLane(LANE_tS);
			   GtsLane lane_tR = area.findLane(LANE_tR);
			   GtsLane lane_tQ = area.findLane(LANE_tQ);
			   GtsLane lane_tZ = area.findLane(LANE_tZ);
			   GtsLaneCarrier headCarrier_tY = lane_tY.getHeadCarrier();
	  		   GtsLaneCarrier headCarrier_tS = lane_tS.getHeadCarrier();
	  		   
			   
			   if(headCarrier_tY == null || headCarrier_tS == null)
				   return;
			   if (headCarrier_tY.getProductionLot().compareTo(headCarrier_tS.getProductionLot()) < 0) 
				   issueMoveRequest(LANE_tY, LANE_tC);
			   else if (headCarrier_tY.getProductionLot().compareTo(headCarrier_tS.getProductionLot()) > 0) {
				   issueMoveRequest(LANE_tS, LANE_tC);
				   if ( (lane_tS.getLaneCarriers().size() + lane_tR.getLaneCarriers().size() + lane_tQ.getLaneCarriers().size() + lane_tZ.getLaneCarriers().size()) > 11 && movePossible_tY_tZ ) {
					   issueMoveRequest(LANE_tY, LANE_tZ);
					   return;
				   }
				 
			   } else {
				   if (isLastMove_tS_tC())                // tS lot= tC lot
					   issueMoveRequest(LANE_tY, LANE_tC);
				   else
					   issueMoveRequest(LANE_tS, LANE_tC);   			   
			   }
    	   	}
    	   	return;   	   
       }
               
	}
             
   // more logic need to put in
	private boolean isLastMove_tS_tC() {
		GtsMove move_tY_tC = this.getMove(LANE_tY, LANE_tC);
		GtsMove move_tS_tC = this.getMove(LANE_tS, LANE_tC);
		
		if(move_tY_tC == null) return true;
		if(move_tS_tC == null) return false;
		
		return move_tS_tC.getActualTimestamp().after(move_tY_tC.getActualTimestamp());		
	}
}
