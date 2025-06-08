package com.honda.galc.handheld.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.entity.enumtype.PartSerialNumberScanType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.handheld.data.BuildResultBean;
import com.honda.galc.handheld.data.BuildResultContainer;
import com.honda.galc.handheld.data.HandheldConstants;
import com.honda.galc.handheld.forms.ChooseProcessForm;
import com.honda.galc.handheld.forms.ScanProductIdForm;

public class ScanProductIdAction extends LotControlRuleProcessingAction{

	public ActionForward localExecute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		setScanProductIdForm((ScanProductIdForm) form);
		getScanProductIdForm().setRules(null);
		
		if (getScanProductIdForm().isStationChangeRequested()) {
			getScanProductIdForm().setChangeStationRequest("");
			return mapping.findForward("stationChangeRequested");
		}

		ChooseProcessForm chooseProcessForm = (ChooseProcessForm)request.getSession().getAttribute("chooseProcessForm");
		setProcessPoint(chooseProcessForm.getSelectedProcess());
		
		request.getSession().setAttribute(HandheldConstants.SCAN_PRODUCT_ID_FORM, form);

		BaseProduct product = getProductForProductId(getScanProductIdForm().getProductId());
		if(product == null) {
			setProductIdMessage(HandheldConstants.INVALID_PRODUCT_ID_MESSAGE);
			return mapping.findForward(HandheldConstants.PRODUCT_ID_NOT_VALID);
		}
		if(!isProductInInstalledPartUpdateDivision(product)) {
			setProductIdMessage("Not In Assembly");
			return mapping.findForward(HandheldConstants.PRODUCT_ID_NOT_VALID);
		}
		if(getCurrentProductSpecCode(product) == null) {
			setProductIdMessage("Invalid Product Spec Code");
			return mapping.findForward(HandheldConstants.PRODUCT_ID_NOT_VALID);
		}

		setProduct(product);
		request.getSession().setAttribute(HandheldConstants.PRODUCT, product);
		List<LotControlRule> rules = getRulesForProductAndStation(chooseProcessForm.getSelectedProcess());
		
		if (rules.isEmpty()) {
			setProductIdMessage("No Rules Found");
			return mapping.findForward(HandheldConstants.NO_RULES_FOR_PRODUCT_ID);
		} 
		if (!getScanProductIdForm().validateRules(rules)) {
			setProductIdMessage("Invalid Rule Format");
			return mapping.findForward(HandheldConstants.NO_RULES_FOR_PRODUCT_ID);
		}
		
		getScanProductIdForm().setRules(rules);
		
		List<BuildResultBean> results = new ArrayList<BuildResultBean>();
		for (LotControlRule eachRule : rules) {
			BuildResultBean newBean = new BuildResultBean();
			newBean.setRule(eachRule);
			newBean.setProduct(product);
			results.add(newBean);
		}
		
		BuildResultContainer containerBean = new BuildResultContainer();
		containerBean.setBuildResults(results);
		request.getSession().setAttribute("buildResultContainerBean", containerBean);
//		request.getSession().getServletContext().setAttribute("serialNumberBuildResults", results);
		request.getSession().setAttribute("results", results);
		return mapping.findForward("scanParts");
    }
	
	public boolean isSerialNumberRule(LotControlRule eachRule) {
		return !(eachRule.getPartMasks().trim().length() == 0)
				&& (eachRule.getSerialNumberScanType() == PartSerialNumberScanType.PART 
				|| eachRule.getSerialNumberScanType() == PartSerialNumberScanType.PART_MASK);
	}

	private void setProductIdMessage(String productIdMessage) {
		logInfo(String.format("Scanned product: %1s - %2s on process %3s - %4s", productIdMessage, getScanProductIdForm().getProductId(), getProcessPoint().getProcessPointId(), getProcessPoint().getProcessPointName()));
		getScanProductIdForm().setProductId(productIdMessage);
	}
	
	protected void compilePersisentResultsForRule(LotControlRule aRule, List<InstalledPart> partsToBeRemoved,
			List<InstalledPart> persistentInstalledParts) {
		InstalledPart persistentInstalledPart = getInstalledPartFor(aRule);
		addMeasurementsToPartForRule(persistentInstalledPart, aRule, true);
		persistentInstalledParts.add(persistentInstalledPart);
	}

	@Override
	protected String formName() {
		return HandheldConstants.SCAN_PRODUCT_ID_FORM;
	}
}