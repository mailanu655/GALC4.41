package com.honda.galc.client.ui.component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.util.BeanUtils;
import com.sun.javafx.scene.control.skin.TableViewSkin;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>DynamicTablePane</code> is ... .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Mar 1, 2019
 */
public class DynamicTablePane<T> extends BorderPane {

    private TableView<T> tableView;
    private Map<String, TableColumn<T, Object>> columns;

    public static final String ROW_ID = "$rowid";

    private Method resizeColumnMethod;
    private boolean reuseColumns;

    public DynamicTablePane(List<ColumnMapping> columnMappings) {
        this(columnMappings, false);
    }

    public DynamicTablePane(List<ColumnMapping> columnMappings, boolean autoResizeColumns) {
        this(columnMappings, autoResizeColumns, false);
    }
    
    public DynamicTablePane(List<ColumnMapping> columnMappings, boolean autoResizeColumns, boolean reuseColumns) {
        this.tableView = new TableView<T>();
        this.columns = new LinkedHashMap<String, TableColumn<T, Object>>();
        setReuseColumns(reuseColumns);
        addColumns(columnMappings);
        if (autoResizeColumns) {
            setAutoResizeColumns();
        }
        setCenter(getTable());
    }

    // === factory methods === //
    protected TableColumn<T, Object> getColumn(ColumnMapping mapping) {
        TableColumn<T, Object> column = getColumns().get(mapping.getAccessor());
        if (column != null) {
            return column;
        }
        column = createColumn(mapping);
        if (isReuseColumns()) {
            getColumns().put(mapping.getAccessor(), column);
        }
        return column;
    }

    protected TableColumn<T, Object> createColumn(ColumnMapping mapping) {
        final ColumnMapping columnMapping = mapping;
        TableColumn<T, Object> tableColumn = new TableColumn<T, Object>();
        tableColumn.setText(columnMapping.getTitle());
        tableColumn.setSortable(columnMapping.isSortable());

        if (columnMapping.getSubColumns() == null || columnMapping.getSubColumns().isEmpty()) {
            tableColumn.setCellValueFactory(new Callback<CellDataFeatures<T, Object>, ObservableValue<Object>>() {
                @Override
                public ObservableValue<Object> call(CellDataFeatures<T, Object> p) {
                    try {
                        Object value = null;
                        if (ROW_ID.equals(columnMapping.getAccessor())) {
                            value = getTable().getItems().indexOf(p.getValue()) + 1;
                        } else if (StringUtils.isBlank(columnMapping.getAccessor())) {
                            value = p.getValue();
                        } else {
                            value = BeanUtils.getNestedPropertyValue(p.getValue(), columnMapping.getAccessor());
                            if (StringUtils.isNotBlank(columnMapping.getFormat())) {
                                value = String.format(columnMapping.getFormat(), value);
                            }
                        }
                        return new ReadOnlyObjectWrapper<Object>(value);
                    } catch (Exception e) {
                        Logger.getLogger().warn(e, "Failed to setCellValueFactory");
                        return new ReadOnlyObjectWrapper<Object>(p.getValue());
                    }
                }
            });
        } else {
            for (ColumnMapping cm : columnMapping.getSubColumns()) {
                TableColumn<T, Object> tc = createColumn(cm);
                tableColumn.getColumns().add(tc);
            }
        }
        return tableColumn;
    }

    // === behavior === //
    protected void setAutoResizeColumns() {
        TableView<T> tableView = getTable();
        setResizeColumnMethod();
        if (getResizeColumnMethod() == null) {
            return;
        }
        tableView.getItems().addListener(new ListChangeListener<Object>() {
            @Override
            public void onChanged(Change<?> c) {
                resizeColumns();
            }
        });
    }

    protected void setResizeColumnMethod() {
        try {
            this.resizeColumnMethod = TableViewSkin.class.getDeclaredMethod("resizeColumnToFitContent", TableColumn.class, int.class);
        } catch (NoSuchMethodException | SecurityException e) {
            Logger.getLogger().warn(e, "Failed to set resizeColumnMethod.");
        }
        if (getResizeColumnMethod() == null) {
            return;
        }
        getResizeColumnMethod().setAccessible(true);
    }

    public void resizeColumns() {
        resizeColumns(-1, -1);
    }

    public void resizeColumns(int colCount, int rowCount) {
        if (getResizeColumnMethod() == null) {
            setResizeColumnMethod();
        }
        if (getResizeColumnMethod() == null) {
            return;
        }
        if (colCount < 0 || getTable().getColumns().size() <= colCount) {
            resizeColumns(getTable(), getTable().getColumns(), rowCount);
        } else {
            resizeColumns(getTable(), getTable().getColumns().subList(0, colCount), rowCount);
        }
    }

    protected void resizeColumns(TableView<T> table, List<TableColumn<T, ?>> columns, int rowCount) {
        for (TableColumn<T, ?> column : columns) {
            if (column.getColumns().isEmpty()) {
                invokeResizeColumnMethod(table, column, rowCount);
            } else {
                resizeColumns(table, column.getColumns(), rowCount);
            }
        }
    }

    protected void invokeResizeColumnMethod(TableView<T> table, TableColumn<T, ?> column, int rowCount) {
        try {
            getResizeColumnMethod().invoke(getTable().getSkin(), column, rowCount);
        } catch (Exception e) {
            String str = "";
            str = str + "Colum : " + column + ":";
            if (column != null) {
                str = str + column.getText();
                if (column.getParentColumn() != null) {
                    str = str + ", parent:" + column.getParentColumn().getText();
                }
            }
            Logger.getLogger().warn(e, "Failed to invoke ResizeColumnMethod " + str);
        }
    }

    // === add/remove columns === //
    public void removeColumns() {
        getTable().getColumns().clear();
    }

    public void removeColumns(List<TableColumn<T, ?>> columns) {
        if (columns == null || columns.isEmpty()) {
            return;
        }
        getTable().getColumns().removeAll(columns);
    }

    public void addColumns(List<ColumnMapping> columns) {
        if (columns == null || columns.isEmpty()) {
            return;
        }
        for (ColumnMapping columnMapping : columns) {
            TableColumn<T, Object> column = getColumn(columnMapping);
            getTable().getColumns().add(column);
        }
    }

    // === get/set === //
    public T getItem(T item) {
        int ix = getTable().getItems().lastIndexOf(item);
        if (ix == -1) {
            return null;
        }
        return getTable().getItems().get(ix);
    }

    public void addItem(T item) {
        if (item == null) {
            return;
        }
        getTable().getItems().add(item);
    }

    public void removeItem(T item) {
        if (item == null) {
            return;
        }
        getTable().getItems().remove(item);
    }

    public void removeItems(List<T> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        getTable().getItems().removeAll(items);
    }

    public List<T> getItems() {
        return new ArrayList<T>(getTable().getItems());
    }

    public void setItems(List<T> items) {
        getTable().getItems().clear();
        if (items != null && !items.isEmpty()) {
            getTable().getItems().addAll(items);
        }
    }

    public void clearData() {
        getTable().getItems().clear();
    }

    // === get/set === //
    public TableView<T> getTable() {
        return tableView;
    }

    public T getSelectedItem() {
        return getTable().getSelectionModel().getSelectedItem();
    }

    public ObservableList<T> getSelectedItems() {
        return getTable().getSelectionModel().getSelectedItems();
    }

    protected Map<String, TableColumn<T, Object>> getColumns() {
        return columns;
    }

    protected Method getResizeColumnMethod() {
        return resizeColumnMethod;
    }

    protected boolean isReuseColumns() {
        return reuseColumns;
    }

    protected void setReuseColumns(boolean reuseColumns) {
        this.reuseColumns = reuseColumns;
    }
}
