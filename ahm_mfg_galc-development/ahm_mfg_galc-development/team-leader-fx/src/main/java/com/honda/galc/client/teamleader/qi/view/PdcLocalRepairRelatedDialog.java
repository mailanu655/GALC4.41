package com.honda.galc.client.teamleader.qi.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.honda.galc.client.teamleader.qi.controller.PdcLocalAttributeMaintDialogController;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;

public class PdcLocalRepairRelatedDialog extends TitledPane {
	
	private LoggedComboBox<String> initialRepairAreaComboBox;
	private LoggedComboBox<String> initialRepairMethodComboBox;
	private UpperCaseFieldBean initialRepairMethodTimeText;
	private UpperCaseFieldBean totalTimeToFixText;
	private CheckBox defectRepairRequireCheckBox;
	private LoggedRadioButton realProblemRadioBtn;
	private LoggedRadioButton symptomRadioBtn;
	private LoggedRadioButton informationalRadioBtn;
	private LoggedRadioButton defaultRadioBtn;
	private static PdcLocalRepairRelatedDialog instance;
	private static PdcLocalAttributeMaintDialogController attributeMaintDialogController;
	private ToggleGroup group = new ToggleGroup();
	private VBox initialRepairVBox;
	private CheckBox initialRepairAreaCheckBox;
	private CheckBox initialRepairMethodCheckBox;
	private CheckBox initialRepairMethodTimeCheckBox;
	private CheckBox defectCategoryCheckBox;
	private CheckBox totalTimeToFixCheckBox;
	private  static  boolean isMassUpdateBtn=false;
	private static String buttonName;
	
	
	private PdcLocalRepairRelatedDialog() {	}
	
	public static PdcLocalRepairRelatedDialog getInstance(PdcLocalAttributeMaintDialogController dialogController,boolean isMassUpdate,String buttonTitle) {
		if(instance == null) {
			instance = new PdcLocalRepairRelatedDialog();
		}
		isMassUpdateBtn=isMassUpdate;
		buttonName=buttonTitle;
		attributeMaintDialogController = dialogController;
		return instance;
	}
	
	/**
	 * Creates the repair related details.
	 *
	 * @return the titled pane
	 */
	public TitledPane createRepairRelatedDetails(){
		TitledPane titledPane = new TitledPane();
        titledPane.setText("Repair Related");
		VBox vBoxAttributeMaint = new VBox();
		initialRepairVBox = new VBox();
		titledPane.setPadding(new Insets(10, 5, 5, 5));
		initialRepairVBox.getChildren().addAll(createInitialRepairArea(), createInitialRepairMethod(), createInitialRepairMethodTime(),	createTotalTimeToFix());
		vBoxAttributeMaint.getChildren().addAll(createDefectCategory(), initialRepairVBox);
		titledPane.setContent(vBoxAttributeMaint);
		return titledPane;
	}
	
	/**
	 * Creates the initial Repair area combo box.
	 *
	 * @return the h box
	 */
	private HBox createInitialRepairArea() {
		HBox initialRepairAreaContainer = new HBox();
		initialRepairAreaComboBox = new LoggedComboBox<String>("intialRepairAreaComboBox");
		initialRepairAreaComboBox.getStyleClass().add("combo-box-base");
		initialRepairAreaComboBox.setMinWidth(250.0);
		initialRepairAreaComboBox.setMaxWidth(250.0);
		initialRepairAreaComboBox.setMinHeight(30.0);
		initialRepairAreaComboBox.setMaxHeight(30.0);
		
		
		HBox labelBox = new HBox();
		LoggedLabel intialRepairAreaLabel = UiFactory.createLabel("intialRepairAreaLabel", "Initial Repair Area ");
		labelBox.getChildren().addAll(intialRepairAreaLabel);
		labelBox.setMaxWidth(250.0);
		labelBox.setPrefWidth(250.0);
		
		initialRepairAreaContainer.setAlignment(Pos.CENTER_LEFT);
		initialRepairAreaContainer.setSpacing(10);
		initialRepairAreaContainer.setMaxWidth(600);
		initialRepairAreaContainer.setMinWidth(600);
		initialRepairAreaContainer.setPadding(new Insets(10, 0, 0, 10));
		initialRepairAreaCheckBox=new CheckBox();
		if(isMassUpdateBtn && buttonName.contains(QiConstant.UPDATE_ATTRIBUTE))
			initialRepairAreaContainer.getChildren().addAll(initialRepairAreaCheckBox,labelBox, initialRepairAreaComboBox);
		else
			initialRepairAreaContainer.getChildren().addAll(labelBox, initialRepairAreaComboBox);
		return initialRepairAreaContainer;
	}

	/**
	 * Creates the Initial Repair Method Combobox.
	 *
	 * @return the h box
	 */
	private HBox createInitialRepairMethod() {
		HBox initialRepairMethodContainer = new HBox();
		initialRepairMethodComboBox = new LoggedComboBox<String>("intialRepairMethodComboBox"); 
		initialRepairMethodComboBox.getStyleClass().add("combo-box-base");
		initialRepairMethodComboBox.setMinWidth(250.0);
		initialRepairMethodComboBox.setMaxWidth(250.0);
		initialRepairMethodComboBox.setMinHeight(30.0);
		initialRepairMethodComboBox.setMaxHeight(30.0);
		
		HBox labelBox = new HBox();
		LoggedLabel intialRepairMethodLabel = UiFactory.createLabel("intialRepairMethodLabel", "Initial Repair Method ");
		labelBox.getChildren().addAll(intialRepairMethodLabel);
		labelBox.setMaxWidth(250.0);
		labelBox.setPrefWidth(250.0);
		
		initialRepairMethodContainer.setAlignment(Pos.CENTER_LEFT);
		initialRepairMethodContainer.setSpacing(10);
		initialRepairMethodContainer.setPadding(new Insets(10, 0, 0, 10));
		initialRepairMethodCheckBox=new CheckBox();
		if(isMassUpdateBtn && buttonName.contains(QiConstant.UPDATE_ATTRIBUTE))
			initialRepairMethodContainer.getChildren().addAll(initialRepairMethodCheckBox,labelBox, initialRepairMethodComboBox);
		else
			initialRepairMethodContainer.getChildren().addAll(labelBox, initialRepairMethodComboBox);
		return initialRepairMethodContainer;
	}
	
	/**
	 * Creates the initial repair Method time Hbox.
	 *
	 * @return the h box
	 */
	private HBox createInitialRepairMethodTime() {
		HBox initialRepairMethodTimeContainer = new HBox();
		LoggedLabel initialRepairMethodTimeLabel = UiFactory.createLabel("intialRepairMethodTimeLabel", "Intial Repair Method Time ");
		initialRepairMethodTimeLabel.setMaxWidth(250);
		initialRepairMethodTimeLabel.setPrefWidth(250.0);
		initialRepairMethodTimeText = UiFactory.createUpperCaseFieldBean("intialRepairMethodTimeText", 21, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		initialRepairMethodTimeText.setText("0");
		initialRepairMethodTimeContainer.setAlignment(Pos.CENTER_LEFT);
		initialRepairMethodTimeContainer.setSpacing(10);
		initialRepairMethodTimeContainer.setPadding(new Insets(10, 0, 0, 10));
		initialRepairMethodTimeCheckBox=new CheckBox();
		if(isMassUpdateBtn && buttonName.contains(QiConstant.UPDATE_ATTRIBUTE))
			initialRepairMethodTimeContainer.getChildren().addAll(initialRepairMethodTimeCheckBox,initialRepairMethodTimeLabel, initialRepairMethodTimeText);
		else
			initialRepairMethodTimeContainer.getChildren().addAll(initialRepairMethodTimeLabel, initialRepairMethodTimeText);
		return initialRepairMethodTimeContainer;
	}
	
	/**
	 * Creates the total Time To fix.
	 *
	 * @return the h box
	 */
	private HBox createTotalTimeToFix() {
		HBox totalTimeToFixContainer = new HBox();
		LoggedLabel intialRepairMethodTimeToFixLabel = UiFactory.createLabel("iqsQuestionLabel", "Total Time To Fix The Problem");
		intialRepairMethodTimeToFixLabel.setMaxWidth(250);
		intialRepairMethodTimeToFixLabel.setPrefWidth(250.0);
		totalTimeToFixText = UiFactory.createUpperCaseFieldBean("intialRepairMethodTimeToFixText", 21, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);;
		totalTimeToFixText.setText("0");
		totalTimeToFixContainer.setAlignment(Pos.CENTER_LEFT);
		totalTimeToFixContainer.setSpacing(10);
		totalTimeToFixContainer.setPadding(new Insets(10, 0, 0, 10));
		totalTimeToFixCheckBox=new CheckBox();
		if(isMassUpdateBtn && buttonName.contains(QiConstant.UPDATE_ATTRIBUTE))
			totalTimeToFixContainer.getChildren().addAll(totalTimeToFixCheckBox,intialRepairMethodTimeToFixLabel, totalTimeToFixText);
		else
			totalTimeToFixContainer.getChildren().addAll(intialRepairMethodTimeToFixLabel, totalTimeToFixText);
		return totalTimeToFixContainer;
	}
	
	/**
	 * Creates the defect category.
	 *
	 * @return the h box
	 */
	private HBox createDefectCategory() {
		HBox defectCategoryContainer = new HBox();
		VBox radioButtonLabel = new VBox();
		VBox radioButtonRows = new VBox();
		HBox radioBtnContainerRow1 = new HBox();
		HBox radioBtnContainerRow2 = new HBox();
		LoggedLabel defectCategoryLabel = UiFactory.createLabel("defectCategoryLabel", "Defect Category");
		defectCategoryLabel.setMaxWidth(150);
		defectCategoryLabel.setMinWidth(150);
		realProblemRadioBtn = createRadioButton(QiConstant.REAL_PROBLEM, this.group, false, getAttributeMaintDialogController());
		symptomRadioBtn = createRadioButton(QiConstant.SYMPTOM, this.group, false, getAttributeMaintDialogController());
		informationalRadioBtn = createRadioButton(QiConstant.INFORMATIONAL, this.group, false, getAttributeMaintDialogController());
   	    defaultRadioBtn = createRadioButton(QiConstant.DEFAULT_CATEGORY, this.group, false, getAttributeMaintDialogController());
   	    
		radioBtnContainerRow2.getChildren().addAll(defaultRadioBtn);
		radioButtonRows.getChildren().addAll(radioBtnContainerRow2);
		radioBtnContainerRow2.setPadding(new Insets(5, 0, 0, 0));
		
		defectCategoryCheckBox=new CheckBox();
		defectCategoryContainer.setAlignment(Pos.CENTER_LEFT);
		defectCategoryContainer.setSpacing(10);
		defectCategoryContainer.setPadding(new Insets(10, 0, 0, 10));
		if(isMassUpdateBtn && buttonName.contains(QiConstant.UPDATE_ATTRIBUTE))  {
			radioButtonLabel.getChildren().addAll(defectCategoryCheckBox,defectCategoryLabel);
		}
		else  {
			radioButtonLabel.getChildren().addAll(defectCategoryLabel);
		}
		defectCategoryContainer.getChildren().addAll(radioButtonLabel, radioButtonRows);
		return defectCategoryContainer;
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

	public LoggedComboBox<String> getInitialRepairAreaComboBox() {
		return initialRepairAreaComboBox;
	}

	public void setInitialRepairAreaComboBox(
			LoggedComboBox<String> initialRepairAreaComboBox) {
		this.initialRepairAreaComboBox = initialRepairAreaComboBox;
	}

	public LoggedComboBox<String> getInitialRepairMethodComboBox() {
		return initialRepairMethodComboBox;
	}

	public void setInitialRepairMethodComboBox(
			LoggedComboBox<String> initialRepairMethodComboBox) {
		this.initialRepairMethodComboBox = initialRepairMethodComboBox;
	}

	public UpperCaseFieldBean getInitialRepairMethodTimeText() {
		return initialRepairMethodTimeText;
	}

	public void setInitialRepairMethodTimeText(
			UpperCaseFieldBean initialRepairMethodTimeText) {
		this.initialRepairMethodTimeText = initialRepairMethodTimeText;
	}

	public UpperCaseFieldBean getTotalTimeToFixText() {
		return totalTimeToFixText;
	}

	public void setTotalTimeToFixText(
			UpperCaseFieldBean totalTimeToFixText) {
		this.totalTimeToFixText = totalTimeToFixText;
	}

	public CheckBox getDefectRepairRequireCheckBox() {
		return defectRepairRequireCheckBox;
	}

	public void setDefectRepairRequireCheckBox(CheckBox defectRepairRequireCheckBox) {
		this.defectRepairRequireCheckBox = defectRepairRequireCheckBox;
	}

	public LoggedRadioButton getRealProblemRadioBtn() {
		return realProblemRadioBtn;
	}

	public void setRealProblemRadioBtn(LoggedRadioButton realProblemRadioBtn) {
		this.realProblemRadioBtn = realProblemRadioBtn;
	}

	public LoggedRadioButton getSymptomRadioBtn() {
		return symptomRadioBtn;
	}

	public void setSymptomRadioBtn(LoggedRadioButton symptomRadioBtn) {
		this.symptomRadioBtn = symptomRadioBtn;
	}

	public LoggedRadioButton getInformationalRadioBtn() {
		return informationalRadioBtn;
	}

	public void setInformationalRadioBtn(LoggedRadioButton informationalRadioBtn) {
		this.informationalRadioBtn = informationalRadioBtn;
	}

	public LoggedRadioButton getDefaultRadioBtn() {
		return defaultRadioBtn;
	}

	public void setDefaultRadioBtn(LoggedRadioButton defaultRadioBtn) {
		this.defaultRadioBtn = defaultRadioBtn;
	}

	public static void setInstance(PdcLocalRepairRelatedDialog instance) {
		PdcLocalRepairRelatedDialog.instance = instance;
	}

	public PdcLocalAttributeMaintDialogController getAttributeMaintDialogController() {
		return attributeMaintDialogController;
	}

	public ToggleGroup getGroup() {
		return group;
	}

	public void setGroup(ToggleGroup group) {
		this.group = group;
	}

	public VBox getInitialRepairVBox() {
		return initialRepairVBox;
	}

	public void setInitialRepairVBox(VBox initialRepairVBox) {
		this.initialRepairVBox = initialRepairVBox;
	}

	public CheckBox getInitialRepairAreaCheckBox() {
		return initialRepairAreaCheckBox;
	}

	public void setInitialRepairAreaCheckBox(CheckBox initialRepairAreaCheckBox) {
		this.initialRepairAreaCheckBox = initialRepairAreaCheckBox;
	}

	public CheckBox getInitialRepairMethodCheckBox() {
		return initialRepairMethodCheckBox;
	}

	public void setInitialRepairMethodCheckBox(CheckBox initialRepairMethodCheckBox) {
		this.initialRepairMethodCheckBox = initialRepairMethodCheckBox;
	}

	public CheckBox getInitialRepairMethodTimeCheckBox() {
		return initialRepairMethodTimeCheckBox;
	}

	public void setInitialRepairMethodTimeCheckBox(
			CheckBox initialRepairMethodTimeCheckBox) {
		this.initialRepairMethodTimeCheckBox = initialRepairMethodTimeCheckBox;
	}

	public CheckBox getDefectCategoryCheckBox() {
		return defectCategoryCheckBox;
	}

	public void setDefectCategoryCheckBox(CheckBox defectCategoryCheckBox) {
		this.defectCategoryCheckBox = defectCategoryCheckBox;
	}

	public CheckBox getTotalTimeToFixCheckBox() {
		return totalTimeToFixCheckBox;
	}

	public void setTotalTimeToFixCheckBox(CheckBox totalTimeToFixCheckBox) {
		this.totalTimeToFixCheckBox = totalTimeToFixCheckBox;
	}
	
	
}
