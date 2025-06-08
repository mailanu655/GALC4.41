package com.honda.galc.client;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.ProductTypeData;

public class ClientController {
	
	private MainWindow mainWindow;
	
	private Exception exception;
	
	public ClientController(MainWindow mainWindow) {
		
		this.mainWindow = mainWindow;
	}

	public MainWindow getMainWindow() {
		return mainWindow;
	}

	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}
	
	public void displayException(Exception e) {
		getMainWindow().setErrorMessage("Exception occurs..." + exception);
	}
	
	public boolean isMsgExists() {
		return exception != null;
	}
	
	public String getMessage() {
		return exception == null ? "" : exception.getMessage();
	}
	
	public ProductType getProductType() {
		return mainWindow.getProductType();
	}
	
	public ProductTypeData getProductTypeData() {
		return mainWindow.getApplicationContext().getProductTypeData();
	}
 }
