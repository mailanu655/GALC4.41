package com.honda.galc.client.qics.view.frame;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.datacollection.control.headless.PlcDataReadyMonitorFactory;
import com.honda.galc.client.events.RfidProductInspectionRequest;
import com.honda.galc.client.headless.PlcDataCollectionController;
import com.honda.galc.client.qics.view.screen.IdlePanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.entity.conf.Application;

public class QicsAutoDirectPassFrame extends QicsFrame implements EventSubscriber<RfidProductInspectionRequest> {

	private static final long serialVersionUID = -294805202806819540L;
	
	private volatile ConcurrentLinkedQueue<RfidProductInspectionRequest> _productQueue = new ConcurrentLinkedQueue<RfidProductInspectionRequest>();
	
	public QicsAutoDirectPassFrame(ApplicationContext appContext, Application application) {
		super(appContext, application);
		initPlcMonitors();
	}	
	
	public void initPlcMonitors() {
		EventBus.subscribe(RfidProductInspectionRequest.class, this);
		PlcDataReadyMonitorFactory drMonitorFactory = new PlcDataReadyMonitorFactory(getApplication().getApplicationId());
		getPlcDcController().setApplicationContext(getApplicationContext());
		drMonitorFactory.createMonitors(getPlcDcController());
	}
	
	public void onEvent(RfidProductInspectionRequest event) {
		addNewProductToQueue(event);
		
		if (isIdle()) {
				if (getProductQueue() != null && !getProductQueue().isEmpty()) {
					((IdlePanel) getCurrentPanel()).getProductNumberTextField().setText(getProductQueue().poll().getProductId());
					getLogger().info("Auto-populating product id "+((IdlePanel) getCurrentPanel()).getProductNumberTextField().getText());
				}
		}
		
		if(event.isRfidValidationStatus())
			autoDirectPass();
		
	}
	
	private void autoDirectPass(){
		getLogger().info("event called autodirectpass");
		((IdlePanel) getCurrentPanel()).getProductNumberTextField().getAction().actionPerformed(null);
		getQicsController().submitDirectPass();
		if (getCurrentPanel().getQicsFrame().displayDelayedMessage()) return;

		getCurrentPanel().getQicsFrame().displayIdleView();
		sendDataCollectionCompleteToPlcIfDefined();
		
	}

	private void addNewProductToQueue(RfidProductInspectionRequest pInspectionRequest) {
		getProductQueue().add(pInspectionRequest);
		getLogger().info("New product " + pInspectionRequest.getProductId() + " added to product queue");
	}
	
	public ConcurrentLinkedQueue<RfidProductInspectionRequest> getProductQueue() {
		return _productQueue;
	}
	
	private PlcDataCollectionController getPlcDcController() {
		return PlcDataCollectionController.getInstance(getApplication().getApplicationId());
	}
}
