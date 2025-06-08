package com.honda.galc.oif.web.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.oif.values.OifServiceConfigValue;
import com.honda.galc.oif.web.utils.OifWebUtility;

/**
 * @version 1.0
 * @author
 */
public class ConfigurationAction extends AbstractAction
{

	private static final String ATTR_INTERFACES = "oifConfigs";
	private static final String OIF_NOTIFICATIONS = "displayOifNotifications";
	private static final String OIF_NOTIFICATION_PROPERTIES = "OIF_NOTIFICATION_PROPERTIES";

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		final String logMethod = "execute()";
		
		ActionErrors errors = new ActionErrors();
		ActionForward forward = mapping.findForward("result"); // return value

		if(OifWebUtility.isComponentDefined(OIF_NOTIFICATION_PROPERTIES)) {
			request.setAttribute(OIF_NOTIFICATIONS, "true");
		}
		try {
			List<OifServiceConfigValue> services = OifWebUtility.listOifServices();
			request.setAttribute(ATTR_INTERFACES, services);
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
