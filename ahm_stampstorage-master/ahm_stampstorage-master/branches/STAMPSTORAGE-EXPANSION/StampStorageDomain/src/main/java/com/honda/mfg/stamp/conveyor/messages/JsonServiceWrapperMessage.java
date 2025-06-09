package com.honda.mfg.stamp.conveyor.messages;

public class JsonServiceWrapperMessage implements Message {

	/**
	 * @param content
	 * @param messageType
	 */
	public JsonServiceWrapperMessage(ServiceRequestMessage content, JsonServiceMessageTypes messageType) {
		super();
		this.content = content;
		this.messageType = messageType;
	}

	private ServiceRequestMessage content;

	public ServiceRequestMessage getContent() {
		return content;
	}

	public void setContent(ServiceRequestMessage content) {
		this.content = content;
	}

	private JsonServiceMessageTypes messageType;

	public JsonServiceMessageTypes getMessageType() {
		return messageType;
	}

	public void setMessageType(JsonServiceMessageTypes messageType) {
		this.messageType = messageType;
	}

	@Override
	public String toString() {
		return "JsonServiceWrapperMessage [content=" + content + ", messageType=" + messageType + "]";
	}
}
