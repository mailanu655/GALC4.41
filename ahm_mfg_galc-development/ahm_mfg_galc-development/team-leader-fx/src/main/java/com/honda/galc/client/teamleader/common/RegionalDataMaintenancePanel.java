package com.honda.galc.client.teamleader.common;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.mvc.AbstractTabbedView;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.conf.RegionalCode;

public class RegionalDataMaintenancePanel extends AbstractTabbedView<RegionalDataMaintenanceModel, RegionalDataMaintenanceController> {

	private ObjectTablePane<RegionalCode> regionalCodeTablePane;
	private ObjectTablePane<RegionalCode> regionalValueTablePane;

	public RegionalDataMaintenancePanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
	}

	/**
	 * This method is used to initialize the components of panel
	 */
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		final HBox mainContainer = new HBox();
		mainContainer.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
		mainContainer.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.15);
		mainContainer.getChildren().addAll(createRegionalCodeVBox(), createRegionalValueVBox());
		this.setCenter(mainContainer);
	}

	private VBox createRegionalCodeVBox() {
		final VBox regionalCodeVbox = new VBox();
		regionalCodeVbox.setPadding(new Insets(0, 0, 0, 0));
		regionalCodeVbox.getChildren().addAll(createTitledPane("Regional Code Name", createRegionalCodeMigPane()));
		return regionalCodeVbox;
	}
	
	private TitledPane createTitledPane(String title,Node content) {
		final TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		return titledPane;
	}
	
	private MigPane createRegionalCodeMigPane() {
		final MigPane pane = new MigPane("insets 0 0 0 0", "[center, grow]", "");
		regionalCodeTablePane = createRegionalCodeObjectTablePane();
		pane.add(regionalCodeTablePane);
		pane.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth()/3);
		pane.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth()/3);
		pane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth()/3);
		pane.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.15);
		pane.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.15);
		pane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.15);
		return pane;
	}
	
	private ObjectTablePane<RegionalCode> createRegionalCodeObjectTablePane(){
		final ColumnMappingList columnMappingList = ColumnMappingList.with("Regional Code Name", "id.regionalCodeName");
		final Double[] columnWidth = new Double[] {0.3};
		final ObjectTablePane<RegionalCode> pane = new ObjectTablePane<RegionalCode>(columnMappingList, columnWidth);
		pane.setConstrainedResize(false);
		pane.setId("ReginalCodeObjectTablePane");
		LoggedTableColumn<RegionalCode, Integer> serialNoColRegionalCode = new LoggedTableColumn<RegionalCode, Integer>();
		createSerialNumber(serialNoColRegionalCode);
		pane.getTable().getColumns().add(0, serialNoColRegionalCode);
		pane.getTable().getColumns().get(0).setText("#");
		pane.getTable().getColumns().get(0).setResizable(true);
		pane.getTable().getColumns().get(0).setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth()/30);
		pane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.15);
		pane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth()/3);		
		return pane;
	}

	private VBox createRegionalValueVBox() {
		final VBox regionalValueVBox = new VBox();
		regionalValueVBox.setPadding(new Insets(0, 0, 0, 0));
		regionalValueVBox.getChildren().addAll(createTitledPane("Regional Value", createRegionalValueMigPane()));
		return regionalValueVBox;
	}
	
	private MigPane createRegionalValueMigPane() {
		final MigPane pane = new MigPane("insets 0 0 0 0","[center,grow]","");
		regionalValueTablePane = createRegionalValueObjectTablePane();
		pane.add(regionalValueTablePane);
		pane.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth()/1.5);
		pane.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth()/1.5);
		pane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth()/1.5);
		pane.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.15);
		pane.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.15);
		pane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.15);
		return pane;
	}
	
	private ObjectTablePane<RegionalCode> createRegionalValueObjectTablePane(){
		final ColumnMappingList columnMappingList = ColumnMappingList.with("Regional Value", "id.regionalValue")
				.put("Regional Value Name", "regionalValueName").put("Regional Value Abbreviation", "regionalValueAbbr")
				.put("Regional Value Description", "regionalValueDesc");
		final Double[] columnWidth = new Double[] {0.15, 0.15, 0.15, 0.15};
		final ObjectTablePane<RegionalCode> pane = new ObjectTablePane<RegionalCode>(columnMappingList,columnWidth);
		pane.setConstrainedResize(false);
		pane.setId("ReginalValueObjectTablePane");
		LoggedTableColumn<RegionalCode, Integer> serialNoColRegionalValue = new LoggedTableColumn<RegionalCode, Integer>();
		createSerialNumber(serialNoColRegionalValue);
		pane.getTable().getColumns().add(0, serialNoColRegionalValue);
		pane.getTable().getColumns().get(0).setText("#");
		pane.getTable().getColumns().get(0).setResizable(true);
		pane.getTable().getColumns().get(0).setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth()/15);
		pane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.15);
		pane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth()/1.5);
		return pane;
	}
	
	private void loadData() {
		try {
			this.regionalCodeTablePane.setData(getModel().findAllDistinctRegionalCode());
			this.regionalValueTablePane.getTable().getItems().clear();
		} catch (Exception exception) {
			handleException(exception);
		}
	}

	public void onTabSelected() {
		getController().clearMessages();
		getController().activate();
		reload();
	}

	/**
	 * This method is used to return name for the tab in main screen 
	 */
	public String getScreenName() {
		return "Regional Data";
	}

	@Override
	public void reload() {
		loadData();
	}

	@Override
	public void start() {
	}

	public ObjectTablePane<RegionalCode> getRegionalCodeTablePane() {
		return regionalCodeTablePane;
	}

	public ObjectTablePane<RegionalCode> getRegionalValueTablePane() {
		return regionalValueTablePane;
	}
}