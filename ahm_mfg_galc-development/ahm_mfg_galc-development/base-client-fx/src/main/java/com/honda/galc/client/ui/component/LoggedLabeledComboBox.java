package com.honda.galc.client.ui.component;

import com.honda.galc.client.utils.GuiChangeListenerFactory;
import com.honda.galc.common.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;

/**
 * An extenstion to the JavaFX LabeledComboBox class that adds hooks to produce check level
 * log messages for all actions and changes to the LabeledComboBox
 * @author Joseph Allen
 *
 */
public class LoggedLabeledComboBox<T> extends LabeledComboBox<T> implements EventHandler<Event>, ILoggedComponent{

	public LoggedLabeledComboBox(String labelName, boolean isHorizontal) {
		super(labelName, isHorizontal);
		addHandlersAndListeners();
	}

	private void addHandlersAndListeners(){
		this.addEventFilter(ActionEvent.ACTION, this);		
		this.focusedProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.effectProperty().addListener(GuiChangeListenerFactory.getEffectListener(this));
		this.backgroundProperty().addListener(GuiChangeListenerFactory.getBackgroundListener(this));
		this.disabledProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.rotateProperty().addListener(GuiChangeListenerFactory.getNumberListener(this));
		this.visibleProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.getLabel().textProperty().addListener(GuiChangeListenerFactory.getStringListener(this));
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
		message.append(this.getLabel().getText());
		return message;
	}
}
