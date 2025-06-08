package com.honda.galc.fsm;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * <h3>FSMBuilder Class description</h3>
 * <p> FSMBuilder description </p>
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
public class FSMBuilder {
	
	
	public static <T> T buildFSM(Class<T> classToken,FxFsmContext<?> context) {
		return buildFSM(classToken,context,new ArrayList<Class<?>>());
	}	
	
	public static <T> T buildFSM(Class<T> classToken,FxFsmContext<?> context, List<Class<?>> ctrClasses) {   
		return buildFSM(classToken, ctrClasses, 
				extractStateClasses(classToken),
				extractInitialState(classToken),
				extractFinalState(classToken),context); 
	}
	
	private static List<Class<? extends IState>> extractStateClasses(Class<?> classToken) 
	{        
		FiniteStateMachine fsm = classToken.getAnnotation(FiniteStateMachine.class); 
		return Arrays.asList(fsm.states());    
	}    
	private static Class<? extends IState> extractInitialState(Class<?> classToken) 
	{        
		FiniteStateMachine fsm = classToken.getAnnotation(FiniteStateMachine.class);
		return fsm.initialState();    
	}    
	private static Class<? extends IState> extractFinalState(Class<?> classToken) 
	{   FiniteStateMachine fsm = classToken.getAnnotation(FiniteStateMachine.class);        
		return fsm.finalState();    
	} 
	
	protected static <T> T buildFSM(Class<T> fsmClassToken, List<Class<?>> ctrClasses, 
			List<Class<? extends IState>> stateClasses, Class<? extends IState> initialStateClass, 
			Class<? extends IState> finalStateClass,FxFsmContext<?>  context) 
	{        
		
		ctrClasses.add(fsmClassToken);
		Class<?>[] interfaces = new Class<?>[ctrClasses.size()];
		ctrClasses.toArray(interfaces);
		
        //Suppressed unchecked warning        
		// The proxy will be of class T as the classToken is passed as one of the        
		// interfaces to be implemented by the proxy.
		return (T) Proxy.newProxyInstance(fsmClassToken.getClassLoader(), interfaces, 
				getInvocationHandler(stateClasses, initialStateClass, finalStateClass,context));    
	}
	
	protected static InvocationHandler getInvocationHandler(List<Class<? extends IState>> stateClasses, 
			Class<? extends IState> initialStateClass, Class<? extends IState> finalStateClass
			,FxFsmContext<?> context) 
	{
		return new FSMInvocationHandler(stateClasses, initialStateClass, finalStateClass,context);
	}    

}
