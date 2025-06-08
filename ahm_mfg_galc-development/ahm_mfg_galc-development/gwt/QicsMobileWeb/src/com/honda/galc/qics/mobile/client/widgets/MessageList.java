package com.honda.galc.qics.mobile.client.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.gwt.beansbinding.core.client.util.AbstractBean;

/**
 * The Class MessageList is a container for messages.  When messages change a
 * property change is fired. The MessageList class is designed to work with 
 * the MessageListPanel.
 */
public class MessageList extends AbstractBean {

	// Comparable is used for sorting messages.
	private MessageComparable messageComparable = new MessageComparable();
	
	// All of the displayable messages
	private List<Message> messageList = new ArrayList<Message>();
	
	public List<Message> getMessageList() {
		return messageList;
	}

	class Message {
		// The field or item the message is referencing
		public String fieldName;
		// The originator of the message
		public Object source;
		// the message
		public String message;
	}
	
	/** 
	 * Message comparator for sorting messages
	 * 
	 */
	public class MessageComparable implements Comparator<MessageList.Message>{
		 
	    @Override
	    public int compare(Message m1, Message m2) {
	    	// compare fieldName, then verifierName and finally message
	    	// the easy way is to compare the concatenated strings
	    	String s1 = m1.fieldName + m1.source.getClass().getName() + m1.message;
	    	String s2 = m2.fieldName + m2.source.getClass().getName() + m2.message;
	    	
	        return s1.compareTo(s2);
	    }
	} 
	
	

	
	public void removeMessage( String name, Object source ) {
		// remove all matching messages
		for ( int i = messageList.size() -1 ; i >= 0 ; i-- ) {
			if ( name.equals(messageList.get(i).fieldName) && 
				 source.equals( messageList.get(i).source )) {
				messageList.remove(i);
			}
		}
		fireChange();		

	}
	
	
	public void addMessage( String name, Object source, String message ) {
		Message m = new Message();
		m.fieldName = name;
		m.source = source;
		m.message = message;
		messageList.add(m);
		fireChange();
	}


	
	public void clearMessages() {
		messageList.clear();
	}

	private void fireChange() {
		// Sort the list
		Collections.sort(messageList, messageComparable);
		// ignore sending old values
		firePropertyChange("messageList", null, messageList);
	}
	
	public boolean isEmpty() {
		return messageList.isEmpty();
	}
}
