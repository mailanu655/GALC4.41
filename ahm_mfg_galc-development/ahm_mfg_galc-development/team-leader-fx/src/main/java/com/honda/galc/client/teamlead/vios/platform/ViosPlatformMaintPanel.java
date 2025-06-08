package com.honda.galc.client.teamlead.vios.platform;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamlead.vios.AbstractViosPanel;
import com.honda.galc.client.teamlead.vios.ExcelFileUploadDialog;
import com.honda.galc.client.teamlead.vios.IExcelUploader;
import com.honda.galc.client.teamlead.vios.IViosPanel;
import com.honda.galc.client.teamlead.vios.ViosConstants;
import com.honda.galc.client.teamlead.vios.ViosExcelUploader;
import com.honda.galc.client.teamlead.vios.ViosPlatform;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.ExcelFilesDownloader;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.MCViosMasterPlatformDao;
import com.honda.galc.dao.conf.MCViosMasterProcessDao;
import com.honda.galc.entity.conf.MCViosMasterOperationPart;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.vios.dto.PddaPlatformDto;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * <h3>ViosPlatformMaintPanel Class description</h3>
 * <p>
 * Panel for Vios Platform Maintenance
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
 * @author Hemant Kumar<br>
 *         Aug 28, 2018
 */
public class ViosPlatformMaintPanel extends AbstractViosPanel {

	private ObjectTablePane<MCViosMasterPlatform> platformTablePane;
	private LoggedButton addBtn;
	private LoggedButton deleteBtn;
	private LoggedButton importBtn;
	
	private boolean isSelectAllowed = false;

	public ViosPlatformMaintPanel(IViosPanel iPanel) {
		super(iPanel);
	}

	public ViosPlatformMaintPanel(IViosPanel iPanel, boolean isSelectAllowed) {
		super(iPanel);
		this.isSelectAllowed = isSelectAllowed;
	}

	@Override
	public Node getPanelBody() {
		VBox mainBox = new VBox();
		mainBox.setAlignment(Pos.CENTER);
		mainBox.setPadding(new Insets(10));

		platformTablePane = createPlatformTablePane();
		platformTablePane.getTable().getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<MCViosMasterPlatform>() {
					@Override
					public void changed(ObservableValue<? extends MCViosMasterPlatform> observable,
							MCViosMasterPlatform oldValue, MCViosMasterPlatform newValue) {
						addContextMenuItems(newValue);
					}
				});

		HBox buttonContainer = new HBox();
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(10));
		buttonContainer.setSpacing(10);

		addBtn = createBtn(ViosConstants.ADD);
		addBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addAction();
			}
		});
		deleteBtn = createBtn(ViosConstants.DELETE);
		deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				deleteAction();
			}
		});
		importBtn = createBtn(ViosConstants.IMPORT);
		importBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				importAction();
			}
		});

		buttonContainer.getChildren().addAll(addBtn, deleteBtn, importBtn);

		mainBox.getChildren().addAll(platformTablePane, buttonContainer);

		TitledPane mainPane = createTitiledPane("VIOS Platform Maintenance", mainBox);
		mainPane.setPrefHeight(getScreenHeight() * 0.80);

		platformTablePane.minHeightProperty().bind(mainPane.heightProperty().multiply(0.80));
		platformTablePane.maxHeightProperty().bind(mainPane.heightProperty().multiply(0.80));
		return mainPane;
	}

	@Override
	public void loadData() {
		List<MCViosMasterPlatform> platformList = getDao(MCViosMasterPlatformDao.class).findAllPlatforms();
		platformTablePane.setData(platformList);
	}

	private ObjectTablePane<MCViosMasterPlatform> createPlatformTablePane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with(ViosConstants.PLANT, "plantLocCode")
				.put(ViosConstants.DEPARTMENT, "deptCode").put(ViosConstants.MODEL_YEAR, "modelYearDate")
				.put(ViosConstants.PRODUCTION_RATE, "prodSchQty").put(ViosConstants.LINE_NUMBER, "prodAsmLineNo")
				.put(ViosConstants.VEHICLE_MODEL_CODE, "vehicleModelCode").put("Status", "status").put("Discard Year","DiscardYearS");
		Double[] columnWidth = new Double[] { 0.12, 0.12, 0.12, 0.12, 0.12, 0.12, 0.12, 0.12 };
		ObjectTablePane<MCViosMasterPlatform> panel = new ObjectTablePane<MCViosMasterPlatform>(columnMappingList,
				columnWidth, true);
		panel.setConstrainedResize(false);
		panel.setId("platformTablePane");
		return panel;
	}

	public void addContextMenuItems(MCViosMasterPlatform masterPlat) {
		clearErrorMessage();
		if (masterPlat != null) {
			String[] menuItems;
			
			if (masterPlat.isDiscardYear()) {
				if (masterPlat.isActive()) {
					if (isSelectAllowed) {
						menuItems = new String[] { ViosConstants.UPDATE, ViosConstants.INACTIVATE, ViosConstants.SELECT,
								ViosConstants.DOWNLOAD, ViosConstants.DISABLE_DISCARD_YEAR };
					} else {
						menuItems = new String[] { ViosConstants.UPDATE, ViosConstants.INACTIVATE, ViosConstants.DISABLE_DISCARD_YEAR };
					}
				} else {
					if (isSelectAllowed) {
						menuItems = new String[] { ViosConstants.UPDATE, ViosConstants.ACTIVATE, ViosConstants.SELECT,
								ViosConstants.DOWNLOAD, ViosConstants.DISABLE_DISCARD_YEAR };
					} else {
						menuItems = new String[] { ViosConstants.UPDATE, ViosConstants.ACTIVATE, ViosConstants.DISABLE_DISCARD_YEAR };
					}
				}
			} else {
				if (masterPlat.isActive()) {
					if (isSelectAllowed) {
						menuItems = new String[] { ViosConstants.UPDATE, ViosConstants.INACTIVATE, ViosConstants.SELECT,
								ViosConstants.DOWNLOAD, ViosConstants.ENABLE_DISCARD_YEAR };
					} else {
						menuItems = new String[] { ViosConstants.UPDATE, ViosConstants.INACTIVATE, ViosConstants.ENABLE_DISCARD_YEAR };
					}
				} else {
					if (isSelectAllowed) {
						menuItems = new String[] { ViosConstants.UPDATE, ViosConstants.ACTIVATE, ViosConstants.SELECT,
								ViosConstants.DOWNLOAD, ViosConstants.ENABLE_DISCARD_YEAR };
					} else {
						menuItems = new String[] { ViosConstants.UPDATE, ViosConstants.ACTIVATE , ViosConstants.ENABLE_DISCARD_YEAR};
					}
				}
			}
			
			

			platformTablePane.createContextMenu(menuItems, new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {

					if (event.getSource() instanceof MenuItem) {
						MenuItem menuItem = (MenuItem) event.getSource();
						if (menuItem.getText().equals(ViosConstants.UPDATE)) {
							updateAction();
						} else if (menuItem.getText().equals(ViosConstants.ACTIVATE)) {
							updateStatusAction(true);
						} else if (menuItem.getText().equals(ViosConstants.INACTIVATE)) {
							updateStatusAction(false);
						} else if (menuItem.getText().equals(ViosConstants.SELECT)) {
							selectAction();
						} else if (menuItem.getText().equals(ViosConstants.DOWNLOAD)) {
							downloadAction();
						} else if (menuItem.getText().equals(ViosConstants.ENABLE_DISCARD_YEAR)) {
							updateDiscardYear(true);
						} else if (menuItem.getText().equals(ViosConstants.DISABLE_DISCARD_YEAR)) {
							updateDiscardYear(false);
						}

					}
				}
			});
		} else {
			if (platformTablePane.getTable().getContextMenu() != null) {
				platformTablePane.getTable().getContextMenu().getItems().clear();
			}
		}
	}

	private void selectAction() {
		MCViosMasterPlatform selectedPlatform = platformTablePane.getSelectedItem();
		if (selectedPlatform != null) {
			ViosPlatform.getInstance().setViosPlatform(selectedPlatform.getPlantLocCode(),
					selectedPlatform.getDeptCode(), selectedPlatform.getModelYearDate().floatValue(),
					selectedPlatform.getProdSchQty().floatValue(), selectedPlatform.getProdAsmLineNo(),
					selectedPlatform.getVehicleModelCode());
			setInfoMessage("Selected Platform - " + getPlatformString(selectedPlatform));
		}
	}

	private void downloadAction() {
		final MCViosMasterPlatform selectedPlatform = platformTablePane.getSelectedItem();
		if (selectedPlatform != null) {

			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Zip File", "*.zip");
			fileChooser.getExtensionFilters().add(extFilter);
			fileChooser.setInitialFileName(selectedPlatform.getViosPlatformId());
			final File file = fileChooser.showSaveDialog((Stage) this.getScene().getWindow());
			if (file != null) {
				ExcelFilesDownloader download = new ExcelFilesDownloader(file);
				String statusMessage = download.downloadAllExcelFiles(file, selectedPlatform.getViosPlatformId());
			    if(StringUtils.isEmpty(statusMessage)) {
				  setErrorMessage(statusMessage);
				  return ;
			     }
			}
		}
	}

	private String getPlatformString(MCViosMasterPlatform platform) {
		return " Plant: " + platform.getPlantLocCode() + " Dept: " + platform.getDeptCode() + " Model Year: "
				+ platform.getModelYearDate().toString() + " Qty: " + platform.getProdSchQty().toString() + " Line: "
				+ platform.getProdAsmLineNo() + " VMC: " + platform.getVehicleModelCode();
	}

	private void addAction() {
		ViosPlatformDialog dialog = new ViosPlatformDialog(getStage(), ViosConstants.ADD, null);
		dialog.showDialog();
		loadData();
	}

	private void updateAction() {
		MCViosMasterPlatform selectedPlatform = platformTablePane.getSelectedItem();
		if (ServiceFactory.getDao(MCViosMasterProcessDao.class)
				.getCountByViosPlatformId(selectedPlatform.getGeneratedId()) > 0) {
			setErrorMessage("Selected Platform cannot be updated as it is in use");
			return;
		}
		ViosPlatformDialog dialog = new ViosPlatformDialog(getStage(), ViosConstants.UPDATE, selectedPlatform);
		dialog.showDialog();
		loadData();
	}

	private void updateStatusAction(boolean isActive) {
		MCViosMasterPlatform selectedPlatform = platformTablePane.getSelectedItem();
		selectedPlatform.setActive(isActive);
		getDao(MCViosMasterPlatformDao.class).save(selectedPlatform);
		loadData();
	}

	private void updateDiscardYear(boolean discardYearFlag) {
		MCViosMasterPlatform selectedPlatform = platformTablePane.getSelectedItem();
		selectedPlatform.setDiscardYear(discardYearFlag);
		getDao(MCViosMasterPlatformDao.class).save(selectedPlatform);
		loadData();
	}
	
	private void deleteAction() {
		try {
			MCViosMasterPlatform selectedPlatform = platformTablePane.getSelectedItem();
			if (selectedPlatform == null) {
				setErrorMessage("Please select Platform to Delete");
				return;
			} else if (ServiceFactory.getDao(MCViosMasterProcessDao.class)
					.getCountByViosPlatformId(selectedPlatform.getGeneratedId()) > 0) {
				setErrorMessage("Selected Platform cannot be deleted as it is in use");
				return;
			} else if (MessageDialog.confirm(getStage(), "Are you sure you want to delete the selected platform?")) {
				getDao(MCViosMasterPlatformDao.class).remove(selectedPlatform);
				loadData();
			}
		} catch (Exception e) {
			Logger.getLogger().error(e,
					new LogRecord("An exception occured while deleting MCViosMasterPlatform object"));
			setErrorMessage("Something went wrong while deleting Platform");
		}
	}

	private void importAction() {
		ImportViosPlatformDialog dialog = new ImportViosPlatformDialog(getStage(), this.getUserId());
		dialog.showDialog();
		loadData();
	}



	public double getScreenWidth() {
		return Screen.getPrimary().getVisualBounds().getWidth();
	}

	public double getScreenHeight() {
		return Screen.getPrimary().getVisualBounds().getHeight();
	}

	@Override
	public void onPanelSelected() {
		PddaPlatformDto platformDto = ViosPlatform.getInstance().getPddaPlatformDto();
		if (platformDto != null) {
			for (MCViosMasterPlatform platform : platformTablePane.getTable().getItems()) {
				if (platform.getPlantLocCode().equals(platformDto.getPlantLocCode())
						&& platform.getDeptCode().equals(platformDto.getDeptCode())
						&& platform.getModelYearDate().floatValue() == platformDto.getModelYearDate()
						&& platform.getProdSchQty().floatValue() == platformDto.getProdSchQty()
						&& platform.getProdAsmLineNo().equals(platformDto.getProdAsmLineNo())
						&& platform.getVehicleModelCode().equals(platformDto.getVehicleModelCode())) {
					platformTablePane.getTable().getSelectionModel().select(platform);
				}
			}
		}
	}
}
