package com.honda.galc.fsm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.apache.commons.lang.ClassUtils;

import javafx.application.Platform;


/**
 * <h3>DataCollectionObserverBase</h3>
 * <h4>
 * Data Collection Observer base class.
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Feb.19, 2014</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Jeffray Huang
 */
public abstract class AbstractObserver implements IObserver {
	
	public void publish(IState<?> state, Class<? extends IActionType> actionType ){
		invoke(state,actionType);
	}

	public void cleanUp(){
		
	}
	
	protected void invoke(IState<?> state, Class<? extends IActionType> actionType){
		List<Method> methods = getValidMethods(state, actionType);
		for(Method method : methods) 
			invokeNow(method,state);
	}
	

	protected void invokeAndWait(IState<?> state, Class<? extends IActionType> actionType){
		List<Method> methods = getValidMethods(state, actionType);
		for(Method method : methods){ 
			try {
				invokeAndWait(method,state);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void invokeNow(Method method, IState<?> state) {
		try {
			method.invoke(this, state);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void invokeAndWait(final Method method, final IState<?> state) throws InterruptedException, ExecutionException {
		this.runAndWait(new Runnable() {

			@Override
			public void run() {
				invokeNow(method,state);
			}
			
		});
	}
	
	

	@SuppressWarnings("unchecked")
	protected List<Method> getValidMethods(IState<?> state, Class<? extends IActionType> actionType) {
		
		List<Method> methods = new ArrayList<Method>();
		List<Class<?>> interfaces = (List<Class<?>>)ClassUtils.getAllInterfaces(this.getClass());
		for(Class<?> intf : interfaces) {
			if(intf.getAnnotation(ObserverInterface.class) == null) continue;
			for(Method method : intf.getMethods()) {
				if(isObserver(method,state,actionType))
						methods.add(method);
			}
		}
		return methods;
	}	
	
	private boolean isObserver(Method method,IState<?> state,Class<? extends IActionType> actionType) {
		if(isObserver(method.getAnnotation(Observer.class),state,actionType)) return true;
	
		Observers observers = method.getAnnotation(Observers.class);
		if(observers != null) {
			for(Observer observer : observers.value()) {
				if(isObserver(observer,state,actionType)) return true;
			}
		}
		return false;
	}
	
	private boolean isObserver(Observer observer, IState<?> state,Class<? extends IActionType> actionType){
		if(observer != null && observer.state().isAssignableFrom(state.getClass())) {
			for(Class<? extends IActionType> type : observer.actions()) {
				if (type == actionType) return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private void runAndWait(Runnable runnable) throws InterruptedException, ExecutionException {
		FutureTask future = new FutureTask(runnable, null);
		Platform.runLater(future);
		future.get();
		}
}
