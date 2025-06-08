package com.honda.galc.client.teamleader.qi.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.honda.galc.client.teamleader.qi.controller.IqsCategoryDialogController;
import com.honda.galc.client.teamleader.qi.model.IqsMaintenanceModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiIqsCategory;

/**
 * 
 * <h3>IqsCategoryDialog Class description</h3>
 * <p>
 * IqsCategoryDialog is used to display Category dialog popup screen 
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
 * @author L&TInfotech<br>
 *        Sep 06, 2016
 * 
 */

public class IqsCategoryDialog extends QiFxDialog<IqsMaintenanceModel> {

	private String title;
	private IqsCategoryDialogController controller;
	private IqsMaintenanceModel model;
	private QiIqsCategory iqsCategory;

	private LoggedButton createBtn;
	private LoggedButton cancelBtn;

	private UpperCaseFieldBean iqsCategoryTextField;
	private LoggedTextArea iqsReasonTextArea;

	private VBox outerPane = new VBox();
	private LoggedLabel msgLabel;

	public IqsCategoryDialog(String title, QiIqsCategory iqsCategory, IqsMaintenanceModel model,String applicationId) {
		super(title, applicationId,model);//Fix : Passing applicationId as parameter to fetch correct owner stage.
		this.title = title;
		this.controller = new IqsCategoryDialogController(model, this, iqsCategory);
		this.iqsCategory = iqsCategory;
		initComponents();
		controller.initListeners();
	}

	private void initComponents(){
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		HBox iqsCategoryFieldContainer = new HBox();
		iqsCategoryFieldContainer.setSpacing(20);
		iqsCategoryFieldContainer.setPadding(new Insets(10));
		iqsCategoryFieldContainer.setAlignment(Pos.CENTER_LEFT);

		HBox iqsReasonContainer = new HBox();
		iqsReasonContainer.setSpacing(20);
		iqsReasonContainer.setPadding(new Insets(10));
		iqsReasonContainer.setAlignment(Pos.CENTER_LEFT);

		HBox iqsButtonContainer = new HBox();
		iqsButtonContainer.setSpacing(20);
		iqsButtonContainer.setPadding(new Insets(10));
		iqsButtonContainer.setAlignment(Pos.CENTER);

		HBox labelBox = new HBox();
		HBox categorylabelBox = new HBox();

		createLabel(labelBox, categorylabelBox);

		createInnerComponents(iqsCategoryFieldContainer, iqsReasonContainer,
				iqsButtonContainer, labelBox, categorylabelBox);
		HBox msgBox = createErrorLabel();

		outerPane.getChildren().addAll(iqsCategoryFieldContainer,iqsReasonContainer,iqsButtonContainer,msgBox);
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
	}
	
	/**
	 * This method is to create error label
	 * @return
	 */
	private HBox createErrorLabel() {
		HBox msgBox= new HBox();;
		msgBox.setPadding(new Insets(10, 10, 10, 10));
		msgLabel = UiFactory.createLabel("messageLabel");
		msgLabel.setWrapText(true);
		msgLabel.setPrefWidth(400);
		msgLabel.setAlignment(Pos.CENTER);
		msgBox.getChildren().add(msgLabel);
		msgBox.setAlignment(Pos.CENTER);
		return msgBox;
	}
	/**
	 * This method is used to create components and add it to container
	 * @param iqsCategoryFieldContainer
	 * @param iqsReasonContainer
	 * @param iqsButtonContainer
	 * @param labelBox
	 * @param categorylabelBox
	 */
	private void createInnerComponents(HBox iqsCategoryFieldContainer,
			HBox iqsReasonContainer, HBox iqsButtonContainer, HBox labelBox,
			HBox categorylabelBox) {
		createBtn = createBtn(QiConstant.CREATE, getController());
		updateButton = createBtn(QiConstant.UPDATE,getController());
		cancelBtn = createBtn(QiConstant.CANCEL,getController());

		if(title.equalsIgnoreCase(QiConstant.CREATE)){
			updateButton.setDisable(true);
			iqsReasonTextArea.setDisable(true);
		}

		if(title.equalsIgnoreCase(QiConstant.UPDATE)){
			iqsCategoryTextField.settext(iqsCategory.getIqsCategory());
			createBtn.setDisable(true);
		}

		iqsCategoryFieldContainer.getChildren().addAll(categorylabelBox,iqsCategoryTextField);
		iqsReasonContainer.getChildren().addAll(labelBox,iqsReasonTextArea);
		iqsButtonContainer.getChildren().addAll(createBtn,updateButton,cancelBtn);
	}
	/**
	 * This method is used to create label
	 * @param labelBox
	 * @param categorylabelBox
	 */
	private void createLabel(HBox labelBox, HBox categorylabelBox) {
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("iqsReasonLabel", "Reason for Change");
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		asterisk.setStyle("-fx-text-fill: red");
		labelBox.getChildren().addAll(reasonForChangeLabel,asterisk);

		LoggedLabel iqsCategoryLabel = UiFactory.createLabel("iqsCategoryLabel", "IQS Category");
		iqsCategoryLabel.setPadding(new Insets(0,0,0,34));
		LoggedLabel asterisk1 = UiFactory.createLabel("label", "*");
		asterisk1.setStyle("-fx-text-fill: red");
		categorylabelBox.getChildren().addAll(iqsCategoryLabel,asterisk1);
		iqsCategoryTextField = UiFactory.createUpperCaseFieldBean("iqsCategoryTextField", 17, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		iqsReasonTextArea = UiFactory.createTextArea();
		iqsReasonTextArea.setPrefRowCount(3);
		iqsReasonTextArea.setPrefColumnCount(17);
	}

	public IqsCategoryDialogController getController() {
		return controller;
	}

	public void setController(IqsCategoryDialogController controller) {
		this.controller = controller;
	}

	public IqsMaintenanceModel getModel() {
		return model;
	}

	public void setModel(IqsMaintenanceModel model) {
		this.model = model;
	}

	public QiIqsCategory getIqsCategory() {
		return iqsCategory;
	}

	public void setIqsCategory(QiIqsCategory iqsCategory) {
		this.iqsCategory = iqsCategory;
	}

	public LoggedButton getCreateBtn() {
		return createBtn;
	}

	public void setCreateBtn(LoggedButton createBtn) {
		this.createBtn = createBtn;
	}

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public void setCancelBtn(LoggedButton cancelBtn) {
		this.cancelBtn = cancelBtn;
	}

	public UpperCaseFieldBean getIqsCategoryTextField() {
		return iqsCategoryTextField;
	}

	public void setIqsCategoryTextField(UpperCaseFieldBean iqsCategoryTextField) {
		this.iqsCategoryTextField = iqsCategoryTextField;
	}

	public LoggedTextArea getIqsReasonTextArea() {
		return iqsReasonTextArea;
	}

	public void setIqsReasonTextArea(LoggedTextArea iqsReasonTextArea) {
		this.iqsReasonTextArea = iqsReasonTextArea;
	}

	public VBox getOuterPane() {
		return outerPane;
	}

	public void setOuterPane(VBox outerPane) {
		this.outerPane = outerPane;
	}

	public LoggedLabel getMsgLabel() {
		return msgLabel;
	}

	public void setMsgLabel(LoggedLabel msgLabel) {
		this.msgLabel = msgLabel;
	}

}
