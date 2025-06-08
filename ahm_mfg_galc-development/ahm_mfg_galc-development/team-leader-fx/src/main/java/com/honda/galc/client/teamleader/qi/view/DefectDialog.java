package com.honda.galc.client.teamleader.qi.view;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.controller.DefectDialogController;
import com.honda.galc.client.teamleader.qi.model.ItemMaintenanceModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiDefect;
import com.honda.galc.entity.qi.QiDefectCategory;

/**
 * 
 * <h3>DefectDialog Class description</h3>
 * <p> DefectDialog description </p>
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
 * July 26, 2016
 *
 *
 */

public class DefectDialog extends QiFxDialog<ItemMaintenanceModel> {

	private String title;
	private DefectDialogController controller;
	private QiDefect qiDefect; 


	private LoggedButton createBtn;
	private LoggedButton cancelBtn;

	private UpperCaseFieldBean defectNameTextField;
	private UpperCaseFieldBean defectAbbrTextField;

	private UpperCaseFieldBean defectDescTextField;
	private LoggedComboBox<QiDefectCategory> defectCategoryComboBox;

	private LoggedTextArea reasonForChangeTextArea;
	private volatile boolean isCancel = false;

	public DefectDialog(String title, QiDefect qiDefect, ItemMaintenanceModel model,String applicationId) {
		super(title, applicationId,model);//Fix : Passing applicationId as parameter to fetch correct owner stage.
		this.title = title;
		this.controller = new DefectDialogController(model,this,qiDefect);
		this.qiDefect = qiDefect;
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		initComponents();
		if(this.title.equalsIgnoreCase(QiConstant.CREATE))
			loadCreateData();
		else if(this.title.equalsIgnoreCase(QiConstant.UPDATE))
			loadUpdateData();
		controller.initListeners();
	}

	private void initComponents(){

		VBox outerPane = new VBox();
		HBox defectNameContainer = new HBox();
		HBox defectNameAbbrContainer = new HBox();
		HBox defectCategoryDescContainer = new HBox();
		HBox setDesiredPositionContainer = new HBox();
		HBox reasonForChangeContainer = new HBox();
		HBox changeAsterikContainer = new HBox();
		HBox positionContainer = new HBox();
		HBox buttonContainer = new HBox();
		HBox radioButtonContainer = createStatusRadioButtons(getController());


		LoggedLabel defectNameLabel = UiFactory.createLabel("defectNameLabel", "Defect Name");
		defectNameLabel.getStyleClass().add("display-label");
		LoggedLabel asteriskdefect = UiFactory.createLabel("label", "*");
		asteriskdefect.setStyle("-fx-text-fill: red");

		LoggedLabel defectAbbrLabel = UiFactory.createLabel("defectAbbrLabel", "Defect Abbr");
		defectAbbrLabel.getStyleClass().add("display-label");
		defectNameTextField =  UiFactory.createUpperCaseFieldBean("defectNameTextField", 38, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		defectAbbrTextField =  UiFactory.createUpperCaseFieldBean("defectAbbrTextField", 9, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		defectNameContainer.getChildren().addAll(defectNameLabel,asteriskdefect);
		defectNameContainer.setAlignment(Pos.CENTER_LEFT);

		defectNameAbbrContainer.getChildren().addAll(defectNameContainer,defectNameTextField,defectAbbrLabel,defectAbbrTextField);
		defectNameAbbrContainer.setSpacing(21);
		defectNameAbbrContainer.setPadding(new Insets(5,5,5,25));
		defectNameAbbrContainer.setAlignment(Pos.CENTER_LEFT);

		LoggedLabel defectCategoryLabel = UiFactory.createLabel("defectCategoryLabel", "Defect Category");
		defectCategoryLabel.getStyleClass().add("display-label");
		LoggedLabel defectDescLabel = UiFactory.createLabel("defectDescLabel", "Defect Description");
		defectDescLabel.getStyleClass().add("display-label");

		defectCategoryComboBox = new LoggedComboBox<QiDefectCategory>("defectCategoryComboBox");
		defectCategoryComboBox.getStyleClass().add("combo-box-base");
		defectDescTextField =  UiFactory.createUpperCaseFieldBean("defectDescTextField", 30, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		defectCategoryDescContainer.getChildren().addAll(defectCategoryLabel,defectCategoryComboBox,defectDescLabel,defectDescTextField);
		defectCategoryDescContainer.setSpacing(20);
		defectCategoryDescContainer.setPadding(new Insets(0,0,0,9));
		defectCategoryDescContainer.setAlignment(Pos.CENTER_LEFT);

		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("iqsReasonLabel", "Reason for"+"\n"+"   Change");
		reasonForChangeLabel.getStyleClass().add("display-label");
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		asterisk.setStyle("-fx-text-fill: red");

		changeAsterikContainer.getChildren().addAll(reasonForChangeLabel,asterisk);
		positionContainer.getChildren().addAll(createTitiledPane("Set Desired Position", createPositionPanel()));

		reasonForChangeTextArea = UiFactory.createTextArea();
		reasonForChangeTextArea.setPrefRowCount(2);
		reasonForChangeTextArea.setPrefColumnCount(25);
		reasonForChangeTextArea.setWrapText(true);
		reasonForChangeContainer.getChildren().addAll(changeAsterikContainer,reasonForChangeTextArea);
		reasonForChangeContainer.setSpacing(20);
		reasonForChangeContainer.setPadding(new Insets(20,20,20,30));
		reasonForChangeContainer.setAlignment(Pos.CENTER_LEFT);

		setDesiredPositionContainer.getChildren().addAll(reasonForChangeContainer,positionContainer);
		setDesiredPositionContainer.setSpacing(58);
		setDesiredPositionContainer.setPadding(new Insets(8));

		createBtn = createBtn(QiConstant.CREATE, getController());
		createBtn.setPadding(new Insets(5,5,5,5));

		updateButton = createBtn(QiConstant.UPDATE,getController());
		cancelBtn = createBtn(QiConstant.CANCEL,getController());

		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(5,5,5,5));
		buttonContainer.setSpacing(20);
		buttonContainer.getChildren().addAll(createBtn, updateButton, cancelBtn);

		outerPane.setSpacing(20);
		outerPane.getChildren().addAll(radioButtonContainer,defectNameAbbrContainer,defectCategoryDescContainer ,setDesiredPositionContainer,buttonContainer);
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);

	}


	/**
	 * This method is used to create Position Panel
	 */
	private MigPane createPositionPanel(){
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");

		HBox positionradioBtnContainer = createPositionRadioButtons(getController());
		pane.add(positionradioBtnContainer,"span,wrap");
		return pane;
	}

	/**
	 * This method is used to create TitledPane for Part panel.
	 * @param title
	 * @param content
	 * @return
	 */
	private TitledPane createTitiledPane(String title,Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		titledPane.setPrefSize(295, 100);
		return titledPane;
	}
	/**
	 * This method is used to load Create Defect Layout.
	 */
	private void loadCreateData()
	{
		try {
			qiDefect = new QiDefect();
			updateButton.setDisable(true);
			reasonForChangeTextArea.setDisable(true);
			List<QiDefectCategory> defectCategoryList=getModel().populateDefectCategory();
			defectCategoryComboBox.getItems().addAll(defectCategoryList);
			defectCategoryComboBox.getSelectionModel().select(1);
		} catch (Exception e) {
			controller.handleException("An error occured in loading Create Defect pop up screen ", "Failed to Open Create Defect popup screen", e);
		}
	}
	/**
	 * This method is used to load Update data.
	 */
	private void loadUpdateData()
	{
		try {
			defectNameTextField.settext(qiDefect.getDefectTypeName());
			defectAbbrTextField.settext(qiDefect.getDefectTypeDescriptionShort());
			defectDescTextField.settext(qiDefect.getDefectTypeDescriptionLong());
			List<QiDefectCategory> defectCategoryList=getModel().populateDefectCategory();
			defectCategoryComboBox.getItems().addAll(defectCategoryList);
			defectCategoryComboBox.setValue(qiDefect.getDefectCategoryName());
			getActiveRadioBtn().setSelected(qiDefect.isActive());
			getPrimaryRadioBtn().setSelected(qiDefect.isPrimaryPostion());
			getInactiveRadioBtn().setSelected(!qiDefect.isActive());
			getSecondaryRadioBtn().setSelected(!qiDefect.isPrimaryPostion());
			createBtn.setDisable(true);
		} catch (Exception e) {
			controller.handleException("An error occured in loading Update Defect pop up screen ", "Failed to Open Update Defect popup screen", e);
		}
	}

	public DefectDialogController getController() {
		return controller;
	}

	public void setController(DefectDialogController controller) {
		this.controller = controller;
	}

	public QiDefect getQiDefect() {
		return qiDefect;
	}

	public void setQiDefect(QiDefect qiDefect) {
		this.qiDefect = qiDefect;
	}

	public LoggedButton getCreateBtn() {
		return createBtn;
	}

	public void setCreateBtn(LoggedButton createBtn) {
		this.createBtn = createBtn;
	}

	public LoggedButton getUpdateBtn() {
		return updateButton;
	}

	public void setUpdateBtn(LoggedButton updateBtn) {
		this.updateButton = updateBtn;
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

	public UpperCaseFieldBean getDefectNameTextField() {
		return defectNameTextField;
	}

	public void setDefectNameTextField(UpperCaseFieldBean defectNameTextField) {
		this.defectNameTextField = defectNameTextField;
	}

	public UpperCaseFieldBean getDefectAbbrTextField() {
		return defectAbbrTextField;
	}

	public void setDefectAbbrTextField(UpperCaseFieldBean defectAbbrTextField) {
		this.defectAbbrTextField = defectAbbrTextField;
	}

	public UpperCaseFieldBean getDefectDescTextField() {
		return defectDescTextField;
	}

	public void setDefectDescTextField(UpperCaseFieldBean defectDescTextField) {
		this.defectDescTextField = defectDescTextField;
	}

	public LoggedComboBox<QiDefectCategory> getDefectCategoryComboBox() {
		return defectCategoryComboBox;
	}

	public void setDefectCategoryComboBox(LoggedComboBox<QiDefectCategory> defectCategoryComboBox) {
		this.defectCategoryComboBox = defectCategoryComboBox;
	}

	public boolean isCancel() {
		return isCancel;
	}

	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}
}
