package com.honda.galc.client.datacollection.fsm;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.datacollection.state.State;

public class FSMInvocationHandler implements InvocationHandler {
    
	protected List<Class<? extends State>> stateClasses;    
    protected Class<? extends State> initialStateClass;    
    protected Class<? extends State> finalStateClass;    
    protected Map<Class<? extends State>, Object> stateObjects = new Hashtable<Class<? extends State>, Object>();    
    protected Object currentState;    
	protected FSMContext context;
	
    public FSMInvocationHandler(List<Class<? extends State>> stateClasses, Class<? extends State> initialStateClass,
            Class<? extends State> finalStateClass) {        
    	this.stateClasses = stateClasses;        
    	this.initialStateClass = initialStateClass;        
    	this.finalStateClass = finalStateClass;        
    	initializeStateObjects();        
    	setInitialState();    
    }

    public FSMInvocationHandler() {
		super();
	}

	protected void initializeStateObjects() {        
    	for (Class<? extends State> stateClass : stateClasses) 
    	{            
    		try {               
    			State stateObject = stateClass.newInstance();
    			stateObject.setApplicationId(context.getApplicationId());
    			stateObjects.put(stateClass, stateObject);
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
    	currentState = stateObjects.get(initialStateClass);    
    }
	
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
	{

		if ("getCurrentState".equals(method.getName()) && (method.getParameterTypes().length == 0)) 
		{            
			return currentState.getClass().getSimpleName();        
		}        
		Method stateMethod = lookupCurrentStateMethod(method, args);
		Object methodReturn = stateMethod.invoke(currentState, args);
		Transition transition = stateMethod.getAnnotation(Transition.class);
		if (transition != null) {
			Class<?> nextStateClass = transition.state();
			currentState = stateObjects.get(nextStateClass);        
		}        
		return methodReturn;    
	}
	
    protected Method lookupCurrentStateMethod(Method method, Object[] args) 
    {        
    	return lookUpMethod(currentState.getClass(), method);    
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

	public Class<? extends State> getFinalStateClass() {
		return finalStateClass;
	}

	public void setFinalStateClass(Class<? extends State> finalStateClass) {
		this.finalStateClass = finalStateClass;
	}
}


