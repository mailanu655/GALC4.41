package com.honda.galc.client.datacollection.fsm;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

import com.honda.galc.client.datacollection.state.State;

public class FSMBuilder {
	protected FSMBuilder() {    }    
	public <T> T buildFSM(Class<T> classToken, List<Class<?>> ctrClasses) {   
		return buildFSM(classToken, ctrClasses, 
				extractStateClasses(classToken),
				extractInitialState(classToken),
				extractFinalState(classToken)); 
	}
	
	private List<Class<? extends State>> extractStateClasses(Class<?> classToken) 
	{        
		IFiniteStateMachine fsm = classToken.getAnnotation(IFiniteStateMachine.class); 
		return Arrays.asList(fsm.states());    
	}    
	private Class<? extends State> extractInitialState(Class<?> classToken) 
	{        
		IFiniteStateMachine fsm = classToken.getAnnotation(IFiniteStateMachine.class);
		return fsm.initialState();    
	}    
	private Class<? extends State> extractFinalState(Class<?> classToken) 
	{   IFiniteStateMachine fsm = classToken.getAnnotation(IFiniteStateMachine.class);        
		return fsm.finalState();    
	} 
	
	@SuppressWarnings("unchecked")  
	protected <T> T buildFSM(Class<T> fsmClassToken, List<Class<?>> ctrClasses, 
			List<Class<? extends State>> stateClasses, Class<? extends State> initialStateClass, Class<? extends State> finalStateClass) 
	{        
		
		ctrClasses.add(fsmClassToken);
		Class<?>[] interfaces = new Class<?>[ctrClasses.size()];
		ctrClasses.toArray(interfaces);
		
        //Suppressed unchecked warning        
		// The proxy will be of class T as the classToken is passed as one of the        
		// interfaces to be implemented by the proxy.
		return (T) Proxy.newProxyInstance(fsmClassToken.getClassLoader(), interfaces, 
				getInvocationHandler(stateClasses, initialStateClass, finalStateClass));    
	}
	
	protected InvocationHandler getInvocationHandler(List<Class<? extends State>> stateClasses, 
			Class<? extends State> initialStateClass, Class<? extends State> finalStateClass) 
	{
		return new FSMInvocationHandler(stateClasses, initialStateClass, finalStateClass);
	}    

}
