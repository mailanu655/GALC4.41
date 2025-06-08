package com.honda.galc.fsm.engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.honda.galc.fsm.FsmContext;
import com.honda.galc.fsm.FsmStateEnhancer;
import com.honda.galc.fsm.FsmTransition;
import com.honda.galc.fsm.IFsmTimerService;
import com.honda.galc.fsm.StateEventMatrix;
import com.honda.galc.fsm.TransConfig;

public class FsmEngineContext extends FsmContext {

	private StateEventMatrix sem;
	private Set<FsmDirective> directives = new HashSet<FsmDirective>();
	/**
	 * Current multi-level state
	 */
	private Stack<IndividualState> states = new Stack<IndividualState>();
	/**
	 * Keeps some work variables
	 */
	private Map<Class<?>, FsmTransition<Object, Object>> transCache = new HashMap<Class<?>, FsmTransition<Object, Object>>();
	private Map<Class<?>, FsmStateEnhancer<Object>> enhanceCache = new HashMap<Class<?>, FsmStateEnhancer<Object>>();

	private IFsmTimerService timers;

	/**
	 * @param sem
	 * @param argAppCtx 
	 */
	public FsmEngineContext(StateEventMatrix sem, Object argAppCtx) {
		this.sem = sem;
		this.model = argAppCtx;
		this.reset();
		this.timers = new FsmJreTimerService(this);
	}

	protected void reset() {
		this.states.clear();
		this.states.push(new IndividualState(sem, this.sem.getInitialState()));
		this.directives.clear();
	}

	/**
	 * @return the sem
	 */
	public StateEventMatrix getSem() {
		return sem;
	}

	/**
	 * @param object
	 * @return
	 * @see java.util.Set#add(java.lang.Object)
	 */
	public boolean addDirective(FsmDirective object) {
		return directives.add(object);
	}

	/**
	 * 
	 * @see java.util.Set#clear()
	 */
	public void clearDirectives() {
		directives.clear();
	}

	/**
	 * @return
	 * @see java.util.Set#isEmpty()
	 */
	public boolean isDirectivesEmpty() {
		return directives.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.Stack#peek()
	 */
	public IndividualState getCurrentIndividualState() {
		return states.peek();
	}

	/**
	 * @return
	 * @see java.util.Stack#peek()
	 */
	public StateEventMatrix getCurrentSem() {		
		return getCurrentIndividualState().getSem();
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public FsmTransition<Object, Object> getTransition(Object key) {
		return transCache.get(key);
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public void putTransition(Class<?> key, FsmTransition<Object, Object> value) {
		transCache.put(key, value);
	}

	/**
	 * @return the states
	 */
	Stack<IndividualState> getStates() {
		return states;
	}

	IndividualState getTransConfig(Object inputValue) {
		TransConfig tfc = null;
		IndividualState ic = null;
	
		for (int i = states.size() - 1; i >= 0 && tfc == null; i--) {
			ic = states.get(i);
			Enum<?> stateOrdinal = ic.getState();
			StateEventMatrix sem = ic.getSem();
			tfc = sem.getTransConfig(stateOrdinal, inputValue);
		}
		
		if (ic != null && tfc != null) {
			ic.setCurrentTransition(tfc);
		} else {
			return null;
		}
		
		return ic;
	}

	void setCurrentExitState(Enum<?> exitState) {
		getCurrentIndividualState().setExitState(exitState);
	}

	boolean isStateAlreadyMoved() {
		return !isDirectivesEmpty() 
			&& directivesContain(FsmDirective.STATE_ALREADY_CHANGED);
	}

	/**
	 * @param directive
	 * @return
	 */
	public boolean directivesContain(FsmDirective directive) {
		return directives.contains(directive);
	}

	public void setCurrentState(Enum<?> newState) {
		this.getStates().peek().setState(newState);		
	}

	@Override
	public void changeState(Enum<?> newState) {
		addDirective(FsmDirective.STATE_ALREADY_CHANGED);
		setCurrentState(newState);
	}

	@Override
	public void fillInStates(StringBuilder sb) {
		final String SEPARATOR = ", ";
		for (IndividualState ic : states) {
			sb.append(ic.getSem().getName()).append(":").append(ic.getState()).append(SEPARATOR);
		}
		sb.setLength(sb.length() - SEPARATOR.length());
	}
	
	/**
	 * @param nextState
	 */
	void advanceState(Enum<?> nextState) {
		IndividualState prevState = this.getStates().peek();
		prevState.setState(nextState);
	}

	@Override
	public Enum<?> getCurrentState() {
		return getCurrentIndividualState().getState();
	}

	/**
	 * @return the timers
	 */
	public IFsmTimerService getTimers() {
		return timers;
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public FsmStateEnhancer<Object> getEnhancerClass(Class<?> key) {
		return enhanceCache.get(key);
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public FsmStateEnhancer<Object> putEnhancerClass(Class<?> key, FsmStateEnhancer<Object> value) {
		return enhanceCache.put(key, value);
	}


}
