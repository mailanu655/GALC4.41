package com.honda.galc.client.ui.component;

import com.honda.galc.client.utils.GuiChangeListenerFactory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CustomMenuItem;

/**
 * An extenstion to the JavaFX CustomMenuItem class that adds hooks to produce check level
 * log messages for all actions and changes to the CustomMenuItem
 * @author Joseph Allen
 *
 */
public class LoggedCustomMenuItem extends CustomMenuItem implements EventHandler<ActionEvent>, ILoggedComponent{
	public LoggedCustomMenuItem(){
		super();
		addHandlersAndListeners();
	}
	
	public LoggedCustomMenuItem(Node arg1){
		super(arg1);
		addHandlersAndListeners();
	}
	
	private void addHandlersAndListeners(){
		this.addEventHandler(ActionEvent.ACTION, this);
		
		this.visibleProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.idProperty().addListener(GuiChangeListenerFactory.getStringListener(this));
	}

	@Override
	public void handle(ActionEvent event) {
		StringBuilder message = getMessage();
		message.append(" performed event type ");
		message.append(event.getEventType().getName());
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
