package com.honda.galc.client.teamleader.mbpn;

import java.util.List;

import org.tbee.javafx.scene.layout.MigPane;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.StatusMessagePane;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.product.Mbpn;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * 
 * <h3>MbpnDialog Class description</h3>
 * <p> MbpnDialog description </p>
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
 * @author L&T Infotech<br>
 *
 *
 */

/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
public class MbpnDialog extends FxDialog {

	private LoggedComboBox<String> mainNoCombobox;
	private LoggedComboBox<String> classNoCombobox;
	private LoggedComboBox<String> protoTypeCombobox;
	private LoggedComboBox<String> typeNoCombobox;
	private LoggedComboBox<String> supplementaryNoCombobox;
	private LoggedComboBox<String> targetNoCombobox;
	private LoggedComboBox<String> hesColorCombobox;
	private LoggedTextArea descriptionTextField;
	private LoggedButton createBtn;
	private LoggedButton updateBtn;
	private LoggedButton cancelBtn;
	private MbpnDialogController mbpnDialogController;
	private LoggedTextArea reasonForChangeTextArea;
	private StatusMessagePane statusMessagePane;
	MbpnModel model;
	String screenName;

	public MbpnDialog(String title, Mbpn mbpn, MbpnModel model,String applicationId) {
		super(title, applicationId);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.model=model;
		this.mbpnDialogController=new MbpnDialogController(model, this, mbpn);
		EventBusUtil.register(this);
		((BorderPane) this.getScene().getRoot()).setBottom(initStatusMessagePane());
		initComponents();
		if(title.equalsIgnoreCase(QiConstant.CREATE))
			loadCreateData();			
		else if(title.equalsIgnoreCase(QiConstant.UPDATE))
			loadUpdateData(mbpn);		
		mbpnDialogController.initListeners();
	}

	@SuppressWarnings("unchecked")
	private void loadCreateData() {
		try{
			updateBtn.setDisable(true);
			reasonForChangeTextArea.setDisable(true);
			List<String> mainNoList=getModel().findAllMainNo();
			mainNoCombobox.getItems().addAll(mainNoList);
			List<String> classNoList=getModel().findAllClassNo();
			classNoCombobox.getItems().addAll(classNoList);
			List<String> protoTypeList=getModel().findAllProtoTypeCode();
			protoTypeCombobox.getItems().addAll(protoTypeList);
			List<String> typeNoList=getModel().findAllTypeNo();
			typeNoCombobox.getItems().addAll(typeNoList);
			List<String> supplementaryNoList=getModel().findAllSupplementaryNo();
			supplementaryNoCombobox.getItems().addAll(supplementaryNoList);
			List<String> targetNoList=getModel().findAllTargetNo();
			targetNoCombobox.getItems().addAll(targetNoList);
			List<String> hesColorList=getModel().findAllHesColor();
			hesColorCombobox.getItems().addAll(hesColorList);
		}
		catch (Exception e) {
			mbpnDialogController.handleException("An error occured in loading Create pop up screen ", "Failed to Open Create popup screen", e);
		}
	}

	@SuppressWarnings("unchecked")
	private void loadUpdateData(Mbpn mbpn) {
		try{
			createBtn.setDisable(true);
			mainNoCombobox.getItems().addAll(getModel().findAllMainNo());
			mainNoCombobox.setValue(mbpn.getMainNo());
			classNoCombobox.getItems().addAll(getModel().findAllClassNo());
			classNoCombobox.setValue(mbpn.getClassNo());
			protoTypeCombobox.getItems().addAll(getModel().findAllProtoTypeCode());
			protoTypeCombobox.setValue(mbpn.getPrototypeCode());
			typeNoCombobox.getItems().addAll(getModel().findAllTypeNo());
			typeNoCombobox.setValue(mbpn.getTypeNo());
			supplementaryNoCombobox.getItems().addAll(getModel().findAllSupplementaryNo());
			supplementaryNoCombobox.setValue(mbpn.getSupplementaryNo());
			targetNoCombobox.getItems().addAll(getModel().findAllTargetNo());
			targetNoCombobox.setValue(mbpn.getTargetNo());
			hesColorCombobox.getItems().addAll(getModel().findAllHesColor());
			hesColorCombobox.setValue(mbpn.getHesColor());
			descriptionTextField.setText(mbpn.getDescription());
		}
		catch (Exception e) {
			mbpnDialogController.handleException("An error occured in loading Update pop up screen ", "Failed to Open Update popup screen", e);
		}
	}

	private void initComponents() {
		VBox outerPane = new VBox();
		outerPane.setSpacing(20);
		outerPane.setPrefWidth(600);
		outerPane.setPrefHeight(400);
		outerPane.getChildren().addAll(createMainContainer());
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
	}

	private HBox reasonForChange() {
		HBox reasonForChangeContainer = new HBox();
		reasonForChangeTextArea = UiFactory.createTextArea();
		reasonForChangeTextArea.setPrefRowCount(4);
		reasonForChangeTextArea.setPrefColumnCount(35);
		reasonForChangeTextArea.setWrapText(true);
		reasonForChangeTextArea.setId("reasonForChangeTextArea");

		HBox reasonAsterickContainer = new HBox();
		LoggedLabel reasonForChangeLabel = UiFactory.createLabelWithStyle("reasonForChangeLabel", "Reason for"+"\n"+"   Change","display-label");
		LoggedLabel asterisk2 = UiFactory.createLabelWithStyle("asterisk", "*","display-label");
		asterisk2.setStyle("-fx-text-fill: red");
		reasonAsterickContainer.getChildren().addAll(reasonForChangeLabel,asterisk2);

		reasonForChangeContainer.getChildren().addAll(reasonAsterickContainer,reasonForChangeTextArea);
		reasonForChangeContainer.setPadding(new Insets(10,0,0,30));
		return reasonForChangeContainer;
	}

	@SuppressWarnings("unchecked")
	private HBox hesColorCombobox() {
		HBox hesColorComboboxContainer = new HBox();
		LoggedLabel hesColorLabel = UiFactory.createLabelWithStyle("hesColorLabel", "Hes Color", "display-label");
		hesColorLabel.setPadding(new Insets(5,5,5,5));
		hesColorLabel.setMaxWidth(130.0);
		hesColorLabel.setPrefWidth(130.0);

		hesColorCombobox = new LoggedComboBox<String>();
		hesColorCombobox.getStyleClass().add("combo-box-base");		
		hesColorCombobox.setPadding(new Insets(4, 4, 4, 4));
		hesColorCombobox.setOnAction(getMbpnDialogController());
		hesColorCombobox.setEditable(true);
		hesColorComboboxContainer.getChildren().addAll(hesColorLabel,hesColorCombobox);
		return hesColorComboboxContainer;
	}

	private HBox createDescriptionContainer() {
		HBox descContainer = new HBox();
		LoggedLabel descLabel= UiFactory.createLabelWithStyle("descLabel", "Description", "display-label");
		descLabel.setPadding(new Insets(5,5,5,5));
		descLabel.setMaxWidth(85.0);
		descLabel.setPrefWidth(85.0);
		descriptionTextField = UiFactory.createTextArea();
		descriptionTextField.setPrefRowCount(2);
		descriptionTextField.setPrefColumnCount(13);
		descriptionTextField.setPadding(new Insets(1, 1, 1, 1));
		descContainer.setSpacing(5);
		descContainer.getChildren().addAll(descLabel, descriptionTextField);
		return descContainer;
	}

	@SuppressWarnings("unchecked")
	private HBox targetNoCombobox() {
		HBox targetNoComboboxContainer = new HBox();
		LoggedLabel targetNoLabel = UiFactory.createLabelWithStyle("targetNoLabel", "Target No", "display-label");
		targetNoLabel.setPadding(new Insets(5,5,5,5));
		targetNoLabel.setMaxWidth(85.0);
		targetNoLabel.setPrefWidth(85.0);

		targetNoCombobox = new LoggedComboBox<String>();
		targetNoCombobox.getStyleClass().add("combo-box-base");		
		targetNoCombobox.setPadding(new Insets(4, 4, 4, 4));
		targetNoCombobox.setOnAction(getMbpnDialogController());
		targetNoCombobox.setEditable(true);
		targetNoComboboxContainer.getChildren().addAll(targetNoLabel,targetNoCombobox);
		return targetNoComboboxContainer;
	}

	@SuppressWarnings("unchecked")
	private HBox supplementaryCombobox() {
		HBox supplementaryNoComboboxContainer = new HBox();
		LoggedLabel supplementaryNoLabel = UiFactory.createLabelWithStyle("supplementaryNoLabel", "Supplementary No", "display-label");
		supplementaryNoLabel.setPadding(new Insets(5,5,5,5));
		supplementaryNoLabel.setMaxWidth(130.0);
		supplementaryNoLabel.setPrefWidth(130.0);

		supplementaryNoCombobox = new LoggedComboBox<String>();
		supplementaryNoCombobox.getStyleClass().add("combo-box-base");		
		supplementaryNoCombobox.setPadding(new Insets(4, 4, 4, 4));
		supplementaryNoCombobox.setOnAction(getMbpnDialogController());
		supplementaryNoCombobox.setEditable(true);
		supplementaryNoComboboxContainer.getChildren().addAll(supplementaryNoLabel,supplementaryNoCombobox);
		return supplementaryNoComboboxContainer;
	}

	@SuppressWarnings("unchecked")
	private HBox typeNoCombobox() {
		HBox typeNoComboboxContainer = new HBox();
		LoggedLabel typeNoLabel = UiFactory.createLabelWithStyle("typeNoLabel", "Type No", "display-label");
		typeNoLabel.setPadding(new Insets(5,5,5,5));
		typeNoLabel.setMaxWidth(85.0);
		typeNoLabel.setPrefWidth(85.0);

		typeNoCombobox = new LoggedComboBox<String>();
		typeNoCombobox.getStyleClass().add("combo-box-base");		
		typeNoCombobox.setPadding(new Insets(4, 4, 4, 4));
		typeNoCombobox.setOnAction(getMbpnDialogController());
		typeNoCombobox.setEditable(true);
		typeNoComboboxContainer.getChildren().addAll(typeNoLabel,typeNoCombobox);
		return typeNoComboboxContainer;
	}

	@SuppressWarnings("unchecked")
	private HBox prototypeComboBox() {
		HBox protoTypeComboboxContainer = new HBox();
		LoggedLabel protoTypeCodeLabel = UiFactory.createLabelWithStyle("protoTypeCodeLabel", "Prototype Code", "display-label");
		protoTypeCodeLabel.setPadding(new Insets(5,5,5,5));
		protoTypeCodeLabel.setMaxWidth(130.0);
		protoTypeCodeLabel.setPrefWidth(130.0);

		protoTypeCombobox = new LoggedComboBox<String>();
		protoTypeCombobox.getStyleClass().add("combo-box-base");		
		protoTypeCombobox.setPadding(new Insets(4, 4, 4, 4));
		protoTypeCombobox.setOnAction(getMbpnDialogController());
		protoTypeCombobox.setEditable(true);
		protoTypeCombobox.setId("protoTypeCombobox");
		protoTypeComboboxContainer.getChildren().addAll(protoTypeCodeLabel,protoTypeCombobox);
		return protoTypeComboboxContainer;
	}

	@SuppressWarnings("unchecked")
	private HBox classNoCombobox() {
		HBox classNoContainer = new HBox();
		classNoCombobox = new LoggedComboBox<String>("classNoCombobox");
		classNoCombobox.getStyleClass().add("combo-box-base");		
		classNoCombobox.setPadding(new Insets(4, 4, 4, 4));
		classNoCombobox.setOnAction(getMbpnDialogController());
		classNoCombobox.setEditable(true);

		HBox labelBox = new HBox();
		LoggedLabel classNoLabel = UiFactory.createLabelWithStyle("classNoLabel", "Class No", "display-label");
		LoggedLabel asterisk = UiFactory.createLabelWithStyle("asterisk", "*","display-label");
		asterisk.setStyle("-fx-text-fill: red");
		labelBox.setPadding(new Insets(5,5,5,5));
		labelBox.setMaxWidth(85.0);
		labelBox.setPrefWidth(85.0);
		labelBox.getChildren().addAll(classNoLabel,asterisk);

		classNoContainer.getChildren().addAll(labelBox,classNoCombobox);
		return classNoContainer;
	}

	@SuppressWarnings("unchecked")
	private HBox mainNoCombobox() {
		HBox mainNoContainer = new HBox();

		mainNoCombobox = new LoggedComboBox<String>("mainNoCombobox");
		mainNoCombobox.getStyleClass().add("combo-box-base");		
		mainNoCombobox.setPadding(new Insets(4, 4, 4, 4));
		mainNoCombobox.setOnAction(getMbpnDialogController());
		mainNoCombobox.setEditable(true);

		HBox labelBox = new HBox();
		LoggedLabel mainNoLabel = UiFactory.createLabelWithStyle("mainNoLabel", "Main No", "display-label");
		LoggedLabel asterisk1 = UiFactory.createLabelWithStyle("asterisk1", "*","display-label");
		asterisk1.setStyle("-fx-text-fill: red");
		labelBox.setMaxWidth(130.0);
		labelBox.setPrefWidth(130.0);
		labelBox.setPadding(new Insets(5,5,5,5));
		labelBox.getChildren().addAll(mainNoLabel,asterisk1);

		mainNoContainer.getChildren().addAll(labelBox, mainNoCombobox);
		return mainNoContainer;
	}

	private HBox createButtonContainer() {
		HBox buttonContainer = new HBox();
		createBtn = createBtn(QiConstant.CREATE, mbpnDialogController);
		updateBtn = createBtn(QiConstant.UPDATE, mbpnDialogController);
		cancelBtn = createBtn(QiConstant.CANCEL, mbpnDialogController);

		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(10, 10, 10, 10));
		buttonContainer.setSpacing(13);
		buttonContainer.getChildren().addAll(createBtn, updateBtn,cancelBtn);
		return buttonContainer;
	}

	/**
	 * This method is used to create MigPane containing all comboboxes
	 */
	private MigPane createMainContainer(){
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");

		HBox firstComboBoxContainer = new HBox();
		firstComboBoxContainer.getChildren().addAll(mainNoCombobox(), classNoCombobox());
		firstComboBoxContainer.setSpacing(10);
		firstComboBoxContainer.setPadding(new Insets(5));

		HBox secondComboBoxContainer= new HBox();
		secondComboBoxContainer.getChildren().addAll(prototypeComboBox(), typeNoCombobox());
		secondComboBoxContainer.setSpacing(10);
		secondComboBoxContainer.setPadding(new Insets(5));

		HBox thirdComboBoxContainer= new HBox();
		thirdComboBoxContainer.getChildren().addAll(supplementaryCombobox(), targetNoCombobox());
		thirdComboBoxContainer.setSpacing(10);
		thirdComboBoxContainer.setPadding(new Insets(5));

		HBox fourthComboBoxContainer = new HBox();
		fourthComboBoxContainer.getChildren().addAll(hesColorCombobox(), createDescriptionContainer());
		fourthComboBoxContainer.setSpacing(10);
		fourthComboBoxContainer.setPadding(new Insets(5));

		pane.add(firstComboBoxContainer,"span,wrap");
		pane.add(secondComboBoxContainer,"span,wrap");
		pane.add(thirdComboBoxContainer,"span,wrap");
		pane.add(fourthComboBoxContainer,"span,wrap");
		pane.add(reasonForChange(),"span,wrap");
		pane.add(createButtonContainer(),"span,wrap");

		return pane;
	}

	@Subscribe
	public void processEvent(StatusMessageEvent event) {
		String applicationId = event.getApplicationId();
		if (applicationId == null || this.model.getApplicationContext().equals(applicationId)) {
			switch(event.getEventType()) {
			case DIALOG_ERROR:
				setErrorMessage(event.getMessage());
				break;
			case CLEAR:
				clearErrorMessage();
				break;
			default:
			}
		}
	}

	public void setErrorMessage(String message) {
		this.getStatusMessagePane().setErrorMessageArea(message);
	}

	public void clearErrorMessage() {
		this.getStatusMessagePane().setStatusMessage(null);
		this.getStatusMessagePane().clearErrorMessageArea();
	}

	public Pane initStatusMessagePane() {
		statusMessagePane = new StatusMessagePane(true);
		return statusMessagePane;
	}
	public MbpnDialogController getMbpnDialogController() {
		return mbpnDialogController;
	}

	public void setMbpnDialogController(MbpnDialogController mbpnDialogController) {
		this.mbpnDialogController = mbpnDialogController;
	}

	public LoggedComboBox<String> getMainNoCombobox() {
		return mainNoCombobox;
	}

	public void setMainNoCombobox(LoggedComboBox<String> mainNoCombobox) {
		this.mainNoCombobox = mainNoCombobox;
	}

	public LoggedComboBox<String> getClassNoCombobox() {
		return classNoCombobox;
	}

	public void setClassNoCombobox(LoggedComboBox<String> classNoCombobox) {
		this.classNoCombobox = classNoCombobox;
	}

	public LoggedComboBox<String> getProtoTypeCombobox() {
		return protoTypeCombobox;
	}

	public void setProtoTypeCombobox(LoggedComboBox<String> protoTypeCombobox) {
		this.protoTypeCombobox = protoTypeCombobox;
	}

	public LoggedComboBox<String> getTypeNoCombobox() {
		return typeNoCombobox;
	}

	public void setTypeNoCombobox(LoggedComboBox<String> typeNoCombobox) {
		this.typeNoCombobox = typeNoCombobox;
	}

	public LoggedComboBox<String> getSupplementaryNoCombobox() {
		return supplementaryNoCombobox;
	}

	public void setSupplementaryNoCombobox(LoggedComboBox<String> supplementaryNoCombobox) {
		this.supplementaryNoCombobox = supplementaryNoCombobox;
	}

	public LoggedComboBox<String> getTargetNoCombobox() {
		return targetNoCombobox;
	}

	public void setTargetNoCombobox(LoggedComboBox<String> targetNoCombobox) {
		this.targetNoCombobox = targetNoCombobox;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public LoggedComboBox<String> getHesColorCombobox() {
		return hesColorCombobox;
	}

	public void setHesColorCombobox(LoggedComboBox<String> hesColorCombobox) {
		this.hesColorCombobox = hesColorCombobox;
	}

	public LoggedButton getCreateBtn() {
		return createBtn;
	}

	public void setCreateBtn(LoggedButton createBtn) {
		this.createBtn = createBtn;
	}

	public LoggedButton getUpdateBtn() {
		return updateBtn;
	}

	public void setUpdateBtn(LoggedButton updateBtn) {
		this.updateBtn = updateBtn;
	}

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public void setCancelBtn(LoggedButton cancelBtn) {
		this.cancelBtn = cancelBtn;
	}

	public LoggedTextArea getReasonForChangeTextArea() {
		return reasonForChangeTextArea;
	}

	public void setReasonForChangeTextArea(LoggedTextArea reasonForChangeTextArea) {
		this.reasonForChangeTextArea = reasonForChangeTextArea;
	}

	public LoggedTextArea getDescriptionTextField() {
		return descriptionTextField;
	}

	public void setDescriptionTextField(LoggedTextArea descriptionTextField) {
		this.descriptionTextField = descriptionTextField;
	}

	public MbpnModel getModel() {
		return model;
	}

	public void setModel(MbpnModel model) {
		this.model = model;
	}

	public StatusMessagePane getStatusMessagePane() {
		return statusMessagePane;
	}

	public void setStatusMessagePane(StatusMessagePane statusMessagePane) {
		this.statusMessagePane = statusMessagePane;
	}

}

