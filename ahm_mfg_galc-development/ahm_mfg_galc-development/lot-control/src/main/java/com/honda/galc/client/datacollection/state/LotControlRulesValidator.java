package com.honda.galc.client.datacollection.state;
import java.util.List;

import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.view.ImageViewManager;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.utils.LotControlRuleValidator;
import com.honda.galc.entity.enumtype.StrategyType;
import com.honda.galc.entity.product.LotControlRule;

/**
 * <h3>LotControlRuleValidator</h3>
 * <h4>
 * Simple validator to validate Lot Control Rule
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Aug.19, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */
public class LotControlRulesValidator {
	static LotControlRuleValidator validator = new LotControlRuleValidator();
	/**
	 * Simple lot control rule validator
	 * @param rules
	 * @return
	 */
	public static boolean validate(List<LotControlRule> rules){
		if(rules.size() == 0)
			Logger.getLogger().warn("validator warning: no lot control rules defined!");
		
		for(LotControlRule rule : rules){
			
			if(!validator.validate(rule)){
				Logger.getLogger().warn(validator.getMessage());
				return false;
			}
		}

		return true;
	}
}
