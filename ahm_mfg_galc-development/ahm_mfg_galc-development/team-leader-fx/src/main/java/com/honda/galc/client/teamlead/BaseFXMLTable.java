package com.honda.galc.client.teamlead;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;

public class BaseFXMLTable<T> {

	private TableView<T> tblView;
	private boolean dragndrop = false;
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public ContextMenu rowMenu;

	private Map<String, TableColumn<T, String>> columnMap = new HashMap<String, TableColumn<T, String>>();
	private boolean isTextWrap = false;

	public BaseFXMLTable(TableView<T> t,
			Map<String, TableColumn<T, String>> columnMap, boolean draganddrop) {
		tblView = t;
		this.columnMap = columnMap;
		this.dragndrop = draganddrop;
		init();

	}
	
	public BaseFXMLTable(TableView<T> t,
			Map<String, TableColumn<T, String>> columnMap, boolean draganddrop, boolean isTextWrap) {
		tblView = t;
		this.columnMap = columnMap;
		this.dragndrop = draganddrop;
		this.isTextWrap  = isTextWrap;
		init();

	}

	public BaseFXMLTable(TableView<T> t) {

	}

	public BaseFXMLTable() {

	}

	private void init() {

		for (Map.Entry<String, TableColumn<T, String>> entry : columnMap
				.entrySet()) {
			setOnEditCommit(entry.getValue(), entry.getKey());
			Object o = entry.getValue();
			entry.getValue().setCellValueFactory(
					new PropertyValueFactory<T, String>(entry.getKey()));
			entry.getValue().setCellFactory(this.<T> getCellFactory());
		}

	}

	public Map<String, TableColumn<T, String>> getColumnMap() {
		return columnMap;
	}

	public void setColumnMap(Map<String, TableColumn<T, String>> columnMap) {
		this.columnMap = columnMap;
	}

	public <T> void setOnEditCommit(TableColumn<T, String> t,
			final String colName) {

		t.setOnEditCommit(new EventHandler<CellEditEvent<T, String>>() {

			public void handle(CellEditEvent<T, String> t) {
				Object o = ((T) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));

				try {

					new PropertyDescriptor(colName, o.getClass())
							.getWriteMethod().invoke(o, t.getNewValue());

				} catch (IllegalAccessException e) {

					e.printStackTrace();
				} catch (IllegalArgumentException e) {

					e.printStackTrace();
				} catch (InvocationTargetException e) {

					e.printStackTrace();
				} catch (IntrospectionException e) {

					e.printStackTrace();
				}

			}

		});

	}

	protected <T> Callback<TableColumn<T, String>, TableCell<T, String>> getCellFactory() {

		return new Callback<TableColumn<T, String>, TableCell<T, String>>() {
			public TableCell call(TableColumn p) {

				EditingCell<T> cell = new EditingCell<T>(isTextWrap);
				if (dragndrop) {
					cell.setOnDragDetected(createDragDetectedHandler(cell));
					cell.setOnDragOver(createDragOverHandler(cell));
					cell.setOnDragDropped(createDragDroppedHandler(cell));
				}
				return cell;
			}
		};

	}

	private <T> EventHandler<MouseEvent> createDragDetectedHandler(
			final TableCell<T, String> cell) {
		return new EventHandler<MouseEvent>() {

			public void handle(MouseEvent arg0) {
				Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
				ClipboardContent content = new ClipboardContent();
				content.putString(String.valueOf(cell.getIndex()));
				db.setContent(content);

			}

		};
	}

	private <T> EventHandler<DragEvent> createDragOverHandler(
			final TableCell<T, String> cell) {
		return new EventHandler<DragEvent>() {

			public void handle(DragEvent event) {

				TableView<T> tbl = cell.getTableView();
				final String INTEGER_REGEX = ("-?\\d+");
				final Dragboard dragboard = event.getDragboard();
				if (dragboard.hasString()) {
					final String value = dragboard.getString();
					if (Pattern.matches(INTEGER_REGEX, value)) {
						try {
							final int index = Integer.parseInt(value);
							if (index != cell.getIndex()
									&& index != -1
									&& (index < tbl.getItems().size() - 1 || cell
											.getIndex() != -1)) {
								event.acceptTransferModes(TransferMode.MOVE);
							}
						} catch (NumberFormatException exc) {

						}
					}
				}

			}
		};
	}

	public <T> EventHandler<DragEvent> createDragDroppedHandler(
			final TableCell<T, String> cell) {
		return new EventHandler<DragEvent>() {

			public void handle(DragEvent event) {

				TableView<T> tbl = cell.getTableView();
				Dragboard db = event.getDragboard();
				int myIndex = cell.getIndex();
				if (myIndex < 0 || myIndex >= tbl.getItems().size()) {
					myIndex = tbl.getItems().size() - 1;
				}
				int incomingIndex = Integer.parseInt(db.getString());
				tbl.getItems().add(myIndex,
						tbl.getItems().remove(incomingIndex));
				event.setDropCompleted(true);
				dropHandler();
			}
		};
	}
	
	
	public void dropHandler()  {
			
	}

	public void onRowSelection() {

		tblView.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener() {

					public void changed(ObservableValue arg0, Object arg1,
							Object arg2) {

					}

				});

	}

}
