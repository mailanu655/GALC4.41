package com.honda.galc.fsm.engine;

import java.util.TimerTask;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.fsm.FsmContext;

public class FsmTimerTask extends TimerTask {
	private static final Logger log = Logger.getLogger();

	Object timerInput;
	FsmContext fsmContext;
	
	/**
	 * @param context
	 * @param input
	 */
	public FsmTimerTask(FsmContext context, Object input) {
		this.fsmContext = context;
		this.timerInput = input;
	}

	@Override
	public void run() {
		log.info("Received timer: " + timerInput);
		this.fsmContext.getTimers().clear(timerInput);
		this.fsmContext.dispatch(timerInput);
	}

}
