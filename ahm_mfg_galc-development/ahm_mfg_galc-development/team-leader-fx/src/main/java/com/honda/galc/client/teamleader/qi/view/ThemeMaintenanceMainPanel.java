package com.honda.galc.client.teamleader.qi.view;


import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.ThemeMaintenanceMainController;
import com.honda.galc.client.teamleader.qi.model.ThemeMaintenanceModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiTheme;
import com.honda.galc.entity.qi.QiThemeGroup;
import com.honda.galc.entity.qi.QiThemeGrouping;

public class ThemeMaintenanceMainPanel extends QiAbstractTabbedView<ThemeMaintenanceModel, ThemeMaintenanceMainController> {

	private ObjectTablePane<QiTheme> themeTablePane;
	private ObjectTablePane<QiThemeGroup> themeGroupTablePane; 
	private ObjectTablePane<QiThemeGrouping> themeGroupingTablePane;
	private double width;
	private LoggedRadioButton themeGrpAllRadioBtn;
	private LoggedRadioButton themeGrpActiveRadioBtn;
	private LoggedRadioButton themeGrpInactiveRadioBtn;
	private LoggedRadioButton themeAllRadioBtn;
	private LoggedRadioButton themeActiveRadioBtn;
	private LoggedRadioButton themeInactiveRadioBtn;

	public ThemeMaintenanceMainPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
	}

	public void initView(){

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		width = primaryScreenBounds.getWidth()/2;
		getMainWindow().getStylesheets().add(com.honda.galc.client.utils.QiConstant.CSS_PATH);

		VBox mainPane = new VBox();
		HBox themeGroupPane = new HBox();
		HBox themePane = new HBox();
		HBox upperBox = new HBox();
		themeGroupPane.getChildren().add(createTitiledPane("Theme Group", createThemeGroupPanel()));
		themePane.getChildren().add(createTitiledPane("Theme", createThemePanel()));

		LoggedTableColumn<QiThemeGroup, Integer> column = new LoggedTableColumn<QiThemeGroup, Integer>();

		createSerialNumber(column);
		themeGroupTablePane.getTable().getColumns().add(0, column);
		themeGroupTablePane.getTable().getColumns().get(0).setText("#");
		themeGroupTablePane.getTable().getColumns().get(0).setResizable(true);
		themeGroupTablePane.getTable().getColumns().get(0).setMaxWidth(100);
		themeGroupTablePane.getTable().getColumns().get(0).setMinWidth(1);

		LoggedTableColumn<QiTheme, Integer> columnRow = new LoggedTableColumn<QiTheme, Integer>();

		createSerialNumber(columnRow);
		themeTablePane.getTable().getColumns().add(0, columnRow);
		themeTablePane.getTable().getColumns().get(0).setText("#");
		themeTablePane.getTable().getColumns().get(0).setResizable(true);
		themeTablePane.getTable().getColumns().get(0).setMaxWidth(100);
		themeTablePane.getTable().getColumns().get(0).setMinWidth(1);

		themeGroupingTablePane = createThemeGroupingTablePane();

		upperBox.getChildren().addAll(themeGroupPane,themePane);
		upperBox.setSpacing(10);
		mainPane.getChildren().addAll(upperBox,themeGroupingTablePane);
		mainPane.setPadding(new Insets(10));
		mainPane.setSpacing(10);
		this.setCenter(mainPane);
	}

	/**
	 * This method is used to create Theme Group panel.
	 * @return
	 */
	private MigPane createThemeGroupPanel() {
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");

		HBox radioBtnThemeGroup = createFilterRadioButtonsForThemeGroup();
		pane.add(radioBtnThemeGroup,"span,wrap");
		themeGroupTablePane = createThemeGroupTablePane();
		pane.add(themeGroupTablePane,"span,wrap");	
		pane.setId("themeGroupTablePane");
		pane.getStyleClass().add("mig-pane");

		return pane;
	}

	private HBox createFilterRadioButtonsForThemeGroup() {
		HBox radioBtnContainer = new HBox();
		ToggleGroup group = new ToggleGroup();
		themeGrpAllRadioBtn = createRadioButton(QiConstant.ALL, group, true, getController());
		themeGrpAllRadioBtn.setId("THEME_GROUP_ALL");
		themeGrpActiveRadioBtn = createRadioButton(QiConstant.ACTIVE, group, false, getController());
		themeGrpActiveRadioBtn.setId("THEME_GROUP_ACTIVE");
		themeGrpInactiveRadioBtn = createRadioButton(QiConstant.INACTIVE, group, false, getController());
		themeGrpInactiveRadioBtn.setId("THEME_GROUP_INACTIVE");

		radioBtnContainer.getChildren().addAll(themeGrpAllRadioBtn, themeGrpActiveRadioBtn, themeGrpInactiveRadioBtn);
		radioBtnContainer.setAlignment(Pos.CENTER_LEFT);
		radioBtnContainer.setSpacing(10);
		radioBtnContainer.setPadding(new Insets(0, 0, 0, 10));
		radioBtnContainer.setPrefWidth(width);
		return radioBtnContainer;
	}

	/**
	 * This method is used to create Theme panel.
	 * @return
	 */
	private MigPane createThemePanel() {
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");

		HBox radioBtnTheme = createFilterRadioButtonsForTheme();
		pane.add(radioBtnTheme,"span,wrap");
		themeTablePane = createThemeTablePane();
		pane.add(themeTablePane,"span,wrap");	
		pane.setId("themeTablePane");
		pane.getStyleClass().add("mig-pane");

		return pane;
	}

	private HBox createFilterRadioButtonsForTheme() {
		HBox radioBtnContainer = new HBox();
		ToggleGroup group = new ToggleGroup();
		themeAllRadioBtn = createRadioButton(QiConstant.ALL, group, true, getController());
		themeAllRadioBtn.setId("THEME_ALL");
		themeActiveRadioBtn = createRadioButton(QiConstant.ACTIVE, group, false, getController());
		themeActiveRadioBtn.setId("THEME_ALL");
		themeInactiveRadioBtn = createRadioButton(QiConstant.INACTIVE, group, false, getController());
		themeInactiveRadioBtn.setId("THEME_ALL");

		radioBtnContainer.getChildren().addAll(themeAllRadioBtn, themeActiveRadioBtn, themeInactiveRadioBtn);
		radioBtnContainer.setAlignment(Pos.CENTER_LEFT);
		radioBtnContainer.setSpacing(10);
		radioBtnContainer.setPadding(new Insets(0, 0, 0, 10));
		radioBtnContainer.setPrefWidth(width);
		return radioBtnContainer;
	}

	private ObjectTablePane<QiThemeGroup> createThemeGroupTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Theme Group Name", "themeGroupName")
				.put("Theme Group Description", "themeGroupDescription")
				.put("Status","status");

		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] {
				0.16, 0.15, 0.10
		}; 
		ObjectTablePane<QiThemeGroup> panel = new ObjectTablePane<QiThemeGroup>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		return panel;
	}

	/**
	 * This method is used to create TitledPane for Theme and Theme Group panel.
	 * @param title
	 * @param content
	 * @return
	 */
	private TitledPane createTitiledPane(String title,Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		titledPane.setPrefSize(830,350);
		return titledPane;
	}

	private ObjectTablePane<QiTheme> createThemeTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Theme Name", "themeName")
				.put("Theme Description", "themeDescription")
				.put("Status","status");

		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] {
				0.16, 0.15, 0.10
		}; 
		ObjectTablePane<QiTheme> panel = new ObjectTablePane<QiTheme>(columnMappingList,columnWidth);

		panel.setConstrainedResize(false);
		return panel;
	}

	private ObjectTablePane<QiThemeGrouping> createThemeGroupingTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Theme Group Name", "themeGroupName")
				.put("Theme Name", "themeName");

		//Fix : Resizing a column shrunks other columns & headers misaligned with column data.
		Double[] columnWidth = new Double[] {
				0.490, 0.485
		}; 
		ObjectTablePane<QiThemeGrouping> panel = new ObjectTablePane<QiThemeGrouping>(columnMappingList,columnWidth);

		panel.setConstrainedResize(false);
		return panel;
	}

	public void onTabSelected() {
		reload();
	}

	@Override
	public ViewId getViewId() {
		return null;
	}

	public String getScreenName() {
		return "Theme";
	}

	@Override
	public void reload() {
		List<QiTheme> themeSelectedItem = new ArrayList<QiTheme>();
		List<QiThemeGroup> themeGroupSelectedItem = new ArrayList<QiThemeGroup>();
		themeSelectedItem.addAll(getThemeTablePane().getSelectedItems());
		themeGroupSelectedItem.addAll(getThemeGroupTablePane().getSelectedItems());
		reloadThemeGroup();
		reloadTheme();
		for(QiTheme qiTheme :  themeSelectedItem)
			getThemeTablePane().getTable().getSelectionModel().select(qiTheme);
		for(QiThemeGroup qiThemeGroup :  themeGroupSelectedItem)
			getThemeGroupTablePane().getTable().getSelectionModel().select(qiThemeGroup);

	}

	/**
	 * Reload theme group.
	 */
	public void reloadThemeGroup() {
		themeGroupTablePane.setData(getModel().findAllThemeGroup(getSelectedRadioButtonValueForThemeGroup()));
	}

	/**
	 * Reload theme.
	 */
	public void reloadTheme() {
		themeTablePane.setData(getModel().findAllTheme(getSelectedRadioButtonValueForTheme()));
	}

	/**
	 * Reload theme grouping.
	 *
	 * @param themeGroupNameList the theme group name list
	 * @param themeNameList the theme name list
	 */
	public void reloadThemeGrouping(List<String> themeGroupNameList, List<String> themeNameList) {
		themeGroupingTablePane.setData(getModel().findThemeGrouping(themeGroupNameList, themeNameList));
	}

	/**
	 * This method return a list of values based on selected radio buttons (e.g. Active - 1, Inactive - 0, All - 0 & 1)
	 * @return
	 */
	public List<Short> getSelectedRadioButtonValueForThemeGroup() {
		List<Short> statusList = new ArrayList<Short>();
		if(getThemeGrpAllRadioBtn().isSelected()) {
			statusList.add((short)1);
			statusList.add((short)0);
		} else {
			if(getThemeGrpActiveRadioBtn().isSelected())
				statusList.add((short)1);
			else
				statusList.add((short)0);
			statusList.add((short)2);
		}
		return statusList;
	}

	/**
	 * This method return a list of values based on selected radio buttons (e.g. Active - 1, Inactive - 0, All - 0 & 1)
	 * @return
	 */
	public List<Short> getSelectedRadioButtonValueForTheme() {
		List<Short> statusList = new ArrayList<Short>();
		if(getThemeAllRadioBtn().isSelected()) {
			statusList.add((short)1);
			statusList.add((short)0);
		} else {
			if(getThemeActiveRadioBtn().isSelected())
				statusList.add((short)1);
			else
				statusList.add((short)0);
			statusList.add((short)2);
		}
		return statusList;
	}

	@Override
	public void start() {
	}

	public ObjectTablePane<QiThemeGroup> getThemeGroupTablePane() {
		return themeGroupTablePane;
	}

	public ObjectTablePane<QiTheme> getThemeTablePane() {
		return themeTablePane;
	}

	public ObjectTablePane<QiThemeGrouping> getThemeGroupingTablePane() {
		return themeGroupingTablePane;
	}

	public void setThemeGroupingTablePane(
			ObjectTablePane<QiThemeGrouping> themeGroupingTablePane) {
		this.themeGroupingTablePane = themeGroupingTablePane;
	}

	public LoggedRadioButton getThemeGrpAllRadioBtn() {
		return themeGrpAllRadioBtn;
	}

	public void setThemeGrpAllRadioBtn(LoggedRadioButton themeGrpAllRadioBtn) {
		this.themeGrpAllRadioBtn = themeGrpAllRadioBtn;
	}

	public LoggedRadioButton getThemeGrpActiveRadioBtn() {
		return themeGrpActiveRadioBtn;
	}

	public void setThemeGrpActiveRadioBtn(LoggedRadioButton themeGrpActiveRadioBtn) {
		this.themeGrpActiveRadioBtn = themeGrpActiveRadioBtn;
	}

	public LoggedRadioButton getThemeGrpInactiveRadioBtn() {
		return themeGrpInactiveRadioBtn;
	}

	public void setThemeGrpInactiveRadioBtn(
			LoggedRadioButton themeGrpInactiveRadioBtn) {
		this.themeGrpInactiveRadioBtn = themeGrpInactiveRadioBtn;
	}

	public LoggedRadioButton getThemeAllRadioBtn() {
		return themeAllRadioBtn;
	}

	public void setThemeAllRadioBtn(LoggedRadioButton themeAllRadioBtn) {
		this.themeAllRadioBtn = themeAllRadioBtn;
	}

	public LoggedRadioButton getThemeActiveRadioBtn() {
		return themeActiveRadioBtn;
	}

	public void setThemeActiveRadioBtn(LoggedRadioButton themeActiveRadioBtn) {
		this.themeActiveRadioBtn = themeActiveRadioBtn;
	}

	public LoggedRadioButton getThemeInactiveRadioBtn() {
		return themeInactiveRadioBtn;
	}

	public void setThemeInactiveRadioBtn(LoggedRadioButton themeInactiveRadioBtn) {
		this.themeInactiveRadioBtn = themeInactiveRadioBtn;
	}

}

