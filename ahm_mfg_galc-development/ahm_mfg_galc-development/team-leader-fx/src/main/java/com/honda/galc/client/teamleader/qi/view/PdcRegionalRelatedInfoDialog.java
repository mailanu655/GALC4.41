package com.honda.galc.client.teamleader.qi.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.honda.galc.client.teamleader.qi.controller.PdcLocalAttributeMaintDialogController;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedText;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;

public class PdcRegionalRelatedInfoDialog extends VBox {

	private static PdcRegionalRelatedInfoDialog instance;
	private static PdcLocalAttributeMaintDialogController attributeMaintDialogController;
	private static PdcLocalAttributeMaintDialog localAttributeMaintDialog;
	private LoggedRadioButton reportableRadioButton;
	private LoggedRadioButton nonReportableRadioButton;
	private LoggedRadioButton defaultReportableRadioBtn;
	private LoggedText themeName;
	private LoggedText iqsVersion;
	private LoggedText iqsCategory;
	private LoggedText iqsQuestion;
	private LoggedComboBox<String> localThemeComboBox;
	private CheckBox reportableNonReportableCheckbox;
	private CheckBox localThemeCheckbox;
	private CheckBox fireEngineCheckbox;
	private LoggedRadioButton fireEngineYesRadioBtn;
	private LoggedRadioButton fireEngineNoRadioBtn;
	private ToggleGroup group = new ToggleGroup();
	private  static  boolean isMassUpdateBtn=false;
	
	private PdcRegionalRelatedInfoDialog() {}
	
	public static PdcRegionalRelatedInfoDialog getInstance(PdcLocalAttributeMaintDialogController dialogController, PdcLocalAttributeMaintDialog pdcLocalAttributeMaintDialog,boolean isMassUpdate) {
		if(instance == null) {
			instance = new PdcRegionalRelatedInfoDialog();
		}
		isMassUpdateBtn=isMassUpdate;
		attributeMaintDialogController = dialogController;
		localAttributeMaintDialog = pdcLocalAttributeMaintDialog;
		return instance;
	}
	
	public VBox createThemeDetails(){
		VBox vBoxAttributeMaint = new VBox();
		if(isMassUpdateBtn){
			vBoxAttributeMaint.getChildren().addAll(createReportableRadioButtons(),  createTrackingComboBox(), createFireEngineCheckBox());
		}else{
			vBoxAttributeMaint.getChildren().addAll(createReportableRadioButtons(), createThemeText(), createIqsVersionText(), createIqsCategoryText(), createIqsQuestionText(), createTrackingComboBox(), createFireEngineCheckBox());
		}
		return vBoxAttributeMaint;
	}
	
	/**
	 * Creates the reportable radio buttons.
	 *
	 * @return the hbox
	 */
	private HBox createReportableRadioButtons() {
		HBox radioBtnContainer = new HBox();
		VBox radioButtonLabel = new VBox();
		VBox radioButtonRows = new VBox();
		HBox radioBtnContainerRow1 = new HBox();
		HBox radioBtnContainerRow2 = new HBox();
		ToggleGroup group = new ToggleGroup();
		reportableRadioButton = localAttributeMaintDialog.createRadioButton(QiConstant.REPORTABLE, group, false, getAttributeMaintDialogController());
		nonReportableRadioButton = localAttributeMaintDialog.createRadioButton(QiConstant.NON_REPORTABLE, group, false, getAttributeMaintDialogController());
		defaultReportableRadioBtn = localAttributeMaintDialog.createRadioButton(QiConstant.DEFAULT_REPORTABLE, group, false, getAttributeMaintDialogController());
		radioBtnContainerRow1.getChildren().addAll(reportableRadioButton, nonReportableRadioButton);
		radioBtnContainerRow2.getChildren().addAll(defaultReportableRadioBtn);
		radioButtonRows.getChildren().addAll(radioBtnContainerRow1,radioBtnContainerRow2);
		radioBtnContainerRow2.setPadding(new Insets(5, 0, 0, 0));
		
		HBox labelBox = new HBox();
		LoggedLabel defectLabel = UiFactory.createLabel("defectLabel", "Defect Will Be ");
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		asterisk.setStyle("-fx-text-fill: red");
		labelBox.getChildren().addAll(defectLabel, asterisk);
		reportableNonReportableCheckbox=new CheckBox();
		if(isMassUpdateBtn && localAttributeMaintDialog.getButtonName().contains(QiConstant.UPDATE_ATTRIBUTE))  {
			radioButtonLabel.getChildren().addAll(reportableNonReportableCheckbox,labelBox);
		}
		else  {
			radioButtonLabel.getChildren().addAll(labelBox);
		}
		radioBtnContainer.getChildren().addAll(radioButtonLabel, radioButtonRows);
		radioBtnContainer.setAlignment(Pos.CENTER_LEFT);
		radioBtnContainer.setSpacing(5);
		radioBtnContainer.setPadding(new Insets(10, 0, 0, 5));
		return radioBtnContainer;
	}
	
	/**
	 * Creates the theme text.
	 *
	 * @return the h box
	 */
	private HBox createThemeText() {
		HBox hBoxTheme = new HBox();
		hBoxTheme.setMaxWidth(350);
		hBoxTheme.setMinWidth(350);
		hBoxTheme.setPadding(new Insets(5, 5, 5, 5));
		hBoxTheme.setSpacing(10);

		LoggedLabel themeLabel = UiFactory.createLabel("themeLabel", "Theme");
		this.themeName = UiFactory.createText();
		themeLabel.setMaxWidth(100);
		themeLabel.setMinWidth(100);
		hBoxTheme.setPadding(new Insets(10, 0, 0, 10));
		hBoxTheme.setSpacing(10);
		hBoxTheme.getChildren().addAll(themeLabel, this.themeName);
		return hBoxTheme;
	}
	
	/**
	 * Creates the iqs version text.
	 *
	 * @return the h box
	 */
	private HBox createIqsVersionText() {
		HBox hBoxIqsVersion = new HBox();
		hBoxIqsVersion.setPadding(new Insets(10, 10, 10, 10));
		hBoxIqsVersion.setSpacing(10);
		LoggedLabel iqsVersionLabel = UiFactory.createLabel("iqsVersionLabel", "IQS Version");
		this.iqsVersion = UiFactory.createText();
		iqsVersionLabel.setMaxWidth(100);
		iqsVersionLabel.setMinWidth(100);
		hBoxIqsVersion.setPadding(new Insets(10, 0, 0, 10));
		hBoxIqsVersion.setSpacing(10);
		hBoxIqsVersion.getChildren().addAll(iqsVersionLabel, this.iqsVersion);
		return hBoxIqsVersion;
	}
	
	/**
	 * Creates the iqs category text.
	 *
	 * @return the h box
	 */
	private HBox createIqsCategoryText() {
		HBox hBoxIqsCategory = new HBox();
		hBoxIqsCategory.setPadding(new Insets(10, 10, 10, 10));
		hBoxIqsCategory.setSpacing(10);
		LoggedLabel iqsCategoryLabel = UiFactory.createLabel("themeLabel", "IQS Category");
		iqsCategoryLabel.setMaxWidth(100);
		iqsCategoryLabel.setMinWidth(100);
		this.iqsCategory = UiFactory.createText();
		hBoxIqsCategory.getChildren().addAll(iqsCategoryLabel, this.iqsCategory);
		return hBoxIqsCategory;
	}
	
	/**
	 * Creates the iqs question text.
	 *
	 * @return the h box
	 */
	private HBox createIqsQuestionText() {
		HBox hBoxIqsQuestion = new HBox();
		hBoxIqsQuestion.setPadding(new Insets(10, 10, 10, 10));
		hBoxIqsQuestion.setSpacing(10);
		LoggedLabel iqsQuestionLabel = UiFactory.createLabel("iqsQuestionLabel", "IQS Question");
		iqsQuestionLabel.setMaxWidth(100);
		iqsQuestionLabel.setMinWidth(100);
		this.iqsQuestion = UiFactory.createText();
		iqsQuestion.setWrappingWidth(200);
		hBoxIqsQuestion.getChildren().addAll(iqsQuestionLabel, this.iqsQuestion);
		return hBoxIqsQuestion;
	}
	
	/**
	 * Creates the tracking combo box.
	 *
	 * @return the h box
	 */
	private HBox createTrackingComboBox() {
		HBox localThemeContainer = new HBox();
		localThemeComboBox = new LoggedComboBox<String>("trackingComboBox"); 
		localThemeComboBox.getStyleClass().add("combo-box-base");
		localThemeComboBox.setMinWidth(200.0);
		localThemeComboBox.setMaxWidth(200.0);
		localThemeComboBox.setMinHeight(30.0);
		localThemeComboBox.setMaxHeight(30.0);
	
		LoggedLabel localThemeLabel = UiFactory.createLabel("trackingLabel", "Local Theme");
		localThemeLabel.setMaxWidth(70.0);
		localThemeLabel.setPrefWidth(70.0);

		localThemeContainer.setPadding(new Insets(10, 0, 0, 5));
		localThemeContainer.setSpacing(10);
		localThemeCheckbox=new CheckBox();
		if(isMassUpdateBtn && localAttributeMaintDialog.getButtonName().contains(QiConstant.UPDATE_ATTRIBUTE))
			localThemeContainer.getChildren().addAll(localThemeCheckbox,localThemeLabel, localThemeComboBox);
		else
			localThemeContainer.getChildren().addAll(localThemeLabel, localThemeComboBox);
		return localThemeContainer;
	}
	
	/**
	 * Creates the fire engine check box.
	 *
	 * @return the h box
	 */
	private HBox createFireEngineCheckBox() {
		
		LoggedLabel fireEngineLabel = UiFactory.createLabel("defectCategoryLabel", "Fire Engine");
		fireEngineLabel.setMaxWidth(70.0);
		fireEngineLabel.setPrefWidth(70.0);
		fireEngineYesRadioBtn = createRadioButton(QiConstant.YES, group, false, getAttributeMaintDialogController());
		fireEngineNoRadioBtn = createRadioButton(QiConstant.NO, group, true, getAttributeMaintDialogController());
		HBox fireEngineContainer = new HBox();
		fireEngineYesRadioBtn.setDisable(attributeMaintDialogController.validationForFireEngine());
		fireEngineNoRadioBtn.setDisable(attributeMaintDialogController.validationForFireEngine());
		fireEngineCheckbox=new CheckBox();
		fireEngineCheckbox.setDisable(attributeMaintDialogController.validationForFireEngine());
		fireEngineContainer.setPadding(new Insets(10, 0, 0, 5));
		fireEngineContainer.setSpacing(10);
		if(isMassUpdateBtn && localAttributeMaintDialog.getButtonName().contains(QiConstant.UPDATE_ATTRIBUTE))
			fireEngineContainer.getChildren().addAll(fireEngineCheckbox,fireEngineLabel,fireEngineYesRadioBtn,fireEngineNoRadioBtn);
		else
			fireEngineContainer.getChildren().addAll(fireEngineLabel,fireEngineYesRadioBtn,fireEngineNoRadioBtn);
		return fireEngineContainer;
	}
	
	public ToggleGroup getGroup() {
		return group;
	}

	public void setGroup(ToggleGroup group) {
		this.group = group;
	}

	/**
	 * Creates the radio button.
	 *
	 * @param title the title
	 * @param group the group
	 * @param isSelected the is selected
	 * @param handler the handler
	 * @return the logged radio button
	 */
	public LoggedRadioButton createRadioButton(String title, ToggleGroup group, boolean isSelected, EventHandler<ActionEvent> handler) {
		LoggedRadioButton radioButton = new LoggedRadioButton(title);
		radioButton.getStyleClass().add("radio-btn");
		radioButton.setToggleGroup(group);
		radioButton.setSelected(isSelected);
		radioButton.setOnAction(handler);
		radioButton.setUserData(title);
		return radioButton;
	}
	

	public LoggedText getThemeName() {
		return themeName;
	}

	public void setThemeName(LoggedText themeName) {
		this.themeName = themeName;
	}

	public LoggedText getIqsVersion() {
		return iqsVersion;
	}

	public void setIqsVersion(LoggedText iqsVersion) {
		this.iqsVersion = iqsVersion;
	}

	public LoggedText getIqsCategory() {
		return iqsCategory;
	}

	public void setIqsCategory(LoggedText iqsCategory) {
		this.iqsCategory = iqsCategory;
	}

	public LoggedText getIqsQuestion() {
		return iqsQuestion;
	}

	public void setIqsQuestion(LoggedText iqsQuestion) {
		this.iqsQuestion = iqsQuestion;
	}

	public LoggedComboBox<String> getLocalThemeComboBox() {
		return localThemeComboBox;
	}

	public void setLocalThemeComboBox(LoggedComboBox<String> localThemeComboBox) {
		this.localThemeComboBox = localThemeComboBox;
	}

	

	public static PdcLocalAttributeMaintDialogController getAttributeMaintDialogController() {
		return attributeMaintDialogController;
	}
	public static PdcLocalAttributeMaintDialog getLocalAttributeMaintDialog() {
		return localAttributeMaintDialog;
	}

	public LoggedRadioButton getReportableRadioButton() {
		return reportableRadioButton;
	}

	public void setReportableRadioButton(LoggedRadioButton reportableRadioBtn) {
		this.reportableRadioButton = reportableRadioBtn;
	}

	public LoggedRadioButton getNonReportableRadioButton() {
		return nonReportableRadioButton;
	}

	public void setNonReportableRadioButton(LoggedRadioButton notReportableRadioBtn) {
		this.nonReportableRadioButton = notReportableRadioBtn;
	}

	public LoggedRadioButton getDefaultReportableRadioBtn() {
		return defaultReportableRadioBtn;
	}

	public void setDefaultReportableRadioBtn(LoggedRadioButton defaultReportableRadioBtn) {
		this.defaultReportableRadioBtn = defaultReportableRadioBtn;
	}

	public CheckBox getReportableNonReportableCheckbox() {
		return reportableNonReportableCheckbox;
	}

	public void setReportableNonReportableCheckbox(
			CheckBox reportableNonReportableCheckbox) {
		this.reportableNonReportableCheckbox = reportableNonReportableCheckbox;
	}

	public CheckBox getLocalThemeCheckbox() {
		return localThemeCheckbox;
	}

	public void setLocalThemeCheckbox(CheckBox localThemeCheckbox) {
		this.localThemeCheckbox = localThemeCheckbox;
	}

	public CheckBox getFireEngineCheckbox() {
		return fireEngineCheckbox;
	}

	public void setFireEngineCheckbox(CheckBox fireEngineCheckbox) {
		this.fireEngineCheckbox = fireEngineCheckbox;
	}

	public LoggedRadioButton getFireEngineYesRadioBtn() {
		return fireEngineYesRadioBtn;
	}

	public void setFireEngineYesRadioBtn(LoggedRadioButton fireEngineYesRadioBtn) {
		this.fireEngineYesRadioBtn = fireEngineYesRadioBtn;
	}

	public LoggedRadioButton getFireEngineNoRadioBtn() {
		return fireEngineNoRadioBtn;
	}

	public void setFireEngineNoRadioBtn(LoggedRadioButton fireEngineNoRadioBtn) {
		this.fireEngineNoRadioBtn = fireEngineNoRadioBtn;
	}
	
	
}
