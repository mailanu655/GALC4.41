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
public class GtsPbs1MoveDecisionRule12A extends GtsPbs1MoveDecisionRuleBase{

	public GtsPbs1MoveDecisionRule12A() {
		super();
	}
	
	public GtsPbs1MoveDecisionRule12A(GtsPbs1TrackingServiceImpl handler) {
		super(handler);
	}
	
	public GtsPbs1TrackingServiceImpl getHandler() {
		return (GtsPbs1TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
               
        boolean movePossible_pS_pG = isMovePossible(LANE_pS, LANE_pG);
        boolean movePossible_pD_pG = isMovePossible(LANE_pD, LANE_pG);
        boolean movePossible_pC_pG = isMovePossible(LANE_pC, LANE_pG);
        
        GtsLane lane_pS = area.findLane(LANE_pS);   
        GtsLane lane_pC = area.findLane(LANE_pC);   
        GtsLane lane_pD = area.findLane(LANE_pD);   
        GtsLaneCarrier headCarrier_pS = lane_pS.getHeadCarrier();
        GtsLaneCarrier headCarrier_pC = lane_pC.getHeadCarrier();
        GtsLaneCarrier headCarrier_pD = lane_pD.getHeadCarrier();
        
        movePossible_pS_pG = movePossible_pS_pG && (headCarrier_pS != null)
        		&& (!headCarrier_pS.getInspectionStatusString().equalsIgnoreCase("STOP"));
        movePossible_pC_pG = movePossible_pC_pG && (headCarrier_pC != null)
        		&& (!headCarrier_pC.getInspectionStatusString().equalsIgnoreCase("STOP"));
        movePossible_pD_pG = movePossible_pD_pG && (headCarrier_pD != null)
        		&& (!headCarrier_pD.getInspectionStatusString().equalsIgnoreCase("STOP"));
        
        String currentLot = this.getCurrentLot(LANE_pG);
        
        // Check for a current lot vehicle at the head of a storage lane
        if (movePossible_pS_pG || movePossible_pC_pG || movePossible_pD_pG) {
           
        	if(this.isSameHeadLot(movePossible_pC_pG, LANE_pC, currentLot)) {
        		issueMoveRequest(LANE_pC, LANE_pG);
        		return;
        	}else if(this.isSameHeadLot(movePossible_pD_pG, LANE_pD, currentLot)) {
        		issueMoveRequest(LANE_pD, LANE_pG);
        		return;
        	}else if(this.isSameHeadLot(movePossible_pS_pG, LANE_pS, currentLot)) {
        		issueMoveRequest(LANE_pS, LANE_pG);
        		return;
        	}	
        	// Check for a released vehicle at the head of a storage lane, oldest lot gets priority
        	//
        	GtsLane currentLane = null;
        	currentLane = getOldestReleasedHeadLotInStorage(movePossible_pC_pG,LANE_pC,currentLane);
        	currentLane = getOldestReleasedHeadLotInStorage(movePossible_pD_pG,LANE_pD,currentLane);
        	currentLane = getOldestReleasedHeadLotInStorage(movePossible_pS_pG,LANE_pS,currentLane);
        
        	if(currentLane != null) {
        		issueMoveRequest(currentLane.getLaneName(), LANE_pG);
        		return;
        	}
        }
        // Check if we should loop a non-released car at the head of lane to get at a current lot car
        //
        boolean movePossible_pC_pH = isMovePossible(LANE_pC, LANE_pH)
        		&& (!headCarrier_pC.getInspectionStatusString().equalsIgnoreCase("STOP"));
        boolean movePossible_pD_pH = isMovePossible(LANE_pD, LANE_pH)
        		&& (!headCarrier_pD.getInspectionStatusString().equalsIgnoreCase("STOP"));
        boolean movePossible_pS_pH = isMovePossible(LANE_pS, LANE_pH)
        		&& (!headCarrier_pS.getInspectionStatusString().equalsIgnoreCase("STOP"));
        
        if(!movePossible_pC_pH && !movePossible_pD_pH && !movePossible_pS_pH) return;
        
        if(movePossible_pC_pH || movePossible_pD_pH) {
        	int count_pC = area.findLane(LANE_pC).getLaneCarriers().size();
        	int count_pD = area.findLane(LANE_pD).getLaneCarriers().size();
        	int count = count_pC > count_pD ? count_pC : count_pD;
        
        	for (int i = 1; i< count; i++) {
        		if(isSameLot(movePossible_pC_pH, LANE_pC, i, currentLot)) {
        			issueMoveRequest(LANE_pC, LANE_pH);
        			return;
        		}else if(isSameLot(movePossible_pD_pH, LANE_pD, i, currentLot)) {
        			issueMoveRequest(LANE_pD, LANE_pH);
        			return;
        			}
        		}
        	}
        
        if(movePossible_pS_pH) {
        	issueMoveRequest(LANE_pS, LANE_pH);
     		return;
        }
        
    }    

}
