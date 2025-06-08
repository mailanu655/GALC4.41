package com.honda.galc.client.teamleader.qi.view;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.ReasonForChangeMaintController;
import com.honda.galc.client.teamleader.qi.model.ReasonForChangeMaintModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.qi.QiReasonForChangeCategory;
import com.honda.galc.entity.qi.QiReasonForChangeDetail;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * 
 * <h3>ReasonForChangeMaintPanel Class description</h3>
 * <p>
 * ReasonForChangeMaintPanel description
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
 * @author Justin Jiang<br>
 *         November 3, 2020
 *
 */

public class ReasonForChangeMaintPanel extends QiAbstractTabbedView<ReasonForChangeMaintModel, ReasonForChangeMaintController> {
	
	private LoggedComboBox<String> plantCombobox;
	private ObjectTablePane<Division> deptTablePane;
	private ObjectTablePane<QiReasonForChangeCategory> categoryTablePane;
	private ObjectTablePane<QiReasonForChangeDetail> detailTablePane;

	public ReasonForChangeMaintPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
	}	

	@Override
	public void initView() {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double width = primaryScreenBounds.getWidth()/2;
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);		

		HBox siteHBox = createSiteHBox();	
		HBox plantHBox = createPlantHBox();
		HBox filterHBox = createFilterHBox(width, siteHBox, plantHBox);
		
		HBox dataHBox = new HBox();
		deptTablePane = createDeptTablePane();
		categoryTablePane = createCategoryTablePane();
		detailTablePane = createDetailTablePane();
		
		dataHBox.getChildren().addAll(deptTablePane, categoryTablePane, detailTablePane);
		dataHBox.setSpacing(5);
		dataHBox.setPadding(new Insets(5, 5, 5, 5));		

		VBox mainVBox = new VBox();
		mainVBox.getChildren().addAll(filterHBox, dataHBox);
		this.setCenter(mainVBox);
	}

	private String getDefaultSiteName() {
		return getModel().getSiteName();
	}
	
	private HBox createSiteHBox() {
		HBox siteHBox = new HBox();
		LoggedLabel siteNameLabel = UiFactory.createLabel("site", "Site");
		siteNameLabel.getStyleClass().add("display-label");
		LoggedLabel siteValueLabel = new LoggedLabel();
		siteValueLabel.setText(getDefaultSiteName());
		siteHBox.getChildren().addAll(siteNameLabel, siteValueLabel);
		siteHBox.setPadding(new Insets(10, 10, 10, 20));
		siteHBox.setSpacing(40);
		return siteHBox;
	}
	
	private HBox createPlantHBox() {
		LoggedLabel plantLabel=UiFactory.createLabel("label", "Plant");
		plantLabel.getStyleClass().add("display-label");
		LoggedLabel asteriskLabel=getAsteriskLabel(UiFactory.createLabel("label", "*"));
		HBox plantLabelBoxContainer = new HBox();
		plantLabelBoxContainer.getChildren().addAll(plantLabel, asteriskLabel);
		plantCombobox = new LoggedComboBox<String>();
		plantCombobox.getStyleClass().add("combo-box-base");
		plantCombobox.setPrefWidth(120);	
		HBox plantContainer = new HBox();
		plantContainer.setAlignment(Pos.BASELINE_LEFT);
		plantContainer.setPadding(new Insets(10, 10, 10, 10));
		plantContainer.setSpacing(40);
		plantContainer.getChildren().addAll(plantLabelBoxContainer, plantCombobox);
		loadPlantCombobox();
		return plantContainer;
	}
	
	private HBox createFilterHBox(double width, HBox siteHBox, HBox plantHBox) {
		HBox filterHBox = new HBox();
		filterHBox.getChildren().addAll(siteHBox, plantHBox);
		filterHBox.setSpacing(20);
		filterHBox.setAlignment(Pos.CENTER_LEFT);
		filterHBox.setPrefWidth(width);
		filterHBox.setPadding(new Insets(5, 5, 5, 5));
		return filterHBox;
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
	
	private ObjectTablePane<Division> createDeptTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Dept", "divisionId")
				.put("Dept Name","divisionName");

		Double[] columnWidth = new Double[] {
				0.1, 0.2
		}; 
		ObjectTablePane<Division> panel = new ObjectTablePane<Division>(columnMappingList, columnWidth);
		panel.setConstrainedResize(false);
		panel.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.1);
		return panel;
	}
	
	private ObjectTablePane<QiReasonForChangeCategory> createCategoryTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Category", "category");

		Double[] columnWidth = new Double[] {
				0.3
		}; 
		ObjectTablePane<QiReasonForChangeCategory> panel = new ObjectTablePane<QiReasonForChangeCategory>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		panel.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.1);
		return panel;
	}
	
	private ObjectTablePane<QiReasonForChangeDetail> createDetailTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Detail", "detail");

		Double[] columnWidth = new Double[] {
				0.4
		}; 
		ObjectTablePane<QiReasonForChangeDetail> panel = new ObjectTablePane<QiReasonForChangeDetail>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		panel.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.1);
		return panel;
	}

	@Override
	public void reload() {
	}
	
	public void onTabSelected() {		
	}

	public ObjectTablePane<Division> getDeptTablePane() {
		return deptTablePane;
	}

	public ObjectTablePane<QiReasonForChangeCategory> getCategoryTablePane() {
		return categoryTablePane;
	}

	public ObjectTablePane<QiReasonForChangeDetail> getDetailTablePane() {
		return detailTablePane;
	}

	public String getScreenName() {
		return "Reason For Change Maintenance";
	}

	public LoggedComboBox<String> getPlantCombobox() {
		return plantCombobox;
	}
	
	@Override
	public void start() {
	}
}
