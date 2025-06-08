package com.honda.galc.service.gts.pbs;

import com.honda.galc.entity.gts.GtsArea;

/**
 * 
 * 
 * <h3>GtsPbs1MoveDecisionRule02A Class description</h3>
 * <p> GtsPbs1MoveDecisionRule02A description </p>
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
public class GtsPbs1MoveDecisionRule02A extends GtsPbs1MoveDecisionRuleBase{

	public GtsPbs1MoveDecisionRule02A() {
		super();
	}
	
	public GtsPbs1MoveDecisionRule02A(GtsPbs1TrackingServiceImpl handler) {
		super(handler);
	}
	
	public GtsPbs1TrackingServiceImpl getHandler() {
		return (GtsPbs1TrackingServiceImpl)super.getHandler();
	}
	
	@Override
	public void executeRule(GtsArea area) {
		this.area = area;
        if(area == null) return;
        
        boolean movePossible_tC_tH = isMovePossible(LANE_tC, LANE_tH);
        
         
        // Only one move possible
        if(movePossible_tC_tH) {
        	issueMoveRequest(LANE_tC, LANE_tH);
     		return;
        }       
	}

}
