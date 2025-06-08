package com.honda.galc.client.schedule;

import java.util.HashMap;
import java.util.Map;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.ui.IEvent;

/**
 * <h3>Class description</h3>
 * ProductEvent Class Description
 * Events used by the client. 
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Shweta Kadav</TD>
 * <TD>Oct 06 2016</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150305</TD>
 * <TD>Initial Release</TD>
 * </TR>
 */

public class ProductEvent implements IEvent{

	private Object targetObject;
	private ProductEventType eventType;
	private Map<Object, Object> arguments;
	
	public ProductEvent(Object targetObject, ProductEventType eventType) {
		this.targetObject = targetObject;
		this.eventType = eventType;
	}

	public Object getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(Object targetObject) {
		this.targetObject = targetObject;
	}

	public ProductEventType getEventType() {
		return eventType;
	}

	public void setEventType(ProductEventType eventType) {
		this.eventType = eventType;
	}

	public Map<Object, Object> getArguments() {
		return arguments;
	}
	
	public void setArguments(Map<Object, Object> arguments) {
		this.arguments = arguments;
	}
	
	public void addArgument(Object key, Object value) {
		if(getArguments() == null) {
			setArguments(new HashMap<Object, Object>());
		}
		getArguments().put(key, value);
	}
	
	public boolean hasArguments() {
		return arguments != null && !arguments.isEmpty();
	}
	
	public Object getArgument(Object key) {
		return hasArguments()
				? arguments.getOrDefault(key, null)
				: null;
	}
}
