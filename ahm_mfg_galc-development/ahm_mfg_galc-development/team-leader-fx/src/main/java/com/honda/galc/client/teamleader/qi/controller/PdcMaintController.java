package com.honda.galc.client.teamleader.qi.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;

import com.honda.galc.client.teamleader.qi.model.PdcLocalAttributeMaintModel;
import com.honda.galc.client.teamleader.qi.stationconfig.dto.ComboBoxDisplayDto;
import com.honda.galc.client.teamleader.qi.view.PdcLocalAttrDispQrCodeDialog;
import com.honda.galc.client.teamleader.qi.view.PdcLocalAttributeMaintDialog;
import com.honda.galc.client.teamleader.qi.view.PdcLocalAttributeMaintPanel;
import com.honda.galc.client.teamleader.qi.view.QiPdcHistoryDialog;
import com.honda.galc.client.teamleader.qi.view.UpdatePddaModelYearDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.qi.PdcRegionalAttributeMaintDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.qi.QiLocalDefectCombination;


/**
 * 
 * <h3>PdcMaintController Class description</h3>
 * <p>
 * PdcMaintController is used to load data in TableView and perform to the action on the RadioButton (All, Activate, Inactivate )etc.
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
public class PdcMaintController  extends AbstractQiController<PdcLocalAttributeMaintModel, PdcLocalAttributeMaintPanel> implements EventHandler<ActionEvent> {


	/**
	 * Instantiates a new pdc regional attribute maint panel controller.
	 *
	 * @param model the model
	 * @param view the view
	 */
	public PdcMaintController(PdcLocalAttributeMaintModel model, PdcLocalAttributeMaintPanel view) {
		super(model, view);
	}

	ChangeListener<String> plantComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
			logCheckMessage( new_val);
			loadProductComboBox(new_val);
		} 
	};
	
	ChangeListener<String> productTypeChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
			logCheckMessage( newValue);
			loadEntryModelComboBox(newValue);
		} 
	};
	
	ChangeListener<String> entryModelComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
			logCheckMessage( newValue);
			loadEntryDeptComboBox(newValue);
		} 
	};
	
	ChangeListener<ComboBoxDisplayDto> entryDeptComboBoxChangeListener = new ChangeListener<ComboBoxDisplayDto>() {
		public void changed(ObservableValue<? extends ComboBoxDisplayDto> ov,  ComboBoxDisplayDto oldValue, ComboBoxDisplayDto newValue) { 
			if(newValue != null  ) {
				logCheckMessage( newValue.getId());
			}
			getView().getEntryScreenComboBox().getItems().clear();
			getView().getEntryScreenComboBox().setPromptText("Select");
			if(newValue != null)  {;
			   loadEntryScreenComboBox(newValue.getId());
			}
		} 
	};
	
	ChangeListener<String> entryScreenComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
			logCheckMessage( newValue);
			getView().reload(newValue, getView().getDefectFilterTextValue());
		} 
	};
	
	public void logCheckMessage(String newValue)
	{
		if(!StringUtils.isEmpty(newValue))
			  Logger.getLogger().check(newValue.trim()+" is selected");
	}
	
	/* (non-Javadoc)
	 * @see com.honda.galc.client.mvc.AbstractController#initEventHandlers()
	 */
	@Override
	public void initEventHandlers() {
		if(isFullAccess()){
			addListener();
		}
		else{
			addComboBoxListener();
		}
	}

	/**
	 * Adds the listener.
	 */
	private void addListener() {
		clearDisplayMessage();
		addComboBoxListener();
		getView().getLocalAttributeMaintTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PdcRegionalAttributeMaintDto>() {
			public void changed(
					ObservableValue<? extends PdcRegionalAttributeMaintDto> arg0, PdcRegionalAttributeMaintDto arg1, PdcRegionalAttributeMaintDto arg2) {
				addContextMenuItems();
				// Enable/disable Update Pdda Model Year button
				if(arg2!=null && getView().getAssignedRadioButton().isSelected()){
					Logger.getLogger().check(arg2.getFullPartName()+ " is selected");
					getView().getUpdateModelYearButton().setDisable(false);
				}
				else{
					getView().getUpdateModelYearButton().setDisable(true);
				}
			}
		});

		getView().getLocalAttributeMaintTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				addContextMenuItems();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.client.teamleader.qi.controller.AbstractQiController#addContextMenuItems()
	 */
	@Override
	public void addContextMenuItems() {
		clearDisplayMessage();
		List<PdcRegionalAttributeMaintDto> localAttributeMaintList = getView().getLocalAttributeMaintTablePane().getTable().getSelectionModel().getSelectedItems();
		if(getView().getLocalAttributeMaintTablePane().getTable().getContextMenu() != null)
			getView().getLocalAttributeMaintTablePane().getTable().getContextMenu().getItems().clear();
		if(localAttributeMaintList != null ) {
			String[] menuItems = new String[] {};
			if(localAttributeMaintList.size() == 1) {
				PdcRegionalAttributeMaintDto dto = getView().getLocalAttributeMaintTablePane().getSelectedItem();
				String entryModel = getView().getEntryModelComboBox().getSelectionModel().getSelectedItem();
				
				if(getView().getAssignedRadioButton().isSelected()) {
					if( dto.getIsUsed() == (short) 1 && getModel().isVersionCreated(entryModel))
						getView().getLocalAttributeMaintTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
					else {
						menuItems = new String[] { QiConstant.UPDATE_ATTRIBUTE ,  QiConstant.HISTORY} ;
						getView().getLocalAttributeMaintTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
					}
				} else if(getView().getNotAssignedRadioButton().isSelected()) {
					if( dto.getIsUsed() == (short) 1 && getModel().isVersionCreated(entryModel))
						getView().getLocalAttributeMaintTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
					else {
						menuItems = new String[] { QiConstant.ASSIGN_ATTRIBUTE };
						getView().getLocalAttributeMaintTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
					}
				} else 
					getView().getLocalAttributeMaintTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
				
			}else if(localAttributeMaintList.size() > 0 ){
				if(getView().getAssignedRadioButton().isSelected()) {
					menuItems = new String[] { QiConstant.UPDATE_ATTRIBUTE};
					getView().getLocalAttributeMaintTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				} else if(getView().getNotAssignedRadioButton().isSelected()) {
					menuItems = new String[] { QiConstant.ASSIGN_ATTRIBUTE };
					getView().getLocalAttributeMaintTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				} else {
					getView().getLocalAttributeMaintTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
				}
			}
			getView().getLocalAttributeMaintTablePane().createContextMenu(menuItems, this);
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
			} else if(QiConstant.HISTORY.equals(menuItem.getText())) {
				showHistory(actionEvent);
			}
		}
		if(actionEvent.getSource() instanceof LoggedRadioButton){
			LoggedRadioButton loggedRadioButton = (LoggedRadioButton) actionEvent.getSource();
			if("ASSIGNED_ALL".equals(loggedRadioButton.getId()) || "ASSIGNED".equals(loggedRadioButton.getId()) ||
					"NOT_ASSIGNED".equals(loggedRadioButton.getId())){
				String entryScreen = getView().getEntryScreenComboBox().getValue() == null ? "" : getView().getEntryScreenComboBox().getValue().toString();
				if(entryScreen.equals(""))
					return;
				getView().reload(entryScreen, getView().getDefectFilterTextValue() == null ? "" : getView().getDefectFilterTextValue());
			}
		}

		if(actionEvent.getSource() instanceof UpperCaseFieldBean){
			if(getView().getDefectFilterTextField().isFocused()) {
				loadDefectAttributeData();
			}
		}

		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			if (QiConstant.REFRESH.equalsIgnoreCase(loggedButton.getId())){
				refreshBtnAction();
				EventBusUtil.publish(new StatusMessageEvent("Refreshed successfully", StatusMessageEventType.INFO));
			}
			else if(actionEvent.getSource().equals(getView().getDisplayQrCodeButton())){
				displayQrCodeDialog();
			}
			else if(actionEvent.getSource().equals(getView().getUpdateModelYearButton())){
				updatePddaModelYear();
			}
		}
		
		
	}

	/**
	 * This method is used to refresh the data.
	 */
	public void refreshBtnAction() {
		
		String productType = null;
		String entryModel = null;
		String entryScreen = null;
		String entryDept=null;
		if(null !=getView().getProductTypesComboBox().getSelectionModel().getSelectedItem() && null !=getView().getEntryModelComboBox().getSelectionModel().getSelectedItem() 
				&&  null !=getView().getEntryScreenComboBox().getSelectionModel().getSelectedItem()
				&& null != (getView().getEntryDeptComboBoxSelectedItem())){
			productType = getView().getProductTypesComboBox().getSelectionModel().getSelectedItem().toString();
			entryModel =getView().getEntryModelComboBox().getSelectionModel().getSelectedItem().toString();
			entryScreen = getView().getEntryScreenComboBox().getSelectionModel().getSelectedItem().toString();
			entryDept=getView().getEntryDeptComboBoxSelectedId();
		}
		refreshPlant();                   
		refreshProductType(productType);
		refreshEntryModel(entryModel);	
		refreshEntryDept(getView().getEntryDeptComboBoxSelectedItem());
		refreshEntryScreen(entryScreen);
		
	}

	/**
	 * This method is used to refresh the plant dropdown.
	 */

	private void refreshPlant() {
		String plant;
		if (null != getView().getPlantComboBox().getSelectionModel().getSelectedItem()) {
			plant = getView().getPlantComboBox().getValue().toString();
			getView().getPlantComboBox().getItems().clear();
			List<String> plantList = getModel().findAllBySite(getView().getSite().getText());
			if (plantList.size() > 0) {
				getView().getPlantComboBox().getItems().addAll(plantList);
				if(plantList.contains(plant))
					getView().getPlantComboBox().setValue(plant);
				
			}  
		}
		else{
			getView().getPlantComboBox().getItems().clear();
			getView().loadPlantComboBoxData();
		}
	}

	private void refreshProductType(String productType) {
		if(null!= getView().getPlantComboBox().getValue()){
		String plant;
		if (null != productType) {
			plant = getView().getPlantComboBox().getSelectionModel().getSelectedItem().toString();
			if (plant != null) {
				getView().getProductTypesComboBox().getItems().clear();
				List<String> productTypeList = getModel().findAllProductTypes();
				if (productTypeList.size() > 0) {
					getView().getProductTypesComboBox().getItems().addAll(productTypeList);
					if(productTypeList.contains(productType))
						getView().getProductTypesComboBox().setValue(productType);

				}
			}                   
		 }
		}else{
			getView().getProductTypesComboBox().getItems().clear();
			getView().getEntryModelComboBox().getItems().clear();
			getView().getEntryScreenComboBox().getItems().clear();
			getView().clearEntryDeptComboBox();
			getView().getLocalAttributeMaintTablePane().getTable().getItems().clear();
		}
	}

	private void refreshEntryModel(String entryModel) {
		if(null!= getView().getProductTypesComboBox().getValue()){
		String productType;
		if (null != entryModel) {
			productType = getView().getProductTypesComboBox().getSelectionModel().getSelectedItem().toString();
			if (productType != null) {
				getView().getEntryModelComboBox().getItems().clear();
				List<String> entryModelList = getModel().findAllByPlantAndProductType(getView().getPlantComboBox().getValue().toString(), productType);
				if (entryModelList.size() > 0) {
					getView().getEntryModelComboBox().getItems().addAll(entryModelList);
					if(entryModelList.contains(entryModel))
						getView().getEntryModelComboBox().setValue(entryModel);

				}
			} 
		  }
		}else{
			getView().getEntryModelComboBox().getItems().clear();
			getView().getEntryScreenComboBox().getItems().clear();
			getView().getLocalAttributeMaintTablePane().getTable().getItems().clear();
		}
	}

	private void refreshEntryScreen(String entryScreen) {
		if(null!= getView().getEntryModelComboBox().getValue()){
		String entryModel;
		if (null != entryScreen) {
			entryModel = getView().getEntryModelComboBox().getSelectionModel().getSelectedItem().toString();
			if (entryModel != null) {
				String entryDept=getView().getEntryDeptComboBoxSelectedId();
				getView().getEntryScreenComboBox().getItems().clear();
				List<String> entryScreenList = getModel().findAllByPlantProductTypeEntryModelAndEntryDept(getView().getPlantComboBox().getValue().toString(), getView().getProductTypesComboBox().getValue().toString(), entryModel,entryDept);
				if (entryScreenList.size() > 0) {
					getView().getEntryScreenComboBox().getItems().addAll(entryScreenList);
					if(entryScreenList.contains(entryScreen))
						getView().getEntryScreenComboBox().setValue(entryScreen);
					getView().reload(entryScreen,getView().getDefectFilterTextValue());

				}
			} 
		  }
		}else{
			getView().getEntryScreenComboBox().getItems().clear();
			getView().getLocalAttributeMaintTablePane().getTable().getItems().clear();
		}
	}



	/**
	 * Adds the combo box listener.
	 */
	private void addComboBoxListener() {
		addPlantComboBoxListener();
		addProductTypeComboBoxListener();
		addEntryModelComboBoxListener();
		addEntryDeptComboBoxListener();
		addEntryScreenComboBoxListener();
	}

	/**
	 * Adds the plant combo box listener.
	 */
	private void addPlantComboBoxListener() {
		getView().getPlantComboBox().getSelectionModel().selectedItemProperty().addListener(plantComboBoxChangeListener);
	}

	/**
	 * Load product combo box.
	 *
	 * @param plant 
	 */
	private void loadProductComboBox(String plant) {
		getView().getProductTypesComboBox().getItems().clear();
		getView().getProductTypesComboBox().setPromptText("Select");
		getView().getProductTypesComboBox().getItems().addAll(getModel().findAllProductTypes());
	}

	/**
	 * Adds the product type combo box listener.
	 */
	private void addProductTypeComboBoxListener() {
		getView().getProductTypesComboBox().getSelectionModel().selectedItemProperty().addListener(productTypeChangeListener);
	}

	/**
	 * Load entry model combo box.
	 *
	 * @param productType 
	 */
	private void loadEntryModelComboBox(String productType) {
		String plant ="";
		if(getView().getPlantComboBox()!= null &&  getView().getPlantComboBox().getValue()!=null)
			plant=getView().getPlantComboBox().getValue().toString();
		getView().getEntryModelComboBox().getItems().clear();
		getView().getEntryScreenComboBox().getItems().clear();
		getView().getEntryModelComboBox().setPromptText("Select");
		getView().getEntryModelComboBox().getItems().addAll(getModel().findAllByPlantAndProductType(plant, productType));
	}

	/**
	 * Adds the entry model combo box listener.
	 */
	private void addEntryModelComboBoxListener() {
		getView().getEntryModelComboBox().getSelectionModel().selectedItemProperty().addListener(entryModelComboBoxChangeListener);
	}

	/**
	 * Load entry screen combo box.
	 *
	 * @param entryModel the entry model
	 */
	private void loadEntryScreenComboBox(String entryDept) {
		String plant = getView().getPlantComboBox().getValue() == null ? "" : getView().getPlantComboBox().getValue().toString();
		String productType = getView().getProductTypesComboBox().getValue() == null ? "" : getView().getProductTypesComboBox().getValue().toString();
		String entryModel = getView().getEntryModelComboBox().getValue() == null ? "" : getView().getEntryModelComboBox().getValue().toString();
		getView().getEntryScreenComboBox().getItems().clear();
		getView().getEntryScreenComboBox().setPromptText("Select");
		getView().getEntryScreenComboBox().getItems().addAll(getModel().findAllByPlantProductTypeEntryModelAndEntryDept(plant, productType, entryModel,entryDept));
	}

	/**
	 * Adds the entry screen combo box listener.
	 */
	private void addEntryScreenComboBoxListener() {
		getView().getEntryScreenComboBox().getSelectionModel().selectedItemProperty().addListener(entryScreenComboBoxChangeListener);
	}

	/**
	 * Load defect attribute data.
	 */
	private void loadDefectAttributeData(){
		String entryScreen = getView().getEntryScreenComboBox().getValue() == null ? "" : getView().getEntryScreenComboBox().getValue().toString();
		if(entryScreen.equals(""))
			return;
		if(getView().getAllRadioButton().isSelected())
			getView().reload(entryScreen, getView().getDefectFilterTextValue());
		else if(getView().getAssignedRadioButton().isSelected())
			getView().reload(entryScreen, getView().getDefectFilterTextValue());
		else
			getView().reload(entryScreen, getView().getDefectFilterTextValue());
	}

	/**
	 * Assign attribute.
	 *
	 * @param event the event
	 */
	private void assignAttribute(ActionEvent event){
		clearDisplayMessage();
		String entryModel = getView().getEntryModelComboBox().getSelectionModel().getSelectedItem();
		boolean isVersionCreated  = getModel().isVersionCreated(entryModel);
		boolean currentVersion = false;
		
		for(PdcRegionalAttributeMaintDto regionalAttribute : getView().getLocalAttributeMaintTablePane().getSelectedItems()) {
			if(regionalAttribute.getIsUsed() == (short) 1 && isVersionCreated) {
				currentVersion = true;
				break;
			}
		}
		if(currentVersion) {
			StringBuilder message = new StringBuilder("Attribute(s) can not be assigned as Version exists");
			MessageDialog.showError(getView().getStage(), message.toString());
			return;
		}
		try{
			PdcLocalAttributeMaintDialog dialog = new PdcLocalAttributeMaintDialog(QiConstant.ASSIGN_ATTRIBUTE + createTitle(), getView(), getModel());
			dialog.setScreenName(getView().getScreenName());
			dialog.showDialog();
			String entryScreen = getView().getEntryScreenComboBox().getValue() == null ? "" : getView().getEntryScreenComboBox().getValue().toString();
			if(!dialog.isCancel())  {
				getView().reload(entryScreen, getView().getDefectFilterTextValue(), true, false);
			}
		}catch(Exception e){
			handleException("An error occured in assignAttribute method ","Failed To Assign Attribute", e);
		}
	}

	/**
	 * Update attribute.
	 *
	 * @param event the event
	 */
	private void updateAttribute(ActionEvent event){
		clearDisplayMessage();
		String entryModel = getView().getEntryModelComboBox().getSelectionModel().getSelectedItem();
		boolean isVersionCreated  = getModel().isVersionCreated(entryModel);
		boolean currentVersion = false;
		
		for(PdcRegionalAttributeMaintDto regionalAttribute : getView().getLocalAttributeMaintTablePane().getSelectedItems()) {
			if(regionalAttribute.getIsUsed() == (short) 1 && isVersionCreated) {
				currentVersion = true;
				break;
			}
		}
		if(currentVersion) {
			StringBuilder message = new StringBuilder("Attribute(s) can not be updated as Version exists");
			MessageDialog.showError(getView().getStage(), message.toString());
			return;
		}
		try{
			String entryScreen = StringUtils.trimToEmpty(getView().getEntryScreenComboBox().getValue()); 
			PdcLocalAttributeMaintDialog dialog = new PdcLocalAttributeMaintDialog(QiConstant.UPDATE_ATTRIBUTE + createTitle() + " - " + entryScreen, getView(), getModel());
			dialog.loadUpdatedAssignmentData();

			dialog.setScreenName(getView().getScreenName());
			dialog.showDialog();
			if(!dialog.isCancel())  {
				getView().reload(entryScreen, getView().getDefectFilterTextValue(), true, true);
			}
		}catch(Exception e){
			handleException("An error occured in updateAttribute method ","Failed To Update Attribute", e);
		}
	}
	
	
	
	/**
	 * Load entry dept combo box.
	 *
	 * @param entryDept the entry dept
	 */
	private void loadEntryDeptComboBox(String entryModel) {
		String plant = getView().getPlantComboBox().getValue() == null ? "" : getView().getPlantComboBox().getValue().toString();
		String productType = getView().getProductTypesComboBox().getValue() == null ? "" : getView().getProductTypesComboBox().getValue().toString();
		getView().clearEntryDeptComboBox();
		getView().getEntryDeptComboBox().setPromptText("Select");
		List<Division> allDiv = getModel().findAllDivisionByPlantProductTypeAndEntryModel(plant, productType, entryModel);
		List<ComboBoxDisplayDto> dtoList = ComboBoxDisplayDto.getDivisionUniqueList(allDiv);
		if (dtoList != null)
			getView().getEntryDeptComboBox().getItems().addAll(dtoList);
	}
	

	/**
	 * Adds the entry dept combo box listener.
	 */
	private void addEntryDeptComboBoxListener() {
		getView().getEntryDeptComboBox().getSelectionModel().selectedItemProperty().addListener(entryDeptComboBoxChangeListener);
	}
	
	private void refreshEntryDept(ComboBoxDisplayDto entryDept) {
		if(null!= getView().getEntryModelComboBox().getValue()){
			String entryModel;
			if (null != entryDept) {
				entryModel = getView().getEntryModelComboBox().getSelectionModel().getSelectedItem().toString();
				if (entryModel != null) {
					getView().clearEntryDeptComboBox();
					List<Division> allDiv = getModel().findAllDivisionByPlantProductTypeAndEntryModel(getView().getPlantComboBox().getValue().toString(), getView().getProductTypesComboBox().getValue().toString(), entryModel);
					List<ComboBoxDisplayDto> dtoList = ComboBoxDisplayDto.getDivisionUniqueList(allDiv);
					if (dtoList != null && !dtoList.isEmpty()) {
						getView().getEntryDeptComboBox().getItems().addAll(dtoList);
						if(dtoList.contains(entryDept))  {
							getView().getEntryDeptComboBox().setValue(entryDept);
						}
					}
				} 
			}
		}else{
			getView().clearEntryDeptComboBox();;
			getView().getLocalAttributeMaintTablePane().getTable().getItems().clear();
		}
	}
	
	/**
	 * This method shows no. of PDC's selected in title of dialog
	 *
	 * @return String
	 */
	private String createTitle(){
		String selectedPDC = getView().getLocalAttributeMaintTablePane().getSelectedItem().getFullPartName()+" "+getView().getLocalAttributeMaintTablePane().getSelectedItem().getDefectTypeName()+
				" "+getView().getLocalAttributeMaintTablePane().getSelectedItem().getDefectTypeName2();
		StringBuilder title = new StringBuilder();
		if(getView().getLocalAttributeMaintTablePane().getSelectedItems()!=null && getView().getLocalAttributeMaintTablePane().getSelectedItems().size()>1){
			title.append(" - "+getView().getLocalAttributeMaintTablePane().getSelectedItems().size()).append(" QICS Part Defect Combinations ");
		}else{
			title.append(" - " + selectedPDC);
		}
		return title.toString();
	}
	
	/**
	 * This method displays dialog containing PDC's along with their QR Code.
	 *
	 * @return String
	 */
	private void displayQrCodeDialog(){
		String entryScreen = getView().getEntryScreenComboBox().getValue() == null ? "" : getView().getEntryScreenComboBox().getValue().toString();
		PdcLocalAttrDispQrCodeDialog dialog = new PdcLocalAttrDispQrCodeDialog("Display QR Code", getModel(), entryScreen, getView().getLocalAttributeMaintTablePane().getTable().getItems());
		dialog.setScreenName(getView().getScreenName());
		dialog.showDialog();
	}
	
	/**
	 * This method displays dialog to update Pdda Model Year.
	 *
	 * @return String
	 */
	private void updatePddaModelYear() {
		UpdatePddaModelYearDialog updatePddaModelYearDialog=new UpdatePddaModelYearDialog("Update PDDA Model Year",getApplicationId(),getModel(),getView().getLocalAttributeMaintTablePane().getSelectedItems());
		updatePddaModelYearDialog.showDialog();
		if(!updatePddaModelYearDialog.isCancel())  {
			String entryScreen = getView().getEntryScreenComboBox().getValue() == null ? "" : getView().getEntryScreenComboBox().getValue().toString();
			getView().reload(entryScreen, getView().getDefectFilterTextValue(), true, true);
		}
		
	}
	private void showHistory(ActionEvent event)   {
		
		List<PdcRegionalAttributeMaintDto> defectCombinationDtoList = getView().getLocalAttributeMaintTablePane().getSelectedItems();
		PdcRegionalAttributeMaintDto rDto = defectCombinationDtoList.get(0);
		String entryScreen = getView().getEntryScreenComboBox().getValue().toString();
		String entryModel = getView().getEntryModelComboBox().getValue();
		if(rDto != null)  {
			QiLocalDefectCombination localDefectCombination = getModel().findByRegionalDefectCombinationId(
					rDto.getRegionalDefectCombinationId(), entryScreen, entryModel, rDto.getIsUsed());
			if(localDefectCombination != null)  {
				QiPdcHistoryDialog qiPdcHistoryDialog = new QiPdcHistoryDialog("PDC History", getModel(), getView().getStage(), entryScreen, localDefectCombination, rDto.getFullPartName());
				qiPdcHistoryDialog.showDialog(); 
			}
		}
	}
}	
