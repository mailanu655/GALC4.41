package com.honda.galc.client.datacollection.fsm;

import java.lang.reflect.InvocationHandler;
import java.util.List;

import com.honda.galc.client.datacollection.state.State;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;


public class LotControlFSMBuilder extends FSMBuilder{
	FSMContext context;

	public LotControlFSMBuilder(FSMContext context) {
		super();
		this.context = context;
	}

	@Override
	protected InvocationHandler getInvocationHandler(
			List<Class<? extends State>> stateClasses, Class<? extends State> initialStateClass, Class<? extends State> finalStateClass) {
		if(context.getProperty() != null)
		{
			return new LotControlFSMDefaultHandler(stateClasses, initialStateClass, finalStateClass, context);
		}
		else
		{
			Logger.getLogger().error(this.getClass().getSimpleName() + " Faild to retrieve FSM properties - property is null.");
			throw new TaskException("Faild to retrieve FSM properties.");
		}
	}

	
}
