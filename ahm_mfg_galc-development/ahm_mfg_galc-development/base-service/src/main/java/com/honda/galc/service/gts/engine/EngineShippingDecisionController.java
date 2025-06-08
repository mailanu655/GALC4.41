package com.honda.galc.service.gts.engine;

import java.util.HashMap;
import java.util.Map;

import com.honda.galc.service.gts.AbstractDecisionController;
import com.honda.galc.service.gts.AbstractDecisionRule;


/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ShippingDecisionController</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Jeffray Huang</TD>
 * <TD>Mar 30, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 */

public class EngineShippingDecisionController extends AbstractDecisionController{

    
    public EngineShippingDecisionController(GtsEngineBodyTrackingServiceImpl handler) {
        super(handler);
    }
    
    
    public GtsEngineBodyTrackingServiceImpl getHandler(){
        return (GtsEngineBodyTrackingServiceImpl) handler;
    }

    
    @Override
    protected Map<String, AbstractDecisionRule> defineDecisionRules() {
        Map<String,AbstractDecisionRule> map = new HashMap<String,AbstractDecisionRule>();
        map.put("Shipping", new EngineShippingDecisionRule(getHandler()));
        return map;
    }

}
