package com.honda.galc.service.gts.pbs;

import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsMove;

/**
 * 
 * 
 * <h3>GtsPbs1MoveDecisionRule17A Class description</h3>
 * <p> GtsPbs1MoveDecisionRule17A description </p>
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
public class GtsPbs1MoveDecisionRule17A extends GtsPbs1MoveDecisionRuleBase{

	public GtsPbs1MoveDecisionRule17A() {
		super();
	}
	
	public GtsPbs1MoveDecisionRule17A(GtsPbs1TrackingServiceImpl handler) {
		super(handler);
	}
	
	public GtsPbs1TrackingServiceImpl getHandler() {
		return (GtsPbs1TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        boolean movePossible_tE_tG = isMovePossible(LANE_tE, LANE_tG);
        boolean movePossible_tF_tG = isMovePossible(LANE_tF, LANE_tG);
        
        if (! movePossible_tE_tG && !movePossible_tF_tG) return;
        
        // Only one move possible
        if(movePossible_tE_tG && !movePossible_tF_tG) {
        	issueMoveRequest(LANE_tE, LANE_tG);
     		return;
        }       

        if(!movePossible_tE_tG && movePossible_tF_tG) {
        	if(isLaneEmpty(LANE_tE)) {  	   	
        	issueMoveRequest(LANE_tF, LANE_tG);
     		return;
        	}
        	else {
        		String lotLanetE = area.findLane(LANE_tE).getHeadCarrier().getProductionLot();
        		String lotLanetF = area.findLane(LANE_tF).getHeadCarrier().getProductionLot();
        		if (lotLanetE.compareTo(lotLanetF) >=0 || !isExitGateOpen(LANE_tE)) {
        		   	issueMoveRequest(LANE_tF, LANE_tG);}
            	return;
        		}
        }
        //Both lane possible, empty or bad read carriers priority       
        GtsLane lane_tE = area.findLane(LANE_tE);
        GtsLane lane_tF = area.findLane(LANE_tF);
        GtsLaneCarrier headCarrier_tE = lane_tE.getHeadCarrier();
        GtsLaneCarrier headCarrier_tF = lane_tF.getHeadCarrier();
        
        if (headCarrier_tE.getProduct() == null) {
        	issueMoveRequest(LANE_tE,  LANE_tG);
        	return;
        }
        
        if (headCarrier_tF.getProduct() == null) {
        	issueMoveRequest(LANE_tF,  LANE_tG);
        	return;
        }
        
        if(headCarrier_tE.getProductionLot().compareTo(headCarrier_tF.getProductionLot()) < 0 ){
        	issueMoveRequest(LANE_tE,  LANE_tG);
        	return;
        }
        
        if(headCarrier_tF.getProductionLot().compareTo(headCarrier_tE.getProductionLot()) < 0 ){
        	issueMoveRequest(LANE_tF,  LANE_tG);
        	return;
        }
        
        if(isLastMove_tF_tG()) {
        	issueMoveRequest(LANE_tE,  LANE_tG);
        	return;
        }
        else {
        	issueMoveRequest(LANE_tF,  LANE_tG);
        	return;
        }
        
    }  
	
	private boolean isLastMove_tF_tG() {
		GtsMove move_tE_tG = this.getMove(LANE_tE, LANE_tG);
		GtsMove move_tF_tG = this.getMove(LANE_tF, LANE_tG);
		
		if(move_tE_tG == null) return true;
		if(move_tF_tG == null) return false;
		
		return move_tF_tG.getActualTimestamp().after(move_tE_tG.getActualTimestamp());		
	}

}
