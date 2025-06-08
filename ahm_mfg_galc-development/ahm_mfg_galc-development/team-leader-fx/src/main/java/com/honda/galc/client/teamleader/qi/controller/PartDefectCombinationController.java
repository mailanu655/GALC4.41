package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.qi.model.PartDefectCombMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.PartDefectCombinationDialog;
import com.honda.galc.client.teamleader.qi.view.PartDefectCombinationMaintPanel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiPartDefectCombinationDto;
import com.honda.galc.dto.qi.QiPartLocationCombinationDto;
import com.honda.galc.entity.enumtype.QiActiveStatus;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PartDefectCombinationController</code> is the controller class for Part Defect Combination Panel.
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
 * <TD>26/08/2016</TD>
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
public class PartDefectCombinationController extends AbstractQiController<PartDefectCombMaintenanceModel, PartDefectCombinationMaintPanel> implements EventHandler<ActionEvent> {
	
	public enum ListType {
		ACTIVE, INACTIVE, BOTH;
	}
	private QiPartDefectCombinationDto selectedComb;
	
	public QiPartDefectCombinationDto getSelectedComb() {
		return selectedComb;
	}

	public PartDefectCombinationController(PartDefectCombMaintenanceModel model,PartDefectCombinationMaintPanel view) {
		super(model, view);
	}
	
	@Override
	public void initEventHandlers() {
		/**
		 *  This method is used to add listener on main panel table.
		 */
		if (isFullAccess()) {
			addPartDefectCombinationTableListener();
		}
		addRadioButtonListener();
	}
	
	/**
	 * This method is used to add listers to main panel radio buttons.
	 */
	private void addRadioButtonListener() {
		final ObjectTablePane<QiPartDefectCombinationDto> tablePane = getView().getPartDefectCombinationTablePane();
		
		getView().getAllRadioBtn().selectedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				tablePane.getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			}
		});
		getView().getActiveRadioBtn().selectedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				tablePane.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			}
		});
		getView().getInactiveRadioBtn().selectedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				tablePane.getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			}
		});
	}
	
	private void addPartDefectCombinationTableListener(){
		getView().getPartDefectCombinationTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiPartDefectCombinationDto>() {
			public void changed(
					ObservableValue<? extends QiPartDefectCombinationDto> observableValue,
					QiPartDefectCombinationDto oldValue,
					QiPartDefectCombinationDto newValue) {
				selectedComb = getView().getPartDefectCombinationTablePane().getSelectedItem();
				addContextMenuItems();
			}
		});
		getView().getPartDefectCombinationTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				addContextMenuItems();
			}
		});
		
		getView().getPartTablePane().getTable().getSelectionModel().getSelectedItems().addListener(new ListChangeListener<QiPartLocationCombinationDto>() {
			public void onChanged(Change<? extends QiPartLocationCombinationDto> arg0) {
				loadDefectAttributeData();
			}
		});
	}
	/**
	 * This method is called to add context menus to main panel.
	 */
	@Override
	public void addContextMenuItems()
	{
		clearDisplayMessage();
		ObjectTablePane<QiPartDefectCombinationDto> tablePane = getView().getPartDefectCombinationTablePane();
		if(selectedComb!=null){
			if(selectedComb.getStatus().equalsIgnoreCase(QiActiveStatus.ACTIVE.getName()) && !getView().getAllRadioBtn().isSelected())
			{
				String[] menuItems;
				if(tablePane.getTable().getSelectionModel().getSelectedItems().size() > 1) {
					menuItems = new String[] {
							QiConstant.INACTIVATE
						};
				}
				else {
					menuItems = new String[] {
							QiConstant.CREATE, QiConstant.UPDATE, QiConstant.INACTIVATE
						};
				}
				tablePane.createContextMenu(menuItems, this);
			}
			else
			{
				String[] menuItems = new String[] {
						QiConstant.CREATE, QiConstant.UPDATE
					};
				tablePane.createContextMenu(menuItems, this);
			}
		} else {
			String[] menuItems = new String[] {
					QiConstant.CREATE
				};
			tablePane.createContextMenu(menuItems, this);
		}
	}
	/**
	 * This is an implemented method of EventHandler interface. Called whenever an ActionEvent is triggered.
	 * Selecting context menu is an ActionEvent. So respective method is called based on which action event is triggered.
	 */
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();
			
			if(QiConstant.CREATE.equals(menuItem.getText())) createPartDefectCombination(actionEvent);
			else if(QiConstant.UPDATE.equals(menuItem.getText())) updatePartDefectCombination(actionEvent);
			else if(QiConstant.INACTIVATE.equals(menuItem.getText())) inactivatePartDefectCombination(actionEvent);
		} else if (actionEvent.getSource() instanceof LoggedRadioButton) {
			LoggedRadioButton radio = (LoggedRadioButton)actionEvent.getSource();
			if(!isSearchToggle(radio))  {
				loadDefectAttributeData();
			}
		} else if (actionEvent.getSource() instanceof UpperCaseFieldBean) {
			filterActivePartLocation();
		}
		else if(actionEvent.getSource() instanceof LoggedButton)  {
        	LoggedButton btn = (LoggedButton)actionEvent.getSource();
    		if(QiConstant.CLEAR_TEXT_SYMBOL.equals(btn.getText()))  {
        		getView().getFilterTextField().clear();
        		getView().getPartTablePane().clearSelection();
				getView().getPartDefectCombinationTablePane().setData(new ArrayList<QiPartDefectCombinationDto>());
        		getView().getPartTablePane().setData(new ArrayList<QiPartLocationCombinationDto>());
        		getView().getSearchTextGroup().getToggles().get(0).setSelected(true);
    		}
        }
	}
	
	private boolean isSearchToggle(LoggedRadioButton radio)  {
		if(radio != null && (QiConstant.PART_NAME.equals(radio.getId()) || QiConstant.DEFECT_TYPE.equals(radio.getId())))  return true;
		return false;
	}
	/**
	 * This method is used create a Part Defect Combination
	 * @param event
	 */
	public void createPartDefectCombination(ActionEvent event){
		PartDefectCombinationDialog dialog = new PartDefectCombinationDialog(QiConstant.CREATE, new QiPartDefectCombination(), getModel(),getApplicationId(),this);
		dialog.setScreenName(getView().getScreenName());
		dialog.setListType(getListType());
		dialog.showDialog();
		if(!dialog.isCancel())  {
			loadDefectAttributeData();
		}
	};
	/**
	 * This method is used to update a Part Defect Combination
	 * @param event
	 */
	public void updatePartDefectCombination(ActionEvent event){
		PartDefectCombinationDialog dialog = new PartDefectCombinationDialog(QiConstant.UPDATE, getView().getEntityFromDto(selectedComb), getModel(),getApplicationId(),this);
		dialog.setScreenName(getView().getScreenName());
		dialog.setListType(getListType());
		dialog.showDialog();
		if(!dialog.isCancel())  {
			loadDefectAttributeData(true, true);
		}			
	};
	/**
	 * This method is used to inactivate a Part Defect Combination.
	 * @param event
	 */
	public void inactivatePartDefectCombination(ActionEvent event){
		
		ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
		if(dialog.showReasonForChangeDialog(null))
		{
			clearDisplayMessage();
			try {
				List<QiPartDefectCombinationDto> partLocCombList = getView().getPartDefectCombinationTablePane().getSelectedItems();
				List<Integer> partDefectIdList = new ArrayList<Integer>();
				if(getView().getActiveRadioBtn().isSelected()){
					for(QiPartDefectCombinationDto comb: partLocCombList){
						partDefectIdList.add(comb.getRegionalDefectCombinationId());
					}
				}
				List<QiPartDefectCombination> regionalAttributeList= getModel().findAllRegionalAttributesByPartDefectId(partDefectIdList);
				

				
				if(!regionalAttributeList.isEmpty()){
					boolean isPLCombinationUpdated = MessageDialog.confirm(ClientMainFx.getInstance().getStage(getApplicationId()), QiCommonUtil.getMessage("part defect combination(s)",0,regionalAttributeList.size(),0).toString());
					if(!isPLCombinationUpdated)
						return;
					else{
						String returnValue=isLocalSiteImpacted(partDefectIdList,getView().getStage());
						if(returnValue.equals(QiConstant.NO_LOCAL_SITES_CONFIGURED)){
							return;
						}
						else if(returnValue.equals(QiConstant.LOCAL_SITES_IMPACTED)){
							publishErrorMessage("Inactivation of  PDC(s) affects Local Sites");
							return;
						}
					}
						
				}

				for(QiPartDefectCombinationDto comb: partLocCombList)
				{
					getModel().updatePartDefectCombStatus(comb.getRegionalDefectCombinationId(), (short)0);
					
					//call to capture audit information
					QiPartDefectCombination partDefectCombination = getView().getEntityFromDto(comb);
					QiPartDefectCombination partDefectCombinationCloned = (QiPartDefectCombination) partDefectCombination.deepCopy();
					partDefectCombinationCloned.setActive(true);
					AuditLoggerUtil.logAuditInfo(partDefectCombinationCloned, partDefectCombination,
							dialog.getReasonForChangeText(), getView().getScreenName(),
							getModel().getAuditPrimaryKeyValue(
									partDefectCombinationCloned.getRegionalDefectCombinationId()),getUserId());
				}
				if(partLocCombList != null && partLocCombList.size() > 0)  {
					removeSelectedFromList();
				}
			}catch (Exception e) {
				handleException("An error occured in updatePartDefectCombStatus method ","Failed to update status", e);
			}
		}
		else  {
			return;
		}
	};
	
	public void removeSelectedFromList()  {
		getView().getPartDefectCombinationTablePane().removeSelected();
		getView().getPartDefectCombinationTablePane().clearSelection();
		
	}
	
	public ListType getListType() {
		if(getView().getAllRadioBtn().isSelected()) {
			return ListType.BOTH;
		}
		else if(getView().getActiveRadioBtn().isSelected())
				return ListType.ACTIVE;
		else  {
			return ListType.INACTIVE;
		}
	}
	
	private void loadDefectAttributeData() {
		loadDefectAttributeData(false, false);
	}

	private void loadDefectAttributeData(boolean isPreserveListPosition, boolean isPreserveSelect) {
		List<QiPartLocationCombinationDto> selectedParts = getView().getPartTablePane().getSelectedItems();
		if (selectedParts == null || selectedParts.size() == 0) {
			getView().getPartDefectCombinationTablePane().setData(new ArrayList<QiPartDefectCombinationDto>());
		} else {
			getView().reload(selectedParts, isPreserveListPosition, isPreserveSelect);
		}
	}

	private void filterActivePartLocation() {
		String filter = getView().getFilterTextField().getText();
		int whichFilter = getView().getSearchGroupRadioButtonValue();
		if (StringUtils.isBlank(filter) || StringUtils.trim(filter).length() < 3) {
			getView().getPartTablePane().setData(new ArrayList<QiPartLocationCombinationDto>());
			displayErrorMessage("Please enter minimal 3 char to filter");
			return;
		}
		clearDisplayMessage();
		List<QiPartLocationCombinationDto> partLocationList = getModel()
				.findActivePartLocCombByFilter(StringUtils.trim(filter), whichFilter);
		getView().getPartTablePane().setData(partLocationList);
	}
}
