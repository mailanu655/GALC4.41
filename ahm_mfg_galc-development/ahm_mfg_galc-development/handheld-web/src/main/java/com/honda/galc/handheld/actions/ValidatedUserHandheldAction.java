package com.honda.galc.handheld.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.honda.galc.handheld.data.HandheldConstants;
import com.honda.galc.handheld.forms.ValidatedUserHandheldForm;
import com.honda.galc.handheld.plugin.InitializationPlugIn;

public abstract class ValidatedUserHandheldAction extends HandheldAction {
	private String userId;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		ValidatedUserHandheldForm currentForm = (ValidatedUserHandheldForm)form;

		HttpSession currentSession = request.getSession(false);
		
		if (currentSession == null || currentForm.getSessionTimedOut().equals(HandheldConstants.SESSION_TIMED_OUT)) {
			if (currentSession != null)
				currentSession.invalidate();

			logInfo("Session Timed out");
			currentForm.setSessionTimedOut(HandheldConstants.SESSION_ACTIVE);
			return mapping.findForward("SessionTimedOut");
		} 	else {
			setUserId((String)currentSession.getAttribute(HandheldConstants.USER_ID));
			resetPersistenceErrorMessage();
			return doLocalExecute(mapping, form, request, response);
		}
	}

	private ActionForward doLocalExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		ActionForward forward;
		try {
			forward = localExecute(mapping, form, request, response);
		} catch (Exception e) {
			logError(e.getMessage(),e);
			logInfo("Unexpected processing error.\nNo changes made.\nPlease try again");
			appendPersisteceErrorMessage("Unexpected processing error.\nNo changes made.\nPlease try again");
			updateSessionErrorMessages(request);
			return mapping.findForward(HandheldConstants.FAILURE);
		} 
		
		return forward;
	}

	abstract ActionForward localExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	protected void logInfo(String message) {
		InitializationPlugIn.info(getUserId(), message);
	}
	
	protected void logError(String message, Throwable ex) {
		InitializationPlugIn.error(getUserId(), message,ex);
	}
}