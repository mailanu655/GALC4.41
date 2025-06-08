package com.honda.galc.client.teamleader.common;

import java.util.List;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.data.ProductSpecData;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.enumtype.ScreenAccessLevel;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.util.Callback;

public abstract class SpecMaintPanel<T extends ProductSpec> extends TabbedPanel implements EventHandler<ActionEvent>{

	private ObjectTablePane<T> specViewData;
	private ProductSpecData productSpecData;
	private String productType;
	
	public SpecMaintPanel(String screenName, MainWindow mainWindow) {
		super(screenName, mainWindow);
		init();
	}

	private void init() {
		this.productType = getProductType();
		initView();
		productSpecData =  new ProductSpecData(productType);
		addListeners();
	}

	@Override
	public void onTabSelected() {
		reload();
	}

	/*
	 * Method sets the object list to view object
	 */
	public void reload(){
		List<T> productSpecCode = (List<T>) productSpecData.getProductSpecs();
		specViewData.setData(productSpecCode);
	}
	/**
	 * Retrieves the product type from configuration
	 * @return product type string
	 */
	protected String getProductType() {
		return ApplicationContext.getInstance().getProductTypeData().getId();
	}
	/**
	 * Positions and appends the serial number to spec view data
	 */
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		specViewData = createSpecTablePane();
		setSerialNumberColumnProperty();
		this.setCenter(specViewData);
	}
	/*
	 * Sets serial numbers to rows on view table
	 */
	private void setSerialNumberColumnProperty() {
		LoggedTableColumn<T, Integer> column = new LoggedTableColumn<T, Integer>();
		createSerialNumber(column);
		column.setText("#");
		column.setResizable(true);
		column.setMaxWidth(100);
		column.setMinWidth(1);
		specViewData.getTable().getColumns().add(0, column);
	}
	
	/**
	 * Create view table for Frame and Engine
	 * @return ObjectTablePane
	 */
	private  ObjectTablePane<T> createSpecTablePane(){ 
		ColumnMappingList columnMappingList = getColumnList();
		Double[] columnWidth = getColumnWidth();
		ObjectTablePane<T> panel = new ObjectTablePane<T>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		return panel;
	}
	/**
	 * Default column width
	 * @return column width array
	 */
	protected Double[] getColumnWidth() {
		return new Double[] {
				0.20, 0.15, 0.15, 0.20, 0.20
		};
	}
	/**
	 * Default column list
	 * @return column list
	 */
	protected ColumnMappingList getColumnList() {
		return ColumnMappingList.with("Product Spec Code", "productSpecCode")
				.put("Model Year Code","modelYearCode")
				.put("Model Code", "modelCode").put("Model type Code", "modelTypeCode")
				.put("Model Option Code", "modelOptionCode");
	}
	/**
	 * Method to create row index for each row
	 * @param rowIndex
	 */
	public void createSerialNumber(LoggedTableColumn rowIndex){
		rowIndex.setCellFactory( new Callback<LoggedTableColumn, LoggedTableCell>(){
			public LoggedTableCell call(LoggedTableColumn p)
			{
				return new LoggedTableCell()
				{
					@Override
					public void updateItem( Object item, boolean empty )
					{
						super.updateItem( item, empty );
						setText( empty ? null : getIndex() + 1 + "" );
					}
				};
			}
	    });
	}
	
	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) event.getSource();
			if(QiConstant.CREATE.equals(menuItem.getText()))
				createOrUpdate(event, null, QiConstant.CREATE);	
			else if(QiConstant.UPDATE.equals(menuItem.getText()))  {
				T specCodeObject = getspecViewData().getSelectedItem();
				createOrUpdate(event,specCodeObject, QiConstant.UPDATE);
			}
		}
	}
	/**
	 * Event Handlers on each row
	 */
	public void initEventHandlers() {
		if (isFullAccess()) {
			addListeners();
		}
	}	
	/**
 	* This method is used to check user authorization
 	*
 	*/
	public boolean isFullAccess() {
		return getMainWindow().getApplicationContext().getHighestAccessLevel() == ScreenAccessLevel.FULL_ACCESS;
	}
	/**
	 * Listeners on row tables
	 */
	private void addListeners() {
		getspecViewData().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<T>() {
			@Override
			public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
				addContextMenuItems();
			}
		});
		getspecViewData().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				addContextMenuItems();
			}
		});
	}
	/**
	 * Context menus to add on each row on ObjectTablePane
	 */
	public void addContextMenuItems() {
		if(getspecViewData().getSelectedItem()!=null){
			String[] menuItems = new String[] {
					QiConstant.CREATE, QiConstant.UPDATE
			};
			getspecViewData().createContextMenu(menuItems, this);
		}else{
			String[] menuItems = new String[] {
					QiConstant.CREATE
			};
			getspecViewData().createContextMenu(menuItems, this);
		}
	}
	/**
	 * Open Dialog for frame to engine
	 * @param event CREATE/UPDATE
	 * @param specData selected item value in case of update
	 * @param title Create/Update for dialog
	 */
	private void createOrUpdate(ActionEvent event, T specData, String title) {
		SpecMaintDialog dialog = new SpecMaintDialog(title, specData, productType, productSpecData, (SpecMaintPanel<ProductSpec>) this);
		dialog.showDialog();
		productSpecData.loadProductSpec(productType);
		reload();
	}

	public ObjectTablePane<T> getspecViewData() {
		return specViewData;
	}
	
}
