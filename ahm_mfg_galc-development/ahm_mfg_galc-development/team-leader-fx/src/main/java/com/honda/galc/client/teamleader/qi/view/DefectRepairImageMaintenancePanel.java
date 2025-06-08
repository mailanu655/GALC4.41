package com.honda.galc.client.teamleader.qi.view;


import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.DefectRepairImageMaintenanceController;
import com.honda.galc.client.teamleader.qi.model.DefectRepairImageMaintenanceModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMapping;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiDefectResultImageDto;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * <h3>Class description</h3>
 * 
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * </TABLE>
 * @version 1.0
 * @author Dylan Yang
 * @see
 *
 */
public class DefectRepairImageMaintenancePanel extends QiAbstractTabbedView<DefectRepairImageMaintenanceModel, DefectRepairImageMaintenanceController> {

	private LoggedRadioButton searchProductIdRadioButton;
	private LoggedRadioButton searchPartDefectCombinationRadioButton;
	private LoggedRadioButton searchApplicationIdRadioButton;
	
	private LoggedTextField productIdTextField;
	private LoggedTextField partDefectCombinationTextField;
	private LoggedTextField applicationIdTextField;
	
	private LoggedButton searchButton;
	
	private ObjectTablePane<QiDefectResultImageDto> defectRepairImageTablePane;

	public DefectRepairImageMaintenancePanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
	}

	@Override
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		HBox searchBox = new HBox();

		createRadioButtonGroup();
		productIdTextField = createTF("prodID");
		partDefectCombinationTextField = createTF("pdcID");
		applicationIdTextField = createTF("appID");
		
		searchButton = new LoggedButton("Search", "Search");
		searchButton.setPadding(new Insets(20, 20, 20, 20));
		searchButton.setDefaultButton(true);
		searchButton.setOnAction(getController());
		
		HBox prodIdHBox = new HBox();
		HBox pdcHBox = new HBox();
		HBox appIdHBox = new HBox();
		HBox searchButtonHBox = new HBox();
		prodIdHBox.setAlignment(Pos.CENTER);
		pdcHBox.setAlignment(Pos.CENTER);
		appIdHBox.setAlignment(Pos.CENTER);
		searchButtonHBox.setAlignment(Pos.CENTER);
		
		prodIdHBox.getChildren().addAll(searchProductIdRadioButton, productIdTextField);
		pdcHBox.getChildren().addAll(searchPartDefectCombinationRadioButton, partDefectCombinationTextField);
		appIdHBox.getChildren().addAll(searchApplicationIdRadioButton, applicationIdTextField);
		searchButtonHBox.getChildren().add(searchButton);
		
		VBox groupBox = new VBox();
		groupBox.getChildren().addAll(prodIdHBox, pdcHBox, appIdHBox, searchButtonHBox);
		searchBox.getChildren().addAll(groupBox);
		searchBox.setAlignment(Pos.CENTER);

		defectRepairImageTablePane = createImageTablePane();
		defectRepairImageTablePane.setPadding(new Insets(10, 10, 10, 10));
		this.setTop(searchBox);
		this.setCenter(defectRepairImageTablePane);
	}

	private LoggedTextField createTF(String id) {
		LoggedTextField tf = createLoggedFilterTextField(id, 50, getController());
		tf.setPadding(new Insets(10, 10, 10, 10));
		tf.setPrefSize(500, 50);
		tf.setAlignment(Pos.CENTER_LEFT);
		return tf;
	}
	
	private void createRadioButtonGroup() {
		ToggleGroup group = new ToggleGroup();
		searchProductIdRadioButton = createRB("Search by Product ID:", "SPID", group);
		searchPartDefectCombinationRadioButton = createRB("Search by Part Defect Combination", "SPDC", group);
		searchApplicationIdRadioButton = createRB("Search by Application ID", "SAID", group);
		searchProductIdRadioButton.setSelected(true);
	}
	
	private LoggedRadioButton createRB(String text, String id, ToggleGroup group) {
		LoggedRadioButton button = createRadioButton(text, group, true, getController());
		button.getStyleClass().add("display-label");
		button.setId(id);
		button.setPadding(new Insets(10, 10, 10, 10));
		button.setPrefHeight(70);
		button.setPrefWidth(300);
		button.setWrapText(true);
		return button;
	}

	private ObjectTablePane<QiDefectResultImageDto> createImageTablePane() {
		ColumnMappingList columnMappingList = new ColumnMappingList();
		columnMappingList.get().add(new ColumnMapping("Product ID", "productId"));
		columnMappingList.get().add(new ColumnMapping("Defect Result ID", "defectResultId"));		
		columnMappingList.get().add(new ColumnMapping("Repair ID", "repairId"));
		columnMappingList.get().add(new ColumnMapping("Application ID", "applicationId"));
		columnMappingList.get().add(new ColumnMapping("Part Defect Combination", "partDefectCombination"));
		columnMappingList.get().add(new ColumnMapping("Image/Video File Name", "filename"));

		ColumnMapping mapping = new ColumnMapping("Image URL", "url");
		mapping.setType(Hyperlink.class);
		mapping.setEditable(false);
		mapping.setSortable(false);
		columnMappingList.get().add(mapping);
		
		Double[] columnWidth = new Double[] { 0.1, 0.1, 0.1, 0.15, 0.15, 0.10, 0.3 };
		ObjectTablePane<QiDefectResultImageDto> panel = new ObjectTablePane<QiDefectResultImageDto>(columnMappingList, columnWidth);
		panel.setConstrainedResize(false);
		panel.getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		return panel;
	}

	public void onTabSelected() {
		reload();
	}

	@Override
	public void reload() {
		defectRepairImageTablePane.setData(new ArrayList<QiDefectResultImageDto>());
	}

	public void reload(List<QiDefectResultImageDto> list) {
		defectRepairImageTablePane.setData(list);
	}

	@Override
	public void start() {
	}

	public ObjectTablePane<QiDefectResultImageDto> getImageTablePane() {
		return defectRepairImageTablePane;
	}

	public String getScreenName() {
		return "Defect Repair Image Maintenance";
	}

	public LoggedRadioButton getSearchProductIdRadioButton() {
		return searchProductIdRadioButton;
	}

	public LoggedRadioButton getSearchPartDefectCombinationRadioButton() {
		return searchPartDefectCombinationRadioButton;
	}

	public LoggedRadioButton getSearchApplicationIdRadioButton() {
		return searchApplicationIdRadioButton;
	}

	public LoggedTextField getProductIdTextField() {
		return productIdTextField;
	}

	public LoggedTextField getPartDefectCombinationTextField() {
		return partDefectCombinationTextField;
	}

	public LoggedTextField getApplicationIdTextField() {
		return applicationIdTextField;
	}
}
