package com.honda.galc.client.teamleader.qi.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.honda.galc.client.teamleader.qi.controller.EntryScreenDialogController;
import com.honda.galc.client.teamleader.qi.model.EntryScreenMaintenanceModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.qi.QiEntryScreen;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombination;
import com.honda.galc.entity.qi.QiTextEntryMenu;
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */

public class EntryScreenDialog extends QiFxDialog<EntryScreenMaintenanceModel>{
	private LoggedButton createBtn;
	private LoggedButton cancelBtn;	
	private EntryScreenDialogController controller;
	private LoggedComboBox<String> productTypeCombobox;
	private LoggedComboBox<String> entryModelCombobox;	
	private LoggedComboBox<String> plantCombobox;		
	private ListView<String> entryDepartmentListViewMultiSelection;	
	private UpperCaseFieldBean entryScreenNameTextField;
	private UpperCaseFieldBean entryScreenDescriptionTextField;	
	private LoggedRadioButton imageRadioButton;
	private LoggedRadioButton textRadioButton;	
	private LoggedTextArea reasonForChangeTextArea;
	private QiEntryScreen entryScreen;
	private QiEntryScreenDto entryScreenDto;
	private LoggedLabel entryModelVersionLabelText;

	public EntryScreenDialog(String title, QiEntryScreenDto entryScreen,EntryScreenMaintenanceModel model,String applicationId) {
		super(title, applicationId,model);//Fix : Passing applicationId as parameter to fetch correct owner stage.		
		this.entryScreenDto = entryScreen;	
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.controller = new EntryScreenDialogController(model, this, entryScreenDto);
		initComponents();
		if(title.equals(QiConstant.CREATE))
			loadCreateData();			
		else if(title.equals(QiConstant.UPDATE))
			loadUpdateData();		
		controller.initListeners();
		Logger.getLogger().check("Entry Screen Dialog populated");
	}

	@SuppressWarnings("unchecked")
	private void loadCreateData()
	{
		try {
			entryScreen = new QiEntryScreen();
			updateButton.setDisable(true);
			List<String> productList=getModel().findAllProductTypes();
			productTypeCombobox.getItems().addAll(productList);		
			List<String> entryPlantList=getModel().findPlantBySite(getDefaultSiteName());
			plantCombobox.getItems().addAll(entryPlantList);
			reasonForChangeTextArea.setDisable(true);	
		} catch (Exception e) {
			controller.handleException("An error occured in loading Create Entry Screen pop up  ", "Failed to Open Create Entry Screen popup screen", e);
		}
	}

	@SuppressWarnings("unchecked")
	private void loadUpdateData() {
		int index = 0;
		List<String> entryDepartmentNewList=new ArrayList<String>();
		List<String> entryDepartmentDtoListNew=new ArrayList<String>();	
		String defaultSiteName = getDefaultSiteName();
		createBtn.setDisable(true);
		
		productTypeCombobox.getItems().addAll(getModel().findAllProductTypes());		
		productTypeCombobox.setValue(entryScreenDto.getProductType());
		
		entryModelCombobox.getItems().addAll(getModel().findEntryModelByProductType(entryScreenDto.getProductType()));		
		entryModelCombobox.setValue(entryScreenDto.getEntryModel());
		entryModelCombobox.setId("entryModelCmbBox");
		
		plantCombobox.getItems().addAll(getModel().findPlantBySite(defaultSiteName));
		plantCombobox.setValue(entryScreenDto.getPlantName());
		plantCombobox.setId("plantCmbBox");
		
		String[] entryScreenModelArray= entryScreenDto.getEntryScreen().split("\\)");
		String[] entryScreenModelArrayFinal= entryScreenModelArray[0].split("\\(");
		String entryScreenName=entryScreenModelArrayFinal[0];
		
		entryScreenNameTextField.settext(entryScreenName);
		List<Division> entryDepartmentList = getModel().findAllDivisionByPlant(defaultSiteName,entryScreenDto.getPlantName());
		ObservableList<String> entryDepartmentDtoList = FXCollections.observableArrayList(Arrays.asList(entryScreenDto.getDivisionId().split(",")));
		int[] itemIndex = new int[entryDepartmentDtoList.size()];
		for(Division division:entryDepartmentList){
			entryDepartmentNewList.add(division.getDivisionId().trim());
		}		
		for(String allEntryDepartments:entryDepartmentDtoList){
			entryDepartmentDtoListNew.add(allEntryDepartments.trim());
		}
		for(String allEntryDepartmentDtoList:entryDepartmentDtoListNew){
			if(entryDepartmentNewList.contains(allEntryDepartmentDtoList.trim())){
				itemIndex[index] = entryDepartmentDtoList.indexOf(allEntryDepartmentDtoList);				
				index++;
				entryDepartmentNewList.remove(allEntryDepartmentDtoList);
			}
		}
		entryDepartmentListViewMultiSelection.setItems(entryDepartmentDtoList);
		entryDepartmentListViewMultiSelection.getItems().addAll(entryDepartmentNewList);		
		entryDepartmentListViewMultiSelection.getSelectionModel().selectIndices(0,itemIndex);
		entryScreenDescriptionTextField.setText(entryScreenDto.getEntryScreenDescription());
		
		getActiveRadioBtn().setSelected(entryScreenDto.isActive());
		getInactiveRadioBtn().setSelected(!entryScreenDto.isActive());	
		getImageRadioButton().setSelected(entryScreenDto.isImage());
		getTextRadioButton().setSelected(!entryScreenDto.isImage());
		List<QiTextEntryMenu> associatedTextEntryMenus = getModel().findAllAssociatedMenusByEntryScreen(entryScreenDto);
		List<QiEntryScreenDefectCombination> associatedParts = getModel().findAllAssociatedPartsByEntryScreen(entryScreenDto);
		if(!associatedTextEntryMenus.isEmpty() || !associatedParts.isEmpty()) {
			getImageRadioButton().setDisable(true);
			getTextRadioButton().setDisable(true);
		}
		entryModelVersionLabelText.setText(entryScreenDto.getIsUsedVersionData());
	}

	private void initComponents() {			
		VBox outerPane = new VBox();
		outerPane.setPrefHeight(540);
		HBox radioButtonContainer = createStatusRadioButtons(getController());		
		HBox productTypeContainer = new HBox();
		HBox productLabelBoxContainer = new HBox();
		HBox modelContainer = new HBox();
		HBox modelLabelBoxContainer = new HBox();
		HBox plantContainer = new HBox();
		HBox plantLabelBoxContainer = new HBox();
		HBox departmentContainer = new HBox();
		HBox departmentLabelBoxContainer= new HBox();		
		HBox nameContainer = new HBox();
		HBox entryScreenName=new HBox();
		HBox descriptionContainer = new HBox();
		HBox screenTypeContainer = new HBox();
		HBox screenTypeLableBoxContainer = new HBox();
		HBox buttonContainer = new HBox();
		HBox reasonForChangeContainer = new HBox();

		setProductType(productTypeContainer, productLabelBoxContainer);
		setEntryModel(modelContainer, modelLabelBoxContainer);
		setEntryPlant(plantContainer, plantLabelBoxContainer);
		setEntryDepartment(departmentContainer, departmentLabelBoxContainer);
		setEntryScreen(nameContainer, entryScreenName);
		setEntryScreenDescription(descriptionContainer);
		setEntryScreenType(screenTypeContainer, screenTypeLableBoxContainer);
		setButtonContainer(buttonContainer);		
		setReasonForChange(reasonForChangeContainer);

		outerPane.getChildren().addAll(radioButtonContainer,productTypeContainer,modelContainer,plantContainer,departmentContainer,nameContainer,descriptionContainer,screenTypeContainer,reasonForChangeContainer,buttonContainer);
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);


	}

	private void setReasonForChange(HBox reasonForChangeContainer) {
		HBox reasonForChange = new HBox();
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("reasonForChange","Reason for Change");
		reasonForChangeLabel.getStyleClass().add("display-label");
		LoggedLabel asterisk1 = UiFactory.createLabel("asteriskReasonForChange", "*");
		asterisk1.getStyleClass().add("display-label");
		asterisk1.setStyle("-fx-text-fill: red");
		reasonForChange.getChildren().addAll(reasonForChangeLabel,asterisk1);
		reasonForChangeTextArea = UiFactory.createTextArea();
		reasonForChangeTextArea.setId("reasonForCngTxtArea");
		reasonForChangeTextArea.setPrefRowCount(2);
		reasonForChangeTextArea.setWrapText(true);
		reasonForChangeContainer.setPadding(new Insets(10, 10, 10, 10));
		reasonForChangeContainer.setSpacing(26);
		reasonForChangeContainer.getChildren().addAll(reasonForChange, reasonForChangeTextArea);
	}

	private void setButtonContainer(HBox buttonContainer) {
		createBtn = createBtn(QiConstant.CREATE, getController());
		createBtn.setPadding(new Insets(5,5,5,5));
		updateButton = createBtn(QiConstant.UPDATE,getController());
		cancelBtn = createBtn(QiConstant.CANCEL,getController());		
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(5,5,5,5));
		buttonContainer.setSpacing(10);
		buttonContainer.getChildren().addAll(createBtn, updateButton, cancelBtn);
	}

	private void setEntryScreenType(HBox screenTypeContainer, HBox screenTypeLableBoxContainer) {
		LoggedLabel entryScreenTypeLabel = UiFactory.createLabel("label","Entry Screen Type"); 
		entryScreenTypeLabel.getStyleClass().add("display-label");
		LoggedLabel asteriskLabel=getAsteriskLabel(UiFactory.createLabel("label","*"));		
		ToggleGroup typeToggle = new ToggleGroup();
		imageRadioButton =UiFactory.createRadioButton("Image");
		imageRadioButton.setId("Image");
		imageRadioButton.setToggleGroup(typeToggle);
		imageRadioButton.setSelected(true);
		textRadioButton =UiFactory.createRadioButton("Text");
		textRadioButton.setId("Text");
		textRadioButton.setToggleGroup(typeToggle);		
		screenTypeLableBoxContainer.getChildren().addAll(entryScreenTypeLabel,asteriskLabel);		
		screenTypeContainer.getChildren().addAll(screenTypeLableBoxContainer,imageRadioButton, textRadioButton);
		screenTypeContainer.setAlignment(Pos.CENTER_LEFT);
		screenTypeContainer.setSpacing(15);
		screenTypeContainer.setPadding(new Insets(10, 0, 0, 10));
	}

	private void setEntryScreenDescription(HBox descriptionContainer) {
		LoggedLabel entryScreenDescriptionLabel = UiFactory.createLabel("repairMethodDescription","Entry Screen \nDescription");
		entryScreenDescriptionLabel.getStyleClass().add("display-label");				
		entryScreenDescriptionTextField = UiFactory.createUpperCaseFieldBean("entryScreenDescriptionTextField", 25, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		descriptionContainer.setPadding(new Insets(10, 10, 10, 10));
		descriptionContainer.setSpacing(55);
		descriptionContainer.getChildren().addAll(entryScreenDescriptionLabel, entryScreenDescriptionTextField);
	}

	private void setEntryScreen(HBox nameContainer, HBox entryScreenName) {
		LoggedLabel entryScreenNameLabel = UiFactory.createLabel("entryScreenName", "Entry Screen");
		entryScreenNameLabel.getStyleClass().add("display-label");
		LoggedLabel asterisk = UiFactory.createLabel("asteriskRepairMethodName", "*");
		asterisk.getStyleClass().add("display-label");
		asterisk.setStyle("-fx-text-fill: red");
		entryScreenName.getChildren().addAll(entryScreenNameLabel,asterisk);
		entryScreenNameTextField = UiFactory.createUpperCaseFieldBean("entryScreenNameTextField", 25, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		entryScreenNameTextField.setId("entryScrNmTxtFld");
		nameContainer.setPadding(new Insets(10, 10, 10, 10));
		nameContainer.setSpacing(44);
		nameContainer.getChildren().addAll(entryScreenName, entryScreenNameTextField);
	}

	private void setEntryDepartment(HBox departmentContainer, HBox departmentLabelBoxContainer) {
		LoggedLabel entryDepartmentLabel=UiFactory.createLabel("label","Entry Department");
		entryDepartmentLabel.getStyleClass().add("display-label");
		LoggedLabel asteriskLabel=getAsteriskLabel(UiFactory.createLabel("label","*"));
		departmentLabelBoxContainer.getChildren().addAll(entryDepartmentLabel,asteriskLabel);
		departmentLabelBoxContainer.setPadding(new Insets(0,0,-15,0));
		entryDepartmentListViewMultiSelection=new ListView<String>();
		entryDepartmentListViewMultiSelection.setId("entryDeptLstViewMultiSel");
		entryDepartmentListViewMultiSelection.setPrefHeight(100);
		entryDepartmentListViewMultiSelection.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		departmentContainer.setPadding(new Insets(5,5,5,5));
		departmentContainer.setSpacing(28);
		departmentContainer.getChildren().addAll(departmentLabelBoxContainer,entryDepartmentListViewMultiSelection);
	}

	
	private void setEntryPlant(HBox plantContainer, HBox plantLabelBoxContainer) {
		LoggedLabel plantLabel=UiFactory.createLabel("label","Entry Plant");
		plantLabel.getStyleClass().add("display-label");
		LoggedLabel asteriskLabel=getAsteriskLabel(UiFactory.createLabel("label","*"));
		plantLabelBoxContainer.getChildren().addAll(plantLabel,asteriskLabel);
		plantCombobox = new LoggedComboBox<String>();
		plantCombobox.setId("plantCmbBox");
		plantCombobox.getStyleClass().add("combo-box-base");		
		plantContainer.setAlignment(Pos.BASELINE_LEFT);
		plantContainer.setPadding(new Insets(5,5,5,5));
		plantContainer.setSpacing(62);
		plantContainer.getChildren().addAll(plantLabelBoxContainer,plantCombobox);
	}

	
	private void setEntryModel(HBox modelContainer, HBox modelLabelBoxContainer) {
		LoggedLabel entryModelLabel=UiFactory.createLabel("label","Entry Model");
		entryModelLabel.getStyleClass().add("display-label");
		LoggedLabel asteriskLabel=getAsteriskLabel(UiFactory.createLabel("label","*"));
		modelLabelBoxContainer.getChildren().addAll(entryModelLabel,asteriskLabel);
		entryModelCombobox = new LoggedComboBox<String>();
		entryModelCombobox.setId("entryMdlCmbBox");
		entryModelCombobox.getStyleClass().add("combo-box-base");		
		modelContainer.setAlignment(Pos.BASELINE_LEFT);
		modelContainer.setPadding(new Insets(5,5,5,5));
		modelContainer.setSpacing(55);
		LoggedLabel entryModelVersionLabel = UiFactory.createLabel("label","Is Used");
		entryModelVersionLabel.getStyleClass().add("display-label");
		entryModelVersionLabelText = UiFactory.createLabel("label","");
		modelContainer.getChildren().addAll(modelLabelBoxContainer,entryModelCombobox, entryModelVersionLabel, entryModelVersionLabelText);
	
	}

	
	private void setProductType(HBox productTypeContainer, HBox productLabelBoxContainer) {
		LoggedLabel productTypeLabel=UiFactory.createLabel("label","Product Type");
		productTypeLabel.getStyleClass().add("display-label");
		LoggedLabel asteriskLabel=getAsteriskLabel(UiFactory.createLabel("label","*"));
		productLabelBoxContainer.getChildren().addAll(productTypeLabel,asteriskLabel);
		productTypeCombobox = new LoggedComboBox<String>();
		productTypeCombobox.setId("productTypeCmbBox");
		productTypeCombobox.getStyleClass().add("combo-box-base");
		productTypeContainer.setAlignment(Pos.BASELINE_LEFT);
		productTypeContainer.setPadding(new Insets(5,5,5,5));
		productTypeContainer.setSpacing(50);
		productTypeContainer.getChildren().addAll(productLabelBoxContainer,productTypeCombobox);
	}

	private String getDefaultSiteName() {
		return getModel().getSiteName();
	}

	public LoggedRadioButton getImageRadioButton() {
		return imageRadioButton;
	}

	public void setImageRadioButton(LoggedRadioButton imageRadioButton) {
		this.imageRadioButton = imageRadioButton;
	}

	public LoggedRadioButton getTextRadioButton() {
		return textRadioButton;
	}

	public void setTextRadioButton(LoggedRadioButton textRadioButton) {
		this.textRadioButton = textRadioButton;
	}

	public ListView<String> getEntryDepartmentListViewMultiSelection() {
		return entryDepartmentListViewMultiSelection;
	}

	public void setEntryDepartmentListViewMultiSelection(ListView<String> entryDepartmentListViewMultiSelection) {
		this.entryDepartmentListViewMultiSelection = entryDepartmentListViewMultiSelection;
	}

	public QiEntryScreen getEntryScreen() {
		return entryScreen;
	}

	public void setEntryScreen(QiEntryScreen entryScreen) {
		this.entryScreen = entryScreen;
	}

	public UpperCaseFieldBean getEntryScreenNameTextField() {
		return entryScreenNameTextField;
	}

	public void setEntryScreenNameTextField(UpperCaseFieldBean entryScreenNameTextField) {
		this.entryScreenNameTextField = entryScreenNameTextField;
	}

	public UpperCaseFieldBean getEntryScreenDescriptionTextField() {
		return entryScreenDescriptionTextField;
	}

	public void setEntryScreenDescriptionTextField(UpperCaseFieldBean entryScreenDescriptionTextField) {
		this.entryScreenDescriptionTextField = entryScreenDescriptionTextField;
	}

	public LoggedTextArea getReasonForChangeTextArea() {
		return reasonForChangeTextArea;
	}

	public void setReasonForChangeTextArea(LoggedTextArea reasonForChangeTextArea) {
		this.reasonForChangeTextArea = reasonForChangeTextArea;
	}

	public LoggedComboBox<String> getProductTypeCombobox() {
		return productTypeCombobox;
	}

	public void setProductTypeCombobox(LoggedComboBox<String> productTypeCombobox) {
		this.productTypeCombobox = productTypeCombobox;
	}

	private LoggedLabel getAsteriskLabel(LoggedLabel loggedLabel){
		loggedLabel=UiFactory.createLabel("label","*");
		loggedLabel.setStyle("-fx-text-fill: red");
		return loggedLabel;
	}

	public EntryScreenDialogController getController() {
		return controller;
	}

	public void setController(EntryScreenDialogController controller) {
		this.controller = controller;
	}	

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}


	public LoggedButton getCreateBtn() {
		return createBtn;
	}


	public LoggedComboBox<String> getEntryModelCombobox() {
		return entryModelCombobox;
	}
	public void setEntryModelCombobox(LoggedComboBox<String> entryModelCombobox) {
		this.entryModelCombobox = entryModelCombobox;
	}

	public LoggedComboBox<String> getPlantCombobox() {
		return plantCombobox;
	}

	public void setPlantCombobox(LoggedComboBox<String> plantCombobox) {
		this.plantCombobox = plantCombobox;
	}
	
	public LoggedLabel getEntryModelVersionLabelText() {
		return entryModelVersionLabelText;
	}

}
