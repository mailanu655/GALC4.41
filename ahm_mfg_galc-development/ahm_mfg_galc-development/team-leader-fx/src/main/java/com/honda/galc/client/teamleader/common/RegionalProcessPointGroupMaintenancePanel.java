package com.honda.galc.client.teamleader.common;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.view.QiAbstractTabbedView;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.RegionalProcessPointGroupDto;
import com.honda.galc.entity.conf.RegionalCode;

public class RegionalProcessPointGroupMaintenancePanel extends QiAbstractTabbedView<RegionalProcessPointGroupMaintenanceModel, RegionalProcessPointGroupMaintenanceController>{
	private LoggedButton saveBtn;
	private LoggedButton leftArrowBtn;
	private LoggedButton rightArrowBtn;
	private UpperCaseFieldBean filterTextField;
	private ObjectTablePane<RegionalCode> availableProcessPointGroupObjectTablePane;
	private ObjectTablePane<RegionalProcessPointGroupDto> assignedProcessPointGroupObjectTablePane;
	private LoggedComboBox<String> siteComboBox;
	private LoggedComboBox<String> categoryCodeComboBox;

	public RegionalProcessPointGroupMaintenancePanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
		leftArrowBtn.setDisable(true);
		rightArrowBtn.setDisable(true);
		saveBtn.setDisable(true);
	}

	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		VBox vBox = new VBox();
		vBox.setSpacing(10);
		HBox topHBox = new HBox();
		topHBox.setAlignment(Pos.CENTER_LEFT);
		topHBox.setPadding(new Insets(10, 10, 10, 10));
		topHBox.setSpacing(100);
		HBox categoryCodeHBox = createCategoryCodeHBox();
		HBox siteHBox = createSiteHBox();
		topHBox.getChildren().addAll(categoryCodeHBox, siteHBox);
		HBox.setHgrow(topHBox, Priority.ALWAYS);
		HBox processPointGroupHBox = createProcessPointGroupHBox();
		HBox buttonHBox = createBtnContainer();
		vBox.getChildren().addAll(topHBox, processPointGroupHBox, buttonHBox);
		this.setTop(vBox);
	}

	private HBox createCategoryCodeHBox() {
		HBox categoryCodeHBox = new HBox();
		HBox categoryCodeLabelHbox = new HBox();
		LoggedLabel categoryCodeLabel = UiFactory.createLabel("label", "Category Code");
		categoryCodeLabelHbox.getChildren().addAll(categoryCodeLabel);
		categoryCodeComboBox = new LoggedComboBox<String>("categoryCodeComboBox");
		categoryCodeComboBox.getStyleClass().add("combo-box-base");
		categoryCodeComboBox.setMinWidth(200);
		categoryCodeComboBox.setPromptText("Select");
		categoryCodeHBox.setSpacing(10);
		categoryCodeHBox.setPadding(new Insets(0,0,0,20));
		categoryCodeHBox.getChildren().addAll(categoryCodeLabelHbox, categoryCodeComboBox);
		return categoryCodeHBox;
	}

	private HBox createSiteHBox() {
		HBox siteHBox = new HBox();
		HBox siteLabelHBox = new HBox();
		LoggedLabel siteLabel = UiFactory.createLabel("label", "Site");
		siteLabelHBox.getChildren().addAll(siteLabel);
		siteComboBox = new LoggedComboBox<String>("siteComboBox");
		siteComboBox.getStyleClass().add("combo-box-base");
		siteComboBox.setMinWidth(200);
		siteComboBox.setPromptText("Select");
		siteHBox.setSpacing(10);
		siteHBox.setPadding(new Insets(0,0,0,-75));
		siteHBox.getChildren().addAll(siteLabelHBox, siteComboBox);
		return siteHBox;
	}

	private HBox createProcessPointGroupHBox() {
		HBox processPointGroupHBox = new HBox();
		processPointGroupHBox.setSpacing(10);
		VBox leftRightArrowVBox = new VBox();
		leftRightArrowVBox.setSpacing(10);
		leftArrowBtn = createBtn("<",getController());
		leftArrowBtn.setId("leftArrowBtn");
		rightArrowBtn = createBtn(">",getController());
		leftRightArrowVBox.setAlignment(Pos.CENTER);
		rightArrowBtn.setId("rightArrowBtn");
		processPointGroupHBox.setPadding(new Insets(0, 0, 0, 30));
		leftRightArrowVBox.getChildren().addAll(leftArrowBtn, rightArrowBtn);
		processPointGroupHBox.getChildren().add(createTitiledPane("Available Process Point Group", createAvailableProcessPointGroupMigPane()));
		processPointGroupHBox.getChildren().add(leftRightArrowVBox);
		processPointGroupHBox.getChildren().add(createTitiledPane("Assigned Process Point Group", createAssignedProcessPointGroupMigPane()));
		return processPointGroupHBox;
	}

	private HBox createBtnContainer() {
		HBox buttonContainer= new HBox();
		buttonContainer.setSpacing(10);
		saveBtn = createBtn("Save",getController());
		buttonContainer.setAlignment(Pos.BOTTOM_RIGHT);
		buttonContainer.setPadding(new Insets(0, 40, 0, 0));
		buttonContainer.getChildren().addAll(saveBtn);
		if(!isFullAccess()){
			saveBtn.setDisable(true);
		}
		return buttonContainer;
	}

	private TitledPane createTitiledPane(String title,Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double titlePaneWidth = 0.45*(primaryScreenBounds.getWidth());
		titledPane.setPrefSize(titlePaneWidth, 350);
		return titledPane;
	}

	private MigPane createAvailableProcessPointGroupMigPane() {
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");
		HBox filterLabelBox = new HBox();
		Label filterLabel = UiFactory.createLabel("label", "Filter");
		filterLabel.getStyleClass().add("display-label");
		
		filterTextField =  createFilterTextField("filter-textfield", 12, getController());
		filterLabelBox.setSpacing(05);
		filterLabelBox.setAlignment(Pos.BASELINE_RIGHT);
		filterLabelBox.getChildren().addAll(filterLabel, filterTextField);
		availableProcessPointGroupObjectTablePane = createAvailableProcessPointGroupObjectTablePane();
		availableProcessPointGroupObjectTablePane.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		LoggedTableColumn<RegionalCode, Integer> column = new LoggedTableColumn<RegionalCode, Integer>();

		createSerialNumber(column);
		availableProcessPointGroupObjectTablePane.getTable().getColumns().add(0, column);
		availableProcessPointGroupObjectTablePane.getTable().getColumns().get(0).setText("#");
		availableProcessPointGroupObjectTablePane.getTable().getColumns().get(0).setResizable(true);
		availableProcessPointGroupObjectTablePane.getTable().getColumns().get(0).setMaxWidth(100);
		availableProcessPointGroupObjectTablePane.getTable().getColumns().get(0).setMinWidth(50);

		pane.add(filterLabelBox,"span,wrap");
		pane.add(availableProcessPointGroupObjectTablePane,"span,wrap");
		pane.setId("mig-pane");
		pane.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.35);
		pane.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.35);
		pane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.35);
		return pane;
	}
	
	private MigPane createAssignedProcessPointGroupMigPane() {
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");
		assignedProcessPointGroupObjectTablePane = createAssignedProcessPointGroupObjectTablePane();
		assignedProcessPointGroupObjectTablePane.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		LoggedTableColumn<RegionalProcessPointGroupDto, Integer> column = new LoggedTableColumn<RegionalProcessPointGroupDto, Integer>();

		createSerialNumber(column);
		assignedProcessPointGroupObjectTablePane.getTable().getColumns().add(0, column);
		assignedProcessPointGroupObjectTablePane.getTable().getColumns().get(0).setText("#");
		assignedProcessPointGroupObjectTablePane.getTable().getColumns().get(0).setResizable(true);
		assignedProcessPointGroupObjectTablePane.getTable().getColumns().get(0).setMaxWidth(100);
		assignedProcessPointGroupObjectTablePane.getTable().getColumns().get(0).setMinWidth(50);

		pane.add(assignedProcessPointGroupObjectTablePane,"span,wrap");
		pane.setId("mig-pane");
		pane.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.35);
		pane.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.35);
		pane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.35);
		return pane;
	}

	private ObjectTablePane<RegionalCode> createAvailableProcessPointGroupObjectTablePane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Regional Value", "id.regionalValue").put("Regional Value Name", "regionalValueName")
				.put("Abbreviation", "regionalValueAbbr").put("Description", "regionalValueDesc");
		ObjectTablePane<RegionalCode> panel = new ObjectTablePane<RegionalCode>(columnMappingList);
		panel.setPrefWidth(0.45*Screen.getPrimary().getVisualBounds().getWidth());
		panel.setConstrainedResize(false);
		panel.getTable().getColumns().get(0).setPrefWidth(panel.getPrefWidth()*0.2);
		panel.getTable().getColumns().get(1).setPrefWidth(panel.getPrefWidth()*0.2);
		panel.getTable().getColumns().get(2).setPrefWidth(panel.getPrefWidth()*0.2);
		panel.getTable().getColumns().get(3).setPrefWidth(panel.getPrefWidth()*0.3);
		panel.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.45);
		panel.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.45);
		panel.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.45);
		return panel;
	}
	
	private ObjectTablePane<RegionalProcessPointGroupDto> createAssignedProcessPointGroupObjectTablePane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Regional Value", "regionalValue").put("Regional Value Name", "regionalValueName")
				.put("Abbreviation", "regionalValueAbbr").put("Description", "regionalValueDesc");		
		ObjectTablePane<RegionalProcessPointGroupDto> panel = new ObjectTablePane<RegionalProcessPointGroupDto>(columnMappingList);
		panel.setPrefWidth(0.45*Screen.getPrimary().getVisualBounds().getWidth());
		panel.setConstrainedResize(false);
		panel.getTable().getColumns().get(0).setPrefWidth(panel.getPrefWidth()*0.2);
		panel.getTable().getColumns().get(1).setPrefWidth(panel.getPrefWidth()*0.2);
		panel.getTable().getColumns().get(2).setPrefWidth(panel.getPrefWidth()*0.2);
		panel.getTable().getColumns().get(3).setPrefWidth(panel.getPrefWidth()*0.3);
		panel.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.4);
		panel.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.4);
		panel.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.4);
		return panel;
	}

	@SuppressWarnings("unchecked")
	public void onTabSelected() {
		String selectedCategoryCode = (String)categoryCodeComboBox.getSelectionModel().getSelectedItem();
		int selectedCategoryCodeIndex = 0;
		categoryCodeComboBox.getItems().clear();
		List<RegionalCode> categoryCodeList = getModel().findAllCategoryCode();
		for (int i = 0; i < categoryCodeList.size(); i++) {
			RegionalCode categoryCode = categoryCodeList.get(i);
			String item = categoryCode.getId().getRegionalValue() + " - " + categoryCode.getRegionalValueName();
			categoryCodeComboBox.getItems().add(item);
			if (item.equals(selectedCategoryCode)) {
				selectedCategoryCodeIndex = i;
			}
		}
		categoryCodeComboBox.getSelectionModel().select(selectedCategoryCodeIndex); //default to first item or previous selected
		
		String selectedSite = (String)siteComboBox.getSelectionModel().getSelectedItem();
		int selectedSiteIndex = -1;
		siteComboBox.getItems().clear();
		List<String> siteList = getModel().findAllSite();
		for (int i = 0; i < siteList.size(); i++) {
			String site = siteList.get(i);
			siteComboBox.getItems().add(site);
			if (site.equals(selectedSite)) {
				selectedSiteIndex = i;
			}
		}
		siteComboBox.getSelectionModel().select(selectedSiteIndex); //default to no selected or previous selected
		
		reload(StringUtils.trim(filterTextField.getText()));
		getController().clearDisplayMessage();
	}

	public String getScreenName() {
		return "Process Point Group";
	}

	@Override
	public void reload() {
		reload(StringUtils.trim(filterTextField.getText()));
	}

	public void reload(String filter) {
		String site = getSite();
		if (site != null) {
			short categoryCodeShort = getCategoryCode();
			if (categoryCodeShort != -1) {
				List<RegionalCode> availableProcessPointGroupList = getModel().findAvailableProcessPointGroupByFilter(categoryCodeShort, site, filter);
				List<RegionalProcessPointGroupDto> assignedProcessPointGroupList = getModel().findAssignedProcessPointGroup(categoryCodeShort, site);
				availableProcessPointGroupObjectTablePane.setData(availableProcessPointGroupList);
				assignedProcessPointGroupObjectTablePane.setData(assignedProcessPointGroupList);
			}
		}
	}
	
	public String getSite() {
		return StringUtils.trim((String) siteComboBox.getSelectionModel().getSelectedItem());
	}
	
	public short getCategoryCode() {
		String categoryCodeString = (String) categoryCodeComboBox.getSelectionModel().getSelectedItem();
		if (categoryCodeString != null) {
			return new Short(categoryCodeString.substring(0, categoryCodeString.indexOf(" - "))).shortValue();
		} else {
			return -1;
		}
	}
	
	@Override
	public void start() {
	}

	public LoggedComboBox<String> getCategoryCodeComboBox() {
		return categoryCodeComboBox;
	}

	public LoggedButton getSaveBtn() {
		return saveBtn;
	}

	public LoggedButton getLeftShiftBtn() {
		return leftArrowBtn;
	}

	public LoggedButton getRightShiftBtn() {
		return rightArrowBtn;
	}

	public UpperCaseFieldBean getFilterTextField() {
		return filterTextField;
	}

	public ObjectTablePane<RegionalCode> getAvailableProcessPointGroupObjectTablePane() {
		return availableProcessPointGroupObjectTablePane;
	}

	public ObjectTablePane<RegionalProcessPointGroupDto> getAssignedProcessPointGroupObjectTablePane() {
		return assignedProcessPointGroupObjectTablePane;
	}

	public LoggedComboBox<String> getSiteComboBox() {
		return siteComboBox;
	}
}
