package com.honda.galc.oif.sched;

import java.util.Iterator;
import java.util.Map;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.service.property.PropertyService;
import com.ibm.websphere.scheduler.BeanTaskInfo;
import com.ibm.websphere.scheduler.NotificationException;
import com.ibm.websphere.scheduler.Scheduler;
import com.ibm.websphere.scheduler.SchedulerNotAvailableException;
import com.ibm.websphere.scheduler.TaskInfo;
import com.ibm.websphere.scheduler.TaskInvalid;
import com.ibm.websphere.scheduler.TaskPending;
import com.ibm.websphere.scheduler.TaskStatus;
import com.ibm.websphere.scheduler.UserCalendarInvalid;


/**
 * <h3>OifEventManager</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Manages events - starts and stops them
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
public class EventScheduleManager {

	private static final String SIMPLE_CALENDAR = "SIMPLE";
	private static final String MSG_SCHED_GEN_INFO_ID = "Start OIF events";
	protected static final String MSG_SCHED_GEN_ERR_ID = "Failed to start OIF events";
	public static Logger logger = Logger.getLogger("OIF_EVENTS");
	private static final String CRON_CALENDAR = "CRON";
	private static final String OIF_EVENT_PATTERN = "OIF%";
	private enum CalType {NUMBER, SIMPLE, CRON, UNKNOWN};

	
	
	/**
	 * Returns OifEventManager instance
	 * 
	 * @return OifEventManager instance
	 */
	public static EventScheduleManager getInstance() {
		return new EventScheduleManager();
	}
	
	/**
	 * Default constructor
	 */
	private EventScheduleManager() {
		super();
	}


	/**
	 * Logs message
	 * 
	 * @param msgId - message ID
	 * @param method - method name
	 */
	private void logStatus(String msgId, String method, String userMessage) {
		
		logger.info(msgId,"Method - " + method + " " + userMessage);

	}
	
	/**
	 * Starts OIF event by scheduling it
	 * 
	 * @param scheduler OIF Scheduler
	 * @param oifTaskHandler OIF task hanlder JNDI name
	 * 
	 */
	public void startOifEvents(Scheduler scheduler, String oifTaskHandler) {
		final String method = "startOifEvents(scheduler, oifTaskHandler)";
		
		logStatus("Started to schedule OIF tasks", method);	
	
		try {
			
			EventSchedulePropertyBean oifSched = PropertyService.getRefreshedPropertyBean(EventSchedulePropertyBean.class);
			
			Map<String, String> eventSchedules = oifSched.getEventSchedules();
	
			String[] eventList = oifSched.isUseNewEventList()? oifSched.getNewEventList() : oifSched.getEventList();
			// Go through all tasks and create schedule events
		    for (String event : eventList) {

                if (event.length() > 0) {
                    logStatus(MSG_SCHED_GEN_INFO_ID, method, "Scheduling event: " + event);
                    BeanTaskInfo taskInfo = (BeanTaskInfo) scheduler.createTaskInfo(BeanTaskInfo.class);
                    
                    String eventSchedule = eventSchedules.get(event);
                    logStatus(MSG_SCHED_GEN_INFO_ID, method, "Scheduling event: " + event
                                    + " with schedule: " + eventSchedule);
                    
                    taskInfo.setName(event);

                    setEventSchedule(taskInfo, eventSchedule);
                    
                    taskInfo.setTaskHandler(oifTaskHandler);
                    
                    
//@kw we need this option to be able to read task status, here is the ibm reference (Identifying tasks that are currently running)
//http://publib.boulder.ibm.com/infocenter/wasinfo/v7r0/index.jsp?topic=/com.ibm.websphere.express.iseries.doc/info/iseriesexp/scheduler/tasks/tsch_schedulebtask.html                    
                    taskInfo.setTaskExecutionOptions(TaskInfo.EXECUTION_DELAYEDUPDATE);
                    
                    TaskStatus status = scheduler.create(taskInfo);
                    
            		logStatus("Scheduled OIF event successfully", method, "Scheduled event " + event + ", ID: " + status.getTaskId());
                }                
				
			}
		    
			logStatus("Finished to schedule OIF tasks", method);
			
		} catch (Exception e) {
			logger.error(e, MSG_SCHED_GEN_ERR_ID );
		}
	
		
	}

	private void logStatus(String string, String method) {
		logger.info(string + ". method -" + method);
		
	}


	/**
	 * Set event schedule based on event schedule format
	 * 
	 * @param taskInfo - task info
	 * @param eventSchedule - event schedule: <ul>
	 * <li><b>CRON</b> format (see below)></li>
	 * <li><b>SIMPLE</b> format  (see below)</li>
	 * <li>bare integer number specifying repeat interval in seconds</li>
	 * </ul>
	 * @throws UserCalendarInvalid
	 * <hr>
	 * <b>CRON Calendar</b><br>
	 *  The CRON calendar is named 'CRON'. When the calendar specifier string is
 	 * CRON then the interval is calculated using a cron based scheme. A cron based
 	 * interval looks like a list of term expressions seperated by spaces or tabs.
 	 * Each term represents an element of the time. The element describes the valid
 	 * values for that term. The next time is calculated by moving the lowest term
 	 * to the next valid value for that term. If the value wraps then we move up to
 	 * the next term and do the same.<p>
 	 * <i>Note that whilst seconds can be specified, the scheduler is not designed to be
 	 * accurate to the second so we only recommend intervals at least a minute apart. We therefore
 	 * recommend the seconds term is always 0</i><p>
 	 * second minute hourOfDay DayOfMonth Month DayOfWeek<p>
 	 * All terms except for day of week are very similar in syntax. Here are examples
 	 * of the syntax:
 	 * <ul>
 	 * <li>*<br>This indicates all values are acceptable. The interval simply includes
 	 * all valid numbers.</li>
 	 * <li>1,4,7<br>This indicates only the specified values are acceptable.</li>
 	 * <li>1-4,10-20,25<br>Ranges can be specified for the comma delimited terms.</li>
 	 * <li>4/5<br>This indicates all values starting at 4 incrementing by 5 at a time up
 	 * to the maximum permissable value. For example, for hours, the resulting sequence is [4,9,14,19,4,9,...]</li>
 	 * <li>1-10/3<br>This indicates all numbers inside the range skipping 3 at a time, 
 	 * in this example, this is [1,4,7,10].</li>
 	 * </ul>
 	 * The numbers can be replaced with symbols. The month term can be one of
 	 * JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV or DEC. Example:<p>
 	 * FEB,JAN-DEC/3 is the same as [JAN,FEB,APR,JUL,OCT]<p>
 	 * The day of week and day of month terms cannot be specified at the same time.
 	 * One must be a '?' and the other a term.<p>
 	 * Examples of intervals.
 	 * <ul>
 	 * <li>0 0 18 ? SEP MON-FRI<br>
 	 * This means at 1800 hours from Mon to Fri during Sept only. Note the day of month is a '?' as we specified
 	 * a day of week.</li>
 	 * <li>0 0 18 L * ?<br>
 	 * This means 6pm on the last day of every month (Jan 31, feb 28, Mar 31, etc) taking in to account leap years also.</li>
 	 * <li>0 1/17 9-18 ? * MON-FRI<br>This means from Monday to Friday from 9 until 1800 hours
 	 * every 17 minutes starting at the first minute (9:01,9:18,9:35,9:52,10:01 etc). Note that ? was used
 	 * for the day of month as we specified a day of week. It's also important to note that this
 	 * isn't every 17 minutes minutes during 9-18 hours. For each hour periond, it's every 17 minutes starting at 1 minute 
 	 * but we reset back to 1 once we exhaust all values within the hour. So it's every 17 minutes within
 	 * an hour but not over the range. The final period of the hour is 9 minutes (9:52 -> 10:01)</li>
 	 * </ul>
 	 * If desired then multiple cron expressions can be concatenated if seperated by the
 	 * '|' vertical bar character. This allows the next valid time from a set of cron
 	 * expressions to be returned. This is useful when different times are needed during
 	 * weekdays and weekends. For example:<p>
 	 * 0 0 8 ? * MON-FRI | 0 0 10 ? * SAT,SUN<p>
 	 * The next time is calculated for each of the cron expressions and the first occuring
 	 * time is chosen as the next time.<p>
 	 * When combining multiple calendars using the bar operator then some complex interactions can occur. It's
 	 * recommended that you write a simple client of the user calendar that prints out the various times it would fire to make sure
 	 * that the event times are what you expect.<p>
 
 	 * <b>SIMPLE Arithmetic Calendar</b><br>
 	 * The default calendar is named 'SIMPLE'. When the calendar specifier string is
 	 * absent or 'SIMPLE' then the interval is calculated using a simple java.util.Calendar based scheme.<p>
 	 * The interval for the simple calendar is applied to the base java.util.Date object and consists of a set
 	 * of space-delimited terms and is evaluated from left to right using the default <code>java.util.Calendar.add()</add> instance
 	 * method for the current locale.<p> 
 	 * Each term has the form <b>NNNNNtype</b>, where <b>NNNN</b> is an integer and <b>type</b> indicates one
 	 * of the following values:
 	 * <ul>
 	 * <li>ms<br>Add x milliseconds to the base date.</li>
 	 * <li>seconds<br>Add x seconds to the base date.</li>
 	 * <li>minutes<br>Add x minutes to the base date.</li>
 	 * <li>hours<br>Add x hours to the base date.</li>
 	 * <li>days<br>Add x days to the base date.</li>
 	 * <li>months<br>Add x months to the base date.</li>
 	 * <li>years<br>Add x years to the base date.</li>
 	 * </ul>
 	 * Examples of these sorts of intervals are (note there are no spaces between the number and the type string):
 	 * <ul>
 	 * <li>10minutes<br>Every 10 minutes.</li>
 	 * <li>1hours<br>Every hour. Note the term 'hours' is used even when 1 is the number. The term types are
 	 * always plural</li>
 	 * <li>20minutes 1hours<br>Every hour and twenty minutes.</li>
 	 * <li>5seconds 5minutes 1hours 2days 1months 1years<br>
 	 * Add each term from left-to-right to the base time.</li>
 	 * </ul><p> 
 	 * Note that since each term is processed in order from left to right, the order
 	 * of the term sequence directly affects the final date result.
 	 * For example, if applying "1months 2days" to January 29, 2003, the result will be March 2nd, 2003 
 	 * (Jan 29 + 1 month is Feb 28 + 2 days is March 2nd).
 	 * If you instead apply "2days 1months" to January 29, 2003, the result is now February 28th, 2003
 	 * (Jan 29 + 2 days is Jan 31 + 1 month is Feb 28).
	 */
	private void setEventSchedule(BeanTaskInfo taskInfo, String eventSchedule) 
			throws UserCalendarInvalid {
		
		String calendarTerms = eventSchedule.trim();
		CalType calendarType = getCalendarType(calendarTerms);
		
		switch(calendarType) {
			case CRON:
			case SIMPLE:
				taskInfo.setUserCalendar(null, calendarType.name());
				break;
				
			case NUMBER:
				taskInfo.setUserCalendar(null, CalType.SIMPLE.name());
				calendarTerms += "seconds";
				break;
				
			default:
				throw new UserCalendarInvalid("No calendar matches terms sequence: " + eventSchedule);
				
		}
		
		taskInfo.setStartTimeInterval(calendarTerms);
		taskInfo.setRepeatInterval(calendarTerms);
		taskInfo.setNumberOfRepeats(-1); // Forever
		
	}
	
	/**
	 * Stops and pruges all 'OIF%' events from Scheduler
	 * 
	 * @param scheduler Scheduler
	 * 
	 */
	public void stopOifEvents(Scheduler scheduler, String oifTaskHandler ) {
		final String method = "stopOifEvents(scheduler)";
		
		try {
			// Find all OIF tasks - anything starting from OIF
			Iterator taskIterator = scheduler.findTasksByName(OIF_EVENT_PATTERN);
			
			while (taskIterator.hasNext()) {
				
				BeanTaskInfo taskInfo = (BeanTaskInfo) taskIterator.next();
				if(!taskInfo.getTaskHandlerJNDIName().equalsIgnoreCase(oifTaskHandler)) continue;
				try {
					
					logStatus("Cancelling OIF event", method, "Canceling and purging event: name: " 
							+ taskInfo.getName() + ", Id: " + taskInfo.getTaskId());
					
					scheduler.cancel(taskInfo.getTaskId(), true);
					
					logStatus("Cancelling OIF event", method, "Event cancelled and purged: name: " 
							+ taskInfo.getName() + ", Id: " + taskInfo.getTaskId());
					
				} catch (TaskInvalid e) {
					logger.error(e, "Exception when stopping OIF event");
				} catch (TaskPending e) {
					logger.error(e, "Exception when stopping OIF event");
				} catch (NotificationException e) {
					logger.error(e, "Exception when stopping OIF event");
				}
				
			}
			
		} catch (SchedulerNotAvailableException e) {
			logger.error(e, "Exception when stopping OIF event");
		}
	}
	
	/**
	 * Refreshes OifSchedulePropertyBean properties
	 */
	public void refreshProperties() {
		try {
			
			PropertyService.refreshComponentProperties(EventSchedulePropertyBean.COMPONENT);
			
		} catch (Exception e) {
			
			logStatus(MSG_SCHED_GEN_ERR_ID, "refreshProperties()", e.getMessage());
			
		}
	}
	
	/**
	 * Calendar recognition function:<br>
	 * Does rough calendar recognition based on supplied pattern
	 * 
	 * @param terms - calendar terms sequence
	 * 
	 * @return - type of calendar or <b>UNKNOWN<b> if calndar type cannot be identified
	 * 
	 */
	private static CalType getCalendarType(String terms) {
		// Simple number in seconds
		if(terms.matches("\\s*\\d+\\s*")) {
			return CalType.NUMBER;
		} else if(terms.matches("\\s*(\\d+(ms|seconds|minutes|hours|days|months|years) *)+\\s*")) {
			// SIMPLE calendar
			return CalType.SIMPLE;
		} else if(terms.matches("\\s*([0-9,/\\-L*?]+ *){5}.*")) {
			// CRON calendar
			return CalType.CRON;
		};
		
		return CalType.UNKNOWN;
	}


}
