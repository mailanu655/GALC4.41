package com.honda.galc.client.ui.component;

import com.honda.galc.client.utils.GuiChangeListenerFactory;
import com.honda.galc.common.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

public class LoggedTextFieldTableCell extends TextFieldTableCell implements EventHandler<Event>, ILoggedComponent{
	public LoggedTextFieldTableCell(){
		super();
		addHandlersAndListeners();
	}

	public LoggedTextFieldTableCell(StringConverter stringConverter) {
		super(stringConverter);
		addHandlersAndListeners();
	}

	private void addHandlersAndListeners(){
		this.addEventFilter(ActionEvent.ACTION, this);
		this.focusedProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.backgroundProperty().addListener(GuiChangeListenerFactory.getBackgroundListener(this));
		this.disabledProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.editableProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.visibleProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.idProperty().addListener(GuiChangeListenerFactory.getStringListener(this));
		this.textProperty().addListener(GuiChangeListenerFactory.getStringListener(this));
		this.graphicProperty().addListener(GuiChangeListenerFactory.getNodeListener(this));
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
