package com.honda.galc.client.teamleader.knuckle;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.entity.conf.Application;

public class KsnRecreationWindow extends MainWindow {

	private static final long serialVersionUID = 1L;
	
	public KsnRecreationWindow(ApplicationContext appContext, Application application) {
		super(appContext, application, true);
		
		setSize(1024,768);
		
		initClientPanel();
	}

	private void initClientPanel() {
		
		this.setClientPanel(new KsnRecreationPanel(this));
		
	}


}
