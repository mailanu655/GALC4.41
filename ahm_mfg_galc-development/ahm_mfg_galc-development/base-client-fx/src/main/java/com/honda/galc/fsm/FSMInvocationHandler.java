package com.honda.galc.fsm;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


import com.honda.galc.common.logging.Logger;

/**
 * 
 * <h3>FSMInvocationHandler Class description</h3>
 * <p> FSMInvocationHandler description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Feb 24, 2014
 *
 *
 */
@SuppressWarnings("unchecked")
public class FSMInvocationHandler implements InvocationHandler {
    
	protected List<Class<? extends IState>> stateClasses;    
    protected Class<? extends IState> initialStateClass;    
    protected Class<? extends IState> finalStateClass;    
   protected Map<Class<? extends IState>, Object> stateObjects = new Hashtable<Class<? extends IState>, Object>();    
    protected IState workState;
    protected FxFsmContext context;
    
    public FSMInvocationHandler(List<Class<? extends IState>> stateClasses, Class<? extends IState> initialStateClass,
            Class<? extends IState> finalStateClass,FxFsmContext<?> context) {
    	this.context = context;
    	this.stateClasses = stateClasses;        
    	this.initialStateClass = initialStateClass;        
    	this.finalStateClass = finalStateClass;        
    	initializeStateObjects();        
    	setInitialState();     
    }
    
    public FSMInvocationHandler(List<Class<? extends IState>> stateClasses, Class<? extends IState> initialStateClass,
            Class<? extends IState> finalStateClass) {        
    	this(stateClasses,initialStateClass,finalStateClass,null);    
    }

    public FSMInvocationHandler() {
		super();
	}

	protected void initializeStateObjects() {        
    	for (Class<? extends IState> stateClass : stateClasses) 
    	{            
    		try {               
    			IState stateObject = stateClass.newInstance();
    			stateObject.setContext(context.getModel());
     			stateObjects.put(stateClass, stateObject);
     			getLogger().info("create state : " + stateObject.getName());
    		} catch (InstantiationException ex) {                
    			throw new RuntimeException("State " + stateClass.getName() +
    					" can't be instanciated: " + ex.getMessage(), ex);            
    		} catch (IllegalAccessException ex) {                
    			throw new RuntimeException("State " + stateClass.getName() +
    					" has no accessible default constructor: " + ex.getMessage(), ex); 
    		}
    	}
    }        
    
    protected void setInitialState() {
    	context.setCurrentState((IState)stateObjects.get(initialStateClass));   
    	getLogger().info("set the initial state : " + context.getCurrentState().getName());
    }
    
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
	{
    	workState = stateObjects.containsValue(proxy) ? (IState) proxy : context.getCurrentState();  
    	
		Method stateMethod = lookupStateMethod(method, args);
		Object methodReturn = null;
		if(stateMethod != null) methodReturn = stateMethod.invoke(workState, args);
		processAnnotation(lookupAnnotatedMethod(method, args));
		return methodReturn;    
	}
    
    private void processAnnotation(Method annotatedMethod) throws Throwable {
		
		if(annotatedMethod == null || getAnnotations(annotatedMethod) == null)
			return;
		
		Logger.getLogger().debug(getClass().getName()+" notifyStateChange:" + annotatedMethod.getName());
		//Don't transit state if exception thrown during notification, same behavior as today
		//if(!notifyStateChange(annotatedMethod))
		//	return;
		
		//Error shown on client and logged, continue to process even there were exceptions
		processNotification(annotatedMethod);
		getLogger().debug(getClass().getClass().getSimpleName() +" transitState:" + annotatedMethod.getName());
		transitState(annotatedMethod);
		context.setCurrentState(workState);
	}
    
    protected Annotation[] getAnnotations(Method stateMethod) {
		return stateMethod.getAnnotations();
	}
    
    protected void transitState(Method annotatedMethod) throws Throwable,
	NoSuchMethodException, IllegalAccessException,
	InvocationTargetException {
		List<Transition> transitions = findTransitionAnnotation(annotatedMethod);
		processTransitions(transitions);
	}
    
    private List<Transition> findTransitionAnnotation(Method annotatedMethod) {

		List<Transition> txs;

		//find qualified single Transition first
		txs = findQualifiedTransition(annotatedMethod);
		if(txs != null) return txs;

		//Try to find qualified transitions
		Transitions transitions = annotatedMethod.getAnnotation(Transitions.class);
		if(transitions == null) return null;

		//if multiple Transitions defined, then find the required type.
		txs = convertTransitionsToList(transitions);
		
		return txs;
	}
    
    private List<Transition> findQualifiedTransition(Method annotatedMethod) {
		Transition transition = annotatedMethod.getAnnotation(Transition.class);
		List<Transition> txs;
		if(transition != null){
        	txs = new ArrayList<Transition>();
        	txs.add(transition);
        	return txs;
        }
		return null;
	}
    
    private List<Transition> convertTransitionsToList(Transitions transitions) {
		List<Transition> txs = new ArrayList<Transition>();
		for(int i = 0; i < transitions.value().length; i++){
				txs.add((transitions.value())[i]);
		}
		return txs;
	}
    
    private void processTransitions(List<Transition> transitions) 
	throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, Throwable {
		
		if(transitions == null) return;
		
		//evaluate tx in the list, until a tx is complete
		for(Transition tx: transitions){
			if(transitState(tx)) return; 
		}
	}
    
    private boolean transitState(Transition transition) throws Throwable,
	NoSuchMethodException, IllegalAccessException,
	InvocationTargetException {
    	if(checkCondition(transition.condition())){
    		getLogger().debug("transitNextState:" + transition.newState());
    		transitNextState(transition);
    		return true;
    	}
    	return false;
    }
    
    protected void transitNextState(Transition transition) throws Throwable {
		transit(transition.newState(), transition.init());
	}
    
	protected void transit(Class<?> nextStateClass, String initMethodName) 
		throws NoSuchMethodException, Throwable {
		//Do nothing if next state is not defined
		if(nextStateClass == Annotation.class) return;

		IState state = (IState)(stateObjects.get(nextStateClass));
		Class[] parameterTypes = {};
		Object[] parameters = {};

		Method initMethod = state.getClass().getMethod(initMethodName, parameterTypes);
		this.invoke(state, initMethod, parameters);

	}
	
    protected Method lookupStateMethod(Method method, Object[] args) 
    {        
    	return lookUpMethod(workState.getClass(), method);    
    }

	protected Method lookUpMethod(Class<?> clz, Method method) {
		try {
			if(clz == null) 
				return null;
			else
				return clz.getMethod(method.getName(), method.getParameterTypes());        
		} catch (NoSuchMethodException ex) {
			//OK, the method may not defined
			return null;
    	}
	}

	public Class<? extends IState> getFinalStateClass() {
		return finalStateClass;
	}

	public void setFinalStateClass(Class<? extends IState> finalStateClass) {
		this.finalStateClass = finalStateClass;
	}
	
	protected Method lookupAnnotatedMethod(Method method, Object[] args) {
		return lookUpMethod(findInterface(workState),method);
	}
	
	protected boolean processNotification(Method annotatedMethod) throws Throwable {
		Action action = annotatedMethod.getAnnotation(Action.class);
		if (action != null) {

			processPreAction(action); 
			
			notifyStateChange(action);

			processPostAction(action); 
		}
		return true;
	}
	
	private void processPreAction(Action notification) 
		throws NoSuchMethodException, IllegalAccessException,InvocationTargetException {
		if(!StringUtils.isEmpty(notification.preAction())){
			invokeMethod(notification.preAction());
		}
	}
	
	protected void notifyStateChange(Action action)
		throws NoSuchMethodException, Throwable, IllegalAccessException,InvocationTargetException {
		if(checkCondition(action.condition())){
			long startTimeMillis = System.currentTimeMillis();
			notifyObservers(action);
			getLogger().debug("performance report:" + workState.getClass().getSimpleName() + "-" + action.action() + ":" + (System.currentTimeMillis() - startTimeMillis));
		} 
	}
	
	private void processPostAction(Action notification)
	throws Throwable {
		if(!StringUtils.isEmpty(notification.postAction())){
			invokeMethodAndNotification(notification.postAction());
		}
	}
	
	
	private Object invokeMethodAndNotification(String method) 
		throws Throwable {
		Class[] parameterTypes = {};
		Object[] parameters = {};
		Method stateMethod = workState.getClass().getMethod(method, parameterTypes);
		Object methodResult = stateMethod.invoke(workState, parameters);
		// recursively invoke the notifications
		processNotification(lookupAnnotatedMethod(stateMethod, stateMethod.getParameterTypes()));
		return methodResult;
	}
	
	protected Object invokeMethod(String methodName)
			throws NoSuchMethodException, IllegalAccessException,InvocationTargetException {
		Class[] parameterTypes = {};
		Object[] parameters = {};
		Method conditionMethod = workState.getClass().getMethod(methodName, parameterTypes);
		Object conditionResult = conditionMethod.invoke(workState, parameters);
		return conditionResult;
	}
	
	protected Boolean checkCondition(String conditionMethodName)
	throws NoSuchMethodException, IllegalAccessException,
	InvocationTargetException {
		if(StringUtils.isEmpty(conditionMethodName)) return true;

		Object conditionResult = invokeMethod(conditionMethodName);
		Logger.getLogger().debug("Checking condition " + workState.getClass().getSimpleName() + "." + conditionMethodName + "(): " + " result=" + (Boolean)conditionResult);
		return (Boolean)conditionResult;
	}
	
	private Class<?> findInterface(IState state) {
		for(Class<?> intf : state.getClass().getInterfaces()){
			if(intf.getAnnotation(Fsm.class) != null && intf.getAnnotation(Fsm.class).type() == context.getFsmType()) {
				return intf;
			}
		}
		return null;
	}
	
	private void notifyObservers(Action action) throws NoSuchMethodException, Throwable {
		List<IObserver> observers = context.getObservers();
		for(IObserver observer : observers) {
			observer.publish(workState, action.action());
		}
	}
	
	private Logger getLogger() {
		return context.getLogger();
	}

}


