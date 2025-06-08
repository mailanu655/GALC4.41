package com.honda.galc.client.teamleader.qi.defectTagging;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.defectResult.DefectResultMaintModel;
import com.honda.galc.client.teamleader.qi.view.QiFxDialog;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiIncident;
import com.honda.galc.util.CommonUtil;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

/**
 * 
 * <h3>DefectTaggingDialog Class description</h3>
 * <p>DefectTaggingDialog is used to craete new incident type for Add/Delete tagging to Defect result</p>
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
public class DefectTaggingDialog extends QiFxDialog<DefectResultMaintModel> {

	private LoggedButton createButton;
	private LoggedButton cancelButton;
	private DefectTaggingController controller;
	private LabeledUpperCaseTextField incidentTitle;
	private LabeledUpperCaseTextField incidentDate;
	private LabeledUpperCaseTextField documentControlType;
	private LabeledUpperCaseTextField documentControlId;
	private LoggedRadioButton criticalRadioButton;
	private LoggedRadioButton complexRadioButton;
	private LoggedTextArea incidentCause;
	private LoggedTextArea reasonForChangeTextArea;
	
	public DefectTaggingDialog(String title, DefectResultMaintModel model, String applicationId,String incidentType) {
		super(title, applicationId, model);
		
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.controller = new DefectTaggingController(model, this, applicationId,incidentType);
		initComponents();
		if(title.equals("Defect Tagging Creation"))
			loadCreateData();			
		else if(title.equals("Defect Tagging Updation"))
			loadUpdateData(incidentType);
		controller.initListeners();
	}
	
	private void loadCreateData(){
		reasonForChangeTextArea.setDisable(true);
		incidentDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		updateButton.setDisable(true);	
	}
	
	
	private void loadUpdateData(String defectTagValue) {
		createButton.setDisable(true);	
		String incidentType = defectTagValue.contains("(")?defectTagValue.split("\\(")[0].trim():defectTagValue;
		String incidentDate = defectTagValue.contains("(")?defectTagValue.split("\\(")[1].split("\\)")[0].trim():defectTagValue;
		QiIncident qiIncident = getModel().findByIncidentTitleAndDate(incidentType, incidentDate);
		incidentTitle.setText(qiIncident.getIncidentTitle());
		if(qiIncident.getIncidentType().equals("CRITICAL"))
			criticalRadioButton.setSelected(true);
		else
			complexRadioButton.setSelected(true);
		incidentCause.setText(qiIncident.getIncidentCause());
		documentControlType.setText(qiIncident.getDocumentControlType());
		documentControlId.setText(qiIncident.getDocumentControlId());
		this.incidentDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(qiIncident.getIncidentDate()));
	}
	
	
	private void initComponents() {
		double screenWidth=Screen.getPrimary().getVisualBounds().getWidth();
		double screenHeight=Screen.getPrimary().getVisualBounds().getHeight();
		MigPane pane = new MigPane("insets 20 20 10 10", "[left,grow,shrink 0]", "[]10[shrink 0]");
		pane.setPrefWidth(screenWidth/1.70);
		pane.setPrefHeight(screenHeight/1.7);
		pane.add(getIncidentTitleTextField(),"left, span, wrap");
		pane.add(getIncidentDateTextField(),"left, span, wrap");
		pane.add(getIncidentCauselabel(),"split 3, span 3,left");
		pane.add(getAsteriskLabel(UiFactory.createLabel("label", "*")),"split 3, span 3,left");
		pane.add(getIncidentCauseTextArea(),"left, span, wrap, gapleft 60");
		pane.add(getRadioButtonlabel(),"split 2, span 2,left");
		pane.add(createLoggedRadioButton(),"left, wrap, span, gapleft 80");
		pane.add(getDocumentControlTypeTextField(),"left, span, wrap");
		pane.add(getDocumentControlIdTextField(),"left, span, wrap");
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("iqsReasonLabel", "Reason for\nChange");
		reasonForChangeLabel.getStyleClass().add("display-label");
		pane.add(reasonForChangeLabel,"split 3,left");
		pane.add(getAsteriskLabel(UiFactory.createLabel("label", "*")),"left");
		reasonForChangeTextArea = UiFactory.createTextArea();
        reasonForChangeTextArea.setPrefRowCount(2);
 		reasonForChangeTextArea.setPrefColumnCount(25);
 		reasonForChangeTextArea.setWrapText(true);
		pane.add(reasonForChangeTextArea,"gapleft 90 ,wrap");
		this.createButton = createBtn(QiConstant.CREATE, getController());
		this.updateButton = createBtn(QiConstant.UPDATE, getController());
		this.cancelButton = createBtn(QiConstant.CANCEL, getController());
		pane.add(createButton, "split 3, left, gapleft 190,gaptop 15");
		pane.add(updateButton,"left, gapleft 40,gaptop 15");
		pane.add(cancelButton,"left,gapleft 40 ,gaptop 15");
		((BorderPane) this.getScene().getRoot()).setCenter(pane);
	}

	/**
	 *this method is used Get Incident Title TextField
	 */
	private LabeledUpperCaseTextField getIncidentTitleTextField(){
		incidentTitle=new LabeledUpperCaseTextField("Incident Title", "IncidentTitle", 35, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, true,new Insets(0,0,0,70));
		incidentTitle.getLabel().setAlignment(Pos.BASELINE_LEFT);
		incidentTitle.setHeight(26);
		
		return incidentTitle;
	}
	
	/**
	 *this method is used Get Incident Date TextField
	 */
	private LabeledUpperCaseTextField getIncidentDateTextField(){
		incidentDate=new LabeledUpperCaseTextField("Incident Date", "IncidentDate", 35, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.READ_ONLY, Pos.BASELINE_LEFT, false,new Insets(0,0,0,80));
		incidentDate.getLabel().setAlignment(Pos.BASELINE_LEFT);
		return incidentDate;
	}
	
	/**
	 *this method is used Get Document Controltype TextField
	 */
	private LabeledUpperCaseTextField getDocumentControlTypeTextField(){
		documentControlType=new LabeledUpperCaseTextField("Document Control Type", "DocumentControlType", 35, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, false,new Insets(0,0,0,26));
		documentControlType.getLabel().setAlignment(Pos.BASELINE_LEFT);
		return documentControlType;
	}
	
	/**
	 *this method is used Get Document ControlId TextField
	 */
	private LabeledUpperCaseTextField getDocumentControlIdTextField(){
		documentControlId=new LabeledUpperCaseTextField("Document Control Id", "DocumentControlId", 35, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, false,new Insets(0,0,0,40));
		documentControlId.getLabel().setAlignment(Pos.BASELINE_LEFT);
		return documentControlId;
	}

	/**
	 *this method is used Get set of Radio Button 
	 */	
	private HBox  createLoggedRadioButton(){ 
		HBox hbox=new HBox();
		ToggleGroup togglegroup = new ToggleGroup();
		criticalRadioButton = createRadioButton("Critical", togglegroup, true, getController());
		criticalRadioButton.getStyleClass().add("display-label");
		criticalRadioButton.setId("Critical");
		criticalRadioButton.setPadding(new Insets(0, 20, 0, 0));
		complexRadioButton = createRadioButton("Complex", togglegroup, false, getController());
		complexRadioButton.getStyleClass().add("display-label");
		complexRadioButton.setId("Complex");
		hbox.getChildren().addAll(criticalRadioButton,complexRadioButton);
		hbox.setAlignment(Pos.BASELINE_LEFT);
		return hbox;
	}
	
		
	/**
	 *this method is used Get Radio Button label
	 */	
	private LoggedLabel getRadioButtonlabel(){
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("incidentCause", "Incident Type");
		reasonForChangeLabel.getStyleClass().add("display-label");
		return reasonForChangeLabel;
	}
	
	/**
	 *this method is used Get Incident Cause label
	 */
	private LoggedLabel getIncidentCauselabel(){
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("incidentCause", "Incident Cause ");
		reasonForChangeLabel.getStyleClass().add("display-label");
		return reasonForChangeLabel;
	}
	
	/**
	 *this method is used Get Incident Cause TextArea
	 */
	private LoggedTextArea  getIncidentCauseTextArea(){
		incidentCause = UiFactory.createTextArea();
		incidentCause.setPrefSize(400,100);
		incidentCause.setWrapText(true);
		return incidentCause;
	}
	
	/**
	 * This method is used to get current time stamp  
	 * 
	 */
	public Timestamp getCurrentTime(){ 
		return CommonUtil.convertTimeStamp(CommonUtil.format(new Date()));
    } 
		
	
	/**
	 *this method is used Get asterisk
	 */
	private LoggedLabel getAsteriskLabel(LoggedLabel loggedLabel) {
		loggedLabel = UiFactory.createLabel("label", "*");
		loggedLabel.setStyle("-fx-text-fill: red");
		return loggedLabel;
	}
	
	
	public LoggedTextArea getReasonForChange() {
		return incidentCause;
	}
	
	
	public LoggedButton getCreateButton() {
		return this.createButton;
	}

	public LoggedButton getCancelButton() {
		return cancelButton;
	}


	public DefectTaggingController getController() {
		return this.controller;
	}
	

	public void setController(DefectTaggingController controller) {
		this.controller = controller;
	}

	public LabeledUpperCaseTextField getIncidentTitle() {
		return incidentTitle;
	}

	public LabeledUpperCaseTextField getIncidentDate() {
		return incidentDate;
	}

	public LabeledUpperCaseTextField getDocumentControlType() {
		return documentControlType;
	}

	public LabeledUpperCaseTextField getDocumentControlId() {
		return documentControlId;
	}

	public LoggedRadioButton getCriticalRadioButton() {
		return criticalRadioButton;
	}

	public LoggedRadioButton getComplexRadioButton() {
		return complexRadioButton;
	}

	public LoggedTextArea getIncidentCause() {
		return incidentCause;
	}

	public LoggedTextArea getReasonForChangeTextArea() {
		return reasonForChangeTextArea;
	}

	public void setReasonForChangeTextArea(LoggedTextArea reasonForChangeTextArea) {
		this.reasonForChangeTextArea = reasonForChangeTextArea;
	}
	

}
