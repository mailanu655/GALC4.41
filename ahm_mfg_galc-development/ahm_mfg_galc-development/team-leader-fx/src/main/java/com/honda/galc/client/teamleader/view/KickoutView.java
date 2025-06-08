/**
 * <h1> View Class for Kickout Teamleader Screen</h1>
 * 
 *
 * @author Bradley Brown
 * @version 1.0
 * @since 2.42
 */
package com.honda.galc.client.teamleader.view;

import java.awt.event.KeyEvent;

import com.honda.galc.client.ClientConstants;
import com.honda.galc.client.mvc.AbstractTabPane;
import com.honda.galc.client.product.entry.GenericProductEntryTabPane;
import com.honda.galc.client.product.pane.ProcessPointSelectionPane;
import com.honda.galc.client.product.pane.ReasonEntryPane;
import com.honda.galc.client.teamleader.controller.KickoutViewController;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedMenuItem;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dto.KickoutDto;
import com.honda.galc.property.KickoutPropertyBean;
import com.honda.galc.service.property.PropertyService;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class KickoutView extends AbstractTabPane {
	private static final String PRODUCT_SEARCH_PANE = "Product Search";
	private static final String RELEASE_KICKOUT = "Release Kickout";
	private static final String CREATE_KICKOUT = "Create Kickout";

	private KickoutViewController controller;

	private GenericProductEntryTabPane productInputPane;
	private ProcessPointSelectionPane processPointSelectionPane;
	private ReasonEntryPane kickoutReasonEntryPane;
	private ReasonEntryPane releaseReasonEntryPane;
	private TabPane addRemoveKickoutTabPane;
	private ObjectTablePane<KickoutDto> kickoutTable;
	private ObjectTablePane<KickoutDto> removeKickoutTable;
	private Tab kickoutTab;
	private Tab removeKickoutTab;

	private ContextMenu kickoutTableMenu;
	private ContextMenu removeKickoutTableMenu;

	private LoggedMenuItem kickoutAllMenuItem;
	private LoggedMenuItem kickoutSelectedMenuItem;
	private LoggedMenuItem removeKickoutAllMenuItem;
	private LoggedMenuItem removeKickoutSelectedMenuItem;
	private LoggedMenuItem deselectKickoutMenuItem;

	private LoggedMenuItem releaseKickoutAllMenuItem;
	private LoggedMenuItem releaseKickoutSelectedMenuItem;
	private LoggedMenuItem releaseKickoutRemoveAllMenuItem;
	private LoggedMenuItem releaseKickoutRemoveSelectedMenuItem;
	private LoggedMenuItem deselectReleaseMenuItem;

	public KickoutView(MainWindow mainWindow) {
		super("Kickout View", KeyEvent.VK_V, mainWindow);
		controller = new KickoutViewController(this);
		init();
		controller.init();
	}

	private void init() {
		this.prefHeightProperty().bind(getMainWindow().heightProperty());
		this.prefWidthProperty().bind(getMainWindow().widthProperty());

		this.setCenter(createCenterPane());
		createKickoutTableMenu();
		createRemoveKickoutTableMenu();
	}

	private TitledPane createProductSearchPane() {
		TitledPane titledPane = new TitledPane();
		titledPane.setCollapsible(false);
		titledPane.prefHeightProperty().bind(this.heightProperty().multiply(0.21));
		titledPane.prefWidthProperty().bind(this.widthProperty().multiply(0.98));
		titledPane.setMinHeight(getMainWindow().getHeight() * 0.15);

		titledPane.setText(PRODUCT_SEARCH_PANE);
		productInputPane = new GenericProductEntryTabPane(this);
		productInputPane.prefHeightProperty().bind(this.heightProperty().multiply(0.14));
		productInputPane.minHeightProperty().bind(this.heightProperty().multiply(0.13));
		productInputPane.prefWidthProperty().bind(this.widthProperty().multiply(0.98));

		titledPane.setContent(productInputPane);
		return titledPane;
	}

	private Node createCenterPane() {
		VBox box = new VBox();
		box.prefHeightProperty().bind(this.widthProperty());
		box.getChildren().addAll(createProductSearchPane(), createAddRemoveKickoutTabPane());
		return box;
	}

	private TabPane createAddRemoveKickoutTabPane() {
		addRemoveKickoutTabPane = new TabPane();
		kickoutTab = new Tab(CREATE_KICKOUT);
		kickoutTab.setId(ClientConstants.KICKOUT_TAB_ID);
		kickoutTab.setContent(createKickoutTab());
		kickoutTab.setClosable(false);
		removeKickoutTab = new Tab(RELEASE_KICKOUT);
		removeKickoutTab.setId(ClientConstants.REMOVE_KICKOUT_TAB);
		removeKickoutTab.setClosable(false);
		addRemoveKickoutTabPane.getTabs().addAll(kickoutTab,removeKickoutTab);
		removeKickoutTab.setContent(createRemoveKickoutTab());
		addRemoveKickoutTabPane.setStyle(getBorderStyle());
		addRemoveKickoutTabPane.prefHeightProperty().bind(this.heightProperty().multiply(0.79));
		return addRemoveKickoutTabPane;
	}

	private VBox createKickoutTab() {
		VBox box = new VBox();
		kickoutReasonEntryPane = createReasonEntryPane();
		kickoutReasonEntryPane.setId(ClientConstants.KICKOUT_REASON_PANE_ID);
		box.prefHeightProperty().bind(this.heightProperty().multiply(0.75));
		box.prefWidthProperty().bind(this.widthProperty());
		box.getChildren().addAll(createProcessPointSelectionPanel(), kickoutReasonEntryPane, createKickoutTablePane());
		return box;
	}

	private TitledPane createProcessPointSelectionPanel() {
		TitledPane titledPane = new TitledPane();
		titledPane.minWidthProperty().bind(this.widthProperty().multiply(0.98));
		titledPane.prefHeightProperty().bind(this.heightProperty().multiply(0.13));
		titledPane.setText("Kickout Location");
		titledPane.setCollapsible(false);
		boolean isFilterKickout = PropertyService
				.getPropertyBean(KickoutPropertyBean.class, getMainWindow().getApplicationContext().getApplicationId())
				.isFilterKickoutProcess();
		processPointSelectionPane = new ProcessPointSelectionPane(addRemoveKickoutTabPane, false, true, true,
				isFilterKickout);
		processPointSelectionPane.setStyle(getBorderStyle());
		titledPane.setContent(processPointSelectionPane);
		return titledPane;
	}

	private Node createRemoveKickoutTab() {
		VBox box = new VBox();
		releaseReasonEntryPane = createReasonEntryPane();
		releaseReasonEntryPane.setId(ClientConstants.RELEASE_KICKOUT_REASON_PANE_ID);
		box.getChildren().addAll(releaseReasonEntryPane, createRemoveKickoutTablePane());
		return box;
	}

	private ReasonEntryPane createReasonEntryPane() {
		ReasonEntryPane pane = new ReasonEntryPane(addRemoveKickoutTabPane, true);
		pane.prefWidthProperty().bind(this.widthProperty().multiply(0.98));
		pane.prefHeightProperty().bind(this.heightProperty().multiply(0.08));
		pane.setStyle(getBorderStyle());
		return pane;
	}

	private ObjectTablePane<KickoutDto> createKickoutTablePane() {
		ColumnMappingList columnMappingList;
		if(controller.isDcProduct()) {
			columnMappingList = createKickoutColumnsDc();
		} else {
			columnMappingList = createKickoutColumns();
		}
		kickoutTable = new ObjectTablePane<KickoutDto>(columnMappingList, getcolumnWidth(columnMappingList));
		kickoutTable.prefHeightProperty().bind(this.heightProperty().multiply(0.60));
		kickoutTable.setSelectionMode(SelectionMode.MULTIPLE);
		return kickoutTable;
	}

	private ObjectTablePane<KickoutDto> createRemoveKickoutTablePane() {
		ColumnMappingList columnMappingList;
		if(controller.isDcProduct()) {
			columnMappingList = createRemoveKickoutColumnsDc();
		}else {
			columnMappingList = createRemoveKickoutColumns();
		}
		removeKickoutTable = new ObjectTablePane<KickoutDto>(columnMappingList, getcolumnWidth(columnMappingList));
		removeKickoutTable.prefHeightProperty().bind(this.heightProperty().multiply(0.70));
		removeKickoutTable.setSelectionMode(SelectionMode.MULTIPLE);
		return removeKickoutTable;
	}

	private ColumnMappingList createKickoutColumns() {
		return ColumnMappingList.with("Product Id", "productId")
				.put("Product Spec Code", "productSpecCode")
				.put("Last Passing Process Point", "lastPassingProcessPointName");
	}

	private ColumnMappingList createKickoutColumnsDc() {
		return ColumnMappingList.with("Product Id", "productId")
				.put("MC Serial Numner", "mcSerialNumber")
				.put("DC Serial Number", "dcSerialNumber")
				.put("Product Spec Code", "productSpecCode")
				.put("Dunnage", "dunnage")
				.put("Last Passing Process Point", "lastPassingProcessPointName");
	}

	private ColumnMappingList createRemoveKickoutColumns() {
		return ColumnMappingList.with("Product Id", "productId")
				.put("Kickout Reason", "description")
				.put("Comment", "comment")
				.put("Department", "divisionName")
				.put("Process Point Name", "processPointName")
				.put("Kickout User", "kickoutUser") //NAQICS-1276: show kickout user
				.put("Kickout Status", "kickoutStatusName")
				.put("Timestamp", "createTimestamp");
	}

	private ColumnMappingList createRemoveKickoutColumnsDc() {
		return ColumnMappingList.with("Product Id", "productId")
				.put("MC Serial Numner", "mcSerialNumber")
				.put("DC Serial Number", "dcSerialNumber")
				.put("Dunnage", "dunnage")
				.put("Kickout Reason", "description")
				.put("Comment", "comment")
				.put("Department", "divisionName")
				.put("Process Point Name", "processPointName")
				.put("Kickout User", "kickoutUser") //NAQICS-1276: show kickout user
				.put("Kickout Status", "kickoutStatusName")
				.put("Timestamp", "createTimestamp");
	}

	private void createKickoutTableMenu() {
		if(kickoutTableMenu == null) {
			kickoutTableMenu = new ContextMenu();
		}
		kickoutAllMenuItem = new LoggedMenuItem("Kickout All Products");
		kickoutAllMenuItem.setId(ClientConstants.KICKOUT_ALL_ID);
		kickoutAllMenuItem.setStyle(getContextMenuItemPadding());
		kickoutSelectedMenuItem = new LoggedMenuItem("Kickout Selected Products");
		kickoutSelectedMenuItem.setId(ClientConstants.KICKOUT_SELECTED_ID);
		kickoutSelectedMenuItem.setStyle(getContextMenuItemPadding());
		removeKickoutAllMenuItem = new LoggedMenuItem("Clear All Products");
		removeKickoutAllMenuItem.setId(ClientConstants.REMOVE_ALL_KICKOUT_ID);
		removeKickoutAllMenuItem.setStyle(getContextMenuItemPadding());
		removeKickoutSelectedMenuItem = new LoggedMenuItem("Clear Selected Products");
		removeKickoutSelectedMenuItem.setId(ClientConstants.REMOVE_SELECTED_KICKOUT_ID);
		removeKickoutSelectedMenuItem.setStyle(getContextMenuItemPadding());
		deselectKickoutMenuItem = new LoggedMenuItem("Deselect Selected Products");
		deselectKickoutMenuItem.setId(ClientConstants.DESELECT_KICKOUT_ID);
		deselectKickoutMenuItem.setStyle(getContextMenuItemPadding());

		kickoutTableMenu.getItems().addAll(
				kickoutAllMenuItem,
				kickoutSelectedMenuItem,
				new SeparatorMenuItem(),
				removeKickoutAllMenuItem,
				removeKickoutSelectedMenuItem,
				new SeparatorMenuItem(),
				deselectKickoutMenuItem);

		kickoutTableMenu.setStyle(getContextMenuFontStyle());
	}

	private void createRemoveKickoutTableMenu() {
		if(removeKickoutTableMenu == null) {
			removeKickoutTableMenu = new ContextMenu();
		}
		releaseKickoutAllMenuItem = new LoggedMenuItem("Release All Products");
		releaseKickoutAllMenuItem.setId(ClientConstants.RELEASE_ALL_KICKOUT_ID);
		releaseKickoutAllMenuItem.setStyle(getContextMenuItemPadding());
		releaseKickoutSelectedMenuItem = new LoggedMenuItem("Release Selected Products");
		releaseKickoutSelectedMenuItem.setId(ClientConstants.RELEASE_SELECTED_KICKOUT_ID);
		releaseKickoutSelectedMenuItem.setStyle(getContextMenuItemPadding());
		releaseKickoutRemoveAllMenuItem = new LoggedMenuItem("Clear All Products");
		releaseKickoutRemoveAllMenuItem.setId(ClientConstants.REMOVE_ALL_RELEASE_KICKOUT_ID);
		releaseKickoutRemoveAllMenuItem.setStyle(getContextMenuItemPadding());
		releaseKickoutRemoveSelectedMenuItem = new LoggedMenuItem("Clear Selected Products");
		releaseKickoutRemoveSelectedMenuItem.setId(ClientConstants.REMOVE_SELECTED_RELEASE_KICKOUT_ID);
		releaseKickoutRemoveSelectedMenuItem.setStyle(getContextMenuItemPadding());
		deselectReleaseMenuItem = new LoggedMenuItem("Deselect Selected Products");
		deselectReleaseMenuItem.setId(ClientConstants.DESELECT_RELEASE_KICKOUT_ID);
		deselectReleaseMenuItem.setStyle(getContextMenuItemPadding());

		removeKickoutTableMenu.getItems().addAll(
				releaseKickoutAllMenuItem,
				releaseKickoutSelectedMenuItem,
				new SeparatorMenuItem(),
				releaseKickoutRemoveAllMenuItem,
				releaseKickoutRemoveSelectedMenuItem,
				new SeparatorMenuItem(),
				deselectReleaseMenuItem);

		removeKickoutTableMenu.setStyle(getContextMenuFontStyle());
	}

	@Override
	public void onTabSelected() {
		getLogger().info("Kickout Tab successfully selected.");
	}

	public TabPane getAddRemoveKickoutTabPane() {
		return this.addRemoveKickoutTabPane;
	}

	public ObjectTablePane<KickoutDto> getKickoutTable() {
		return this.kickoutTable;
	}

	public void setKickoutTable(ObjectTablePane<KickoutDto> kickoutTable) {
		this.kickoutTable = kickoutTable;
	}

	public ObjectTablePane<KickoutDto> getRemoveKickoutTable() {
		return this.removeKickoutTable;
	}

	public void setRemoveKickoutTable(ObjectTablePane<KickoutDto> removeKickoutTable) {
		this.removeKickoutTable = removeKickoutTable;
	}

	public Tab getKickoutTab() {
		return this.kickoutTab;
	}

	public Tab getRemoveKickoutTab() {
		return this.removeKickoutTab;
	}

	public ContextMenu getKickoutTableMenu() {
		return this.kickoutTableMenu;
	}

	public void setKickoutTableMenu(ContextMenu kickoutTableMenu) {
		this.kickoutTableMenu = kickoutTableMenu;
	}

	public ContextMenu getRemoveKickoutTableMenu() {
		return this.removeKickoutTableMenu;
	}

	public void setRemoveKickoutTableMenu(ContextMenu removeKickoutTableMenu) {
		this.removeKickoutTableMenu = removeKickoutTableMenu;
	}

	public MenuItem getKickoutAllMenuItem() {
		return this.kickoutAllMenuItem;
	}

	public void setKickoutAllMenuItem(LoggedMenuItem kickoutAllMenuItem) {
		this.kickoutAllMenuItem = kickoutAllMenuItem;
	}

	public LoggedMenuItem getKickoutSelectedMenuItem() {
		return this.kickoutSelectedMenuItem;
	}

	public void setKickoutSelectedMenuItem(LoggedMenuItem kickoutSelectedMenuItem) {
		this.kickoutSelectedMenuItem = kickoutSelectedMenuItem;
	}

	public LoggedMenuItem getRemoveKickoutAllMenuItem() {
		return this.removeKickoutAllMenuItem;
	}

	public void setRemoveKickoutAllMenuItem(LoggedMenuItem removeKickoutAllMenuItem) {
		this.removeKickoutAllMenuItem = removeKickoutAllMenuItem;
	}

	public LoggedMenuItem getRemoveKickoutSelectedMenuItem() {
		return this.removeKickoutSelectedMenuItem;
	}

	public void setRemoveKickoutSelectedMenuItem(LoggedMenuItem removeKickoutSelectedMenuItem) {
		this.removeKickoutSelectedMenuItem = removeKickoutSelectedMenuItem;
	}

	public LoggedMenuItem getDeselectKickoutMenuItem() {
		return this.deselectKickoutMenuItem;
	}

	public void setDeselectKickoutMenuItem(LoggedMenuItem deselectKickoutMenuItem) {
		this.deselectKickoutMenuItem = deselectKickoutMenuItem;
	}

	public LoggedMenuItem getReleaseKickoutAllMenuItem() {
		return this.releaseKickoutAllMenuItem;
	}

	public void setReleaseKickoutAllMenuItem(LoggedMenuItem releaseKickoutAllMenuItem) {
		this.releaseKickoutAllMenuItem = releaseKickoutAllMenuItem;
	}

	public LoggedMenuItem getReleaseKickoutSelectedMenuItem() {
		return this.releaseKickoutSelectedMenuItem;
	}

	public void setReleaseKickoutSelectedMenuItem(LoggedMenuItem releaseKickoutSelectedMenuItem) {
		this.releaseKickoutSelectedMenuItem = releaseKickoutSelectedMenuItem;
	}

	public LoggedMenuItem getReleaseKickoutRemoveAllMenuItem() {
		return this.releaseKickoutRemoveAllMenuItem;
	}

	public void setReleaseKickoutRemoveAllMenuItem(LoggedMenuItem releaseKickoutRemoveAllMenuItem) {
		this.releaseKickoutRemoveAllMenuItem = releaseKickoutRemoveAllMenuItem;
	}

	public LoggedMenuItem getReleaseKickoutRemoveSelectedMenuItem() {
		return this.releaseKickoutRemoveSelectedMenuItem;
	}

	public void setReleaseKickoutRemoveSelectedMenuItem(LoggedMenuItem releaseKickoutRemoveSelectedMenuItem) {
		this.releaseKickoutRemoveSelectedMenuItem = releaseKickoutRemoveSelectedMenuItem;
	}

	public LoggedMenuItem getDeselectReleaseMenuItem() {
		return this.deselectReleaseMenuItem;
	}

	public void setDeselectReleaseMenuItem(LoggedMenuItem deselectReleaseMenuItem) {
		this.deselectReleaseMenuItem = deselectReleaseMenuItem;
	}

	public ReasonEntryPane getReasonEntryPane() {
		return this.kickoutReasonEntryPane;
	}

	public ProcessPointSelectionPane getProcessPointSelectionPane() {
		return this.processPointSelectionPane;
	}

	public GenericProductEntryTabPane getProductInputPane() {
		return this.productInputPane;
	}

	public ReasonEntryPane getReleaseReasonEntryPane() {
		return this.releaseReasonEntryPane;
	}
	
	@Override
	public String getScreenName() {
		return "Kickout View";
	}
}