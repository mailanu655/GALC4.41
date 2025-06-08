package com.honda.galc.client.teamleader.qi.defectResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.defectTagging.DefectTaggingMaintenancePanel;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentPropertyId;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.util.KeyValue;
import com.honda.galc.util.StringUtil;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Screen;
import javafx.util.StringConverter;

public class SearchMaintenancePanel implements EventHandler<ActionEvent>{
	private LoggedLabel siteValueLabel;
	private LabeledComboBox<String> plantComboBox;
	private LabeledComboBox<String> productTypeComboBox;
	private LabeledComboBox<String> workShiftComboBox;
	private LabeledComboBox<String> entryDepartmentComboBox;
	private LabeledComboBox<String> mtcModelComboBox;
	private LabeledComboBox<String> originalDefectStatusComboBox;
    private LabeledComboBox<String> entryModelComboBox;
    private LabeledComboBox<String> changeEntryModelComboBox;
	private LoggedRadioButton updateAttributeRadioBtn;
	private LoggedRadioButton changeDefectRadioBtn;
	private LoggedRadioButton deleteDefectRadioBtn;
	private LoggedRadioButton updateActualProblemAttributeRadioBtn;
	private LoggedRadioButton changeActualProblemRadioBtn;
	private LoggedRadioButton productionDateRadioBtn;
	private LoggedRadioButton entryTimestampRadioBtn;
	private LoggedButton fileSelectBtn;
	private LoggedLabel filePathLabel;
	private DatePicker startDatePicker;
	private DatePicker endDatePicker;
	private LabeledComboBox<String> resSiteComboBox;
	private LabeledComboBox<String> respPlantComboBox;
	private LabeledComboBox<String> respDepartmentComboBox;
	private LabeledComboBox<KeyValue<Integer, String>> responsibleLevel1ComboBox;
	private LabeledComboBox<KeyValue<Integer, String>> responsibleLevel2ComboBox;
	private LabeledComboBox<KeyValue<Integer, String>> responsibleLevel3ComboBox;
	private DefectResultMaintModel model;
	private String controllerType;
	private ToggleGroup group;
	private DefectResultMaintPanel  defectResultMaintPanel;
	private DefectTaggingMaintenancePanel defectTaggingMaintenancePanel;
	private boolean isFullAccess = false;
	private LabeledTextField defectIdTextBox ;	
	private BufferedReader reader = null;
	private ArrayList<String> multipleDefectIds;
	private String entryModelSelectedText;
	private static final String ENTRY_MODEL_ACCEPTED = "Must select entry model before search will be enabled. ";
		
	public SearchMaintenancePanel(DefectResultMaintModel defectResultMaintModel,String controllerType, boolean isFullAccess) {
		group = new ToggleGroup();
		model =defectResultMaintModel;
		siteValueLabel =new LoggedLabel();
		siteValueLabel.setText(getDefaultSiteName());
		plantComboBox = createLabeledComboBox("plantComboBox", "Plant", true, false);
		productTypeComboBox = createLabeledComboBox("productTypeComboBoX", "Product Type", true, false);
		workShiftComboBox = createLabeledComboBoxWorkShift("workShiftComboBoX", "Work Shift ",true, false);
		entryDepartmentComboBox = createLabeledComboBox("entryDepartmentComboBoX", "Entry Dept ", true, false);
		if(controllerType.equalsIgnoreCase(QiConstant.DATA_CORRECTION)){
			defectIdTextBox = new LabeledTextField("Defect ID",true,TextFieldState.EDIT,new Insets(0),75,Pos.TOP_LEFT,false);
			fileSelectBtn = new LoggedButton("Input Multiple DefectIDs","fileSelectBtn");
			fileSelectBtn.setOnAction(this);
		}
		multipleDefectIds = new ArrayList<String>();
		mtcModelComboBox = createLabeledComboBox("mtcModelComboBoX", "MTC Model ", true, false);
		originalDefectStatusComboBox = createLabeledComboBox("originalDefectStatusComboBox", "Original Defect Status ", true, false);
		entryModelComboBox = createLabeledComboBox("entryModelComboBox","Entry Model", true, false);
		entryModelComboBox.setVisible(false);
		resSiteComboBox = createLabeledComboBox("siteComboBox", "Site", true, false);
		respPlantComboBox = createLabeledComboBox("respPlantComboBox", "Resp Plant", true, false);
		respDepartmentComboBox = createLabeledComboBox("departmentComboBox", "Resp Department", true, false);
		responsibleLevel1ComboBox = createLabeledComboBoxWithKeyAndValuePair("responsibility1ComboBox", "Resp Level 1", true, false);
		responsibleLevel2ComboBox = createLabeledComboBoxWithKeyAndValuePair("responsibility2ComboBox", "Resp Level 2", true, false);
		responsibleLevel3ComboBox = createLabeledComboBoxWithKeyAndValuePair("responsibility3ComboBox", "Resp Level 3", true, false);
		startDatePicker = getCalendarDatePicker(00,00,01);
		endDatePicker = getCalendarDatePicker(23,59,59);
		filePathLabel = new LoggedLabel();
		filePathLabel.setText("");
		updateAttributeRadioBtn = createRadioButton(QiConstant.TYPE_OF_CHANGE_UPDATE_ATTRIBUTE, group, true);
		changeDefectRadioBtn = createRadioButton(QiConstant.TYPE_OF_CHANGE_CHANGE_DEFECT, group, false);
		deleteDefectRadioBtn = createRadioButton(QiConstant.TYPE_OF_CHANGE_DELETE_DEFECT, group, false);
		updateActualProblemAttributeRadioBtn = createRadioButton(QiConstant.TYPE_OF_CHANGE_UPDATE_ACTUAL_PROBLEM_ATTRIBUTE, group, false);
		changeActualProblemRadioBtn = createRadioButton(QiConstant.TYPE_OF_CHANGE_CHANGE_ACTUAL_PROBLEM, group, false);
		this.controllerType= controllerType;
		this.isFullAccess = isFullAccess;
		setTimeInitially();
		
		
	}
	
	private void setTimeInitially() {
		

		Date startDate = null;
		Date endDate = null;
		
		ComponentPropertyId componentPropertyId = new ComponentPropertyId("DEFAULT_QI_PROPERTY_INFO","DATA_CORRECTION_CHANGE_DAYS");
		ComponentProperty componentProperty = getModel().findComponentPropertyById(componentPropertyId);

		int totalDays = 10;
		
		if(null!=componentProperty && !StringUtils.isBlank(componentProperty.getPropertyValue())) {
			String  value = componentProperty.getPropertyValue();
			try {
				totalDays = Integer.parseInt(value);
			} catch (NumberFormatException e) {
				totalDays = 10;
			}
		}

		long DAY_IN_MS = 1000 * 60 * 60 * 24;

		startDate = new Date();
		endDate = new Date(startDate.getTime() - (totalDays * DAY_IN_MS));

		startDatePicker.setValue(getLocalDate(endDate));
		endDatePicker.setValue(getLocalDate(startDate));
	}
	
	ChangeListener<String> plantComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
			loadProductComboBox(new_val);
			getModel().setPlant(new_val);
		}
	};
	
	ChangeListener<String> productTypeComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) {
			loadMtcModelComboBox(new_val);
			loadEntryModelComboBox();
		} 
	};
	
	ChangeListener<String> resSiteComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
			loadRespPlantComboBox(new_val);
		} 
	};
	
	ChangeListener<String> respPlantComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
			loadDepartmentComboBox(new_val);
		} 
	};
	
	ChangeListener<String> respDepartmentComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
			 loadResponsibleLevel1ComboBox(new_val);
		} 
	};
	
	ChangeListener<KeyValue<Integer, String>> responsibleLevel1ComboBoxChangeListener = new ChangeListener<KeyValue<Integer, String>>() {
		 public void changed(ObservableValue<? extends KeyValue<Integer, String>> ov,  KeyValue<Integer, String> old_val, KeyValue<Integer, String> new_val) { 
			 loadResponsibleLevel2ComboBox(new_val);
		 }
	};
	
	ChangeListener<KeyValue<Integer, String>> responsibleLevel2ComboBoxChangeListener = new ChangeListener<KeyValue<Integer, String>>() {
		 public void changed(ObservableValue<? extends KeyValue<Integer, String>> ov,  KeyValue<Integer, String> old_val, KeyValue<Integer, String> new_val) { 
			 loadResponsibleLevel3ComboBox(new_val);
		 }
	};
	
	ChangeListener<String> entryModelComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
			if(StringUtils.isBlank(new_val)) {
				getDefectResultMaintPanel().getSearchBtn().setDisable(true);
			}
			else {
				getDefectResultMaintPanel().getSearchBtn().setDisable(false);
			}
		}
	};
	
	private void loadProductComboBox(String plant) {
		getProductTypeComboBox().getItems().clear();
		getProductTypeComboBox().getItems()
				.addAll(getModel().findAllProductTypes());
	}
	
	/**
	 * Load department combo box.
	 *
	 * @param plant the plant
	 */
	public void loadDepartmentComboBox(String plant) {
		getRespDepartmentComboBox().getItems().clear();
		String site = getResSiteComboBox().getValue();
		getRespDepartmentComboBox().getItems().addAll(getModel().findAllBySiteAndPlant(site, plant));
	}
	/**
	 *  Load entry model combo box.
	 *  
	 *  @param plant,
	 *  @param productType
	 */
	
	public void loadEntryModelComboBox() {
		getEntryModelComboBox().getItems().clear();
		
		if(getPlantComboBox().getValue()!=null && getProductTypeComboBox().getValue()!=null) {
			getEntryModelComboBox().getItems().addAll(getModel().findAllByPlantAndProductType(getPlantComboBox().getValue(), getProductTypeComboBox().getValue()));
		}
		
	}
	
	/**
	 * Load mtc model combo box.
	 *
	 * @param productType
	 */
	public void loadMtcModelComboBox(String productType) {
		getMtcModelComboBox().getItems().clear();
		if(controllerType.equalsIgnoreCase(QiConstant.DEFECT_TAGGING)){
			getDefectTaggingMaintenancePanel().getSearchBtn().setDisable(false);
			getDefectTaggingMaintenancePanel().getAdvancedSearchBtn().setDisable(false);
			getDefectTaggingMaintenancePanel().getResetButton().setDisable(false);
		}else if(controllerType.equalsIgnoreCase(QiConstant.DATA_CORRECTION)){
			getDefectResultMaintPanel().getAdvancedSearchBtn().setDisable(false);
			getDefectResultMaintPanel().getResetButton().setDisable(false);
			getDefectResultMaintPanel().getSubmitBtn().setDisable(true);
		}
		
     
     if(controllerType.equalsIgnoreCase(QiConstant.DATA_CORRECTION)) {
    	 if(changeDefectRadioBtn.isSelected() || changeActualProblemRadioBtn.isSelected()) {
    		 getDefectResultMaintPanel().getSearchBtn().setDisable(true);

    	 } if(updateAttributeRadioBtn.isSelected() || deleteDefectRadioBtn.isSelected() || changeActualProblemRadioBtn.isSelected()) {
    		 getDefectResultMaintPanel().getSearchBtn().setDisable(false);
    	 }
     }

		List<String> uniqueArrayList = getMtcModelData(productType);
		getMtcModelComboBox().getItems().setAll(uniqueArrayList);
	}

	private List<String> getMtcModelData(String productType) {
		if (productType == null)
			return Collections.emptyList();
		List<String> mtcModelList = getModel().findAvailableMtcModelData(null, productType.trim());
		List<String> uniqueArrayList = QiCommonUtil.getUniqueArrayList(mtcModelList);
		Collections.sort(uniqueArrayList, Collections.reverseOrder());
		return uniqueArrayList;
	}
	
	
	/**
	 * Load mtc model combo box.
	 *
	 * @param productType
	 */
	public void loadRespPlantComboBox(String siteName) {
		getRespPlantComboBox().getItems().clear();
		List<String> plantList =  getModel().findAllActivePlantsbySite(siteName);
		getRespPlantComboBox().getItems().addAll(plantList);
	}
	
	/**
	 * Load responsible level1 combo box.
	 *
	 * @param new_val the new_val
	 */
	public void loadResponsibleLevel1ComboBox(String department) {
		getResponsibleLevel3ComboBox().getItems().clear();
		getResponsibleLevel2ComboBox().getItems().clear();
		getResponsibleLevel1ComboBox().getItems().clear();
		String siteValue = getResSiteComboBox().getValue();
		String plantValue = getRespPlantComboBox().getValue() != null ? getRespPlantComboBox().getValue().toString() : "";
		
		List<QiResponsibleLevel> responsibleLevel1List = getModel().findAllBySitePlantDepartmentLevel(siteValue, plantValue, department,(short)1);
		for (QiResponsibleLevel responsibleLevel1 : responsibleLevel1List) {
			getResponsibleLevel1ComboBox().getItems().add(getKeyValue(responsibleLevel1.getResponsibleLevelId(), responsibleLevel1.getResponsibleLevelName()));
		}
	}
	
	/**
	 * Load responsible level2 and level2 combo box based on plant site and dept.
	 */
	public void loadResponsibleLevel2ComboBox(KeyValue<Integer, String> new_val) {
		getResponsibleLevel2ComboBox().getItems().clear();
		getResponsibleLevel3ComboBox().getItems().clear();
		if(new_val!=null){
			QiResponsibleLevel responsibleLevel1=getModel().findByResponsibleLevelId(new_val.getKey());
			if(responsibleLevel1!=null && responsibleLevel1.getUpperResponsibleLevelId()!=0){
				QiResponsibleLevel responsibleLevel2=getModel().findByResponsibleLevelId(responsibleLevel1.getUpperResponsibleLevelId());
				if(responsibleLevel2 !=null) {
					getResponsibleLevel2ComboBox().getSelectionModel().select(getKeyValue(responsibleLevel2.getResponsibleLevelId(), responsibleLevel2.getResponsibleLevelName()));
					getResponsibleLevel2ComboBox().getItems().add(getKeyValue(responsibleLevel2.getResponsibleLevelId(), responsibleLevel2.getResponsibleLevelName()));
				}
				QiResponsibleLevel responsibleLevel3=getModel().findByResponsibleLevelId(responsibleLevel2.getUpperResponsibleLevelId());
				if(responsibleLevel3!= null){
					getResponsibleLevel3ComboBox().getSelectionModel().select(getKeyValue(responsibleLevel3.getResponsibleLevelId(), responsibleLevel3.getResponsibleLevelName()));
				}
			}
		}
	}
	
	/**
	 * Load responsible level3 combo box.
	 *
	 * @param department the department
	 */
	public void loadResponsibleLevel3ComboBox(KeyValue<Integer, String> new_val) {
		getResponsibleLevel3ComboBox().getItems().clear();
		if(new_val!=null){
		QiResponsibleLevel responsibleLevel2 = getModel().findByResponsibleLevelId(new_val.getKey());
		 if(responsibleLevel2!=null && responsibleLevel2.getUpperResponsibleLevelId()!=0){
			 QiResponsibleLevel responsibleLevel3 = getModel().findByResponsibleLevelId(responsibleLevel2.getUpperResponsibleLevelId());
				 if(responsibleLevel3!=null) {
					 getResponsibleLevel3ComboBox().getItems().add(getKeyValue(responsibleLevel3.getResponsibleLevelId(), responsibleLevel3.getResponsibleLevelName()));
				}
			}
		}
	}
	
	/**
	 * Gets the key value.
	 *
	 * @param key the key
	 * @param value the value
	 * @return the key value
	 */
	public KeyValue<Integer, String> getKeyValue(Integer key, final String value){
		KeyValue<Integer, String> keyValue = new KeyValue<Integer, String>(key, value){
			private static final long serialVersionUID = 1L;

			@Override
			public String toString() {
				return value;
			}
		};
		return keyValue;
	}
	public MigPane getSearchPanel(){
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		double screenResolutionWidth = screenBounds.getWidth();
		double screenResolutionHeight = screenBounds.getHeight();
		initComponent();
		addListeners();
		MigPane searchPanelMigPane = new MigPane("insets 5", "[left,grow]", "");
		searchPanelMigPane.add(getSiteAndProductContainer(), "wrap");
		
		if(controllerType.equalsIgnoreCase(QiConstant.DATA_CORRECTION)){
			searchPanelMigPane.add(createTitledPane("Type of Change",createTypeOfChangeContainer(),screenResolutionWidth*0.95,screenResolutionHeight*0.10),"span");
		}
		searchPanelMigPane.add(createTitledPane("Where",createWhereContainer(),screenResolutionWidth*0.95,screenResolutionHeight*0.09),"span");
		searchPanelMigPane.add(getResponsibilityAndEntityContainer(screenResolutionWidth , screenResolutionHeight));
		if(QiCommonUtil.isUserInDataCorrectionLimitedGroup()) {
			deleteDefectRadioBtn.setDisable(true);
		} else {
			deleteDefectRadioBtn.setDisable(false);
		}
		return searchPanelMigPane;
	}
	
	private MigPane getResponsibilityAndEntityContainer(double screenResolutionWidth,double screenResolutionHeight){
		MigPane responsibilityAndEntityContainer = new MigPane("insets 5", "[left,grow]", "");
		responsibilityAndEntityContainer.add(createTitledPane("Responsibilities",createResponsibilitiesContainer(),screenResolutionWidth*.60,screenResolutionHeight*0.22));
		responsibilityAndEntityContainer.add(createTitledPane("Entity",createEntityContainer(),screenResolutionWidth*.35,screenResolutionHeight*0.22));
		return responsibilityAndEntityContainer;
		
	}
	private MigPane getSiteAndProductContainer(){
		MigPane siteAndProductContainer = new MigPane("insets 5", "[]10[]35[]10[]10[]10[]", "");
		LoggedLabel siteLabel = UiFactory.createLabel("label", "Site");
		siteLabel.getStyleClass().add("display-label-14"); 
		siteAndProductContainer.add(siteLabel);
		siteAndProductContainer.add(siteValueLabel);
		siteAndProductContainer.add(plantComboBox);
		siteAndProductContainer.add(productTypeComboBox);
		return siteAndProductContainer;
	}
	/**
	 * This method is used to create ComboBox related to Model ,EntryDept , WorkShift etc.
	 */
	private void initComponent() {
		String siteName = siteValueLabel.getText();
		for (Plant plantObj : getModel().findAllBySite(siteName)) { plantComboBox.getControl().getItems().add(plantObj.getPlantName()); }
		List<String> workShiftList = getModel().findAllShifts();
		workShiftComboBox.getControl().getItems().addAll(workShiftList);
		List<String> entryDeptList = getModel().findAllDivisionIdAndName();
		entryDepartmentComboBox.getControl().getItems().addAll(entryDeptList);
		originalDefectStatusComboBox.getControl().getItems().addAll(DefectStatus.getType(1).getName(),DefectStatus.getType(5).getName());
		resSiteComboBox.getControl().getItems().addAll(getModel().findAllQiSite());
		responsibleLevel2ComboBox.getControl().setDisable(true);
		responsibleLevel3ComboBox.getControl().setDisable(true);
	}
	
	public void addListeners() {
		addPlantComboBoxListener();
		addProductTypeComboBoxListener();
		addRespSiteComboBoxListener();
		addRespPlantComboBoxListener();
		addRespDepartmentComboBoxListener();
		addResponsibleLevel1ComboBoxListener();
		addResponsibleLevel2ComboBoxListener();
		addEntryModelComboBoxListener();
	}
	
	/**
	 * This method is event listener for plantComboBox
	 */
	private void addPlantComboBoxListener() {
		getPlantComboBox().valueProperty().addListener(plantComboBoxChangeListener);
	}
	/**
	 * This method is event listener for productTypeComboBox
	 */
	private void addProductTypeComboBoxListener(){
			getProductTypeComboBox().valueProperty().addListener(productTypeComboBoxChangeListener);
	}
	
	/**
	 * This method is event listener for resSiteComboBoxChangeListener
	 */
	private void addRespSiteComboBoxListener(){
		getResSiteComboBox().valueProperty().addListener(resSiteComboBoxChangeListener);
	}
	
	/**
	 * This method is event listener for responsibilities plantComboBox
	 */
	private void addRespPlantComboBoxListener() {
		getRespPlantComboBox().valueProperty().addListener(respPlantComboBoxChangeListener);
	}
	
	/**
	 * This method is event listener for responsibilities departmentComboBox
	 */
	private void addRespDepartmentComboBoxListener() {
		getRespDepartmentComboBox().valueProperty().addListener(respDepartmentComboBoxChangeListener);
	}
	
	/**
	 * This method is event listener for responsibleLevel1ComboBox
	 */
	private void addResponsibleLevel1ComboBoxListener() {
		getResponsibleLevel1ComboBox().valueProperty().addListener(responsibleLevel1ComboBoxChangeListener);
	}
	
	/**
	 * This method is event listener for responsibleLevel2ComboBox
	 */
	private void addResponsibleLevel2ComboBoxListener() {
		getResponsibleLevel2ComboBox().valueProperty().addListener(responsibleLevel2ComboBoxChangeListener);
	}
	/**
	 * This method is event listener for entrymodelcomboBox
	 */
	
	private void addEntryModelComboBoxListener() {
		getEntryModelComboBox().valueProperty().addListener(entryModelComboBoxChangeListener);
	}
		
	/**
	 * This method is used to create Button.
	 * @param text
	 * @param handler
	 * @return
	 */
	public LoggedButton createBtn(String text,EventHandler<ActionEvent> handler)
	{
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setOnAction(handler);
		btn.getStyleClass().add("main-screen-btn");
		return btn;
	}
	/**
	 * This method is used to create type of change container.
	 */
	private MigPane createTypeOfChangeContainer() {
		MigPane typeOfChangePane = new MigPane("insets 5", "[left,grow,fill]", "");
		createTypeOFChangeRadioButton();
		LoggedLabel gatheredDefectLabel = UiFactory.createLabel("gatheredDefects", "Gathered Defects ",Fonts.SS_DIALOG_BOLD(14),TextAlignment.RIGHT);
		LoggedLabel repaireLabel = UiFactory.createLabel("repairs", "Repairs",Fonts.SS_DIALOG_BOLD(14),TextAlignment.RIGHT);
		typeOfChangePane.add(gatheredDefectLabel, "span 3");
		typeOfChangePane.add(repaireLabel, "wrap");
		typeOfChangePane.add(updateAttributeRadioBtn);
		typeOfChangePane.add(changeDefectRadioBtn);
		typeOfChangePane.add(deleteDefectRadioBtn);
		typeOfChangePane.add(updateActualProblemAttributeRadioBtn);
		typeOfChangePane.add(changeActualProblemRadioBtn);
		return typeOfChangePane;
	}
	
	/**
	 * This method is used to create where container.
	 */
	
	private MigPane createWhereContainer() {
		MigPane wherePane = new MigPane("insets 5", "[left,grow,fill]", "");
		createDateAndTimeRadioButton();
		LoggedLabel noteLabel = UiFactory.createLabel("blankFieldNotUsed", "Note: Blank Field Not Used",Fonts.SS_DIALOG_BOLD(14),TextAlignment.RIGHT);
		LoggedLabel startDateLabel = UiFactory.createLabel("startDate", "Start ",Fonts.SS_DIALOG_BOLD(14),TextAlignment.RIGHT);
		LoggedLabel endDateLabel = UiFactory.createLabel("endDate", "End ",Fonts.SS_DIALOG_BOLD(14),TextAlignment.RIGHT);
		wherePane.add(noteLabel);
		wherePane.add(productionDateRadioBtn);
		wherePane.add(entryTimestampRadioBtn);
		wherePane.add(startDateLabel);
		wherePane.add(startDatePicker);
		wherePane.add(endDateLabel);
		wherePane.add(endDatePicker);
		wherePane.add(workShiftComboBox);
		startDateLabel.setPadding(new Insets(0,20,0,0));
		startDatePicker.setStyle("-fx-background-color: white");
		startDatePicker.getEditor().setStyle("-fx-opacity: 1.0;");
		startDatePicker.setPadding(new Insets(0,0,0,-90));
		endDateLabel.setPadding(new Insets(0,20,0,0));
		endDatePicker.setStyle("-fx-background-color: white");
		endDatePicker.getEditor().setStyle("-fx-opacity: 1.0;");
		endDatePicker.setPadding(new Insets(0,0,0,-90));
		workShiftComboBox.setPadding(new Insets(0,50,0,0));
		workShiftComboBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
		return wherePane;
	}
	
	/**
	 * This method is used to create entity container.
	 */
	private MigPane createEntityContainer() {
		MigPane entityPane = new MigPane("insets 5", "[left,fill]", "");
		entityPane.add(entryDepartmentComboBox);
		entityPane.add(mtcModelComboBox,"wrap");
		entityPane.add(originalDefectStatusComboBox);
		
		if(controllerType.equalsIgnoreCase(QiConstant.DATA_CORRECTION)) {
		   entityPane.add(defectIdTextBox,"wrap");
		   entityPane.add(fileSelectBtn);
		   entityPane.add(filePathLabel,"wrap");
		}
		
		entityPane.add(entryModelComboBox);
		
		return entityPane;
	}
	
	/**
	 * This method is used to create responsibilities container.
	 */
	private MigPane createResponsibilitiesContainer() {
		MigPane responsibilitiesPane = new MigPane("insets 5", "[left,fill]", "");
		responsibilitiesPane.add(resSiteComboBox);
		responsibilitiesPane.add(respPlantComboBox);
		responsibilitiesPane.add(respDepartmentComboBox,"wrap");
		responsibilitiesPane.add(responsibleLevel1ComboBox);
		responsibilitiesPane.add(responsibleLevel2ComboBox);
		responsibilitiesPane.add(responsibleLevel3ComboBox);
		return responsibilitiesPane;
	}
	
	/**
	 * This method is used to create the TitlePane 
	 * @param title  
	 * @param content 
	 * @param width
	 * @param height
	 * @return
	 */
	private static TitledPane createTitledPane(String title,Node content,double width,double height) {
        TitledPane titledPane = new TitledPane();
        titledPane.setText(title);
        titledPane.setContent(content);
        titledPane.setPrefSize(width, height);
        return titledPane;
	}
	
	/**
	 * This method is used to create radio button 
	 * @return
	 */
	
	
	private void createTypeOFChangeRadioButton() {
		
		updateAttributeRadioBtn.getStyleClass().add("display-label");
		updateAttributeRadioBtn.setId(QiConstant.TYPE_OF_CHANGE_UPDATE_ATTRIBUTE);
		
		changeDefectRadioBtn.getStyleClass().add("display-label");
		changeDefectRadioBtn.setId(QiConstant.TYPE_OF_CHANGE_CHANGE_DEFECT);
		
		deleteDefectRadioBtn.getStyleClass().add("display-label");
		deleteDefectRadioBtn.setId(QiConstant.TYPE_OF_CHANGE_DELETE_DEFECT);
		
		updateActualProblemAttributeRadioBtn.getStyleClass().add("display-label");
		updateActualProblemAttributeRadioBtn.setId(QiConstant.TYPE_OF_CHANGE_UPDATE_ACTUAL_PROBLEM_ATTRIBUTE);
		
		changeActualProblemRadioBtn.getStyleClass().add("display-label");
		changeActualProblemRadioBtn.setId(QiConstant.TYPE_OF_CHANGE_CHANGE_ACTUAL_PROBLEM);
	}
	
	
	/**
	 * This method is used to create radio button 
	 * @return
	 */
	private void createDateAndTimeRadioButton() {
		ToggleGroup group = new ToggleGroup();
		productionDateRadioBtn = createRadioButton(QiConstant.PRODUCTION_DATE, group, true);
		productionDateRadioBtn.getStyleClass().add("display-label");
		productionDateRadioBtn.setId(QiConstant.PRODUCTION_DATE);
		entryTimestampRadioBtn = createRadioButton(QiConstant.ENTRY_TIMESTAMP, group, false);
		entryTimestampRadioBtn.getStyleClass().add("display-label");
		entryTimestampRadioBtn.setId(QiConstant.PRODUCTION_DATE);
	}
	
	/**
	 * This method is used to create LabeledComboBox
	 * @param id
	 * @return
	 */
	public LabeledComboBox<String> createLabeledComboBox(String id, String labelName, boolean isHorizontal, boolean isMandatory) {
		LabeledComboBox<String> comboBox = new LabeledComboBox<String>(labelName,isHorizontal,new Insets(0),true,isMandatory);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(25);
		comboBox.getControl().getStyleClass().add("combo-box-base");
		comboBox.getControl().setPrefSize(5,5);
		return comboBox;
	}
	
	public LabeledComboBox<String> createLabeledComboBoxWorkShift(String id, String labelName, boolean isHorizontal, boolean isMandatory) {
		LabeledComboBox<String> comboBox = new LabeledComboBox<String>(labelName,isHorizontal,new Insets(0,90,0,10),true,isMandatory);
		BorderPane.setAlignment(comboBox.getLabel(), Pos.CENTER_RIGHT);
		BorderPane.setAlignment(comboBox.getControl(), Pos.CENTER_LEFT);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(25);
		comboBox.getControl().getStyleClass().add("combo-box-base");
		comboBox.getControl().setPrefSize(5,5);
		return comboBox;
	}
	
	
	
	
	/**
	 * This method is used to create Radio Button.
	 * @param title
	 * @param group
	 * @param isSelected
	 * @return
	 */
	public LoggedRadioButton createRadioButton(String title, ToggleGroup group, boolean isSelected) {
		LoggedRadioButton radioButton = new LoggedRadioButton(title);
		radioButton.getStyleClass().add("radio-btn");
		radioButton.setToggleGroup(group);
		radioButton.setSelected(isSelected);
		radioButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent actionEvent) {
				
				if(actionEvent.getSource().equals(getChangeDefectRadioBtn()) || actionEvent.getSource().equals(getChangeActualProblemRadioBtn())) {
					
					EventBusUtil.publish(new StatusMessageEvent(ENTRY_MODEL_ACCEPTED, StatusMessageEventType.ERROR));
					entryModelComboBox.setVisible(true);
					loadEntryModelComboBox();
					getDefectResultMaintPanel().getSearchBtn().setDisable(true);
				}
				else if(actionEvent.getSource().equals(getUpdateAttributeRadioBtn()) || actionEvent.getSource().equals(getDeleteDefectRadioBtn()) || actionEvent.getSource().equals(getUpdateActualProblemAttributeRadioBtn())){
					entryModelComboBox.setVisible(false);
					EventBusUtil.publish(new StatusMessageEvent("",StatusMessageEventType.CLEAR));
					getDefectResultMaintPanel().getSearchBtn().setDisable(false);
					}
					
				else if(actionEvent.getSource().equals(getProductionDateRadioBtn()) || actionEvent.getSource().equals(getEntryTimestampRadioBtn()) ){
					LocalDate start=startDatePicker.getValue();
					LocalDate end=endDatePicker.getValue();
					getStartDatePicker().setValue(null);
					getStartDatePicker().setValue(start);
					getEndDatePicker().setValue(null);
					getEndDatePicker().setValue(end);
				} else {
					getEntryModelComboBox().getItems().clear();
					getDefectResultMaintPanel().getSearchBtn().setDisable(false);
					entryModelComboBox.setVisible(false);
														
				}
			}
		});
		return radioButton;
	}
	/**
	 * This method is used to create Combobox with KeyValue Pair
	 * @param id
	 * @param labelName
	 * @param isHorizontal
	 * @param isMandatory
	 * @return
	 */
	public LabeledComboBox<KeyValue<Integer, String>> createLabeledComboBoxWithKeyAndValuePair(String id, String labelName, boolean isHorizontal, boolean isMandatory) {
		LabeledComboBox<KeyValue<Integer, String>> comboBox = new LabeledComboBox<KeyValue<Integer, String>>(labelName,isHorizontal,new Insets(0),true,isMandatory);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(25);
		comboBox.getControl().getStyleClass().add("combo-box-base");
		comboBox.getControl().setPrefSize(5,5);
		return comboBox;
	}
	
	/**
	 * This method will be used to date picker component.
	 * 
	 * @return
	 */
	private DatePicker getCalendarDatePicker(final int hours , final int minutes , final int seconds) {
		DatePicker datePicker = new DatePicker();
		StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(QiConstant.DATE_FORMAT);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(QiConstant.DATE_TIME_FORMAT);
		ObjectProperty<LocalDateTime> dateTimeValue = new SimpleObjectProperty<LocalDateTime>();
			
			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					if(getEntryTimestampRadioBtn().isSelected()){
						LocalDateTime value=null;
						 if (dateTimeValue.get() == null) {
							  value = LocalDateTime.of(date, LocalTime.of(hours, minutes, seconds));
			                } else {
			                	value =LocalDateTime.of(date, dateTimeValue.get().toLocalTime());
			                }
			            return (value != null) ? dateTimeFormatter.format(value) : "";
					}	
					else
						return dateFormatter.format(date);
				} else {
					return "";
				}
			}
			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					if(getEntryTimestampRadioBtn().isSelected()){
						dateTimeValue.set(LocalDateTime.parse(string,dateTimeFormatter));
						return dateTimeValue.get().toLocalDate();
					}
					return LocalDate.parse(string, dateFormatter);
				} else {
					dateTimeValue.set(null);
					return null;
				}
			}
		};
		datePicker.setConverter(converter);
		datePicker.setPromptText(QiConstant.DATE_FORMAT.toLowerCase());
		datePicker.setStyle("-fx-background-color: #ffc0cb;");
		return datePicker;
	}
	
	/**
	 * This method is used to reset the filter components
	 * @param event
	 */
	public void resetData(ActionEvent event) {
		setTimeInitially();
		List<String> workShiftList = getModel().findAllShifts();
		getWorkShiftComboBox().setItems(FXCollections.observableArrayList(workShiftList));
		getWorkShiftComboBox().getSelectionModel().select(null);
		
		List<String> entryDeptList = getModel().findAllDivisionId();
		getEntryDepartmentComboBox().setItems(FXCollections.observableArrayList(entryDeptList));
		getEntryDepartmentComboBox().getSelectionModel().select(null);
		getEntryModelComboBox().getSelectionModel().select(null);	
		
		List<String> uniqueArrayList = getMtcModelData(getProductTypeComboBox().getValue().trim());
		getMtcModelComboBox().getItems().clear();
     	getMtcModelComboBox().getItems().addAll(uniqueArrayList);
	
		getMtcModelComboBox().getSelectionModel().select(null);
		List<String> originalDefectList = new ArrayList<String>();
		originalDefectList.add(DefectStatus.getType(1).getName());
		originalDefectList.add(DefectStatus.getType(5).getName());
		getOriginalDefectStatusComboBox().setItems(FXCollections.observableArrayList(originalDefectList));
		getOriginalDefectStatusComboBox().getSelectionModel().select(null);
		getResSiteComboBox().setItems(FXCollections.observableArrayList(getModel().findAllQiSite()));
		getResSiteComboBox().getSelectionModel().select(null);
		loadRespPlantComboBox(StringUtils.trimToEmpty(getResSiteComboBox().getSelectionModel().getSelectedItem()));
		loadDepartmentComboBox(StringUtils.trimToEmpty(getRespPlantComboBox().getSelectionModel().getSelectedItem()));
		loadResponsibleLevel1ComboBox(StringUtils.trimToEmpty(getRespDepartmentComboBox().getSelectionModel().getSelectedItem()));
		loadResponsibleLevel2ComboBox(getResponsibleLevel1ComboBox().getSelectionModel().getSelectedItem());
		getResponsibleLevel2ComboBox().getItems().clear();
		getResponsibleLevel2ComboBox().setValue(null);
		getResponsibleLevel3ComboBox().getItems().clear();
		getResponsibleLevel3ComboBox().setValue(null);
		if(controllerType.equalsIgnoreCase(QiConstant.DATA_CORRECTION)) {
			getDefectIdTextBox().setText("");
			getFilePathLabel().setText("");			
		}
		getMultipleDefectIds().clear();
	 
        if(controllerType.equalsIgnoreCase(QiConstant.DATA_CORRECTION)) {    	
		if(updateAttributeRadioBtn.isSelected()|| deleteDefectRadioBtn.isSelected()||updateActualProblemAttributeRadioBtn.isSelected()) {
			getDefectResultMaintPanel().getSearchBtn().setDisable(false);	
		}
        }
	}
	private boolean openFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Text File");
		fileChooser.setInitialDirectory(new File("."));
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt", "*.csv"));
		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			try {
				reader = new BufferedReader(new FileReader(selectedFile));
				fileSelectBtn.setDisable(false);
			} catch (IOException e) {
				return false;
			}
			filePathLabel.setText(selectedFile.toString());
			defectIdTextBox.setText("");
			char delimiter = ',';
			readLinesForDefectlds(delimiter);
		}
		return true;
	}

	private void readLinesForDefectlds(char delimiter) {
		String line = null;
		List<String> listStr = null;
		multipleDefectIds.clear();
		try {
			while ((line = reader.readLine()) != null) {
				listStr = Arrays.asList(line.split(String.valueOf(delimiter)));
				for (String defectId : listStr)
					if (StringUtils.isNumeric(defectId))
						multipleDefectIds.add(defectId);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to perform the actions -- Input Multiple Defect Ids
	 */
	public void handle(ActionEvent event) {
		Object source = event.getSource();
		if (source == fileSelectBtn)
			openFile();
	}
	
	public LoggedLabel getSiteValueLabel() {
		return siteValueLabel;
	}

	private String getDefaultSiteName() {
		return getModel().findSiteName();
	}

	public ComboBox<String> getPlantComboBox() {
		return plantComboBox.getControl();
	}

	public ComboBox<String> getProductTypeComboBox() {
		return productTypeComboBox.getControl();
	}

	public ComboBox<String> getWorkShiftComboBox() {
		return workShiftComboBox.getControl();
	}

	public ComboBox<String> getEntryDepartmentComboBox() {
		return entryDepartmentComboBox.getControl();
	}

	public ComboBox<String> getMtcModelComboBox() {
		return mtcModelComboBox.getControl();
	}

	public ComboBox<String> getOriginalDefectStatusComboBox() {
		return originalDefectStatusComboBox.getControl();
	}

	public ComboBox<String> getResSiteComboBox() {
		return resSiteComboBox.getControl();
	}

	public ComboBox<String> getRespPlantComboBox() {
		return respPlantComboBox.getControl();
	}

	public ComboBox<String> getRespDepartmentComboBox() {
		return respDepartmentComboBox.getControl();
	}

	public ComboBox<KeyValue<Integer, String>> getResponsibleLevel1ComboBox() {
		return responsibleLevel1ComboBox.getControl();
	}

	public ComboBox<KeyValue<Integer, String>> getResponsibleLevel2ComboBox() {
		return responsibleLevel2ComboBox.getControl();
	}

	public ComboBox<KeyValue<Integer, String>> getResponsibleLevel3ComboBox() {
		return responsibleLevel3ComboBox.getControl();
	}

	public LoggedRadioButton getUpdateAttributeRadioBtn() {
		return updateAttributeRadioBtn;
	}

	public LoggedRadioButton getChangeDefectRadioBtn() {
		return changeDefectRadioBtn;
	}

	public LoggedRadioButton getDeleteDefectRadioBtn() {
		return deleteDefectRadioBtn;
	}

	public LoggedRadioButton getUpdateActualProblemAttributeRadioBtn() {
		return updateActualProblemAttributeRadioBtn;
	}

	public LoggedRadioButton getChangeActualProblemRadioBtn() {
		return changeActualProblemRadioBtn;
	}

	public LoggedRadioButton getProductionDateRadioBtn() {
		return productionDateRadioBtn;
	}

	public LoggedRadioButton getEntryTimestampRadioBtn() {
		return entryTimestampRadioBtn;
	}

	public DatePicker getStartDatePicker() {
		return startDatePicker;
	}

	public DatePicker getEndDatePicker() {
		return endDatePicker;
	}
	
	public DefectResultMaintModel getModel() {
		return model;
	}
	
	/**
	 * This method is used to prepare the dynamic query based on parameters
	 * @return
	 */
	public StringBuilder preparedSearchQuery(){
		
		String startDateStr = null;
		String endDateStr = null;
		if (getStartDatePicker().getValue() != null)
			startDateStr = getStartDatePicker().getEditor().getText();
		if (getEndDatePicker().getValue() != null)
			endDateStr = getEndDatePicker().getEditor().getText();
		
		Date startDate = null;
		Date endDate = null;
		if (getStartDatePicker().getValue() != null)
			startDate = java.sql.Date.valueOf(getStartDatePicker().getValue());
		if (getEndDatePicker().getValue() != null)
			endDate = java.sql.Date.valueOf(getEndDatePicker().getValue());
		
		if((startDate != null && startDate.after(new Date()))){
			setUserOperationErrorMessage("Start date should not be greater than today's date.");
		}
		else if((endDate != null &&  startDate!=null && endDate.before(startDate))){
			setUserOperationErrorMessage("End date should be greater than start date.");
		}
		
		
		String productTypeValue=StringUtils.trimToEmpty(getProductTypeComboBox().getValue());
		
		
		String workShiftValue=StringUtils.trimToEmpty(getWorkShiftComboBox().getValue());
		String entryDeptValue=StringUtils.trimToEmpty(getEntryDepartmentComboBox().getValue());
		String mtcModelValue=StringUtils.trimToEmpty(getMtcModelComboBox().getValue());
		String originalDefectValue=StringUtils.trimToEmpty(getOriginalDefectStatusComboBox().getValue());
		String respSiteValue=StringUtils.trimToEmpty(getResSiteComboBox().getValue());
		String respPlantValue=StringUtils.trimToEmpty(getRespPlantComboBox().getValue());
		String respDepartmentValue=StringUtils.trimToEmpty(getRespDepartmentComboBox().getValue());
		String responsibleLevel1Value=StringUtils.trimToEmpty(getResponsibleLevel1ComboBox().getValue()==null?StringUtils.EMPTY:getResponsibleLevel1ComboBox().getValue().toString());
		String responsibleLevel2Value=StringUtils.trimToEmpty(getResponsibleLevel2ComboBox().getValue()==null?StringUtils.EMPTY:getResponsibleLevel2ComboBox().getValue().toString());
		String responsibleLevel3Value=StringUtils.trimToEmpty(getResponsibleLevel3ComboBox().getValue()==null?StringUtils.EMPTY:getResponsibleLevel3ComboBox().getValue().toString());
		String entryModelValue = StringUtils.trimToEmpty(getEntryModelComboBox().getValue()==null?StringUtils.EMPTY:getEntryModelComboBox().getValue().toString());
		String defectIdValue="";
		if(controllerType.equalsIgnoreCase(QiConstant.DATA_CORRECTION))
		{
			defectIdValue=StringUtils.trimToEmpty(getDefectIdTextBox().getText());
		}
		
		StringBuilder searchData=getSearchQuery(startDateStr, endDateStr, productTypeValue, workShiftValue,
				entryDeptValue, mtcModelValue, originalDefectValue,
				respSiteValue, respPlantValue, respDepartmentValue,
				responsibleLevel1Value, responsibleLevel2Value,
				responsibleLevel3Value,defectIdValue,entryModelValue);
		return searchData;
	}
	
	private LocalDate getLocalDate(Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		LocalDate localDate = localDateTime.toLocalDate();
		return localDate;
	}
	
	/**
	 * This method is used to return searchQuery based on filter parameters
	 * @param startDate
	 * @param endDate
	 * @param productTypeValue
	 * @param workShiftValue
	 * @param entryDeptValue
	 * @param mtcModelValue
	 * @param originalDefectValue
	 * @param respSiteValue
	 * @param respPlantValue
	 * @param respDepartmentValue
	 * @param responsibleLevel1Value
	 * @param responsibleLevel2Value
	 * @param responsibleLevel3Value
	 * @param entryModelValue
	 */
	private StringBuilder getSearchQuery(String startDate, String endDate,
			String productTypeValue, String workShiftValue,
			String entryDeptValue, String mtcModelValue,
			String originalDefectValue, String respSiteValue,
			String respPlantValue, String respDepartmentValue,
			String responsibleLevel1Value, String responsibleLevel2Value,
			String responsibleLevel3Value, String defectIdValue,
			String entryModelValue) {
		StringBuilder searchData = new StringBuilder();
		
		String siteName = StringUtils.trimToEmpty(getSiteValueLabel().getText());
		String plantName = StringUtils.trimToEmpty(getPlantComboBox().getValue());
		if(siteName.length()>0)searchData.append("ENTRY_SITE_NAME = '"+siteName+"'");
		if(plantName.length()>0)searchData.append(" AND ENTRY_PLANT_NAME = '"+plantName+"'");
		
		if(productTypeValue.length()>0)searchData.append(" AND PRODUCT_TYPE = '"+productTypeValue+"'");
		if(workShiftValue.length()>0)searchData.append(" AND SHIFT = '"+workShiftValue+"'");
		
		if(entryDeptValue.length()>0) {
			
			String entryDeptSubValue= entryDeptValue;
			if(entryDeptValue.contains("-")) {
				entryDeptSubValue = entryDeptValue.split("-")[0];
			}
			searchData.append(" AND ENTRY_DEPT ='"+entryDeptSubValue+"'");
		}
				
		if(mtcModelValue.length()>0) {
			
			String mtcModelSubValue= mtcModelValue;
			if(mtcModelValue.contains("-")) {
				mtcModelSubValue = mtcModelValue.split("-")[0];
			}
			searchData.append(" AND PRODUCT_SPEC_CODE like '"+"%"+mtcModelSubValue+"%"+"'");
		}
		if(originalDefectValue.length()>0){
			int defectStatus=-1;
			if(originalDefectValue.equals(DefectStatus.REPAIRED.getName()))
				defectStatus=DefectStatus.REPAIRED.getId();
			else if(originalDefectValue.equals(DefectStatus.NOT_REPAIRED.getName()))
				defectStatus=DefectStatus.NOT_REPAIRED.getId();
			searchData.append(" AND ORIGINAL_DEFECT_STATUS ="+defectStatus );
		}
		if(respSiteValue.length()>0)searchData.append(" AND RESPONSIBLE_SITE ='"+respSiteValue+"'" );
		if(respPlantValue.length()>0)searchData.append(" AND RESPONSIBLE_PLANT ='"+respPlantValue+"'");
		if(respDepartmentValue.length()>0)searchData.append(" AND RESPONSIBLE_DEPT ='"+respDepartmentValue+"'");
		if(responsibleLevel1Value.length()>0)searchData.append(" AND RESPONSIBLE_LEVEL1 ='"+responsibleLevel1Value+"'");
		if(responsibleLevel2Value.length()>0)searchData.append(" AND RESPONSIBLE_LEVEL2 ='"+responsibleLevel2Value+"'");
		if(responsibleLevel3Value.length()>0)searchData.append(" AND RESPONSIBLE_LEVEL3 ='"+responsibleLevel3Value+"'");
		if(multipleDefectIds.size()>0) searchData.append(" AND DEFECTRESULTID IN("+StringUtil.toSqlInString(multipleDefectIds)+")");
		else if(defectIdValue.length()>0)searchData.append(" AND DEFECTRESULTID = "+Integer.parseInt(defectIdValue)+"");
			
		if(null!=entryModelValue && entryModelValue.length()>0) searchData.append(" AND ENTRY_MODEL='"+entryModelValue+"'");
		
		if(getEntryTimestampRadioBtn().isSelected()){
			if(startDate!=null && endDate != null){
				searchData.append(" AND c.ACTUAL_TIMESTAMP >= '"+startDate+"' AND c.ACTUAL_TIMESTAMP <='"+endDate+"'");
			}else if(startDate!=null){
				searchData.append(" AND c.ACTUAL_TIMESTAMP >= '"+startDate+"'");
			}
			else if(endDate!=null){
				searchData.append(" AND c.ACTUAL_TIMESTAMP <= '"+endDate+"'");
			}
		}
		else
		{
			if(startDate!=null && endDate != null){
				searchData.append(" AND c.PRODUCTION_DATE >= '"+startDate+" 00:00:01' AND c.PRODUCTION_DATE <='"+endDate+" 23:59:59'");
			}
			else if(startDate!=null){
				searchData.append(" AND c.PRODUCTION_DATE >= '"+startDate+" 00:00:01'");
			}
			else if(endDate!=null){
				searchData.append(" AND c.PRODUCTION_DATE <= '"+endDate+" 23:59:59'");
			}
		}
		return searchData;
	}
	
	public DefectResultMaintPanel getDefectResultMaintPanel() {
		return defectResultMaintPanel;
	}

	public void setDefectResultMaintPanel(DefectResultMaintPanel defectResultMaintPanel) {
		this.defectResultMaintPanel = defectResultMaintPanel;
	}

	public DefectTaggingMaintenancePanel getDefectTaggingMaintenancePanel() {
		return defectTaggingMaintenancePanel;
	}

	public void setDefectTaggingMaintenancePanel(
			DefectTaggingMaintenancePanel defectTaggingMaintenancePanel) {
		this.defectTaggingMaintenancePanel = defectTaggingMaintenancePanel;
	}
	
	/**
	 * this method used to display user operation message
	 */
	public void setUserOperationErrorMessage(final String message){
		EventBusUtil.publish(new StatusMessageEvent(message, StatusMessageEventType.ERROR));
	}
	
    public void setUserOperationErrorMessageOnParkingLot(final String message, boolean flag) {
    	EventBusUtil.publish(new StatusMessageEvent(message, StatusMessageEventType.ERROR));
    }

	public ToggleGroup getGroup() {
		return group;
	}
	
	public LabeledTextField getDefectIdTextBox() {
		return defectIdTextBox;
	}

	public void setDefectIdTextBox(LabeledTextField defectIdTextBox) {
		this.defectIdTextBox = defectIdTextBox;
	}
	
	public LoggedButton getFileSelectButton() {
		return fileSelectBtn;
	}
	
	public void setFileSelectButton(LoggedButton fileSelectBtn) {
		this.fileSelectBtn = fileSelectBtn;
	}
	
	public LoggedLabel getFilePathLabel() {
		return filePathLabel;
	}
	
	public List<String> getMultipleDefectIds() {
		return multipleDefectIds;
	}

	public void setEntryDepartmentComboBox(LabeledComboBox<String> entryDepartmentComboBox) {
		this.entryDepartmentComboBox = entryDepartmentComboBox;
	}

	public ComboBox<String> getEntryModelComboBox() {
		return entryModelComboBox.getControl();
	}

	public void setEntryModelComboBox(LabeledComboBox<String> entryModelComboBox) {
		this.entryModelComboBox = entryModelComboBox;
	}

	public String getEntryModelSelectedText() {
		return entryModelSelectedText;
	}

	public void setEntryModelSelectedText(String entryModelSelectedText) {
		this.entryModelSelectedText = entryModelSelectedText;
	}

	public LabeledComboBox<String> getChangeEntryModelComboBox() {
		return changeEntryModelComboBox;
	}

}
