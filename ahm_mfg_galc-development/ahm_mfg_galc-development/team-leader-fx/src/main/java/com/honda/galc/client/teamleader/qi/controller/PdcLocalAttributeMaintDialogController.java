package com.honda.galc.client.teamleader.qi.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.PdcLocalAttributeMaintModel;
import com.honda.galc.client.teamleader.qi.view.PdcLocalAttributeMaintDialog;
import com.honda.galc.client.teamleader.qi.view.PdcLocalAttributeMaintPanel;
import com.honda.galc.client.teamleader.qi.view.PdcLocalRepairRelatedDialog;
import com.honda.galc.client.teamleader.qi.view.PdcLocalResponsibilityAssignmentDialog;
import com.honda.galc.client.teamleader.qi.view.PdcRegionalRelatedInfoDialog;
import com.honda.galc.client.teamleader.qi.view.QiPdcHistoryDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.qi.PdcRegionalAttributeMaintDto;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiPddaResponsibility;
import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.entity.qi.QiRepairMethod;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.entity.qi.QiLocalTheme;
import com.honda.galc.util.AuditLoggerUtil;
import com.honda.galc.util.KeyValue;

/**
 * 
 * <h3>PdcRegionalAttributeMaintDialogController Class description</h3>
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
public class PdcLocalAttributeMaintDialogController  extends QiDialogController<PdcLocalAttributeMaintModel, PdcLocalAttributeMaintDialog> {
	
	private List<PdcRegionalAttributeMaintDto> defectCombinationDtoList;
	private PdcLocalAttributeMaintPanel localAttributeMaintPanel;
	private QiPddaResponsibility pddaResponsibility;
	private volatile int comboLvlPrimary = 0;
	private int comboLvlSecondary = 0;
	
	protected ChangeListener<KeyValue<Integer, String>> updateEnablerForKeyValueChange = new ChangeListener<KeyValue<Integer, String>>() {
		public void changed(ObservableValue<? extends KeyValue<Integer, String>> observable, KeyValue<Integer, String> oldValue, KeyValue<Integer, String> newValue) {
			if(getDialog().getTitle().contains(QiConstant.UPDATE)){
				getDialog().getUpdateButton().setDisable(false);
			}
		}
	};
	
	protected ChangeListener<KeyValue<String,Integer>> updateEnablerForResponsibilityChange = new ChangeListener<KeyValue<String,Integer>>() {
		public void changed(ObservableValue<? extends KeyValue<String,Integer>> observable, KeyValue<String,Integer> oldValue, KeyValue<String,Integer> newValue) {
			if(getDialog().getTitle().contains(QiConstant.UPDATE)){
				getDialog().getUpdateButton().setDisable(false);
			}
		}
	};
	
	public PdcLocalAttributeMaintDialogController(PdcLocalAttributeMaintModel model, PdcLocalAttributeMaintDialog dialog, PdcLocalAttributeMaintPanel localAttributeMaintPanel) {
		super();
		setModel(model);
		setDialog(dialog);
		this.defectCombinationDtoList = localAttributeMaintPanel.getLocalAttributeMaintTablePane().getSelectedItems();
		this.localAttributeMaintPanel = localAttributeMaintPanel;
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource().equals(getDialog().getAssignButton())) assignButtonAction(actionEvent);
		else if(actionEvent.getSource().equals(getDialog().getUpdateButton())) {
			if(defectCombinationDtoList.size()>1)
				massUpdateAction(actionEvent);
			else
				updateButtonAction(actionEvent);
		}
		else if(actionEvent.getSource().equals(getDialog().getCancelButton())) cancelButtonAction(actionEvent);
		else if(actionEvent.getSource().equals(getDialog().getResponsibilityAssignmentDialog().getClearResp()))  {
			comboLvlPrimary = 0;
			comboLvlSecondary = 0;
			Object dept = getDialog().getResponsibilityAssignmentDialog().getDepartmentComboBox().getValue();
			if(dept != null)  {
				loadRespComboBoxesWithAllValues(dept.toString());
			}
			else  {
				clearAllResponsibleComboBoxes();
			}
		}
		else if(actionEvent.getSource().equals(getDialog().getResponsibilityAssignmentDialog().getResetResp()))  {
			comboLvlPrimary = 1;
			comboLvlSecondary = 2;
			getDialog().loadResponsibilityData();
		}
		else if(actionEvent.getSource().equals(getDialog().getHistoryButton())) historyButtonAction(actionEvent); 
	}

	@Override
	public void initListeners() {
		clearDisplayMessage();
		validationForTextfield();
		addListener();
	}

	@SuppressWarnings("unchecked")
	public void updateEnabler() {
		getDialog().getRegionalRelatedInfoDialog().getDefaultReportableRadioBtn().getToggleGroup().selectedToggleProperty().addListener(updateEnablerForToggle);
		getDialog().getRegionalRelatedInfoDialog().getFireEngineNoRadioBtn().getToggleGroup().selectedToggleProperty().addListener(updateEnablerForToggle);
		getDialog().getRepairRelatedDialog().getDefaultRadioBtn().getToggleGroup().selectedToggleProperty().addListener(updateEnablerForToggle);
		
		getDialog().getResponsibilityAssignmentDialog().getResponsibleLevel1ComboBox().valueProperty().addListener(updateEnablerForResponsibilityChange);
		getDialog().getResponsibilityAssignmentDialog().getResponsibleLevel2ComboBox().valueProperty().addListener(updateEnablerForResponsibilityChange);
		getDialog().getResponsibilityAssignmentDialog().getResponsibleLevel3ComboBox().valueProperty().addListener(updateEnablerForResponsibilityChange);
		getDialog().getRepairRelatedDialog().getInitialRepairAreaComboBox().valueProperty().addListener(updateEnablerForStringValueChange);
		getDialog().getRepairRelatedDialog().getInitialRepairMethodComboBox().valueProperty().addListener(updateEnablerForStringValueChange);
		getDialog().getRegionalRelatedInfoDialog().getLocalThemeComboBox().valueProperty().addListener(updateEnablerForStringValueChange);
		getDialog().getPdcPddaRelatedInfoDialog().getModelYearComboBox().valueProperty().addListener(updateEnablerForBigDecimalValueChange);
		getDialog().getPdcPddaRelatedInfoDialog().getProcessNumberComboBox().valueProperty().addListener(updateEnablerForStringValueChange);
		getDialog().getPdcPddaRelatedInfoDialog().getUnitNumberComboBox().valueProperty().addListener(updateEnablerForStringValueChange);
		getDialog().getPdcPddaRelatedInfoDialog().getVehicleModelCodeComboBox().valueProperty().addListener(updateEnablerForStringValueChange);
		
		
	}

	/**
	 * Adds the pdda related check box listener.
	 */
	private void addPddaRelatedCheckBoxListener() {
		getDialog().getResponsibilityAssignmentDialog().getPddaRelatedInfoCheckBox().selectedProperty().addListener(new ChangeListener<Boolean>() {
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	disablePddaRelatedInfo(newValue);
		    }
		});
	}
	
	/**
	 * Disable pdda related info.
	 *
	 * @param isPddaRelated the is pdda related
	 */
	public void disablePddaRelatedInfo(boolean isPddaRelated){
		getDialog().getPdcPddaRelatedInfoDialog().getTitledPane().setVisible(isPddaRelated);
		clearPDDARelatedInfo();
		loadModelYear();
		loadPDDARelatedInfo();
	}
	
	/**
	 * Load responsibility assignment.
	 */
	public void loadResponsibilityAssignment() {
		loadLocalThemeComboBox();
		loadSiteComboBox();
		loadintialRepairAreaComboBox();
		loadintialRepairMethodComboBox();
	}
	
	/**
	 * Adds the listener.
	 */
	public void addListener() {
		addSiteComboBoxListener();
		addPlantComboBoxListener();
		addDepartmentComboBoxListener();
		addPddaRelatedCheckBoxListener();
		addResponsibleLevel1ComboBoxListener();
		addResponsibleLevel2ComboBoxListener();
		addResponsibleLevel3ComboBoxListener();
		addVehicleModelCodeComboBoxListener();
		addModelYearComboBoxListener();
		addProcessNumberComboBoxListener();
		addUnitNumberComboBoxListener();
	}
	
	
	/**
	 * Adds the model year combo box listener.
	 */
	@SuppressWarnings("unchecked")
	private void addModelYearComboBoxListener() {
		getDialog().getPdcPddaRelatedInfoDialog().getModelYearComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BigDecimal>() {
			 public void changed(ObservableValue<? extends BigDecimal> ov,  BigDecimal old_val, BigDecimal new_val) { 
				 loadVehicleModelCodeComboBox(new_val);
			 } 
		});
	}
	
	/**
	 * Load pdda related info.
	 */
	@SuppressWarnings("unchecked")
	private void loadPDDARelatedInfo() {
		if(defectCombinationDtoList.size() == 1 && defectCombinationDtoList.get(0).getPddaResponsibilityId() != null) {
			QiPddaResponsibility pddaResponsibility = getModel().findByPddaId(defectCombinationDtoList.get(0).getPddaResponsibilityId());
			if (pddaResponsibility != null) {
				getDialog().getPdcPddaRelatedInfoDialog().getModelYearComboBox().getSelectionModel().select(pddaResponsibility.getModelYear());
				loadVehicleModelCodeComboBox(pddaResponsibility.getModelYear());
				getDialog().getPdcPddaRelatedInfoDialog().getVehicleModelCodeComboBox().getSelectionModel().select(pddaResponsibility.getVehicleModelCode());
				loadProcessNumberComboBox(pddaResponsibility.getVehicleModelCode());
				loadUnitNumberComboBox(pddaResponsibility.getProcessNumber());
				getDialog().getPdcPddaRelatedInfoDialog().getUnitDescriptionText().setText(pddaResponsibility.getUnitDescription());
				getDialog().getPdcPddaRelatedInfoDialog().getProcessNumberComboBox().getItems();
				getDialog().getPdcPddaRelatedInfoDialog().getProcessNumberComboBox().getSelectionModel().select(pddaResponsibility.getProcessNumber()+ " - "+pddaResponsibility.getProcessName());
				getDialog().getPdcPddaRelatedInfoDialog().getUnitNumberComboBox().getSelectionModel().select(pddaResponsibility.getUnitNumber()+" - "+pddaResponsibility.getUnitDescription());
			} else {
				Logger.getLogger().error(QiPddaResponsibility.class.getSimpleName() + " not found for id " + defectCombinationDtoList.get(0).getPddaResponsibilityId());
			}
		}
	}
	
	/**
	 * Clear pdda related info.
	 */
	private void clearPDDARelatedInfo() {
		clearComboBox(getDialog().getPdcPddaRelatedInfoDialog().getVehicleModelCodeComboBox());
		clearComboBox(getDialog().getPdcPddaRelatedInfoDialog().getProcessNumberComboBox());
		clearComboBox(getDialog().getPdcPddaRelatedInfoDialog().getUnitNumberComboBox());
		
	}
	
	/**
	 * Load vehicle model code combo box.
	 *
	 * @param modelYear the model year
	 */
	@SuppressWarnings("unchecked")
	public void loadVehicleModelCodeComboBox(BigDecimal modelYear) {
		clearComboBox(getDialog().getPdcPddaRelatedInfoDialog().getVehicleModelCodeComboBox());
		getDialog().getPdcPddaRelatedInfoDialog().getVehicleModelCodeComboBox().setPromptText("Select");
		getDialog().getPdcPddaRelatedInfoDialog().getVehicleModelCodeComboBox().getItems().addAll(
				getModel().findAllVehicleModelCodeByPlantSiteDeptLineModelYear(
				getDialog().getResponsibilityAssignmentDialog().getPlantComboBox().getSelectionModel().getSelectedItem().toString(),
				getDialog().getResponsibilityAssignmentDialog().getSiteComboBox().getSelectionModel().getSelectedItem().toString(),
				getDialog().getResponsibilityAssignmentDialog().getDepartmentComboBox().getSelectionModel().getSelectedItem().toString(),
				getDialog().getResponsibilityAssignmentDialog().getResponsibleLevel1ComboBox().getSelectionModel().getSelectedItem().toString(),
				modelYear));
		
	}

	/**
	 * Adds the vehicle model code combo box listener.
	 */
	@SuppressWarnings("unchecked")
	private void addVehicleModelCodeComboBoxListener() {
		getDialog().getPdcPddaRelatedInfoDialog().getVehicleModelCodeComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			 public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
				 loadProcessNumberComboBox(new_val);
			 } 
		});
	}
	
	/**
	 * Load process number combo box.
	 *
	 * @param vmc the vmc
	 */
	@SuppressWarnings("unchecked")
	public void loadProcessNumberComboBox(String vmc) {
		clearComboBox(getDialog().getPdcPddaRelatedInfoDialog().getProcessNumberComboBox());
		getDialog().getPdcPddaRelatedInfoDialog().getProcessNumberComboBox().setPromptText("Select");
		if(getDialog().getPdcPddaRelatedInfoDialog().getModelYearComboBox().getValue() != null){
		BigDecimal modelYear = (BigDecimal)getDialog().getPdcPddaRelatedInfoDialog().getModelYearComboBox().getSelectionModel().getSelectedItem();
		getDialog().getPdcPddaRelatedInfoDialog().getProcessNumberComboBox().getItems().addAll(getModel().findAllProcessNoByPlantSiteDeptLineModelYearVMC(
				getDialog().getResponsibilityAssignmentDialog().getPlantComboBox().getSelectionModel().getSelectedItem().toString(),
				getDialog().getResponsibilityAssignmentDialog().getSiteComboBox().getSelectionModel().getSelectedItem().toString(),
				getDialog().getResponsibilityAssignmentDialog().getDepartmentComboBox().getSelectionModel().getSelectedItem().toString(),
				getDialog().getResponsibilityAssignmentDialog().getResponsibleLevel1ComboBox().getSelectionModel().getSelectedItem().toString(),
				modelYear,vmc));
		}
	}
	
	/**
	 * Adds the process number combo box listener.
	 */
	@SuppressWarnings("unchecked")
	private void addProcessNumberComboBoxListener() {
		getDialog().getPdcPddaRelatedInfoDialog().getProcessNumberComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			 public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
				 loadUnitNumberComboBox(new_val);
			 } 
		});
	}
	
	/**
	 * Load unit number combo box.
	 *
	 * @param processNumber the process number
	 */
	@SuppressWarnings("unchecked")
	public void loadUnitNumberComboBox(String processNumber) {
		clearComboBox(getDialog().getPdcPddaRelatedInfoDialog().getUnitNumberComboBox());
		getDialog().getPdcPddaRelatedInfoDialog().getUnitNumberComboBox().setPromptText("Select");
		if(getDialog().getPdcPddaRelatedInfoDialog().getModelYearComboBox().getValue() != null 
				&& getDialog().getPdcPddaRelatedInfoDialog().getVehicleModelCodeComboBox().getValue() != null
					&& processNumber!=null && !processNumber.equals("") ){
		BigDecimal modelYear = (BigDecimal) getDialog().getPdcPddaRelatedInfoDialog().getModelYearComboBox().getSelectionModel().getSelectedItem();
		String vmc =  getDialog().getPdcPddaRelatedInfoDialog().getVehicleModelCodeComboBox().getValue().toString();		
		getDialog().getPdcPddaRelatedInfoDialog().getUnitNumberComboBox().getItems().addAll(getModel().findAllUnitNoByPlantSiteDeptLineModelYearVMCProcessNo(
				getDialog().getResponsibilityAssignmentDialog().getPlantComboBox().getSelectionModel().getSelectedItem().toString(),
				getDialog().getResponsibilityAssignmentDialog().getSiteComboBox().getSelectionModel().getSelectedItem().toString(),
				getDialog().getResponsibilityAssignmentDialog().getDepartmentComboBox().getSelectionModel().getSelectedItem().toString(),
				getDialog().getResponsibilityAssignmentDialog().getResponsibleLevel1ComboBox().getSelectionModel().getSelectedItem().toString(),
				modelYear,vmc,processNumber.split("-")[0]));
		}
	}

	/**
	 * Adds the unit number combo box listener.
	 */
	@SuppressWarnings("unchecked")
	private void addUnitNumberComboBoxListener() {
		getDialog().getPdcPddaRelatedInfoDialog().getUnitNumberComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			 public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
				 loadUnitDescription(new_val);
			 } 
		});
	}
	
	/**
	 * Load unit description.
	 *
	 * @param unitNumber the unit number
	 */
	private void loadUnitDescription(String unitNumber) {
		getDialog().getPdcPddaRelatedInfoDialog().getUnitDescriptionText().setText("");
		if(getDialog().getPdcPddaRelatedInfoDialog().getModelYearComboBox().getValue() != null 
				&& getDialog().getPdcPddaRelatedInfoDialog().getVehicleModelCodeComboBox().getValue() != null
					&& getDialog().getPdcPddaRelatedInfoDialog().getProcessNumberComboBox().getValue() != null
					&& unitNumber!=null && !unitNumber.equals("")){
			this.pddaResponsibility = getModel().findUnitDescByPlantSiteDeptLineModelYearVMCProcessNoUnitNo(
					getDialog().getResponsibilityAssignmentDialog().getPlantComboBox().getSelectionModel().getSelectedItem().toString(),
					getDialog().getResponsibilityAssignmentDialog().getSiteComboBox().getSelectionModel().getSelectedItem().toString(),
					getDialog().getResponsibilityAssignmentDialog().getDepartmentComboBox().getSelectionModel().getSelectedItem().toString(),
					getDialog().getResponsibilityAssignmentDialog().getResponsibleLevel1ComboBox().getSelectionModel().getSelectedItem().toString(),
					(BigDecimal) getDialog().getPdcPddaRelatedInfoDialog().getModelYearComboBox().getSelectionModel().getSelectedItem(),
					getDialog().getPdcPddaRelatedInfoDialog().getVehicleModelCodeComboBox().getValue().toString(),
					getDialog().getPdcPddaRelatedInfoDialog().getProcessNumberComboBox().getValue().toString().split("-")[0],unitNumber.split("-")[0]);
			getDialog().getPdcPddaRelatedInfoDialog().getUnitDescriptionText().setText(pddaResponsibility != null ? pddaResponsibility.getUnitDescription() : "");
		}
	}

	/**
	 * Load site combo box.
	 */
	@SuppressWarnings("unchecked")
	private void loadSiteComboBox() {
		clearComboBox(getDialog().getResponsibilityAssignmentDialog().getSiteComboBox());
		getDialog().getResponsibilityAssignmentDialog().getSiteComboBox().setPromptText("Select");
		getDialog().getResponsibilityAssignmentDialog().getSiteComboBox().getItems().addAll(getModel().findAllActiveSites());
	}
	
	/**
	 * Adds the site combo box listener.
	 */
	@SuppressWarnings("unchecked")
	private void addSiteComboBoxListener() {
		getDialog().getResponsibilityAssignmentDialog().getSiteComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			 public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
				 loadPlantComboBox(new_val);
			 } 
		});
	}
	
	/**
	 * Adds the plant combo box listener.
	 */
	@SuppressWarnings("unchecked")
	private void addPlantComboBoxListener() {
		getDialog().getResponsibilityAssignmentDialog().getPlantComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			 public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
				 loadDepartmentComboBox(new_val);
			 } 
		});
	}
	
	/**
	 * Adds the department combo box listener.
	 */
	@SuppressWarnings("unchecked")
	private void addDepartmentComboBoxListener() {
		getDialog().getResponsibilityAssignmentDialog().getDepartmentComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			 public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
				 loadRespComboBoxesWithAllValues(new_val);
			 } 
		});
	}
	
	
	/**
	 * Adds the responsible level1 combo box listener.
	 */
	@SuppressWarnings("unchecked")
	private void addResponsibleLevel1ComboBoxListener() {
		getDialog().getResponsibilityAssignmentDialog().getResponsibleLevel1ComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<KeyValue<String,Integer>>() {
			 public void changed(ObservableValue<? extends KeyValue<String,Integer>> ov,  KeyValue<String,Integer> old_val, KeyValue<String,Integer> new_val) { 
				 loadWhenLevel1Changed(new_val);
				 getDialog().getResponsibilityAssignmentDialog().getPddaRelatedInfoCheckBox().setDisable(false);
				 if(getDialog().getResponsibilityAssignmentDialog().getPddaRelatedInfoCheckBox().isSelected())
					 loadModelYear();
			 } 
		});
	}
	
	/**
	 * Adds the responsible level2 combo box listener.
	 */
	@SuppressWarnings("unchecked")
	private void addResponsibleLevel2ComboBoxListener() {
		getDialog().getResponsibilityAssignmentDialog().getResponsibleLevel2ComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<KeyValue<String,Integer>>() {
			 public void changed(ObservableValue<? extends KeyValue<String,Integer>> ov,  KeyValue<String,Integer> old_val, KeyValue<String,Integer> new_val) { 
				 loadWhenLevel2Changed(new_val);
			 } 
		});
	}
	
	/**
	 * Adds the responsible level2 combo box listener.
	 */
	@SuppressWarnings("unchecked")
	private void addResponsibleLevel3ComboBoxListener() {
		getDialog().getResponsibilityAssignmentDialog().getResponsibleLevel3ComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<KeyValue<String,Integer>>() {
			 public void changed(ObservableValue<? extends KeyValue<String,Integer>> ov,  KeyValue<String,Integer> old_val, KeyValue<String,Integer> new_val) { 
				 loadWhenLevel3Changed(new_val);
			 } 
		});
	}
	
	/**
	 * Load plant combo box.
	 *
	 * @param site the site
	 */
	@SuppressWarnings("unchecked")
	public void loadPlantComboBox(String site) {
		clearComboBox(getDialog().getResponsibilityAssignmentDialog().getPlantComboBox());
		getDialog().getResponsibilityAssignmentDialog().getPlantComboBox().setPromptText("Select");
		getDialog().getResponsibilityAssignmentDialog().getPlantComboBox().getItems().addAll(getModel().findAllActivePlantsBySite(site));
	}
	
	/**
	 * Load department combo box.
	 *
	 * @param plant the plant
	 */
	@SuppressWarnings("unchecked")
	public void loadDepartmentComboBox(String plant) {
		clearComboBox(getDialog().getResponsibilityAssignmentDialog().getDepartmentComboBox());
		String site = getDialog().getResponsibilityAssignmentDialog().getSiteComboBox().getValue() != null ? getDialog().getResponsibilityAssignmentDialog().getSiteComboBox().getValue().toString() : "";
		getDialog().getResponsibilityAssignmentDialog().getDepartmentComboBox().setPromptText("Select");
		getDialog().getResponsibilityAssignmentDialog().getDepartmentComboBox().getItems().addAll(getModel().findAllActiveDepartmentsBySiteAndPlant(site, plant));
	}
	
	/**
	 * Load responsible level3 combo box.
	 *
	 * @param department the department
	 */
	@SuppressWarnings("unchecked")
	public void loadWhenLevel2Changed(KeyValue<String,Integer> new_val) {
		
		PdcLocalResponsibilityAssignmentDialog responsibilityPanel = getDialog().getResponsibilityAssignmentDialog();
		String site = responsibilityPanel.getSiteComboBox().getValue().toString();
		String plant = responsibilityPanel.getPlantComboBox().getValue().toString();
		String department = responsibilityPanel.getDepartmentComboBox().getValue().toString();
		LoggedComboBox<KeyValue<String,Integer>> l1ComboBox = responsibilityPanel.getResponsibleLevel1ComboBox();
		LoggedComboBox<KeyValue<String,Integer>> l3ComboBox = responsibilityPanel.getResponsibleLevel3ComboBox();
		
		if(new_val == null)  return;
		if(comboLvlPrimary == 0)  { //primary is open, set level 2 as primary
			comboLvlPrimary = 2;
		}
		else if(comboLvlPrimary == 2)  { //level 2 is already the primary, clear secondary
			comboLvlSecondary = 0;
		}
		else if(comboLvlSecondary == 0)  comboLvlSecondary = 2;//some other level is primary, but secondary is open
		
		if(comboLvlPrimary != 2 && comboLvlSecondary != 2)  return;
		
		if((comboLvlPrimary == 2 || comboLvlPrimary != 3))  {
			clearComboBox(l3ComboBox);
			List<QiResponsibleLevel> listOfL3=getModel().findAllLevel3HavingSameLevel2(site, plant, department, new_val.getKey());
			if(listOfL3 != null && !listOfL3.isEmpty())  {
				l3ComboBox.getItems().addAll(getUniqueListOfNames(listOfL3));
				if(l3ComboBox.getItems().size() == 1)  {
					l3ComboBox.getSelectionModel().select(0);
				}
			}
		}
		List<QiResponsibleLevel> listOfL1=getModel().findAllLevel1HavingSameLevel2(site, plant, department, new_val.getKey());
		if((comboLvlPrimary == 2 || comboLvlPrimary != 1))  {
			clearComboBox(l1ComboBox);
			if(listOfL1 != null && !listOfL1.isEmpty())  {
				l1ComboBox.getItems().addAll(getUniqueListOfNames(listOfL1));
				if(l1ComboBox.getItems().size() == 1)  {
					l1ComboBox.getSelectionModel().select(0);
				}
			}
		}
	}
	
	/**
	 * Load responsible level3 combo box.
	 *
	 * @param department the department
	 */
	@SuppressWarnings("unchecked")
	public void loadWhenLevel3Changed(KeyValue<String,Integer> new_val) {
		PdcLocalResponsibilityAssignmentDialog responsibilityPanel = getDialog().getResponsibilityAssignmentDialog();
		String site = responsibilityPanel.getSiteComboBox().getValue().toString();
		String plant = responsibilityPanel.getPlantComboBox().getValue().toString();
		String department = responsibilityPanel.getDepartmentComboBox().getValue().toString();
		LoggedComboBox<KeyValue<String,Integer>> l1ComboBox = responsibilityPanel.getResponsibleLevel1ComboBox();
		LoggedComboBox<KeyValue<String,Integer>> l2ComboBox = responsibilityPanel.getResponsibleLevel2ComboBox();
		
		if(new_val == null)  return;
		if(comboLvlPrimary == 0)  { //primary is open, set level 3 as primary
			comboLvlPrimary = 3;
		}
		else if(comboLvlPrimary == 3)  { //level 3 is already the primary, clear secondary
			comboLvlSecondary = 0;
		}
		else if(comboLvlSecondary == 0)  comboLvlSecondary = 3;//some other level is primary, but secondary is open
		if(comboLvlPrimary != 3 && comboLvlSecondary != 3)  return;
		
		if((comboLvlPrimary == 3 || comboLvlPrimary != 1))  {
			clearComboBox(l1ComboBox);
			List<QiResponsibleLevel> listOfL1=getModel().findAllLevel1HavingSameLevel3(site, plant, department, new_val.getKey());
			if(listOfL1 != null && !listOfL1.isEmpty())  {
				l1ComboBox.getItems().addAll(getUniqueListOfNames(listOfL1));
				if(l1ComboBox.getItems().size() == 1)  {
					l1ComboBox.getSelectionModel().select(0);
				}
			}
		}
		if((comboLvlPrimary == 3 || comboLvlPrimary != 2))  {
			clearComboBox(l2ComboBox);
			List<QiResponsibleLevel> listOfL2=getModel().findAllLevel2HavingSameLevel3(site, plant, department, new_val.getKey());
			if(listOfL2 != null && !listOfL2.isEmpty())  {
				l2ComboBox.getItems().addAll(getUniqueListOfNames(listOfL2));
				if(l2ComboBox.getItems().size() == 1)  {
					l2ComboBox.getSelectionModel().select(0);
				}
			}
		}
	}
	
	/**
	 * Load responsible level2 and level2 combo box based on plant site and dept.
	 *
	 * @param new_val the new_val
	 */
	@SuppressWarnings("unchecked")
	public void loadWhenLevel1Changed(KeyValue<String, Integer> new_val) {
		PdcLocalResponsibilityAssignmentDialog responsibilityPanel = getDialog().getResponsibilityAssignmentDialog();
		String site = responsibilityPanel.getSiteComboBox().getValue().toString();
		String plant = responsibilityPanel.getPlantComboBox().getValue().toString();
		String department = responsibilityPanel.getDepartmentComboBox().getValue().toString();
		LoggedComboBox<KeyValue<String,Integer>> l2ComboBox = responsibilityPanel.getResponsibleLevel2ComboBox();
		LoggedComboBox<KeyValue<String,Integer>> l3ComboBox = responsibilityPanel.getResponsibleLevel3ComboBox();
		
		if(new_val == null)  return;
		if(comboLvlPrimary == 0)  { //primary is open, set level 1 as primary
			comboLvlPrimary = 1;
		}
		else if(comboLvlPrimary == 1)  { //level 1 is already the primary, clear secondary
			comboLvlSecondary = 0;
		}
		else if(comboLvlSecondary == 0)  comboLvlSecondary = 1;//some other level is primary, but secondary is open
		if(comboLvlPrimary != 1 && comboLvlSecondary != 1)  return;
		
		if((comboLvlPrimary == 1 || comboLvlPrimary != 2))  {
			clearComboBox(l2ComboBox);
			List<QiResponsibleLevel> listOfL2=getModel().findAllLevel2HavingSameLevel1(site, plant, department, new_val.getKey());
			if(listOfL2 != null && !listOfL2.isEmpty())  {
				l2ComboBox.getItems().addAll(getUniqueListOfNames(listOfL2));
				if(l2ComboBox.getItems().size() == 1)  {
					l2ComboBox.getSelectionModel().select(0);
				}
			}
		}
		if((comboLvlPrimary == 1 || comboLvlPrimary != 3))  {
			clearComboBox(l3ComboBox);
			List<QiResponsibleLevel> listOfL3 = null;
			if(comboLvlPrimary == 1)  {
				listOfL3 = getModel().findAllLevel3HavingSameLevel1(site, plant, department, new_val.getKey());
			}
			if(listOfL3 != null && !listOfL3.isEmpty())  {
				l3ComboBox.getItems().addAll(getUniqueListOfNames(listOfL3));
				if(l3ComboBox.getItems().size() == 1)  {
					l3ComboBox.getSelectionModel().select(0);
				}
			}
		}
	}
	
	private List<KeyValue<String,Integer>> getUniqueListOfNames(List<QiResponsibleLevel> listOfLevels)  {
		if(listOfLevels == null || listOfLevels.isEmpty())  return null;
		HashMap<String,Integer> levelNamesMap = new HashMap<>();
		for(QiResponsibleLevel thisLevel : listOfLevels)  {
			if(thisLevel == null) continue;
			String key = thisLevel.getResponsibleLevelName();
			if(levelNamesMap.get(key) == null)  {
				levelNamesMap.put(key, 1);
			}
			else  {
				int n = levelNamesMap.get(key);
				levelNamesMap.put(key, n++);				
			}
		}
		ArrayList<KeyValue<String,Integer>> levelNamesList = new ArrayList<>();
		
		for (String key : levelNamesMap.keySet())  {
			KeyValue<String,Integer> keyVal = getKeyValue(key,levelNamesMap.get(key));
			levelNamesList.add(keyVal);
		}
		Collections.sort(levelNamesList,
				new Comparator<KeyValue<String,Integer>>()  {
					public int compare(KeyValue<String,Integer> kv1, KeyValue<String,Integer> kv2)  {
						if(kv1 == null && kv2 == null)  return 0;
						else if(kv1.getKey() == null && kv2.getKey() == null)  return 0;
						else if(kv1 == null || kv1.getKey() == null)  return -1;
						else if(kv2 == null || kv2.getKey() == null)  return 1;
						else return kv1.getKey().compareTo(kv2.getKey());
					}
		});
		return levelNamesList;
	}
	
	/**
	 * Load responsible level2 & 3 combo box on selection of department.
	 *
	 * @param new_val the new_val
	 */
	
	/**
	 * Load responsible level1 combo box.
	 *
	 * @param new_val the new_val
	 */
	@SuppressWarnings("unchecked")
	public void loadRespComboBoxesWithAllValues(String department) {
		
		if(StringUtils.isBlank(department))  return;
		PdcLocalResponsibilityAssignmentDialog responsibilityPanel = getDialog().getResponsibilityAssignmentDialog();
		String site = responsibilityPanel.getSiteComboBox().getValue() != null ? getDialog().getResponsibilityAssignmentDialog().getSiteComboBox().getValue().toString() : "";
		String plant = responsibilityPanel.getPlantComboBox().getValue() != null ? getDialog().getResponsibilityAssignmentDialog().getPlantComboBox().getValue().toString() : "";
		LoggedComboBox<KeyValue<String,Integer>> l1ComboBox = responsibilityPanel.getResponsibleLevel1ComboBox();
		LoggedComboBox<KeyValue<String,Integer>> l2ComboBox = responsibilityPanel.getResponsibleLevel2ComboBox();
		LoggedComboBox<KeyValue<String,Integer>> l3ComboBox = responsibilityPanel.getResponsibleLevel3ComboBox();
		
		clearComboBox(l1ComboBox);
		clearComboBox(l2ComboBox);
		clearComboBox(l3ComboBox);
		
		List<QiResponsibleLevel> responsibleLevel1List = getModel().findAllBySitePlantDepartmentLevel(site, plant, department,(short)1);
		List<KeyValue<String,Integer>> listOfL1 = getUniqueListOfNames(responsibleLevel1List);
		if(listOfL1 != null && !listOfL1.isEmpty())  {
			Collections.sort(listOfL1,getKVComparator());
			l1ComboBox.getItems().addAll(listOfL1);
		}
		l1ComboBox.setPromptText("Select");
		
		List<QiResponsibleLevel> responsibleLevel2List = getModel().findAllBySitePlantDepartmentLevel(site, plant, department,(short)2);
		List<KeyValue<String,Integer>> listOfL2 = getUniqueListOfNames(responsibleLevel2List);
		if(listOfL2 != null && !listOfL2.isEmpty())  {
			Collections.sort(listOfL2,getKVComparator());
			l2ComboBox.getItems().addAll(listOfL2);
		}
		l2ComboBox.setPromptText("Select");
		
		List<QiResponsibleLevel> responsibleLevel3List = getModel().findAllBySitePlantDepartmentLevel(site, plant, department,(short)3);
		List<KeyValue<String,Integer>> listOfL3 = getUniqueListOfNames(responsibleLevel3List);
		if(listOfL3 != null && !listOfL3.isEmpty())  {
			Collections.sort(listOfL3,getKVComparator());
			l3ComboBox.getItems().addAll(listOfL3);
		}
		l3ComboBox.setPromptText("Select");
				
	}

	
	@SuppressWarnings("unchecked")
	public void clearAllResponsibleComboBoxes() {
		
		PdcLocalResponsibilityAssignmentDialog responsibilityPanel = getDialog().getResponsibilityAssignmentDialog();
		LoggedComboBox<KeyValue<String,Integer>> l1ComboBox = responsibilityPanel.getResponsibleLevel1ComboBox();
		LoggedComboBox<KeyValue<String,Integer>> l2ComboBox = responsibilityPanel.getResponsibleLevel2ComboBox();
		LoggedComboBox<KeyValue<String,Integer>> l3ComboBox = responsibilityPanel.getResponsibleLevel3ComboBox();
		
		clearComboBox(l1ComboBox);
		clearComboBox(l2ComboBox);
		clearComboBox(l3ComboBox);
		
	}
	public static Comparator<KeyValue<String,Integer>> getKVComparator()  {
		return new Comparator<KeyValue<String,Integer>>()  {
			public int compare(KeyValue<String,Integer> kv1, KeyValue<String,Integer> kv2)  {
				if(kv1 == null && kv2 == null)  return 0;
				else if(kv1.getKey() == null && kv2.getKey() == null)  return 0;
				else if(kv1 == null || kv1.getKey() == null)  return -1;
				else if(kv2 == null || kv2.getKey() == null)  return 1;
				else return kv1.getKey().compareTo(kv2.getKey());
			}};
	}
	
	/**
	 * Load tracking combo box.
	 */
	@SuppressWarnings("unchecked")
	private void loadLocalThemeComboBox() {
		clearComboBox(getDialog().getRegionalRelatedInfoDialog().getLocalThemeComboBox());
		getDialog().getRegionalRelatedInfoDialog().getLocalThemeComboBox().setPromptText("Select");
		getDialog().getRegionalRelatedInfoDialog().getLocalThemeComboBox().getItems().add(QiConstant.EMPTY);
		List<QiLocalTheme> localThemeList = getModel().findAllActiveTracking();
		for (QiLocalTheme localtheme : localThemeList) {
			getDialog().getRegionalRelatedInfoDialog().getLocalThemeComboBox().getItems().add(localtheme.getLocalTheme());
		}
		//setting blank as default value for Temporary Tracking dropdown
		getDialog().getRegionalRelatedInfoDialog().getLocalThemeComboBox().getSelectionModel().select(getDialog().getRegionalRelatedInfoDialog().getLocalThemeComboBox().getItems().get(0));
	}
	
	/**
	 * Loadintial repair area combo box.
	 */
	@SuppressWarnings("unchecked")
	private void loadintialRepairAreaComboBox() {
		clearComboBox(getDialog().getRepairRelatedDialog().getInitialRepairAreaComboBox());
		getDialog().getRepairRelatedDialog().getInitialRepairAreaComboBox().getItems().add(QiConstant.EMPTY);
		List<QiRepairArea> repairAreaList = getModel().findAllRepairArea();
		for (QiRepairArea repairArea : repairAreaList) {
			String repairAreaValue=repairArea.getRepairAreaName()+(repairArea.getRepairAreaDescription().equalsIgnoreCase(StringUtils.EMPTY)?StringUtils.EMPTY:("-"+repairArea.getRepairAreaDescription()));
			getDialog().getRepairRelatedDialog().getInitialRepairAreaComboBox().getItems().add(repairAreaValue);
		}
	}
	
	/**
	 * Loadintial repair method combo box.
	 */
	@SuppressWarnings("unchecked")
	private void loadintialRepairMethodComboBox() {
		clearComboBox(getDialog().getRepairRelatedDialog().getInitialRepairMethodComboBox());
		getDialog().getRepairRelatedDialog().getInitialRepairMethodComboBox().getItems().add(QiConstant.EMPTY);
		List<QiRepairMethod> repairMethodList = getModel().findAllRepairMethod();
		for (QiRepairMethod repairMethod : repairMethodList) {
			getDialog().getRepairRelatedDialog().getInitialRepairMethodComboBox().getItems().add(repairMethod.getRepairMethod());
		}
	}
	
	/**
	 * Load model year.
	 */
	@SuppressWarnings("unchecked")
	public void loadModelYear() {
		clearComboBox(getDialog().getPdcPddaRelatedInfoDialog().getModelYearComboBox());
		getDialog().getPdcPddaRelatedInfoDialog().getModelYearComboBox().setPromptText("Select");
		getDialog().getPdcPddaRelatedInfoDialog().getModelYearComboBox().getItems().addAll(getModel().findAllModelYearByPlantSiteDeptLine(getDialog().getResponsibilityAssignmentDialog().getPlantComboBox().getSelectionModel().getSelectedItem().toString(),
				getDialog().getResponsibilityAssignmentDialog().getSiteComboBox().getSelectionModel().getSelectedItem().toString(),getDialog().getResponsibilityAssignmentDialog().getDepartmentComboBox().getSelectionModel().getSelectedItem().toString(),
				getDialog().getResponsibilityAssignmentDialog().getResponsibleLevel1ComboBox().getSelectionModel().getSelectedItem().toString()
				));
	}


	/**
	 * Assign button action.
	 *
	 * @param actionEvent the action event
	 */
	private void assignButtonAction(ActionEvent actionEvent) {
		try {
			String responsibilityErrorMessage = isValidateResponsibility();
			if(!responsibilityErrorMessage.equals(QiConstant.EMPTY)) {
				displayErrorMessage("Mandatory field is empty", responsibilityErrorMessage);
				return;
			}
			if(getDialog().getResponsibilityAssignmentDialog().getPddaRelatedInfoCheckBox().isSelected()) {
				String pddaRelatedErrorMessage = validatePDDARelated();
				if(!pddaRelatedErrorMessage.equals("")) {
					displayErrorMessage("Mandatory field is empty", pddaRelatedErrorMessage);
					return;
				}
			}
			List<QiLocalDefectCombination> localDefectCombinations = setLocalAttributeToAssign();
			if(localDefectCombinations.size() > 0)  {
				getModel().assignAttributes(localDefectCombinations);
				localAttributeMaintPanel.getLocalAttributeMaintTablePane().removeSelected();
				localAttributeMaintPanel.getLocalAttributeMaintTablePane().clearSelection();
			}
			Stage stage = (Stage) getDialog().getAssignButton().getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in Assign Attribure method " , "Failed to Assign Attribure ", e);
		}
		
	}
	
	/**
	 * Update button action.
	 *
	 * @param actionEvent the action event
	 */
	private void updateButtonAction(ActionEvent actionEvent) {
		try {
			boolean showErrorDialog = false;
			String responsibilityErrorMessage = isValidateResponsibility();
			if(!responsibilityErrorMessage.equals(QiConstant.EMPTY)) {
				displayErrorMessage("Mandatory field is empty", responsibilityErrorMessage);
				return;
			}
			if(getDialog().getResponsibilityAssignmentDialog().getPddaRelatedInfoCheckBox().isSelected()) {
				String pddaRelatedErrorMessage = validatePDDARelated();
				if(!pddaRelatedErrorMessage.equals(QiConstant.EMPTY)) {
					displayErrorMessage("Mandatory field is empty", pddaRelatedErrorMessage);
					return;
				}
			}
			/** Mandatory Check for Reason for Change */
			if(!isValidReason()){
				displayErrorMessage("Mandatory field is empty", "Please enter Reason for Change");
				return;
			}
			if(!getDialog().getResponsibilityAssignmentDialog().getPddaRelatedInfoCheckBox().isSelected()) {
				for (PdcRegionalAttributeMaintDto partdefect : defectCombinationDtoList) {
					if(null != partdefect.getPddaResponsibilityId() && partdefect.getPddaResponsibilityId() != 0) {
						showErrorDialog = true;
						break;
					}
				}
			}
			if(showErrorDialog && !MessageDialog.confirm(getDialog(), "If PDDA Related Info is unchecked, it will remove PDDA data. Do you wish to continue?")) {
				return;
			}
			List<QiLocalDefectCombination> localDefectCombinations = setLocalAttributeToUpdate();
			if(localDefectCombinations.size() > 0) {
				if (isUpdated(localDefectCombinations)) {
					return;
				}
				getModel().updateAttributes(localDefectCombinations);
			}
			Stage stage = (Stage) getDialog().getUpdateButton().getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in updateButtonAction method " , "Failed to Update Attribute ", e);
		}
	}
	
	/**
	 * Cancel button action.
	 *
	 * @param actionEvent the action event
	 */
	private void cancelButtonAction(ActionEvent actionEvent) {
		try {
			Stage stage = (Stage) getDialog().getCancelButton().getScene().getWindow();
			stage.close();
			getDialog().setCancel(true);
		} catch (Exception e) {
			handleException("An error occured in during cancel action ", "Failed to perform cancel action",e);
		}
	}
	
	/**
	 * Checks if is validate responsibility.
	 *
	 * @return the string
	 */
	private String isValidateResponsibility() {
		String errorMsg = "";
		if(!isValidateSite()){
			errorMsg = "Please Select Site.";
		} else if(!isValidatePlant()){
			errorMsg = "Please Select Plant.";
		} else if(!isValidateDepartment()){
			errorMsg = "Please Select Department.";
		} else if(!isValidateResponsibleLevel1()){
			errorMsg = "Please Select Responsible Level1.";
		} else if(!isValidateResponsibleLevel2()){
			errorMsg = "Please Select Responsible Level2.";
		} else if(!isValidateResponsibleLevel3()){
			errorMsg = "Please Select Responsible Level3.";
		}
		return errorMsg;
	}
	
	/**
	 * Checks if is validate site.
	 *
	 * @return true, if is validate site
	 */
	private boolean isValidateSite() {
		return getDialog().getResponsibilityAssignmentDialog().getSiteComboBox().getValue() == null || 
				getDialog().getResponsibilityAssignmentDialog().getSiteComboBox().getValue().toString().equals(QiConstant.EMPTY) ? false : true;
	}
	
	/**
	 * Checks if is validate plant.
	 *
	 * @return true, if is validate plant
	 */
	private boolean isValidatePlant() {
		return getDialog().getResponsibilityAssignmentDialog().getPlantComboBox().getValue() == null ||
				getDialog().getResponsibilityAssignmentDialog().getPlantComboBox().getValue().toString().equals(QiConstant.EMPTY) ? false : true;
	}
	
	/**
	 * Checks if is validate department.
	 *
	 * @return true, if is validate department
	 */
	private boolean isValidateDepartment() {
		return getDialog().getResponsibilityAssignmentDialog().getDepartmentComboBox().getValue() == null || 
				getDialog().getResponsibilityAssignmentDialog().getDepartmentComboBox().getValue().toString().equals(QiConstant.EMPTY) ? false : true;
	}
	
	/**
	 * Checks if is validate responsible level1.
	 *
	 * @return true, if is validate responsible level1
	 */
	private boolean isValidateResponsibleLevel1() {
		
		PdcLocalResponsibilityAssignmentDialog responsiblePanel = getDialog().getResponsibilityAssignmentDialog();
		LoggedComboBox<KeyValue<String,Integer>> l1ComboBox = responsiblePanel.getResponsibleLevel1ComboBox();		
		boolean r1Pass = l1ComboBox.getSelectionModel().selectedIndexProperty().get() >= 0;		
		return r1Pass;
	}
	
	private boolean isValidateResponsibleLevel2() {
		
		PdcLocalResponsibilityAssignmentDialog responsiblePanel = getDialog().getResponsibilityAssignmentDialog();
		LoggedComboBox<KeyValue<String,Integer>> l2ComboBox = responsiblePanel.getResponsibleLevel2ComboBox();
		
		boolean r2Pass = isListNullOrEmpty(l2ComboBox.getItems()) || l2ComboBox.getSelectionModel().selectedIndexProperty().get() >= 0;		
		return r2Pass;
	}
	
	private boolean isValidateResponsibleLevel3() {
		
		PdcLocalResponsibilityAssignmentDialog responsiblePanel = getDialog().getResponsibilityAssignmentDialog();
		LoggedComboBox<KeyValue<String,Integer>> l3ComboBox = responsiblePanel.getResponsibleLevel3ComboBox();
		
		boolean r3Pass = isListNullOrEmpty(l3ComboBox.getItems()) || l3ComboBox.getSelectionModel().selectedIndexProperty().get() >= 0;		
		return r3Pass;
	}
	
	/**
	 * Validate pdda related.
	 *
	 * @return the string
	 */
	private String validatePDDARelated() {
		String errorMsg = "";
		if(isValidateModelYear()){
			errorMsg = "Please Select Model Year.";
		} else if(isValidateVMC()){
			errorMsg = "Please Select Vehicle Model Code.";
		} else if(isValidateProcessNumber()){
			errorMsg = "Please Select Process Number.";
		} else if(isValidateUnitNumber()){
			errorMsg = "Please Select Unit Number.";
		}
		return errorMsg;
	}
	
	/**
	 * Checks if is validate model year.
	 *
	 * @return true, if is validate model year
	 */
	private boolean isValidateModelYear() {
		return getDialog().getPdcPddaRelatedInfoDialog().getModelYearComboBox().getValue() != null && 
				!getDialog().getPdcPddaRelatedInfoDialog().getModelYearComboBox().getValue().toString().equals(QiConstant.EMPTY) ? false : true;
	}
	
	/**
	 * Checks if is validate vmc.
	 *
	 * @return true, if is validate vmc
	 */
	private boolean isValidateVMC() {
		return getDialog().getPdcPddaRelatedInfoDialog().getVehicleModelCodeComboBox().getValue() != null && 
				!getDialog().getPdcPddaRelatedInfoDialog().getVehicleModelCodeComboBox().getValue().toString().equals(QiConstant.EMPTY) ? false : true;
	}
	
	/**
	 * Checks if is validate process number.
	 *
	 * @return true, if is validate process number
	 */
	private boolean isValidateProcessNumber() {
		return getDialog().getPdcPddaRelatedInfoDialog().getProcessNumberComboBox().getValue() != null && 
				!getDialog().getPdcPddaRelatedInfoDialog().getProcessNumberComboBox().getValue().toString().equals(QiConstant.EMPTY) ? false : true;
	}
	
	/**
	 * Checks if is validate unit number.
	 *
	 * @return true, if is validate unit number
	 */
	private boolean isValidateUnitNumber() {
		return getDialog().getPdcPddaRelatedInfoDialog().getUnitNumberComboBox().getValue() != null && 
				!getDialog().getPdcPddaRelatedInfoDialog().getUnitNumberComboBox().getValue().toString().equals(QiConstant.EMPTY) ? false : true;
	}
	
	/**
	 * Checks if is validate reason.
	 *
	 * @return true, if is validate reason
	 */
	private boolean isValidReason() {
		return !QiCommonUtil.isMandatoryFieldEmpty(getDialog().getReasonForChangeTextArea());
	}
	
	/**
	 * Sets the local attribute entity.
	 *
	 * @param assignOrUpdate the assign or update
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	private List<QiLocalDefectCombination> setLocalAttributeToAssign() {
		List<QiLocalDefectCombination> localDefectCombinations = new ArrayList<QiLocalDefectCombination>();
		for (PdcRegionalAttributeMaintDto partdefect : defectCombinationDtoList) {
			QiLocalDefectCombination ldc = new QiLocalDefectCombination();
			ldc.setRegionalDefectCombinationId(partdefect.getRegionalDefectCombinationId());
			//setting Temporary tracking value as null in database when temporary tracking is selected as blank from Local Attribute Screen while assigning attribute
			ldc.setLocalTheme(getDialog().getRegionalRelatedInfoDialog().getLocalThemeComboBox().getValue().equals(StringUtils.EMPTY)?null: getDialog().getRegionalRelatedInfoDialog().getLocalThemeComboBox().getValue().toString());
			ldc.setEngineFiringFlag(getDialog().getRegionalRelatedInfoDialog().getFireEngineYesRadioBtn().isSelected() ? (short)1 : (short)0);
			updateRepairArea(ldc);
			ldc.setRepairMethod(getDialog().getRepairRelatedDialog().getInitialRepairMethodComboBox().getValue() != null ? 
						getDialog().getRepairRelatedDialog().getInitialRepairMethodComboBox().getValue().toString() : "");
			ldc.setRepairMethodTime(getDialog().getRepairRelatedDialog().getInitialRepairMethodTimeText().getText().equals(QiConstant.EMPTY) ? 
						(short)0 : Short.valueOf(getDialog().getRepairRelatedDialog().getInitialRepairMethodTimeText().getText()));
			ldc.setEstimatedTimeToFix(getDialog().getRepairRelatedDialog().getTotalTimeToFixText().getText().equals(QiConstant.EMPTY) ? 
					(short)0 : Integer.valueOf(getDialog().getRepairRelatedDialog().getTotalTimeToFixText().getText()));
			ldc.setEntrySiteName(localAttributeMaintPanel.getSite().getText());
			ldc.setEntryPlantName(localAttributeMaintPanel.getPlantComboBox().getValue() != null ? localAttributeMaintPanel.getPlantComboBox().getValue().toString() : QiConstant.EMPTY);
			ldc.setReportable(getDefectReportable(partdefect, true));
			ldc.setEntryScreen(localAttributeMaintPanel.getEntryScreenComboBox().getValue() != null ? localAttributeMaintPanel.getEntryScreenComboBox().getValue().toString() : "");
			ldc.setTextEntryMenu(partdefect.getTextEntryMenu());
			ldc.setDefectCategoryName(getDefectCategoryName(partdefect, true));
			ldc.setIsUsed(partdefect.getIsUsed());
			ldc.setEntryModel(localAttributeMaintPanel.getEntryModelComboBox().getValue() != null ? localAttributeMaintPanel.getEntryModelComboBox().getValue().toString() : "");
			QiResponsibleLevel l1 = findResponsibleLevel1();
			if(l1 != null)   {
				ldc.setResponsibleLevelId(l1.getResponsibleLevelId());
			}
			ldc.setPddaResponsibilityId(pddaResponsibility != null ? pddaResponsibility.getPddaResponsibilityId() : null);
			ldc.setCreateUser(getUserId());
			localDefectCombinations.add(ldc);
		}
		return localDefectCombinations;
	}
	
	
	private QiResponsibleLevel findResponsibleLevel1()  {
		
		QiResponsibleLevel l1 = null;
		PdcLocalResponsibilityAssignmentDialog responsibilityPanel = getDialog().getResponsibilityAssignmentDialog();
		LoggedComboBox<String> siteComboBox = responsibilityPanel.getSiteComboBox();
		LoggedComboBox<String> plantComboBox = responsibilityPanel.getPlantComboBox();
		LoggedComboBox<String> departmentComboBox = responsibilityPanel.getDepartmentComboBox();
		LoggedComboBox<KeyValue<String,Integer>> l1ComboBox = responsibilityPanel.getResponsibleLevel1ComboBox();
		LoggedComboBox<KeyValue<String,Integer>> l2ComboBox = responsibilityPanel.getResponsibleLevel2ComboBox();
		LoggedComboBox<KeyValue<String,Integer>> l3ComboBox = responsibilityPanel.getResponsibleLevel3ComboBox();
		
		KeyValue<String,Integer> r1 = (KeyValue<String,Integer>)l1ComboBox.getValue();
		KeyValue<String,Integer> r2 = (KeyValue<String,Integer>)l2ComboBox.getValue();
		KeyValue<String,Integer> r3 = (KeyValue<String,Integer>)l3ComboBox.getValue();
		String site = siteComboBox.getSelectionModel().getSelectedItem().toString();
		String plant = plantComboBox.getSelectionModel().getSelectedItem().toString();
		String dept = departmentComboBox.getSelectionModel().getSelectedItem().toString();
		
		boolean r1Selected = r1 != null && !StringUtils.isBlank(r1.getKey());
		boolean r2Selected = r2 != null && !StringUtils.isBlank(r2.getKey());
		boolean r3Selected = r3 != null && !StringUtils.isBlank(r3.getKey());
		
		if(!r1Selected)  return null;
		else if(!r2Selected && !r3Selected)  {  //only L1
			l1 = getModel().findLevel1BySitePlantDepartmentLevelNameAndLevel(site, plant, dept, r1.getKey());
		}
		else if(r2Selected && !r3Selected)  { //L1 and L2
			l1 = getModel().findBySitePlantDeptLvlNameUpperLvlNameAndLvl(site, plant, dept,  r1.getKey(),  r2.getKey(), 1, 2);
		}
		else if(r2Selected && r3Selected)  { //L2, L2 and L3
			l1 = getModel().findResponsibleLevel1BySitePlantDeptL1L2L3Names(site, plant, dept, r1.getKey(),  r2.getKey(), r3.getKey());
		}
		else if(r1Selected && r3Selected)  { //L1 and L3
			l1 = getModel().findBySitePlantDeptLvlNameUpperLvlNameAndLvl(site, plant, dept,  r1.getKey(),  r3.getKey(), 1, 3);
		}
		
		return l1;
	}
	
	/**
	 * Sets the local attribute entity.
	 *
	 * @param assignOrUpdate the assign or update
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	private List<QiLocalDefectCombination> setLocalAttributeToUpdate() {
		List<QiLocalDefectCombination> localDefectCombinations = new ArrayList<QiLocalDefectCombination>();
		String entryScreen =  this.localAttributeMaintPanel.getEntryScreenComboBox().getValue() != null ? 
						this.localAttributeMaintPanel.getEntryScreenComboBox().getValue().toString() : QiConstant.EMPTY;
		String entryModel = this.localAttributeMaintPanel.getEntryModelComboBox().getValue().toString();
		for (PdcRegionalAttributeMaintDto partdefect : defectCombinationDtoList) {
			QiLocalDefectCombination ldc = new QiLocalDefectCombination();
			QiLocalDefectCombination qiLocalDefectCombinationCloned = null;
			QiLocalDefectCombination localDefectCombination = getModel().findByRegionalDefectCombinationId(partdefect.getRegionalDefectCombinationId(), entryScreen, entryModel, partdefect.getIsUsed());
			qiLocalDefectCombinationCloned = (QiLocalDefectCombination) localDefectCombination.deepCopy();
			ldc.setLocalDefectCombinationId(localDefectCombination != null ? localDefectCombination.getLocalDefectCombinationId() : null);
			ldc.setRegionalDefectCombinationId(partdefect.getRegionalDefectCombinationId());
			//setting Temporary tracking value as null in database when temporary tracking is selected as blank from Local Attribute Screen while Updating attribute
			ldc.setLocalTheme(getDialog().getRegionalRelatedInfoDialog().getLocalThemeComboBox().getValue().equals(StringUtils.EMPTY)? null:getDialog().getRegionalRelatedInfoDialog().getLocalThemeComboBox().getValue().toString());
			ldc.setEngineFiringFlag(getDialog().getRegionalRelatedInfoDialog().getFireEngineYesRadioBtn().isSelected() ? (short)1 : (short)0);
			String defectCategory = getDefectCategoryName(partdefect, false);
			if(!StringUtils.isBlank(defectCategory))  {
				ldc.setDefectCategoryName(defectCategory);
			}
			updateRepairArea(ldc);
			ldc.setRepairMethod(getDialog().getRepairRelatedDialog().getInitialRepairMethodComboBox().getValue() != null ? 
						getDialog().getRepairRelatedDialog().getInitialRepairMethodComboBox().getValue().toString() : QiConstant.EMPTY);
			ldc.setRepairMethodTime(getDialog().getRepairRelatedDialog().getInitialRepairMethodTimeText().getText().equals(QiConstant.EMPTY) ? (short) 0:Short.valueOf(getDialog().getRepairRelatedDialog().getInitialRepairMethodTimeText().getText()));
			ldc.setEstimatedTimeToFix(getDialog().getRepairRelatedDialog().getTotalTimeToFixText().getText().equals(QiConstant.EMPTY)?(short)0:Integer.valueOf(getDialog().getRepairRelatedDialog().getTotalTimeToFixText().getText()));
			ldc.setEntrySiteName(localAttributeMaintPanel.getSite().getText());
			ldc.setEntryPlantName(localAttributeMaintPanel.getPlantComboBox().getValue() != null ? localAttributeMaintPanel.getPlantComboBox().getValue().toString() : QiConstant.EMPTY);
			short val = getDefectReportable(partdefect, false);
			val = (val >= 0) ? val : 0;
			ldc.setReportable(val);
			ldc.setEntryScreen(localAttributeMaintPanel.getEntryScreenComboBox().getValue() != null ? localAttributeMaintPanel.getEntryScreenComboBox().getValue().toString() : "");
			ldc.setTextEntryMenu(partdefect.getTextEntryMenu());
			QiResponsibleLevel l1 = findResponsibleLevel1();
			if(l1 != null)   {
				ldc.setResponsibleLevelId(l1.getResponsibleLevelId());
			}
			if(getDialog().getResponsibilityAssignmentDialog().getPddaRelatedInfoCheckBox().isSelected()) {
				ldc.setPddaResponsibilityId(pddaResponsibility != null ? pddaResponsibility.getPddaResponsibilityId() : null);
			} else {
				ldc.setPddaResponsibilityId(null);
			}
			ldc.setEntryModel(localDefectCombination.getEntryModel());
			String reasonForChange;
			if(!getDialog().getReasonForChangeTextArea().getText().isEmpty()){
				reasonForChange = getDialog().getReasonForChangeTextArea().getText();
			}else{
				reasonForChange = QiConstant.ASSIGN_REASON_REGIONAL_ATTRIBUTE_FOR_AUDIT;
			}
			ldc.setIsUsed(localDefectCombination!= null ? localDefectCombination.getIsUsed() : (short)0);
			ldc.setUpdateUser(getUserId());
			ldc.setCreateUser(localDefectCombination.getCreateUser());
			if(partdefect.getLocalPdcUpdateTimestamp()!=null)
				ldc.setUpdateTimestamp(localDefectCombination != null ? new Timestamp(QiCommonUtil.convert(partdefect.getLocalPdcUpdateTimestamp()).getTime()) : null);
			//call to prepare and insert audit data
			if(qiLocalDefectCombinationCloned != null)
				AuditLoggerUtil.logAuditInfo(qiLocalDefectCombinationCloned, ldc, reasonForChange, getDialog().getScreenName(),
						getModel().getAuditPrimaryKeyValue(ldc.getRegionalDefectCombinationId()),getUserId());
			localDefectCombinations.add(ldc);
		}
		return localDefectCombinations;
	}
	
	public QiResponsibleLevel fetchSelectedResposibleLevel()  {
		String l1Name= "";
		String l2Name = "";
		String l3Name = "";
		PdcLocalResponsibilityAssignmentDialog responsibilityPanel = getDialog().getResponsibilityAssignmentDialog();
		String site = responsibilityPanel.getSiteComboBox().getValue().toString();
		String plant = responsibilityPanel.getPlantComboBox().getValue().toString();
		String dept = responsibilityPanel.getDepartmentComboBox().getValue().toString();
		LoggedComboBox<KeyValue<String,Integer>> l1ComboBox = responsibilityPanel.getResponsibleLevel1ComboBox();
		LoggedComboBox<KeyValue<String,Integer>> l2ComboBox = responsibilityPanel.getResponsibleLevel2ComboBox();
		LoggedComboBox<KeyValue<String,Integer>> l3ComboBox = responsibilityPanel.getResponsibleLevel3ComboBox();
		if(l1ComboBox.getValue() != null)  {
			l1Name = l1ComboBox.getValue().toString();
		}
		if(l2ComboBox.getValue() != null)  {
			l2Name = l2ComboBox.getValue().toString();
		}
		if(l3ComboBox.getValue() != null)  {
			l3Name = l3ComboBox.getValue().toString();
		}

		QiResponsibleLevel qiRespL1 = null;
		if(!StringUtils.isBlank(l1Name) && !StringUtils.isBlank(l2Name) && !StringUtils.isBlank(l3Name))  {
			qiRespL1 = getModel().findResponsibleLevel1BySitePlantDeptL1L2L3Names(site, plant, dept, l1Name,l2Name,l3Name);
		}
		else if(!StringUtils.isBlank(l1Name) && !StringUtils.isBlank(l2Name))  {
			qiRespL1 = getModel().findBySitePlantDeptLvlNameUpperLvlNameAndLvl(site, plant, dept, l1Name,l2Name, 1, 2);
		}
		else if(!StringUtils.isBlank(l1Name) && !StringUtils.isBlank(l3Name))  {
			qiRespL1 = getModel().findBySitePlantDeptLvlNameUpperLvlNameAndLvl(site, plant, dept, l1Name,l3Name, 1, 3);
		}
		else if(!StringUtils.isBlank(l1Name))  {
			qiRespL1 = getModel().findLevel1BySitePlantDepartmentLevelNameAndLevel(site, plant, dept, l1Name);
		}
		return qiRespL1;
	}
	
	/**
	 * Gets the defect category name.
	 *
	 * @return the defect category name
	 */
	private String getDefectCategoryName(PdcRegionalAttributeMaintDto pdcRegionalDto, boolean isAssign) {
		PdcLocalRepairRelatedDialog repairPanel = getDialog().getRepairRelatedDialog();
		String defectCategory = "";
		if(repairPanel.getRealProblemRadioBtn().isSelected())  {
			defectCategory = repairPanel.getRealProblemRadioBtn().getText().toUpperCase();
		}
		else if(repairPanel.getSymptomRadioBtn().isSelected())  {
			defectCategory =  repairPanel.getSymptomRadioBtn().getText().toUpperCase();
		}
		else if(repairPanel.getInformationalRadioBtn().isSelected())  {
			defectCategory = repairPanel.getInformationalRadioBtn().getText().toUpperCase();
		}
		else if(repairPanel.getDefaultRadioBtn().isSelected() || isAssign)  {
			defectCategory = pdcRegionalDto.getRegionalDefectCategory();			
		}
		return defectCategory;
	}
	
	/**
	 * Gets the defect category name.
	 *
	 * @return the defect category name
	 */
	private short getDefectReportable(PdcRegionalAttributeMaintDto pdcRegionalDto, boolean isAssign) {
		PdcRegionalRelatedInfoDialog regionalPanel = getDialog().getRegionalRelatedInfoDialog();
		short reportable = -1;
		if(regionalPanel.getReportableRadioButton().isSelected())  {
			reportable = 0;
		}
		else if(regionalPanel.getNonReportableRadioButton().isSelected())  {
			reportable = 2;
		}
		else if(regionalPanel.getDefaultReportableRadioBtn().isSelected() || isAssign)  {
			reportable = pdcRegionalDto.getRegionalReportable();
		}
		return reportable;
	}
	
	/**
	 * This method is used for validation
	 */
	private void validationForTextfield() {
		addFieldListener(getDialog().getRepairRelatedDialog().getInitialRepairMethodTimeText(), false);
		addFieldListener(getDialog().getRepairRelatedDialog().getTotalTimeToFixText(), false);
		addNumericCheckListener(getDialog().getRepairRelatedDialog().getTotalTimeToFixText());
		setTextFieldListener(getDialog().getRepairRelatedDialog().getInitialRepairMethodTimeText());
		setTextFieldListener(getDialog().getRepairRelatedDialog().getTotalTimeToFixText());
		getDialog().getRepairRelatedDialog().getInitialRepairMethodTimeText().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(5));
		getDialog().getRepairRelatedDialog().getTotalTimeToFixText().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(10));
		getDialog().getReasonForChangeTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));
	}
	
	/**
	 * Clear combo box.
	 *
	 * @param comboBox the combo box
	 */
	@SuppressWarnings({ "unchecked", "rawtypes"})
	private void clearComboBox(LoggedComboBox comboBox ) {
		clearDisplayMessage();
		comboBox.getItems().clear();
		comboBox.setValue(null);
		comboBox.getSelectionModel().clearSelection();
	}
	
	/**
	 * Gets the key value.
	 *
	 * @param key the key
	 * @param value the value
	 * @return the key value
	 */
	public KeyValue<Integer, String> getKeyValue(Integer key, final String value){
		KeyValue<Integer, String> kv = new KeyValue<Integer, String>(key, value){
			private static final long serialVersionUID = 1L;

			@Override
			public String toString() {
				return value;
			}
		};
		return kv;
	}
	
	/**
	 * Gets the key value.
	 *
	 * @param key the key
	 * @param value the value
	 * @return the key value
	 */
	public KeyValue<String, Integer> getKeyValue(final String key, Integer value){
		KeyValue<String, Integer> kv = new KeyValue<String, Integer>(key, value){
			private static final long serialVersionUID = 1L;

			@Override
			public String toString() {
				return key;
			}
		};
		return kv;
	}
	
	public KeyValue<String, Integer> getKeyValue(final String key){
		return getKeyValue(key, 1);
	}
	
	public boolean validationForFireEngine() {
		String productType = localAttributeMaintPanel.getProductTypesComboBox().getValue() == null ? "" : localAttributeMaintPanel.getProductTypesComboBox().getValue().toString().trim();
		if(productType.trim().equalsIgnoreCase(ProductType.ENGINE.getProductName())){
			return false;
		}
		return true;
	}
	
	/**
	 * Update button action on mass update.
	 * @param actionEvent 
	 */
	private void massUpdateAction(ActionEvent actionEvent) {
		try {
			if(!isCheckBoxSelected()){
				displayErrorMessage("Checkbox not selected ", "Please select an item to update");
				return;
			}
			//validation for defect category
			if(getDialog().getRepairRelatedDialog().getDefectCategoryCheckBox().isSelected()){
				if(!isValidateDefectCategory()){
					displayErrorMessage("Mandatory field is empty", "Please select Defect Category");
					return;
				}
			}
			//validation for responsibility
			if(getDialog().getResponsibilityAssignmentCheckBox().isSelected()){
				if(!isValidateMassUpdateResponsibility()){
					return;
				}
			}
			
			/** Mandatory Check for Reason for Change */
			if(!isValidReason()){
				displayErrorMessage("Mandatory field is empty", "Please enter Reason for Change");
				return;
			}
			List<QiLocalDefectCombination> localDefectCombinationList = setLocalAttributeToMassUpdate();
			Boolean isUpdateData = MessageDialog
					.confirm(
							getDialog(),
							"You are performing mass update on "
									+ localDefectCombinationList.size()
									+ " part defect combinations. Do you wish to continue ?");
			if(isUpdateData){
				if (isUpdated(localDefectCombinationList)) {
					return;
				}
				getModel().updateAttributes(localDefectCombinationList);
			}
			Stage stage = (Stage) getDialog().getUpdateButton().getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in updateButtonAction method " , "Failed to Update Attribute ", e);
		}
	}

	/**
	 *This method validate responsibility section on local attribute screen if responsibility checkbox is selected
	 * @return boolean
	 */
	private boolean isValidateMassUpdateResponsibility() {
		boolean isValid= true;
		if(!isValidateSite() && !getDialog().getResponsibilityAssignmentDialog().getPddaRelatedInfoCheckBox().isSelected() ){
			displayErrorMessage("Mandatory field is empty", "Please select an item in responsibility to update ");
			isValid = false;
		}
		if(!isValidateSite()){
			String responsibilityErrorMessage = isValidateResponsibility();
			if(!responsibilityErrorMessage.equals(QiConstant.EMPTY)) {
				displayErrorMessage("Mandatory field is empty", responsibilityErrorMessage);
				isValid= false;
			}
		}
		if(getDialog().getResponsibilityAssignmentDialog().getPddaRelatedInfoCheckBox().isSelected()) {
			String pddaRelatedErrorMessage = validatePDDARelated();
			if(!pddaRelatedErrorMessage.equals(QiConstant.EMPTY)) {
				displayErrorMessage("Mandatory field is empty", pddaRelatedErrorMessage);
				isValid = false;
			}
		}
		return isValid;
	}
	
	/**
	 *This methods sets values for mass update
	 * @return List<QiLocalDefectCombination>
	 */
	private List<QiLocalDefectCombination> setLocalAttributeToMassUpdate() {
		List<QiLocalDefectCombination> localDefectCombinationList = new ArrayList<QiLocalDefectCombination>();
		String entryScreen =  this.localAttributeMaintPanel.getEntryScreenComboBox().getValue() != null ? 
				this.localAttributeMaintPanel.getEntryScreenComboBox().getValue().toString() : QiConstant.EMPTY;
		String entryModel = this.localAttributeMaintPanel.getEntryModelComboBox().getValue().toString();
		for (PdcRegionalAttributeMaintDto partdefect : defectCombinationDtoList) {
			QiLocalDefectCombination qiLocalDefectCombinationCloned = null;
			QiLocalDefectCombination localDefectCombination = getModel().findByRegionalDefectCombinationId(partdefect.getRegionalDefectCombinationId(), entryScreen, entryModel, partdefect.getIsUsed());
			qiLocalDefectCombinationCloned = (QiLocalDefectCombination) localDefectCombination.deepCopy();
			localDefectCombination.setEntrySiteName(localAttributeMaintPanel.getSite().getText());
			localDefectCombination.setEntryPlantName(localAttributeMaintPanel.getPlantComboBox().getValue() != null ?
					localAttributeMaintPanel.getPlantComboBox().getValue().toString() : QiConstant.EMPTY);
			if(getDialog().getRegionalRelatedInfoDialog().getReportableNonReportableCheckbox().isSelected())  {
				short val = getDefectReportable(partdefect, false);
				val = (val >= 0) ? val : 0;
				localDefectCombination.setReportable(val);
			} 
			//setting Temporary tracking value as null in database when temporary tracking is selected as blank from Local Attribute Screen while Updating attribute
			if(getDialog().getRegionalRelatedInfoDialog().getLocalThemeCheckbox().isSelected())
				localDefectCombination.setLocalTheme(getDialog().getRegionalRelatedInfoDialog().getLocalThemeComboBox().getValue().equals(StringUtils.EMPTY)? 
						null:getDialog().getRegionalRelatedInfoDialog().getLocalThemeComboBox().getValue().toString());
			if(getDialog().getRegionalRelatedInfoDialog().getFireEngineCheckbox().isSelected())
				localDefectCombination.setEngineFiringFlag(getDialog().getRegionalRelatedInfoDialog().getFireEngineYesRadioBtn().isSelected() ? (short)1 : (short)0);
			if(getDialog().getRepairRelatedDialog().getDefectCategoryCheckBox().isSelected())  {
				String defectCategory = getDefectCategoryName(partdefect, false);
				if(!StringUtils.isBlank(defectCategory))  {
					localDefectCombination.setDefectCategoryName(defectCategory);
				}
			}
			if(getDialog().getRepairRelatedDialog().getInitialRepairAreaCheckBox().isSelected()){
				updateRepairArea(localDefectCombination);
			}
			if(getDialog().getRepairRelatedDialog().getInitialRepairMethodCheckBox().isSelected()){
				localDefectCombination.setRepairMethod(getDialog().getRepairRelatedDialog().getInitialRepairMethodComboBox().getValue() != null ? 
						getDialog().getRepairRelatedDialog().getInitialRepairMethodComboBox().getValue().toString() : QiConstant.EMPTY);
			}
			if(getDialog().getRepairRelatedDialog().getInitialRepairMethodTimeCheckBox().isSelected())
				localDefectCombination.setRepairMethodTime(getDialog().getRepairRelatedDialog().getInitialRepairMethodTimeText().getText().equals(QiConstant.EMPTY) ?
						(short) 0:Short.valueOf(getDialog().getRepairRelatedDialog().getInitialRepairMethodTimeText().getText()));
			if(getDialog().getRepairRelatedDialog().getTotalTimeToFixCheckBox().isSelected())
				localDefectCombination.setEstimatedTimeToFix(getDialog().getRepairRelatedDialog().getTotalTimeToFixText().getText().equals(QiConstant.EMPTY)?
						(short)0:Integer.valueOf(getDialog().getRepairRelatedDialog().getTotalTimeToFixText().getText()));
			if(getDialog().getResponsibilityAssignmentCheckBox().isSelected()){
				massUpdateResponsibility(localDefectCombination); 
			}
			localDefectCombination.setUpdateUser(getUserId());
			if(partdefect.getLocalPdcUpdateTimestamp()!=null)
				localDefectCombination.setUpdateTimestamp(new Timestamp(QiCommonUtil.convert(partdefect.getLocalPdcUpdateTimestamp()).getTime()));
			//call to prepare and insert audit data
			if(qiLocalDefectCombinationCloned != null)
				AuditLoggerUtil.logAuditInfo(
						qiLocalDefectCombinationCloned,
						localDefectCombination,
						getDialog().getReasonForChangeTextArea().getText(),
						getDialog().getScreenName(),
						getModel().getAuditPrimaryKeyValue(
								localDefectCombination
										.getRegionalDefectCombinationId()),
						getUserId());
			localDefectCombinationList.add(localDefectCombination);
		}
		return localDefectCombinationList;
	}

	private void updateRepairArea(
			QiLocalDefectCombination localDefectCombination) {
		if(getDialog().getRepairRelatedDialog().getInitialRepairAreaComboBox().getValue()!=null){
			if(!getDialog().getRepairRelatedDialog().getInitialRepairAreaComboBox().getValue().toString().equalsIgnoreCase(QiConstant.EMPTY)){
				String[] repairAreaName=StringUtils.split(getDialog().getRepairRelatedDialog().getInitialRepairAreaComboBox().getValue().toString(), "-");
				localDefectCombination.setRepairAreaName(repairAreaName!=null?repairAreaName[0]:QiConstant.EMPTY);
			}else{
				localDefectCombination.setRepairAreaName(StringUtils.EMPTY);
			}
		}else{
			localDefectCombination.setRepairAreaName(QiConstant.EMPTY);
		}
	}

	@SuppressWarnings("unchecked")
	private void massUpdateResponsibility(
			QiLocalDefectCombination ldc) {
		ldc.setEntryScreen(localAttributeMaintPanel.getEntryScreenComboBox().getValue() != null ?
				localAttributeMaintPanel.getEntryScreenComboBox().getValue().toString() : "");
		QiResponsibleLevel l1 = findResponsibleLevel1();
		if(l1 != null)   {
			ldc.setResponsibleLevelId(l1.getResponsibleLevelId());
		}
		boolean isPddaChecked = getDialog().getResponsibilityAssignmentDialog().getPddaRelatedInfoCheckBox().isSelected();
		if(!isPddaChecked || pddaResponsibility == null)  {
			ldc.setPddaResponsibilityId(null);
		}
		else if(isPddaChecked && pddaResponsibility != null) {
			ldc.setPddaResponsibilityId(pddaResponsibility.getPddaResponsibilityId());
		}
	}
	
	
	/**
	 * This method checks if any checkbox is selected or not for mass update
	 * @return boolean
	 */
	private boolean isCheckBoxSelected() {
		if (getDialog().getRegionalRelatedInfoDialog()
				.getReportableNonReportableCheckbox().isSelected()
				|| getDialog().getRegionalRelatedInfoDialog()
						.getLocalThemeCheckbox().isSelected()
				|| getDialog().getRegionalRelatedInfoDialog()
						.getFireEngineCheckbox().isSelected()
				|| getDialog().getRepairRelatedDialog()
						.getDefectCategoryCheckBox().isSelected()
				|| getDialog().getRepairRelatedDialog()
						.getInitialRepairAreaCheckBox().isSelected()
				|| getDialog().getRepairRelatedDialog()
						.getInitialRepairMethodCheckBox().isSelected()
				|| getDialog().getRepairRelatedDialog()
						.getInitialRepairMethodTimeCheckBox().isSelected()
				|| getDialog().getRepairRelatedDialog()
						.getTotalTimeToFixCheckBox().isSelected()
				|| getDialog().getResponsibilityAssignmentCheckBox()
						.isSelected()) {
			return true;	
		}
		return false;
	}
	
	/**
	 * This method validates if defect category is selected or not for mass update
	 * @return boolean
	 */
	private boolean isValidateDefectCategory() {
		if(getDialog().getRepairRelatedDialog().getRealProblemRadioBtn().isSelected()
				|| getDialog().getRepairRelatedDialog().getSymptomRadioBtn().isSelected() 
				|| getDialog().getRepairRelatedDialog().getInformationalRadioBtn().isSelected()
				|| getDialog().getRepairRelatedDialog().getDefaultRadioBtn().isSelected())
			return true;
		else
			return false;
	}
	
	boolean isListNullOrEmpty(List newList)  {
		return newList == null || newList.isEmpty();
	}

	private void historyButtonAction(ActionEvent actionEvent) {
	}
}

