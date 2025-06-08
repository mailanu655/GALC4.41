package com.honda.galc.service.gts.pbs;

import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;

/**
 * 
 * 
 * <h3>GtsPbs1MoveDecisionRule16A Class description</h3>
 * <p> GtsPbs1MoveDecisionRule16A description </p>
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
public class GtsPbs1MoveDecisionRule16A extends GtsPbs1MoveDecisionRuleBase{

	public GtsPbs1MoveDecisionRule16A() {
		super();
	}
	
	public GtsPbs1MoveDecisionRule16A(GtsPbs1TrackingServiceImpl handler) {
		super(handler);
	}
	
	public GtsPbs1TrackingServiceImpl getHandler() {
		return (GtsPbs1TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        // First priority is removing any empty carriers at the head of lanes N or M, if lane pR is not full
        
        boolean movePossible_pE_pS = isMovePossible(LANE_pE, LANE_pS);              
        boolean movePossible_pF_pS = isMovePossible(LANE_pF, LANE_pS);
        boolean movePossible_pK_pS = isMovePossible(LANE_pK, LANE_pS);
        boolean movePossible_pW_pS = isMovePossible(LANE_pW, LANE_pS);
                  
        GtsLaneCarrier headCarrier_pW = area.findLane(LANE_pW).getHeadCarrier();
        GtsLaneCarrier headCarrier_pK = area.findLane(LANE_pK).getHeadCarrier();
        GtsLaneCarrier headCarrier_pF = area.findLane(LANE_pF).getHeadCarrier();
        GtsLaneCarrier headCarrier_pE = area.findLane(LANE_pE).getHeadCarrier();
            
        movePossible_pE_pS = movePossible_pE_pS && (headCarrier_pE != null)
        		&& (!headCarrier_pE.getInspectionStatusString().equalsIgnoreCase("STOP"));        
        movePossible_pF_pS = movePossible_pF_pS && (headCarrier_pF != null)
        		&& (!headCarrier_pF.getInspectionStatusString().equalsIgnoreCase("STOP"));
        movePossible_pK_pS = movePossible_pK_pS && (headCarrier_pK != null)
        		&& (!headCarrier_pK.getInspectionStatusString().equalsIgnoreCase("STOP"));
        movePossible_pW_pS = movePossible_pW_pS && (headCarrier_pW != null)
        		&& (!headCarrier_pW.getInspectionStatusString().equalsIgnoreCase("STOP"));
        
        if(!movePossible_pW_pS &&!movePossible_pE_pS && !movePossible_pF_pS && !movePossible_pK_pS) {
        	return;
        }
        
        // Check for a current lot vehicle at the head of a storage lane
        
        String currentLot = this.getCurrentLot(LANE_pG);
       
        if(this.isSameReleasedHeadLot(movePossible_pW_pS, LANE_pW, currentLot)) {
        	issueMoveRequest(LANE_pW, LANE_pS);
     		return;
        }else if(this.isSameReleasedHeadLot(movePossible_pK_pS, LANE_pK, currentLot)) {
        	issueMoveRequest(LANE_pK, LANE_pS);
     		return;
        }else if(this.isSameReleasedHeadLot(movePossible_pF_pS, LANE_pF, currentLot)) {
        	issueMoveRequest(LANE_pF, LANE_pS);
     		return;
        }else if(this.isSameReleasedHeadLot(movePossible_pE_pS, LANE_pE, currentLot)) {
        	issueMoveRequest(LANE_pE, LANE_pS);
     		return;
        }
        
        // Check for a released vehicle at the head of a storage lane, oldest lot gets priority
        //
        GtsLane currentLane = null;
        currentLane = getOldestReleasedHeadLotInStorage(movePossible_pW_pS,LANE_pW,currentLane);
        currentLane = getOldestReleasedHeadLotInStorage(movePossible_pK_pS,LANE_pK,currentLane);
        currentLane = getOldestReleasedHeadLotInStorage(movePossible_pF_pS,LANE_pF,currentLane);
        currentLane = getOldestReleasedHeadLotInStorage(movePossible_pE_pS,LANE_pE,currentLane);
        
        if(currentLane != null) {
        	issueMoveRequest(currentLane.getLaneName(), LANE_pS);
     		return;
        }
        
        //Release empty carriers to lane pW automatically
        
        if((movePossible_pW_pS & headCarrier_pW != null && headCarrier_pW.getProduct() == null  && headCarrier_pW.getCarrier() != null)) {
        	issueMoveRequest(LANE_pW, LANE_pS);
     		return;
        }else if((movePossible_pK_pS & headCarrier_pK != null && headCarrier_pK.getProduct() == null  && headCarrier_pK.getCarrier() != null)) {
        	issueMoveRequest(LANE_pK, LANE_pS);
     		return;
        }else if((movePossible_pF_pS & headCarrier_pF != null && headCarrier_pF.getProduct() == null  && headCarrier_pF.getCarrier() != null)) {
        	issueMoveRequest(LANE_pF, LANE_pS);
     		return;
        }else if((movePossible_pE_pS & headCarrier_pE != null && headCarrier_pE.getProduct() == null  && headCarrier_pE.getCarrier() != null)) {
        	issueMoveRequest(LANE_pE, LANE_pS);
     		return;
        }
        
	}   

}
