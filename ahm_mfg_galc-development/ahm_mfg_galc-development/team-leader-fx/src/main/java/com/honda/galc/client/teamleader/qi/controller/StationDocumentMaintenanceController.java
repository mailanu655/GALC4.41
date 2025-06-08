package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.StationDocumentMaintenanceModel;
import com.honda.galc.client.teamleader.qi.stationconfig.dto.ComboBoxDisplayDto;
import com.honda.galc.client.teamleader.qi.view.StationDocumentMaintenancePanel;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.qi.QiDocument;
import com.honda.galc.entity.qi.QiStationDocument;
import com.honda.galc.entity.qi.QiStationDocumentId;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * 
 * <h3>StationDocumentMaintenanceController Class description</h3>
 * <p>
 * StationDocumentMaintenanceController description
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
 * @author Justin Jiang<br>
 *         February 20, 2020
 *
 */

public class StationDocumentMaintenanceController
		extends AbstractQiController<StationDocumentMaintenanceModel, StationDocumentMaintenancePanel>
		implements EventHandler<ActionEvent> {

	private boolean isRefresh = false;

	public StationDocumentMaintenanceController(StationDocumentMaintenanceModel model,
			StationDocumentMaintenancePanel view) {
		super(model, view);
	}

	ChangeListener<String> plantComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
			if (!isRefresh) {
				Logger.getLogger().check(newValue + " plant code selected");
				clearDisplayMessage();
				getView().clearDivisionComboBox();
				disableAvailableAndAssignedDocumentTablePane();
				loadDivisionComboBox(newValue);
			}
		}
	};

	ChangeListener<ComboBoxDisplayDto> divisionComboBoxChangeListener = new ChangeListener<ComboBoxDisplayDto>() {
		public void changed(ObservableValue<? extends ComboBoxDisplayDto> ov, ComboBoxDisplayDto oldValue,
				ComboBoxDisplayDto newValue) {
			if (!isRefresh) {
				Logger.getLogger().check(newValue + " division selected");
				clearDisplayMessage();
				getView().getDocumentFilterTextField().clear();
				getView().getStationListView().getItems().clear();
				getView().getAvailableDocumentTablePane().getTable().getItems().clear();
				getView().getAssignedDocumentTablePane().getTable().getItems().clear();
				getView().reload();
				getView().getLeftShiftBtn().setDisable(true);
				getView().getRightShiftBtn().setDisable(true);
				getView().getSaveBtn().setDisable(true);
				disableAvailableAndAssignedDocumentTablePane();
				if (newValue != null) {
					loadStationListView(newValue.getId());
				}
			}
		}
	};

	ChangeListener<String> qicsStationListViewChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
			if (!isRefresh) {
				clearDisplayMessage();
				getView().getDocumentFilterTextField().clear();
				enableOrDisableLeftRightShiftAndSaveBtn(true);
				if (null == getView().getStationListView().getSelectionModel().getSelectedItem()) {
					disableAvailableAndAssignedDocumentTablePane();
				}
				if (null != getView().getStationListView().getSelectionModel().getSelectedItem()) {
					getView().getAvailableDocumentTablePane().setDisable(false);
					getView().getAssignedDocumentTablePane().setDisable(false);
					getView().getDocumentFilterTextField().setDisable(false);
					Logger.getLogger()
							.check(getView().getStationListView().getSelectionModel().getSelectedItem().toString()
									+ " station selected");
				}
				getView().getAssignedDocumentTablePane().getTable().getItems().clear();
				getView().getAvailableDocumentTablePane().getTable().getItems().clear();
				setAvailableAndSelectedDocumentTablePaneData();
			}
		}
	};

	/**
	 * This is an implemented method of EventHandler interface. Called whenever an
	 * ActionEvent is triggered. Selecting Left shift,right shift,save Btn is an
	 * ActionEvent. So respective method is called based on which action event is
	 * triggered.
	 */
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			if (actionEvent.getSource().equals(getView().getRightShiftBtn()))
				shiftDocumentsAvailableToAssignedTablePaneBtnAction(actionEvent);
			else if (actionEvent.getSource().equals(getView().getLeftShiftBtn()))
				shiftDocumentsAssignedToAvailableTablePaneBtnAction(actionEvent);
			else if (actionEvent.getSource().equals(getView().getSaveBtn()))
				saveBtnAction(actionEvent);
			else if (actionEvent.getSource().equals(getView().getRefreshBtn())) {
				List<QiDocument> selectedDocumentList = new ArrayList<QiDocument>();
				selectedDocumentList.addAll(
						getView().getAvailableDocumentTablePane().getTable().getSelectionModel().getSelectedItems());
				refreshBtnAction(selectedDocumentList);
				EventBusUtil.publish(new StatusMessageEvent("Refreshed successfully", StatusMessageEventType.INFO));
			}
		} else if (actionEvent.getSource() instanceof LoggedTextField) {
			if (getView().getDocumentFilterTextField().isFocused())
				getView().reload(StringUtils.trim(getView().getDocumentFilterTextField().getText()));
		}

	}

	/**
	 * This method is to refresh the data
	 */
	public void refreshBtnAction(List<QiDocument> selectedDocumentList) {
		isRefresh = true;
		String div = null;
		String filter = getView().getDocumentFilterTextField().getText();
		List<QiDocument> selectedAssignedDocumentList = new ArrayList<QiDocument>();
		String selectedEntryStation = getView().getStationListView().getSelectionModel().getSelectedItem();
		selectedAssignedDocumentList
				.addAll(getView().getAssignedDocumentTablePane().getTable().getSelectionModel().getSelectedItems());

		if (null != getView().getDivisionComboBoxSelectedItem()) {
			div = getView().getDivisionComboBoxSelectedId();
		}
		refreshPlant();
		refreshDivision(div, selectedEntryStation, filter, selectedDocumentList, selectedAssignedDocumentList);
		isRefresh = false;
	}

	@SuppressWarnings("unchecked")
	private void refreshDivision(String div, String selectedEntryStation, String filter,
			List<QiDocument> selectedDocumentList, List<QiDocument> selectedAssignedDocumentList) {
		String plant;
		if (null != div) {
			plant = getView().getPlantComboBox().getSelectionModel().getSelectedItem().toString();
			if (plant != null) {
				getView().clearDivisionComboBox();
				List<Division> divList = getModel().findById(getView().getSiteValueLabel().getText().trim(), plant);
				for (Division divObj : divList) {
					ComboBoxDisplayDto dto = ComboBoxDisplayDto.getInstance(divObj);
					getView().getDivisionComboBox().getItems().add(dto);
					if ((divObj.getDivisionId()).equals(div)) {
						getView().getDivisionComboBox().setValue(dto);
					}
				}
			}
			if (selectedEntryStation == null) {
				getView().reload();
			} else {
				getView().getStationListView().getSelectionModel().select(selectedEntryStation);
				List<QiDocument> allDocumentByStationList = getModel()
						.findAllDocumentByStation(getStationIdFromDescription());
				getView().getDocumentFilterTextField().setText(filter);
				getView().getAssignedDocumentTablePane().getTable().getItems().clear();
				getView().getAssignedDocumentTablePane().getTable().getItems().addAll(allDocumentByStationList);
				getView().reload(filter);
				for (QiDocument selectedAvailableQiDocument : selectedDocumentList)
					getView().getAvailableDocumentTablePane().getTable().getSelectionModel()
							.select(selectedAvailableQiDocument);
				for (QiDocument selectedAssignedQiDocument : selectedAssignedDocumentList)
					getView().getAssignedDocumentTablePane().getTable().getSelectionModel()
							.select(selectedAssignedQiDocument);
			}
		}
	}

	/**
	 * This method is to refresh the plant
	 */
	@SuppressWarnings("unchecked")
	private void refreshPlant() {
		String plant;
		List<Plant> plantList = getModel().findAllBySite(getView().getSiteValueLabel().getText().trim());
		if (null != getView().getPlantComboBox().getSelectionModel().getSelectedItem()) {
			plant = getView().getPlantComboBox().getSelectionModel().getSelectedItem().toString();
			getView().getPlantComboBox().getItems().clear();
			for (Plant plantObj : plantList) {
				getView().getPlantComboBox().getItems().add(plantObj.getPlantName());
				if ((plantObj.getPlantName()).equals(plant))
					getView().getPlantComboBox().setValue(plant);
			}
		} else {
			getView().getPlantComboBox().getItems().clear();
			for (Plant plantObj : plantList) {
				getView().getPlantComboBox().getItems().add(plantObj.getPlantName());
			}
		}
	}

	@Override
	public void addContextMenuItems() {
	}

	/**
	 * This method is used to add listener on main panel table.
	 */
	@Override
	public void initEventHandlers() {
		addPlantComboBoxListener();
		addDivisionComboBoxListener();
		addStationListViewListener();
		addAvailableAndSelectedDocumentTableListener();
		disableAvailableAndAssignedDocumentTablePane();
	}

	/**
	 * This method is used to disable Available and Assigned Document table pane
	 * 
	 * @param
	 */
	private void disableAvailableAndAssignedDocumentTablePane() {
		if (null == getView().getStationListView().getSelectionModel().getSelectedItem()) {
			getView().getAssignedDocumentTablePane().setDisable(true);
			getView().getAvailableDocumentTablePane().setDisable(true);
			getView().getDocumentFilterTextField().setDisable(true);
		}
	}

	/**
	 * This method is used shift Available Document to Assigned Document Table Pane
	 * 
	 * @param event
	 */
	private void shiftDocumentsAvailableToAssignedTablePaneBtnAction(ActionEvent actionEvent) {
		List<QiDocument> availableDocumentlist = getView().getAvailableDocumentTablePane().getSelectedItems();
		if (!availableDocumentlist.isEmpty()) {
			List<QiDocument> addRemoveAvailableDocumentList = new ArrayList<QiDocument>();
			for (QiDocument qiDocument : availableDocumentlist) {
				if (qiDocument != null) {
					addRemoveAvailableDocumentList.add(qiDocument);
				}
			}
			getView().getAssignedDocumentTablePane().getTable().getItems().addAll(addRemoveAvailableDocumentList);
			getView().getAvailableDocumentTablePane().getTable().getItems().removeAll(addRemoveAvailableDocumentList);
			if (availableDocumentlist.size() > 1)
				getView().getAvailableDocumentTablePane().getTable().getSelectionModel().clearSelection();
		}

		getView().getSaveBtn().setDisable(false);
		if (!isFullAccess()) {
			getView().getSaveBtn().setDisable(true);
		}

	}

	/**
	 * This method is used to shift Assigned Document to Available Document Table
	 * Pane
	 * 
	 * @param event
	 */
	private void shiftDocumentsAssignedToAvailableTablePaneBtnAction(ActionEvent actionEvent) {
		List<QiDocument> selectedDocumentList = getView().getAssignedDocumentTablePane().getSelectedItems();
		if (!selectedDocumentList.isEmpty()) {
			List<QiDocument> addRemoveSelectedDocumentList = new ArrayList<QiDocument>();
			for (QiDocument qiDocument : selectedDocumentList) {
				if (qiDocument != null) {
					addRemoveSelectedDocumentList.add(qiDocument);
				}
			}
			getView().getAvailableDocumentTablePane().getTable().getItems().addAll(addRemoveSelectedDocumentList);
			getView().getAssignedDocumentTablePane().getTable().getItems().removeAll(addRemoveSelectedDocumentList);
			if (selectedDocumentList.size() > 1)
				getView().getAssignedDocumentTablePane().getTable().getSelectionModel().clearSelection();
		}

		getView().getSaveBtn().setDisable(false);
		if (!isFullAccess()) {
			getView().getSaveBtn().setDisable(true);
		}
	}

	/**
	 * This method is used to save station document table
	 * 
	 * @param method
	 *            list
	 */
	private void createQiStationDocuments(List<QiDocument> selectedDocumentlist) {
		QiStationDocument qiStationDocument = new QiStationDocument();
		QiStationDocumentId qiStationDocumentId = new QiStationDocumentId();

		if (selectedDocumentlist.isEmpty()) {
			EventBusUtil.publish(new StatusMessageEvent("Deassignment document from QICS station successfully",
					StatusMessageEventType.INFO));
		} else {
			for (QiDocument qiDocument : selectedDocumentlist) {
				qiStationDocumentId.setProcessPointId(getStationIdFromDescription());
				qiStationDocumentId.setDocumentId(qiDocument.getDocumentId());
				qiStationDocument.setId(qiStationDocumentId);
				qiStationDocument.setCreateUser(getModel().getUserId());
				getModel().createQiStationDocument(qiStationDocument);
			}
			EventBusUtil.publish(new StatusMessageEvent("Assigned document to QICS station successfully",
					StatusMessageEventType.INFO));
		}
	}

	/**
	 * This method is used to save document assignment
	 * 
	 * @param event
	 */
	private void saveBtnAction(ActionEvent actionEvent) {
		getView().getSaveBtn().setDisable(true);
		setDeassignButton();
		setAssignButton();
		List<QiDocument> selectedDocumentlist = getView().getAssignedDocumentTablePane().getTable().getItems();
		List<QiDocument> allDocumentByStationList = getModel().findAllDocumentByStation(getStationIdFromDescription());
		if (allDocumentByStationList.isEmpty()) {
			try {
				createQiStationDocuments(selectedDocumentlist);
			} catch (Exception e) {
				handleException("An error occurred while deassignment document from QICS station",
						"Failed to deassign document from QICS station", e);
			}
		} else if (!allDocumentByStationList.isEmpty()) {
			List<QiStationDocument> removedQiStationDocumentList = new ArrayList<QiStationDocument>();
			List<QiDocument> addDocumentStationList = new ArrayList<QiDocument>();
			for (QiDocument qiDocumentObj : allDocumentByStationList) {
				if (!selectedDocumentlist.contains(qiDocumentObj)) {
					QiStationDocument removedQiStationDocumentObj = new QiStationDocument();
					QiStationDocumentId qiStationDocumentId = new QiStationDocumentId();
					qiStationDocumentId.setProcessPointId(getStationIdFromDescription());
					qiStationDocumentId.setDocumentId(qiDocumentObj.getDocumentId());
					removedQiStationDocumentObj.setId(qiStationDocumentId);
					removedQiStationDocumentList.add(removedQiStationDocumentObj);
					// Call to audit log
					AuditLoggerUtil.logAuditInfo(removedQiStationDocumentObj, null,
							QiConstant.SAVE_REASON_FOR_QICS_STATION_DOCUMENT_AUDIT, getView().getScreenName(),
							getUserId());
				}
			}

			try {
				if (removedQiStationDocumentList.isEmpty())
					EventBusUtil.publish(new StatusMessageEvent("Assigned document to QICS station successfully",
							StatusMessageEventType.INFO));
				else {
					getModel().deleteQiStationDocuments(removedQiStationDocumentList);
					EventBusUtil.publish(new StatusMessageEvent("Deassignment document to QICS station successfully",
							StatusMessageEventType.INFO));
				}
			} catch (Exception e) {
				handleException("An error occurred while deassignment document from QICS station",
						"Failed to deassign document from QICS station", e);
			}

			for (QiDocument qiDocumentObj : selectedDocumentlist) {
				if (!allDocumentByStationList.contains(qiDocumentObj)) {
					addDocumentStationList.add(qiDocumentObj);
				}
			}

			if (!addDocumentStationList.isEmpty()) {
				try {
					createQiStationDocuments(addDocumentStationList);
				} catch (Exception e) {
					handleException("An error occurred while assigning document to QICS station",
							"Failed to assign document to QICS station", e);
				}
			}
		}
		getView().getAvailableDocumentTablePane().getTable().getSelectionModel().clearSelection();
		getView().getAssignedDocumentTablePane().getTable().getSelectionModel().clearSelection();

	}

	private void addStationListViewListener() {
		getView().getStationListView().getSelectionModel().selectedItemProperty()
				.addListener(qicsStationListViewChangeListener);

	}

	/**
	 * This method is event listener for Available and Assigned Document table
	 */
	private void addAvailableAndSelectedDocumentTableListener() {
		getView().getAvailableDocumentTablePane().getTable().getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<QiDocument>() {
					public void changed(ObservableValue<? extends QiDocument> observableValue, QiDocument oldValue,
							QiDocument newValue) {
						if (isFullAccess()) {
							if (null != newValue) {
								Logger.getLogger()
										.check(newValue.getDocumentName() + " : Available document is selected");
								clearDisplayMessage();
							}
							setAssignButton();
						}
					}

				});

		getView().getAssignedDocumentTablePane().getTable().getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<QiDocument>() {
					public void changed(ObservableValue<? extends QiDocument> observableValue, QiDocument oldValue,
							QiDocument newValue) {
						if (isFullAccess()) {
							if (null != newValue) {
								Logger.getLogger()
										.check(newValue.getDocumentName() + " : Assigned document is selected");
								clearDisplayMessage();
							}
							setDeassignButton();
						}
					}

				});

	}

	/**
	 * This method is used to enable/disable assign button.
	 */
	private void setAssignButton() {
		if (null != getView().getAvailableDocumentTablePane().getSelectedItem())
			getView().getRightShiftBtn().setDisable(false);
		else
			getView().getRightShiftBtn().setDisable(true);
	}

	/**
	 * This method is used to enable/disable deassign button.
	 */
	private void setDeassignButton() {
		if (null != getView().getAssignedDocumentTablePane().getSelectedItem()) {
			getView().getLeftShiftBtn().setDisable(false);
		} else
			getView().getLeftShiftBtn().setDisable(true);
	}

	/**
	 * This method is used to Enable or Disable Left,Right shift and Save button
	 * base on flag
	 * 
	 * @param flag
	 * @return
	 */
	private void enableOrDisableLeftRightShiftAndSaveBtn(Boolean flag) {
		getView().getLeftShiftBtn().setDisable(flag);
		getView().getRightShiftBtn().setDisable(flag);
		getView().getSaveBtn().setDisable(flag);

	}

	/**
	 * This method is used to set Available and Assigned Document table pane data
	 */
	@SuppressWarnings("unchecked")
	private void setAvailableAndSelectedDocumentTablePaneData() {
		List<QiDocument> allDocumentList = getModel().findAllDocument();
		List<QiDocument> allDocumentByStationList = getModel().findAllDocumentByStation(getStationIdFromDescription());

		if (allDocumentByStationList.isEmpty()) {
			getView().getAvailableDocumentTablePane().getTable().getItems().addAll(allDocumentList);
		} else if (!allDocumentByStationList.isEmpty()) {
			getView().getAssignedDocumentTablePane().getTable().getItems().addAll(allDocumentByStationList);
			List<QiDocument> subtractedAllDocumentList = ListUtils.subtract(allDocumentList, allDocumentByStationList);
			getView().getAvailableDocumentTablePane().getTable().getItems().addAll(subtractedAllDocumentList);
		}

	}

	/**
	 * This method is event listener for plantComboBox
	 */
	@SuppressWarnings("unchecked")
	private void addPlantComboBoxListener() {
		getView().getPlantComboBox().getSelectionModel().selectedItemProperty()
				.addListener(plantComboBoxChangeListener);
	}

	/**
	 * This method is event listener for divisionComboBox
	 */
	@SuppressWarnings("unchecked")
	private void addDivisionComboBoxListener() {
		getView().getDivisionComboBox().getSelectionModel().selectedItemProperty()
				.addListener(divisionComboBoxChangeListener);
	}

	/**
	 * This method is used to load Division ComboBox based on Plant
	 * 
	 * @param text
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void loadDivisionComboBox(String plantName) {
		String siteName = getView().getSiteValueLabel().getText();
		for (Division divisionObj : getModel().findById(siteName, plantName)) {
			ComboBoxDisplayDto dto = ComboBoxDisplayDto.getInstance(divisionObj);
			getView().getDivisionComboBox().getItems().add(dto);
		}
	}

	/**
	 * This method is used to load QICS station list view based on division
	 * 
	 * @param divisionId
	 */
	private void loadStationListView(String divisionId) {
		for (ProcessPoint processPointObj : getModel().findStationByApplicationComponentDivision(divisionId)) {
			String qicsStationDesc = processPointObj.getProcessPointId() + "  ("
					+ processPointObj.getProcessPointDescription() + ")";
			getView().getStationListView().getItems().add(qicsStationDesc);
		}

	}

	/**
	 * This method is used to split Station from string StationDescription return
	 * Station
	 */
	private String getStationIdFromDescription() {
		String selectedStationWithDesc = "";
		if (getView().getStationListView().getSelectionModel().getSelectedItem() != null) {
			selectedStationWithDesc = getView().getStationListView().getSelectionModel().getSelectedItem();
		}
		String[] selectedStation = selectedStationWithDesc.split(" ", 2);
		return selectedStation[0];
	}
}
