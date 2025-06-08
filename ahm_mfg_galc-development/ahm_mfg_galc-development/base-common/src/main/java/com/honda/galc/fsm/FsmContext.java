package com.honda.galc.fsm;


import com.honda.galc.fsm.engine.FsmEngineContext;
import com.honda.galc.fsm.engine.FsmEngine;

public abstract class FsmContext {
	protected Object model;
	
	public static FsmContext create(StateEventMatrix pm, Object argAppCtx) {
		FsmEngineContext eCtx = new FsmEngineContext(pm, argAppCtx);
		return eCtx;
	}

	public void dispatch(Object input) {
		// Synchronize since some events can come concurrently
		synchronized (this) {
			FsmEngine.dispatch(this, input);
		}
	}

	/**
	 * @return the appContext
	 */
	public Object getModel() { 
		return model;
	}

	/**
	 * Change state explicitly from transition function
	 * 
	 * @param newState
	 */
	public abstract void changeState(Enum<?> newState);

	public abstract void fillInStates(StringBuilder sb);

	public abstract Enum<?> getCurrentState();

	public abstract IFsmTimerService getTimers();
	
}
