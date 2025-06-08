package com.honda.galc.client.teamleader.qi.view;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.controller.DunnageDialogController;
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
 * <code>DunnageDialog</code> is the class for Dunnage creation pop up screen.
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
public class DunnageDialog extends QiFxDialog<DunnageMaintModel>{

	private LoggedButton okButton;
	private DunnageDialogController controller;
	public DunnageDialogController getController() {
		return controller;
	}

	public void setController(DunnageDialogController controller) {
		this.controller = controller;
	}

	public void setButtonClickedname(String buttonClickedname) {
		this.buttonClickedname = buttonClickedname;
	}
	private LoggedButton cancelButton;
	private String buttonClickedname;
	private LabeledUpperCaseTextField quantityTextField;
	private String cssFontStyle; 
	private LabeledUpperCaseTextField productSpecCodeTextField;
	MigPane migPane;
	LoggedLabel messageLabel;


	public DunnageDialog(String title, String applicationId, DunnageMaintModel model) {
		super(title, applicationId,model);//Fix : Passing applicationId as parameter to fetch correct owner stage.
		this.initStyle(StageStyle.DECORATED);
		this.controller = new DunnageDialogController(model,this);
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
				"[][][]");
		quantityTextField = new LabeledUpperCaseTextField("Enter Expected Quantity", " Expected Quantity",30, Fonts.SS_DIALOG_PLAIN(16), TextFieldState.EDIT, Pos.BASELINE_LEFT, true,null);
		quantityTextField.getLabel().setPadding(new Insets(10));
		quantityTextField.setStyle(cssFontStyle);

		productSpecCodeTextField = new LabeledUpperCaseTextField("Enter Product Spec Code", " Product Spec Code",30, Fonts.SS_DIALOG_PLAIN(16), TextFieldState.EDIT, Pos.BASELINE_LEFT, true,null);
		productSpecCodeTextField.getLabel().setPadding(new Insets(10));
		productSpecCodeTextField.setStyle(cssFontStyle);

		okButton = createBtn("Ok", getController());
		okButton.setMinWidth(80);
		cancelButton = createBtn("Cancel", getController());
		cancelButton.setMinWidth(100);

		migPane.add(quantityTextField,"wrap");
		migPane.add(productSpecCodeTextField,"wrap");
		migPane.add(okButton, "gaptop 20, center ,split2");
		migPane.add(cancelButton,"gapleft 30, gaptop 20, wrap");
		migPane.setPrefWidth(screenWidth/3.5);
		migPane.setPrefHeight(screenHeight/4);

		migPane.setPadding(new Insets(10));
		((BorderPane) this.getScene().getRoot()).setCenter(migPane);		
	}

	public String getDunnageQuantityText()
	{
		return StringUtils.trim(quantityTextField.getText());
	}

	public String getProductspecCodeText()
	{
		return StringUtils.trim(productSpecCodeTextField.getText());
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


	public String getButtonClickedname() {
		return StringUtils.trimToEmpty(buttonClickedname);
	}
	public LoggedButton getCancelBtn() {
		return cancelButton;
	}
	public void setCancelBtn(LoggedButton cancelBtn) {
		this.cancelButton = cancelBtn;
	}

	public LabeledUpperCaseTextField getQuantityTextField()
	{
		return quantityTextField;
	}
	public void setQuantityTextField(LabeledUpperCaseTextField quantityTextField) {
		this.quantityTextField = quantityTextField;
	}
	public LabeledUpperCaseTextField getProductSpecCodeTextField() {
		return productSpecCodeTextField;
	}
	public void setProductSpecCodeTextField(LabeledUpperCaseTextField productSpecCodeTextField) {
		this.productSpecCodeTextField = productSpecCodeTextField;
	}

	public LoggedButton getOkBtn() {
		return okButton;
	}
	public void setOkBtn(LoggedButton okBtn) {
		this.okButton = okBtn;
	}

	public boolean showDunnageDialog()
	{
		showDialog();
		return getButtonClickedname().equalsIgnoreCase("OK");
	}

}
