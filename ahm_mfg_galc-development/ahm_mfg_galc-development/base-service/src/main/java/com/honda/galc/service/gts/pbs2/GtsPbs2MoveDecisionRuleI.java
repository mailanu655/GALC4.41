package com.honda.galc.service.gts.pbs2;

import com.honda.galc.entity.gts.GtsArea;

/**
 * @author IS08925
 *
 */
public class GtsPbs2MoveDecisionRuleI extends GtsPbs2MoveDecisionRuleBase{

	public GtsPbs2MoveDecisionRuleI() {
		super();
	}
	
	public GtsPbs2TrackingServiceImpl getHandler() {
		return (GtsPbs2TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        if(isMovePossible(LANE_13, LANE_14A)) {
        	issueMoveRequest(LANE_13, LANE_14A);
        }

    }  	

}
