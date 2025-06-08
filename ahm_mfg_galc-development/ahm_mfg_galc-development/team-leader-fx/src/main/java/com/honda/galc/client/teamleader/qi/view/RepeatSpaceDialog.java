package com.honda.galc.client.teamleader.qi.view;

import java.util.List;

import com.honda.galc.client.teamleader.qi.controller.RepeatSpaceDialogController;
import com.honda.galc.client.teamleader.qi.model.ParkingLocationMaintenanceModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiRepairAreaSpace;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
/**
 * 
 * <h3>RepeatSpaceDialog Class description</h3>
 * <p>
 * RepeatSpaceDialog is used to create multiple spaces
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
public class RepeatSpaceDialog extends QiFxDialog<ParkingLocationMaintenanceModel> {
	private UpperCaseFieldBean spaceTextField;
	private UpperCaseFieldBean repeatTextField;
	private UpperCaseFieldBean incrementTextField;
	private LoggedButton okBtn;
	private LoggedButton cancelBtn;
	private RepeatSpaceDialogController controller;
	public RepeatSpaceDialog(String title, String owner, ParkingLocationMaintenanceModel model,List<QiRepairAreaSpace> repairAreaSpacelist) {
		super(title, owner, model);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		controller = new RepeatSpaceDialogController(getModel(), this,repairAreaSpacelist);
		initComponents();
		controller.initListeners();
	}


	private void initComponents() {
		VBox outerPane = new VBox();
		outerPane.setPrefHeight(150);
		HBox repeatRow = new HBox();
		createRepeatRow(repeatRow);
		HBox btnContainer = new HBox();
		okBtn = createBtn(QiConstant.OK,getController());
		cancelBtn = createBtn(QiConstant.CANCEL,getController());
		btnContainer.getChildren().addAll(okBtn, cancelBtn);
		btnContainer.setAlignment(Pos.CENTER);
		btnContainer.setPadding(new Insets(0, 10, 10, 10));
		btnContainer.setSpacing(10);
		outerPane.getChildren().addAll(repeatRow,btnContainer);
		outerPane.setSpacing(20);
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
	}


	private HBox createRepeatRow(HBox repeatRow){
		HBox rowContainer = new HBox();
		HBox repeatContainer = new HBox();
		HBox incrementContainer = new HBox();
		LoggedLabel startingRowLabel = UiFactory.createLabel("startingRowLabel", "Starting Space");
		applyCssOnLabel(startingRowLabel);
		LoggedLabel rowAsterisk = UiFactory.createLabel("label", "*");
		rowAsterisk.setStyle("-fx-text-fill: red");
		rowContainer.getChildren().addAll(startingRowLabel,rowAsterisk);
		rowContainer.setAlignment(Pos.CENTER_LEFT);
		spaceTextField =  UiFactory.createUpperCaseFieldBean("rowTextField", 5, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);

		LoggedLabel repeatLabel = UiFactory.createLabel("repeatLabel", "# of times to repeat");
		applyCssOnLabel(repeatLabel);
		LoggedLabel repeatAsterisk = UiFactory.createLabel("label", "*");
		repeatAsterisk.setStyle("-fx-text-fill: red");
		repeatContainer.getChildren().addAll(repeatLabel,repeatAsterisk);
		repeatTextField =  UiFactory.createUpperCaseFieldBean("repeatTextField", 5, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);

		LoggedLabel incrementLabel = UiFactory.createLabel("incrementLabel", "Increment");
		applyCssOnLabel(incrementLabel);
		LoggedLabel incrementAsterisk = UiFactory.createLabel("label", "*");
		incrementAsterisk.setStyle("-fx-text-fill: red");
		incrementContainer.getChildren().addAll(incrementLabel,incrementAsterisk);
		incrementContainer.setAlignment(Pos.CENTER_LEFT);
		incrementTextField =  UiFactory.createUpperCaseFieldBean("incrementTextField", 5, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);

		repeatRow.getChildren().addAll(rowContainer,spaceTextField,repeatContainer,repeatTextField,incrementContainer,incrementTextField);
		repeatRow.setSpacing(10);
		repeatRow.setPadding(new Insets(20,10,0,20));
		repeatRow.setAlignment(Pos.CENTER_LEFT);
		return repeatRow;
	}


	/**
	 * This method is used to apply CSS on the Label
	 * @param label
	 */
	private void applyCssOnLabel(LoggedLabel label){
		label.getStyleClass().add("display-label");
	}
	public LoggedButton getOkBtn() {
		return okBtn;
	}

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public RepeatSpaceDialogController getController() {
		return controller;
	}

	public UpperCaseFieldBean getSpaceTextField() {
		return spaceTextField;
	}

	public UpperCaseFieldBean getRepeatTextField() {
		return repeatTextField;
	}

	public UpperCaseFieldBean getIncrementTextField() {
		return incrementTextField;
	}

}
