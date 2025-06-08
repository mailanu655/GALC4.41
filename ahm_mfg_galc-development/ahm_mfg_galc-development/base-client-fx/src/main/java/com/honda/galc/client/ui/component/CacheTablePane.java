package com.honda.galc.client.ui.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.cache.PersistentCache;
import com.honda.galc.util.BeanUtils;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>CacheTablePane</code> is ... .
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
public class CacheTablePane extends PaginatedTablePane<Object> {

    private PersistentCache cache;

    public CacheTablePane(List<ColumnMapping> columnMappings, PersistentCache cache, int pageSize) {
        this(columnMappings, cache, pageSize, false);
    }

    public CacheTablePane(List<ColumnMapping> columnMappings, PersistentCache cache, int pageSize, boolean autoResizeColumns) {
        this(columnMappings, cache, pageSize, autoResizeColumns, false);
    }

    public CacheTablePane(List<ColumnMapping> columnMappings, PersistentCache cache, int pageSize, boolean autoResizeColumns, boolean reuseColumns) {
        super(columnMappings, pageSize, autoResizeColumns, reuseColumns);
        this.cache = cache;
    }
    // === data === //
    @Override
    protected void sortData(String propertyName, boolean desc) {

        Map<Object, Object> sortableData = new HashMap<Object, Object>();
        for (Object key : getData()) {
            Object item = getCache().get(key, Object.class);
            Object value = BeanUtils.getNestedPropertyValue(item, propertyName);
            sortableData.put(key, value);
        }

        List<Map.Entry<Object, Object>> entryList = new ArrayList<Map.Entry<Object, Object>>(sortableData.entrySet());
        Comparator<Map.Entry<Object, Object>> comparator = null;
        if (desc) {
            comparator = new Comparator<Map.Entry<Object, Object>>() {
                @Override
                public int compare(Entry<Object, Object> o1, Entry<Object, Object> o2) {
                    return -1 * BeanUtils.safeCompare(o1.getValue(), o2.getValue());
                }
            };
        } else {
            comparator = new Comparator<Map.Entry<Object, Object>>() {
                @Override
                public int compare(Entry<Object, Object> o1, Entry<Object, Object> o2) {
                    return BeanUtils.safeCompare(o1.getValue(), o2.getValue());
                }
            };
        }

        Collections.sort(entryList, comparator);
        List<Object> sortedList = new ArrayList<Object>();
        for (Map.Entry<Object, Object> entry : entryList) {
            sortedList.add(entry.getKey());
        }
        getData().clear();
        getData().addAll(sortedList);
        resetPagination(getPagination().getCurrentPageIndex());
        sortedList.clear();
        entryList.clear();
        sortableData.clear();
    }

    // === pagination === //
    @Override
    protected List<Object> getData(int fromIx, int toIx) {
        List<Object> list = new ArrayList<Object>();
        if (getCache() == null || getData() == null || getData().isEmpty()) {
            return list;
        }
        toIx = Math.min(toIx, getDataSize());
        for (int i = fromIx; i < toIx; i++) {
            Object key = getData().get(i);
            Object item = getCache().get(key, Object.class);
            if (item != null) {
                list.add(item);
            } else {
                Logger.getLogger().error("Data does not exist for item : " + key);
            }
        }
        return list;
    }

    // === get/set === //
    protected PersistentCache getCache() {
        return cache;
    }
}
