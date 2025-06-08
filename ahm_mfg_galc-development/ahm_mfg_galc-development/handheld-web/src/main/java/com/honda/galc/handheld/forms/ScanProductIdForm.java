package com.honda.galc.handheld.forms;

import java.util.ArrayList;
import java.util.List;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.service.ServiceFactory;

public class ScanProductIdForm extends ValidatedUserHandheldForm {
	private static final long serialVersionUID = 1L;
	private String productId, changeStationRequest, lastProcessedProductId;
	private List<LotControlRule> rules;
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public boolean isStationChangeRequested() {
		return changeStationRequest.equals("Change Station");
	}
	public String getChangeStationRequest() {
		return changeStationRequest;
	}
	public void setChangeStationRequest(String changeStationRequest) {
		this.changeStationRequest = changeStationRequest;
	}
	public List<LotControlRule> getRules() {
		return rules;
	}
	public void setRules(List<LotControlRule> rules) {
		this.rules = rules;
	}
	
	public List<String> getPartNames() {
		List<String> partNames = new ArrayList<String>();
		if (getRules() == null)
				return partNames;
		for (LotControlRule eachRule : getRules()) {
			partNames.add(eachRule.getPartNameString());
		}
		return partNames;
	}

	public List<LotControlRule> getTorqueRules() {
		List<LotControlRule> torqueRules = new ArrayList<LotControlRule>();
		if (rules == null)
			return torqueRules;
		
		for (LotControlRule eachRule : rules) {
			if (eachRule.getParts().get(0).getMeasurementCount()> 0) {
				torqueRules.add(eachRule);
			}
		}
		return torqueRules;
	}
	
	public boolean validateRules(List<LotControlRule> lotControlRules) {
		for (LotControlRule eachRule : lotControlRules) {
			if (!validateRule(eachRule))
				return false;
		}
		return true;
	}
	
	//aRule is invalid if there are more than one PartSpec with MeasurementSpecs
	private boolean validateRule(LotControlRule aRule) {
		boolean alreadyFoundMeasurementSpecs = false;
		for (PartSpec eachPartSpec : aRule.getParts()) {
			if (eachPartSpec.getMeasurementCount() > 0) {
				if(alreadyFoundMeasurementSpecs)
					return false;
				alreadyFoundMeasurementSpecs = true;
			}
		}
		return true;
	}
	public String getLastProcessedProductId() {
		return lastProcessedProductId;
	}
	public void setLastProcessedProductId(String lastProcessedProductId) {
		this.lastProcessedProductId = lastProcessedProductId;
	}
	
}
