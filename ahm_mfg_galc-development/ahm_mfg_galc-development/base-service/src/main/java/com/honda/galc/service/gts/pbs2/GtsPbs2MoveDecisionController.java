package com.honda.galc.service.gts.pbs2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsMove;
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

public class GtsPbs2MoveDecisionController extends AbstractDecisionController{

	public GtsPbs2MoveDecisionController(AbstractBodyTrackingService handler) {
		super(handler);
	}

	@Override
	protected Map<String, AbstractDecisionRule> defineDecisionRules() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<AbstractDecisionRule> getDecisionRules(GtsArea area, List<GtsMove> expiredMoves,String sourceLane, String destinationLane) {      
		
		Map<Integer,AbstractDecisionRule> rules = new HashMap<Integer,AbstractDecisionRule>();
        		
		List<AbstractDecisionRule> ruleValues = super.getDecisionRules(area, expiredMoves, sourceLane, destinationLane);
		
		for (AbstractDecisionRule ruleValue:ruleValues) {
			ruleValue.setHandler(handler);
			rules.put(ruleValue.getDecisionPoint().getId().getDecisionPointId(), ruleValue);
		}
		
			
		return new ArrayList<AbstractDecisionRule>(rules.values());
	}
	
	private void addRule(GtsArea area, Class<?> ruleClass,Map<Integer,AbstractDecisionRule> rules) {
		AbstractDecisionRule rule = getDecisionRule(area, ruleClass);
		if(rule != null) rules.put(rule.getDecisionPoint().getDecisionPointId(),rule);
	}

}
