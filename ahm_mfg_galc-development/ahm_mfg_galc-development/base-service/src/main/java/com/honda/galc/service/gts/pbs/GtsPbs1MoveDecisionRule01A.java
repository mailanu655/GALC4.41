package com.honda.galc.service.gts.pbs;

import com.honda.galc.entity.gts.GtsArea;

/**
 * 
 * 
 * <h3>GtsPbs1MoveDecisionRule01A Class description</h3>
 * <p> GtsPbs1MoveDecisionRule01A description </p>
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
 * Oct.26, 2018
 *
 *
 */
public class GtsPbs1MoveDecisionRule01A extends GtsPbs1MoveDecisionRuleBase{

	public GtsPbs1MoveDecisionRule01A() {
		super();
	}
	
	public GtsPbs1MoveDecisionRule01A(GtsPbs1TrackingServiceImpl handler) {
		super(handler);
	}
	
	public GtsPbs1TrackingServiceImpl getHandler() {
		return (GtsPbs1TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        boolean movePossible_tA_tY = isMovePossible(LANE_tA, LANE_tY);
        boolean movePossible_tA_tD = isMovePossible(LANE_tA, LANE_tD);
       
        // The default move is from tA to tY (long track). Only if this move
        // is not possible will a carrier be moved to tD (short track)
        
        if(movePossible_tA_tD ) {	   	
        	issueMoveRequest(LANE_tA, LANE_tD);
     		return;
        }
        
        if(movePossible_tA_tY) {
        	issueMoveRequest(LANE_tA, LANE_tY);
     		return;
        }       

    }  	

}
