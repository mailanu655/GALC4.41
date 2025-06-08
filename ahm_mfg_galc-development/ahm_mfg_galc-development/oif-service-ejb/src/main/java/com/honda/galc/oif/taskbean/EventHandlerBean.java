package com.honda.galc.oif.taskbean;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.oif.sched.EventScheduleManager;
import com.honda.galc.oif.sched.EventSchedulePropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;
import com.honda.galc.system.oif.svc.common.OifServiceFactory;
import com.honda.galc.util.KeyValue;
import com.honda.galc.util.OIFConstants;
import com.ibm.websphere.asynchbeans.WorkManager;
import com.ibm.websphere.scheduler.TaskStatus;

/**
 * <h3>EventHandlerBean</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Bean implementation class for Enterprise Bean: OifTaskHandler<br>
 * Event handler for OIF events: it processes all tasks configured in OIF event
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
public class EventHandlerBean implements javax.ejb.SessionBean {

	static final long serialVersionUID = 3206093459760846163L;
	protected static final String OIF_WORK_MANAGER = "java:comp/env/wm/EventWorkManager";
	private static final String SEPARATOR = "\\s*,\\s*";
	private static final String TASK_NAMES_SERIAL_EXEC_SEP = "\\s*;\\s*";
	private javax.ejb.SessionContext mySessionCtx;
	private Map<String, String> eventTasks;
	private Logger logger = null;
	private String userName = null;
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
		// Logger
		logger = EventScheduleManager.logger;
		
		initBean();
	}
	
	/**
	 * ejbActivate
	 */
	public void ejbActivate() {
		initBean();
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
	 * Processes all tasks associated with particular events
	 * 
	 * @param taskStatus task parameter passed in by server Scheduler
	 * 
	 * @see com.ibm.websphere.scheduler.TaskHandler#process(com.ibm.websphere.scheduler.TaskStatus)
	 */
	public void process(TaskStatus taskStatus) {
		final String logMethod = "process(taskStatus)";
		long startTime = System.currentTimeMillis();		
		
		// refresh the configuration
		initBean();
		
		String event = taskStatus.getName();
		
		// Initialize logging with user name == event
		setUserName(event);
		
		this.logStatus("Started to process OIF event", logMethod, "event: " + event);
		
		String[] eventTaskList = null;
		try{
			eventTaskList = eventTasks.get(event).split(SEPARATOR);
			this.logStatus("OIF event processing ", logMethod, Arrays.toString(eventTaskList));
		}catch (Exception ex) {
			logger.emergency("Could not run event " + event + " due to EVENT_TASKS not configured" );
			return;
		}
		
		for (String task : eventTaskList) {
			// Processing interface
			this.logStatus("OIF processing event ", logMethod, "event: " + event + " started task: " + task);
						
			try {
				execute(event, task, null);
			} catch (Exception e) {
				String stackTrace = getStackTrace(e);
				this.logStatus("OIF processing event ", logMethod, "Error running event task: " + task + ", event: " + event+ ". Exception: " + e + ", stackTrace:"+ stackTrace);
			}
		}
		
		this.logStatus("Finished to process OIF event ", logMethod);
		long endTime = System.currentTimeMillis();
		EventStatistic.registerStatistic(taskStatus.getName(), startTime, endTime);
	}
	
	public void execute(String userName, String eventTaskString, Object[] args)  {
		String[] taskNames = eventTaskString.split(TASK_NAMES_SERIAL_EXEC_SEP);
		for (String taskName : taskNames) {
			String[] distributionInfo = taskName.split(OIFConstants.DISTRIBUTION_TASK_NAMES_SEP);
			if(distributionInfo.length > 0 && OIFConstants.OIF_DISTRIBUTION.equalsIgnoreCase(distributionInfo[0])) {
				int argSize = args == null ? 0 : args.length;
				Object[] newArgs = new Object[argSize+1];
				newArgs[0] = new KeyValue<String, String>(OIFConstants.INTERFACE_ID, distributionInfo[1]);
				for(int i=0; i<argSize; i++) {
					newArgs[i+1] = args[i]; 
				}
				
				executeTask(distributionInfo[0],userName,newArgs);
				
			} else {
				executeTask(taskName,userName,args);
			}
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
	
	
	/**
	 * Helper factory method to obtain a work manager 
	 * 
	 * @return
	 * @throws NamingException
	 */
	protected WorkManager getWorkManager() throws NamingException {
		InitialContext context = new InitialContext();
		WorkManager wm = (WorkManager)context.lookup(OIF_WORK_MANAGER);
		return wm;
	}
	
	/**
	 * Initialize internal resources
	 * 
	 */
	private void initBean() {
		
		EventSchedulePropertyBean oifSched;
		try {
			PropertyService.refreshComponentProperties(EventSchedulePropertyBean.COMPONENT);
			oifSched = PropertyService.getPropertyBean(EventSchedulePropertyBean.class);
			eventTasks = oifSched.getEventTasks();
		} catch (Exception e) {
			logger.error(e,"Error running OIF service at EventhandlerBean");
		}
	}
	
	/**
	 * Log message
	 * 
	 * @param msgId - message ID
	 * @param logMethod - method to log
	 */
	private void logStatus(String message, String logMethod) {
		logger.info(message, ", method - " + logMethod);
	}
	
	/**
	 * Logs message
	 * 
	 * @param msgId - message ID
	 * @param method - method name
	 */
	private void logStatus(String msgId, String method, String userMessage) {
		logger.info("Method - " + method + " " + msgId + "-" + userMessage);
	}

	private void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String str = sw.toString();
       	pw.close();
        return str;
	}
}
