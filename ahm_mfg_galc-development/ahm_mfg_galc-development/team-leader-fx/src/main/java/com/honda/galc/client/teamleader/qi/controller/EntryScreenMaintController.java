package com.honda.galc.client.teamleader.qi.controller;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import com.honda.galc.client.teamleader.qi.model.EntryScreenMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.EntryScreenDialog;
import com.honda.galc.client.teamleader.qi.view.EntryScreenMaintPanel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.entity.qi.QiEntryScreen;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombination;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiStationEntryScreen;
import com.honda.galc.entity.qi.QiTextEntryMenu;
import com.honda.galc.util.AuditLoggerUtil;
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */

public class EntryScreenMaintController extends AbstractQiController<EntryScreenMaintenanceModel, EntryScreenMaintPanel> implements EventHandler<ActionEvent> {

	private QiEntryScreenDto qiEntryScreen; 
	private List<QiEntryScreenDto> qiEntryScreenList;
	private QiEntryScreen selectedEntryScreenCloned;

	public EntryScreenMaintController(EntryScreenMaintenanceModel model,EntryScreenMaintPanel view) {
		super(model, view);
	}

	@Override
	public void initEventHandlers() {
		if (isFullAccess()) {
			addEntryScreenTableListners();
		}
	}

	public void addContextMenuItems()
	{
		List<String> menuItemsList = new ArrayList<String>();
		qiEntryScreenList = getView().getEntryScreenTablePane().getSelectedItems();
		if(qiEntryScreenList.size()==1){
			
			boolean isVersionCreated = getModel().isVersionCreated(getView().getEntryScreenTablePane().getSelectedItem().getEntryModel());
			
			if(getView().getEntryScreenTablePane().getSelectedItem().getIsUsedVersion() == 1 && isVersionCreated) {
				menuItemsList.add(QiConstant.CREATE);
			} else {
				menuItemsList.add(QiConstant.CREATE);
				menuItemsList.add(QiConstant.UPDATE);
			}
			
			if(getView().getAllRadioBtn().isSelected())
			{
				getView().getEntryScreenTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			}
			else if (getView().getActiveRadioBtn().isSelected()) {
				if (getView().getEntryScreenTablePane().getSelectedItem().getIsUsedVersion() == 1 && isVersionCreated) {
					getView().getEntryScreenTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				} else {
					menuItemsList.add(QiConstant.INACTIVATE);
					getView().getEntryScreenTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				}
			}
			else
			{
				menuItemsList.add(QiConstant.REACTIVATE);
				getView().getEntryScreenTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			}
		}else if(qiEntryScreenList.size()>1){
			if(getView().getActiveRadioBtn().isSelected()){
				menuItemsList.add(QiConstant.INACTIVATE);
			}else{
				menuItemsList.add(QiConstant.REACTIVATE);
			}
		}
		else{

			menuItemsList.add(QiConstant.CREATE);
		}
		getView().getEntryScreenTablePane().createContextMenu((String[]) menuItemsList.toArray(new String[menuItemsList.size()]), this);
	}



	private void addEntryScreenTableListners(){
		getView().getEntryScreenTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiEntryScreenDto>() {
			public void changed(
					ObservableValue<? extends QiEntryScreenDto> arg0,
					QiEntryScreenDto arg1,
					QiEntryScreenDto arg2) {
				clearDisplayMessage();
				qiEntryScreen = getView().getEntryScreenTablePane().getSelectedItem();
				qiEntryScreenList = getView().getEntryScreenTablePane().getSelectedItems();
				addContextMenuItems();
				if(arg0!=null && arg0.getValue()!=null)
				   Logger.getLogger().check(arg0.getValue().getEntryScreen() + " Entry Screen row selected");
			}
		});

		getView().getEntryScreenTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				addContextMenuItems();
			}
		});
	}

	/**
	 * This method is added because initEventHandlers() is called before the selection listener is triggered.
	 */


	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();
			
			Logger.getLogger().check(menuItem.getText() + " menu selected");
			
			if(QiConstant.CREATE.equals(menuItem.getText())) createEntryScreen(actionEvent);
			else if(QiConstant.UPDATE.equals(menuItem.getText()))updateEntryScreen(actionEvent,qiEntryScreen);
			else if(QiConstant.INACTIVATE.equals(menuItem.getText())) updateEntryScreenStatus(actionEvent,false);
			else if(QiConstant.REACTIVATE.equals(menuItem.getText())) updateEntryScreenStatus(actionEvent,true);
		} 
		if(actionEvent.getSource() instanceof LoggedRadioButton){
			getView().reload(getView().getFilterTextData());	
		}
		if(actionEvent.getSource() instanceof UpperCaseFieldBean){
			getView().reload(getView().getFilterTextData());
		}

	}

	private void createEntryScreen(ActionEvent event){		
		EntryScreenDialog dialog = new EntryScreenDialog(QiConstant.CREATE, new QiEntryScreenDto(), getModel(), getApplicationId());
		dialog.showDialog();
		getView().reload(getView().getFilterTextData());
	};

	private void updateEntryScreen(ActionEvent event,QiEntryScreenDto qiEntryScreen){
		EntryScreenDialog dialog = new EntryScreenDialog(QiConstant.UPDATE, qiEntryScreen, getModel(), getApplicationId());
		dialog.setScreenName(getView().getScreenName());
		dialog.showDialog();
		getView().reload(getView().getFilterTextData());
		if(getView().getAllRadioBtn().isSelected())
			getView().reload(getView().getFilterTextData());
		else if(getView().getActiveRadioBtn().isSelected())
			getView().reload(getView().getFilterTextData());
		else
			getView().reload(getView().getFilterTextData());
	};	

	private void updateEntryScreenStatus(ActionEvent actionEvent,boolean status)
	{
		clearDisplayMessage();
		try{
			if(qiEntryScreen.isActive()) {
				boolean checkVersion = false;
				for(QiEntryScreenDto entryScreen : qiEntryScreenList) {
					if(entryScreen.getIsUsedVersion() == (short) 1 && getModel().isVersionCreated(entryScreen.getEntryModel())){
						checkVersion = true;
						break;
					}
				}
				if(checkVersion) {
					StringBuilder message=new StringBuilder("Entry Screen(s) can not be updated as Version exists");
					MessageDialog.showError(getView().getStage(), message.toString());
					return;
				}
			}
			
			ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());			
			List<QiEntryScreenDefectCombination> entryScreenDefectCombList = new ArrayList<QiEntryScreenDefectCombination>();
			List<QiEntryScreenDefectCombination> qiEntryScreenDefectCombList = new ArrayList<QiEntryScreenDefectCombination>();	
			List<QiTextEntryMenu> entryScreenMenuCombList = new ArrayList<QiTextEntryMenu>();
			List<QiTextEntryMenu> qiEntryScreenMenuCombList = new ArrayList<QiTextEntryMenu>();	
			List<QiStationEntryScreen> entryScreenStationCombList = new ArrayList<QiStationEntryScreen>();
			List<QiStationEntryScreen> qiEntryScreenStationCombList = new ArrayList<QiStationEntryScreen>();
			List<QiLocalDefectCombination> entryScreenLocalDefectCombList = new ArrayList<QiLocalDefectCombination>();
			List<QiLocalDefectCombination> qiEntryScreenLocalDefectCombList = new ArrayList<QiLocalDefectCombination>();

			try {										
				if(qiEntryScreen.isActive()){
					for(QiEntryScreenDto entryScreen : qiEntryScreenList){
						qiEntryScreenDefectCombList =getModel().findAllAssociatedPartsByEntryScreen(entryScreen);
						entryScreenDefectCombList.addAll((Collection<? extends QiEntryScreenDefectCombination>) qiEntryScreenDefectCombList);
						qiEntryScreenMenuCombList =getModel().findAllAssociatedMenusByEntryScreen(entryScreen);
						entryScreenMenuCombList.addAll((Collection<? extends QiTextEntryMenu>) qiEntryScreenMenuCombList);
						qiEntryScreenStationCombList =getModel().findAllAssociatedStationsByEntryScreen(entryScreen);
						entryScreenStationCombList.addAll((Collection<? extends QiStationEntryScreen>) qiEntryScreenStationCombList);
						qiEntryScreenLocalDefectCombList =getModel().findAllAssociatedLocalDefectsByEntryScreen(entryScreen);
						entryScreenLocalDefectCombList.addAll((Collection<? extends QiLocalDefectCombination>) qiEntryScreenLocalDefectCombList);
					}
				}
				List<QiEntryScreenDefectCombination> uniqueList = new ArrayList<QiEntryScreenDefectCombination>(new HashSet<QiEntryScreenDefectCombination>(entryScreenDefectCombList));
				List<QiTextEntryMenu> uniqueListForMenu = new ArrayList<QiTextEntryMenu>(new HashSet<QiTextEntryMenu>(entryScreenMenuCombList));					
				List<QiStationEntryScreen> uniqueListForStation = new ArrayList<QiStationEntryScreen>(new HashSet<QiStationEntryScreen>(entryScreenStationCombList));
				List<QiLocalDefectCombination> uniqueListForLocalDefects = new ArrayList<QiLocalDefectCombination>(new HashSet<QiLocalDefectCombination>(entryScreenLocalDefectCombList));
				if(uniqueList.size()>0 || uniqueListForMenu.size()>0 || uniqueListForStation.size()>0 || uniqueListForLocalDefects.size()>0){
					StringBuilder message=new StringBuilder("The Entry Screen(s) being updated is associated with "); 
					if(entryScreenMenuCombList.size()>0){
						message.append(entryScreenMenuCombList.size() + " Menu(s), ");
					}
					if(entryScreenDefectCombList.size()>0){
						message.append(entryScreenDefectCombList.size() + " Part Defect Comb(s)");
					}		
					if(entryScreenStationCombList.size()>0){
						message.append(", ");
						message.append(entryScreenStationCombList.size() + " Qics Station Entry Screen(s)");
					}
					if(entryScreenLocalDefectCombList.size()>0){
						message.append(", ");
						message.append(entryScreenLocalDefectCombList.size() + " Local Defect Comb(s)");
					}
					MessageDialog.showError(getView().getStage(),message.toString());					
				}
				else{
					if(dialog.showReasonForChangeDialog(null))
					{
						getModel().updateEntryScreenStatus(convertDtoToEntity(getView().getEntryScreenTablePane().getTable().getSelectionModel().getSelectedItems(),status));
						List<QiEntryScreen> qiEntryScreenList=convertDtoToEntity(getView().getEntryScreenTablePane().getTable().getSelectionModel().getSelectedItems(),status);
						for(QiEntryScreen qiEntryScreen : qiEntryScreenList ){
							selectedEntryScreenCloned=(QiEntryScreen) qiEntryScreen.deepCopy();
							selectedEntryScreenCloned.setActive(!status);
							qiEntryScreen.setUpdateUser(getUserId());
							//call to prepare and insert audit data
							AuditLoggerUtil.logAuditInfo(selectedEntryScreenCloned, qiEntryScreen, dialog.getReasonForChangeText(), getView().getScreenName(),getUserId());
						}
						getView().reload(getView().getFilterTextData());
					}
					else
					{
						return;
					}
				}
			} catch (Exception e) {
				handleException("An error occured in updateEntryScreenStatus method ","Failed to Update status", e);
			}
		}catch (Exception e) {
			handleException("An error occurred in inactivate entry screen method ", "Failed to open inactivate Entry Screen popup ", e);
		}
	}

	private List<QiEntryScreen> convertDtoToEntity(ObservableList<QiEntryScreenDto> selectedEntryScreenList,boolean status) {
		List<QiEntryScreen> entryScreenList=new ArrayList<QiEntryScreen>();
		QiEntryScreen entryScreen;
		for(QiEntryScreenDto entryScreenDto:selectedEntryScreenList){
			entryScreen =new QiEntryScreen(entryScreenDto.getEntryScreen(),entryScreenDto.getEntryModel(),entryScreenDto.getIsUsedVersion());
			entryScreen.setProductType(entryScreenDto.getProductType());
			entryScreen.setEntryScreenDescription(entryScreenDto.getEntryScreenDescription());
			entryScreen.setUpdateUser(getUserId());
			entryScreen.setImage(entryScreenDto.isImage());
			entryScreen.setActive(status);
			entryScreenList.add(entryScreen);
		}
		return entryScreenList;
	}
}

