package com.honda.galc.client.datacollection.fsm;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.honda.galc.client.common.exception.LotControlTaskException;
import com.honda.galc.client.datacollection.state.State;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
/**
 * 
 * <h3>LotControlFSMInvocationBaseHandler</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlFSMInvocationBaseHandler description </p>
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
public class LotControlFSMInvocationBaseHandler extends FSMInvocationHandler{
	
	public static final String NA = "N/A";
	public static final String INIT = "init";
	public static final String COMPLETE = "complete";

	public LotControlFSMInvocationBaseHandler(List<Class<? extends State>> stateClasses,
			Class<? extends State> initialStateClass, Class<? extends State> finalStateClass, FSMContext context) {
		this.context = context;
		this.stateClasses = stateClasses;        
    	this.initialStateClass = initialStateClass;        
    	this.finalStateClass = finalStateClass;        
    	initializeStateObjects();        
    	setInitialState();    
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws TaskException, Throwable 
	{
		
		try{
			Method stateMethod = lookupCurrentStateMethod(method, args);
			Object methodReturn = stateMethod.invoke(context.getState(), args);
			processAnnotation(lookupAnnotatedMethod(method, args));

			return methodReturn;   
		}catch (Exception e){
			if(proxy instanceof DataCollectionState){
				DataCollectionState state = (DataCollectionState) proxy;
				Logger.getLogger().error(e, "currentState:" + state.getClass().getSimpleName() 
						+ " partIndex:" + state.getCurrentPartIndex() + " : " + state.getCurrentPartName() 
						+ " torqueIndex: " + state.getCurrentTorqueIndex()
						+ " scanPart:" +  (state.getCurrentPartIndex()> 0? state.isScanPartSerialNumber() : false));
				state.exception(new LotControlTaskException(e.getMessage(), this.getClass().getSimpleName()), true);
			} else {
				Logger.getLogger().error(e, this.getClass().getSimpleName());
				
			}
			if (e instanceof InvocationTargetException) 
				throw e.getCause(); else throw e;
			
		}catch(Throwable t){
			Logger.getLogger().error(t, this.getClass().getSimpleName());
			if(proxy instanceof DataCollectionState){
				DataCollectionState state = (DataCollectionState) proxy;
				state.exception(new LotControlTaskException(t.getMessage(), this.getClass().getSimpleName()), true);
			}
			throw t;
		}
	}

	protected Method lookupAnnotatedMethod(Method method, Object[] args) {
		return lookupCurrentStateMethod(method, args);
	}

	protected Annotation[] getAnnotations(Method stateMethod) {
		return stateMethod.getAnnotations();
	}

	private void processAnnotation(Method annotatedMethod) throws Throwable {
		
		if(annotatedMethod == null || getAnnotations(annotatedMethod) == null)
			return;
		
		Logger.getLogger().debug(getClass().getName()+" notifyStateChange:" + annotatedMethod.getName());
		//Don't transit state if exception thrown during notification, same behavior as today
		//if(!notifyStateChange(annotatedMethod))
		//	return;
		
		//Error shown on client and logged, continue to process even there were exceptions
		notifyStateChange(annotatedMethod);
		Logger.getLogger().debug(getClass().getClass().getSimpleName() +" transitState:" + annotatedMethod.getName());
		transitState(annotatedMethod);
	}

	protected boolean notifyStateChange(Method annotatedMethod) 
	throws Throwable{return true;};
	
	protected void transitState(Method annotatedMethod) 
	throws Throwable{};
	
	
	@SuppressWarnings("unchecked")
	protected void transit(Class<?> nextStateClass, String initMethodName) 
	throws NoSuchMethodException, Throwable {
		//Do nothing if next state is not defined
		if(nextStateClass == Annotation.class) return;

		context.setState(stateObjects.get(nextStateClass));
		Class[] parameterTypes = {};
		Object[] parameters = {};

		Method initMethod = context.getState().getClass().getMethod(initMethodName, parameterTypes);
		this.invoke(context.getState(), initMethod, parameters);

	}
	
	protected Boolean checkCondition(String conditionMethodName)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		if(NA.equalsIgnoreCase(conditionMethodName)) return true;
		
		Object conditionResult = invokeMethod(conditionMethodName);
		Logger.getLogger().debug("Checking condition " + context.getState().getClass().getSimpleName() + "." + conditionMethodName + "(): " + " result=" + (Boolean)conditionResult);
		return (Boolean)conditionResult;
	}

	@SuppressWarnings("unchecked")
	protected Object invokeMethod(String methodName)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		Class[] parameterTypes = {};
		Object[] parameters = {};
		Method conditionMethod = context.getState().getClass().getMethod(methodName, parameterTypes);
		Object conditionResult = conditionMethod.invoke(context.getState(), parameters);
		return conditionResult;
	}

	protected void setInitialState() {
		context.setState(stateObjects.get(initialStateClass)); 
	}

	protected Method lookupCurrentStateMethod(Method method, Object[] args) 
	{        
		return lookUpMethod(context.getState().getClass(), method);    
	}
}
