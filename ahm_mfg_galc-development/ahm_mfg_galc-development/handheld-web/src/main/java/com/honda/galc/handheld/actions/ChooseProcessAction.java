package com.honda.galc.handheld.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.*;

import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.handheld.data.HandheldConstants;
import com.honda.galc.handheld.forms.ChooseProcessForm;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;

public class ChooseProcessAction extends ValidatedUserHandheldAction{

	private String mbpnOnRequseted;
	
	public ActionForward localExecute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception { 
		
		ChooseProcessForm currentForm = (ChooseProcessForm) form;
		
		if (currentForm.wasMbpnOnRequseted()) {
			currentForm.setMbpnOnRequseted("");
			return mapping.findForward("MBPNOnRequested");
		}
		
		request.getSession().setAttribute(HandheldConstants.CHOOSE_PROCESS_FORM, form);
		boolean didDivisionOrZoneSelectionsChange = currentForm.didDivisionSelectionChange() || currentForm.didZoneSelectionChange();
		currentForm.setLastSelectedDivision(currentForm.getSelectedDivision());
		currentForm.setLastSelectedZone(currentForm.getSelectedZone());
		if (!StringUtil.isNullOrEmpty(currentForm.getSelectedProcessPointId()))
			request.getSession().setAttribute(HandheldConstants.PRODUCT_TYPE, getProductTypeForProcessPointId(currentForm.getSelectedProcessPointId().trim()));
		return mapping.findForward(didDivisionOrZoneSelectionsChange
					? "zoneSelectionChanged"
					: "success");
    }

	public String getMbpnOnRequseted() {
		return mbpnOnRequseted;
	}

	public void setMbpnOnRequseted(String mbpnOnRequseted) {
		this.mbpnOnRequseted = mbpnOnRequseted;
	}

	@Override
	protected String formName() {
		return HandheldConstants.CHOOSE_PROCESS_FORM;
	}
	
	private ProductType getProductTypeForProcessPointId(String processPointId) {
		if (StringUtil.isNullOrEmpty(processPointId))
			return null;
		return ProductTypeCatalog.getProductType(PropertyService.getPropertyBean(SystemPropertyBean.class, StringUtils.trim(processPointId)).getProductType());
	}
}
