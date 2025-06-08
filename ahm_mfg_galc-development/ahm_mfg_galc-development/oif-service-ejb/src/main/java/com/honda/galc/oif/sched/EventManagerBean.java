package com.honda.galc.oif.sched;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.honda.galc.common.logging.Logger;
import com.ibm.websphere.scheduler.Scheduler;

/**
 * <h3>EventManagerBean</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Bean implementation class for Enterprise Bean: OifStartingService<br>
 * EJB facade to OIF event maanger - starts and stops OIF events
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
public class EventManagerBean implements javax.ejb.SessionBean {

	private static final String OIF_TASK_HANDLER = "java:comp/env/ejb/OifEventHandler";
	private static final String GALC_SCHEDULER = "java:comp/env/sched/GalcScheduler";
	private static final long serialVersionUID = 3206093459760846163L;
	private javax.ejb.SessionContext mySessionCtx;
	private Scheduler scheduler = null;
	private Logger logger = null;
	private EventScheduleManager eventManager = null;
	
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
		this.logger = EventScheduleManager.logger;

		try {
			scheduler = (Scheduler)new InitialContext().lookup(GALC_SCHEDULER);
		} catch (NamingException e) {
			this.logStatus("EventManagerBean", "start()", e.getMessage());
		}
		
		this.eventManager = EventScheduleManager.getInstance();
	}
	
	/**
	 * ejbActivate
	 */
	public void ejbActivate() {
		// Logger
		this.logger = Logger.getLogger();
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
	 *
	 * @see com.honda.galc.oif.sched.EventManager#start()
	 */
	public void start() {

		// Clean Up existing events - there might be some leftovers
		eventManager.stopOifEvents(this.scheduler,OIF_TASK_HANDLER);
		
		// Start all scheduled OIF events
		eventManager.startOifEvents(this.scheduler, OIF_TASK_HANDLER);		
	}
	
	/**
	 * Logs message
	 * 
	 * @param msgId - message ID
	 * @param method - method name
	 */
	private void logStatus(String source, String method, String userMessage) {
		
		logger.info("Method - " + method + " " + userMessage);

	}
	
	/**
	 *
	 * @see com.honda.galc.oif.sched.EventManager#stop()
	 */
	public void stop() {
		
		eventManager.stopOifEvents(this.scheduler,OIF_TASK_HANDLER); 
		
	}
	
	/**
	 * Refreshes event schedule by stopping and starting OIF events
	 */
	public void refreshSchedule() {
		
		eventManager.refreshProperties();
		
		// Clean Up existing events - there might be some leftovers
		eventManager.stopOifEvents(this.scheduler,OIF_TASK_HANDLER);
		
		// Start all scheduled OIF events
		eventManager.startOifEvents(this.scheduler, OIF_TASK_HANDLER);
				
	}
	


}
