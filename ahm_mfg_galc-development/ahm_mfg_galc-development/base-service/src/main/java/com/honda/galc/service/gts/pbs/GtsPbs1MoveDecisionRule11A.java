package com.honda.galc.service.gts.pbs;

import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;

/**
 * 
 * 
 * <h3>GtsPbs1MoveDecisionRule11A Class description</h3>
 * <p> GtsPbs1MoveDecisionRule11A description </p>
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
public class GtsPbs1MoveDecisionRule11A extends GtsPbs1MoveDecisionRuleBase{

	private final String pM1_CARRIER_EMPTY = "CS-P12NPpM1CarrierEmpty";
	private final String pN1_CARRIER_EMPTY = "CS-P12NPpN1CarrierEmpty";
	
	
	public GtsPbs1MoveDecisionRule11A() {
		super();
	}
	
	public GtsPbs1MoveDecisionRule11A(GtsPbs1TrackingServiceImpl handler) {
		super(handler);
	}
	
	public GtsPbs1TrackingServiceImpl getHandler() {
		return (GtsPbs1TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        boolean movePossible_pN_pT = isMovePossible(LANE_pN, LANE_pT);
        
        boolean movePossible_pM_pT = isMovePossible(LANE_pM, LANE_pT);
        
        // Move any empty carriers at the head of lane pR
        boolean movePossible_pR_pT = isMovePossible(LANE_pR, LANE_pT);

        if(movePossible_pR_pT) {
        	issueMoveRequest(LANE_pR, LANE_pT);
        }
        
        // Move any empty carriers at the head of lanes N or M, if lane pR is not possible
        if(movePossible_pN_pT && isStatusOn(pN1_CARRIER_EMPTY)) {
        	issueMoveRequest(LANE_pN, LANE_pT);
     		return;
        }
        
        if(movePossible_pM_pT && isStatusOn(pM1_CARRIER_EMPTY)) {
        	issueMoveRequest(LANE_pM, LANE_pT);
     		return;
        }
        
        boolean movePossible_pN_pW = isMovePossible(LANE_pN, LANE_pW);
        boolean movePossible_pM_pW = isMovePossible(LANE_pM, LANE_pW);
        boolean movePossible_pL_pW = isMovePossible(LANE_pL, LANE_pW);
        
        GtsLaneCarrier headCarrier_pN = area.findLane(LANE_pN).getHeadCarrier();
        GtsLaneCarrier headCarrier_pM = area.findLane(LANE_pM).getHeadCarrier();
        GtsLaneCarrier headCarrier_pL = area.findLane(LANE_pL).getHeadCarrier();
                   
        movePossible_pN_pW = movePossible_pN_pW && (headCarrier_pN != null)
        		&& (!headCarrier_pN.getInspectionStatusString().equalsIgnoreCase("STOP"));
        movePossible_pM_pW = movePossible_pM_pW && (headCarrier_pM != null)
        		&& (!headCarrier_pM.getInspectionStatusString().equalsIgnoreCase("STOP"));  
        movePossible_pL_pW = movePossible_pL_pW && (headCarrier_pL != null)
        		&& (!headCarrier_pL.getInspectionStatusString().equalsIgnoreCase("STOP"));  

        if (!movePossible_pN_pW && !movePossible_pM_pW && !movePossible_pL_pW ) return;
        
        // Check for a current lot vehicle at the head of a storage lane
        
        String currentLot = this.getCurrentLot(LANE_pG);
        
        if(this.isSameReleasedHeadLot(movePossible_pN_pW, LANE_pN, currentLot)) {
        	issueMoveRequest(LANE_pN, LANE_pW);
     		return;
        }else if(this.isSameReleasedHeadLot(movePossible_pM_pW, LANE_pM, currentLot)) {
        	issueMoveRequest(LANE_pM, LANE_pW);
     		return;
        }else if(this.isSameReleasedHeadLot(movePossible_pL_pW, LANE_pL, currentLot)) {
        	issueMoveRequest(LANE_pL, LANE_pW);
     		return;
        }
        
        // Check for a released vehicle at the head of a storage lane, oldest lot gets priority
        //
       
        GtsLane currentLane = null;
        currentLane = getOldestReleasedHeadLotInStorage(movePossible_pN_pW,LANE_pN,currentLane);
        currentLane = getOldestReleasedHeadLotInStorage(movePossible_pM_pW,LANE_pM,currentLane);
        currentLane = getOldestReleasedHeadLotInStorage(movePossible_pL_pW,LANE_pL,currentLane);
      
        if(currentLane != null) {
        	issueMoveRequest(currentLane.getLaneName(), LANE_pW);
     		return;
        }

 
        //Release empty carriers to lane pW automatically
        if(movePossible_pN_pW && headCarrier_pN != null && headCarrier_pN.getProduct() == null && headCarrier_pN.getCarrier() != null) {   
        	issueMoveRequest(LANE_pN, LANE_pW);
     		return;
        }else if(movePossible_pM_pW & headCarrier_pM != null && headCarrier_pM.getProduct() == null  && headCarrier_pM.getCarrier() != null) {
        	issueMoveRequest(LANE_pM, LANE_pW);
     		return;
        }else if(movePossible_pL_pW & headCarrier_pL != null && headCarrier_pL.getProduct() == null  && headCarrier_pL.getCarrier() != null) {
        	issueMoveRequest(LANE_pL, LANE_pW);
     		return;
        }
        
	}  

}
