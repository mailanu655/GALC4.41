package com.honda.galc.service.gts.ebs2;

import java.util.Map;

import com.honda.galc.service.gts.AbstractBodyTrackingService;
import com.honda.galc.service.gts.AbstractDecisionController;
import com.honda.galc.service.gts.AbstractDecisionRule;

/**
 * 
 * 
 * <h3>GtsPbs1MoveDecisionController Class description</h3>
 * <p> GtsPbs1MoveDecisionController description </p>
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
 * @author is08925<br>
 * Nov 30, 2017
 *
 *
 */

public class GtsEbs2MoveDecisionController extends AbstractDecisionController{

	public GtsEbs2MoveDecisionController(AbstractBodyTrackingService handler) {
		super(handler);
	}

	@Override
	protected Map<String, AbstractDecisionRule> defineDecisionRules() {
		// TODO Auto-generated method stub
		return null;
	}

}
