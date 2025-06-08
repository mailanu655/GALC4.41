package com.honda.galc.client.ui.component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.utils.UiFactory;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.util.Callback;

/**
 * 
 * <h3>ObjectTablePane</h3>
 * <p>
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
 * @author Suriya Sena Feb 11, 2014 - JavaFx migration
 * 
 */


public class ObjectTablePane<T> extends BorderPane{
	
	private final double MIN_WIDTH_IN_PERCENT = 0.20;
	
	private TableView<T>  tableView;
	private ObservableList<T> itemList;
	private ObservableList<T> lazyItems = FXCollections.observableArrayList();
	private List<T> newItemList;
	private int listStart = 0;
	private int listSteps = 0;
	private int listFlag = 0;

	
	private static final long serialVersionUID = 1L;
	
	private Callback<TableColumn<T,Object>,TableCell<T,Object>> cellFactory =
             new Callback<TableColumn<T,Object>, TableCell<T,Object>>() {
                 public TableCell<T,Object> call(TableColumn<T,Object> p) {
                    return new EditingCell();
                 }
             };
    private Callback<TableColumn<T,Object>,TableCell<T,Object>> toolTipCellFactory =
             new Callback<TableColumn<T,Object>, TableCell<T,Object>>() {
                 public TableCell<T,Object> call(TableColumn<T,Object> p) {
                    return new TableCellTooltip();
                 }
             };
	
	public ObjectTablePane(ColumnMappingList columnMappingList) {
		tableView = new LoggedTableView<T>();
		tableView.setEditable(true);
		
		
		for (ColumnMapping columnMapping : columnMappingList.get()) {
			TableColumn<T, Object> column = createColumn(columnMapping);
			tableView.getColumns().add(column);
			
		}
		
		setCenter(tableView);
		//Fix : Copy records from main window to excel
		tableView.addEventHandler(KeyEvent.KEY_RELEASED,new CopySelectedRowEventHandler());
	}
	
	public ObjectTablePane(ColumnMappingList columnMappingList, Double[] columnWidth) {
		this(columnMappingList, columnWidth, false);
	}
	
	public ObjectTablePane(ColumnMappingList columnMappingList, Double[] columnWidth, boolean isColTableRelative) {
		int index = 0;
		tableView = new LoggedTableView<T>();
		tableView.setEditable(true);
		tableView.setTableMenuButtonVisible(true);
		final KeyCombination keyComb1 = new KeyCodeCombination(KeyCode.A,
                KeyCombination.CONTROL_DOWN);
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		for (ColumnMapping columnMapping : columnMappingList.get()) {
			
			TableColumn<T, Object> column = createColumn(columnMapping);
			if(isColTableRelative) {
				column.prefWidthProperty().bind(tableView.widthProperty().multiply(columnWidth[index]));
				column.minWidthProperty().bind(tableView.widthProperty().multiply(columnWidth[index]));
				column.maxWidthProperty().bind(tableView.widthProperty().multiply(columnWidth[index]));
			} else {
				column.setPrefWidth(primaryScreenBounds.getWidth() * columnWidth[index]);
				column.setMinWidth((primaryScreenBounds.getWidth() * columnWidth[index]) * MIN_WIDTH_IN_PERCENT);
			}

			column.setResizable(true);
			if(!Hyperlink.class.equals(columnMapping.getType())) {
				column.setCellFactory(toolTipCellFactory);
			}
			if(columnMapping.getType().equals(Integer.class))  {
				column.setComparator(
						new Comparator<Object>() {
							@Override
							public int compare(Object arg0, Object arg1) {
								int result = 0;
								if(arg0 == null && arg1 == null)  result = 0;
								else if(arg0 == null)  result = -1;
								else if(arg1 == null)  result = 1;
								//else - both are not null
								try {
									if(arg0 instanceof String && arg1 instanceof String)  {
										result = Integer.valueOf(arg0.toString()).compareTo(Integer.valueOf(arg1.toString()));
									}
									else if(arg0 instanceof Integer && arg1 instanceof Integer)  {
										result = Integer.valueOf((Integer)arg0).compareTo(Integer.valueOf((Integer)arg1));
									}
									else result = arg0.toString().compareTo(arg1.toString());

								} catch (NumberFormatException e) {
									result = arg0.toString().compareTo(arg1.toString());
								}
								return result;
							}
						}
				);
			}
			tableView.getColumns().add(column);
			
			index++;
		}
		setCenter(tableView);
		//Fix : Copy records from main window to excel
		tableView.addEventHandler(KeyEvent.KEY_RELEASED,new CopySelectedRowEventHandler());
		tableView.setOnKeyPressed(new EventHandler<KeyEvent>() {

	        public void handle(KeyEvent ke) {
	            if (keyComb1.match(ke)) {
	            	tableView.getSelectionModel().clearSelection();
	            }
	        }
	    });
	}
	
	public ObjectTablePane() {
		tableView = new LoggedTableView<T>();
		tableView.setEditable(true);
		setCenter(tableView);
	}
  
	public void setSelectionMode(SelectionMode selectionMode ) {
		tableView.getSelectionModel().setSelectionMode(selectionMode);
	}

	
	public void setData(T [] itemArray) {
		setData(Arrays.asList(itemArray));
	}
	
	public void setData(List<T> itemList) {
		this.itemList =  FXCollections.observableArrayList(itemList);
		this.lazyItems = FXCollections.observableArrayList(itemList);
		tableView.getItems().clear();
		tableView.setItems(this.itemList);
	}
	
	public void setDataWithPreserveSort(List<T> itemList)  {
		ObservableList<TableColumn<T, ?>> sortOrder = FXCollections.emptyObservableList();
		if(tableView.getItems() != null && !tableView.getItems().isEmpty())  {
			sortOrder = FXCollections.observableArrayList(tableView.getSortOrder());
		}
		this.itemList =  FXCollections.observableArrayList(itemList);
		this.lazyItems = FXCollections.observableArrayList(itemList);
		tableView.getItems().clear();
		tableView.setItems(this.itemList);
		if(sortOrder != null && !sortOrder.isEmpty())  {
			tableView.getSortOrder().addAll(sortOrder);
		}
	}
	
	public void setData(List<T> itemList, boolean isRestoreSelect) {
		List<Integer> selectedIndices = getTable().getSelectionModel().getSelectedIndices();
		int[] selectedArray = new int[selectedIndices.size()];
		for(int i = 0; i < selectedIndices.size(); i++)  {
			selectedArray[i] = selectedIndices.get(i);
		}
		this.itemList =  FXCollections.observableArrayList(itemList);
		tableView.getItems().clear();
		tableView.setItems(this.itemList);
		this.lazyItems = this.itemList;
		if(isRestoreSelect)  {
			for(int thisIndex = 0; thisIndex < selectedArray.length; thisIndex++)  {
				if(thisIndex < lazyItems.size())  {
					tableView.getSelectionModel().select(selectedArray[thisIndex]);
				}
			}
		}
	}
	
	public void setDataAndLazyList(List<T> itemList) {
		this.itemList =  FXCollections.observableArrayList(itemList);
		this.lazyItems = FXCollections.observableArrayList(itemList);
		tableView.getItems().clear();
		tableView.setItems(this.itemList);
	}
	
	public void removeSelected() {
		List<T> selectedItems = tableView.getSelectionModel().getSelectedItems();
		if(selectedItems != null && !selectedItems.isEmpty())  {
			tableView.getItems().removeAll(selectedItems);
		}
	}
	
	public void setData (ObservableList<T>  list) {
		this.itemList = list;
		tableView.setItems(list);
	}

	public void setData(List<T> itemList,int displayRows) {
		newItemList =  new ArrayList<T>(itemList);
		if (displayRows == 0) {
			setData(itemList);
			return;
		}
		tableView.getItems().clear();
		lazyItems.clear();
		
		listStart = 0;
		listSteps = displayRows;
		listFlag = displayRows;
		
		addItem(newItemList);
		tableView.setItems(lazyItems);
		final ScrollBar bar = getVerticalScrollbar(tableView);
		if (bar == null) {
			setData(itemList);
		} else {
			bar.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double value = newValue.doubleValue();
                ScrollBar scrollbar = getVerticalScrollbar(tableView);
                if (value == scrollbar.getMax()) {
                	listStart = listStart + listFlag;
                	listSteps = listSteps + listFlag;  
                    double targetValue = value * lazyItems.size();
                    addItem(newItemList);
                    bar.adjustValue(targetValue / lazyItems.size());
                }
              }
            });
		}
	}

	public void setData(List<T> itemList,int displayRows, boolean isRestoreListPosition, boolean isRestoreSelect) {
		if(itemList == null)  return;
		if (displayRows == 0) {
			setData(itemList);
			return;
		}
		double listPosition = 0.0D;
		int lazySize = 0;
		//save current sort order
		ObservableList<TableColumn<T, ?>> sortOrder = FXCollections.emptyObservableList();
		if(tableView.getItems() != null && !tableView.getItems().isEmpty())  {
			sortOrder = FXCollections.observableArrayList(tableView.getSortOrder());
		}
		//save selected indices
		List<Integer> selectedIndices = getTable().getSelectionModel().getSelectedIndices();
		int[] selectedArray = new int[selectedIndices.size()];
		for(int i = 0; i < selectedIndices.size(); i++)  {
			selectedArray[i] = selectedIndices.get(i);
		}
		//save list position
		if(isRestoreListPosition)  {
			lazySize = lazyItems.size();
			if(getVerticalScrollbar(tableView) != null)  {
				listPosition = getVerticalScrollbar(tableView).getValue();
			}
		}
		List<T> sorted = new ArrayList<T>(itemList);
		if(sortOrder != null && !sortOrder.isEmpty()) sorted.sort(tableView.comparatorProperty().get());
		newItemList = sorted;	
		
		listStart = 0;
		listSteps = displayRows;
		listFlag = displayRows;
		lazyItems.clear();
		addItem(newItemList, lazySize);
		tableView.setItems(lazyItems);
		if(isRestoreSelect)  {
			for(int thisIndex = 0; thisIndex < selectedArray.length; thisIndex++)  {
				if(thisIndex < lazyItems.size())  {
					tableView.getSelectionModel().select(selectedArray[thisIndex]);
				}
			}
		}
		
		final ScrollBar bar = getVerticalScrollbar(tableView);
		if (bar == null) {
			setData(itemList);
		} else {
			if(isRestoreListPosition)  {
				bar.adjustValue(listPosition);
			}
			bar.valueProperty().addListener(new ChangeListener<Number>() {
	        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
	            double value = newValue.doubleValue();
	            ScrollBar scrollbar = getVerticalScrollbar(tableView);
	            if (value == scrollbar.getMax()) {
                	listStart = listStart + listFlag;
                	listSteps = listSteps + listFlag;  
	                double targetValue = value * lazyItems.size();
	                if(lazyItems.size() < newItemList.size())  {
	                	addItem(newItemList);
	                }
	                bar.adjustValue(targetValue / lazyItems.size());
	            }
	          }
	        });
		}
	}


	private void addItem(List<T> itemList) {
		for (int i = listStart; i <listSteps&&i<itemList.size(); i++) {
			lazyItems.add(itemList.get(i));
		}
	}
	
	private void addItem(List<T> itemList, int lazySize) {
		for (int i = listStart; i < (lazySize > 0 ? lazySize : listSteps) && i < itemList.size(); i++) {
			lazyItems.add(itemList.get(i));
		}
	}
	
	private ScrollBar getVerticalScrollbar(TableView<?> table) {
        ScrollBar result = null;
        for (Node n : table.lookupAll(".scroll-bar")) {
            if (n instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar) n;
                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                    result = bar;
                }
            }
        }        
        return result;
    }

	private TableColumn<T,Object> createColumn(ColumnMapping columnMapping) {
		final String accessor = columnMapping.getAccessor();
		final String format = columnMapping.getFormat();
		
		TableColumn<T,Object> tableColumn = new LoggedTableColumn<T,Object>();
		tableColumn.setText(columnMapping.getTitle());
		tableColumn.setEditable(columnMapping.isEditable());
		
		if (columnMapping.isEditable()) {
			tableColumn.setCellFactory(cellFactory);	
			tableColumn.setOnEditCommit(new CellEditingEventHandler<T,Object>(accessor));
		}
		
		tableColumn.setSortable(columnMapping.isSortable());
	
        tableColumn.setCellValueFactory(new Callback<CellDataFeatures<T,Object>, ObservableValue<Object>>() {
			@Override
			public ObservableValue<Object> call(CellDataFeatures<T,Object> p) {
            		Object obj = p.getValue();
	            	try {
	            		// Special columns start with $ symbol
	            		if (accessor.compareToIgnoreCase("$rowid") == 0)  {
	            			return new ReadOnlyObjectWrapper<Object>(itemList.indexOf(p.getValue()) + 1);
                        } else {
                        	if(obj instanceof Map) {
                        		return new ReadOnlyObjectWrapper<Object>(((Map)obj).get(accessor));
                        	} else if(Hyperlink.class.equals(columnMapping.getType())) {
        						return new ReadOnlyObjectWrapper<Object>(callAccessor(accessor, obj));
                        	} else {
        					    return new ReadOnlyObjectWrapper<Object>(String.format(format,callAccessor(accessor, obj)));
                        	}
                        }
					} catch (Exception e) {
						System.out.println("Error unable to setCellValueFactory");
						e.printStackTrace();
						return new ReadOnlyObjectWrapper<Object>();
					} 
            }
         });
	    return tableColumn;
    }

    public int getColumnCount() {
        return tableView.getColumns().size();
    }

    public int getRowCount() {
    	if ( tableView.getItems() == null ) {
    		return 0;
    	}	else {
    		return tableView.getItems().size();
    	}
    }
    
    public ObservableList<T> getSelectedItems() {
    	return tableView.getSelectionModel().getSelectedItems();
    }
    
    public T getSelectedItem() {
    	return tableView.getSelectionModel().getSelectedItem();
    }
	
	public void setColumns() {
	}
	
	public void setEditable(boolean isEnabled) {
		tableView.setEditable(isEnabled);
	}
	
	public void setConstrainedResize(boolean isConstrainedResizePolicy) {
		if (isConstrainedResizePolicy) {
		  tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		} else {
			  tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		}
	}
	
	
	
	public Object callAccessor(String callpath, Object obj,Object ... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (callpath == null || callpath.length() == 0) {
			throw new IllegalArgumentException("accessor expr must not be null or empty");
		}

		String methodName = callpath.split("\\.")[0];
		int pos = callpath.indexOf('.');
		boolean isLeaf = (pos < 0 ? true :false);    
		if (methodName.length() > 0) {

			Method method = findAccessor(methodName, obj,isLeaf,args);

			if (method != null) {
				
				Object [] params;
				if (isLeaf) {
					params = args;
				} else {
					params = null;
				}
			
				obj = method.invoke(obj,params);

				if (pos > 0) {
					callpath = callpath.substring(pos + 1);
			        return  callAccessor(callpath, obj, args);
				} else {
					return  obj;
				}
			} else {
				throw new IllegalArgumentException(String.format("Unable to find method name matching  [(get|set|is)%s] with %d args",methodName,args.length));
			}
		}
		throw new IllegalArgumentException("invalid accessor format " + callpath);
	}

	public Method findAccessor(String methodName, Object obj,boolean isLeaf, Object ... args) {
		
		String pattern;

		if (isLeaf && ((args == null? 0 : args.length) != 0)) {
		   pattern = String.format("set%s",methodName.toLowerCase());
		} else {
		   pattern = String.format("(get|is)%s",methodName.toLowerCase());
		}
		
		Method[] methods = obj.getClass().getMethods();
		for (Method m : methods) {
			obj = m.getReturnType();
			if (m.getName().toLowerCase().matches(pattern)) {
			//DEBUG	System.out.printf("return type %s  method %s \n",m.getName(), m.getName());
				
				Class<?>[] paramTypes = m.getParameterTypes();
				
				if (isLeaf &&  ((args == null? 0 : args.length) != paramTypes.length))  {
					return null;
				} else {
				   return m;
				}
			}
		}
		return null;
	}
	

	
	protected boolean equals(Map<Object, Object> o1, Map<Object, Object> o2) {

		if (o1 == null && o2 == null) {
			return true;
		}
		if (o1.size() != o2.size()) {
			return false;
		}

		Iterator<Object> i = o1.keySet().iterator();
		while (i.hasNext()) {
			Object key = i.next();
			Object v1 = o1.get(key);
			Object v2 = o2.get(key);
			if (!equals(v1, v2)) {
				return false;
			}
		}
		return true;
	}
	
	protected boolean equals(Object o, Object o2) {

		if (o == null && o2 == null) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (o2 == null) {
			return false;
		}

		if (o instanceof String) {
			o = ((String) o).trim();
		}
		if (o2 instanceof String) {
			o2 = ((String) o2).trim();
		}
		return o.equals(o2);
	}
	
	
	class CellEditingEventHandler<R,S> implements EventHandler<CellEditEvent<R, S>>{
		
		private final String accessor;
		
		public CellEditingEventHandler(String accessor) {
			this.accessor = accessor;
		}
				
		@Override
		public void handle(CellEditEvent<R, S> evt) {
		 	R item = (R) evt.getTableView().getItems().get(evt.getTablePosition().getRow());
		 	try {
				callAccessor(accessor,item,(String)evt.getNewValue());
				if (item instanceof ObservableAdapter<?> ) {
			 	    ((ObservableAdapter<?> )item).update();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
	
    class EditingCell extends TableCell<T, Object> {
    	 
        private TextField textField;
 
       
        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField("editCell");
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }
 
        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText((String) getItem());
            setGraphic(null);
        }
 
        @Override
        public void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);
 
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }
 
        private void createTextField(String id) {
            textField = UiFactory.createTextField(id, getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
            textField.focusedProperty().addListener(new ChangeListener<Boolean>(){
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0, 
                    Boolean arg1, Boolean arg2) {
                        if (!arg2) {
                            commitEdit(textField.getText());
                        }
                }
            });
        }
 
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
    
    class TableCellTooltip extends TableCell<T, Object> {

    	private Tooltip tooltip = new Tooltip();
        
        @Override
        public void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);
 
            if (item == null) {
                setTooltip(null);
                setText(null);
            } else {
            	setText(getString());
                tooltip.setText(getString());
                setTooltip(tooltip);
            }
        }
 
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
    
	public TableView<T> getTable() {
		return tableView;
	}

	public void clearSelection() {
		tableView.getSelectionModel().clearSelection();
		
	}

	public void selectList(
			List<T> selectedItems) {
		clearSelection();
		for(T item : selectedItems)
			tableView.getSelectionModel().select(item);
	}
	
	public void createContextMenu(String[] menuItems,  EventHandler<ActionEvent> eventHandler) {
		ContextMenu contextMenu = new ContextMenu();
		contextMenu.getItems().clear();
		for (String menu : menuItems) {
			MenuItem menuItem = UiFactory.createMenuItem(menu);
			menuItem.setOnAction(eventHandler);
			contextMenu.getItems().add(menuItem);
			getTable().contextMenuProperty().bind(
					Bindings.when(
							Bindings.isNotNull(getTable().itemsProperty()))
							.then(contextMenu).otherwise((ContextMenu) null));
		}
	}
	
	public void addListener(ChangeListener<T> changeListener) {
		tableView.getSelectionModel().selectedItemProperty().addListener(changeListener);
	}
	
	//Fix : Copy records from main window to excel
	class CopySelectedRowEventHandler implements EventHandler<KeyEvent>{
		final KeyCombination keyComb = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);
		@Override
		public void handle(KeyEvent event) {
			if (keyComb.match((KeyEvent) event)) {
				int colSize=tableView.getColumns().size();
				ObservableList<Integer> selectedRowIndices =tableView.getSelectionModel().getSelectedIndices();
		        final ClipboardContent content = new ClipboardContent();
		        StringBuilder clipboardString = new StringBuilder();
		        for(Integer n : selectedRowIndices){
		        	for(int i=0;i<colSize;i++){
						TableColumn col=tableView.getColumns().get(i);
						if(col.getText()=="#"){
							clipboardString.append(n+1);
							clipboardString.append("\t");
							i++;
							col=tableView.getColumns().get(i);
						}
						String data = (String) col.getCellObservableValue(tableView.getItems().get(n)).getValue();
						clipboardString.append(data);
						clipboardString.append("\t");
					}
		        	clipboardString.append("\n");
		        }
				
				content.putString(clipboardString.toString());
			    Clipboard.getSystemClipboard().setContent(content);
	        }			
		}
	}
	
	
}
