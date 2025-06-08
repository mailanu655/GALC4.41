package com.honda.galc.client.ui.component;

import com.honda.galc.client.utils.GuiChangeListenerFactory;
import com.honda.galc.common.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;

public class LoggedTableView<T> extends TableView <T> implements EventHandler<ActionEvent>, ILoggedComponent{
	public LoggedTableView(){
		super();
		addHandlersAndListeners();
	}
	
	public LoggedTableView(String id){
		super();
		this.setId(id);
		addHandlersAndListeners();
	}

	private void addHandlersAndListeners(){
		this.addEventHandler(ActionEvent.ACTION, this);
		this.focusedProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.backgroundProperty().addListener(GuiChangeListenerFactory.getBackgroundListener(this));
		this.disabledProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.editableProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.visibleProperty().addListener(GuiChangeListenerFactory.getBooleanListener(this));
		this.idProperty().addListener(GuiChangeListenerFactory.getStringListener(this));
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
		return message;
	}
}
