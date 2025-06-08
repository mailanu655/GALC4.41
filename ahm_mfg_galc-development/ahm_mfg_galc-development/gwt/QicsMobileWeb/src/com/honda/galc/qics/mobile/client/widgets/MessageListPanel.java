package com.honda.galc.qics.mobile.client.widgets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import com.honda.galc.qics.mobile.client.widgets.MessageList.Message;

/**
 * The Class MessageListPanel is used to display a list of message associated with a form.  
 * When there are no message, the panel is hidden.
 */
public class MessageListPanel extends MessagePanel {
	
	public MessageListPanel( MessageList messageList ) {
		super();
        
         // add a listener to update on message changes
        messageList.addPropertyChangeListener( new PropertyChangeListener(){

			@SuppressWarnings("unchecked")
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateContent( (List<MessageList.Message>) evt.getNewValue() ); 
			}
        });
	}
	


	private void updateContent(List<Message> list ) {
		if ( list.isEmpty() ) {
			clearMessage();
		} else {
			String content = buildContent(list);
			setMessage( content );					
		}
	}
	
	private String buildContent(List<Message> list) {
		// build the html 
		StringBuilder sb = new StringBuilder();
		sb.append("<ul>");
		for ( Message m : list ) {
			sb.append( "<li>");
			sb.append( m.fieldName + ": " + m.message );
			sb.append("</li>");
		}
		sb.append("</ul>");
		return sb.toString();
	}
	


}
