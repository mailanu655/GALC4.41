package com.honda.galc.client.teamleader.qi.view;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.honda.galc.client.teamleader.qi.controller.RepairMethodDialogController;
import com.honda.galc.client.teamleader.qi.model.RepairMethodMaintenanceModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiRepairMethod;

public class RepairMethodDialog extends QiFxDialog<RepairMethodMaintenanceModel>{	
	private LoggedButton createBtn;
	private LoggedButton cancelBtn;
	
	private LoggedLabel repairMethodNameLabel;
	private LoggedLabel repairMethodDescriptionLabel;
	
	private LoggedTextArea repairMethodDescriptionTextArea;
	private LoggedTextArea reasonForChangeTextArea;
	
	private UpperCaseFieldBean repairMethodNameTextField;
	
	private ObjectTablePane<QiRepairMethod> repairMethodPane;
	
	private String title;
	RepairMethodDialogController controller;
	QiRepairMethod repairMethod;
	
	
	public RepairMethodDialog(String title, QiRepairMethod owner, RepairMethodMaintenanceModel model,String applicationId) {
		super(title, applicationId,model);//Fix : Passing applicationId as parameter to fetch correct owner stage.
		this.title = title;
		this.repairMethod = owner;
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.controller = new RepairMethodDialogController(model, this,owner);
		initComponents();
		if(this.title.equals(QiConstant.CREATE))
			loadCreateData();
		else if(this.title.equals(QiConstant.UPDATE))
			loadUpdateData();
		controller.initListeners();
	}
	
	private void loadCreateData(){	
			repairMethod = new QiRepairMethod();
			updateButton.setDisable(true);
			reasonForChangeTextArea.setDisable(true);		
	}
	
	private void loadUpdateData() {		
			createBtn.setDisable(true);
			repairMethodNameTextField.settext(repairMethod.getRepairMethod());
			repairMethodDescriptionTextArea.setText(repairMethod.getRepairMethodDescription());
			getActiveRadioBtn().setSelected(repairMethod.isActive());
			getInactiveRadioBtn().setSelected(!repairMethod.isActive());		
	}



	private void initComponents() {
		VBox outerPane = new VBox();
		HBox mainPane = new HBox();
		outerPane.setPrefHeight(300);
		HBox buttonContainer = new HBox();
		HBox repairMethodNameLabelContainer = new HBox();
		HBox repairMethodDescLabelContainer = new HBox();
		HBox reasonForChangeContainer = new HBox();
		HBox radioButtonContainer = createStatusRadioButtons(getController());
		
		repairMethodDescriptionTextArea = UiFactory.createTextArea();
		repairMethodDescriptionTextArea.setPrefRowCount(2);	
		repairMethodDescriptionTextArea.setWrapText(true);
		reasonForChangeTextArea = UiFactory.createTextArea();
		reasonForChangeTextArea.setPrefRowCount(2);
		reasonForChangeTextArea.setWrapText(true);
		
		mainPane.getStyleClass().add("main-pane");
		repairMethodNameLabelContainer.setAlignment(Pos.CENTER_LEFT);
		repairMethodNameLabelContainer.setPadding(new Insets(10, 10, 10, 10));
		repairMethodNameLabelContainer.setSpacing(10);

		setRepairMethodName(repairMethodNameLabelContainer);
		
		setRepairMethodDescLabelContainer(repairMethodDescLabelContainer);
		
		setReasonForChangeContainer(reasonForChangeContainer);
			
		createBtn = createBtn(QiConstant.CREATE, getController());
		createBtn.setPadding(new Insets(5,5,5,5));
		updateButton = createBtn(QiConstant.UPDATE,getController());
		cancelBtn = createBtn(QiConstant.CANCEL,getController());
		
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(5,5,5,5));
		buttonContainer.setSpacing(10);
		buttonContainer.getChildren().addAll(createBtn, updateButton, cancelBtn);
		
		outerPane.getChildren().addAll(radioButtonContainer,repairMethodNameLabelContainer, repairMethodDescLabelContainer,reasonForChangeContainer,buttonContainer);
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
		
	}

	private void setRepairMethodDescLabelContainer(HBox repaiMethodDescLabelContainer) {
		HBox repairMethoDescription = new HBox();
		LoggedLabel repairMethodDescLabel = UiFactory.createLabel("repairMethodDescription","Repair Method Description");
		repairMethodDescLabel.getStyleClass().add("display-label");
		repairMethoDescription.getChildren().add(repairMethodDescLabel);
		repaiMethodDescLabelContainer.setPadding(new Insets(10, 10, 10, 10));
		repaiMethodDescLabelContainer.setSpacing(10);
		repaiMethodDescLabelContainer.getChildren().addAll(repairMethoDescription, repairMethodDescriptionTextArea);
	}

	private void setReasonForChangeContainer(HBox reasonForChangeContainer) {
		HBox reasonForChange = new HBox();
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("reasonForChange","Reason for Change");
		reasonForChangeLabel.getStyleClass().add("display-label");
		LoggedLabel asterisk1 = UiFactory.createLabel("asteriskReasonForChange", "*");
		asterisk1.getStyleClass().add("display-label");
		asterisk1.setStyle("-fx-text-fill: red");
		reasonForChange.getChildren().addAll(reasonForChangeLabel,asterisk1);
		reasonForChangeContainer.setPadding(new Insets(10, 10, 10, 10));
		reasonForChangeContainer.setSpacing(47);
		reasonForChangeContainer.getChildren().addAll(reasonForChange, reasonForChangeTextArea);
	}

	private void setRepairMethodName(HBox repaiMethodNameLabelContainer) {
		HBox repairMethodName = new HBox();
		LoggedLabel repairMethodNameLabel = UiFactory.createLabel("repairMethodName", "Repair Method Name");
		repairMethodNameLabel.getStyleClass().add("display-label");
		LoggedLabel asterisk = UiFactory.createLabel("asteriskRepairMethodName", "*");
		asterisk.getStyleClass().add("display-label");
		asterisk.setStyle("-fx-text-fill: red");
		repairMethodName.getChildren().addAll(repairMethodNameLabel,asterisk);
		repairMethodNameTextField = UiFactory.createUpperCaseFieldBean("repairMethodNameTextField", 40, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		repaiMethodNameLabelContainer.setPadding(new Insets(10, 10, 10, 10));
		repaiMethodNameLabelContainer.setSpacing(37);
		repaiMethodNameLabelContainer.getChildren().addAll(repairMethodName, repairMethodNameTextField);
	}

	public ObjectTablePane<QiRepairMethod> getRepairMethodPane() {
		return repairMethodPane;
	}


	public void setRepairMethodPane(ObjectTablePane<QiRepairMethod> repairMethodPane) {
		this.repairMethodPane = repairMethodPane;
	}

	public LoggedButton getCreateBtn() {
		return createBtn;
	}


	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public LoggedLabel getRepairMethodNameLabel() {
		return repairMethodNameLabel;
	}


	public void setRepairMethodNameLabel(LoggedLabel repairMethodNameLabel) {
		this.repairMethodNameLabel = repairMethodNameLabel;
	}


	public LoggedLabel getRepairMethodDescriptionLabel() {
		return repairMethodDescriptionLabel;
	}


	public void setRepairMethodDescriptionLabel(LoggedLabel repairMethodDescriptionLabel) {
		this.repairMethodDescriptionLabel = repairMethodDescriptionLabel;
	}


	public LoggedTextArea getRepairMethodDescriptionTextArea() {
		return repairMethodDescriptionTextArea;
	}


	public void setRepairMethodDescriptionTextArea(LoggedTextArea repairMethodDescriptionTextArea) {
		this.repairMethodDescriptionTextArea = repairMethodDescriptionTextArea;
	}


	public UpperCaseFieldBean getRepairMethodNameTextField() {
		return repairMethodNameTextField;
	}


	public void setRepairMethodNameTextField(UpperCaseFieldBean repairMethodNameTextField) {
		this.repairMethodNameTextField = repairMethodNameTextField;
	}


	public RepairMethodDialogController getController() {
		return controller;
	}


	public void setController(RepairMethodDialogController controller) {
		this.controller = controller;
	}

	public LoggedTextArea getReasonForChangeTextArea() {
		return reasonForChangeTextArea;
	}

	public void setReasonForChangeTextArea(LoggedTextArea reasonForChangeTextArea) {
		this.reasonForChangeTextArea = reasonForChangeTextArea;
	}
}
