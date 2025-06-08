package com.honda.galc.client.ui.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.honda.galc.util.SortedArrayList;
/**
 * 
 * <h3>ObjectTablePane Class description</h3>
 * <p> ObjectTablePane description </p>
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
 * @author Jeffray Huang<br>
 * May 4, 2011
 *
 *
 */
public class ObjectTablePane<T> extends TablePane{
	
	
	private static final long serialVersionUID = 1L;
	
	private BaseTableModel<T> tableModel;
	
	private long dataSelectedTime;
	
	// define if table is sortable , disabled by default
	private boolean isSortable = false;

	public ObjectTablePane() {
		
	}
	
	public ObjectTablePane(String title) {
		super(title);
	}
	
	public ObjectTablePane(String title, int selectionMode) {
		super(title,selectionMode);
	}	
	
	public ObjectTablePane(String title,boolean supportUnselect) {
		
		super(title,supportUnselect);
		
	}
	
	public ObjectTablePane(String title,int selectionMode,boolean supportUnselect){
		
		super(title,selectionMode,supportUnselect);
		
	}
	
	public ObjectTablePane(List<ColumnMapping> columnMappings) {
		tableModel = new ObjectTableModel<T>(getTable(),null,columnMappings);
	}	
	
	public ObjectTablePane(List<ColumnMapping> columnMappings,boolean supportUnselect) {
		super(null,supportUnselect);
		tableModel = new ObjectTableModel<T>(getTable(),null,columnMappings);
		
	}
	
	public ObjectTablePane(List<ColumnMapping> columnMappings,boolean supportUnselect,boolean isSortable) {
		
		this(null,columnMappings,supportUnselect,isSortable);
	}
	
	public ObjectTablePane(String title,List<ColumnMapping> columnMappings,boolean supportUnselect,boolean isSortable) {
		super(title,supportUnselect);
		this.isSortable = isSortable;
		tableModel = createTableModel(columnMappings);
	}
	
	public ObjectTablePane(String title,List<ColumnMapping> columnMappings) {
		
		this(title);
		tableModel = createTableModel(columnMappings);
		
	}
	
	public ObjectTablePane(String[] columnNames) {
		this(columnNames,false);
	}
	
	public ObjectTablePane(String[] columnNames,boolean isSortable) {
		this(null,columnNames,isSortable);
	}
	
	public ObjectTablePane(String title,String[] columnNames) {
		this(title,columnNames,false);
	}
	
	
	public ObjectTablePane(String title,String[] columnNames,boolean isSortable) {
		
		this(title,columnNames,isSortable,false);
		
	}
	
	public ObjectTablePane(String title,String[] columnNames,boolean isSortable, boolean supportUnselect) {
		
		super(title,supportUnselect);
		tableModel = isSortable ? 
				new SortableObjectTableModel<T>(getTable(),columnNames) :
				new ObjectTableModel<T>(getTable(),columnNames);
				
	}	
	
	
	private BaseTableModel<T> createTableModel(List<ColumnMapping> columnMappings) {
		return isSortable ?
				new SortableObjectTableModel<T>(getTable(),null,columnMappings): 
					new ObjectTableModel<T>(getTable(),null,columnMappings);
	}
	
	public BaseTableModel<T> getTableModel() {
		return this.tableModel;
	}
	
	public void setTableModel(BaseTableModel<T> tableModel) {
		this.tableModel = tableModel;
	}
	
	public void reloadData(List<T> items) {
		if(tableModel != null ) tableModel.refresh(items);
	}
	
	public void reloadData(List<T> items,long dataSelectedTime) {
		if(tableModel != null ){
			tableModel.refresh(items);
			setDataSelectedTime(dataSelectedTime);
		}
	}
	
	public void refresh() {
		reloadData(getItems());
	}
	
	public void addTableModelListener(TableModelListener tableModelListner) {
		if(tableModel != null)
			tableModel.addTableModelListener(tableModelListner);
	}
	
	public boolean isEvent(TableModelEvent tableModelEvent) {
		return tableModelEvent.getSource().equals(tableModel);
	}
	
	public void reloadData(List<T> items,long dataSelectedTime,String sortMethod) {
		if(tableModel != null ){
			
			tableModel.refresh(new SortedArrayList<T>(items,sortMethod));
			setDataSelectedTime(dataSelectedTime);
			
		}
	}
	public void removeData() {

		if (getTable().getSelectedRowCount() > 0) {
			getTable().clearSelection();
		}

		ListSelectionListener[] listeners = removeSelectionListener();
		
		tableModel.refresh(null);
		
		addSelectionListener(listeners);
	}
	
	public T getSelectedItem() {
		return tableModel.getSelectedItem();
	}
	
	public List<T> getSelectedItems() {
		return tableModel.getSelectedItems();
	}
	
	public void setSelectedItems(List<T> items) {
		tableModel.selectItems(items);
	}
	
	
	public List<T> getItems() {
		return tableModel.getItems();
	}

	public long getDataSelectedTime() {
		return dataSelectedTime;
	}

	public void setDataSelectedTime(long dataSelectedTime) {
		this.dataSelectedTime = dataSelectedTime;
	}
	
	public void setAlignment(int alignment) {
		if(tableModel !=null) tableModel.setAlignment(alignment);
	}	

	public void setColumnMappings(List<ColumnMapping> columnMappings) {
		tableModel = createTableModel(columnMappings);
	}
	
	public void setColumnWidths(int[] widths) {
		if(tableModel != null) tableModel.setColumnWidths(widths);
	}
	
	public void select(List<T> list) {
		for(T object : list) {
			select(object);
		}
	}
	
	public void select(T object) {
		int ix = -1;
		int i = 0;

		for (T item : getItems()) {
			if (item.equals(object)) {
				ix = i; 
				break;
			}
			i++;
		}
		if(ix >=0) getTable().addRowSelectionInterval(ix, ix);
	}
	
	@SuppressWarnings("unchecked")
	public void setSelectedObjectDataRow(Object o2) {
		List<T> list = getObjectData();
		if (list == null || list.isEmpty()) {
			return;
		}

		int ix = 0;

		for (int i = 0; i < list.size(); i++) {
			Object o1 = list.get(i);

			if (o2 instanceof Map && o1 instanceof Map) {
				if (equals((Map) o1, (Map) o2)) {
					ix = i;
					break;
				}
			} else {
				if (equals(o1, o2)) {
					ix = i;
					break;
				}
			}
		}
		// getTable().getSelectionModel().setLeadSelectionIndex(ix);
		getTable().addRowSelectionInterval(ix, ix);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getObjectData() {
		List<T> list = new ArrayList<T>();

		int count = getTable().getModel().getRowCount();
		for (int i = 0; i < count; i++) {
			T v = (T) getTable().getModel().getValueAt(i, 0);
			list.add(v);
		}
		return list;
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
	
	
}
