package com.honda.galc.service.gts.pbs;

import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.gts.GtsMove;

/**
 * 
 * 
 * <h3>GtsPbs1MoveDecisionRule04A Class description</h3>
 * <p> GtsPbs1MoveDecisionRule04A description </p>
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
public class GtsPbs1MoveDecisionRule04A extends GtsPbs1MoveDecisionRuleBase{

	static public boolean first_qE_wA = false;
	
	public GtsPbs1MoveDecisionRule04A() {
		super();
	}
	
	public GtsPbs1MoveDecisionRule04A(GtsPbs1TrackingServiceImpl handler) {
		super(handler);
	}
	
	public GtsPbs1TrackingServiceImpl getHandler() {
		return (GtsPbs1TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        boolean movePossible_qE_wA = isMovePossible(LANE_qE, LANE_wA);
        boolean movePossible_qH_wA = isMovePossible(LANE_qH, LANE_wA);
        
        if (! movePossible_qE_wA && !movePossible_qH_wA) return;
        
        // Only one move possible
        if(movePossible_qE_wA && !movePossible_qH_wA) {
        	issueMoveRequest(LANE_qE, LANE_wA);
     		return;
        }       

        if(!movePossible_qE_wA && movePossible_qH_wA) {
        	if(isLaneEmpty(LANE_qE)) {  	   	
        		issueMoveRequest(LANE_qH, LANE_wA);
        		return;
        	}else {
        		String lotLaneqE = area.findLane(LANE_qE).getHeadCarrier().getProductionLot();
        		String lotLaneqH = area.findLane(LANE_qH).getHeadCarrier().getProductionLot();
        		if (lotLaneqE.compareTo(lotLaneqH) >=0 || !isExitGateOpen(LANE_qE)) {
        		   	issueMoveRequest(LANE_qH, LANE_wA);
        		}
            	return;
        	}
        }
        //Both lane possible, empty or bad read carriers priority       
        GtsLane lane_qE = area.findLane(LANE_qE);
        GtsLane lane_qH = area.findLane(LANE_qH);
        GtsLaneCarrier headCarrier_qE = lane_qE.getHeadCarrier();
        GtsLaneCarrier headCarrier_qH = lane_qH.getHeadCarrier();
        
        if (headCarrier_qE.getProduct() == null) {
        	issueMoveRequest(LANE_qE,  LANE_wA);
        	return;
        }
        
        if (headCarrier_qH.getProduct() == null) {
        	issueMoveRequest(LANE_qH,  LANE_wA);
        	return;
        }
        
        if(headCarrier_qE.getProductionLot().compareTo(headCarrier_qH.getProductionLot()) < 0 ){
        	issueMoveRequest(LANE_qE,  LANE_wA);
        	return;
        }
        
        if(headCarrier_qH.getProductionLot().compareTo(headCarrier_qE.getProductionLot()) < 0 ){
        	issueMoveRequest(LANE_qH,  LANE_wA);
        	return;
        }
        
        // 2 qE-wA moves 1 qH-wA move alternatively
        
        if(isLastMove_qH_wA()) {
        	issueMoveRequest(LANE_qE,  LANE_wA);
        	first_qE_wA = true;      
        	return;
        }else if (first_qE_wA){
        	issueMoveRequest(LANE_qE,  LANE_wA);
        	first_qE_wA = false;
        	return;
        }else {
        	issueMoveRequest(LANE_qH,  LANE_wA);
        	return;
        }
        
    }  
	
	private boolean isLastMove_qH_wA() {
		GtsMove move_qE_wA = this.getMove(LANE_qE, LANE_wA);
		GtsMove move_qH_wA = this.getMove(LANE_qH, LANE_wA);
		
		if(move_qE_wA == null) return true;
		if(move_qH_wA == null) return false;
		
		return move_qH_wA.getActualTimestamp().after(move_qE_wA.getActualTimestamp());		
	}

}
