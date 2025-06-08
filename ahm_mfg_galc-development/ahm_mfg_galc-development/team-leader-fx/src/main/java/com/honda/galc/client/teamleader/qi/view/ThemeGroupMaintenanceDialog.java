package com.honda.galc.client.teamleader.qi.view;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import com.honda.galc.client.teamleader.qi.controller.ThemeGroupMaintenanceDialogController;
import com.honda.galc.client.teamleader.qi.model.ThemeMaintenanceModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiThemeGroup;
import com.honda.galc.entity.qi.QiThemeGrouping;

public class ThemeGroupMaintenanceDialog extends QiFxDialog<ThemeMaintenanceModel> {

	private LoggedButton createBtn;
	private LoggedButton cancelBtn;
	private LoggedLabel themeGroupNameLabel;
	private LoggedLabel themeGroupDescriptionLabel;
	private UpperCaseFieldBean themeGroupNameTextField;
	private UpperCaseFieldBean themeGroupDescriptionTextArea;
	private LoggedTextArea reasonForChangeTextArea;	
	private ThemeGroupMaintenanceDialogController themeGroupDialogController;
	QiThemeGroup qiThemeGroup;
	private ObjectTablePane<QiThemeGrouping> themeGroupingTablePane;

	public ThemeGroupMaintenanceDialog(String title, QiThemeGroup qiThemeGroup,ThemeMaintenanceModel thememodel,String applicationId,ObjectTablePane<QiThemeGrouping> themeGroupingTablePane) {
		super(title, applicationId,thememodel);
		this.themeGroupDialogController = new ThemeGroupMaintenanceDialogController(thememodel,this,qiThemeGroup);
		this.qiThemeGroup = qiThemeGroup;
		this.themeGroupingTablePane=themeGroupingTablePane;
		this.getScene().getStylesheets().add(com.honda.galc.client.utils.QiConstant.CSS_PATH);
		initComponents(title);
		if(title.equals(QiConstant.CREATE))
			loadCreateData();
		else if(title.equals(QiConstant.UPDATE))
			loadUpdateData();
		themeGroupDialogController.initListeners();
	}
	/**
	 * This method is used to initialize the components of Dialog screen.
	 */
	private void initComponents(String title) {
		VBox outerPane = new VBox();
		outerPane.setMaxWidth(500);
		outerPane.setPrefWidth(500);
		outerPane.getChildren().addAll(
				createStatusRadioButtons(getThemeGroupDialogController()),
				createThemeGroupNameContainer(),
				createThemeGroupDescriptionContainer(),
				createReasonForChangeContainer(title), createButtonContainer());
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
		((BorderPane) this.getScene().getRoot()).setBottom(initStatusMessagePane());

	}
	
	/**
	 * This method is used to load Create data.
	 */
	private void loadCreateData() {
		qiThemeGroup = new QiThemeGroup();
		updateButton.setDisable(true);
		reasonForChangeTextArea.setDisable(true);
	}

	/**
	 * This method is used to load Update data.
	 */
	private void loadUpdateData() {
		createBtn.setDisable(true);
		themeGroupNameTextField.settext(qiThemeGroup.getThemeGroupName());
		themeGroupDescriptionTextArea.setText(qiThemeGroup.getThemeGroupDescription());
		getActiveRadioBtn().setSelected(qiThemeGroup.isActive());
		getInactiveRadioBtn().setSelected(!qiThemeGroup.isActive());
	}
	
	/**
	 * Create the button container.
	 *
	 * @return the button container
	 */
	private HBox createButtonContainer() {
		HBox buttonContainer = new HBox();
		createBtn = createBtn(QiConstant.CREATE,getThemeGroupDialogController());
		createBtn.setPadding(new Insets(5,5,5,5));
		updateButton = createBtn(QiConstant.UPDATE,getThemeGroupDialogController());
		cancelBtn = createBtn(QiConstant.CANCEL,getThemeGroupDialogController());

		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(5,5,5,5));
		buttonContainer.setSpacing(10);
		buttonContainer.getChildren().addAll(createBtn, updateButton, cancelBtn);
		return buttonContainer;
	}
	
	/**
	 * Create the theme group name container.
	 *
	 * @return the theme group name container
	 */
	private HBox createThemeGroupNameContainer() {
		HBox themeGroupNameContainer = new HBox();
		HBox themeLabelBox = new HBox(10);
		themeLabelBox.setMaxWidth(145);
		themeLabelBox.setPrefWidth(145);
		themeGroupNameLabel= UiFactory.createLabel("label", "Theme Group Name", "", TextAlignment.RIGHT);
		
		LoggedLabel asterisk = UiFactory.createLabel("label", "*", "", TextAlignment.RIGHT);
		asterisk.setStyle("-fx-text-fill: red");
		themeLabelBox.getChildren().addAll(themeGroupNameLabel,asterisk);
		themeGroupNameTextField = UiFactory.createUpperCaseFieldBean("filter-textfield", 20, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		themeGroupNameTextField.setFixedLength(true);
		themeGroupNameTextField.setMaximumLength(5);
		themeGroupNameContainer.setSpacing(15);
		themeGroupNameContainer.setPadding(new Insets(15));
		themeGroupNameContainer.getChildren().addAll(themeLabelBox,themeGroupNameTextField);
		return themeGroupNameContainer;
	}
	
	/**
	 * Create the theme group description container.
	 *
	 * @return the theme group description container
	 */
	private HBox createThemeGroupDescriptionContainer() {
		HBox themeGroupDescriptionContainer = new HBox();
		themeGroupDescriptionLabel= UiFactory.createLabel("label", "Theme Group Description", "", TextAlignment.RIGHT, 150);
		themeGroupDescriptionTextArea = UiFactory.createUpperCaseFieldBean("filter-textfield", 20, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		themeGroupDescriptionContainer.setSpacing(20);
		themeGroupDescriptionContainer.setPadding(new Insets(5));
		themeGroupDescriptionContainer.getChildren().addAll(themeGroupDescriptionLabel,themeGroupDescriptionTextArea);
		return themeGroupDescriptionContainer;
	}
	
	/**
	 * Create the reason for change container.
	 *
	 * @return the reason for change container
	 */
	private HBox createReasonForChangeContainer(String title) {
		HBox reasonForChangeContainer = new HBox();
		reasonForChangeTextArea = UiFactory.createTextArea();
		reasonForChangeTextArea.setPrefRowCount(2);
		reasonForChangeTextArea.setPrefColumnCount(25);
	
		HBox labelBox = new HBox(10);
		labelBox.setMaxWidth(155);
		labelBox.setPrefWidth(155);
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("label", "Reason for change", "", TextAlignment.RIGHT);
		LoggedLabel asterisk = new LoggedLabel();
		if(title.equals(QiConstant.UPDATE)) {
			asterisk = UiFactory.createLabel("label", "*", "", TextAlignment.RIGHT);
			asterisk.setStyle("-fx-text-fill: red");
		}
		labelBox.getChildren().addAll(reasonForChangeLabel,asterisk);

		reasonForChangeContainer.setPadding(new Insets(20, 10, 10, 10));
		reasonForChangeContainer.setSpacing(10);
		reasonForChangeContainer.getChildren().addAll(labelBox, reasonForChangeTextArea);
		
		return reasonForChangeContainer;
	}

	public ThemeGroupMaintenanceDialogController getThemeGroupDialogController() {
		return themeGroupDialogController;
	}
	public void setThemeGroupDialogController(
			ThemeGroupMaintenanceDialogController themeGroupDialogController) {
		this.themeGroupDialogController = themeGroupDialogController;
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
	public LoggedLabel getThemeGroupNameLabel() {
		return themeGroupNameLabel;
	}
	public void setThemeGroupNameLabel(LoggedLabel themeGroupNameLabel) {
		this.themeGroupNameLabel = themeGroupNameLabel;
	}
	public LoggedLabel getThemeGroupDescriptionLabel() {
		return themeGroupDescriptionLabel;
	}
	public void setThemeGroupDescriptionLabel(LoggedLabel themeGroupDescriptionLabel) {
		this.themeGroupDescriptionLabel = themeGroupDescriptionLabel;
	}
	public UpperCaseFieldBean getThemeGroupNameTextField() {
		return themeGroupNameTextField;
	}
	public void setThemeGroupNameTextField(
			UpperCaseFieldBean themeGroupNameTextField) {
		this.themeGroupNameTextField = themeGroupNameTextField;
	}
	
	public UpperCaseFieldBean getThemeGroupDescriptionTextArea() {
		return themeGroupDescriptionTextArea;
	}
	public void setThemeGroupDescriptionTextArea(
			UpperCaseFieldBean themeGroupDescriptionTextArea) {
		this.themeGroupDescriptionTextArea = themeGroupDescriptionTextArea;
	}
	public LoggedTextArea getReasonForChangeTextArea() {
		return reasonForChangeTextArea;
	}
	public void setReasonForChangeTextArea(LoggedTextArea reasonForChangeTextArea) {
		this.reasonForChangeTextArea = reasonForChangeTextArea;
	}
	public ObjectTablePane<QiThemeGrouping> getThemeGroupingTablePane() {
		return themeGroupingTablePane;
	}
	public void setThemeGroupingTablePane(
			ObjectTablePane<QiThemeGrouping> themeGroupingTablePane) {
		this.themeGroupingTablePane = themeGroupingTablePane;
	}

}


