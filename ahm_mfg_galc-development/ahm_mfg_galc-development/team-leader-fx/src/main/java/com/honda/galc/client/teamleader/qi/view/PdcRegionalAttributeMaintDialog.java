package com.honda.galc.client.teamleader.qi.view;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.controller.PdcRegionalAttributeMaintDialogController;
import com.honda.galc.client.teamleader.qi.controller.PdcRegionalAttributeMaintPanelController;
import com.honda.galc.client.teamleader.qi.model.PdcRegionalAttributeMaintModel;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiRegionalAttributeDto;
import com.honda.galc.util.KeyValue;

/**
 * 
 * <h3>PdcRegionalAttributeMaintDialog Class description</h3>
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
public class PdcRegionalAttributeMaintDialog extends QiFxDialog<PdcRegionalAttributeMaintModel> {

	private LoggedRadioButton reportableRadioButton;
	private LoggedRadioButton nonReportableRadioButton;
	private LoggedComboBox<String> themeComboBox;
	private LoggedComboBox<String> iqsVersionComboBox;
	private LoggedComboBox<String> iqsCategoryComboBox;
	private LoggedComboBox<KeyValue<Integer, String>> iqsQuestionComboBox;
	private LoggedTextArea reasonForChangeTextArea;
	private LoggedButton assignButton;
	private LoggedButton cancelButton;
	private PdcRegionalAttributeMaintDialogController attributeMaintDialogController;
	private List<QiRegionalAttributeDto> defectCombinationDtoList;
	private CheckBox reportableCheckBox;
	private CheckBox themeCheckBox;
	private CheckBox iqsVersionCheckBox;
	private String buttonName;
	private volatile boolean isCancel = false;

	
	public PdcRegionalAttributeMaintDialog(String title, List<QiRegionalAttributeDto> defectCombinationDtoList, PdcRegionalAttributeMaintModel model,String applicationId, PdcRegionalAttributeMaintPanelController parentController) {
		super(title,applicationId, model);//Fix : Passing applicationId as parameter to fetch correct owner stage.
		buttonName=title;
		this.defectCombinationDtoList = defectCombinationDtoList;
		this.attributeMaintDialogController = new PdcRegionalAttributeMaintDialogController(model, this, defectCombinationDtoList, parentController);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		initComponents(title);
		attributeAction(title);
		attributeMaintDialogController.initListeners();
		isCancel = false;
	}

	/**
	 * Attribute action.
	 *
	 * @param title the title
	 */
	private void attributeAction(String title) {
		if(title.contains(QiConstant.ASSIGN_ATTRIBUTE)) {
			assignAttribute();
		} else if(title.contains(QiConstant.UPDATE_ATTRIBUTE)) {
			updateAttribute();
		}
	}
	
	/**
	 * Assign attribute.
	 */
	@SuppressWarnings("unchecked")
	private void assignAttribute() {
		try {
			updateButton.setDisable(true);
			attributeMaintDialogController.loadComboBoxData();
			attributeMaintDialogController.addIqsVersionComboBoxListener();
			if (defectCombinationDtoList != null && defectCombinationDtoList.size() == 1) {
				if(!StringUtils.isBlank(defectCombinationDtoList.get(0).getThemeName())) {				
					themeComboBox.getSelectionModel().select(defectCombinationDtoList.get(0).getThemeName());
				} else if(!StringUtils.isBlank(defectCombinationDtoList.get(0).getIqsVersion())) {
					attributeMaintDialogController.setComboBoxDataForUpdate(false);
				} 
				reportableRadioButton.setSelected(defectCombinationDtoList.get(0).getReportable() == 0);
				nonReportableRadioButton.setSelected(defectCombinationDtoList.get(0).getReportable() == 1);
				if(StringUtils.isBlank(defectCombinationDtoList.get(0).getThemeName()) || StringUtils.isBlank(defectCombinationDtoList.get(0).getIqsVersion())){
					reasonForChangeTextArea.setDisable(true);
				}
			} else {
				reasonForChangeTextArea.setDisable(true);
			}
		} catch (Exception e) {
			attributeMaintDialogController.handleException("An error occured in loading pop up screen ", "Failed To Open Assign Attribute Popup Screen", e);
		}
	}
	
	/**
	 * Update attribute.
	 */
	private void updateAttribute() {
		assignButton.setDisable(true);
		if(defectCombinationDtoList.size() > 1) {
			attributeMaintDialogController.loadComboBoxData();
			attributeMaintDialogController.addIqsVersionComboBoxListener();
			reportableCheckBox.setVisible(true);
			themeCheckBox.setVisible(true);
			iqsVersionCheckBox.setVisible(true);
		} else {
			attributeMaintDialogController.loadComboBoxData();
			attributeMaintDialogController.setComboBoxDataForUpdate(true);
			attributeMaintDialogController.addIqsVersionComboBoxListener();
			attributeMaintDialogController.addIqsCategoryComboBoxListener();
		}
	}
	
	/**
	 * This method is used to initialize the components of Dialog screen.
	 */
	private void initComponents(String title) {
		VBox outerPane = new VBox();
		outerPane.setMaxWidth(500);
		outerPane.setPrefWidth(500);
		outerPane.getChildren().addAll(createReportableRadioButtons(),
				createTheme(), createIqsVersion(), createIqsCategory(),
				createIqsQuestion(), createReasonForChangeContainer(title),
				createButtonContainer());
		reportableCheckBox.setVisible(false);
		themeCheckBox.setVisible(false);
		iqsVersionCheckBox.setVisible(false);
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
		((BorderPane) this.getScene().getRoot()).setBottom(initStatusMessagePane());

	}
	
	/**
	 * Creates the reportable radio buttons.
	 *
	 * @return the h box
	 */
	private HBox createReportableRadioButtons() {
		HBox radioBtnContainer = new HBox();
		ToggleGroup group = new ToggleGroup();
		reportableRadioButton = createRadioButton(QiConstant.REPORTABLE, group, true, getAttributeMaintDialogController());
		nonReportableRadioButton = createRadioButton(QiConstant.NON_REPORTABLE, group, false, getAttributeMaintDialogController());
		reportableCheckBox=new CheckBox();
		radioBtnContainer.getChildren().addAll(reportableCheckBox,reportableRadioButton, nonReportableRadioButton);
		radioBtnContainer.setAlignment(Pos.CENTER_LEFT);
		radioBtnContainer.setSpacing(10);
		radioBtnContainer.setPadding(new Insets(10, 0, 0, 10));
		return radioBtnContainer;
	}
	
	/**
	 * Creates the theme.
	 *
	 * @return the h box
	 */
	private HBox createTheme() {
		HBox themeContainer = new HBox();
		themeComboBox = new LoggedComboBox<String>("themeComboBox");
		themeComboBox.getStyleClass().add("combo-box-base");
		themeComboBox.setMinWidth(220.0);
		themeComboBox.setMaxWidth(220.0);
		themeComboBox.setMinHeight(35.0);
		themeComboBox.setMaxHeight(35.0);
		
		HBox labelBox = new HBox();
		LoggedLabel themeLabel = UiFactory.createLabel("themeLabel", "Theme");
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		asterisk.setStyle("-fx-text-fill: red");
		if(isMassUpdate())
			labelBox.getChildren().addAll(themeLabel);
		else
			labelBox.getChildren().addAll(themeLabel,asterisk);
		labelBox.setMaxWidth(120.0);
		labelBox.setPrefWidth(120.0);
		
		themeContainer.setAlignment(Pos.CENTER_LEFT);
		themeContainer.setSpacing(10);
		themeContainer.setPadding(new Insets(10, 0, 0, 10));
		themeCheckBox=new CheckBox();
		themeContainer.getChildren().addAll(themeCheckBox,labelBox, themeComboBox);
		return themeContainer;
	}

	/**
	 * Creates the iqs version.
	 *
	 * @return the h box
	 */
	private HBox createIqsVersion() {
		HBox iqsVersionContainer = new HBox();
		iqsVersionComboBox = new LoggedComboBox<String>("iqsVersionComboBox");
		iqsVersionComboBox.getStyleClass().add("combo-box-base");
		iqsVersionComboBox.setMinWidth(220.0);
		iqsVersionComboBox.setMaxWidth(220.0);
		iqsVersionComboBox.setMinHeight(35.0);
		iqsVersionComboBox.setMaxHeight(35.0);
		HBox labelBox = new HBox();
		LoggedLabel iqsVersionLabel = UiFactory.createLabel("iqsVersionLabel", "IQS Version");
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		asterisk.setStyle("-fx-text-fill: red");
		if(isMassUpdate())
			labelBox.getChildren().addAll(iqsVersionLabel);
		else
			labelBox.getChildren().addAll(iqsVersionLabel,asterisk);
		labelBox.setMaxWidth(120.0);
		labelBox.setPrefWidth(120.0);
		
		iqsVersionContainer.setAlignment(Pos.CENTER_LEFT);
		iqsVersionContainer.setSpacing(10);
		iqsVersionContainer.setPadding(new Insets(10, 0, 0, 10));
		iqsVersionCheckBox=new CheckBox();
		iqsVersionContainer.getChildren().addAll(iqsVersionCheckBox,labelBox, iqsVersionComboBox);
		return iqsVersionContainer;
	}
	
	/**
	 * Creates the iqs category.
	 *
	 * @return the h box
	 */
	private HBox createIqsCategory() {
		HBox iqsCategoryContainer = new HBox();
		iqsCategoryComboBox = new LoggedComboBox<String>("iqsCategoryComboBox");
		iqsCategoryComboBox.getStyleClass().add("combo-box-base");
		iqsCategoryComboBox.setMinWidth(220.0);
		iqsCategoryComboBox.setMaxWidth(220.0);
		iqsCategoryComboBox.setMinHeight(35.0);
		iqsCategoryComboBox.setMaxHeight(35.0);
		
		HBox labelBox = new HBox();
		LoggedLabel iqsCategoryLabel = UiFactory.createLabel("iqsCategoryLabel", "IQS Category");
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		asterisk.setStyle("-fx-text-fill: red");
		if(isMassUpdate())
			labelBox.getChildren().addAll(iqsCategoryLabel);
		else
			labelBox.getChildren().addAll(iqsCategoryLabel, asterisk);
		labelBox.setMaxWidth(120.0);
		labelBox.setPrefWidth(120.0);
		labelBox.setSpacing(20);
		iqsCategoryContainer.setAlignment(Pos.CENTER_LEFT);
		iqsCategoryContainer.setSpacing(10);
		iqsCategoryContainer.setPadding(new Insets(10, 10, 0, 50));
		iqsCategoryContainer.getChildren().addAll(labelBox, iqsCategoryComboBox);
		return iqsCategoryContainer;
	}
	
	/**
	 * Creates the iqs question.
	 *
	 * @return the h box
	 */
	private HBox createIqsQuestion() {
		HBox iqsQuestionContainer = new HBox();
		iqsQuestionComboBox = new LoggedComboBox<KeyValue<Integer, String>>("iqsQuestionComboBox");
		iqsQuestionComboBox.getStyleClass().add("combo-box-base");
		iqsQuestionComboBox.setMinWidth(300.0);
		iqsQuestionComboBox.setMaxWidth(300.0);
		iqsQuestionComboBox.setMinHeight(35.0);
		iqsQuestionComboBox.setMaxHeight(35.0);
		
		HBox labelBox = new HBox();
		LoggedLabel iqsQuestionLabel = UiFactory.createLabel("iqsQuestionLabel", "IQS Question");
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		asterisk.setStyle("-fx-text-fill: red");
		if(isMassUpdate())
			labelBox.getChildren().addAll(iqsQuestionLabel);
		else	
			labelBox.getChildren().addAll(iqsQuestionLabel, asterisk);
			
		labelBox.setMaxWidth(120.0);
		labelBox.setPrefWidth(120.0);
		
		iqsQuestionContainer.setAlignment(Pos.CENTER_LEFT);
		iqsQuestionContainer.setSpacing(10);
		iqsQuestionContainer.setPadding(new Insets(10, 0, 0, 50));
		iqsQuestionContainer.getChildren().addAll(labelBox, iqsQuestionComboBox);
		return iqsQuestionContainer;
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
	
		HBox labelBox = new HBox();
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("label", "Reason for change");
		LoggedLabel asterisk = new LoggedLabel();
		if(title.contains(QiConstant.UPDATE)) {
			asterisk = UiFactory.createLabel("label", "*");
			asterisk.setStyle("-fx-text-fill: red");
		}
		labelBox.getChildren().addAll(reasonForChangeLabel, asterisk);
		labelBox.setMaxWidth(120.0);
		labelBox.setPrefWidth(120.0);

		reasonForChangeContainer.setPadding(new Insets(20, 10, 10, 50));
		reasonForChangeContainer.setSpacing(10);
		reasonForChangeContainer.getChildren().addAll(labelBox, reasonForChangeTextArea);
		return reasonForChangeContainer;
	}
	
	/**
	 * create the button container.
	 *
	 * @return the button container
	 */
	private HBox createButtonContainer() {
		HBox buttonContainer = new HBox();
		assignButton = createBtn(QiConstant.ASSIGN, getAttributeMaintDialogController());
		assignButton.setPadding(new Insets(5,5,5,5));
		updateButton = createBtn(QiConstant.UPDATE, getAttributeMaintDialogController());
		cancelButton = createBtn(QiConstant.CANCEL, getAttributeMaintDialogController());

		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(5,5,5,5));
		buttonContainer.setSpacing(10);
		buttonContainer.getChildren().addAll(assignButton, updateButton, cancelButton);
		return buttonContainer;
	}
	
	/**
	 * This method checks for mass update
	 * @return boolean
	 */
	private boolean isMassUpdate() {
		if(defectCombinationDtoList.size()>1 &&   buttonName.contains(QiConstant.UPDATE_ATTRIBUTE))
			return true;
		else
			return false;
	}
	
	public PdcRegionalAttributeMaintDialogController getAttributeMaintDialogController() {
		return attributeMaintDialogController;
	}

	public void setAttributeMaintDialogController(
			PdcRegionalAttributeMaintDialogController attributeMaintDialogController) {
		this.attributeMaintDialogController = attributeMaintDialogController;
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

	public LoggedComboBox<String> getIqsVersionComboBox() {
		return iqsVersionComboBox;
	}

	public void setIqsVersionComboBox(LoggedComboBox<String> iqsVersionComboBox) {
		this.iqsVersionComboBox = iqsVersionComboBox;
	}

	public LoggedComboBox<String> getIqsCategoryComboBox() {
		return iqsCategoryComboBox;
	}

	public void setIqsCategoryComboBox(LoggedComboBox<String> iqsCategoryComboBox) {
		this.iqsCategoryComboBox = iqsCategoryComboBox;
	}
	
	public LoggedComboBox<KeyValue<Integer, String>> getIqsQuestionComboBox() {
		return iqsQuestionComboBox;
	}

	public void setIqsQuestionComboBox(
			LoggedComboBox<KeyValue<Integer, String>> iqsQuestionComboBox) {
		this.iqsQuestionComboBox = iqsQuestionComboBox;
	}

	public LoggedTextArea getReasonForChangeTextArea() {
		return reasonForChangeTextArea;
	}

	public void setReasonForChangeTextArea(LoggedTextArea reasonForChangeTextArea) {
		this.reasonForChangeTextArea = reasonForChangeTextArea;
	}

	public LoggedButton getAssignButton() {
		return assignButton;
	}

	public void setAssignButton(LoggedButton assignButton) {
		this.assignButton = assignButton;
	}

	public LoggedButton getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(LoggedButton cancelButton) {
		this.cancelButton = cancelButton;
	}

	public LoggedComboBox<String> getThemeComboBox() {
		return themeComboBox;
	}

	public void setThemeComboBox(LoggedComboBox<String> themeComboBox) {
		this.themeComboBox = themeComboBox;
	}

	public CheckBox getReportableCheckBox() {
		return reportableCheckBox;
	}

	public void setReportableCheckBox(CheckBox reportableCheckBox) {
		this.reportableCheckBox = reportableCheckBox;
	}

	public CheckBox getThemeCheckBox() {
		return themeCheckBox;
	}

	public void setThemeCheckBox(CheckBox themeCheckBox) {
		this.themeCheckBox = themeCheckBox;
	}

	public CheckBox getIqsVersionCheckBox() {
		return iqsVersionCheckBox;
	}

	public void setIqsVersionCheckBox(CheckBox iqsVersionCheckBox) {
		this.iqsVersionCheckBox = iqsVersionCheckBox;
	}

	/**
	 * @return the isCancel
	 */
	public boolean isCancel() {
		return isCancel;
	}

	/**
	 * @param isCancel the isCancel to set
	 */
	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}
	
}
