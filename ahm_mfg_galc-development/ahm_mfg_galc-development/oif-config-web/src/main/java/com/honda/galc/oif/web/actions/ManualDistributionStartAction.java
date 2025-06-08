package com.honda.galc.oif.web.actions;

import java.security.Principal;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.honda.galc.oif.taskbean.EventTaskWork;
import com.honda.galc.oif.web.forms.OifStartFormBean;
import com.honda.galc.util.KeyValue;
import com.honda.galc.util.OIFConstants;
import com.ibm.websphere.asynchbeans.WorkManager;

/**
 * @version 	1.0
 * @author
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class ManualDistributionStartAction extends AbstractAction

{
	protected static final String WORK_MANAGER = "java:comp/env/wm/SchedulerWorkManager";
	protected static final String OIF_DISTRIBUTION = "OIF_DISTRIBUTION";
	private static final String START_TIMESTAMP = "START_TIMESTAMP";

    public ActionForward execute(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response)
	    	throws Exception {

    	ActionMessages messages = new ActionMessages();
		ActionErrors errors = new ActionErrors();
		ActionForward forward = mapping.findForward("result"); // return value
		OifStartFormBean startForm = (OifStartFormBean) form;
		WorkManager wm = null;
		try {
	    	wm = getWorkManager();
		} catch (NamingException e) {
			e.printStackTrace();
			errors.add(ActionErrors.GLOBAL_MESSAGE, 
					new ActionError("ERROR_IN_RUN_OIF", e));
		}
    	String user = "Unknown User";
    	Principal userPrincipal = request.getUserPrincipal();
		if (userPrincipal != null) {
			user = userPrincipal.getName();
		} 
		
		Object[] params = new Object[1];
		if (startForm.isStartTimestampSet()) {
			params = new Object[2];
			String startTimestamp = startForm.getStartTimeStamp();
			params[1] = new KeyValue<String, String>(START_TIMESTAMP, startTimestamp);
		}		
		String interfaceId = startForm.getInterfaceName();
		params[0] = new KeyValue<String, String>(OIFConstants.INTERFACE_ID, interfaceId);
		EventTaskWork work = new EventTaskWork(OIF_DISTRIBUTION, user, params);
		try {
		    wm.startWork(work);
		} catch (Exception e) {
			e.printStackTrace();
			errors.add(ActionErrors.GLOBAL_MESSAGE, 
					new ActionError("ERROR_IN_RUN_OIF", e));
		}
	
		if (errors.isEmpty()) {
		    messages.add(ActionMessages.GLOBAL_MESSAGE, 
		    		new ActionMessage("OIF_START_SUCCESS", startForm.getInterfaceName()));		
		} else {
		    messages.add(ActionMessages.GLOBAL_MESSAGE, 
		    		new ActionMessage("ERROR_IN_RUN_OIF", startForm.getInterfaceName()));		
			saveErrors(request, errors);
		}  
	    saveMessages(request, messages);
		forward = mapping.findForward("result");
		// Finish with
		return (forward);
    }

	private WorkManager getWorkManager() throws NamingException {
		InitialContext context = new InitialContext();
		WorkManager wm = (WorkManager)context.lookup(WORK_MANAGER);
		return wm;
	}
}
