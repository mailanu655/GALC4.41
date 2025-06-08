package com.honda.galc.client.ui.component;

import com.honda.galc.client.utils.GuiChangeListenerFactory;
import com.honda.galc.common.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;

/**
 * An extenstion to the JavaFX Menu class that adds hooks to produce check level
 * log messages for all actions and changes to the Menu
 * @author Joseph Allen
 *
 */
public class LoggedMenu extends Menu implements EventHandler<ActionEvent>, ILoggedComponent{
	public LoggedMenu(){
		super();
		addHandlersAndListeners();
	}

	public LoggedMenu(String label) {
		super(label);
		addHandlersAndListeners();
	}
	
	private void addHandlersAndListeners(){
		this.addEventHandler(ActionEvent.ACTION,  this);
		this.visibleProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.idProperty().addListener(GuiChangeListenerFactory.getStringListener(this));
		this.textProperty().addListener(GuiChangeListenerFactory.getStringListener(this));
		this.disableProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
	}

	@Override
	public void handle(ActionEvent event) {
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

	@Override
	public Scene getScene() {
		return null;
	}
}
