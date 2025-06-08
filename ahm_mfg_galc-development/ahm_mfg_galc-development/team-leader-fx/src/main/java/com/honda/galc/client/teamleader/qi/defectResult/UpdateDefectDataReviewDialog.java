package com.honda.galc.client.teamleader.qi.defectResult;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.view.QiFxDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiRepairResult;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

public class UpdateDefectDataReviewDialog extends QiFxDialog<DefectResultMaintModel> {

	private LoggedButton changeButton;
	private LoggedButton cancelButton;
	private LabeledTextField approverTextField;
	private UpdateDefectController controller;
	private LoggedTextArea reasonForChangeTextArea;
	private LoggedLabel reasonForChangeErrorMessage;
	private String gdpDefects;
	private String trpuDefects;
	public UpdateDefectDataReviewDialog(String title, String applicationId, DefectResultMaintModel model, List<QiRepairResult> selectedRepairResultList, String typeOfChange,
			List<QiDefectResult> selectedDefectResultList, String gdpDefects, String trpuDefects, LocalAttributeMaintDialog localAttributeMaintDialog) {
		super(title, applicationId, model);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.gdpDefects=gdpDefects;
		this.trpuDefects=trpuDefects;
		this.controller = new UpdateDefectController(model, this,selectedRepairResultList,typeOfChange,selectedDefectResultList,localAttributeMaintDialog);
		initComponents();
	}

	/**
	 * This method is used to initialize the Components and add those to Migpane
	 */
	private void initComponents() {
		double screenResolutionWidth=Screen.getPrimary().getVisualBounds().getWidth();
		double screenResolutionHeight=Screen.getPrimary().getVisualBounds().getHeight();
		MigPane migPane = new MigPane();
		migPane.setPrefWidth(screenResolutionWidth/2.3);
		migPane.setPrefHeight(screenResolutionHeight/2.5);		
		getDefectResultData();
		migPane.add(getReasonForChangeMigPane(),"wrap");
		migPane.add(createButtonContainer(),"wrap");
		migPane.add(reasonForChangeErrorMessage);
		((BorderPane) this.getScene().getRoot()).setCenter(migPane);
	}
	
	private void getDefectResultData(){
		reasonForChangeErrorMessage = UiFactory.createLabel("reasonForChangeErrorMessage", "");
	}

	/**
	 * This method is used to create the Reason for change TextArea
	 * @return
	 */
	private LoggedTextArea getReasonForChangeTextArea(){
		reasonForChangeTextArea = UiFactory.createTextArea();
		reasonForChangeTextArea.setPrefRowCount(4);
		reasonForChangeTextArea.setWrapText(true);
		reasonForChangeTextArea.setPrefWidth(300);
		reasonForChangeTextArea.setPrefHeight(120);
		reasonForChangeTextArea.addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(80));
		return reasonForChangeTextArea;
	}

	/**
	 * This method is used to create the Reason for change label
	 * @return
	 */
	private MigPane getReasonForChangeMigPane() {
		MigPane reasonForChangeMigPane = new MigPane("insets 10 20 20 10");
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("reasonForChangeLabel", "Comment");
		reasonForChangeLabel.getStyleClass().add("display-label");
		LoggedLabel asterisk = UiFactory.createLabel("asteriskReasonForChange", "*             ");
		asterisk.getStyleClass().add("display-label");
		asterisk.setStyle("-fx-text-fill: red");
		
		approverTextField = new LabeledTextField("Correction Requested By",true,true);
		approverTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(32));
		approverTextField.getControl().setPrefWidth(257); 
		approverTextField.getLabel().setPrefWidth(150); 
		
		HBox labelHBox = new HBox();
		labelHBox.getChildren().addAll(reasonForChangeLabel, asterisk);
		
		reasonForChangeMigPane.add(labelHBox);
		reasonForChangeMigPane.add(getReasonForChangeTextArea(), "span");
		reasonForChangeMigPane.add(approverTextField, "span");
				
		return reasonForChangeMigPane;
	}
		

	private MigPane createButtonContainer() {
		MigPane buttonContainer = new MigPane();
		this.changeButton = createBtn("Apply Change", getController());
		changeButton.setPadding(new Insets(5, 5, 5, 5));
		this.cancelButton = createBtn(QiConstant.CANCEL, getController());
		cancelButton.setPadding(new Insets(5, 5, 5, 5));
		buttonContainer.setPadding(new Insets(0, 0, 5, 0));
		buttonContainer.getChildren().addAll(cancelButton,changeButton);
		return buttonContainer;
	}
	
	public String getTrpuDefects() {
		return trpuDefects;
	}

	public String getGdpDefects() {
		return gdpDefects;
	}

	public String getReasonForChangeText()
	{
		return StringUtils.trimToEmpty(reasonForChangeTextArea.getText());
	}
	
	public String getApproverText() {
		return StringUtils.trimToEmpty(approverTextField.getText());
	}
	
	public LoggedTextArea getReasonForChange() {
		return reasonForChangeTextArea;
	}

	public UpdateDefectController getController() {
		return this.controller;
	}

	public void setController(UpdateDefectController controller) {
		this.controller = controller;
	}

	public LoggedLabel getReasonForChangeErrorMessage() {
		return reasonForChangeErrorMessage;
	}
	public void setReasonForChangeErrorMessage(
			LoggedLabel reasonForChangeErrorMessage) {
		this.reasonForChangeErrorMessage = reasonForChangeErrorMessage;
	}
	
	public LabeledTextField getApproverTextField() {
		return approverTextField;
	}

	public LoggedButton getChangeButton() {
		return changeButton;
	}

	public LoggedButton getCancelButton() {
		return cancelButton;
	}
		
	public void setUserOperationMessage(final String message){
		EventBusUtil.publish(new StatusMessageEvent(message, StatusMessageEventType.INFO));
	}
	ChangeListener<String> reasonForChangeDeptComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) { 
		} 
	};
	
	ChangeListener<String> reasonForChangeCategoryComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) { 
		} 
	};
	
	
}
