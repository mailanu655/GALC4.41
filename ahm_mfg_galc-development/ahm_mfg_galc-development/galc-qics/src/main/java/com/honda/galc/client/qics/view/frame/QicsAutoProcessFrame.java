/**
 * 
 */
package com.honda.galc.client.qics.view.frame;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventTopicSubscriber;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.datacollection.control.headless.PlcDataReadyMonitorFactory;
import com.honda.galc.client.events.ProductInspectionRequest;
import com.honda.galc.client.headless.PlcDataCollectionController;
import com.honda.galc.client.qics.view.screen.IdlePanel;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.net.Request;
import com.honda.galc.notification.service.IProductPassedNotification;

/**
 * @author Subu Kathiresan
 * @date Feb 21, 2013
 */
public class QicsAutoProcessFrame extends QicsFrame implements IProductPassedNotification,EventSubscriber<ProductInspectionRequest> {

	private static final long serialVersionUID = -294805202806819540L;
	
	private static String SOURCE_PRODUCT_PROCESS_POINT_ID = "SOURCE_PRODUCT_PROCESS_POINT_ID";

	private volatile ConcurrentLinkedQueue<String> _productQueue = new ConcurrentLinkedQueue<String>();
	
	public QicsAutoProcessFrame(ApplicationContext appContext, Application application) {
		super(appContext,application);
		AnnotationProcessor.process(this);
		initPlcMonitors();
	}	
	
	public void initPlcMonitors() {
		EventBus.subscribe(ProductInspectionRequest.class, this);
				
		PlcDataReadyMonitorFactory drMonitorFactory = new PlcDataReadyMonitorFactory(getApplication().getApplicationId());
		getPlcDcController().setApplicationContext(getApplicationContext());
		drMonitorFactory.createMonitors(getPlcDcController());
	}
	
	public void onEvent(ProductInspectionRequest pInspectionRequest) {
		productIdReceived(pInspectionRequest.getProductId());
	}
	
	private void productIdReceived(String productId) {
		addNewProductToQueue(productId);
		
		if (isIdle()) {
				if (getProductQueue() != null && !getProductQueue().isEmpty()) {
					((IdlePanel) getCurrentPanel()).getProductNumberTextField().setText(getProductQueue().poll());
					((IdlePanel) getCurrentPanel()).getProductNumberTextField().getAction().actionPerformed(null);
				}			
		}
	}

	private void addNewProductToQueue(String productId) {
		getProductQueue().add(productId);
		getLogger().info("New product " + productId + " added to product queue");
	}
	
	public ConcurrentLinkedQueue<String> getProductQueue() {
		return _productQueue;
	}

	private PlcDataCollectionController getPlcDcController() {
		return PlcDataCollectionController.getInstance(getApplication().getApplicationId());
	}
	
	@EventTopicSubscriber(topic="IProductPassedNotification")
	public void productPassedNotificationReceived(String event, Request request) {
	     try {
			request.invoke(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	     
	}
	
	// process notification IProductPassedNotification
	@Override
	public void execute(String processPointId, String productId) {
		if(!getSourceProcessPointId().equalsIgnoreCase(processPointId)) return;
		productIdReceived(productId);
	}
	
	public String getSourceProcessPointId() {
		return StringUtils.trimToEmpty(getApplicationProperty(SOURCE_PRODUCT_PROCESS_POINT_ID));
	}
}
