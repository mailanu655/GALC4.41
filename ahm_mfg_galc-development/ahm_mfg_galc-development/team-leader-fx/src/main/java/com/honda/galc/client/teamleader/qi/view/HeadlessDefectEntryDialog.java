package com.honda.galc.client.teamleader.qi.view;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.controller.HeadlessDefectEntryDialogController;
import com.honda.galc.client.teamleader.qi.model.HeadlessDefectEntryModel;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedText;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.DefectMapDto;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>HeadlessDefectEntryDialog</code> is the Dialog  class for Headless Entry Screen.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>3/10/2017</TD>

 * </TABLE>
*/
 public class HeadlessDefectEntryDialog extends QiFxDialog<HeadlessDefectEntryModel> {
	private DefectMapDto defectMapDto;
	private HeadlessDefectEntryDialogController headlessDefectEntryDialogController;
	private LoggedText newExternalSystemCode;
	private LoggedText entryModel;
	private LoggedText entryScreen;
	private LoggedText textentryMenu;
	private LoggedText partDefectDescText;
	private UpperCaseFieldBean externalPartCode;
	private UpperCaseFieldBean externalDefectCode;
	private LoggedButton createButton;
	private LoggedButton cancelButton;
	private ObjectTablePane<DefectMapDto> textentryTablePane;
	private ObjectTablePane<DefectMapDto> partDefectCombTablePane;
	private Boolean isImageEntryScreen;
	private LoggedLabel newExternalCodeLabel;
	
	private LoggedText newExternalCode ;
	private LoggedTextArea reasonForChangeTextArea;
	private String buttonName;
	private HBox textEntryMenucontainer;
	private CheckBox qicsRepairCheckBox;
	private CheckBox extSysRepairCheckBox;
	private volatile boolean isCancel = false;

	public HeadlessDefectEntryDialog(String title, DefectMapDto defectMapDto,HeadlessDefectEntryModel headlessDefectEntryModel,String applicationId) {
		super(title, applicationId, headlessDefectEntryModel);
		setButtonName(title);
		this.defectMapDto = defectMapDto;
		this.headlessDefectEntryDialogController = new HeadlessDefectEntryDialogController(model, this, this.defectMapDto);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		initComponents();
		headlessDefectEntryDialogController.initListeners();
		buttonAction(title);
	}
	
	
	/**
	 * This method maps button action.
	 * @param title 
	 */
	private void buttonAction(String title) {
		if(title.equals(QiConstant.CREATE)) {
			createButtonAction();
		} else if(title.equals(QiConstant.UPDATE)) {
			updateButtonAction();
		}
	}
	

	/**
	 * This method defines create button action.
	 */
	private void createButtonAction() {
		try {
			loadInitialData();
			if(isImageEntryScreen){
				loadPartDefectCombData();
			}else{
				loadTextEntryMenuData();
			}
			reasonForChangeTextArea.setDisable(true);
			updateButton.setDisable(true);
		} catch (Exception e) {
			headlessDefectEntryDialogController.handleException("An error occured in loading pop up screen ", "Failed To Open Create Popup Screen", e);
		}
	}
	
	/**
	 * This method defines update button action.
	 */
	private void updateButtonAction() {
		try {
			loadInitialData();
			loadUpdatedAssignmentData();
			newExternalCodeLabel.setText("External System Code :");
			createButton.setDisable(true);
			updateButton.setDisable(true);
			if(!isImageEntryScreen)
				textEntryMenucontainer.setVisible(true);
		} catch (Exception e) {
			headlessDefectEntryDialogController.handleException("An error occured in loading pop up screen ", "Failed To Open Update Popup Screen", e);
		}
	}
	
	/**
	 * This method adds the Part Defect Combination related check box listener.
	 */
	private void loadPartDefectCombData() {
		partDefectCombTablePane.setData(getModel().findAllPartDefectCombByEntryScreenModelMenu(entryScreen.getText(), entryModel.getText(),StringUtils.EMPTY));
	}
	
	/**
	 * This method loads initial data.
	 */
	private void loadInitialData() {
		entryModel.setText(defectMapDto.getEntryModel());
		entryScreen.setText(defectMapDto.getEntryScreen());
		isImageEntryScreen=getModel().findIsImageEntryScreen(defectMapDto.getEntryModel(), defectMapDto.getEntryScreen());
	}
	
	/**
	 * This method Loads text entry menu.
	 */
	private void loadTextEntryMenuData() {
		List<DefectMapDto> textEntryMenu = getModel().findAllTextEntryMenu(entryScreen.getText(),entryModel.getText(),isImageEntryScreen);
		textentryTablePane.setData(textEntryMenu);
	}
	
	/**
	 *This method Loads updated assigned data.
	 */
	private void loadUpdatedAssignmentData() {
		DefectMapDto textEntryMenuTemp = new DefectMapDto();
		textEntryMenuTemp.setTextEntryMenu(defectMapDto.getTextEntryMenu());
		textentryMenu.setText(defectMapDto.getTextEntryMenu());
		partDefectDescText.setText(defectMapDto.getFullPartDesc());
		DefectMapDto partDefectCombTemp = new DefectMapDto();
		partDefectCombTemp.setFullPartDesc(defectMapDto.getFullPartDesc());
		partDefectCombTemp.setLocalDefectCombinationId(defectMapDto.getLocalDefectCombinationId());
		partDefectCombTemp.setIsQicsRepairReqd(defectMapDto.getIsQicsRepairReqd());
		partDefectCombTemp.setIsExtSysRepairReqd(defectMapDto.getIsExtSysRepairReqd());
		externalPartCode.settext(defectMapDto.getExternalPartCode());
		externalDefectCode.settext(defectMapDto.getExternalDefectCode());
		if(!isImageEntryScreen){
			List<DefectMapDto> textEntryMenuList = getModel().findAllTextEntryMenu(entryScreen.getText(),entryModel.getText(),isImageEntryScreen);
			textentryTablePane.setData(textEntryMenuList);
			textentryTablePane.getTable().getSelectionModel().select(textEntryMenuTemp);
			textentryTablePane.getTable().scrollTo(textentryTablePane.getTable().getSelectionModel().getSelectedIndex());
		}
		else  {//image entry screen
			partDefectCombTablePane.setData(getModel().findAllPartDefectCombByEntryScreenModelMenu(entryScreen.getText(), entryModel.getText(),textentryMenu.getText()));
		}
		partDefectCombTablePane.getTable().getSelectionModel().select(partDefectCombTemp);
		partDefectCombTablePane.getTable().scrollTo(partDefectCombTablePane.getTable().getSelectionModel().getSelectedIndex());
		newExternalSystemCode.setText(externalPartCode.getText()+"-"+externalDefectCode.getText());
		qicsRepairCheckBox.setSelected(defectMapDto.getIsQicsRepairReqd() != 0);
		extSysRepairCheckBox.setSelected(defectMapDto.getIsExtSysRepairReqd() != 0);
	}
	
	/**
	 * This method is used to create layout of the Headless Defect Entry Screen
	 */
	public void initComponents() {
		VBox outerPane = new VBox();
		outerPane.setMaxWidth(800);
		outerPane.setPrefWidth(800);
		outerPane.setPrefHeight(500);
		outerPane.getChildren().addAll(createExternalSystemCodeContainer());
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
		((BorderPane) this.getScene().getRoot()).setBottom(initStatusMessagePane());
	}
	
	/**
	 * This method is used to create Pane of Headless Defect Entry Screen
	 * @return MigPane
	 */
	private MigPane createExternalSystemCodeContainer(){
		 MigPane pane = new MigPane("insets 0 5 0 5", "[center,grow,fill]", "");
		 HBox newExternalSystemCode = new HBox();
		 newExternalSystemCode.getChildren().addAll(createNewExternalCode());
		 newExternalSystemCode.setSpacing(15);
		 newExternalSystemCode.setPadding(new Insets(0,15,0,5));
		 HBox defectDescContainer= new HBox();
		 HBox menuTextContainer= new HBox();
		 if(getButtonName().equalsIgnoreCase(QiConstant.UPDATE)){
			 menuTextContainer.getChildren().addAll(createTextEntryMenuTextContainer());
			 menuTextContainer.setSpacing(50);
			 menuTextContainer.setPadding(new Insets(0,25,0,5));
			 defectDescContainer.getChildren().addAll(createPartDefectCombContainer());
			 defectDescContainer.setSpacing(50);
			 defectDescContainer.setPadding(new Insets(0,25,0,10));
		 }
		 
		 VBox thirdComboBoxContainer= new VBox();
		 thirdComboBoxContainer.getChildren().addAll( createEntryModelCotainer());
		 thirdComboBoxContainer.setSpacing(15);
		 thirdComboBoxContainer.setPadding(new Insets(0,15,0,0));
		 HBox textEntryPartDefectCombContainer = new HBox();
		 textEntryPartDefectCombContainer.getChildren().addAll( createTextEntryMenuObjectTablePane(),createFullPartNameObjectTablePane());
		 textEntryPartDefectCombContainer.setSpacing(0);
		 textEntryPartDefectCombContainer.setPadding(new Insets(20,15,0,5));
		 HBox reasonForChangeContainer = new HBox();
		 reasonForChangeContainer.setPadding(new Insets(0,5,0,0));
		 reasonForChangeContainer.getChildren().addAll( createReasonForChangeContainer(),createButtonContainer());
		 pane.add(newExternalSystemCode,"span,wrap");
		 if(getButtonName().equalsIgnoreCase(QiConstant.UPDATE)){
			 pane.add(menuTextContainer,"span,wrap");
			 pane.add(defectDescContainer,"span,wrap");
		 }
		 pane.add(thirdComboBoxContainer,"span,wrap"); 
		 pane.add(textEntryPartDefectCombContainer,"span,wrap");
		 pane.add(reasonForChangeContainer,"span,wrap");
		 return pane;
	}
	

	/**
	 *This method is used to create New External System Code container.
	 *@return HBox
	 */
	private HBox createNewExternalCode() {
		HBox container = new HBox();
	    newExternalCodeLabel = UiFactory.createLabel("newExternalCode", "New External System Code :", Fonts.SS_DIALOG_BOLD(20));
		newExternalSystemCode = UiFactory.createText("");
		newExternalSystemCode.setStyle("-fx-font-size: 12pt;");
		container.getChildren().addAll(newExternalCodeLabel,newExternalSystemCode);
		container.setPadding(new Insets(5,15,0,7));
		container.setSpacing(10);
		return container;
	}
	
	/**
	 *This method is used to create text entry menu container
	 *@return HBox
	 */
	private HBox createTextEntryMenuTextContainer() {
		textEntryMenucontainer = new HBox();
		LoggedLabel textEntryMenuLabel = UiFactory.createLabel("textEntryMenuLabel", "Text Entry Menu :", Fonts.SS_DIALOG_BOLD(14));
		this.textentryMenu=UiFactory.createText("");
		textEntryMenucontainer.getChildren().addAll(textEntryMenuLabel, this.textentryMenu);
		textEntryMenucontainer.setVisible(false);
		textEntryMenucontainer.setSpacing(48);
		textEntryMenucontainer.setPadding(new Insets(5,15,0,7));textEntryMenucontainer.setSpacing(48);
		textEntryMenucontainer.setPadding(new Insets(5,15,0,7));
		return textEntryMenucontainer;
	}
	
	/**
	 *This method is used to create QICS Part Defect Comb container
	 *@return HBox
	 */
	private HBox createPartDefectCombContainer() {
		HBox container = new HBox();
		LoggedLabel defectDescLabel = UiFactory.createLabel("defectDescLabel", "QICS Part Defect Combination :", Fonts.SS_DIALOG_BOLD(14));
		this.partDefectDescText=UiFactory.createText("");
		container.getChildren().addAll(defectDescLabel,this.partDefectDescText);
		container.setPadding(new Insets(5,15,0,5));
		container.setSpacing(18);
		return container;
	}
	
	/**
	 *This method is used to create External Part Code container
	 *@return HBox
	 */
	private MigPane createEntryModelCotainer() {
		MigPane pane = new MigPane("insets 0 0 0 0", "[center,grow,fill]", "");
		VBox firstContainer = new VBox();
		VBox secondContainer = new VBox();
		VBox ThirdContainer = new VBox();
		VBox forthContainer = new VBox();
		HBox partlabelContainer = new HBox();
		HBox defectLabelContainer = new HBox();
		HBox qicsRepairCheckBoxContainer = new HBox();
		HBox extSysRepairCheckBoxContainer = new HBox();

		//creating label and 
		LoggedLabel	externalPartCodeLabel = UiFactory.createLabel("externalPartCodeLabel ", "External Part Code", Fonts.SS_DIALOG_BOLD(14));
		LoggedLabel	externalDefectCodeLabel = UiFactory.createLabel("externalDefectCodeLabel ", "External Defect Code", Fonts.SS_DIALOG_BOLD(14));
		LoggedLabel entryModelLabel = UiFactory.createLabel("EntryModelLabel", "Entry Model :", Fonts.SS_DIALOG_BOLD(14));
		LoggedLabel entryScreenLabel = UiFactory.createLabel("EntryScreenLabel", "Entry Screen :", Fonts.SS_DIALOG_BOLD(14));
		LoggedLabel qicsCheckBoxLabel = UiFactory.createLabel("QicsCheckBoxLabel","QICS Repair Required :", Fonts.SS_DIALOG_BOLD(12));
		LoggedLabel extSysRepairLabel = UiFactory.createLabel("ExtSysCheckBoxLabel","External Repair Required :", Fonts.SS_DIALOG_BOLD(12));
		LoggedLabel partLabelAsterisk = UiFactory.createLabel("label", "*");
		partLabelAsterisk.setStyle("-fx-text-fill: red");
		LoggedLabel defectLabelasterisk = UiFactory.createLabel("label", "*");
		defectLabelasterisk.setStyle("-fx-text-fill: red");
		externalPartCode =UiFactory.createUpperCaseFieldBean("externalPartCodeTxt",15,Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		externalDefectCode =  UiFactory.createUpperCaseFieldBean("externalDefectCodeTxt", 15, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		
		qicsRepairCheckBox =  new CheckBox();
		qicsRepairCheckBox.setStyle("-fx-font: 15 arial;");
		qicsRepairCheckBox.setPrefSize(200,30);
		qicsRepairCheckBox.setDisable(false);
		
		extSysRepairCheckBox =  new CheckBox();
		extSysRepairCheckBox.setStyle("-fx-font: 15 arial;");
		extSysRepairCheckBox.setPrefSize(200,30);
		extSysRepairCheckBox.setDisable(false);
		
		this.entryModel=UiFactory.createText("");
		this.entryScreen=UiFactory.createText("");
		partlabelContainer.getChildren().addAll(externalPartCodeLabel,partLabelAsterisk);
		defectLabelContainer.getChildren().addAll(externalDefectCodeLabel,defectLabelasterisk);
		qicsRepairCheckBoxContainer.getChildren().addAll(qicsCheckBoxLabel,qicsRepairCheckBox);
		extSysRepairCheckBoxContainer.getChildren().addAll(extSysRepairLabel,extSysRepairCheckBox);
		firstContainer.setPadding(new Insets(2,20,0,25));
		firstContainer.setSpacing(10);
		secondContainer.setSpacing(6);
		secondContainer.setPadding(new Insets(2,20,0,20));
		ThirdContainer.setPadding(new Insets(-35,0,0,5));
		ThirdContainer.setSpacing(12);
		forthContainer.setSpacing(8);
		forthContainer.setPadding(new Insets(-35,0,0,10));
		firstContainer.getChildren().addAll(entryModelLabel,partlabelContainer,qicsCheckBoxLabel,extSysRepairLabel);
		secondContainer.getChildren().addAll(this.entryModel,externalPartCode,qicsRepairCheckBox,extSysRepairCheckBox);
		ThirdContainer.getChildren().addAll(entryScreenLabel,defectLabelContainer);
		forthContainer.getChildren().addAll(this.entryScreen,externalDefectCode);
		pane.getChildren().addAll(firstContainer,secondContainer,ThirdContainer,forthContainer);
		pane.setPadding(new Insets(0,0,10,0));
		return pane;
	}
	
	/**
	 *This method is used for alignment of full part name table
	 *@return ObjectTablePane<DefectMapDto>
	 */
	public ObjectTablePane<DefectMapDto> createFullPartNameObjectTablePane() {
		Double[] columnWidth = new Double[] { 0.401 };
		ColumnMappingList columnMappingList = ColumnMappingList.with("QICS Part Defect Combination", "fullPartDesc");
		partDefectCombTablePane = new ObjectTablePane<DefectMapDto>(columnMappingList, columnWidth);
		partDefectCombTablePane.setPadding(new Insets(0, 10, 0, 0));
		partDefectCombTablePane.setPrefWidth(600);
		partDefectCombTablePane.setPrefHeight(200);
		return partDefectCombTablePane;
	}
	
	/**
	 *This method is used for text entry menu table
	 *@return ObjectTablePane<DefectMapDto>
	 */
	private ObjectTablePane<DefectMapDto> createTextEntryMenuObjectTablePane() {
		Double[] columnWidth = new Double[] { 0.145 };
		ColumnMappingList columnMappingList = ColumnMappingList.with("Text Entry Menu ", "textEntryMenu");
		textentryTablePane= new ObjectTablePane<DefectMapDto>(columnMappingList, columnWidth);
		textentryTablePane.setPadding(new Insets(0, 0, 0, 10));
		textentryTablePane.setPrefWidth(250);
		textentryTablePane.setPrefHeight(250);
		return textentryTablePane;
	}
	
	/**
	 *This method is used to create update button container
	 *@return HBox
	 */
	private HBox createButtonContainer() {
		HBox buttonContainer = new HBox();
		createButton = createBtn(QiConstant.CREATE, getHeadlessDefectEntryDialogController());
		updateButton = createBtn(QiConstant.UPDATE, getHeadlessDefectEntryDialogController());
		cancelButton = createBtn(QiConstant.CANCEL, getHeadlessDefectEntryDialogController());
		buttonContainer.setAlignment(Pos.CENTER_RIGHT);
		buttonContainer.setPadding(new Insets(5,5,5,5));
		buttonContainer.setSpacing(10);
		buttonContainer.getChildren().addAll(createButton,updateButton, cancelButton);
		return buttonContainer;
	}
	
	/**
	 * This method creates Reason for change container
	 * @return HBox
	 */
	private HBox createReasonForChangeContainer() {
		HBox reasonForChangeContainer = new HBox();
		reasonForChangeTextArea = UiFactory.createTextArea();
		reasonForChangeTextArea.setPrefRowCount(3);
		reasonForChangeTextArea.setPrefColumnCount(23);
		HBox labelBox = new HBox();
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("label", "Reason For Change",Fonts.SS_DIALOG_BOLD(14));
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		asterisk.setStyle("-fx-text-fill: red");
		if(getButtonName().equalsIgnoreCase(QiConstant.UPDATE)){
			labelBox.getChildren().addAll(reasonForChangeLabel, asterisk);
		}else{
			labelBox.getChildren().addAll(reasonForChangeLabel);	
		}
		labelBox.setMaxWidth(160.0);
		labelBox.setPrefWidth(160.0);
		reasonForChangeContainer.setPadding(new Insets(5, 0, 5, 15));
		reasonForChangeContainer.setSpacing(5);
		reasonForChangeContainer.getChildren().addAll(labelBox, reasonForChangeTextArea);
		return reasonForChangeContainer;
	}
	

	/**
	 * @return the externalPartCode
	 */
	public UpperCaseFieldBean getExternalPartCode() {
		return externalPartCode;
	}

	/**
	 * @param externalPartCode the externalPartCode to set
	 */
	public void setExternalPartCode(UpperCaseFieldBean externalPartCode) {
		this.externalPartCode = externalPartCode;
	}

	/**
	 * @return the externalDefectCode
	 */
	public UpperCaseFieldBean getExternalDefectCode() {
		return externalDefectCode;
	}

	/**
	 * @param externalDefectCode the externalDefectCode to set
	 */
	public void setExternalDefectCode(UpperCaseFieldBean externalDefectCode) {
		this.externalDefectCode = externalDefectCode;
	}

	

	/**
	 * @return the defectMapDto
	 */
	public DefectMapDto getDefectMapDto() {
		return defectMapDto;
	}


	/**
	 * @param defectMapDto the defectMapDto to set
	 */
	public void setDefectMapDto(DefectMapDto defectMapDto) {
		this.defectMapDto = defectMapDto;
	}


	/**
	 * @return the externalSystemCode
	 */
	public LoggedText getExternalSystemCode() {
		return newExternalSystemCode;
	}

	/**
	 * @return the buttonName
	 */
	public String getButtonName() {
		return buttonName;
	}

	/**
	 * @param buttonName the buttonName to set
	 */
	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}

	/**
	 * @param externalSystemCode the externalSystemCode to set
	 */
	public void setExternalSystemCode(LoggedText externalSystemCode) {
		this.newExternalSystemCode = externalSystemCode;
	}

	/**
	 * @return the entryModel
	 */
	public LoggedText getEntryModel() {
		return entryModel;
	}

	/**
	 * @param entryModel the entryModel to set
	 */
	public void setEntryModel(LoggedText entryModel) {
		this.entryModel = entryModel;
	}

	/**
	 * @return the entryScreen
	 */
	public LoggedText getEntryScreen() {
		return entryScreen;
	}

	/**
	 * @param entryScreen the entryScreen to set
	 */
	public void setEntryScreen(LoggedText entryScreen) {
		this.entryScreen = entryScreen;
	}

	/**
	 * @return the textentryMenu
	 */
	public LoggedText getTextentryMenu() {
		return textentryMenu;
	}

	/**
	 * @param textentryMenu the textentryMenu to set
	 */
	public void setTextentryMenu(LoggedText textentryMenu) {
		this.textentryMenu = textentryMenu;
	}

	/**
	 * @return the partDefectDescText
	 */
	public LoggedText getPartDefectDescText() {
		return partDefectDescText;
	}

	/**
	 * @param defectDesc the partDefectDescText to set
	 */
	public void setPartDefectDescText(LoggedText partDefectDescText) {
		this.partDefectDescText = partDefectDescText;
	}

	/**
	 * @return the textentryTablePane
	 */
	public ObjectTablePane<DefectMapDto> getTextentryTablePane() {
		return textentryTablePane;
	}

	/**
	 * @return the partDefectCombTablePane
	 */
	public ObjectTablePane<DefectMapDto> getPartDefectCombTablePane() {
		return partDefectCombTablePane;
	}


	/**
	 * @param textentryTablePane the textentryTablePane to set
	 */
	public void setTextentryTablePane(ObjectTablePane<DefectMapDto> textentryTablePane) {
		this.textentryTablePane = textentryTablePane;
	}

	/**
	 * @return the newExternalCode
	 */
	public LoggedText getNewExternalCode() {
		return newExternalCode;
	}

	/**
	 * @param newExternalCode the newExternalCode to set
	 */
	public void setNewExternalCode(LoggedText newExternalCode) {
		this.newExternalCode = newExternalCode;
	}

	/**
	 * @return the reasonForChangeTextArea
	 */
	public LoggedTextArea getReasonForChangeTextArea() {
		return reasonForChangeTextArea;
	}

	/**
	 * @param reasonForChangeTextArea the reasonForChangeTextArea to set
	 */
	public void setReasonForChangeTextArea(LoggedTextArea reasonForChangeTextArea) {
		this.reasonForChangeTextArea = reasonForChangeTextArea;
	}

	/**
	 * @return the headlessDefectEntryDialogController
	 */
	public HeadlessDefectEntryDialogController getHeadlessDefectEntryDialogController() {
		return headlessDefectEntryDialogController;
	}

	/**
	 * @param headlessDefectEntryDialogController the headlessDefectEntryDialogController to set
	 */
	public void setHeadlessDefectEntryDialogController(HeadlessDefectEntryDialogController headlessDefectEntryDialogController) {
		this.headlessDefectEntryDialogController = headlessDefectEntryDialogController;
	}
	
	/**
	 * @param partDefectCombTablePane the partDefectCombTablePane to set
	 */
	public void setPartDefectCombTablePane(ObjectTablePane<DefectMapDto> partDefectCombTablePane) {
		this.partDefectCombTablePane = partDefectCombTablePane;
	}

	
	/**
	 * @return the addButton
	 */
	public LoggedButton getCreateButton() {
		return createButton;
	}

	/**
	 * @return the cancelButton
	 */
	public LoggedButton getCancelButton() {
		return cancelButton;
	}
	
	/**
	 * @return the isImageEntryScreen
	 */
	public Boolean getIsImageEntryScreen() {
		return isImageEntryScreen;
	}
	
	/**
	 * @param isImageEntryScreen the isImageEntryScreen to set
	 */
	public void setIsImageEntryScreen(Boolean isImageEntryScreen) {
		this.isImageEntryScreen = isImageEntryScreen;
	}
	public CheckBox getQicsRepairCheckBox() {
		return qicsRepairCheckBox;
	}


	public void setQicsRepairCheckBox(CheckBox qicsRepairCheckBox) {
		this.qicsRepairCheckBox = qicsRepairCheckBox;
	}


	public CheckBox getExtSysRepairCheckBox() {
		return extSysRepairCheckBox;
	}

	public void setExtSysRepairCheckBox(CheckBox extSysRepairCheckBox) {
		this.extSysRepairCheckBox = extSysRepairCheckBox;
	}

	public boolean isUpdate()  {
		if(buttonName.equals(QiConstant.UPDATE)) {
			return true;
		}
		else return false;
	}


	public boolean isCancel() {
		return isCancel;
	}


	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}
}
