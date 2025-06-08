package com.honda.galc.client.teamlead.vios.process;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamlead.vios.AbstractViosDialog;
import com.honda.galc.client.teamlead.vios.ViosConstants;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledControl;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.MCViosMasterProcessDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.entity.conf.MCViosMasterProcess;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.vios.dto.MCViosMasterProcessDto;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * <h3>MapProcessDialog Class description</h3>
 * <p>
 * Dialog for Vios Process Mapping
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
 *        Aug 28, 2018
 */
public class MapProcessDialog extends AbstractViosDialog {

	private MCViosMasterPlatform platform;
	private LabeledControl<LoggedLabel> plantLabel;
	private LabeledControl<LoggedLabel> deptLabel;
	private LabeledControl<LoggedLabel> modelYearLabel;
	private LabeledControl<LoggedLabel> prodRateLabel;
	private LabeledControl<LoggedLabel> lineNoLabel;
	private LabeledControl<LoggedLabel> vmcLabel;

	private LabeledComboBox<String> processPointCombobox;
	private ObjectTablePane<MCViosMasterProcessDto> processTablePane;

	private LoggedButton mapButton;
	private LoggedButton cancelButton;

	public MapProcessDialog(Stage stage, MCViosMasterPlatform platform, List<MCViosMasterProcessDto> processList) {
		super("Map Unmapped VIOS Process", stage);
		this.platform = platform;
		loadPlatformData();
		processTablePane.getTable().getItems().clear();
		processTablePane.getTable().getItems().addAll(processList);
	}

	@Override
	public Node getMainContainer() {
		VBox mainBox = new VBox();
		mainBox.setAlignment(Pos.CENTER);
		mainBox.setSpacing(10);
		mainBox.setPadding(new Insets(10));

		VBox platformBox = new VBox();
		platformBox.setAlignment(Pos.CENTER);
		platformBox.setSpacing(10);
		platformBox.setPadding(new Insets(10));

		plantLabel = createLabeledControl(ViosConstants.PLANT);
		deptLabel = createLabeledControl(ViosConstants.DEPARTMENT);
		modelYearLabel = createLabeledControl(ViosConstants.MODEL_YEAR);
		prodRateLabel = createLabeledControl(ViosConstants.PRODUCTION_RATE);
		lineNoLabel = createLabeledControl(ViosConstants.LINE_NUMBER);
		vmcLabel = createLabeledControl(ViosConstants.VEHICLE_MODEL_CODE);

		platformBox.getChildren().addAll(plantLabel, deptLabel, modelYearLabel, prodRateLabel, lineNoLabel, vmcLabel);

		TitledPane platformPane = createTitiledPane("Selected Platform", platformBox);

		processPointCombobox = createLabeledComboBox("processPointCombobox", "Process Point", true, true, false);
		processPointCombobox.getControl().setPromptText("Select");
		
		processTablePane = createProcessTablePane();

		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(10);
		buttonBox.setPadding(new Insets(10));

		mapButton = createBtn(ViosConstants.MAP);
		cancelButton = createBtn(ViosConstants.CANCEL);

		buttonBox.getChildren().addAll(mapButton, cancelButton);

		mainBox.getChildren().addAll(platformPane, processPointCombobox, processTablePane, buttonBox);
		return mainBox;
	}

	@Override
	public void initHandler() {
		buttonActionHandler();
	}

	@Override
	public void loadData() {
		loadProcessPointCombobox();
	}

	private void buttonActionHandler() {
		
		mapButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					if(checkMandatoryFields()) {
						return;
					}
					String processPointId = processPointCombobox.getControl().getSelectionModel().getSelectedItem();
					int maxSeqNum = ServiceFactory.getDao(MCViosMasterProcessDao.class).getMaxProcesSeqNumBy(platform.getGeneratedId(), processPointId);
					for(MCViosMasterProcessDto dto : processTablePane.getTable().getItems()) {
						maxSeqNum++;
						MCViosMasterProcess masterProcess = new MCViosMasterProcess(platform.getGeneratedId(), 
								dto.getAsmProcNo(), processPointId, maxSeqNum);
						masterProcess.setUserId(getUserId());
						ServiceFactory.getDao(MCViosMasterProcessDao.class).resequenceAndInsert(masterProcess);
					}
					Stage stage = (Stage) mapButton.getScene().getWindow();
					stage.close();
				} catch (Exception e) {
					Logger.getLogger().error(e, new LogRecord("An exception occured while saving MCViosMasterProcess Object"));
					setErrorMessage("Something went wrong while mapping Vios Process");
				}
			}
		});

		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage stage = (Stage) cancelButton.getScene().getWindow();
				stage.close();
			}
		});
	}
	
	private boolean checkMandatoryFields() {
		if(StringUtils.isEmpty(processPointCombobox.getControl().getSelectionModel().getSelectedItem())) {
			setErrorMessage("Please select Process Point");
			return true;
		}
		return false;
	}

	private void loadProcessPointCombobox() {
		List<String> processPointList = ServiceFactory.getDao(ProcessPointDao.class).findAllProcessPoint();
		processPointCombobox.getControl().getItems().clear();
		processPointCombobox.getControl().getItems().addAll(processPointList);
	}

	private void loadPlatformData() {
		if(platform != null) {
			plantLabel.getControl().setText(platform.getPlantLocCode());
			deptLabel.getControl().setText(platform.getDeptCode());
			modelYearLabel.getControl().setText(platform.getModelYearDate().toString());
			prodRateLabel.getControl().setText(platform.getProdSchQty().toString());
			lineNoLabel.getControl().setText(platform.getProdAsmLineNo());
			vmcLabel.getControl().setText(platform.getVehicleModelCode());
		}
	}

	private LabeledControl<LoggedLabel> createLabeledControl(String labelName) {
		LabeledControl<LoggedLabel> label = new LabeledControl<LoggedLabel>(labelName, new LoggedLabel(labelName), true, false, new Insets(0), true, false);
		return label;
	}
	
	private ObjectTablePane<MCViosMasterProcessDto> createProcessTablePane(){
		ColumnMappingList columnMappingList = ColumnMappingList.with("Process Number", "asmProcNo");

		Double[] columnWidth = new Double[] {0.15};
		final ObjectTablePane<MCViosMasterProcessDto> panel = new ObjectTablePane<MCViosMasterProcessDto>(columnMappingList,columnWidth);
		panel.setMaxHeight(getScreenHeight() * 0.20);
		return panel;
	}
}
