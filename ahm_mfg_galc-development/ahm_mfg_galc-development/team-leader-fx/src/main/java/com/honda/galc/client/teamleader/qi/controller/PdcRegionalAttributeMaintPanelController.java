package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.PdcRegionalAttributeMaintModel;
import com.honda.galc.client.teamleader.qi.view.PdcRegionalAttributeMaintDialog;
import com.honda.galc.client.teamleader.qi.view.PdcRegionalAttributeMaintPanel;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiPartLocationCombinationDto;
import com.honda.galc.dto.qi.QiRegionalAttributeDto;

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
 * 
 * <h3>PdcRegionalAttributeMaintPanelController Class description</h3>
 * <p>
 * PdcRegionalAttributeMaintPanelController is used to load data in TableView and perform to the action on the RadioButton (All, Activate, Inactivate )etc.
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
public class PdcRegionalAttributeMaintPanelController  extends AbstractQiController<PdcRegionalAttributeMaintModel, PdcRegionalAttributeMaintPanel> implements EventHandler<ActionEvent> {

	
	/**
	 * Instantiates a new pdc regional attribute maint panel controller.
	 *
	 * @param model the model
	 * @param view the view
	 */
	public PdcRegionalAttributeMaintPanelController(PdcRegionalAttributeMaintModel model, PdcRegionalAttributeMaintPanel view) {
		super(model, view);
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.client.mvc.AbstractController#initEventHandlers()
	 */
	@Override
	public void initEventHandlers() {
		if(isFullAccess()){
			addTableListener();
		}
	}

	/**
	 * Adds the listener.
	 */
	
	private void addTableListener(){
		getView().getRegionalAttributeMaintTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiRegionalAttributeDto>() {
			public void changed(
					ObservableValue<? extends QiRegionalAttributeDto> arg0, QiRegionalAttributeDto arg1, QiRegionalAttributeDto arg2) {
				addContextMenuItems();
			}
		});
		
		getView().getRegionalAttributeMaintTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				if(getView().getRegionalAttributeMaintTablePane() != null && getView().getRegionalAttributeMaintTablePane().getSelectedItems().size() < 1)
					addContextMenuItems();
			}
		});
		
		getView().getPartTablePane().getTable().getSelectionModel().getSelectedItems().addListener(new ListChangeListener<QiPartLocationCombinationDto>() {
			public void onChanged(Change<? extends QiPartLocationCombinationDto> arg0) {
				loadDefectAttributeData();
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see com.honda.galc.client.teamleader.qi.controller.AbstractQiController#addContextMenuItems()
	 */
	@Override
	public void addContextMenuItems() {
		clearDisplayMessage();
		List<QiRegionalAttributeDto> regionalAttributeMaintList = getView().getRegionalAttributeMaintTablePane().getTable().getSelectionModel().getSelectedItems();
		if(getView().getRegionalAttributeMaintTablePane().getTable().getContextMenu() != null)
			getView().getRegionalAttributeMaintTablePane().getTable().getContextMenu().getItems().clear();
		if(regionalAttributeMaintList != null && regionalAttributeMaintList.size() > 0 ) {
			String[] menuItems = new String[] {};
			if(getView().getAssignedRadioButton().isSelected()){
				menuItems = new String[] { QiConstant.UPDATE_ATTRIBUTE };
				getView().getRegionalAttributeMaintTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			} else if(getView().getNotAssignedRadioButton().isSelected()){
				menuItems = new String[] { QiConstant.ASSIGN_ATTRIBUTE };
				getView().getRegionalAttributeMaintTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			} else {
				getView().getRegionalAttributeMaintTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			}
			getView().getRegionalAttributeMaintTablePane().createContextMenu(menuItems, this);
		}
	}

	/**
	 * This method is to map the action event to context menu and radio button.
	 *
	 * @param actionEvent the action event
	 */
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();
			if(QiConstant.ASSIGN_ATTRIBUTE.equals(menuItem.getText())){
				assignAttribute(actionEvent);
			} else if(QiConstant.UPDATE_ATTRIBUTE.equals(menuItem.getText())) {
				updateAttribute(actionEvent);
			}
		}
		List<QiPartLocationCombinationDto> selectedParts = getView().getPartTablePane().getSelectedItems();
		if(actionEvent.getSource() instanceof LoggedRadioButton){
			LoggedRadioButton loggedRadioButton = (LoggedRadioButton) actionEvent.getSource();
			if(QiConstant.ASSIGNED_ALL.equals(loggedRadioButton.getId()) || QiConstant.ASSIGNED.equals(loggedRadioButton.getId()) ||
					QiConstant.NOT_ASSIGNED.equals(loggedRadioButton.getId())){
				
				if (selectedParts == null || selectedParts.size() == 0) {
					if(QiConstant.NOT_ASSIGNED.equals(loggedRadioButton.getId()))  {
						List<QiPartLocationCombinationDto> partLocationList = getModel().findUnassignedPartLocComb();
						getView().getPartTablePane().setData(partLocationList);
					}
					else  {
						getView().getRegionalAttributeMaintTablePane().setData(new ArrayList<QiRegionalAttributeDto>());
					}
				} else {
					getView().reload(selectedParts);
				}
			}
		}
		
        if(actionEvent.getSource() instanceof UpperCaseFieldBean){
            if(getView().getDefectFilterTextField().isFocused()) {
            	filterActivePartLocation();
            }
        }
        if(actionEvent.getSource() instanceof LoggedButton)  {
        	LoggedButton btn = (LoggedButton)actionEvent.getSource();
        	if(btn.getId().equalsIgnoreCase("Search"))  {
        		filterActivePartLocation();
            }
        	else if(QiConstant.CLEAR_TEXT_SYMBOL.equals(btn.getText()))  {
        		clearFilterTxt();
        		getView().getPartTablePane().clearSelection();
        		getView().getSearchTextGroup().getToggles().get(0).setSelected(true);
           	 	getView().getRegionalAttributeMaintTablePane().setData(new ArrayList<QiRegionalAttributeDto>());
        		getView().getPartTablePane().setData(new ArrayList<QiPartLocationCombinationDto>());
        	}
        }
	}
	
	/**
	 * This method is used to clear search text
	 */
	private void clearFilterTxt(){
		getView().getDefectFilterTextField().clear();
	}

	/**
	 * Assign attribute.
	 *
	 * @param event the event
	 */
	private void assignAttribute(ActionEvent event){
		clearDisplayMessage();
		try{
			PdcRegionalAttributeMaintDialog dialog = new PdcRegionalAttributeMaintDialog(QiConstant.ASSIGN_ATTRIBUTE + createTitle(), getView().getRegionalAttributeMaintTablePane().getSelectedItems(), getModel(),getApplicationId(),this);
			dialog.setScreenName(getView().getScreenName());
			dialog.showDialog();
			
			if(!dialog.isCancel())  {
				loadDefectAttributeData(true, false);
			}
		}catch(Exception e){
			handleException("An error occured in assignAttribute method ","Failed To Assign Attribute", e);
		}
	}
	
	
	/**
	 * Load defect attribute data.
	 */
	private void loadDefectAttributeData()  {
		loadDefectAttributeData(false, false);
	}
	/**
	 * Load defect attribute data.
	 */
	private void loadDefectAttributeData(boolean isPreserveListPostion, boolean isPreserveSelect){
		List<QiPartLocationCombinationDto> selectedParts = getView().getPartTablePane().getSelectedItems();
		if (selectedParts == null || selectedParts.size() == 0) {
			getView().getRegionalAttributeMaintTablePane().setData(new ArrayList<QiRegionalAttributeDto>());
		} else {
			getView().reload(selectedParts, isPreserveListPostion, isPreserveSelect);
		}
	}
	
	/**
	 * Update attribute.
	 *
	 * @param event the event
	 */
	private void updateAttribute(ActionEvent event){
		clearDisplayMessage();
		try{
			PdcRegionalAttributeMaintDialog dialog = new PdcRegionalAttributeMaintDialog(QiConstant.UPDATE_ATTRIBUTE + createTitle() , getView().getRegionalAttributeMaintTablePane().getSelectedItems(), getModel(),getApplicationId(), this);
			dialog.setScreenName(getView().getScreenName());
			dialog.showDialog();
			
			if(!dialog.isCancel())  {
				loadDefectAttributeData(true, true);
			}
		}catch(Exception e){
			handleException("An error occured in updateAttribute method ","Failed To Update Attribute", e);
		}
	}
	
	/**
	 * This method shows no. of PDC's selected in title of dialog
	 *
	 * @return String
	 */
	private String createTitle(){
		String selectedPDC=getView().getRegionalAttributeMaintTablePane().getSelectedItem().getFullPartName() + " "+getView().getRegionalAttributeMaintTablePane().getSelectedItem().getDefectTypeName()+
				" " +getView().getRegionalAttributeMaintTablePane().getSelectedItem().getDefectTypeName2();
		StringBuilder title = new StringBuilder();
		if(getView().getRegionalAttributeMaintTablePane().getSelectedItems()!=null && getView().getRegionalAttributeMaintTablePane().getSelectedItems().size()>1){
			title.append(" - "+getView().getRegionalAttributeMaintTablePane().getSelectedItems().size()).append(" QICS Part Defect Combinations ");
		}else{
			title.append(" - " + selectedPDC);
		}
		return title.toString();
	}
	
	public void removeSelectedFromList()  {
		getView().getRegionalAttributeMaintTablePane().removeSelected();
		getView().getRegionalAttributeMaintTablePane().clearSelection();
		
	}
	
	private void filterActivePartLocation() {
		String searchText = getView().getDefectFilterTextValue();
		int assignedFilter = getView().getAssignedGroupRadioButtonValue();
		int searchFilter = getView().getSearchGroupRadioButtonValue();
		List<QiPartLocationCombinationDto> partLocationList = new ArrayList<QiPartLocationCombinationDto>();
		if (StringUtils.isBlank(searchText) && assignedFilter == 0)    {  //search text is blank and Not-Assigned is selected
			partLocationList = getModel().findUnassignedPartLocComb();			
		}
		else if (searchFilter < 5 && (StringUtils.isBlank(searchText) || StringUtils.trim(searchText).length() < 3)) {
			displayErrorMessage("Please enter minimal 3 char to filter");
			return;
		}

		clearDisplayMessage();
		partLocationList = getModel().findActivePartLocCombByFilter(StringUtils.trim(searchText), searchFilter);
		getView().getPartTablePane().setData(partLocationList);
	}
}	
