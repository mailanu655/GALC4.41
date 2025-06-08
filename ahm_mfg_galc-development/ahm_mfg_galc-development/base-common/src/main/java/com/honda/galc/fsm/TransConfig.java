package com.honda.galc.fsm;

import com.honda.galc.fsm.engine.FsmDirective;



public class TransConfig {
	private Enum<?> nextState;
	private Class<?> transitionClass;
	private FsmDirective directive;

	public TransConfig(Class<?> transition, Enum<?> nextState, FsmDirective argDirective) {
		super();
		this.transitionClass = transition;
		this.nextState = nextState;
		this.directive = argDirective;
	}
	
	public Enum<?> getNextState() {
		return nextState;
	}
	
	public Class<?> getTransitionClass() {
		return transitionClass;
	}
	
	/**
	 * @return the directive
	 */
	public FsmDirective getDirective() {
		return directive;
	}
	
	public boolean isToExit() {
		return this.directive != null && this.directive == FsmDirective.EXIT_NESTED_STATE;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("TransConfig: class: ").append(transitionClass.getName());
		sb.append(", next: ").append(this.nextState);
		sb.append(", directive: ").append(this.directive);
		return sb.toString();
	}

}
