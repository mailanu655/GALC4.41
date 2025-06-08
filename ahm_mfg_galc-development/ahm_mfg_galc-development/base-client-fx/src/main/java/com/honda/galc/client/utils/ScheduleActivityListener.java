package com.honda.galc.client.utils;

import java.util.TimerTask;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.schedule.SchedulingEvent;
import com.honda.galc.client.schedule.SchedulingEventType;
import com.honda.galc.client.ui.EventBusUtil;


public class ScheduleActivityListener extends ActivityListener {
	
	
	public ScheduleActivityListener(long delay) {
		super(delay);
	}
	
	@Subscribe
	public void received(SchedulingActivityEvent event) {
		reschedule();
	}

	protected void schedule() {
		timerTask = new TimerTask() {
			public void run() {
				EventBusUtil.publish(new SchedulingEvent(null, SchedulingEventType.REFRESH_SCHEDULE_CLIENT_ON_TIMEOUT));
			};
		};
		
		timer.schedule(timerTask, delay);
	}
	
}
