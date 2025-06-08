package com.honda.galc.service.gts.pbs;

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

public class GtsPbs1MoveDecisionController extends AbstractDecisionController{

	public GtsPbs1MoveDecisionController(AbstractBodyTrackingService handler) {
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
			rules.put(ruleValue.getDecisionPoint().getId().getDecisionPointId(), ruleValue);
		}
		
		if(!StringUtils.isEmpty(destinationLane)) {
			//MP-pP-pM and MP_pP-pN, evaluate DP_15
			if (sourceLane.equalsIgnoreCase("pP") && 			
				(destinationLane.equalsIgnoreCase("pM") || destinationLane.equalsIgnoreCase("pN")))
				addRule(area,GtsPbs1MoveDecisionRule15A.class,rules);
		}else {
			if (sourceLane.equalsIgnoreCase("pR")) { 
				addRule(area,GtsPbs1MoveDecisionRule10A.class,rules);
			}else if(sourceLane.equalsIgnoreCase("pV") || sourceLane.equalsIgnoreCase("pE")||
			   sourceLane.equalsIgnoreCase("pF") || sourceLane.equalsIgnoreCase("pK")){
				addRule(area,GtsPbs1MoveDecisionRule09A.class,rules);
			} else if(sourceLane.equalsIgnoreCase("pQ") || sourceLane.equalsIgnoreCase("pL") ||
					  sourceLane.equalsIgnoreCase("pM") || sourceLane.equalsIgnoreCase("pN") ) {
				addRule(area,GtsPbs1MoveDecisionRule09A.class,rules);
				addRule(area,GtsPbs1MoveDecisionRule15A.class,rules);
			} else if (sourceLane.equalsIgnoreCase("pG")) {
				addRule(area,GtsPbs1MoveDecisionRule08A.class,rules);
			} else if (sourceLane.equalsIgnoreCase("tW")) {
				addRule(area,GtsPbs1MoveDecisionRule05A.class,rules);
			}
		}
			
		return new ArrayList<AbstractDecisionRule>(rules.values());
	}
	
	private void addRule(GtsArea area, Class<?> ruleClass,Map<Integer,AbstractDecisionRule> rules) {
		AbstractDecisionRule rule = getDecisionRule(area, ruleClass);
		if(rule != null) rules.put(rule.getDecisionPoint().getDecisionPointId(),rule);
	}

}
