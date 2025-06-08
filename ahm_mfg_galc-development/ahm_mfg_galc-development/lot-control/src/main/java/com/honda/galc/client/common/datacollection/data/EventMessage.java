package com.honda.galc.client.common.datacollection.data;
/**
 * 
 * <h3>EventMessage</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EventMessage description </p>
 * Define message for event bus.
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
 * Mar 18, 2010
 *
 */
public class EventMessage<T> {
	private String id;
	private T message;
	
	public EventMessage(String id, T message) {
		super();
		this.id = id;
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public T getMessage() {
		return message;
	}

	public void setMessage(T message) {
		this.message = message;
	}
}
