package com.honda.scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.listeners.TriggerListenerSupport;



/**
 * VetoJobTriggerListener : The trigger is fired when Quartz determines that a job is ready to run
 * the trigger checks whether the scheduler has been disabled by GALC and if so vetoes job execution
 *  
 * @author      Suriya Sena
 * Date         3/31/2016
 */
public class VetoJobTriggerListener extends TriggerListenerSupport {
	
	private static final Logger log = LogManager.getLogger(VetoJobTriggerListener.class.getName());
	public static final String NAME = VetoJobTriggerListener.class.getName();
	private final Schedule schedule;
	
	public VetoJobTriggerListener(Schedule schedule) {
	   this.schedule=schedule;
	}

	@Override
	public String getName() {
		return NAME;
	}


	@Override
	public boolean vetoJobExecution(Trigger arg0, JobExecutionContext ctx) {
		boolean isVetoed = !schedule.isEventServiceEnabled();
		if (isVetoed) {
		   log.info(String.format("Job %s will be vetoed because event service is disabled",ctx.getJobDetail().getKey().toString()));
		}
		return isVetoed;
	}


}