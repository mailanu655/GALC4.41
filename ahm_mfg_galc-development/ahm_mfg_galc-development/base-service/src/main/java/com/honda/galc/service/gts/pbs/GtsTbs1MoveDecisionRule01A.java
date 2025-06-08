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
public class GtsTbs1MoveDecisionRule01A extends GtsTbs1MoveDecisionRuleBase{

	public GtsTbs1MoveDecisionRule01A() {
		super();
	}
	
	public GtsTbs1MoveDecisionRule01A(GtsTbs1TrackingServiceImpl handler) {
		super(handler);
	}
	
	public GtsPbs1TrackingServiceImpl getHandler() {
		return (GtsPbs1TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        boolean movePossible_toC_toD = isMovePossible(LANE_toC, LANE_toD);
        boolean movePossible_toC_tsA = isMovePossible(LANE_toC, LANE_tsA);
       
        // The default move is from toC to toD. Only if this move
        // is not possible will a carrier be moved to tsA 
        
        if(movePossible_toC_toD ) {	   	
        	issueMoveRequest(LANE_toC, LANE_toD);
     		return;
        }
        
        if(movePossible_toC_tsA) {
        	issueMoveRequest(LANE_toC, LANE_tsA);
     		return;
        }       

    }  	

}
