package com.honda.galc.fsm.engine;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.fsm.FsmContext;
import com.honda.galc.fsm.FsmStateEnhancer;
import com.honda.galc.fsm.FsmTransition;
import com.honda.galc.fsm.StateEventMatrix;
import com.honda.galc.fsm.TransConfig;


public class FsmEngine {
	private static final Logger log = Logger.getLogger();
	
	private FsmEngine() {
		// don't create instances
	}

	/**
	 * Process input and execute transition
	 * 
	 * @param inputValue
	 * @deprecated Use {@link #dispatch(FsmContext,Object)} instead
	 */
	public void dispatch(Object inputValue) {
		dispatch(null, inputValue);
	}

	/**
	 * Process input and execute transition
	 * @param ctx FSM context
	 * @param inputValue
	 */
	public static void dispatch(FsmContext argCtx, Object inputValue) {
		FsmEngineContext ctx = (FsmEngineContext) argCtx;
		checkState(ctx, inputValue);
		
		// Find responsible nested state starting from the top of the stack
		IndividualState ic = ctx.getTransConfig(inputValue);
		
		if(ic == null) {
			// Nobody could handle the input
			log.info((fsmHeader(ctx, inputValue) + "Skipping input: <" + inputValue + "> - no transition found"));
			return;
		}
		
		TransConfig tfc = ic.getCurrentTransition();
		
		Enum<?> nextState = tfc.getNextState();
		StateEventMatrix sem = ic.getSem();
		
		if(nextState != null && sem.getStateTransitions().get(nextState) == null) {
			// Check that the next state is valid
			throw new IllegalStateException("SEM is not configured for next state " + nextState + " !!!"); 
		}
		
		FsmTransition<Object, Object> transObject = getTransition(ctx, tfc);
		
		transObject.setModel(ctx.getModel());
		transObject.setFsmContext(ctx);
		
		log.info((fsmHeader(ctx, inputValue) + "SM executing transition: " + tfc));
		boolean changeState;
		try {
			changeState = transObject.execute(inputValue);
		} catch (ClassCastException e) {
			logError(fsmHeader(ctx, inputValue) + "Cannot execute transition: " + transObject + ": " + e.getMessage(), e);
			return;
		}
		
		// Advance state if:
		//   - transition wanted to change the state (was successful)
		//   - next state is present in transition configuration
		//   - it is not "exit nested state" transition
		if (changeState) {
			if (nextState != null) {
				Enum<?> prevState = ic.getState();
				// Check for entering nested state
				
				CompositeStateConfig config;
				if ((config = sem.getCompositeStateConfigs().get(nextState)) != null) {
					executeStateExit(ctx);
					
					// transition to the nested state
					enterNestedState(ctx, config);
					
					executeStateEnter(ctx);
				} else {
					if (!ctx.isStateAlreadyMoved()) { // only for simple state move we can use jumps
						                              // composite states might require this feature as well
						executeStateExit(ctx);

						ctx.advanceState(nextState);
						log.info(("SM advanced state from " + prevState + " to " + nextState));

						executeStateEnter(ctx);
					} else {
						log.info("State has been moved already from transition");
					}
				}
			} else if(tfc.isToExit()) {
				executeStateExit(ctx);
				
				exitNestedState(ctx);
				
				executeStateEnter(ctx);
			}
		}
		// Reset all directives
		ctx.clearDirectives();
		if (log.isInfoEnabled()) {
			log.info((fsmHeader(ctx, inputValue) + " completed processing"));
		}
	}

	private static void executeStateEnter(FsmEngineContext ctx) {
		// Get current state enhancer
		FsmStateEnhancer<Object> enhancer = getStateEnhancer(ctx);
		
		if (enhancer != null) {
			// execute Enter
			enhancer.enterState();
		}
	}

	private static void executeStateExit(FsmEngineContext ctx) {
		// Get current state enhancer
		FsmStateEnhancer<Object> enhancer = getStateEnhancer(ctx);
		
		if (enhancer != null) {
			// execute Exit		
			enhancer.exitState();
		}
	}
	
	/**
	 * @param ctx
	 * @return
	 */
	private static FsmStateEnhancer<Object> getStateEnhancer(
			FsmEngineContext ctx) {
		// Get current state
		Enum<?> currentState = ctx.getCurrentState();
		
		// Get enhancer
		FsmStateEnhancer<Object> enhancer = getEnhancer(ctx, currentState);
		
		if (enhancer != null) {
			// Initialize enhnancer
			enhancer.setModel(ctx.getModel());
			enhancer.setFsmContext(ctx);
		}
		return enhancer;
	}

	/**
	 * @param ctx
	 * @param inputValue
	 * @return
	 */
	private static String fsmHeader(FsmContext ctx, Object inputValue) {
		StringBuilder sb = new StringBuilder("State:(");
		ctx.fillInStates(sb);
		sb.append("), Input: ").append(inputValue);
		sb.append(": ");	
		return sb.toString();
	}

	private static void exitNestedState(FsmEngineContext ctx) {
		ctx.getStates().pop();
		IndividualState ic = ctx.getStates().peek();
		ic.setState(ic.getExitState());
	}

	private static void logError(String message, Exception e) {
		if (e == null) {
			log.error(message);
		} else {
			log.error(e,message);
		}
		
	}

	/**
	 * @param ctx context
	 * @param nestedState
	 */
	private static void enterNestedState(FsmEngineContext ctx, CompositeStateConfig nestedState) {
		// set exit state for current ind.state
		ctx.setCurrentExitState(nestedState.getExitState());
		ctx.setCurrentState(nestedState.getEntryState());

		// create new individual state and push it to the stack
		IndividualState ic = new IndividualState(nestedState.getNestedSem(), nestedState.getNestedSem().getInitialState());
		ctx.getStates().push(ic);
	}

	private static void checkState(FsmEngineContext ctx, Object inputValue) {
		if(ctx.getCurrentIndividualState() == null) {
			throw new IllegalStateException("State is not initialized !!!");
		}
		StateEventMatrix sem = ctx.getCurrentSem();
		if(sem.getInputs() == null || sem.getInputs().size() <= 0) {
			throw new IllegalStateException("Inputs are not configured !!!");
		}
		
		if(inputValue == null) {
			throw new IllegalArgumentException("Input is null !!!");
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private static FsmTransition<Object, Object> getTransition(FsmEngineContext ctx, TransConfig tc) {
		FsmTransition<Object, Object> transObject = ctx.getTransition(tc.getTransitionClass());
		if(transObject == null) {
			try {
				transObject = (FsmTransition<Object, Object>) tc.getTransitionClass().newInstance();
			} catch (Exception e) {
				String message = "Could not instantiate: " + tc.getTransitionClass().getName();
				logError(message + ": ", e);
				throw new IllegalStateException(message, e);
			}
			ctx.putTransition(tc.getTransitionClass(), transObject);
		}
		return transObject;
	}

	/**
	 * @param ctx
	 * @param currentState
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static FsmStateEnhancer<Object> getEnhancer(FsmEngineContext ctx,
			Enum<?> currentState) {
		
		StateEventMatrix sem = ctx.getCurrentSem();
		
		Class<? extends FsmStateEnhancer<?>> enhancerClass = sem.getEnhancerClass(currentState);
		
		// skip if there is no enhancer
		if(enhancerClass == null) {
			return null;
		}
		
		FsmStateEnhancer<Object> enhancer = ctx.getEnhancerClass(enhancerClass);
		
		if (enhancer == null) {
			try {
				enhancer = (FsmStateEnhancer<Object>) enhancerClass.newInstance();
				ctx.putEnhancerClass(enhancerClass, enhancer);
			} catch (Exception e) {
				String message = "Could not instantiate: "
						+ enhancerClass.getName();
				logError(message + ": ", e);
				throw new IllegalStateException(message, e);
			}
		}
		return enhancer;
	}


}
