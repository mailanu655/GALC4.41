package com.honda.galc.client.ui.component;

import com.honda.galc.client.utils.GuiChangeListenerFactory;
import com.honda.galc.common.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

/**
 * An extension to the JavaFX TextField class that adds hooks to produce check level
 * log messages for all actions and changes to the TextField
 * @author Joseph Allen
 *
 */
public class LoggedTextField extends TextField implements EventHandler<Event>, ILoggedComponent{
	public LoggedTextField(){
		super();
		addHandlersAndListeners();
	}
	public LoggedTextField(String id){
		super();
		addHandlersAndListeners();
		this.setId(id);
	}
	
	public LoggedTextField(String id, String text){
		super(text);
		addHandlersAndListeners();
		this.setId(id);
	}

	private void addHandlersAndListeners(){
		this.addEventFilter(ActionEvent.ACTION, this);
		this.focusedProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.disabledProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.editableProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
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
		message.append(" with text ");
		message.append(this.getText());
		return message;
	}
}
