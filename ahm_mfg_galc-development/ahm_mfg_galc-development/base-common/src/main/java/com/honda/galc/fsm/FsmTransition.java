package com.honda.galc.fsm;

public abstract class FsmTransition<M, I> extends FsmModelViewUser<M> {
	abstract public boolean execute(I inputValue);
}
