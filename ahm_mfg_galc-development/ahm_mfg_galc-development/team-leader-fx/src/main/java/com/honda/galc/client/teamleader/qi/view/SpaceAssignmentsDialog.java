package com.honda.galc.client.teamleader.qi.view;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.honda.galc.client.teamleader.qi.controller.SpaceAssignmentDialogController;
import com.honda.galc.client.teamleader.qi.model.ParkingLocationMaintenanceModel;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiRepairAreaSapceAssignmentDto;
import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.entity.qi.QiRepairAreaRow;
/**
 * 
 * <h3>SpaceAssignmentsDialog Class description</h3>
 * <p>
 * SpaceAssignmentsDialog is used to display the Space data based on RepairArea and RepairAreaRow while clicking 'Space Assignment' context menu
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
 * @author LnTInfotech<br>
 * 
 */
public class SpaceAssignmentsDialog extends QiFxDialog<ParkingLocationMaintenanceModel> {
	
	private LoggedLabel siteValueLabel;
	private LoggedLabel plantValueLabel;
	private LoggedLabel repairAreaCodeValueLabel;
	private LoggedComboBox<Integer> rowCombobox;
	private ObjectTablePane<QiRepairAreaSapceAssignmentDto> viewSpaceAssignmentsTablePane ;
	private String planetName;
	private QiRepairArea repairArea;
	private QiRepairAreaRow qiRepairAreaRow;
	private SpaceAssignmentDialogController controller;
	private QiRepairAreaSapceAssignmentDto qiRepairAreaSapceAssignmentDto;
	public SpaceAssignmentsDialog(String title, String owner, ParkingLocationMaintenanceModel model,QiRepairAreaRow qiRepairAreaRow, String applicationId,String planetName,QiRepairArea repairArea) {
		super(title, applicationId, model);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.planetName=planetName;
		this.repairArea=repairArea;
		this.qiRepairAreaRow=qiRepairAreaRow;
		this.controller=new SpaceAssignmentDialogController(model, this, qiRepairAreaSapceAssignmentDto);
		viewSpaceAssignmentsTablePane= new ObjectTablePane<QiRepairAreaSapceAssignmentDto>();
		initComponents();	
		loadData();
		controller.initListeners();
		
	}

	private void initComponents() {
		VBox outerPane = new VBox();
		outerPane.setPrefHeight(300);
		HBox mainPane = new HBox();
		createSpaceAssignment(mainPane);
		viewSpaceAssignmentsTablePane = createViewSpaceAssignmentsTablePane();	
		outerPane.getChildren().addAll(createTitiledPane("Current Combination", mainPane),createTitiledPane("Space Data", viewSpaceAssignmentsTablePane));
		outerPane.setSpacing(20);
		outerPane.setPadding(new Insets(10, 0,0, 0));
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
	}
	
	private void loadData(){	
		viewSpaceAssignmentsTablePane.getTable().setItems(FXCollections.observableArrayList(getModel().findAllSpaceAssignmentByRepairAreaNameAndRow(qiRepairAreaRow.getId())));		
	}

	@SuppressWarnings("unchecked")
	private void createSpaceAssignment(HBox mainPane){
		LoggedLabel siteNameLabel = UiFactory.createLabel("site", "Site :");
		siteNameLabel.getStyleClass().add("display-label");
		siteValueLabel =new LoggedLabel();
		siteValueLabel.setText(getModel().getSiteName());
		LoggedLabel plantNameLabel = UiFactory.createLabel("plant", "Plant :");
		plantNameLabel.getStyleClass().add("display-label");
		plantValueLabel =new LoggedLabel();
		plantValueLabel.setText(planetName);
		LoggedLabel repairAreaCodeNameLabel = UiFactory.createLabel("repairAreaCode", "Repair Area Code :");
		repairAreaCodeNameLabel.getStyleClass().add("display-label");
		repairAreaCodeValueLabel =new LoggedLabel();
		repairAreaCodeValueLabel.setText(repairArea.getRepairAreaName());
		LoggedLabel rowLabel = UiFactory.createLabel("row", "Row");
		rowLabel.getStyleClass().add("display-label");
		rowCombobox = new LoggedComboBox<Integer>();
		List<QiRepairAreaRow> repairAreaRowsList = getModel().findAllByRepairAreaName(repairArea.getRepairAreaName());
		for (QiRepairAreaRow qiRepairAreaRow : repairAreaRowsList) {
			rowCombobox.getItems().add(String.valueOf(qiRepairAreaRow.getId().getRepairAreaRow()));
		}
	
		rowCombobox.setValue(String.valueOf(qiRepairAreaRow.getId().getRepairAreaRow()));
		mainPane.getChildren().addAll(siteNameLabel,siteValueLabel,plantNameLabel,plantValueLabel,repairAreaCodeNameLabel,repairAreaCodeValueLabel,rowLabel,rowCombobox);
		mainPane.setSpacing(20);
	}
	private ObjectTablePane<QiRepairAreaSapceAssignmentDto> createViewSpaceAssignmentsTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Repair Area Name", "repairAreaName")
				.put("Row","repairAreaRow")
				.put("Space", "repairAreaSpace")
				.put("Prod Unit Id No", "productId")
				.put("Prod Unit Type", "productType")
				.put("Defect", "defectTypeName")
				.put("Defect Desc", "defectTypeName")
				.put("Resp Plant", "responsiblePlant");

		Double[] columnWidth = new Double[] {
				0.09, 0.09, 0.09, 0.1, 0.09, 0.09, 0.1, 0.09
		}; 
		ObjectTablePane<QiRepairAreaSapceAssignmentDto> panel = new ObjectTablePane<QiRepairAreaSapceAssignmentDto>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		return panel;
	}

	 /**
     * This method is used to create TitledPane for Part panel.
     * @param title
     * @param content
     * @return
     */
  private TitledPane createTitiledPane(String title,Node content) {
                     TitledPane titledPane = new TitledPane();
                     titledPane.setText(title);
                     titledPane.setContent(content);
                     if("Current Combination".equals(title)){
                  	   titledPane.setPrefSize(1000, 100);
                     }
                     else{
                         titledPane.setPrefSize(1000, 500);
                     }
                     return titledPane;
     }

	public ObjectTablePane<QiRepairAreaSapceAssignmentDto> getViewSpaceAssignmentsTablePane() {
		return viewSpaceAssignmentsTablePane;
	}

	public void setViewSpaceAssignmentsTablePane(
			ObjectTablePane<QiRepairAreaSapceAssignmentDto> viewSpaceAssignmentsTablePane) {
		this.viewSpaceAssignmentsTablePane = viewSpaceAssignmentsTablePane;
	}

	public LoggedComboBox<Integer> getRowCombobox() {
		return rowCombobox;
	}

	public void setRowCombobox(LoggedComboBox<Integer> rowCombobox) {
		this.rowCombobox = rowCombobox;
	}

	public QiRepairAreaRow getQiRepairAreaRow() {
		return qiRepairAreaRow;
	}

	public void setQiRepairAreaRow(QiRepairAreaRow qiRepairAreaRow) {
		this.qiRepairAreaRow = qiRepairAreaRow;
	}
  
}
