package com.honda.galc.client.teamleader.qi.view;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.controller.DunnageContentDialogController;
import com.honda.galc.client.teamleader.qi.model.DunnageMaintModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.UiFactory;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.StageStyle;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>DunnageContentDialog</code> is the class for Dunnage Content Dialog.
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
 * <TD>14/07/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
public class DunnageContentDialog extends QiFxDialog<DunnageMaintModel>{



	private LoggedButton okButton;
	private DunnageContentDialogController controller;
	private LoggedButton cancelButton;
	private String buttonClickedname;
	private String cssFontStyle;
	private LabeledUpperCaseTextField rowTextField;
	private LabeledUpperCaseTextField layerTextField;
	private LabeledUpperCaseTextField columnTextField;
	MigPane migPane;
	LoggedLabel messageLabel;


	public DunnageContentDialog(String title, String applicationId, DunnageMaintModel model) {
		super(title, applicationId,model);//Fix : Passing applicationId as parameter to fetch correct owner stage.
		this.initStyle(StageStyle.DECORATED);
		this.controller = new DunnageContentDialogController(model,this);
		initComponents();
		controller.initListeners();
	}

	public void initComponents(){
		double screenWidth=Screen.getPrimary().getVisualBounds().getWidth();
		double screenHeight=Screen.getPrimary().getVisualBounds().getHeight();
		messageLabel = UiFactory.createLabel("messageLabel");
		migPane = new MigPane(
				"center", 
				"", 
				"[][]");
		rowTextField = new LabeledUpperCaseTextField("Enter Row", " Enter Row",30, Fonts.SS_DIALOG_PLAIN(16), TextFieldState.EDIT, Pos.BASELINE_LEFT, false,null);
		rowTextField.getLabel().setPadding(new Insets(10));
		rowTextField.setStyle(cssFontStyle);

		columnTextField = new LabeledUpperCaseTextField("Enter Column", " Enter Column",30, Fonts.SS_DIALOG_PLAIN(16), TextFieldState.EDIT, Pos.BASELINE_LEFT, false,null);
		columnTextField.getLabel().setPadding(new Insets(10));
		columnTextField.setStyle(cssFontStyle);

		layerTextField = new LabeledUpperCaseTextField("Enter Layer", " Enter Layer",30, Fonts.SS_DIALOG_PLAIN(16), TextFieldState.EDIT, Pos.BASELINE_LEFT, false,null);
		layerTextField.getLabel().setPadding(new Insets(10));
		layerTextField.setStyle(cssFontStyle);

		okButton = createBtn("Ok", getController());
		okButton.setMinWidth(80);
		cancelButton = createBtn("Cancel", getController());
		cancelButton.setMinWidth(100);


		migPane.add(rowTextField,"wrap");
		migPane.add(columnTextField,"wrap ");
		migPane.add(layerTextField,"wrap");
		migPane.add(okButton ,"gaptop 20, center ,split2");
		migPane.add(cancelButton,"gapleft 30, gaptop 20, wrap");

		migPane.setPrefWidth(screenWidth/3.5);
		migPane.setPrefHeight(screenHeight/4);

		migPane.setPadding(new Insets(10));
		((BorderPane) this.getScene().getRoot()).setCenter(migPane);		
	}
	public String getRowText()
	{
		return StringUtils.trim(rowTextField.getText());
	}

	public String getColumnText()
	{
		return StringUtils.trim(columnTextField.getText());
	}

	public String getLayerText()
	{
		return StringUtils.trim(layerTextField.getText());
	}

	public void addMessage(String message, String styleClass){
		messageLabel.setText(message);
		messageLabel.setWrapText(true);
		messageLabel.setPrefWidth(400);
		messageLabel.setAlignment(Pos.CENTER);
		messageLabel.setStyle("-fx-text-fill: red");
		Tooltip tooltip = new Tooltip(message);
		tooltip.getStyleClass().add("display-label");
		messageLabel.setTooltip(tooltip);
		messageLabel.getStyleClass().add(styleClass);
		migPane.add(messageLabel);
	}

	public LoggedButton getCancelBtn() {
		return cancelButton;
	}

	public void setCancelBtn(LoggedButton cancelBtn) {
		this.cancelButton = cancelBtn;
	}

	public String getButtonClickedname() {
		return buttonClickedname;
	}

	public void setButtonClickedname(String buttonClickedname) {
		this.buttonClickedname = buttonClickedname;
	}

	public LabeledUpperCaseTextField getRowTextField() {
		return rowTextField;
	}

	public void setRowTextField(LabeledUpperCaseTextField rowTextField) {
		this.rowTextField = rowTextField;
	}

	public LabeledUpperCaseTextField getLayerTextField() {
		return layerTextField;
	}

	public void setLayerTextField(LabeledUpperCaseTextField layerTextField) {
		this.layerTextField = layerTextField;
	}

	public LabeledUpperCaseTextField getColumnTextField() {
		return columnTextField;
	}

	public void setColumnTextField(LabeledUpperCaseTextField columnTextField) {
		this.columnTextField = columnTextField;
	}
	public DunnageContentDialogController getController() {
		return controller;
	}

	public void setController(DunnageContentDialogController controller) {
		this.controller = controller;
	}

	public boolean showDunnageContentDialog()
	{
		showDialog();
		return getButtonClickedname().equalsIgnoreCase("OK");
	}
	
	public LoggedButton getOkButton() {
		return okButton;
	}
}

