package com.honda.galc.client.teamleader.qi.view;

import java.util.List;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.ExternalSystemMaintController;
import com.honda.galc.client.teamleader.qi.model.ExternalSystemMaintModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiExternalSystemInfo;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * 
 * <h3>ExternalSystemMaintPanel Class description</h3>
 * <p>
 * ExternalSystemMaintPanel description
 * </p>
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
 * @author Justin Jiang<br>
 *         January 14, 2021
 *
 */

public class ExternalSystemMaintPanel extends QiAbstractTabbedView<ExternalSystemMaintModel, ExternalSystemMaintController> {
	
	private ObjectTablePane<QiExternalSystemInfo> externalSystemTablePane;

	public ExternalSystemMaintPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
	}	

	@Override
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);		

		HBox dataHBox = new HBox();
		externalSystemTablePane = createExternalSystemTablePane();
		dataHBox.getChildren().addAll(externalSystemTablePane);
		dataHBox.setSpacing(20);
		dataHBox.setPadding(new Insets(50, 50, 50, 50));		

		VBox mainVBox = new VBox();
		mainVBox.getChildren().addAll(dataHBox);
		this.setCenter(mainVBox);
	}

	private ObjectTablePane<QiExternalSystemInfo> createExternalSystemTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("External System Name", "externalSystemName")
				.put("External System Description", "externalSystemDesc");

		Double[] columnWidth = new Double[] {0.4, 0.5}; 
		ObjectTablePane<QiExternalSystemInfo> panel = new ObjectTablePane<QiExternalSystemInfo>(columnMappingList, columnWidth);
		panel.setConstrainedResize(false);
		panel.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.1);
		return panel;
	}
	
	@Override
	public void reload() {
	}
	
	@Override
	public void onTabSelected() {	
		List<QiExternalSystemInfo> externalSystems = getModel().findAllExternalSystem();
		getExternalSystemTablePane().setData(externalSystems);
	}

	public ObjectTablePane<QiExternalSystemInfo> getExternalSystemTablePane() {
		return externalSystemTablePane;
	}

	public String getScreenName() {
		return "External System Maintenance";
	}
	
	@Override
	public void start() {
	}
}
