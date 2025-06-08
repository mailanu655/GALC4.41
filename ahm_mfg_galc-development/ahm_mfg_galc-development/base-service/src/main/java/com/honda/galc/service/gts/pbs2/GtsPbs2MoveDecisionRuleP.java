package com.honda.galc.service.gts.pbs2;

import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsLaneCarrier;

/**
 * 
 * @author IS08925
 *
 */
public class GtsPbs2MoveDecisionRuleP extends GtsPbs2MoveDecisionRuleBase{

	public GtsPbs2MoveDecisionRuleP() {
		super();
	}
	
	public GtsPbs2TrackingServiceImpl getHandler() {
		return (GtsPbs2TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        boolean movePossible_20C_20D = isMovePossible(LANE_20C, LANE_20D);
        boolean movePossible_20C_22A = isMovePossible(LANE_20C, LANE_22A) && isLaneEmpty(LANE_20D);
        
        if(!movePossible_20C_20D && !movePossible_20C_22A) return;
        
        GtsLaneCarrier lc = getHeadOfLaneCarrier(LANE_20C);
        
        //Empty carrier goes to lane 21B only
        if(lc.isEmptyCarrier()) {
        	if(movePossible_20C_20D) {
        		issueMoveRequest(LANE_20C, LANE_20D);
        	}
        	
        	return;
        }
        
        // get biggest lot in ABS area
        
        String[] laneNames = new String[] {LANE_20D,LANE_21B,LANE_30A,LANE_30B,LANE_30C,LANE_22,LANE_22A,LANE_21E,LANE_21C};
        String biggestLot = getBiggestLot(laneNames);
        
        if(isGoodProduct(lc)) {
        	if(movePossible_20C_20D && movePossible_20C_22A) {
        		if(lc.getProduct().getLotNumber().compareTo(biggestLot) >= 0) {
        			issueMoveRequest(LANE_20C, LANE_20D);
        		}else {
        			issueMoveRequest(LANE_20C,LANE_22A);
        		}
        	}else if(movePossible_20C_20D) {
        		issueMoveRequest(LANE_20C, LANE_20D);
        	}else {
        		issueMoveRequest(LANE_20C, LANE_22A);
        	}
        }
        
    }  	
	
	private String getBiggestLot(String[] laneNames) {
		String biggestLot = "";
        for (String laneName : laneNames) {
            String tmpLot = getBiggestLot(laneName);
            if(tmpLot.compareTo(biggestLot) > 0) biggestLot = tmpLot;
        }
        return biggestLot;
	}
}
