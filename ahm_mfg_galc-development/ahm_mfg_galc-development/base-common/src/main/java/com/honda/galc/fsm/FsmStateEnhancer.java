package com.honda.galc.fsm;

public abstract class FsmStateEnhancer<M> extends FsmModelViewUser<M> {

	public abstract void enterState();

	public abstract void exitState();

}
