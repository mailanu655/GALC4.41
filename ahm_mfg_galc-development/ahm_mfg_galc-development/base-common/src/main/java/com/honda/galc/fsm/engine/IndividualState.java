package com.honda.galc.fsm.engine;

import com.honda.galc.fsm.StateEventMatrix;
import com.honda.galc.fsm.TransConfig;

public class IndividualState {

	private Enum<?> state;
	
	private StateEventMatrix sem;
	
	private TransConfig currentTransition;
	
	private Enum<?> exitState;

	/**
	 * @param sem
	 * @param stateOrdinal
	 */
	public IndividualState(StateEventMatrix sem, Enum<?> stateOrdinal) {
		this.sem = sem;
		this.state = stateOrdinal;
	}

	/**
	 * @return the stateOrdinal
	 */
	public Enum<?> getState() {
		return state;
	}

	/**
	 * @param stateOrdinal the stateOrdinal to set
	 */
	public void setState(Enum<?> stateOrdinal) {
		this.state = stateOrdinal;
	}

	/**
	 * @return the sem
	 */
	public StateEventMatrix getSem() {
		return sem;
	}

	/**
	 * @return the currentTransition
	 */
	public TransConfig getCurrentTransition() {
		return currentTransition;
	}

	/**
	 * @param currentTransition the currentTransition to set
	 */
	public void setCurrentTransition(TransConfig currentTransition) {
		this.currentTransition = currentTransition;
	}

	/**
	 * @return the exitState
	 */
	public Enum<?> getExitState() {
		return exitState;
	}

	/**
	 * @param exitState the exitState to set
	 */
	public void setExitState(Enum<?> exitState) {
		this.exitState = exitState;
	}
}
