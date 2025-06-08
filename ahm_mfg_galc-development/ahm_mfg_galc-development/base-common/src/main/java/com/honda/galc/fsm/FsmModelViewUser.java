package com.honda.galc.fsm;

public class FsmModelViewUser<M> {

	protected M model;
	protected IFsmTimerService timers;
	private FsmContext fsmContext;

	public void setModel(M argModel) {
		this.model = argModel;
	}

	public void setFsmContext(FsmContext ctx) {
		fsmContext = ctx;
		this.timers = ctx.getTimers();
	}

	protected FsmContext getFsm() {
		return fsmContext;
	}

	/**
	 * @return the model
	 */
	protected M getModel() {
		return model;
	}

}
