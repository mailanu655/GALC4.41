package com.honda.galc.visualoverview.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

public class WaitDialog extends DialogBox {

	public WaitDialog()
	{

		Image spinnerImage = new Image(GWT.getModuleBaseURL() + "ajax-loader.gif");
		spinnerImage.setSize("100px", "100px");
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(spinnerImage);
		setWidget(panel);
	}
	
}
