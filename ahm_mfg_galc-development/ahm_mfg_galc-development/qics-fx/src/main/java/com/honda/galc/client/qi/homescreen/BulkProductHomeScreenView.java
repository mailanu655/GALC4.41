package com.honda.galc.client.qi.homescreen;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.data.ProductSearchResult;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.qi.base.AbstractQiProcessView;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.entity.enumtype.ProcessPointType;
import com.honda.galc.entity.product.StationUser;
import com.honda.galc.entity.qi.QiStationEntryDepartment;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.MultiLineHelper;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.scene.text.Text;

public class BulkProductHomeScreenView extends AbstractQiProcessView<HomeScreenModel, BulkProductHomeScreenController> {

	private TabPane tabbedPane; 
	private LoggedButton newButton;
	private LoggedButton logoutButton;
	private LoggedButton commentButton;
	private LoggedButton enterTrainingModeButton;
	private LoggedButton exitTrainingModeButton;
	private LoggedButton refreshCacheButton;
	private LoggedButton submitProductsButton;
	private LoggedButton removeProductsButton;
	private LoggedButton exportTableButton;
	private LoggedButton fileChooserButton;
	public double screenWidth;
	public double screenHeight;
	private CheckBox keepSignedInChkBox;
	private LabeledTextField teamTextField;
	private LabeledTextField shiftTextField;
	private LabeledTextField extensionTextField;
	private LabeledTextField inspectedTextField;
	private LabeledTextField stationNameTextField;
	private LabeledTextField directPassedTextField;
	private LabeledTextField prdsScrappedTextField;
	private LabeledTextField totalRejectionsTextField;
	private LabeledTextField stationLocationTextField;
	private LabeledTextField prdsWithRejectionsTextField;
	private LabeledTextField notRepairedRejectionsTextField;
	private LabeledTextField scanCountTextField;

	private ComboBox<QiStationEntryDepartment> entryDeptComboBox;
	private ComboBox<QiStationEntryDepartment> entryDeptComboBox2;
	private ObjectTablePane<StationUser> associateTable;
	private ObjectTablePane<QiDefectResultDto> processedPrdTable;
	private ObjectTablePane<ProductSearchResult> scannedPrdTable;
	private ObjectTablePane<ProductSearchResult> nonProcessablePrdTable;
	private ObjectTablePane<ProductSearchResult> scrappedPrdTable;

	private ContextMenu scannedProductsTableMenu;
	private MenuItem processAllMenuItem;
	private MenuItem processSelectedMenuItem;
	private MenuItem removeAllMenuItem;
	private MenuItem removeSelectedMenuItem;
	private MenuItem deselectMenuItem;
	private MenuItem exportSelectedMenuItem;
	private MenuItem exportAllMenuItem;
	private MenuItem updateTrackingMenuItem;
	private MenuItem updateAllTrackingMenuItem;
	
	private ContextMenu scrappedProductsTableMenu;
	private MenuItem processAllMenuItemScrap;
	private MenuItem processSelectedMenuItemScrap;
	private MenuItem removeAllMenuItemScrap;
	private MenuItem removeSelectedMenuItemScrap;
	private MenuItem deselectMenuItemScrap;
	private MenuItem exportSelectedMenuItemScrap;
	private MenuItem exportAllMenuItemScarp;

	private Tab processedTab;
	private Tab scannedProductsTab;
	private Tab nonProcessableProductsTab;
	private Tab scrappedProductsTab;
	private Text filePath;

	public BulkProductHomeScreenView(MainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
		getController().activate();
	}

	@Override
	public void reload() {
		getController().loadStationAndProductData();
	}

	@Override
	public void start() {
	}

	@Override
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);

		screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
		screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

		this.setPrefHeight(screenHeight * 0.75);
		this.setMinHeight(screenHeight * 0.30);
		this.setMaxHeight(screenHeight * 0.90);
		this.setMinWidth(screenWidth * 0.99);
		this.setMaxWidth(screenWidth * 0.99);	

		this.setTop(getTopPanel());
		this.setCenter(getCenterPanel());
	}

	private TitledPane getTopPanel() {
		TitledPane titledPane = new TitledPane();

		titledPane.setPrefHeight(screenHeight * 0.21);
		titledPane.setPrefWidth(screenWidth * 0.99);
		titledPane.setMinWidth(screenWidth * 0.99);
		
		HBox defectDataContainer = new HBox();
		stationNameTextField = createStationLabeledTextField("Station Name", new Insets(0));
		int textSize =  (int) (0.10 * (titledPane.getPrefHeight()));
		int textSizeSmall =  (int) (0.07 * (titledPane.getPrefHeight()));

		stationNameTextField.getControl().setStyle(getLabelStyle(textSize));
		stationNameTextField.getLabel().setStyle(getLabelStyle(textSize));
		stationNameTextField.setText(this.getApplicationId());
		defectDataContainer.getChildren().add(stationNameTextField);

		VBox allEntryDeptVBox = new VBox();
		if(!getModel().getProperty().isUpcStation()) {
			LoggedLabel entryDeptLbl = createLoggedLabel("entryDeptLbl1", "Entry Dept: ", getLabelStyle(textSize));
			HBox entryDept1HBox = new HBox();
			VBox entryDeptVBox1 = new VBox();
			LoggedLabel stationNameLbl1 = createLoggedLabel("stationLbl1", getProcessPointId(), getLabelStyleSmall(textSizeSmall));
			entryDeptComboBox = createEntryDeptComboBox();
			entryDeptVBox1.getChildren().addAll(stationNameLbl1,entryDeptComboBox);
			entryDept1HBox.getChildren().addAll(entryDeptLbl, entryDeptVBox1);
			allEntryDeptVBox.getChildren().add(entryDept1HBox);
			MultiLineHelper multiLineHelper = MultiLineHelper.getInstance(getProcessPointId());
			if(multiLineHelper.isMultiLine() && !StringUtils.isBlank(multiLineHelper.getFirstAlternateStation())) {
				String altPP = multiLineHelper.getFirstAlternateStation();
				LoggedLabel entryDeptLbl2 = createLoggedLabel("entryDeptLbl2", "Entry Dept: " , getLabelStyleSmall(textSizeSmall));
				HBox entryDept2HBox = new HBox();
				VBox entryDeptVBox2 = new VBox();
				LoggedLabel stationNameLbl2 = createLoggedLabel("stationLbl2", altPP, getLabelStyleSmall(textSizeSmall));
				entryDeptComboBox2 = createEntryDeptComboBox();
				entryDeptVBox2.getChildren().addAll(stationNameLbl2,entryDeptComboBox2);
				entryDept2HBox.getChildren().addAll(entryDeptLbl2,entryDeptVBox2);
				allEntryDeptVBox.getChildren().add(entryDept2HBox);
			}
			defectDataContainer.getChildren().add(allEntryDeptVBox);
		}

		LoggedLabel associateLbl = createLoggedLabel("associateLbl", "Associate", getLabelStyle(textSize));
		defectDataContainer.getChildren().add(associateLbl);
		defectDataContainer.getChildren().add(createAssociateTable((int) titledPane.getPrefHeight()));

		VBox buttonContainer = new VBox();
		buttonContainer.getChildren().add(createNewButton(textSize));

		keepSignedInChkBox = createCheckBox("Keep me Signed in",getController());
		keepSignedInChkBox.setStyle(getLabelStyle(textSize));
		buttonContainer.getChildren().add(keepSignedInChkBox);
		buttonContainer.setAlignment(Pos.CENTER_LEFT);
		buttonContainer.setSpacing(10);

		defectDataContainer.getChildren().add(buttonContainer);
		defectDataContainer.getChildren().add(createlogoutButton(textSize));
		if(ClientMainFx.getInstance().isLightSecurityLogin())
			logoutButton.setDisable(true);

		defectDataContainer.setAlignment(Pos.CENTER);
		defectDataContainer.setSpacing(20);
		
		titledPane.setText("Station Details");
		titledPane.setContent(defectDataContainer);
		return titledPane;
	}
	
	private Node getCenterPanel() {
		TitledPane titledPane = new TitledPane();
		titledPane.setPrefHeight(screenHeight * 0.48);
		titledPane.setPrefWidth(screenWidth * 0.99);

		HBox centerPanel = new HBox();
		centerPanel.setSpacing(0.02 * titledPane.getPrefWidth());
		centerPanel.getChildren().addAll(createDefectDataGridpane((int) titledPane.getPrefHeight()), createProductTable((int) titledPane.getPrefHeight()), createProductButtonPanel((int) titledPane.getPrefHeight()));
		titledPane.setText("Station Counts");
		titledPane.setContent(centerPanel);
		return titledPane;
	}

	private Node createDefectDataGridpane(int paneHeight) {

		int textSize = (int) (paneHeight * 0.04);
		int textSizeSmall = (int) (paneHeight * 0.03);

		GridPane defectDataContainer = createGrid((int) paneHeight);

		shiftTextField = createStationLabeledTextField("Shift", new Insets(0));
		shiftTextField.getLabel().setStyle(getTextStyleSmall(textSizeSmall));
		shiftTextField.getControl().setStyle(getTextStyle(textSize));
		shiftTextField.getControl().setMaxWidth(screenWidth * 0.067);
		shiftTextField.getControl().setPrefHeight(screenHeight * 0.040);
		defectDataContainer.add(shiftTextField, 0, 0);

		teamTextField = createStationLabeledTextField("Team", new Insets(0));
		teamTextField.getLabel().setStyle(getTextStyleSmall(textSizeSmall));
		teamTextField.getControl().setStyle(getTextStyle(textSize));
		teamTextField.getControl().setMaxWidth(screenWidth * 0.067);
		teamTextField.getControl().setPrefHeight(screenHeight * 0.040);
		defectDataContainer.add(teamTextField, 1, 0);

		inspectedTextField = createStationLabeledTextField("Unique Inspected", new Insets(0));
		inspectedTextField.getLabel().setStyle(getTextStyleSmall(textSizeSmall));
		inspectedTextField.getControl().setStyle(getTextStyle(textSize));
		inspectedTextField.getControl().setMaxWidth(screenWidth * 0.067);
		inspectedTextField.getControl().setPrefHeight(screenHeight * 0.040);
		defectDataContainer.add(inspectedTextField, 0, 1);

		directPassedTextField = createStationLabeledTextField("Direct Pass", new Insets(0));
		directPassedTextField.getLabel().setStyle(getTextStyleSmall(textSizeSmall));
		directPassedTextField.getControl().setStyle(getTextStyle(textSize));
		directPassedTextField.getControl().setMaxWidth(screenWidth * 0.067);
		directPassedTextField.getControl().setPrefHeight(screenHeight * 0.040);
		defectDataContainer.add(directPassedTextField, 1, 1);

		scanCountTextField = createStationLabeledTextField("Scan Count", new Insets(0));
		scanCountTextField.getLabel().setStyle(getTextStyleSmall(textSizeSmall));
		scanCountTextField.getControl().setStyle(getTextStyle(textSize));
		scanCountTextField.getControl().setMaxWidth(screenWidth * 0.067);
		scanCountTextField.getControl().setPrefHeight(screenHeight * 0.040);
		defectDataContainer.add(scanCountTextField, 0, 5);

		notRepairedRejectionsTextField = createStationLabeledTextField("Products With Not \nRepaired Rejections", new Insets(0));
		notRepairedRejectionsTextField.getLabel().setStyle(getTextStyleSmall(textSizeSmall));
		notRepairedRejectionsTextField.getControl().setStyle(getTextStyle(textSize));
		notRepairedRejectionsTextField.getControl().setMaxWidth(screenWidth * 0.067);
		notRepairedRejectionsTextField.getControl().setPrefHeight(screenHeight * 0.040);
		defectDataContainer.add(notRepairedRejectionsTextField, 0, 2);

		totalRejectionsTextField = createStationLabeledTextField("Total \nRejections", new Insets(0));
		totalRejectionsTextField.getLabel().setStyle(getTextStyleSmall(textSizeSmall));
		totalRejectionsTextField.getControl().setStyle(getTextStyle(textSize));
		totalRejectionsTextField.getControl().setMaxWidth(screenWidth * 0.067);
		totalRejectionsTextField.getControl().setPrefHeight(screenHeight * 0.040);
		defectDataContainer.add(totalRejectionsTextField, 1, 2);

		prdsWithRejectionsTextField = createStationLabeledTextField("Products With \nRejections", new Insets(0));
		prdsWithRejectionsTextField.getLabel().setStyle(getTextStyleSmall(textSizeSmall));
		prdsWithRejectionsTextField.getControl().setStyle(getTextStyle(textSize));
		prdsWithRejectionsTextField.getControl().setMaxWidth(screenWidth * 0.067);
		prdsWithRejectionsTextField.getControl().setPrefHeight(screenHeight * 0.040);
		defectDataContainer.add(prdsWithRejectionsTextField, 0, 3);

		prdsScrappedTextField = createStationLabeledTextField("Products \nScrapped", new Insets(0));
		prdsScrappedTextField.getLabel().setStyle(getTextStyleSmall(textSizeSmall));
		prdsScrappedTextField.getControl().setStyle(getTextStyle(textSize));
		prdsScrappedTextField.getControl().setMaxWidth(screenWidth * 0.067);
		prdsScrappedTextField.getControl().setPrefHeight(screenHeight * 0.040);
		defectDataContainer.add(prdsScrappedTextField, 1, 3);	

		stationLocationTextField = createStationLabeledTextField("Station Location", new Insets(0));
		stationLocationTextField.getLabel().setStyle(getTextStyleSmall(textSizeSmall));
		stationLocationTextField.getControl().setStyle(getTextStyle(textSize));
		stationLocationTextField.getControl().setMaxWidth(screenWidth * 0.067);
		stationLocationTextField.getControl().setPrefHeight(screenHeight * 0.040);
		defectDataContainer.add(stationLocationTextField, 0, 4);	

		extensionTextField = createStationLabeledTextField("Extension", new Insets(0));
		extensionTextField.getLabel().setStyle(getTextStyleSmall(textSizeSmall));
		extensionTextField.getControl().setStyle(getTextStyle(textSize));
		extensionTextField.getControl().setMaxWidth(screenWidth * 0.067);
		extensionTextField.getControl().setPrefHeight(screenHeight * 0.040);
		defectDataContainer.add(extensionTextField, 1, 4);	

		defectDataContainer.add(getButtonPanel(), 0, 6, 2, 1);	

		
		return defectDataContainer;
	}
	
	private Pane createProductTable(int paneHeight) {
		Pane table = new Pane();
		table.getChildren().add(createTabbedPane(paneHeight));
		createScannedProductsTableMenu();
		createScrappedProductsTableMenu();
		return table;
	}

	protected void createScannedProductsTableMenu() {
		if(scannedProductsTableMenu == null) {
			scannedProductsTableMenu = new ContextMenu();	

			processAllMenuItem = new MenuItem("Process All Products");
			processAllMenuItem.setStyle(getContextMenuItemPadding());
			processSelectedMenuItem = new MenuItem("Process Selected Products");
			processSelectedMenuItem.setStyle(getContextMenuItemPadding());
			removeAllMenuItem = new MenuItem("Remove All Products");
			removeAllMenuItem.setStyle(getContextMenuItemPadding());
			removeSelectedMenuItem = new MenuItem("Remove Selected Products");
			removeSelectedMenuItem.setStyle(getContextMenuItemPadding());
			deselectMenuItem = new MenuItem("Deselect selected Products");
			deselectMenuItem.setStyle(getContextMenuItemPadding());
			exportSelectedMenuItem = new MenuItem("Export Selected Products");
			exportSelectedMenuItem.setStyle(getContextMenuItemPadding());
			exportAllMenuItem = new MenuItem("Export All Products");
			exportAllMenuItem.setStyle(getContextMenuItemPadding());
			updateTrackingMenuItem = new MenuItem("Update Tracking");
			updateTrackingMenuItem.setStyle(getContextMenuItemPadding());
			updateAllTrackingMenuItem = new MenuItem("Update Tracking - All");
			updateAllTrackingMenuItem.setStyle(getContextMenuItemPadding());

			scannedProductsTableMenu.getItems().addAll(
					processAllMenuItem,
					processSelectedMenuItem,
					new SeparatorMenuItem(),
					removeAllMenuItem,
					removeSelectedMenuItem,
					new SeparatorMenuItem(),
					deselectMenuItem,
					new SeparatorMenuItem(),
					exportSelectedMenuItem,
					exportAllMenuItem);
			
			scannedProductsTableMenu.setStyle(getContextMenuFontStyle());
		}
	}
	
	public void addTrackingMenuItems()  {
		if(!scannedProductsTableMenu.getItems().contains(updateTrackingMenuItem)) {
			scannedProductsTableMenu.getItems().addAll(
					updateTrackingMenuItem,
					updateAllTrackingMenuItem);
		}
	}

	public void clearTrackingMenuItems()  {
		scannedProductsTableMenu.getItems().removeAll(
				updateTrackingMenuItem,
				updateAllTrackingMenuItem);
	}

	protected void createScrappedProductsTableMenu() {
		if(scrappedProductsTableMenu == null) {
			scrappedProductsTableMenu = new ContextMenu();	

			processAllMenuItemScrap = new MenuItem("Process All Products");
			processSelectedMenuItemScrap = new MenuItem("Process Selected Products");
			removeAllMenuItemScrap = new MenuItem("Remove All Products");
			removeSelectedMenuItemScrap = new MenuItem("Remove Selected Products");
			deselectMenuItemScrap = new MenuItem("Deselect selected Products");
			exportSelectedMenuItemScrap = new MenuItem("Export Selected Products");
			exportAllMenuItemScarp = new MenuItem("Export All Products");

			scrappedProductsTableMenu.getItems().addAll(
					processAllMenuItemScrap,
					processSelectedMenuItemScrap,
					new SeparatorMenuItem(),
					removeAllMenuItemScrap,
					removeSelectedMenuItemScrap,
					new SeparatorMenuItem(),
					deselectMenuItemScrap,
					new SeparatorMenuItem(),
					exportSelectedMenuItemScrap,
					exportAllMenuItemScarp);
		}
	}

	private Node createProductButtonPanel(int paneHeight) {
		VBox buttonContainer = new VBox();
		HBox importBox = new HBox();
		submitProductsButton = createBtn(QiConstant.PROCESS_ALL_PRODUCTS, getController());
		submitProductsButton.setPrefWidth(screenWidth * 0.11);
		submitProductsButton.setPrefHeight(screenHeight * 0.100); 
		submitProductsButton.setStyle(getTextStyle((int) (0.04 * paneHeight)));
		submitProductsButton.setDisable(true);

		removeProductsButton = createBtn("Clear Products Table", getController());
		removeProductsButton.setPrefWidth(screenWidth * 0.11);
		removeProductsButton.setPrefHeight(screenHeight * 0.07);
		removeProductsButton.setStyle(getTextStyle((int) (0.04 * paneHeight)));
		removeProductsButton.setDisable(true);
		
		fileChooserButton = createBtn(QiConstant.IMPORT_PRODUCT_LIST, getController());
		filePath = new Text();
		importBox.getChildren().addAll(filePath, fileChooserButton);
		importBox.setPrefWidth(screenWidth * 0.11);
		importBox.setPrefHeight(screenHeight * 0.07);
		importBox.setStyle(getTextStyle((int) (0.04 * paneHeight)));
		importBox.setDisable(false);
		
		exportTableButton = createBtn(QiConstant.EXPORT_TABLE, getController());
		exportTableButton.setPrefWidth(screenWidth * 0.11);
		exportTableButton.setPrefHeight(screenHeight * 0.06);
		exportTableButton.setStyle(getTextStyle((int) (0.04 * paneHeight)));
		exportTableButton.setDisable(true);

		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setSpacing(0.08 * paneHeight);
		buttonContainer.getChildren().addAll(submitProductsButton, removeProductsButton, importBox, exportTableButton);

		return buttonContainer;
	}

	private Node getButtonPanel() {
		HBox  buttonContainer = new HBox();
		
		enterTrainingModeButton = createBtn(QiConstant.ENTER_TRAINING_MODE, getController());
		enterTrainingModeButton.setPrefWidth(screenWidth * 0.09);
		
		exitTrainingModeButton = createBtn(QiConstant.EXIT_TRAINING_MODE, getController());
		exitTrainingModeButton.setPrefWidth(screenWidth * 0.09);
		
		StackPane stack = new StackPane();
		stack.getChildren().addAll(exitTrainingModeButton,enterTrainingModeButton);
		

		commentButton = createBtn(QiConstant.COMMENT, getController());
		commentButton.setPrefWidth(screenWidth * 0.09);

		refreshCacheButton = createBtn(QiConstant.REFRESH_CACHE, getController());
		refreshCacheButton.setPrefWidth(screenWidth * 0.09);

		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setSpacing(30);
		buttonContainer.getChildren().addAll(stack, commentButton, refreshCacheButton);
		return buttonContainer;
	}

	private TabPane createTabbedPane(int paneHeight){
		VBox processedProductContainer= new VBox();
		VBox scannedProductsContainer = new VBox();
		VBox nonProcessableProductsContainer = new VBox();
		VBox scrappedProductsContainer = new VBox();
		processedTab = new Tab();
		scannedProductsTab = new Tab();
		nonProcessableProductsTab = new Tab();
		scrappedProductsTab = new Tab();

		tabbedPane = new TabPane();
		tabbedPane.setPrefHeight(0.82 * paneHeight);

		processedProductContainer.getChildren().addAll(createProcessedProductTable(paneHeight));
		scannedProductsContainer.getChildren().addAll(createScannedProductsTable(paneHeight));
		nonProcessableProductsContainer.getChildren().addAll(createNonProcessableProductsTable(paneHeight));
		scrappedProductsContainer.getChildren().addAll(createScrappedProductsTable(paneHeight));

		processedTab.setText("Processed Products(0)");
		processedTab.setStyle(getLabelStyleSmaller());
		processedTab.setId("processedProdId");
		processedTab.setContent(processedProductContainer);		
		processedTab.setClosable(false);

		scannedProductsTab.setText("Scanned Products(0)");
		scannedProductsTab.setStyle(getLabelStyleSmaller());
		scannedProductsTab.setId("scannedProducts");
		scannedProductsTab.setContent(scannedProductsContainer);
		scannedProductsTab.setClosable(false);

		nonProcessableProductsTab.setText("Invalid Products(0)");
		nonProcessableProductsTab.setStyle(getLabelStyleSmaller());
		nonProcessableProductsTab.setId("nonProcessableProducts");
		nonProcessableProductsTab.setContent(nonProcessableProductsContainer);
		nonProcessableProductsTab.setClosable(false);
		
		scrappedProductsTab.setText("Scrapped Products(0)");
        scrappedProductsTab.setStyle(getLabelStyleSmaller());
        scrappedProductsTab.setId("scrappedProducts");
        scrappedProductsTab.setContent(scrappedProductsContainer);
        scrappedProductsTab.setClosable(false);

		tabbedPane.getTabs().addAll(scannedProductsTab, nonProcessableProductsTab, scrappedProductsTab, processedTab);

		return tabbedPane;
	}

	private Node createAssociateTable(int panelHeight) {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Associate Id", "id.user");
		Double[] columnWidth = new Double[] {0.123}; 

		associateTable = new ObjectTablePane<StationUser>(columnMappingList,columnWidth);
		associateTable.setPadding(new Insets(0,5,0,0));
		associateTable.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.008 * screenWidth)));
		associateTable.setMaxWidth((int)(0.15 * screenWidth));
		associateTable.setMaxHeight((int)(0.95 * panelHeight));

		LoggedTableColumn<StationUser, Integer> column = new LoggedTableColumn<StationUser, Integer>();
		createSerialNumber(column);
		associateTable.getTable().getColumns().add(0, column);
		associateTable.getTable().getColumns().get(0).setText("#");
		associateTable.getTable().getColumns().get(0).setResizable(true);
		associateTable.getTable().getColumns().get(0).setMaxWidth((int)(0.021 * screenWidth));
		associateTable.getTable().getColumns().get(0).setMinWidth(5);
		associateTable.setEditable(false);
		associateTable.setConstrainedResize(false);

		return associateTable;
	}

	private Node createProcessedProductTable(int paneHeight) {
		ProcessPointType processPointType = ClientMainFx.getInstance().getApplicationContext().getProcessPoint().getProcessPointType();
		String countHeader="Defect \nCount";
		if(processPointType.equals(ProcessPointType.QICSRepair)) countHeader = "Repair \nCount";
		
		if(getController().isDieCastProduct()) {
			ColumnMappingList columnMappingList = ColumnMappingList
					.with("DC Number", "dcSerialNumber")
					.put("MC Number", "mcSerialNumber")
					.put(countHeader, "count")
					.put("Associate", "createUser")
					.put("Create date","createTime");
			Double[] columnWidth = new Double[] {0.10,0.10,0.07,0.08,0.125};
			processedPrdTable = new ObjectTablePane<QiDefectResultDto>(columnMappingList,columnWidth);
		}else {
			ColumnMappingList columnMappingList = ColumnMappingList
					.with("Processed", "productId")
					.put(countHeader, "count")
					.put("Associate", "createUser")
					.put("Create date","createTime");
			Double[] columnWidth = new Double[] {0.13,0.11,0.11,0.125};
			processedPrdTable = new ObjectTablePane<QiDefectResultDto>(columnMappingList,columnWidth);
		}
		
		processedPrdTable.setStyle(getTableFontStyle());
		processedPrdTable.setMaxWidth((int)(0.50 * screenWidth));
		processedPrdTable.setPrefHeight((int)(0.95 * paneHeight));


		LoggedTableColumn<QiDefectResultDto, Integer> column = new LoggedTableColumn<QiDefectResultDto, Integer>();
		createSerialNumber(column);
		processedPrdTable.getTable().getColumns().add(0, column);
		processedPrdTable.getTable().getColumns().get(0).setText("#");
		processedPrdTable.getTable().getColumns().get(0).setResizable(true);
		processedPrdTable.getTable().getColumns().get(0).setMaxWidth((int)(0.021 * screenWidth));
		processedPrdTable.getTable().getColumns().get(0).setMinWidth(5);
		processedPrdTable.setEditable(false);

		return processedPrdTable;
	}

	private Node createNonProcessableProductsTable(int paneHeight) {
		if(getController().isDieCastProduct()) {
			ColumnMappingList columnMappingList = ColumnMappingList
					.with("DC Number", "product.dcSerialNumber")
					.put("MC Number", "product.mcSerialNumber")
					.put("YMTO", "product.productSpecCode")
					.put("Tracking \nStatus", "trackingStatusName")
					.put("Error \nMessage","errorMessage")
					.put("Process Timestamp","formattedProductUpdateTimestamp");
			Double[] columnWidth = new Double[] {0.08,0.08,0.05,0.06,0.13,0.0705};

			nonProcessablePrdTable = new ObjectTablePane<ProductSearchResult>(columnMappingList,columnWidth);
		}else if (getController().isFrameProduct()){
			ColumnMappingList columnMappingList = ColumnMappingList
					.with("Product Id", "product.productId")
					.put("YMTO", "product.productSpecCode")
					.put("Tracking \nStatus", "trackingStatusName")
					.put("Seq. no.", "afOnSeqNum")
					.put("Production Lot","productionLot")
					.put("Error \nMessage","errorMessage")
					.put("Process Timestamp","formattedProductUpdateTimestamp");
			Double[] columnWidth = new Double[] {0.10,0.06,0.06,0.06,0.12,0.15,0.105};

			nonProcessablePrdTable = new ObjectTablePane<ProductSearchResult>(columnMappingList,columnWidth);
		}else {
			ColumnMappingList columnMappingList = ColumnMappingList
					.with("Product Id", "product.productId")
					.put("YMTO", "product.productSpecCode")
					.put("Tracking \nStatus", "trackingStatusName")
					.put("Error \nMessage","errorMessage")
					.put("Process Timestamp","formattedProductUpdateTimestamp");
			Double[] columnWidth = new Double[] {0.10,0.06,0.06,0.15,0.105};

			nonProcessablePrdTable = new ObjectTablePane<ProductSearchResult>(columnMappingList,columnWidth);
		}		
		nonProcessablePrdTable.setStyle(getTableFontStyle());
		nonProcessablePrdTable.setMaxWidth((int)(0.50 * screenWidth));
		nonProcessablePrdTable.setPrefHeight((int)(0.95 * paneHeight));

		LoggedTableColumn<ProductSearchResult, Integer> column = new LoggedTableColumn<ProductSearchResult, Integer>();
		createSerialNumber(column);
		nonProcessablePrdTable.getTable().getColumns().add(0, column);
		nonProcessablePrdTable.getTable().getColumns().get(0).setText("#");
		nonProcessablePrdTable.getTable().getColumns().get(0).setResizable(true);
		nonProcessablePrdTable.getTable().getColumns().get(0).setMaxWidth((int)(0.021 * screenWidth));
		nonProcessablePrdTable.getTable().getColumns().get(0).setMinWidth(5);
		nonProcessablePrdTable.setEditable(false);
		nonProcessablePrdTable.setSelectionMode(SelectionMode.MULTIPLE);

		return nonProcessablePrdTable;
	}

	private Node createScannedProductsTable(int paneHeight) {
		
		if(getController().isDieCastProduct()) {
			ColumnMappingList columnMappingList = ColumnMappingList
					.with("DC Number", "product.dcSerialNumber")
					.put("MC Number", "product.mcSerialNumber")
					.put("YMTO", "product.productSpecCode")
					.put("Tracking \nStatus", "trackingStatusName")
					.put("Defect \nStatus","defectStatus")
					.put("Kickout \nLocation","kickoutProcessPointName")
					.put("Process Timestamp","formattedProductUpdateTimestamp")
					.put("Search Process \nPoint","searchProcessPointId")
					.put("Search Process \nPoint Timestamp", "formattedSearchPpIdTimestamp");
			Double[] columnWidth;
			if (getController().isProductDunnagable()) {
				columnMappingList.put("Dunnage","dunnage");
				columnWidth = new Double[] {0.08,0.08,0.05,0.06,0.06,0.05,0.095,0.05,0.05,0.10};
			} else {
				columnWidth = new Double[] {0.08,0.08,0.05,0.06,0.06,0.05,0.095,0.05,0.05};
			}
			
			scannedPrdTable = new ObjectTablePane<ProductSearchResult>(columnMappingList,columnWidth);
		}else if (getController().isFrameProduct())  {
			ColumnMappingList columnMappingList = ColumnMappingList
					.with("Product Id", "product.productId")
					.put("YMTO", "product.productSpecCode")
					.put("Tracking \nStatus", "trackingStatusName")
					.put("Seq. no.", "afOnSeqNum")
					.put("Production Lot","productionLot")
					.put("Defect \nStatus","defectStatus")
					.put("Kickout \nLocation","kickoutProcessPointName")
					.put("Process Timestamp","formattedProductUpdateTimestamp")
					.put("Search Process \nPoint","searchProcessPointId")
					.put("Search Process \nPoint Timestamp", "formattedSearchPpIdTimestamp");
			Double[] columnWidth;
				columnWidth = new Double[] {0.10,0.06,0.08,0.05,0.1,0.07,0.07,0.095,0.05,0.05};
			scannedPrdTable = new ObjectTablePane<ProductSearchResult>(columnMappingList,columnWidth);
		}else {
			ColumnMappingList columnMappingList = ColumnMappingList
					.with("Product Id", "product.productId")
					.put("YMTO", "product.productSpecCode")
					.put("Tracking \nStatus", "trackingStatusName")
					.put("Defect \nStatus","defectStatus")
					.put("Kickout \nLocation","kickoutProcessPointName")
					.put("Process Timestamp","formattedProductUpdateTimestamp")
					.put("Search Process \nPoint","searchProcessPointId")
					.put("Search Process \nPoint Timestamp", "formattedSearchPpIdTimestamp");
			Double[] columnWidth;
			if (getController().isProductDunnagable()) {
				columnMappingList.put("Dunnage","dunnage");
				columnWidth = new Double[] {0.10,0.06,0.08,0.07,0.07,0.095,0.05,0.05,0.10};
			} else {
				columnWidth = new Double[] {0.10,0.06,0.08,0.07,0.07,0.095,0.05,0.05};
			}

			scannedPrdTable = new ObjectTablePane<ProductSearchResult>(columnMappingList,columnWidth);
		}
		scannedPrdTable.setStyle(getTableFontStyle());
		scannedPrdTable.setMaxWidth((int)(0.50 * screenWidth));
		scannedPrdTable.setPrefHeight((int)(0.95 * paneHeight));


		LoggedTableColumn<ProductSearchResult, Integer> column = new LoggedTableColumn<ProductSearchResult, Integer>();
		createSerialNumber(column);
		scannedPrdTable.getTable().getColumns().add(0, column);
		scannedPrdTable.getTable().getColumns().get(0).setText("#");
		scannedPrdTable.getTable().getColumns().get(0).setResizable(true);
		scannedPrdTable.getTable().getColumns().get(0).setMaxWidth((int)(0.021 * screenWidth));
		scannedPrdTable.getTable().getColumns().get(0).setMinWidth(5);
		scannedPrdTable.setEditable(false);
		scannedPrdTable.setSelectionMode(SelectionMode.MULTIPLE);

		return scannedPrdTable;
	}
	
	private Node createScrappedProductsTable(int paneHeight) {
		if(getController().isDieCastProduct()) {
			ColumnMappingList columnMappingList = ColumnMappingList
					.with("DC Number", "product.dcSerialNumber")
					.put("MC Number", "product.mcSerialNumber")
					.put("YMTO", "product.productSpecCode")
					.put("Tracking \nStatus", "trackingStatusName")
					.put("Defect \nStatus","defectStatus")
					.put("Kickout \nLocation","kickoutProcessPointName")
					.put("Process Timestamp","formattedProductUpdateTimestamp")
					.put("Search Process \nPoint","searchProcessPointId")
					.put("Search Process \nPoint Timestamp", "formattedSearchPpIdTimestamp");
			Double[] columnWidth = new Double[] {0.09,0.09,0.06,0.07,0.07,0.095,0.05,0.05,0.05};

			scrappedPrdTable = new ObjectTablePane<ProductSearchResult>(columnMappingList,columnWidth);
		}else if(getController().isFrameProduct()){
			ColumnMappingList columnMappingList = ColumnMappingList
					.with("Product Id", "product.productId")
					.put("YMTO", "product.productSpecCode")
					.put("Tracking \nStatus", "trackingStatusName")
					.put("Seq. no.", "afOnSeqNum")
					.put("Production Lot","productionLot")
					.put("Defect \nStatus","defectStatus")
					.put("Kickout \nLocation","kickoutProcessPointName")
					.put("Process Timestamp","formattedProductUpdateTimestamp")
					.put("Search Process \nPoint","searchProcessPointId")
					.put("Search Process \nPoint Timestamp", "formattedSearchPpIdTimestamp");
			Double[] columnWidth = new Double[] {0.12,0.08,0.08,0.08,0.16,0.08,0.08,0.08,0.08,0.115};

			scrappedPrdTable = new ObjectTablePane<ProductSearchResult>(columnMappingList,columnWidth);
		}else {
			ColumnMappingList columnMappingList = ColumnMappingList
					.with("Product Id", "product.productId")
					.put("YMTO", "product.productSpecCode")
					.put("Tracking \nStatus", "trackingStatusName")
					.put("Defect \nStatus","defectStatus")
					.put("Kickout \nLocation","kickoutProcessPointName")
					.put("Process Timestamp","formattedProductUpdateTimestamp")
					.put("Search Process \nPoint","searchProcessPointId")
					.put("Search Process \nPoint Timestamp", "formattedSearchPpIdTimestamp");
			Double[] columnWidth = new Double[] {0.12,0.08,0.08,0.08,0.08,0.08,0.08,0.115};

			scrappedPrdTable = new ObjectTablePane<ProductSearchResult>(columnMappingList,columnWidth);
		}
		
		scrappedPrdTable.setStyle(getTableFontStyle());
		scrappedPrdTable.setMaxWidth((int)(0.50 * screenWidth));
		scrappedPrdTable.setPrefHeight((int)(0.95 * paneHeight));


		LoggedTableColumn<ProductSearchResult, Integer> column = new LoggedTableColumn<ProductSearchResult, Integer>();
		createSerialNumber(column);
		scrappedPrdTable.getTable().getColumns().add(0, column);
		scrappedPrdTable.getTable().getColumns().get(0).setText("#");
		scrappedPrdTable.getTable().getColumns().get(0).setResizable(true);
		scrappedPrdTable.getTable().getColumns().get(0).setMaxWidth((int)(0.021 * screenWidth));
		scrappedPrdTable.getTable().getColumns().get(0).setMinWidth(5);
		scrappedPrdTable.setEditable(false);
		scrappedPrdTable.setSelectionMode(SelectionMode.MULTIPLE);

		return scrappedPrdTable;
	}

	private ComboBox<QiStationEntryDepartment> createEntryDeptComboBox() {
		ComboBox<QiStationEntryDepartment> myComboBox = new ComboBox<QiStationEntryDepartment>();
		myComboBox  = new ComboBox<QiStationEntryDepartment>();
		myComboBox.setMaxWidth((int)(0.15 * screenWidth));
		return myComboBox;
	}

	private GridPane createGrid(int paneHeight) {
		GridPane homeScreenScreenGridpane = new GridPane();
		homeScreenScreenGridpane.setHgap((int)(0.08 * paneHeight));
		homeScreenScreenGridpane.setVgap((int)(0.035 * paneHeight));
		return homeScreenScreenGridpane;
	}

	private LoggedButton createNewButton(int textSize) {
		newButton = createBtn("New", getController());
		newButton.setPrefWidth(screenWidth * 0.05);
		newButton.setMaxHeight(screenHeight * 0.020);
		newButton.setStyle(getTextStyle(textSize));
		return newButton;
	}

	private LoggedButton createlogoutButton(int textSize) {
		logoutButton = createBtn("Logout", getController());
		logoutButton.setPrefWidth(screenWidth * 0.10);
		logoutButton.setMaxHeight(screenHeight * 0.020);
		logoutButton.setStyle(getTextStyle(textSize));
		return logoutButton;
	}

	private LoggedLabel createLoggedLabel(String id, String text,String cssClass) {
		LoggedLabel label = UiFactory.createLabel(id, text);
		label.setStyle(cssClass);
		return label;
	}

	private LabeledTextField createStationLabeledTextField(String labelName, Insets insets) {
		LabeledTextField field = new LabeledTextField(labelName, true, insets, true, false);
		TextFieldState.DISABLED.setState(field.getControl());
		return field;
	}

	public  String  getTableFontStyle() {
		return String.format("-fx-font-size: %dpx;", (int)(0.009 * screenWidth));
	}
	
	public  String  getLabelStyle(int textSize) {
		return String.format("-fx-font-weight: bold; fx-padding: 0 10px 0 0;-fx-font-size: %dpx;", textSize);
	}
	
	public  String  getLabelStyleSmall(int textSize) {
		return String.format("-fx-font-weight: bold; fx-padding: 0 4px 0 0;-fx-font-size: %dpx;", textSize);
	}
	
	public  String  getTextStyle(int textSize) {
		return String.format("-fx-alignment:center;-fx-font-weight: bold;-fx-font-size: %dpx;", textSize);
	}
	
	public  String  getTextStyleSmall(int textSize) {
		return String.format("-fx-alignment:center;-fx-font-weight: bold;-fx-font-size: %dpx;", textSize);
	}

	public  String  getLabelStyleSmaller() {
		return String.format("-fx-font-weight: bold; fx-padding: 0 2px 0 0;-fx-font-size: %dpx;", (int)(0.007 * (screenWidth + screenHeight / 25)));
	}
	
	public  String  getTextStyleSmall() {
		return String.format("-fx-alignment:center;-fx-font-weight: bold;-fx-font-size: %dpx;", (int)(0.0070 * (screenWidth + screenHeight / 25)));
	}
	
	public  String  getTextStyleSmall2() {
		return String.format("-fx-alignment:center;-fx-font-weight: bold;-fx-font-size: %dpx;", (int)(0.0063 * (screenWidth + screenHeight / 25)));
	}

	public  String  getTabStyleSmaller() {
		return String.format("-fx-font-weight: bold; fx-padding: 0 10px 0 0;-fx-font-size: %dpx;", (int)(0.007 * (screenWidth + screenHeight / 25)));
	}

	public  String  getTabStyleSmallerReceived() {
		return String.format("-fx-font-weight: bold; fx-padding: 0 10px 0 0;-fx-font-size: %dpx; -fx-background-color: #7bed9f; -fx-text-base-color: #f1f2f6;", (int)(0.007 * (screenWidth + screenHeight / 25)));
	}

	public void onTabSelected() {
	}

	@Override
	public ViewId getViewId() {
		return null;
	}

	public String getScreenName() {
		return "Home Screen For Product";
	}

	public TabPane getTabbedPane() {
		return tabbedPane;
	}

	public void setTabbedPane(TabPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	public Tab getProcessedTab() {
		return processedTab;
	}

	public void setProcessedTab(Tab processedTab) {
		this.processedTab = processedTab;
	}

	public ComboBox<QiStationEntryDepartment> getEntryDeptComboBox() {
		return entryDeptComboBox;
	}

	public ComboBox<QiStationEntryDepartment> getEntryDeptComboBox2() {
		return entryDeptComboBox2;
	}

	public void setEntryDeptComboBox2(ComboBox<QiStationEntryDepartment> entryDeptComboBox2) {
		this.entryDeptComboBox2 = entryDeptComboBox2;
	}

	public void setEntryDeptComboBox(ComboBox<QiStationEntryDepartment> entryDeptComboBox) {
		this.entryDeptComboBox = entryDeptComboBox;
	}

	public LoggedButton getCommentButton() {
		return commentButton;
	}

	public void setCommentButton(LoggedButton commentButton) {
		this.commentButton = commentButton;
	}

	public LoggedButton getRefreshCacheButton() {
		return refreshCacheButton;
	}

	public void setRefreshCacheButton(LoggedButton refreshCacheButton) {
		this.refreshCacheButton = refreshCacheButton;
	}

	public ObjectTablePane<QiDefectResultDto> getProcessedPrdTable() {
		return processedPrdTable;
	}

	public void setProcessedPrdTable(
			ObjectTablePane<QiDefectResultDto> processedPrdTable) {
		this.processedPrdTable = processedPrdTable;
	}

	public ObjectTablePane<ProductSearchResult> getScannedPrdTable() {
		return scannedPrdTable;
	}

	public void setScannedPrdTable(
			ObjectTablePane<ProductSearchResult> scannedPrdTable) {
		this.scannedPrdTable = scannedPrdTable;
	}

	/**
	 * @return the directPassedTextField
	 */
	public LabeledTextField getDirectPassedTextField() {
		return directPassedTextField;
	}

	/**
	 * @param directPassedTextField the directPassedTextField to set
	 */
	public void setDirectPassedTextField(LabeledTextField directPassedTextField) {
		this.directPassedTextField = directPassedTextField;
	}

	/**
	 * @return the stationNameTextField
	 */
	public LabeledTextField getStationNameTextField() {
		return stationNameTextField;
	}

	/**
	 * @param stationNameTextField the stationNameTextField to set
	 */
	public void setStationNameTextField(LabeledTextField stationNameTextField) {
		this.stationNameTextField = stationNameTextField;
	}

	/**
	 * @return the shiftTextField
	 */
	public LabeledTextField getShiftTextField() {
		return shiftTextField;
	}

	/**
	 * @param shiftTextField the shiftTextField to set
	 */
	public void setShiftTextField(LabeledTextField shiftTextField) {
		this.shiftTextField = shiftTextField;
	}

	/**
	 * @return the inspectedTextField
	 */
	public LabeledTextField getInspectedTextField() {
		return inspectedTextField;
	}

	/**
	 * @param inspectedTextField the inspectedTextField to set
	 */
	public void setInspectedTextField(LabeledTextField inspectedTextField) {
		this.inspectedTextField = inspectedTextField;
	}

	/**
	 * @return the notRepairedRejectionsTextField
	 */
	public LabeledTextField getNotRepairedRejectionsTextField() {
		return notRepairedRejectionsTextField;
	}

	/**
	 * @param notRepairedRejectionsTextField the notRepairedRejectionsTextField to set
	 */
	public void setNotRepairedRejectionsTextField(
			LabeledTextField notRepairedRejectionsTextField) {
		this.notRepairedRejectionsTextField = notRepairedRejectionsTextField;
	}

	/**
	 * @return the totalRejectionsTextField
	 */
	public LabeledTextField getTotalRejectionsTextField() {
		return totalRejectionsTextField;
	}

	/**
	 * @param totalRejectionsTextField the totalRejectionsTextField to set
	 */
	public void setTotalRejectionsTextField(LabeledTextField totalRejectionsTextField) {
		this.totalRejectionsTextField = totalRejectionsTextField;
	}

	/**
	 * @return the prdsWithRejectionsTextField
	 */
	public LabeledTextField getPrdsWithRejectionsTextField() {
		return prdsWithRejectionsTextField;
	}

	/**
	 * @param prdsWithRejectionsTextField the prdsWithRejectionsTextField to set
	 */
	public void setPrdsWithRejectionsTextField(LabeledTextField prdsWithRejectionsTextField) {
		this.prdsWithRejectionsTextField = prdsWithRejectionsTextField;
	}

	/**
	 * @return the prdsScrappedTextField
	 */
	public LabeledTextField getPrdsScrappedTextField() {
		return prdsScrappedTextField;
	}

	/**
	 * @param prdsScrappedTextField the prdsScrappedTextField to set
	 */
	public void setPrdsScrappedTextField(LabeledTextField prdsScrappedTextField) {
		this.prdsScrappedTextField = prdsScrappedTextField;
	}

	/**
	 * @return the stationLocationTextField
	 */
	public LabeledTextField getStationLocationTextField() {
		return stationLocationTextField;
	}

	/**
	 * @param stationLocationTextField the stationLocationTextField to set
	 */
	public void setStationLocationTextField(LabeledTextField stationLocationTextField) {
		this.stationLocationTextField = stationLocationTextField;
	}

	/**
	 * @return the extentionTextField
	 */
	public LabeledTextField getExtentionTextField() {
		return extensionTextField;
	}

	/**
	 * @param extentionTextField the extentionTextField to set
	 */
	public void setExtentionTextField(LabeledTextField extentionTextField) {
		this.extensionTextField = extentionTextField;
	}

	/**
	 * @return the keepSignedInChkBox
	 */
	public CheckBox getKeepSignedInChkBox() {
		return keepSignedInChkBox;
	}

	/**
	 * @param keepSignedInChkBox the keepSignedInChkBox to set
	 */
	public void setKeepSignedInChkBox(CheckBox keepSignedInChkBox) {
		this.keepSignedInChkBox = keepSignedInChkBox;
	}

	/**
	 * @return the teamTextField
	 */
	public LabeledTextField getTeamTextField() {
		return teamTextField;
	}

	/**
	 * @param teamTextField the teamTextField to set
	 */
	public void setTeamTextField(LabeledTextField teamTextField) {
		this.teamTextField = teamTextField;
	}

	public LoggedButton getFileChooserButton() {
		return fileChooserButton;
	}

	public void setFileChooserButton(LoggedButton fileChooserButton) {
		this.fileChooserButton = fileChooserButton;
	}
	
	public Text getFilePath() {
		return filePath;
	}

	public void setFilePath(Text filePath) {
		this.filePath = filePath;
	}
	
	public LoggedButton getNewButton() {
		return newButton;
	}

	public LoggedButton getlogoutButton() {
		return logoutButton;
	}

	public void setNewButton(LoggedButton newButton) {
		this.newButton = newButton;
	}

	public ObjectTablePane<StationUser> getAssociateTable() {
		return associateTable;
	}

	public void setAssociateTable(ObjectTablePane<StationUser> associateTable) {
		this.associateTable = associateTable;
	}

	public LabeledTextField getScanCountTextField() {
		return scanCountTextField;
	}

	public void setScanCountTextField(LabeledTextField scanCountTextField) {
		this.scanCountTextField = scanCountTextField;
	}

	public Tab getScannedProductsTab() {
		return scannedProductsTab;
	}

	public Tab getNonProcessableProductsTab() {
		return nonProcessableProductsTab;
	}

	public ObjectTablePane<ProductSearchResult> getNonProcessablePrdTable() {
		return nonProcessablePrdTable;
	}

	public MenuItem getProcessAllMenuItem() {
		return processAllMenuItem;
	}

	public MenuItem getProcessSelectedMenuItem() {
		return processSelectedMenuItem;
	}

	public MenuItem getRemoveAllMenuItem() {
		return removeAllMenuItem;
	}

	public MenuItem getRemoveSelectedMenuItem() {
		return removeSelectedMenuItem;
	}

	public ContextMenu getScannedProductsTableMenu() {
		return scannedProductsTableMenu;
	}

	public MenuItem getDeselectMenuItem() {
		return deselectMenuItem;
	}
	
	public MenuItem getExportSelectedMenuItem() {
		return exportSelectedMenuItem;
	}

	public MenuItem getExportAllMenuItem() {
		return exportAllMenuItem;
	}

	public MenuItem getUpdateTrackingMenuItem() {
		return updateTrackingMenuItem;
	}

	public MenuItem getUpdateAllTrackingMenuItem() {
		return updateAllTrackingMenuItem;
	}

	public MenuItem getExportSelectedMenuItemScrap() {
		return exportSelectedMenuItemScrap;
	}

	public MenuItem getExportAllMenuItemScarp() {
		return exportAllMenuItemScarp;
	}

	public LoggedButton getSubmitProductsButton() {
		return submitProductsButton;
	}

	public LoggedButton getRemoveProductsButton() {
		return removeProductsButton;
	}

	public LoggedButton getExportTableButton() {
		return exportTableButton;
	}

	public ObjectTablePane<ProductSearchResult> getScrappedPrdTable() {
		return scrappedPrdTable;
	}

	public ContextMenu getScrappedProductsTableMenu() {
		return scrappedProductsTableMenu;
	}

	public MenuItem getProcessAllMenuItemScrap() {
		return processAllMenuItemScrap;
	}

	public MenuItem getProcessSelectedMenuItemScrap() {
		return processSelectedMenuItemScrap;
	}

	public MenuItem getRemoveAllMenuItemScrap() {
		return removeAllMenuItemScrap;
	}

	public MenuItem getRemoveSelectedMenuItemScrap() {
		return removeSelectedMenuItemScrap;
	}

	public MenuItem getDeselectMenuItemScrap() {
		return deselectMenuItemScrap;
	}

	public Tab getScrappedProductsTab() {
		return scrappedProductsTab;
	}

	public LoggedButton getEnterTrainingModeButton() {
		return enterTrainingModeButton;
	}

	public void setEnterTrainingModeButton(LoggedButton enterTrainingModeButton) {
		this.enterTrainingModeButton = enterTrainingModeButton;
	}

	public LoggedButton getExitTrainingModeButton() {
		return exitTrainingModeButton;
	}

	public void setExitTrainingModeButton(LoggedButton exitTrainingModeButton) {
		this.exitTrainingModeButton = exitTrainingModeButton;
	}
	
	public String getContextMenuItemPadding() {
		return "-fx-padding: 10 0 5 0";
	}
	
	public String getContextMenuFontStyle() {
		return String.format("-fx-font-family : Dialog; -fx-font-weight: bold; -fx-background-color: #e8ecf1; -fx-font-size: %dpx;", (int)(0.007 * screenWidth));
	}
	
}
