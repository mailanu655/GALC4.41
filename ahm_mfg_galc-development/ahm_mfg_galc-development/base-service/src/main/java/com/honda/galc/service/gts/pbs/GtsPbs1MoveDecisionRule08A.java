package com.honda.galc.service.gts.pbs;

import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsMove;

/**
 * 
 * 
 * <h3>GtsPbs1MoveDecisionRule08A Class description</h3>
 * <p> GtsPbs1MoveDecisionRule08A description </p>
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
//*  This is the Decision Point for moving carriers from pA into pI or pB  *
//*  Also for moving carriers from pH to pB.                               *
//*                                                                        *
//**************************************************************************

public class GtsPbs1MoveDecisionRule08A extends GtsPbs1MoveDecisionRuleBase{

	public GtsPbs1MoveDecisionRule08A() {
		super();
	}
	
	public GtsPbs1MoveDecisionRule08A(GtsPbs1TrackingServiceImpl handler) {
		super(handler);
	}
	
	public GtsPbs1TrackingServiceImpl getHandler() {
		return (GtsPbs1TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        String currentLot = getCurrentLot(LANE_pI);
        
        boolean isMovePossible_pA_pI = isMovePossible(LANE_pA, LANE_pI);
        boolean isMovePossible_pA_pB = isMovePossible(LANE_pA, LANE_pB);
        
        boolean movePossible_pA_pI = isSameReleasedHeadLot(isMovePossible_pA_pI,LANE_pA,currentLot) ||
               		 (!isEntryGateOpen(LANE_pB) && !isExitGateOpen(LANE_pG));		

        boolean movePossible_pA_pB = isMovePossible_pA_pB && !(isSameReleasedHeadLot(isMovePossible_pA_pB,LANE_pA,currentLot) &&
        		area.findLane(LANE_pA).getLaneCarriers().size() < 3);
        
        boolean movePossible_pH_pB = isMovePossible(LANE_pH, LANE_pB);
        
        if(!movePossible_pA_pI && !movePossible_pA_pB && !movePossible_pH_pB) return;
        
        if(movePossible_pA_pI) {
        	issueMoveRequest(LANE_pA, LANE_pI);
        	
        	if(movePossible_pH_pB) {
        		issueMoveRequest(LANE_pH, LANE_pB);
           }
           return;
     	}
        
        if(!movePossible_pA_pB && movePossible_pH_pB) {
        	issueMoveRequest(LANE_pH, LANE_pB);
        	return;
        }
        
        if(movePossible_pA_pB && !movePossible_pH_pB) {
        	issueMoveRequest(LANE_pA, LANE_pB);
        	return;
        }
        
        if(isLastMove_pA_pB()) {
        	issueMoveRequest(LANE_pH, LANE_pB);
        }else {
        	issueMoveRequest(LANE_pA, LANE_pB);
        }
        
    }
	
	private boolean isLastMove_pA_pB() {
		GtsMove move_pA_pB = this.getMove(LANE_pA, LANE_pB);
		GtsMove move_pH_pB = this.getMove(LANE_pH, LANE_pB);
		
		if(move_pA_pB == null) return false;
		if(move_pH_pB == null) return true;
		
		return move_pA_pB.getActualTimestamp().after(move_pH_pB.getActualTimestamp());
		
	}

}
