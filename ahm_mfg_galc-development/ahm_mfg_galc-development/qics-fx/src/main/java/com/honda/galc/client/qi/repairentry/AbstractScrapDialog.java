/**
 * 
 */
package com.honda.galc.client.qi.repairentry;

import java.util.HashSet;
import java.util.Set;

import com.honda.galc.client.qi.base.QiFxDialog;
import com.honda.galc.client.teamleader.fx.dataRecovery.Utils;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiRepairResultDto;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * @author vf031824
 *
 */
public abstract class AbstractScrapDialog extends QiFxDialog<RepairEntryModel> {
	protected AbstractScrapDialogController dialogController;
	protected LoggedTextArea scrapReasonTextArea;
	private LoggedButton scrapButton;
	private LoggedButton cancelButton;
	private LoggedLabel messageLabel;
	private boolean isUnitScraped;
	protected Set<Integer> selectedList = new HashSet<Integer>();
	protected QiRepairResultDto defectToScrap;
	protected LoggedLabel defectToScrapLabel;


	public QiRepairResultDto getDefectToScrap() {
		return defectToScrap;
	}


	public LoggedLabel getDefectToScrapLabel() {
		return defectToScrapLabel;
	}


	public void setDefectToScrapLabel(LoggedLabel defectToScrapLabel) {
		this.defectToScrapLabel = defectToScrapLabel;
	}


	public void setDefectToScrap(QiRepairResultDto defectToScrap) {
		this.defectToScrap = defectToScrap;
	}


	/**
	 * @param title
	 * @param applicationId
	 * @param model
	 */
	public AbstractScrapDialog(String title, String applicationId, RepairEntryModel model) {
		super(title, applicationId, model);
	}


	public AbstractScrapDialogController getDialogController() {
		return this.dialogController;
	}


	public void setDialogController(AbstractScrapDialogController dialogController) {
		this.dialogController = dialogController;
	}
	
	public LoggedTextArea getScrapReasonTextArea() {
		return scrapReasonTextArea;
	}

	public void setScrapReasonTextArea(LoggedTextArea scrapReasonTextArea) {
		this.scrapReasonTextArea = scrapReasonTextArea;
	}


	private BorderPane getRootBorderPane() {
		return (BorderPane) getScene().getRoot();
	}


	protected void initComponents() {
		VBox mainDialog = new VBox();
		mainDialog.setPrefHeight(400);
		mainDialog.setPrefWidth(750);
		mainDialog.getChildren().addAll(createLoggedLabel(), createCommentArea(), createMsgLabel());
		getRootBorderPane().setCenter(mainDialog);
	}


	/**
	 * This method will be used to create comment text area.
	 * 
	 * @return
	 */
	private HBox createCommentArea() {
		scrapReasonTextArea = UiFactory.createTextArea();
		scrapReasonTextArea.setWrapText(true);
		scrapReasonTextArea.setPrefRowCount(10);
		scrapReasonTextArea.setPrefColumnCount(20);
		LoggedLabel scrapReasonLabel = UiFactory.createLabel("scrapReasonLabel", "Scrap Reason", Fonts.SS_DIALOG_PLAIN(16), TextAlignment.CENTER);
		scrapReasonLabel.setPadding(new Insets(25, 0, 0, 0));
	
		HBox commentAreaBox = new HBox();
		commentAreaBox.setPadding(new Insets(10,0,0,10));
		commentAreaBox.setSpacing(20);
		commentAreaBox.getChildren().addAll(scrapReasonLabel, scrapReasonTextArea, createButtonContainer());
		return commentAreaBox;
	}


	/**
	 * This method is used to create a Button container and all the components
	 * inside it.
	 * 
	 * @return
	 */
	private HBox createButtonContainer() {
		HBox buttonContainer = new HBox();
		scrapButton = createBtn("Scrap", getDialogController());
		cancelButton = createBtn(QiConstant.CANCEL, getDialogController());
		buttonContainer.setAlignment(Pos.CENTER_RIGHT);
		buttonContainer.setSpacing(20);
		buttonContainer.getChildren().addAll(scrapButton, cancelButton);
		return buttonContainer;
	}
	
	/**
	 * This method is to create Selected Defect label
	 * 
	 * @return
	 */
	protected Node createLoggedLabel() {		
		HBox tableContainer = new HBox();
		tableContainer.setPadding(new Insets(20,10,30,10));		
		defectToScrapLabel = UiFactory.createLabel("");
		defectToScrapLabel.setFont(Utils.getMainLabelFont());
		defectToScrapLabel.setWrapText(true);
		defectToScrapLabel.setAlignment(Pos.CENTER);		
		tableContainer.getChildren().add(defectToScrapLabel);
		tableContainer.setAlignment(Pos.CENTER);
		return tableContainer;
	}


	/**
	 * This method is to create error label
	 * 
	 * @return
	 */
	private HBox createMsgLabel() {
		HBox messageBox = new HBox();
		messageBox.setPadding(new Insets(5));
		messageLabel = UiFactory.createLabel("messageLabel");
		messageLabel.setWrapText(true);
		messageLabel.setPrefWidth(400);
		messageLabel.setAlignment(Pos.CENTER);
		messageBox.getChildren().add(messageLabel);
		messageBox.setAlignment(Pos.CENTER);
		return messageBox;
	}


	public LoggedButton getScrapBtn() {
		return scrapButton;
	}


	public void setScrapBtn(LoggedButton scrapBtn) {
		this.scrapButton = scrapBtn;
	}


	public LoggedButton getCancelBtn() {
		return cancelButton;
	}


	public void setCancelBtn(LoggedButton cancelBtn) {
		this.cancelButton = cancelBtn;
	}


	public LoggedLabel getMsgLabel() {
		return messageLabel;
	}


	public void setMsgLabel(LoggedLabel msgLabel) {
		this.messageLabel = msgLabel;
	}

	public boolean isUnitScraped() {
		return isUnitScraped;
	}


	public void setUnitScraped(boolean isUnitScraped) {
		this.isUnitScraped = isUnitScraped;
	}
	
	public void reload() {			
		getDefectToScrapLabel().setText(defectToScrap.getDefectDesc().toString());
	}

}
