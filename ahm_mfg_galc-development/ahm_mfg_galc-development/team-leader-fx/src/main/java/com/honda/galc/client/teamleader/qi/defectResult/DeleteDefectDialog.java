package com.honda.galc.client.teamleader.qi.defectResult;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.view.QiFxDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.ColumnMappingList;
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
import com.honda.galc.entity.qi.QiDefectResult;

/**
 * 
 * <h3>DeleteDefectDialog Class description</h3>
 * <p>DeleteDefectDialog is used to delete defects in the Defect Result table.</p>
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
public class DeleteDefectDialog extends QiFxDialog<DefectResultMaintModel> {

	private LoggedButton changeBtn; 
	private LoggedButton cancelBtn;
	private LabeledTextField correctionRequesterTextField;
	private LoggedTextArea reasonForChangeTextArea;
	private ObjectTablePane<QiDefectResult> currentDefectResultTablePane;
	private List<QiDefectResult> selectedDefectResultList;
	private LoggedLabel reasonForChangeErrorMessage;
	private DeleteDefectController controller;
	
	public DeleteDefectDialog(String title,DefectResultMaintModel model, String applicationId,List<QiDefectResult> selectedDefectResultList) {
		super("Delete Defect/Actual Problem", applicationId, model);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.controller = new DeleteDefectController(model, this);
		this.selectedDefectResultList=selectedDefectResultList;
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
		correctionRequesterTextField = new LabeledTextField("Correction Requested By",true,true);
		correctionRequesterTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(32));
		correctionRequesterTextField.getControl().setPrefWidth(screenResolutionWidth/6); 
		correctionRequesterTextField.getLabel().setPrefWidth(screenResolutionWidth/9); 
		correctionRequesterTextField.setDisable(true);
		getDefectResultData(migPane,screenResolutionWidth);
		migPane.add(getReasonForChangeLabel(),"wrap");
		migPane.add(createButtonContainer(),"wrap"); 
		migPane.add(reasonForChangeErrorMessage);
		((BorderPane) this.getScene().getRoot()).setCenter(migPane);
	}
	 
	/**
	 * This method is used to create TableView for Current DefectResult and updated DefectResult
	 * @param defectResultMigpane
	 */
	private void getDefectResultData(MigPane defectResultMigpane,double screenResolutionWidth){
		currentDefectResultTablePane = createDefectResultTablePane("CurrentDefectResultTablePane", screenResolutionWidth);
		currentDefectResultTablePane.getTable().setItems(FXCollections.observableArrayList(selectedDefectResultList));
		reasonForChangeErrorMessage = UiFactory.createLabel("reasonForChangeErrorMessage", "");
		defectResultMigpane.add(createTitledPane("", currentDefectResultTablePane,screenResolutionWidth/.03,200),"wrap");
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
		reasonForChangeTextArea.setPrefHeight(100);
		reasonForChangeTextArea.addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(80));
		reasonForChangeTextArea.setDisable(true);
		return reasonForChangeTextArea;
	}

	/**
	 * This method is used to create the Reason for change label
	 * @return
	 */
	private MigPane getReasonForChangeLabel(){
		MigPane reasonForChangeLabelContainer = new MigPane();
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("reasonForChangeLabel", "Reason for Change");
		reasonForChangeLabel.getStyleClass().add("display-label");
		LoggedLabel asterisk = UiFactory.createLabel("asteriskReasonForChange", "*");
		asterisk.getStyleClass().add("display-label");
		asterisk.setStyle("-fx-text-fill: red");
		reasonForChangeLabelContainer.getChildren().addAll(reasonForChangeLabel, asterisk);
		reasonForChangeLabelContainer.add(getReasonForChangeTextArea());
		reasonForChangeLabelContainer.add(correctionRequesterTextField,"wrap");
		return reasonForChangeLabelContainer; 
	}
	/**
	 * This method is used to create a Table Pane for DefectResult.
	 * 
	 * @return
	 */
	
	private ObjectTablePane<QiDefectResult> createDefectResultTablePane(String id,double screenResolutionWidth) {
		ColumnMappingList columnMappingList = ColumnMappingList.with("DefectResultId", "defectResultId")
				.put("ProductId", "productId").put("InspectionPartName", "inspectionPartName").put("InspectionPartLocationName", "inspectionPartLocationName")
				.put("InspectionPartLocation2Name", "inspectionPartLocation2Name").put("InspectionPart2Name", "inspectionPart2Name").put("InspectionPart2LocationName", "inspectionPart2LocationName")
				.put("InspectionPart2Location2Name", "inspectionPart2Location2Name").put("InspectionPart3Name", "inspectionPart3Name").put("DefectTypeName", "defectTypeName")
				.put("DefectTypeName2", "defectTypeName2").put("PointX", "pointX").put("PointY", "pointY").put("ImageName", "imageName").put("ApplicationId", "applicationId").put("IqsVersion", "iqsVersion").put("IqsCategoryName", "iqsCategoryName").put("IqsQuestionNo", "iqsQuestionNo").put("IqsQuestion", "iqsQuestion").put("ThemeName", "themeName")
				.put("Reportable", "reportable").put("ResponsibleSite", "responsibleSite").put("ResponsiblePlant", "responsiblePlant")
				.put("ResponsibleDept", "responsibleDept").put("ResponsibleLevel3", "responsibleLevel3")
				.put("ResponsibleLevel2", "responsibleLevel2").put("ResponsibleLevel1", "responsibleLevel1")
				.put("ProcessNo", "processNo").put("ProcessName", "processName").put("UnitNo", "unitNo").put("UnitDesc", "unitDesc").put("ResponsibleAssociate", "responsibleAssociate")
				.put("WriteUpDept", "writeUpDept").put("EntrySiteName", "entrySiteName").put("EntryPlantName", "entryPlantName")
				.put("EntryProdLineNo", "entryProdLineNo").put("EntryDept", "entryDept").put("ActualTimestamp", "actualTimestamp")
				.put("ProductionDate", "productionDate").put("Shift", "shift").put("Team", "team").put("ProductType", "productType")
				.put("ProductSpecCode", "productSpecCode").put("BomMainPartNo", "bomMainPartNo").put("OriginalDefectStatus", "originalDefectStatus")
				.put("CurrentDefectStatus", "currentDefectStatus").put("RepairArea", "repairArea").put("RepairMethodNamePlan", "repairMethodNamePlan")
				.put("RepairTimePlan", "repairTimePlan").put("LocalTheme", "localTheme").put("Deleted", "deleted")
				.put("GdpDefect", "gdpDefect").put("TrpuDefect", "trpuDefect").put("TerminalName", "terminalName").put("ProductionLot", "productionLot")
				.put("KdLotNumber", "kdLotNumber").put("afOnSequenceNumber", "afOnSequenceNumber").put("DefectCategoryName", "defectCategoryName")
				.put("IsRepairRelated", "isRepairRelated").put("GroupTimestamp", "groupTimestamp").put("EntryScreen", "entryScreen")
				.put("EngineFiringFlag", "engineFiringFlag").put("IncidentId", "incidentId");
						
		Double[] columnWidth = new Double[] {0.1,0.1,0.1,0.1,0.1,
				0.1,0.1,0.1,0.1,0.1,
				0.1,0.1,0.1,0.1,0.1,
				0.1,0.1,0.1,0.1,0.1
				,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,
				0.1,0.1,0.1,0.1,0.1,
				0.1,0.1,0.1,0.1,0.1,
				0.1,0.1,0.1,0.1,0.1
				,0.1,0.1,0.1,0.1,0.1,
				0.1,0.1,0.1,0.1,0.1,
				0.1,0.1,0.1,0.1,0.1,
				0.1,0.1,0.1
		};	
		
		ObjectTablePane<QiDefectResult> objectTablePane = new ObjectTablePane<QiDefectResult>(columnMappingList,columnWidth);
		objectTablePane.setConstrainedResize(false);
		objectTablePane.setId(id);
		objectTablePane.setPrefWidth(screenResolutionWidth*0.7);
		objectTablePane.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		return objectTablePane;	
	}

	/**
	 * This method is used to create Buttons like Change and Cancel button
	 * @return
	 */
		
	private MigPane createButtonContainer() {
		MigPane buttonContainer = new MigPane();
		this.changeBtn = createBtn("Apply Change", getController());
		changeBtn.setPadding(new Insets(5, 5, 5, 5));
		changeBtn.setDisable(true);
		this.cancelBtn = createBtn(QiConstant.CANCEL, getController());
		cancelBtn.setPadding(new Insets(5, 5, 5, 5));
		buttonContainer.setPadding(new Insets(0, 0, 5, 0));
		buttonContainer.getChildren().addAll(cancelBtn,changeBtn);
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
	
	public String getReasonForChangeText()
	{
		return StringUtils.trimToEmpty(reasonForChangeTextArea.getText());
	}
	
	public LoggedTextArea getReasonForChange() {
		return reasonForChangeTextArea;
	}

	public LoggedButton getChangeBtn() {
		return changeBtn;
	}

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public ObjectTablePane<QiDefectResult> getCurrentDefectResultTablePane() {
		return currentDefectResultTablePane;
	}

	public LoggedLabel getReasonForChangeErrorMessage() {
		return reasonForChangeErrorMessage;
	}
	public void setReasonForChangeErrorMessage(
			LoggedLabel reasonForChangeErrorMessage) {
		this.reasonForChangeErrorMessage = reasonForChangeErrorMessage;
	}
	public LabeledTextField getCorrectionRequesterTextField() {
		return correctionRequesterTextField;
	}

	public DeleteDefectController getController() {
		return controller;
	}

	public void setController(
			DeleteDefectController controller) {
		this.controller = controller;
	}
	
	/**
	 * this method used to display user operation message
	 */
	public void setUserOperationMessage(final String message){
		EventBusUtil.publish(new StatusMessageEvent(message, StatusMessageEventType.INFO));
	}
}
