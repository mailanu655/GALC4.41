package com.honda.galc.visualoverview.client.view;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.honda.galc.visualoverview.client.presenter.ConfigPresenter;

public class ConfigView extends DialogBox implements ConfigPresenter.Display {

	private HorizontalPanel buttonPanel;
	private Button viewsButton;
	private Button layersButton;

	
	
	public ConfigView()
	{
		setGlassEnabled(true);
		setModal(false);
		
		buildPanel();
		
		add(buttonPanel);
		setPopupPosition((int)(Window.getClientWidth() * .45), (int)(Window.getClientHeight() * .45));
		center();

	}
	
	private void buildPanel()
	{

		viewsButton = new Button("Views");
		viewsButton.setStylePrimaryName("custom-Button");
		layersButton = new Button("Layers");
		layersButton.setStylePrimaryName("custom-Button");
		viewsButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide();
				
			}
			
		});
		layersButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide();
				
			}
			
		});
		

		buttonPanel = new HorizontalPanel();
		buttonPanel.setWidth("100%");
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		buttonPanel.add(viewsButton);
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		buttonPanel.add(layersButton);
		
	}

	@Override
	public HasClickHandlers getSaveButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasClickHandlers getCancelButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasClickHandlers getViewsButton() {
		return viewsButton;
	}

	@Override
	public HasClickHandlers getLayersButton() {
		return layersButton;
	}

	

}
