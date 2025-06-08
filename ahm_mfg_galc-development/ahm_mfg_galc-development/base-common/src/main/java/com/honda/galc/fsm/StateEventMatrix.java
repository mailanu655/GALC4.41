package com.honda.galc.fsm;

import java.util.HashMap;
import java.util.Map;

import com.honda.galc.fsm.engine.CompositeStateConfig;
import com.honda.galc.fsm.engine.FsmDirective;

public class StateEventMatrix {
	private Map<Class<?>, Integer> inputs;
	private TransConfig defaultTransition;
	private Map<Enum<?>, TransConfig[]> stateTransitions;
	
	/**
	 * Enhancer enriches some state by allowing to invoke certain<be>
	 * function before entering or exiting some state
	 */
	private Map<Enum<?>, Class<? extends FsmStateEnhancer<?>>> enhancers;
	private Map<Enum<?>, CompositeStateConfig> compositeStateConfigs;
	private String name;
	private Enum<?> initialState;

	/**
	 * @param name
	 */
	public StateEventMatrix(String name) {
		this.name = name;
		inputs = new HashMap<Class<?>, Integer>();
		stateTransitions = new HashMap<Enum<?>, TransConfig[]>();
		compositeStateConfigs = new HashMap<Enum<?>, CompositeStateConfig>();
		enhancers = new HashMap<Enum<?>, Class<? extends FsmStateEnhancer<?>>>();
	}

	/**
	 * Define inputs
	 * 
	 * @param argInputs
	 */
	public void inputs(Class<?>... argInputs) {
		for (int i = 0; i < argInputs.length; i++) {
			inputs.put(argInputs[i], i);
		}
	}

	public Map<Class<?>, Integer> getInputs() {
		return inputs;
	}

	public void setInputs(Map<Class<?>, Integer> inputs) {
		this.inputs = inputs;
	}

	public TransConfig getDefaultTransition() {
		return defaultTransition;
	}

	public Map<Enum<?>, TransConfig[]> getStateTransitions() {
		return stateTransitions;
	}

	public void setStateTransitions(Map<Enum<?>, TransConfig[]> stateTransitions) {
		this.stateTransitions = stateTransitions;
	}

	public Map<Enum<?>, CompositeStateConfig> getCompositeStateConfigs() {
		return compositeStateConfigs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Enum<?> getInitialState() {
		return initialState;
	}

	public void setInitialState(Enum<?> initialState) {
		this.initialState = initialState;
	}
	
	/**
	 * @param argInitialState
	 * @return
	 */
	public StateEventMatrix initialState(Enum<?> argInitialState) {
		this.initialState = argInitialState;
		return this;
	}
	
	/**
	 * Set default transition for the top state
	 * 
	 * @param argDefaultTransition
	 */
	public StateEventMatrix setDefaultTransition(TransConfig argDefaultTransition) {
		this.defaultTransition = argDefaultTransition;
		return this;
	}

	/**
	 * Define transitions for the state
	 * 
	 * @param state
	 * @param transitions
	 */
	public void state(Enum<?> state, TransConfig... transitions) {
		stateTransitions.put(state, transitions);		
	}

	public StateEventMatrix setNestedEntryAndExitStates(Enum<?> entryState,
			StateEventMatrix nestedSem, Enum<?> exitState) {
		this.getCompositeStateConfigs().put(entryState, new CompositeStateConfig(entryState, nestedSem, exitState));
		return this;
	}

	public TransConfig getTransConfig(Enum<?> stateOrdinal, Object inputValue, boolean exceptionOnError) {
		TransConfig[] transConfigs = this.stateTransitions.get(stateOrdinal);
		if(transConfigs == null) {
			if (exceptionOnError) {
				throw new IllegalStateException("SM is not configured for state " + stateOrdinal + " !!!");
			} else {
				return null;
			}
		}
		
		Integer inputIndexObject = this.inputs.get(inputValue.getClass());
		
		int inputIndex;
				
		if(inputIndexObject == null 
				|| (inputIndex = inputIndexObject.intValue()) >= transConfigs.length) {
			if (exceptionOnError) {
				throw new IllegalArgumentException("Input <" + inputValue + "> is invalid for state:<" + stateOrdinal
						+ ">: input index:" + inputIndexObject + ", max: " + (transConfigs.length - 1) + " !!!");
			} else {
				return null;
			}
		}
		
		TransConfig transConfig = transConfigs[inputIndex];
		if(transConfig == null) {
			transConfig = this.getDefaultTransition();
		}
		
		return transConfig;
	}

	public TransConfig getTransConfig(Enum<?> stateOrdinal, Object inputValue) {
		return getTransConfig(stateOrdinal, inputValue, false);
	}

	/**
	 * Factory method for transition configuration
	 * 
	 * @param transition
	 * @param nextState
	 * @return
	 */
	public static TransConfig tx(Class<?> transition, Enum<?> nextState) {
		return new TransConfig(transition, nextState, null);
	}

	public static TransConfig txX(Class<?> transition) {
		return new TransConfig(transition, null, FsmDirective.EXIT_NESTED_STATE);
	}

	/**
	 * Factory method for transition configuration
	 * 
	 * @param transition
	 * @return
	 */
	public static TransConfig txS(Class<?> transition) {
		return new TransConfig(transition, null, null);
	}

	public void enrich(Enum<?> state, Class<? extends FsmStateEnhancer<?>> enhancer) {
		this.enhancers.put(state, enhancer);		
	}

	public Class<? extends FsmStateEnhancer<?>> getEnhancerClass(Enum<?> currentState) {
		return this.enhancers.get(currentState);
	}


}