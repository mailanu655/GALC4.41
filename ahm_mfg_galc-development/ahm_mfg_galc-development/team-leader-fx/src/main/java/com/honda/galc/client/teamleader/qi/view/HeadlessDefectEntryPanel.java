package com.honda.galc.client.teamleader.qi.view;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.HeadlessDefectEntryController;
import com.honda.galc.client.teamleader.qi.model.HeadlessDefectEntryModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.DefectMapDto;

/**
 * 
 * <h3>HeadlessDefectEntryPanel Class description</h3>
 * <p>
 * HeadlessDefectEntryPanel is used to load data in TableViews and perform the action on Update ,Create and delete
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
 * @author LnTInfotech<br>
 * 
 */
public class HeadlessDefectEntryPanel extends QiAbstractTabbedView<HeadlessDefectEntryModel,HeadlessDefectEntryController>{
	
	private LoggedLabel siteLabel;
	private LabeledComboBox<String> externalSystemComboBox;
	private LabeledComboBox<String> plantComboBox;
	private LabeledComboBox<String> productTypeComboBox;
	private LabeledComboBox<String> entryModelComboBox;
	private LabeledComboBox<String> entryScreenComboBox;
	private ListView<DefectMapDto> entryScreenListView; 
	private ListView<DefectMapDto> entryModelListView; 
	private ObjectTablePane<DefectMapDto> externalSystemDefectList;
	private ObjectTablePane<DefectMapDto> externalSystemDefectTablePane;
	private LoggedButton refreshBtn;

	public HeadlessDefectEntryPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
	}

	@Override
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		ColumnMappingList columnMappingList = ColumnMappingList.with("External Part Code", "externalPartCode")
				.put("External Defect Code","externalDefectCode")
				.put("Text Entry Menu", "textEntryMenu")
				.put("QICS Part Defect Combination", "fullPartDesc")
				.put("QICS Repair Required", "QicsRepairReqdStr");
		Double[] columnWidth = new Double[] {0.15,0.15,0.10,0.46,0.10};
		externalSystemDefectTablePane= new ObjectTablePane<DefectMapDto>(columnMappingList,columnWidth);
        LoggedTableColumn<DefectMapDto, Integer> column = new LoggedTableColumn<DefectMapDto, Integer>();
		createSerialNumber(column);
		externalSystemDefectTablePane.getTable().getColumns().add(0, column);
		externalSystemDefectTablePane.getTable().getColumns().get(0).setText("#");
		externalSystemDefectTablePane.getTable().getColumns().get(0).setResizable(true);
		externalSystemDefectTablePane.getTable().getColumns().get(0).setMaxWidth(40);
		externalSystemDefectTablePane.getTable().getColumns().get(0).setMinWidth(1);
        this.setTop(getHeadlessDefectEntryPane());
        this.setCenter(externalSystemDefectTablePane);
		loadPlantComboBox(siteLabel.getText());
	}
	
	/**
  	 * This method refreshes the combo boxes 
  	 */
	public void onTabSelected() {
		getController().refreshBtnAction(); 
	}
	
	/**
  	 * This method reloads the external system defect data.
  	 */
	@Override
	public void reload(){
		List<DefectMapDto> selectedDataList =new ArrayList<DefectMapDto>();
		selectedDataList.addAll(externalSystemDefectTablePane.getTable().getSelectionModel().getSelectedItems());
		String entryScreen = getEntryScreenComboBox().getSelectionModel().getSelectedItem() == null ? StringUtils.EMPTY : getEntryScreenComboBox().getSelectionModel().getSelectedItem().toString();
		String externalSystemName = getExternalSystemComboBox().getSelectionModel().getSelectedItem() == null ? StringUtils.EMPTY : getExternalSystemComboBox().getSelectionModel().getSelectedItem().toString();
		String entryModel = getEntryModelComboBox().getSelectionModel().getSelectedItem() == null ? StringUtils.EMPTY : getEntryModelComboBox().getSelectionModel().getSelectedItem().toString();
		externalSystemDefectTablePane.setData(getModel().findAllExternalSystemDefect(externalSystemName, entryScreen, entryModel));
		for(DefectMapDto dto : selectedDataList){
			externalSystemDefectTablePane.getTable().getSelectionModel().select(dto);
		}
		if(!entryScreen.equals(StringUtils.EMPTY) && isFullAccess()){
			getController().addContextMenuItems();
		}
	}
	
	@Override
	public void start() {
	}
	
	/**
	 * This method returns screen name.
	 * @return String 
	 */
	public String getScreenName() {
		return "Headless";
	}

	/**
	 *This method Loads plant combo box.
	 * @param plant 
	 */
	public void loadPlantComboBox(String site) {
		List<String> plantList = getModel().findAllBySite(site);
		getPlantComboBox().getItems().clear();
		getProductTypeComboBox().getItems().clear();
		getExternalSystemComboBox().getItems().clear();
		getEntryModelComboBox().getItems().clear();
		getEntryScreenComboBox().getItems().clear();
		getPlantComboBox().setPromptText("Select");
		if(plantList!=null)
			getPlantComboBox().getItems().addAll(plantList);
	}
	
  	/**
  	 * This method is used to create HeadLess defect entry pane
  	 * @return pane
  	 */
	private MigPane getHeadlessDefectEntryPane(){
		//creates all combo boxes
		 plantComboBox=createCombobox("Plant",plantComboBox);
		 entryModelComboBox=createCombobox("Entry Model",entryModelComboBox);
		 externalSystemComboBox=createCombobox("External System",externalSystemComboBox);
		 productTypeComboBox=createCombobox("Product Type",productTypeComboBox);
		 entryScreenComboBox=createCombobox("Entry Screen",entryScreenComboBox);
		 MigPane pane = new MigPane("insets 0 0 0 0", "[center,grow,fill]", "");
		 VBox comboVBoxFirst = new VBox();
		 comboVBoxFirst.getChildren().addAll(createSiteLabel(),externalSystemComboBox);
		 comboVBoxFirst.setPadding(new Insets(5,25,10,0));
		 VBox comboVBoxSecond = new VBox();
		 comboVBoxSecond.getChildren().addAll(plantComboBox,entryModelComboBox );
		 comboVBoxSecond.setPadding(new Insets(5,25,10,0));
		 VBox comboVBoxThird = new VBox();
		 comboVBoxThird.getChildren().addAll(productTypeComboBox,entryScreenComboBox);
		 comboVBoxSecond.setPadding(new Insets(5,25,10,0));
		 comboVBoxFirst.setAlignment(Pos.CENTER_LEFT);
		 comboVBoxSecond.setAlignment(Pos.CENTER_LEFT);
		 comboVBoxThird.setAlignment(Pos.CENTER_LEFT);
		 comboVBoxFirst.setSpacing(5);
		 comboVBoxSecond.setSpacing(5);
		 comboVBoxThird.setSpacing(5);
		 pane.getChildren().addAll(comboVBoxFirst,comboVBoxSecond,comboVBoxThird,createRefreshButton());
		 return pane;
  	}
  
	/**
  	 * This method is used to create site labeled combo box
  	 */
	private HBox createSiteLabel() {
		HBox siteContainer = new HBox();
		LoggedLabel site = UiFactory.createLabel("siteLabel", "Site", Fonts.SS_DIALOG_BOLD(14));
		siteLabel = UiFactory.createLabel("siteLabel", getModel().getSiteName(), Fonts.SS_DIALOG_PLAIN(14));
		siteContainer.setAlignment(Pos.CENTER_LEFT);
		siteContainer.setSpacing((Screen.getPrimary().getVisualBounds().getWidth()/2)*0.30);
		siteContainer.getChildren().addAll(site,siteLabel );
		siteContainer.setPadding(new Insets(7,0,10,0));
		return siteContainer;
	}
	
	

	/**
  	 * This method is used to create labeled combo box
  	 */
	private LabeledComboBox<String> createCombobox(String comboBoxName,LabeledComboBox<String> comboBox) {
		comboBox = createLabeledComboBox(comboBoxName, true, new Insets(0), true, true);
		comboBox.getControl().setMinHeight(35.0);
		comboBox.getLabel().setStyle("-fx-font-size: 10pt; -fx-font-weight: bold");
		comboBox.getLabel().setPadding(new Insets(7,15,7,0));
		return comboBox;
	}

	/**
  	 * This method creates refresh button
  	 * @return HBox
  	 */
	private HBox createRefreshButton() {
		HBox buttonContainer = new HBox();
		refreshBtn = createBtn(QiConstant.REFRESH, getController());
		refreshBtn.setStyle("-fx-font-size: 12pt; -fx-font-weight: bold;");
		buttonContainer.setAlignment(Pos.CENTER_RIGHT);
		buttonContainer.setPadding(new Insets(0,5,0,20));
		buttonContainer.getChildren().addAll(refreshBtn);
		return buttonContainer;
	}

	
	/**
	 * This method is used to create LabeledComboBox
	 * @param id
	 * @return LabeledComboBox<String>
	 */
	private LabeledComboBox<String> createLabeledComboBox(String label,boolean isHorizontal, Insets insets, boolean isLabelBold, boolean isMandaotry) {
		LabeledComboBox<String> comboBox = new LabeledComboBox<String>(label,isHorizontal,insets,isLabelBold,isMandaotry);
		return comboBox;
	}
	
	
	/**
	 * @param externalSystemDefectList the externalSystemDefectList to set
	 */
	public void setExternalSystemDefectList(ObjectTablePane<DefectMapDto> externalSystemDefectList) {
		this.externalSystemDefectList = externalSystemDefectList;
	}

	/**
	 * @return the siteLValueLabel
	 */
	public String getSiteLabel() {
		return StringUtils.trimToEmpty(siteLabel.getText());
	}

	/**
	 * @param siteLValueLabel the siteLValueLabel to set
	 */
	public void setSiteLabel(LoggedLabel siteLabel) {
		this.siteLabel = siteLabel;
	}

	/**
	 * @return the entryScreenListView
	 */
	public ListView<DefectMapDto> getEntryScreenListView() {
		return entryScreenListView;
	}

	/**
	 * @param entryScreenListView the entryScreenListView to set
	 */
	public void setEntryScreenListView(ListView<DefectMapDto> entryScreenListView) {
		this.entryScreenListView = entryScreenListView;
	}

	/**
	 * @return the entryModelListView
	 */
	public ListView<DefectMapDto> getEntryModelListView() {
		return entryModelListView;
	}

	/**
	 * @param entryModelListView the entryModelListView to set
	 */
	public void setEntryModelListView(ListView<DefectMapDto> entryModelListView) {
		this.entryModelListView = entryModelListView;
	}

	/**
	 * @return the externalSystemDefectList
	 */
	public ObjectTablePane<DefectMapDto> getExternalSystemDefectList() {
		return externalSystemDefectList;
	}

	/**
	 * @return the screenWidth
	 */
	public double getScreenWidth() {
		return Screen.getPrimary().getVisualBounds().getWidth();
	}

	/**
	 * @return the screenHeight
	 */
	public double getScreenHeight() {
		return Screen.getPrimary().getVisualBounds().getHeight();
	}
	
	/**
	 * @return the externalSystemDefectTablePane
	 */
	public ObjectTablePane<DefectMapDto> getExternalSystemDefectTablePane() {
		return externalSystemDefectTablePane;
	}

	/**
	 * @param externalSystemDefectTablePane the externalSystemDefectTablePane to set
	 */
	public void setExternalSystemDefectTablePane(ObjectTablePane<DefectMapDto> externalSystemDefectTablePane) {
		this.externalSystemDefectTablePane = externalSystemDefectTablePane;
	}

	/**
	 * @return the externalSystemComboBox
	 */
	public ComboBox<String> getExternalSystemComboBox() {
		return externalSystemComboBox.getControl();
	}

	/**
	 * @param externalSystemComboBox the externalSystemComboBox to set
	 */
	public void setExternalSystemComboBox(
			LabeledComboBox<String> externalSystemComboBox) {
		this.externalSystemComboBox = externalSystemComboBox;
	}

	/**
	 * @return the plantComboBox
	 */
	public ComboBox<String> getPlantComboBox() {
		return plantComboBox.getControl();
	}

	/**
	 * @param plantComboBox the plantComboBox to set
	 */
	public void setPlantComboBox(LabeledComboBox<String> plantComboBox) {
		this.plantComboBox = plantComboBox;
	}

	/**
	 * @return the productTypeComboBox
	 */
	public ComboBox<String>  getProductTypeComboBox() {
		return productTypeComboBox.getControl();
	}

	/**
	 * @param productTypeComboBox the productTypeComboBox to set
	 */
	public void setProductTypeComboBox(LabeledComboBox<String> productTypeComboBox) {
		this.productTypeComboBox = productTypeComboBox;
	}

	/**
	 * @return the entryModelComboBox
	 */
	public ComboBox<String> getEntryModelComboBox() {
		return entryModelComboBox.getControl();
	}

	/**
	 * @param entryModelComboBox the entryModelComboBox to set
	 */
	public void setEntryModelComboBox(LabeledComboBox<String> entryModelComboBox) {
		this.entryModelComboBox = entryModelComboBox;
	}

	/**
	 * @return the entryScreenComboBox
	 */
	public ComboBox<String> getEntryScreenComboBox() {
		return entryScreenComboBox.getControl();
	}

	/**
	 * @param entryScreenComboBox the entryScreenComboBox to set
	 */
	public void setEntryScreenComboBox(LabeledComboBox<String> entryScreenComboBox) {
		this.entryScreenComboBox = entryScreenComboBox;
	}
	
	public LoggedButton getRefreshBtn() {
		return refreshBtn;
	}
	
}
