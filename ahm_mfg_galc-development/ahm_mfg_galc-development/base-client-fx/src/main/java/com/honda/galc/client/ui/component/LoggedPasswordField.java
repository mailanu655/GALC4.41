package com.honda.galc.client.ui.component;

import com.honda.galc.client.utils.GuiChangeListenerFactory;
import com.honda.galc.common.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.PasswordField;

/**
 * An extenstion to the JavaFX PasswordField class that adds hooks to produce check level
 * log messages for all actions and changes to the PasswordField
 * @author Joseph Allen
 *
 */
public class LoggedPasswordField extends PasswordField implements EventHandler<Event>, ILoggedComponent{
	public LoggedPasswordField(){
		super();
		addHandlersAndListeners();
	}
	
	private void addHandlersAndListeners(){
		this.addEventFilter(ActionEvent.ACTION, this);
		this.focusedProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.disabledProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
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
