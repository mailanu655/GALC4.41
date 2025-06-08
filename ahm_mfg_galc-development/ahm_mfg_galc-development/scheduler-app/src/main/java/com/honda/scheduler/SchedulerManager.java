package com.honda.scheduler;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.ListenerManager;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.EverythingMatcher;
import org.quartz.impl.matchers.GroupMatcher;



/**
 * SchedulerManager : Provides functionality to schedule jobs using the 
 * Quartz Scheduler using a schedule defined in the GAL489TBX.
 *  
 * WARNING: The cron expression used to setup IBM Jobs differ slightly from 
 * the cron expressions used by the Quartz scheduler. 
 *  
 * @author      Suriya Sena
 * Date         3/17/2016
 */


public class SchedulerManager {


		private static final Logger log = LogManager.getLogger(SchedulerManager.class.getName());
		private static final String DEFAULT_EVENT = "AT ONCE";
		private Scheduler scheduler;
		private Schedule  schedule;
		private String    serverUrl;
		private int       timeout;
		private int       syncInterval;
		private final String replyToAddress;
		private final String user;
		private static final int MAX_SYNC_INTERVAL = 60;
		private static final int MIN_SYNC_INTERVAL = 5;
		private ScheduledExecutorService executor;
		private JobMap jobRegistry = new JobMap(); 
        public  static final String JOB_MAP ="job_registry";		
		
	
		private SchedulerManager(Scheduler scheduler, String serverUrl,int timeout,int  syncInterval,String replyToAddr, Schedule schedule, String user) throws UnknownHostException  {
			this.scheduler = scheduler;
			this.schedule = schedule;
			this.serverUrl = serverUrl;
			this.timeout = timeout;
			this.replyToAddress = replyToAddr;
			this.syncInterval = syncInterval;
			this.user = user;
			
			try {
				scheduler.getContext().put(JOB_MAP,jobRegistry);
			} catch (SchedulerException e) {
				log.error("Failed to setup active job map",e);
			}
		}
		

		
		public void startScheduler() {
			standBy();
			unscheduleEvents();			
			schedule.load();
			addJobVetoListener();
			scheduleEvents();
			start();
		}
		
		public void stopScheduler() {
			standBy();
			unscheduleEvents();
		}
		

		public String syncScheduler() {
			
			int countDeleted=0;
			int countNew=0;
			int countUpdated=0;
			int countUnchanged=0;
			
			
			log.info("Synchronize schedule started");

			
			schedule.load();

			Map<JobKey,String> galcSchedule = schedule.getScheduleAsMap();
			Iterator<Map.Entry<JobKey,String>> scheduleIterator = galcSchedule.entrySet().iterator();

			try {
				// Check database scheduler against Quartz schedule, handle schedule changes and  job additions to db
				while (scheduleIterator.hasNext()) {
					Map.Entry<JobKey,String> entry  = scheduleIterator.next();
					String srcTask = entry.getKey().getName();
					String srcGroup = entry.getKey().getGroup();
					String srcCronExpr = entry.getValue();

					JobKey jobKey = new JobKey(srcTask,srcGroup);
					JobDetail tgtJobDetail = scheduler.getJobDetail(jobKey);
					if (tgtJobDetail != null) {
						List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
						Trigger tgtTrigger= triggers.get(0);
						String tgtCronExpr = tgtTrigger.getDescription();
						if (srcCronExpr.equals(tgtCronExpr)) {
							log.info(String.format("Unchanged schedule :Task %s, group %s expr %s", srcTask, srcGroup,srcCronExpr));
							countUnchanged++;
						} else {
							log.info(String.format("Reschedule : Task %s, group %s, expr %s!=%s",srcTask, srcGroup, srcCronExpr,tgtCronExpr));
							scheduler.deleteJob(jobKey);
							scheduleCronTask(srcTask,srcGroup,srcCronExpr) ;
							countUpdated++;
						}
					} else {
						log.info(String.format("Schedule new : Task %s, group %s, expr %s",srcTask, srcGroup, srcCronExpr));
						scheduleCronTask(srcTask,srcGroup,srcCronExpr) ;
						countNew++;
					}
				}
				
				// Check Quartz scheduler against Database, handle job deletions
				for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.anyJobGroup())) {
					if (galcSchedule.get(jobKey) == null) {
						log.info(String.format("Delete : Task %s, group %s",jobKey.getName(), jobKey.getGroup()));
						scheduler.deleteJob(jobKey);
						countDeleted++;
					}
				}
			} catch (SchedulerException e) {
				e.printStackTrace();
			}

			String summary = String.format("Job synchronization summary : added (%d), updated (%d), deleted (%d), unchanged (%d)", countNew, countUpdated, countDeleted, countUnchanged);
			log.info(summary);
			return summary;
		}
		
		public String getStatus() {
			StringBuilder sb = new StringBuilder();
			try {
				sb.append("\n");
				sb.append(scheduler.getMetaData().getSummary());
				sb.append("\n");
				
				sb.append(String.format("Scheduler is %s\n\n",(schedule.isEventServiceEnabled()?"ENABLED":"***DISABLED***")));

				sb.append(String.format("%-40.40s%-40.40s%-30.30s%-30.30s%-9s%n","Group","Job","Schedule","NextFireTime","isRunning**"));
				sb.append(String.format("%-40.40s%-40.40s%-30.30s%-30.30s%-9s%n"," "," "," "," "," ").replace(' ', '='));

				for (String group : scheduler.getJobGroupNames()) {
					for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group))) {
						List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
						
						Trigger trigger = triggers.get(0);
						
						sb.append(String.format("%-40.40s%-40.40s%-30.30s%-30.30s%9s%n",jobKey.getGroup(),jobKey.getName(),trigger.getDescription(),nextFireTimeAsString(trigger.getNextFireTime()),isJobRunning(jobKey)));
					}
				}
				
				sb.append(String.format("%n%n** - Run status for jobs started on this instance, i.e. its not cluster aware%n"));
			} catch (SchedulerException e) {
				sb.append(e.getMessage());
			}

			return sb.toString();
		}
		
		private String nextFireTimeAsString(Date time) {
			if (time == null){
				return "";
			} else {
				return String.format("%tc", time);
			}
		}
		
		
		public void notify(String jobId, String status) {
			jobRegistry.notify(jobId, status);
		}
		
		private boolean isJobRunning(JobKey jobKey) throws SchedulerException {
		    List<JobExecutionContext> currentJobs = scheduler.getCurrentlyExecutingJobs();
		    
		    for (JobExecutionContext jobCtx : currentJobs) {
		        String thisJobName = jobCtx.getJobDetail().getKey().getName();
		        String thisGroupName = jobCtx.getJobDetail().getKey().getGroup();
		        if (jobKey.getName().equalsIgnoreCase(thisJobName) && jobKey.getGroup().equalsIgnoreCase(thisGroupName))  {
		            return true;
		        }
		    }
		    return false;
		}
		
		
	    public String getShortStatus() {
	    	String message;
	    	try {
				String status=(schedule.isEventServiceEnabled() && scheduler.isStarted() && !scheduler.isInStandbyMode()== true? "RUNNING" : "STOPPED");
				message = String.format("%s,isEnabled=%b,isStarted=%b,isInStandby=%b",status,schedule.isEventServiceEnabled(),scheduler.isStarted(),scheduler.isInStandbyMode());
				return message;
			} catch (SchedulerException e) {
				return e.getMessage();
			}
	    }
	    
		
		private void unscheduleEvents() {
			try {
				scheduler.standby();
				
				for (String group : scheduler.getJobGroupNames()) {
					for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group))) {
						if (!isJobRunning(jobKey)) {
						   scheduler.deleteJob(jobKey);
						}  else {
							log.warn(String.format("Job [%s] is still running, waiting for completion", jobKey));
						}
					}
				}
			} catch (SchedulerException e) {
				log.error("Failed to unschedule events",e);
			}

		}
		

		private void scheduleEvents() {
			log.info("Schedule events started.");
			
            scheduleSyncJob();
			
			List<String> eventList = schedule.getEventList();
			
			for (String event : eventList) {
				if (event.length() > 0) {
					String eventSchedule = schedule.getSchedule(event);
					List<String> eventTaskList = schedule.getTaskList(event);
					
					if  (eventSchedule == null || (eventSchedule.length()==0)) {
						log.warn(String.format("No event schedule (cron expression) defined for [%s]!!!",event));
						continue;
					} else if (eventTaskList.size() == 0) {
						log.warn(String.format("No tasks scheduled for event [%s]!!!",event));
						continue;
					} 
					
					for (String task : eventTaskList) {
						  if (scheduleCronTask(task,event,eventSchedule)) {
							log.info(String.format("Task [%s] scheduled with [%s] for event [%s] successfully.",task,eventSchedule,event));
						  } else {
							log.error(String.format("Failed to schedule task [%s] with [%s] for event [%s].",task,eventSchedule,event));
						  }
					}
				}
			}

			log.info("Schedule events completed successfully.");
		}


		
	    private boolean scheduleCronTask(String taskName,String eventName, String cronExpr) {
	    	
	    	if (!isValidCronExpr(cronExpr)) {
	    		log.error(String.format("Failed to scheduled OIF event [%s], invalid cron expression [%s]!!!",taskName,cronExpr));	
	    		return false;
	    	}
	    	
	     	try {
				JobDetail jobDetail = createJobDetail(taskName, eventName);
				
				
				/* Create a cron schedule to run the HTTPInvokerJob */
	            Trigger cronTrigger = TriggerBuilder
	                    .newTrigger() 
	                    .forJob(jobDetail) 
	                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpr))
	                    .withDescription(cronExpr)
	                    .startNow() 
	                    .build();
	         
	            
	            /* Register the job and its schedule to the scheduler */
				scheduler.scheduleJob(jobDetail, cronTrigger);
				
		     	return true;
				
			} catch (SchedulerException e) {
				log.error(String.format("Failed to scheduled OIF event [%s],cron expression [%s]",taskName,cronExpr),e);	
				return false;
			} 
	    }
	    
	    public boolean scheduleImmediateTask(String taskName) {
	     	try {
				JobDetail jobDetail = createJobDetail(taskName,DEFAULT_EVENT);
	            Trigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail).withDescription(DEFAULT_EVENT).startNow().build();
				scheduler.scheduleJob(jobDetail, trigger);
		     	return true;
			} catch (SchedulerException e) {
				log.error(String.format("Failed to scheduled OIF event [%s] to run immediately",taskName),e);	
				return false;
			} 
	    }



		private JobDetail createJobDetail(String taskName, String eventName) {
			/* Create a HTTPInvokerJob to process the Event and ultimately the OIFTask */
			JobDetail jobDetail = JobBuilder.newJob()
					                        .withIdentity(taskName.toUpperCase(),eventName)
					                        .ofType(HTTPInvokerJob.class)
					                        .build();
			
			/* Tell the HTTPInvokerJob the name of task to execute and the server URL*/
			jobDetail.getJobDataMap().put(HTTPInvokerJob.TASKNAME_KEY,taskName);
			jobDetail.getJobDataMap().put(HTTPInvokerJob.SERVER_KEY,serverUrl);
			jobDetail.getJobDataMap().put(HTTPInvokerJob.TIMEOUT_SECS,timeout);
			jobDetail.getJobDataMap().put(HTTPInvokerJob.REPLY_TO_URL,replyToAddress);
			jobDetail.getJobDataMap().put(HTTPInvokerJob.USER, user);
			return jobDetail;
		}

	    
	    private boolean isValidCronExpr(String expr) {
	    	try {
			    new CronExpression(expr);
				return true;
			} catch (ParseException e) {
				return false;
			}
	    }
	    
	 
		private void standBy() {
			try {
				if (!scheduler.isInStandbyMode()) {
					scheduler.standby();
				}
			} catch (SchedulerException e) {
				log.error("Failed to put scheduler into standby", e);
			}
		}

		
		private void start() {
			try {
				if (scheduler.isInStandbyMode()) {
				  scheduler.start();
				} else {
					log.info("Scheduler has already been started!");
				}
				
			} catch (SchedulerException e) {
				log.error("failed to start scheduler",e);
			}
		}

		private void addJobVetoListener() {
			/* Add listener to skip GALC Job execution if the GALC scheduler has been disabled in DB */
			ListenerManager listenerManager;
			try {
				listenerManager = scheduler.getListenerManager();
				
				if (listenerManager.getTriggerListener(VetoJobTriggerListener.NAME) == null) {
					listenerManager.addTriggerListener(new VetoJobTriggerListener(schedule),EverythingMatcher.allTriggers());
					log.info(VetoJobTriggerListener.NAME + " trigger added successfully.");
				}

			} catch (SchedulerException e) {
                log.error("Failed to add veto listener ", e);
			}
		}
		
		
		private void scheduleSyncJob() {
		    /* I'm not using the quartz here because I need a reference to the schedule manager, quartz requires all
		     * job parameters to be serializable which is a problem	
		     */
			if (executor != null) {
				return;
			}
			
		    syncInterval = Math.max(MIN_SYNC_INTERVAL,syncInterval);
		    syncInterval = Math.min(MAX_SYNC_INTERVAL,syncInterval);
			
			executor = Executors.newScheduledThreadPool(1);
			executor.scheduleAtFixedRate(new Runnable() {
				
				@Override
				public void run() {
					syncScheduler();
				}
			 }, syncInterval * 60, syncInterval * 60, TimeUnit.SECONDS);
			
			 log.info(String.format("Scheduled synchronize every %d minute(s)",syncInterval));
		}

}
