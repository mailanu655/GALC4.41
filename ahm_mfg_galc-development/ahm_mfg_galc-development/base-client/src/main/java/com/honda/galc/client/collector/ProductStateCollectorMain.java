package com.honda.galc.client.collector;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.service.property.PropertyService;

public class ProductStateCollectorMain extends CollectorMain {

	private static final long serialVersionUID = 1L;
	protected ProductStateCollectorPanel collectorPanel;
	protected ProductStateCollectorClientController controller;
	private ProductStateCollectorClientPropertyBean property;

	public ProductStateCollectorMain(ApplicationContext appContext, Application application) {
		super(appContext, application);
	}
	
	public ProductStateCollectorClientPropertyBean getProperty() {
		if(property == null)
			property = PropertyService.getPropertyBean(ProductStateCollectorClientPropertyBean.class, getApplicationContext().getProcessPointId());
		
		return property;
	}
	
	public ProductStateCollectorPanel getCollectorPanel() {
		if(collectorPanel == null)
			collectorPanel = new ProductStateCollectorPanel(getProperty(), this);
		
		return collectorPanel;
	}


	@Override
	protected void setFrameProperties() {
		super.setFrameProperties();
		
		addWindowListener(new WindowAdapter() {			
			public void windowOpened(WindowEvent e){				
				collectorPanel.getProductPane().getProductIdField().requestFocusInWindow();
			}
		});
	}
}
