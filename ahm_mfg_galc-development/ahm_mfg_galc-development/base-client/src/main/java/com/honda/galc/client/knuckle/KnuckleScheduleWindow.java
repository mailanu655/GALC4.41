package com.honda.galc.client.knuckle;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.entity.conf.Application;

public class KnuckleScheduleWindow extends MainWindow {

	private static final long serialVersionUID = 1L;
	
	private KnuckleSchedulePanel knucklePanel ;
	public KnuckleScheduleWindow(ApplicationContext appContext, Application application) {
		super(appContext, application, true);
		
		setSize(1024,768);
		
		initClientPanel();
	}

	private void initClientPanel() {
		knucklePanel = new KnuckleSchedulePanel(getApplicationContext());
		this.setClientPanel(knucklePanel);
	}
	
	public void cleanUp() {
		super.cleanUp();
	}
}
