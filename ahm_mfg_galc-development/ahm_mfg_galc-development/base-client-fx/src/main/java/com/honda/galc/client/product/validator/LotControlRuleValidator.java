package com.honda.galc.client.product.validator;

import java.util.List;

import com.honda.galc.client.product.process.AbstractProcessModel;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonPartUtility;

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
	private AbstractProcessModel model;
	private UniqueInstalledPartValidator uniqueValidator;

	public LotControlRuleValidator(String partName, List<LotControlRule> rules, AbstractProcessModel model) {
		this.partName = partName;
		this.rules = rules;
		this.model = model;
		this.uniqueValidator = new UniqueInstalledPartValidator(getPartName(), getModel());
		setDetailedMessageTemplate("{0} failed lot control rule");
	}

	public boolean execute(String value) {
		LotControlRule rule = find(getRules(), getModel().getProductModel().getProduct(), getPartName());
		if (rule == null) {
			String specCode = getModel().getProductModel().getProduct().getProductSpecCode();
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

	// === supporting api === //
	protected LotControlRule find(List<LotControlRule> rules, BaseProduct product, String partName) {
		if (rules == null || product == null) {
			return null;
		}

		LotControlRuleId id = new LotControlRuleId();
		String productSpec = product.getProductSpecCode();

		if (productSpec == null) {
			return null;
		}
		productSpec = productSpec.trim();

		id.setProcessPointId(getModel().getProductModel().getApplicationContext().getProcessPointId());
		id.setPartName(partName);

		id.setModelYearCode(ProductSpec.extractModelYearCode(productSpec));
		id.setModelCode(ProductSpec.extractModelCode(productSpec));
		id.setModelTypeCode(ProductSpec.extractModelTypeCode(productSpec));
		id.setModelOptionCode(ProductSpec.extractModelOptionCode(productSpec));
		id.setExtColorCode(ProductSpec.extractExtColorCode(productSpec));
		id.setIntColorCode(ProductSpec.extractIntColorCode(productSpec));

		LotControlRule rule = find(rules, id);
		if (rule != null) {
			return rule;
		}

		id.setIntColorCode(ProductSpec.WILDCARD);
		rule = find(rules, id);
		if (rule != null) {
			return rule;
		}

		id.setExtColorCode(ProductSpec.WILDCARD);
		rule = find(rules, id);
		if (rule != null) {
			return rule;
		}

		id.setModelOptionCode(ProductSpec.WILDCARD);
		rule = find(rules, id);
		if (rule != null) {
			return rule;
		}

		id.setModelTypeCode(ProductSpec.WILDCARD);
		rule = find(rules, id);
		if (rule != null) {
			return rule;
		}
		// REMARK - we do not wildcard rules deeper, year and model are concrete values
		return rule;
	}

	protected LotControlRule find(List<LotControlRule> rules, LotControlRuleId id) {
		if (rules == null || id == null) {
			return null;
		}
		for (LotControlRule rule : rules) {
			if (rule == null) {
				continue;
			}
			if (rule.getId().equals(id)) {
				return rule;
			}
		}
		return null;
	}

	// === get/set === //
	protected List<LotControlRule> getRules() {
		return rules;
	}

	protected AbstractProcessModel getModel() {
		return model;
	}

	protected String getPartName() {
		return partName;
	}

	protected UniqueInstalledPartValidator getUniqueValidator() {
		return uniqueValidator;
	}
}
