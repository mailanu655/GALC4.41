package com.honda.galc.handheld.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;

import com.honda.galc.handheld.data.HandheldConstants;

public class ErrorPageAction extends ValidatedUserHandheldAction{
	
	public ActionForward localExecute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		request.getSession().setAttribute(HandheldConstants.PERSISTENCE_ERROR_MESSAGE, null);
		if (getErrorSource(request).equals(HandheldConstants.MBPN_ON_FORM))
			return mapping.findForward(HandheldConstants.MBPN_ON_FORM);
		return mapping.findForward(HandheldConstants.SUCCESS);
    }

	@Override
	protected String formName() {
		return HandheldConstants.ERROR_PAGE_FORM;
	}
}
