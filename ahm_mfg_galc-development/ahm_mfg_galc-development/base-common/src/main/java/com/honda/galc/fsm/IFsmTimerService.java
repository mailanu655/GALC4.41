package com.honda.galc.fsm;

public interface IFsmTimerService {

	void start(Object timerInput, long delayMills);

	void cancelAll();
	
	void cancel(Object timerInput);

	public abstract void clear(Object timerInput);

}
