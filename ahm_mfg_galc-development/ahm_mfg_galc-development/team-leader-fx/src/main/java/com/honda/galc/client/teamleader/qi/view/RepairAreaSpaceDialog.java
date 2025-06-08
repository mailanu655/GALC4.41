package com.honda.galc.client.teamleader.qi.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.controller.RepairAreaSpaceDialogController;
import com.honda.galc.client.teamleader.qi.model.ParkingLocationMaintenanceModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.entity.qi.QiRepairAreaRow;
import com.honda.galc.entity.qi.QiRepairAreaSpace;

/**
 * 
 * <h3>RepairAreaSpaceDialog Class description</h3>
 * <p>
 * RepairAreaSpaceDialog is used to create and update the Repair Area Row 
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
public class RepairAreaSpaceDialog extends QiFxDialog<ParkingLocationMaintenanceModel>{
	private LoggedLabel siteValueLabel;
	private LoggedLabel plantValueLabel;
	private LoggedLabel repairAreaCodeValueLabel;
	private LoggedLabel repairAreaDescValueLabel;
	private LoggedLabel lotLocationValueLabel;
	private LoggedLabel rowNoValueLabel;
	private RepairAreaSpaceDialogController controller;	
	private LoggedButton createBtn;
	private LoggedButton cancelBtn;		
	private LabeledUpperCaseTextField spaceNoTextField;
	private LoggedTextArea reasonForChangeTextArea;
	private QiRepairAreaRow repairAreaRow;
	private QiRepairArea repairArea;
	private QiRepairAreaSpace repairAreaSpace;
	private String plantName;

	public RepairAreaSpaceDialog(String title, QiRepairAreaSpace repairAreaSpace, ParkingLocationMaintenanceModel model,String applicationId,String plantName,QiRepairArea repairArea,QiRepairAreaRow repairAreaRow) {
		super(title, applicationId, model);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);		
		this.repairAreaSpace=repairAreaSpace;
		this.plantName=plantName;
		this.repairArea=repairArea;
		this.repairAreaRow=repairAreaRow;
		controller = new RepairAreaSpaceDialogController(model, this, repairAreaRow, plantName, repairArea, repairAreaSpace);
		initComponents();
		if(title.equalsIgnoreCase(QiConstant.CREATE_SPACE))
			loadCreateData();
		controller.initListeners();
	}

	private void initComponents() {
		VBox outerPane = new VBox();				
		HBox currentCombinationContainer = new HBox();
		HBox repairAreaDataContainer = new HBox();		
		currentCombinationContainer.getChildren().addAll(createTitiledPane("Current Combination", createCurrentCombinationPanel()));
		currentCombinationContainer.setPadding(new Insets(10));
		repairAreaDataContainer.getChildren().addAll(createTitiledPane("Space Data", repairAreaDataPanel()));
		repairAreaDataContainer.setPadding(new Insets(10));
		outerPane.getChildren().addAll(currentCombinationContainer,repairAreaDataContainer);		
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);

	}

	private MigPane createCurrentCombinationPanel(){
		MigPane pane = new MigPane("", // Layout Constraints
				"47[]45[]", // Column constraints
				"10[]20[]");

		LoggedLabel siteNameLabel = UiFactory.createLabel("site", "Site");
		siteNameLabel.getStyleClass().add("display-label");
		siteValueLabel =new LoggedLabel();
		siteValueLabel.setText(getModel().getSiteName());

		LoggedLabel plantNameLabel = UiFactory.createLabel("plant", "Plant");
		plantNameLabel.getStyleClass().add("display-label");
		plantValueLabel =new LoggedLabel();
		plantValueLabel.setText(plantName);

		LoggedLabel repairAreaCodeLabel = UiFactory.createLabel("repairAreaCode", "Repair Area Code");
		repairAreaCodeLabel.getStyleClass().add("display-label");
		repairAreaCodeValueLabel =new LoggedLabel();
		repairAreaCodeValueLabel.setText(repairArea.getRepairAreaName());

		LoggedLabel repairAreaDescLabel = UiFactory.createLabel("repairAreaDesc", "Repair Area Description");
		repairAreaDescLabel.getStyleClass().add("display-label");
		repairAreaDescValueLabel =new LoggedLabel();
		repairAreaDescValueLabel.setText(repairArea.getRepairAreaDescription());

		LoggedLabel lotLocationLabel = UiFactory.createLabel("lotLocation", "Lot Location");
		lotLocationLabel.getStyleClass().add("display-label");
		lotLocationValueLabel =new LoggedLabel();
		lotLocationValueLabel.setText(String.valueOf(repairArea.getLocation()));
		
		LoggedLabel rowNoLabel = UiFactory.createLabel("lotLocation", "Row Number");
		rowNoLabel.getStyleClass().add("display-label");
		rowNoValueLabel =new LoggedLabel();
		rowNoValueLabel.setText(String.valueOf(repairAreaRow.getId().getRepairAreaRow()));

		pane.add(siteNameLabel,"align left");
		pane.add(siteValueLabel,"wrap");
		pane.add(plantNameLabel,"align left");
		pane.add(plantValueLabel,"wrap");
		pane.add(repairAreaCodeLabel,"align left");
		pane.add(repairAreaCodeValueLabel,"wrap");
		pane.add(repairAreaDescLabel,"align left");
		pane.add(repairAreaDescValueLabel,"wrap");
		pane.add(lotLocationLabel,"align left");
		pane.add(lotLocationValueLabel,"wrap");
		pane.add(rowNoLabel,"align left");
		pane.add(rowNoValueLabel);


		return pane;
	}

	private MigPane repairAreaDataPanel(){
		
		MigPane pane = new MigPane("insets 10 10 10 10", "", "");      
		HBox reasonForChangeContainer = new HBox();
		HBox buttonContainer = new HBox();
		
		
		
		reasonForChangeTextArea = UiFactory.createTextArea();
		reasonForChangeTextArea.setPrefWidth(250);
		reasonForChangeTextArea.setPrefRowCount(2);
		reasonForChangeTextArea.setWrapText(true);
		reasonForChangeTextArea.setId("Reason for Change");
		
		spaceNoTextField = new LabeledUpperCaseTextField("Space #", "Space #", 10, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, true,new Insets(10,10,10,10));
		spaceNoTextField.getLabel().setPadding(new Insets(10,135,10,40));
		
		setReasonForChangeContainer(reasonForChangeContainer);
		setButtonContainer(buttonContainer);
		pane.add(createStatusRadioButtons(getController()),"wrap");
		pane.add(spaceNoTextField,"wrap");
		pane.add(reasonForChangeContainer,"wrap");
		pane.add(buttonContainer,"span,wrap");
		return pane;
	}
	
	private void setReasonForChangeContainer(HBox reasonForChangeContainer) {
		HBox reasonForChange = new HBox();
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("reasonForChange","Reason for Change");
		reasonForChangeLabel.getStyleClass().add("display-label");
		LoggedLabel asterisk1 = UiFactory.createLabel("asteriskReasonForChange", "*");
		asterisk1.getStyleClass().add("display-label");
		asterisk1.setStyle("-fx-text-fill: red");
		reasonForChange.getChildren().addAll(reasonForChangeLabel,asterisk1);
		reasonForChangeContainer.setPadding(new Insets(5, 10, 10, 40));
		reasonForChangeContainer.setSpacing(90);
		reasonForChangeContainer.getChildren().addAll(reasonForChange, reasonForChangeTextArea);
	}

	private void setButtonContainer(HBox buttonContainer) {
		createBtn = createBtn(QiConstant.CREATE, getController());
		createBtn.setPadding(new Insets(5,5,5,5));
		updateButton = createBtn(QiConstant.UPDATE,getController());
		cancelBtn = createBtn(QiConstant.CANCEL,getController());		
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(10, 10, 10, 140));
		buttonContainer.setSpacing(20);
		buttonContainer.getChildren().addAll(createBtn, updateButton, cancelBtn);
	}


	private TitledPane createTitiledPane(String title,Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		if("Current Combination".equals(title)){
			titledPane.setPrefSize(650, 270);
		}
		else{
			titledPane.setPrefSize(650,390);
		}
		return titledPane;
	}

	public RepairAreaSpaceDialogController getController() {
		return controller;
	}
	
	public LabeledUpperCaseTextField getSpaceNoTextField() {
		return spaceNoTextField;
	}

	public LoggedButton getCreateBtn() {
		return createBtn;
	}


	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}


	private void loadCreateData(){	
		updateButton.setDisable(true);
		reasonForChangeTextArea.setDisable(true);
	}

	public LoggedTextArea getReasonForChangeTextArea() {
		return reasonForChangeTextArea;
	}
	
}
