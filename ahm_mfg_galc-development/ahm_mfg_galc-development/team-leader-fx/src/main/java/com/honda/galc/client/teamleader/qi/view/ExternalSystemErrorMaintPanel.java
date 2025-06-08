package com.honda.galc.client.teamleader.qi.view;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ComboBox;
import javafx.stage.Screen;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.ExternalSystemErrorMaintController;
import com.honda.galc.client.teamleader.qi.model.ExternalSystemErrorMaintModel;
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
import com.honda.galc.entity.qi.QiExternalSystemData;

/**
 * 
 * <h3>ExternalSystemErrorMaintPanel Class description</h3>
 * <p>
 * ExternalSystemErrorMaintPanel is used to load headless error data and perform reprocess and delete action.
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
public class ExternalSystemErrorMaintPanel extends QiAbstractTabbedView<ExternalSystemErrorMaintModel,ExternalSystemErrorMaintController> {

	private LoggedLabel siteLabel;
	private LabeledComboBox<String> plantComboBox;
	private LabeledComboBox<String> productTypeComboBox;
	private LabeledComboBox<String> externalSystemComboBox;
	private ObjectTablePane<QiExternalSystemData> externalSystemErrorTablePane;
	private double width;
	private LoggedButton refreshBtn;

	public ExternalSystemErrorMaintPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
	}

	public void onTabSelected() {
		getController().refreshBtnAction();
	}

	public String getScreenName() {
		return "External System Error";
	}

	@Override
	public void reload() {
		List<QiExternalSystemData> selectedDataList =new ArrayList<QiExternalSystemData>();
		selectedDataList.addAll(externalSystemErrorTablePane.getTable().getSelectionModel().getSelectedItems());
		String externalSystemName = StringUtils.trimToEmpty(getExternalSystemComboBox().getSelectionModel().getSelectedItem());
		String siteName = StringUtils.trimToEmpty(getSiteLabel().getText());
		String plantName = StringUtils.trimToEmpty(getPlantComboBox().getSelectionModel().getSelectedItem());
		String productType = StringUtils.trimToEmpty(getProductTypeComboBox().getSelectionModel().getSelectedItem());
		List<QiExternalSystemData> list = getModel().findAllExternalSystemData(siteName, plantName, productType, externalSystemName);
		externalSystemErrorTablePane.setData(list);
		for(QiExternalSystemData dto : selectedDataList){
			externalSystemErrorTablePane.getTable().getSelectionModel().select(dto);
		}
	}

	@Override
	public void start() {

	}
	
	@Override
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		this.width = screenBounds.getWidth()/2;
		ColumnMappingList columnMappingList = ColumnMappingList.with("Product Id", "id.productId")
				.put("Process "+"\n"+"Point Id","id.processPointId")
				.put("External"+"\n"+"Defect Code", "id.externalDefectCode")
				.put("External "+"\n"+"Part Code", "id.externalPartCode")
				.put("Entry Dept", "entryDept")
				.put("Associate Id", "associateId")
				.put("Original"+"\n"+"Defect Status", "originalDefectStatus")
				.put("Current"+"\n"+"Defect Status", "currentDefectStatus")
				.put("WriteUp"+"\n"+"Dept", "writeUpDept")
				.put("Image"+"\n"+"Name", "imageName")
				.put("Point X", "pointX")
				.put("Point Y", "pointY")
				.put("Entry"+"\n"+"Timestamp", "id.entryTimestamp")
				;
		
		Double[] columnWidth = new Double[] {0.10,0.09,0.08,0.08,0.05,0.07,0.08,0.08,0.06,0.07,0.04,0.04,0.12};
		externalSystemErrorTablePane= new ObjectTablePane<QiExternalSystemData>(columnMappingList,columnWidth);
		LoggedTableColumn<QiExternalSystemData, Integer> column = new LoggedTableColumn<QiExternalSystemData, Integer>();
		createSerialNumber(column);
		externalSystemErrorTablePane.getTable().getColumns().add(0, column);
		externalSystemErrorTablePane.getTable().getColumns().get(0).setText("#");
		externalSystemErrorTablePane.getTable().getColumns().get(0).setResizable(true);
		externalSystemErrorTablePane.getTable().getColumns().get(0).setMaxWidth(40);
		externalSystemErrorTablePane.getTable().getColumns().get(0).setMinWidth(1);
		this.setTop(getExternalSystemErrorPane());
		this.setCenter(externalSystemErrorTablePane);
		loadPlantComboBox(siteLabel.getText());
	}

	/**
	 * This method is used to migpane for external system error
	 */
	private MigPane getExternalSystemErrorPane(){
		double screenWidth=Screen.getPrimary().getVisualBounds().getWidth();
		double screenHeight=Screen.getPrimary().getVisualBounds().getHeight();
		//creates all combo boxes
		plantComboBox=createCombobox("Plant",plantComboBox);
		productTypeComboBox=createCombobox("Product Type",productTypeComboBox);
		externalSystemComboBox=createCombobox("External System",externalSystemComboBox);
		siteLabel = UiFactory.createLabel("siteLabel", getModel().getSiteName(), Fonts.SS_DIALOG_PLAIN(14));
		refreshBtn = createBtn("Refresh",getController());
		refreshBtn.setStyle("-fx-font-size: 12pt;");
		MigPane pane = new MigPane("", 
				"", 
				"[]");
		pane.add(createSiteLabelName(),"gapleft 20");
		pane.add(siteLabel,"gapleft 10");
		pane.add(plantComboBox,"gapleft 20");
		pane.add(productTypeComboBox,"gapleft 20");
		pane.add(externalSystemComboBox,"gapleft 20");
		pane.add(refreshBtn,"gapleft 60");
		pane.setPrefWidth(screenWidth/3.5);
		pane.setPrefHeight(screenHeight/4.5);
		pane.setPadding(new Insets(10,10,10,40));
		return pane;
	}

	/**
	 * This method is used to create site labeled combo box
	 */
	private LoggedLabel createSiteLabelName() {
		LoggedLabel site = UiFactory.createLabel("siteLabel", "Site", Fonts.SS_DIALOG_BOLD(14));
		return site;
	}

	/**
	 * This method is used to create labeled combo box
	 */
	private LabeledComboBox<String> createCombobox(String comboBoxName,LabeledComboBox<String> comboBox) {
		comboBox = createLabeledComboBox(comboBoxName, true, new Insets(0), true, true);
		comboBox.getControl().setMinHeight(35.0);
		comboBox.getLabel().setStyle("-fx-font-size: 10pt;  -fx-font-weight: bold;");
		return comboBox;
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
		getPlantComboBox().setPromptText("Select");
		if(plantList!=null)
			getPlantComboBox().getItems().addAll(plantList);
	}

	public LabeledComboBox<String> createLabeledComboBox(String id,boolean isHorizontal, Insets insets, boolean isLabelBold, boolean isMandatory) {
		LabeledComboBox<String> comboBox = new LabeledComboBox<String>(id,isHorizontal,insets,isLabelBold,isMandatory);
		comboBox.setId(id);
		return comboBox;
	}

	public ComboBox<String> getPlantComboBox() {
		return plantComboBox.getControl();
	}

	public void setPlantComboBox(LabeledComboBox<String> plantComboBox) {
		this.plantComboBox = plantComboBox;
	}

	public ComboBox<String> getProductTypeComboBox() {
		return productTypeComboBox.getControl();
	}

	public void setProductTypeComboBox(LabeledComboBox<String> productTypeComboBox) {
		this.productTypeComboBox = productTypeComboBox;
	}

	public ComboBox<String> getExternalSystemComboBox() {
		return externalSystemComboBox.getControl();
	}

	public void setExternalSystemComboBox(
			LabeledComboBox<String> externalSystemComboBox) {
		this.externalSystemComboBox = externalSystemComboBox;
	}

	public ObjectTablePane<QiExternalSystemData> getExternalSystemErrorTablePane() {
		return externalSystemErrorTablePane;
	}

	public void setExternalSystemErrorTablePane(
			ObjectTablePane<QiExternalSystemData> externalSystemErrorTablePane) {
		this.externalSystemErrorTablePane = externalSystemErrorTablePane;
	}

	public LoggedLabel getSiteLabel() {
		return siteLabel;
	}

	public void setSiteLabel(LoggedLabel siteLabel) {
		this.siteLabel = siteLabel;
	}

	public LoggedButton getRefreshBtn() {
		return refreshBtn;
	}

}
