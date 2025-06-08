package com.honda.galc.handheld.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.handheld.data.HandheldUtil;
import com.honda.galc.util.ParseInfo;

public class BuildResultBean implements Serializable {

	private static final long serialVersionUID = -2122959229040383706L;
	private String result;
	private String required = "on";
	private LotControlRule rule;
	private String validationMessage;
	private BaseProduct product;
	private boolean updateTorquesRequested = false;
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public LotControlRule getRule() {
		return rule;
	}
	public void setRule(LotControlRule rule) {
		this.rule = rule;
	}
	public boolean isCertScan() {
		return rule.getStrategy() != null && rule.getStrategy().equals("CERT_PLATE_SCAN");
	}
	
	public String getScanType() {
		return isCertScan()
				? "Cert"
				: "Part";
	}
	public String getPartMasks() {
		return rule == null
				? ""
				: isCertScan()
					? rule.getPartMasks().length() == 0
							? ""
							: rule.getPartMasks().substring(0, 1) + getProduct().getProductId()
					: CommonPartUtility.replacePartMaskConstants(CommonPartUtility.parsePartMask(rule.getPartMasks()), getProduct());
	}
	
	public String getMinLength() {
		int maxLength = -1;
		for (PartSpec eachPartSpec : this.getRule().getParts()) {
			for (ParseInfo eachParseInfo : getParseInfosForPartSpec(eachPartSpec)) {
				if (maxLength < (eachParseInfo.getOffset() + eachParseInfo.getLength()))
					maxLength = (eachParseInfo.getOffset() + eachParseInfo.getLength()); 
			}	
		}
		return Integer.toString(maxLength);
	}

	private List<ParseInfo> getParseInfosForPartSpec(PartSpec partSpec) {
		List<ParseInfo> parseInfos = new ArrayList<ParseInfo>();
		try{
			parseInfos = CommonPartUtility.getParseInfos(partSpec);
		} catch (NullPointerException e) {
			// no-op this is acceptable
		}
		return parseInfos;
	}
	
	public String getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}
	
	public boolean isValid() {
		return result != null && result.equals(validationMessage);
	}
	public BaseProduct getProduct() {
		return product;
	}
	public void setProduct(BaseProduct product) {
		this.product = product;
	}
	
	public boolean hasResult() {
		return result != null && !(result.trim().length() == 0);
	}
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
	}

	public boolean isTorqueDataCollector() {
		return HandheldUtil.doesRuleCollectTorques(rule);
	}
	
	public boolean isSerialNumberRule() {
		return HandheldUtil.doesRuleCollectASerialNumber(rule);
	}
	
	public String getPartName() {
		return rule == null
				? ""
				: rule.getPartName().getPartName();
	}
	
	public boolean isUpdateTorquesRequested() {
		return updateTorquesRequested;
	}
	public void setUpdateTorquesRequested(boolean updateTorquesRequested) {
		this.updateTorquesRequested = updateTorquesRequested;
	}
	
	public String toString() {
		return "rule " + rule + " result =  " + result;
	}
}