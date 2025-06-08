package com.honda.galc.client.dc.view;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import com.honda.galc.client.dc.processor.IDataCollectionTaskProcessor;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.IDeviceData;

/**
 * 
 * <h3>AbstractDataCollectionWidget Class description</h3>
 * <p> AbstractDataCollectionWidget description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Feb 24, 2014
 *
 *
 */
public abstract class AbstractDataCollectionWidget<T extends IDataCollectionTaskProcessor<? extends IDeviceData>> extends BorderPane implements IDataCollectionWidget<T>{
	
	private T processor;
	
	private Logger logger;
	
	public AbstractDataCollectionWidget(T processor) {
		this.processor = processor;
		initComponents();
	}

	public T getProcessor() {
		return processor;
	}

	public void setProcessor(T processor) {
		this.processor = processor;
	}
	
	public abstract void initComponents();
	
	/**
	 * release resources used by the widget e.g unregister from the EventBus
	 */
	public void destroy() {
	}
	
	public Logger getLogger() {
		if (logger == null) {
			logger = Logger.getLogger();
		}
		return logger;
	}
	
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	/**
	 * Refresh the current widget view. The function is called when a work unit
	 * is selected. The sub classes should override this function to refresh the
	 * UI components and processor's states and get the UI ready for users
	 * 
	 */
	public void refreshView() {

	}
	
	/**
	 * Reset the current widget view to its original states. The function is
	 * called when a product id is OK. The sub classes should override this
	 * function to reset the UI components and processor's states.
	 */
	public void resetView() {

	}
	
	public void addMeasurementBoxes(VBox vBox) {
		
	}

	public void handleValidInput(int inputIndex) {
	}
}
