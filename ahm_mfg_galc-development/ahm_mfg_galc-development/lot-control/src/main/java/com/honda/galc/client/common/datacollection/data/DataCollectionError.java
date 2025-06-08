package com.honda.galc.client.common.datacollection.data;

import java.util.Date;

import com.honda.galc.client.common.component.Message;

public class DataCollectionError {
	private Message message;
	private Date timeStamp;
	
	public DataCollectionError(Message message, Date date) {
		super();
		this.message = message;
		this.timeStamp = date;
	}
	
	public DataCollectionError(String messageId, String description, Date date) {
		message = new Message(messageId, description);
		this.timeStamp = date;
	}

	//Getters & Setters
	public Date getTimeStamp() {
		return timeStamp;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

}
