package com.honda.galc.client.dc.common;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.message.MessageType;
/**
 * 
 * <h3>Message</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> Message description </p>
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
 * Jun 10, 2010
 *
 */

public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	String id;
	String description;
	MessageType type = MessageType.ERROR;
	
	public Message() {
		super();
	}
	
	public Message(String description) {
		super();
		this.description = description;
	}
	
	public Message(String id, String description) {
		super();
		this.id = id;
		this.description = description;
	}
	
	public Message(String id, String description, MessageType type) {
		super();
		this.id = id;
		this.description = description;
		this.type = type;
	}

	//Getters & Setters
	public String getId() {
		return id;
	}
	public void setId(String messageId) {
		this.id = messageId;
	}
	public String getDescription() {
		return description;
	}
	public void setMessage(String message) {
		this.description = message;
	}

	public boolean isEmpty() {
		return StringUtils.isEmpty(description);
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}
}
