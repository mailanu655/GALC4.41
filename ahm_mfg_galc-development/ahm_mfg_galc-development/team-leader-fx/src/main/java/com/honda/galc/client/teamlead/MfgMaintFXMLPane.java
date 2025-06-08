package com.honda.galc.client.teamlead;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.dto.ChangeFormDTO;
import com.honda.galc.client.dto.MCMeasurementDTO;
import com.honda.galc.client.dto.MCOperationMatrixDTO;
import com.honda.galc.client.dto.MCOperationPartRevisionDTO;
import com.honda.galc.client.dto.MCOperationRevisionDTO;
import com.honda.galc.client.dto.MCPddaPlatformDTO;
import com.honda.galc.client.dto.MCRevisionDTO;
import com.honda.galc.client.teamlead.vios.ViosConstants;
import com.honda.galc.client.teamlead.vios.platform.ViosPlatformMaintDialog;
import com.honda.galc.client.teamlead.vios.process.MapProcessDialog;
import com.honda.galc.client.teamlead.vios.process.ViosProcessMaintDialog;
import com.honda.galc.client.ui.ApplicationMainPane;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.RevisionStatus;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.dao.conf.MCPddaPlatformDao;
import com.honda.galc.dao.conf.MCRevisionDao;
import com.honda.galc.dao.conf.MCViosMasterPlatformDao;
import com.honda.galc.dao.conf.PlantDao;
import com.honda.galc.dao.pdda.ChangeFormDao;
import com.honda.galc.dao.pdda.ChangeFormUnitDao;
import com.honda.galc.dao.product.ProductSpecCodeDao;
import com.honda.galc.dto.MCRevisionDto;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.conf.ComponentStatusId;
import com.honda.galc.entity.conf.MCPddaPlatform;
import com.honda.galc.entity.conf.MCRevision;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.pdda.ChangeForm;
import com.honda.galc.entity.product.ProductSpecCode;
import com.honda.galc.property.MfgControlMaintenancePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.task.AsyncTaskExecutorService;
import com.honda.galc.vios.dto.MCViosMasterProcessDto;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/* The main controller class. The fxml
 * file is located in the CLIENT_RESOURCES project with the same
 * name as this class. The primary elements of this screen are javafx tableviews.
 * the BaseFXMLTable class represents a tableview. This base class handle common functions
 * such as setting up cell factories, cell value factories, drag and drop behavior of rows, etc.
 * Most of the behavior of the tableviews are presented through a context menu (by right clicking)
 * For a table, a map of the columns is created. Wherever possible the pdda entities are used for tableviews.
 * Setting the cell factories and cell value factories are done in the  base class using reflection, for 
 * the string typed members of the type T (TableView<T>). Using this approach reduces repetitive code,
 * The non string columns has to be handled separately.
 * 
 * Each tableview supports the concept of child tables.
 * 

 */

public class MfgMaintFXMLPane extends ApplicationMainPane {
	private String userId;

	private String selectedProcessPoint;
	private String siteName;
	private List<String> plantNames;

	private MCRevisionsTable revisionsTable;
	private MCRevisionsTable revisionsTableOneClick;
	private NewChangeFormsTable newchangeFormsTable;
	private NewChangeFormsTable newchangeFormsTableOneClick;
	private PddaPlatformMatrixMaintTable pddaPlatformMatrixMaintTable;

	private MCViosMasterPlatform platform;
	
	Timer timer = null;
	
	TimerTask mainTask = null;
	private boolean hasStarted = false;

	//Platform Filter Comboboxes
	@FXML
	private LoggedComboBox<String> plantCombobox;

	@FXML
	private LoggedComboBox<String> deptCombobox;

	@FXML
	private LoggedComboBox<String> modelYearCombobox;

	@FXML
	private LoggedComboBox<String> prodRateCombobox;

	@FXML
	private LoggedComboBox<String> lineNumberCombobox;

	@FXML
	private LoggedComboBox<String> vmcCombobox;

	@FXML
	private Hyperlink addNewPlatformLink;

	@FXML
	private LoggedLabel jobRunningState;
	
	@FXML
	private ProgressBar progressBar;
	//Till here

	@FXML
	private AnchorPane mfgCtrlMaintPane;

	@FXML
	private ScrollPane revMaintScrollPane;

	@FXML
	private ScrollPane revMaintScrollPaneOneClick;
	
	@FXML
	private ScrollPane operMaintScrollPane;

	@FXML
	private ScrollPane partsMaintScrollPane;

	@FXML
	private TabPane mainTabPane;

	public TabPane getMainTabPane() {
		return mainTabPane;
	}

	public void setMainTabPane(TabPane mainTabPane) {
		this.mainTabPane = mainTabPane;
	}

	// revisions table view
	@FXML
	private TableView<MCRevisionDTO> revisionsTblView;
	
	// revisions table view
	@FXML
	private TableView<MCRevisionDTO> revisionsTblViewOneClick;

	@FXML
	private TableColumn<MCRevisionDTO, String> revAssociateTblCol;

	@FXML
	private TableColumn<MCRevisionDTO, String> revTypeTblCol;

	@FXML
	private TableColumn<MCRevisionDTO, Long> revIdTblCol;

	@FXML
	private TableColumn<MCRevisionDTO, String> revStatusTblCol;

	@FXML
	private TableColumn<MCRevisionDTO, String> revDescriptionTblCol;

	@FXML
	private TableColumn<MCRevisionDTO, String> revAssociateTblColOneClick;

	@FXML
	private TableColumn<MCRevisionDTO, String> revTypeTblColOneClick;

	@FXML
	private TableColumn<MCRevisionDTO, Long> revIdTblColOneClick;

	@FXML
	private TableColumn<MCRevisionDTO, String> revStatusTblColOneClick;
	
	@FXML
	private TableColumn<MCRevisionDTO, String> revDescriptionTblColOneClick;

	@FXML
	private TableColumn<MCRevisionDTO, String> revControlNoColOneClick;
	// Change Forms table view

	@FXML
	private TableView<ChangeFormDTO> chngFormsTblView;

	@FXML
	private TableColumn<ChangeFormDTO, Integer> cFormIdCol;

	@FXML
	private TableColumn<ChangeFormDTO, String> cFormTypeCol;

	@FXML
	private TableColumn<ChangeFormDTO, String> cFormAsmProcNosCol;

	@FXML
	private TableColumn<ChangeFormDTO, String> cFormAsmProcNamesCol;

	// New Change Forms table view

	@FXML
	private TableView<ChangeFormDTO> newchngFormsTblView;

	@FXML
	private TableColumn<ChangeFormDTO, Integer> newcFormIdCol;

	@FXML
	private TableColumn<ChangeFormDTO, String> newcFormTypeCol;

	@FXML
	private TableColumn<ChangeFormDTO, String> newcFormAsmProcNosCol;

	@FXML
	private TableColumn<ChangeFormDTO, String> newcFormAsmProcNamesCol;

	//One Click Approval
	@FXML
	private TableView<ChangeFormDTO> newchngFormsTblViewOneClick;

	@FXML
	private TableColumn<ChangeFormDTO, Integer> newcFormIdColOneClick;

	@FXML
	private TableColumn<ChangeFormDTO, String> newcFormTypeColOneClick;

	@FXML
	private TableColumn<ChangeFormDTO, String> newcFormAsmProcNosColOneClick;
	
	@FXML
	private TableColumn<ChangeFormDTO, Integer> cFormControlNumber;

	@FXML
	private TableColumn<ChangeFormDTO, String> newcFormAsmProcNamesColOneClick;
	
	@FXML
	private TableColumn<ChangeFormDTO, Integer> newcFormControlNoColOneClick;
	
	@FXML
	private TableColumn<ChangeFormDTO, Integer> newcFormControlNoCol;
	// operations tab

	// operations rev table

	@FXML
	private Accordion opPartAccordian;

	public Accordion getOpPartAccordian() {
		return opPartAccordian;
	}

	public void setOpPartAccordian(Accordion opPartAccordian) {
		this.opPartAccordian = opPartAccordian;
	}

	@FXML
	private TableView<MCOperationRevisionDTO> opRevTblView;

	@FXML
	private TableColumn<MCOperationRevisionDTO, String> opRevNameCol;

	@FXML
	private TableColumn<MCOperationRevisionDTO, String> opRevRevCol;

	@FXML
	private TableColumn<MCOperationRevisionDTO, String> opRevDescriptionCol;

	@FXML
	private TableColumn<MCOperationRevisionDTO, String> opRevRevIdCol;

	@FXML
	private TableColumn<MCOperationRevisionDTO, String> opRevViewCol;

	@FXML
	private TableColumn<MCOperationRevisionDTO, String> opRevProcessorCol;

	@FXML
	private TableColumn<MCOperationRevisionDTO, String> opRevStatusCol;

	@FXML
	private ChoiceBox<String> opTab_plants_cb;

	@FXML
	private ChoiceBox<String> opTab_processPoints_cb;

	@FXML
	private ChoiceBox<String> opTab_divisions_cb;

	// operation parts

	@FXML
	private TableView<MCOperationPartRevisionDTO> opPartTblView;

	public TableView<MCOperationPartRevisionDTO> getOpPartTblView() {
		return opPartTblView;
	}

	public void setOpPartTblView(
			TableView<MCOperationPartRevisionDTO> opPartTblView) {
		this.opPartTblView = opPartTblView;
	}

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> opPartOpNameCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> opPartPartRevCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> opPartRevIdCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> opPartViewCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> opPartProcessorCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> opPartPartNoCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> opPartPartItemNoCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> opPartSectionCodeCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> opPartDescrCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> opPartPartMaskCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> opPartPartIdCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> opPartDeviceMsgCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> opPartTypeCol;

	// operations matrix table view

	@FXML
	private TableView<MCOperationMatrixDTO> opMatrixTblView;

	public TableView<MCOperationMatrixDTO> getOpMatrixTblView() {
		return opMatrixTblView;
	}

	public void setOpMatrixTblView(
			TableView<MCOperationMatrixDTO> opMatrixTblView) {
		this.opMatrixTblView = opMatrixTblView;
	}

	@FXML
	private TableColumn<MCOperationMatrixDTO, String> opMatrixNameCol;

	@FXML
	private TableColumn<MCOperationMatrixDTO, String> opMatrixSpecCodeTypeCol;

	@FXML
	private TableColumn<MCOperationMatrixDTO, String> opMatrixSpecCodeMaskCol;

	// Matrix Maintenance

	@FXML
	private TableColumn<MCPddaPlatformDTO, Integer> platformProcSeqNoMMCol;

	@FXML
	private TableView<MCPddaPlatformDTO> platformMMTblView;

	@FXML
	private TableView<SpecCodeMaskUnitsDTO> specCodeUnitsTblView;

	@FXML
	private TableColumn<SpecCodeMaskUnitsDTO, String> specCodeMaskCol;

	@FXML
	private TableColumn<SpecCodeMaskUnitsDTO, String> unitsCol;

	@FXML
	private TableColumn<MCPddaPlatformDTO, Long> platformIdMMCol;

	@FXML
	private TableColumn<MCPddaPlatformDTO, String> platformProcPtIdMMCol;

	@FXML
	private ScrollPane matrixMaintScrollPane;

	@FXML
	private TableColumn<MCPddaPlatformDTO, String> platformProcNoMMCol;

	@FXML
	private Tab matrixMaintTab;

	public List<String> getPlantNames() {
		return plantNames;
	}

	public void setPlantNames(List<String> plantNames) {
		this.plantNames = plantNames;
	}

	// parts maintenance table
	PartsMaintenanceTable partsMaintenanceTable;

	public PartsMaintenanceTable getPartsMaintenanceTable() {
		return partsMaintenanceTable;
	}

	public void setPartsMaintenanceTable(
			PartsMaintenanceTable partaMaintenanceTable) {
		this.partsMaintenanceTable = partaMaintenanceTable;
	}

	@FXML
	private TableView<MCOperationPartRevisionDTO> partMaintTblView;

	public TableView<MCOperationPartRevisionDTO> getPartMaintTblView() {
		return partMaintTblView;
	}

	public void setPartMaintTblView(
			TableView<MCOperationPartRevisionDTO> partMaintTblView) {
		this.partMaintTblView = partMaintTblView;
	}

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> partMaintOpNameCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> partMaintPartIdCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> partMaintPartRevCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> partMaintRevIdCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> partMaintSectionCodeCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> partMaintPartMaskCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> partMaintPartCheckCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> partMaintDescriptionCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> partMaintViewCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> partMaintProcessorCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> partMaintPartNoCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> partMaintDeviceMsgCol;

	@FXML
	private TableColumn<MCOperationPartRevisionDTO, String> partTypeMaintCol;
	// part matrix table

	@FXML
	private TableView<MCOperationMatrixDTO> partMatrixTblView;

	public TableView<MCOperationMatrixDTO> getPartMatrixTblView() {
		return partMatrixTblView;
	}

	public void setPartMatrixTblView(
			TableView<MCOperationMatrixDTO> partMatrixTblView) {
		this.partMatrixTblView = partMatrixTblView;
	}

	@FXML
	private TableColumn<MCOperationMatrixDTO, String> partMatrixPartNameCol;

	@FXML
	private TableColumn<MCOperationMatrixDTO, String> partMatrixSpecCodeMaskCol;

	@FXML
	private TableColumn<MCOperationMatrixDTO, String> partMatrixSpecCodeType;

	@FXML
	private TableColumn<MCOperationMatrixDTO, String> partEffectiveBeginDateCol;

	@FXML
	private TableColumn<MCOperationMatrixDTO, String> partEffectiveEndDateCol;

	private MCOperationPartMatrixTable operationPartMatrixTable;

	public MCOperationPartMatrixTable getOperationPartMatrixTable() {
		return operationPartMatrixTable;
	}

	public void setOperationPartMatrixTable(
			MCOperationPartMatrixTable operationMatrixTable) {
		this.operationPartMatrixTable = operationMatrixTable;
	}

	// measurements table

	private MeasurementsTable measurementsTable;

	public MeasurementsTable getMeasurementsTable() {
		return measurementsTable;
	}

	public void setMeasurementsTable(MeasurementsTable measurementsTable) {
		this.measurementsTable = measurementsTable;
	}

	@FXML
	private TableView<MCMeasurementDTO> measurementsTblView;

	public TableView<MCMeasurementDTO> getMeasurementsTblView() {
		return measurementsTblView;
	}

	public void setMeasurementsTblView(
			TableView<MCMeasurementDTO> measurementsTblView) {
		this.measurementsTblView = measurementsTblView;
	}

	@FXML
	private TableColumn<MCMeasurementDTO, String> measOpNameCol;

	@FXML
	private TableColumn<MCMeasurementDTO, String> measDeviceIdCol;

	@FXML
	private TableColumn<MCMeasurementDTO, String> measPartRevCol;

	@FXML
	private TableColumn<MCMeasurementDTO, String> measMaxLimitCol;

	@FXML
	private TableColumn<MCMeasurementDTO, String> measViewCol;

	@FXML
	private TableColumn<MCMeasurementDTO, String> measSeqNoCol;

	@FXML
	private TableColumn<MCMeasurementDTO, String> measDeviceMsg;

	@FXML
	private TableColumn<MCMeasurementDTO, String> measMaxAttempts;

	@FXML
	private TableColumn<MCMeasurementDTO, String> measPartIdCol;

	@FXML
	private TableColumn<MCMeasurementDTO, String> measTypeCol;

	@FXML
	private TableColumn<MCMeasurementDTO, String> measProcessorCol;

	@FXML
	private TableColumn<MCMeasurementDTO, String> measMinLimitCol;

	@FXML
	private LoggedButton addMeasurementButton;

	@FXML
	private LoggedButton makeActivate;
	
	private MfgControlMaintenancePropertyBean propertyBean;

	public LoggedButton getAddMeasurementButton() {
		return addMeasurementButton;
	}

	public void setAddMeasurementButton(LoggedButton addMeasurementButton) {
		this.addMeasurementButton = addMeasurementButton;
	}
	
	public LoggedButton getMakeActivateButton() {
		return makeActivate;
	}

	public void setMakeActivateButton(LoggedButton makeActivate) {
		this.makeActivate = makeActivate;
	}

	public MfgMaintFXMLPane(com.honda.galc.client.ui.MainWindow window) {
		super(window, true);

	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSelectedProcessPoint() {
		return selectedProcessPoint;
	}

	public void setSelectedProcessPoint(String selectedProcessPoint) {
		this.selectedProcessPoint = selectedProcessPoint;
	}

	@FXML
	void initialize() {

		mainTabPane.setDisable(true);
		propertyBean = PropertyService.getPropertyBean(MfgControlMaintenancePropertyBean.class, this.getProcessPointId());
		
		userId = getMainWindow().getApplicationContext().getUserId();

		siteName = getMainWindow().getApplicationContext().getApplicationPropertyBean().getSiteName();

		Map<String, TableColumn<MCOperationRevisionDTO, String>> mcOpRevColumnmap = new HashMap<String, TableColumn<MCOperationRevisionDTO, String>>();
		mcOpRevColumnmap.put("status", opRevStatusCol);
		mcOpRevColumnmap.put("operationName", opRevNameCol);
		mcOpRevColumnmap.put("processor", opRevProcessorCol);
		mcOpRevColumnmap.put("revId", opRevRevIdCol);
		mcOpRevColumnmap.put("opRevision", opRevRevCol);
		mcOpRevColumnmap.put("view", opRevViewCol);
		mcOpRevColumnmap.put("description", opRevDescriptionCol);

		MCOperationsTable operationsRevTable = new MCOperationsTable(this,
				opRevTblView, mcOpRevColumnmap);

		Map<String, TableColumn<MCOperationPartRevisionDTO, String>> mcOpPartColumnmap = new HashMap<String, TableColumn<MCOperationPartRevisionDTO, String>>();
		mcOpPartColumnmap.put("operationName", opPartOpNameCol);
		mcOpPartColumnmap.put("partRev", opPartPartRevCol);
		mcOpPartColumnmap.put("revId", opPartRevIdCol);
		mcOpPartColumnmap.put("view", opPartViewCol);
		mcOpPartColumnmap.put("processor", opPartProcessorCol);
		mcOpPartColumnmap.put("partNo", opPartPartNoCol);
		mcOpPartColumnmap.put("partItemNo", opPartPartItemNoCol);
		mcOpPartColumnmap.put("sectionCode", opPartSectionCodeCol);
		mcOpPartColumnmap.put("description", opPartDescrCol);
		mcOpPartColumnmap.put("partMask", opPartPartMaskCol);
		mcOpPartColumnmap.put("partId", opPartPartIdCol);
		mcOpPartColumnmap.put("deviceMsg", opPartDeviceMsgCol);
		mcOpPartColumnmap.put("partType", opPartTypeCol);

		//Operation Parts Table
		new MCOperationPartsTable(
				opPartTblView, mcOpPartColumnmap, this);

		//Process point chooser
		new ProcessPointChooser(this,
				opTab_plants_cb, opTab_divisions_cb, opTab_processPoints_cb,
				siteName, opRevTblView, operationsRevTable, opPartTblView,
				opMatrixTblView);

		//setup operations matrix table
		Map<String, TableColumn<MCOperationMatrixDTO, String>> opMatrixColumnMap = new HashMap<String, TableColumn<MCOperationMatrixDTO, String>>();

		opMatrixColumnMap.put("operationName", opMatrixNameCol);
		opMatrixColumnMap.put("specCodeMask", opMatrixSpecCodeMaskCol);
		opMatrixColumnMap.put("specCodeType", opMatrixSpecCodeTypeCol);

		//Operation Matrix Table
		new MCOperationMatrixTable(
				opMatrixTblView, opMatrixColumnMap);

		// setup revisions table
		Map<String, TableColumn<MCRevisionDTO, String>> revisionsColumnMap = new HashMap<String, TableColumn<MCRevisionDTO, String>>();
		revisionsColumnMap.put("associateNo", revAssociateTblCol);
		revisionsColumnMap.put("status", revStatusTblCol);
		revisionsColumnMap.put("type", revTypeTblCol);
		revisionsColumnMap.put("description", revDescriptionTblCol);
		revisionsTable = new MCRevisionsTable(this,platformMMTblView,
				revisionsTblView, revisionsColumnMap, getUserId(), false);


		// setup revisions table
		Map<String, TableColumn<MCRevisionDTO, String>> revisionsColumnMapOneClick = new HashMap<String, TableColumn<MCRevisionDTO, String>>();
		revisionsColumnMapOneClick.put("associateNo", revAssociateTblColOneClick);
		revisionsColumnMapOneClick.put("status", revStatusTblColOneClick);
		revisionsColumnMapOneClick.put("type", revTypeTblColOneClick);
		revisionsColumnMapOneClick.put("description", revDescriptionTblColOneClick);
		revisionsColumnMapOneClick.put("controlNo", revControlNoColOneClick);

		
		revisionsTableOneClick = new MCRevisionsTable(this,platformMMTblView,
				revisionsTblViewOneClick, revisionsColumnMapOneClick, getUserId(), true);
		
		
		// change forms for revisions
		Map<String, TableColumn<ChangeFormDTO, String>> changeFormColumnMap = new HashMap<String, TableColumn<ChangeFormDTO, String>>();

		changeFormColumnMap.put("asmProcNumbers", cFormAsmProcNosCol);
		changeFormColumnMap.put("asmProcNames", cFormAsmProcNamesCol);
		changeFormColumnMap.put("type", cFormTypeCol);

		cFormControlNumber
		.setCellValueFactory(new PropertyValueFactory<ChangeFormDTO, Integer>(
				"controlNo"));
		cFormIdCol
		.setCellValueFactory(new PropertyValueFactory<ChangeFormDTO, Integer>(
				"changeFormId"));

		ChangeFormsTable changeFormsTable = new ChangeFormsTable(
				chngFormsTblView, changeFormColumnMap);
		changeFormsTable.setUserId(userId);

		revIdTblCol
		.setCellValueFactory(new PropertyValueFactory<MCRevisionDTO, Long>(
				"id"));

		// setup new change forms table
		Map<String, TableColumn<ChangeFormDTO, String>> newChangeFormColumnMap = new HashMap<String, TableColumn<ChangeFormDTO, String>>();

		revIdTblCol
		.setCellValueFactory(new PropertyValueFactory<MCRevisionDTO, Long>(
				"id"));

		newChangeFormColumnMap.put("changeFormType", newcFormTypeCol);
		newChangeFormColumnMap.put("asmProcNumbers", newcFormAsmProcNosCol);
		newChangeFormColumnMap.put("asmProcNames", newcFormAsmProcNamesCol);

		newcFormIdCol
		.setCellValueFactory(new PropertyValueFactory<ChangeFormDTO, Integer>(
				"changeFormId"));

		newchangeFormsTable = new NewChangeFormsTable(
				newchngFormsTblView, newChangeFormColumnMap, revisionsTblView, getPropertyBean(), false);
		newchangeFormsTable.setUserId(userId);
		
		
		//One Click Approval

		revIdTblColOneClick
		.setCellValueFactory(new PropertyValueFactory<MCRevisionDTO, Long>(
				"id"));

		// setup new change forms table
		Map<String, TableColumn<ChangeFormDTO, String>> newChangeFormColumnMapOneClick = new HashMap<String, TableColumn<ChangeFormDTO, String>>();

		revIdTblColOneClick
		.setCellValueFactory(new PropertyValueFactory<MCRevisionDTO, Long>(
				"id"));

		newChangeFormColumnMapOneClick.put("changeFormType", newcFormTypeColOneClick);
		newChangeFormColumnMapOneClick.put("asmProcNumbers", newcFormAsmProcNosColOneClick);
		newChangeFormColumnMapOneClick.put("asmProcNames", newcFormAsmProcNamesColOneClick);

		newcFormIdColOneClick
		.setCellValueFactory(new PropertyValueFactory<ChangeFormDTO, Integer>(
				"changeFormId"));
		

		newcFormControlNoColOneClick
		.setCellValueFactory(new PropertyValueFactory<ChangeFormDTO, Integer>(
				"controlNo"));
		
		newcFormControlNoCol
		.setCellValueFactory(new PropertyValueFactory<ChangeFormDTO, Integer>(
				"controlNo"));

		newchangeFormsTableOneClick = new NewChangeFormsTable(
				newchngFormsTblViewOneClick, newChangeFormColumnMapOneClick, null, getPropertyBean(), true);
		newchangeFormsTableOneClick.setUserId(userId);

		// setup structure revisions

		List<ProductSpecCode> prodSpecCodes = ServiceFactory.getDao(
				ProductSpecCodeDao.class).findAll();
		List<Plant> plants = ServiceFactory.getDao(PlantDao.class)
				.findAllBySite(siteName);

		List<String> specCodes = new ArrayList<String>();
		for (ProductSpecCode spec : prodSpecCodes) {
			specCodes.add(spec.getProductSpecCode());
		}

		List<String> plantStr = new ArrayList<String>();
		for (Plant plant : plants) {
			plantStr.add(plant.getPlantName());
		}
		this.setPlantNames(plantStr);

		// setup parts maintenance table
		Map<String, TableColumn<MCOperationPartRevisionDTO, String>> partMaintColumnmap = new HashMap<String, TableColumn<MCOperationPartRevisionDTO, String>>();
		partMaintColumnmap.put("operationName", partMaintOpNameCol);
		partMaintColumnmap.put("partRev", partMaintPartRevCol);
		partMaintColumnmap.put("revId", partMaintRevIdCol);
		partMaintColumnmap.put("view", partMaintViewCol);
		partMaintColumnmap.put("processor", partMaintProcessorCol);
		partMaintColumnmap.put("partNo", partMaintPartNoCol);
		partMaintColumnmap.put("sectionCode", partMaintSectionCodeCol);
		partMaintColumnmap.put("description", partMaintDescriptionCol);
		partMaintColumnmap.put("partMask", partMaintPartMaskCol);
		partMaintColumnmap.put("partCheck", partMaintPartCheckCol);
		partMaintColumnmap.put("partId", partMaintPartIdCol);
		partMaintColumnmap.put("deviceMsg", partMaintDeviceMsgCol);
		partMaintColumnmap.put("partType", partTypeMaintCol);

		partsMaintenanceTable = new PartsMaintenanceTable(partMaintTblView,
				partMaintColumnmap, this);

		// setup parts matrix table
		Map<String, TableColumn<MCOperationMatrixDTO, String>> partMatrixColumnmap = new HashMap<String, TableColumn<MCOperationMatrixDTO, String>>();

		partMatrixColumnmap.put("operationName", partMatrixPartNameCol);
		partMatrixColumnmap.put("specCodeMask", partMatrixSpecCodeMaskCol);
		partMatrixColumnmap.put("specCodeType", partMatrixSpecCodeType);
		partMatrixColumnmap.put("effectiveBeginDate", partEffectiveBeginDateCol);
		partMatrixColumnmap.put("effectiveEndDate", partEffectiveEndDateCol);

		operationPartMatrixTable = new MCOperationPartMatrixTable(
				partMatrixTblView, partMatrixColumnmap);

		this.getAddMeasurementButton().setDisable(true);
		// setup measurements table
		Map<String, TableColumn<MCMeasurementDTO, String>> measurementsColumnmap = new HashMap<String, TableColumn<MCMeasurementDTO, String>>();

		measurementsColumnmap.put("operationName", measOpNameCol);
		measurementsColumnmap.put("deviceId", measDeviceIdCol);
		measurementsColumnmap.put("partRev", measPartRevCol);
		measurementsColumnmap.put("maxLimit", measMaxLimitCol);
		measurementsColumnmap.put("view", measViewCol);
		measurementsColumnmap.put("seqNumber", measSeqNoCol);
		measurementsColumnmap.put("deviceMsg", measDeviceMsg);
		measurementsColumnmap.put("maxAttempts", measMaxAttempts);
		measurementsColumnmap.put("partId", measPartIdCol);
		measurementsColumnmap.put("measurementType", measTypeCol);
		measurementsColumnmap.put("processor", measProcessorCol);
		measurementsColumnmap.put("minLimit", measMinLimitCol);

		measurementsTable = new MeasurementsTable(measurementsTblView,
				measurementsColumnmap, this);

		// setup matrix maintenance Table

		if (!propertyBean.isMatrixMaintenanceTabDisplayed()) {
			mainTabPane.getTabs().remove(matrixMaintTab);
		} else {
			Map<String, TableColumn<MCPddaPlatformDTO, String>> platformMMColumnMap = new HashMap<String, TableColumn<MCPddaPlatformDTO, String>>();
			platformMMColumnMap.put("asmProcessNo", platformProcNoMMCol);
			platformMMColumnMap.put("processPointId", platformProcPtIdMMCol);

			platformIdMMCol
			.setCellValueFactory(new PropertyValueFactory<MCPddaPlatformDTO, Long>(
					"platformId"));
			platformProcSeqNoMMCol
			.setCellValueFactory(new PropertyValueFactory<MCPddaPlatformDTO, Integer>(
					"processSeqNum"));

			Map<String, TableColumn<SpecCodeMaskUnitsDTO, String>> specCodesMaskMMColumnMap = new HashMap<String, TableColumn<SpecCodeMaskUnitsDTO, String>>();
			specCodesMaskMMColumnMap.put("specCodeMask", specCodeMaskCol);
			specCodesMaskMMColumnMap.put("units", unitsCol);

			new SpecCodeMaskUnitsTable(this, specCodeUnitsTblView,
					specCodesMaskMMColumnMap);

			pddaPlatformMatrixMaintTable = new PddaPlatformMatrixMaintTable(
					this, platformMMTblView, platformMMColumnMap);
		}
		opPartAccordian.setTooltip(new Tooltip(
				"Click To Filter Operations By Process Point"));

		// Setting proper screen size with respect to resolution
		Rectangle2D parentBounds = Screen.getPrimary().getVisualBounds();
		setPaneSize(mfgCtrlMaintPane, parentBounds.getWidth(), parentBounds.getHeight());

		double scrollPaneWidth = parentBounds.getWidth() - 5;
		double scrollPaneHeight = parentBounds.getHeight() - 251;
		setPaneSize(revMaintScrollPane, scrollPaneWidth, scrollPaneHeight);
		setPaneSize(revMaintScrollPaneOneClick, scrollPaneWidth, scrollPaneHeight);
		setPaneSize(operMaintScrollPane, scrollPaneWidth, scrollPaneHeight);
		setPaneSize(partsMaintScrollPane, scrollPaneWidth, scrollPaneHeight);
		setPaneSize(matrixMaintScrollPane, scrollPaneWidth, scrollPaneHeight);

	
		//Load all comboboxes
		loadComboBox();
		addNewPlatformLink.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ViosPlatformMaintDialog dialog = new ViosPlatformMaintDialog(getMainWindow().getStage());
				dialog.showDialog();
			}
		});

		progressBar.setVisible(false);
		jobRunningState.setVisible(false);
		
		getMakeActivateButton().setOnAction(
				new EventHandler<ActionEvent>() {
					public void handle(ActionEvent arg0) {
						MCViosMasterPlatform platform = getPlatform();
						Map<String, String> paramMap = new HashMap<String, String>();
						paramMap.put("plantCode", getPlantValue());
						paramMap.put("deptCode", getDepartmentValue());
						paramMap.put("modelYear", getModelYearValue());
						paramMap.put("productionRate", getProductionRateValue());
						paramMap.put("line", getLineNoValue());
						paramMap.put("vmc", getVmcValue());
						paramMap.put("userId", getUserId());
						
						List<MCViosMasterProcessDto> unmappedProcesses = checkForUnMappedProcesses(platform);
						if(unmappedProcesses.size() > 0) {
							// Unmapped processes.
							mapUnmappedProcesses(platform);
						} else {
							runMFGApprovalJob(paramMap);
						}
					  return;
					}
		});
		
	}

	
	private void mapUnmappedProcesses(MCViosMasterPlatform platform) {
			Stage dialog = new Stage();
			GridPane grid = new GridPane();
			dialog.setTitle("Unmapped Processes");
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(10, 10, 10, 10));
			Label userMessage = UiFactory.createLabel("userMessage");
			userMessage.setTextFill(Color.RED);
			Hyperlink hyperLink = new Hyperlink("here");
			hyperLink.setStyle("-fx-font-size: 10pt;-fx-font-weight: bold ;");
			TextFlow linkFlow = new TextFlow(new Text("Please click "), hyperLink , new Text(" to map all unmapped processes"));
			linkFlow.setStyle("-fx-font-size: 10pt; -fx-font-family: arial;");
			linkFlow.setVisible(true);
			hyperLink.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent actionEvent) {
	            	ViosProcessMaintDialog viosProcessMaintDialog = new ViosProcessMaintDialog(dialog, 0, platform);
	            	viosProcessMaintDialog.showDialog();
	            	dialog.close();
	            }
	        });
			grid.add(linkFlow, 0, 4);
			userMessage.setText("There are Unmapped Processes for this Change Form");
			grid.add(userMessage, 0, 3);
			Button cancelButton = UiFactory.createButton("Cancel");
			
			cancelButton.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg0) {dialog.close();}
			});
			
			HBox hButtonBox = new HBox(20);
			hButtonBox.setPadding(new Insets(0, 0, 0, 10));
			hButtonBox.getChildren().addAll(cancelButton);
			
			VBox vbox = new VBox();
			vbox.getChildren().addAll(grid, hButtonBox);
			
			Scene scene = new Scene(vbox);
			dialog.initStyle(StageStyle.DECORATED);
			dialog.initModality(Modality.APPLICATION_MODAL);
			
			dialog.setHeight(200);
			dialog.setWidth(500);
	
			dialog.setScene(scene);
			dialog.centerOnScreen();
	
			dialog.toFront();
			dialog.showAndWait();
	}
	
 	private void runMFGApprovalJob(Map<String, String> paramMap) {
		ServiceFactory.getService(AsyncTaskExecutorService.class).execute("OIF_MFG_CTRL_APPROVAL", paramMap, null, "");
		getMakeActivateButton().setDisable(true);
		try {
			Thread.sleep(500);
		
			mainTask = new TimerTask() {
				@Override
				public void run(){
					hasStarted = true;
					System.out.println("Inside main task");
					refreshRevisionOneClick();
				}
			};
			timer = new Timer(); 
			timer.schedule(mainTask, 0, 5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private MCViosMasterPlatform getPlatform() {
		MCViosMasterPlatform platform = new MCViosMasterPlatform();
		platform.setPlantLocCode(getPlantValue());
		platform.setDeptCode(getDepartmentValue());
		platform.setModelYearDate(new BigDecimal(getModelYearValue()));
		platform.setProdSchQty(new BigDecimal(getProductionRateValue()));
		platform.setProdAsmLineNo(getLineNoValue());
		platform.setVehicleModelCode(getVmcValue());
		return platform;
	}
	
	private List<MCViosMasterProcessDto> checkForUnMappedProcesses(MCViosMasterPlatform platform) {
		return ServiceFactory
				.getDao(ChangeFormUnitDao.class).findAllUnmappedProcessByPlatform(platform);
	}
	
	private void setPaneSize(Region pane, double width, double height) {
		pane.setMinSize(width, height);
		pane.setPrefSize(width, height);
		pane.setMaxSize(width, height);
	}

	public MfgControlMaintenancePropertyBean getPropertyBean() {
		return propertyBean;
	}

	private void loadComboBox() {
		addComboBoxListener();
		loadPlantComboBox();
	}

	private void addComboBoxListener() {
		addPlantComboBoxListener();
		addDepartmentComboBoxListener();
		addModelYearComboBoxListener();
		addProductionRateComboBoxListener();
		addLineNoComboBoxListener();
		addVMCComboBoxListener();
	}

	@SuppressWarnings("unchecked")
	private void loadPlantComboBox() {
		plantCombobox.getItems().clear();
		plantCombobox.setPromptText(ViosConstants.SELECT);
		plantCombobox.getItems().addAll(getMCViosMasterPlatformDao().findAllPlants());
	}

	@SuppressWarnings("unchecked")
	private void addPlantComboBoxListener() {
		plantCombobox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				if(newValue != null) {
					loadDepartmentComboBox(newValue);
				}
			} 
		});
	}

	@SuppressWarnings("unchecked")
	private void loadDepartmentComboBox(String plant) {
		deptCombobox.getItems().clear();
		modelYearCombobox.getItems().clear();
		modelYearCombobox.setValue(null);
		prodRateCombobox.getItems().clear();
		prodRateCombobox.setValue(null);
		lineNumberCombobox.getItems().clear();
		lineNumberCombobox.setValue(null);
		vmcCombobox.getItems().clear();
		vmcCombobox.setValue(null);
		deptCombobox.setPromptText(ViosConstants.SELECT);
		deptCombobox.getItems().addAll(getMCViosMasterPlatformDao().findAllDeptBy(plant));
		if(deptCombobox.getItems().size() == 1) {
			deptCombobox.getSelectionModel().selectFirst();
		}
	}

	@SuppressWarnings("unchecked")
	private void addDepartmentComboBoxListener() {
		deptCombobox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				if(newValue != null) {
					loadModelYearComboBox(newValue);
				}
			} 
		});
	}

	@SuppressWarnings("unchecked")
	private void loadModelYearComboBox(String dept) {
		modelYearCombobox.getItems().clear();
		modelYearCombobox.setValue(null);
		prodRateCombobox.getItems().clear();
		prodRateCombobox.setValue(null);
		lineNumberCombobox.getItems().clear();
		lineNumberCombobox.setValue(null);
		vmcCombobox.getItems().clear();
		vmcCombobox.setValue(null);
		modelYearCombobox.setPromptText(ViosConstants.SELECT);
		String plant = plantCombobox != null ? plantCombobox.getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
		for (BigDecimal modelYear : getMCViosMasterPlatformDao().findAllModelYearBy(plant, dept)) {
			modelYearCombobox.getItems().add(modelYear.toString());
		}
		if(modelYearCombobox.getItems().size() == 1) {
			modelYearCombobox.getSelectionModel().selectFirst();
		}
	}

	@SuppressWarnings("unchecked")
	private void addModelYearComboBoxListener() {
		modelYearCombobox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				if(newValue != null) {
					loadProductionRateComboBox(newValue);
				}
			} 
		});
	}

	@SuppressWarnings("unchecked")
	private void loadProductionRateComboBox(String modelYear) {
		prodRateCombobox.getItems().clear();
		prodRateCombobox.setValue(null);
		lineNumberCombobox.getItems().clear();
		lineNumberCombobox.setValue(null);
		vmcCombobox.getItems().clear();
		vmcCombobox.setValue(null);
		prodRateCombobox.setPromptText(ViosConstants.SELECT);
		String plant = plantCombobox != null ? plantCombobox.getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
		String dept = deptCombobox != null ? deptCombobox.getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
		for (BigDecimal l : getMCViosMasterPlatformDao().findAllProdQtyBy(plant, dept, new BigDecimal(modelYear.replaceAll(",", StringUtils.EMPTY)))) {
			prodRateCombobox.getItems().add(l.toString());
		}
		if(prodRateCombobox.getItems().size() == 1) {
			prodRateCombobox.getSelectionModel().selectFirst();
		}
	}

	@SuppressWarnings("unchecked")
	private void addProductionRateComboBoxListener() {
		prodRateCombobox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				if(newValue != null) {
					loadLineNumberComboBox(newValue);
				}
			} 
		});
	}

	@SuppressWarnings("unchecked")
	private void loadLineNumberComboBox(String productionRate) {
		lineNumberCombobox.getItems().clear();
		vmcCombobox.getItems().clear();
		lineNumberCombobox.setValue(null);
		vmcCombobox.setValue(null);
		lineNumberCombobox.setPromptText(ViosConstants.SELECT);
		vmcCombobox.getItems().clear();
		String plant = plantCombobox != null  && deptCombobox.getSelectionModel().getSelectedItem() != null ? 
				plantCombobox.getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
				String dept = deptCombobox != null && deptCombobox.getSelectionModel().getSelectedItem() != null ? 
						deptCombobox.getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
						String modelYear = modelYearCombobox != null && modelYearCombobox.getSelectionModel().getSelectedItem() != null ? 
								modelYearCombobox.getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
								if(!StringUtils.isBlank(modelYear))
									lineNumberCombobox.getItems().addAll(getMCViosMasterPlatformDao().findAllLineNoBy(plant, dept, 
											new BigDecimal(modelYear.replaceAll(",", StringUtils.EMPTY)), new BigDecimal(productionRate.replaceAll(",", StringUtils.EMPTY))));
								if(lineNumberCombobox.getItems().size() == 1) {
									lineNumberCombobox.getSelectionModel().selectFirst();
								}
	}

	@SuppressWarnings("unchecked")
	private void addLineNoComboBoxListener() {
		lineNumberCombobox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				if(newValue != null) {
					loadVMCComboBox(newValue);
				}
			} 
		});
	}

	@SuppressWarnings("unchecked")
	private void loadVMCComboBox(String lineNo) {
		vmcCombobox.getItems().clear();
		vmcCombobox.setValue(null);
		vmcCombobox.setPromptText(ViosConstants.SELECT);
		String plant = plantCombobox != null  && plantCombobox.getSelectionModel().getSelectedItem() != null ? 
				plantCombobox.getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
				String dept = deptCombobox != null  && deptCombobox.getSelectionModel().getSelectedItem() != null ? 
						deptCombobox.getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
						String modelYear = modelYearCombobox != null  && modelYearCombobox.getSelectionModel().getSelectedItem() != null ? 
								modelYearCombobox.getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
								String productionRate = prodRateCombobox != null  && prodRateCombobox.getSelectionModel().getSelectedItem() != null ? 
										prodRateCombobox.getSelectionModel().getSelectedItem().toString() : StringUtils.EMPTY;
										if(!StringUtils.isBlank(modelYear) && !StringUtils.isBlank(productionRate))
											vmcCombobox.getItems().addAll(getMCViosMasterPlatformDao().findAllVMCBy(plant, dept, 
													new BigDecimal(modelYear.replaceAll(",", StringUtils.EMPTY)), new BigDecimal(productionRate.replaceAll(",", StringUtils.EMPTY)), lineNo));
										if(vmcCombobox.getItems().size() == 1) {
											vmcCombobox.getSelectionModel().selectFirst();
										}
	}

	@SuppressWarnings("unchecked")
	private void addVMCComboBoxListener() {
		vmcCombobox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String oldValue, String newValue) { 
				if(newValue == null) {
					mainTabPane.setDisable(true);
					return;
				}
				platform = new MCViosMasterPlatform(getPlantValue(), getDepartmentValue(), new BigDecimal(getModelYearValue()), 
						new BigDecimal(getProductionRateValue()), getLineNoValue(), getVmcValue());

				
				progressBar.setVisible(true);
				Task<Void> mainTask = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						refreshRevisionMaintTab();
						refreshMatrixMaintTab();
						refreshRevisionOneClick();
						startTimer();
						updateProgress(100, 100);
						return null;
					}
				};

				mainTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					public void handle(WorkerStateEvent t) {
						progressBar.setVisible(false);
						mainTabPane.setDisable(false);
					}
				});
				
				mainTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent event) {
						Logger.getLogger().error(new LogRecord("An exception occured while refreshing tabs"));
						setErrorMessage("Something went wrong while loading data");
						progressBar.setVisible(false);
					}
				});
				progressBar.progressProperty().bind(mainTask.progressProperty());
				new Thread(mainTask).start();
			} 
		});
	}

	private void refreshRevisionMaintTab() {
		//Load Revisions Table
		List<MCRevision> revisionList = ServiceFactory.getDao(
				MCRevisionDao.class).findAllByPddaPlatform(getPlantValue(), getDepartmentValue(), new BigDecimal(getModelYearValue()), 
						new BigDecimal(getProductionRateValue()), getLineNoValue(), getVmcValue());
		revisionsTable.init(platform, chngFormsTblView,
				DTOConverter.convertMCRevision(revisionList));

		//Load New Change Forms Table
		List<ChangeForm> newchangeFormsList = ServiceFactory.getDao(ChangeFormDao.class).findAllByPddaPlatform(getPlantValue(), getDepartmentValue(), new BigDecimal(getModelYearValue()), 
				new BigDecimal(getProductionRateValue()), getLineNoValue(), getVmcValue());
		newchangeFormsTable.init(DTOConverter.convertChangeForm(newchangeFormsList));
		
		makeAllTabsDisableEnable(false);
	}
	
	private void refreshRevisionOneClick() {

		//Load New Change Forms Table
		List<ChangeForm> newchangeFormsList = ServiceFactory.getDao(ChangeFormDao.class).findAllByPddaPlatform(getPlantValue(), getDepartmentValue(), new BigDecimal(getModelYearValue()), 
				new BigDecimal(getProductionRateValue()), getLineNoValue(), getVmcValue());
		newchangeFormsTableOneClick.init(DTOConverter.convertChangeForm(newchangeFormsList));
		
		//Load Revisions Table
		List<MCRevisionDto> revisionList = ServiceFactory.getDao(
				MCRevisionDao.class).findAllByPddaPlatformWithControlNumber(getPlantValue(), getDepartmentValue(), new BigDecimal(getModelYearValue()), 
						new BigDecimal(getProductionRateValue()), getLineNoValue(), getVmcValue());
		revisionsTableOneClick.init(platform, chngFormsTblView,
				DTOConverter.convertMCRevisionDto(revisionList));
		
		makeAllTabsDisableEnable(true);
		
		if(newchngFormsTblViewOneClick.getItems().size() <= 0){
			getMakeActivateButton().setDisable(true);
		}
	}

	private void startTimer(){
		ComponentStatusId cpIdKd = new ComponentStatusId("OIF_MFG_CTRL_APPROVAL", "RUNNING_STATUS{"+platform.getGeneratedId()+"}");
		ComponentStatus cpKdLot = getDao(ComponentStatusDao.class).findByKey(cpIdKd);
		
		if(cpKdLot!=null && cpKdLot.getStatusValue().equalsIgnoreCase("RUNNING")) {
			mainTask = new TimerTask() {
				@Override
				public void run(){
					hasStarted = true;
					refreshRevisionOneClick();
				}
			};
			timer = new Timer(); 
			timer.schedule(mainTask, 0, 5000);
		}
	}
	private void makeAllTabsDisableEnable(boolean isOneClick) {
		
		ComponentStatusId cpIdKd = new ComponentStatusId("OIF_MFG_CTRL_APPROVAL", "RUNNING_STATUS{"+platform.getGeneratedId()+"}");
		ComponentStatus cpKdLot = getDao(ComponentStatusDao.class).findByKey(cpIdKd);
		
		if(cpKdLot!=null && cpKdLot.getStatusValue().equalsIgnoreCase("RUNNING")) {
			
			getMakeActivateButton().setDisable(true);
			matrixMaintScrollPane.setDisable(true);
			revMaintScrollPane.setDisable(true);
			operMaintScrollPane.setDisable(true);
			partsMaintScrollPane.setDisable(true);
			jobRunningState.setVisible(true);
			jobRunningState.setStyle("-fx-text-fill:blue;");
			
		} else {
			
			getMakeActivateButton().setDisable(false);
			matrixMaintScrollPane.setDisable(false);
			revMaintScrollPane.setDisable(false);
			operMaintScrollPane.setDisable(false);
			partsMaintScrollPane.setDisable(false);
			jobRunningState.setVisible(false);
			
			if(hasStarted) {
				if(timer !=null)
					timer.cancel();
				
				if(mainTask != null)
					mainTask.cancel();
				
				hasStarted = false;
				refreshRevisionMaintTab();
				refreshRevisionOneClick();
			}
			
		}
		
	}
	private void refreshMatrixMaintTab() {
		if (propertyBean.isMatrixMaintenanceTabDisplayed()) {
			List<MCPddaPlatform> aprvdPDDAPlatforms = ServiceFactory.getDao(MCPddaPlatformDao.class)
					.findAllByPddaPlatform(getPlantValue(), getDepartmentValue(), new BigDecimal(getModelYearValue()), 
							new BigDecimal(getProductionRateValue()), getLineNoValue(), getVmcValue());
			pddaPlatformMatrixMaintTable.init(specCodeUnitsTblView, DTOConverter.convertPlatformByDept(aprvdPDDAPlatforms));
		}
	}

	private MCViosMasterPlatformDao getMCViosMasterPlatformDao() {
		return ServiceFactory.getDao(MCViosMasterPlatformDao.class);
	}

	public String getPlantValue() {
		return StringUtils.trimToEmpty(plantCombobox.getSelectionModel().getSelectedItem().toString());
	}

	public String getDepartmentValue() {
		return StringUtils.trimToEmpty(deptCombobox.getSelectionModel().getSelectedItem().toString());
	}

	public String getModelYearValue() {
		return StringUtils.trimToEmpty(modelYearCombobox.getSelectionModel().getSelectedItem().toString());
	}

	public String getProductionRateValue() {
		return StringUtils.trimToEmpty(prodRateCombobox.getSelectionModel().getSelectedItem().toString());
	}

	public String getLineNoValue() {
		return StringUtils.trimToEmpty(lineNumberCombobox.getSelectionModel().getSelectedItem().toString());
	}

	public String getVmcValue() {
		return StringUtils.trimToEmpty(vmcCombobox.getSelectionModel().getSelectedItem().toString());
	}
	
	

}
