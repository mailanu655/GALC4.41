package com.honda.galc.client.datacollection.observer;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.honda.galc.client.common.Observable;
import com.honda.galc.client.common.exception.LotControlTaskException;
import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessRefresh;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.common.logging.Logger;
/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>DataCollectionObserverInvoker</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Paul Chou</TD>
 * <TD>Sep 11, 2009</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Paul Chou
 */
public class DataCollectionObserverInvoker{
	private static HashMap<Class<? extends DataCollectionState>, Class<? extends Annotation>> annotationList;
	static {registerAnnotations();};
	enum InvokeType{Invoke, InvokeAndWait}
	private synchronized static void registerAnnotations(){
		if (annotationList == null) {
			annotationList = new HashMap<Class<? extends DataCollectionState>, Class<? extends Annotation>>();
			annotationList.put(ProcessProduct.class, ProcessProductState.class);
			annotationList.put(ProcessPart.class, ProcessPartState.class);
			annotationList.put(ProcessTorque.class, ProcessTorqueState.class);
			annotationList.put(ProcessRefresh.class, ProcessRefreshState.class);
		}		
	}
	
	@SuppressWarnings("unchecked")
	private static Class<? extends Annotation> getAnnotationClass(Class clz){
		return annotationList.get(clz);
	}

	public static void invoke(Object observer, Observable o, Object arg) {
		invoke(observer, arg, InvokeType.Invoke);
	}

	
	public static void invokeAndWait(Object observer, Observable o, Object arg) {
		invoke(observer, arg, InvokeType.InvokeAndWait);		
	}

	@SuppressWarnings("unchecked")
	private static void invoke(Object observer, Object arg, InvokeType invokeType) {
		try {
			List<Class<?>> interfaces = findInterfaces(observer);
			for (Class interface1 : interfaces) {
				Logger.getLogger().debug("DataCollectionObserverInvoker: interfaces list size="+interfaces.size());
				Annotation ifAnnotation = interface1.getAnnotation(ObserverInterface.class);
				if (ifAnnotation != null) {
					Logger.getLogger().debug("DataCollectionObserverInvoker: annotation="+ifAnnotation.getClass().getName());
					Method[] methods = interface1.getMethods();
					for (int j = 0; j < methods.length; j++) {

						final Method method = methods[j];
						//Logger.getLogger().debug("DataCollectionObserverInvoker: class="+method.getClass().getName()+" method="+method.getName());
						Class annotationClass = getAnnotationClass(arg.getClass());
						Annotation annotation = method.getAnnotation(annotationClass);
				
						if (annotation != null && isInvoke(annotation, arg)) {
							Logger.getLogger().debug("DataCollectionObserverInvoker: methodannotation="+annotation.getClass().getName());
							Logger.getLogger().debug("DataCollectionObserverInvoker: isInvoke="+isInvoke(annotation, arg));
							Logger.getLogger().debug("invoke method:" + method.getName());
							if (invokeType == InvokeType.InvokeAndWait){
								Logger.getLogger().debug("invokeAndWait: observer="+observer.getClass().getName()+" arg="+arg+ " method="+method.getName());
								invokeAndWait(observer, arg, method);
							}
							else if (invokeType == InvokeType.Invoke){
								Logger.getLogger().debug("invoke: observer="+observer.getClass().getName()+" arg="+arg+ " method="+method.getName());
								invoke(observer, arg, method);
							}
							Logger.getLogger().debug("finished invoking method:" + method.getName());

						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Observer: " + observer.getClass().getSimpleName() + " arg:" + arg.getClass().getSimpleName() + " action:" + ((DataCollectionState)arg).getAction());
			((DataCollectionState)arg).exception(new LotControlTaskException(e.getCause() != null ? e.getCause().toString() : e.toString(),
					"DataCollectionObserverInvoker"), false);
			Logger.getLogger().error(e, "DataCollectionObserverInvoker::invoke() exception.");
		}

	}

	private static void invoke(Object observer, Object arg, Method method) 
	throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			Method invokingMethod = lookupInvocationMethod(observer, method, method.getParameterTypes());
			invokingMethod.invoke(observer, arg);
	}

	private static void invokeAndWait(final Object observer, final Object farg, final Method method) 
	throws InterruptedException, InvocationTargetException
	{
		Runnable r = new Runnable() {
			public void run() {
				try {
					Logger.getLogger().debug("lookupInvocationMethod: observer="+observer.getClass().getName()+" method="+method+" parameterTypes="+method.getParameterTypes());
					Method invokingMethod = lookupInvocationMethod(observer, method, method.getParameterTypes());
					Logger.getLogger().debug("invokingMethod="+invokingMethod.getName());
					invokingMethod.invoke(observer, farg);
					Logger.getLogger().debug("invokingMethod returned");
				} catch (Exception e) {
					Logger.getLogger().error(e, "DataCollectionObserverInvoker::invokeAndWait() exception.");
				}
			}
		};

		Logger.getLogger().debug("before swing invokeAndWait EDT="+javax.swing.SwingUtilities.isEventDispatchThread());
		javax.swing.SwingUtilities.invokeAndWait(r);
		Logger.getLogger().debug("after swing invokeAndWait");
	}


	private static List<Class<?>> findInterfaces(Object observer) {
		List<Class<?>> interfaceList = new ArrayList<Class<?>>();
		interfaceList.addAll(Arrays.asList((Class<?>[])observer.getClass().getInterfaces()));
		findInterfaces(observer.getClass().getSuperclass(), interfaceList);
		return interfaceList;
	}

	private static void findInterfaces(Class<?> superclass,	List<Class<?>> interfaceList) {
		if(superclass != Object.class){
			interfaceList.addAll(Arrays.asList((Class<?>[])superclass.getInterfaces()));
			findInterfaces(superclass.getSuperclass(), interfaceList);
		}
	}

	/**
	 * Look the method from the observer class 
	 * @param observer
	 * @param method
	 * @param parameterTypes
	 * @return
	 */
	private static Method lookupInvocationMethod(Object observer, Method method, Class<?>[] parameterTypes) {
		try {            
			return observer.getClass().getMethod(method.getName(), method.getParameterTypes());        
		} catch (NoSuchMethodException ex) {
			throw new RuntimeException("observer " + observer.getClass().getName()
					+ "' doesn't allow '" + method.getName() + "'");        
		}    
		
	}

	/**
	 * The method is invokable only if both annotation state and action are equal to
	 * current state and action.
	 * @param annotation
	 * @param arg
	 * @return
	 */
	private static boolean isInvoke(Annotation annotation, Object arg) {
		
		Action action = ((DataCollectionState)arg).getAction();
		Action[] actions = null;
		if(annotation.annotationType() == ProcessProductState.class) {
			actions = ((ProcessProductState)annotation).actions();
		} else if(annotation.annotationType() == ProcessPartState.class) {
			actions = ((ProcessPartState)annotation).actions();
		} else if(annotation.annotationType() == ProcessTorqueState.class) {
			actions = ((ProcessTorqueState)annotation).actions();
		} else if(annotation.annotationType() == ProcessRefreshState.class) {
			actions = ((ProcessRefreshState)annotation).actions();
		}
		
		return isOnAction(action, actions);
	}

	/**
	 * Check if the broadcasting action is in the method's invoke action list
	 * @param action
	 * @param actions
	 * @return
	 */
	private static boolean isOnAction(Action action, Action[] actions) {
		if(actions == null) {
			Logger.getLogger().warn("Actions is null.");
			return false;
		}
		
		for(int j = 0; j < actions.length; j++)
		{
			if(action == actions[j])
				return true;
		}
		
		return false;
	}

}
