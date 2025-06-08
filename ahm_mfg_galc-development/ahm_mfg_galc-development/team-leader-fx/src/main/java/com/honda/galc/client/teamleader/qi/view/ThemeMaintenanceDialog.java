package com.honda.galc.client.teamleader.qi.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import com.honda.galc.client.teamleader.qi.controller.ThemeMaintenanceDialogController;
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
import com.honda.galc.entity.qi.QiTheme;
import com.honda.galc.entity.qi.QiThemeGrouping;

public class ThemeMaintenanceDialog extends QiFxDialog<ThemeMaintenanceModel> {

	private LoggedButton createBtn;
	private LoggedButton cancelBtn;
	private LoggedLabel themeNameLabel;
	private LoggedLabel themeDescriptionLabel;
	private UpperCaseFieldBean themeNameTextField;
	private UpperCaseFieldBean themeDescriptionTextArea;
	private LoggedTextArea reasonForChangeTextArea;	
	private ThemeMaintenanceDialogController themeDialogController;
	QiTheme qiTheme;
	private ObjectTablePane<QiThemeGrouping> themeGroupingTablePane;

	public ThemeMaintenanceDialog(String title, QiTheme qiTheme,ThemeMaintenanceModel themeModel,String applicationId,ObjectTablePane<QiThemeGrouping> themeGroupingTablePane) {
		super(title, applicationId,themeModel);//Fix : Passing applicationId as parameter to fetch correct owner stage.
		this.themeDialogController = new ThemeMaintenanceDialogController(themeModel,this,qiTheme);
		this.qiTheme = qiTheme;
		this.themeGroupingTablePane=themeGroupingTablePane;
		this.getScene().getStylesheets().add(com.honda.galc.client.utils.QiConstant.CSS_PATH);
		initComponents(title);
		if(title.equals(QiConstant.CREATE))
			loadCreateData();
		else if(title.equals(QiConstant.UPDATE))
			loadUpdateData();
		themeDialogController.initListeners();
	}
	/**
	 * This method is used to initialize the components of Dialog screen.
	 */
	private void initComponents(String title) {
		VBox outerPane = new VBox();
		outerPane.setMaxWidth(500);
		outerPane.setPrefWidth(500);
		outerPane.getChildren().addAll(
				createStatusRadioButtons(getThemeDialogController()),
				createThemeNameContainer(), createThemeDescriptionContainer(),
				createReasonForChangeContainer(title), createButtonContainer());
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
		((BorderPane) this.getScene().getRoot()).setBottom(initStatusMessagePane());

	}
	
	/**
	 * This method is used to load Create data.
	 */
	private void loadCreateData() {
		qiTheme = new QiTheme();
		updateButton.setDisable(true);
		reasonForChangeTextArea.setDisable(true);
	}

	/**
	 * This method is used to load Update data.
	 */
	private void loadUpdateData() {
		createBtn.setDisable(true);
		themeNameTextField.settext(qiTheme.getThemeName());
		themeDescriptionTextArea.setText(qiTheme.getThemeDescription());
		getActiveRadioBtn().setSelected(qiTheme.isActive());
		getInactiveRadioBtn().setSelected(!qiTheme.isActive());
	}
	
	/**
	 * create the button container.
	 *
	 * @return the button container
	 */
	private HBox createButtonContainer() {
		HBox buttonContainer = new HBox();
		createBtn = createBtn(QiConstant.CREATE, getThemeDialogController());
		createBtn.setPadding(new Insets(5,5,5,5));
		updateButton = createBtn(QiConstant.UPDATE,getThemeDialogController());
		cancelBtn = createBtn(QiConstant.CANCEL,getThemeDialogController());

		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(5,5,5,5));
		buttonContainer.setSpacing(10);
		buttonContainer.getChildren().addAll(createBtn, updateButton, cancelBtn);
		return buttonContainer;
	}
	
	/**
	 * Reason for change container.create
	 *
	 * @return the h box
	 */
	private HBox createReasonForChangeContainer(String title) {
		HBox reasonForChangeContainer = new HBox();
		reasonForChangeTextArea = UiFactory.createTextArea();
		reasonForChangeTextArea.setPrefRowCount(2);
		reasonForChangeTextArea.setPrefColumnCount(25);
	
		HBox labelBox = new HBox(10);
		labelBox.setMaxWidth(125);
		labelBox.setPrefWidth(125);
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("label", "Reason for change");
		LoggedLabel asterisk = new LoggedLabel();
		if(title.equals(QiConstant.UPDATE)) {
			asterisk = UiFactory.createLabel("label", "*");
			asterisk.setStyle("-fx-text-fill: red");
		}
		labelBox.getChildren().addAll(reasonForChangeLabel,asterisk);

		reasonForChangeContainer.setPadding(new Insets(20, 10, 10, 10));
		reasonForChangeContainer.setSpacing(10);
		reasonForChangeContainer.getChildren().addAll(labelBox, reasonForChangeTextArea);
		return reasonForChangeContainer;
	}
	
	/**
	 * create the theme name container.
	 *
	 * @return the theme name container
	 */
	private HBox createThemeNameContainer() {
		HBox themeNameContainer = new HBox();
		themeNameTextField = UiFactory.createUpperCaseFieldBean("filter-textfield", 20, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		themeNameTextField.setFixedLength(true);
		themeNameTextField.setMaximumLength(5);
		themeNameContainer.setSpacing(15);
		themeNameContainer.setPadding(new Insets(15));
		themeNameContainer.getChildren().addAll(createThemeLabelBox(),themeNameTextField);
		return themeNameContainer;
	}
	
	/**
	 * create the theme description container.
	 *
	 * @return the theme description container
	 */
	private HBox createThemeDescriptionContainer() {
		HBox themeDescriptionContainer = new HBox();
		themeDescriptionLabel= UiFactory.createLabel("label", "Theme Description", "", TextAlignment.RIGHT, 120);
		themeDescriptionTextArea = UiFactory.createUpperCaseFieldBean("filter-textfield", 20, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		themeDescriptionContainer.setSpacing(20);
		themeDescriptionContainer.setPadding(new Insets(5));
		themeDescriptionContainer.getChildren().addAll(themeDescriptionLabel,themeDescriptionTextArea);
		return themeDescriptionContainer;
	}
	
	/**
	 * create the theme label box.
	 *
	 * @return the theme label box
	 */
	private HBox createThemeLabelBox() {
		HBox hBox = new HBox();
		hBox.setMaxWidth(115);
		hBox.setPrefWidth(115);
		themeNameLabel= UiFactory.createLabel("label", "Theme Name");
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		asterisk.setStyle("-fx-text-fill: red");
		hBox.setPadding(new Insets(0,0,0,20));
		hBox.getChildren().addAll(themeNameLabel,asterisk);
		return hBox;
	}

	public ThemeMaintenanceDialogController getThemeDialogController() {
		return themeDialogController;
	}
	public void setThemeDialogController(
			ThemeMaintenanceDialogController themeDialogController) {
		this.themeDialogController = themeDialogController;
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
	public LoggedLabel getThemeNameLabel() {
		return themeNameLabel;
	}
	public void setThemeNameLabel(LoggedLabel themeNameLabel) {
		this.themeNameLabel = themeNameLabel;
	}
	public LoggedLabel getThemeDescriptionLabel() {
		return themeDescriptionLabel;
	}
	public void setThemeDescriptionLabel(LoggedLabel themeDescriptionLabel) {
		this.themeDescriptionLabel = themeDescriptionLabel;
	}
	public UpperCaseFieldBean getThemeNameTextField() {
		return themeNameTextField;
	}
	public void setThemeNameTextField(UpperCaseFieldBean themeNameTextField) {
		this.themeNameTextField = themeNameTextField;
	}
	
	public UpperCaseFieldBean getThemeDescriptionTextArea() {
		return themeDescriptionTextArea;
	}
	public void setThemeDescriptionTextArea(
			UpperCaseFieldBean themeDescriptionTextArea) {
		this.themeDescriptionTextArea = themeDescriptionTextArea;
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
	public void setThemeGroupingTablePane(ObjectTablePane<QiThemeGrouping> themeGroupingTablePane) {
		this.themeGroupingTablePane = themeGroupingTablePane;
	}
	
}

