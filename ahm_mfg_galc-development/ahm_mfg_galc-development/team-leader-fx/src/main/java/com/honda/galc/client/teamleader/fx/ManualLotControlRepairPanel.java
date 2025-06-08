package com.honda.galc.client.teamleader.fx;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamlead.ltCtrRepair.ManualLotControlRepairBlockController;
import com.honda.galc.client.teamlead.ltCtrRepair.ManualLotControlRepairConrodController;
import com.honda.galc.client.teamlead.ltCtrRepair.ManualLotControlRepairController;
import com.honda.galc.client.teamlead.ltCtrRepair.ManualLotControlRepairCrankshaftController;
import com.honda.galc.client.teamlead.ltCtrRepair.ManualLotControlRepairEngineController;
import com.honda.galc.client.teamlead.ltCtrRepair.ManualLotControlRepairHeadController;
import com.honda.galc.client.teamlead.ltCtrRepair.ManualLotCtrRepairUtil;
import com.honda.galc.client.teamleader.model.PartResult;
import com.honda.galc.client.teamleader.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.constant.OperationType;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.property.PropertyService;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public class ManualLotControlRepairPanel extends TabbedPanel {

	private static final long serialVersionUID = 1L;

	private ManualLotControlRepairController controller;
	private ManualLotControlRepairPropertyBean property;
	private TableView<PartResult> partStatusTableView;
	private TabbedMainWindow mainWin;
	private TextField productIdTextField;
	private Button exportToExcelBtn;
	private TextField productTypeTextField;
	private ProductTypeData productTypeData;
	private ProductType currentProductType;
	private Button addResult;
	private Button removeResult;
	private Button removeAllButton;
	private Button refresh;
	private Button doneNextProduct;
	private Button showHistory;
	private CheckBox stopShipCheckBox;
	private CheckBox nonGALCPartsCheckBox;
	private CheckBox requiredPartsCheckBox;
	private ComboBox<Division> divisionComboBox;
	private LoggedComboBox<String> productTypeCombobox;
	private TextField partMask;
	private ComboBox<String> partStatus;
	private ComboBox<String> specalityScreen;
	private ComboBox<String> toolType;
	private TextField psetValue;
	private TextField unitNumbOrDesc;
	private TextField serialNumber;
	private ListView<String> processPointComboBox;
	private ListView<String> operationTypeComboBox;
	private ComboBox<String> operationStatusComboBox;
	private ManualLotControlResultsDialog resultsDialog;
	private Button findButton;
	private TitledPane filterPane;
	private Button productIdBtn;
	private Button insertResultBtn;
	private Map<String, ProcessPoint> processPointMap;
	private Button processPointButton;
	private Button opTypeButton;
	
	private BaseProduct product;
	private Boolean invalidStopShipPart = false;
	//style
	final private String font_style="-fx-font: 12 arial;";
	private boolean isDoneBtnClicked = false;
	private Button  clearFilters;
	
	public ManualLotControlRepairPanel(TabbedMainWindow mainWin) {
		super("ManualLotControlRepair", KeyEvent.VK_E, mainWin);
		this.mainWin = mainWin;
		init();
	}

	private void init() {
		initialize();
	}

	private void initialize() {

		try {

			property = PropertyService.getPropertyBean(ManualLotControlRepairPropertyBean.class);

			controller = createController();

			currentProductType = this.mainWin.getProductType();
			onTabSelected();

		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception to start ManualLotControlRepairView");
		}
	}

	private ManualLotControlRepairController createController() {
		controller = new ManualLotControlRepairController(this.mainWin, this);
		return controller;
	}

	public void initComponents() {
		BorderPane pane = new BorderPane();
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10, 20, 10, 20));
		vbox.setSpacing(10);

		vbox.getChildren().add(createProductPanel());
		vbox.getChildren().add(createFilterProductPanel() );
		vbox.getChildren().add(createButtonsPanel());

		pane.setCenter(vbox);
		setCenter(pane);
		mapActions();

		
	}
	
	private ScrollPane createFilterProductPanel(){
		ScrollPane scrollPane = new ScrollPane();
		VBox vBox = new VBox();
		scrollPane.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight() * 0.66);
		scrollPane.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight() * 0.66);
		vBox.getChildren().addAll(createFilterPanel(), createPartStatusTable());
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setContent(vBox);
		return scrollPane;
	}
	
	@SuppressWarnings({"unchecked" })
	private HBox createProductPanel() {
		HBox hbox = new HBox();
	
		//Product/VIN Text field
		productIdTextField = UiFactory.createTextField("productIdTextField", 17,TextFieldState.EDIT);
		productIdTextField.setMaxWidth(200);
		productIdTextField.setFocusTraversable(true);
		productIdTextField.requestFocus();
		productIdTextField.setStyle(font_style);
		productIdTextField.setPrefSize(200, 30);
		HBox row = new HBox();
		row.setPadding(new Insets(5, 10, 5, 10));
		row.setAlignment(Pos.BASELINE_LEFT);
		row.setSpacing(10);
		row.getChildren().add(UiFactory.createLabel("Product Type", "Product Type ", font_style,100));
		
		productTypeCombobox = new LoggedComboBox<String>();
		addProductTypeComboBoxListener();
		List<String> productList=new ArrayList<String>();
		for(ProductTypeData type : window.getApplicationContext().getProductTypeDataList()){
			productList.add(type.getProductTypeName());
		}
		productTypeCombobox.getItems().addAll(FXCollections.observableArrayList(productList));	
		productTypeCombobox.setValue(window.getApplicationContext().getApplicationPropertyBean().getProductType());
		row.getChildren().add(productTypeCombobox);
		
		row.getChildren().add(UiFactory.createLabel("", "", font_style, 50));
		
		productIdBtn = UiFactory.createButton(getProductTypeData().getProductIdLabel(),font_style,true );
		productIdBtn.setPrefSize(100, 30);
		row.getChildren().add(productIdBtn);
		row.getChildren().add(productIdTextField);
			
		productTypeTextField = UiFactory.createTextField("productTypeTextField", 17,font_style,TextFieldState.READ_ONLY);
		productTypeTextField.setMaxWidth(200);
		productTypeTextField.setPrefSize(200,30);
		productTypeTextField.setFocusTraversable(false);
		
		row.getChildren().add(UiFactory.createLabel("", "", font_style, 40));
		row.getChildren().add(UiFactory.createLabel("productTypeLabel", "Spec Code ", font_style,80));
		row.getChildren().add(productTypeTextField);
		
		VBox vbox= new VBox();
		vbox.getChildren().add(row);
		hbox.getChildren().add(vbox);

		return hbox;
	}
	
	@SuppressWarnings("static-access")
	private HBox createFilterPanel() {
		
		VBox mainbox = new VBox();
		HBox filterBox = new HBox();
		filterPane = new TitledPane();
		filterPane.setText("Filter Criteria ");
		filterPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
		filterPane.setExpanded(false);
		filterPane.setFocusTraversable(true);
		filterBox.getChildren().add(filterPane);
		
		HBox innerBox = new HBox();
		VBox column1 = new VBox();
		VBox column2 = new VBox();
		VBox column3 = new VBox();
		VBox column4 = new VBox();
		VBox column5 = new VBox();
		VBox column6 = new VBox();
		
		innerBox.getChildren().addAll(column1,column2,column3,column4,column5,column6);
		mainbox.getChildren().addAll(innerBox);
		filterPane.setContent(mainbox);

		
		//Divison combo box
		divisionComboBox = new ComboBox<Division>();
		divisionComboBox.setId("division");
		divisionComboBox.getItems().addAll(FXCollections.observableArrayList(controller.getDivisionList()));
		divisionComboBox.setConverter(ManualLotCtrRepairUtil.divisionStringConverter());
		divisionComboBox.setDisable(true);
		divisionComboBox.setStyle(font_style);
		divisionComboBox.setPrefSize(200, 30);
		divisionComboBox.setFocusTraversable(true);
		VBox vbox1 = new VBox();
		vbox1.setSpacing(10);
		vbox1.setPadding(new Insets(10, 10, 10, 10));
		vbox1.getChildren()
						.add(UiFactory.createLabel("Division", "Division", font_style,150));
		vbox1.getChildren().add(divisionComboBox);
		column1.getChildren().add(vbox1);
		
		//serial number
		serialNumber = UiFactory.createTextField("Serial Number");
		serialNumber.setPrefSize(200,30);
		serialNumber.setMaxWidth(200);
		serialNumber.setDisable(true);
		serialNumber.setStyle(font_style);
		vbox1 = new VBox();
		vbox1.setSpacing(10);
		vbox1.setPadding(new Insets(10, 10, 10, 10));
		vbox1.getChildren()
				.add(UiFactory.createLabel("Serial Number", "Serial Number",font_style,150));
		vbox1.getChildren().add(serialNumber);
		column1.getChildren().add(vbox1);
		
		//stop ship checkbox
		stopShipCheckBox = new CheckBox(" Stop Ship Only ");
		stopShipCheckBox.setStyle(font_style);
		stopShipCheckBox.setPrefSize(200,30);
		stopShipCheckBox.setDisable(true);
		controller.addStopShipListener();
		vbox1 = new VBox();
		vbox1.getChildren().add(stopShipCheckBox);
		vbox1.setPadding(new Insets(10, 10, 10, 10));
		column1.getChildren().add(vbox1);
		
		//Column2
		//Process point combo box
		processPointComboBox = new ListView<String>();
		processPointComboBox.setId("processpoint");
		processPointComboBox.setPrefSize(250,110);
		processPointComboBox.setStyle(font_style);
		List<String> processPointList = new ArrayList<String>();
		processPointMap = new HashMap<String, ProcessPoint>();
		for (ProcessPoint processpoint : controller.getProcessPointList()) {
			processPointMap.put(processpoint.getProcessPointId()+"-"+processpoint.getProcessPointName(), processpoint);
			processPointList.add(processpoint.getProcessPointId()+"-"+processpoint.getProcessPointName());
		}

		
		processPointComboBox.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		processPointComboBox.getItems().addAll(processPointList);
		processPointComboBox.setDisable(true);
		VBox vbox2 = new VBox();
		HBox hbox = new HBox();
		vbox2.setSpacing(10);
		vbox2.setPadding(new Insets(10, 10, 10, 10));
		hbox.getChildren().add(UiFactory.createLabel("Process Point", "Process Point	",font_style,150));
		
		processPointButton = UiFactory.createButton("Selected PP",true);
		processPointButton.setPrefSize(90, 15);
		processPointButton.setStyle("-fx-font: 10 arial;-fx-font-weight:bold;");
		processPointButton.setDisable(true);
		hbox.getChildren().add(processPointButton);
		vbox2.getChildren().addAll(hbox, processPointComboBox);
		column2.getChildren().add(vbox2);
		
		//non-GALC parts or Lot control rules
		nonGALCPartsCheckBox = new CheckBox(" Non-GALC Parts Only ");
		nonGALCPartsCheckBox.setStyle(font_style);
		nonGALCPartsCheckBox.setDisable(true);
		nonGALCPartsCheckBox.setPrefSize(200, 30);
		controller.addNonGalcPartChechBoxListener();
		vbox2= new VBox();
		vbox2.getChildren().add(nonGALCPartsCheckBox);
		column2.getChildren().add(vbox2);
		
		//column 3
		//Operation Type combo box
		operationTypeComboBox = new ListView<String>();
		operationTypeComboBox.setId("operationType");
		operationTypeComboBox.setPrefSize(200,110);
		operationTypeComboBox.setItems(FXCollections.observableArrayList(controller.getOperationTypeList()));
		operationTypeComboBox.setDisable(true);
		operationTypeComboBox.setStyle(font_style);
		operationTypeComboBox.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		VBox vbox3 = new VBox();
		HBox hboxOptype = new HBox();
		hboxOptype.setPadding(new Insets(10, 10, 10, 10));
		hboxOptype.getChildren().add(UiFactory.createLabel("Operation Type", "Operation Type", font_style,120));
		
		opTypeButton = UiFactory.createButton("Selected OpType",true);
		opTypeButton.setPrefSize(110, 15);
		opTypeButton.setStyle("-fx-font: 10 arial;-fx-font-weight:bold;");
		opTypeButton.setDisable(true);
		hboxOptype.getChildren().add(opTypeButton);
		
		
		vbox3.getChildren().addAll(hboxOptype,operationTypeComboBox);
		column3.getChildren().add(vbox3);
		


		//required parts check box
		requiredPartsCheckBox = new CheckBox(" Required Parts Only ");
		requiredPartsCheckBox.setStyle(font_style);
		requiredPartsCheckBox.setDisable(true);
		requiredPartsCheckBox.setPrefSize(200, 30);
		controller.addRequiredPartChechBoxListener();
		vbox3 = new VBox();
		vbox3.getChildren().add(requiredPartsCheckBox);
		vbox3.setPadding(new Insets(10, 10, 10, 10));
		column3.getChildren().add(vbox3);
		
		
		//Column4
		//operation status
		operationStatusComboBox = new ComboBox<String>();
		operationStatusComboBox.setId("operationStatus");
		operationStatusComboBox.setPrefSize(230,30);
		operationStatusComboBox.setItems(FXCollections.observableArrayList(controller.getOperationStatusList()));
		operationStatusComboBox.setDisable(true);
		operationStatusComboBox.setStyle(font_style);
		VBox vbox4= new VBox();
		vbox4.setSpacing(10);
		vbox4.setPadding(new Insets(10, 10, 10, 10));
		vbox4.getChildren().add(UiFactory.createLabel("Operation Status", "Operation Status",font_style,150));
		vbox4.getChildren().add(operationStatusComboBox);
		column4.getChildren().add(vbox4);
		
		
		//Pset value
		psetValue = UiFactory.createTextField("Pset Value");
		psetValue.setPrefSize(200,30);
		psetValue.setDisable(true);
		psetValue.setStyle(font_style);
		vbox4 = new VBox();
		vbox4.setSpacing(10);
		vbox4.setPadding(new Insets(10, 10, 10, 10));
		vbox4.getChildren().add(UiFactory.createLabel("Pset Value", "Pset Value",font_style,150));
		vbox4.getChildren().add(psetValue);
		column4.getChildren().add(vbox4);
		
		//Find Button
		findButton = UiFactory.createButton("Find",false);
		findButton.setPrefSize(100, 30);
		findButton.setStyle("-fx-font: 15 arial;");
		findButton.setDisable(true);
		HBox hboxbutton = new HBox();
		hboxbutton.setSpacing(10);
		hboxbutton.setPadding(new Insets(10, 10, 10, 10));
		//Clear Filter Button
		clearFilters = UiFactory.createButton("Clear Filters",false);
		clearFilters.setPrefSize(120, 30);
		clearFilters.setStyle("-fx-font: 15 arial;");
		clearFilters.setDisable(true);
		
		hboxbutton.getChildren().addAll(findButton, clearFilters);
		column4.getChildren().addAll(hboxbutton);

		
		//Column 5
		
		//Part mask
		partMask = UiFactory.createTextField("Part Mask");
		partMask.setMaxWidth(200);
		partMask.setPrefSize(200,30);
		partMask.setDisable(true);
		partMask.setStyle(font_style);
		VBox vbox5= new VBox();
		vbox5.setSpacing(10);
		vbox5.setPadding(new Insets(10, 10, 10, 10));
		vbox5.getChildren()
						.add(UiFactory.createLabel("Part Mask", "Part Mask",font_style,150));
		vbox5.getChildren().add(partMask);
		column5.getChildren().add(vbox5);
		
		//Unit number / Description
		unitNumbOrDesc = UiFactory.createTextField("Unit Number/Desc");
		unitNumbOrDesc.setMaxWidth(200);
		unitNumbOrDesc.setPrefSize(200,30);
		unitNumbOrDesc.setDisable(true);
		unitNumbOrDesc.setStyle(font_style);
		vbox5= new VBox();
		vbox5.setSpacing(10);
		vbox5.setPadding(new Insets(10, 10, 10, 10));
		vbox5.getChildren()
						.add(UiFactory.createLabel("Unit Number/Desc", "Unit Number/Desc",font_style,150));
		vbox5.getChildren().add(unitNumbOrDesc);
		column5.getChildren().add(vbox5);
				

		
		//Column6
		//Specality Screens
		specalityScreen = new ComboBox<String>();
		specalityScreen.setId("specalityScreen");
		specalityScreen.setPrefSize(200,30);
		specalityScreen.setItems(FXCollections.observableArrayList(controller.getSpecialityScreenList()));
		specalityScreen.setDisable(true);
		specalityScreen.setStyle(font_style);
		VBox hbox6= new VBox();
		hbox6.setSpacing(10);
		hbox6.setPadding(new Insets(10, 10, 10, 10));
		hbox6.getChildren()
							.add(UiFactory.createLabel("Specality Screen", "Specality Screen",font_style,150));
		hbox6.getChildren().add(specalityScreen);
		column6.getChildren().add(hbox6);
		
		//Tool Type
		toolType = new ComboBox<String>();
		toolType.setId("toolType");
		toolType.setPrefSize(200,30);
		toolType.setItems(FXCollections.observableArrayList(controller.getToolTypeList()));
		toolType.setDisable(true);
		toolType.setStyle(font_style);
		hbox6 = new VBox();
		hbox6.setSpacing(10);
		hbox6.setPadding(new Insets(10, 10, 10, 10));
		hbox6.getChildren()
						.add(UiFactory.createLabel("Tool Type", "Tool Type",font_style,150));
		hbox6.getChildren().add(toolType);
		column6.getChildren().add(hbox6);
		
		return filterBox;
	}

	private HBox createButtonsPanel() {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(5, 5, 5, 5));
		hbox.setSpacing(30);

		addResult = UiFactory.createButton(" Edit Result ",font_style, false);
		addResult.setPrefSize(150, 30);
		removeResult = UiFactory.createButton(" Remove Result ",font_style, false);
		removeResult.setPrefSize(150, 30);
		removeAllButton = UiFactory.createButton(" Remove All ",font_style, false);
		removeAllButton.setPrefSize(150, 30);
		refresh = UiFactory.createButton(" Refresh ",font_style, false);
		refresh.setPrefSize(100, 30);
		showHistory = UiFactory.createButton(" Show History ",font_style, false);
		showHistory.setPrefSize(150, 30);
		doneNextProduct = UiFactory.createButton(" Done ",font_style, false);
		doneNextProduct.setPrefSize(100, 30);
		exportToExcelBtn = UiFactory.createButton(" Export To Excel ",font_style, false);
		exportToExcelBtn.setPrefSize(150, 30);
		insertResultBtn = UiFactory.createButton(" Insert Result ",font_style, true);
		insertResultBtn.setPrefSize(150, 30);
		insertResultBtn.setDisable(true);
		
		hbox.getChildren().add(addResult);
		hbox.getChildren().add(removeResult);
		boolean isRemoveAllButtonVisible = PropertyService.getPropertyBean(ManualLotControlRepairPropertyBean.class, getProcessPointId()).isRemoveAllButtonVisible();
		if(isRemoveAllButtonVisible) {
			hbox.getChildren().add(removeAllButton);
		}
		hbox.getChildren().add(refresh);
		hbox.getChildren().add(showHistory);
		hbox.getChildren().add(doneNextProduct);
		hbox.getChildren().add(exportToExcelBtn);
		hbox.getChildren().add(insertResultBtn);
		
		return hbox;
	}

	@SuppressWarnings("unchecked")
	private HBox createPartStatusTable() {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(5, 10, 5, 10));

		partStatusTableView = UiFactory.createTableView(PartResult.class);
		partStatusTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		partStatusTableView.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() * 0.60);
		HBox.setHgrow(partStatusTableView, Priority.ALWAYS);
		
		TableColumn<PartResult, String> statusCol = UiFactory.createTableColumn(PartResult.class, String.class,
				"Part Status");
		statusCol.setPrefWidth(100);
		statusCol.setCellValueFactory(new Callback<CellDataFeatures<PartResult, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<PartResult, String> p) {
				String partStatus = p.getValue().getStatus() == null ? "" : p.getValue().getStatus().name();
				//Part status should not be displayed for operation type GALC Meas and GALC Meas Manual
				if(p.getValue().getOperationType() != null && (p.getValue().getOperationType().equalsIgnoreCase(OperationType.GALC_MEAS.name())
								|| p.getValue().getOperationType().equalsIgnoreCase(OperationType.GALC_MEAS_MANUAL.name()))){
					MCOperationRevision  mcOperationRev = controller.getOperation(p.getValue());
					String measurementStatus =p.getValue().getBuildResultStatusMeasure(mcOperationRev)== null?"":  p.getValue().getBuildResultStatusMeasure(mcOperationRev).name();
					if(measurementStatus.equalsIgnoreCase(MeasurementStatus.OK.name())){
						partStatus = ApplicationConstants.OK_COLOR;
					}else if(measurementStatus.equalsIgnoreCase(MeasurementStatus.NG.name())){
						partStatus = ApplicationConstants.NG_COLOR;
					}
				}
				
				return new ReadOnlyObjectWrapper<String>(partStatus);
			}
		});
		
		statusCol.setCellFactory(new Callback<TableColumn<PartResult,String>, TableCell<PartResult,String>>(){
				//@Override
				public TableCell<PartResult, String> call( TableColumn<PartResult, String> param) {
					
					final TableCell<PartResult, String> cell = new TableCell<PartResult, String>() {
						@Override
						public void updateItem(String item, boolean empty) {
							super.updateItem(item, empty);
							setText(null);
							setStyle("");
							if (!isEmpty()) {
								setText(item.toString());
								if(ApplicationConstants.NG.equals(item.toString()) || ApplicationConstants.NG_COLOR.equalsIgnoreCase(item.toString())){
									setStyle("-fx-background-color: #ff3333;-fx-border-color:grey;");
									if(ApplicationConstants.NG_COLOR.equalsIgnoreCase(item.toString())){
										setText("");
									}
								}
								else if(ApplicationConstants.OK.equals(item.toString()) || ApplicationConstants.OK_COLOR.equalsIgnoreCase(item.toString())){
									setStyle("-fx-background-color: #5ae1b5;-fx-border-color:grey;");
									if(ApplicationConstants.OK_COLOR.equalsIgnoreCase(item.toString())){
										setText("");
									}
								}else { 
									setStyle("-fx-background-color: none;");
								}
							}
						}
					};
				   
				    
					return cell;
				}
			} );

		TableColumn<PartResult, String> statusMeasureCol = UiFactory.createTableColumn(PartResult.class, String.class,
				"Measurement Status");
		statusMeasureCol.setPrefWidth(150);
		statusMeasureCol
				.setCellValueFactory(new Callback<CellDataFeatures<PartResult, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<PartResult, String> p) {
						MCOperationRevision  mcOperationRev = controller.getOperation(p.getValue());
						String statusMeasure = null  ;
						if(mcOperationRev==null  && p.getValue().getLotControlRule() != null)
							statusMeasure = p.getValue().getBuildResultStatusMeasure(p.getValue().getLotControlRule())== null?"":  p.getValue().getBuildResultStatusMeasure(p.getValue().getLotControlRule()).name();
						else
							statusMeasure =p.getValue().getBuildResultStatusMeasure(mcOperationRev)== null?"":  p.getValue().getBuildResultStatusMeasure(mcOperationRev).name();
						//Measurement status should not be displayed for operation type instruction ,GALC Instruction, auto complete instruction,GALC scan and GALC Made From 
						if(p.getValue().getOperationType() != null && (p.getValue().getOperationType().equalsIgnoreCase(OperationType.GALC_SCAN.name())
								|| p.getValue().getOperationType().equalsIgnoreCase(OperationType.GALC_INSTRUCTION.name())
								|| p.getValue().getOperationType().equalsIgnoreCase(OperationType.GALC_AUTO_COMPLETE.name()) 
								|| p.getValue().getOperationType().equalsIgnoreCase(OperationType.INSTRUCTION.name())
								|| p.getValue().getOperationType().equalsIgnoreCase(OperationType.GALC_MADE_FROM.name()))){
							if(statusMeasure.equalsIgnoreCase(MeasurementStatus.OK.name())) 
								statusMeasure = ApplicationConstants.OK_COLOR;
							else if(statusMeasure.equalsIgnoreCase(MeasurementStatus.NG.name()))
								statusMeasure = ApplicationConstants.NG_COLOR;
							
						}
						return new ReadOnlyObjectWrapper<String>(statusMeasure);
					}
				});
		statusMeasureCol.setCellFactory(new Callback<TableColumn<PartResult,String>, TableCell<PartResult,String>>(){
			//@Override
			public TableCell<PartResult, String> call( TableColumn<PartResult, String> param) {
				
				final TableCell<PartResult, String> cell = new TableCell<PartResult, String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						setText(null);
						setStyle("");
						if (!isEmpty()) {
							setText(item.toString());
							if(ApplicationConstants.NG.equals(item.toString()) || ApplicationConstants.NG_COLOR.equalsIgnoreCase(item.toString()) ){
								setStyle("-fx-background-color: #ff3333;-fx-border-color:grey;");
								if(ApplicationConstants.NG_COLOR.equalsIgnoreCase(item.toString())){
									setText("");
								}
							}
							else if(ApplicationConstants.OK.equals(item.toString()) || ApplicationConstants.OK_COLOR.equalsIgnoreCase(item.toString())){
								setStyle("-fx-background-color: #5ae1b5;-fx-border-color:grey;");
								if(ApplicationConstants.OK_COLOR.equalsIgnoreCase(item.toString())){
									setText("");
								}
							}else setStyle("");
						}else{
							setStyle("");
						}
					}
				};
			   
			    
				return cell;
			}
		} );
		
		TableColumn<PartResult, String> partNameCol = UiFactory.createTableColumn(PartResult.class, String.class,
				"Part/Operation Name");
		partNameCol.setPrefWidth(200);
		partNameCol.setCellValueFactory(new Callback<CellDataFeatures<PartResult, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<PartResult, String> p) {
				PartResult partResult = p.getValue();
				MCOperationRevision  mcOperationRev = controller.getOperation(partResult);
				if(partResult.isStopShipOnly() &&
						((partResult.getBuildResultStatusMeasure(mcOperationRev) != null && MeasurementStatus.NG.equals(partResult.getBuildResultStatusMeasure(mcOperationRev)) ) ||
						 (partResult.getStatus() != null && InstalledPartStatus.NG.equals(partResult.getStatus()))
						) 
					){
					invalidStopShipPart = true;
				}else{
					invalidStopShipPart = false;
				}
				return new ReadOnlyObjectWrapper<String>(p.getValue().getPartName());
			}
		});
		partNameCol.setCellFactory(new Callback<TableColumn<PartResult,String>, TableCell<PartResult,String>>(){
			//@Override
			public TableCell<PartResult, String> call( TableColumn<PartResult, String> param) {
				final TableCell<PartResult, String> cell = new TableCell<PartResult, String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						setText(null);
						setStyle("");
						if (!isEmpty()) {
							setText(item.toString());
							if(invalidStopShipPart){
								setStyle("-fx-background-color: yellow;-fx-border-color:grey;");
							}else{
								setStyle("");
							}
						}
						
						
					}
				};
			   
			    
				return cell;
			}
		} );

		TableColumn<PartResult, String> partDescCol = UiFactory.createTableColumn(PartResult.class, String.class,
				"Unit Description");
		partDescCol.setPrefWidth(255);
		partDescCol.setCellValueFactory(new Callback<CellDataFeatures<PartResult, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<PartResult, String> p) {
				PartResult partResult = p.getValue();
				MCOperationRevision  mcOperationRev = controller.getOperation(partResult);
				if(partResult.isStopShipOnly() &&
						((partResult.getBuildResultStatusMeasure(mcOperationRev) != null && MeasurementStatus.NG.equals(partResult.getBuildResultStatusMeasure(mcOperationRev)) ) ||
						 (partResult.getStatus() != null && InstalledPartStatus.NG.equals(partResult.getStatus()))
						) 
					){
					invalidStopShipPart = true;
				}else{
					invalidStopShipPart = false;
				}
				return new ReadOnlyObjectWrapper<String>(p.getValue().getPartDesc());
			}
		});
		partDescCol.setCellFactory(new Callback<TableColumn<PartResult,String>, TableCell<PartResult,String>>(){
			//@Override
			public TableCell<PartResult, String> call( TableColumn<PartResult, String> param) {
				final TableCell<PartResult, String> cell = new TableCell<PartResult, String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						setText(null);
						setStyle("");
						if (!isEmpty() && !StringUtils.isEmpty(item) ) {
							setText(item.toString());
							if(invalidStopShipPart){
								setStyle("-fx-background-color: yellow;-fx-border-color:grey;");
							}else{
								setStyle("");
							}
						}
						
					}
				};
			   
			    
				return cell;
			}
		} );

		TableColumn<PartResult, String> partSerialNumberCol = UiFactory.createTableColumn(PartResult.class,
				String.class, "Serial Number");
		partSerialNumberCol.setPrefWidth(200);
		partSerialNumberCol
				.setCellValueFactory(new Callback<CellDataFeatures<PartResult, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<PartResult, String> p) {
						String partSerialNumber = "";
						if (p.getValue().getBuildResult() != null)
							partSerialNumber = StringUtils.isEmpty(p.getValue().getBuildResult().getPartSerialNumber())?"??":p.getValue().getBuildResult().getPartSerialNumber();
						else partSerialNumber = "??";
						return new ReadOnlyObjectWrapper<String>(partSerialNumber);
					}
				});
		partSerialNumberCol.setCellFactory(new Callback<TableColumn<PartResult,String>, TableCell<PartResult,String>>(){
			//@Override
			public TableCell<PartResult, String> call( TableColumn<PartResult, String> param) {
				final TableCell<PartResult, String> cell = new TableCell<PartResult, String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						setText(null);
						setStyle("");
						if (!isEmpty()) {
							setText(item.toString());
							if(invalidStopShipPart){
								setStyle("-fx-background-color: yellow;-fx-border-color:grey;");
							}else{
								setStyle("");
							}
						}
						
					}
				};
			   
			    
				return cell;
			}
		} );

		

		TableColumn<PartResult, String> measurementResultCol = UiFactory.createTableColumn(PartResult.class,
				String.class, "Measurement Result");
		measurementResultCol.setPrefWidth(150);
		measurementResultCol
				.setCellValueFactory(new Callback<CellDataFeatures<PartResult, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<PartResult, String> p) {
						return new ReadOnlyObjectWrapper<String>(p.getValue().getMeasurementResult());
					}
				});
		measurementResultCol.setCellFactory(new Callback<TableColumn<PartResult,String>, TableCell<PartResult,String>>(){
			//@Override
			public TableCell<PartResult, String> call( TableColumn<PartResult, String> param) {
				final TableCell<PartResult, String> cell = new TableCell<PartResult, String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						setText(null);
						setStyle("");
						if (!isEmpty()) {
							setText(item.toString());
							if(invalidStopShipPart){
								setStyle("-fx-background-color: yellow;-fx-border-color:grey;");
							}else{
								setStyle("");
							}
						}
						
						
					}
				};
			   
			    
				return cell;
			}
		} );

		TableColumn<PartResult, String> timestampCol = UiFactory.createTableColumn(PartResult.class, String.class,
				"Timestamp");
		timestampCol.setPrefWidth(150);
		timestampCol.setCellValueFactory(new Callback<CellDataFeatures<PartResult, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<PartResult, String> p) {
				String timestamp = p.getValue().getTimeStamp() == null ? "" : p.getValue().getTimeStamp().toString();
				return new ReadOnlyObjectWrapper<String>(timestamp);
			}
		});
		timestampCol.setCellFactory(new Callback<TableColumn<PartResult,String>, TableCell<PartResult,String>>(){
			//@Override
			public TableCell<PartResult, String> call( TableColumn<PartResult, String> param) {
				final TableCell<PartResult, String> cell = new TableCell<PartResult, String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						setText(null);
						setStyle("");
						if (!isEmpty()) {
							setText(item.toString());
							if(invalidStopShipPart){
								setStyle("-fx-background-color: yellow;-fx-border-color:grey;");
							}else{
								setStyle("");
							}
						}
						
						
					}
				};
			   
			    
				return cell;
			}
		} );

		TableColumn<PartResult, String> repairedCol = UiFactory.createTableColumn(PartResult.class, String.class,
				"Repaired");
		repairedCol.setPrefWidth(150);
		repairedCol.setCellValueFactory(new Callback<CellDataFeatures<PartResult, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<PartResult, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getRepaired());
			}
		});
		repairedCol.setCellFactory(new Callback<TableColumn<PartResult,String>, TableCell<PartResult,String>>(){
			//@Override
			public TableCell<PartResult, String> call( TableColumn<PartResult, String> param) {
				final TableCell<PartResult, String> cell = new TableCell<PartResult, String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						setText(null);
						setStyle("");
						if (!isEmpty()) {
							setText(item.toString());
							if(invalidStopShipPart){
								setStyle("-fx-background-color: yellow;-fx-border-color:grey;");
							}else{
								setStyle("");
							}
						}
						
						
					}
				};
			   
			    
				return cell;
			}
		} );


		TableColumn<PartResult, String> associateCol = UiFactory.createTableColumn(PartResult.class, String.class,
				"Associate");
		associateCol.setPrefWidth(100);
		associateCol.setCellValueFactory(new Callback<CellDataFeatures<PartResult, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<PartResult, String> p) {
				return new ReadOnlyObjectWrapper<String>(p.getValue().getAssociate());
			}
		});
		associateCol.setCellFactory(new Callback<TableColumn<PartResult,String>, TableCell<PartResult,String>>(){
			//@Override
			public TableCell<PartResult, String> call( TableColumn<PartResult, String> param) {
				final TableCell<PartResult, String> cell = new TableCell<PartResult, String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						setText(null);
						setStyle("");
						if (!isEmpty()) {
							setText(item.toString());
							if(invalidStopShipPart){
								setStyle("-fx-background-color: yellow;-fx-border-color:grey;");
							}else{
								setStyle("");
							}
						}
						
						
					}
				};
			   
			    
				return cell;
			}
		} );

		partStatusTableView.getColumns().clear();
		partStatusTableView.getColumns().addAll(statusCol, statusMeasureCol, partNameCol, partDescCol, partSerialNumberCol,
				measurementResultCol, timestampCol, repairedCol, associateCol);

		hbox.getChildren().add(partStatusTableView);

		return hbox;
	}

	// Getters & Setters
	@SuppressWarnings("unchecked")
	public ManualLotControlRepairController getController() {
		switch (getProductTypeData().getProductType()) {
		case ENGINE:
			controller = new ManualLotControlRepairEngineController(window, this);
			break;

		case HEAD:
			controller = new ManualLotControlRepairHeadController(window, this);
			break;
		case BLOCK:
			controller = new ManualLotControlRepairBlockController(window, this);
			break;
		case CONROD:
			controller = new ManualLotControlRepairConrodController(window, this);
			break;
		case CRANKSHAFT:
			controller = new ManualLotControlRepairCrankshaftController(window, this);
			break;

		default:
			controller = new ManualLotControlRepairController(window, this);
		}

		return controller;
	}

	@SuppressWarnings("unchecked")
	public void setController(ManualLotControlRepairController controller) {
		this.controller = controller;
	}

	public ProductTypeData getProductTypeData() {
		if(this.productTypeData == null) {
			setProductTypeData(getProductType().name());
		}
		return this.productTypeData;
	}
	
	public void setProductTypeData(String productType) {
		for (ProductTypeData type : this.controller.getAppContext().getProductTypeDataList()) {
			if (type.getProductTypeName().equals(productType)) {
				this.productTypeData = type;
				break;
			}
		}
	}

	public ManualLotControlRepairPropertyBean getProperty() {
		return property;
	}

	public ProductType getCurrentProductType() {
		return currentProductType;
	}

	public ProductType getProductType() {
		if (currentProductType == null) {
			currentProductType = ProductType.valueOf(property.getProductType());
		}
		return currentProductType;
	}

	@Override
	public void onTabSelected() {
		initComponents();
	}
	
	private void showResults() {
		controller.setHasProductIdChanged(true);
		filterPane.setText("Filter Criteria");
		EventBusUtil.publish(new StatusMessageEvent("",StatusMessageEventType.CLEAR));
		product = controller.checkProductOnServer(productIdTextField.getText());
		if (product != null) {
			productTypeTextField.setText(product.getProductSpecCode());
			disableFilters(false);
			filterPane.setExpanded(true);
			partStatusTableView.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() * 0.364);
			filterPane.setText(filterPane.getText()+" : "+" Please select division");
			filterPane.requestFocus();
			Platform.runLater(new Runnable() {
				public void run() {
					divisionComboBox.show();
					if(isDoneBtnClicked){
						findButton.setDisable(false);
						clearFilters.setDisable(false);
						isDoneBtnClicked = false;
					}
				}
			});
		}
	}

	private void mapActions() {

		productIdTextField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		    	productTypeTextField.setText(StringUtils.EMPTY);
				partStatusTableView.getItems().clear();
				filterPane.setGraphic(null);
				filterPane.setText("Filter Criteria");
				disableFilters(true);
				productIdTextField.setText(newValue);
				showResults();
		    }
		});
		
		findButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				controller.loadProductBuildResults(product);
				controller.setFilterCriteriaText();
				filterPane.setExpanded(false);
				partStatusTableView.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() * 0.60);
				// Enable Export To Excel Button only if table has record
				if(partStatusTableView != null && partStatusTableView.getItems().size() > 1) {
					exportToExcelBtn.setDisable(false);
					exportInExcel();
				} else
					exportToExcelBtn.setDisable(true);
			}
		});
		
		processPointButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				ManualLotControlViewMessageDialog dialog = 
						new ManualLotControlViewMessageDialog(getMainWindow(), 
								"Selected Process Points", getMainWindow().getStage(), true, processPointComboBox.getSelectionModel().getSelectedItems());
				dialog.showDialog();
			}
		});
		
		
		opTypeButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				ManualLotControlViewMessageDialog dialog = 
						new ManualLotControlViewMessageDialog(getMainWindow(), 
								"Selected Operation Types", getMainWindow().getStage(), false, operationTypeComboBox.getSelectionModel().getSelectedItems());
				dialog.showDialog();
			}
		});
		
		clearFilters.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				resetOnProduct();
				showResults();
			}
		});
		
		processPointComboBox.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends String> ov, String old_val, String new_val) -> {
					if(processPointComboBox.getSelectionModel().getSelectedItems().size()>0) {
						controller.setSelectedProcessPointChanged(true);
						processPointButton.setDisable(false);
					} else {
						processPointButton.setDisable(true);
					}
		});
		
		operationTypeComboBox.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends String> ov, String old_val, String new_val) -> {
					if(operationTypeComboBox.getSelectionModel().getSelectedItems().size()>0) 
						opTypeButton.setDisable(false);
					else
						opTypeButton.setDisable(true);	
		});
		
		divisionComboBox.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				findButton.setDisable(false);
				clearFilters.setDisable(false);
				controller.setSelectedDivisionChanged(true);
				Division selectedDivision = divisionComboBox.getSelectionModel().getSelectedItem();
				List<ProcessPoint> processPointList = new ArrayList<ProcessPoint>();
				List<String> processPointNameList = new ArrayList<String>();
				if(selectedDivision != null) {
					processPointList = controller.getProcessPointListByDivision(selectedDivision);
				} else {
					processPointList = controller.getAllProcessPoint();
				}
				for (ProcessPoint processpoint : processPointList) {
					processPointMap.put(processpoint.getProcessPointId()+"-"+processpoint.getProcessPointName(), processpoint);
					processPointNameList.add(processpoint.getProcessPointId()+"-"+processpoint.getProcessPointName());
				}
				processPointComboBox.getItems().clear();
				processPointComboBox.getItems().addAll(processPointNameList);
			}
		});

		addResult.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				TableViewSelectionModel<PartResult> selectedRow = partStatusTableView.getSelectionModel();
				PartResult partResult = selectedRow.getSelectedItem();
				if (product != null && partResult != null) {
					MCOperationRevision operation = controller.getOperation(partResult);
					createAndShowResultDialog(operation, partResult, ManualLotControlRepairActions.EDIT_RESULTS);
				}
			}
		});

		removeResult.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				TableViewSelectionModel<PartResult> selectedRow = partStatusTableView.getSelectionModel();
				PartResult partResult = selectedRow.getSelectedItem();
				controller.removePartResult(product.getProductId(),partResult);
			}
		});
		
		removeAllButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				List<PartResult> partResultList = new ArrayList<PartResult>(partStatusTableView.getItems());
				controller.removeAllPartResult(product.getProductId(),partResultList);
			}
		});
		
		refresh.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				if (product != null) {
					productTypeTextField.setText(product.getProductSpecCode());
					controller.setHasProductIdChanged(true);
					controller.loadProductBuildResults(product);
				}
			}
		});

		showHistory.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				TableViewSelectionModel<PartResult> selectedRow = partStatusTableView.getSelectionModel();
				PartResult partResult = selectedRow.getSelectedItem();
				if (product != null && partResult != null) {
					MCOperationRevision operation = controller.getOperation(partResult);
					createAndShowResultDialog(operation, partResult, ManualLotControlRepairActions.SHOW_HISTORY);
				}
			}
		}); 

		doneNextProduct.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				filterPane.setGraphic(null);
				filterPane.setText("Filter Criteria ");
				reset();
				isDoneBtnClicked = true;
			}
		});

		productIdBtn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(final ActionEvent arg0) {
					ManualProductEntryDialog manualProductEntryDialog = new ManualProductEntryDialog(
							"Manual Product Entry Dialog",productTypeData,getMainWindow().getApplicationContext().getApplicationId());
					manualProductEntryDialog.showDialog();
					String productId = manualProductEntryDialog.getResultProductId();
					if (!StringUtils.isEmpty(productId)) {
						productTypeTextField.setText(StringUtils.EMPTY);
						partStatusTableView.getItems().clear();
						filterPane.setGraphic(null);
						filterPane.setText("Filter Criteria");
						disableFilters(true);
						productIdTextField.setText(productId);
						showResults();
					}
				}

			});
		
		filterPane.expandedProperty().addListener(
				new ChangeListener<Boolean>() {
					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
						if(newValue)
							partStatusTableView.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() * 0.364);
						else
							partStatusTableView.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() * 0.60);
					}
				});

		insertResultBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (product != null) {
					createAndShowResultDialog(null, null, ManualLotControlRepairActions.INSERT_RESULTS);
				}
			}
		});
	}
	
	private void exportInExcel() {
		if(partStatusTableView == null)
			return;
		exportToExcelBtn.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	exportButtonAction(e);
		    }
		});
	}
	
	public void exportButtonAction(ActionEvent event) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		FileOutputStream fileOut = null;
		try {
			String fileName = getExcelFileName();
			if(StringUtils.isBlank(fileName))
				return;
			fileOut = new FileOutputStream(fileName);
			XSSFSheet sheet = workbook.createSheet(productIdTextField.getText()) ;
			CellStyle headerStyle = workbook.createCellStyle();
			setStyleproperties(headerStyle);
			CellStyle cellStyle = workbook.createCellStyle();
			setStyleproperties(cellStyle);
		    Font font = workbook.createFont();
		    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		    headerStyle.setFont(font);
		    XSSFRow headerRow=sheet.createRow(0);
		    int index = 0;
			for (Node n : partStatusTableView.lookupAll(".column-header > .label")) {
				if (n instanceof Label) {
					Label label = (Label) n;
					createCell(sheet, headerRow.createCell(index), label.getText().toString(), headerStyle);
					index++;
				}
			}
			
			for(int i = 0; i < partStatusTableView.getItems().size();  i++) {
      			XSSFRow row = sheet.createRow(i + 1);
      			for(int j = 0; j < partStatusTableView.getColumns().size(); j++) { 
      				if(partStatusTableView.getColumns().get(j).getCellData(i) != null && 
                    	!ApplicationConstants.OK_COLOR.equalsIgnoreCase(partStatusTableView.getColumns().get(j).getCellData(i).toString()) ) {
                    	createCell(sheet, row.createCell(j), partStatusTableView.getColumns().get(j).getCellData(i).toString(), cellStyle);
                    }else{
                    	createCell(sheet, row.createCell(j), "", cellStyle);
                    }
                }
      			if(i == 3)
      				sheet.setColumnWidth(i, 40*256);
      			else
      				sheet.setColumnWidth(i, 20*256);
      		}
			workbook.write(fileOut);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception while excel export");
		} finally {
            try {
            	if(fileOut != null) {
	            	fileOut.flush();
	    			fileOut.close();
            	}
			} catch (IOException ioe) {
				getLogger().error("Error occurred while closing Excel file: " + ioe.getMessage());
			}
		}
	}
	
	private String getExcelFileName() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Folder");
    	fileChooser.setInitialFileName("MLCR_" + productIdTextField.getText());
		fileChooser.setInitialDirectory(File.listRoots()[0]);
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel Files","*.xlsx");
		fileChooser.getExtensionFilters().add(extFilter);
		File file=fileChooser.showSaveDialog(ClientMainFx.getInstance().getStage());
		if(file != null)
			return file.getAbsolutePath();
		else
			return "";
	}
	
	private XSSFCell createCell(XSSFSheet sheet, XSSFCell cell, Object cellValue, CellStyle cellStyle) {
		cell.setCellValue(cellValue.toString());
		cell.setCellStyle(cellStyle);
		return cell;
	}
	
	private void setStyleproperties(CellStyle style) {
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setWrapText(true);
	}
	
	public TableView<PartResult> getPartStatusTableView() {
		return partStatusTableView;
	}

	public BaseProduct getProduct() {
		return product;
	}

	public void setProduct(BaseProduct product) {
		this.product = product;
	}

	private void reset() {
		productIdTextField.setText("");
		productTypeTextField.setText("");
		partStatusTableView.getItems().clear();
		addResult.setDisable(true);
		removeResult.setDisable(true);
		removeAllButton.setDisable(true);
		refresh.setDisable(true);
		showHistory.setDisable(true);
		doneNextProduct.setDisable(true);
		findButton.setDisable(true);
		insertResultBtn.setDisable(true);
		disableFilters(true);
	}
	
	private void disableFilters(boolean disable){
		divisionComboBox.setDisable(disable);
		processPointComboBox.setDisable(disable);
		operationTypeComboBox.setDisable(disable);
		stopShipCheckBox.setDisable(disable);
		nonGALCPartsCheckBox.setDisable(disable);
		requiredPartsCheckBox.setDisable(disable);
		serialNumber.setDisable(disable);
		partMask.setDisable(disable);
		unitNumbOrDesc.setDisable(disable);
		psetValue.setDisable(disable);
		operationStatusComboBox.setDisable(disable);
		specalityScreen.setDisable(disable);
		toolType.setDisable(disable);
		if(!disable) {
		
			if(processPointComboBox.getSelectionModel().getSelectedItems().size()>0)
				processPointButton.setDisable(disable);
			
			if(operationTypeComboBox.getSelectionModel().getSelectedItems().size()>0)
				opTypeButton.setDisable(disable);
		} else {
			processPointButton.setDisable(disable);
			opTypeButton.setDisable(disable);
		}
		
	}

	private void createAndShowResultDialog(final MCOperationRevision operation, final PartResult partResult, final ManualLotControlRepairActions action) {
		resultsDialog = new ManualLotControlResultsDialog("Enter Results", ClientMainFx.getInstance().getStage(),
				product, operation, partResult, action);
		resultsDialog.setOnCloseRequest(new EventHandler<WindowEvent>() {

			public void handle(WindowEvent we) {
				refreshData(partResult, action);
			}

		});
		resultsDialog.setOnHidden(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				refreshData(partResult, action);
			}
			
		});
		resultsDialog.showDialog();
	}
	
	private void refreshData(final PartResult partResult, ManualLotControlRepairActions action) {
		if(ManualLotControlRepairActions.isInsertResultsAction(action)) {
			String partName = resultsDialog.getPartNameTextField().getText();
			PartResult result = new PartResult();
			result.setPartName(partName);
			controller.refreshPartData(product.getProductId(), result);
		}else if(partResult != null) { 
			controller.refreshPartData(product.getProductId(), partResult);
		}
	}
	
	
	private void resetOnProduct() {
		partMask.setText(StringUtils.EMPTY);
		unitNumbOrDesc.setText(StringUtils.EMPTY);
		psetValue.setText(StringUtils.EMPTY);
		toolType.getSelectionModel().clearSelection();
		specalityScreen.getSelectionModel().clearSelection();
		stopShipCheckBox.setSelected(false);
		nonGALCPartsCheckBox.setSelected(false);
		requiredPartsCheckBox.setSelected(false);
		partStatusTableView.getItems().clear();
		productTypeTextField.setText(StringUtils.EMPTY);
		filterPane.setGraphic(null);
		filterPane.setText("Filter Criteria");
		divisionComboBox.getSelectionModel().clearSelection();
		processPointComboBox.getSelectionModel().clearSelection();
		operationTypeComboBox.getSelectionModel().clearSelection();
		operationStatusComboBox.getSelectionModel().clearSelection();
		serialNumber.setText(StringUtils.EMPTY);
		findButton.setDisable(true);
		clearFilters.setDisable(true);
		divisionComboBox.show();
		divisionComboBox.requestFocus();

	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public TabbedMainWindow getMainWin() {
		return mainWin;
	}

	public TextField getProductIdTextField() {
		return productIdTextField;
	}
	
	public TextField getProductTypeTextField() {
		return productTypeTextField;
	}

	public Button getAddResult() {
		return addResult;
	}
	
	public Button getInsertResultBtn() {
		return insertResultBtn;
	}

	public Button getRemoveResult() {
		return removeResult;
	}
	
	public Button getRemoveAllButton() {
		return removeAllButton;
	}

	public Button getRefresh() {
		return refresh;
	}

	public Button getDoneNextProduct() {
		return doneNextProduct;
	}

	public Button getShowHistory() {
		return showHistory;
	}

	public CheckBox getStopShipCheckBox() {
		return stopShipCheckBox;
	}
	public CheckBox getNonGALCPartsCheckBox() {
		return nonGALCPartsCheckBox;
	}

	public CheckBox getRequiredPartsCheckBox() {
		return requiredPartsCheckBox;
	}

	public ComboBox<Division> getDivisionComboBox() {
		return divisionComboBox;
	}

	public ListView<String> getProcessPointComboBox() {
		return processPointComboBox;
	}

	public LoggedComboBox<String> getProductTypeComboBox() {
		return productTypeCombobox;
	}

	public ListView<String> getOperationTypeComboBox() {
		return operationTypeComboBox;
	}

	public TextField getSerialNumber() {
		return serialNumber;
	}

	public TextField getUnitNumbOrDesc() {
		return unitNumbOrDesc;
	}
	
	
	public TextField getPartMask() {
		return partMask;
	}

	public ComboBox<String> getPartStatus() {
		return partStatus;
	}

	public ComboBox<String> getSpecalityScreen() {
		return specalityScreen;
	}

	public ComboBox<String> getToolType() {
		return toolType;
	}
	
	public TitledPane getFilterPane() {
		return filterPane;
	}

	public ComboBox<String> getOperationStatusType() {
		return operationStatusComboBox;
	}

	public TextField getPsetValue() {
		return psetValue;
	}

	public Boolean getInvalidStopShipPart() {
		return invalidStopShipPart;
	}

	public ManualLotControlResultsDialog getResultsDialog() {
		return resultsDialog;
	}
	
	
	public Map<String, ProcessPoint> getProcessPointMap() {
		return processPointMap;
	}

	public void setProcessPointMap(Map<String, ProcessPoint> processPointMap) {
		this.processPointMap = processPointMap;
	}

	public TextField getProductIdField(String productId) {
		if(productId != null)
			this.productIdTextField.setText(productId);
		return this.productIdTextField;
	}
	
	
	/**
	 * This method adds product type combo box listener
	 */
	@SuppressWarnings("unchecked")
	private void addProductTypeComboBoxListener() {
		productTypeCombobox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
				if(productIdBtn != null){
					setProductTypeData(new_val);
					productIdBtn.setText(productTypeData.getProductIdLabel());
					resetFilter();
				}
				
			} 
		});
	}
	
	
	/**
	 * This method resets filter
	 */
	private void resetFilter() {
		divisionComboBox.getSelectionModel().clearSelection();
		processPointComboBox.getSelectionModel().clearSelection();
		operationTypeComboBox.getSelectionModel().clearSelection();
		operationStatusComboBox.getSelectionModel().clearSelection();
		serialNumber.setText(StringUtils.EMPTY);
		partMask.setText(StringUtils.EMPTY);
		unitNumbOrDesc.setText(StringUtils.EMPTY);
		psetValue.setText(StringUtils.EMPTY);
		toolType.getSelectionModel().clearSelection();
		specalityScreen.getSelectionModel().clearSelection();
		productIdTextField.setText(StringUtils.EMPTY);
		partStatusTableView.getItems().clear();
		productTypeTextField.setText(StringUtils.EMPTY);
		filterPane.setText("Filter Criteria");
	}

	public Button getProcessPointButton() {
		return processPointButton;
	}

	public Button getOpTypeButton() {
		return opTypeButton;
	}
	
	
	
}
