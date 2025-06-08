package com.honda.galc.client.teamleader.qi.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.honda.galc.client.teamleader.qi.controller.IqsVersionDialogController;
import com.honda.galc.client.teamleader.qi.model.IqsMaintenanceModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiIqsVersion;

/**
 * 
 * <h3>IqsVersionDialog Class description</h3>
 * <p>
 * IqsVersionDialog is used to display version dialog popup screen 
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
 * @author L&TInfotech<br>
 *        Sep 06, 2016
 * 
 */

public class IqsVersionDialog extends QiFxDialog<IqsMaintenanceModel> {

	private String title;
	private IqsVersionDialogController controller;
	private QiIqsVersion iqsVersion;

	private LoggedButton createBtn;
	private LoggedButton cancelBtn;

	private UpperCaseFieldBean iqsVersionTextField;
	private LoggedTextArea iqsReasonTextArea;
	private LoggedLabel iqsVersionLabel;
	private VBox outerPane = new VBox();
	private LoggedLabel msgLabel;

	public IqsVersionDialog(String title, QiIqsVersion iqsVersion, IqsMaintenanceModel model,String applicationId) {
		super(title, applicationId,model);//Fix : Passing applicationId as parameter to fetch correct owner stage.
		this.title = title;
		this.controller = new IqsVersionDialogController(model,this,iqsVersion);
		this.iqsVersion = iqsVersion;
		initComponents();
		controller.initListeners();
	}

	private void initComponents(){
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		HBox iqsVersionFieldContainer = new HBox();
		outerPane.setPrefHeight(200);
		iqsVersionFieldContainer.setSpacing(20);
		iqsVersionFieldContainer.setPadding(new Insets(10));
		iqsVersionFieldContainer.setAlignment(Pos.CENTER_LEFT);

		HBox iqsReasonContainer = new HBox();
		iqsReasonContainer.setSpacing(20);
		iqsReasonContainer.setPadding(new Insets(10));
		iqsReasonContainer.setAlignment(Pos.CENTER_LEFT);

		HBox iqsButtonContainer = new HBox();
		iqsButtonContainer.setSpacing(20);
		iqsButtonContainer.setPadding(new Insets(10));
		iqsButtonContainer.setAlignment(Pos.CENTER);

		HBox versionLabelBox = new HBox();
		HBox labelBox = new HBox();

		createLabel(versionLabelBox, labelBox);

		createInnerComponent(iqsVersionFieldContainer, iqsReasonContainer,
				iqsButtonContainer, versionLabelBox, labelBox);

		HBox msgBox = createErrorLabel();
		
		outerPane.getChildren().addAll(iqsVersionFieldContainer,iqsReasonContainer,iqsButtonContainer,msgBox);
		outerPane.setMinHeight(74);
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
	}
	/**
	 * This method is to create error label
	 * @return
	 */
	private HBox createErrorLabel() {
		HBox msgBox= new HBox();
		msgBox.setPadding(new Insets(10, 10, 10, 10));
		msgLabel = UiFactory.createLabel("messageLabel");
		msgLabel.setWrapText(true);
		msgLabel.setPrefWidth(400);
		msgLabel.setAlignment(Pos.CENTER);
		msgBox.getChildren().add(msgLabel);
		msgBox.setAlignment(Pos.CENTER);
		return msgBox;
	}
	
	/**
	 * This method creates labels on popup screen
	 * @param versionLabelBox
	 * @param labelBox
	 */
	private void createLabel(HBox versionLabelBox, HBox labelBox) {
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("iqsReasonLabel", "Reason for Change");
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		asterisk.setStyle("-fx-text-fill: red");
		labelBox.getChildren().addAll(reasonForChangeLabel,asterisk);

		iqsVersionLabel = UiFactory.createLabel("iqsVersionLabel", "IQS Version");
		iqsVersionLabel.setPadding(new Insets(0,0,0,40));
		LoggedLabel asterisk1 = UiFactory.createLabel("label", "*");
		asterisk1.setStyle("-fx-text-fill: red");
		versionLabelBox.getChildren().addAll(iqsVersionLabel,asterisk1);

		iqsVersionTextField = UiFactory.createUpperCaseFieldBean("iqsVersionTextField", 17, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		iqsReasonTextArea = UiFactory.createTextArea();
		iqsReasonTextArea.setPrefRowCount(3);
		iqsReasonTextArea.setPrefColumnCount(17);
		iqsReasonTextArea.setWrapText(true);
	}
	/**
	 * Method to create inner components and add it to container
	 * @param iqsVersionFieldContainer
	 * @param iqsReasonContainer
	 * @param iqsButtonContainer
	 * @param versionLabelBox
	 * @param labelBox
	 */
	private void createInnerComponent(HBox iqsVersionFieldContainer,
			HBox iqsReasonContainer, HBox iqsButtonContainer,
			HBox versionLabelBox, HBox labelBox) {
		createBtn = createBtn(QiConstant.CREATE, getController());
		updateButton = createBtn(QiConstant.UPDATE,getController());
		cancelBtn = createBtn(QiConstant.CANCEL,getController());

		if(title.equalsIgnoreCase(QiConstant.CREATE)){
			updateButton.setDisable(true);
			iqsReasonTextArea.setDisable(true);
		}

		if(title.equalsIgnoreCase(QiConstant.UPDATE)){
			iqsVersionTextField.settext(iqsVersion.getIqsVersion());
			createBtn.setDisable(true);
		}

		iqsVersionFieldContainer.getChildren().addAll(versionLabelBox,iqsVersionTextField);
		iqsReasonContainer.getChildren().addAll(labelBox,iqsReasonTextArea);
		iqsButtonContainer.getChildren().addAll(createBtn,updateButton,cancelBtn);
	}

	public LoggedLabel getIqsVersionLabel() {
		return iqsVersionLabel;
	}

	public void setIqsVersionLabel(LoggedLabel iqsVersionLabel) {
		this.iqsVersionLabel = iqsVersionLabel;
	}

	public IqsVersionDialogController getController() {
		return controller;
	}

	public void setController(IqsVersionDialogController controller) {
		this.controller = controller;
	}

	public LoggedButton getCreateBtn() {
		return createBtn;
	}

	public void setCreateBtn(LoggedButton createBtn) {
		this.createBtn = createBtn;
	}

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public void setCancelBtn(LoggedButton cancelBtn) {
		this.cancelBtn = cancelBtn;
	}

	public QiIqsVersion getIqsVersion() {
		return iqsVersion;
	}

	public void setIqsVersion(QiIqsVersion iqsVersion) {
		this.iqsVersion = iqsVersion;
	}

	public UpperCaseFieldBean getIqsVersionTextField() {
		return iqsVersionTextField;
	}

	public void setIqsVersionTextField(UpperCaseFieldBean iqsVersionTextField) {
		this.iqsVersionTextField = iqsVersionTextField;
	}

	public LoggedTextArea getIqsReasonTextArea() {
		return iqsReasonTextArea;
	}

	public void setIqsReasonTextArea(LoggedTextArea iqsReasonTextArea) {
		this.iqsReasonTextArea = iqsReasonTextArea;
	}

	public VBox getOuterPane() {
		return outerPane;
	}

	public void setOuterPane(VBox outerPane) {
		this.outerPane = outerPane;
	}

	public LoggedLabel getMsgLabel() {
		return msgLabel;
	}

	public void setMsgLabel(LoggedLabel msgLabel) {
		this.msgLabel = msgLabel;
	}

}
