package com.honda.galc.client.teamleader.qi.stationconfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.controller.AbstractQiController;
import com.honda.galc.client.teamleader.qi.stationconfig.department.StationDepartmentPanel;
import com.honda.galc.client.teamleader.qi.stationconfig.dto.ApplicationDetailDto;
import com.honda.galc.client.teamleader.qi.stationconfig.dto.ComboBoxDisplayDto;
import com.honda.galc.client.teamleader.qi.stationconfig.entryscreen.StationEntryScreenPanel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.qi.QiRepairAreaSpaceDao;
import com.honda.galc.dao.qi.QiRepairResultDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.qi.QiEntryDepartmentDto;
import com.honda.galc.dto.qi.QiEntryStationSettingsDto;
import com.honda.galc.dto.qi.QiTerminalDetailDto;
import com.honda.galc.dto.qi.QiWriteUpDepartmentDto;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentPropertyId;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.enumtype.StationConfigurationOperations;
import com.honda.galc.entity.qi.QiBomQicsPartMapping;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qi.QiStationConfigurationId;
import com.honda.galc.entity.qi.QiStationUpcPart;
import com.honda.galc.entity.qi.QiStationUpcPartId;
import com.honda.galc.util.AuditLoggerUtil;
import static com.honda.galc.service.ServiceFactory.getDao;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
/**
 * 
 * <h3>EntryStationConfigController Class description</h3>
 * <p>
 * EntryStationConfigController is used to load data in TableViews and perform the action on Update , Reset and Shift buttonsetc.
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
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
public class EntryStationConfigController extends AbstractQiController<EntryStationConfigModel,EntryStationConfigPanel> implements EventHandler<ActionEvent>{

	private EntryStationPanel entryStationView;
	private StationDepartmentPanel entryDeptAndWriteUpDeptPanel; 
	private StationEntryScreenPanel entryStationDefaultStatusView;
	public EntryStationConfigController(EntryStationConfigModel model,EntryStationConfigPanel view) {
		super(model, view);
	}

	@Override
	public void initEventHandlers() {
		entryStationView = getView().getEntryStationPanel();
		entryDeptAndWriteUpDeptPanel =getView().getEntryDeptAndWriteUpDeptPanel();
		entryStationDefaultStatusView= getView().getEntryStationDefaultStatusPanel();
		addPlantComboBoxListener();
		addDivisionComboBoxListener();
		addQicsStationListener();
		addUPCPartsFilterListener();
		if(isFullAccess()){
			unassignUPCPartsListener();
			assignUPCPartsListener();
		}
		addSettingsTableListener();
		addTabChangeListener();
		addStationRepairAreaComboboxListener();
		addSettingComboBoxListener();
	}

	@SuppressWarnings("unchecked")
	private void addSettingComboBoxListener() {
		getView().getUpdateSettingValueComboBox().getSelectionModel().selectedItemProperty().addListener(updSettingValueCmbBoxListener);
	}
	
	@SuppressWarnings("rawtypes")
	ChangeListener updSettingValueCmbBoxListener = new ChangeListener<String>() {

		public void changed(ObservableValue<? extends String> arg0,
				String arg1, String arg2) {
			if(arg0 != null && arg0.getValue() != null)
				Logger.getLogger().check("updateSettingValueComboBox value :" + arg0.getValue() +" selected");
		}
	};

	/**
	 * This method is used to handle filter and panel switch event  
	 * 
	 */
	private void addUPCPartsFilterListener() {
		getView().getUpcPartFilterTextfield().setOnAction(getView().getController());
	}

	/**
	 * This method is used to handle unassign UPC Parts button event  
	 * 
	 */
	private void unassignUPCPartsListener() {
		getView().getAssignedUPCTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiBomQicsPartMapping>() {
			public void changed(ObservableValue<? extends QiBomQicsPartMapping> arg0,QiBomQicsPartMapping arg1, QiBomQicsPartMapping arg2) {
				if (getView().getAssignedUPCTablePane().getTable().getSelectionModel().getSelectedItem() != null) {
					clearDisplayMessage();
					getView().getLeftShiftUPCPartBtn().setDisable(false);
				}
			}
		});
	}

	public void addTabChangeListener(){

		getView().getTabPane().getSelectionModel().selectedItemProperty().addListener(listener);
	}

	ChangeListener<Tab> listener= new ChangeListener<Tab>() {
		@SuppressWarnings("unchecked")
		@Override
		public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
			
			if(newValue != null && newValue.getId() != null){
				Logger.getLogger().check("Station Config Sub Tab :: " + newValue.getText() + " selected");
			}
			
			if(!StringUtils.isEmpty(getEntryStationView().getQicsStationComboBoxSelectedId())){
				String processPoint=StringUtils.trimToEmpty(getEntryStationView().getQicsStationComboBoxSelectedId());
				if(newValue.getId().equals(getView().getEntryScreenTab().getId())){
					getView().getEntryDeptAndWriteUpDeptPanel().reload();
					getView().getEntryStationDefaultStatusPanel().reload();
					Object productTypeValue = getView().getEntryStationDefaultStatusPanel().getProductTypeComboBox().getValue();
					if(productTypeValue != null && !StringUtils.isEmpty(productTypeValue.toString())){
						getView().getEntryStationDefaultStatusPanel().getController().loadEntryModelComboboxList(StringUtils.trimToEmpty(productTypeValue.toString()));
					}
					getView().getEntryStationDefaultStatusPanel().clearDepartmentComboBox();
					List<QiEntryDepartmentDto> currentlyAssignedEntryDeptList = getView().getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedEntryDeptObjectTablePane().getTable().getItems();
					if(null!=currentlyAssignedEntryDeptList && !currentlyAssignedEntryDeptList.isEmpty()){
						getView().getEntryStationDefaultStatusPanel().getDepartmentComboBox().setItems(FXCollections.observableArrayList(QiCommonUtil.getUniqueArrayList(currentlyAssignedEntryDeptList)));
					}
				}else if(newValue.getId().equals(getView().getSettingTab().getId())){
					setSettingsForSelectedEntryStation();
				}else if(newValue.getId().equals(getView().getPreviousDefectVisible().getId())){
					getView().getEntryDepartmentDefectPanel().reload();
				}else if(newValue.getId().equals(getView().getLimitResponsibilityTab().getId())){
					getView().getLimitResponsibilityPanel().reload();
				}else{
					ComponentPropertyId componentPropertyId = new ComponentPropertyId(StringUtils.trimToEmpty(processPoint),"UPC_STATION");
					ComponentProperty componentProperty = getModel().findComponentPropertyById(componentPropertyId);
					if(null!=componentProperty && componentProperty.getPropertyValue().equalsIgnoreCase("TRUE")){
						getView().getUpcTab().setDisable(false);
						setUPCPartsForSelectedEntryStation();
					}
				}
			}
		}
	};
	
	/**
	 * This method is used to handle UPC Parts button event 
	 * 
	 */
	private void assignUPCPartsListener() {
		getView().getAvilableUPCTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiBomQicsPartMapping>() {
			public void changed(ObservableValue<? extends QiBomQicsPartMapping> observable,QiBomQicsPartMapping oldValue, QiBomQicsPartMapping newValue) {
				if (getView().getAvilableUPCTablePane().getTable().getSelectionModel().getSelectedItem() != null) {
					clearDisplayMessage();
					getView().getRightShiftUPCPartBtn().setDisable(false);
				}
			}
		});
	}

	/**
	 * This method is added because initEventHandlers() is called before the selection listener is triggered.
	 */
	@Override
	public void addContextMenuItems() {

	}

	/**
	 * this method reloads the CloneStationPanel if that tab is selected
	 */
	private void reloadCloneStationPanel()  {
		if(getView().getCloneStationTab().isSelected())  {
			getView().getCloneStationPanel().reload();
		}
	}
	
	/**
	 * This method is used to populate EntryDept, WriteupDept table and EntryDept dropdown
	 */
	ChangeListener<ComboBoxDisplayDto> qicsStationListener = new ChangeListener<ComboBoxDisplayDto>() {
		public void changed(ObservableValue<? extends ComboBoxDisplayDto> arg0,
				ComboBoxDisplayDto arg1, ComboBoxDisplayDto arg2) {
			Logger.getLogger().check("Station Config :: QICS Station " + arg0.getValue() + " selected");
			getView().getEntryStationDefaultStatusPanel().clearDepartmentComboBox();
			getView().getEntryStationDefaultStatusPanel().getDepartmentComboBox().getSelectionModel().clearSelection();
			getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getItems().clear();
			getView().getEntryStationDefaultStatusPanel().getEntryScreenObjectTablePane().getTable().getItems().clear();
			getView().getEntryStationDefaultStatusPanel().clearProcessPointDetails();
			clearUPCTableData();
			clearUPCTableSelection();
			if(arg2!=null){
				ComboBoxDisplayDto newDto = (ComboBoxDisplayDto)arg2;
				clearDisplayMessage();
				if(getView().getEntryScreenTab().isSelected()){
					getView().getEntryDeptAndWriteUpDeptPanel().reload();
					getView().getEntryStationDefaultStatusPanel().reload();
				}
				if(getView().getLimitResponsibilityTab().isSelected())
					getView().getLimitResponsibilityPanel().reload();
				if(getView().getSettingTab().isSelected())
					setSettingsForSelectedEntryStation();
				if(getView().getPreviousDefectVisible().isSelected())
					getView().getEntryDepartmentDefectPanel().reload();
				reloadCloneStationPanel();
				disableUpdateButtons();
				ComponentPropertyId componentPropertyId = new ComponentPropertyId(StringUtils.trimToEmpty(newDto.getId()),"UPC_STATION");
				ComponentProperty componentProperty = getModel().findComponentPropertyById(componentPropertyId);
				
				ComponentPropertyId componentPropertyId2 = new ComponentPropertyId(StringUtils.trimToEmpty(newDto.getId()),"PRODUCT_TYPE");
				ComponentProperty componentProperty2 = getModel().findComponentPropertyById(componentPropertyId2);
				
				if(null!=componentProperty && componentProperty.getPropertyValue().equalsIgnoreCase("TRUE") ||
						null!=componentProperty2 && componentProperty2.getPropertyValue().equalsIgnoreCase(ProductType.MBPN.name())){
					getView().getUpcTab().setDisable(false);
					setUPCPartsForSelectedEntryStation();
				}else
				{	
					if(getView().getUpcTab().isSelected())
						getView().getTabPane().getSelectionModel().select(0);	
					getView().getUpcTab().setDisable(true);	
				}
				Application app = getModel().findAppByProcessPoint(arg2.id);
				ApplicationDetailDto appDto = ApplicationDetailDto.getInstance(app);
				getView().getEntryStationDefaultStatusPanel().setProcessPointDetails(appDto);
				List<QiTerminalDetailDto> terminalDtoList = getModel().findAllTerminalDetailByApplicationId(appDto.getId());
				getView().getEntryStationDefaultStatusPanel().reloadTerminalDetails(terminalDtoList);
			}

			if(isFullAccess()){
				enabledAndDisabledButtons(false);
			}
		}
	};
	
	@SuppressWarnings("unchecked")
	public void addQicsStationListener(){
		getEntryStationView().getQicsStationComboBox().getSelectionModel().selectedItemProperty().addListener(qicsStationListener);
	}
	/**
	 * This method is used to set property values for selected setting .
	 */
	private void addSettingsTableListener() {
		getView().getEntryStationConfigSettingsTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiEntryStationSettingsDto>() {
			public void changed(ObservableValue<? extends QiEntryStationSettingsDto> observableValue, QiEntryStationSettingsDto oldValue,QiEntryStationSettingsDto newValue) {
				if(isFullAccess()){
					getView().getUpdateSettingValueTextField().setDisable(false);
					getView().reloadPropertyValue();

					if(getView().getEntryStationConfigSettingsTablePane().getTable().getSelectionModel().getSelectedItem() !=null && getView().getEntryStationConfigSettingsTablePane().getTable().getSelectionModel().getSelectedItem().getId() == QiEntryStationConfigurationSettings.MAX_UPC_QUANTITY.getId()){ 
						getView().getUpdateSettingValueTextField().setVisible(true);
						getView().getUpdateSettingValueComboBox().setVisible(false);
						getView().getUpdateSettingValueTextField().setText(getView().getEntryStationConfigSettingsTablePane().getTable().getSelectionModel().getSelectedItem().getSettingPropertyValue());
					}else{
						getView().getUpdateSettingValueTextField().setVisible(false);
						getView().getUpdateSettingValueComboBox().setVisible(true);
					}
					if(QiEntryStationConfigurationSettings.getType(getView().getEntryStationConfigSettingsTablePane().getSelectedItem().getId()).isUpdateSettingsPropertyBtnDisabled()) {
						getView().getUpdateSettingsPropertyBtn().setDisable(true);
						getView().getUpdateSettingValueComboBox().setDisable(true);
					}else {
						getView().getUpdateSettingsPropertyBtn().setDisable(false);
						getView().getUpdateSettingValueComboBox().setDisable(false);
					}

					if(getView().getEntryStationConfigSettingsTablePane().getTable().getSelectionModel().getSelectedItem() != null){
						QiEntryStationSettingsDto selectedItem = getView().getEntryStationConfigSettingsTablePane().getTable().getSelectionModel().getSelectedItem();
						Logger.getLogger().check("Entry Station Config :: Setting Name : " + selectedItem.getSettingName() + " selected");								
					}
				}

				clearDisplayMessage();
			}
		});

		getView().getEntryStationConfigSettingsTablePane().getTable().setRowFactory(new Callback<TableView<QiEntryStationSettingsDto>, TableRow<QiEntryStationSettingsDto>>() {
			public TableRow<QiEntryStationSettingsDto> call (TableView<QiEntryStationSettingsDto> tableView) {
				final TableRow<QiEntryStationSettingsDto> row= new TableRow<QiEntryStationSettingsDto>();
				row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						final int index= row.getIndex();
						if(index >= 0 && index < getView().getEntryStationConfigSettingsTablePane().getTable().getItems().size() && getView().getEntryStationConfigSettingsTablePane().getTable().getSelectionModel().isSelected(index) && event.isControlDown()) {
							getView().getEntryStationConfigSettingsTablePane().getTable().getSelectionModel().clearSelection(index);
							clearDisplayMessage();
							disableUpdateButtons();
							event.consume();
						}
					}
				});
				return row;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private void addStationRepairAreaComboboxListener() {
		getView().getStationRepairAreaComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				clearDisplayMessage();
				if(isFullAccess()) {
					getView().getSaveStationRepairAreaBtn().setDisable(false);
				}
			}
		});
	}
	/**
	 * This method is used to set buttons .
	 */	
	private void disableUpdateButtons() {
		getEntryDeptAndWriteUpDeptPanel().getUpdateEntryDepartmentBtn().setDisable(true);
		getEntryDeptAndWriteUpDeptPanel().getUpdateWriteUpDepartmentBtn().setDisable(true);
		getView().getEntryStationDefaultStatusPanel().getUpdateEntryScreenBtn().setDisable(true);
		getEntryDeptAndWriteUpDeptPanel().getResetEntryDepartmentBtn().setDisable(true);
		getEntryDeptAndWriteUpDeptPanel().getResetWriteUpDepartmentBtn().setDisable(true);
		getView().getEntryStationDefaultStatusPanel().getResetEntryScreenBtn().setDisable(true);
		getView().getUpdateSettingValueComboBox().setDisable(true);
		getView().getUpdateSettingsPropertyBtn().setDisable(true);
		getView().getUpdateSettingValueTextField().setDisable(true);
		disableUPCOperationButton(true);
		disableUPCShiftButton(true);
		getEntryStationDefaultStatusView().getUpdateDefaultStatusBtn().setDisable(true);
		getEntryStationDefaultStatusView().getRepairedChkBox().setDisable(true);
		getEntryStationDefaultStatusView().getNotRepairedChkBox().setDisable(true);
		getEntryStationDefaultStatusView().getNonRepairableChkBox().setDisable(true);
		getEntryStationDefaultStatusView().getNoneRadioBtn().setDisable(true);
		getEntryStationDefaultStatusView().getRepairedRadioBtn().setDisable(true);
		getEntryStationDefaultStatusView().getNotRepairedRadioBtn().setDisable(true);
		getEntryStationDefaultStatusView().getNonRepairableRadioBtn().setDisable(true);
		getView().getUpcPartFilterTextfield().clear();
		getView().getEntryDepartmentDefectPanel().getUpdateEntryDepartmenDefectStatustBtn().setDisable(true);
		getView().getEntryDepartmentDefectPanel().getResetEntryDepartmenDefectStatustBtn().setDisable(true);
	}
	/**
	 * This method is event listener for plantComboBox
	 */

	ChangeListener<String> plantComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
			getEntryStationView().clearDivisionComboBox();;
			loadDivisionComboBox(new_val);
			clearEntryDeptWriteUpDept();
			getView().getEntryStationConfigSettingsTablePane().getTable().getItems().clear();
			clearItemsForLimitRespPanel();
			clearUPCTableData();
			disableUpdateButtons();
			getView().getEntryStationDefaultStatusPanel().getController().disableEntryStationDefaultStatusPanel(true);
			Logger.getLogger().check("Station Config :: Entry Station Plant " + ov.getValue() + " selected");
			reloadCloneStationPanel();
		}
	};
	
	/**
	 * This method is used to clear Limit Responsibility Panel Items
	 */
	private void clearItemsForLimitRespPanel() {
		getView().getLimitResponsibilityPanel().getAvailableResponsibilitySiteTablePane().getTable().getItems().clear();
		getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilitySiteTablePane().getTable().getItems().clear();
		getView().getLimitResponsibilityPanel().getAvailableResponsibilityPlantTablePane().getTable().getItems().clear();
		getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityPlantTablePane().getTable().getItems().clear();
		getView().getLimitResponsibilityPanel().getAvailableResponsibilityDepartmentTablePane().getTable().getItems().clear();
		getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityDepartmentTablePane().getTable().getItems().clear();
		getView().getLimitResponsibilityPanel().getAvailableResponsibilityLevel1TablePane().getTable().getItems().clear();
		getView().getLimitResponsibilityPanel().getCurrentlyAssignedResponsibilityLevel1TablePane().getTable().getItems().clear();
		
	}
	
	@SuppressWarnings("unchecked")
	private void addPlantComboBoxListener() {
		getEntryStationView().getPlantComboBox().getSelectionModel().selectedItemProperty()
		.addListener(plantComboBoxChangeListener);
	}

	/**
	 * This method is event listener for divisionComboBox
	 */
	ChangeListener<ComboBoxDisplayDto> divisionComboBoxChangeListener = new ChangeListener<ComboBoxDisplayDto>() {
		public void changed(ObservableValue<? extends ComboBoxDisplayDto> ov,  ComboBoxDisplayDto old_val, ComboBoxDisplayDto new_val) {
			getEntryStationView().clearQicsStationComboBox();
			if(new_val != null)  {
				loadQicsStationListView(new_val.id);
			}
			clearEntryDeptWriteUpDept();
			getView().getEntryStationConfigSettingsTablePane().getTable().getItems().clear();
			clearUPCTableData();
			disableUpdateButtons();
			getView().getEntryStationDefaultStatusPanel().getController().disableEntryStationDefaultStatusPanel(true);
			clearItemsForLimitRespPanel();
			Logger.getLogger().check("Station Config :: Entry Station Division " + ov.getValue() + " selected");
			reloadCloneStationPanel();
		} 
	};
	
	@SuppressWarnings("unchecked")
	private void addDivisionComboBoxListener(){
		getEntryStationView().getDivisionComboBox().getSelectionModel().selectedItemProperty().addListener(divisionComboBoxChangeListener);
	}

	/**
	 * This method is used to load Division ComboBox based on Plant
	 * @param text
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void loadDivisionComboBox(String plantName) {
		String siteName = getEntryStationView().getSiteValueLabel().getText();
		List<String> divisionList= new ArrayList<String>();
		List<ComboBoxDisplayDto> dtoList = new ArrayList<ComboBoxDisplayDto>();
		for (Division divisionObj : getModel().findAllDivisionBySiteAndPlant(siteName, plantName)) {
			divisionList.add(divisionObj.getDivisionId());
			dtoList.add(ComboBoxDisplayDto.getInstance(divisionObj));
		}
		Collections.sort(dtoList);
		getEntryStationView().getDivisionComboBox().setItems(FXCollections.observableArrayList(dtoList));
	}	

	/**
	 * This method is used to load Qics station list view based on division 
	 * @param divisionId
	 */
	@SuppressWarnings("unchecked")
	private void loadQicsStationListView(String divisionId){
		List<ProcessPoint> qicsStationList = getModel().findAllQicsStationByApplicationComponentDivision(divisionId);
		List<String> processPoints= new ArrayList<String>();
		List<ComboBoxDisplayDto> dtoList = new ArrayList<ComboBoxDisplayDto>();
		if(qicsStationList.size()==0){
			getEntryStationView().clearQicsStationComboBox();
			getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedEntryDeptObjectTablePane().getTable().getItems().clear();
			getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedWriteUpDepartObjectTablePane().getTable().getItems().clear();
			getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getItems().clear();
		}else{
			for (ProcessPoint processPoint : qicsStationList) {
				processPoints.add(processPoint.getProcessPointId());
				dtoList.add(ComboBoxDisplayDto.getInstance(processPoint));
			}
			Collections.sort(dtoList);
			getEntryStationView().getQicsStationComboBox().setItems(FXCollections.observableArrayList(dtoList));
		}
		enabledAndDisabledButtons(true);
	}	

	/**
	 * This method is used to perform action on 'Update', 'Reset' ,'shift' buttons for EntryDept , WriteupDept and EntryScreen table view
	 */
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			switch(StationConfigurationOperations.getType(loggedButton.getId())){

			case RIGHT_SHIFT_UPC_PART_BUTTON :
				rightShiftUPCPart(actionEvent);
				break;

			case LEFT_SHIFT_UPC_PART_BUTTON :
				leftShiftUPCPart(actionEvent);
				break;

			case UPDATE_UPC_PART_BUTTON :
				updateUPCPart(actionEvent);
				break; 

			case RESET_UPC_PART_BUTTON :
				resetUPCPart(actionEvent);
				break; 

			case UPDATE_SETTINGS_PROPERTY :
				updateEntryStationSettingsProperty(actionEvent);
				break; 
				
			case SAVE_STATION_REPAIR_AREA :
				saveStationRepairArea();
				break; 

			default:
				break;
			}
		}
		actionPerformedByComponent(actionEvent);
	}

	/**
	 * This method is used to perform action on MenuItem , RadioButton and LoggedTextField components
	 * @param actionEvent
	 */
	private void actionPerformedByComponent(ActionEvent actionEvent){
		if (actionEvent.getSource() instanceof LoggedTextField) {
			if(( (LoggedTextField) actionEvent.getSource()).getId().equals(
					getView().getUpcPartFilterTextfield().getId())){
				if (getView().getUpcPartFilterTextfield().isFocused()){
					final String processPointId=StringUtils.trimToEmpty((String)getEntryStationView().getQicsStationComboBoxSelectedId());
					if(!StringUtils.isEmpty(processPointId))
						filterAvailableUPCParts();
				}
			}
		}
	}

	/**
	 * this method is use to filter on available UPC Parts
	 */
	private void filterAvailableUPCParts() {
		List<QiBomQicsPartMapping> currentAssignedUPCPartsList= new ArrayList<QiBomQicsPartMapping> ();
		currentAssignedUPCPartsList.addAll(getView().getAssignedUPCTablePane().getTable().getItems()) ;
		loadUPCParts();
		List<QiBomQicsPartMapping> existingList = new ArrayList<QiBomQicsPartMapping>();
		existingList.addAll(getView().getAvilableUPCTablePane().getTable().getItems());
		if(currentAssignedUPCPartsList.size()>0) {
			getView().getAssignedUPCTablePane().getTable().getItems().clear();
			getView().getAssignedUPCTablePane().getTable().getItems().addAll(currentAssignedUPCPartsList);
			for(QiBomQicsPartMapping currentBomQicsPartMapping:currentAssignedUPCPartsList){
				for(QiBomQicsPartMapping bomQicsPartMapping:existingList){
					if(StringUtils.trimToEmpty(currentBomQicsPartMapping.getMainPartNo()).equals(StringUtils.trimToEmpty(bomQicsPartMapping.getMainPartNo()))) {
						getView().getAvilableUPCTablePane().getTable().getItems().remove(bomQicsPartMapping);
					}
				}
			}
			currentAssignedUPCPartsList.clear();
		}
		String filterKey = StringUtils.trimToEmpty(getView().getUpcPartFilterTextfield().getText());
		List<QiBomQicsPartMapping> newList=new ArrayList<QiBomQicsPartMapping>();
		if (filterKey.equals(StringUtils.EMPTY)){
			return;
		}else {
			existingList.clear();
			existingList.addAll(getView().getAvilableUPCTablePane().getTable().getItems());
			for(QiBomQicsPartMapping bomQicsPartMapping:existingList){
				if(StringUtils.trimToEmpty(bomQicsPartMapping.getMainPartNo()).contains(filterKey) ||
						StringUtils.trimToEmpty(bomQicsPartMapping.getInspectionPartName()).contains(filterKey))
					newList.add(bomQicsPartMapping);
			}
			getView().getAvilableUPCTablePane().getTable().getItems().clear();
			getView().getAvilableUPCTablePane().getTable().getItems().addAll(newList);
		}
	}

	/**
	 * This method is used to handle UPC part reset button event
	 */
	private void resetUPCPart(ActionEvent actionEvent) {
		getView().getUpcPartFilterTextfield().clear();
		disableUPCOperationButton(true);
		disableUPCShiftButton(true);
		clearDisplayMessage();
		loadUPCParts();
	}

	/**
	 * This method is used to handle unassign UPC Part button event
	 */
	private void leftShiftUPCPart(ActionEvent actionEvent) {
		List<QiBomQicsPartMapping> unassignedList=getView().getAssignedUPCTablePane().getSelectedItems();
		for(QiBomQicsPartMapping bomQicsPartMapping:unassignedList) {
			if(!StringUtils.isEmpty(bomQicsPartMapping.getMainPartNo()) &&
					!StringUtils.isEmpty(bomQicsPartMapping.getInspectionPartName()))
				getView().getAvilableUPCTablePane().getTable().getItems().addAll(bomQicsPartMapping);
		}
		getView().getAssignedUPCTablePane().getTable().getItems().removeAll(unassignedList);
		disableUPCOperationButton(false);
	}

	/**
	 * this method is use to enable reset and update button
	 * @param disable 
	 */
	private void disableUPCOperationButton(boolean disable) {
		getView().getUpdateUPCPartBtn().setDisable(disable); 
		getView().getResetUPCPartBtn().setDisable(disable);
	}

	/**
	 * This method is used to handle assign UPC Part button event
	 */
	private void rightShiftUPCPart(ActionEvent actionEvent) {
		List<QiBomQicsPartMapping> assignedList=getView().getAvilableUPCTablePane().getSelectedItems();
		for(QiBomQicsPartMapping bomQicsPartMapping:assignedList) {
			if(!StringUtils.isEmpty(bomQicsPartMapping.getMainPartNo()) &&
					!StringUtils.isEmpty(bomQicsPartMapping.getInspectionPartName()))
				getView().getAssignedUPCTablePane().getTable().getItems().addAll(bomQicsPartMapping);
		}	
		getView().getAvilableUPCTablePane().getTable().getItems().removeAll(assignedList);
		disableUPCOperationButton(false);
	}

	/**
	 * This method used to disable UPC Shift Button
	 */
	private void disableUPCShiftButton(boolean disableValue) {
		getView().getLeftShiftUPCPartBtn().setDisable(disableValue);
		getView().getRightShiftUPCPartBtn().setDisable(disableValue);
	}

	/**
	 *  This method is used to clear selection on UPC table view
	 */
	private void clearUPCTableSelection() {
		getView().getAvilableUPCTablePane().getTable().getSelectionModel().clearSelection();
		getView().getAssignedUPCTablePane().getTable().getSelectionModel().clearSelection();
	}

	/**
	 * This method is used to handle update UPC Part button event
	 */
	private void updateUPCPart(ActionEvent actionEvent) {
		final String processPointId=StringUtils.trimToEmpty((String)getEntryStationView().getQicsStationComboBoxSelectedId());
		if (!StringUtils.isEmpty(processPointId)){
			try {
				List<QiStationUpcPart> removedUPCPartsList = new ArrayList<QiStationUpcPart>();
				List<QiBomQicsPartMapping> addedUPCPartsList = new ArrayList<QiBomQicsPartMapping>();
				List<QiStationUpcPart> assignedUPCPartsList = getModel()
						.findAllStationUpcPartByProcessPointId(processPointId);
				List<QiBomQicsPartMapping> currentAssignedUPCPartsList = getView().getAssignedUPCTablePane().getTable().getItems();
				if (assignedUPCPartsList.size() == 0 && currentAssignedUPCPartsList.size() > 0) {
					createStationUpcPart(currentAssignedUPCPartsList, processPointId);
				}
				if (assignedUPCPartsList.size() > 0 && currentAssignedUPCPartsList.size() == 0) {
					removeStationUpcPart(assignedUPCPartsList, processPointId);
				}
				updateOnBomQicsPartAndUPCPartStation(processPointId, removedUPCPartsList, addedUPCPartsList,
						assignedUPCPartsList, currentAssignedUPCPartsList);
			} catch (Exception e) {
				handleException("An error occurred while updatinging UPC Parts", "Failed to get update UPC Parts in Entry Screen", e);
			} finally {
				loadUPCParts();
				if (!StringUtils.isEmpty(getView().getUpcPartFilterTextfield().getText()))
					filterAvailableUPCParts();
				disableUPCShiftButton(true);
				disableUPCOperationButton(true);
			}
		}
	}

	/**
	 * This method is used to update on QicsBomPart and UPCPartStation
	 *
	 * @param processPointId
	 * @param removedUPCPartsList
	 * @param addedUPCPartsList
	 * @param assignedUPCPartsList
	 * @param currentAssignedUPCPartsList
	 */
	private void updateOnBomQicsPartAndUPCPartStation(final String processPointId,
			List<QiStationUpcPart> removedUPCPartsList, List<QiBomQicsPartMapping> addedUPCPartsList,
			List<QiStationUpcPart> assignedUPCPartsList, List<QiBomQicsPartMapping> currentAssignedUPCPartsList) {
		if (assignedUPCPartsList.size()>0 && currentAssignedUPCPartsList.size()>0){
			for(QiStationUpcPart assignedStationUpcPart:assignedUPCPartsList){
				boolean isAssignedStationDeleted = true;
				for(QiBomQicsPartMapping currentBomQicsPartMapping:currentAssignedUPCPartsList){
					if (StringUtils.trimToEmpty(assignedStationUpcPart.getId().getMainPartNo()).equals(StringUtils.trimToEmpty(currentBomQicsPartMapping.getMainPartNo())))
						isAssignedStationDeleted=false;
				}
				if(isAssignedStationDeleted)
					removedUPCPartsList.add(assignedStationUpcPart);
			}
			for(QiBomQicsPartMapping currentBomQicsPartMapping:currentAssignedUPCPartsList){
				boolean isBomQicsPartAdded = true;
				for(QiStationUpcPart assignedStationUpcPart:assignedUPCPartsList){ 
					if (StringUtils.trimToEmpty(currentBomQicsPartMapping.getMainPartNo()).equals(StringUtils.trimToEmpty(assignedStationUpcPart.getId().getMainPartNo())))
						isBomQicsPartAdded=false;
				}
				if(isBomQicsPartAdded)
					addedUPCPartsList.add(currentBomQicsPartMapping);
			}
			if(addedUPCPartsList.size()>0)
				createStationUpcPart(addedUPCPartsList,processPointId);
			if(removedUPCPartsList.size()>0){
				removeStationUpcPart(removedUPCPartsList, processPointId);
			}
		}
	}

	/**
	 * This method is used to assign UPC Part to Process PointId
	 */
	private void createStationUpcPart(final List<QiBomQicsPartMapping> currentAssignedUPCPartsList, final String processPointId) {
		final List<QiStationUpcPart> newUPCPartsList = new ArrayList<QiStationUpcPart>();
		QiStationUpcPart newUPCParts=null;
		for(QiBomQicsPartMapping bomQicsPartMapping:currentAssignedUPCPartsList){
			newUPCParts=new QiStationUpcPart();
			newUPCParts.setId(new QiStationUpcPartId());
			newUPCParts.getId().setMainPartNo(StringUtils.trimToEmpty(bomQicsPartMapping.getMainPartNo()));
			newUPCParts.getId().setProcessPointId(processPointId);
			newUPCParts.setCreateUser(StringUtils.trimToEmpty(getView().getMainWindow().getApplicationContext().getUserId()).toUpperCase());
			newUPCPartsList.add(newUPCParts);
		}
		getModel().createStationUpcPart(newUPCPartsList);
	}

	/**
	 * This method is used to unassign UPC Part to Process PointId
	 */
	private void removeStationUpcPart(final List<QiStationUpcPart> stationUpcPartList, final String processPointId) {
		final ReasonForChangeDialog qiUserDailog=new ReasonForChangeDialog(getApplicationId());
		if (MessageDialog.confirm(getView().getStage(), "Are you sure to update the UPC part details?")) {
			if (qiUserDailog.showReasonForChangeDialog(null)) {
				for (QiStationUpcPart removedUPCParts : stationUpcPartList) 
					AuditLoggerUtil.logAuditInfo(removedUPCParts, null, "UPC Part has been deleted from "+processPointId+" associated.", getView().getScreenName(),getUserId());
				getModel().deleteStationUpcPart(stationUpcPartList);
			}
		}
	}


	/**
	 * This method is used to set the value of primary key
	 * @param processPointId
	 * @param propertyKey
	 * @return
	 */
	private QiStationConfigurationId findbyId(String processPointId,String propertyKey){
		QiStationConfigurationId qiStationConfigurationId = new QiStationConfigurationId();
		qiStationConfigurationId.setProcessPointId(processPointId);
		qiStationConfigurationId.setPropertyKey(propertyKey);
		return qiStationConfigurationId;
	}
	/**
	 * This method is used to perform disabling and enabling buttons
	 * @param enabledAndDisabled
	 */
	private void enabledAndDisabledButtons(boolean enabledAndDisabled){
		getEntryDeptAndWriteUpDeptPanel().getRightShiftDepartmentBtn().setDisable(enabledAndDisabled);
		getEntryDeptAndWriteUpDeptPanel().getLeftShiftDepartmentBtn().setDisable(enabledAndDisabled);
		getEntryDeptAndWriteUpDeptPanel().getRightShiftWriteUpDepartmentBtn().setDisable(enabledAndDisabled);
		getEntryDeptAndWriteUpDeptPanel().getLeftShiftWriteUpDepartmentBtn().setDisable(enabledAndDisabled);
		getView().getEntryStationDefaultStatusPanel().getRightShiftEntryScreenBtn().setDisable(enabledAndDisabled);
		getView().getEntryStationDefaultStatusPanel().getLeftShiftEntryScreenBtn().setDisable(enabledAndDisabled);
		
		if (enabledAndDisabled) getEntryStationDefaultStatusView().getController().disableEntryStationDefaultStatusPanel(true);
		else getEntryStationDefaultStatusView().getController().setEntryStationStatus();
	}
	
	/**
	 * This method is used to prepare previous entity for audit .
	 */
	private QiStationConfiguration cloneAuditData(QiStationConfiguration entryStationConfigManagement) {
		QiStationConfiguration qiEntryStationConfigManagementCloned = new QiStationConfiguration();
		QiStationConfigurationId qiEntryStationConfigManagementIdCloned = new QiStationConfigurationId();
		qiEntryStationConfigManagementIdCloned.setProcessPointId(StringUtils.trimToEmpty(entryStationConfigManagement.getId().getProcessPointId()));
		qiEntryStationConfigManagementIdCloned.setPropertyKey(StringUtils.trimToEmpty(entryStationConfigManagement.getId().getPropertyKey()));
		qiEntryStationConfigManagementCloned.setId(qiEntryStationConfigManagementIdCloned);
		qiEntryStationConfigManagementCloned.setPropertyValue(StringUtils.trimToEmpty(entryStationConfigManagement.getPropertyValue()));
		qiEntryStationConfigManagementCloned.setActive(entryStationConfigManagement.getActive());
		return qiEntryStationConfigManagementCloned;
	}


	/**
	 * This method is used to update selected setting's property .
	 */
	private void updateEntryStationSettingsProperty(ActionEvent actionEvent) {
		boolean isMaxUpcSetting = getView().getEntryStationConfigSettingsTablePane().getTable().getSelectionModel().getSelectedItem().getId()==QiEntryStationConfigurationSettings.MAX_UPC_QUANTITY.getId();
		QiStationConfiguration qiEntryStationConfigManagement = new QiStationConfiguration();
		QiStationConfigurationId qiEntryStationConfigManagementid = new QiStationConfigurationId();
		qiEntryStationConfigManagementid.setProcessPointId(StringUtils.trimToEmpty((String)getEntryStationView().getQicsStationComboBoxSelectedId()));
		qiEntryStationConfigManagementid.setPropertyKey(StringUtils.trimToEmpty(getView().getEntryStationConfigSettingsTablePane().getSelectedItem().getSettingName()));
		qiEntryStationConfigManagement.setId(qiEntryStationConfigManagementid);
		qiEntryStationConfigManagement.setActive((short)1);
		if(QiEntryStationConfigurationSettings.getType(getView().getEntryStationConfigSettingsTablePane().getSelectedItem().getId()).isReportableSetting())
			qiEntryStationConfigManagement.setPropertyValue(StringUtils.trimToEmpty((String)getView().getUpdateSettingValueComboBox().getSelectionModel().getSelectedItem()).equalsIgnoreCase("Yes")?"0":"3");
		else {
			if(isMaxUpcSetting){
				if(validUpcMaxValue(getView().getUpdateSettingValueTextField().getText())){
					qiEntryStationConfigManagement.setPropertyValue(StringUtils.trimToEmpty(getView().getUpdateSettingValueTextField().getText()));
				}else{
					return;
				}
			} else{
				qiEntryStationConfigManagement.setPropertyValue(StringUtils.trimToEmpty((String)getView().getUpdateSettingValueComboBox().getSelectionModel().getSelectedItem()));
			}
		}
		qiEntryStationConfigManagement.setUpdateUser(getUserId());
		//call to prepare and insert audit data
		QiStationConfiguration entryStationConfigManagement= getModel().findEntryStationById(findbyId(StringUtils.trimToEmpty((String)getEntryStationView().getQicsStationComboBoxSelectedId()), StringUtils.trimToEmpty(getView().getEntryStationConfigSettingsTablePane().getSelectedItem().getSettingName())));
		getModel().updateEntryStation(qiEntryStationConfigManagement);
		setSettingsForSelectedEntryStation();
		if(isMaxUpcSetting) {
			getView().getUpdateSettingValueTextField().setDisable(true);
			getView().getUpdateSettingValueTextField().setVisible(true);
			getView().getUpdateSettingValueComboBox().setVisible(false);
		} else {
			getView().getUpdateSettingValueComboBox().setDisable(true);
			getView().getUpdateSettingValueTextField().setVisible(false);
		}
		getView().getUpdateSettingsPropertyBtn().setDisable(true);
		if(null!=entryStationConfigManagement){	
			AuditLoggerUtil.logAuditInfo(cloneAuditData(entryStationConfigManagement), qiEntryStationConfigManagement, QiConstant.SAVE_REASON_FOR_ENTRY_STATION_COFIGURATION_AUDIT,getView().getScreenName(),getUserId());
		}
	}
	


	private boolean validUpcMaxValue(String text) {
		String upcMaxValue = getView().getUpdateSettingValueTextField().getText();
		if(!StringUtils.isNumeric(upcMaxValue)){
			displayErrorMessage("Please enter numeric values only");
			return false;
		}else if(Integer.parseInt(upcMaxValue) <= QiConstant.MIN_UPC_COUNT){
			displayErrorMessage("Please enter value greater than "+QiConstant.MIN_UPC_COUNT);
			return false;
		}else if(Integer.parseInt(upcMaxValue) > QiConstant.MAX_UPC_COUNT){
			displayErrorMessage("UPC max value cannot be greater than "+QiConstant.MAX_UPC_COUNT);
			return false;
		}
		return true;
	}

	/**
	 * This method is used to set property values of settings for selected Entry Station .
	 */
	public void setSettingsForSelectedEntryStation() {
		if(null!=getEntryStationView().getQicsStationComboBox().getSelectionModel().getSelectedItem()){
			List<QiStationConfiguration> qiEntryStationConfigurationList= getModel().findAllByProcessPoint(StringUtils.trimToEmpty((String)getEntryStationView().getQicsStationComboBoxSelectedId()));
			List<QiEntryStationSettingsDto> qiEntryStationSettingsDtoList = new ArrayList<QiEntryStationSettingsDto>();
			for(QiEntryStationConfigurationSettings qiEntryStationConfigurationSetting: QiEntryStationConfigurationSettings.values()){
				QiEntryStationSettingsDto qiEntryStationSettingsDto= new QiEntryStationSettingsDto(qiEntryStationConfigurationSetting); 
				qiEntryStationSettingsDtoList.add(qiEntryStationSettingsDto);
			}
			if(!qiEntryStationConfigurationList.isEmpty()){
				for (QiStationConfiguration qiEntryStationConfigManagement : qiEntryStationConfigurationList){
					for (QiEntryStationSettingsDto qiEntryStationSettingsDto :qiEntryStationSettingsDtoList){
						if(qiEntryStationConfigManagement.getId().getPropertyKey().equalsIgnoreCase(qiEntryStationSettingsDto.getSettingName())){
							if(QiEntryStationConfigurationSettings.getType(qiEntryStationSettingsDto.getId()).isReportableSetting()){
								qiEntryStationSettingsDto.setSettingPropertyValue(StringUtils.trimToEmpty(qiEntryStationConfigManagement.getPropertyValue()).equalsIgnoreCase("0")? "Yes" : "No");
							}else
								qiEntryStationSettingsDto.setSettingPropertyValue(qiEntryStationConfigManagement.getPropertyValue());
						}	
					}
				}
			}
			getView().getEntryStationConfigSettingsTablePane().getTable().getItems().clear();
			getView().getEntryStationConfigSettingsTablePane().getTable().getItems().addAll(qiEntryStationSettingsDtoList);
			
			loadRepairAreaData();
			clearDisplayMessage();
		}	
	}

	@SuppressWarnings("unchecked")
	private void loadRepairAreaData() {
		String siteName = StringUtils.trimToEmpty(getEntryStationView().getSiteValueLabel().getText());
		String plantName = StringUtils.trimToEmpty(getEntryStationView().getPlantComboBox().getSelectionModel().getSelectedItem().toString());
		String processPointId = StringUtils.trimToEmpty((String)getEntryStationView().getQicsStationComboBoxSelectedId());
		
		List<String> repairAreaList = getModel().findAllRepairAreasBySiteAndPlant(siteName, plantName, 'A');
		getView().getStationRepairAreaComboBox().getItems().clear();
		getView().getStationRepairAreaComboBox().getItems().add(StringUtils.EMPTY);
		getView().getStationRepairAreaComboBox().getItems().add(QiConstant.CLEAR_REPAIR_AREA);		
		getView().getStationRepairAreaComboBox().getItems().addAll(repairAreaList);
		
		QiStationConfigurationId qiStationConfigId = findbyId(processPointId, AdditionalStationConfigSettings.REPAIR_AREA.getSettingName());
		
		QiStationConfiguration repairAreaConfig= getModel().findEntryStationById(qiStationConfigId);
		if(null != repairAreaConfig) {
			getView().getStationRepairAreaComboBox().getSelectionModel().select(repairAreaConfig.getPropertyValue());
		}
	}
	
	/**
	 * This method is used to save station repair area
	 */
	private void saveStationRepairArea() {
		String processPointId = StringUtils.trimToEmpty((String)getEntryStationView().getQicsStationComboBoxSelectedId());
		String propertyValue = StringUtils.trimToEmpty((String)getView().getStationRepairAreaComboBox().getSelectionModel().getSelectedItem());
		
		QiStationConfiguration qiStationConfig = new QiStationConfiguration();
		QiStationConfigurationId qiStationConfigId = findbyId(processPointId, AdditionalStationConfigSettings.REPAIR_AREA.getSettingName());
		qiStationConfigId.setProcessPointId(StringUtils.trimToEmpty((String)getEntryStationView().getQicsStationComboBoxSelectedId()));
		qiStationConfigId.setPropertyKey(AdditionalStationConfigSettings.REPAIR_AREA.getSettingName());
		qiStationConfig.setId(qiStationConfigId);
		qiStationConfig.setActive((short)1);
		qiStationConfig.setPropertyValue(StringUtils.trimToEmpty((String)getView().getStationRepairAreaComboBox().getSelectionModel().getSelectedItem()));
		qiStationConfig.setUpdateUser(getUserId());
		getModel().updateEntryStation(qiStationConfig);
		
		QiStationConfiguration entryStationConfig = getModel().findEntryStationById(qiStationConfigId);
		getModel().updateEntryStation(entryStationConfig);
		
		loadRepairAreaData();
		getView().getSaveStationRepairAreaBtn().setDisable(true);
		if(null != entryStationConfig){	
			AuditLoggerUtil.logAuditInfo(cloneAuditData(entryStationConfig), qiStationConfig, QiConstant.SAVE_REASON_FOR_ENTRY_STATION_COFIGURATION_AUDIT,getView().getScreenName(),getUserId());
		}
	}

	/**
	 * This method is used to set UPC Parts values for selected Entry Station .
	 */
	public void setUPCPartsForSelectedEntryStation() {
		if(null!= getEntryStationView().getQicsStationComboBox().getSelectionModel().getSelectedItem()){
			loadUPCParts();
		}
		disableUPCOperationButton(true);
		disableUPCShiftButton(true);
		getView().getUpcPartFilterTextfield().clear();
	}

	/**
	 * This method is used to load UPC Parts
	 */
	private void loadUPCParts() {
		try {	
			List<QiStationUpcPart> assignedUPCPartsList= getModel().findAllStationUpcPartByProcessPointId(StringUtils.trimToEmpty((String)getEntryStationView().getQicsStationComboBoxSelectedId()));
			if(!assignedUPCPartsList.isEmpty()) {
				List<QiBomQicsPartMapping> avilableUPCpartsList=getModel().findAllAvailableUPCParts();
				clearUPCTableData();
				for(QiStationUpcPart qiStationUpcPart:assignedUPCPartsList){
					outerloop:for(QiBomQicsPartMapping bomQicsPartMapping:avilableUPCpartsList){
						if(StringUtils.trimToEmpty(qiStationUpcPart.getId().getMainPartNo()).equals(StringUtils.trimToEmpty(bomQicsPartMapping.getMainPartNo()))) {
							getView().getAssignedUPCTablePane().getTable().getItems().add(bomQicsPartMapping);
							avilableUPCpartsList.remove(bomQicsPartMapping);
							break outerloop;
						}
					}
				}
				getView().getAvilableUPCTablePane().getTable().getItems().addAll(avilableUPCpartsList);
			} else {
				clearUPCTableData();
				getView().getAvilableUPCTablePane().getTable().getItems().addAll(getModel().findAllAvailableUPCParts());
			}
		} catch (Exception e) {
			handleException("An error occurred while updatinging UPC Parts", "Failed to get update UPC Parts in Entry Screen", e);
		}
	}

	/**
	 * This method is used to clear all data in UPC parts table
	 */
	private void clearUPCTableData() {
		getView().getAssignedUPCTablePane().getTable().getItems().clear();
		getView().getAvilableUPCTablePane().getTable().getItems().clear();
	}

	public EntryStationPanel getEntryStationView() {
		return entryStationView;
	}

	public StationDepartmentPanel getEntryDeptAndWriteUpDeptPanel() {
		return entryDeptAndWriteUpDeptPanel;
	}

	public StationEntryScreenPanel getEntryStationDefaultStatusView() {
		return entryStationDefaultStatusView;
	}
	
	private void clearEntryDeptWriteUpDept() {
		getEntryDeptAndWriteUpDeptPanel().getAvailableEntryDepartObjectTablePane().getTable().getItems().clear();
		getEntryDeptAndWriteUpDeptPanel().getAvailableWriteUpDepartObjectTablePane().getTable().getItems().clear();
		getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedEntryDeptObjectTablePane().getTable().getItems().clear();
		getEntryDeptAndWriteUpDeptPanel().getCurrentlyAssignedWriteUpDepartObjectTablePane().getTable().getItems().clear();
	}
	
	
}