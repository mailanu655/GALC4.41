package com.honda.galc.service.gts.pbs2;

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
public class GtsPbs2MoveDecisionRuleQ extends GtsPbs2MoveDecisionRuleBase{

	public GtsPbs2MoveDecisionRuleQ() {
		super();
	}
	
	public GtsPbs2TrackingServiceImpl getHandler() {
		return (GtsPbs2TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        if(isMovePossible(LANE_23, LANE_23A)) {
        	issueMoveRequest(LANE_23, LANE_23A);
        }
        
     }  	

}
