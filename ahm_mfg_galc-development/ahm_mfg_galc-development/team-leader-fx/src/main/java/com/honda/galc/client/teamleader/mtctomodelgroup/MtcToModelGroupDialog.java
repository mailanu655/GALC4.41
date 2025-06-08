package com.honda.galc.client.teamleader.mtctomodelgroup;

import org.tbee.javafx.scene.layout.MigPane;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.StatusMessagePane;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.product.ModelGroup;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
/**
 * <h3>MtcToModelGroupDialog Class description</h3> <h4>Description</h4>
 * <p>
 * <code>MtcToModelGroupDialog</code> is the Dialog Panel class for Mtc to Model Group Assignment Screen.
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
 * <TD>31/03/2017</TD>

 * </TABLE>
 */

public class MtcToModelGroupDialog extends FxDialog {
	private double  screenWidth;
	private double  screenHeight;
	private boolean cancelled;
	private LoggedRadioButton activeRadioBtn;
	private LoggedRadioButton inactiveRadioBtn;
	private RadioButton primaryRadioBtn;
	private RadioButton secondaryRadioBtn;	 
	private LoggedLabel productTypeContextLabel;
	private LoggedLabel productType;
	private LoggedLabel modelName;
	private LoggedLabel modelDesc;
	private LoggedLabel modelSyst;
	private LoggedLabel reasonForChangeLabel;
	private UpperCaseFieldBean modelGroupName;
	private UpperCaseFieldBean modelGroupDesc;
	private UpperCaseFieldBean modelGroupSyst;
	private LoggedTextArea reasonForChangeText;
	private LoggedButton createBtn;
	private LoggedButton updateBtn;
	private LoggedButton cancelBtn;
	private String actionType;
	private MtcToModelGroupDialogController controller;
	private ModelGroup selectedModel;
	private MtcToModelGroupMaintenanceModel model;
	private String screenName;
	private StatusMessagePane statusMessagePane;
	protected final static String CREATE = "Create";
	protected final static String UPDATE = "Update";
	protected final static String ACTIVE = "Active";
	protected final static String INACTIVE = "Inactive";
	protected final static String CANCEL = "Cancel";
	protected final static String CSS_PATH = "/resource/css/QiMainCss.css";


	public MtcToModelGroupDialog(String title, ModelGroup selectedModel, MtcToModelGroupMaintenanceModel model,String applicationId) {
		super(title, applicationId);
		this.actionType = title;
		this.selectedModel = selectedModel;
		this.model=model;
		this.getScene().getStylesheets().add(CSS_PATH);
		this.controller = new MtcToModelGroupDialogController(model, this, selectedModel);
		this.screenWidth=Screen.getPrimary().getVisualBounds().getWidth();
		this.screenHeight=Screen.getPrimary().getVisualBounds().getHeight();
		EventBusUtil.register(this);
		((BorderPane) this.getScene().getRoot()).setBottom(initStatusMessagePane());
		initComponents();
		if (this.actionType.equals(CREATE))
			loadCreateData();
		else if (this.actionType.equals(UPDATE))
			loadUpdateData();
		controller.initListeners();
	}
	/**
	 * This method is used to initialize the components of panel
	 */
	private void initComponents() {
		MigPane pane = new MigPane("insets 0 0 0 0", "[left,grow][shrink 0]", "[]15[shrink 0]");
		pane.setPrefWidth(screenWidth/2.0);
		pane.setPrefHeight(screenHeight/2.6);
		pane.add(createStatusRadioButtons(getController()),"left, span, wrap");
		pane.add(getProductTypeLoggedLabel(),"split 2, span 2,left, gapleft 20");
		pane.add(getProductTypeLoggedLabelContext(),"left, span, wrap");
		pane.add(getModelGroupNameContainer(),"split 3, span 3,left");
		pane.add(getAsteriskLabel(UiFactory.createLabel("label", "*")),"split 3, span 3,left");
		pane.add(getModelGroupNameFieldContainer(),"left, span, wrap, gapleft 20");
		pane.add(getDescriptionLoggedLabel(),"split 2, span 2,left, gapleft 10");
		pane.add(getDescriptionUpperCaseFieldBean(),"left, span, wrap, gapleft 22");
		pane.add(getModelGroupSystemContainer(),"split 3, span 3,left");
		pane.add(getAsteriskLabel(UiFactory.createLabel("label", "*")),"split 3, span 3,left");
		pane.add(getModelGroupSystemFieldContainer(),"left, span, wrap, gapleft 20");
		pane.add(getReasonContainerLabel(),"split 3, span 3,left, gapleft 39");
		pane.add(getAsteriskLabel(UiFactory.createLabel("label", "*")),"split 3, span 3,left");
		pane.add(getReasonTextContainer(),"left, span, wrap , gapleft 17");
		this.createBtn = createBtn(CREATE, getController());
		this.createBtn.setPadding(new Insets(15, 15, 15, 15));
		this.updateBtn = createBtn(UPDATE, getController());
		this.updateBtn.setPadding(new Insets(15, 15, 15, 15));
		this.cancelBtn = createBtn(CANCEL, getController());
		this.cancelBtn.setPadding(new Insets(15, 15, 15, 15));
		pane.add(createBtn, "split 3, span 3, left, gapleft 120");
		pane.add(updateBtn, "split 3, span 3, left, gapleft 20");
		pane.add(cancelBtn,"gapleft 20");
		((BorderPane) this.getScene().getRoot()).setCenter(pane);
	}

	@Override
	public void showDialog() {
		this.cancelled = false;
		super.showDialog();
	}

	/**
	 * Cancels the dialog operation and closes the dialog.
	 */
	public void cancel() {
		this.cancelled = true;
		closeDialog();
	}

	/**
	 * Returns true iff this dialog's operation has been cancelled.
	 */
	public boolean isCancelled() {
		return this.cancelled;
	}

	/**
	 * Closes the Stage for this dialog.
	 */
	public void closeDialog() {
		Stage stage = (Stage) this.getScene().getWindow();
		stage.close();
	}

	public MtcToModelGroupDialogController getController() {
		return controller;
	}

	public void setController(MtcToModelGroupDialogController controller) {
		this.controller = controller;
	}

	/**
	 * this method is used to Get Product Type label
	 */
	private LoggedLabel getProductTypeLoggedLabel() {
		productType = UiFactory.createLabel("productType", "Product Type");
		productType.setPadding(new Insets(0, 0, 0, 60));
		productType.getStyleClass().add("display-label");
		return productType;
	}

	/**
	 * this method is used to Get Product Type label value
	 */
	private LoggedLabel getProductTypeLoggedLabelContext() {
		productTypeContextLabel = UiFactory.createLabel("siteNameContextLabel");
		productTypeContextLabel.setPadding(new Insets(0, 0, 0,15));
		return productTypeContextLabel;
	}

	/**
	 *this method is used to Get Model Group Name LoggedLabel 
	 */
	private LoggedLabel  getModelGroupNameContainer() {
		modelName = UiFactory.createLabel("modelGroupName", "Model Group");
		modelName.getStyleClass().add("display-label");
		modelName.setPadding(new Insets(0, 0, 0, 70));
		return modelName;
	}

	/**
	 *this method is used to Get Model Group Name
	 */
	private UpperCaseFieldBean  getModelGroupNameFieldContainer() {
		modelGroupName = (UpperCaseFieldBean) UiFactory.createTextField("modelGroupNameTxt",4,Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, true);
		modelGroupName.setPrefSize(320, 20);
		return modelGroupName;
	}

	/**
	 *this method is used to Get Entry model description label
	 */
	private LoggedLabel  getDescriptionLoggedLabel() {
		modelDesc = UiFactory.createLabel("modelDesc", "Model Group Description");
		modelDesc.getStyleClass().add("display-label");
		return modelDesc;
	}

	/**
	 *this method is used to Get Entry model description text field
	 */
	private UpperCaseFieldBean  getDescriptionUpperCaseFieldBean () {
		modelGroupDesc =(UpperCaseFieldBean) UiFactory.createTextField("modelGroupDesc",23,Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, true);
		modelGroupDesc.setPrefSize(320, 20);
		return modelGroupDesc;
	}

	/**
	 *this method is used to Get Model Group System LoggedLabel 
	 */
	private LoggedLabel  getModelGroupSystemContainer() {
		modelSyst = UiFactory.createLabel("modelSyst", "System");
		modelSyst.getStyleClass().add("display-label");
		modelSyst.setPadding(new Insets(0, 0, 0, 104));
		return modelSyst;
	}

	/**
	 *this method is used to Get Model Group System
	 */
	private UpperCaseFieldBean  getModelGroupSystemFieldContainer() {
		modelGroupSyst = (UpperCaseFieldBean) UiFactory.createTextField("modelGroupSyst",4,Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT, true);
		modelGroupSyst.setPrefSize(320, 20);
		return modelGroupSyst;
	}

	/**
	 *this method is used to Get reason for change label
	 */
	private LoggedLabel getReasonContainerLabel() {
		reasonForChangeLabel = UiFactory.createLabel("reasonForChangeLabel", "Reason For Change");
		reasonForChangeLabel.getStyleClass().add("display-label");
		return reasonForChangeLabel;
	}

	/**
	 *this method is used Get reason for change text area
	 */
	private LoggedTextArea  getReasonTextContainer() {
		reasonForChangeText = UiFactory.createTextArea();
		reasonForChangeText.setPrefSize(320,50);
		reasonForChangeText.setWrapText(true);
		return reasonForChangeText;
	}

	/**
	 *this method is used Get asterisk
	 */
	private LoggedLabel getAsteriskLabel(LoggedLabel loggedLabel) {
		loggedLabel = UiFactory.createLabel("label", "*");
		loggedLabel.setStyle("-fx-text-fill: red");
		return loggedLabel;
	}
	/**
	 * This method is used to load Update data.
	 */
	private void loadUpdateData() {
		createBtn.setDisable(true);
		getActiveRadioBtn().setSelected(selectedModel.isActive());
		getInactiveRadioBtn().setSelected(!selectedModel.isActive());
		productTypeContextLabel.setText(selectedModel.getProductType());
		modelGroupName.setText(selectedModel.getId().getModelGroup());
		modelGroupDesc.setText(selectedModel.getModelGroupDescription());
		modelGroupSyst.setText(selectedModel.getSystem());
		getUpdateBtn().setDisable(true);
	}

	/**
	 * This method is used to load Create data.
	 */
	private void loadCreateData() {
		productTypeContextLabel.setText(selectedModel.getProductType());
		modelGroupSyst.setText(selectedModel.getSystem());
		selectedModel = new ModelGroup();
		updateBtn.setDisable(true);
		reasonForChangeText.setDisable(true);
	}
	
	/**
	 * Test if the dialog opened for update
	 */
	public boolean isUpdate() {
		return UPDATE.equals(actionType);
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

	/**
	 * This method is used to Create HBox which consists of active/inactive radio buttons
	 * @param handler
	 * @return radioBtnContainer
	 */
	public HBox createStatusRadioButtons(EventHandler<ActionEvent> handler) {
		HBox radioBtnContainer = new HBox();
		ToggleGroup group = new ToggleGroup();
		activeRadioBtn = createRadioButton(ACTIVE, group, true, handler);
		inactiveRadioBtn = createRadioButton(INACTIVE, group, false, handler);

		radioBtnContainer.getChildren().addAll(activeRadioBtn, inactiveRadioBtn);
		radioBtnContainer.setAlignment(Pos.CENTER_LEFT);
		radioBtnContainer.setSpacing(10);
		radioBtnContainer.setPadding(new Insets(10, 0, 0, 10));
		return radioBtnContainer;
	}

	/**
	 * This method is used to create Radio Button.
	 * @param title
	 * @param group
	 * @param isSelected
	 * @return
	 */
	public LoggedRadioButton createRadioButton(String title, ToggleGroup group, boolean isSelected, EventHandler<ActionEvent> handler) {
		LoggedRadioButton radioButton = new LoggedRadioButton(title);
		radioButton.getStyleClass().add("radio-btn");
		radioButton.setToggleGroup(group);
		radioButton.setSelected(isSelected);
		radioButton.setOnAction(handler);
		return radioButton;
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

	public LoggedRadioButton getActiveRadioBtn() {
		return activeRadioBtn;
	}

	public void setActiveRadioBtn(LoggedRadioButton activeRadioBtn) {
		this.activeRadioBtn = activeRadioBtn;
	}

	public LoggedRadioButton getInactiveRadioBtn() {
		return inactiveRadioBtn;
	}

	public void setInactiveRadioBtn(LoggedRadioButton inactiveRadioBtn) {
		this.inactiveRadioBtn = inactiveRadioBtn;
	}

	public RadioButton getPrimaryRadioBtn() {
		return primaryRadioBtn;
	}

	public void setPrimaryRadioBtn(RadioButton primaryRadioBtn) {
		this.primaryRadioBtn = primaryRadioBtn;
	}

	public RadioButton getSecondaryRadioBtn() {
		return secondaryRadioBtn;
	}

	public void setSecondaryRadioBtn(RadioButton secondaryRadioBtn) {
		this.secondaryRadioBtn = secondaryRadioBtn;
	}

	public StatusMessagePane getStatusMessagePane() {
		return statusMessagePane;
	}

	public void setStatusMessagePane(StatusMessagePane statusMessagePane) {
		this.statusMessagePane = statusMessagePane;
	}

	public LoggedTextArea getReasonForChangeText() {
		return reasonForChangeText;
	}

	public LoggedLabel getProductTypeContextLabel() {
		return productTypeContextLabel;
	}

	public UpperCaseFieldBean getModelGroupName() {
		return modelGroupName;
	}

	public UpperCaseFieldBean getModelGroupDesc() {
		return modelGroupDesc;
	}

	public UpperCaseFieldBean getModelGroupSystem() {
		return modelGroupSyst;
	}

	public LoggedButton getCreateBtn() {
		return createBtn;
	}

	public LoggedButton getUpdateBtn() {
		return updateBtn;
	}
	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}


}
