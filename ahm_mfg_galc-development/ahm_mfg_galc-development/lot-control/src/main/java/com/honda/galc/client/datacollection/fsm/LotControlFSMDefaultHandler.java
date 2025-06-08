package com.honda.galc.client.datacollection.fsm;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.datacollection.state.Action;
import com.honda.galc.client.datacollection.state.State;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.Fsm;
import com.honda.galc.common.logging.Logger;
/**
 * 
 * <h3>LotControlFSMDefaultHandler</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlFSMDefaultHandler description </p>
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
 * @author Paul Chou
 * Jun 15, 2010
 *
 */
public class LotControlFSMDefaultHandler extends LotControlFSMInvocationBaseHandler {

	Class<?> interfaceClass;
	
	public LotControlFSMDefaultHandler(List<Class<? extends State>> stateClasses,
			Class<? extends State> initialStateClass, Class<? extends State> finalStateClass, FSMContext context) {
		super(stateClasses, initialStateClass, finalStateClass, context);
		Logger.getLogger().debug("FSM:" + this.getClass().getSimpleName()+ " type:" + context.getProperty().getFsmType());
	}
	
	

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		interfaceClass = null;
		return super.invoke(proxy, method, args);
	}



	@Override
	protected boolean notifyStateChange(Method annotatedMethod) throws Throwable {
		Notification notification = annotatedMethod.getAnnotation(Notification.class);
		if (notification != null) {

			processPreAction(notification); 
			
			notifyStateChange(notification);

			processPostAction(notification); 
		}
		
		if(((DataCollectionState)context.getState()).getExceptionList().size() > 0)
		{
			((DataCollectionState)context.getState()).getExceptionList().clear();
			Logger.getLogger().error("Exception thrown in notification.");
			return false;
		}
		
		return true;
	}


	private void processPreAction(Notification notification) 
	throws NoSuchMethodException, IllegalAccessException,
	InvocationTargetException {
		if(!NA.equalsIgnoreCase(notification.preAction())){
			invokeMethod(notification.preAction());
		}
	}
	
	protected void notifyStateChange(Notification notification)
	throws NoSuchMethodException, Throwable, IllegalAccessException,InvocationTargetException {
		if(checkCondition(notification.condition())){
			long startTimeMillis = System.currentTimeMillis();
			notifyObservers(notification);
			Logger.getLogger().debug("performance report:" + context.getState().getClass().getSimpleName() + "-" + notification.action() + ":" + (System.currentTimeMillis() - startTimeMillis));
		} 
	}

	private void processPostAction(Notification notification)
	throws Throwable {
		if(!NA.equalsIgnoreCase(notification.postAction())){
			invokeMethodAndNotification(notification.postAction());
		}
	}

	@SuppressWarnings("unchecked")
	private Object invokeMethodAndNotification(String method) 
	throws Throwable {
		Class[] parameterTypes = {};
		Object[] parameters = {};
		Method stateMethod = context.getState().getClass().getMethod(method, parameterTypes);
		Object methodResult = stateMethod.invoke(context.getState(), parameters);
		
		notifyStateChange(lookupAnnotatedMethod(stateMethod, stateMethod.getParameterTypes()));
		return methodResult;
	}


	@SuppressWarnings("unchecked")
	private void notifyObservers(Notification notification)
	throws NoSuchMethodException, Throwable {
		Class[] parameterTypes = {Action.class};
		Object[] parameters = {notification.action()};

		Method notifyMethod = context.getState().getClass().getMethod("stateChanged", parameterTypes);
		this.invoke(context.getState(), notifyMethod, parameters);
	}

	@Override
	protected void transitState(Method annotatedMethod) throws Throwable,
	NoSuchMethodException, IllegalAccessException,
	InvocationTargetException {
		List<Transition> transitions = findTransitionAnnotation(annotatedMethod);
		processTransitions(transitions);
	}

	private void processTransitions(List<Transition> transitions) 
	throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, Throwable {
		
		if(transitions == null) return;
		
		//evaluate tx in the list, until a tx is complete
		for(Transition tx: transitions){
			if(transitState(tx)) return; 
		}
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

	private boolean transitState(Transition transition) throws Throwable,
			NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		if(checkCondition(transition.condition())){
			Logger.getLogger().debug("transitNextState:" + transition.state());
			transitNextState(transition);
			return true;
		}
		return false;
	}

	protected void transitNextState(Transition transition) throws Throwable {
		transit(transition.state(), transition.init());
	}

	@Override
	protected Annotation[] getAnnotations(Method stateMethod) {
		return getInterface().getAnnotations();
	}

	private Class<?> getInterface() {
		return interfaceClass == null ? findInterface() : interfaceClass;
	}

	private Class<?> findInterface() {
		for(Class<?> intf : context.getState().getClass().getInterfaces())
		{
			FsmType type = FsmType.valueOf(context.getProperty().getFsmType());
			if(intf.getAnnotation(Fsm.class) != null && intf.getAnnotation(Fsm.class).type() == type) {
				return intf;
			}
		}
		return null;
	}

	@Override
	protected Method lookupAnnotatedMethod(Method method, Object[] args) {
		return super.lookUpMethod(getInterface(),method);
	}

}
