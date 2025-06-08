package com.honda.galc.device.utils;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.enumtype.LotControlRuleFlag;
import com.honda.galc.entity.enumtype.PartSerialNumberScanType;
import com.honda.galc.entity.product.LotControlRule;

/**
 * 
 * <h3>LotControlRuleValidator</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlRuleValidator description </p>
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
 * <TD>May 19, 2011</TD>
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
 * @since May 19, 2011
 */

public class LotControlRuleValidator {
	String message;
	
	public boolean validate(LotControlRule rule){
		return validate(rule, true);
	}

	public boolean validate(LotControlRule rule, boolean headed){
		
		return headed ? validateHeaded(rule) : validateHeadLess(rule);
	}
	
	/**
	 * Validate rule for Headed e.g. Lot Control Client
	 * @param rule
	 * @return
	 */
	public boolean validateHeaded(LotControlRule rule){
		message = null;
		
		//STATUS only is added for headless RGALCDEV-1508
		if(rule.getSerialNumberScanType() == PartSerialNumberScanType.STATUS_ONLY) return false;
		
		if(!StringUtils.isEmpty(rule.getStrategy())) return true;
		
		if(!checkPartSpecDefined(rule)) return false;
		
		if(!checkEmptyRule(rule)) return false; 
		
		if(!checkRuleForPartSnMaskVerification(rule)) return false;

		if(!checkInstructionCode(rule)) return false;
		
		if(!checkMeasurementCount(rule)) return false;
		
		//TODO - to add more checking
		
		return true;
	}
	
	/**
	 * Lot Control Rule validation for head less process point
	 * @param rule
	 * @return
	 */
	public boolean validateHeadLess(LotControlRule rule){
		message = null;
		
		// -- no validation for headless for now
		//if(!checkRuleForPartSnMaskVerification(rule)) return false;
		
		//TODO - to add more check
		
		return true;
	}
	

	/**
	 * Measurement count must be specified and the same number of Measurement spec must be configured
	 * 
	 * @param rule
	 * @return
	 */
	private boolean checkMeasurementCount(LotControlRule rule) {
		if(rule.getParts().get(0).getMeasurementCount() > 0 &&
				rule.getParts().get(0).getMeasurementSpecs().size() != rule.getParts().get(0).getMeasurementCount())
		{
			message = "validator failed: incomplete measurement spec configuration. " + 
			rule.getPartName().getPartName() + ":" + rule.getParts().get(0).getId().getPartId();
			return false;
		}
		return true;
	}

	/**
	 * Torque Controller instruction code must be set up when collecting torque
	 * 
	 * @param rule
	 * @return
	 */
	private boolean checkInstructionCode(LotControlRule rule) {
		if(rule.getParts().get(0).getMeasurementCount() > 0  && StringUtils.isEmpty(rule.getInstructionCode()))
		{
			//Invalid rule: Instruction code not set up properly for Torque
			message = "validator failed: Instruction code is not set up for " + 
			rule.getPartName().getPartName();
			return false;
		}
		
		return true;
	}

	/**
	 *  Not scan part serial number and Not collect torque and no valid part mask
	 *  
	 * @param rule
	 * @return
	 */
	private boolean checkEmptyRule(LotControlRule rule) {
		if(rule.getSerialNumberScanFlag() == LotControlRuleFlag.OFF.getId() &&
				rule.getParts().get(0).getMeasurementCount() <= 0 &&
				StringUtils.isEmpty(rule.getParts().get(0).getPartSerialNumberMask())) 
		{
			message = "validator failed: no part scan, no measurement and is not part mark.";
			return false;
		}
		
		return true;
	}

	/**
	 * Part_Spec must be defined
	 * @param rule
	 */
	private boolean checkPartSpecDefined(LotControlRule rule) {
		if(rule.getParts() == null || rule.getParts().size() == 0)
		{
			message = "validator failed: no part spec - imcomplete rule.";
			return false;
		}
		return true;
	}

	/**
	 * Part Serial Number mask must configured for part verification check
	 * @param rule
	 * @return
	 */
	private boolean checkRuleForPartSnMaskVerification(LotControlRule rule) {
		if(rule.isVerify() &&
				(rule.getParts() == null ||
				rule.getParts().size() == 0 ||
				StringUtils.isEmpty(rule.getParts().get(0).getPartSerialNumberMask()))){
			
			message = "validator failed: Invalid part mask for scan part.";
			return false;
		}
		
		return true;
	}
	

	public String getMessage() {
		return message;
	}
}
