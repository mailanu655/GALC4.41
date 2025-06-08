package com.honda.galc.fsm.engine;

import com.honda.galc.fsm.StateEventMatrix;

public class CompositeStateConfig {

	private Enum<?> entryState;
	private StateEventMatrix nestedSem;
	private Enum<?> exitState;
	
	/**
	 * @param entryState
	 * @param nestedSem2
	 * @param exitState
	 */
	public CompositeStateConfig(Enum<?> entryState, StateEventMatrix nestedSem2,
			Enum<?> exitState) {
		this.entryState = entryState;
		this.nestedSem = nestedSem2;
		this.exitState = exitState;
	}
	
	/**
	 * @return the entryState
	 */
	public Enum<?> getEntryState() {
		return entryState;
	}
	
	/**
	 * @return the exitState
	 */
	public Enum<?> getExitState() {
		return exitState;
	}
	
	/**
	 * @return the nestedSem
	 */
	public StateEventMatrix getNestedSem() {
		return nestedSem;
	}

}
