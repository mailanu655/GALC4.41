package com.honda.galc.client.ui;

import java.awt.event.KeyEvent;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import com.honda.galc.common.logging.Logger;

/**
 * 
 * <h3>TabbedPanelFXML Class description</h3>
 * <p>This is a generic class to load fxml file and create a tab in main window
 * The tab list is configurable by property PANELS</p>
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
 * @author Justin Jiang
 * April 9, 2016
 */

public class TabbedPanelFXML extends TabbedPanel {
	
	private String fxmlFile = "";

	public TabbedPanelFXML(String screenName, String fxmlFile, MainWindow mainWindow) {
		super(screenName, KeyEvent.VK_E, mainWindow);
		this.fxmlFile = fxmlFile;
		init();
	}

	private void init() {
		onTabSelected();
	}
	
	@Override
	public void onTabSelected() {
		if (isInitialized) {
			return;
		} 
		
		initComponents();
		isInitialized = true;
	}
	
	private void initComponents() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
			Parent root = loader.load();
			Object controller = loader.getController();
			if (controller instanceof FXMLController) {
				((FXMLController)controller).setApplicationContext(getMainWindow().getApplicationContext());
				((FXMLController)controller).refreshData();
			}
			setCenter(root);
		} catch (IOException e) {
			getLogger().info("Invalid FXML File: " + fxmlFile);
		}
	}
}
