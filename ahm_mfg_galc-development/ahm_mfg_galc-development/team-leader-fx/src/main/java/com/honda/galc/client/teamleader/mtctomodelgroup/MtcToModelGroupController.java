package com.honda.galc.client.teamleader.mtctomodelgroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.mvc.AbstractController;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.dto.MtcToModelGroupDto;
import com.honda.galc.entity.product.ModelGroup;
import com.honda.galc.entity.product.ModelGroupId;
import com.honda.galc.entity.product.ModelGrouping;
import com.honda.galc.util.AuditLoggerUtil;
import com.honda.galc.util.ObjectComparator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>MtcToModelGroupController</code> is the controller class for Mtc model to
 * Model Group Panel.
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>31/03/2017</TD>
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
public class MtcToModelGroupController extends AbstractController<MtcToModelGroupMaintenanceModel, MtcToModelGroupPanel>
implements EventHandler<ActionEvent> {

	private static final String SAVE_REASON_FOR_MTC_TO_MODEL_GROUP_AUDIT = "MTC Model removed from Model Group.";

	public MtcToModelGroupController(MtcToModelGroupMaintenanceModel model, MtcToModelGroupPanel view) {
		super(model, view);
		getModel().setApplicationContext(getView().getMainWindow().getApplicationContext());
	}

	ChangeListener<String> productTypeChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			inputChanged();
		}
	};

	ChangeListener<String> systemChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			inputChanged();
		}
	};

	public void inputChanged() {
		try {
			clearMessage();
			getView().getSaveBtn().setDisable(true);
			getView().getDeassignBtn().setDisable(true);
			getView().getAssignBtn().setDisable(true);
			getView().getMtcModelNameFilterTextField().clear();
			clearModelGroupTableData();
			clearAssginedTableData();
			clearAvailableTableData();
			if (getView().getSelectedProductType() != null && getView().getSelectedSystem() != null) {
				getView().reloadModelGroup(StringUtils.trimToEmpty(getView().getSelectedProductType()), StringUtils.trimToEmpty(getView().getSelectedSystem()));
			}
			getView().reload();
			disableMTCmodelTables();
			if (isFullAccess()) {
				addContextMenuItems();
			}
		} catch (Exception e) {
			clearModelGroupTableData();
			clearAssginedTableData();
			clearAvailableTableData();
			displayErrorMessage("An error occurred while searching data ,Failed to search Avl MTC Model data ");
		}
	}

	ChangeListener<ModelGroup> modelGroupTablePaneChangeListener = new ChangeListener<ModelGroup>() {
		public void changed(ObservableValue<? extends ModelGroup> arg0, ModelGroup arg1,
				ModelGroup arg2) {
			clearMessage();
			getView().getSaveBtn().setDisable(true);
			if(isFullAccess()){
				setAssignButton();
				setDeassignButton();
				addContextMenuItems();
			}
			if (getView().getSelectedModelGroup() != null) {
				getView().getAvailableMtcModelgroupingTablePane().setDisable(false);
				getView().getAssignedModelGroupingTablePane().setDisable(false);
				getView().getMtcModelNameFilterTextField().setDisable(false);
				getView().getAssignedModelGroupingTablePane().setData(setMtcModelYearDescription(StringUtils.trimToEmpty(getView().getSelectedModelGroup().getModelGroup())));
			}
			getView().reload();	
		}
	};

	/**
	 * This method is used to add listener on main Model Group and Available Mtc
	 * Model table.
	 */
	@Override
	public void initEventHandlers() {
		clearMessage();
		if(isFullAccess()){
			addContextMenuItems();
		}
		addProductTypeListener();
		addSystemListener();
		addModelGroupTableListener();
		addAvailableMtcModelTableListener();
	}

	/**
	 * This method is the Listener for Product Type ComboBox.
	 */
	private void addProductTypeListener() {
		getView().getProductType().getControl().valueProperty().addListener(productTypeChangeListener);
	}

	/**
	 * This method adds the corresponding change listener to the System ComboBox.
	 */
	private void addSystemListener() {
		getView().getSystem().getControl().valueProperty().addListener(systemChangeListener);
	}

	/**
	 * This method is the Listener for filter.
	 */
	private void addAvailableMtcModelTableListener() {
		getView().getAvailableMtcModelgroupingTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MtcToModelGroupDto>() {
			public void changed(ObservableValue<? extends MtcToModelGroupDto> observableValue, MtcToModelGroupDto oldValue,MtcToModelGroupDto newValue) {
				clearMessage();
				if(isFullAccess()){
					if (!getModel().getProperty().isOneGroupPerModel()) {
						setAssignButton();
					} else if (newValue != null) {
						String mtcModel = newValue.getModelYearCode() + newValue.getModelCode();
						if (getModel().findExistingModelGroupings(mtcModel, getView().getSelectedSystem(), getView().getSelectedProductType()) == null) {
							setAssignButton();
						}
					}
				}
			}
		});

		getView().getAssignedModelGroupingTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MtcToModelGroupDto>() {
			public void changed(ObservableValue<? extends MtcToModelGroupDto> arg0, MtcToModelGroupDto arg1,MtcToModelGroupDto arg2) {
				clearMessage();
				if(isFullAccess()){
					setDeassignButton();
				}
			}

		});

	}
	/**
	 * This method is used to reload assigned Mtc based on model group selection.
	 */
	private void addModelGroupTableListener() {
		getView().getModelGroupTable().getSelectionModel().selectedItemProperty().addListener(modelGroupTablePaneChangeListener);

		getView().getModelGroupTable().setRowFactory(new Callback<TableView<ModelGroup>, TableRow<ModelGroup>>() {
			public TableRow<ModelGroup> call (TableView<ModelGroup> tableView) {
				final TableRow<ModelGroup> row= new TableRow<ModelGroup>();
				row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						final int index= row.getIndex();
						if(index >= 0 && index < getView().getModelGroupTable().getItems().size() && getView().getModelGroupTable().getSelectionModel().isSelected(index) && event.isControlDown()) {
							getView().getModelGroupTable().getSelectionModel().clearSelection(index);
							clearMessage();
							clearAssginedTableData();
							disableMTCmodelTables();
							getView().reload();
							event.consume();
						}
					}

				});
				return row;
			}
		});

	}

	private void clearModelGroupTableData() {
		if (getView().getModelGroupTable().getItems() != null)
			getView().getModelGroupTable().getItems().clear();
	}

	/**
	 * This method is used to clear assigned mtc table.
	 */
	private void clearAssginedTableData() {
		if (getView().getAssignedModelGroupingTable().getItems() != null)
			getView().getAssignedModelGroupingTable().getItems().clear();
	}

	/**
	 * This method is used to clear assigned mtc table.
	 */
	private void clearAvailableTableData() {
		if (getView().getAvailableMtcModelgroupingTable().getItems() != null)
			getView().getAvailableMtcModelgroupingTable().getItems().clear();
	}

	/**
	 * This method is used to enable/disable assign button.
	 */
	private void setAssignButton() {
		if (getView().getSelectedAvailableMtcModelgrouping() != null && getView().getSelectedModelGroup() != null)
			getView().getAssignBtn().setDisable(false);
		else
			getView().getAssignBtn().setDisable(true);
	}
	/**
	 * This method is used to enable/disable deassign button.
	 */
	private void setDeassignButton() {
		if (getView().getSelectedAssignedModelGrouping() != null && getView().getSelectedModelGroup() != null){
			getView().getDeassignBtn().setDisable(false);
		}
		else
			getView().getDeassignBtn().setDisable(true);
	}

	public void handle(ActionEvent event) {
		if (event.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) event.getSource();
			handlerForMenuItem(menuItem,event);		
		}
		else if (event.getSource() instanceof LoggedButton) {
			LoggedButton button = (LoggedButton) event.getSource();
			handlerForLoggedButton(button,event);	
		}
		else if (event.getSource() instanceof LoggedRadioButton) {
			LoggedRadioButton radioButton = (LoggedRadioButton) event.getSource();
			handlerForLoggedRadioButton(radioButton,event);
		}
		else if(event.getSource() instanceof UpperCaseFieldBean){
			handleForUpperCaseFieldBean();
		}
	}
	private void handleForUpperCaseFieldBean() {
		try {
			clearMessage();
			getView().reload();
		} 
		catch (Exception e) {
			displayErrorMessage("An error occurred while searching data ,Failed to search Mtc Model data ");
		}

	}

	/**
	 * This method is used to handle loggedRadioButton action.
	 */
	private void handlerForLoggedRadioButton(LoggedRadioButton radioButton, ActionEvent event) {
		if (MtcToModelGroupPanel.ALL.equalsIgnoreCase(radioButton.getText())) {
			allRadioButtonAction(event);
		} else if (MtcToModelGroupPanel.ACTIVE.equalsIgnoreCase(radioButton.getText())) {
			activeRadioButtonAction(event);
		} else if (MtcToModelGroupPanel.INACTIVE.equalsIgnoreCase(radioButton.getText())) {
			inactiveRadioButtonAction(event);
		}
	}
	/**
	 * This method is used to handle loggedButton action.
	 */
	private void handlerForLoggedButton(LoggedButton button, ActionEvent event) {

		if (MtcToModelGroupPanel.DEASSIGN.equals(button.getId())) {
			deassignButtonAction(event);
			getView().getSaveBtn().setDisable(false);
			if (!isFullAccess()) {
				getView().getSaveBtn().setDisable(true);
			}
		} else if (MtcToModelGroupPanel.ASSIGN.equals(button.getId())) {
			assignButtonAction(event);
			getView().getSaveBtn().setDisable(false);
			if (!isFullAccess()) {
				getView().getSaveBtn().setDisable(true);
			}
		} else if (MtcToModelGroupPanel.SAVE.equalsIgnoreCase(button.getText()))
			saveButtonAction(event);
		else if (MtcToModelGroupPanel.REFRESH.equalsIgnoreCase(button.getText()))
		{
			refreshBtnAction();
			activate();
			EventBusUtil.publish(new StatusMessageEvent("Refreshed successfully", StatusMessageEventType.INFO));
		}
	}

	/**
	 * This method is used to refresh the data.
	 */
	public void refreshBtnAction() {
		getView().getProductType().getControl().valueProperty().removeListener(productTypeChangeListener);
		getView().getSystem().getControl().valueProperty().removeListener(systemChangeListener);
		getView().getModelGroupTable().getSelectionModel().selectedItemProperty().removeListener(modelGroupTablePaneChangeListener);

		String productType = refreshProductTypes();
		String system = refreshSystems();
		int selectedModelGroup = getView().getModelGroupTable().getSelectionModel().getSelectedIndex();

		List<Integer> selectedAvailablMtcModelList = new ArrayList<Integer>();
		List<MtcToModelGroupDto> selectedAssignedMtcModelList = new ArrayList<MtcToModelGroupDto>();
		selectedAvailablMtcModelList.addAll(getView().getAvailableMtcModelgroupingTable().getSelectionModel().getSelectedIndices());
		selectedAssignedMtcModelList.addAll(getView().getAssignedModelGroupingTable().getSelectionModel().getSelectedItems());

		if (productType != null && system != null) {
			getView().reloadModelGroup(productType, system);
			getView().getModelGroupTable().getSelectionModel().select(selectedModelGroup);
			getView().getMtcModelNameFilterTextField().getControl().settext(StringUtils.trimToEmpty(getView().getMtcModelNameFilterTextField().getText()));
			getView().reload();
			if (getView().getSelectedModelGroup() != null) {
				getView().getAssignedModelGroupingTablePane().setData(setMtcModelYearDescription(StringUtils.trimToEmpty(getView().getSelectedModelGroup().getModelGroup())));
			}
			for (int selectedAvailablMtcModel : selectedAvailablMtcModelList) { 
				getView().getAvailableMtcModelgroupingTable().getSelectionModel().select(selectedAvailablMtcModel);
			}
			for (MtcToModelGroupDto selectedAssignedMtcModel : selectedAssignedMtcModelList) { 
				getView().getAssignedModelGroupingTable().getSelectionModel().select(selectedAssignedMtcModel);
			}
		} else {
			getView().getSaveBtn().setDisable(true);
			getView().getDeassignBtn().setDisable(true);
			getView().getAssignBtn().setDisable(true);
			getView().getMtcModelNameFilterTextField().clear();
			clearModelGroupTableData();
			clearAssginedTableData();
			clearAvailableTableData();
		}
	}

	/**
	 * Refreshes the product type list in its corresponding drop-down.<br>
	 * Returns the product type which was selected, or null if no selection was made.
	 */
	private String refreshProductTypes() {
		String productType = getView().getProductType().getControl().getValue();
		getView().getProductType().getControl().getItems().clear();
		List<String> productTypeList = getModel().getProductTypes();
		if (productTypeList.size() > 0) {
			getView().getProductType().getControl().getItems().addAll(productTypeList);
			if (productTypeList.size() == 1) {
				productType = productTypeList.get(0);
			}
		}
		if (productType != null && productTypeList.contains(productType)) {
			getView().getProductType().getControl().setValue(productType);
			return productType;
		}
		return null;
	}

	/**
	 * Refreshes the system list in its corresponding drop-down.<br>
	 * Returns the system which was selected, or null if no selection was made.
	 */
	private String refreshSystems() {
		String system = getView().getSystem().getControl().getValue();
		getView().getSystem().getControl().getItems().clear();
		List<String> systemList = getModel().getSystems();
		if (systemList.size() > 0) {
			getView().getSystem().getControl().getItems().addAll(systemList);
			if (systemList.size() == 1) {
				system = systemList.get(0);
			}
		}
		if (system != null && systemList.contains(system)) {
			getView().getSystem().getControl().setValue(system);
			return system;
		}
		return null;
	}

	/**
	 * This method is used to handle MenuItem action.
	 */
	private void handlerForMenuItem(MenuItem menuItem,ActionEvent event) {
		if (MtcToModelGroupPanel.CREATE.equals(menuItem.getText()))
			createModelGroup(event);
		else if (MtcToModelGroupPanel.UPDATE.equals(menuItem.getText()))
			updateModelGroup(event);
		else if (MtcToModelGroupPanel.INACTIVATE.equals(menuItem.getText()))
			inactivateModelGroup(event);
		else if (MtcToModelGroupPanel.REACTIVATE.equals(menuItem.getText()))
			reactivateModelGroup(event);
		else if (MtcToModelGroupPanel.DELETE.equals(menuItem.getText()))
			deleteModelGroup(event);
	}

	/**
	 * This method is used to inactivate model group.
	 */
	private void inactivateModelGroup(ActionEvent event) {
		updateModelGroupStatus(false);
		getView().reloadModelGroup(StringUtils.trimToEmpty(getView().getSelectedProductType()), StringUtils.trimToEmpty(getView().getSelectedSystem()));
		addContextMenuItems();
	}
	/**
	 * This method is used to reactivate model group.
	 */
	private void reactivateModelGroup(ActionEvent event) {
		updateModelGroupStatus(true);
		getView().reloadModelGroup(StringUtils.trimToEmpty(getView().getSelectedProductType()), StringUtils.trimToEmpty(getView().getSelectedSystem()));
		addContextMenuItems();
	}
	/**
	 * This method is used to update model group status.
	 */
	private void updateModelGroupStatus(boolean isActive) {
		ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
		if (dialog.showReasonForChangeDialog(null)) {
			try {
				clearMessage();
				ModelGroup modelGroup=getView().getSelectedModelGroup();
				ModelGroup modelGroupCloned=(ModelGroup) modelGroup.deepCopy();
				getView().getSelectedModelGroup().setActive((short) (isActive ? 1 : 0));
				getView().getSelectedModelGroup().setUpdateUser(getModel().getUserId());
				getModel().updateModelGroup(getView().getSelectedModelGroup());
				AuditLoggerUtil.logAuditInfo(modelGroupCloned, modelGroup, dialog.getReasonForChangeText(),getView().getScreenName(),getModel().getUserId());
			} catch (Exception e) {
				displayErrorMessage("An error occured in updateModelGroupStatus method ,Failed to update status");
			}
		}
		clearAssginedTableData();
		disableMTCmodelTables();
	}
	/**
	 * This method is used to update model group.
	 */
	private void updateModelGroup(ActionEvent event) {
		MtcToModelGroupDialog dialog =new MtcToModelGroupDialog(MtcToModelGroupPanel.UPDATE, getView().getSelectedModelGroup(), getModel(), getApplicationId());
		dialog.setScreenName(getView().getScreenName());
		dialog.showDialog();
		if (!dialog.isCancelled()) {
			dialogOperationRefresh();
		}
	}

	/**
	 * This method is used to create Model Group.
	 */
	private void createModelGroup(ActionEvent event) {
		ModelGroup modelGroup = new ModelGroup();
		ModelGroupId modelGroupId = new ModelGroupId();
		modelGroup.setId(modelGroupId);
		modelGroup.setProductType(StringUtils.trimToEmpty(getView().getSelectedProductType()));
		modelGroup.setSystem(StringUtils.trimToEmpty(getView().getSelectedSystem()));
		MtcToModelGroupDialog dialog = new MtcToModelGroupDialog(MtcToModelGroupPanel.CREATE, modelGroup, getModel(),getApplicationId());
		dialog.showDialog();
		if (!dialog.isCancelled()) {
			dialogOperationRefresh();
		}
	}

	/**
	 * This method refreshes the screen after a dialog operation has been completed.
	 */
	private void dialogOperationRefresh() {
		final String productType = refreshProductTypes();
		final String system = refreshSystems();
		getView().reloadModelGroup(StringUtils.trimToEmpty(productType), StringUtils.trimToEmpty(system));
		getView().reload();
		clearAssginedTableData();
		disableMTCmodelTables();
	}

	/**
	 * This method is used to find inactive Mtc Models.
	 */
	private void inactiveRadioButtonAction(ActionEvent event) {
		getView().getModelGroupTablePane().setData(getModel().findAllByStatusProductTypeAndSystem(MtcToModelGroupPanel.INACTIVE, StringUtils.trimToEmpty(getView().getSelectedProductType()), StringUtils.trimToEmpty(getView().getSelectedSystem())));
		clearAssginedTableData();
		disableMTCmodelTables();
	}
	/**
	 * This method is used to find active Mtc Models.
	 */
	private void activeRadioButtonAction(ActionEvent event) {
		getView().getModelGroupTablePane().setData(getModel().findAllByStatusProductTypeAndSystem(MtcToModelGroupPanel.ACTIVE, StringUtils.trimToEmpty(getView().getSelectedProductType()), StringUtils.trimToEmpty(getView().getSelectedSystem())));
		clearAssginedTableData();
		disableMTCmodelTables();
	}
	/**
	 * This method is used to find all Mtc Models.
	 */
	private void allRadioButtonAction(ActionEvent event) {
		getView().reloadModelGroup(StringUtils.trimToEmpty(getView().getSelectedProductType()), StringUtils.trimToEmpty(getView().getSelectedSystem()));
		clearAssginedTableData();
		disableMTCmodelTables();
	}


	/**
	 * This method is used to assign and deaasign Mtc models.
	 */
	private void saveButtonAction(ActionEvent event) {
		getView().setCursor(Cursor.WAIT);
		getView().getSaveBtn().setDisable(true);
		getView().getBtnRefresh().setDisable(true);
		setDeassignButton();
		setAssignButton();
		final Task<Integer> saveTask = new Task<Integer>() {
			@Override
			public Integer call() {
				try {
					List<String> previousMtcList = getPreviousMtcList(getExistingAvailableMtcModelList());
					List<ModelGrouping> newModelGroupingList = getNewMGroupingOfMtcModels(findExistingAssignedMtcModelList());
					List<ModelGrouping> updatedModelGroupingList = getModel().updateNewMtcListToModelGroup(previousMtcList, newModelGroupingList, getView().getSelectedModelGroup().getModelGroup(), getView().getSelectedSystem());
					for (ModelGrouping modelGroupGrouping : updatedModelGroupingList) {
						AuditLoggerUtil.logAuditInfo(modelGroupGrouping, null, SAVE_REASON_FOR_MTC_TO_MODEL_GROUP_AUDIT, getView().getScreenName(), getModel().getUserId());
					}
					getView().reload();
					updatedModelGroupingList.removeAll(Collections.singleton(null));
					if (updatedModelGroupingList.isEmpty()) {
						return 1; // assigned MTC models
					} else if (newModelGroupingList.isEmpty()) {
						return 2; // deassigned MTC models
					} else {
						return 0; // assigned and deassigned MTC models
					}
				} catch (Exception e) {
					getView().getLogger().error(e, "An error occured in Assign/Deaasign Mtc models ,Failed to update Model Group");
					return -1;
				}
			}
		};
		setOnExit(saveTask, new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent wse) {
				try {
					switch (saveTask.getValue()) {
					case 0:
						EventBusUtil.publish(new StatusMessageEvent("Assigned/Deassigned MTC Model(s) to/from Model Group successfully", StatusMessageEventType.INFO));
						break;
					case 1:
						EventBusUtil.publish(new StatusMessageEvent("Assigned MTC Model(s) to Model Group successfully", StatusMessageEventType.INFO));
						break;
					case 2:
						EventBusUtil.publish(new StatusMessageEvent("Deassigned MTC Model(s) from Model Group successfully", StatusMessageEventType.INFO));
						break;
					default:
						EventBusUtil.publish(new StatusMessageEvent("An error occured in Assign/Deaasign Mtc models ,Failed to update Model Group", StatusMessageEventType.ERROR));
						break;
					}
					getView().getAvailableMtcModelgroupingTable().getSelectionModel().clearSelection();
					getView().getAssignedModelGroupingTable().getSelectionModel().clearSelection();
				} finally {
					getView().getBtnRefresh().setDisable(false);
					getView().setCursor(Cursor.DEFAULT);
				}
			}
		});
		new Thread(saveTask).start();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setOnExit(final Task task, final EventHandler<WorkerStateEvent> eventHandler) {
		task.setOnSucceeded(eventHandler);
		task.setOnFailed(eventHandler);
		task.setOnCancelled(eventHandler);
	}

	/**
	 * This method is used to get new combination Mtc Models.
	 */
	private List<ModelGrouping> getNewMGroupingOfMtcModels(List<ModelGrouping> existingAssignedMtcModelList) {
		List<MtcToModelGroupDto> assignedModelGrouping= new ArrayList<MtcToModelGroupDto>(getView().getAssignedModelGroupingTable().getItems());
		String modelGroup = getView().getSelectedModelGroup().getModelGroup();
		return getModel().getNewMGroupingOfMtcModels(existingAssignedMtcModelList,assignedModelGrouping,modelGroup);
	}

	/**
	 * This method is used to get previous Mtc Models.
	 */
	private List<String> getPreviousMtcList(List<MtcToModelGroupDto> existingAvailableMtcModelList) {
		List<String> previousMtcList=new ArrayList<String>();
		if (existingAvailableMtcModelList != null) {
			for (MtcToModelGroupDto dto : getView().getAvailableMtcModelgroupingTable().getItems()) {
				if (dto != null && !existingAvailableMtcModelList.contains(dto)) {
					previousMtcList.add(StringUtils.trimToEmpty(dto.getMtcModel()));
				}
			}
		}
		else {
			for (MtcToModelGroupDto dto : getView().getAvailableMtcModelgroupingTable().getItems()) {
				if (dto != null) {
					previousMtcList.add(StringUtils.trimToEmpty(dto.getMtcModel()));
				}
			}
		}
		return previousMtcList;
	}
	/**
	 * This method is used to get existing available Mtc Models.
	 */
	private List<MtcToModelGroupDto> getExistingAvailableMtcModelList() {
		List<MtcToModelGroupDto> existingAvailableMtcModelList = getAvailableMtcModelData("", StringUtils.trimToEmpty(getView().getSelectedProductType()));
		if (existingAvailableMtcModelList != null) {
			return existingAvailableMtcModelList;
		}
		return new ArrayList<MtcToModelGroupDto>();
	}
	/**
	 * This method is used to find existing assigned Mtc Models.
	 */
	private List<ModelGrouping> findExistingAssignedMtcModelList() {
		return getModel().findExistingAssignedMtcModelList(StringUtils.trimToEmpty(getView().getSelectedModelGroup().getModelGroup()),
				StringUtils.trimToEmpty(getView().getSelectedSystem()));
	}
	/**
	 * This method is used to populate available Mtc Models.
	 */
	public List<MtcToModelGroupDto> getAvailableMtcModelData(String filter, String productType) {
		List<MtcToModelGroupDto> availableMtcModelList = new ArrayList<MtcToModelGroupDto>();
		String productTypeName = StringUtils.trimToEmpty(getView().getSelectedProductType());
		List<MtcToModelGroupDto> assignedMtcModelList = getView().getAssignedModelGroupingTable().getItems();

		availableMtcModelList = getModel().findAvailableMtcModelData(filter, productType, productTypeName, assignedMtcModelList);
		Collections.sort(availableMtcModelList, new ObjectComparator<MtcToModelGroupDto>("availablelMtcModels"));
		return availableMtcModelList;
	}

	/**
	 * This method is used to assign Mtc Models to Model Group.
	 */
	private void assignButtonAction(ActionEvent event) {
		List<MtcToModelGroupDto> availableMtcMdellist = new ArrayList<MtcToModelGroupDto>();
		List<MtcToModelGroupDto> selectedAvailableMtc = getView().getAvailableMtcModelgroupingTable().getSelectionModel().getSelectedItems();
		for (MtcToModelGroupDto dto : selectedAvailableMtc) {
			if (dto != null) {
				String modelCode = dto.getModelCode();
				String modelYearCode = dto.getModelYearCode();
				dto.setMtcModelForPane(modelYearCode, modelCode);
				availableMtcMdellist.add(dto);
			}
		}
		getView().getAssignedModelGroupingTable().getItems().addAll(availableMtcMdellist);
		availableMtcMdellist = null;
		if (selectedAvailableMtc != null) {
			getView().getAvailableMtcModelgroupingTable().getItems().removeAll(selectedAvailableMtc);
			if(selectedAvailableMtc.size() > 1)
				getView().getAvailableMtcModelgroupingTable().getSelectionModel().clearSelection();
		}
	}
	/**
	 * This method is used to deassign Mtc Models from Model Group.
	 */
	private void deassignButtonAction(ActionEvent event) {
		List<MtcToModelGroupDto> assignedMtclist = new ArrayList<MtcToModelGroupDto>();
		List<MtcToModelGroupDto> selectedAssignedMtc = getView().getAssignedModelGroupingTable().getSelectionModel().getSelectedItems();
		for (MtcToModelGroupDto dto : selectedAssignedMtc) {
			if (dto != null) {
				String mtcModel = StringUtils.trimToEmpty(dto.getMtcModel());
				String modelYearCode, modelCode = null;
				modelYearCode = mtcModel.substring(0, 1);
				if (mtcModel.length() > 1) {
					modelCode = mtcModel.substring(1, mtcModel.length());
				}
				dto.setModelYearCode(modelYearCode);
				dto.setModelCode(modelCode);
				assignedMtclist.add(dto);
			}
		}
		getView().getAvailableMtcModelgroupingTable().getItems().addAll(assignedMtclist);
		assignedMtclist = null;
		if (getView().getAssignedModelGroupingTable().getSelectionModel().getSelectedItems() != null) {
			getView().getAssignedModelGroupingTable().getItems().removeAll(getView().getAssignedModelGroupingTable().getSelectionModel().getSelectedItems());
			if(selectedAssignedMtc.size() > 1)
				getView().getAssignedModelGroupingTable().getSelectionModel().clearSelection();
		}
	}
	/**
	 * This method is called to add context menus to Model Group table.
	 */
	public void addContextMenuItems() {
		ObjectTablePane<ModelGroup> tablePane = getView().getModelGroupTablePane();
		String[] menuItems;
		if (getView().getProductType().getControl().getValue() != null && !StringUtils.EMPTY.equals(getView().getProductType().getControl().getValue())) {
			if (getView().getSelectedModelGroup() != null && !getView().getAllRadioBtn().isSelected()) {

				if (getView().getSelectedModelGroup().isActive()) {

					menuItems = new String[] { MtcToModelGroupPanel.CREATE, MtcToModelGroupPanel.UPDATE, MtcToModelGroupPanel.INACTIVATE,MtcToModelGroupPanel.DELETE };
				} else {
					menuItems = new String[] { MtcToModelGroupPanel.CREATE, MtcToModelGroupPanel.UPDATE, MtcToModelGroupPanel.REACTIVATE,MtcToModelGroupPanel.DELETE };
				}

			} else if (getView().getSelectedModelGroup() != null && getView().getAllRadioBtn().isSelected()) {
				menuItems = new String[] { MtcToModelGroupPanel.CREATE, MtcToModelGroupPanel.UPDATE,MtcToModelGroupPanel.DELETE };
			} else {
				menuItems = new String[] { MtcToModelGroupPanel.CREATE };
			}

			tablePane.createContextMenu(menuItems, this);
		}
	}
	/**
	 * This method is used to set assigned MTC model and description to MtcToModelgroupModelDto.
	 */
	private List<MtcToModelGroupDto> setMtcModelYearDescription(String selectedModelGroup) {
		String selectedProductType = StringUtils.trimToEmpty(getView().getSelectedProductType());
		String selectedSystem = StringUtils.trimToEmpty(getView().getSelectedSystem());
		return getModel().setMtcModelYearDescriptionToDto(selectedModelGroup, selectedProductType, selectedSystem);
	}
	/**
	 * This method is used to disable the content of MTC Model data when no Model Group is selected
	 */
	private void disableMTCmodelTables() {
		getView().getAvailableMtcModelgroupingTablePane().setDisable(true);
		getView().getAssignedModelGroupingTablePane().setDisable(true);
		getView().getMtcModelNameFilterTextField().setDisable(true);
	}

	/**
	 * This method is used to delete Model Group.
	 */
	private void deleteModelGroup(ActionEvent event) {
		clearMessage();
		final ModelGroup selectedModelGroup = getView().getSelectedModelGroup();
		List<ModelGrouping> modelGroupingList = new ArrayList<ModelGrouping>();
		modelGroupingList = getModel().findAllModelGroupingsByModelGroupAndSystem(selectedModelGroup.getModelGroup(), getView().getSelectedSystem());
		if (modelGroupingList.size() > 0) {
			if (!MessageDialog.confirm(null, selectedModelGroup.getModelGroup() + " is associated with " + modelGroupingList.size() + " MTC model(s).  This will deassign its associated MTC model(s) from it.\nAre you sure you want to delete " + selectedModelGroup.getModelGroup() + "?")) {
				return;
			}
		}
		ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
		if (dialog.showReasonForChangeDialog(null)) {
			try {
				getModel().removeModelGroup(getView().getSelectedModelGroup());
				ModelGroup modelGroup = getView().getSelectedModelGroup();
				AuditLoggerUtil.logAuditInfo(modelGroup, null, dialog.getReasonForChangeTextArea().getText(), getView().getScreenName(),getModel().getUserId());
				dialogOperationRefresh();
			}catch (Exception e) {
				displayErrorMessage("An error occured while delete ,Failed to delete Model Group");
			}
		}
	}
}
