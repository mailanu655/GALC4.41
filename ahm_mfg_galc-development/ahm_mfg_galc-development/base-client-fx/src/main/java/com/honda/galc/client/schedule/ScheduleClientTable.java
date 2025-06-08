package com.honda.galc.client.schedule;

import java.awt.Color;
import java.util.Formatter;
import java.util.Map;
import java.util.regex.Pattern;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Font;
import javafx.util.Callback;

import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.UiFactory;

/**
 * <h3>Class description</h3> Schedule Client Table Class Description <h4>
 * Description</h4> <h4>Special Notes</h4> <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Janak Bhalla & Alok Ghode</TD>
 * <TD>March 05, 2015</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150305</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 */

public class ScheduleClientTable<T> extends
ObjectTablePane<MultiValueObject<T>> {

	Map<String, Object> properties;
	ContextMenu rowMenu = new ContextMenu();
	ScheduleClientController controller;

	public ScheduleClientTable() {
		super();
	}

	public ScheduleClientTable(ScheduleClientController controller,
			Map<String, Object> properties) {
		super();
		this.controller = controller;
		this.properties = properties;
		createTableColumns();
		initialize();
		addListeners();

	}

	/*
	 * Added for Setting data in Table View
	 */
	Callback<TableColumn.CellDataFeatures<MultiValueObject<T>, String>, ObservableValue<String>> callBack = new Callback<TableColumn.CellDataFeatures<MultiValueObject<T>, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(
				TableColumn.CellDataFeatures<MultiValueObject<T>, String> param) {
			Object val = param.getValue().getValue(
					(Integer) param.getTableColumn().getUserData());

			if (val != null) {
				return new ReadOnlyObjectWrapper<String>(val.toString());

			} else
				return null;
		}
	};


	/*
	 * Added for highlighting Upcoming Pre Production Lots
	 */
	Callback<TableColumn<MultiValueObject<T>, String>, TableCell<MultiValueObject<T>, String>> callBackFill = new Callback<TableColumn<MultiValueObject<T>, String>, TableCell<MultiValueObject<T>, String>>() {
		@Override
		public LoggedTableCell<MultiValueObject<T>, String> call(
				TableColumn<MultiValueObject<T>, String> p) {
			return new LoggedTableCell<MultiValueObject<T>, String>() {
				private Formatter f;

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					setText(item == null ? "" : item);
					setGraphic(null);
					if (!(isEmpty() || item == null)) {
						String mask = (String) properties
								.get(DefaultScheduleClientProperty.HIGHLIGHT_VALUE);

						Color color = (Color) properties
								.get(DefaultScheduleClientProperty.HIGHLIGHT_COLOR);
						f = new Formatter(new StringBuffer("#"));
						f.format("%02X", color.getRed());
						f.format("%02X", color.getGreen());
						f.format("%02X", color.getBlue());

						if (Pattern.matches(mask, item)) {
							this.setStyle("-fx-background-color:" + f);
						} else {
							setStyle("");
						}

					}
				}
			};
		}
	};

	private void createTableColumns() {

		String[] headings = (String[]) properties
				.get(DefaultScheduleClientProperty.COLUMN_HEADINGS);
		boolean isSortEnable= (Boolean) properties
				.get(DefaultScheduleClientProperty.ENABLE_SORT);
		int i = 0;
		for (String heading : headings) {
			TableColumn<MultiValueObject<T>, String> column = new LoggedTableColumn<MultiValueObject<T>, String>(heading);
			column.setUserData(i);
			column.setCellValueFactory(callBack);
			//Disable Sort Order
			column.setSortable(isSortEnable);
			
			Object object = properties.get(DefaultScheduleClientProperty.HIGHLIGHT_COLUMN);
			if (object != null && ((Integer) object) == i)
				column.setCellFactory(callBackFill);
			getTable().getColumns().add(
					(TableColumn<MultiValueObject<T>, String>) column);
			i++;
		}
		setConstrainedResize(true);

	}

	/*
	 * Set Font, Row Height and Menus
	 */
	protected void initialize() {

		int rowHeight = (Integer) properties
				.get(DefaultScheduleClientProperty.ROW_HEIGHT);
		Font font = (Font) properties.get(DefaultScheduleClientProperty.FONT);
		getTable().setFixedCellSize(rowHeight);
		getTable().setStyle(
				"-fx-font-size: " + font.getSize() + "; -fx-font-family:"
						+ font.getFamily() + ";" + "-fx-font-weight:"
						+ font.getStyle() + ";");
		createContextMenu();

	}

	/*
	 * Enable/Disable Menu based on selection
	 */
	protected void addListeners() {

		getTable().getSelectionModel().getSelectedIndices()
		.addListener(new ListChangeListener<Integer>() {
			@Override
			public void onChanged(Change<? extends Integer> change) {
				enableDisableMenus();

					}

		});
	}

	protected void createContextMenu() {
		String[] menuItems = (String[]) properties
				.get(DefaultScheduleClientProperty.POPUP_MENU_ITEMS);
		rowMenu.getItems().clear();
		for (String menu : menuItems) {
			if(menu.equalsIgnoreCase(ScheduleClientModel.CUTPASTE)){
				createMenus(ScheduleClientModel.CUT);
				createMenus(ScheduleClientModel.PASTE);
				createMenus(ScheduleClientModel.CANCEL);
			}
			else{
				createMenus(menu);
			}
		}
	}
	
	protected void createMenus(String menu){
		MenuItem menuItem = UiFactory.createMenuItem(menu);
		menuItem.setOnAction(new ScheduleAction(
				ScheduleClientTable.this));
		rowMenu.getItems().add(menuItem);
		getTable().contextMenuProperty().bind(
				Bindings.when(
						Bindings.isNotNull(getTable().itemsProperty()))
						.then(rowMenu).otherwise((ContextMenu) null));
	}
	
	protected void enableDisableMenus() {
		//To enable disable Menus after Cut 
		if(controller.getModel().isOnCut()){
			if(getTable().getContextMenu()!=null && getTable().getContextMenu().getItems()!=null) {
				for(MenuItem menuItem : getTable().getContextMenu().getItems()){
					//To enable only Paste menu after cut
					if(menuItem.getText().equalsIgnoreCase(ScheduleClientModel.PASTE) || 
							menuItem.getText().equalsIgnoreCase(ScheduleClientModel.CANCEL)){
						menuItem.setDisable(false);
					}
					else
					menuItem.setDisable(true);
				}
			}
		}
		else{
		if(getTable().getContextMenu()!=null && getTable().getContextMenu().getItems()!=null) {
				for(MenuItem menuItem : getTable().getContextMenu().getItems())
					menuItem.setDisable(!(controller.canEnableMenuItem(
							menuItem.getText(), ScheduleClientTable.this)));
				
		}
		}
	}

}
