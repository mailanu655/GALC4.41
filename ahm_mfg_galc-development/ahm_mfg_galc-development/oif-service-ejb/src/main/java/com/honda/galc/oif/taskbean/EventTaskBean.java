package com.honda.galc.oif.taskbean;

import com.honda.galc.common.exception.OifServiceException;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;
import com.honda.galc.system.oif.svc.common.OifServiceFactory;

/**
 * <h3>EventTaskBean</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Bean implementation class for Enterprise Bean: EventTask<br>
 * EJB facade for event task execution
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update Date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Apr 9, 2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL009</TD>
 * <TD>Initial Version</TD>
 * </TR>
 * </TABLE>
 */
public class EventTaskBean implements javax.ejb.SessionBean {

	private static final String TASK_NAMES_SERIAL_EXEC_SEP = "\\s*;\\s*";

	static final long serialVersionUID = 3206093459760846163L;
	
	private javax.ejb.SessionContext mySessionCtx;
	
	/**
	 * getSessionContext
	 */
	public javax.ejb.SessionContext getSessionContext() {
		return mySessionCtx;
	}
	
	/**
	 * setSessionContext
	 */
	public void setSessionContext(javax.ejb.SessionContext ctx) {
		mySessionCtx = ctx;
	}
	
	/**
	 * ejbCreate
	 */
	public void ejbCreate() throws javax.ejb.CreateException {

	}
	
	/**
	 * ejbActivate
	 */
	public void ejbActivate() {
	}
	
	/**
	 * ejbPassivate
	 */
	public void ejbPassivate() {
	}
	
	/**
	 * ejbRemove
	 */
	public void ejbRemove() {
	}
	
	/**
	 * Executes event tasks in their sequence getting them from factory
	 * @param userName TODO
	 * @param eventTaskString - list of tasks (separated by <b>;</b>)
	 * @param args - task arguments
	 * 
	 * @throws OifServiceException
	 */
	public void execute(String userName, String eventTaskString, Object[] args) {
		
		String[] taskNames = eventTaskString.split(TASK_NAMES_SERIAL_EXEC_SEP);
		
		for (String taskName : taskNames) {
			executeTask(taskName, userName, args);
		}		

	}
	
	private void executeTask(String taskName, String userName,Object[] args) {
		try {
			OifServiceFactory.executeTask(taskName,userName,args);
		}catch(Exception ex) {
			String message = "Task failed. Event Name: " + userName +
					", Name: " + taskName + ", Reason: "+ ex.toString();
			OifServiceFactory.logger.error("OIF processing error ", "Creation", message);
		
			OifErrorsCollector errorsCollector = new OifErrorsCollector("OIF_EVENTS");
			errorsCollector.error(message);
			errorsCollector.sendEmail();
		}
	}
	
	
}
