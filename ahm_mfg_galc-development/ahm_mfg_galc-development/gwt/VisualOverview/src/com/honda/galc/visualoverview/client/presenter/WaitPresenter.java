package com.honda.galc.visualoverview.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.honda.galc.visualoverview.client.event.EditLayerEvent;
import com.honda.galc.visualoverview.client.event.EditViewEvent;


public class WaitPresenter implements Presenter {
	public interface Display {
		HasClickHandlers getSaveButton();
	    HasClickHandlers getCancelButton();
	    Widget asWidget();
	}
	
	private final HandlerManager eventBus;
	private final Display display;
	
	public WaitPresenter(HandlerManager eventBus, Display display)
	{
		this.eventBus = eventBus;
		this.display = display;
	}
	
	public WaitPresenter(HandlerManager eventBus, Display display, String id)
	{
		this.eventBus = eventBus;
		this.display = display;
	}
	
	
	@Override
	public void go(HasWidgets container) {
	    container.add(display.asWidget());
		
	}
	

}
