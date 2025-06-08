package com.honda.galc.client.teamlead.vios.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamlead.vios.AbstractViosPanel;
import com.honda.galc.client.teamlead.vios.ExcelFileUploadDialog;
import com.honda.galc.client.teamlead.vios.IExcelUploader;
import com.honda.galc.client.teamlead.vios.IViosPanel;
import com.honda.galc.client.teamlead.vios.ViosConstants;
import com.honda.galc.client.teamlead.vios.ViosMasterProcessExcelUploader;
import com.honda.galc.client.teamlead.vios.ViosPlatform;
import com.honda.galc.client.teamlead.vios.ViosPlatformPane;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.AutoCompleteTextField;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledControl;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.MCViosMasterProcessDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.pdda.ChangeFormUnitDao;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.entity.conf.MCViosMasterProcess;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.PddaPlatformUtil;
import com.honda.galc.vios.dto.MCViosMasterProcessDto;
import com.honda.galc.vios.dto.PddaPlatformDto;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * <h3>ViosProcessMaintPanel Class description</h3>
 * <p>
 * Panel for Vios Process Maintenance
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
public class ViosProcessMaintPanel extends AbstractViosPanel {

	private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

	private ViosPlatformPane platformTitledPane;

	private TitledPane processPane;
	private LabeledComboBox<String> processPointCombobox;
	private LabeledControl<AutoCompleteTextField> processNoTextField;
	private LabeledComboBox<String> filterCombobox;
	private LoggedButton searchButton;

	private ObjectTablePane<MCViosMasterProcessDto> processTablePane;

	private LoggedButton addButton;
	private LoggedButton deleteButton;
	private LoggedButton moveButton;
	private LoggedButton uploadButton;

	private MCViosMasterPlatform platform;
	private long revId;

	public ViosProcessMaintPanel(IViosPanel iPanel, long revId, MCViosMasterPlatform selectedPlatform) {
		super(iPanel);
		if (selectedPlatform != null && revId > 0) {
			this.revId = revId;
			this.platform = selectedPlatform;
			loadDialogData(selectedPlatform);
		}
		
		
		if (selectedPlatform != null && revId <= 0) {
			this.platform = selectedPlatform;
			loadDialogData(selectedPlatform);
		}
	}

	@Override
	public Node getPanelBody() {
		VBox mainBox = new VBox();
		mainBox.setAlignment(Pos.CENTER);
		mainBox.setSpacing(10);

		HBox platformContainer = new HBox();
		platformContainer.setAlignment(Pos.CENTER);
		platformContainer.setPadding(new Insets(10));
		platformContainer.setSpacing(20);

		platformTitledPane = new ViosPlatformPane(iPanel) {
			@Override
			public void vmcComboboxListener(String value) {
				addVMCComboBoxListener(value);
			}
		};

		VBox processContainer = new VBox();
		processContainer.setAlignment(Pos.CENTER);
		processContainer.setPadding(new Insets(10));
		processContainer.setSpacing(10);

		HBox filterContainer = new HBox();
		filterContainer.setAlignment(Pos.CENTER);
		filterContainer.setPadding(new Insets(10));
		filterContainer.setSpacing(10);

		processPointCombobox = createLabeledComboBox("processPointCombobox", "Process Point", true, false, false);
		processPointCombobox.getControl().setPromptText("Select");

		processNoTextField = new LabeledControl<AutoCompleteTextField>("Process Number",
				new AutoCompleteTextField("processNoTextField"), true, false, new Insets(0), true, false);
		processNoTextField.setMinWidth(getScreenWidth() * 0.22);
		BorderPane.setMargin(processNoTextField, new Insets(10));

		filterCombobox = createLabeledComboBox("filterCombobox", "Mapped/Unmapped", true, false, false);
		filterCombobox.getControl().setPromptText("Select");
		searchButton = createBtn(ViosConstants.SEARCH);
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				searchAction();
			}
		});

		filterContainer.getChildren().addAll(processPointCombobox, processNoTextField, filterCombobox, searchButton);

		processTablePane = createProcessTablePane();
		processTablePane.getTable().getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<MCViosMasterProcessDto>() {
					@Override
					public void changed(ObservableValue<? extends MCViosMasterProcessDto> observable,
							MCViosMasterProcessDto oldValue, MCViosMasterProcessDto newValue) {
						addContextMenuItems();
					}
				});
		processTablePane.getTable().focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				addContextMenuItems();
			}
		});

		HBox buttonContainer = new HBox();
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(5));
		buttonContainer.setSpacing(10);

		addButton = createBtn(ViosConstants.ADD);
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ViosProcessDialog dialog = new ViosProcessDialog(getStage(), ViosConstants.ADD, platform, null);
				dialog.showDialog();
				reload();
			}
		});

		deleteButton = createBtn(ViosConstants.DELETE);
		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				deleteAction();
			}
		});

		moveButton = createBtn(ViosConstants.MOVE);
		moveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				moveAction();
			}
		});

		uploadButton = createBtn(ViosConstants.UPLOAD);
		uploadButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				uploadExcelAction();
			}
		});

		buttonContainer.getChildren().addAll(addButton, deleteButton, moveButton, uploadButton);

		processContainer.getChildren().addAll(filterContainer, processTablePane, buttonContainer);
		processPane = createTitiledPane("Process Maintenance", processContainer);
		processPane.setDisable(true);

		mainBox.getChildren().addAll(platformTitledPane, processPane);
		return mainBox;
	}

	private ObjectTablePane<MCViosMasterProcessDto> createProcessTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Process Point", "processPointId")
				.put("Process Number", "asmProcNo")
				.put("Process Seq Num", "processSeqNumAsString");

		Double[] columnWidth = new Double[] {0.40,0.40,0.15};
		final ObjectTablePane<MCViosMasterProcessDto> panel = new ObjectTablePane<MCViosMasterProcessDto>(columnMappingList,columnWidth,true);
		panel.getTable().setRowFactory(new Callback<TableView<MCViosMasterProcessDto>, TableRow<MCViosMasterProcessDto>>() {
			@Override
			public TableRow<MCViosMasterProcessDto> call(TableView<MCViosMasterProcessDto> param) {
				final TableRow<MCViosMasterProcessDto> row = new TableRow<MCViosMasterProcessDto>();

				row.setOnDragDetected(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (!row.isEmpty()) {
							Integer index = row.getIndex();
							Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
							db.setDragView(row.snapshot(null, null));
							ClipboardContent cc = new ClipboardContent();
							cc.put(SERIALIZED_MIME_TYPE, index);
							db.setContent(cc);
							event.consume();
						}
					}
				});

				row.setOnDragOver(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent event) {
						Dragboard db = event.getDragboard();
						if (db.hasContent(SERIALIZED_MIME_TYPE)) {
							if (row.getIndex() != ((Integer)db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
								event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
								event.consume();
							}
						}
					}
				});

				row.setOnDragDropped(new EventHandler<DragEvent>() {
					@Override
					public void handle(DragEvent event) {
						Dragboard db = event.getDragboard();
						if (db.hasContent(SERIALIZED_MIME_TYPE)) {
							int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
							MCViosMasterProcessDto draggedItem = panel.getTable().getItems().remove(draggedIndex);

							int dropIndex ; 

							if (row.isEmpty()) {
								dropIndex = panel.getTable().getItems().size() ;
							} else {
								dropIndex = row.getIndex();
							}

							panel.getTable().getItems().add(dropIndex, draggedItem);

							event.setDropCompleted(true);
							panel.getTable().getSelectionModel().select(dropIndex);

							if(filterCombobox != null && filterCombobox.getControl().getSelectionModel().getSelectedItem().equals(ViosConstants.MAPPED)) {
								List<MCViosMasterProcessDto> list = panel.getTable().getItems();
								int i = 0;
								for (MCViosMasterProcessDto dto : list) {
									i++;
									MCViosMasterProcess process = new MCViosMasterProcess(platform.getGeneratedId(), dto.getAsmProcNo(), dto.getProcessPointId(), i);
									process.setUserId(getUserId());
									if(process.getCreateTimestamp() == null) {
										process.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
							    	}
									try {
									ServiceFactory.getDao(MCViosMasterProcessDao.class).save(process);
									}catch (Exception e){e.printStackTrace();}
								}
								reload();
							}
							event.consume();
						}
					}
				});
				return row;
			}
		});
		return panel;
	}
	
	
	@Override
	public void loadData() {
		addFilterComboboxListener();
	}

	private void loadDialogData(MCViosMasterPlatform selectedPlatform) {
		platformTitledPane.selectPlatform(selectedPlatform);
		filterCombobox.getControl().getSelectionModel().select(ViosConstants.UNMAPPED);
		searchAction();
	}

	private void reload() {
		String processPoint = StringUtils
				.trimToEmpty(processPointCombobox.getControl().getSelectionModel().getSelectedItem());
		String processNo = StringUtils.trimToEmpty(processNoTextField.getControl().getText());
		String filter = StringUtils.trimToEmpty(filterCombobox.getControl().getSelectionModel().getSelectedItem());

		if (!(StringUtils.isEmpty(processPoint) && StringUtils.isEmpty(processNo))
				|| filter.equals(ViosConstants.UNMAPPED)) {
			searchAction();
		}
	}

	private void deleteAction() {
		try {
			MCViosMasterProcessDto selectedProcess = processTablePane.getSelectedItem();
			if (selectedProcess == null) {
				setErrorMessage("Please select VIOS Process to delete");
				return;
			} else if (ServiceFactory.getDao(MCViosMasterProcessDao.class).getMappedCountBy(platform.getGeneratedId(),
					selectedProcess.getAsmProcNo()) > 0) {
				setErrorMessage("Selected VIOS Process cannot be deleted as it is in use");
				return;
			} else if (MessageDialog.confirm(getStage(),
					"Are you sure you want to delete the selected VIOS Process?")) {
				MCViosMasterProcess entity = new MCViosMasterProcess(platform.getGeneratedId(),
						selectedProcess.getAsmProcNo(), selectedProcess.getProcessPointId(),
						selectedProcess.getProcessSeqNum());
				ServiceFactory.getDao(MCViosMasterProcessDao.class).resequenceAndDelete(entity);
				reload();
			}
		} catch (Exception e) {
			Logger.getLogger().error(e,
					new LogRecord("An exception occured in delete action while deleting MCViosMasterProcess"));
			setErrorMessage("Something went wrong while deleting VIOS Process");
		}
	}

	private void moveAction() {
		MCViosMasterProcessDto selectedProcess = processTablePane.getSelectedItem();
		if (selectedProcess == null) {
			setErrorMessage("Please select VIOS Process to move");
			return;
		}
		MCViosMasterProcess entity = new MCViosMasterProcess(platform.getGeneratedId(), selectedProcess.getAsmProcNo(),
				selectedProcess.getProcessPointId(), selectedProcess.getProcessSeqNum());
		entity.setUserId(getUserId());
		ViosProcessDialog dialog = new ViosProcessDialog(getStage(), ViosConstants.MOVE, platform, entity);
		dialog.showDialog();
		reload();
	}

	private void uploadExcelAction() {
		IExcelUploader<MCViosMasterProcess> excelUploader = new ViosMasterProcessExcelUploader(this.platform.getViosPlatformId(), this.getUserId());
		ExcelFileUploadDialog dialog = new ExcelFileUploadDialog(getStage(), MCViosMasterProcess.class,excelUploader);
		dialog.showDialog();
		loadData();
	}

	private void searchAction() {
		clearErrorMessage();
		String processPoint = StringUtils
				.trimToEmpty(processPointCombobox.getControl().getSelectionModel().getSelectedItem());
		String processNo = StringUtils.trimToEmpty(processNoTextField.getControl().getText());
		String filter = StringUtils.trimToEmpty(filterCombobox.getControl().getSelectionModel().getSelectedItem());

		List<MCViosMasterProcessDto> processList = new ArrayList<MCViosMasterProcessDto>();
		if (filter.equals(ViosConstants.MAPPED)) {
			if (StringUtils.isEmpty(processNo) && StringUtils.isEmpty(processPoint)) {
				setErrorMessage("Please select atleast one of Process Point or Process Number");
				return;
			}
			if (StringUtils.isEmpty(processNo)) {
				// Get data by process point
				processList = ServiceFactory.getDao(MCViosMasterProcessDao.class)
						.findAllByProcessPoint(platform.getGeneratedId(), processPoint);
			} else {
				if (StringUtils.isEmpty(processPoint)
						|| (StringUtils.isNotEmpty(processPoint) && ServiceFactory.getDao(MCViosMasterProcessDao.class)
								.findBy(platform.getGeneratedId(), processNo, processPoint) != null)) {
					// Get data by process number's process point
					processList = ServiceFactory.getDao(MCViosMasterProcessDao.class)
							.findAllByProcessNo(platform.getGeneratedId(), processNo);
				}
			}
		} else if (filter.equals(ViosConstants.UNMAPPED)) {
			if (revId > 0) {
				processList = ServiceFactory.getDao(ChangeFormUnitDao.class).findAllProcessesByRevId(revId, false);
			} else {
				processList = ServiceFactory.getDao(ChangeFormUnitDao.class).findAllUnmappedProcessByPlatform(platform);
			}
		}
		
		
		processTablePane.getTable().getItems().clear();
		processTablePane.getTable().getItems().addAll(processList);
		if (StringUtils.isNotEmpty(processNo)) {
			for (MCViosMasterProcessDto dto : processTablePane.getTable().getItems()) {
				if (dto.getAsmProcNo().equals(processNo)) {
					processTablePane.getTable().getSelectionModel().select(dto);
				}
			}
		}
	}

	private void addVMCComboBoxListener(String value) {
		clearErrorMessage();
		if (value != null) {
			processPane.setDisable(false);
			platform = new MCViosMasterPlatform(
					platformTitledPane.getPlantCombobox().getSelectionModel().getSelectedItem(),
					platformTitledPane.getDeptCombobox().getSelectionModel().getSelectedItem(),
					new BigDecimal(platformTitledPane.getModelYearCombobox().getSelectionModel().getSelectedItem()),
					new BigDecimal(platformTitledPane.getProdRateCombobox().getSelectionModel().getSelectedItem()),
					platformTitledPane.getLineNoCombobox().getSelectionModel().getSelectedItem(),
					platformTitledPane.getVmcCombobox().getSelectionModel().getSelectedItem());
			loadProcessNoTextField();
			loadProcessPointCombobox();
			loadFilterCombobox();
		} else {
			processPane.setDisable(true);
			processPointCombobox.getControl().getSelectionModel().clearSelection();
			processNoTextField.getControl().clear();
			filterCombobox.getControl().getSelectionModel().clearSelection();
			processTablePane.getTable().getItems().clear();
			platform = null;
		}
	}

	private void loadProcessNoTextField() {
		List<String> processList = ServiceFactory.getDao(ChangeFormUnitDao.class).findAllProcessByPlatform(platform);
		processNoTextField.getControl().getSuggestionList().clear();
		processNoTextField.getControl().getSuggestionList().addAll(processList);
	}

	private void loadProcessPointCombobox() {
		List<String> processPointList = ServiceFactory.getDao(ProcessPointDao.class).findAllProcessPoint();
		processPointCombobox.getControl().getItems().clear();
		processPointCombobox.getControl().getItems().addAll(processPointList);
	}

	private void loadFilterCombobox() {
		filterCombobox.getControl().getItems().clear();
		filterCombobox.getControl().getItems().addAll(ViosConstants.MAPPED, ViosConstants.UNMAPPED);
		filterCombobox.getControl().getSelectionModel().select(ViosConstants.MAPPED);
	}

	private void addFilterComboboxListener() {
		filterCombobox.getControl().getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {
						clearErrorMessage();
						if (newValue != null) {
							processPointCombobox.setDisable(newValue.equals(ViosConstants.UNMAPPED));
							processNoTextField.setDisable(newValue.equals(ViosConstants.UNMAPPED));
							deleteButton.setDisable(newValue.equals(ViosConstants.UNMAPPED));
							moveButton.setDisable(newValue.equals(ViosConstants.UNMAPPED));
							processTablePane.getTable().getItems().clear();
							if (newValue.equals(ViosConstants.UNMAPPED)) {
								processPointCombobox.getControl().getSelectionModel().clearSelection();
								processNoTextField.getControl().clear();
								processTablePane.getTable().getSelectionModel()
										.setSelectionMode(SelectionMode.MULTIPLE);
							} else if (newValue.equals(ViosConstants.MAPPED)) {
								processTablePane.getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
							}
						}
					}
				});
	}

	public void addContextMenuItems() {
		clearErrorMessage();
		if (filterCombobox.getControl().getSelectionModel().getSelectedItem().equals(ViosConstants.UNMAPPED)
				&& processTablePane.getSelectedItems().size() > 0) {
			String[] menuItems;
			menuItems = new String[] { ViosConstants.MAP };
			processTablePane.createContextMenu(menuItems, new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {

					if (event.getSource() instanceof MenuItem) {
						MenuItem menuItem = (MenuItem) event.getSource();
						if (menuItem.getText().equals(ViosConstants.MAP)) {
							mapUnmappedProcesses();
						}
					}
				}
			});
		} else {
			if (processTablePane.getTable().getContextMenu() != null) {
				processTablePane.getTable().getContextMenu().getItems().clear();
			}
		}
	}

	private void mapUnmappedProcesses() {
		MapProcessDialog dialog = new MapProcessDialog(getStage(), platform, processTablePane.getSelectedItems());
		dialog.showDialog();
		reload();
	}

	@Override
	public void onPanelSelected() {
		PddaPlatformDto platformDto = ViosPlatform.getInstance().getPddaPlatformDto();
		if (!PddaPlatformUtil.isBlankPddaPlatform(platformDto)) {
			MCViosMasterPlatform selectedPlatform = new MCViosMasterPlatform(platformDto);
			platformTitledPane.selectPlatform(selectedPlatform);
			platformTitledPane.setDisable(true);
		}
	}
}
