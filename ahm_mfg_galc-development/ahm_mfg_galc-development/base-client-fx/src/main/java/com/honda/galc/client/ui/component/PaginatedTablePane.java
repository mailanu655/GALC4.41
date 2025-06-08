package com.honda.galc.client.ui.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.honda.galc.util.BeanUtils;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>PaginatedTablePane</code> is ... .
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
public class PaginatedTablePane<T> extends DynamicTablePane<T> {

    private List<T> data;
    private int pageSize;
    private Pagination pagination;

    public PaginatedTablePane(List<ColumnMapping> columnMappings, int pageSize) {
        this(columnMappings, pageSize, false);
    }

    public PaginatedTablePane(List<ColumnMapping> columnMappings, int pageSize, boolean autoResizeColumns) {
        this(columnMappings, pageSize, autoResizeColumns, false);
    }

    public PaginatedTablePane(List<ColumnMapping> columnMappings, int pageSize, boolean autoResizeColumns, boolean reuseColumns) {
        super(columnMappings, autoResizeColumns, reuseColumns);
        this.data = FXCollections.observableArrayList();
        this.pagination = new Pagination();
        setPageSize(pageSize);
        setCenter(getPagination());
        getPagination().setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                return createPage(param);
            }
        });
        resetPagination();
    }

    // === factory methods === //
    @Override
    protected TableColumn<T, Object> createColumn(final ColumnMapping columnMapping) {
        TableColumn<T, Object> column = super.createColumn(columnMapping);
        if (ROW_ID.equals(columnMapping.getAccessor()) || columnMapping.getSubColumns() != null && !columnMapping.getSubColumns().isEmpty()) {
            return column;
        }
        ContextMenu cm = createSortAllPagesContextMenu(columnMapping);
        column.setContextMenu(cm);
        return column;
    }

    protected ContextMenu createSortAllPagesContextMenu(ColumnMapping columnMapping) {
        ContextMenu cm = new ContextMenu();
        MenuItem sortAsc = new MenuItem("Sort all pages by " + columnMapping.getTitle() + " asc");
        MenuItem sortDesc = new MenuItem("Sort all pages by " + columnMapping.getTitle() + " desc");
        MenuItem close = new MenuItem("Close Menu");
        sortAsc.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sortData(columnMapping.getAccessor(), false);
            }
        });
        sortDesc.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sortData(columnMapping.getAccessor(), true);
            }
        });
        cm.getItems().add(sortAsc);
        cm.getItems().add(sortDesc);
        cm.getItems().add(new SeparatorMenuItem());
        cm.getItems().add(close);
        return cm;
    }

    // === pagination === //
    protected Node createPage(int pageIndex) {
        int fromIx = pageIndex * getPageSize();
        int toIx = Math.min(fromIx + getPageSize(), getDataSize());
        List<T> items = getData(fromIx, toIx);
        getTable().getItems().clear();
        if (items != null && !items.isEmpty()) {
            getTable().getItems().addAll(items);
        }
        return getTable();
    }

    protected List<T> getData(int fromIx, int toIx) {
        if (getData() == null || getData().isEmpty()) {
            return new ArrayList<T>();
        }
        List<T> list = getData().subList(fromIx, toIx);
        return list;
    }

    // === data === //
    protected void sortData(final String propertyName, boolean desc) {
        List<T> data = getData();
        Comparator<T> comparator = null;
        if (desc) {
            comparator = new Comparator<T>() {
                @Override
                public int compare(T o1, T o2) {
                    Object value1 = BeanUtils.getNestedPropertyValue(o1, propertyName);
                    Object value2 = BeanUtils.getNestedPropertyValue(o2, propertyName);
                    return -1 * BeanUtils.safeCompare(value1, value2);
                }
            };
        } else {
            comparator = new Comparator<T>() {
                @Override
                public int compare(T o1, T o2) {
                    Object value1 = BeanUtils.getNestedPropertyValue(o1, propertyName);
                    Object value2 = BeanUtils.getNestedPropertyValue(o2, propertyName);
                    return BeanUtils.safeCompare(value1, value2);
                }
            };
        }
        Collections.sort(data, comparator);
        resetPagination(getPagination().getCurrentPageIndex());
    }

    @Override
    public void setItems(List<T> items) {
        getTable().getItems().clear();
        if (items == null || items.isEmpty()) {
            return;
        }
        getData().addAll(items);
        resetPagination();
    }

    @Override
    public List<T> getItems() {
        return new ArrayList<T>(getData());
    }

    @Override
    public void addItem(T item) {
        if (item == null) {
            return;
        }
        getData().add(item);
        resetPagination(getPagination().getCurrentPageIndex());
    }

    @Override
    public void removeItem(T item) {
        if (item == null) {
            return;
        }
        getData().remove(item);
        resetPagination(getPagination().getCurrentPageIndex());
    }

    public void removeItems(List<T> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        getData().removeAll(items);
        resetPagination(getPagination().getCurrentPageIndex());
    }

    @Override
    public void clearData() {
        if (getData() != null) {
            getData().clear();
        }
        resetPagination();
    }

    public void resetPagination() {
        resetPagination(0);
    }

    public void resetPagination(int pageIx) {
        int pageCount = getDataSize() / getPageSize();
        if (getDataSize() % getPageSize() != 0) {
            pageCount++;
        }
        if (pageCount == 0) {
            pageCount = 1;
        }
        if (pageIx > (pageCount - 1)) {
            pageIx = pageCount - 1;
        } else if (pageIx < 0) {
            pageIx = 0;
        }
        getPagination().setPageCount(pageCount);
        if (getDataSize() <= getPageSize() || getPagination().getCurrentPageIndex() == pageIx) {
            createPage(pageIx);
        }
        getPagination().setCurrentPageIndex(pageIx);
    }

    // === get/set === //
    public Pagination getPagination() {
        return pagination;
    }

    public List<T> getData() {
        return data;
    }

    public int getDataSize() {
        return getData().size();
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
