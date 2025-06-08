package com.honda.galc.client.teamleader.qi.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.qi.controller.CopyPartDefectCombinationDialogController;
import com.honda.galc.client.teamleader.qi.model.PdcToEntryScreenAssignmentModel;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.StyleUtil;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.dto.qi.QiRegionalPartDefectLocationDto;
import com.honda.galc.entity.qi.QiTextEntryMenu;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * 
 * <h3>CopyPartDefectCombinationDialog Class description</h3>
 * <p>
 * CopyPartDefectCombinationDialog is used to display Copy Part Defect Combination dialog screen 
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
 * @author L&TInfotech<br>
 *        Apr 12, 2017
 * 
 */

public class CopyPartDefectCombinationDialog extends QiFxDialog<PdcToEntryScreenAssignmentModel> {

	private String title;

	private LoggedButton okButton;
	private LoggedButton cancelButton;
	private CopyPartDefectCombinationDialogController controller;
	
	private ObjectTablePane<QiEntryScreenDto> entryScreenPanel;
	private ObjectTablePane<QiRegionalPartDefectLocationDto> assignedPartdefectPanel;
	private ObjectTablePane<QiTextEntryMenu> textEntryMenuPane;
	private PdcToEntryScreenAssignmentMainPanel mainPanel = null;
	
	private double screenWidth;
	
	public enum CopyMode {COPY_SCREEN, COPY_MENU, COPY_PDC, MOVE_PDC};
	private final CopyMode screenType;
	private ComboBox<String> productTypeComboBox;
	private ComboBox<String> entryModelComboBox;
	private ComboBox<String> plantComboBox;
	private CheckBox copyModeChkBox;

	private String siteName;
	QiEntryScreenDto sourceDto;
	List<QiTextEntryMenu> sourceMenuList;
	String filterText = "";

	/**
	 * @param title 
	 * @param model PdcToEntryScreenAssignmentModel
	 * @param applicationId process-point
	 * @param entryScreenPanel list of entry screens available to copy (destination)
	 * @param assignedPartdefectPanel part defects associated with source entry screen or PDC selection
	 * @param entryScreenType which menu was invoked, copyFrom, copy-menu or copyPDC
	 * @param entryScreenDto source entry screen details
	 * @param menu which menu was selected in case of text entry screen/copyPDC
	 * 
	 * called by copyFrom (for image entry screens) and copyPDC (for text entry screens)
	 */
	public CopyPartDefectCombinationDialog(String title, PdcToEntryScreenAssignmentModel model, String applicationId,
			List<QiEntryScreenDto> entryScreenPanel, List<QiRegionalPartDefectLocationDto> srcAssignedPartdefectPanel,
			CopyMode entryScreenType, QiEntryScreenDto entryScreenDto, QiTextEntryMenu menu, String pdcFilterText) {
		super(title, ClientMainFx.getInstance().getStage(applicationId),model);
		this.title = title;
		this.controller = new CopyPartDefectCombinationDialogController(model,this,entryScreenType);
		getController().setEntryScreenDto(entryScreenDto);
		if(menu != null)  { // set menu label text in source label panel
			List<QiTextEntryMenu> menuList = new ArrayList<QiTextEntryMenu>();
			menuList.add(menu);
			setSourceMenuList(menuList);
			setFilterText(pdcFilterText);
			getController().setSourceMenuList(menuList);
			getController().setTextEntryMenuList(menuList);
		}
		this.screenType = entryScreenType;
		initComponents();
		getEntryScreenPanel().setData(entryScreenPanel);
		getAssignedPartdefectPanel().setData(srcAssignedPartdefectPanel);
		controller.initListeners();
		if (entryScreenType == CopyMode.MOVE_PDC)  {
			getPlantComboBox().getItems().clear();
			List<String> newPlantList = Arrays.asList(entryScreenDto.getPlantName());
			getPlantComboBox().setItems(FXCollections.observableArrayList(newPlantList));
			getPlantComboBox().getSelectionModel().selectFirst();
			
			getProductTypeComboBox().getItems().clear();
			List<String> productTypeList = Arrays.asList(entryScreenDto.getProductType());
			getProductTypeComboBox().setItems(FXCollections.observableArrayList(productTypeList));
			getProductTypeComboBox().getSelectionModel().selectFirst();
			
			getEntryModelComboBox().getItems().clear();
			List<String> newEntryModelList = Arrays.asList(entryScreenDto.getEntryModel());
			getEntryModelComboBox().setItems(FXCollections.observableArrayList(newEntryModelList));
			getEntryModelComboBox().getSelectionModel().selectFirst();
			
		}
		else  {
			selectDefaultFilters();
		}
	}
	
	/**
	 * @param title 
	 * @param model PdcToEntryScreenAssignmentModel
	 * @param applicationId process-point
	 * @param entryScreenType which menu was invoked, copyFrom, copy-menu or copyPDC
	 * @param entryScreenDto source entry screen details
	 * @param menuList which menu items were selected in case of text entry screen/copyMenu
	 * 
	 * called when Copy_Menu is invoked
	 */
	public CopyPartDefectCombinationDialog(String title, PdcToEntryScreenAssignmentModel model, String applicationId,
			CopyMode entryScreenType, QiEntryScreenDto entryScreenDto, List<QiTextEntryMenu> menuList) {
		super(title, ClientMainFx.getInstance().getStage(applicationId),model);
		this.title = title;
		this.controller = new CopyPartDefectCombinationDialogController(model,this,entryScreenType);
		getController().setEntryScreenDto(entryScreenDto);
		setSourceMenuList(menuList);
		getController().setSourceMenuList(menuList);
		getController().setTextEntryMenuList(menuList);
		this.screenType = entryScreenType;
		initComponents();
		controller.initListeners();
		selectDefaultFilters();
	}

	private void initComponents(){
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		VBox box = new VBox();
		QiEntryScreenDto entryScreenDto = getController().getEntryScreenDto();
		screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
		if (screenType.equals(CopyPartDefectCombinationDialog.CopyMode.COPY_PDC)
				|| screenType.equals(CopyPartDefectCombinationDialog.CopyMode.MOVE_PDC)) {
			HBox topContainer = new HBox();
			topContainer.getChildren().addAll(getEntryScreenTablePane(), getMenuTable());
			box.getChildren().addAll(getSourcePanel(entryScreenDto),getAssignedPartDefectTable(),getFilterPanel(),topContainer, getButtonContainer());
		}
		else{
			
			box.getChildren().addAll(getSourcePanel(entryScreenDto),getAssignedPartDefectTable(), getFilterPanel(), getEntryScreenTablePane(), getButtonContainer());
		}
		
		((BorderPane) this.getScene().getRoot()).setCenter(box);	
	}

	/**
	 * Get all filter on top panel
	 */
	private VBox getSourcePanel(QiEntryScreenDto entryScreenDto) {
		VBox vbox = new VBox();
		siteName = getDefaultSiteName();		
		LoggedLabel sourceLabel = UiFactory.createLabel("label_src","Source",(int)(0.01 * screenWidth));
		sourceLabel.getStyleClass().add("display-label-blue");
		sourceLabel.setPadding(new Insets(0,50,0,0));
		LoggedLabel siteLbl = UiFactory.createLabel("label","Site: " + siteName, (int)(0.01 * screenWidth));
		LoggedLabel productTypeLbl = UiFactory.createLabel("label","Product Type: " + entryScreenDto.getProductType() , (int)(0.01 * screenWidth));
		LoggedLabel entryScreenLbl = UiFactory.createLabel("label","Entry Screen: " + entryScreenDto.getEntryScreen() , (int)(0.01 * screenWidth));
		LoggedLabel entryModelLbl = UiFactory.createLabel("label","Entry Model: " + entryScreenDto.getEntryModel(), (int)(0.01 * screenWidth));
		LoggedLabel plantLbl = UiFactory.createLabel("label","Plant: " + entryScreenDto.getPlantName(), (int)(0.01 * screenWidth));
		LoggedLabel filterLbl = null;
		if(getScreenType() == CopyMode.COPY_PDC && !StringUtils.isEmpty(getFilterText()))  {
			filterLbl = UiFactory.createLabel("label","Filter: " + getFilterText(), (int)(0.0075 * screenWidth));
		}
		HBox firstRow = new HBox();
		firstRow.setPadding(new Insets(0,0,5,10));
		HBox secondRow = new HBox();
		secondRow.setPadding(new Insets(0,0,5,10));
		 
		HBox siteContainer = new HBox();
		HBox plantContainer = new HBox();
		HBox productTypeContainer = new HBox();
		HBox entryModelContainer = new HBox();
		HBox entryScreenContainer = new HBox();
		HBox menuLabelContainer = new HBox();
		HBox filterLabelContainer = new HBox();
	
		HBox sourceBox = new HBox();
		sourceBox.getChildren().add(sourceLabel);
		sourceBox.setAlignment(Pos.TOP_LEFT);
		
		siteContainer.getChildren().addAll(siteLbl);
		HBox.setHgrow(siteContainer, Priority.ALWAYS);
		plantContainer.getChildren().addAll(plantLbl);
		plantContainer.setPadding(new Insets(0,10,0, 0));
		plantContainer.setAlignment(Pos.TOP_LEFT);
		productTypeContainer.getChildren().addAll(productTypeLbl);
		firstRow.getChildren().addAll(sourceBox,siteContainer,productTypeContainer,plantContainer);
		firstRow.setPrefWidth(screenWidth/2.05);
		
		entryModelContainer.getChildren().addAll(entryModelLbl);
		entryModelContainer.setAlignment(Pos.TOP_LEFT);
		entryScreenContainer.getChildren().addAll(entryScreenLbl);
		entryScreenContainer.setPadding(new Insets(0,10,0, 50));
		entryScreenContainer.setAlignment(Pos.TOP_RIGHT);
		HBox.setHgrow(productTypeContainer, Priority.ALWAYS);
		
		if(isTextScreen() && sourceMenuList != null && !sourceMenuList.isEmpty())  {
			LoggedLabel menuLbl = UiFactory.createLabel("label","Menu: " + getMenuString(), (int)(0.0075 * screenWidth));
			menuLabelContainer.getChildren().add(menuLbl);
		}
		secondRow.getChildren().addAll(entryModelContainer, entryScreenContainer, menuLabelContainer);
		if(getScreenType() == CopyMode.COPY_PDC && filterLbl != null)  {
			filterLabelContainer.getChildren().add(filterLbl);
			filterLabelContainer.setPadding(new Insets(0,0,0,10));
			secondRow.getChildren().add(filterLabelContainer);
		}
		secondRow.setPrefWidth(screenWidth/2.05);

		vbox.getChildren().addAll(firstRow,secondRow);
		vbox.setAlignment(Pos.TOP_RIGHT);
		return vbox;
	}
	

	private String getMenuString()  {
		StringBuilder sb = new StringBuilder();
		if(sourceMenuList == null || sourceMenuList.isEmpty())  return "";
		for(int i = 0; i < sourceMenuList.size(); i++)  {
			if(sourceMenuList.get(i) != null)  {
				sb.append(sourceMenuList.get(i).getId().getTextEntryMenu());
				if(i < (sourceMenuList.size() - 1))  {
					sb.append(',');
				}
			}
		}
		return sb.toString();
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


		LoggedLabel siteLbl = UiFactory.createLabel("label","Site", (int)(0.01 * screenWidth));
		siteLbl.setPadding(new Insets(0,10,0,0));
		siteName = getDefaultSiteName();
		
		LoggedLabel siteValueFilterLabel = UiFactory.createLabel("label",siteName,(int)(0.01 * screenWidth));
		LoggedLabel targetLabel = UiFactory.createLabel("label_tgt","Target",(int)(0.01 * screenWidth));
		targetLabel.setPadding(new Insets(0,50,0,0));
		targetLabel.getStyleClass().add("display-label-blue");
		LoggedLabel productTypeLbl = UiFactory.createLabel("label","Product Type", (int)(0.01 * screenWidth));
		LoggedLabel entryModelLbl = UiFactory.createLabel("label","Entry Model", (int)(0.01 * screenWidth));
		LoggedLabel plantLbl = UiFactory.createLabel("label","Plant", (int)(0.01 * screenWidth));

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
		loadEntryModelComboBox();
		
		plantComboBox.setPrefHeight(27);
		plantComboBox.setMinHeight(27);
		plantComboBox.setMaxHeight(27);


		HBox siteFilterContainer = new HBox();
		siteFilterContainer.setPadding(new Insets(0,0,5,10));
		 
		HBox siteContainer = new HBox();
		HBox plantContainer = new HBox();
		HBox productTypeContainer = new HBox();
		HBox entryModelContainer = new HBox();
		
		HBox productTypeFilterContainer = new HBox();
		productTypeFilterContainer.setPadding(new Insets(0,0,5,10));

		HBox targetBox = new HBox();
		targetBox.getChildren().add(targetLabel);
		targetBox.setAlignment(Pos.TOP_LEFT);
		
		siteContainer.getChildren().addAll(siteLbl,siteValueFilterLabel);
		HBox.setHgrow(siteContainer, Priority.ALWAYS);
		plantContainer.getChildren().addAll(plantLbl,plantAsterisk,plantComboBox);
		plantContainer.setPadding(new Insets(0,10,0, 0));
		siteFilterContainer.getChildren().addAll(targetBox,siteContainer,plantContainer);
		
		entryModelContainer.getChildren().addAll(entryModelLbl,asterisk,entryModelComboBox);
		entryModelContainer.setPadding(new Insets(0,10,0, 0));
		productTypeContainer.getChildren().addAll(productTypeLbl,productTypeAsterisk,productTypeComboBox);
		HBox.setHgrow(productTypeContainer, Priority.ALWAYS);
		
		productTypeFilterContainer.getChildren().addAll(productTypeContainer,entryModelContainer);
		productTypeFilterContainer.setPrefWidth(screenWidth/2.05);
		siteFilterContainer.setPrefWidth(screenWidth/2.05);

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
	}
	
	public void loadProductTypeCombobox(){
		List<String> productTypeList = getModel().getAllProductType();
		getProductTypeComboBox().setItems(FXCollections.observableArrayList(productTypeList));
	}

	/**
	 * This method is to load the Entry Model Data
	 */
	public void loadEntryModelComboBox(){
		getEntryModelComboBox().getItems().clear();
		List<String> entryModelList = getModel().getEntryModelForProductType(StringUtils.trimToEmpty(getProductTypeComboBox().getValue()));
		getEntryModelComboBox().getItems().addAll(FXCollections.observableArrayList(entryModelList));
	}
	
	private void selectDefaultFilters()  {
		List<String> plantList = getPlantComboBox().getItems();
		if(null != plantList && !plantList.isEmpty())  {
			if(plantList.size() == 1)  {
				getPlantComboBox().getSelectionModel().selectFirst();
			}
			else  {
				getPlantComboBox().getSelectionModel().select(getController().getEntryScreenDto().getPlantName());
			}
		}
		List<String> productTypeList = getProductTypeComboBox().getItems();
		if(null != productTypeList && !productTypeList.isEmpty())  {
			if(productTypeList.size() == 1)  {
				getProductTypeComboBox().getSelectionModel().selectFirst();
			}
			else  {
				getProductTypeComboBox().getSelectionModel().select(getController().getEntryScreenDto().getProductType());
			}
		}
		List<String> entryModelList = getEntryModelComboBox().getItems();
		if(entryModelList.size() == 1)  {
			getEntryModelComboBox().getSelectionModel().selectFirst();
		}
		else  {
			getEntryModelComboBox().getSelectionModel().select(getController().getEntryScreenDto().getEntryModel());
		}
	}
	
	private Node getButtonContainer() {
		okButton = createBtn(QiConstant.OK, getController());
		cancelButton =createBtn(QiConstant.CANCEL, getController());

		HBox btnContainer = new HBox();
		btnContainer.setSpacing(6);
		btnContainer.setPadding(new Insets(0,0,20,0));
		btnContainer.setAlignment(Pos.CENTER);
		btnContainer.getChildren().addAll(okButton, cancelButton);
		return btnContainer;
	}

	/**
	 * This method is used to create checkbox
	 * @param text
	 * @param handler
	 * @param style
	 * @return
	 */
	public CheckBox createCheckBox(String text,EventHandler<ActionEvent> handler,String style)
	{
		CheckBox checkBox = new CheckBox(text);
		checkBox.setOnAction(handler);
		checkBox.setWrapText(true);
		checkBox.setPadding(new Insets(10));
		checkBox.getStyleClass().add(style);
		return checkBox;
	}
	
	private Node getEntryScreenTablePane() {
		TitledPane entryScreenImagePane  = new TitledPane();
		entryScreenImagePane.setPadding(new Insets(10));
		entryScreenImagePane.setText("Entry Screen List");
		entryScreenImagePane.setCollapsible(false);
		entryScreenImagePane.setPrefWidth(screenWidth/2.05);

		ColumnMappingList columnMappingList;
		Double[] columnWidth;
		
		if(screenType.equals(QiConstant.IMAGE)){
			columnMappingList = ColumnMappingList.with("Screen", "entryScreen").put("Model", "entryModel").put("Is Used", "isUsedVersionData").put("Image", "imageName").put("Used", "used").put("Entry Department", "divisionId");
			columnWidth = new Double[] {0.15,0.10,0.05,0.1,0.1,0.1}; 
		}
		else{
			columnMappingList = ColumnMappingList.with("Screen", "entryScreen").put("Model", "entryModel").put("Is Used", "isUsedVersionData").put("Used", "used").put("Entry Department", "divisionId");
			columnWidth = new Double[] {0.15,0.10,0.05,0.1,0.1}; 
		}

		entryScreenPanel = new ObjectTablePane<QiEntryScreenDto>(columnMappingList,columnWidth);
		entryScreenPanel.setConstrainedResize(false);
		entryScreenPanel.setPrefHeight(190);

		LoggedTableColumn<QiEntryScreenDto, Integer> column = new LoggedTableColumn<QiEntryScreenDto, Integer>();
		createSerialNumber(column);
		entryScreenPanel.getTable().getColumns().add(0, column);
		entryScreenPanel.getTable().getColumns().get(0).setText("#");
		entryScreenPanel.getTable().getColumns().get(0).setResizable(true);
		entryScreenPanel.getTable().getColumns().get(0).setMaxWidth(100);
		entryScreenPanel.getTable().getColumns().get(0).setMinWidth(5);

		entryScreenImagePane.setContent(entryScreenPanel);
		return entryScreenImagePane;
	}

	
	/**
	 * Assigned Part Defect Table
	 */
	private Node getAssignedPartDefectTable() {

		TitledPane assignedDefectPane = new TitledPane();

		assignedDefectPane.setPadding(new Insets(10));
		assignedDefectPane.setText("Assigned Part Defect");
		assignedDefectPane.setCollapsible(false);
		assignedDefectPane.setPrefWidth(screenWidth/2.05);


		ColumnMappingList columnMappingList = ColumnMappingList.with("Part Name", "inspectionPartName").put("Full QICS Part Defect Description", "defectTypeName");
		//Fix : Resizing a column shrinks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] {0.2,0.7}; 

		assignedPartdefectPanel = new ObjectTablePane<QiRegionalPartDefectLocationDto>(columnMappingList,columnWidth);
		assignedPartdefectPanel.setPrefHeight(190);
		assignedPartdefectPanel.setConstrainedResize(false);
		assignedPartdefectPanel.setSelectionMode(SelectionMode.MULTIPLE);

		LoggedTableColumn<QiRegionalPartDefectLocationDto, Integer> column = new LoggedTableColumn<QiRegionalPartDefectLocationDto, Integer>();
		createSerialNumber(column);
		assignedPartdefectPanel.getTable().getColumns().add(0, column);
		assignedPartdefectPanel.getTable().getColumns().get(0).setText("#");
		assignedPartdefectPanel.getTable().getColumns().get(0).setResizable(true);
		assignedPartdefectPanel.getTable().getColumns().get(0).setMaxWidth(50);
		assignedPartdefectPanel.getTable().getColumns().get(0).setMinWidth(5);
		assignedPartdefectPanel.setPrefWidth(screenWidth/2.05);

		assignedDefectPane.setContent(assignedPartdefectPanel);

		return assignedDefectPane;
	}

	
	/**
	 * Menu Table
	 */
	private Node getMenuTable() {
		
		TitledPane menuPane = new TitledPane();
		menuPane.setPadding(new Insets(10));
		menuPane.setText("Menu");
		menuPane.setCollapsible(false);
		menuPane.setPrefWidth(screenWidth/2.05);

		menuPane.setContent(createTextEntryMenuTable());

		return menuPane;

	}



	private ObjectTablePane<QiTextEntryMenu> createTextEntryMenuTable() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Name", "id.textEntryMenu").put("Menu Desc","textEntryMenuDesc");
		Double[] columnWidth = new Double[] {0.210,0.210};
		textEntryMenuPane = new ObjectTablePane<QiTextEntryMenu>(columnMappingList,columnWidth);
		textEntryMenuPane.setConstrainedResize(false);
		textEntryMenuPane.setPrefHeight(190);

		LoggedTableColumn<QiTextEntryMenu, Integer> column = new LoggedTableColumn<QiTextEntryMenu, Integer>();
		createSerialNumber(column);
		textEntryMenuPane.getTable().getColumns().add(0, column);
		textEntryMenuPane.getTable().getColumns().get(0).setText("#");
		textEntryMenuPane.getTable().getColumns().get(0).setResizable(true);
		textEntryMenuPane.getTable().getColumns().get(0).setMaxWidth(50);
		textEntryMenuPane.getTable().getColumns().get(0).setMinWidth(5);
		textEntryMenuPane.setId("textEntryMenuPane");
		
		
		return textEntryMenuPane;
	}

	public boolean isTextScreen()  {
		if(screenType != null
				&& (screenType.equals(CopyMode.COPY_MENU) || screenType.equals(CopyMode.COPY_PDC) || screenType.equals(CopyMode.MOVE_PDC)))  {
			return true;
		}
		return false;	
	}
	
	public ObjectTablePane<QiTextEntryMenu> getTextEntryMenuPane() {
		return textEntryMenuPane;
	}

	public void setTextEntryMenuPane(ObjectTablePane<QiTextEntryMenu> textEntryMenuPane) {
		this.textEntryMenuPane = textEntryMenuPane;
	}

	public CopyMode getScreenType() {
		return screenType;
	}


	public LoggedButton getOkButton() {
		return okButton;
	}

	public LoggedButton getCancelButton() {
		return cancelButton;
	}

	public CopyPartDefectCombinationDialogController getController() {
		return controller;
	}

	public ObjectTablePane<QiEntryScreenDto> getEntryScreenPanel() {
		return entryScreenPanel;
	}

	public ObjectTablePane<QiRegionalPartDefectLocationDto> getAssignedPartdefectPanel() {
		return assignedPartdefectPanel;
	}

	public void setEntryScreenPanel(ObjectTablePane<QiEntryScreenDto> entryScreenPanel) {
		this.entryScreenPanel = entryScreenPanel;
	}

	public void setAssignedPartdefectPanel(ObjectTablePane<QiRegionalPartDefectLocationDto> assignedPartdefectPanel) {
		this.assignedPartdefectPanel = assignedPartdefectPanel;
	}

	public ComboBox<String> getEntryModelComboBox() {
		return entryModelComboBox;
	}


	public void setEntryModelComboBox(ComboBox<String> entryModelComboBox) {
		this.entryModelComboBox = entryModelComboBox;
	}

	public ComboBox<String> getProductTypeComboBox() {
		return productTypeComboBox;
	}


	public void setProductTypeComboBox(ComboBox<String> productTypeComboBox) {
		this.productTypeComboBox = productTypeComboBox;
	}

	public ComboBox<String> getPlantComboBox() {
		return plantComboBox;
	}

	public void setPlantComboBox(ComboBox<String> plantComboBox) {
		this.plantComboBox = plantComboBox;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public CheckBox getCopyModeChkBox() {
		return copyModeChkBox;
	}

	public void setCopyModeChkBox(CheckBox copyModeChkBox) {
		this.copyModeChkBox = copyModeChkBox;
	}

	public List<QiTextEntryMenu> getSourceMenuList() {
		return sourceMenuList;
	}

	public void setSourceMenuList(List<QiTextEntryMenu> sourceMenuList) {
		this.sourceMenuList = sourceMenuList;
	}

	public String getFilterText() {
		return filterText;
	}

	public void setFilterText(String filterText) {
		this.filterText = filterText;
	}

	public PdcToEntryScreenAssignmentMainPanel getMainPanel() {
		return mainPanel;
	}

	public void setMainPanel(PdcToEntryScreenAssignmentMainPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

}
