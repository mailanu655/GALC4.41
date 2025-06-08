package com.honda.galc.service.gts.pbs;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.message.Message;
import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.service.gts.AbstractDecisionRule;

/**
 * 
 * 
 * <h3>GtsPbs1MoveDecisionRuleBase Class description</h3>
 * <p> GtsPbs1MoveDecisionRuleBase description </p>
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
public abstract class GtsTbs1MoveDecisionRuleBase extends AbstractDecisionRule{

	protected final String LANE_toC = "toC";  
	protected final String LANE_toD = "toD";
	protected final String LANE_tsA = "tsA";
	
	protected final String LANE_tsC = "tsC";
	protected final String LANE_tsD = "tsD";
	protected final String LANE_tsE = "tsE";
	
	protected final String LANE_tsF = "tsF";
	protected final String LANE_tsH = "tsH";

	protected final String LANE_tsI = "tsI";
	protected final String LANE_tsR = "tsR";
	protected final String LANE_tsS = "tsS";
	
	public GtsTbs1MoveDecisionRuleBase() {
		super();
	}
	
	public GtsTbs1MoveDecisionRuleBase(GtsTbs1TrackingServiceImpl handler) {
		super(handler);
	}
	
}
