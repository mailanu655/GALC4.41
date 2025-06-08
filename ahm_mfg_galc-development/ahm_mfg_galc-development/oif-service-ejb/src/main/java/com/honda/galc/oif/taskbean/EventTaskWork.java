package com.honda.galc.oif.taskbean;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.oif.sched.EventScheduleManager;
import com.ibm.websphere.asynchbeans.Work;

/**
 * <h3>EventTaskWork</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Event Task processor - runs in a background thread
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
 * <TD>Apr 10, 2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL009</TD>
 * <TD>Initial Version</TD>
 * </TR>
 * 
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Jul 09, 2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL035</TD>
 * <TD>added getApplicationName() for monitoring by implementing ApplicationNameProvider</TD>
 * </TR>
 * 
 * </TABLE>
 */
public class EventTaskWork implements Work {

	protected static final String EVENT_TASK_EJB = "java:comp/env/ejb/EventTask";
	private String name;
	private Object[] args;
	private String eventName;
	private Logger logger;

	/**
	 * Constructor
	 * 
	 * @param name - task name
	 * @param event - event name (for logging)
	 * @param args - arbitrary arguments
	 * 
	 */
	public EventTaskWork(String name, String event, Object[] args) {
		super();
		this.name = name;
		this.args = args;
		this.eventName = event;
		this.logger = EventScheduleManager.logger;
	}

	/**
	 * Release bean resources if interrupted
	 * 
	 * @see com.ibm.websphere.asynchbeans.Work#release()
	 */
	public void release() {
		// do nothing
	}

	/**
	 * Main processing method to be executed in a separate thread<br>
	 * Invokes EJB facade what allows to use transaction context and other EJB container<br>
	 * services 
	 *
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
       
		try {
			
			InitialContext context = new InitialContext();
			
	    	EventTaskHome eventTaskHome = (EventTaskHome)context.lookup(EVENT_TASK_EJB);
	    	
	    	EventTask eventTask = eventTaskHome.create();
	    	
	    	eventTask.execute(this.eventName, this.name, getArgs());
	    	
		} catch (NamingException e) {
			logger.error("OIF processing error",getFaultMessage(e));
		} catch (CreateException e) {
			logger.error("OIF processing error",getFaultMessage(e));
		}catch (RemoteException e) {
			logger.error("OIF processing error",getFaultMessage(e));
		}
    	
	}

	/**
	 * @param e
	 * @return
	 */
	private String getFaultMessage(Throwable e) {
		return "Task failed. Event Name: " + this.eventName +
				", Name: " + this.name + ", Reason: "+ e.toString();
	}

	/**
	 * Getter the arguments
	 * 
	 * @return the args
	 */
	public Object[] getArgs() {
		return args;
	}

	/**
	 * Getter to the event name
	 * 
	 * @return the event name
	 */
	public String getEventName() {
		return eventName;
	}


	/**
	 * Returns an application name for the monitoring tools
	 * 
	 * @return - application name
	 */
	public String getApplicationName() {
		return getEventName();
	}

}
