package com.honda.galc.visualoverview.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.honda.galc.visualoverview.client.event.EditLayerEvent;
import com.honda.galc.visualoverview.client.event.EditViewEvent;


public class ConfigPresenter implements Presenter {
	public interface Display {
	    HasClickHandlers getSaveButton();
	    HasClickHandlers getCancelButton();
	    HasClickHandlers getViewsButton();
	    HasClickHandlers getLayersButton();
	    Widget asWidget();
	}
	
	private final HandlerManager eventBus;
	private final Display display;
	
	public ConfigPresenter(HandlerManager eventBus, Display display)
	{
		this.eventBus = eventBus;
		this.display = display;
		bind();
	}
	
	public ConfigPresenter(HandlerManager eventBus, Display display, String id)
	{
		this.eventBus = eventBus;
		this.display = display;
		bind();
	}
	
	public void bind() {
	    
		display.getViewsButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new EditViewEvent(""));
			}
			
		});
	    display.getLayersButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new EditLayerEvent(""));
			}
	    	
	    });
	    
	    display.getSaveButton().addClickHandler(new ClickHandler() {   
		      public void onClick(ClickEvent event) {
		        doSave();
		      }
		});
	}
	
	@Override
	public void go(HasWidgets container) {
	    bind();
	    container.add(display.asWidget());
		
	}
	
	private void doSave()
	{

	}

}
