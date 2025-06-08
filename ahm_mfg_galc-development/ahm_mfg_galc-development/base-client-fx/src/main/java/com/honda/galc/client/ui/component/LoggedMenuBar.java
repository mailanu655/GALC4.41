package com.honda.galc.client.ui.component;

import com.honda.galc.client.utils.GuiChangeListenerFactory;
import com.honda.galc.common.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.MenuBar;

/**
 * An extenstion to the JavaFX MenuBar class that adds hooks to produce check level
 * log messages for all actions and changes to the MenuBar
 * @author Joseph Allen
 *
 */
public class LoggedMenuBar extends MenuBar implements EventHandler<Event>, ILoggedComponent{
	public LoggedMenuBar(){
		super();
		addHandlersAndListeners();
	}
	
	private void addHandlersAndListeners(){
		this.addEventHandler(ActionEvent.ACTION,  this);
		this.focusedProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.disabledProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.visibleProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.idProperty().addListener(GuiChangeListenerFactory.getStringListener(this));
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
