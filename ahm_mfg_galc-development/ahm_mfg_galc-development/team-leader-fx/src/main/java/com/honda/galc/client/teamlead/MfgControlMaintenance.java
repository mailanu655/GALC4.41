package com.honda.galc.client.teamlead;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.entity.conf.Application;

public class MfgControlMaintenance extends MainWindow {

	public MfgControlMaintenance(ApplicationContext appContext,
			Application application) {
		super(appContext, application, false);
		setClientPane(new MfgMaintFXMLPane(this));
		setBottom(initStatusMessagePane());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
