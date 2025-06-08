package com.honda.galc.client.teamleader.qi.view;

import com.honda.galc.client.teamleader.qi.controller.DocumentDialogController;
import com.honda.galc.client.teamleader.qi.model.DocumentMaintenanceModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiDocument;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * 
 * <h3>DocumentDialog Class description</h3>
 * <p>
 * DocumentDialog description
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
 *         February 20, 2020
 *
 */

public class DocumentDialog extends QiFxDialog<DocumentMaintenanceModel> {
	private LoggedButton createBtn;
	private LoggedButton cancelBtn;
	private LoggedButton testLinkBtn;

	private LoggedTextField documentNameTextField;
	private LoggedTextField documentLinkTextField;
	private LoggedTextArea descriptionTextArea;
	private LoggedTextArea reasonForChangeTextArea;

	private String title;
	private DocumentDialogController controller;
	private QiDocument document;

	public DocumentDialog(String title, QiDocument oldDocument, DocumentMaintenanceModel model, String applicationId) {
		super(title, applicationId, model);
		this.title = title;
		this.document = oldDocument;
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.controller = new DocumentDialogController(model, this, oldDocument);
		initComponents();
		if (this.title.equals(QiConstant.CREATE)) {
			loadCreateData();
		} else if (this.title.equals(QiConstant.UPDATE)) {
			loadUpdateData();
		}
		controller.initListeners();
	}

	private void loadCreateData() {
		document = new QiDocument();
		updateButton.setDisable(true);
		reasonForChangeTextArea.setDisable(true);
	}

	private void loadUpdateData() {
		createBtn.setDisable(true);
		updateButton.setDisable(false);
		documentNameTextField.setText(document.getDocumentName());
		documentLinkTextField.setText(document.getDocumentLink());
		descriptionTextArea.setText(document.getDescription());
	}

	private void initComponents() {
		VBox outerPane = new VBox();
		outerPane.setPrefHeight(300);
		HBox buttonContainer = new HBox();
		HBox documentNameContainer = new HBox();
		HBox documentLinkContainer = new HBox();
		HBox descriptionContainer = new HBox();
		HBox reasonForChangeContainer = new HBox();

		documentNameTextField = UiFactory.createTextField("documentNameTextField", 45, Fonts.SS_DIALOG_PLAIN(12),
				TextFieldState.EDIT, Pos.BASELINE_LEFT);
		documentLinkTextField = UiFactory.createTextField("documentLinkTextField", 45, Fonts.SS_DIALOG_PLAIN(12),
				TextFieldState.EDIT, Pos.BASELINE_LEFT);

		descriptionTextArea = UiFactory.createTextArea();
		descriptionTextArea.setPrefRowCount(2);
		descriptionTextArea.setWrapText(true);
		reasonForChangeTextArea = UiFactory.createTextArea();
		reasonForChangeTextArea.setPrefRowCount(2);
		reasonForChangeTextArea.setWrapText(true);

		setDocumentNameContainer(documentNameContainer);
		setDocumentLinkContainer(documentLinkContainer);
		setDescriptionContainer(descriptionContainer);
		setReasonForChangeContainer(reasonForChangeContainer);

		createBtn = createBtn(QiConstant.CREATE, getController());
		updateButton = createBtn(QiConstant.UPDATE, getController());
		cancelBtn = createBtn(QiConstant.CANCEL, getController());
		testLinkBtn = createBtn(QiConstant.TEST_LINK, getController());

		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(5, 5, 5, 5));
		buttonContainer.setSpacing(7);
		buttonContainer.getChildren().addAll(createBtn, updateButton, cancelBtn, testLinkBtn);

		outerPane.setPadding(new Insets(20, 1, 1, 1));
		outerPane.getChildren().addAll(documentNameContainer, documentLinkContainer, descriptionContainer,
				reasonForChangeContainer, buttonContainer);
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
	}

	private void setDocumentNameContainer(HBox documentNameContainer) {
		HBox documentNameHBox = new HBox();
		LoggedLabel documentNameLabel = UiFactory.createLabel("documentName", "Document Name");
		documentNameLabel.getStyleClass().add("display-label");
		LoggedLabel asterisk = UiFactory.createLabel("asteriskDocumentName", "*");
		asterisk.getStyleClass().add("display-label");
		asterisk.setStyle("-fx-text-fill: red");
		documentNameHBox.getChildren().addAll(documentNameLabel, asterisk);
		documentNameContainer.setPadding(new Insets(10, 10, 10, 10));
		documentNameContainer.setSpacing(10);
		documentNameContainer.getChildren().addAll(documentNameHBox, documentNameTextField);
	}

	private void setDocumentLinkContainer(HBox documentLinkContainer) {
		HBox documentLinkHBox = new HBox();
		LoggedLabel documentLinkLabel = UiFactory.createLabel("documentLink", "Document Link");
		documentLinkLabel.getStyleClass().add("display-label");
		LoggedLabel asterisk = UiFactory.createLabel("asteriskDocumentLink", "*");
		asterisk.getStyleClass().add("display-label");
		asterisk.setStyle("-fx-text-fill: red");
		documentLinkHBox.getChildren().addAll(documentLinkLabel, asterisk);
		documentLinkContainer.setPadding(new Insets(10, 10, 10, 10));
		documentLinkContainer.setSpacing(20);
		documentLinkContainer.getChildren().addAll(documentLinkHBox, documentLinkTextField);
	}

	private void setDescriptionContainer(HBox descriptionContainer) {
		HBox descriptionLabelHBox = new HBox();
		LoggedLabel descriptionLabel = UiFactory.createLabel("description", "Description");
		descriptionLabel.getStyleClass().add("display-label");
		descriptionLabelHBox.getChildren().add(descriptionLabel);
		descriptionContainer.setPadding(new Insets(10, 10, 10, 10));
		descriptionContainer.setSpacing(48);
		descriptionContainer.getChildren().addAll(descriptionLabelHBox, descriptionTextArea);
	}

	private void setReasonForChangeContainer(HBox reasonForChangeContainer) {
		HBox reasonForChange = new HBox();
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("reasonForChange", "Reason for Change");
		reasonForChangeLabel.getStyleClass().add("display-label");
		LoggedLabel asterisk1 = UiFactory.createLabel("asteriskReasonForChange", "*");
		asterisk1.getStyleClass().add("display-label");
		asterisk1.setStyle("-fx-text-fill: red");
		reasonForChange.getChildren().addAll(reasonForChangeLabel, asterisk1);
		reasonForChangeContainer.setPadding(new Insets(10, 10, 10, 10));
		reasonForChangeContainer.setSpacing(1);
		reasonForChangeContainer.getChildren().addAll(reasonForChange, reasonForChangeTextArea);
	}

	public LoggedButton getCreateBtn() {
		return createBtn;
	}

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public LoggedButton getTestLinkBtn() {
		return testLinkBtn;
	}

	public LoggedTextArea getDescriptionTextArea() {
		return descriptionTextArea;
	}

	public void setDescriptionTextArea(LoggedTextArea descriptionTextArea) {
		this.descriptionTextArea = descriptionTextArea;
	}

	public LoggedTextField getDocumentNameTextField() {
		return documentNameTextField;
	}

	public void setDocumentNameTextField(LoggedTextField documentNameTextField) {
		this.documentNameTextField = documentNameTextField;
	}

	public LoggedTextField getDocumentLinkTextField() {
		return documentLinkTextField;
	}

	public void setDocumentLinkTextField(LoggedTextField documentLinkTextField) {
		this.documentLinkTextField = documentLinkTextField;
	}

	public DocumentDialogController getController() {
		return controller;
	}

	public void setController(DocumentDialogController controller) {
		this.controller = controller;
	}

	public LoggedTextArea getReasonForChangeTextArea() {
		return reasonForChangeTextArea;
	}

	public void setReasonForChangeTextArea(LoggedTextArea reasonForChangeTextArea) {
		this.reasonForChangeTextArea = reasonForChangeTextArea;
	}
}
