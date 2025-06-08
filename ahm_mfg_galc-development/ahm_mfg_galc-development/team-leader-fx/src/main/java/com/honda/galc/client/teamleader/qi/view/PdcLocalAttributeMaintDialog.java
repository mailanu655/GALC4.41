package com.honda.galc.client.teamleader.qi.view;

import java.util.List;


import org.apache.commons.lang.StringUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.mvc.AbstractController;
import com.honda.galc.client.teamleader.qi.controller.PdcLocalAttributeMaintDialogController;
import com.honda.galc.client.teamleader.qi.model.PdcLocalAttributeMaintModel;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.PdcRegionalAttributeMaintDto;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiPddaResponsibility;
import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.entity.qi.QiResponsibleLevel;

/**
 * 
 * <h3>PdcLocalAttributeMaintDialog Class description</h3>
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
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
public class PdcLocalAttributeMaintDialog extends QiFxDialog<PdcLocalAttributeMaintModel> {


	private LoggedTextArea reasonForChangeTextArea;
	private LoggedButton historyButton;
	private LoggedButton assignButton;
	private LoggedButton cancelButton;
	private PdcLocalAttributeMaintDialogController attributeMaintDialogController;
	private List<PdcRegionalAttributeMaintDto> defectCombinationDtoList;
	private PdcLocalRepairRelatedDialog repairRelatedDialog;
	private PdcLocalResponsibilityAssignmentDialog responsibilityAssignmentDialog;
	private PdcLocalPddaRelatedInfoDialog pdcPddaRelatedInfoDialog;
	private PdcRegionalRelatedInfoDialog regionalRelatedInfoDialog;
	private PdcLocalAttributeMaintPanel localAttributeMaintPanel;
	private String buttonName;
	private CheckBox responsibilityAssignmentCheckBox;
	private volatile boolean isCancel = false;

	public PdcLocalAttributeMaintDialog(String title, PdcLocalAttributeMaintPanel localAttributeMaintPanel, PdcLocalAttributeMaintModel model) {
		super(title, localAttributeMaintPanel.getMainWindow().getStage(), model);
		defectCombinationDtoList = localAttributeMaintPanel.getLocalAttributeMaintTablePane().getSelectedItems();
		buttonName=title;
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.attributeMaintDialogController = new PdcLocalAttributeMaintDialogController(model, this, localAttributeMaintPanel);
		this.localAttributeMaintPanel = localAttributeMaintPanel;
		initComponents();
		pdcPddaRelatedInfoDialog.getTitledPane().setVisible(false);
		attributeMaintDialogController.loadResponsibilityAssignment();
		attributeMaintDialogController.initListeners();
		attributeAction(title);
		isCancel = false;
	}

	/**
	 * Attribute action.
	 *
	 * @param title the title
	 */
	private void attributeAction(String title) {
		if(title.contains(QiConstant.ASSIGN_ATTRIBUTE)) {
			assignAttribute();
		} else if(title.contains(QiConstant.UPDATE_ATTRIBUTE)) {
			updateAttribute();
			getAttributeMaintDialogController().updateEnabler();
		}
	}
	
	/**
	 * Assign attribute.
	 */
	private void assignAttribute() {
		try {
			updateButton.setDisable(true);
			reasonForChangeTextArea.setDisable(true);
			pdcPddaRelatedInfoDialog.getTitledPane().setVisible(false);
			if(!isMassUpdate())  {
				loadThemeDetailsData();
			}
			else  {
				initializeReportableOption();
			}
			loadDefectCategoryData();
		} catch (Exception e) {
			attributeMaintDialogController.handleException("An error occured in loading pop up screen ", "Failed To Open Assign Attribute Popup Screen", e);
		}
	}
	
	/**
	 * Update attribute.
	 */
	private void updateAttribute() {
		try {
			assignButton.setDisable(true);
			
			regionalRelatedInfoDialog.getReportableRadioButton().setDisable(true);
			regionalRelatedInfoDialog.getNonReportableRadioButton().setDisable(true);
			regionalRelatedInfoDialog.getDefaultReportableRadioBtn().setDisable(true);
			regionalRelatedInfoDialog.getDefaultReportableRadioBtn().setSelected(true);
			
			if(!isMassUpdate())  {
				
			}
			else{
				getResponsibilityAssignmentDialog().getPddaRelatedInfoCheckBox().setDisable(true);
				addResponsibilityCheckBoxListener();
			}
			
		} catch (Exception e) {
			attributeMaintDialogController.handleException("An error occured in loading pop up screen ", "Failed To Open Update Attribute Popup Screen", e);
		}
	}
	
	/**
	 * This method is used to initialize the components of Dialog screen.
	 */
	private void initComponents() {
		responsibilityAssignmentDialog = PdcLocalResponsibilityAssignmentDialog.getInstance(getAttributeMaintDialogController());
		repairRelatedDialog = PdcLocalRepairRelatedDialog.getInstance(getAttributeMaintDialogController(),isMassUpdate(),buttonName);
		pdcPddaRelatedInfoDialog = PdcLocalPddaRelatedInfoDialog.getInstance(getAttributeMaintDialogController());
		regionalRelatedInfoDialog = PdcRegionalRelatedInfoDialog.getInstance(getAttributeMaintDialogController(), this,isMassUpdate());
		VBox outerPane = new VBox();
		outerPane.setMaxWidth(1000);
		outerPane.setPrefWidth(1000);
		outerPane.getChildren().addAll(
				createThemeAndRepairRelated(), createResponsibilityAssignmentAndPddaRelatedInfo(), createReasonForChangeContainer());
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
		((BorderPane) this.getScene().getRoot()).setBottom(initStatusMessagePane());

	}
	
	/**
	 * Creates the theme and repair related.
	 *
	 * @return the h box
	 */
	private HBox createThemeAndRepairRelated(){
		HBox hBox = new HBox();
		hBox.maxWidth(1000);
		hBox.minWidth(1000);
		hBox.getChildren().addAll(regionalRelatedInfoDialog.createThemeDetails(), repairRelatedDialog.createRepairRelatedDetails());
		return hBox;
	}
	
	/**
	 * Load theme details data.
	 */
	private void loadThemeDetailsData() {
		for(PdcRegionalAttributeMaintDto dto : defectCombinationDtoList) {
			regionalRelatedInfoDialog.getThemeName().setText(dto.getThemeName());
			regionalRelatedInfoDialog.getIqsVersion().setText(dto.getIqsVersion());
			regionalRelatedInfoDialog.getIqsCategory().setText(dto.getIqsCategory());
			regionalRelatedInfoDialog.getIqsQuestion().setText(dto.getIqsQuestion());
			regionalRelatedInfoDialog.getNonReportableRadioButton().setDisable(true);
			regionalRelatedInfoDialog.getReportableRadioButton().setDisable(true);
			regionalRelatedInfoDialog.getDefaultReportableRadioBtn().setSelected(true);
			
		}
		
	}
	  
	public void loadResponsibilityData()  {
		if(defectCombinationDtoList!=null && defectCombinationDtoList.size() == 1) {
			String entryScreen =  this.localAttributeMaintPanel.getEntryScreenComboBox().getValue() != null ? 
					this.localAttributeMaintPanel.getEntryScreenComboBox().getValue().toString() : QiConstant.EMPTY;
			String entryModel = this.localAttributeMaintPanel.getEntryModelComboBox().getValue().toString();
			QiLocalDefectCombination localDefectCombination = getModel().findByRegionalDefectCombinationId(
					defectCombinationDtoList.get(0).getRegionalDefectCombinationId(), entryScreen, entryModel, defectCombinationDtoList.get(0).getIsUsed());
			if(localDefectCombination != null) {
					QiResponsibleLevel responsibleLevel = getModel().findById(localDefectCombination.getResponsibleLevelId());
					this.responsibilityAssignmentDialog.getSiteComboBox().getSelectionModel().select(responsibleLevel.getSite());
					this.attributeMaintDialogController.loadPlantComboBox(responsibleLevel.getSite());
					this.responsibilityAssignmentDialog.getPlantComboBox().getSelectionModel().select(responsibleLevel.getPlant());
					this.attributeMaintDialogController.loadDepartmentComboBox(responsibleLevel.getPlant());
					
					if(responsibleLevel != null) {
						this.responsibilityAssignmentDialog.getDepartmentComboBox().getSelectionModel().select(responsibleLevel.getDepartment());
						//load all responsibility combo boxes
						this.attributeMaintDialogController.loadRespComboBoxesWithAllValues(responsibleLevel.getDepartment());
						//select level 1, this will trigger filtering of level 2 list
						this.responsibilityAssignmentDialog.getResponsibleLevel1ComboBox().getSelectionModel().select(getAttributeMaintDialogController().getKeyValue(responsibleLevel.getResponsibleLevelName()));
						if(responsibleLevel.getUpperResponsibleLevelId() != 0) {
							selectParentLevels(responsibleLevel);
						}
						
					}
					if(localDefectCombination.getPddaResponsibilityId() != null && localDefectCombination.getPddaResponsibilityId() != 0) {
						loadUpdatedPddaRelatedData(localDefectCombination);
					} else {
						this.pdcPddaRelatedInfoDialog.getTitledPane().setVisible(false);
					} 
				}
			}
	}
	
	/**
	 * Load updated assignment data.
	 */
	@SuppressWarnings("unchecked")
	public void loadUpdatedAssignmentData() {
		if(defectCombinationDtoList!=null && defectCombinationDtoList.size() == 1) {
			loadThemeDetailsData();
			String entryScreen =  this.localAttributeMaintPanel.getEntryScreenComboBox().getValue() != null ? 
					this.localAttributeMaintPanel.getEntryScreenComboBox().getValue().toString() : QiConstant.EMPTY;
			String entryModel = this.localAttributeMaintPanel.getEntryModelComboBox().getValue().toString();
			QiLocalDefectCombination localDefectCombination = getModel().findByRegionalDefectCombinationId(
					defectCombinationDtoList.get(0).getRegionalDefectCombinationId(), entryScreen, entryModel, defectCombinationDtoList.get(0).getIsUsed());
			if(localDefectCombination != null) {
				this.attributeMaintDialogController.loadResponsibilityAssignment();
				this.regionalRelatedInfoDialog.getLocalThemeComboBox().getSelectionModel().select((localDefectCombination.getLocalTheme()!=null)? localDefectCombination.getLocalTheme():StringUtils.EMPTY);
				this.regionalRelatedInfoDialog.getDefaultReportableRadioBtn().setSelected(true);
				this.regionalRelatedInfoDialog.getFireEngineYesRadioBtn().setSelected(localDefectCombination.getEngineFiringFlag() == 1);
				this.regionalRelatedInfoDialog.getFireEngineNoRadioBtn().setSelected(localDefectCombination.getEngineFiringFlag() == 0);
				QiRepairArea qiRepairArea=getModel().findRepairAreaByName(localDefectCombination.getRepairAreaName());
				if(qiRepairArea	!=null){
					if( !qiRepairArea.getRepairAreaDescription().equalsIgnoreCase(StringUtils.EMPTY))
						this.repairRelatedDialog.getInitialRepairAreaComboBox().getSelectionModel().select(localDefectCombination.getRepairAreaName()+"-"+qiRepairArea.getRepairAreaDescription());
					else
						this.repairRelatedDialog.getInitialRepairAreaComboBox().getSelectionModel().select(localDefectCombination.getRepairAreaName());
				}else{
					this.repairRelatedDialog.getInitialRepairAreaComboBox().getSelectionModel().select(StringUtils.EMPTY);
				}
				
				this.repairRelatedDialog.getInitialRepairMethodComboBox().getSelectionModel().select(localDefectCombination.getRepairMethod());
				this.repairRelatedDialog.getInitialRepairMethodTimeText().setText(String.valueOf(localDefectCombination.getRepairMethodTime()));
				this.repairRelatedDialog.getTotalTimeToFixText().setText(String.valueOf(localDefectCombination.getEstimatedTimeToFix()));
				this.repairRelatedDialog.getRealProblemRadioBtn().setSelected(localDefectCombination.getDefectCategoryName().equalsIgnoreCase(QiConstant.REAL_PROBLEM));
				this.repairRelatedDialog.getSymptomRadioBtn().setSelected(localDefectCombination.getDefectCategoryName().equalsIgnoreCase(QiConstant.SYMPTOM));
				this.repairRelatedDialog.getInformationalRadioBtn().setSelected(localDefectCombination.getDefectCategoryName().equalsIgnoreCase(QiConstant.INFORMATIONAL));
				loadResponsibilityData();
			} else {
				this.pdcPddaRelatedInfoDialog.getTitledPane().setVisible(false);
			}
		}
		
	}
	
	/**
	 * Load updated responsibility data.
	 *
	 * @param responsibleLevel the responsible level
	 */
	@SuppressWarnings("unchecked")
	private void selectParentLevels(QiResponsibleLevel l1) {
		QiResponsibleLevel nextLevel = null;
		QiResponsibleLevel l3 = null;
		
		nextLevel = getModel().findById(l1.getUpperResponsibleLevelId());
		if(nextLevel == null)  return;  //nothing to do
		if(nextLevel.getLevel() == (short)2)  { // level 2
			//select level2, this will trigger filtering of level 3 list
			this.responsibilityAssignmentDialog.getResponsibleLevel2ComboBox().getSelectionModel().select(getAttributeMaintDialogController().getKeyValue(nextLevel.getResponsibleLevelName()));
			if(nextLevel.getUpperResponsibleLevelId() != 0) {  //level 2 has a level 3
				l3 = getModel().findById(nextLevel.getUpperResponsibleLevelId()); 
				this.responsibilityAssignmentDialog.getResponsibleLevel3ComboBox().getSelectionModel().select(getAttributeMaintDialogController().getKeyValue(l3.getResponsibleLevelName()));
			}
		}
		else if(nextLevel.getLevel() == (short)3) { //level 1 has direct level 3 parent
			this.responsibilityAssignmentDialog.getResponsibleLevel3ComboBox().getSelectionModel().select(getAttributeMaintDialogController().getKeyValue(nextLevel.getResponsibleLevelName()));
		}
		
	}
	
	/**
	 * Load updated pdda related data.
	 *
	 * @param localDefectCombination the local defect combination
	 */
	@SuppressWarnings({"unchecked" })
	private void loadUpdatedPddaRelatedData(QiLocalDefectCombination localDefectCombination) {
		this.responsibilityAssignmentDialog.getPddaRelatedInfoCheckBox().setSelected(true);
		this.attributeMaintDialogController.loadModelYear();
		QiPddaResponsibility pddaResponsibility = getModel().findByPddaId(localDefectCombination.getPddaResponsibilityId());
		this.pdcPddaRelatedInfoDialog.getModelYearComboBox().getSelectionModel().select(pddaResponsibility.getModelYear());
		this.attributeMaintDialogController.loadVehicleModelCodeComboBox(pddaResponsibility.getModelYear());
		this.pdcPddaRelatedInfoDialog.getVehicleModelCodeComboBox().getSelectionModel().select(pddaResponsibility.getVehicleModelCode());
		this.attributeMaintDialogController.loadProcessNumberComboBox(pddaResponsibility.getVehicleModelCode());
		this.pdcPddaRelatedInfoDialog.getProcessNumberComboBox().getSelectionModel().select(pddaResponsibility.getProcessNumber()+" - "+pddaResponsibility.getProcessName());
		this.attributeMaintDialogController.loadUnitNumberComboBox(pddaResponsibility.getProcessNumber());
		this.pdcPddaRelatedInfoDialog.getUnitNumberComboBox().getSelectionModel().select(pddaResponsibility.getUnitNumber()+" - "+pddaResponsibility.getUnitDescription());
		this.pdcPddaRelatedInfoDialog.getUnitDescriptionText().setText(pddaResponsibility.getUnitDescription());
	}
	
	private TitledPane createResponsibilityAssignmentAndPddaRelatedInfo(){
		TitledPane titledPane = new TitledPane();
        titledPane.setText("Responsibility Assignment");
        responsibilityAssignmentCheckBox=new CheckBox();
        if(isMassUpdate() && buttonName.contains(QiConstant.UPDATE_ATTRIBUTE))
        	titledPane.setGraphic(responsibilityAssignmentCheckBox);
		HBox hBox = new HBox();
		hBox.getChildren().addAll(responsibilityAssignmentDialog.createResponsibilityAssignmentDetails(), pdcPddaRelatedInfoDialog.createPddaRelatedInfo());
		titledPane.setPadding(new Insets(5, 2, 5, 5));
		titledPane.setContent(hBox);
		return titledPane;
	}
	
	
	/**
	 * Reason for change container.create
	 *
	 * @return the h box
	 */
	private HBox createReasonForChangeContainer() {
		HBox reasonForChangeContainer = new HBox();
		reasonForChangeTextArea = UiFactory.createTextArea();
		reasonForChangeTextArea.setPrefRowCount(2);
		reasonForChangeTextArea.setPrefColumnCount(25);
		reasonForChangeTextArea.setId("reasonForChangeTextArea");
		reasonForChangeContainer.setMaxWidth(1000);
		reasonForChangeContainer.setMinWidth(1000);
		
		HBox labelBox = new HBox();
		LoggedLabel reasonForChangeLabel = UiFactory.createLabel("label", "Reason For Change");
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		asterisk.setStyle("-fx-text-fill: red");
		labelBox.getChildren().addAll(reasonForChangeLabel, asterisk);
		labelBox.setMaxWidth(120.0);
		labelBox.setPrefWidth(120.0);

		reasonForChangeContainer.setPadding(new Insets(5, 2, 5, 5));
		reasonForChangeContainer.setSpacing(10);
		reasonForChangeContainer.getChildren().addAll(labelBox, reasonForChangeTextArea, createButtonContainer());
		return reasonForChangeContainer;
	}
	
	/**
	 * create the button container.
	 *
	 * @return the button container
	 */
	private HBox createButtonContainer() {
		HBox buttonContainer = new HBox();
		historyButton = createBtn(QiConstant.HISTORY, getAttributeMaintDialogController());
		
		if (defectCombinationDtoList.size() > 1) {
			historyButton.setDisable(true);
		}
		assignButton = createBtn(QiConstant.ASSIGN, getAttributeMaintDialogController());
		updateButton = createBtn(QiConstant.UPDATE, getAttributeMaintDialogController());
		cancelButton = createBtn(QiConstant.CANCEL, getAttributeMaintDialogController());
		buttonContainer.setMaxWidth(550);
		buttonContainer.setMinWidth(550);
		buttonContainer.setAlignment(Pos.CENTER_RIGHT);
		buttonContainer.setPadding(new Insets(5,5,5,5));
		buttonContainer.setSpacing(10);
		buttonContainer.getChildren().addAll(historyButton,assignButton, updateButton, cancelButton);
		return buttonContainer;
	}
	
	/**
	 * Load defect category data
	 */
	private void loadDefectCategoryData() {
		this.repairRelatedDialog.getRealProblemRadioBtn().setSelected(false);
		this.repairRelatedDialog.getSymptomRadioBtn().setSelected(false);
		this.repairRelatedDialog.getInformationalRadioBtn().setSelected(false);
		this.repairRelatedDialog.getDefaultRadioBtn().setSelected(true);
	}
	
	/**
	 * Load reportable/non-reportable
	 */
	private void initializeReportableOption() {
		this.regionalRelatedInfoDialog.getReportableRadioButton().setDisable(true);
		this.regionalRelatedInfoDialog.getNonReportableRadioButton().setDisable(true);
		this.regionalRelatedInfoDialog.getDefaultReportableRadioBtn().setSelected(true);
	}
	
	/**
	 * This method checks mass update .
	 */

	private boolean isMassUpdate() {
		return  (defectCombinationDtoList.size()>1);
	}
	

	private void addResponsibilityCheckBoxListener() {
		getResponsibilityAssignmentCheckBox().selectedProperty().addListener(new ChangeListener<Boolean>() {
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    		getResponsibilityAssignmentDialog().getPddaRelatedInfoCheckBox().setDisable(!newValue);
		    }
		});
	}

	public LoggedTextArea getReasonForChangeTextArea() {
		return reasonForChangeTextArea;
	}

	public void setReasonForChangeTextArea(LoggedTextArea reasonForChangeTextArea) {
		this.reasonForChangeTextArea = reasonForChangeTextArea;
	}

	public LoggedButton getAssignButton() {
		return assignButton;
	}

	public void setAssignButton(LoggedButton assignButton) {
		this.assignButton = assignButton;
	}

	public LoggedButton getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(LoggedButton cancelButton) {
		this.cancelButton = cancelButton;
	}

	public PdcLocalAttributeMaintDialogController getAttributeMaintDialogController() {
		return attributeMaintDialogController;
	}

	public void setAttributeMaintDialogController(
			PdcLocalAttributeMaintDialogController attributeMaintDialogController) {
		this.attributeMaintDialogController = attributeMaintDialogController;
	}

	public List<PdcRegionalAttributeMaintDto> getDefectCombinationDtoList() {
		return defectCombinationDtoList;
	}

	public void setDefectCombinationDtoList(
			List<PdcRegionalAttributeMaintDto> defectCombinationDtoList) {
		this.defectCombinationDtoList = defectCombinationDtoList;
	}

	public PdcLocalRepairRelatedDialog getRepairRelatedDialog() {
		return repairRelatedDialog;
	}

	public void setRepairRelatedDialog(
			PdcLocalRepairRelatedDialog repairRelatedDialog) {
		this.repairRelatedDialog = repairRelatedDialog;
	}

	public PdcLocalResponsibilityAssignmentDialog getResponsibilityAssignmentDialog() {
		return responsibilityAssignmentDialog;
	}

	public void setResponsibilityAssignmentDialog(
			PdcLocalResponsibilityAssignmentDialog responsibilityAssignmentDialog) {
		this.responsibilityAssignmentDialog = responsibilityAssignmentDialog;
	}

	public PdcLocalPddaRelatedInfoDialog getPdcPddaRelatedInfoDialog() {
		return pdcPddaRelatedInfoDialog;
	}

	public void setPdcPddaRelatedInfoDialog(
			PdcLocalPddaRelatedInfoDialog pdcPddaRelatedInfoDialog) {
		this.pdcPddaRelatedInfoDialog = pdcPddaRelatedInfoDialog;
	}

	public PdcRegionalRelatedInfoDialog getRegionalRelatedInfoDialog() {
		return regionalRelatedInfoDialog;
	}

	public void setRegionalRelatedInfoDialog(
			PdcRegionalRelatedInfoDialog regionalRelatedInfoDialog) {
		this.regionalRelatedInfoDialog = regionalRelatedInfoDialog;
	}

	public CheckBox getResponsibilityAssignmentCheckBox() {
		return responsibilityAssignmentCheckBox;
	}

	public void setResponsibilityAssignmentCheckBox(
			CheckBox responsibilityAssignmentCheckBox) {
		this.responsibilityAssignmentCheckBox = responsibilityAssignmentCheckBox;
	}

	public String getButtonName() {
		return buttonName;
	}

	/**
	 * @return the localAttributeMaintPanel
	 */
	public PdcLocalAttributeMaintPanel getLocalAttributeMaintPanel() {
		return localAttributeMaintPanel;
	}

	/**
	 * @param localAttributeMaintPanel the localAttributeMaintPanel to set
	 */
	public void setLocalAttributeMaintPanel(PdcLocalAttributeMaintPanel localAttributeMaintPanel) {
		this.localAttributeMaintPanel = localAttributeMaintPanel;
	}

	/**
	 * @return the isCancel
	 */
	public boolean isCancel() {
		return isCancel;
	}

	/**
	 * @param isCancel the isCancel to set
	 */
	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}

	public LoggedButton getHistoryButton() {
	
		PdcRegionalAttributeMaintDto rDto = defectCombinationDtoList.get(0);
	
		String entryScreen =  localAttributeMaintPanel.getEntryScreenComboBox().getValue();
		String entryModel = localAttributeMaintPanel.getEntryModelComboBox().getValue();
		
		if(rDto != null)  {
			QiLocalDefectCombination localDefectCombination = getModel().findByRegionalDefectCombinationId(
					rDto.getRegionalDefectCombinationId(), entryScreen, entryModel, rDto.getIsUsed());
			if(localDefectCombination != null)  {
				QiPdcHistoryDialog qiPdcHistoryDialog = new QiPdcHistoryDialog("PDC History", getModel(), this, entryScreen, localDefectCombination,rDto.getFullPartName());
				qiPdcHistoryDialog.showDialog(); 
			}
		}
	
		return historyButton;
	}

	public void setHistoryButton(LoggedButton historyButton) {
		this.historyButton = historyButton;
	}
	
}
