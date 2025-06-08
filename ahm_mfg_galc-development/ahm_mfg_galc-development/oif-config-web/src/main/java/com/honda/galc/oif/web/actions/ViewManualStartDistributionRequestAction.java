package com.honda.galc.oif.web.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.oif.values.OifStartMenuItem;
import com.honda.galc.oif.web.utils.OifWebUtility;

/**
 * @version 1.0
 * @author
 */
public class ViewManualStartDistributionRequestAction extends AbstractAction
{
	
	private static final String OIF_MENU_OPTIONS = "oifMenuOptions";

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		final String logMethod = "execute()";
		
		ActionErrors errors = new ActionErrors();
		ActionForward forward = mapping.findForward("result"); // return value

		try {
			List<OifStartMenuItem> options = OifWebUtility.listDistributionMenuOptions(); 
			request.setAttribute(OIF_MENU_OPTIONS, options);
		} catch (Exception e) {
			// Report the error using the appropriate name and ID.
			errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionError("ERROR", e));
			logException(logMethod, e);
		}

		// If a message is required, save the specified key(s)
		// into the request for use by the <struts:errors> tag.
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		// Finish with
		return (forward);

	}
}
