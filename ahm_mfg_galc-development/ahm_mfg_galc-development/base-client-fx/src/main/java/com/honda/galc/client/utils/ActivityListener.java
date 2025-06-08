package com.honda.galc.client.utils;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.RelaunchUtil;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.common.logging.Logger;

public class ActivityListener  {
    protected Timer timer;
	protected TimerTask timerTask;
	protected long delay;
	private boolean isRunning=false;
	
	
	public ActivityListener(long delay) {
		this.delay = delay;
		timer = new Timer();
	}
	
	public void start() {
		EventBusUtil.register(this);
		schedule();
		isRunning =true;
	}

	public void stop() {
		if (isRunning == true) {
		   timerTask.cancel();
		   EventBusUtil.unregister(this);
		   isRunning = false;
		}
	}
	
	@Subscribe
	public void received(ActivityEvent event) {
		reschedule();
	}

	protected void schedule() {
		timerTask = new TimerTask() {
			public void run() {
				try {
					Logger.getLogger().info(ActivityListener.class.getSimpleName() + " triggered - restarting the terminal");
					RelaunchUtil.restart();
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		};
		
		timer.schedule(timerTask, delay);
	}
	
	protected void reschedule() {
		timerTask.cancel();
		schedule();
	}
	
}
