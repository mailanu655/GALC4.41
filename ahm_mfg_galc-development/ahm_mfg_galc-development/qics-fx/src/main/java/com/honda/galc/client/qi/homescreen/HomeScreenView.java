package com.honda.galc.client.qi.homescreen;



import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>HomeScreenMainPanel</code> is the Panel class for home screen with product Id and home screen with UPC.
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
 * <TD>15/09/2016</TD>
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
public class HomeScreenView extends AbstractQiProcessView<HomeScreenModel, HomeScreenController> {
	
	private TabPane tabbedPane; 
	private LoggedButton newButton;
	private LoggedButton logoutButton;
	private LoggedButton commentButton;
	private LoggedButton enterTrainingModeButton;
	private LoggedButton exitTrainingModeButton;
	private LoggedButton refreshCacheButton;
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
	
	//Jira-6369
	private Tab productSeqTab;
	private Tab processedTab;
	private ProductSequencePanel prodSeqPanel;
	
	public HomeScreenView(MainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
		getController().activate();
	}
	
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		
		screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
		screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
		
		this.setPrefHeight(screenHeight * 0.70);
		this.setMinHeight(screenHeight * 0.30);
		this.setMaxHeight(screenHeight);
		this.setMinWidth(screenWidth * 0.99);
		this.setMaxWidth(screenWidth * 0.99);
		this.setTop(getTopPanel());
		this.setCenter(createDefectDataGridpane());
	}
	
	private TitledPane getTopPanel() {
		HBox defectDataContainer = new HBox();
		stationNameTextField = createStationLabeledTextField("Station Name", new Insets(10));
		stationNameTextField.getControl().setStyle(getLabelStyle());
		stationNameTextField.getLabel().setStyle(getLabelStyle());
		stationNameTextField.setText(this.getApplicationId());
		defectDataContainer.getChildren().add(stationNameTextField);

		VBox allEntryDeptVBox = new VBox();
		if(!getModel().getProperty().isUpcStation()) {
			LoggedLabel entryDeptLbl = createLoggedLabel("entryDeptLbl1", "Entry Dept: ", getLabelStyleSmall());
			HBox entryDept1HBox = new HBox();
			VBox entryDeptVBox1 = new VBox();
			LoggedLabel stationNameLbl1 = createLoggedLabel("stationLbl1", getProcessPointId(), getLabelStyleSmall());
			entryDeptComboBox = createEntryDeptComboBox();
			entryDeptVBox1.getChildren().addAll(stationNameLbl1,entryDeptComboBox);
			entryDept1HBox.getChildren().addAll(entryDeptLbl, entryDeptVBox1);
			allEntryDeptVBox.getChildren().add(entryDept1HBox);
			MultiLineHelper multiLineHelper = MultiLineHelper.getInstance(getProcessPointId());
			if(multiLineHelper.isMultiLine() && !StringUtils.isBlank(multiLineHelper.getFirstAlternateStation())) {
				String altPP = multiLineHelper.getFirstAlternateStation();
				LoggedLabel entryDeptLbl2 = createLoggedLabel("entryDeptLbl2", "Entry Dept: " , getLabelStyleSmall());
				HBox entryDept2HBox = new HBox();
				VBox entryDeptVBox2 = new VBox();
				LoggedLabel stationNameLbl2 = createLoggedLabel("stationLbl2", altPP, getLabelStyleSmall());
				entryDeptComboBox2 = createEntryDeptComboBox();
				entryDeptVBox2.getChildren().addAll(stationNameLbl2,entryDeptComboBox2);
				entryDept2HBox.getChildren().addAll(entryDeptLbl2,entryDeptVBox2);
				allEntryDeptVBox.getChildren().add(entryDept2HBox);
			}
			defectDataContainer.getChildren().add(allEntryDeptVBox);
		}

		LoggedLabel associateLbl = createLoggedLabel("associateLbl", "Associate", getLabelStyle());
		defectDataContainer.getChildren().add(associateLbl);
		defectDataContainer.getChildren().add(createAssociateTable());
		
		VBox buttonContainer = new VBox();
		buttonContainer.getChildren().add(createNewButton());

		keepSignedInChkBox = createCheckBox("Keep me Signed in",getController());
		keepSignedInChkBox.setStyle(getLabelStyle());
		buttonContainer.getChildren().add(keepSignedInChkBox);
		buttonContainer.setAlignment(Pos.CENTER_LEFT);
		buttonContainer.setSpacing(10);
		
		defectDataContainer.getChildren().add(buttonContainer);
		defectDataContainer.getChildren().add(createlogoutButton());
		if(ClientMainFx.getInstance().isLightSecurityLogin())
			logoutButton.setDisable(true);
		
		defectDataContainer.setAlignment(Pos.CENTER);
		defectDataContainer.setSpacing(20);
		TitledPane titledPane = new TitledPane("Station Details", defectDataContainer);
		return titledPane;
	}
	
	private LoggedButton createNewButton() {
		newButton = createBtn("New", getController());
		newButton.setPrefWidth(screenWidth * 0.05);
		return newButton;
	}
	
	private LoggedButton createlogoutButton() {
		logoutButton = createBtn("Logout", getController());
		logoutButton.setPrefWidth(screenWidth * 0.10);
		return logoutButton;
	}
	


	private Node createAssociateTable() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Associate Id", "id.user");
		Double[] columnWidth = new Double[] {0.123}; 
		
		associateTable = new ObjectTablePane<StationUser>(columnMappingList,columnWidth);
		associateTable.setPadding(new Insets(0,5,0,0));
		associateTable.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.008 * screenWidth)));
		associateTable.setMaxWidth((int)(0.15 * screenWidth));
		associateTable.setMaxHeight((int)(0.20 * screenHeight));
		
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

	private ComboBox<QiStationEntryDepartment> createEntryDeptComboBox() {
		ComboBox<QiStationEntryDepartment> myComboBox = new ComboBox<QiStationEntryDepartment>();
		myComboBox  = new ComboBox<QiStationEntryDepartment>();
		myComboBox.setMaxWidth((int)(0.15 * screenWidth));
		return myComboBox;
	}
	
	private ProductType getProductType(){
		return ProductTypeCatalog.getProductType(getApplicationPropertyBean().getProductType());
	}

	private Node createProcessedProductTable() {
		ProcessPointType processPointType = ClientMainFx.getInstance().getApplicationContext().getProcessPoint().getProcessPointType();
		String countHeader="Defect \nCount";
		if(processPointType.equals(ProcessPointType.QICSRepair)) countHeader = "Repair \nCount";
		
		if(ProductTypeUtil.isDieCast(getProductType())) {
			ColumnMappingList columnMappingList = ColumnMappingList.with("DC Number", "dcSerialNumber")
					.put("MC Number", "mcSerialNumber" )
					.put(countHeader, "count")
					.put("Associate", "createUser")
					.put("Create date","createTime");
			Double[] columnWidth = new Double[] {0.08,0.08,0.04,0.06,0.125}; 
			
			processedPrdTable = new ObjectTablePane<QiDefectResultDto>(columnMappingList,columnWidth);
		}else {
			ColumnMappingList columnMappingList = ColumnMappingList.with("Processed", "productId")
					.put(countHeader, "count")
					.put("Associate", "createUser")
					.put("Create date","createTime");
			Double[] columnWidth = new Double[] {0.12,0.05,0.07,0.145};
			
			processedPrdTable = new ObjectTablePane<QiDefectResultDto>(columnMappingList,columnWidth);
		}
		
		processedPrdTable.setStyle(getTableFontStyle());
		processedPrdTable.setMaxWidth((int)(0.41 * screenWidth));
		processedPrdTable.setMaxHeight((int)(0.43 * screenHeight));
		
		LoggedTableColumn<QiDefectResultDto, Integer> column = new LoggedTableColumn<QiDefectResultDto, Integer>();
		createSerialNumber(column);
		processedPrdTable.getTable().getColumns().add(0, column);
		processedPrdTable.getTable().getColumns().get(0).setText("#");
		processedPrdTable.getTable().getColumns().get(0).setResizable(true);
		processedPrdTable.getTable().getColumns().get(0).setMaxWidth((int)(0.021 * screenWidth));
		processedPrdTable.getTable().getColumns().get(0).setMinWidth(5);
		processedPrdTable.setEditable(false);
		processedPrdTable.setConstrainedResize(false);
		
		return processedPrdTable;
	}

	private Node getButtonPanel() {
		HBox  buttonContainer = new HBox();
		
		enterTrainingModeButton = createBtn(QiConstant.ENTER_TRAINING_MODE, getController());
		enterTrainingModeButton.setPrefWidth(screenWidth * 0.146);
		
		exitTrainingModeButton = createBtn(QiConstant.EXIT_TRAINING_MODE, getController());
		exitTrainingModeButton.setPrefWidth(screenWidth * 0.146);
		
		StackPane stack = new StackPane();
		stack.getChildren().addAll(exitTrainingModeButton,enterTrainingModeButton);
		
		commentButton = createBtn(QiConstant.COMMENT, getController());
		commentButton.setPrefWidth(screenWidth * 0.146);
		
		refreshCacheButton = createBtn(QiConstant.REFRESH_CACHE, getController());
		refreshCacheButton.setPrefWidth(screenWidth * 0.146);
		
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setSpacing(30);
		buttonContainer.getChildren().addAll(stack,commentButton,refreshCacheButton);
		return buttonContainer;
	}

	private Node createDefectDataGridpane() {
		GridPane defectDataContainer = createGrid();
		
		shiftTextField = createStationLabeledTextField("Shift", new Insets(0));
		shiftTextField.getLabel().setStyle(getLabelStyle());
		shiftTextField.getControl().setStyle(getTextStyle());
		shiftTextField.getControl().setMaxWidth(screenWidth * 0.087);
		defectDataContainer.add(shiftTextField, 0, 0);
		
		teamTextField = createStationLabeledTextField("Team", new Insets(0));
		teamTextField.getLabel().setStyle(getLabelStyle());
		teamTextField.getControl().setStyle(getTextStyle());
		teamTextField.getControl().setMaxWidth(screenWidth * 0.087);
		defectDataContainer.add(teamTextField, 1, 0);
		
		inspectedTextField = createStationLabeledTextField("Unique Inspected", new Insets(0));
		inspectedTextField.getLabel().setStyle(getLabelStyle());
		inspectedTextField.getControl().setStyle(getTextStyle());
		inspectedTextField.getControl().setMaxWidth(screenWidth * 0.087);
		defectDataContainer.add(inspectedTextField, 0, 1);

		//Jira-6369
		defectDataContainer.add(createTabbedPane(), 4, 0, 1, 7);
		
		directPassedTextField = createStationLabeledTextField("Direct Pass", new Insets(0));
		directPassedTextField.getLabel().setStyle(getLabelStyle());
		directPassedTextField.getControl().setStyle(getTextStyle());
		directPassedTextField.getControl().setMaxWidth(screenWidth * 0.087);
		defectDataContainer.add(directPassedTextField, 1, 1);
		
		notRepairedRejectionsTextField = createStationLabeledTextField("Products With Not Repaired Rejections", new Insets(0));
		notRepairedRejectionsTextField.getLabel().setStyle(getLabelStyle());
		notRepairedRejectionsTextField.getControl().setStyle(getTextStyle());
		notRepairedRejectionsTextField.getControl().setMaxWidth(screenWidth * 0.087);
		defectDataContainer.add(notRepairedRejectionsTextField, 0, 2);
		
		totalRejectionsTextField = createStationLabeledTextField("Total Rejections", new Insets(0));
		totalRejectionsTextField.getLabel().setStyle(getLabelStyle());
		totalRejectionsTextField.getControl().setStyle(getTextStyle());
		totalRejectionsTextField.getControl().setMaxWidth(screenWidth * 0.087);
		defectDataContainer.add(totalRejectionsTextField, 1, 2);
		
		prdsWithRejectionsTextField = createStationLabeledTextField("Products With Rejections", new Insets(0));
		prdsWithRejectionsTextField.getLabel().setStyle(getLabelStyle());
		prdsWithRejectionsTextField.getControl().setStyle(getTextStyle());
		prdsWithRejectionsTextField.getControl().setMaxWidth(screenWidth * 0.087);
		defectDataContainer.add(prdsWithRejectionsTextField, 0, 3);
		
		prdsScrappedTextField = createStationLabeledTextField("Products Scrapped", new Insets(0));
		prdsScrappedTextField.getLabel().setStyle(getLabelStyle());
		prdsScrappedTextField.getControl().setStyle(getTextStyle());
		prdsScrappedTextField.getControl().setMaxWidth(screenWidth * 0.087);
		defectDataContainer.add(prdsScrappedTextField, 1, 3);	
		
		stationLocationTextField = createStationLabeledTextField("Station Location", new Insets(0));
		stationLocationTextField.getLabel().setStyle(getLabelStyle());
		stationLocationTextField.getControl().setStyle(getTextStyle());
		stationLocationTextField.getControl().setMaxWidth(screenWidth * 0.087);
		defectDataContainer.add(stationLocationTextField, 0, 4);	
		
		extensionTextField = createStationLabeledTextField("Extension", new Insets(0));
		extensionTextField.getLabel().setStyle(getLabelStyle());
		extensionTextField.getControl().setStyle(getTextStyle());
		extensionTextField.getControl().setMaxWidth(screenWidth * 0.087);
		defectDataContainer.add(extensionTextField, 1, 4);	
		
		scanCountTextField = createStationLabeledTextField("Scan Count", new Insets(0));
		scanCountTextField.getLabel().setStyle(getLabelStyle());
		scanCountTextField.getControl().setStyle(getTextStyle());
		scanCountTextField.getControl().setMaxWidth(screenWidth * 0.087);
		defectDataContainer.add(scanCountTextField, 0, 5);

		defectDataContainer.add(getButtonPanel(), 0, 6, 4, 1);	
		
		TitledPane titledPane = new TitledPane("Station Counts", defectDataContainer);
		return titledPane;
	}
	
	/**Jira-6369
	 * This method is used to create static Tabs
		productSeqTab;
		processedTab;
	 * @return
	 */
	private TabPane createTabbedPane(){
		VBox processedProductContainer= new VBox();
		VBox rfidContainer= new VBox();
		productSeqTab = new Tab();
		processedTab = new Tab();
		tabbedPane = new TabPane();
		prodSeqPanel = new ProductSequencePanel(this, getModel());
		rfidContainer.getChildren().addAll(prodSeqPanel.getProductSequencePanel());
		processedProductContainer.getChildren().addAll(createProcessedProductTable());
		productSeqTab.setText("ProductSequence");
		productSeqTab.setStyle(getLabelStyleSmaller());
		productSeqTab.setId("productSeqId");
		productSeqTab.setClosable(false);
		productSeqTab.setContent(rfidContainer);
		processedTab.setText("ProcessedProduct");
		processedTab.setStyle(getLabelStyleSmaller());
		processedTab.setId("processedProdId");
		processedTab.setContent(processedProductContainer);
		processedTab.setClosable(false);
		if(getController().isShowProductSequenceTab())  {
	        tabbedPane.getTabs().addAll(productSeqTab, processedTab);			
		}
		else  {
			tabbedPane.getTabs().addAll(processedTab);
		}
        return tabbedPane;
	}


	private LoggedLabel createLoggedLabel(String id, String text,String cssClass) {
		LoggedLabel label = UiFactory.createLabel(id, text);
		label.setStyle(cssClass);
		return label;
	}
	
	private GridPane createGrid() {
		GridPane homeScreenScreenGridpane = new GridPane();
		homeScreenScreenGridpane.setHgap((int)(0.008 * screenWidth));
		homeScreenScreenGridpane.setVgap((int)(0.012 * screenWidth));
		return homeScreenScreenGridpane;
	}
	
	public  String  getTableFontStyle() {
		return String.format("-fx-font-size: %dpx;", (int)(0.009 * screenWidth));
	}
	
	public  String  getTableFontStyleSmall() {
		return String.format("-fx-font-size: %dpx;", (int)(0.007 * screenWidth));
	}
	
	public  String  getTableFontStyleSmallBold() {
		return String.format("-fx-font-weight: bold; -fx-font-size: %dpx;", (int)(0.007 * screenWidth));
	}
	
	public  String  getLabelStyle() {
		return String.format("-fx-font-weight: bold; fx-padding: 0 10px 0 0;-fx-font-size: %dpx;", (int)(0.013 * screenWidth));
	}
	
	public  String  getLabelStyleSmall() {
		return String.format("-fx-font-weight: bold; fx-padding: 0 4px 0 0;-fx-font-size: %dpx;", (int)(0.01 * screenWidth));
	}

	public  String  getLabelStyleSmaller() {
		return String.format("-fx-font-weight: bold; fx-padding: 0 2px 0 0;-fx-font-size: %dpx;", (int)(0.007 * screenWidth));
	}

	public  String  getTextStyle() {
		return String.format("-fx-alignment:center;-fx-font-weight: bold;-fx-font-size: %dpx;", (int)(0.013 * screenWidth));
	}
	
	private LabeledTextField createStationLabeledTextField(String labelName, Insets insets) {
		LabeledTextField field = new LabeledTextField(labelName, true, insets, true, false);
		TextFieldState.DISABLED.setState(field.getControl());
		return field;
	}
	
	public void onTabSelected() {
		
	}
	
	@Override
	public ViewId getViewId() {
		return null;
	}
	
	@Override
	public void reload() {
		getController().loadStationAndProductData();
	}
	
	@Override
	public void start() {
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

	public Tab getProductSeqTab() {
		return productSeqTab;
	}

	public void setProductSeqTab(Tab productSeqTab) {
		this.productSeqTab = productSeqTab;
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
	
	public LoggedButton getEnterTrainingModeButton() {
		return enterTrainingModeButton;
	}

	public void setEnterTrainingModeButton(LoggedButton entertrainingModeButton) {
		this.enterTrainingModeButton = entertrainingModeButton;
	}

	public LoggedButton getExitTrainingModeButton() {
		return exitTrainingModeButton;
	}

	public void setExitTrainingModeButton(LoggedButton exitTrainingModeButton) {
		this.exitTrainingModeButton = exitTrainingModeButton;
	}

	public LabeledTextField getScanCountTextField() {
		return scanCountTextField;
	}

	public void setScanCountTextField(LabeledTextField scanCountTextField) {
		this.scanCountTextField = scanCountTextField;
	}

	public ProductSequencePanel getProdSeqPanel() {
		return prodSeqPanel;
	}

	public void setProdSeqPanel(ProductSequencePanel prodSeqPanel) {
		this.prodSeqPanel = prodSeqPanel;
	}
}
