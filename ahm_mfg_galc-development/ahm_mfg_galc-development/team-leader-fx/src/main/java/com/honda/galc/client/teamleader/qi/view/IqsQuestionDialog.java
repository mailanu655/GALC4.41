package com.honda.galc.client.teamleader.qi.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.honda.galc.client.teamleader.qi.controller.IqsQuestionDialogController;
import com.honda.galc.client.teamleader.qi.model.IqsMaintenanceModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiIqsQuestion;

/**
 * 
 * <h3>IqsQuestionDialog Class description</h3>
 * <p>
 * IqsQuestionDialog is used to display version dialog popup screen 
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

public class IqsQuestionDialog extends QiFxDialog<IqsMaintenanceModel> {

	private String title;
	private IqsQuestionDialogController controller;
	private IqsMaintenanceModel model;
	private QiIqsQuestion iqsQuestion;

	private LoggedButton createBtn;
	private LoggedButton cancelBtn;

	private VBox outerPane = new VBox();

	private UpperCaseFieldBean iqsQuestionNoTextField;
	private LoggedTextArea iqsQuestionTextArea;
	private LoggedTextArea iqsReasonTextArea;
	private LoggedLabel msgLabel;


	public IqsQuestionDialog(String title, QiIqsQuestion iqsQuestion, IqsMaintenanceModel model,String applicationId) {
		super(title, applicationId,model);//Fix : Passing applicationId as parameter to fetch correct owner stage.
		this.title = title;
		this.controller = new IqsQuestionDialogController(model,this,iqsQuestion);
		this.iqsQuestion = iqsQuestion;
		initComponents();
		controller.initListeners();
	}

	private void initComponents(){
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		HBox iqsQuestionNoFieldContainer = new HBox();
		iqsQuestionNoFieldContainer.setSpacing(20);
		iqsQuestionNoFieldContainer.setPadding(new Insets(10));
		iqsQuestionNoFieldContainer.setAlignment(Pos.CENTER_LEFT);

		HBox iqsQuestionFieldContainer = new HBox();
		iqsQuestionFieldContainer.setSpacing(20);
		iqsQuestionFieldContainer.setPadding(new Insets(10));
		iqsQuestionFieldContainer.setAlignment(Pos.CENTER_LEFT);

		HBox iqsReasonContainer = new HBox();
		iqsReasonContainer.setSpacing(20);
		iqsReasonContainer.setPadding(new Insets(10));
		iqsReasonContainer.setAlignment(Pos.CENTER_LEFT);

		HBox iqsButtonContainer = new HBox();
		iqsButtonContainer.setSpacing(20);
		iqsButtonContainer.setPadding(new Insets(10));
		iqsButtonContainer.setAlignment(Pos.CENTER);

		HBox quesNolabelBox = new HBox();
		HBox queslabelBox = new HBox();
		HBox labelBox = new HBox();

		createLabels(quesNolabelBox, queslabelBox, labelBox);
		createInnerComponent();

		iqsQuestionNoFieldContainer.getChildren().addAll(quesNolabelBox,iqsQuestionNoTextField);
		iqsQuestionFieldContainer.getChildren().addAll(queslabelBox,iqsQuestionTextArea);
		iqsReasonContainer.getChildren().addAll(labelBox,iqsReasonTextArea);
		iqsButtonContainer.getChildren().addAll(createBtn,updateButton,cancelBtn);

		HBox msgBox = createErrorLabel();

		outerPane.getChildren().addAll(iqsQuestionNoFieldContainer,iqsQuestionFieldContainer,iqsReasonContainer,iqsButtonContainer,msgBox);
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
	}
	/**
	 * This method is to create error label
	 * @return
	 */
	private HBox createErrorLabel() {
		HBox msgBox= new HBox();
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
	 * This method creates labels on popup screen
	 * @param quesNolabelBox
	 * @param queslabelBox
	 * @param labelBox
	 */
	private void createLabels(HBox quesNolabelBox, HBox queslabelBox,
			HBox labelBox) {
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("iqsReasonLabel", "Reason for Change");
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		asterisk.setStyle("-fx-text-fill: red");
		labelBox.getChildren().addAll(reasonForChangeLabel,asterisk);

		LoggedLabel iqsQuestionNoLabel = UiFactory.createLabel("iqsQuestionNoLabel", "IQS Question #");
		iqsQuestionNoLabel.setPadding(new Insets(0,0,0,15));
		LoggedLabel asterisk1 = UiFactory.createLabel("label", "*");
		asterisk1.setStyle("-fx-text-fill: red");
		quesNolabelBox.getChildren().addAll(iqsQuestionNoLabel,asterisk1);
		iqsQuestionNoTextField = UiFactory.createUpperCaseFieldBean("iqsQuestionNoTextField", 18, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);

		LoggedLabel iqsQuestionLabel = UiFactory.createLabel("iqsQuestionLabel", "IQS Question");
		iqsQuestionLabel.setPadding(new Insets(0,0,0,27));
		LoggedLabel asterisk2 = UiFactory.createLabel("label", "*");
		asterisk2.setStyle("-fx-text-fill: red");
		queslabelBox.getChildren().addAll(iqsQuestionLabel,asterisk2);
	}
	/**
	 * This method creates inner component
	 */
	private void createInnerComponent() {
		iqsQuestionTextArea = UiFactory.createTextArea();
		iqsQuestionTextArea.setPrefRowCount(3);
		iqsQuestionTextArea.setPrefColumnCount(17);
		iqsQuestionTextArea.setWrapText(true);

		iqsReasonTextArea = UiFactory.createTextArea();
		iqsReasonTextArea.setPrefRowCount(3);
		iqsReasonTextArea.setPrefColumnCount(17);
		iqsReasonTextArea.setWrapText(true);

		createBtn = createBtn(QiConstant.CREATE, getController());
		updateButton = createBtn(QiConstant.UPDATE,getController());
		cancelBtn = createBtn(QiConstant.CANCEL,getController());

		if(title.equalsIgnoreCase(QiConstant.CREATE)){
			updateButton.setDisable(true);
			iqsReasonTextArea.setDisable(true);
		}

		if(title.equalsIgnoreCase(QiConstant.UPDATE)){
			iqsQuestionNoTextField.setText(Integer.toString(iqsQuestion.getId().getIqsQuestionNo()));
			iqsQuestionTextArea.setText(iqsQuestion.getId().getIqsQuestion());
			createBtn.setDisable(true);
		}
	}

	public IqsQuestionDialogController getController() {
		return controller;
	}

	public void setController(IqsQuestionDialogController controller) {
		this.controller = controller;
	}

	public IqsMaintenanceModel getModel() {
		return model;
	}

	public void setModel(IqsMaintenanceModel model) {
		this.model = model;
	}

	public QiIqsQuestion getIqsQuestion() {
		return iqsQuestion;
	}

	public void setIqsQuestion(QiIqsQuestion iqsQuestion) {
		this.iqsQuestion = iqsQuestion;
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

	public UpperCaseFieldBean getIqsQuestionNoTextField() {
		return iqsQuestionNoTextField;
	}

	public void setIqsQuestionNoTextField(UpperCaseFieldBean iqsQuestionNoTextField) {
		this.iqsQuestionNoTextField = iqsQuestionNoTextField;
	}

	public VBox getOuterPane() {
		return outerPane;
	}

	public void setOuterPane(VBox outerPane) {
		this.outerPane = outerPane;
	}

	public LoggedTextArea getIqsQuestionTextArea() {
		return iqsQuestionTextArea;
	}

	public void setIqsQuestionTextArea(LoggedTextArea iqsQuestionTextArea) {
		this.iqsQuestionTextArea = iqsQuestionTextArea;
	}

	public LoggedTextArea getIqsReasonTextArea() {
		return iqsReasonTextArea;
	}

	public void setIqsReasonTextArea(LoggedTextArea iqsReasonTextArea) {
		this.iqsReasonTextArea = iqsReasonTextArea;
	}

	public LoggedLabel getMsgLabel() {
		return msgLabel;
	}

	public void setMsgLabel(LoggedLabel msgLabel) {
		this.msgLabel = msgLabel;
	}

}
