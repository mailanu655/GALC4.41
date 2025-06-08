package com.honda.galc.client.datacollection.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.entity.enumtype.StrategyType;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;

/**
 * 
 * <h3>PartSnProcessorWithDuplicateCheck</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PartSnProcessorWithDuplicateCheck description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Oct 21, 2013</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Oct 21, 2013
 */
public class PartSnProcessorDuplicateCheckLocal extends PartSerialNumberProcessor {

	public PartSnProcessorDuplicateCheckLocal(ClientContext context) {
		super(context);
		
	}

	@Override
	protected void checkDuplicatePart(String partnumber) {
		
		checkDuplicatePartLocal(partnumber);
		
		// TODO Auto-generated method stub
		super.checkDuplicatePart(partnumber);
	}

	protected void checkDuplicatePartLocal(String partnumber) {
		//Only check parts with this Strategy is defined
		List<String> checkPartNames = getCheckPartNames();
		
		if(checkPartNames.size() == 0) return; //No need to go through the list
		
		List<InstalledPart> duplidatedPartList = new ArrayList<InstalledPart>();
		for(InstalledPart p: getController().getState().getProduct().getPartList())
			if(checkPartNames.contains(p.getPartName()) && 
					StringUtils.equals(partnumber, p.getPartSerialNumber()) &&
					(p.isValidPartSerialNumber() && !p.isSkipped()))
				duplidatedPartList.add(p);
		
		if(duplidatedPartList.size() > 0){
			installedPart.setValidPartSerialNumber(false);
			
			StringBuilder sb = new StringBuilder();
			sb.append("Duplicate Part:").append(partnumber).append(" was already installed on this product."); 
					
			handleException(sb.toString());
		}
	}
	
	protected List<String> getCheckPartNames() {
		List<String> checkPartNames = new ArrayList<String>();
		for (LotControlRule r : getController().getState().getLotControlRules()){
			if(!StringUtils.isEmpty(r.getStrategy()) && 
					!(r.getPartNameString().equals(getController().getCurrentLotControlRule().getPartNameString())) &&
					(StrategyType.DUP_PART_SN_VALIDATION == StrategyType.valueOf(r.getStrategy())))
				
			        checkPartNames.add(r.getPartNameString());
		}
		return checkPartNames;
	}
	
	

}
