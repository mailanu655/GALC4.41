package com.honda.galc.client.teamleader.view;

import java.awt.event.KeyEvent;

import com.honda.galc.client.mvc.AbstractTabPane;
import com.honda.galc.client.product.entry.GenericProductEntryTabPane;
import com.honda.galc.client.teamleader.controller.UnscrapViewController;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.dto.ScrappedProductDto;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public class UnscrapView  extends AbstractTabPane  {
	private GenericProductEntryTabPane productScanPane;
	
	private UnscrapViewController controller;

	private LabeledTextField associateIdTextField;
	private LabeledTextField unscrapReasonTextField;

	private ContextMenu scrappedProductsTableMenu;

	private MenuItem unscrapAllMenuItem;
	private MenuItem unscrapSelectedMenuItem;
	private MenuItem removeAllMenuItem;
	private MenuItem removeSelectedMenuItem;
	private MenuItem deselectMenuItem;

	ObjectTablePane<ScrappedProductDto> scrappedProductsTable;

	public UnscrapView(TabbedMainWindow mainWindow) {
		super("UNSCRAP", KeyEvent.VK_U, mainWindow);
		setMainWindow(mainWindow);
		controller = new UnscrapViewController(this);
		init();
		getController().initData();
		getController().initListeners();
		}

	public void init() {
		setScreenWidth(Screen.getPrimary().getVisualBounds().getWidth());
		setScreenHeight(Screen.getPrimary().getVisualBounds().getHeight());
		this.setPrefWidth(getScreenWidth());
		this.setPrefHeight(getScreenHeight());
		initialize();
	}

	private void initialize() {
		this.setTop(createTopView());
		createScrappedProductInfoPane(getController().createScrappedProductTableColumns());

		this.setCenter(createScrappedProductInfoPane(getController().createScrappedProductTableColumns()));
		createScrappedProductsTableMenu();
	}

	private Node createScrappedProductInfoPane(ColumnMappingList columnMappingList) {
		Double[] columnWidth = getcolumnWidth(columnMappingList);

		scrappedProductsTable = new ObjectTablePane<ScrappedProductDto>(columnMappingList, columnWidth);
		scrappedProductsTable.setStyle(getTableFontStyle());
		scrappedProductsTable.setPrefWidth((int) 0.95 * getScreenWidth());
		scrappedProductsTable.setPrefHeight((int) 0.65 * getScreenHeight());
		scrappedProductsTable.setSelectionMode(SelectionMode.MULTIPLE);

		return scrappedProductsTable;
	}

	private Node createTopView() {
		VBox box = new VBox();
		box.getChildren().add(createProductScanPane());
		box.getChildren().add(createReasonPane());
		box.setStyle(getBorderStyle());

		return box;
	}

	private HBox createReasonPane() {
		HBox box = new HBox();

		associateIdTextField = new LabeledTextField("Associate Id :",true, new Insets(15, 30, 15, 5), true, false);
		TextFieldState.DISABLED.setState(associateIdTextField.getControl());
		associateIdTextField.getControl().setPrefWidth(getScreenWidth() * 0.15); 
		associateIdTextField.setStyle(getLabelStyle());

		unscrapReasonTextField = new LabeledTextField("Unscrap Reason :", true, new Insets(15, 5, 15, 5), true, false);
		unscrapReasonTextField.setStyle(getLabelStyle());
		unscrapReasonTextField.getControl().setPrefWidth(getScreenWidth() * 0.55); 

		box.getChildren().addAll(associateIdTextField, unscrapReasonTextField);

		box.setStyle(getBorderStyle());

		return box;
	}

	private GenericProductEntryTabPane createProductScanPane() {
		productScanPane = new GenericProductEntryTabPane(this);
		return productScanPane; 
	}	

	public ColumnMappingList getNonDiecastProductTableColumns() {
		return ColumnMappingList.with(getController().getProductTypeData().getProductIdLabel(), "product.productId")
				.put(getController().getProductTypeData().getProductSpecCodeLabel(), "product.productSpecCode")
				.put("Last Passing \nProcess Point", "processPointName")
				.put("Tracking Status", "product.trackingStatus")
				.put("Scrap Reason", "exceptionalOut.exceptionalOutReasonString")
				.put("Comment", "exceptionalOut.exceptionalOutComment")
				.put("Timestamp", "exceptionalOut.id.actualTimestamp")
				.put("User", "exceptionalout.associateNo");
	}

	public ColumnMappingList getDiecastProductTableColumns() {
		return ColumnMappingList.with(getController().getProductTypeData().getProductIdLabel() + " DC", "product.dcSerialNumber")
				.put(getController().getProductTypeData().getProductIdLabel() + " MC", "product.mcSerialNumber")
				.put(getController().getProductTypeData().getProductSpecCodeLabel(), "product.productSpecCode")
				.put("Last Passing \nProcess Point", "processPointName")
				.put("Tracking Status", "product.trackingStatus")
				.put("Scrap Reason", "exceptionalOut.exceptionalOutReasonString")
				.put("Comment", "exceptionalOut.exceptionalOutComment")
				.put("Timestamp", "exceptionalOut.id.actualTimestamp")
				.put("User", "exceptionalout.associateNo");
	}

	private void createScrappedProductsTableMenu() {
		if(scrappedProductsTableMenu == null) {
			scrappedProductsTableMenu = new ContextMenu();

			unscrapAllMenuItem = new MenuItem("Unscrap All Products");
			unscrapAllMenuItem.setStyle(getContextMenuItemPadding());
			unscrapSelectedMenuItem = new MenuItem("Unscrap Selected Products");
			unscrapSelectedMenuItem.setStyle(getContextMenuItemPadding());
			removeAllMenuItem = new MenuItem("Remove All Products");
			removeAllMenuItem.setStyle(getContextMenuItemPadding());
			removeSelectedMenuItem = new MenuItem("Remove Selected Products");
			removeSelectedMenuItem.setStyle(getContextMenuItemPadding());
			deselectMenuItem = new MenuItem("Deselect Selected Products");
			deselectMenuItem.setStyle(getContextMenuItemPadding());

			scrappedProductsTableMenu.getItems().addAll(
					unscrapAllMenuItem,
					unscrapSelectedMenuItem,
					new SeparatorMenuItem(),
					removeAllMenuItem,
					removeSelectedMenuItem,
					new SeparatorMenuItem(),
					deselectMenuItem);
			
			scrappedProductsTableMenu.setStyle(getContextMenuFontStyle());
		}
	}

	public UnscrapViewController getController() {
		if(controller == null) {
			controller = new UnscrapViewController(this);
		}
		return controller;
	}

	@Override
	public void onTabSelected() {
		getLogger().info("Unscrap tab successfully selected.");
	}


	public LabeledTextField getAssociateIdTextField() {
		return this.associateIdTextField;
	}

	public ObjectTablePane<ScrappedProductDto> getScrappedProductsTable() {
		return this.scrappedProductsTable;
	}

	public ContextMenu getScrappedProductsTableMenu() {
		return this.scrappedProductsTableMenu;
	}

	public void setScrappedProductsTableMenu(ContextMenu scrappedProductsTableMenu) {
		this.scrappedProductsTableMenu = scrappedProductsTableMenu;
	}

	public MenuItem getUnscrapAllMenuItem() {
		return this.unscrapAllMenuItem;
	}

	public void setUnscrapAllMenuItem(MenuItem unscrapAllMenuItem) {
		this.unscrapAllMenuItem = unscrapAllMenuItem;
	}

	public MenuItem getUnscrapSelectedMenuItem() {
		return this.unscrapSelectedMenuItem;
	}

	public void setUnscrapSelectedMenuItem(MenuItem unscrapSelectedMenuItem) {
		this.unscrapSelectedMenuItem = unscrapSelectedMenuItem;
	}

	public MenuItem getRemoveAllMenuItem() {
		return this.removeAllMenuItem;
	}

	public void setRemoveAllMenuItem(MenuItem removeAllMenuItem) {
		this.removeAllMenuItem = removeAllMenuItem;
	}

	public MenuItem getRemoveSelectedMenuItem() {
		return this.removeSelectedMenuItem;
	}

	public void setRemoveSelectedMenuItem(MenuItem removeSelectedMenuItem) {
		this.removeSelectedMenuItem = removeSelectedMenuItem;
	}

	public MenuItem getDeselectMenuItem() {
		return this.deselectMenuItem;
	}

	public void setDeselectMenuItem(MenuItem deselectMenuItem) {
		this.deselectMenuItem = deselectMenuItem;
	}

	public LabeledTextField getUnscrapReasonTextField() {
		return this.unscrapReasonTextField;
	}

	public GenericProductEntryTabPane getProductScanPane() {
		return this.productScanPane;
	}
	
	@Override
	public String getScreenName() {
		return "UNSCRAP";
	}
}
