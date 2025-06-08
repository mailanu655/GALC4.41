package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.controller.PartDefectCombinationController.ListType;
import com.honda.galc.client.teamleader.qi.model.PartDefectCombMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.PartDefectCombinationDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiDefect;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.entity.qi.QiPartLocationCombination;
import com.honda.galc.util.AuditLoggerUtil;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PartDefectCombinationDialogController</code> is the controller class for Part Defect Combination Dialog.
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
public class PartDefectCombinationDialogController extends QiDialogController<PartDefectCombMaintenanceModel, PartDefectCombinationDialog> {

	private static final String COMB_ALREADY_EXIST_MSG = "Selected combination(s) already exists. Please create a new combination(s).";
	private static final String MANDATORY_REASON_FOR_CHANGE_MSG = "Please enter Reason for Change.";
	private static final String INACTIVE_SECONDARY_DEFECT_MSG = "Update Failed! Secondary defect is Inactive";
	private static final String INACTIVE_PRIMARY_DEFECT_MSG = "Update Failed! Primary Defect is Inactive.";
	private static final String INACTIVE_QICS_FULL_PART_NAME_MSG = "Update Failed! QICS Full Part Name is Inactive.";
	private static final String MANDATORY_PRIMARY_DEFECT_MSG = "Please select atleast one Primary Defect.";
	private static final String MANDATORY_QICS_FULL_PART_NAME_MSG = "Please select atleast one QICS Full Part Name.";
	private QiPartDefectCombination selectedComb;
	private List<QiPartLocationCombination> partList;
	private List<QiDefect> primaryDefectList;
	private List<QiDefect> secondaryDefectList;
	private List<QiPartDefectCombination> partDefectCombList;
	private QiPartDefectCombination selectedCombCloned;
	private PartDefectCombinationController mainPanelController;

	public PartDefectCombinationDialogController(PartDefectCombMaintenanceModel model, PartDefectCombinationDialog dialog, QiPartDefectCombination selectedComb, PartDefectCombinationController parentController) {
		super();
		setModel(model);
		setDialog(dialog);
		this.selectedComb = selectedComb;
		this.selectedCombCloned = (QiPartDefectCombination) selectedComb.deepCopy();
		mainPanelController = parentController;
	}

	@Override
	public void initListeners() {
		setFullPartTableListeners();
		setPrimaryDefectTableListeners();
		setSecondaryDefectTableListeners();
		
		getDialog().getActiveRadioBtn().getToggleGroup().selectedToggleProperty().addListener(updateEnablerForToggle);
		
		addUpdateEnablerForTableChangeListener(getDialog().getFullPartTablePane());
		addUpdateEnablerForTableChangeListener(getDialog().getPrimaryDefectTablePane());
		addUpdateEnablerForTableChangeListener(getDialog().getSecondaryDefectTablePane());
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof LoggedButton) {
			if(actionEvent.getSource().equals(getDialog().getCreateBtn())) createBtnAction(actionEvent);
			else if(actionEvent.getSource().equals(getDialog().getUpdateButton())) updateBtnAction(actionEvent);
			else if(actionEvent.getSource().equals(getDialog().getCancelBtn())) cancelBtnAction(actionEvent);
		}
		else if(actionEvent.getSource() instanceof UpperCaseFieldBean) {
			if(actionEvent.getSource().equals(getDialog().getFullPartFilterTextField())) reloadFullPartNameTable();
			else if(actionEvent.getSource().equals(getDialog().getPrimaryDefectFilterTextField())) reloadPrimaryDefectTable();
			else if(actionEvent.getSource().equals(getDialog().getSecondaryDefectFilterTextField())) reloadSecondaryDefectTable();
		}
	}

	/**
	 * This method is used to create a Part Defect Combination.
	 * @param event
	 */
	public void createBtnAction(ActionEvent event) {
		clearDisplayMessage();
		try {
			getTableList();
			if(partList.isEmpty()) 
				publishErrorMessage(MANDATORY_QICS_FULL_PART_NAME_MSG);
			else {
				if(primaryDefectList.isEmpty()) 
					publishErrorMessage(MANDATORY_PRIMARY_DEFECT_MSG);
				else {
					if(!isPartDefectCombinationExist()) 
						createPartDefectCombination();
					else {
						if(partDefectCombList.isEmpty()) 
							publishErrorMessage(COMB_ALREADY_EXIST_MSG);
						else {
							if(MessageDialog.confirm(getDialog(), "Some of the selected combination(s) already exist. New combination(s) will be created. Do you wish to continue?")) 
								createPartDefectCombination();
						}
						return;
					}
				}
			}
		} catch (Exception e) {
			handleException("An error occured in during create action ", "Failed to Create Part Defect Combination", e);
		}
	}

	private void createPartDefectCombination() {
		getModel().createPartDefectComb(partDefectCombList);
		Stage stage = (Stage) getDialog().getCreateBtn().getScene().getWindow();
		stage.close();
	}

	/**
	 * This method is used to update Part Defect Combination.
	 * @param event
	 */
	public void updateBtnAction(ActionEvent event)
	{
		boolean wasActive = selectedCombCloned.isActive();
		boolean isActive = getDialog().getActiveRadioBtn().isSelected();
		QiPartLocationCombination partLocComb = getModel().findPartLocCombByKey(selectedComb.getPartLocationId());
		QiDefect primaryDefect = getModel().findDefectByKey(selectedComb.getDefectTypeName());
		QiDefect secondaryDefect = getModel().findDefectByKey(selectedComb.getDefectTypeName2());
		clearDisplayMessage();
		selectedComb.setUpdateUser(getUserId());
		List<Integer> partDefectIdList = new ArrayList<Integer>();
		partDefectIdList.add(selectedComb.getRegionalDefectCombinationId());		
		List<QiPartDefectCombination> regionalAttributeList= getModel().findAllRegionalAttributesByPartDefectId(partDefectIdList);
		try {
			getTableList();
			if(partList.isEmpty()) {
				if(selectedComb.getPartLocationId() != null && (partLocComb == null ? true : !partLocComb.isActive())) 
					publishErrorMessage(INACTIVE_QICS_FULL_PART_NAME_MSG);
				else 
					publishErrorMessage(MANDATORY_QICS_FULL_PART_NAME_MSG);
				return;
			}
			if(primaryDefectList.isEmpty()) {
				if(selectedComb.getDefectTypeName() != null && (primaryDefect == null ? true : !primaryDefect.isActive())) 
					publishErrorMessage(INACTIVE_PRIMARY_DEFECT_MSG);
				else 
					publishErrorMessage(MANDATORY_PRIMARY_DEFECT_MSG);
				return;
			}
			else {
				if(secondaryDefectList.isEmpty()) {
					if(StringUtils.trimToNull(selectedComb.getDefectTypeName2()) != null && (secondaryDefect == null ? true : !secondaryDefect.isActive())) {
						publishErrorMessage(INACTIVE_SECONDARY_DEFECT_MSG);
						return;
					}
					else 
						selectedComb.setDefectTypeName2(StringUtils.EMPTY);
				}
				selectedComb.setProductKind(getModel().getProductKind());
				if(getModel().checkPartDefectCombination(selectedComb)) 
					publishErrorMessage(COMB_ALREADY_EXIST_MSG);
				else {
					if(StringUtils.isEmpty(getDialog().getReasonForChangeText())) 
						publishErrorMessage(MANDATORY_REASON_FOR_CHANGE_MSG);
					else {
						
						selectedComb.setActive(getDialog().getActiveRadioBtn().isSelected());
						selectedComb.setUpdateUser(getUserId());
						if((!regionalAttributeList.isEmpty())){
							if(!MessageDialog.confirm(getDialog(),QiCommonUtil.getMessage("part defect combination(s)",0,regionalAttributeList.size(),0).toString()))
								return;
							else if ((!isActive && wasActive)){
								String returnValue=isLocalSiteImpacted(partDefectIdList,getDialog().getScreenName(),getDialog());
								if(returnValue.equals(QiConstant.NO_LOCAL_SITES_CONFIGURED)){
									return;
								}
								else if(returnValue.equals(QiConstant.LOCAL_SITES_IMPACTED)){
									displayErrorMessage(" PDC(s) cannot be inactivated", "Inactivation of PDC(s) affects Local Sites");
									return;
								}
							}
						}
						if (isUpdated(selectedCombCloned)) {
							return;
						}
						getModel().updatePartDefectComb(selectedComb);

						if(
								(!isActive && (getDialog().getListType() == ListType.ACTIVE))
								|| (isActive && (getDialog().getListType() == ListType.INACTIVE)))  {
							//changed from active to inactive in Active list or inactive to active in Inactive list
							mainPanelController.removeSelectedFromList();
						}
						//call to capture audit information
						AuditLoggerUtil.logAuditInfo(selectedCombCloned, selectedComb,
								getDialog().getReasonForChangeText(), getDialog().getScreenName(),
								getModel().getAuditPrimaryKeyValue(
										selectedCombCloned.getRegionalDefectCombinationId()),getUserId());

						Stage stage = (Stage) getDialog().getUpdateButton().getScene().getWindow();
						stage.close();
					}
				}
			}
		}

		catch (Exception e) {
			handleException("An error occured in during update action ", "Failed to Update Part Defect Combination", e);
		}
	}

	/**
	 * This method is used to close the Popup.
	 * @param event
	 */
	public void cancelBtnAction(ActionEvent event) {
		clearDisplayMessage();
		try {
			Stage stage = (Stage) getDialog().getCancelBtn().getScene().getWindow();
			stage.close();
			getDialog().setCancel(true);
		} catch (Exception e) {
			handleException("An error occured in during cancel action ", "Failed to perform cancel action", e);
		}
	}

	/**
	 * This method is used to set listener on QICS Full Part Name table.
	 */
	private void setFullPartTableListeners()
	{
		getDialog().getFullPartTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiPartLocationCombination>() {

			public void changed(
					ObservableValue<? extends QiPartLocationCombination> observableValue,
					QiPartLocationCombination oldValue,
					QiPartLocationCombination newValue) {
				clearDisplayMessage();
				if(newValue != null && selectedComb != null && getDialog().getTitle().equalsIgnoreCase(QiConstant.UPDATE)) 
					selectedComb.setPartLocationId(newValue.getPartLocationId());
			}
		});
	}

	/**
	 * This method is used to set listener on Primary Defect table.
	 */
	private void setPrimaryDefectTableListeners()
	{
		getDialog().getPrimaryDefectTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiDefect>() {

			public void changed(ObservableValue<? extends QiDefect> observableValue,
					QiDefect oldValue, QiDefect newValue) {
				clearDisplayMessage();
				if(newValue != null && selectedComb != null && getDialog().getTitle().equalsIgnoreCase(QiConstant.UPDATE)) 
					selectedComb.setDefectTypeName(newValue.getDefectTypeName());
			}
		});
	}

	/**
	 * This method is used to set listener on Secondary Defect table.
	 */
	private void setSecondaryDefectTableListeners()
	{
		getDialog().getSecondaryDefectTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiDefect>() {

			public void changed(ObservableValue<? extends QiDefect> observableValue,
					QiDefect oldValue, QiDefect newValue) {
				clearDisplayMessage();
				if(newValue != null && selectedComb != null && getDialog().getTitle().equalsIgnoreCase(QiConstant.UPDATE)) 
					selectedComb.setDefectTypeName2(StringUtils.trimToEmpty(newValue.getDefectTypeName()));
			}
		});
	}

	/**
	 * This method is used to get List of all selected values from Part and Defect tables.
	 */
	private void getTableList() {
		partList = getDialog().getFullPartTablePane().getSelectedItems();
		primaryDefectList = getDialog().getPrimaryDefectTablePane().getSelectedItems();
		secondaryDefectList = getDialog().getSecondaryDefectTablePane().getSelectedItems();
	}

	/**
	 * This method returns true if Part Defect Combination already exist and displays appropriate message.
	 * @return
	 */
	private boolean isPartDefectCombinationExist() {
		boolean isExist = false;
		partDefectCombList = new ArrayList<QiPartDefectCombination>();
		for(QiPartLocationCombination partLocComb : partList) {
			for(QiDefect primaryDefect : primaryDefectList) {
				if(!secondaryDefectList.isEmpty()) {
					for(QiDefect secondaryDefect : secondaryDefectList) {
						setObjectForCreate(partLocComb, primaryDefect, secondaryDefect);
						if(getModel().checkPartDefectCombination(selectedComb)) {
							isExist = true;
						}
						else {
							partDefectCombList.add(selectedComb);
						}
					}
				} else {
					setObjectForCreate(partLocComb, primaryDefect, null);
					if(getModel().checkPartDefectCombination(selectedComb)) {
						isExist = true;
					}
					else {
						partDefectCombList.add(selectedComb);
					}
				}
			}
		}
		return isExist;
	}

	/**
	 * This method is used to set object for Part Defect Combination for Create operation.
	 * @param partLocComb
	 * @param primaryDefect
	 * @param secondaryDefect
	 */
	private void setObjectForCreate(QiPartLocationCombination partLocComb, QiDefect primaryDefect, QiDefect secondaryDefect) {
		selectedComb = new QiPartDefectCombination();
		selectedComb.setPartLocationId(partLocComb.getPartLocationId());
		selectedComb.setDefectTypeName(primaryDefect.getDefectTypeName());
		selectedComb.setDefectTypeName2(
				secondaryDefect == null ? StringUtils.EMPTY : StringUtils.trimToEmpty(secondaryDefect.getDefectTypeName()));
		selectedComb.setProductKind(getModel().getProductKind());
		selectedComb.setActive(getDialog().getActiveRadioBtn().isSelected());
		selectedComb.setCreateUser(getUserId());
	}

	/**
	 * Reload Full Part Name Table
	 */
	private void reloadFullPartNameTable() {
		String filterText = getDialog().getFullPartFilterTextField().getText();
		List<Short> statusList = new ArrayList<Short>();
		statusList.add((short)1);
		statusList.add((short)2);
		try {
			clearDisplayMessage();
			getDialog().getFullPartTablePane().setData(getModel().getPartLocCombByFilter(StringUtils.trim(filterText), statusList));
		} catch (Exception e) {
			handleException("An error occured during filter action ", "Failed to perform filter action on QICS Full Part", e);
		}
	}

	/**
	 * Reload Primary Defect Table
	 */
	private void reloadPrimaryDefectTable() {
		String filterText = getDialog().getPrimaryDefectFilterTextField().getText();
		try {
			clearDisplayMessage();
			getDialog().getPrimaryDefectTablePane().setData(getModel().getActivePrimaryDefectByFilter(StringUtils.trim(filterText)));
		} catch (Exception e) {
			handleException("An error occured during filter action ", "Failed to perform filter action on Primary Defect", e);
		}
	}

	/**
	 * Reload Secondary Defect Table
	 */
	private void reloadSecondaryDefectTable() {
		String filterText = getDialog().getSecondaryDefectFilterTextField().getText();
		try {
			clearDisplayMessage();
			getDialog().getSecondaryDefectTablePane().setData(getModel().getActiveSecondaryDefectByFilter(StringUtils.trim(filterText)));
		} catch (Exception e) {
			handleException("An error occured during filter action ", "Failed to perform filter action on Secondary Defect", e);
		}
	}
}	
