package com.honda.galc.client.teamleader.qi.defectResult;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.view.QiFxDialog;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.PdcRegionalAttributeMaintDto;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiRepairResult;

/**
 * 
 * <h3>UpdatePdcDialog Class description</h3>
 * <p>
 * UpdatePdcDialog is used to change part defect combination attribute values in the Defect/Repair Result table.
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

public class UpdatePdcDialog extends QiFxDialog<DefectResultMaintModel> {

	private LabeledComboBox<String> entryScreenComboBox;
	private LoggedLabel filterNameLabel;
	private UpperCaseFieldBean resultsFilterTxt;
	private CheckBox updateAttribute;
	private LoggedButton changeBtn;
	private LoggedButton cancelBtn;
	private UpdatePdcController controller;
	private ObjectTablePane<PdcRegionalAttributeMaintDto> localAttributeMaintTablePanel;
	private List<String> selectedPartDefectDescList;
	private LoggedLabel entryModelTextField;
	private LoggedLabel entryModelTextFieldValue;
	private String entryModelText;
	private LoggedLabel entryPlantLabel;
	private LoggedLabel entryPlantValueLabel;
	
	public UpdatePdcDialog(String title, DefectResultMaintModel model, String applicationId, String productType, List<String> selectedPartDefectDescList,String typeOfChange,List<QiDefectResult> selectedDefectResultList,List<QiRepairResult> selectedRepairResultList,String entryModelText) {
		super(title, applicationId, model);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.selectedPartDefectDescList=selectedPartDefectDescList;
		this.controller = new UpdatePdcController(model, this, productType,typeOfChange,selectedDefectResultList,selectedRepairResultList);
		this.entryModelText = entryModelText;
		initComponents();
		loadEntryModelValues(productType);
		controller.initListeners();
		loadEntryScreenComboBox(this.entryModelText);
	}
	
	private void loadEntryScreenComboBox(String entryModel) {
		
		if(this.entryModelText!=null) {
			getEntryScreenComboBox().getItems().clear();
			getEntryScreenComboBox().setPromptText("Select");
			getEntryScreenComboBox().getItems().addAll(getModel().findAllByEntryModel(entryModel));
		}
		
	}

	/**
	 * This method is used to initialze the components and put those into Migpane
	 */
	private void initComponents() {
		double screenResolutionWidth=Screen.getPrimary().getVisualBounds().getWidth();
		double screenResolutionHeight=Screen.getPrimary().getVisualBounds().getHeight();
		MigPane migPane = new MigPane();
		migPane.setPrefWidth(screenResolutionWidth/1.3);
		migPane.setPrefHeight(screenResolutionHeight/1.3);
		migPane.add(filterComboBoxContainerPanel(),"wrap");
		migPane.add(createDefectTablePane(),"wrap");
		migPane.add(createButtonContainer());
		((BorderPane) this.getScene().getRoot()).setCenter(migPane);
	}
	
	/**
	 * This method is used to load the EntryModel based on ProductType
	 * @param productType
	 */
	private void loadEntryModelValues(String productType) {
		getEntryScreenComboBox().getItems().clear();
	}
	
	/**
	 * This method is used to create the Combobox related to EntryModel , EntryScreen etc.
	 * @return
	 */
	private MigPane filterComboBoxContainerPanel(){
		MigPane pane = new MigPane("","10[200]5[]50[]5[]50[]50[right]10","10[]5[]" );	
		entryModelTextField = UiFactory.createLabel("Entry Model", "Entry Model:");
		entryModelTextField.getStyleClass().add("display-label");
		entryModelTextField.setPadding(new Insets(0,0,0,0));
		entryModelTextField.setId("Entry Model");
		entryModelTextFieldValue = UiFactory.createLabel("Entry Model", getEntryModelText());
		entryModelTextFieldValue.setPadding(new Insets(0,0,0,-120));
		
		entryPlantLabel = UiFactory.createLabel("Entry Plant", "Entry Plant:");
		entryPlantLabel.getStyleClass().add("display-label");
		entryPlantLabel.setPadding(new Insets(0, 5, 0, 5));
		entryPlantValueLabel = UiFactory.createLabel("Entry Plant Value", getController().getModel().getPlant());
		entryPlantValueLabel.setPadding(new Insets(0, 5, 0, 5));
		
		entryScreenComboBox = new LabeledComboBox<String>("Entry Screen", true, new Insets(10,10,10,10), true, true);
		entryScreenComboBox.getLabel().setPadding(new Insets(10,10,5,30));
		entryScreenComboBox.setId("Entry Screen");
		entryScreenComboBox.getControl().getStyleClass().add("combo-box-base");
		entryScreenComboBox.getControl().setMinHeight(35.0);

		filterNameLabel = UiFactory.createLabel("filter", "Filter");
		filterNameLabel.getStyleClass().add("display-label");
		filterNameLabel.setPadding(new Insets(10,20,5,70));

		resultsFilterTxt = createFilterTextField("Filter",20, getController());
		updateAttribute = createCheckBox("Update Attributes", getController());
		updateAttribute.setId("radio-btn");
		updateAttribute.getStyleClass().add("display-label");
		updateAttribute.setPadding(new Insets(5,20,10,10));
		updateAttribute.setSelected(true);

		pane.add(entryModelTextField);
		pane.add(entryModelTextFieldValue);
		pane.add(entryPlantLabel);
		pane.add(entryPlantValueLabel);
		pane.add(entryScreenComboBox);
		pane.add(filterNameLabel);
		pane.add(resultsFilterTxt,"wrap");
		pane.add(updateAttribute);
		return pane;
	}
	
	/**
	 * This method is used to create the buttons like Change, Cancel
	 * @return
	 */
	private MigPane createButtonContainer(){
		MigPane pane = new MigPane("","410[]25[]","15[]");
		changeBtn = createBtn(QiConstant.CHANGE, getController());
		changeBtn.setPadding(new Insets(5, 5, 5, 5));
		changeBtn.setDisable(true);
		cancelBtn = createBtn(QiConstant.CANCEL, getController());
		cancelBtn.setPadding(new Insets(5, 5, 5, 5));
		pane.add(changeBtn);
		pane.add(cancelBtn);
		pane.setPadding(new Insets(5,10,5,250));
		return pane;
	}

	/**
	 * This method is used to create the TableView
	 * @return
	 */
	private ObjectTablePane<PdcRegionalAttributeMaintDto> createDefectTablePane() { 
		ColumnMappingList columnMappingList = ColumnMappingList.with("QICS Full Part Name", "fullPartName")
				.put("Primary Defect","defectTypeName")
				.put("Secondary Defect", "defectTypeName2")
				.put("Rpt/Non-Rpt", "reportableDefect")
				.put("Responsibility","responsibility")
				.put("Local Theme","localTheme")
				.put("Theme", "themeName")
				.put("IQS Version", "iqsVersion")
				.put("IQS Category","iqsCategory")
				.put("IQS Question", "iqsQuestion")
				.put("PDDA Info", "pddaInfo")
				.put("Is Used", "IsUsedVersionData")
				;

		Double[] columnWidth = new Double[] {
				0.18,0.08,0.08,0.06,0.13,0.06,0.07,0.06,0.06,0.08,0.10,0.05
		};
		
		localAttributeMaintTablePanel = new ObjectTablePane<PdcRegionalAttributeMaintDto>(columnMappingList,columnWidth);
		LoggedTableColumn<PdcRegionalAttributeMaintDto, Integer> column = new LoggedTableColumn<PdcRegionalAttributeMaintDto, Integer>();
		createSerialNumber(column);
		localAttributeMaintTablePanel.getTable().getColumns().add(0, column);
		localAttributeMaintTablePanel.getTable().getColumns().get(0).setText("#");
		localAttributeMaintTablePanel.getTable().getColumns().get(0).setResizable(true);
		localAttributeMaintTablePanel.getTable().getColumns().get(0).setMaxWidth(40);
		localAttributeMaintTablePanel.getTable().getColumns().get(0).setMinWidth(1);
		return localAttributeMaintTablePanel;
	}

	/**
	 * This method is used to load the data into TableView
	 * @param entryScreen
	 * @param filterValue
	 */
	public void reload(String entryScreen, String entryModel, String filterValue) {
		List<PdcRegionalAttributeMaintDto> defectList=getModel().findAllPdcLocalAttributes(entryScreen, entryModel, (short)1, filterValue);
		List<PdcRegionalAttributeMaintDto> defectList2 =new ArrayList<PdcRegionalAttributeMaintDto>();
		for (PdcRegionalAttributeMaintDto pdcRegionalAttributeMaintDto : defectList) {
			if(!selectedPartDefectDescList.contains(pdcRegionalAttributeMaintDto.getPartDefectDesc().trim())){
				defectList2.add(pdcRegionalAttributeMaintDto);
			}
		}
		localAttributeMaintTablePanel.setData(defectList2);
	}

	/**
	 * This method is used to create LabeledComboBox
	 * @param id
	 * @return
	 */
	public LabeledComboBox<String> createLabeledComboBox(String id, String labelName, boolean isHorizontal, boolean isMandatory) {
		LabeledComboBox<String> comboBox = new LabeledComboBox<String>(labelName,isHorizontal,new Insets(0),true,isMandatory);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(25);
		comboBox.getControl().getStyleClass().add("combo-box-base");
		return comboBox;
	}

	public ComboBox<String> getEntryScreenComboBox() {
		return entryScreenComboBox.getControl();
	}

	public void setEntryScreenComboBox(LabeledComboBox<String> entryScreenComboBox) {
		this.entryScreenComboBox = entryScreenComboBox;
	}
	public String getFilterTextValue(){
		return StringUtils.trimToEmpty(resultsFilterTxt.getText());
	}

	public ObjectTablePane<PdcRegionalAttributeMaintDto> getLocalAttributeMaintTablePane() {
		return localAttributeMaintTablePanel;
	}

	public void setLocalAttributeMaintTablePane(
			ObjectTablePane<PdcRegionalAttributeMaintDto> localAttributeMaintTablePane) {
		this.localAttributeMaintTablePanel = localAttributeMaintTablePane;
	}

	public LoggedButton getChangeBtn() {
		return this.changeBtn;
	}

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public UpdatePdcController getController() {
		return controller;
	}

	public void setController(UpdatePdcController controller) {
		this.controller = controller;
	}
	
	public CheckBox getUpdateAttribute() {
		return updateAttribute;
	}

	public void setUpdateAttribute(CheckBox updateAttribute) {
		this.updateAttribute = updateAttribute;
	}

	public String getEntryModelText() {
		return entryModelText;
	}

	public void setEntryModelText(String entryModelText) {
		this.entryModelText = entryModelText;
	}

}