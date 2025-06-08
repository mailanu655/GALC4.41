package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.enumtype.ObservableListChangeEventType;
import com.honda.galc.client.teamleader.qi.controller.PartDefectCombinationController.ListType;
import com.honda.galc.client.teamleader.qi.model.ItemMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.PartLocationCombinationDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.ObservableListChangeEvent;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiImageSection;
import com.honda.galc.entity.qi.QiInspectionLocation;
import com.honda.galc.entity.qi.QiInspectionPart;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.entity.qi.QiPartLocationCombination;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PartLocationCombinationDialogController</code> is the controller class for Part Location Combination Dialog.
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
public class PartLocationCombinationDialogController extends QiDialogController<ItemMaintenanceModel, PartLocationCombinationDialog> {

	private QiPartLocationCombination selectedComb;
	private final static String PART1_SELECTION_MANDATORY = "Selection of Part 1 is mandatory. Please select Part 1.";
	private final static String ALLOW_MULTIPLE_WARNING_MESSAGE = "Part is not allowed for multiple selection. Please select another Part.";
	private final static String LOCATION_ALREADY_SELECTED_WARNING_MESSAGE = "This location is already selected. Please select another location.";
	private List<QiInspectionPart> partList;
	private List<QiInspectionLocation> locationList;
	private QiPartLocationCombination selectedCombCloned;
	private boolean swapValue = false;
	private PartLocationCombinationController mainPanelController;

	public PartLocationCombinationDialogController(ItemMaintenanceModel model, PartLocationCombinationDialog dialog, QiPartLocationCombination selectedComb, PartLocationCombinationController parentController) {
		super();
		setModel(model);
		setDialog(dialog);
		this.selectedComb = selectedComb;
		partList = model.getActivePartNames();
		locationList = model.getActiveLocationNames();
		selectedCombCloned =(QiPartLocationCombination) selectedComb.deepCopy();
		mainPanelController = parentController;
		EventBusUtil.register(this);
	}
	
	@Override
	public void close() {
		EventBusUtil.unregister(this);
	}

	@Override
	public void initListeners() {
		setPart1TableListeners();
		setPart1Loc1TableListeners();
		setPart1Loc2TableListeners();

		setPart2TableListeners();
		setPart2Loc1TableListeners();
		setPart2Loc2TableListeners();

		setPart3TableListeners();
		
		getDialog().getActiveRadioBtn().getToggleGroup().selectedToggleProperty().addListener(updateEnablerForToggle);
		addUpdateEnablerForTableChangeListener(getDialog().getPart1Location1TablePane());
		addUpdateEnablerForTableChangeListener(getDialog().getPart1Location2TablePane());
		addUpdateEnablerForTableChangeListener(getDialog().getPart1TablePane());
		addUpdateEnablerForTableChangeListener(getDialog().getPart2Location1TablePane());
		addUpdateEnablerForTableChangeListener(getDialog().getPart2Location2TablePane());
		addUpdateEnablerForTableChangeListener(getDialog().getPart2TablePane());
		addUpdateEnablerForTableChangeListener(getDialog().getPart3TablePane());
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof LoggedButton) {
			if(actionEvent.getSource().equals(getDialog().getCreateBtn())) createBtnAction(actionEvent);
			else if(actionEvent.getSource().equals(getDialog().getUpdateButton())) updateBtnAction(actionEvent);
			else if(actionEvent.getSource().equals(getDialog().getCancelBtn())) cancelBtnAction(actionEvent);
		}
		else if(actionEvent.getSource() instanceof UpperCaseFieldBean) {
			if(actionEvent.getSource().equals(getDialog().getPart1FilterTextField())) reloadPart1Table();
			else if(actionEvent.getSource().equals(getDialog().getPart1Location1FilterTextField())) reloadPart1Loc1Table();
			else if(actionEvent.getSource().equals(getDialog().getPart1Location2FilterTextField())) reloadPart1Loc2Table();
			else if(actionEvent.getSource().equals(getDialog().getPart2FilterTextField())) reloadPart2Table();
			else if(actionEvent.getSource().equals(getDialog().getPart2Location1FilterTextField())) reloadPart2Loc1Table();
			else if(actionEvent.getSource().equals(getDialog().getPart2Location2FilterTextField())) reloadPart2Loc2Table();
			else if(actionEvent.getSource().equals(getDialog().getPart3FilterTextField())) reloadPart3Table();
		}
	}

	public void createBtnAction(ActionEvent event) {
		clearDisplayMessage();
		LoggedButton createBtn = getDialog().getCreateBtn();
		selectedComb.setActive(getDialog().getActiveRadioBtn().isSelected());
		selectedComb.setCreateUser(getUserId());
		selectedComb.setUpdateUser(StringUtils.EMPTY);
		selectedComb.setProductKind(getModel().getProductKind());

		if (isCombinationValid(selectedComb)) {
			if (getModel().checkPartLocComb(selectedComb))
				publishErrorMessage("Part Loc Combination already exists. Create a new combination.");
			else {
				try {
					getModel().createPartLocComb(selectedComb);
					selectedComb.clear();
					Stage stage = (Stage) createBtn.getScene().getWindow();
					stage.close();
				} catch (Exception e) {
					handleException("An error occured in during create action ", "Failed to Create Part Location Combination", e);
				}
			}
		}
	}

	public void updateBtnAction(ActionEvent event)
	{
		clearDisplayMessage();
		LoggedButton updateBtn = getDialog().getUpdateButton();
		String reasonForChange = getDialog().getReasonForChangeText();
		boolean hasOldActiveStatusValue = selectedComb.isActive();
		boolean hasNewActiveStatusValue = getDialog().getActiveRadioBtn().isSelected();
		selectedComb.setUpdateUser(getUserId());
		List<Integer> partLocationIdList = new ArrayList<Integer>();
		partLocationIdList.add(selectedComb.getPartLocationId());
		List<QiImageSection> imageSecList = getModel().findPartLocationIdsInImageSection(partLocationIdList);
		List<QiPartDefectCombination> partDefectCombList = getModel().findPartLocationIdsInPartDefectCombination(partLocationIdList);
		List<QiPartDefectCombination> regionalAttributeList= getModel().findAllRegionalAttributesByPartLocationId(partLocationIdList);
		
		boolean isPLCombinationUpdated = false;

		if(selectedComb.getInspectionPartName().equals(StringUtils.EMPTY) || selectedComb.getInspectionPartName()==null)
			publishErrorMessage(PART1_SELECTION_MANDATORY);
		else if(reasonForChange==null || reasonForChange.equals(StringUtils.EMPTY))
			publishErrorMessage("Please enter Reason for Change");
		else
		{
			if((!imageSecList.isEmpty()   && hasOldActiveStatusValue != hasNewActiveStatusValue) && (hasNewActiveStatusValue == false)) {
				StringBuilder errorMesg=new StringBuilder("The Part Location Combination(s) being updated affects " );
				if(imageSecList.size()>0 )
					errorMesg.append(imageSecList.size()+" Image Section(s) ");
				errorMesg.append(". Hence, inactivation of this PLC is not allowed.");
				MessageDialog.showError(getDialog(),errorMesg.toString());
				return;
			}
			else if((!partDefectCombList.isEmpty()  || !regionalAttributeList.isEmpty() )  &&
					(hasOldActiveStatusValue != hasNewActiveStatusValue) && (hasNewActiveStatusValue == false)){
				String alertMsg = QiCommonUtil.getMessage("Part Location Combination(s)",partDefectCombList.size(),regionalAttributeList.size(),imageSecList.size()).toString();
				isPLCombinationUpdated  = MessageDialog.confirm(getDialog(), alertMsg);
				if(!isPLCombinationUpdated)
					return;
				else if(hasOldActiveStatusValue != hasNewActiveStatusValue)
				{
					String returnValue=isLocalSiteImpacted(partLocationIdList,getDialog().getScreenName(),getDialog());
					if(returnValue.equals(QiConstant.NO_LOCAL_SITES_CONFIGURED)){
						return;
					}
					else if(returnValue.equals(QiConstant.LOCAL_SITES_IMPACTED)){
						displayErrorMessage("Inactivation of PLC(s) affects Local Sites", "Inactivation of PLC(s) affects Local Sites");
						return;
					}
				}
			}
			selectedComb.setActive(getDialog().getActiveRadioBtn().isSelected());
			selectedComb.setUpdateUser(getUserId());
			if(getModel().checkPartLocComb(selectedComb))
				publishErrorMessage("Part Loc Combination already exists. Create a new combination.");
			else
			{
				try {
					if(getModel().checkPartLocationCombinationForUpdate(selectedComb))
					{
						if(isCombinationValid(selectedComb))
						{
							if((!partDefectCombList.isEmpty()  || !regionalAttributeList.isEmpty() || !imageSecList.isEmpty()) 
									&& (hasOldActiveStatusValue == hasNewActiveStatusValue))	{
								if(!MessageDialog.confirm(getDialog(), QiCommonUtil.getMessage("Part Location Combination(s)",partDefectCombList.size(),regionalAttributeList.size(),imageSecList.size()).toString()))
									return;
								else if(hasOldActiveStatusValue != hasNewActiveStatusValue)
								{
									String returnValue=isLocalSiteImpacted(partLocationIdList,getDialog().getScreenName(),getDialog());
									if(returnValue.equals(QiConstant.NO_LOCAL_SITES_CONFIGURED)){
										return;
									}
									else if(returnValue.equals(QiConstant.LOCAL_SITES_IMPACTED)){
										displayErrorMessage("Inactivation of PLC(s) affects Local Sites", "Inactivation of PLC(s) affects Local Sites");
										return;
									}
								}
									
							}
							if (isUpdated(selectedCombCloned)) {
								return;
							}							
							getModel().updatePartLocComb(selectedComb);
							if(
									(!hasNewActiveStatusValue && (getDialog().getListType() == ListType.ACTIVE))
									|| (hasNewActiveStatusValue && (getDialog().getListType() == ListType.INACTIVE)))  {
								//changed from active to inactive in Active list or inactive to active in Inactive list
								mainPanelController.removeSelectedFromList();
							}
							//call to prepare and insert audit data
							AuditLoggerUtil.logAuditInfo(selectedCombCloned, selectedComb, getDialog().getReasonForChangeText(), getDialog().getScreenName(),getUserId());

							if(isPLCombinationUpdated && (hasOldActiveStatusValue ^ selectedComb.isActive())){
								updatePartDefectCombStatus(partLocationIdList);
							}
							Stage stage = (Stage) updateBtn.getScene().getWindow();
							stage.close();
						}
					}
					else
						publishErrorMessage("Failed to update as one of the Part or Location in combination is inactive.");
				}catch (Exception e) {
					handleException("An error occured in during update action ", "Failed to update Part Location Combination", e);
				}
			}
		}
	}

	public void cancelBtnAction(ActionEvent event) {
		clearDisplayMessage();
		LoggedButton cancelBtn = getDialog().getCancelBtn();
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
			getDialog().setCancel(true);
		} catch (Exception e) {
			handleException("An error occured in during cancel action ", "Failed to perform cancel action", e);
		}
	}

	private boolean isCombinationValid(QiPartLocationCombination selectedComb)
	{
		boolean isValid;
		if (StringUtils.isEmpty(selectedComb.getInspectionPartName()) || selectedComb.getInspectionPartName()==null) {
			publishErrorMessage(PART1_SELECTION_MANDATORY);
			isValid = false;
		} else {
			if ((StringUtils.isEmpty(selectedComb.getInspectionPartLocationName()))
					&& (!StringUtils.isEmpty(selectedComb.getInspectionPartLocation2Name()))) {
				publishErrorMessage("Part 1 Loc 2 cannot be selected without selecting Part 1 Loc 1.");
				isValid = false;
			} else if (StringUtils.isEmpty(selectedComb.getInspectionPart2Name())) {
				if ((!StringUtils.isEmpty(selectedComb.getInspectionPart2LocationName()))
						|| (!StringUtils.isEmpty(selectedComb.getInspectionPart2Location2Name()))) {
					publishErrorMessage("Part 2 locations cannot be selected without selecting Part 2.");
					isValid = false;
				} else if (!StringUtils.isEmpty(selectedComb.getInspectionPart3Name())) {
					publishErrorMessage("Part 3 cannot be selected without selecting Part 2.");
					isValid = false;
				} else {
					isValid = true;
				}
			} else if ((StringUtils.isEmpty(selectedComb.getInspectionPart2LocationName()))
					&& (!StringUtils.isEmpty(selectedComb.getInspectionPart2Location2Name()))) {
				publishErrorMessage("Part 2 Loc 2 cannot be selected without selecting Part 2 Loc 1.");
				isValid = false;
			} else {
				isValid = true;
			}
		}
		return isValid;
	}

	/**
	 * This method is used to set listener on Part 1 table.
	 */
	private void setPart1TableListeners()
	{
		setPartTablePaneDeselectListener(getDialog().getPart1TablePane(), 1);
		getDialog().getPart1TablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiInspectionPart>() {

			public void changed(
					ObservableValue<? extends QiInspectionPart> arg0,
					QiInspectionPart arg1, QiInspectionPart arg2) {
				if(swapValue)
					return;
				clearDisplayMessage();
				QiInspectionPart part1 = getDialog().getPart1TablePane().getSelectedItem();
				QiInspectionPart part2 = getDialog().getObjectfromList(partList, selectedComb.getInspectionPart2Name());
				if(part1!=null)
				{
					selectedComb.setInspectionPartName(part1.getInspectionPartName());
					if(!StringUtils.isBlank(selectedComb.getInspectionPart2Name())
							&& part2!=null
							&& part1.equals(part2)
							&& !part2.isAllowMultiple())
					{
						publishErrorMessage(ALLOW_MULTIPLE_WARNING_MESSAGE);
						selectedComb.setInspectionPartName(StringUtils.EMPTY);
					}
					else
					{
						selectedComb.setInspectionPartName(part1.getInspectionPartName());
						if(!StringUtils.isBlank(selectedComb.getInspectionPart2Name())
								&& part2!=null
								&& part2.isPrimaryPosition()
								&& partHasLowerHierarchy(part2, part1))
						{
							if(MessageDialog.confirm(getDialog(), "The selected Part 1 has lower priority compared to Part 2. Please press 'Yes' to swap parts or press 'No' to select another part."))
								EventBusUtil.publish(new ObservableListChangeEvent("swapPart1Part2", ObservableListChangeEventType.CHANGE_SELECTION));
							else
								selectedComb.setInspectionPartName(StringUtils.EMPTY);
						}
						else
							selectedComb.setInspectionPartName(part1.getInspectionPartName());
					}
					getDialog().getFullPartNameValueLabel().setText(selectedComb.getFullPartDesc());
				}
			}
		});
	}
	/**
	 * This method is used to set listener on Part 1 Location 1 table.
	 */
	private void setPart1Loc1TableListeners()
	{
		setLocationTablePaneDeselectListener(getDialog().getPart1Location1TablePane(), 11);
		getDialog().getPart1Location1TablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiInspectionLocation>() {

			public void changed(
					ObservableValue<? extends QiInspectionLocation> arg0,
					QiInspectionLocation arg1, QiInspectionLocation arg2) {
				if(swapValue)
					return;
				clearDisplayMessage();
				QiInspectionLocation location1 = getDialog().getPart1Location1TablePane().getSelectedItem();
				QiInspectionLocation location2 = getDialog().getObjectfromList(locationList, selectedComb.getInspectionPartLocation2Name());
				if(location1!=null)
				{
					selectedComb.setInspectionPartLocationName(location1.getInspectionPartLocationName());
					if(!StringUtils.isBlank(selectedComb.getInspectionPartLocation2Name())
							&& location2!=null
							&& location1.equals(location2))
					{
						publishErrorMessage(LOCATION_ALREADY_SELECTED_WARNING_MESSAGE);
						selectedComb.setInspectionPartLocationName(StringUtils.EMPTY);
					}
					else
					{
						selectedComb.setInspectionPartLocationName(location1.getInspectionPartLocationName());
						if(!StringUtils.isBlank(selectedComb.getInspectionPartLocation2Name()) 
								&& location2 != null
								&& location2.isPrimaryPosition()
								&& location2.hasLowerHierarchy(location1))
						{
							if(MessageDialog.confirm(getDialog(), "The selected Location 1 has lower priority compared to Location 2. Please press 'Yes' to swap locations or press 'No' to select another location."))
								EventBusUtil.publish(new ObservableListChangeEvent("swapPart1Location1Location2", ObservableListChangeEventType.CHANGE_SELECTION));
							else
								selectedComb.setInspectionPartLocationName(StringUtils.EMPTY);
						}
						else
							selectedComb.setInspectionPartLocationName(location1.getInspectionPartLocationName());
						getDialog().getFullPartNameValueLabel().setText(selectedComb.getFullPartDesc());
					}
				}
			}
		});
	}
	/**
	 * This method is used to set listener on Part 1 Location 2 table.
	 */
	private void setPart1Loc2TableListeners()
	{
		setLocationTablePaneDeselectListener(getDialog().getPart1Location2TablePane(), 12);
		getDialog().getPart1Location2TablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiInspectionLocation>() {

			public void changed(
					ObservableValue<? extends QiInspectionLocation> arg0,
					QiInspectionLocation arg1, QiInspectionLocation arg2) {
				if(swapValue)
					return;
				clearDisplayMessage();
				QiInspectionLocation location1 = getDialog().getObjectfromList(locationList, selectedComb.getInspectionPartLocationName());
				QiInspectionLocation location2 = getDialog().getPart1Location2TablePane().getSelectedItem();
				if(location2!=null)
				{
					selectedComb.setInspectionPartLocation2Name(location2.getInspectionPartLocationName());
					if(!StringUtils.isBlank(selectedComb.getInspectionPartLocationName()) 
							&& location1!=null
							&& location1.equals(location2))
					{
						publishErrorMessage(LOCATION_ALREADY_SELECTED_WARNING_MESSAGE);
						selectedComb.setInspectionPartLocation2Name(StringUtils.EMPTY);
					}
					else
					{
						selectedComb.setInspectionPartLocation2Name(location2.getInspectionPartLocationName());
						if(!StringUtils.isBlank(selectedComb.getInspectionPartLocationName()) 
								&& location1 != null
								&& location2.isPrimaryPosition()
								&& location2.hasLowerHierarchy(location1))
						{
							if(MessageDialog.confirm(getDialog(), "The selected Location 2 has higher priority compared to Location 1. Please press 'Yes' to swap locations or press 'No' to select another location."))
								EventBusUtil.publish(new ObservableListChangeEvent("swapPart1Location1Location2", ObservableListChangeEventType.CHANGE_SELECTION));
							else
								selectedComb.setInspectionPartLocation2Name(StringUtils.EMPTY);
						}
						else
							selectedComb.setInspectionPartLocation2Name(location2.getInspectionPartLocationName());
						getDialog().getFullPartNameValueLabel().setText(selectedComb.getFullPartDesc());
					}
				}
			}
		});
	}
	/**
	 * This method is used to set listener on Part 2 table.
	 */
	private void setPart2TableListeners()
	{
		setPartTablePaneDeselectListener(getDialog().getPart2TablePane(), 2);
		getDialog().getPart2TablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiInspectionPart>() {

			public void changed(
					ObservableValue<? extends QiInspectionPart> arg0,
					QiInspectionPart arg1, QiInspectionPart arg2) {
				if(swapValue)
					return;
				clearDisplayMessage();
				QiInspectionPart part2 = getDialog().getPart2TablePane().getSelectedItem();
				QiInspectionPart part1 = getDialog().getObjectfromList(partList, selectedComb.getInspectionPartName());
				QiInspectionPart part3 = getDialog().getObjectfromList(partList, selectedComb.getInspectionPart3Name());
				if(part2!=null)
				{
					if((!StringUtils.isBlank(selectedComb.getInspectionPartName())
							&& part1!=null
							&& part1.equals(part2)
							&& !part1.isAllowMultiple()) || 
							(!StringUtils.isBlank(selectedComb.getInspectionPart3Name())
									&& part3!=null
									&& part3.equals(part2)
									&& !part3.isAllowMultiple()))
					{
						publishErrorMessage(ALLOW_MULTIPLE_WARNING_MESSAGE);
						selectedComb.setInspectionPart2Name(StringUtils.EMPTY);
					}
					else
					{
						selectedComb.setInspectionPart2Name(part2.getInspectionPartName());
						if((!StringUtils.isBlank(selectedComb.getInspectionPartName()) 
								&& part1!=null
								&& part2.isPrimaryPosition()
								&& partHasLowerHierarchy(part2, part1)) ||
								(!StringUtils.isBlank(selectedComb.getInspectionPart3Name()) 
										&& part3 != null
										&& !part2.isPrimaryPosition()
										&& part3.hasLowerHierarchy(part2)))
						{
							if(part2.isPrimaryPosition())
							{
								if(MessageDialog.confirm(getDialog(), "The selected Part 2 has higher priority compared to Part 1. Please press 'Yes' to swap parts or press 'No' to select another part."))
									EventBusUtil.publish(new ObservableListChangeEvent("swapPart1Part2", ObservableListChangeEventType.CHANGE_SELECTION));
								else
									selectedComb.setInspectionPart2Name(StringUtils.EMPTY);
							}
							else
							{
								if(MessageDialog.confirm(getDialog(), "The selected Part 2 has lower priority compared to Part 3. Please press 'Yes' to swap parts or press 'No' to select another part."))
									EventBusUtil.publish(new ObservableListChangeEvent("swapPart2Part3", ObservableListChangeEventType.CHANGE_SELECTION));
								else
									selectedComb.setInspectionPart2Name(StringUtils.EMPTY);
							}
						}
						else
						{
							selectedComb.setInspectionPart2Name(part2.getInspectionPartName());
						}
					}
					getDialog().getFullPartNameValueLabel().setText(selectedComb.getFullPartDesc());
				}
			}
		});
	}
	/**
	 * This method is used to set listener on Part 2 Location 1 table.
	 */
	private void setPart2Loc1TableListeners()
	{
		setLocationTablePaneDeselectListener(getDialog().getPart2Location1TablePane(), 21);
		getDialog().getPart2Location1TablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiInspectionLocation>() {

			public void changed(
					ObservableValue<? extends QiInspectionLocation> arg0,
					QiInspectionLocation arg1, QiInspectionLocation arg2) {
				if(swapValue)
					return;
				clearDisplayMessage();
				QiInspectionLocation location1 = getDialog().getPart2Location1TablePane().getSelectedItem();
				QiInspectionLocation location2 = getDialog().getObjectfromList(locationList, selectedComb.getInspectionPart2Location2Name());
				if(location1!=null)
				{
					if(!StringUtils.isBlank(selectedComb.getInspectionPart2Location2Name()) 
							&& location2!=null
							&& location1.equals(location2))
					{
						publishErrorMessage(LOCATION_ALREADY_SELECTED_WARNING_MESSAGE);
						selectedComb.setInspectionPart2LocationName(StringUtils.EMPTY);
					}
					else
					{
						selectedComb.setInspectionPart2LocationName(location1.getInspectionPartLocationName());
						if(!StringUtils.isBlank(selectedComb.getInspectionPart2Location2Name())
								&& location2 != null
								&& location2.isPrimaryPosition()
								&& location2.hasLowerHierarchy(location1))
						{
							if(MessageDialog.confirm(getDialog(), "The selected Location 1 has lower priority compared to Location 2. Please press 'Yes' to swap locations or press 'No' to select another location."))
								EventBusUtil.publish(new ObservableListChangeEvent("swapPart2Location1Location2", ObservableListChangeEventType.CHANGE_SELECTION));
							else
								selectedComb.setInspectionPart2LocationName(StringUtils.EMPTY);
						}
						else
							selectedComb.setInspectionPart2LocationName(location1.getInspectionPartLocationName());
						getDialog().getFullPartNameValueLabel().setText(selectedComb.getFullPartDesc());
					}
				}
			}
		});
	}
	/**
	 * This method is used to set listener on Part 2 Location 2 table.
	 */
	private void setPart2Loc2TableListeners()
	{
		setLocationTablePaneDeselectListener(getDialog().getPart2Location2TablePane(), 22);
		getDialog().getPart2Location2TablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiInspectionLocation>() {

			public void changed(
					ObservableValue<? extends QiInspectionLocation> arg0,
					QiInspectionLocation arg1, QiInspectionLocation arg2) {
				if(swapValue)
					return;
				clearDisplayMessage();
				QiInspectionLocation location2 = getDialog().getPart2Location2TablePane().getSelectedItem();
				QiInspectionLocation location1 = getDialog().getObjectfromList(locationList, selectedComb.getInspectionPart2LocationName());
				if(location2!=null)
				{
					if(!StringUtils.isBlank(selectedComb.getInspectionPart2LocationName())
							&& location1!=null
							&& location2.equals(location1))
					{
						publishErrorMessage(LOCATION_ALREADY_SELECTED_WARNING_MESSAGE);
						selectedComb.setInspectionPart2Location2Name(StringUtils.EMPTY);
					}
					else
					{
						selectedComb.setInspectionPart2Location2Name(location2.getInspectionPartLocationName());
						if(!StringUtils.isBlank(selectedComb.getInspectionPart2LocationName())
								&& location1 != null
								&& location2.isPrimaryPosition() 
								&& location2.hasLowerHierarchy(location1))
						{
							if(MessageDialog.confirm(getDialog(), "The selected Location 2 has higher priority compared to Location 1. Please press 'Yes' to swap locations or press 'No' to select another location."))
								EventBusUtil.publish(new ObservableListChangeEvent("swapPart2Location1Location2", ObservableListChangeEventType.CHANGE_SELECTION));
							else
								selectedComb.setInspectionPart2Location2Name(StringUtils.EMPTY);
						}
						else
							selectedComb.setInspectionPart2Location2Name(location2.getInspectionPartLocationName());
						getDialog().getFullPartNameValueLabel().setText(selectedComb.getFullPartDesc());
					}
				}
			}	
		});
	}
	/**
	 * changed
	 * This method is used to set listener on Part 3 table.
	 */
	private void setPart3TableListeners()
	{
		setPartTablePaneDeselectListener(getDialog().getPart3TablePane(), 3);
		getDialog().getPart3TablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiInspectionPart>() {

			public void changed(
					ObservableValue<? extends QiInspectionPart> arg0,
					QiInspectionPart arg1, QiInspectionPart arg2) {
				if(swapValue)
					return;
				clearDisplayMessage();
				QiInspectionPart part3 = getDialog().getPart3TablePane().getSelectedItem();
				QiInspectionPart part2 = getDialog().getObjectfromList(partList, selectedComb.getInspectionPart2Name());
				if(part3!=null)
				{
					if(!StringUtils.isBlank(selectedComb.getInspectionPart2Name())
							&& part2!=null
							&& part2.equals(part3)
							&& !part2.isAllowMultiple())
					{
						publishErrorMessage(ALLOW_MULTIPLE_WARNING_MESSAGE);
						selectedComb.setInspectionPart3Name(StringUtils.EMPTY);
					}
					else
					{
						selectedComb.setInspectionPart3Name(part3.getInspectionPartName());
						if(!StringUtils.isBlank(selectedComb.getInspectionPart2Name())
								&& part2 != null
								&& !part2.isPrimaryPosition()
								&& part3.hasLowerHierarchy(part2))
						{
							if(MessageDialog.confirm(getDialog(), "The selected Part 3 has higher priority compared to Part 2. Please press 'Yes' to swap parts or press 'No' to select another part."))
								EventBusUtil.publish(new ObservableListChangeEvent("swapPart2Part3", ObservableListChangeEventType.CHANGE_SELECTION));
							else
								selectedComb.setInspectionPart3Name(StringUtils.EMPTY);
						}
						else
							selectedComb.setInspectionPart3Name(part3.getInspectionPartName());
					}
					getDialog().getFullPartNameValueLabel().setText(selectedComb.getFullPartDesc());
				}
			}
		});
	}
	/**
	 * This method is used to set deselect listener on Part table.
	 */
	private void setPartTablePaneDeselectListener(final ObjectTablePane<QiInspectionPart> partTblView, final int selection) {
		partTblView.getTable().setRowFactory(new Callback<TableView<QiInspectionPart>, TableRow<QiInspectionPart>>() {
			public TableRow<QiInspectionPart> call(TableView<QiInspectionPart> tableView) {
				final TableRow<QiInspectionPart> row = new TableRow<QiInspectionPart>();
				row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						final int index = row.getIndex();
						if (index >= 0 && index < partTblView.getTable().getItems().size() && partTblView.getTable().getSelectionModel().isSelected(index)) {
							partTblView.getTable().getSelectionModel().clearSelection(index);
							switch(selection) {
							case 1:
								selectedComb.setInspectionPartName(StringUtils.EMPTY);
								break;
							case 2:
								selectedComb.setInspectionPart2Name(StringUtils.EMPTY);
								break;
							case 3:
								selectedComb.setInspectionPart3Name(StringUtils.EMPTY);
								break;
							}
							getDialog().getFullPartNameValueLabel().setText(selectedComb.getFullPartDesc());
							event.consume();
						}
					}
				});
				return row;
			}
		});
	}
	/**
	 * This method is used to set deselect listener on location table.
	 */
	private void setLocationTablePaneDeselectListener(final ObjectTablePane<QiInspectionLocation> locationTablePane, final int selection) {
		locationTablePane.getTable().setRowFactory(new Callback<TableView<QiInspectionLocation>, TableRow<QiInspectionLocation>>() {
			public TableRow<QiInspectionLocation> call(TableView<QiInspectionLocation> tableView) {
				final TableRow<QiInspectionLocation> row = new TableRow<QiInspectionLocation>();
				row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						final int index = row.getIndex();
						if (index >= 0 && index < locationTablePane.getTable().getItems().size() && locationTablePane.getTable().getSelectionModel().isSelected(index)) {
							locationTablePane.getTable().getSelectionModel().clearSelection(index);
							switch(selection) {
							case 11:
								selectedComb.setInspectionPartLocationName(StringUtils.EMPTY);
								break;
							case 12:
								selectedComb.setInspectionPartLocation2Name(StringUtils.EMPTY);
								break;
							case 21:
								selectedComb.setInspectionPart2LocationName(StringUtils.EMPTY);
								break;
							case 22:
								selectedComb.setInspectionPart2Location2Name(StringUtils.EMPTY);
								break;
							}
							getDialog().getFullPartNameValueLabel().setText(selectedComb.getFullPartDesc());
							event.consume();
						}
					}
				});
				return row;
			}
		});
	}

	/**
	 * This method update Part Defect Combination related to defect 
	 */
	private void updatePartDefectCombStatus(List<Integer> partLocationIdList) {
		List<QiPartDefectCombination> qiPartDefectCombinationsList = getModel().findPartLocationIdsInPartDefectCombination(partLocationIdList);
		for (QiPartDefectCombination qiPartDefectCombination : qiPartDefectCombinationsList) {
			qiPartDefectCombination.setActive(false);
			qiPartDefectCombination.setUpdateUser(getUserId());
			getModel().updatePartDefectCombination(qiPartDefectCombination) ;
		}
	}
	
	private void reloadPart1Table() {
		try {
			clearDisplayMessage();
			getDialog().getPart1TablePane().setData(getModel().getFilteredActivePrimaryPartNames(StringUtils.trim(getDialog().getPart1FilterTextField().getText())));
		} catch (Exception e) {
			handleException("An error occured during filter action ", "Failed to perform filter action on Part 1", e);
		}
	}
	
	private void reloadPart1Loc1Table() {
		try {
			clearDisplayMessage();
			getDialog().getPart1Location1TablePane().setData(getModel().getFilteredActivePrimaryLocationNames(StringUtils.trim(getDialog().getPart1Location1FilterTextField().getText())));
		} catch (Exception e) {
			handleException("An error occured during filter action ", "Failed to perform filter action on Part 1 Location 1", e);
		}
	}
	
	private void reloadPart1Loc2Table() {
		try {
			clearDisplayMessage();
			getDialog().getPart1Location2TablePane().setData(getModel().getFilteredActiveLocationNames(StringUtils.trim(getDialog().getPart1Location2FilterTextField().getText())));
		} catch (Exception e) {
			handleException("An error occured during filter action ", "Failed to perform filter action on Part 1 Location 2", e);
		}
	}
	
	private void reloadPart2Table() {
		try {
			clearDisplayMessage();
			getDialog().getPart2TablePane().setData(getModel().getFilteredActivePartNames(StringUtils.trim(getDialog().getPart2FilterTextField().getText())));
		} catch (Exception e) {
			handleException("An error occured during filter action ", "Failed to perform filter action on Part 2", e);
		}
	}
	
	private void reloadPart2Loc1Table() {
		try {
			clearDisplayMessage();
			getDialog().getPart2Location1TablePane().setData(getModel().getFilteredActivePrimaryLocationNames(StringUtils.trim(getDialog().getPart2Location1FilterTextField().getText())));
		} catch (Exception e) {
			handleException("An error occured during filter action ", "Failed to perform filter action on Part 2 Location 1", e);
		}
	}
	
	private void reloadPart2Loc2Table() {
		try {
			clearDisplayMessage();
			getDialog().getPart2Location2TablePane().setData(getModel().getFilteredActiveLocationNames(StringUtils.trim(getDialog().getPart2Location2FilterTextField().getText())));
		} catch (Exception e) {
			handleException("An error occured during filter action ", "Failed to perform filter action on Part 2 Location 2", e);
		}
	}
	
	private void reloadPart3Table() {
		try {
			clearDisplayMessage();
			getDialog().getPart3TablePane().setData(getModel().getFilteredActiveSecondaryPartNames(StringUtils.trim(getDialog().getPart3FilterTextField().getText())));
		} catch (Exception e) {
			handleException("An error occured during filter action ", "Failed to perform filter action on Part 3", e);
		}
	}
	
	@Subscribe()
	public void onObjectTablePaneEvent(ObservableListChangeEvent event) {
		if (event == null)
			return;
		
		swapValue = true;
		 if(event.getEventType().equals(ObservableListChangeEventType.CHANGE_SELECTION)) {
			
			String swapTable = (String) event.getObject();
			if(swapTable != null && StringUtils.trim("swapPart1Part2").equals(swapTable)) {
				QiInspectionPart qiInspectionpart1 = getDialog().getPart1TablePane().getSelectedItem();
				QiInspectionPart qiInspectionpart2 = getDialog().getPart2TablePane().getSelectedItem();
				getDialog().getPart1FilterTextField().clear();
				getDialog().getPart2FilterTextField().clear();
				reloadPart1Table();
				reloadPart2Table();
				
				QiInspectionPart part1 = checkPartDataInList(getDialog().getPart1TablePane().getTable().getItems(), qiInspectionpart2);	
				if(part1 != null) {
					getDialog().getPart1TablePane().getTable().scrollTo(part1);
					getDialog().getPart1TablePane().getTable().getSelectionModel().select(part1);
				}
				
				QiInspectionPart part2 = checkPartDataInList(getDialog().getPart2TablePane().getTable().getItems(), qiInspectionpart1);	
				if(part2 != null) {
					getDialog().getPart2TablePane().getTable().scrollTo(part2);
					getDialog().getPart2TablePane().getTable().getSelectionModel().select(part2);
				}
				selectedComb.swapPart12();
				
			} else if(swapTable != null && StringUtils.trim("swapPart2Part3").equals(swapTable)) {
				QiInspectionPart qiInspectionpart2 = getDialog().getPart2TablePane().getSelectedItem();
				QiInspectionPart qiInspectionpart3 = getDialog().getPart3TablePane().getSelectedItem();
				getDialog().getPart2FilterTextField().clear();
				getDialog().getPart3FilterTextField().clear();
				reloadPart2Table();
				reloadPart3Table();
				
				QiInspectionPart part2 = checkPartDataInList(getDialog().getPart2TablePane().getTable().getItems(), qiInspectionpart3);	
				if(part2 != null) {
					getDialog().getPart2TablePane().getTable().scrollTo(part2);
					getDialog().getPart2TablePane().getTable().getSelectionModel().select(part2);
				}
				
				QiInspectionPart part3 = checkPartDataInList(getDialog().getPart3TablePane().getTable().getItems(), qiInspectionpart2);	
				if(part3 != null) {
					getDialog().getPart3TablePane().getTable().scrollTo(part3);
					getDialog().getPart3TablePane().getTable().getSelectionModel().select(part3);
				}
				selectedComb.swapPart23();
				
			} else if(swapTable != null && StringUtils.trim("swapPart1Location1Location2").equals(swapTable)) {
				QiInspectionLocation location1 = getDialog().getPart1Location1TablePane().getSelectedItem();
				QiInspectionLocation location2 = getDialog().getPart1Location2TablePane().getSelectedItem();
				getDialog().getPart1Location1FilterTextField().clear();
				getDialog().getPart1Location2FilterTextField().clear();
				reloadPart1Loc1Table();
				reloadPart1Loc2Table();
				
				QiInspectionLocation part1loc1 = checkInspectionLocationDataInList(getDialog().getPart1Location1TablePane().getTable().getItems(), location2);	
				if (part1loc1 != null) {
					getDialog().getPart1Location1TablePane().getTable().scrollTo(part1loc1);
					getDialog().getPart1Location1TablePane().getTable().getSelectionModel().select(part1loc1);
				}
				
				QiInspectionLocation part1loc2 = checkInspectionLocationDataInList(getDialog().getPart1Location2TablePane().getTable().getItems(), location1);
				if (part1loc2 != null) {
					getDialog().getPart1Location2TablePane().getTable().scrollTo(part1loc2);
					getDialog().getPart1Location2TablePane().getTable().getSelectionModel().select(part1loc2);
				}
				selectedComb.swapPartLocation12();		
			} else if(swapTable != null && StringUtils.trim("swapPart2Location1Location2").equals(swapTable)) {
				QiInspectionLocation location1 = getDialog().getPart2Location1TablePane().getSelectedItem();
				QiInspectionLocation location2 = getDialog().getPart2Location2TablePane().getSelectedItem();
				
				getDialog().getPart2Location1FilterTextField().clear();
				getDialog().getPart2Location2FilterTextField().clear();
				reloadPart2Loc1Table();
				reloadPart2Loc2Table();
				
				QiInspectionLocation part2loc1 = checkInspectionLocationDataInList(getDialog().getPart2Location1TablePane().getTable().getItems(), location2);	
				if (part2loc1 != null) {
					getDialog().getPart2Location1TablePane().getTable().scrollTo(part2loc1);
					getDialog().getPart2Location1TablePane().getTable().getSelectionModel().select(part2loc1);
				}
				
				QiInspectionLocation part2loc2 = checkInspectionLocationDataInList(getDialog().getPart2Location2TablePane().getTable().getItems(), location1);
				if (part2loc2 != null) {
					getDialog().getPart2Location2TablePane().getTable().scrollTo(part2loc2);
					getDialog().getPart2Location2TablePane().getTable().getSelectionModel().select(part2loc2);
				}
				selectedComb.swapPart2Location12();
			}
			getDialog().getFullPartNameValueLabel().setText(selectedComb.getFullPartDesc());
		}
		 swapValue = false;
	}
	
	private QiInspectionLocation checkInspectionLocationDataInList(ObservableList<QiInspectionLocation> list, QiInspectionLocation record) {
		QiInspectionLocation inspectionPart = null;
		for(QiInspectionLocation part : list) {
			if(part.getInspectionPartLocationName().equals(record.getInspectionPartLocationName())){
				inspectionPart = part;
			}
		}
		return inspectionPart;
	}
	
	private QiInspectionPart checkPartDataInList(ObservableList<QiInspectionPart> list, QiInspectionPart record) {
		QiInspectionPart inspectionPart = null;
		for(QiInspectionPart part : list) {
			if(part.getInspectionPartName().equals(record.getInspectionPartName())){
				inspectionPart = part;
			}
		}
		return inspectionPart;
	}
	
	private boolean partHasLowerHierarchy(QiInspectionPart firstPart, QiInspectionPart secondPart) {
		if (firstPart.getHierarchy() > 0)
			return firstPart.getHierarchy() < secondPart.getHierarchy();
		else
			return false;
	}
	
}	
