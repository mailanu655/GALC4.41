package com.honda.galc.client.datacollection.fsm;

import com.honda.galc.client.datacollection.property.FSMPropertyBean;

public class FSMContext {
	private FSMPropertyBean property;
	private Object state;
	private String applicationId;
	
	public FSMContext(FSMPropertyBean property, Object state, String applicationId) {
		super();
		this.property = property;
		this.state = state;
		this.applicationId = applicationId;
	}
	
	public FSMContext(FSMPropertyBean property) {
		super();
		this.property = property;
	}


	public FSMPropertyBean getProperty() {
		return property;
	}
	public void setProperty(FSMPropertyBean property) {
		this.property = property;
	}
	public Object getState() {
		return state;
	}
	public void setState(Object state) {
		this.state = state;
	}
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
}
