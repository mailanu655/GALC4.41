package com.honda.galc.fsm;

import java.util.List;

import com.honda.galc.common.logging.Logger;

/**
 * 
 * <h3>FsmContext Class description</h3>
 * <p> FsmContext description </p>
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
public class FxFsmContext<M> {
	
	private FsmType fsmType;
	
	private M model;
	
	private Logger logger;
	
	private List<IObserver> observers;
	
	private IState<?> currentState = null;
	
	public FxFsmContext(FsmType fsmType,M model,List<IObserver> observers,Logger logger) {
		this.fsmType = fsmType;
		this.model = model;
		this.observers = observers;
		this.logger = logger;
	}

	public FsmType getFsmType() {
		return fsmType;
	}

	public void setFsmType(FsmType fsmType) {
		this.fsmType = fsmType;
	}

	public M getModel() {
		return model;
	}

	public void setModel(M model) {
		this.model = model;
	}

	public List<IObserver> getObservers() {
		return observers;
	}

	public void setObservers(List<IObserver> observers) {
		this.observers = observers;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public IState<?> getCurrentState() {
		return currentState;
	}

	public void setCurrentState(IState<?> currentState) {
		this.currentState = currentState;
	}
	
	
}
