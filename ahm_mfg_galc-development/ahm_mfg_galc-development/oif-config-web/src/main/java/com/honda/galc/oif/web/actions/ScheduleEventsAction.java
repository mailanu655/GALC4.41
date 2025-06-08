package com.honda.galc.oif.web.actions;

import java.rmi.RemoteException;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.oif.sched.EventManager;
import com.honda.galc.oif.sched.EventManagerHome;

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
public class ScheduleEventsAction extends AbstractAction

{

	private static final String COMMAND_STOP = "stop";
	private static final String COMMAND_PARAM = "cmd";
	private static final String SOURCE = "scheduling events";
	private final static String MANAGER_JNDI_REF = "java:comp/env/ejb/EventScheduleManager";

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		final String logMethod = "execute()";
		
		ActionErrors errors = new ActionErrors();
		ActionForward forward = new ActionForward(); // return value
		String param = request.getParameter(COMMAND_PARAM);
		

		try {

			// do something here
			EventManagerHome anOifScheduleRefresherHome 
				= (EventManagerHome) new InitialContext().lookup(MANAGER_JNDI_REF);
			
			EventManager manager = null;
			
			try {
				if (anOifScheduleRefresherHome != null)
					manager = anOifScheduleRefresherHome.create();
			} catch (javax.ejb.CreateException ce) {
				logException(SOURCE, logMethod, ce);
			    errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionError("ERROR", ce));
			} catch (RemoteException re) {
				logException(SOURCE, logMethod, re);
			    errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionError("ERROR", re));
			}

			try {
				if(param != null && param.equalsIgnoreCase(COMMAND_STOP)) {
					manager.stop();
				} else {
					manager.start();
				}
			} catch (RemoteException ex) {
				logException(SOURCE, logMethod, ex);
			    errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionError("ERROR", ex));
			}
		} catch (Exception e) {
	
		    // Report the error using the appropriate name and ID.
			logException(SOURCE, logMethod, e);
		    errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionError("ERROR", e));
	
		}

	// If a message is required, save the specified key(s)
	// into the request for use by the <struts:errors> tag.

	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	}
	// Write logic determining how the user should be forwarded.
	forward = mapping.findForward("result");

	// Finish with
	return (forward);

    }

	/**
	 * @param msgId
	 * @param logMethod
	 * @param ce
	 */
	private void logException(String source, final String logMethod, Exception ce) {
		logError(source,logMethod, ce.getMessage() + ": " + stackTraceToString(ce));
	}
}
