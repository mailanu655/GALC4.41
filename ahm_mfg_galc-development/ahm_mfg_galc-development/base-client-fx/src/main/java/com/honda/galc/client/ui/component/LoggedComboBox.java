package com.honda.galc.client.ui.component;

import com.honda.galc.client.utils.GuiChangeListenerFactory;
import com.honda.galc.common.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;

/**
 * An extenstion to the JavaFX Button class that adds hooks to produce check level
 * log messages for all actions and changes to the button
 * @author Joseph Allen
 *
 */
public class LoggedComboBox<T> extends ComboBox implements EventHandler<Event>, ILoggedComponent{
	public LoggedComboBox(){
		super();
		addHandlersAndListeners();
	}
	
	public LoggedComboBox(String id){
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
	}

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
		return message;
	}
}
