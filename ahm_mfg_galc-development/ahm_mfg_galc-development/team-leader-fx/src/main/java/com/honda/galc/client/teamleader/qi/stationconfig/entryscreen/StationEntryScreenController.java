package com.honda.galc.client.teamleader.qi.stationconfig.entryscreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigController;
import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigModel;
import com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigPanel;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.qi.QiEntryDepartmentDto;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.entity.enumtype.StationConfigurationOperations;
import com.honda.galc.entity.qi.QiEntryModel;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qi.QiStationConfigurationId;
import com.honda.galc.entity.qi.QiStationEntryScreen;
import com.honda.galc.entity.qi.QiStationEntryScreenId;
import com.honda.galc.util.AuditLoggerUtil;
import com.honda.galc.util.StringUtil;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
/**
 * 
 * <h3>StationEntryScreenController Class description</h3>
 * <p>
 * StationEntryScreenController is used to load data in TableViews and perform the action on Update , Reset and Shift buttonsetc.
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
public class StationEntryScreenController extends EntryStationConfigController {
	
	private static final String TEXT = "TEXT";
	
	public StationEntryScreenController(EntryStationConfigModel model,
			EntryStationConfigPanel view) {
		super(model, view);
	}

	public void initListeners() {
		addProductTypeComboboxListener();
		addEntryModelComboboxListener();
		addEntryDepartmentListener();
		if(isFullAccess())
			addTableListener();
	}

	public void loadInitialData() {
		setEntryStationStatus();
	}
	/**
	 * This method is used to perform action on 'Update', 'Reset' ,'shift' buttons for EntryDept , WriteupDept and EntryScreen table view
	 */
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			switch(StationConfigurationOperations.getType(loggedButton.getId())){
			case RIGHT_SHIFT_ENTRY_SCREEN :
				assignEntryScreenAction(actionEvent);
				break;
			case LEFT_SHIFT_ENTRY_SCREEN :
				deassignEntryScreenAction(actionEvent);
				break;
			case UPDATE_ENTRY_SCREEN :
				updateEntryScreenBtnAction(actionEvent);
				break; 
			case RESET_ENTRY_SCREEN :
				resetEntryScreen();
				break; 
			case UPDATE_ENTRY_STATION_STATUS :
				updateEntryStationStatus();
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
		if(actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem)actionEvent.getSource();
			if(QiConstant.SET_SEQUENCE.equals(menuItem.getText()))
				setSequence(actionEvent);
			else if(QiConstant.SET_ANGLE.equals(menuItem.getText()))
				setAngle(actionEvent);
			else if(QiConstant.SET_SCAN.equals(menuItem.getText()))
			    setScan(actionEvent,true);
			else if(QiConstant.UNSET_SCAN.equals(menuItem.getText()))
				setScan(actionEvent,false);
		}
		if ((actionEvent.getSource() instanceof LoggedRadioButton) || (actionEvent.getSource() instanceof CheckBox) ) {
			clearDisplayMessage();
			setUpdateEntryStationStatusButton();	
		}
	}
	/**
	 * This method is used to update status for selected Entry Station .
	 */
	private void updateEntryStationStatus() {
		List<String> availableDefectStatusList =new ArrayList<String>();
		if(getView().getEntryStationDefaultStatusPanel().getRepairedChkBox().isSelected()){
			availableDefectStatusList.add(StringUtils.trimToEmpty(getView().getEntryStationDefaultStatusPanel().getRepairedChkBox().getId()));
		}
		if(getView().getEntryStationDefaultStatusPanel().getNotRepairedChkBox().isSelected()){
			availableDefectStatusList.add(StringUtils.trimToEmpty(getView().getEntryStationDefaultStatusPanel().getNotRepairedChkBox().getId()));
		}
		if(getView().getEntryStationDefaultStatusPanel().getNonRepairableChkBox().isSelected()){
			availableDefectStatusList.add(StringUtils.trimToEmpty(getView().getEntryStationDefaultStatusPanel().getNonRepairableChkBox().getId()));
		}
		updateEntryStationConfig(QiConstant.ENTRY_STATION_AVAILABLE_DEFECT_STATUS,StringUtils.join(availableDefectStatusList,","));
		if(!getView().getEntryStationDefaultStatusPanel().getNoneRadioBtn().isSelected()){
			String defectType = "";
			if(getView().getEntryStationDefaultStatusPanel().getRepairedRadioBtn().isSelected()) {
				defectType= QiConstant.REPAIRED; 
			}
			else if(getView().getEntryStationDefaultStatusPanel().getNotRepairedRadioBtn().isSelected()) {
				defectType= QiConstant.NOT_REPAIRED; 
			}
			else if(getView().getEntryStationDefaultStatusPanel().getNonRepairableRadioBtn().isSelected()) {
				defectType= QiConstant.NON_REPAIRABLE; 
			}
			updateEntryStationConfig(QiConstant.ENTRY_STATION_DEFAULT_DEFECT_STATUS, defectType);
		
		}else{
			removeEntryStationAvailableStatus();	
		}
		getView().getEntryStationDefaultStatusPanel().getUpdateDefaultStatusBtn().setDisable(true);
	}
	/**
	 * This method is used to update selected Entry Station .
	 */
	private void updateEntryStationConfig(String propertyKey, String propertyValue) {
		QiStationConfiguration qiEntryStationConfigManagement = new QiStationConfiguration();
		QiStationConfigurationId qiEntryStationConfigManagementid = new QiStationConfigurationId();
		qiEntryStationConfigManagementid.setProcessPointId(StringUtils.trimToEmpty((String)getView().getEntryStationPanel().getQicsStationComboBoxSelectedId()));
		qiEntryStationConfigManagementid.setPropertyKey(StringUtils.trimToEmpty(propertyKey));
		qiEntryStationConfigManagement.setId(qiEntryStationConfigManagementid);
		qiEntryStationConfigManagement.setPropertyValue(StringUtils.trimToEmpty(propertyValue));
		qiEntryStationConfigManagement.setUpdateUser(getUserId());
		qiEntryStationConfigManagement.setActive((short)1);
		//call to prepare and insert audit data
		QiStationConfiguration entryStationConfigManagement = getModel().findEntryStationById(findbyId(StringUtils.trimToEmpty((String)getView().getEntryStationPanel().getQicsStationComboBoxSelectedId()), StringUtils.trimToEmpty(propertyKey)));
		getModel().updateEntryStation(qiEntryStationConfigManagement);
		if(null!=entryStationConfigManagement){
			AuditLoggerUtil.logAuditInfo(cloneAuditData(entryStationConfigManagement),qiEntryStationConfigManagement,QiConstant.SAVE_REASON_FOR_ENTRY_STATION_COFIGURATION_AUDIT,getView().getScreenName(),getUserId());
		}
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
	 * This method is used to remove status if it is default .
	 */
	private void removeEntryStationAvailableStatus() {
		QiStationConfiguration qiEntryStationConfigManagement = new QiStationConfiguration();
		QiStationConfigurationId qiEntryStationConfigManagementid = new QiStationConfigurationId();
		qiEntryStationConfigManagementid.setProcessPointId(StringUtils.trimToEmpty((String)getView().getEntryStationPanel().getQicsStationComboBoxSelectedId()));
		qiEntryStationConfigManagementid.setPropertyKey(QiConstant.ENTRY_STATION_DEFAULT_DEFECT_STATUS);
		qiEntryStationConfigManagement.setId(qiEntryStationConfigManagementid);
		//call to prepare and insert audit data

		QiStationConfiguration entryStationConfigManagement = getModel().findEntryStationById(findbyId(StringUtils.trimToEmpty((String)getView().getEntryStationPanel().getQicsStationComboBoxSelectedId()), StringUtils.trimToEmpty(QiConstant.ENTRY_STATION_AVAILABLE_DEFECT_STATUS)));
		getModel().deleteEntryStationDefaultDefectStatus(qiEntryStationConfigManagement);
		if(null!=entryStationConfigManagement){
			AuditLoggerUtil.logAuditInfo(cloneAuditData(entryStationConfigManagement), qiEntryStationConfigManagement, QiConstant.SAVE_REASON_FOR_ENTRY_STATION_COFIGURATION_AUDIT,getView().getScreenName(),getUserId());
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
	 * This method is used to reset EntryScreen tableviews
	 */
	private void resetEntryScreen(){
		loadEntryScreenTable();
		getView().getEntryStationDefaultStatusPanel().getUpdateEntryScreenBtn().setDisable(true);
		getView().getEntryStationDefaultStatusPanel().getResetEntryScreenBtn().setDisable(true);
	}

	private void updateEntryScreenBtnAction(ActionEvent actionEvent){
		if(validationOnComboBox())return;
		List<QiEntryScreenDto> currentlyAssignedEntryScreenList = getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getItems();
		String entryModel  = StringUtils.trimToEmpty(((String)getView().getEntryStationDefaultStatusPanel().getModelComboBox().getValue()).split("-")[0]);
		String entryDept =(String) getView().getEntryStationDefaultStatusPanel().getDepartmentComboBoxSelectedId();
		String qicsStation = StringUtils.trimToEmpty(getView().getEntryStationPanel().getQicsStationComboBoxSelectedId().toString());
		
		List<QiEntryScreenDto> qiStationAssignedEntryScreenDBList = getModel().findAllEntryScreenInfoByProcessPoint(entryModel,entryDept,qicsStation);
		List<QiEntryScreenDto> removedStationEntryScreen=ListUtils.subtract(qiStationAssignedEntryScreenDBList, currentlyAssignedEntryScreenList);
		List<QiEntryScreenDto> addedStationEntryScreen=ListUtils.subtract(currentlyAssignedEntryScreenList, qiStationAssignedEntryScreenDBList);
		boolean isVersionCreated = getModel().isVersionCreated(entryModel);
		if (isVersionCreated && currentlyAssignedEntryScreenList.size() == 0 && !isScreenUsedByOtherProcessPoint(entryModel, qiStationAssignedEntryScreenDBList)) {
			if (!MessageDialog.confirm(ClientMainFx.getInstance().getStage(getApplicationId()),
					"Entry Model in use will be deleted. Are you sure you want to delete?")) {
				return;
			}
		}
		try {
			getModel().removeStationEntryByProcessPoint(qicsStation,entryModel,entryDept);
			addStationEntryScreenList(getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getItems());
			performVersionOperation(addedStationEntryScreen, removedStationEntryScreen, entryModel, isVersionCreated);

		} catch(Exception e) {
			handleException("An error occurred at updateEntryScreenBtnAction method ", "Failed to update entry screen", e);
		}
		getView().getEntryStationDefaultStatusPanel().getUpdateEntryScreenBtn().setDisable(true);
		getView().getEntryStationDefaultStatusPanel().getResetEntryScreenBtn().setDisable(true);

	}
	/**
	 * This method is used to add EntryScreen station when not available in currently assigned EntryScreen
	 * @param currentlyAssignedEntryScreenList
	 */
	private void addStationEntryScreenList(List<QiEntryScreenDto> addedStationEntryScreen){
		String processPoint=StringUtils.trimToEmpty((String)getView().getEntryStationPanel().getQicsStationComboBoxSelectedId());
		String divisionId=StringUtils.trimToEmpty((String)getView().getEntryStationDefaultStatusPanel().getDepartmentComboBoxSelectedId());
		String entrymodel=StringUtils.trimToEmpty(((String)getView().getEntryStationDefaultStatusPanel().getModelComboBox().getValue()).split("-")[0]);
		QiStationEntryScreen qiStationEntryScreen;
		QiStationEntryScreenId  qiStationEntryScreenId;
		List<QiStationEntryScreen> addedStationEntryDepartmentList = new ArrayList<QiStationEntryScreen>();
		if(!addedStationEntryScreen.isEmpty()){
			for (QiEntryScreenDto entryScreen : addedStationEntryScreen) {
				qiStationEntryScreen = new QiStationEntryScreen();
				qiStationEntryScreenId = new QiStationEntryScreenId();
				qiStationEntryScreenId.setProcessPointId(processPoint);
				qiStationEntryScreenId.setDivisionId(divisionId);
				qiStationEntryScreenId.setEntryModel(entrymodel);
				qiStationEntryScreenId.setSeq(entryScreen.getSeq());
				qiStationEntryScreen.setId(qiStationEntryScreenId);
				qiStationEntryScreen.setOrientationAngle(entryScreen.getOrientationAngle());
				qiStationEntryScreen.setEntryScreen(entryScreen.getEntryScreen());
				qiStationEntryScreen.setAllowScan(entryScreen.getAllowScan());
				addedStationEntryDepartmentList.add(qiStationEntryScreen);
			}
		}
		if(!addedStationEntryDepartmentList.isEmpty()){
			getModel().createStationEntryScreens(addedStationEntryDepartmentList);
		}
	}

	/**
	 * This method is used to shift available EntryScreen to currently assigned Entry Screen
	 * @param actionEvent
	 */
	private void assignEntryScreenAction(ActionEvent actionEvent){
		clearMessage();
		short seq=(short)(getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getItems().size()+1);
		List<QiEntryScreenDto> availableEntryScreenList = getView().getEntryStationDefaultStatusPanel().getEntryScreenObjectTablePane().getSelectedItems();

		if(!availableEntryScreenList.isEmpty()){
			getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getItems().addAll(availableEntryScreenList);
			for (QiEntryScreenDto qiEntryScreenDto : availableEntryScreenList) {
				qiEntryScreenDto.setSeq(seq);
				qiEntryScreenDto.setOrientationAngle((short)0);
				seq++;
			}
			getView().getEntryStationDefaultStatusPanel().getEntryScreenObjectTablePane().getTable().getItems().removeAll(availableEntryScreenList);
			getView().getEntryStationDefaultStatusPanel().getUpdateEntryScreenBtn().setDisable(false);
			getView().getEntryStationDefaultStatusPanel().getResetEntryScreenBtn().setDisable(false);
		}
	}
	/**
	 * This method is used to shift currently assigned Entry Screen to available EntryScreen 
	 * @param actionEvent
	 */
	private void deassignEntryScreenAction(ActionEvent actionEvent){

		clearMessage();
		List<QiEntryScreenDto> currentlyAssignedEntryScreenList = getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getSelectedItems();

		String entryModel  = StringUtils.trimToEmpty(((String)getView().getEntryStationDefaultStatusPanel().getModelComboBox().getValue()).split("-")[0]);
		if(getModel().isVersionCreated(entryModel)) {
			boolean screenIsUsed = false;
			for(QiEntryScreenDto entryScreenDto : currentlyAssignedEntryScreenList) {
				if(getModel().checkScreenIsUsed(entryModel, entryScreenDto.getEntryScreen(), (short) 0)) {
					screenIsUsed = true;
					break;
				}
			}
			if(screenIsUsed) {
				displayErrorMessage("Pending version of Entry Model is created so can not de-assign screen");
				return;
			}
		}
		short seq=1;
		if(!currentlyAssignedEntryScreenList.isEmpty()){
			getView().getEntryStationDefaultStatusPanel().getEntryScreenObjectTablePane().getTable().getItems().addAll(currentlyAssignedEntryScreenList);
			getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getItems().removeAll(currentlyAssignedEntryScreenList);
			for (QiEntryScreenDto qiEntryScreenDto : getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getItems()) {
				qiEntryScreenDto.setSeq(seq);
				seq++;
			}
			getView().getEntryStationDefaultStatusPanel().getUpdateEntryScreenBtn().setDisable(false);
			getView().getEntryStationDefaultStatusPanel().getResetEntryScreenBtn().setDisable(false);
		}
	}
	
	private void setUpdateEntryStationStatusButton() {
		StationEntryScreenPanel panel = getView().getEntryStationDefaultStatusPanel();

		panel.getRepairedRadioBtn().setDisable(!panel.getRepairedChkBox().isSelected());
		panel.getNotRepairedRadioBtn().setDisable(!panel.getNotRepairedChkBox().isSelected());
		panel.getNonRepairableRadioBtn().setDisable(!panel.getNonRepairableChkBox().isSelected());
		
		if (((LoggedRadioButton)panel.getNoneRadioBtn().getToggleGroup().getSelectedToggle()).isDisabled())
			panel.getNoneRadioBtn().setSelected(true);
		for (CheckBox checkBox : new CheckBox[] {panel.getRepairedChkBox(),panel.getNotRepairedChkBox(),panel.getNonRepairableChkBox()}) {
			if(checkBox.isSelected() && isFullAccess()) {
				panel.getUpdateDefaultStatusBtn().setDisable(false);
				return;
			}
		}
		panel.getUpdateDefaultStatusBtn().setDisable(true);
	}
	
	private void setSequence(ActionEvent event){
		int maxSize=getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getItems().size();
		SetSequenceDialog dialog= new SetSequenceDialog(QiConstant.SET_SEQUENCE, getApplicationId(),getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getSelectionModel().getSelectedItem(), maxSize);
		dialog.showDialog();
		QiEntryScreenDto qiEntryScreenDto=getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getSelectionModel().getSelectedItem();
		if(dialog.showSetSequenceDialog(null))
		{
			short newSeqence = Short.parseShort(dialog.getSetSequenceNoTextField().getText());
			int currentSeqNo=qiEntryScreenDto.getSeq();
			List<QiEntryScreenDto> qiEntryScreenDtosList = getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getItems();
			List<QiEntryScreenDto> qiEntryScreenDtosPrimaryList = new ArrayList<QiEntryScreenDto>();
			List<QiEntryScreenDto> qiEntryScreenDtosSecondaryList = new ArrayList<QiEntryScreenDto>();
			qiEntryScreenDtosSecondaryList=qiEntryScreenDtosList;
			if(newSeqence <= maxSize){
				if(newSeqence < currentSeqNo){
					for(int i=0;i< newSeqence-1;i++){
						qiEntryScreenDtosPrimaryList.add(qiEntryScreenDtosList.get(i));
					}
					QiEntryScreenDto entryScreenDto=qiEntryScreenDtosList.get(currentSeqNo-1);
					entryScreenDto.setSeq(newSeqence);
					qiEntryScreenDtosPrimaryList.add(entryScreenDto);
					for(int i=newSeqence-1;i<currentSeqNo-1;i++){
						qiEntryScreenDtosSecondaryList.get(i).setSeq((short)(qiEntryScreenDtosSecondaryList.get(i).getSeq()+1));
						qiEntryScreenDtosPrimaryList.add(qiEntryScreenDtosSecondaryList.get(i));
					}
					for(int i=currentSeqNo;i<maxSize;i++){
						qiEntryScreenDtosPrimaryList.add(qiEntryScreenDtosSecondaryList.get(i));
					}
				}else{
					for(int i=0;i< currentSeqNo-1;i++){
						qiEntryScreenDtosPrimaryList.add(qiEntryScreenDtosList.get(i));
					}
					for(int i=currentSeqNo;i< newSeqence;i++){
						qiEntryScreenDtosSecondaryList.get(i).setSeq((short)(qiEntryScreenDtosSecondaryList.get(i).getSeq()-1));
						qiEntryScreenDtosPrimaryList.add(qiEntryScreenDtosSecondaryList.get(i));
					}
					QiEntryScreenDto entryScreenDto=qiEntryScreenDtosList.get(currentSeqNo-1);
					entryScreenDto.setSeq(newSeqence);
					qiEntryScreenDtosPrimaryList.add(entryScreenDto);
					for(int i=newSeqence;i< maxSize;i++){
						qiEntryScreenDtosPrimaryList.add(qiEntryScreenDtosList.get(i));
					}
				}
				getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getItems().clear();
				getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getItems().addAll(qiEntryScreenDtosPrimaryList);
				getView().getEntryStationDefaultStatusPanel().getUpdateEntryScreenBtn().setDisable(false);
				getView().getEntryStationDefaultStatusPanel().getResetEntryScreenBtn().setDisable(false);
			}
			else{
				addMessage("Can not Change the Sequence");
			}

		}
		else
			return;
	};

	private void setAngle(ActionEvent event){
		OrientationAngleDialog dialog= new OrientationAngleDialog(QiConstant.SET_ANGLE, getApplicationId(),getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getSelectionModel().getSelectedItem());
		dialog.showDialog();
		if(dialog.showOrientationAngleDialog())
		{
			short angle =dialog.getSelectedAngle();
			List<QiEntryScreenDto> qiEntryScreenDtosPrimaryList = new ArrayList<QiEntryScreenDto>();
			for(QiEntryScreenDto qiEntryScreen:getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getItems()){
				if(getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getSelectionModel().getSelectedItem().equals(qiEntryScreen)){
					qiEntryScreen.setOrientationAngle(angle);
				}
				qiEntryScreenDtosPrimaryList.add(qiEntryScreen);
			}
			getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getItems().clear();
			getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getItems().addAll(qiEntryScreenDtosPrimaryList);
			getView().getEntryStationDefaultStatusPanel().getUpdateEntryScreenBtn().setDisable(false);
			getView().getEntryStationDefaultStatusPanel().getResetEntryScreenBtn().setDisable(false);


		}
		else
			return;
	};

	/**
	 * This method is used to apply listener on TableView
	 */
	private void addTableListener() {
		getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiEntryScreenDto>() {
			public void changed(ObservableValue<? extends QiEntryScreenDto> arg0,
					QiEntryScreenDto arg1, QiEntryScreenDto arg2) {
				addContextMenuItems();
				
				if(arg2 != null && arg2.getEntryScreen() != null)
					Logger.getLogger().check("QICS Related Model and Screen :: Entry Screen : " + arg2.getEntryScreen() + " selected");
			}
		});
		getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				addContextMenuItems();
			}
		});
	}
	/**
	 * This method is added because initEventHandlers() is called before the selection listener is triggered.
	 */
	@Override
	public void addContextMenuItems() {
		String[] menuItems;
	     if(getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().isFocused()){
			if(getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getContextMenu()!=null)
				getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getContextMenu().getItems().clear();
			if(null!=getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getSelectionModel().getSelectedItem() && 
					getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getSelectionModel().getSelectedItems().size()==1 && 
					!getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getSelectionModel().getSelectedItem().getImageName().equals(TEXT)){
				menuItems = new String[] {QiConstant.SET_SEQUENCE, QiConstant.SET_ANGLE};
				getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().createContextMenu(menuItems, this);
			}else if(null!=getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getSelectionModel().getSelectedItem() && 
					getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getSelectionModel().getSelectedItems().size()==1
					 && getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getSelectionModel().getSelectedItem().getImageName().equalsIgnoreCase(TEXT)
					 &&  getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getSelectionModel().getSelectedItem().getAllowScan()==0){
				menuItems = new String[] {QiConstant.SET_SEQUENCE,QiConstant.SET_SCAN};
				getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().createContextMenu(menuItems, this);
			}
			else if(null!=getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getSelectionModel().getSelectedItem() && 
					getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getSelectionModel().getSelectedItems().size()==1
					 && getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getSelectionModel().getSelectedItem().getImageName().equalsIgnoreCase(TEXT)
					 &&  getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getSelectionModel().getSelectedItem().getAllowScan()==1){
				menuItems = new String[] {QiConstant.SET_SEQUENCE,QiConstant.UNSET_SCAN};
				getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().createContextMenu(menuItems, this);
			}
			else if(null!=getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getSelectionModel().getSelectedItem() && 
					getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getSelectionModel().getSelectedItems().size()==1){
				menuItems = new String[] {QiConstant.SET_SEQUENCE};
				getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().createContextMenu(menuItems, this);
			}
		}
	}
	/**
	 * This method is used to populate EntryScreen and EntryScreenModel tableview
	 */
	public void addEntryDepartmentListener(){
		getView().getEntryStationDefaultStatusPanel().getDepartmentComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiEntryDepartmentDto>() {
			public void changed(ObservableValue<? extends QiEntryDepartmentDto> arg0,
					QiEntryDepartmentDto arg1, QiEntryDepartmentDto arg2) {
				clearDisplayMessage();
				loadEntryScreenTable();
			}
		});
	}
	/**
	 * This method is used to populate Entry Screen table based on Model and EntryDept
	 */
	private void addEntryModelComboboxListener() {
		getView().getEntryStationDefaultStatusPanel().getModelComboBox().valueProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				clearDisplayMessage();				
				loadEntryScreenTable();
			}
		});		
	}
	/**
	 * This method is used to populate Entrymodel dropdown based on ProductType
	 */
	private void addProductTypeComboboxListener() {
		getView().getEntryStationDefaultStatusPanel().getProductTypeComboBox().valueProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				clearDisplayMessage();				
				getView().getEntryStationDefaultStatusPanel().getModelComboBox().getItems().clear();
				String productType= (String)getView().getEntryStationDefaultStatusPanel().getProductTypeComboBox().getValue();
				loadEntryModelComboboxList(productType);
				loadEntryScreenTable();
			}
		});		
	}
	/**
	 * This method is used to perform validation on ProductType ,Model and Department combobox
	 * @return
	 */
	private boolean validationOnComboBox(){
		if(getView().getEntryStationDefaultStatusPanel().getProductTypeComboBox().getValue()==null){
			displayErrorMessage("Please select Product Type");
			return true;
		}else if(getView().getEntryStationDefaultStatusPanel().getModelComboBox().getValue()==null){
			displayErrorMessage("Please select Model");
			return true;
		}else if(getView().getEntryStationDefaultStatusPanel().getDepartmentComboBoxSelectedId()==null){
			displayErrorMessage("Please select Dept");
			return true;
		}
		return false;

	}

	/**
	 * This method is used to populate EntryScreen and EntryModel tableview
	 */
	private void loadEntryScreenTable(){
		try {
			clearDisplayMessage();
			if(validationOnComboBox())return;
			String entryModel = StringUtils.trimToEmpty(((String)getView().getEntryStationDefaultStatusPanel().getModelComboBox().getValue()).split("-")[0]);
			String entryDept =(String) getView().getEntryStationDefaultStatusPanel().getDepartmentComboBoxSelectedId();
			List<QiEntryScreenDto> entryScreenDtoList= getModel().findAllEntryScreenByEntryModelAndEntryDepartment(entryModel, entryDept);

			if(entryScreenDtoList.isEmpty()){
				getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getItems().clear();
			}
			String qicsStation = (String)getView().getEntryStationPanel().getQicsStationComboBoxSelectedId();
			List<QiEntryScreenDto>  assignedEntryScreensDtoList = getModel().findAllEntryScreenInfoByProcessPoint(entryModel,entryDept,qicsStation);
			List<QiEntryScreenDto> subtractedAllEntryScreenList = ListUtils.subtract(entryScreenDtoList,assignedEntryScreensDtoList);
			getView().getEntryStationDefaultStatusPanel().getEntryScreenObjectTablePane().setData(subtractedAllEntryScreenList);
			getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().setData(assignedEntryScreensDtoList);

		} catch (Exception e) {
			handleException("An error occurred while fetching Entry Scren list", "Failed to get Entry Screen", e);
		}
	}
	/**
	 * This method is used to set the values of primary key
	 * @return
	 */
	private QiStationEntryScreenId setStationEntryScreenId(){
		String entryModel = StringUtils.trimToEmpty(((String)getView().getEntryStationDefaultStatusPanel().getModelComboBox().getValue()).split("-")[0]);
		String entryDept = (String)getView().getEntryStationDefaultStatusPanel().getDepartmentComboBoxSelectedId();
		String qicsStation = (String)getView().getEntryStationPanel().getQicsStationComboBoxSelectedId();
		QiStationEntryScreenId stationEntryScreenId = new QiStationEntryScreenId();
		stationEntryScreenId.setDivisionId(entryDept);
		stationEntryScreenId.setEntryModel(entryModel);
		stationEntryScreenId.setProcessPointId(qicsStation);
		return stationEntryScreenId;
	}

	/**
	 * This method is used to populate Entrymodel dropdown based on ProductType
	 * @param productType
	 */
	public void loadEntryModelComboboxList(String productType) {
		try {
			productType=StringUtils.trimToNull(productType);
			if(productType.equalsIgnoreCase(ProductType.FRAME.getProductName()) || productType.equalsIgnoreCase(ProductType.ENGINE.getProductName())){
				if(getView().getEntryStationDefaultStatusPanel().getNonRepairableChkBox().isSelected()){
					getView().getEntryStationDefaultStatusPanel().getNonRepairableChkBox().setSelected(false);
				}
				getView().getEntryStationDefaultStatusPanel().getNonRepairableChkBox().setDisable(true);
			}else{
				getView().getEntryStationDefaultStatusPanel().getNonRepairableChkBox().setDisable(false);
			}
			List<QiEntryModel> qiEntryModelList = getModel().findAllEntryModelByProductType(productType);
			Set<String> entryModelSet = new TreeSet<String>();
			if (qiEntryModelList.size() > 0) {
				for (QiEntryModel qiEntryModel : qiEntryModelList) {
					entryModelSet.add(qiEntryModel.getId().getEntryModel()+" - "+qiEntryModel.getEntryModelDescription());
				}
				getView().getEntryStationDefaultStatusPanel().getModelComboBox().getItems().clear();
				getView().getEntryStationDefaultStatusPanel().getModelComboBox().getItems().addAll(entryModelSet);
				getView().getEntryStationDefaultStatusPanel().getModelComboBox().getSelectionModel().select(0);
			}
		} catch (Exception e) {
			handleException("An error occurred while fetching Entry Model list", "Failed to get Entry Model", e);
		}
	}
	
	public void setEntryStationStatus() {
		StationEntryScreenPanel defaultStatusPanel = getView().getEntryStationDefaultStatusPanel();
		String selectedStation = StringUtils.trimToEmpty(getView().getEntryStationPanel().getQicsStationComboBoxSelectedId());
		if(selectedStation.isEmpty()) return;
		
		disableEntryStationDefaultStatusPanel(false);
		
		QiStationConfiguration availableDefectStatuses = getModel().findEntryStationById(findbyId(selectedStation, QiConstant.ENTRY_STATION_AVAILABLE_DEFECT_STATUS));
		if(availableDefectStatuses == null) return;
		
		for(String status : availableDefectStatuses.getPropertyValue().split(",")){
			if(StringUtils.trimToEmpty(status).equalsIgnoreCase(defaultStatusPanel.getRepairedChkBox().getId())){
				defaultStatusPanel.getRepairedChkBox().setSelected(true);
				defaultStatusPanel.getRepairedRadioBtn().setDisable(false);
			}else if(StringUtils.trimToEmpty(status).equalsIgnoreCase(defaultStatusPanel.getNotRepairedChkBox().getId())){
				defaultStatusPanel.getNotRepairedChkBox().setSelected(true);
				defaultStatusPanel.getNotRepairedRadioBtn().setDisable(false);
			}else if(StringUtils.trimToEmpty(status).equalsIgnoreCase(defaultStatusPanel.getNonRepairableChkBox().getId())){
				defaultStatusPanel.getNonRepairableChkBox().setSelected(true);
				defaultStatusPanel.getNonRepairableRadioBtn().setDisable(false);
			}
		}
		
		QiStationConfiguration defaultDefectStatus = getModel().findEntryStationById(findbyId(selectedStation, QiConstant.ENTRY_STATION_DEFAULT_DEFECT_STATUS));
		if(defaultDefectStatus == null) return;
		
		if(StringUtils.trimToEmpty(defaultDefectStatus.getPropertyValue()).equalsIgnoreCase(QiConstant.REPAIRED) && 
				!defaultStatusPanel.getRepairedChkBox().isDisabled())
			defaultStatusPanel.getRepairedRadioBtn().setSelected(true);
		else if (StringUtils.trimToEmpty(defaultDefectStatus.getPropertyValue()).equalsIgnoreCase(QiConstant.NOT_REPAIRED) &&
				!defaultStatusPanel.getNotRepairedChkBox().isDisabled())
			defaultStatusPanel.getNotRepairedRadioBtn().setSelected(true);
		else if (StringUtils.trimToEmpty(defaultDefectStatus.getPropertyValue()).equalsIgnoreCase(QiConstant.NON_REPAIRABLE) &&
				!defaultStatusPanel.getNonRepairableChkBox().isDisabled())
			defaultStatusPanel.getNonRepairableRadioBtn().setSelected(true);
	}
	
	public void disableEntryStationDefaultStatusPanel(Boolean b) {
		entryStationDefaultStatusChkBoxToggle(b);
		
		getView().getEntryStationDefaultStatusPanel().getRepairedRadioBtn().setDisable(true);
		getView().getEntryStationDefaultStatusPanel().getNotRepairedRadioBtn().setDisable(true);
		getView().getEntryStationDefaultStatusPanel().getNonRepairableRadioBtn().setDisable(true);
	
		getView().getEntryStationDefaultStatusPanel().getNoneRadioBtn().setDisable(b);
		getView().getEntryStationDefaultStatusPanel().getNoneRadioBtn().setSelected(true);
		
		getView().getEntryStationDefaultStatusPanel().getUpdateDefaultStatusBtn().setDisable(true);
	}
	
	public void entryStationDefaultStatusChkBoxToggle(Boolean b) {
		getView().getEntryStationDefaultStatusPanel().getRepairedChkBox().setDisable(b);
		getView().getEntryStationDefaultStatusPanel().getNotRepairedChkBox().setDisable(b);
		getView().getEntryStationDefaultStatusPanel().getNonRepairableChkBox().setDisable(b);
		
		getView().getEntryStationDefaultStatusPanel().getRepairedChkBox().setSelected(false);
		getView().getEntryStationDefaultStatusPanel().getNotRepairedChkBox().setSelected(false);
		getView().getEntryStationDefaultStatusPanel().getNonRepairableChkBox().setSelected(false);
	}
	
	private void setScan(ActionEvent actionEvent, boolean allowScan) {
		List<QiEntryScreenDto> qiEntryScreenDtosPrimaryList = new ArrayList<QiEntryScreenDto>();
		for(QiEntryScreenDto qiEntryScreen:getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getItems()){
			if(getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getSelectionModel().getSelectedItem().equals(qiEntryScreen)){
				qiEntryScreen.setAllowScan(allowScan?(short)1:(short)0);
			}
			qiEntryScreenDtosPrimaryList.add(qiEntryScreen);
		}
		getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getItems().clear();
		getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getItems().addAll(qiEntryScreenDtosPrimaryList);
		getView().getEntryStationDefaultStatusPanel().getUpdateEntryScreenBtn().setDisable(false);
		getView().getEntryStationDefaultStatusPanel().getResetEntryScreenBtn().setDisable(false);
	}
	
	private void performVersionOperation(List<QiEntryScreenDto> addedEntryScreenList, 
			List<QiEntryScreenDto> removedEntryScreenList, String entryModel, boolean isVersionCreated) {
		if(removedEntryScreenList.size() > 0) {
			boolean updateModel = getView().getEntryStationDefaultStatusPanel().getEntryScreenModelObjectTablePane().getTable().getItems().size() == 0 ? true : false;
			boolean isScreenAssigned = getModel().findAllByEntryModel(entryModel).size() > 0 ? true : false;
				if(isVersionCreated) {
				if (updateModel && !isScreenAssigned) {
					// delete isUsed version 1 records
					getModel().removeVersionRelationship(entryModel, (short) 1);

					// Apply new version - set isUsed to 1
					getModel().updateVersionValue(entryModel, (short) 0, (short) 1, true);

				} else {
						// update only in Entry screen screenIsUsed to 0
						for(QiEntryScreenDto entryScreen : removedEntryScreenList)
							if(!getModel().isScreenUsedByStation(entryModel, entryScreen.getEntryScreen()))
								getModel().updateScreenIsUsed(entryModel, entryScreen.getEntryScreen(), (short)0);
					}
					
				} else {
					
					// update only in Entry screen screenIsUsed to 0
					for(QiEntryScreenDto entryScreen : removedEntryScreenList)
						if(!getModel().isScreenUsedByStation(entryModel, entryScreen.getEntryScreen()))
							getModel().updateScreenIsUsed(entryModel, entryScreen.getEntryScreen(), (short)0);
					
					if(updateModel && !isScreenAssigned) {
						//update isUsed version 1 to 0. for all screen
						getModel().updateVersionValue(entryModel, (short)1, (short) 0, updateModel);
					}
						
				}
		}
		
		if (addedEntryScreenList.size() > 0) {
			boolean isScreenUsed = getModel().isScreenUsed(entryModel);
			//update isUsed version 0 to 1
			if(!isScreenUsed) {
					getModel().updateVersionValue(entryModel, (short)0, (short) 1, !isVersionCreated);
			}
					// update only in Entry screen screenIsUsed to 1
					for (QiEntryScreenDto entryScreen : addedEntryScreenList)
						getModel().updateScreenIsUsed(entryModel, entryScreen.getEntryScreen(), (short)1);
		}
	}
	
	private boolean isScreenUsedByOtherProcessPoint(String entryModel,
			List<QiEntryScreenDto> qiStationAssignedEntryScreenDBList) {

		List<QiStationEntryScreen> allEntryScreenList = getModel().findAllByEntryModel(entryModel);
		List<QiEntryScreenDto> entryScreenList = ListUtils.subtract(allEntryScreenList,
				qiStationAssignedEntryScreenDBList);
		return (entryScreenList.size() > 0);
	}
}
