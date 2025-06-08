package com.honda.galc.client.teamleader.qi.view;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.ParkingLocationMaintController;
import com.honda.galc.client.teamleader.qi.model.ParkingLocationMaintenanceModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.entity.qi.QiRepairAreaRow;
import com.honda.galc.entity.qi.QiRepairAreaSpace;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * 
 * <h3>ParkingLocationMaintPanel Class description</h3>
 * <p>
 * ParkingLocationMaintPanel is used to create the main functionality for the Parking Location. 
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
public class ParkingLocationMaintPanel extends QiAbstractTabbedView<ParkingLocationMaintenanceModel,ParkingLocationMaintController>{
	private LoggedLabel siteValueLabel;
	private LoggedComboBox<String> plantCombobox;
	private ObjectTablePane<QiRepairArea> repairAreaTablePane;
	private ObjectTablePane<QiRepairAreaRow> repairRowDataTablePane;
	private ObjectTablePane<QiRepairAreaSpace> spaceDataTablePane;

	public ParkingLocationMaintPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
	}	

	@Override
	public void initView() {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double width = primaryScreenBounds.getWidth()/2;
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);		
		VBox mainContainer = new VBox();
		HBox filterContainer = new HBox();				
		HBox siteContainer = new HBox();
		HBox plantContainer = new HBox();
		HBox plantLabelBoxContainer = new HBox();
		HBox repairAreaAndRowDataContainer = new HBox();
		HBox spaceDataContainer = new HBox();
		
		setSiteName(siteContainer);		
		setPlant(plantContainer, plantLabelBoxContainer);		
		setSiteAndPlant(width, filterContainer, siteContainer, plantContainer);
		
		repairAreaTablePane = createRepairAreaTablePane();
		repairRowDataTablePane = createRowDataTablePane();
		spaceDataTablePane = createSpaceDataTablePane();
		repairAreaAndRowDataContainer.getChildren().addAll(repairAreaTablePane,repairRowDataTablePane);
		repairAreaAndRowDataContainer.setSpacing(5);
		repairAreaAndRowDataContainer.setPadding(new Insets(5, 5, 5, 5));		
		spaceDataContainer.getChildren().add(spaceDataTablePane);		
		spaceDataTablePane.setPadding(new Insets(5, 5, 5, 5));
		
		mainContainer.getChildren().addAll(filterContainer,repairAreaAndRowDataContainer,spaceDataContainer);
		this.setCenter(mainContainer);
		
	}

	private void setSiteAndPlant(double width, HBox filterContainer, HBox siteContainer, HBox plantContainer) {
		filterContainer.getChildren().addAll(siteContainer,plantContainer);
		filterContainer.setSpacing(20);
		filterContainer.setAlignment(Pos.CENTER_LEFT);
		filterContainer.setPrefWidth(width);
		filterContainer.setPadding(new Insets(5, 5, 5, 5));
	}
	
	private void setSiteName(HBox siteContainer) {
		LoggedLabel siteNameLabel = UiFactory.createLabel("site", "Site");
		siteNameLabel.getStyleClass().add("display-label");
		siteValueLabel =new LoggedLabel();
		siteValueLabel.setText(getDefaultSiteName());
		siteContainer.getChildren().addAll(siteNameLabel,siteValueLabel);
		siteContainer.setPadding(new Insets(10, 10, 10, 20));
		siteContainer.setSpacing(40);
	}
	
	private String getDefaultSiteName() {
		return getModel().getSiteName();
	}
	
	private void setPlant(HBox plantContainer, HBox plantLabelBoxContainer) {
		LoggedLabel plantLabel=UiFactory.createLabel("label","Plant");
		plantLabel.getStyleClass().add("display-label");
		LoggedLabel asteriskLabel=getAsteriskLabel(UiFactory.createLabel("label","*"));
		plantLabelBoxContainer.getChildren().addAll(plantLabel,asteriskLabel);
		plantCombobox = new LoggedComboBox<String>();
		plantCombobox.getStyleClass().add("combo-box-base");
		plantCombobox.setPrefWidth(120);	
		plantContainer.setAlignment(Pos.BASELINE_LEFT);
		plantContainer.setPadding(new Insets(10, 10, 10, 10));
		plantContainer.setSpacing(40);
		plantContainer.getChildren().addAll(plantLabelBoxContainer,plantCombobox);
		loadPlantCombobox();
	}
	
	@SuppressWarnings("unchecked")
	public void loadPlantCombobox(){
		List<String> plantList = QiCommonUtil.getUniqueArrayList(Lists.transform(getModel().findAllPlantBySite(getDefaultSiteName()), new Function<Plant, String>() {
			@Override
			public String apply(final Plant entity) {
				return StringUtils.trimToEmpty(entity.getPlantName());
			}
		}));
		getPlantCombobox().setItems(FXCollections.observableArrayList(plantList));
	}
	
	private LoggedLabel getAsteriskLabel(LoggedLabel loggedLabel){
		loggedLabel=UiFactory.createLabel("label","*");
		loggedLabel.setStyle("-fx-text-fill: red");
		return loggedLabel;
	}
	
	private ObjectTablePane<QiRepairArea> createRepairAreaTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Repair Area Name", "repairAreaName")
				.put("Description","repairAreaDescription")
				.put("Location", "location")
				.put("Priority", "priority")
				.put("Row Sequence", "rowFillSeq")
				.put("Division Name", "divName");

		Double[] columnWidth = new Double[] {
				0.1,0.2, 0.08, 0.05, 0.05,0.1
		}; 
		ObjectTablePane<QiRepairArea> panel = new ObjectTablePane<QiRepairArea>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		return panel;
	}
	
	private ObjectTablePane<QiRepairAreaRow> createRowDataTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Repair Area Name", "id.repairAreaName")
				.put("Row","id.repairAreaRow",Integer.class)
				.put("Fill Sequence", "spaceFillSequence")
				.put("Created By", "createUser");

		Double[] columnWidth = new Double[] {
				0.1, 0.1, 0.1, 0.1
		}; 
		ObjectTablePane<QiRepairAreaRow> panel = new ObjectTablePane<QiRepairAreaRow>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		return panel;
	}
	
	private ObjectTablePane<QiRepairAreaSpace> createSpaceDataTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Repair Area Name", "id.repairAreaName")
				.put("Row #","id.repairArearRow",Integer.class)
				.put("Space #","id.repairArearSpace",Integer.class)
				.put("Status"+"\n"+"ACTIVE=1"+"\n"+"INACTIVE=0","status")
				.put("Created By", "createUser")
				.put("Created TimeStamp", "createDate");

		Double[] columnWidth = new Double[] {
				0.3, 0.1, 0.1, 0.1, 0.2, 0.2
		}; 
		ObjectTablePane<QiRepairAreaSpace> panel = new ObjectTablePane<QiRepairAreaSpace>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		panel.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		return panel;
	}

	@Override
	public void reload() {
		String plantName = StringUtils.trim(getPlantCombobox().getSelectionModel().getSelectedItem().toString());
		repairAreaTablePane.setData(getModel().findAllBySiteAndPlant(getDefaultSiteName(),plantName));
	}
	
	public void onTabSelected() {		
	}

	public ObjectTablePane<QiRepairArea> getRepairAreaTablePane() {
		return repairAreaTablePane;
	}

	public ObjectTablePane<QiRepairAreaRow> getRepairRowDataTablePane() {
		return repairRowDataTablePane;
	}

	public ObjectTablePane<QiRepairAreaSpace> getSpaceDataTablePane() {
		return spaceDataTablePane;
	}

	public String getScreenName() {
		return "Parking Location Maintenance";
	}

	public LoggedComboBox<String> getPlantCombobox() {
		return plantCombobox;
	}
	
	@Override
	public void start() {
		
	}
}
