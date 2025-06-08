package com.honda.galc.client.metrics.monitors;

import static com.honda.galc.common.logging.Logger.getLogger;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import com.honda.galc.client.enumtype.DaysOfWeek;
import com.honda.galc.dao.conf.MetricGroupTypeDao;
import com.honda.galc.dao.conf.MetricScheduleDao;
import com.honda.galc.entity.conf.MetricGroupType;
import com.honda.galc.entity.conf.MetricSchedule;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Todd Roling
 * @author Subu Kathiresan
 * 
 * Checks which scheduled monitors should be activated.
 * Uses a format similar to cron, but only numbers, commas, and hyphens are supported.	
 */
public class MetricsScheduleManager extends Thread {
	private static final int MAX_CONCURRENT_MONITORS = 20;
	private static final int MAX_MONITOR_EXECUTION_TIME = 3600; 	// in seconds
	private static final int NUM_EXPECTED_SCHEDULE_TERMS = 5; 		// number of terms on a cron job schedule
	private static final int SCHEDULE_PERIOD = 60;
	
	private static MetricsScheduleManager _instance = null;
	private ScheduledExecutorService _scheduler = null;
	
	@SuppressWarnings("unchecked")
	private Hashtable<ScheduledFuture<?>, AbstractBaseMetricsMonitor> _monitorHandles = new Hashtable<ScheduledFuture<?>, AbstractBaseMetricsMonitor>();
		
	private MetricsScheduleManager() {
		setScheduler(Executors.newScheduledThreadPool(MAX_CONCURRENT_MONITORS));
	}

	/**
	 * returns the singleton instance of MetricsScheduleManager
	 * 
	 * @return
	 */
	public static MetricsScheduleManager getInstance() {
		if (_instance == null)
			_instance = new MetricsScheduleManager();
		
		return _instance;
	}
	
	/**
	 * Starts daemon thread to execute run command every SCHEDULE_PERIOD seconds
	 */
	public void startManager() {
		try {
			Executors.newScheduledThreadPool(1).scheduleAtFixedRate(getInstance(), 0, SCHEDULE_PERIOD, SECONDS);
		}
		catch(Exception e) {
			getLogger().error("Could not start Metrics Schedule Manager daemon");
		}
	}
	
	/**
	 * retrieves schedules and kicks off monitors 
	 */
	@SuppressWarnings("unchecked")
	public void run() {		
		Date currentDate = new Date();
		Date effectiveStart, effectiveEnd;
		List<AbstractBaseMetricsMonitor> metricsMonitors;
		String schedule;
		
		removeInactiveMonitors();
		
		getLogger().info("Number of active monitors: " + _monitorHandles.size());
		
		for(MetricSchedule metricSchedule : populateSchedules()) {
			effectiveStart = metricSchedule.getEffectiveStart();
			effectiveEnd = metricSchedule.getEffectiveEnd();
			schedule = metricSchedule.getMetricSchedule();
			
			try {
				metricsMonitors = createNewMetricsMonitors(metricSchedule);
				
				for(AbstractBaseMetricsMonitor monitor: metricsMonitors) {
					// null schedules will have associated monitors canceled/stopped
					if(schedule == null) {
						cancelMonitorByName(monitor.getMonitorName());
					}
					else if((effectiveStart == null || currentDate.compareTo(effectiveStart) > -1) &&
							(effectiveEnd == null || currentDate.compareTo(effectiveEnd) < 1) &&
							isKickOffTime(schedule)) {						
						// single Schedule could be used by multiple monitors, need to kick off all of them
						scheduleKickOff(monitor);
					}
				}
			}
			catch(Exception e) {
				getLogger().error("Metric Schedule: " + metricSchedule.getMetricScheduleName() + " could not be run");
			}
		}
	}

	/**
	 * Creates a new instance of the Metrics Monitor to start
	 * 
	 * @param metricSchedule
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@SuppressWarnings("unchecked")
	private List<AbstractBaseMetricsMonitor> createNewMetricsMonitors(MetricSchedule metricSchedule) 
		throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		
		List<AbstractBaseMetricsMonitor>  metricsMonitors = new ArrayList<AbstractBaseMetricsMonitor>();		
		List<MetricGroupType> metricGroupTypes = ServiceFactory.getDao(MetricGroupTypeDao.class)
				.findAllByMetricScheduleId(ServiceFactory.getDao(MetricScheduleDao.class)
				.findFirstByScheduleName(metricSchedule.getMetricScheduleName()).getMetricScheduleId());
		
		for(MetricGroupType groupType: metricGroupTypes) {
			Class<AbstractBaseMetricsMonitor> monitorClass = null;
			try {
				monitorClass = (Class<AbstractBaseMetricsMonitor>) Thread.currentThread().getContextClassLoader().loadClass(groupType.getMetricGroupTypeStrategy().trim());
				metricsMonitors.add(monitorClass.newInstance());
			}catch(Exception ex) {
				getLogger().warn("Could not instantiate " + monitorClass);
			}
		}
		return metricsMonitors;
	}

	/**
	 * schedules a kick off for the passed in monitor
	 * 
	 * @param metricsMonitor
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	private void scheduleKickOff(AbstractBaseMetricsMonitor metricsMonitor) {
		if (!(metricsMonitor.isSingleInstanceOnly() && isRunning(metricsMonitor))) {
			final ScheduledFuture<?> monitorHandle = getScheduler().schedule(metricsMonitor, 0, SECONDS);
			getMmonitorHandles().put(monitorHandle, metricsMonitor);

			// Schedule the cancels
			// TODO: get the cancel schedule from the MTX_SCHEDULE table?
//			getScheduler().schedule(new Runnable() {
//				public void run() { 
//					monitorHandle.cancel(true); 
//				}
//			}, MAX_MONITOR_EXECUTION_TIME, SECONDS);

			if (metricsMonitor.activate())
				getLogger().info("Successfully activated " + metricsMonitor.getMonitorName() + " on " + metricsMonitor.getName());
			else
				getLogger().info(metricsMonitor.getMonitorName() + " Activation failed");
		}
	}

	/**
	 * populates schedules from the MTX_SCHEDULE_TBX table
	 * 
	 * @param schedules
	 * @return
	 */
	private List<MetricSchedule> populateSchedules() {
		List<MetricSchedule> schedules = null;
		try {
			schedules = ServiceFactory.getDao(MetricScheduleDao.class).findAll();
		}
		catch(Exception e) {
			getLogger().error("Failed to find schedules");
		}
		return schedules;
	}

	/**
	 * removes inactive monitors from the monitor handles map
	 */
	private void removeInactiveMonitors() {
		try {
			// Remove inactive monitors
			for(ScheduledFuture<?> monitorHandle : getMmonitorHandles().keySet()) {
				if(monitorHandle.isCancelled() || monitorHandle.isDone())
					getMmonitorHandles().remove(monitorHandle);
			}
		}catch(Exception e) {
			getLogger().error("Failed to remove inactive monitor");
		}
	}
	
	/**
	 * schedules cancel operation for all running monitors
	 */
	public void cancelAllMonitors() {
		try {
			for(ScheduledFuture<?> monitorHandle : getMmonitorHandles().keySet()) {
				final ScheduledFuture<?> cancelledFuture = monitorHandle;				
//				getScheduler().schedule(new Runnable() {
//		            public void run() { 
//		            	cancelledFuture.cancel(true);
//		            } 
//		        }, 0, SECONDS);
			}
			
			getMmonitorHandles().clear();
		}catch(Exception e) {
			getLogger().error("Schedule could not be cancelled");
		}
	}
	
	/**
	 * schedules cancel operation for running monitors having the provided name
	 */
	public void cancelMonitorByName(String monitorName) {
		try {
			for(ScheduledFuture<?> monitorHandle : getMmonitorHandles().keySet()) {
				if(_monitorHandles.get(monitorHandle).getMonitorName().equals(monitorName)) {
					final ScheduledFuture<?> cancelledFuture = monitorHandle;		
					
//					getScheduler().schedule(new Runnable() {
//			            public void run() { 
//			            	cancelledFuture.cancel(true); 
//			            } 
//			        }, 0, SECONDS);
					
					getLogger().info("Cancelling monitor: " + monitorName);
				}
			}
		}catch(Exception e) {
			getLogger().error("Monitor could not be cancelled");
		}
	}
	
	/**
	 * checks if current time matches the kick off time from the provided schedule
	 * 
	 * @param schedule
	 * @return
	 */
	private boolean isKickOffTime(String schedule) {
		String[] curTime = getCurrentDateTimeInCronFormat();
		String[] scheduleTerms = schedule.split(" ");								// 30 4,5 * * 1-6  Schedule terms are space separated.
		
		// Are there the right number of tokens in each string?
		if (!(curTime.length == NUM_EXPECTED_SCHEDULE_TERMS 
				&& scheduleTerms.length == NUM_EXPECTED_SCHEDULE_TERMS))
			return false;
		
		boolean result = true;
		
		try {
			for(int i = 0; i < curTime.length; ++i) {
				if (!result)
					return result;
				
				// Skip any *
				if(!scheduleTerms[i].equals("*")) {				
					String[] scheduleTokens = scheduleTerms[i].split(",");			// 4,5
					for(int j = 0; j < scheduleTokens.length; ++j) {
						String token = scheduleTokens[j];
						if(token.contains("-")) {									// Ranged schedule term tokens use hyphens.
							String[] range = token.split("-");						// 1-6				
							int curToken = Integer.parseInt(curTime[i]);
							// Is the current date time term token in range?
							result = Integer.parseInt(range[0]) <= curToken && curToken <= Integer.parseInt(range[1]);
						} else
							result = token.equals(curTime[i]);
					}
				}
			}
		}catch(Exception e) {
			getLogger().error("Invalid metric schedule format..." + schedule);
			return false;
		}		
			
		return result;
	}
	
	/**
	 * returns current date and time in the 'cron' format
	 * 
	 * returns 
	 * @return
	 */
	private String[] getCurrentDateTimeInCronFormat() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm HH dd MM EEE");
		String[] cronValue = simpleDateFormat.format(new Date()).split(" ");
		
		// Convert current day from string to integer format
		cronValue[NUM_EXPECTED_SCHEDULE_TERMS - 1] = Integer.toString(DaysOfWeek.getDay(cronValue[NUM_EXPECTED_SCHEDULE_TERMS - 1].toUpperCase()));
		return cronValue;
	}
	
	/**
	 * returns true if the monitor is already running
	 * 
	 * @param newMonitor
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isRunning(AbstractBaseMetricsMonitor newMonitor) {
		for(AbstractBaseMetricsMonitor monitor : getMmonitorHandles().values()) {
			if(monitor.getMonitorName().equals(newMonitor.getMonitorName()))
				return monitor.isRunning();
		}
		return false;
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private void setMonitorHandles(Hashtable<ScheduledFuture<?>, AbstractBaseMetricsMonitor> monitorHandles) {
		_monitorHandles = monitorHandles;
	}

	@SuppressWarnings("unchecked")
	private Hashtable<ScheduledFuture<?>, AbstractBaseMetricsMonitor> getMmonitorHandles() {
		return _monitorHandles;
	}

	private void setScheduler(ScheduledExecutorService scheduler) {
		_scheduler = scheduler;
	}

	private ScheduledExecutorService getScheduler() {
		return _scheduler;
	}
}
