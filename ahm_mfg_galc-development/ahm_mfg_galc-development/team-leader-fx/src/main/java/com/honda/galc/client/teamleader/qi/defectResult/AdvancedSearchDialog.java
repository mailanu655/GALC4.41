package com.honda.galc.client.teamleader.qi.defectResult;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.view.QiFxDialog;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.enumtype.QiReportable;
import com.honda.galc.entity.qi.QiLocalTheme;

/**
 * 
 * <h3>AdvancedSearchDialog Class description</h3>
 * <p>
 * AdvancedSearchDialog is used to update update values in the "Part Defect Combination" for gathered defects in the Defect Results table,Part Defect Combination for the actual problems in the Repair table.
 * </p>
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
public class AdvancedSearchDialog extends QiFxDialog<DefectResultMaintModel> {

	private String productType;
	private AdvancedSearchController controller;
	private LabeledComboBox<String> writeUpDepartmentComboBox;
	private LabeledComboBox<String> nodeIdComboBox; 
	private LabeledComboBox<String> entryScreenComboBx;
	private LabeledComboBox<String> reportableComboBox;
	private LabeledComboBox<String> defectTagComboBox; 
	private LabeledComboBox<String> localThemeComboBox;
	private LabeledComboBox<String> currentStatusComboBox;
	private LabeledComboBox<String> mtcModelComboBox;
	private LabeledUpperCaseTextField assemblySeqTextField;
	private LabeledUpperCaseTextField thruTextField;
	private LabeledUpperCaseTextField productIdStartTextField;
	private LabeledUpperCaseTextField productIdEndTextField;
	private String siteName;
	private String plantName;
	private LoggedButton okButton;
	private LoggedButton cancelButton;
	private LoggedButton resetButton;
	private String advancedSearchedQuery;
	private String controllerType;

	public AdvancedSearchDialog(String title,DefectResultMaintModel model, String applicationId,String siteName,String plant, String productType,String controllerType) {
		super(title, applicationId, model);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.productType= productType;
		this.siteName=siteName;
		this.plantName=plant;
		this.controllerType=controllerType;
		controller= new AdvancedSearchController(model,this);
		initComponents();
		controller.initListeners();
	}
	
	/**
	 * This method is used to initialize the components and add those to Migpane
	 */
	private void initComponents() {
		MigPane migPane = new MigPane();
		assemblySeqTextField= getLabeledUpperCaseTextField("Assembly Seq#", "assemblySeqId",25);
		thruTextField= getLabeledUpperCaseTextField("Thru", "thruId",25);
		entryScreenComboBx=createLabeledComboBox("entryScreenId", "Entry Screen", true, false);
		writeUpDepartmentComboBox=createLabeledComboBox("writeUpdepartmentId", "Writeup Dept", true, false);
		writeUpDepartmentComboBox.setItems(FXCollections.observableList(getModel().findAllDeptBySiteAndPlant(siteName,plantName)));
		writeUpDepartmentComboBox.getLabel().setPadding(new Insets(0, 10, 0, 0));
		entryScreenComboBx.setItems(FXCollections.observableList(getModel().findAllByProductType(productType)));
		nodeIdComboBox=createLabeledComboBox("processPointId", "Process Point", true, false);
		if(controllerType.equalsIgnoreCase(QiConstant.DATA_CORRECTION))
			nodeIdComboBox.setItems(FXCollections.observableList(getModel().findAllNAQProcessPointId()));
		okButton= createBtn(QiConstant.OK, getController());
		double screenResolutionWidth=Screen.getPrimary().getVisualBounds().getWidth();
		double screenResolutionHeight=Screen.getPrimary().getVisualBounds().getHeight();
		migPane.setPrefWidth(screenResolutionWidth/1.6);
		migPane.setPrefHeight(screenResolutionHeight/1.7);
		migPane.add(writeUpDepartmentComboBox,"height ::40");
		migPane.add(entryScreenComboBx,"height ::40");
		migPane.add(nodeIdComboBox,"wrap,height ::40");
		migPane.add(createTitledPane("Data Groupings",getDataGrouping(),screenResolutionWidth*0.4,screenResolutionHeight*0.2),"span 6,wrap");
		migPane.add(createTitledPane("Product Range",getProductRange(),screenResolutionWidth*0.4,screenResolutionHeight*0.25),"span 6,wrap");
		migPane.add(okButton,"skip,split 3");
		cancelButton= createBtn("Cancel", getController());
		resetButton = createBtn("Reset", getController());
		migPane.add(resetButton);
		migPane.add(cancelButton);
		((BorderPane) this.getScene().getRoot()).setCenter(migPane);
	}
	
	/**
	 * This method is used to create the Components and add those into Migpane
	 * @return
	 */
	private MigPane getDataGrouping(){
		MigPane dataGrouping = new MigPane();
		reportableComboBox=createLabeledComboBox("reportableId", "Reportable", true, false); 
		loadReportableComboBox();
		defectTagComboBox=createLabeledComboBox("defectTagId", "Defect Tag", true, false);
		loadIncidentComboBox();
		localThemeComboBox=createLabeledComboBox("trackingCodeId", "Local Theme", true, false); 
		loadLocalThemeComboBox();
		currentStatusComboBox=createLabeledComboBox("currentStatusId", "Current Status", true, false); 
		currentStatusComboBox.getControl().getItems().addAll("Fixed","Not Fixed","Non Repairable");
		reportableComboBox.getLabel().setPadding(new Insets(0, 40, 0, 0));
		defectTagComboBox.getLabel().setPadding(new Insets(0, 40, 0, 0));
		localThemeComboBox.getLabel().setPadding(new Insets(0, 20, 0, 0));
		currentStatusComboBox.getLabel().setPadding(new Insets(0, 20, 0, 0));
		defectTagComboBox.getControl().setMinSize(400, 25);
		dataGrouping.add(reportableComboBox,"wrap,height ::40");
		dataGrouping.add(defectTagComboBox,"wrap,height ::30");
		dataGrouping.add(localThemeComboBox,"wrap,height ::40");
		dataGrouping.add(currentStatusComboBox,"height ::40");
		return dataGrouping;
	}

	private void loadLocalThemeComboBox() {
		for (QiLocalTheme qiLocalTheme : getModel().findAllActiveLocalTheme()) {
			localThemeComboBox.getControl().getItems().add(qiLocalTheme.getLocalTheme());
		}
	}

	private void loadIncidentComboBox() {
		for (String incidentData : getModel().findAllQiIncidentTitle()) {
			defectTagComboBox.getControl().getItems().add(incidentData);
		}
	}

	private void loadReportableComboBox() {
		for (QiReportable qiReportable : QiReportable.values()) {
			reportableComboBox.getControl().getItems().add(qiReportable.getName());
		}
	}
	/**
	 * This method is used to create Components like Start and Ent TextField etc. and add the same in Migpane
	 * @return
	 */
	private MigPane getProductRange(){
		MigPane productRange = new MigPane();
		productIdStartTextField= getLabeledUpperCaseTextField("Product Id Start", "productIdStart",25);
		productIdEndTextField= getLabeledUpperCaseTextField("Product Id End", "productIdEnd",25);
		productRange.add(productIdStartTextField,"wrap,height ::40");
		productRange.add(productIdEndTextField,"wrap,height ::40");
		productRange.add(assemblySeqTextField,"pushx,growx");
		productRange.add(thruTextField,"height ::40");
		return productRange;
	}
	
	/**
	 * This method is used to create LabeledUpperCaseTextField and set the values if seleted on Advance Search
	 * @param labelName
	 * @param componentId
	 * @param height
	 * @param labeledUpperCaseTextField
	 * @return
	 */
	private LabeledUpperCaseTextField getLabeledUpperCaseTextField(String labelName,String componentId,int height){
		LabeledUpperCaseTextField labeledUpperCaseTextField = new LabeledUpperCaseTextField(labelName, componentId,25, Fonts.SS_DIALOG_PLAIN(16), TextFieldState.EDIT, Pos.BASELINE_LEFT, false,new Insets(0,10,10,10));
		labeledUpperCaseTextField.setHeight(30);
		return labeledUpperCaseTextField;
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
	 * This method is used to create LabeledComboBox
	 * @param id
	 * @return
	 */
	public LabeledComboBox<String> createLabeledComboBox(String id, String labelName ,boolean isHorizontal, boolean isMandatory) {
		LabeledComboBox<String> comboBox = new LabeledComboBox<String>(labelName,isHorizontal,new Insets(0),true,isMandatory);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(25);
		comboBox.getControl().getStyleClass().add("combo-box-base");
		comboBox.getControl().setPrefSize(5,5);
		return comboBox;
	}
	
	/**
	 * This method is used to perform Reset operation
	 * @param actionEvent
	 */
	public void resetBtnAction(ActionEvent actionEvent) {
			getWriteUpDepartmentComboBox().setItems(FXCollections.observableList(getModel().findAllDeptBySiteAndPlant(siteName,plantName)));
			getWriteUpDepartmentComboBox().getControl().getSelectionModel().select(null);
			getEntryScreenComboBx().setItems(FXCollections.observableList(getModel().findAllByProductType(productType)));
			getEntryScreenComboBx().getControl().getSelectionModel().select(null);
			if(controllerType.equalsIgnoreCase(QiConstant.DATA_CORRECTION))
			{
				getNodeIdComboBox().setItems(FXCollections.observableList(getModel().findAllNAQProcessPointId()));
				getNodeIdComboBox().getControl().getSelectionModel().select(null);
			}else
			{
				getNodeIdComboBox().getControl().getItems().clear();				
			}
			getReportableComboBox().getControl().getItems().clear();
			loadReportableComboBox();
			getReportableComboBox().getControl().getSelectionModel().select(null);
			getDefectTagComboBox().getControl().getItems().clear();
			loadIncidentComboBox();
			getDefectTagComboBox().getControl().getSelectionModel().select(null);
			getLocalThemeComboBox().getControl().getItems().clear();
			loadLocalThemeComboBox();
			getLocalThemeComboBox().getControl().getSelectionModel().select(null);
			getCurrentStatusComboBox().getControl().getItems().clear();
			getCurrentStatusComboBox().getControl().getItems().addAll("Fixed","Not Fixed","Non Repairable");
			getCurrentStatusComboBox().getControl().getSelectionModel().select(null);
			getProductIdStartTextField().setText(StringUtils.EMPTY);
			getProductIdEndTextField().setText(StringUtils.EMPTY);
			getAssemblySeqTextField().setText(StringUtils.EMPTY);
			getThruTextField().setText(StringUtils.EMPTY);
			setAdvancedSearchedQuery("");
	}
	
	public AdvancedSearchController getController() {
		return controller;
	}

	public void setController(AdvancedSearchController controller) {
		this.controller = controller;
	}

	public LabeledComboBox<String> getWriteUpDepartmentComboBox() {
		return writeUpDepartmentComboBox;
	}

	public void setWriteUpDepartmentComboBox(
			LabeledComboBox<String> writeUpdepartmentComboBox) {
		this.writeUpDepartmentComboBox = writeUpdepartmentComboBox;
	}

	public LabeledComboBox<String> getNodeIdComboBox() {
		return nodeIdComboBox;
	}

	public void setNodeIdComboBox(LabeledComboBox<String> nodeIdComboBox) {
		this.nodeIdComboBox = nodeIdComboBox;
	}

	public LabeledComboBox<String> getEntryScreenComboBx() {
		return entryScreenComboBx;
	}

	public void setEntryScreenComboBx(LabeledComboBox<String> entryScreenComboBx) {
		this.entryScreenComboBx = entryScreenComboBx;
	}

	public LabeledComboBox<String> getReportableComboBox() {
		return reportableComboBox;
	}

	public void setReportableComboBox(LabeledComboBox<String> reportableComboBox) {
		this.reportableComboBox = reportableComboBox;
	}

	public LabeledComboBox<String> getDefectTagComboBox() {
		return defectTagComboBox;
	}

	public void setDefectTagComboBox(LabeledComboBox<String> defectTagComboBox) {
		this.defectTagComboBox = defectTagComboBox;
	}

	public LabeledComboBox<String> getLocalThemeComboBox() {
		return localThemeComboBox;
	}

	public void setLocalThemeComboBox(LabeledComboBox<String> trackingCodeComboBox) {
		this.localThemeComboBox = trackingCodeComboBox;
	}

	public LabeledComboBox<String> getCurrentStatusComboBox() {
		return currentStatusComboBox;
	}

	public void setCurrentStatusComboBox(
			LabeledComboBox<String> currentStatusComboBox) {
		this.currentStatusComboBox = currentStatusComboBox;
	}

	public LabeledComboBox<String> getMtcModelComboBox() {
		return mtcModelComboBox;
	}

	public void setMtcModelComboBox(LabeledComboBox<String> mtcModelComboBox) {
		this.mtcModelComboBox = mtcModelComboBox;
	}

	public LabeledUpperCaseTextField getAssemblySeqTextField() {
		return assemblySeqTextField;
	}

	public void setAssemblySeqTextField(LabeledUpperCaseTextField assemblySeqTextField) {
		this.assemblySeqTextField = assemblySeqTextField;
	}

	public LabeledUpperCaseTextField getThruTextField() {
		return thruTextField;
	}

	public void setThruTextField(LabeledUpperCaseTextField thruTextField) {
		this.thruTextField = thruTextField;
	}

	public LabeledUpperCaseTextField getProductIdStartTextField() {
		return productIdStartTextField;
	}

	public void setProductIdStartTextField(
			LabeledUpperCaseTextField productIdStartTextField) {
		this.productIdStartTextField = productIdStartTextField;
	}

	public LabeledUpperCaseTextField getProductIdEndTextField() {
		return productIdEndTextField;
	}

	public void setProductIdEndTextField(LabeledUpperCaseTextField productIdEndTextField) {
		this.productIdEndTextField = productIdEndTextField;
	}

	public LoggedButton getOkButton() {
		return okButton;
	}

	public void setOkButton(LoggedButton okButton) {
		this.okButton = okButton;
	}

	public LoggedButton getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(LoggedButton cancelButton) {
		this.cancelButton = cancelButton;
	}

	public LoggedButton getResetButton() {
		return resetButton;
	}
	
	public String getAdvancedSearchedQuery() {
		return advancedSearchedQuery;
	}

	public void setAdvancedSearchedQuery(String advancedSearchedQuery) {
		this.advancedSearchedQuery = advancedSearchedQuery;
	}
	
	public String getControllerType() {
		return controllerType;
	}

	public void setControllerType(String controllerType) {
		this.controllerType = controllerType;
	}

	
}
