package com.honda.galc.visualoverview.client.view;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.honda.galc.visualoverview.client.presenter.WaitPresenter;

public class WaitView extends DialogBox implements WaitPresenter.Display {

	private HorizontalPanel imagePanel;

	public WaitView()
	{
		setGlassEnabled(true);
		setModal(false);
		
		buildPanel();
		
		add(imagePanel);
		//setPopupPosition((int)(Window.getClientWidth() * .45), (int)(Window.getClientHeight() * .45));
		center();

	}
	
	private void buildPanel()
	{
		Image spinnerImage = new Image(GWT.getModuleBaseURL() + "ajax-loader.gif");
		imagePanel = new HorizontalPanel();
		imagePanel.add(spinnerImage);
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



	

}
