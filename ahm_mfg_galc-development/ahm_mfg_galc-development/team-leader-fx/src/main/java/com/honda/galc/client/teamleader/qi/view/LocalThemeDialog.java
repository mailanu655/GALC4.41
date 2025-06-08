package com.honda.galc.client.teamleader.qi.view;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.controller.LocalThemeDialogController;
import com.honda.galc.client.teamleader.qi.model.LocalThemeModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiLocalTheme;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
/**
 * 
 * <h3>LocalThemeDialog Class description</h3>
 * <p>
 * LocalThemeDialog is used to create the components on the PopUp screen etc
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
public class LocalThemeDialog extends QiFxDialog<LocalThemeModel> {

	private String title;

	private LocalThemeDialogController controller;
	private LocalThemeModel model;
	private QiLocalTheme oldLocalTheme;

	private LoggedButton createButton;
	private LoggedButton updateButton;
	private LoggedButton cancelButton;
	private UpperCaseFieldBean LocalThemeTextField;
	private LoggedTextArea localThemeDescTextArea;
	private LoggedTextArea reasonForChangeTextArea;

	public LocalThemeDialog(String title, QiLocalTheme oldLocalTheme, LocalThemeModel model,String applicationId) {
		super(title,applicationId,model);//Fix : Passing applicationId as parameter to fetch correct owner stage.
		this.title = title;
		this.controller = new LocalThemeDialogController(model, this, oldLocalTheme);
		this.oldLocalTheme = oldLocalTheme;
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		initComponents();
		if(this.title.equalsIgnoreCase(QiConstant.CREATE)){
			loadCreateData();
		}else if(this.title.equalsIgnoreCase(QiConstant.UPDATE)){
			loadUpdateData();
		}
		controller.initListeners();
	}


	private void initComponents(){
		
		MigPane pane = new MigPane("insets 5 5 5 5 ", "[center,grow,fill]", "");

		pane.add(createTextInputContainer(),"span,wrap");
		
		pane.setPrefSize(Screen.getPrimary().getVisualBounds().getWidth()/2.5, 350 );

		((BorderPane) this.getScene().getRoot()).setCenter(pane);

	}


	/**
	 * This method is used to disable Update button , TextArea and set '0' value in hierarchy value field on the Popup screen 
	 */
	private void loadCreateData(){
		updateButton.setDisable(true);
		reasonForChangeTextArea.setDisable(true);
	}

	/**
	 * This method is used to load the data of Part on the Pop screen
	 */
	private void loadUpdateData(){
		createButton.setDisable(true);
		LocalThemeTextField.setText(oldLocalTheme.getLocalTheme());
		localThemeDescTextArea.setText(oldLocalTheme.getLocalThemeDescription());
		boolean isActive = oldLocalTheme.isActive();
		getActiveRadioBtn().setSelected(isActive);
		getInactiveRadioBtn().setSelected(!isActive);
	}


	/** This method will create text input component container.
	 * 
	 * @return grid pane
	 */
	private Node createTextInputContainer(){
		
		MigPane pane = new MigPane("insets 10 10 10 10", "[left]", "[]10[]");
		pane.add(createStatusRadioButtons(getController()),"span,wrap");
		pane.add(createLoggedLabel("localThemeLabel", "Local Theme", "display-label-14"),"split 7 ,span 3 ");
		pane.add(createLoggedLabel("astrikLocalTheme", "*", "display-label-14", "-fx-text-fill: red"),"left");
		LocalThemeTextField =  UiFactory.createUpperCaseFieldBean("localThemeTextField", 18, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		pane.add(LocalThemeTextField ,"gapleft 45 ,span,wrap ");
		pane.add(createLoggedLabel("localThemeDescLabel", "Description", "display-label-14"),"left");
		localThemeDescTextArea = UiFactory.createTextArea();
		localThemeDescTextArea.setPrefRowCount(2);
		localThemeDescTextArea.setPrefColumnCount(23);
		localThemeDescTextArea.setWrapText(true);
		pane.add(localThemeDescTextArea ,"gapleft 10 ,span,wrap");
		pane.add(createLoggedLabel("reasonForChangeLabel", "Reason for Change", "display-label-14"));
		pane.add(createLoggedLabel("astrikLocalTheme", "*", "display-label-14", "-fx-text-fill: red"));
		reasonForChangeTextArea = UiFactory.createTextArea();
		reasonForChangeTextArea.setPrefRowCount(2);
		reasonForChangeTextArea.setPrefColumnCount(23);
		reasonForChangeTextArea.setWrapText(true);
		pane.add(reasonForChangeTextArea ,"span,wrap,left");
		createButton = createBtn(QiConstant.CREATE, getController());
		createButton.getStyleClass().add("popup-btn"); 
		updateButton = createBtn(QiConstant.UPDATE, getController());
		updateButton.getStyleClass().add("popup-btn");
		cancelButton =  createBtn(QiConstant.CANCEL, getController());
		cancelButton.getStyleClass().add("popup-btn");
		pane.add(createButton," split 5 ,span 3 ,center");
		pane.add(updateButton);
		pane.add(cancelButton);
		return pane;
	}

	/**
	 * This method is used to create LoggedLabel
	 * 
	 * @param id
	 * @param text
	 * @param cssClass
	 * @return LoggedLabel
	 */
	private LoggedLabel createLoggedLabel(String id, String text,String cssClass) {
		LoggedLabel label=UiFactory.createLabel(id, text);
		label.getStyleClass().add(cssClass);
		return label;
	}

	/**
	 * This method is used to create LoggedLabel
	 * 
	 * @param id
	 * @param text
	 * @param cssClass
	 * @param cssStyle
	 * @return LoggedLabel
	 */
	private LoggedLabel createLoggedLabel(String id, String text,String cssClass,String cssStyle) {
		LoggedLabel label=UiFactory.createLabel(id, text);
		label.getStyleClass().add(cssClass);
		label.setStyle(cssStyle);
		return label;
	}
	
	public LocalThemeDialogController getController() {
		return controller;
	}

	public LocalThemeModel getModel() {
		return model;
	}

	public QiLocalTheme getPreviousInspectionPart() {
		return oldLocalTheme;
	}

	public LoggedButton getCreateButton() {
		return createButton;
	}

	public LoggedButton getUpdateButton() {
		return updateButton;
	}

	public LoggedButton getCancelButton() {
		return cancelButton;
	}

	public UpperCaseFieldBean getLocalThemeTextField() {
		return LocalThemeTextField;
	}

	public LoggedTextArea getLocalThemeDescTextArea() {
		return localThemeDescTextArea;
	}

	public LoggedTextArea getReasonForChangeTextArea() {
		return reasonForChangeTextArea;
	}
}
