
package com.honda.galc.client.teamleader.qi.view;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.controller.RepairAreaRowDataDialogController;
import com.honda.galc.client.teamleader.qi.model.ParkingLocationMaintenanceModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.entity.qi.QiRepairAreaRow;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * 
 * <h3>RepairAreaRowDataDialog Class description</h3>
 * <p>
 * RepairAreaRowDataDialog is used to create and update the Repair Area Row
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
public class RepairAreaRowDataDialog extends QiFxDialog<ParkingLocationMaintenanceModel>{
	private LoggedLabel siteValueLabel;
	private LoggedLabel plantValueLabel;
	private LoggedLabel repairAreaCodeValueLabel;
	private LoggedLabel repairAreaDescValueLabel;
	private LoggedLabel lotLocationValueLabel;
	private RepairAreaRowDataDialogController controller;	
	private LoggedButton createBtn;
	private LoggedButton cancelBtn;		
	private LabeledComboBox<String> spaceFillSeqCombobox;
	private LabeledUpperCaseTextField rowNoTextField;
	private QiRepairAreaRow qiRepairAreaRow;
	private String planetName;
	private QiRepairArea repairArea;
	private LoggedTextArea reasonForChangeTextArea;
	public RepairAreaRowDataDialog(String title, QiRepairAreaRow qiRepairAreaRow, ParkingLocationMaintenanceModel model,String applicationId,String planetName,QiRepairArea repairArea) {
		super(title, applicationId, model);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		controller = new RepairAreaRowDataDialogController(model, this, qiRepairAreaRow);
		this.qiRepairAreaRow=qiRepairAreaRow;
		this.planetName=planetName;
		this.repairArea=repairArea;
		initComponents();
		if(title.equalsIgnoreCase(QiConstant.CREATE_ROW)){
			loadCreateData();
		}else if(title.equalsIgnoreCase(QiConstant.UPDATE_ROW)){
			loadUpdateData();
		}
		controller.initListeners();
	}

	private void initComponents() {
		VBox outerPane = new VBox();				
		HBox currentCombinationContainer = new HBox();
		HBox repairAreaDataContainer = new HBox();		
		currentCombinationContainer.getChildren().addAll(createTitiledPane("Current Combination", createCurrentCombinationPanel()));
		currentCombinationContainer.setPadding(new Insets(10));
		repairAreaDataContainer.getChildren().addAll(createTitiledPane("Row Data", repairAreaDataPanel()));
		repairAreaDataContainer.setPadding(new Insets(10));
		outerPane.getChildren().addAll(currentCombinationContainer,repairAreaDataContainer);		
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);

	}

	private MigPane createCurrentCombinationPanel(){
		MigPane pane = new MigPane("", // Layout Constraints
				"37[]92[]", // Column constraints
				"10[]20[]");

		LoggedLabel siteNameLabel = UiFactory.createLabel("site", "Site");
		siteNameLabel.getStyleClass().add("display-label");
		siteValueLabel =new LoggedLabel();
		siteValueLabel.setText(getModel().getSiteName());

		LoggedLabel plantNameLabel = UiFactory.createLabel("plant", "Plant");
		plantNameLabel.getStyleClass().add("display-label");
		plantValueLabel =new LoggedLabel();
		plantValueLabel.setText(planetName);

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

		pane.add(siteNameLabel,"align left");
		pane.add(siteValueLabel,"wrap");
		pane.add(plantNameLabel,"align left");
		pane.add(plantValueLabel,"wrap");
		pane.add(repairAreaCodeLabel,"align left");
		pane.add(repairAreaCodeValueLabel,"wrap");
		pane.add(repairAreaDescLabel,"align left");
		pane.add(repairAreaDescValueLabel,"wrap");
		pane.add(lotLocationLabel,"align left");
		pane.add(lotLocationValueLabel);


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
		
		rowNoTextField = new LabeledUpperCaseTextField("Row #", "Row #", 10, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, true,new Insets(10,10,10,10));
		rowNoTextField.getLabel().setPadding(new Insets(10,155,10,40));
		spaceFillSeqCombobox = new LabeledComboBox<String>("Space Fill Sequence", true, new Insets(10,10,10,10), true, true);
		spaceFillSeqCombobox.getLabel().setPadding(new Insets(10,90,10,30));
		spaceFillSeqCombobox.setId("Space Fill Sequence");
		spaceFillSeqCombobox.getControl().getStyleClass().add("combo-box-base");
		spaceFillSeqCombobox.getControl().setMinHeight(35.0);
		ObservableList<String> spaceFillSeqList =FXCollections.observableArrayList("A - Ascending", "D - Descending");
		spaceFillSeqCombobox.setItems(spaceFillSeqList);
		
		setReasonForChangeContainer(reasonForChangeContainer);
		setButtonContainer(buttonContainer);
		
		pane.add(rowNoTextField,"wrap");
		pane.add(spaceFillSeqCombobox,"wrap");
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
		reasonForChangeContainer.setSpacing(98);
		reasonForChangeContainer.getChildren().addAll(reasonForChange, reasonForChangeTextArea);
	}

	private void setButtonContainer(HBox buttonContainer) {
		createBtn = createBtn(QiConstant.ADD, getController());
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
			titledPane.setPrefSize(600, 250);
		}
		else{
			titledPane.setPrefSize(600,320);
		}
		return titledPane;
	}

	/**
	 * This method is used to disable Update button , TextArea and set '0' value in hierarchy value field on the Popup screen 
	 */
	private void loadCreateData(){
		updateButton.setDisable(true);
		reasonForChangeTextArea.setDisable(true);
	}
	/**
	 * This method is used to load the data of Part on the Pop screen
	 */
	private void loadUpdateData(){
		createBtn.setDisable(true);
		rowNoTextField.setDisable(true);
		rowNoTextField.setText(String.valueOf(qiRepairAreaRow.getId().getRepairAreaRow()));
		if(qiRepairAreaRow.getSpaceFillSequence() == 'A')
			spaceFillSeqCombobox.getControl().setValue(qiRepairAreaRow.getSpaceFillSequence()+" - Ascending");
		else if(qiRepairAreaRow.getSpaceFillSequence() == 'D')
			spaceFillSeqCombobox.getControl().setValue(qiRepairAreaRow.getSpaceFillSequence()+" - Descending");
	}

	public RepairAreaRowDataDialogController getController() {
		return controller;
	}


	public LoggedLabel getSiteValueLabel() {
		return siteValueLabel;
	}


	public LoggedLabel getPlantValueLabel() {
		return plantValueLabel;
	}


	public LoggedLabel getRepairAreaCodeValueLabel() {
		return repairAreaCodeValueLabel;
	}


	public LoggedLabel getRepairAreaDescValueLabel() {
		return repairAreaDescValueLabel;
	}

	public LoggedLabel getLotLocationValueLabel() {
		return lotLocationValueLabel;
	}


	public LoggedButton getCreateBtn() {
		return createBtn;
	}


	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public LabeledComboBox<String> getSpaceFillSeqCombobox() {
		return spaceFillSeqCombobox;
	}

	public LabeledUpperCaseTextField getRowNoTextField() {
		return rowNoTextField;
	}

	public QiRepairArea getRepairArea() {
		return repairArea;
	}

	public void setRepairArea(QiRepairArea repairArea) {
		this.repairArea = repairArea;
	}

	public LoggedTextArea getReasonForChangeTextArea() {
		return reasonForChangeTextArea;
	}

}
