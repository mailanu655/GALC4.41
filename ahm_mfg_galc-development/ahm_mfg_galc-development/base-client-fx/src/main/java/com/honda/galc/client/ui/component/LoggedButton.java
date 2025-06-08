package com.honda.galc.client.ui.component;

import com.honda.galc.client.utils.GuiChangeListenerFactory;
import com.honda.galc.common.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;

/**
 * An extenstion to the JavaFX Button class that adds hooks to produce check level
 * log messages for all actions and changes to the button
 * @author Joseph Allen
 *
 */
public class LoggedButton extends Button implements EventHandler<Event>, ILoggedComponent{
	public LoggedButton(){
		super();
		addHandlersAndListeners();
	}
	
	public LoggedButton(String text, String id){
		super(text);
		addHandlersAndListeners();
		this.setId(id);
	}
	
	public LoggedButton(String text, Node arg1){
		super(text, arg1);
		addHandlersAndListeners();
	}
	
	public LoggedButton(String text, Node arg1, String id){
		super(text, arg1);
		addHandlersAndListeners();
		this.setId(id);
	}

	/**
	 * Adds the Handlers and Listeners to the Button Object
	 */
	private void addHandlersAndListeners(){
		this.addEventFilter(ActionEvent.ACTION, this);
		
		this.focusedProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.disabledProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.visibleProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.textProperty().addListener(GuiChangeListenerFactory.getStringListener(this));
	}

	/**
	 * Create check log message for event actions
	 */
	@Override
	public void handle(Event event) {
		StringBuilder message = getMessage();
		message.append(" performed event type ");
		message.append(event.getEventType().getName());
		Logger.getLogger().check(message.toString());
	}
	
	public StringBuilder getMessage(){
		StringBuilder message = new StringBuilder(super.getClass().getSimpleName());
		message.append(" ");
		message.append(this.getId());
		message.append(" with text ");
		message.append(this.getText());
		return message;
	}
}
