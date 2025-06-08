package com.honda.galc.client.product.validator;

import java.util.List;

import com.honda.galc.client.product.process.model.ProcessModel;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.LotControlPartUtil;

/**
 * 
 * 
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>LotControlRuleValidator</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class LotControlRuleValidator extends AbstractValidator {

	private String partName;
	private List<LotControlRule> rules;
	private ProcessModel model;
	private UniqueInstalledPartValidator uniqueValidator;

	public LotControlRuleValidator(String partName, List<LotControlRule> rules, ProcessModel model) {
		this.partName = partName;
		this.rules = rules;
		this.model = model;
		this.uniqueValidator = new UniqueInstalledPartValidator(getPartName(), getModel());
		setDetailedMessageTemplate("{0} failed lot control rule");
	}

	public boolean execute(String value) {
		LotControlRule rule = findLotControlRuleByPartName(getRules(), getModel().getProduct(), getPartName());
				
		if (rule == null) {
			String specCode = getModel().getProduct().getProductSpecCode();
			setDetailedMessageTemplate(String.format("No Lot Controle Rule is defined for part and spec: %s, %s", getPartName(), specCode));
			return false;
		}

		if (rule.isVerify()) {
			if (!isValidMask(rule, value)) {
				return false;
			}
		}

		if (rule.isUnique()) {
			if (!getUniqueValidator().execute(value)) {
				setDetailedMessageTemplate(getUniqueValidator().getDetailedMessageTemplate());
				return false;
			}
		}
		return true;
	}

	private LotControlRule findLotControlRuleByPartName(List<LotControlRule> rules, BaseProduct product, String partName) {
		BaseProductSpec findByProductSpecCode = ProductTypeUtil.getProductSpecDao(product.getProductType()).findByProductSpecCode(product.getProductSpecCode(), product.getProductType().toString());
		List<LotControlRule> ruleList = LotControlPartUtil.getLotControlRuleByProductSpec(findByProductSpecCode, rules);
		for(LotControlRule r : ruleList){
			if(partName.equals(r.getPartName().getPartName()))
					return r;
		}
	
		return null;
	}

	protected boolean isValidMask(LotControlRule rule, String value) {
		for (PartSpec partSpec : rule.getParts()) {
			String mask = partSpec.getPartSerialNumberMask();
			mask = mask.trim();
			if (CommonPartUtility.verification(value, mask, PropertyService.getPartMaskWildcardFormat())) {
				return true;
			}
		}
		String msg = String.format("{0} must match %s", CommonPartUtility.parsePartMask(rule.getPartMasks()));
		setDetailedMessageTemplate(msg);
		return false;
	}


	// === get/set === //
	protected List<LotControlRule> getRules() {
		return rules;
	}

	protected ProcessModel getModel() {
		return model;
	}

	protected String getPartName() {
		return partName;
	}

	protected UniqueInstalledPartValidator getUniqueValidator() {
		return uniqueValidator;
	}
}
