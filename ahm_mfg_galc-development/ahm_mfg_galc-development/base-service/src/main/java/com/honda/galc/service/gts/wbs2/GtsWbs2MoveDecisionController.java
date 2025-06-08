package com.honda.galc.service.gts.wbs2;

import java.util.List;
import java.util.Map;

import com.honda.galc.entity.enumtype.GtsIndicatorType;
import com.honda.galc.entity.gts.GtsIndicator;
import com.honda.galc.entity.gts.GtsMove;
import com.honda.galc.entity.gts.GtsNode;
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

public class GtsWbs2MoveDecisionController extends AbstractDecisionController{
	
	private final String LANE_MERGE = "Merge";
	

	public GtsWbs2MoveDecisionController(AbstractBodyTrackingService handler) {
		super(handler);
	}

	@Override
	protected Map<String, AbstractDecisionRule> defineDecisionRules() {
		return null;
	}
	
    public void executeRules(GtsIndicator indicator, List<GtsMove> expiredMoves){
       	if(indicator.getIndicatorType().equals(GtsIndicatorType.MOVE_CONTROL_REQUEST)  && indicator.isStatusOn()) {
    		if(indicator.getSourceLaneName().equals(LANE_MERGE)) {
    			new GtsWbs2StorageInMoveDecisionRule(handler).executeRule(indicator);
    		}else {
    			new GtsWbs2StorageOutMoveDecisionRule(handler).executeRule(indicator);;
    		}
    	}
    }
    
	   // execute rules based on open/close gate
    public void executeRules(GtsNode gate, List<GtsMove> expiredMoves){
    }

}
