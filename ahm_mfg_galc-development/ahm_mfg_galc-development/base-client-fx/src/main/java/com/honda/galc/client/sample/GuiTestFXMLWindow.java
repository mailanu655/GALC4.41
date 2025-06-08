package com.honda.galc.client.sample;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.entity.conf.Application;

public class GuiTestFXMLWindow  extends MainWindow {

	public GuiTestFXMLWindow(ApplicationContext appContext, Application application) {
		super(appContext, application, true);
		initClientPanel();
	}

	private void initClientPanel() {
		setClientPane(new GuiTestFXMLPane(this));
	}

}
