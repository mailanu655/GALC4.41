package com.honda.galc.client.teamleader.qi.defectResult;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.view.QiFxDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.enumtype.DataCorrectionUpdateAttributes;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiRepairResult;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.util.Callback;

/**
 * 
 * <h3>DefectDataReviewDialog Class description</h3>
 * <p>DefectDataReviewDialog is used to update values in the "Part Defect Combination" for gathered defects in the Defect Results table,Part Defect Combination for the actual problems in the Repair table.</p>
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
public class DefectDataReviewDialog extends QiFxDialog<DefectResultMaintModel> {

	private static final String GDP_DEFECT = "GdpDefect";
	private static final String LOCAL_THEME = "LocalTheme";
	private static final String REPAIR_TIME_PLAN = "RepairTimePlan";
	private static final String REPAIR_METHOD_NAME_PLAN = "RepairMethodNamePlan";
	private static final String REPAIR_AREA = "RepairArea";
	private static final String CURRENT_DEFECT_STATUS = "CurrentDefectStatus";
	private static final String ORIGINAL_DEFECT_STATUS = "OriginalDefectStatus";
	private static final String ENTRY_PLANT_NAME = "EntryPlantName";
	private static final String ENTRY_SITE_NAME = "EntrySiteName";
	private static final String UNIT_DESC = "UnitDesc";
	private static final String UNIT_NO = "UnitNo";
	private static final String PROCESS_NAME = "ProcessName";
	private static final String PROCESS_NO = "ProcessNo";
	private static final String RESPONSIBLE_LEVEL1 = "ResponsibleLevel1";
	private static final String RESPONSIBLE_LEVEL2 = "ResponsibleLevel2";
	private static final String RESPONSIBLE_LEVEL3 = "ResponsibleLevel3";
	private static final String RESPONSIBLE_DEPT = "ResponsibleDept";
	private static final String RESPONSIBLE_PLANT = "ResponsiblePlant";
	private static final String RESPONSIBLE_SITE = "ResponsibleSite";
	private static final String REPORTABLE = "Reportable";
	private static final String DEFECT_TYPE_NAME2 = "DefectTypeName2";
	private static final String DEFECT_TYPE_NAME = "DefectTypeName";
	private static final String INSPECTION_PART3_NAME = "InspectionPart3Name";
	private static final String INSPECTION_PART2_LOCATION2_NAME = "InspectionPart2Location2Name";
	private static final String INSPECTION_PART2_LOCATION_NAME = "InspectionPart2LocationName";
	private static final String INSPECTION_PART2_NAME = "InspectionPart2Name";
	private static final String INSPECTION_PART_LOCATION2_NAME = "InspectionPartLocation2Name";
	private static final String INSPECTION_PART_LOCATION_NAME = "InspectionPartLocationName";
	private static final String INSPECTION_PART_NAME = "InspectionPartName";
	private static final String IQS_VERSION = "IqsVersion";
	private static final String IQS_CATEGORY_NAME = "IqsCategoryName";
	private static final String IQS_QUESTION_NO = "IqsQuestionNo";
	private static final String IQS_QUESTION = "IqsQuestion";
	private static final String IQS_SCORE = "IqsScore";
	private static final String THEME_NAME = "ThemeName";
	private static final String DEFECT_CATEGORY_NAME = "DefectCategoryName";
	private static final String ENTRY_SCREEN = "EntryScreen";
	private LoggedButton changeButton;
	private LoggedButton cancelButton;
	private LabeledTextField approverTextField;
	private DefectDataReviewController controller;
	private LoggedTextArea reasonForChangeTextArea;
	private ObjectTablePane<QiDefectResult> currentDefectResultTablePane;
	private ObjectTablePane<QiDefectResult> updatedDefectResultTablePane;
	private List<QiDefectResult> currentDefectResultList;
	private List<QiDefectResult> updatedResultListFromPopUp;
	private LoggedLabel reasonForChangeErrorMessage;
	private ObjectTablePane<QiRepairResult> currentRepairResultTablePane;
	private ObjectTablePane<QiRepairResult> updatedRepairResultTablePane;
	private List<QiRepairResult> currentRepairResultList;
	private String typeOfChange;
	private boolean noProblemFound;
	private LabeledComboBox<String> reasonForChangeDeptComboBox;
	private LabeledComboBox<String> reasonForChangeCategoryComboBox;
	private LabeledComboBox<String> reasonForChangeDetailComboBox;	

	public DefectDataReviewDialog(String title,DefectResultMaintModel model, String applicationId,List<QiDefectResult> currentDefectResultList,
			List<QiDefectResult> updatedResultListFromPopUp ,List<QiRepairResult> currentRepairResultList , String typeOfChange, boolean noProblemFound) {
		super("Review/Verification Screen", applicationId, model);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.controller = new DefectDataReviewController(model, this,typeOfChange);
		this.currentDefectResultList=currentDefectResultList;
		this.currentRepairResultList = currentRepairResultList;
		this.typeOfChange = typeOfChange;
		this.updatedResultListFromPopUp= updatedResultListFromPopUp;
		this.noProblemFound = noProblemFound;
		initComponents();
		controller.initListeners();
	}

	/**
	 * This method is used to initialize the Components and add those to Migpane
	 */
	private void initComponents() {
		double screenResolutionWidth=Screen.getPrimary().getVisualBounds().getWidth();
		double screenResolutionHeight=Screen.getPrimary().getVisualBounds().getHeight();
		MigPane migPane = new MigPane();
		migPane.setPrefWidth(screenResolutionWidth/1.01);
		migPane.setPrefHeight(screenResolutionHeight/1.5);

		if(typeOfChange.equals(QiConstant.DEFECT_TYPE)){
			getDefectResultData(migPane,screenResolutionWidth);
		}else if(typeOfChange.equals(QiConstant.REPAIR_TYPE)){
			getRepairResultData(migPane,screenResolutionWidth);
		}
		migPane.add(getReasonForChangeMigPane(),"wrap");
		migPane.add(getNoProblemFoundLabel(),"wrap");
		migPane.add(createButtonContainer(),"wrap");
		migPane.add(reasonForChangeErrorMessage);
		((BorderPane) this.getScene().getRoot()).setCenter(migPane);
	}
	
	/**
	 * This method is used to create the Reason for change TextArea
	 * @return
	 */
	private LoggedTextArea getReasonForChangeTextArea(){
		reasonForChangeTextArea = UiFactory.createTextArea();
		reasonForChangeTextArea.setPrefRowCount(4);
		reasonForChangeTextArea.setWrapText(true);
		reasonForChangeTextArea.setPrefWidth(300);
		reasonForChangeTextArea.setPrefHeight(120);
		reasonForChangeTextArea.addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(80));
		return reasonForChangeTextArea;
	}

	/**
	 * This method is used to create the Reason for change label
	 * @return
	 */
	private MigPane getReasonForChangeMigPane() {
		MigPane reasonForChangeMigPane = new MigPane("insets 10 10 10 10");
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("reasonForChangeLabel", "Reason for Change");
		reasonForChangeLabel.getStyleClass().add("display-label");
		LoggedLabel asterisk = UiFactory.createLabel("asteriskReasonForChange", "*");
		asterisk.getStyleClass().add("display-label");
		asterisk.setStyle("-fx-text-fill: red");

		reasonForChangeDeptComboBox = new LabeledComboBox<String>("Department", true, new Insets(5, 5, 5, 5), true, false);
		reasonForChangeCategoryComboBox = new LabeledComboBox<String>("Category", true, new Insets(5, 5, 5, 5), true, false);
		reasonForChangeDetailComboBox = new LabeledComboBox<String>("Detail", true, new Insets(5, 5, 5, 5), true, false);		
		
		reasonForChangeDeptComboBox.getControl().setMinSize(282, 30);
		reasonForChangeCategoryComboBox.getControl().setMinSize(300, 30);
		reasonForChangeDetailComboBox.getControl().setMinSize(320, 30);
		
		reasonForChangeDeptComboBox.getControl().valueProperty().addListener(reasonForChangeDeptComboBoxChangeListener);
		reasonForChangeCategoryComboBox.getControl().valueProperty().addListener(reasonForChangeCategoryComboBoxChangeListener);
		
		approverTextField = new LabeledTextField("Correction Requested By",true,true);
		approverTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(32));
		approverTextField.getControl().setPrefWidth(257); 
		approverTextField.getLabel().setPrefWidth(150); 
		
		HBox labelHBox = new HBox();
		labelHBox.getChildren().addAll(reasonForChangeLabel, asterisk);
		
		reasonForChangeMigPane.add(labelHBox, "span 1 3");
		reasonForChangeMigPane.add(getReasonForChangeTextArea(), "span 1 3");
		reasonForChangeMigPane.add(reasonForChangeDeptComboBox, "wrap");
		reasonForChangeMigPane.add(reasonForChangeCategoryComboBox, "wrap");
		reasonForChangeMigPane.add(reasonForChangeDetailComboBox, "wrap");
		reasonForChangeMigPane.add(approverTextField, "span");
		
		loadDeptCombobox();
		
		return reasonForChangeMigPane;
	}
	
	private LoggedLabel getNoProblemFoundLabel(){
		LoggedLabel noProbleFoundLabel = null;
		if (noProblemFound) {
			if (typeOfChange.equals(QiConstant.DEFECT_TYPE)) {
				noProbleFoundLabel = UiFactory.createLabel("reasonForChangeLabel", "No Problem Found Process will be applied. New Actual Problem with No Problem Found will be created.", 18);
			} else {
				noProbleFoundLabel = UiFactory.createLabel("reasonForChangeLabel", "No Problem Found Process will be applied.", 18);
			}
		} else {
			noProbleFoundLabel = UiFactory.createLabel("reasonForChangeLabel", "");
		}
		noProbleFoundLabel.getStyleClass().add("display-label");
		return noProbleFoundLabel;
	}
	
	/**
	 * This method is used to create a Table Pane for DefectResult.
	 * 
	 * @return
	 */
	
	private ObjectTablePane<QiDefectResult> createDefectResultTablePane(String id,double screenResolutionWidth) {
		ColumnMappingList columnMappingList = getColumnMappingList();
		Double[] columnWidth = getColumnWidths();	
		ObjectTablePane<QiDefectResult> objectTablePane = new ObjectTablePane<QiDefectResult>(columnMappingList,columnWidth);
		objectTablePane.setConstrainedResize(false);
		objectTablePane.setId(id);
		objectTablePane.setPrefWidth(screenResolutionWidth*0.7);
		return objectTablePane;	
	}

	
	/**
	 * This method is used to create a Table Pane for RepairResult                     
	 * 
	 * @return
	 */
	private ObjectTablePane<QiRepairResult> createRepairResultTablePane(String id,double screenResolutionWidth) {
		ColumnMappingList columnMappingList = getColumnMappingList();
		Double[] columnWidth = getColumnWidths();	
		ObjectTablePane<QiRepairResult> objectTablePane = new ObjectTablePane<QiRepairResult>(columnMappingList,columnWidth);
		objectTablePane.setConstrainedResize(false);
		objectTablePane.setId(id);
		objectTablePane.setPrefWidth(screenResolutionWidth*0.7);
		return objectTablePane;	
	}

	private Double[] getColumnWidths() {
		return  new Double[] {0.1,0.1,0.1,0.1,0.1,
				0.1,0.1,0.1,0.1,0.1,
				0.1,0.1,0.1,0.1,0.1,
				0.1,0.1,0.1,0.1,0.1
				,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,
				0.1,0.1,0.1,0.1,0.1,0.1,
				0.1,0.1,0.1,0.1,0.1,
				0.1,0.1,0.1,0.1,0.1
				,0.1,0.1,0.1,0.1,0.1,
				0.1,0.1,0.1,0.1,0.1,
				0.1,0.1,0.1,0.1,0.1,
				0.1,0.1,0.1
		};	
	}

	private ColumnMappingList getColumnMappingList() {
		return ColumnMappingList.with("DefectResultId", "defectResultId")
				.put("ProductId", "productId").put(INSPECTION_PART_NAME, "inspectionPartName").put(INSPECTION_PART_LOCATION_NAME, "inspectionPartLocationName")
				.put(INSPECTION_PART_LOCATION2_NAME, "inspectionPartLocation2Name").put(INSPECTION_PART2_NAME, "inspectionPart2Name")
				.put(INSPECTION_PART2_LOCATION_NAME, "inspectionPart2LocationName").put(INSPECTION_PART2_LOCATION2_NAME, "inspectionPart2Location2Name")
				.put(INSPECTION_PART3_NAME, "inspectionPart3Name").put(DEFECT_TYPE_NAME, "defectTypeName").put(DEFECT_TYPE_NAME2, "defectTypeName2")
				.put("PointX", "pointX").put("PointY", "pointY").put("ImageName", "imageName").put("ApplicationId", "applicationId").put(IQS_VERSION, "iqsVersion")
				.put(IQS_CATEGORY_NAME, "iqsCategoryName").put(IQS_QUESTION_NO, "iqsQuestionNo").put(IQS_QUESTION, "iqsQuestion").put(IQS_SCORE, "iqsScore").put(THEME_NAME, "themeName")
				.put(REPORTABLE, "reportable").put(RESPONSIBLE_SITE, "responsibleSite").put(RESPONSIBLE_PLANT, "responsiblePlant").put(RESPONSIBLE_DEPT, "responsibleDept")
				.put(RESPONSIBLE_LEVEL3, "responsibleLevel3").put(RESPONSIBLE_LEVEL2, "responsibleLevel2").put(RESPONSIBLE_LEVEL1, "responsibleLevel1")
				.put(PROCESS_NO, "processNo").put(PROCESS_NAME, "processName").put(UNIT_NO, "unitNo").put(UNIT_DESC, "unitDesc")
				.put("ResponsibleAssociate", "responsibleAssociate").put("WriteUpDept", "writeUpDept").put(ENTRY_SITE_NAME, "entrySiteName")
				.put(ENTRY_PLANT_NAME, "entryPlantName").put("EntryProdLineNo", "entryProdLineNo").put("EntryDept", "entryDept").put("ActualTimestamp", "actualTimestamp")
				.put("ProductionDate", "productionDate").put("Shift", "shift").put("Team", "team").put("ProductType", "productType")
				.put("ProductSpecCode", "productSpecCode").put("BomMainPartNo", "bomMainPartNo").put(ORIGINAL_DEFECT_STATUS, "originalDefectStatus")
				.put(CURRENT_DEFECT_STATUS, "currentDefectStatus").put(REPAIR_AREA, "repairArea").put(REPAIR_METHOD_NAME_PLAN, "repairMethodNamePlan")
				.put(REPAIR_TIME_PLAN, "repairTimePlan").put(LOCAL_THEME, "localTheme").put("Deleted", "deleted")
				.put(GDP_DEFECT, "gdpDefect").put("TrpuDefect", "trpuDefect").put("TerminalName", "terminalName").put("ProductionLot", "productionLot")
				.put("KdLotNumber", "kdLotNumber").put("afOnSequenceNumber", "afOnSequenceNumber").put(DEFECT_CATEGORY_NAME, "defectCategoryName")
				.put("IsRepairRelated", "isRepairRelated").put("GroupTimestamp", "groupTimestamp").put("EntryScreen", "entryScreen")
				.put("EngineFiringFlag", "engineFiringFlag").put("IncidentId", "incidentId");
	}

	/**
	 * This method is used to create Buttons like Change and Cancel button
	 * @return
	 */
		
	private MigPane createButtonContainer() {
		MigPane buttonContainer = new MigPane();
		this.changeButton = createBtn("Apply Change", getController());
		changeButton.setPadding(new Insets(5, 5, 5, 5));
		this.cancelButton = createBtn(QiConstant.CANCEL, getController());
		cancelButton.setPadding(new Insets(5, 5, 5, 5));
		buttonContainer.setPadding(new Insets(0, 0, 5, 0));
		buttonContainer.getChildren().addAll(cancelButton,changeButton);
		return buttonContainer;
	}
	
	/**
	 * This method is used to create TitledPane for User List.
	 */
	private static TitledPane createTitledPane(String title,Node content,double width,double height) {
        TitledPane titledPane = new TitledPane();
        titledPane.setText(title);
        titledPane.setContent(content);
        titledPane.setPrefSize(width, height);
        return titledPane;
	}
	 
	/**
	 * This method is used to create TableView for Current DefectResult and updated DefectResult
	 * @param defectResultMigpane
	 */
	private void getDefectResultData(MigPane defectResultMigpane,double screenResolutionWidth){
		currentDefectResultTablePane = createDefectResultTablePane("CurrentDefectResultTablePane",screenResolutionWidth);
		updatedDefectResultTablePane = createDefectResultTablePane("UpdatedDefectResultTablePane",screenResolutionWidth);
		currentDefectResultTablePane.getTable().setItems(FXCollections.observableArrayList(currentDefectResultList));
		updatedDefectResultTablePane.getTable().setItems(FXCollections.observableArrayList(updatedResultListFromPopUp));
		reasonForChangeErrorMessage = UiFactory.createLabel("reasonForChangeErrorMessage", "");
		addCellFactoryForDefectResultData();
		defectResultMigpane.add(createTitledPane("Current Data", currentDefectResultTablePane,screenResolutionWidth/.03,200),"wrap");
		defectResultMigpane.add(createTitledPane("Updated Data",updatedDefectResultTablePane ,screenResolutionWidth/.03,200),"wrap");
	}

	private TableColumn<QiDefectResult, String> getDefectResultTableColumnObj(String columnName) {
		@SuppressWarnings("unchecked")
		final TableColumn<QiDefectResult, String> responsibleSite = (TableColumn<QiDefectResult, String>) getTableColumnByName(updatedDefectResultTablePane.getTable(),columnName);
		return responsibleSite;
	}
	
	/**
	 * This method is used to create TableView for Current RepairResult and updated RepairResult
	 * @param repairResultMigpane
	 */
	private void getRepairResultData(MigPane repairResultMigpane,double screenResolutionWidth){
		currentRepairResultTablePane = createRepairResultTablePane("CurrentRepairResultTablePane",screenResolutionWidth);
		updatedRepairResultTablePane = createRepairResultTablePane("UpdatedRepairResultTablePane",screenResolutionWidth);
		currentRepairResultTablePane.getTable().setItems(FXCollections.observableArrayList(currentRepairResultList));
		List<QiRepairResult> updatedRepairResultList = getUpdatedRepairResultList();
		updatedRepairResultTablePane.getTable().setItems(FXCollections.observableArrayList(updatedRepairResultList));
		reasonForChangeErrorMessage = UiFactory.createLabel("reasonForChangeErrorMessage", "");
		addCellFactoryForRepairResultData();
		repairResultMigpane.add(createTitledPane("Current Data", currentRepairResultTablePane,screenResolutionWidth/.03,200),"wrap");
		repairResultMigpane.add(createTitledPane("Updated Data",updatedRepairResultTablePane ,screenResolutionWidth/.03,200),"wrap");
	}
	
	private TableColumn<QiRepairResult, String> getRepairResultTableColumnObj(String columnName) {
		@SuppressWarnings("unchecked")
		final TableColumn<QiRepairResult, String> columnObj = (TableColumn<QiRepairResult, String>) getTableColumnByName(updatedRepairResultTablePane.getTable(),columnName);
		return columnObj;
	}
	
	public List<QiRepairResult> getUpdatedRepairResultList() {
		List<QiRepairResult> currentRepairResultList = populateUpdatedRepairResultList();
		List<QiRepairResult> updatedRepairResultList = new ArrayList<QiRepairResult>(); 
		
		//update repair result with defect result object from popup
		long repairId = 0;
		short actualProblemSeq = 0;
		QiRepairResult currentRepairResult = null;
		QiDefectResult updatedDefectResult = null;
		for (int i = 0; i < currentRepairResultList.size(); i++) {
			currentRepairResult = currentRepairResultList.get(i);
			updatedDefectResult = updatedResultListFromPopUp.get(i);
			repairId = currentRepairResult.getRepairId();
			actualProblemSeq = currentRepairResult.getActualProblemSeq();
			QiRepairResult updatedRepairResult = new QiRepairResult(updatedDefectResult);
			updatedRepairResult.setRepairId(repairId);
			updatedRepairResult.setActualProblemSeq(actualProblemSeq);
			updatedRepairResultList.add(updatedRepairResult);
		}

		return updatedRepairResultList;
	}
	
	/**
	 * This method returns the clone of Repair Result
	 * @return
	 */
	private List<QiRepairResult> populateUpdatedRepairResultList(){
		List<QiRepairResult> updatedRepairResultList= new ArrayList<QiRepairResult>();
		for (QiRepairResult repairResult : currentRepairResultList) {
			updatedRepairResultList.add((QiRepairResult) repairResult.deepCopy());
		}
		return updatedRepairResultList;
	}
	
	/**
	 * this method used to display user operation message
	 */
	public void setUserOperationMessage(final String message){
		EventBusUtil.publish(new StatusMessageEvent(message, StatusMessageEventType.INFO));
	}
	
	public String getReasonForChangeText()
	{
		return StringUtils.trimToEmpty(reasonForChangeTextArea.getText());
	}
	
	public LoggedTextArea getReasonForChange() {
		return reasonForChangeTextArea;
	}

	public DefectDataReviewController getController() {
		return this.controller;
	}

	public void setController(DefectDataReviewController controller) {
		this.controller = controller;
	}

	public ObjectTablePane<QiDefectResult> getCurrentDefectResultTablePane() {
		return currentDefectResultTablePane;
	}

	public ObjectTablePane<QiDefectResult> getUpdatedDefectResultTablePane() {
		return updatedDefectResultTablePane;
	}

	public LoggedLabel getReasonForChangeErrorMessage() {
		return reasonForChangeErrorMessage;
	}
	public void setReasonForChangeErrorMessage(
			LoggedLabel reasonForChangeErrorMessage) {
		this.reasonForChangeErrorMessage = reasonForChangeErrorMessage;
	}
	
	public LabeledTextField getApproverTextField() {
		return approverTextField;
	}

	public LoggedButton getChangeButton() {
		return changeButton;
	}

	public LoggedButton getCancelButton() {
		return cancelButton;
	}

	public ObjectTablePane<QiRepairResult> getCurrentRepairResultTablePane() {
		return currentRepairResultTablePane;
	}

	public void setCurrentRepairResultTablePane(
			ObjectTablePane<QiRepairResult> currentRepairResultTablePane) {
		this.currentRepairResultTablePane = currentRepairResultTablePane;
	}
	
	public ObjectTablePane<QiRepairResult> getUpdatedRepairResultTablePane() {
		return updatedRepairResultTablePane;
	}

	/**
	 * @param objectTablePane 
	 * @param orientation 
	 * @param result
	 * @return
	 */
	public ScrollBar getScrollbar(Orientation orientation, ObjectTablePane objectTablePane) {
		for (Node n :objectTablePane.getTable().lookupAll(".scroll-bar")) {
		    if (n instanceof ScrollBar) {
		        ScrollBar bar = (ScrollBar) n;
		        if (bar.getOrientation().equals(orientation)) {
		            return bar;
		        }
		    }
		}
		return null;
	}
	
	/** This method is used to move scroll bar for update table pane when current table pane scroll bar is moved and viceversa.
	 */
	public void scrollCurrentAndUpdateTablePane(String typeOfChange) {
		ObjectTablePane currentPane=null;
		ObjectTablePane updatedPane=null;
		if(typeOfChange.equals(QiConstant.DEFECT_TYPE)){
			currentPane=getCurrentDefectResultTablePane();
			updatedPane=getUpdatedDefectResultTablePane();
		}else if(typeOfChange.equals(QiConstant.REPAIR_TYPE)){
			currentPane=getCurrentRepairResultTablePane();
			updatedPane=getUpdatedRepairResultTablePane();
		}
		final ScrollBar currentPaneHScroll =getScrollbar(Orientation.HORIZONTAL,currentPane);
		final ScrollBar currentPaneVScroll =getScrollbar(Orientation.VERTICAL,currentPane);
		final ScrollBar updatPaneHScroll =getScrollbar(Orientation.HORIZONTAL,updatedPane);
		final ScrollBar updatePaneVScroll =getScrollbar(Orientation.VERTICAL,updatedPane);
		ChangeListener<Number> currentHScrollChangeListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1,Number arg2) {
				updatPaneHScroll.setValue(currentPaneHScroll.getValue());
			}
		};
		ChangeListener<Number> updateHScrollChangeListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1,Number arg2) {
				currentPaneHScroll.setValue(updatPaneHScroll.getValue());
			}
		};
		
		ChangeListener<Number> currentVScrollChangeListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1,Number arg2) {
				updatePaneVScroll.setValue(currentPaneVScroll.getValue());
			}
		};
		
		ChangeListener<Number> updateVScrollChangeListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1,Number arg2) {
				currentPaneVScroll.setValue(updatePaneVScroll.getValue());
			}
		};
		
		
		updatPaneHScroll.valueProperty().addListener(updateHScrollChangeListener);
		currentPaneVScroll.valueProperty().addListener(currentVScrollChangeListener);
		currentPaneHScroll.valueProperty().addListener(currentHScrollChangeListener);
		updatePaneVScroll.valueProperty().addListener(updateVScrollChangeListener);
	}
	
	private void addCellFactoryForDefectResultData() {
		changeCellColorForDefectResult(getDefectResultTableColumnObj(RESPONSIBLE_SITE));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(RESPONSIBLE_PLANT));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(RESPONSIBLE_DEPT));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(RESPONSIBLE_LEVEL1));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(RESPONSIBLE_LEVEL2));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(RESPONSIBLE_LEVEL3));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(LOCAL_THEME));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(REPORTABLE));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(ORIGINAL_DEFECT_STATUS));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(CURRENT_DEFECT_STATUS));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(GDP_DEFECT));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(INSPECTION_PART_NAME));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(INSPECTION_PART_LOCATION_NAME));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(INSPECTION_PART_LOCATION2_NAME));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(INSPECTION_PART2_NAME));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(INSPECTION_PART2_LOCATION_NAME));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(INSPECTION_PART2_LOCATION2_NAME));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(INSPECTION_PART3_NAME));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(DEFECT_TYPE_NAME));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(DEFECT_TYPE_NAME2));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(PROCESS_NO));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(PROCESS_NAME));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(UNIT_NO));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(UNIT_DESC));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(ENTRY_SITE_NAME));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(ENTRY_PLANT_NAME));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(REPAIR_AREA));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(REPAIR_METHOD_NAME_PLAN));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(REPAIR_TIME_PLAN));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(IQS_VERSION));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(IQS_CATEGORY_NAME));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(IQS_QUESTION));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(IQS_QUESTION_NO));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(IQS_SCORE));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(THEME_NAME));
		changeCellColorForDefectResult(getDefectResultTableColumnObj(DEFECT_CATEGORY_NAME));	
		changeCellColorForDefectResult(getDefectResultTableColumnObj(ENTRY_SCREEN));
	}
	
	/**
	 * This method compares the current data and updated data if not matched then mark that column cell values in red color for Defect Result
	 * @param column
	 * @param columnValue
	 */
	private void changeCellColorForDefectResult(final TableColumn<QiDefectResult, String> column) {
		column.setCellFactory(new Callback<TableColumn<QiDefectResult, String>, TableCell<QiDefectResult, String>>() {
			public TableCell<QiDefectResult, String> call(TableColumn<QiDefectResult, String> soCalledFriendStringTableColumn) {
				return new TableCell<QiDefectResult, String>() {
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						String columnName = DataCorrectionUpdateAttributes.getType(column.getText()).getName();
						QiDefectResult result = (QiDefectResult)this.getTableRow().getItem();
						if(result!= null && isDefectResultUpdated(item, result.getDefectResultId(), columnName)) {
							this.setStyle("-fx-background-color: YELLOW;");
						}
						else
							this.setStyle("");
						setText(item);
					}
				};
			}
		});
	}
	
	private boolean isDefectResultUpdated(String item, long defectResultId, String propertyName) {
		for (QiDefectResult defectResult: currentDefectResultList) {
			try {
				if(defectResult.getDefectResultId()==defectResultId){
					if (!item.equalsIgnoreCase(String.valueOf(new PropertyDescriptor(propertyName, QiDefectResult.class).getReadMethod().invoke(defectResult)))) {
						if(propertyName.equalsIgnoreCase(ORIGINAL_DEFECT_STATUS) || propertyName.equalsIgnoreCase(CURRENT_DEFECT_STATUS)){
							if(!item.equals("-1")){
								return true;
							}
						}else{
							return true;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				controller.handleException("Failed to check updated defect result ", "An error occurred to check updated defect result ",e);
			}
		}
		return false;
	}
	
	private void addCellFactoryForRepairResultData() {
		changeCellColorForRepairResult(getRepairResultTableColumnObj(RESPONSIBLE_SITE));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(RESPONSIBLE_PLANT));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(RESPONSIBLE_DEPT));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(RESPONSIBLE_LEVEL1));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(RESPONSIBLE_LEVEL2));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(RESPONSIBLE_LEVEL3));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(LOCAL_THEME));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(REPORTABLE));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(ORIGINAL_DEFECT_STATUS));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(CURRENT_DEFECT_STATUS));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(GDP_DEFECT));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(INSPECTION_PART_NAME));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(INSPECTION_PART_LOCATION_NAME));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(INSPECTION_PART_LOCATION2_NAME));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(INSPECTION_PART2_NAME));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(INSPECTION_PART2_LOCATION_NAME));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(INSPECTION_PART2_LOCATION2_NAME));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(INSPECTION_PART3_NAME));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(DEFECT_TYPE_NAME));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(DEFECT_TYPE_NAME2));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(PROCESS_NO));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(PROCESS_NAME));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(UNIT_NO));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(UNIT_DESC));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(ENTRY_SITE_NAME));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(ENTRY_PLANT_NAME));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(REPAIR_AREA));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(REPAIR_METHOD_NAME_PLAN));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(REPAIR_TIME_PLAN));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(IQS_VERSION));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(IQS_CATEGORY_NAME));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(IQS_QUESTION));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(IQS_QUESTION_NO));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(THEME_NAME));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(DEFECT_CATEGORY_NAME));
		changeCellColorForRepairResult(getRepairResultTableColumnObj(ENTRY_SCREEN));
	}
	
	/**
	 * This method compares the current data and updated data if not matched then mark that column cell values in yellow color for Defect Result
	 * @param column
	 * @param columnValue
	 */
	private void changeCellColorForRepairResult(final TableColumn<QiRepairResult, String> column) {
		column.setCellFactory(new Callback<TableColumn<QiRepairResult, String>, TableCell<QiRepairResult, String>>() {
		       public TableCell<QiRepairResult, String> call(TableColumn<QiRepairResult, String> soCalledFriendStringTableColumn) {
			        return new TableCell<QiRepairResult, String>() {
			            public void updateItem(String item, boolean empty) {
			            super.updateItem(item, empty);
			            String columnName = DataCorrectionUpdateAttributes.getType(column.getText()).getName();
			            QiRepairResult result = (QiRepairResult)this.getTableRow().getItem();
			            if(result!= null && isRepairResultUpdated(item, result.getRepairId(), columnName)) {
 			            	 this.setStyle("-fx-background-color: YELLOW;");
			            }
			            else
			            	this.setStyle("");
			            setText(item);
			          }
			        };
			      }
			});
	}
	
	private boolean isRepairResultUpdated(String item, long repairId, String propertyName) {
		for (QiRepairResult repairResult: currentRepairResultList) {
			try {
				if(repairResult.getRepairId()==repairId){
					if (!item.equalsIgnoreCase(String.valueOf(new PropertyDescriptor(propertyName, QiRepairResult.class).getReadMethod().invoke(repairResult)))) {
						if(propertyName.equalsIgnoreCase(ORIGINAL_DEFECT_STATUS) || propertyName.equalsIgnoreCase(CURRENT_DEFECT_STATUS)){
							if(!item.equals("-1")){
								return true;
							}
						}else{
							return true;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				controller.handleException("Failed to check updated repair result ", "An error occurred to check updated repair result ",e);
			}
		}
		return false;
	}
	
	public List<QiDefectResult> getUpdatedResultListFromPopUp() {
		return updatedResultListFromPopUp;
	}

	public boolean isNoProblemFound() {
		return noProblemFound;
	}

	public void setNoProblemFound(boolean noProblemFound) {
		this.noProblemFound = noProblemFound;
	}
	
	ChangeListener<String> reasonForChangeDeptComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) { 
			loadCategoryComboBox(new_val);
		} 
	};
	
	ChangeListener<String> reasonForChangeCategoryComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) { 
			loadDetailComboBox(new_val);
		} 
	};
	
	public void loadDeptCombobox(){
		List<String> deptList = getModel().findAllReasonForChangeDept(getModel().getSiteName(), getModel().getPlant());
		deptList.add(0, "");
		reasonForChangeDeptComboBox.setItems(FXCollections.observableArrayList(deptList));
		reasonForChangeDeptComboBox.getControl().getSelectionModel().select(0);
	}
	
	private void loadCategoryComboBox(String dept) {
		reasonForChangeCategoryComboBox.getControl().getItems().clear();
		List<String> categoryList = new ArrayList<String>();
		if (!StringUtils.isBlank(dept)) {
			categoryList = getModel().findReasonForChangeCategory(
				getModel().getSiteName(), getModel().getPlant(), dept);
		}
		reasonForChangeCategoryComboBox.getControl().getItems().addAll(categoryList);
		if (categoryList.size() == 1) {
			reasonForChangeCategoryComboBox.getControl().getSelectionModel().select(0);
		}
	}
	
	private void loadDetailComboBox(String category) {
		reasonForChangeDetailComboBox.getControl().getItems().clear();
		List<String> detailList = new ArrayList<String>();
		if (!StringUtils.isBlank(category)) {
			detailList = getModel().findReasonForChangeDetail(
				getModel().getSiteName(), getModel().getPlant(), 
				reasonForChangeDeptComboBox.getControl().getSelectionModel().getSelectedItem(), category);
		}
		reasonForChangeDetailComboBox.getControl().getItems().addAll(detailList);
		if (detailList.size() == 1) {
			reasonForChangeDetailComboBox.getControl().getSelectionModel().select(0);
		}
	}
	
	public String getSelectedReasonForChangeDept() {
		return reasonForChangeDeptComboBox.getControl().getSelectionModel().getSelectedItem();
	}
	
	public String getSelectedReasonForChangeCategory() {
		return reasonForChangeCategoryComboBox.getControl().getSelectionModel().getSelectedItem();
	}
	
	public String getSelectedReasonForChangeDetail() {
		return reasonForChangeDetailComboBox.getControl().getSelectionModel().getSelectedItem();
	}
}
