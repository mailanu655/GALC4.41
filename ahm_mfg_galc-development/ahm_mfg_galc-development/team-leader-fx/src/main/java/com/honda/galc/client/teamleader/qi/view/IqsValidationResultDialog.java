package com.honda.galc.client.teamleader.qi.view;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.honda.galc.client.teamleader.qi.controller.IqsValidationResultDialogController;
import com.honda.galc.client.teamleader.qi.model.IqsMaintenanceModel;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiIqsValidationResultDto;

/**
 * 
 * <h3>IqsValidationResultDialog Class description</h3>
 * <p> IqsValidationResultDialog description </p>
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
 * Sept 26, 2016
 *
 *
 */

public class IqsValidationResultDialog extends QiFxDialog<IqsMaintenanceModel> {

	private IqsValidationResultDialogController controller;
	private List<QiIqsValidationResultDto> dtoList;
	private ObjectTablePane<QiIqsValidationResultDto> iqsValidationResultTablePane;
	private LoggedLabel validNumbers;
	private LoggedLabel invalidNumbers;
	private LoggedLabel duplicateNumbers;
	private LoggedButton createBtn;
	private LoggedButton cancelBtn;
	private UpperCaseFieldBean fileNameText;
	

	public IqsValidationResultDialog(String title, List<QiIqsValidationResultDto> dtoList, Integer validCount, 
			Integer duplicateCount, String fileName, IqsMaintenanceModel model,String applicationId) {
		super(title, applicationId,model);//Fix : Passing applicationId as parameter to fetch correct owner stage.
		this.dtoList= dtoList;
		this.controller = new IqsValidationResultDialogController(model, this, dtoList);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		initComponents();
		loadCreateData(validCount,duplicateCount,fileName);
		controller.initListeners();
	}

	private void initComponents(){
		VBox outerPane = new VBox();
		HBox totalResultLabelContainer = createTotalResultPane();
		HBox fileNameContainer = createFileNamePane();
		HBox validationResultTableContainer = createValidationResultPane();
		HBox buttonContainer = createButtonPane();
		HBox noteContainer = createNotePane();
		
		ScrollPane mainPane = new ScrollPane();
		outerPane.setSpacing(20);
		outerPane.getChildren().addAll(totalResultLabelContainer,fileNameContainer,validationResultTableContainer,buttonContainer,noteContainer);
		mainPane.setContent(outerPane);
		((BorderPane) this.getScene().getRoot()).setCenter(mainPane);
	}
	/**
	 * This method is used to create a Button HBox and all components inside it.
	 * @return
	 */
	private HBox createButtonPane() {
		HBox buttonContainer = new HBox();
		createBtn = createBtn(QiConstant.IMPORT, getController());
		cancelBtn = createBtn(QiConstant.CANCEL,getController());
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(5,5,5,5));
		buttonContainer.setSpacing(20);
		buttonContainer.getChildren().addAll(createBtn, cancelBtn);
		return buttonContainer;
	}
	/**
	 * This method is used to create a Note HBox and all components inside it.
	 * @return
	 */
	private HBox createNotePane() {
		HBox noteContainer = new HBox();
		LoggedLabel noteLabel = UiFactory.createLabelWithStyle("noteLabel","Note: All rows which has comments as 'VALID' will get imported.","display-label");
		noteContainer.setPadding(new Insets(5,5,5,5));
		noteContainer.getChildren().addAll(noteLabel);
		return noteContainer;
	}
	/**
	 * This method is used to create a validationResult HBox and all components inside it.
	 * @return
	 */
	private HBox createValidationResultPane() {
		HBox validationResultTableContainer = new HBox();
		iqsValidationResultTablePane = createValidationResultTablePane();
		createSerialNumberColumn();
		validationResultTableContainer.getChildren().addAll(iqsValidationResultTablePane);
		return validationResultTableContainer;
	}
	/**
	 * This method is used to create a FileName HBox and all components inside it.
	 * @return
	 */
	private HBox createFileNamePane() {
		HBox fileNameContainer = new HBox();
		LoggedLabel fileNameLabel = UiFactory.createLabelWithStyle("fileNameLabel", "File Name","display-label");
		fileNameContainer.setPadding(new Insets(5,5,5,5));
		fileNameContainer.setSpacing(10);
		fileNameText = UiFactory.createUpperCaseFieldBean("fileNameText", 25, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.DISABLED, Pos.BASELINE_LEFT);
		fileNameContainer.getChildren().addAll(fileNameLabel,fileNameText);
		return fileNameContainer;
	}
	/**
	 * This method is used to create a Total Result HBox and all components inside it.
	 * @return
	 */
	private HBox createTotalResultPane() {
		validNumbers = UiFactory.createLabel("validNumbers");
		invalidNumbers = UiFactory.createLabel("invalidNumbers");
		duplicateNumbers = UiFactory.createLabel("duplicateNumbers");
		HBox totalResultLabelContainer = new HBox();
		totalResultLabelContainer.setPadding(new Insets(5,5,5,5));
		totalResultLabelContainer.setSpacing(5);
		LoggedLabel validNumbersLabel = UiFactory.createLabelWithStyle("validNumbers", "Valid :","display-label");
		LoggedLabel invalidNumbersLabel = UiFactory.createLabelWithStyle("invalidNumbers", ", Invalid :","display-label");
		LoggedLabel duplicateNumbersLabel = UiFactory.createLabelWithStyle("duplicateNumbers", ", Duplicate :","display-label");
		totalResultLabelContainer.getChildren().addAll(validNumbersLabel,validNumbers,
				invalidNumbersLabel,invalidNumbers,duplicateNumbersLabel,duplicateNumbers);
		return totalResultLabelContainer;
	}
	
	/**
	 * This method is used to create serial number column
	 */
	private void createSerialNumberColumn() {
		LoggedTableColumn<QiIqsValidationResultDto, Integer> column = new LoggedTableColumn<QiIqsValidationResultDto, Integer>();

		createSerialNumber(column);
		iqsValidationResultTablePane.getTable().getColumns().add(0, column);
		iqsValidationResultTablePane.getTable().getColumns().get(0).setText("#");
		iqsValidationResultTablePane.getTable().getColumns().get(0).setResizable(true);
		iqsValidationResultTablePane.getTable().getColumns().get(0).setMaxWidth(100);
		iqsValidationResultTablePane.getTable().getColumns().get(0).setMinWidth(1);
	}

	/**
	 * This method is used to create a Table Pane.
	 * @return
	 */
	private ObjectTablePane<QiIqsValidationResultDto> createValidationResultTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("IQS Version", "iqsVersion")
				.put("IQS Category", "iqsCategory")
				.put("IQS Question #", "iqsQuestionNo")
				.put("IQS Question","iqsQuestion")
				.put("Comments","comments");

		Double[] columnWidth = new Double[] {0.10,0.14,0.10,0.40,0.10};
		ObjectTablePane<QiIqsValidationResultDto> panel = new ObjectTablePane<QiIqsValidationResultDto>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		return panel;
	}


	/**
	 * This method is used to load Create validation result Layout.
	 */
	private void loadCreateData(Integer validCount, Integer duplicateCount, String fileName)
	{
		try {
			iqsValidationResultTablePane.setData(dtoList);
			Integer invalidCount = (dtoList.size()-(validCount+duplicateCount));// Invalid count comes when we subtract valid and duplicate count from total
			validNumbers.setText(validCount.toString());
			duplicateNumbers.setText(duplicateCount.toString());
			invalidNumbers.setText(invalidCount.toString());
			fileNameText.setText(fileName);
		} catch (Exception e) {
			controller.handleException("An error occured in loading Create validation result pop up screen ", "Failed to Open Create validation result popup screen", e);
		}
	}

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public void setCancelBtn(LoggedButton cancelBtn) {
		this.cancelBtn = cancelBtn;
	}

	public ObjectTablePane<QiIqsValidationResultDto> getIqsValidationResultTablePane() {
		return iqsValidationResultTablePane;
	}

	public void setIqsValidationResultTablePane(
			ObjectTablePane<QiIqsValidationResultDto> iqsValidationResultTablePane) {
		this.iqsValidationResultTablePane = iqsValidationResultTablePane;
	}

	public LoggedLabel getValidNumbers() {
		return validNumbers;
	}

	public void setValidNumbers(LoggedLabel validNumbers) {
		this.validNumbers = validNumbers;
	}

	public LoggedLabel getInvalidNumbers() {
		return invalidNumbers;
	}

	public void setInvalidNumbers(LoggedLabel invalidNumbers) {
		this.invalidNumbers = invalidNumbers;
	}

	public LoggedLabel getDuplicateNumbers() {
		return duplicateNumbers;
	}

	public void setDuplicateNumbers(LoggedLabel duplicateNumbers) {
		this.duplicateNumbers = duplicateNumbers;
	}

	public LoggedButton getCreateBtn() {
		return createBtn;
	}

	public void setCreateBtn(LoggedButton createBtn) {
		this.createBtn = createBtn;
	}

	public IqsValidationResultDialogController getController() {
		return controller;
	}

	public void setController(IqsValidationResultDialogController controller) {
		this.controller = controller;
	}

	public UpperCaseFieldBean getFileNameText() {
		return fileNameText;
	}

	public void setFileNameText(UpperCaseFieldBean fileNameText) {
		this.fileNameText = fileNameText;
	}

}
