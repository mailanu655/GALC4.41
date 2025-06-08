package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.honda.galc.client.teamleader.qi.model.PdcToEntryScreenAssignmentModel;
import com.honda.galc.client.teamleader.qi.view.CopyPartDefectCombinationDialog;
import com.honda.galc.client.teamleader.qi.view.PdcToEntryMenuDialog;
import com.honda.galc.client.teamleader.qi.view.PdcToEntryScreenAssignmentMainPanel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.dto.qi.QiRegionalPartDefectLocationDto;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombination;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombinationId;
import com.honda.galc.entity.qi.QiExternalSystemDefectMap;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiTextEntryMenu;
import com.honda.galc.entity.qi.QiTextEntryMenuId;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.AuditLoggerUtil;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PartLocationCombinationController</code> is the controller class for Part Location Combination Panel.
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
 * <TD>14/07/2016</TD>
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
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
public class PdcToEntryScreenAssignmentController extends AbstractQiController<PdcToEntryScreenAssignmentModel, PdcToEntryScreenAssignmentMainPanel> implements EventHandler<ActionEvent> {

	private boolean isLocalPdcDeAssigned = false;
	private boolean isRefresh=false;
	private int maxPdcToEntryScreen = 100;

	//existing assigned PDCs in DB
	private Set<QiRegionalPartDefectLocationDto> existingAssignedPdcSet = new HashSet<QiRegionalPartDefectLocationDto>();

	//currently assigned PDCs in cache
	private Set<QiRegionalPartDefectLocationDto> assignedPdcSet = new HashSet<QiRegionalPartDefectLocationDto>();

	public PdcToEntryScreenAssignmentController(PdcToEntryScreenAssignmentModel model,PdcToEntryScreenAssignmentMainPanel view) {
		super(model, view);
		maxPdcToEntryScreen = PropertyService.getPropertyBean(QiPropertyBean.class).getMaxPdcToEntryScreen();			
	}

	private final ChangeListener<? super String> plantChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue ov, String t, String newValue) {
			if(!isRefresh){
				if(null!=getView().getEntryModelComboBox().getItems()){
					populateEntryModelComboBox();
				}
			}
		}
	};

	private final ChangeListener<String> productTypeChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue ov, String t, String newValue) {
			if(!isRefresh){
				getView().getEntryScreenPanel().getTable().getItems().removeAll(getView().getEntryScreenPanel().getTable().getItems());
				populateEntryModelComboBox();
				Logger.getLogger().check("Product Type : " + newValue.trim() + " selected");
			}
		}
	};

	private final ChangeListener<String> entryModelChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue ov, String t, String t1) {
			if(!isRefresh){
				disableAndResetAll();
				getView().getEntryScreenPanel().setData(getModel().getEntryScreenData(getView().getEntryModelComboBox().getSelectionModel().getSelectedItem(),
						StringUtils.trimToEmpty(getView().getPlantComboBox().getSelectionModel().getSelectedItem())));
				enableDisableArrowBtn();
				resetRadioButton();
				Logger.getLogger().check("Entry Model : " + t1 + " selected");
			}
		}
	};

	private final ChangeListener<QiRegionalPartDefectLocationDto> availablePartDefectPanelChangeListener = new ChangeListener<QiRegionalPartDefectLocationDto>() {
		public void changed(ObservableValue<? extends QiRegionalPartDefectLocationDto> arg0,
				QiRegionalPartDefectLocationDto arg1, QiRegionalPartDefectLocationDto arg2) {
			if(!isRefresh){
				clearDisplayMessage();
				if(null==getView().getAvailablePartDefectPanel().getSelectedItems() || getView().getAvailablePartDefectPanel().getSelectedItems().isEmpty()){
					getView().getDownArrowBtn().setDisable(true);
				}else {
					if(isFullAccess()) {
						QiEntryScreenDto entryScreenDto = getView().getEntryScreenPanel().getSelectedItem();
						if(entryScreenDto == null || (entryScreenDto.getIsUsedVersion() == (short) 1 && getModel().isVersionCreated(entryScreenDto.getEntryModel())))
							getView().getDownArrowBtn().setDisable(true);
						else
							getView().getDownArrowBtn().setDisable(false);
					}
					Logger.getLogger().check("Available Part Defect Description selected");
				}
			}
		}
	};

	private final ChangeListener<QiRegionalPartDefectLocationDto> assignedPartDefectPanelChangeListener = new ChangeListener<QiRegionalPartDefectLocationDto>() {
		public void changed(ObservableValue<? extends QiRegionalPartDefectLocationDto> arg0,
				QiRegionalPartDefectLocationDto arg1, QiRegionalPartDefectLocationDto arg2) {
			if(!isRefresh){
				clearDisplayMessage();
				if(null==getView().getAssignedPartdefectPanel().getSelectedItems() || getView().getAssignedPartdefectPanel().getSelectedItems().isEmpty()){
					getView().getUpArrowBtn().setDisable(true);
				}else {
					if(isFullAccess()) {
						QiEntryScreenDto entryScreenDto = getView().getEntryScreenPanel().getSelectedItem();
						if(entryScreenDto == null || (entryScreenDto.getIsUsedVersion() == (short) 1 && getModel().isVersionCreated(entryScreenDto.getEntryModel())))
							getView().getUpArrowBtn().setDisable(true);
						else
							getView().getUpArrowBtn().setDisable(false);
					}
				}

				Logger.getLogger().check("Assigned Part Defect Description selected");

			}
		}
	};
	private ChangeListener<QiEntryScreenDto> entryScreenTableChangeListener = new ChangeListener<QiEntryScreenDto>() {
		public void changed(ObservableValue<? extends QiEntryScreenDto> arg0,
				QiEntryScreenDto arg1, QiEntryScreenDto arg2) {
			if(!isRefresh){
				loadEntryScreenDetails(arg2);
				getView().getSaveBtn().setDisable(true);
				Logger.getLogger().check("Entry Screen/Image selected");

			}
		}

	};
	private ChangeListener<QiTextEntryMenu> textEntryMenuChangeListener = new ChangeListener<QiTextEntryMenu>() {
		public void changed(ObservableValue<? extends QiTextEntryMenu> arg0, QiTextEntryMenu arg1, QiTextEntryMenu arg2) {
			if(!isRefresh){
				clearDisplayMessage();

				if (null != getView().getTextEntryMenuPane().getTable().getSelectionModel().getSelectedItem()) {
					QiEntryScreenDto entryScreenDto = getView().getEntryScreenPanel().getTable().getSelectionModel().getSelectedItem();
					QiTextEntryMenu textEntryMenu = getView().getTextEntryMenuPane().getTable().getSelectionModel().getSelectedItem();
					String filter = getView().getAssignedPartDefectFilterTxt().getText();

					List<QiRegionalPartDefectLocationDto> assignedPartDefectDtoList = getModel()
							.getAssignedPartDefectDetails("", getModel().getProductKind(), entryScreenDto.getEntryScreen(),
									textEntryMenu.getId().getTextEntryMenu(), entryScreenDto.getEntryModel(),  textEntryMenu.getId().getIsUsed());
					List<QiRegionalPartDefectLocationDto> assignedPartDefectDtoListWithFilter = assignedPartDefectDtoList;
					if (!StringUtils.isBlank(filter)) {
						assignedPartDefectDtoListWithFilter = getModel()
								.getAssignedPartDefectDetails(filter, getModel().getProductKind(), entryScreenDto.getEntryScreen(),
										textEntryMenu.getId().getTextEntryMenu(), entryScreenDto.getEntryModel(),  textEntryMenu.getId().getIsUsed());
					} 
					assignedPdcSet.clear();
					assignedPdcSet.addAll(assignedPartDefectDtoList);
					existingAssignedPdcSet.clear();
					existingAssignedPdcSet.addAll(assignedPartDefectDtoList);
					getView().getAssignedPartdefectPanel().setData(assignedPartDefectDtoListWithFilter);
					getView().getAvailablePartDefectPanel().setData(getModel().getPartDefectDetails(getView().getAvailablePartDefectFilterTxt().getText(),getModel().getProductKind(),getView().getEntryScreenPanel().getSelectedItem()));
					getView().getSaveBtn().setDisable(true);

					getView().getAvailablePartDefectFilterTxt().setDisable(false);
					getView().getAssignedPartDefectFilterTxt().setDisable(false);
					getView().getAvailablePartDefectPanel().setDisable(false);
					getView().getAssignedPartdefectPanel().setDisable(false);
				}
				getView().getSaveBtn().setDisable(true);
				enableDisableArrowBtn();
			}
		}
	};


	/**
	 * @param arg2
	 */
	private void loadEntryScreenDetails(QiEntryScreenDto arg2) {
		if(null!=arg2){

			assignedPdcSet.clear();
			existingAssignedPdcSet.clear();

			if(arg2.getImageValue().toString().equals("Text")){

				try {
					if (isFullAccess()) {
						getView().getAssignedPartdefectPanel().createContextMenu(new String[] { QiConstant.COPY_PDC, QiConstant.MOVE_PDC },
								this);

						if (!isListEmpty(getView().getEntryScreenPanel().getTable().getContextMenu().getItems())) {
							getView().getEntryScreenPanel().getTable().getContextMenu().getItems().clear();
						}
					}
				} catch (Exception e) {
					displayErrorMessage("Problem occured while updating context menu.");
				}

				getView().getTextEntryMenuPane().setData(getModel().getMenuDetailsByEntryScreen(getView().getEntryScreenPanel().getSelectedItem()));

				addContextMenuItems();

				getView().getTextEntryMenuPane().setDisable(false);
				getView().getAvailablePartDefectFilterTxt().setDisable(true);
				getView().getAssignedPartDefectFilterTxt().setDisable(true);
				getView().getAvailablePartDefectPanel().setDisable(true);
				getView().getAssignedPartdefectPanel().setDisable(true);

				getView().getAvailablePartDefectPanel().getTable().getSelectionModel().selectedItemProperty().removeListener(availablePartDefectPanelChangeListener);
				getView().getAssignedPartdefectPanel().getTable().getSelectionModel().selectedItemProperty().removeListener(assignedPartDefectPanelChangeListener);
				getView().getAvailablePartDefectPanel().getTable().getItems().clear();
				getView().getAssignedPartdefectPanel().getTable().getItems().clear();
				getView().getAvailablePartDefectPanel().getTable().getSelectionModel().selectedItemProperty().addListener(availablePartDefectPanelChangeListener);
				getView().getAssignedPartdefectPanel().getTable().getSelectionModel().selectedItemProperty().addListener(assignedPartDefectPanelChangeListener);
			}
			else {

				getView().getEntryScreenPanel().createContextMenu(new String[] {QiConstant.COPY_FROM}, this);

				if (!isListEmpty(null != getView().getAssignedPartdefectPanel().getTable().getContextMenu()
						? getView().getAssignedPartdefectPanel().getTable().getContextMenu().getItems() : null)) {
					getView().getAssignedPartdefectPanel().getTable().getContextMenu().getItems().clear();
				}

				if(null!=getView().getEntryModelComboBox().getSelectionModel().getSelectedItem() && null!=getView().getEntryScreenPanel().getSelectedItem()){
					QiEntryScreenDto entryScreenDto = getView().getEntryScreenPanel().getTable().getSelectionModel().getSelectedItem();

					String filter = getView().getAssignedPartDefectFilterTxt().getText();

					List<QiRegionalPartDefectLocationDto> assignedPartDefectList = getModel()
							.getAssignedPartDefectDetails("", getModel().getProductKind(),
									entryScreenDto.getEntryScreen(), "", entryScreenDto.getEntryModel(), entryScreenDto.getIsUsedVersion());
					List<QiRegionalPartDefectLocationDto> assignedPartDefectListWithFilter  = assignedPartDefectList;
					if (!StringUtils.isBlank(filter)) {
						assignedPartDefectListWithFilter = getModel()
								.getAssignedPartDefectDetails(filter, getModel().getProductKind(),
										entryScreenDto.getEntryScreen(), "", entryScreenDto.getEntryModel(), entryScreenDto.getIsUsedVersion());
					}

					assignedPdcSet.addAll(assignedPartDefectList);
					existingAssignedPdcSet.addAll(assignedPartDefectList);

					if(null!=assignedPartDefectListWithFilter){
						getView().getSaveBtn().setDisable(false);
						getView().getAssignedPartdefectPanel().setData(assignedPartDefectListWithFilter);
					}

					getView().getAvailablePartDefectPanel().setData(getModel().getPartDefectDetails(getView().getAvailablePartDefectFilterTxt().getText(),getModel().getProductKind(),getView().getEntryScreenPanel().getSelectedItem()));
				}

				getView().getTextEntryMenuPane().getTable().getSelectionModel().selectedItemProperty().removeListener(textEntryMenuChangeListener );

				getView().getAvailablePartDefectFilterTxt().setDisable(false);
				getView().getAvailablePartDefectPanel().setDisable(false);
				getView().getAssignedPartDefectFilterTxt().setDisable(false);
				getView().getAssignedPartdefectPanel().setDisable(false);
				getView().getTextEntryMenuPane().setDisable(true);
				getView().getTextEntryMenuPane().getTable().getItems().clear();

				getView().getTextEntryMenuPane().getTable().getSelectionModel().selectedItemProperty().addListener(textEntryMenuChangeListener);
			}
			enableDisableArrowBtn();
		}
	}

	@Override
	public void initEventHandlers() {
		addDropDownListner();
		addEntryScreenTableListner();
		addTextEntryMenuTableListner();
		addAvailablePartDefectTableListner();
		addAssignedPartDefectTableListner();
		getView().getEntryScreenPanel().createContextMenu(new String[] {QiConstant.COPY_FROM}, this);
	}


	private void addTextEntryMenuTableListner() {

		getView().getTextEntryMenuPane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
			}
		});

		getView().getTextEntryMenuPane().getTable().getSelectionModel().selectedItemProperty().addListener(textEntryMenuChangeListener );
	}

	private void addAvailablePartDefectTableListner() {
		getView().getAvailablePartDefectPanel().getTable().getSelectionModel().selectedItemProperty().addListener(availablePartDefectPanelChangeListener);
	}

	private void addEntryScreenTableListner() {
		getView().getEntryScreenPanel().getTable().getSelectionModel().selectedItemProperty().addListener(entryScreenTableChangeListener );
	}

	private void resetRadioButton() {
		getView().getAllRadioBtn().setSelected(true);
		getView().getUsedRadioBtn().setSelected(false);
		getView().getNotUsedRadioBtn().setSelected(false);
	}

	private void addDropDownListner() {
		getView().getPlantComboBox().valueProperty().addListener(plantChangeListener);
		getView().getProductTypeComboBox().valueProperty().addListener(productTypeChangeListener);
		getView().getEntryModelComboBox().valueProperty().addListener(entryModelChangeListener);
	}

	private void addAssignedPartDefectTableListner() {
		getView().getAssignedPartdefectPanel().getTable().getSelectionModel().selectedItemProperty().addListener(assignedPartDefectPanelChangeListener);
	}


	/**
	 * This method is called to add context menus to main panel.
	 */
	@Override
	public void addContextMenuItems(){
		if (isFullAccess()) {
			if (!isListEmpty(null != getView().getTextEntryMenuPane().getTable().getContextMenu()
					? getView().getTextEntryMenuPane().getTable().getContextMenu().getItems() : null)) {
				getView().getTextEntryMenuPane().getTable().getContextMenu().getItems().clear();
			}

			if (isListEmpty(getView().getTextEntryMenuPane().getTable().getItems())) {
				if (!isVersionCreated())
					getView().getTextEntryMenuPane().createContextMenu(new String[] { QiConstant.CREATE_MNEU }, this);
			} else {
				if (null != getView().getEntryScreenPanel().getSelectedItem()) {
					String[] menuItems = new String[] {};
					if (isVersionCreated()) {
						menuItems = new String[] { QiConstant.COPY_MENU};
					} else
						menuItems = new String[] { QiConstant.CREATE_MNEU, QiConstant.UPDATE_MNEU, QiConstant.COPY_MENU,
								QiConstant.DELETE_MNEU };

					ObjectTablePane<QiTextEntryMenu> tablePane = getView().getTextEntryMenuPane();
					getView().getTextEntryMenuPane().createContextMenu(menuItems, this);
					tablePane.createContextMenu(menuItems, this);
				}
			}
		}
	}


	public void handle(ActionEvent actionEvent) {

		if(actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();

			if(QiConstant.CREATE_MNEU.equals(menuItem.getText())) createTextEntryMenu(actionEvent);
			else if(QiConstant.UPDATE_MNEU.equals(menuItem.getText())) updateTextEntryMenu(actionEvent);
			else if(QiConstant.DELETE_MNEU.equals(menuItem.getText())) deleteTextEntryMenu(actionEvent);
			else if(QiConstant.COPY_FROM.equals(menuItem.getText()))  copyFrom();
			else if(QiConstant.COPY_MENU.equals(menuItem.getText()))  copyMenu();
			else if(QiConstant.COPY_PDC.equals(menuItem.getText()))  copyPDC();
			else if(QiConstant.MOVE_PDC.equals(menuItem.getText()))  movePDC();
		}

		if(actionEvent.getSource() instanceof LoggedRadioButton) {
			allRadioButtonAction(actionEvent);
		} 
		if (actionEvent.getSource() instanceof LoggedButton) {
			if (actionEvent.getSource().equals(getView().getDownArrowBtn()))downBtnAction(actionEvent);
			else if (actionEvent.getSource().equals(getView().getUpArrowBtn()))upBtnAction(actionEvent);
			else if (actionEvent.getSource().equals(getView().getSaveBtn()))saveBtnAction(actionEvent);
			else if (actionEvent.getSource().equals(getView().getBtnRefresh())) {
				refreshBtnAction();
				EventBusUtil.publish(new StatusMessageEvent("Refreshed successfully.", StatusMessageEventType.INFO));
			}
		}
		if(actionEvent.getSource() instanceof UpperCaseFieldBean){
			if (actionEvent.getSource().equals(getView().getAvailablePartDefectFilterTxt())){
				getView().reloadAvailablePdc(StringUtils.trim(getView().getAvailablePartDefectFilterTxt().getText()));
			}
			else if(actionEvent.getSource().equals(getView().getAssignedPartDefectFilterTxt())){
				List<QiRegionalPartDefectLocationDto> filteredList = Lists.newArrayList(Collections2.filter(assignedPdcSet, new FilterSearchPredicate(StringUtils.trimToEmpty(getView().getAssignedPartDefectFilterTxt().getText()))));
				getView().getAssignedPartdefectPanel().setData(filteredList);
				getView().getAssignedPartdefectPanel().getTable().getItems().sort(new Comparator<QiRegionalPartDefectLocationDto>() {
					@Override
					public int compare(QiRegionalPartDefectLocationDto obj1, QiRegionalPartDefectLocationDto obj2) {
						return obj1.getDefectTypeName().compareTo(obj2.getDefectTypeName());
					}
				});
			}
		}
	}

	/** This method will open dialog to copy all pdc's for image type entry screen.
	 * 
	 */
	private void copyFrom() {
		if (!isListEmpty(getView().getAssignedPartdefectPanel().getTable().getItems())) {

			List<QiEntryScreenDto> imgeEntryScreenList = getModel().getEntryScreenDataByImage(getView().getEntryModelComboBox().getValue(),getView().getPlantComboBox().getValue(), true);
			QiEntryScreenDto entryScreenDto = getSelectedEntryScreenDto();
			imgeEntryScreenList.remove(entryScreenDto);

			CopyPartDefectCombinationDialog copyPDC = new CopyPartDefectCombinationDialog("Copy PDC", getModel(),
					getApplicationId(), imgeEntryScreenList,
					getView().getAssignedPartdefectPanel().getTable().getItems(),
					CopyPartDefectCombinationDialog.CopyMode.COPY_SCREEN, entryScreenDto, null, null);
			copyPDC.show();
		}
		else{
			displayErrorMessage("Please select entry screen with PDCs assigned to it.");
		}
	}

	/**  This method will open dialog to copy particular pdc for text type entry screen.
	 * 
	 */
	private void copyPDC() {
		if (!isListEmpty(getView().getAssignedPartdefectPanel().getTable().getItems())) {

			List<QiEntryScreenDto> textEntryScreenList = getModel().getEntryScreenDataByImage(getView().getEntryModelComboBox().getValue(),getView().getPlantComboBox().getValue(), false);

			QiEntryScreenDto entryScreenDto = getSelectedEntryScreenDto();
			QiTextEntryMenu selectedEntryMenu = getView().getTextEntryMenuPane().getSelectedItem();

			String filterText = getView().getAssignedPartDefectFilterTxt().getText();
			if( filterText == null)  filterText = "";
			CopyPartDefectCombinationDialog copyPDC = new CopyPartDefectCombinationDialog("Copy PDC", getModel(),
					getApplicationId(), textEntryScreenList,
					getView().getAssignedPartdefectPanel().getSelectedItems(),
					CopyPartDefectCombinationDialog.CopyMode.COPY_PDC, entryScreenDto, selectedEntryMenu,
					filterText.trim());
			copyPDC.show();

		}
		else{
			displayErrorMessage("Please select entry screen with PDCs assigned to it.");
		}
	}

	/**  This method will open dialog to copy particular pdc for text type entry screen.
	 * 
	 */
	private void movePDC() {
		if (!isListEmpty(getView().getAssignedPartdefectPanel().getTable().getItems())) {

			List<QiEntryScreenDto> textEntryScreenList = getModel().getEntryScreenDataByImage(getView().getEntryModelComboBox().getValue(),getView().getPlantComboBox().getValue(), false);

			QiEntryScreenDto entryScreenDto = getSelectedEntryScreenDto();
			QiTextEntryMenu selectedEntryMenu = getView().getTextEntryMenuPane().getSelectedItem();

			String filterText = getView().getAssignedPartDefectFilterTxt().getText();
			if( filterText == null)  filterText = "";
			if (!isListEmpty(textEntryScreenList)) {
				CopyPartDefectCombinationDialog movePDC = new CopyPartDefectCombinationDialog("Move PDC", getModel(),
						getApplicationId(), textEntryScreenList,
						getView().getAssignedPartdefectPanel().getSelectedItems(),
						CopyPartDefectCombinationDialog.CopyMode.MOVE_PDC, entryScreenDto, selectedEntryMenu,
						filterText.trim());
				movePDC.setMainPanel(getView());
				movePDC.show();
			}
			else{
				displayErrorMessage("No Entry screen is available for selected Product Type.");
			}

		}
		else{
			displayErrorMessage("Please select entry screen with PDCs assigned to it.");
		}
	}

	/**  This method will open dialog to copy entire menu for text type entry screen.
	 * 
	 */
	private void copyMenu() {
		if (!isListEmpty(getView().getAssignedPartdefectPanel().getTable().getItems())) {

			QiEntryScreenDto entryScreenDto = getSelectedEntryScreenDto();

			List<QiTextEntryMenu> textEntryMenuList = getView().getTextEntryMenuPane().getTable().getSelectionModel().getSelectedItems();
			Map<String, List<QiRegionalPartDefectLocationDto>> textEntryMenuMap = new HashMap<String, List<QiRegionalPartDefectLocationDto>>();
			List<QiRegionalPartDefectLocationDto> assignedPartdefectPanelList = new ArrayList<QiRegionalPartDefectLocationDto>();
			if(!isListEmpty(textEntryMenuList)) {
				for(QiTextEntryMenu textEntryMenu : textEntryMenuList) {
					List<QiRegionalPartDefectLocationDto> assignedPartDefectDtoList = getModel()
							.getAssignedPartDefectDetails(getView().getAssignedPartDefectFilterTxt().getText(), getModel().getProductKind(), entryScreenDto.getEntryScreen(),
									textEntryMenu.getId().getTextEntryMenu(), entryScreenDto.getEntryModel(), textEntryMenu.getId().getIsUsed());
					textEntryMenuMap.put(textEntryMenu.getId().getTextEntryMenu(), assignedPartDefectDtoList);
					assignedPartdefectPanelList.addAll(assignedPartDefectDtoList);
				}

				List<QiEntryScreenDto> textEntryScreenList = getModel().getEntryScreenDataByImage(getView().getEntryModelComboBox().getValue(),getView().getPlantComboBox().getValue(), false);
				textEntryScreenList.remove(getView().getEntryScreenPanel().getTable().getSelectionModel().getSelectedItem());

				CopyPartDefectCombinationDialog copyPDC = new CopyPartDefectCombinationDialog("Copy PDC", getModel(), getApplicationId(), CopyPartDefectCombinationDialog.CopyMode.COPY_MENU, entryScreenDto,textEntryMenuList);
				copyPDC.getController().setTextEntryMenuList(textEntryMenuList);
				copyPDC.getController().setTextEntryMenuMap(textEntryMenuMap);
				copyPDC.getController().getDialog().getEntryScreenPanel().setData(textEntryScreenList);
				copyPDC.getController().getDialog().getAssignedPartdefectPanel().setData(assignedPartdefectPanelList);
				copyPDC.show();
			}
		} else {
			displayErrorMessage("Please select entry screen with PDCs assigned to it.");
		}
	}

	private QiEntryScreenDto getSelectedEntryScreenDto()  {
		QiEntryScreenDto entryScreenDto = getView().getEntryScreenPanel().getTable().getSelectionModel().getSelectedItem();
		entryScreenDto.setPlantName(getView().getPlantComboBox().getValue());
		entryScreenDto.setProductType(getView().getProductTypeComboBox().getValue());
		return entryScreenDto;
	}

	/**
	 * This method is to refresh the data
	 */

	public void refreshBtnAction() {
		getView().getSaveBtn().setDisable(true);
		clearDisplayMessage();
		isRefresh=true;
		String productType = null;
		String entryModel = null;
		List<QiRegionalPartDefectLocationDto> selectedPartDefectComb = new ArrayList<QiRegionalPartDefectLocationDto>();
		List<QiRegionalPartDefectLocationDto> assignedPartDefectComb = new ArrayList<QiRegionalPartDefectLocationDto>();
		QiEntryScreenDto selectedEntryScreen = getView().getEntryScreenPanel().getSelectedItem();
		selectedPartDefectComb.addAll(getView().getAvailablePartDefectPanel().getTable().getSelectionModel().getSelectedItems());
		QiTextEntryMenu selectedEntryMenu = getView().getTextEntryMenuPane().getSelectedItem();
		assignedPartDefectComb.addAll(getView().getAssignedPartdefectPanel().getTable().getSelectionModel().getSelectedItems());
		if(null !=getView().getProductTypeComboBox().getSelectionModel().getSelectedItem() && null !=getView().getEntryModelComboBox().getSelectionModel().getSelectedItem()){
			productType = getView().getProductTypeComboBox().getSelectionModel().getSelectedItem().toString();
			entryModel =getView().getEntryModelComboBox().getSelectionModel().getSelectedItem().toString();	
		}
		refreshPlant(); 
		refreshProductType(productType);
		refreshEntryModel(entryModel);
		refreshOthers(selectedEntryScreen,selectedPartDefectComb,selectedEntryMenu,assignedPartDefectComb);

		isRefresh=false;
	}

	/**
	 * @param entryModel
	 * @param availablePdcFilter
	 * @param selectedEntryScreen
	 */

	private void refreshEntryModel(String entryModel) {
		String productType;

		if (getView().getProductTypeComboBox().getSelectionModel().getSelectedItem() != null) {
			productType = getView().getProductTypeComboBox().getSelectionModel().getSelectedItem().toString();
			if (productType != null) {
				getView().getEntryModelComboBox().getItems().clear();
				List<String> entryModelList = getModel().getEntryModelForProductType(productType);
				if (entryModelList.size() > 0) {
					getView().getEntryModelComboBox().getItems().addAll(entryModelList);
					if(entryModelList.contains(entryModel))
						getView().getEntryModelComboBox().setValue(entryModel);
				}
			} 
		}
	}

	private void refreshOthers(QiEntryScreenDto selectedEntryScreen, List<QiRegionalPartDefectLocationDto> selectedPartDefectCombList, QiTextEntryMenu selectedEntryMenu,List<QiRegionalPartDefectLocationDto> assignedPartDefectCombList) {
		String entryModelforEntryScreen = null;

		if (getView().getProductTypeComboBox().getSelectionModel().getSelectedItem() != null) {
			//refresh entry screen table
			getView().getEntryScreenPanel().setData(getModel().getEntryScreenData(getView().getEntryModelComboBox().getSelectionModel().getSelectedItem(),getView().getPlantComboBox().getSelectionModel().getSelectedItem()));
			getView().getEntryScreenPanel().getTable().getSelectionModel().select(selectedEntryScreen);

			//refresh text entry menu table
			getView().getTextEntryMenuPane().setData(getModel().getMenuDetailsByEntryScreen(getView().getEntryScreenPanel().getSelectedItem()));
			getView().getTextEntryMenuPane().getTable().getSelectionModel().select(selectedEntryMenu);

			//refresh assigned and available PDC table
			List<QiRegionalPartDefectLocationDto> assignedPartDefectDtoListWithFilter = new ArrayList<QiRegionalPartDefectLocationDto>();
			List<QiRegionalPartDefectLocationDto> assignedPartDefectDtoList = new ArrayList<QiRegionalPartDefectLocationDto>();
			short isUsed = 0;
			String selectedTextEntryMenu = "";
			boolean isRefreshAvailablePdcTable = false;

			if (selectedEntryScreen != null) { //selected entry screen
				if (QiConstant.TEXT.equalsIgnoreCase(selectedEntryScreen.getImageValue().toString())) { //selected text entry screen
					QiTextEntryMenu textEntryMenu = getView().getTextEntryMenuPane().getTable().getSelectionModel().getSelectedItem();
					if (textEntryMenu != null) {
						selectedTextEntryMenu = textEntryMenu.getId().getTextEntryMenu();
						entryModelforEntryScreen = textEntryMenu.getId().getEntryModel();
						isUsed = textEntryMenu.getId().getIsUsed();
						isRefreshAvailablePdcTable = true; // text entry screen and text entry menu are selected
					}
				} else { //selected image entry screen
					entryModelforEntryScreen = selectedEntryScreen.getEntryModel();
					isUsed = selectedEntryScreen.getIsUsedVersion();
					isRefreshAvailablePdcTable = true; // image entry screen is selected
				}

				//refresh assigned PDC table
				String assignedPdcfilter = getView().getAssignedPartDefectFilterTxt().getText();
				assignedPartDefectDtoList = getModel().getAssignedPartDefectDetails("",
						getModel().getProductKind(),getView().getEntryScreenPanel().getSelectedItem().getEntryScreen(),
						selectedTextEntryMenu, entryModelforEntryScreen, isUsed);
				assignedPartDefectDtoListWithFilter = assignedPartDefectDtoList;

				if (!StringUtils.isBlank(assignedPdcfilter)) {
					assignedPartDefectDtoListWithFilter = getModel().getAssignedPartDefectDetails(assignedPdcfilter,
							getModel().getProductKind(),getView().getEntryScreenPanel().getSelectedItem().getEntryScreen(),
							selectedTextEntryMenu, entryModelforEntryScreen, isUsed);
				}

				//refresh avaiable PDC table
				if (isRefreshAvailablePdcTable) {
					getView().getAvailablePartDefectPanel().setData(getModel().getPartDefectDetails(getView().getAvailablePartDefectFilterTxt().getText(),getModel().getProductKind(),selectedEntryScreen));
					for(QiRegionalPartDefectLocationDto selectedPartDefectComb : selectedPartDefectCombList) {
						getView().getAvailablePartDefectPanel().getTable().getSelectionModel().select(selectedPartDefectComb);
					}
				}
			}

			assignedPdcSet.clear();
			assignedPdcSet.addAll(assignedPartDefectDtoList);
			existingAssignedPdcSet.clear();
			existingAssignedPdcSet.addAll(assignedPartDefectDtoList);
			getView().getAssignedPartdefectPanel().setData(assignedPartDefectDtoListWithFilter);

			for (QiRegionalPartDefectLocationDto selectedAssignedPartDefectComb : assignedPartDefectCombList) {
				getView().getAssignedPartdefectPanel().getTable().getSelectionModel().select(selectedAssignedPartDefectComb);
			}
		}
	}

	/**
	 * @param productType
	 */
	private void refreshProductType(String productType) {
		String plant;
		if (null != productType) {
			plant = getView().getPlantComboBox().getValue();
			if (plant != null) {
				getView().getProductTypeComboBox().getItems().clear();
				List<String> productTypeList = getModel().getAllProductType();
				if (productTypeList.size() > 0) {
					getView().getProductTypeComboBox().getItems().addAll(productTypeList);
					if(productTypeList.contains(productType))
						getView().getProductTypeComboBox().setValue(productType);
				}
			}    
			else{
				getView().getProductTypeComboBox().getItems().clear();
				List<String> productTypeList = getModel().getAllProductType();
				if (productTypeList.size() > 0) {
					getView().getProductTypeComboBox().getItems().addAll(productTypeList);
				}
			}
		}
	}

	/**
	 * This method is to refresh the Plant Dropdown
	 */
	private void refreshPlant() {
		String plant;
		if (null != getView().getPlantComboBox().getSelectionModel().getSelectedItem()) {
			plant = getView().getPlantComboBox().getSelectionModel().getSelectedItem().toString();
			getView().getPlantComboBox().getItems().clear();
			List<String> plantList = getModel().getPlantForSite(getView().getSiteName());
			if (plantList.size() > 0) {
				getView().getPlantComboBox().getItems().addAll(plantList);
				if(plantList.contains(plant))
					getView().getPlantComboBox().setValue(plant);
			}  
		}
		else{
			getView().getPlantComboBox().getItems().clear();
			getView().loadPlantCombobox();
		}
	}

	private void deleteTextEntryMenu(ActionEvent actionEvent) {
		try {
			QiTextEntryMenu selectedComb = getView().getTextEntryMenuPane().getSelectedItem();
			List<QiLocalDefectCombination> localDefectCombinationList = getModel().findAllByTextMenuAndEntryScreen(getView().getEntryScreenPanel().getSelectedItem().getEntryScreen(), selectedComb.getId().getTextEntryMenu(),
					getView().getEntryModelComboBox().getSelectionModel().getSelectedItem());
			List<Integer> localCombinationIdList =new ArrayList<Integer>();
			List<QiExternalSystemDefectMap> qiExternalSystemDefectMapList=new ArrayList<QiExternalSystemDefectMap>();
			for(QiLocalDefectCombination localDefectCombination:localDefectCombinationList){
				localCombinationIdList.add(localDefectCombination.getLocalDefectCombinationId());
			}
			if(localCombinationIdList !=null && localCombinationIdList.size()>0)
				qiExternalSystemDefectMapList = getModel().findAllByLocalCombinationId(localCombinationIdList);
			StringBuilder deleteMessage = new StringBuilder();
			if(localCombinationIdList.size()>0){
				deleteMessage.append("Deletion of selected Text Entry Menu impacts " +localCombinationIdList.size() + " Local Attributes");
			}
			if(qiExternalSystemDefectMapList.size()>0  ){			
				deleteMessage.append(" and " +qiExternalSystemDefectMapList.size() + " Headless Error Code(s). ")	;
				MessageDialog.showError(getView().getStage(),deleteMessage.append(" Hence, deletion of Text Entry Menu is not allowed.").toString());
				return;
			}else if(localCombinationIdList.size()>0){
				deleteMessage.append(".");
			}

			boolean deleteStatus = MessageDialog.confirm(getView().getStage(),deleteMessage.append(" Are you sure you want to delete Text Entry Menu ?").toString());

			if(deleteStatus){
				ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
				if (dialog.showReasonForChangeDialog(null)) {
					QiTextEntryMenu qiTextEntryMenu = new QiTextEntryMenu();
					QiEntryScreenDto entryModelDto = getView().getEntryScreenPanel().getTable().getSelectionModel().getSelectedItem();

					qiTextEntryMenu.setId(new QiTextEntryMenuId(selectedComb.getId().getEntryScreen(), selectedComb.getId().getTextEntryMenu(),selectedComb.getId().getEntryModel(), selectedComb.getId().getIsUsed()));
					qiTextEntryMenu.setTextEntryMenuDesc(selectedComb.getTextEntryMenuDesc());
					getModel().deleteTextEntryMenu(qiTextEntryMenu);
					getModel().deleteAssignedPartList(entryModelDto,qiTextEntryMenu);
					if(localDefectCombinationList.size()>0){
						getModel().deleteLocalDefectCombination(localDefectCombinationList);
					}
					qiTextEntryMenu.setUpdateUser(getUserId());
					//call to prepare and insert audit data
					AuditLoggerUtil.logAuditInfo(qiTextEntryMenu, null,QiConstant.UPDATE_REASON_FOR_AUDIT, getView().getScreenName(),getUserId());
					getView().reloadTextEntryMenu(getView().getTextEntryMenuPane());

					//clear available and assigned PDC tables
					getView().getAvailablePartDefectPanel().setData(new ArrayList<QiRegionalPartDefectLocationDto>());
					getView().getAssignedPartdefectPanel().setData(new ArrayList<QiRegionalPartDefectLocationDto>());
				}
			}
			enableDisableArrowBtn();
			resetRadioButton();
			addContextMenuItems();
		} catch (Exception e) {
			handleException("An error occured in during delete action ", "Failed to delete Text Entry Menu", e);
		}
	}

	private void updateTextEntryMenu(ActionEvent actionEvent) {
		QiTextEntryMenu selectedComb = getView().getTextEntryMenuPane().getSelectedItem();
		PdcToEntryMenuDialog dialog = new PdcToEntryMenuDialog(QiConstant.UPDATE, selectedComb, getModel(),getView().getEntryScreenPanel().getSelectedItem(),getApplicationId());
		dialog.setScreenName(getView().getScreenName());
		if(null!=selectedComb && null!=selectedComb.getId()){
			clearDisplayMessage();
			dialog.showDialog();
			getView().reloadTextEntryMenu(getView().getTextEntryMenuPane());
		}else{
			displayErrorMessage("Please select a text entry menu");
		}
	}

	public void createTextEntryMenu(ActionEvent event){
		PdcToEntryMenuDialog dialog = new PdcToEntryMenuDialog(QiConstant.CREATE, new QiTextEntryMenu(), getModel(),getView().getEntryScreenPanel().getSelectedItem(),getApplicationId());
		dialog.showDialog();
		getView().reloadTextEntryMenu(getView().getTextEntryMenuPane());
		addContextMenuItems();

		//clear available and assigned PDC tables
		getView().getAvailablePartDefectPanel().setData(new ArrayList<QiRegionalPartDefectLocationDto>());
		getView().getAssignedPartdefectPanel().setData(new ArrayList<QiRegionalPartDefectLocationDto>());
	};

	private void allRadioButtonAction(ActionEvent event) {
		clearDisplayMessage();
		LoggedRadioButton radioButton = (LoggedRadioButton) event.getSource();
		QiEntryScreenDto entryModelDto = getView().getEntryScreenPanel().getTable().getSelectionModel().getSelectedItem();
		QiTextEntryMenu entryMenu = getView().getTextEntryMenuPane().getTable().getSelectionModel().getSelectedItem();

		getView().getTextEntryMenuPane().getTable().getItems().removeAll(getView().getTextEntryMenuPane().getTable().getItems());
		if(QiConstant.USED.equalsIgnoreCase(radioButton.getText())){
			getView().getEntryScreenPanel().setData(getModel().getUsedEntryModelData(getView().getEntryModelComboBox().getSelectionModel().getSelectedItem(),getView().getPlantComboBox().getSelectionModel().getSelectedItem()));
		}
		else if(QiConstant.NOT_USED.equalsIgnoreCase(radioButton.getText())){
			getView().getEntryScreenPanel().setData(getModel().getNotUsedEntryModelData(getView().getEntryModelComboBox().getSelectionModel().getSelectedItem(),getView().getPlantComboBox().getSelectionModel().getSelectedItem()));
		}else{
			getView().getEntryScreenPanel().setData(getModel().getEntryScreenData(getView().getEntryModelComboBox().getSelectionModel().getSelectedItem(),getView().getPlantComboBox().getSelectionModel().getSelectedItem()));
		}

		if(getView().getEntryScreenPanel().getTable().getItems().contains(entryModelDto)){
			getView().getEntryScreenPanel().getTable().getSelectionModel().select(entryModelDto);
			getView().getEntryScreenPanel().getTable().scrollTo(getView().getEntryScreenPanel().getTable().getSelectionModel().getSelectedIndex()-3);
			if(null!=entryMenu){
				getView().getTextEntryMenuPane().getTable().getSelectionModel().select(entryMenu);
			}
		}
		else {
			disableAndResetAll();
		}
		enableDisableArrowBtn();
	}

	private void downBtnAction(ActionEvent actionEvent) {
		int selectedSize = getView().getAvailablePartDefectPanel().getSelectedItems().size();
		if (selectedSize > maxPdcToEntryScreen) {
			MessageDialog.showError(getView().getStage(), "There are " + selectedSize + " PDCs to be assigned, which is more than maximal number of PDCs (" + maxPdcToEntryScreen + ") to be assigned at a time. You can configure maximal number of PDCs by updating the property MAX_PDC_TO_ENTRY_SCREEN of component DEFAULT_QI_PROPERTY_INFO.");
			return;
		}
		getView().getAssignedPartdefectPanel().getTable().setDisable(false);
		getView().getAssignedPartdefectPanel().getTable().getItems().addAll(getView().getAvailablePartDefectPanel().getSelectedItems());
		assignedPdcSet.addAll(getView().getAvailablePartDefectPanel().getSelectedItems());
		getView().getAvailablePartDefectPanel().getTable().getItems().removeAll(getView().getAvailablePartDefectPanel().getSelectedItems());
		if(selectedSize > 1)
			getView().getAvailablePartDefectPanel().getTable().getSelectionModel().clearSelection();

		getView().getSaveBtn().setDisable(isDisableSaveBtn());
	}

	private void upBtnAction(ActionEvent actionEvent) {
		List<QiRegionalPartDefectLocationDto> selectedPdc = new ArrayList<QiRegionalPartDefectLocationDto>(getView().getAssignedPartdefectPanel().getSelectedItems());

		int selectedSize = selectedPdc.size();
		if (selectedSize > maxPdcToEntryScreen) {
			MessageDialog.showError(getView().getStage(), "There are " + selectedSize + " PDCs to be deassigned, which is more than maximal number of PDCs (" + maxPdcToEntryScreen + ") to be deassigned at a time. You can configure maximal number of PDCs by updating the property MAX_PDC_TO_ENTRY_SCREEN of component DEFAULT_QI_PROPERTY_INFO.");
			return;
		}

		List<Integer> regionalIdList =new ArrayList<Integer>();
		for(QiRegionalPartDefectLocationDto localDefectCombination : selectedPdc){
			regionalIdList.add(localDefectCombination.getRegionalDefectCombinationId());
		}

		long qiExternalSystemDefectMapListSize = 0, localPdcAttributesListSize = 0;
		if(regionalIdList!=null && regionalIdList.size()>0){
			localPdcAttributesListSize = getModel().findLocalAttributeCountByRegionalAndScreenName(
					getView().getEntryScreenPanel().getTable().getSelectionModel().getSelectedItem().getEntryScreen(), regionalIdList);

			qiExternalSystemDefectMapListSize = getModel().findHeadlessCountByRegionalAndScreenName(
					getView().getEntryScreenPanel().getTable().getSelectionModel().getSelectedItem().getEntryScreen(), regionalIdList);
		}

		if(localPdcAttributesListSize >0){
			if(qiExternalSystemDefectMapListSize>0){
				MessageDialog.showError(getView().getStage(), "The Part Defect Combinations(s) being deassigned is associated with "+qiExternalSystemDefectMapListSize+" Headless Error Code(s) .Hence, selected part defect combination(s) cannot be deassigned.");
				return;
			}else{
				isLocalPdcDeAssigned = MessageDialog.confirm(getView().getStage(), "The Part Defect Combinations(s) being deassigned is associated with "+localPdcAttributesListSize+" Local Attribute(s). On click of Save button,the association will be removed. Do you still want to continue?");
				if(!isLocalPdcDeAssigned)
					return;
			}
		}
		if(localPdcAttributesListSize <= 0 || isLocalPdcDeAssigned ){
			assignedPdcSet.removeAll(selectedPdc);
			getView().getAvailablePartDefectPanel().getTable().getItems().addAll(getView().getAssignedPartdefectPanel().getSelectedItems());
			getView().getAssignedPartdefectPanel().getTable().getItems().removeAll(getView().getAssignedPartdefectPanel().getSelectedItems());
			if(selectedSize > 1)
				getView().getAssignedPartdefectPanel().getTable().getSelectionModel().clearSelection();
		}
		getView().getSaveBtn().setDisable(isDisableSaveBtn());
	}

	/**
	 * @param selectedPdc 
	 * @return
	 */
	private List<QiLocalDefectCombination> getLocalDefectCombinationList(List<QiRegionalPartDefectLocationDto> selectedPdc) {
		List<QiLocalDefectCombination> localDefectCombinationList = new ArrayList<QiLocalDefectCombination>();
		QiEntryScreenDto entryScreenDto = getView().getEntryScreenPanel().getSelectedItem();
		for (QiRegionalPartDefectLocationDto qiRegionalPartDefectLocationDto : selectedPdc) {
			QiLocalDefectCombination localDefectCombination = getModel().getAssociatedAttributesByEntryScreenAndModel(
					qiRegionalPartDefectLocationDto.getRegionalDefectCombinationId(), entryScreenDto);
			if (null != localDefectCombination)
				localDefectCombinationList.add(localDefectCombination);
		}
		return localDefectCombinationList;
	}

	private void saveBtnAction(ActionEvent actionEvent) {
		clearDisplayMessage();
		boolean isSaved = false;
		QiEntryScreenDto selectedEntryScreenDto = getView().getEntryScreenPanel().getTable().getSelectionModel().getSelectedItem();
		QiTextEntryMenu selectedTextEntryMenu = getView().getTextEntryMenuPane().getTable().getSelectionModel().getSelectedItem();

		if (null != selectedEntryScreenDto && null != selectedEntryScreenDto.getEntryScreen()) {
			if (QiConstant.TEXT.equalsIgnoreCase(selectedEntryScreenDto.getImageValue())) {
				if (null == selectedTextEntryMenu) {
					displayErrorMessage("Please select Menu.");
					return;
				}
			}
		} else {
			displayErrorMessage("Please select entry screen value.");
			return;
		}

		if (isPdcMoved()) {
			List<QiEntryScreenDefectCombination> assignedEntryScreenDefectCombinationsList = new ArrayList<QiEntryScreenDefectCombination>();
			List<QiEntryScreenDefectCombination> deAssignedEntryScreenDefectCombinationsList = new ArrayList<QiEntryScreenDefectCombination>();
			//create a list of newly assigned and deassigned PDCs
			Set<QiRegionalPartDefectLocationDto> newlyDeAssignedPdcSet = new HashSet<QiRegionalPartDefectLocationDto>(existingAssignedPdcSet);
			Set<QiRegionalPartDefectLocationDto> newlyAssignedPdcSet = new HashSet<QiRegionalPartDefectLocationDto>(assignedPdcSet);
			newlyDeAssignedPdcSet.removeAll(assignedPdcSet);
			newlyAssignedPdcSet.removeAll(existingAssignedPdcSet);

			if (newlyAssignedPdcSet.size() > maxPdcToEntryScreen) {
				MessageDialog.showError(getView().getStage(), "There are " + newlyAssignedPdcSet.size() + " PDCs to be assigned, which is more than maximal number of PDCs (" + maxPdcToEntryScreen + ") to be assigned at a time. You can configure maximal number of PDCs by updating the property MAX_PDC_TO_ENTRY_SCREEN of component DEFAULT_QI_PROPERTY_INFO.");
				return;
			}
			if (newlyDeAssignedPdcSet.size() > maxPdcToEntryScreen) {
				MessageDialog.showError(getView().getStage(), "There are " + newlyDeAssignedPdcSet.size() + " PDCs to be deassigned, which is more than maximal number of PDCs (" + maxPdcToEntryScreen + ") to be deassigned at a time. You can configure maximal number of PDCs by updating the property MAX_PDC_TO_ENTRY_SCREEN of component DEFAULT_QI_PROPERTY_INFO.");
				return;
			}

			//create a list of newly assigned QiEntryScreenDefectCombination
			for (QiRegionalPartDefectLocationDto assignedPartDefectLocationDto : newlyAssignedPdcSet) {
				QiEntryScreenDefectCombination assignedEntryScreenDefectCombination = new QiEntryScreenDefectCombination();
				if (null != assignedPartDefectLocationDto.getRegionalDefectCombinationId()) {
					assignedEntryScreenDefectCombination.setId(new QiEntryScreenDefectCombinationId(
							selectedEntryScreenDto.getEntryScreen(), assignedPartDefectLocationDto.getRegionalDefectCombinationId(),
							selectedEntryScreenDto.getEntryModel(), selectedEntryScreenDto.getIsUsedVersion()));
					assignedEntryScreenDefectCombination.setCreateUser(getUserId());
					if (null != selectedTextEntryMenu) {
						assignedEntryScreenDefectCombination.setTextEntryMenu(selectedTextEntryMenu.getId().getTextEntryMenu());
					}
					assignedEntryScreenDefectCombinationsList.add(assignedEntryScreenDefectCombination);
				}
			}

			//save newly assigned QiEntryScreenDefectCombination
			if(!assignedEntryScreenDefectCombinationsList.isEmpty()) {
				getModel().saveAssignedEntryScreenPdcList(assignedEntryScreenDefectCombinationsList);
				isSaved = true;
			}

			//get a list of deassigned QiEntryScreenDefectCombination
			for (QiRegionalPartDefectLocationDto deAssignedPartDefectLocationDto : newlyDeAssignedPdcSet) {
				QiEntryScreenDefectCombination deAssignedEntryScreenDefectCombination = new QiEntryScreenDefectCombination();
				if (null != deAssignedPartDefectLocationDto.getRegionalDefectCombinationId()) {
					deAssignedEntryScreenDefectCombination.setId(new QiEntryScreenDefectCombinationId(
							selectedEntryScreenDto.getEntryScreen(), deAssignedPartDefectLocationDto.getRegionalDefectCombinationId(),
							selectedEntryScreenDto.getEntryModel(), selectedEntryScreenDto.getIsUsedVersion()));
					if (null != selectedTextEntryMenu) {
						deAssignedEntryScreenDefectCombination.setTextEntryMenu(selectedTextEntryMenu.getId().getTextEntryMenu());
					}
					deAssignedEntryScreenDefectCombinationsList.add(deAssignedEntryScreenDefectCombination);
				}
			}

			if (!deAssignedEntryScreenDefectCombinationsList.isEmpty()) {
				//keep audit primary key value in map
				Map<QiEntryScreenDefectCombination, String> keyValueMap = new HashMap<QiEntryScreenDefectCombination, String>();
				for (QiEntryScreenDefectCombination deAssignedQiEntryScreenPdc : deAssignedEntryScreenDefectCombinationsList) {
					keyValueMap.put(deAssignedQiEntryScreenPdc,
							getModel().getAuditPrimaryKeyValue(deAssignedQiEntryScreenPdc.getId().getRegionalDefectCombinationId(),
									selectedEntryScreenDto.getEntryScreen(), selectedEntryScreenDto.getEntryModel()));
				}

				//delete deassigned QiEntryScreenDefectCombination
				getModel().removeDeAssignedEntryScreenPdcList(deAssignedEntryScreenDefectCombinationsList);

				//delete related QiLocalDefectCombination
				if (isLocalPdcDeAssigned) {
					List<QiRegionalPartDefectLocationDto> newlyDeAssignedPdcList = new ArrayList<QiRegionalPartDefectLocationDto>(newlyDeAssignedPdcSet);
					List<QiLocalDefectCombination> localDefectCombinationList = getLocalDefectCombinationList(newlyDeAssignedPdcList);
					if(!localDefectCombinationList.isEmpty()) {
						getModel().deleteLocalDefectCombination(localDefectCombinationList);
					}
				}

				//create audit log for deassigned 
				for (QiEntryScreenDefectCombination deAssignedQiEntryScreenPdc : deAssignedEntryScreenDefectCombinationsList) {
					deAssignedQiEntryScreenPdc.setUpdateUser(getUserId());
					AuditLoggerUtil.logAuditInfo(deAssignedQiEntryScreenPdc, null, QiConstant.SAVE_REASON_FOR_PDC_TO_ENTRY_SCREEN_AUDIT, getView().getScreenName(),
							keyValueMap.get(deAssignedQiEntryScreenPdc), getUserId());
				}
				isSaved = true;
				isLocalPdcDeAssigned = false;
			}

			if (isSaved) {
				existingAssignedPdcSet.clear();
				existingAssignedPdcSet.addAll(assignedPdcSet);
				getView().getSaveBtn().setDisable(true);
				EventBusUtil.publish(new StatusMessageEvent("Assigned part defect saved successfully", StatusMessageEventType.INFO));
			}

			getView().getAllRadioBtn().setSelected(true);
		} else {
			clearMessage();
			displayErrorMessage("No change detected.");
		}
	}

	private void enableDisableArrowBtn() {
		clearDisplayMessage();
		if(isFullAccess()){
			if(getView().getAvailablePartDefectPanel().getSelectedItems() == null || getView().getAvailablePartDefectPanel().getSelectedItems().isEmpty()){
				getView().getDownArrowBtn().setDisable(true);
			}else{
				getView().getDownArrowBtn().setDisable(false);
			}if(getView().getAssignedPartdefectPanel().getSelectedItems() == null || getView().getAssignedPartdefectPanel().getSelectedItems().isEmpty()){
				getView().getUpArrowBtn().setDisable(true);
			}else{
				getView().getUpArrowBtn().setDisable(false);
			}
		}
	}

	/** This method will disable and reset components.
	 *  
	 */
	private void disableAndResetAll() {
		clearDisplayMessage();
		getView().getTextEntryMenuPane().setDisable(true);
		getView().getAvailablePartDefectFilterTxt().setDisable(true);
		getView().getAssignedPartDefectFilterTxt().setDisable(true);
		getView().getAvailablePartDefectPanel().setDisable(true);
		getView().getAssignedPartdefectPanel().setDisable(true);

		getView().getSaveBtn().setDisable(true);
		getView().getUpArrowBtn().setDisable(true);
		getView().getDownArrowBtn().setDisable(true);
		clearDependentTablesOnEntryScreen();
	}


	/** Clear content of TextEntryMenuPane, PartDefectDescPanel and AssignedPartdefectPanel.
	 * 
	 */
	private void clearDependentTablesOnEntryScreen() {
		getView().getTextEntryMenuPane().getTable().getItems().clear();
		getView().getAvailablePartDefectPanel().getTable().getItems().clear();
		getView().getAssignedPartdefectPanel().getTable().getItems().clear();
	}

	/** Check whether list contains any element or not.
	 * 
	 * @param list
	 * @return true if list is empty or not have any element in it.
	 */
	private boolean isListEmpty(List list){
		return null != list ? list.isEmpty() :  true;
	}

	private boolean isVersionCreated() {
		QiEntryScreenDto entryScreenDto = getView().getEntryScreenPanel().getSelectedItem();
		if(null != entryScreenDto && entryScreenDto.getIsUsedVersion() == (short) 1 && getModel().isVersionCreated(entryScreenDto.getEntryModel()))
			return true;
		return false;
	}
	/**
	 * populates Entry Model combobox
	 */
	private void populateEntryModelComboBox() {
		disableAndResetAll();
		getView().getEntryModelComboBox().getItems().clear();
		getView().getEntryModelComboBox().getItems().addAll(FXCollections.observableArrayList(getModel().getEntryModelForProductType(StringUtils.trimToEmpty(getView().getProductTypeComboBox().getValue()))));
		enableDisableArrowBtn();
		resetRadioButton();
	}

	/**
	 * This class is used as a custom Predicate to perform filtering on List of QiRegionalPartDefectLocationDto  
	 * @author vcc88331
	 */
	private final class FilterSearchPredicate implements Predicate<QiRegionalPartDefectLocationDto> {
		Pattern pattern = null;

		public FilterSearchPredicate(final String regex) {
			String myRegEx = regex.toUpperCase();
			if(StringUtils.isBlank(myRegEx)) {
				myRegEx = ".*";
			} else {
				if (!myRegEx.startsWith("%"))
					myRegEx = ".*" + myRegEx;
				if (!myRegEx.endsWith("%"))
					myRegEx = myRegEx + ".*";
				if (myRegEx.contains("%"))
					myRegEx = myRegEx.replaceAll("%", ".*");
			}
			pattern = Pattern.compile(myRegEx);
		}

		@Override
		public boolean apply(QiRegionalPartDefectLocationDto input) {
			Matcher matcher = pattern.matcher(input.toStringNoNulls());
			return  matcher.matches();
		}
	}


	protected boolean isDisableSaveBtn() {
		if (isFullAccess() && isPdcMoved()) {
			return false;
		} else {
			return true;
		}		
	}

	protected boolean isPdcMoved() {
		if (assignedPdcSet.containsAll(existingAssignedPdcSet) && existingAssignedPdcSet.containsAll(assignedPdcSet)) {
			return false;
		} else {
			return true;
		}
	}	
}
