package com.honda.galc.client.common.exception;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.message.MessageType;
/**
 * 
 * <h3>LotControlTaskException</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlTaskException description </p>
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
public class LotControlTaskException extends TaskException {
	private static final long serialVersionUID = 1L;
	MessageType messageType;
	String messageId;
	public LotControlTaskException(String message, String messageId, MessageType messageType) {
		super(message);
		this.messageType = messageType;
		this.messageId = messageId;
	}
	
	public LotControlTaskException(String message, String id) {
		super(message);
		this.messageId = id;
		this.messageType = MessageType.ERROR; //default to error
	}

	//Getters & Setters
	public MessageType getMessageType() {
		return messageType;
	}
	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	public Message toMessage(){
		return new Message(getMessageId(), getMessage(), getMessageType());
	}
}
