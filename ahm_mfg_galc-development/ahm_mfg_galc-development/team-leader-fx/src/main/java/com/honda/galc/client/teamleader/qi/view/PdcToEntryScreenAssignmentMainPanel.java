package com.honda.galc.client.teamleader.qi.view;

import java.net.URL;
import java.util.List;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.PdcToEntryScreenAssignmentController;
import com.honda.galc.client.teamleader.qi.model.PdcToEntryScreenAssignmentModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.dto.qi.QiRegionalPartDefectLocationDto;
import com.honda.galc.entity.qi.QiTextEntryMenu;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PartLocationCombinationMaintPanel</code> is the Panel class for Part Defect Combination to Entry screen.
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
 * <TD>25/07/2016</TD>
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
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
public class PdcToEntryScreenAssignmentMainPanel extends QiAbstractTabbedView<PdcToEntryScreenAssignmentModel, PdcToEntryScreenAssignmentController> {


	private ObjectTablePane<QiTextEntryMenu> textEntryMenuPane ;
	private ObjectTablePane<QiEntryScreenDto> entryScreenPanel;
	private ObjectTablePane<QiRegionalPartDefectLocationDto> availablePartDefectPanel;
	private ObjectTablePane<QiRegionalPartDefectLocationDto> assignedPartDefectPanel;

	private ComboBox<String> productTypeComboBox;
	private ComboBox<String> entryModelComboBox;
	private ComboBox<String> plantComboBox;
	private UpperCaseFieldBean availablePartDefectFilterTxt;
	private UpperCaseFieldBean assignedPartDefectFilterTxt;

	private LoggedButton saveBtn;
	private LoggedButton downArrowBtn;
	private LoggedButton upArrowBtn;
	private LoggedButton refreshBtn;
	private LoggedRadioButton allRadioBtn;
	private LoggedRadioButton usedRadioBtn;
	private LoggedRadioButton notUsedRadioBtn;
	private double width;
	private double height;

	private String siteName;


	public PdcToEntryScreenAssignmentMainPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
	}


	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		HBox box = new HBox();
		Rectangle2D primaryScreenBounds = getCurrentScreenBounds();
		width = primaryScreenBounds.getWidth();
		height = primaryScreenBounds.getHeight();
		box.getChildren().addAll(getLeftPanel(),getRightPanel());
		this.setTop(box);
	}


	/**
	 * Get Left panel
	 */
	private Node getLeftPanel() {
		VBox leftPanel = new VBox();
		leftPanel.prefHeight(100);
		leftPanel.setAlignment(Pos.TOP_LEFT);

		leftPanel.getChildren().add(getFilterPanel());
		leftPanel.getChildren().add(getEntryScreenTable());
		leftPanel.getChildren().add(getMenuTable());

		return leftPanel;
	}

	/**
	 * Get all filter on top panel
	 */
	private VBox getFilterPanel() {
		VBox vbox = new VBox();
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		LoggedLabel productTypeAsterisk = UiFactory.createLabel("label", "*");
		LoggedLabel plantAsterisk = UiFactory.createLabel("label", "*");
		asterisk.setStyle("-fx-text-fill: red");
		productTypeAsterisk.setStyle("-fx-text-fill: red");
		plantAsterisk.setStyle("-fx-text-fill: red");


		LoggedLabel siteLbl = UiFactory.createLabel("label","Site", (int)(0.01 * width));
		siteLbl.setPadding(new Insets(0,10,0,0));
		siteName = getDefaultSiteName();
		LoggedLabel siteValueFilterLabel = UiFactory.createLabel("label",siteName,(int)(0.01 * width));

		LoggedLabel productTypeLbl = UiFactory.createLabel("label","Product Type", (int)(0.01 * width));

		LoggedLabel entryModelLbl = UiFactory.createLabel("label","Entry Model", (int)(0.01 * width));

		LoggedLabel plantLbl = UiFactory.createLabel("label","Plant", (int)(0.01 * width));

		productTypeComboBox = new ComboBox<String>();
		productTypeComboBox.setId("productTypeComboBox");
		entryModelComboBox = new ComboBox<String>();
		entryModelComboBox.setId("entryModelComboBox");
		plantComboBox  = new ComboBox<String>();
		loadPlantCombobox();

		productTypeComboBox.setPrefHeight(27);
		productTypeComboBox.setMinHeight(27);
		productTypeComboBox.setMaxHeight(27);
		loadProductTypeCombobox();

		entryModelComboBox.setPrefHeight(27);
		entryModelComboBox.setMinHeight(27);
		entryModelComboBox.setMaxHeight(27);

		plantComboBox.setPrefHeight(27);
		plantComboBox.setMinHeight(27);
		plantComboBox.setMaxHeight(27);


		HBox siteFilterContainer = new HBox();
		siteFilterContainer.setPadding(new Insets(10,0,5,10));
		 
		HBox siteContainer = new HBox();
		HBox plantContainer = new HBox();
		HBox productTypeContainer = new HBox();
		HBox entryModelContainer = new HBox();
		
		HBox productTypeFilterContainer = new HBox();
		productTypeFilterContainer.setPadding(new Insets(0,0,5,0));

		
		siteContainer.getChildren().addAll(siteLbl,siteValueFilterLabel);
		HBox.setHgrow(siteContainer, Priority.ALWAYS);
		plantContainer.getChildren().addAll(plantLbl,plantAsterisk,plantComboBox);
		siteFilterContainer.getChildren().addAll(siteContainer,plantContainer);
		
		entryModelContainer.getChildren().addAll(entryModelLbl,asterisk,entryModelComboBox);
		productTypeContainer.getChildren().addAll(productTypeLbl,productTypeAsterisk,productTypeComboBox);
		HBox.setHgrow(productTypeContainer, Priority.ALWAYS);
		
		productTypeFilterContainer.getChildren().addAll(productTypeContainer,entryModelContainer);
		productTypeFilterContainer.setPrefWidth(width/2.05);
		siteFilterContainer.setPrefWidth(width/2.05);

		vbox.getChildren().addAll(siteFilterContainer,productTypeFilterContainer);
		vbox.setAlignment(Pos.TOP_RIGHT);
		return vbox;
	}

	private String getDefaultSiteName() {
		return getModel().getSiteName();
	}

	/**
	 * This method is to load the Plant Data
	 */
	public void loadPlantCombobox(){
		List<String> plantList = getModel().getPlantForSite(getSiteName());
		getPlantComboBox().setItems(FXCollections.observableArrayList(plantList));
		if(null!=plantList && !plantList.isEmpty() && plantList.size() ==1){
			getPlantComboBox().getSelectionModel().selectFirst();
		}
	}
	
	public void loadProductTypeCombobox(){
		List<String> productTypeList = getModel().getAllProductType();
		getProductTypeComboBox().setItems(FXCollections.observableArrayList(productTypeList));
		if(null!=productTypeList && !productTypeList.isEmpty() && productTypeList.size() ==1){
			getProductTypeComboBox().getSelectionModel().selectFirst();
		}
	}

	/**
	 * Entry Screen Table panel
	 */
	private Node getEntryScreenTable() {
		TitledPane entryScreenImagePane = new TitledPane();
		entryScreenImagePane.setContent(getEntryScreenTablePane(entryScreenImagePane));

		return entryScreenImagePane;
	}



	private Node getEntryScreenTablePane(TitledPane entryScreenImagePane) {

		VBox entryScreenTableContainer = new VBox();
		entryScreenImagePane.setPadding(new Insets(5,0,10,20));
		entryScreenImagePane.setText("Entry Screen/Image");
		entryScreenImagePane.setCollapsible(false);
		entryScreenImagePane.setPrefWidth(width/2.05);

		ColumnMappingList columnMappingList = ColumnMappingList.with("Screen", "entryScreen").put("Is Used", "IsUsedVersionData").put("Image", "imageName").put("Image or Text", "imageValue").put("Used", "used").put("Entry Department", "divisionId").put("Dept Name", "divisionName");
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] {0.1,0.05,0.05,0.05,0.03,0.04,0.1};  

		entryScreenPanel = new ObjectTablePane<QiEntryScreenDto>(columnMappingList,columnWidth);
		entryScreenPanel.setConstrainedResize(false);
		entryScreenPanel.setPrefHeight(190);

		LoggedTableColumn<QiEntryScreenDto, Integer> column = new LoggedTableColumn<QiEntryScreenDto, Integer>();
		createSerialNumber(column);
		entryScreenPanel.getTable().getColumns().add(0, column);
		entryScreenPanel.getTable().getColumns().get(0).setText("#");
		entryScreenPanel.getTable().getColumns().get(0).setResizable(true);
		
		entryScreenPanel.getTable().setPrefWidth(width/2.05);
		entryScreenPanel.getTable().getColumns().get(0).setPrefWidth(entryScreenPanel.getTable().getPrefWidth()*0.07);
		entryScreenPanel.setId("entryScreenPanel");
		entryScreenTableContainer.getChildren().addAll(entryScreenPanel,getRadioButtons());
		return entryScreenTableContainer;
	}


	/**
	 * Entry screen Table Radio 
	 */
	private HBox getRadioButtons() {

		HBox radioButtonContainer = new HBox();	
		final ToggleGroup radioGroup = new ToggleGroup();

		allRadioBtn = createRadioButton(QiConstant.ALL, radioGroup, true,getController());
		allRadioBtn.setPadding(new Insets(5));
		allRadioBtn.setSelected(true);
		allRadioBtn.setToggleGroup(radioGroup);


		usedRadioBtn = createRadioButton(QiConstant.USED, radioGroup, false,getController());
		usedRadioBtn.setPadding(new Insets(5));
		usedRadioBtn.setToggleGroup(radioGroup);

		notUsedRadioBtn = createRadioButton(QiConstant.NOT_USED, radioGroup, false,getController());
		notUsedRadioBtn.setPadding(new Insets(5));
		notUsedRadioBtn.setToggleGroup(radioGroup);

		radioButtonContainer.setAlignment(Pos.CENTER);
		radioButtonContainer.setSpacing(45);
		radioButtonContainer.getChildren().addAll(allRadioBtn,usedRadioBtn,notUsedRadioBtn);


		return radioButtonContainer;
	}
	/**
	 * Menu Table
	 */
	private Node getMenuTable() {

		VBox menuTableContainer = new VBox();

		TitledPane menuPane = new TitledPane();
		menuPane.setPadding(new Insets(5,0,10,20));
		menuPane.setText("Menu");
		menuPane.setCollapsible(false);
		menuPane.setPrefWidth(650);
		menuPane.setMaxWidth(650);


		menuPane.setContent(createTextEntryMenuTable());

		menuTableContainer.getChildren().add(menuPane);
		return menuPane;

	}



	private ObjectTablePane<QiTextEntryMenu> createTextEntryMenuTable() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Name", "id.textEntryMenu")
				.put("Is Used","IsUsedVersion").put("Menu Desc","textEntryMenuDesc");
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] {0.210,0.05,0.210};
		textEntryMenuPane = new ObjectTablePane<QiTextEntryMenu>(columnMappingList,columnWidth);
		textEntryMenuPane.setConstrainedResize(false);
		textEntryMenuPane.setPrefWidth(width/2.05);
		textEntryMenuPane.setPrefHeight(210);

		LoggedTableColumn<QiTextEntryMenu, Integer> column = new LoggedTableColumn<QiTextEntryMenu, Integer>();
		createSerialNumber(column);
		textEntryMenuPane.getTable().getColumns().add(0, column);
		textEntryMenuPane.getTable().getColumns().get(0).setText("#");
		textEntryMenuPane.getTable().getColumns().get(0).setResizable(true);
		textEntryMenuPane.getTable().getColumns().get(0).setMaxWidth(50);
		textEntryMenuPane.getTable().getColumns().get(0).setMinWidth(5);
		textEntryMenuPane.setId("textEntryMenuPane");
		textEntryMenuPane.setSelectionMode(SelectionMode.MULTIPLE);
		textEntryMenuPane.setDisable(true);
		
		return textEntryMenuPane;
	}


	/**
	 * Right Panel
	 */
	private Node getRightPanel() {
		VBox rightPanel = new VBox();
		rightPanel.prefHeight(100);
		rightPanel.prefWidth(100);
		rightPanel.setAlignment(Pos.TOP_RIGHT);
		refreshBtn = createBtn(QiConstant.REFRESH,getController());	
		rightPanel.getChildren().add(refreshBtn);
		rightPanel.getChildren().add(getPartDefectDescriptionTable());
		rightPanel.getChildren().add(getArrowPanel());
		rightPanel.getChildren().add(getAssignedPartDefectTable());
		rightPanel.getChildren().add(getSaveButtonPanel());

		return rightPanel;
	}

	/**
	 * Part Description Table Panel
	 */
	private Node getPartDefectDescriptionTable() {
		VBox partDefectDescContainer = new VBox();

		TitledPane partDescPane = new TitledPane();
		partDescPane.setPadding(new Insets(5,0,5,5));
		partDescPane.setText("Part Defect Description");
		partDescPane.setCollapsible(false);
		partDescPane.setPrefWidth(650);
		partDescPane.setMaxWidth(650);

		ColumnMappingList columnMappingList = ColumnMappingList.with("Part Name", "inspectionPartName").put("Full QICS Part Defect Description", "defectTypeName");
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] { 0.13,0.301}; 

		availablePartDefectPanel = new ObjectTablePane<QiRegionalPartDefectLocationDto>(columnMappingList,columnWidth);
		partDefectDescContainer.getChildren().addAll(getFilterContainer(),availablePartDefectPanel);
		availablePartDefectPanel.setSelectionMode(SelectionMode.MULTIPLE);
		availablePartDefectPanel.setPrefHeight(height*0.20);

		LoggedTableColumn<QiRegionalPartDefectLocationDto, Integer> column = new LoggedTableColumn<QiRegionalPartDefectLocationDto, Integer>();
		createSerialNumber(column);
		availablePartDefectPanel.getTable().getColumns().add(0, column);
		availablePartDefectPanel.getTable().getColumns().get(0).setText("#");
		availablePartDefectPanel.getTable().getColumns().get(0).setResizable(true);
		availablePartDefectPanel.getTable().getColumns().get(0).setMaxWidth(50);
		availablePartDefectPanel.getTable().getColumns().get(0).setMinWidth(5);
		availablePartDefectPanel.setPrefWidth(210);
		availablePartDefectPanel.setDisable(true);
		availablePartDefectPanel.setId("partDefectDescPanel");
		
		partDescPane.setContent(partDefectDescContainer);

		return partDescPane;
	}


	/**
	 * Right Panel Filter container
	 */
	private Node getFilterContainer() {
		LoggedLabel partDefectFilterLbl = UiFactory.createLabel("label", "Filter", (int)(0.01 * width));
		partDefectFilterLbl.getStyleClass().add("display-label");
		HBox filterContainer = new HBox();
		availablePartDefectFilterTxt = createFilterTextField("filterTextFieldForPDC", 25, getController());
		availablePartDefectFilterTxt.setDisable(true);
		availablePartDefectFilterTxt.setId("PartDefectFilter");
		filterContainer.getChildren().addAll(partDefectFilterLbl, availablePartDefectFilterTxt);
		filterContainer.setPadding(new Insets(0,0,5,0));
		filterContainer.setAlignment(Pos.BASELINE_RIGHT);

		return filterContainer;
	}

	/**
	 * Assigned Part Defect Table
	 */
	private Node getAssignedPartDefectTable() {
		
		VBox assignedPartDefectDescContainer = new VBox();

		TitledPane assignedDefectPane = new TitledPane();

		assignedDefectPane.setPadding(new Insets(5,0,5,5));
		assignedDefectPane.setText("Assigned Part Defect");
		assignedDefectPane.setCollapsible(false);
		assignedDefectPane.setPrefWidth(670);
		assignedDefectPane.setMaxWidth(670);


		ColumnMappingList columnMappingList = ColumnMappingList.with("Part Name", "inspectionPartName").put("Full QICS Part Defect Description", "defectTypeName");
		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] {0.15,0.295}; 

		assignedPartDefectPanel = new ObjectTablePane<QiRegionalPartDefectLocationDto>(columnMappingList,columnWidth);
		assignedPartDefectPanel.setPrefHeight(height*0.20);
		assignedPartDefectDescContainer.getChildren().addAll(getAssignedFilterContainer(),assignedPartDefectPanel);
		assignedPartDefectPanel.setConstrainedResize(false);
		assignedPartDefectPanel.setSelectionMode(SelectionMode.MULTIPLE);

		LoggedTableColumn<QiRegionalPartDefectLocationDto, Integer> column = new LoggedTableColumn<QiRegionalPartDefectLocationDto, Integer>();
		createSerialNumber(column);
		assignedPartDefectPanel.getTable().getColumns().add(0, column);
		assignedPartDefectPanel.getTable().getColumns().get(0).setText("#");
		assignedPartDefectPanel.getTable().getColumns().get(0).setResizable(true);
		assignedPartDefectPanel.getTable().getColumns().get(0).setMaxWidth(50);
		assignedPartDefectPanel.getTable().getColumns().get(0).setMinWidth(5);
		assignedPartDefectPanel.setPrefWidth(210);

		assignedPartDefectPanel.setDisable(true);
		assignedPartDefectPanel.setId("assignedPartdefectPanel");
		assignedDefectPane.setContent(assignedPartDefectDescContainer);

		return assignedDefectPane;
	}
	
	private Node getAssignedFilterContainer() {
		LoggedLabel partDefectFilterLbl = UiFactory.createLabel("label", "Filter", (int)(0.01 * width));
		partDefectFilterLbl.getStyleClass().add("display-label");
		HBox filterContainer = new HBox();
		assignedPartDefectFilterTxt = createFilterTextField("filterTextFieldForPDC", 25, getController());
		assignedPartDefectFilterTxt.setDisable(true);
		assignedPartDefectFilterTxt.setId("AssignedPartDefectFilter");
		filterContainer.getChildren().addAll(partDefectFilterLbl, assignedPartDefectFilterTxt);
		filterContainer.setPadding(new Insets(0,0,5,0));
		filterContainer.setAlignment(Pos.BASELINE_RIGHT);
		return filterContainer;
	}

	/**
	 * Reload Button
	 */
	private HBox getSaveButtonPanel() {
		HBox reloadWholePageContainer = new HBox();

		saveBtn = createBtn(QiConstant.SAVE,getController());
		saveBtn.setDisable(true);
		reloadWholePageContainer.getChildren().add(saveBtn);
		reloadWholePageContainer.setAlignment(Pos.BASELINE_RIGHT);
		if(!isFullAccess()){
			saveBtn.setDisable(true);
		}
		return reloadWholePageContainer;
	}


	private Node getArrowPanel() {

		HBox buttonContainer = new HBox();
		HBox arrowUpContainer = new HBox();
		HBox arrowDownContainer = new HBox();

		URL downArrowUrl = getClass().getResource("/resource/com/honda/galc/client/images/qi/down.png");
		downArrowBtn = createBtn(downArrowUrl,"Click to push selected rows down", "downArrowBtn");
		arrowDownContainer.getChildren().addAll(downArrowBtn);

		URL upArrowUrl = getClass().getResource("/resource/com/honda/galc/client/images/qi/up.png");
		upArrowBtn = createBtn(upArrowUrl,"Click to push selected rows up", "upArrowBtn");
		arrowUpContainer.getChildren().addAll(upArrowBtn);
		arrowDownContainer.setPadding(new Insets(0,50,0,0));
		buttonContainer.getChildren().addAll(arrowDownContainer,arrowUpContainer);
		buttonContainer.setAlignment(Pos.CENTER);
		return buttonContainer;
	}
	
	private LoggedButton createBtn(URL url, String text, String id)
	{
		LoggedButton btn = UiFactory.createButton("");
		btn.setId(id);
		btn.setDisable(true);
		btn.setOnAction(getController());
		ImageView imageView = new ImageView();
		Image image = new Image(url.toString());
		imageView.setImage(image);
		imageView.setFitWidth(25);
		imageView.setFitHeight(25);
		btn.setGraphic(imageView);
		Tooltip tooltip = new Tooltip();
		tooltip.setText(text);
		btn.setTooltip(tooltip);
		btn.getStyleClass().add("drawing-tools");
		return btn;
	}


	public void onTabSelected() {
		getController().refreshBtnAction();
		
	}


	@Override
	public ViewId getViewId() {
		return null;
	}


	@Override
	public void reload() {

	}


	public void reloadAvailablePdc(String filter) {

		if(null!=entryModelComboBox.getSelectionModel().getSelectedItem()){

			List<QiRegionalPartDefectLocationDto> partDefectList = getModel().getPartDefectDetails(filter, getModel().getProductKind(),getEntryScreenPanel().getSelectedItem());

			for (QiRegionalPartDefectLocationDto qiReginalPartDefectLoactionDto : getAssignedPartdefectPanel().getTable().getItems()) {
				if (partDefectList.contains(qiReginalPartDefectLoactionDto)) {
					partDefectList.remove(qiReginalPartDefectLoactionDto);
				}
			}

			availablePartDefectPanel.setData(partDefectList); 
		}

	}


	@Override
	public void start() {
	}


	public String getScreenName() {
		return "PDC to Entry Screen";
	}


	public ObjectTablePane<QiEntryScreenDto> getEntryScreenPanel() {
		return entryScreenPanel;
	}



	public ComboBox<String> getEntryModelComboBox() {
		return entryModelComboBox;
	}


	public void setEntryModelComboBox(ComboBox<String> entryModelComboBox) {
		this.entryModelComboBox = entryModelComboBox;
	}


	public UpperCaseFieldBean getAvailablePartDefectFilterTxt() {
		return availablePartDefectFilterTxt;
	}


	public void setAvailablePartDefectFilterTxt(UpperCaseFieldBean availablePartDefectFilterTxt) {
		this.availablePartDefectFilterTxt = availablePartDefectFilterTxt;
	}


	public ObjectTablePane<QiRegionalPartDefectLocationDto> getAvailablePartDefectPanel() {
		return availablePartDefectPanel;
	}


	public void setAvailablePartDefectPanel(
			ObjectTablePane<QiRegionalPartDefectLocationDto> availablePartDefectPanel) {
		this.availablePartDefectPanel = availablePartDefectPanel;
	}


	public ObjectTablePane<QiTextEntryMenu> getTextEntryMenuPane() {
		return textEntryMenuPane;
	}


	public void setTextEntryMenuPane(
			ObjectTablePane<QiTextEntryMenu> textEntryMenuPane) {
		this.textEntryMenuPane = textEntryMenuPane;
	}


	public ObjectTablePane<QiRegionalPartDefectLocationDto> getAssignedPartdefectPanel() {
		return assignedPartDefectPanel;
	}



	public void reloadTextEntryMenu(
			ObjectTablePane<QiTextEntryMenu> textEntryMenuPane2) {
		getTextEntryMenuPane().setData(getModel().getMenuDetailsByEntryScreen(getEntryScreenPanel().getSelectedItem()));

	}


	public ComboBox<String> getProductTypeComboBox() {
		return productTypeComboBox;
	}


	public void setProductTypeComboBox(ComboBox<String> productTypeComboBox) {
		this.productTypeComboBox = productTypeComboBox;
	}


	public LoggedButton getDownArrowBtn() {
		return downArrowBtn;
	}


	public void setDownArrowBtn(LoggedButton downArrowBtn) {
		this.downArrowBtn = downArrowBtn;
	}


	public LoggedButton getUpArrowBtn() {
		return upArrowBtn;
	}


	public void setUpArrowBtn(LoggedButton upArrowBtn) {
		this.upArrowBtn = upArrowBtn;
	}


	public LoggedRadioButton getAllRadioBtn() {
		return allRadioBtn;
	}


	public void setAllRadioBtn(LoggedRadioButton allRadioBtn) {
		this.allRadioBtn = allRadioBtn;
	}


	public LoggedRadioButton getUsedRadioBtn() {
		return usedRadioBtn;
	}


	public void setUsedRadioBtn(LoggedRadioButton usedRadioBtn) {
		this.usedRadioBtn = usedRadioBtn;
	}


	public LoggedRadioButton getNotUsedRadioBtn() {
		return notUsedRadioBtn;
	}


	public void setNotUsedRadioBtn(LoggedRadioButton notUsedRadioBtn) {
		this.notUsedRadioBtn = notUsedRadioBtn;
	}


	public String getSiteName() {
		return siteName;
	}


	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}


	public ComboBox<String> getPlantComboBox() {
		return plantComboBox;
	}


	public void setPlantComboBox(ComboBox<String> plantComboBox) {
		this.plantComboBox = plantComboBox;
	}


	/**
	 * @return the saveBtn
	 */
	public LoggedButton getSaveBtn() {
		return saveBtn;
	}


	public UpperCaseFieldBean getAssignedPartDefectFilterTxt() {
		return assignedPartDefectFilterTxt;
	}


	public LoggedButton getBtnRefresh() {
		return refreshBtn;
	}
}
