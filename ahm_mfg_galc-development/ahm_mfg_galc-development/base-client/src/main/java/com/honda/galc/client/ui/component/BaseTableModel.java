package com.honda.galc.client.ui.component;

import java.awt.Component;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.util.Primitive;
import com.honda.galc.util.ReflectionUtils;
/**
 * 
 * <h3>BaseTableModel Class description</h3>
 * <p> BaseTableModel description </p>
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
 * May 4, 2010
 *
 *
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public abstract class BaseTableModel<T> extends AbstractTableModel {

    
    private static final long serialVersionUID = 1L;
    
    protected String[] columnNames;
    protected List<T> items;
    protected JTable table;
    protected int currentColumn;
    protected int currentRow;
    protected Object currentValue;
    private boolean isRollback = false;
    
    private boolean autoPackColumnSize = true;

    protected int alignment= JLabel.LEFT;
    private static final String WORD_IS = "is";
	private static final String WORD_GET = "get";
    
    public BaseTableModel(List<T> items, String[] columnNames){
        this.items = items;
        this.columnNames = columnNames;
    }    
    public BaseTableModel(List<T> items, String[] columnNames, JTable table){
        this(items,columnNames);
        this.table = table;
        table.setModel(this);
    }
    
    public void refresh(List<T> items) {
    	
    	this.items = items == null ? new ArrayList<T>() : items;
    	pack();
    	if(table != null){
    		table.updateUI();
    	}
    }
    
    public T getItem(int index) {
        if(index < 0 || items == null || index >= items.size()) return null;
        return items.get(index);
    }
    
    public List<T> getItems() {
        if(items == null) items = new ArrayList<T>();
        return items;
    }
    
    public String getColumnName(int column) {
        return columnNames[column];
    }   
    
    public boolean isCellEditable (int row, int column){
        return false;
    }
    
    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        if(items == null) return 0;
        return items.size();
    }
    
    public int getAlignment() {
		return alignment;
	}
    
	public void setAlignment(int alignment) {
		this.alignment = alignment;
		
		if(table == null) return;
		for(int i=0;i<table.getColumnCount();i++) {
			for (int r=0; r<table.getRowCount(); r++) {
				TableCellRenderer aRenderer = table.getCellRenderer(r, i);
				// invoke setHorizonal alignment when the class has the method
				ReflectionUtils.invoke(aRenderer, "setHorizontalAlignment", new Primitive(alignment));
			}
		}
        
	}
	
	public void add(T item) {
        insertRow(getRowCount(),item);
    }
    
    public void insertRow(int index,T item) {
        
        getItems().add(index, item);
        this.fireTableRowsInserted(index, index);
    }
    
    public void remove(T item) {
    	int index = getItems().indexOf(item);
    	if(index <0) return;
    	remove(index);
    }
    
    public void remove(int index){
        getItems().remove(index);
        this.fireTableRowsDeleted(index, index);
    }
    
    public T getSelectedItem() {
    	
    	if(table != null) {
    		if(table.getSelectedRow() < 0) return null;
    		return getItem(table.getSelectedRow());
    	}
    	return null;
    }
    
    public List<T> getSelectedItems() {
    	List<T> selectedItems = new ArrayList<T>();
    	if(table != null) {
    		int[] selectedRows = table.getSelectedRows();
    		for(int i = 0;i<selectedRows.length; i++) {
    			T item = getItem(selectedRows[i]);
    			if(item != null) selectedItems.add(item);
    		}
    	}
    	
    	return selectedItems;
    }
    
    public void setColumnWidths(int[] widths) {
    	
    	for(int i=0;i<getColumnCount() && i<widths.length;i++) {
    		table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
    	}
    	
    	autoPackColumnSize = false;
    	
    	if(table == null) return;
    	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    	
    	
    }
    
   private void packColumn(int columnIndx){
        
        int margin = 2;
        TableColumn col = table.getColumnModel().getColumn(columnIndx);
 //       if(col.getPreferredWidth() > 0) return;
        
        // Get width of column header
        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = table.getTableHeader().getDefaultRenderer();
        }
        
        
        Component comp = renderer.getTableCellRendererComponent(
            table, col.getHeaderValue(), false, false, 0, 0);
        int width = comp.getPreferredSize().width;
    
        // Get maximum width of column data
        for (int r=0; r<table.getRowCount(); r++) {
            
            TableCellRenderer aRenderer = table.getCellRenderer(r, columnIndx);
            if(aRenderer instanceof DefaultTableCellRenderer)
            	((DefaultTableCellRenderer)aRenderer).setHorizontalAlignment(alignment);
            comp = aRenderer.getTableCellRendererComponent(
                table, table.getValueAt(r, columnIndx), false, false, r, columnIndx);
            width = Math.max(width, comp.getPreferredSize().width);
        }
        
        // Add margin
        width += 2*margin;
        
        // Set the width
        col.setPreferredWidth(width);
    }
   
   public void selectItem(T item) {
	   int index = items.indexOf(item);
	   if(table != null) {
		   table.clearSelection();
		   table.getSelectionModel().setSelectionInterval(index, index);
	   }
   }
   
   public void selectItems(List<T> items){
	   if(table != null) {
		   table.clearSelection();
		   for(T item : items) {
			   int indx = this.items.indexOf(item);
			   
			   table.getSelectionModel().addSelectionInterval(indx, indx);
		   }
	   }
   }
   
   public int getIndex(T item) {
	   
	   return items.indexOf(item);
	   
   }

    
    public void pack() {
        
        if(table == null || !autoPackColumnSize) return;
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        for(int i=0;i<table.getColumnCount();i++){
            
            packColumn(i);
        }
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

    }
    
    /**
     * Default getValueAt implementation for Entity
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
    	if(rowIndex >= getRowCount()) return null;
    	T entity = items.get(rowIndex);

    	Method method = null;
    	try {
    		method = getMethod(entity, columnIndex);
    		if(method == null){
    			Logger.getLogger().warn("Failed to find Method for " + entity.getClass().getSimpleName() + 	" " + columnNames[columnIndex]);
    			return null;
    		}
    		
			return method.invoke(entity, new Object[] {});
		} catch (Exception e) {
			Logger.getLogger().warn("Failed on Method:" + entity.getClass().getSimpleName() + ":" + method.getName());
		}
		return null;
    }
    
    private Method getMethod(T entity, int index) {
		String colunmName = columnNames[index];
		StringBuilder sb = new StringBuilder();
		
		String[] tokens = colunmName.split("_");
		for(int i = 0; i < tokens.length; i++ ){
			String token = tokens[i].toLowerCase();
			sb.append(StringUtils.capitalize(token));
		}
		
		//try get method first
		Method method = getMethod(entity, WORD_GET + sb.toString());
		
		//If it's not get, then try is method
		if(method == null)
			method = getMethod(entity, WORD_IS + sb.toString());
		
		return method;
	}
    
	private Method getMethod(T entity, String methodName) {
		try {
			//try get method
			return entity.getClass().getMethod(methodName, new Class[] {});
		} catch (Exception e) {
			//Ok method may not defined
			return null;
		}
	}
	
	public void fireTableCellUpdated(int row, int column) {
	    if(!isRollback) super.fireTableCellUpdated(row,column);
	}
	/**
	 * roll back change if the remote database update fails
	 *
	 */
	public void rollback() {
		isRollback = true;
		setValueAt(currentValue, currentRow, currentColumn);
//		if(table != null) table.updateUI();
		isRollback = false;
	}
	
	public void setValueAt(Object value, int row, int column) {
		currentValue = getValueAt(row,column);
		currentRow = row;
		currentColumn = column;
	}

	
	protected int parseInt(String valueString ) {
        Pattern pattern =Pattern.compile("^\\d{1,2}$");
		   
		Matcher m = pattern.matcher(valueString);
		if (m.matches()) {
			 return Integer.parseInt(valueString);
		} else {
			MessageDialog.showError(getParentComponent(),String.format("'%s' is invalid, it must be in the format 99",valueString));
			throw new NumberFormatException();
		}
	}	
	

	protected Double parseDouble(String valueString ) {
		
		//Lot Control Changes for Measurement Spec
		try {
			return Double.parseDouble(StringUtils.trimToEmpty(valueString));
		} 
		catch(NumberFormatException nfe) {
			MessageDialog.showError(getParentComponent(),String.format("'%s' is an invalid number",valueString));
			throw nfe;
		}
	}
	
	protected Component getParentComponent() {
		if(table == null) return null;
		return table.getParent();
	}
	
	public JTable getTable(){
		return table;
	}
	
	public Object getCurrentValue() {
		return currentValue;
	}
	
}
