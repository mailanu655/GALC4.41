package com.honda.galc.fsm.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import com.honda.galc.fsm.FsmContext;
import com.honda.galc.fsm.IFsmTimerService;

public class FsmJreTimerService implements IFsmTimerService {
	
	private Map<Object, Timer> timers = new HashMap<Object, Timer>();
	
	private FsmContext fsmContext;

	public FsmJreTimerService(FsmEngineContext fsmEngineContext) {
		fsmContext = fsmEngineContext;
	}

	public void cancel(Object timerInput) {
		synchronized (timers) {
			Timer timer = timers.get(timerInput);
			if (timer != null) {
				timer.cancel();
				timers.remove(timerInput);
			}
		}
	}

	public void clear(Object timerInput) {
		synchronized (timers) {
			timers.remove(timerInput);
		}
	}
	
	public void cancelAll() {
		synchronized (timers) {
			timers.values();
			for (Timer timer : timers.values()) {
				timer.cancel();
			}
			timers.clear();
		}

	}

	public void start(Object timerInput, long delayMills) {
		Timer timer = new Timer(timerInput.toString());

		timer.schedule(new FsmTimerTask(this.fsmContext, timerInput), delayMills);
		
		timers.put(timerInput, timer);
	}

}
