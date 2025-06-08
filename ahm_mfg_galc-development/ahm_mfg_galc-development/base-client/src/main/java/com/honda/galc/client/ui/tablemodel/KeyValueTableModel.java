package com.honda.galc.client.ui.tablemodel;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.SortableTableModel;


import com.honda.galc.util.KeyValue;

public class KeyValueTableModel extends SortableTableModel<KeyValue> {

	private static final long serialVersionUID = 1L;
	private List<String> templateAtt = null;
	private Hashtable<String,Integer> attributesWithLength = null;
	private String attributename;
	boolean rowSelected = false;
	int rowed, col;

	
	public KeyValueTableModel(List<KeyValue> items, String keyColumnName, String valueColumnName, JTable table) {
		
		super(items, new String[] {"#",keyColumnName, valueColumnName}, table);
		setAlignment(JLabel.CENTER);
		pack();
	}
	
	public List<String> getTemplateData(String[] tds, Hashtable<String, Integer> ht)
	{
		if(tds != null){
		templateAtt = new ArrayList<String>(tds.length);
		attributesWithLength = new Hashtable<String,Integer>();
		attributesWithLength = ht;
		for(int i =0; i<tds.length;i++)
		templateAtt.add(tds[i]);
		}
		else 
		{
			templateAtt = new ArrayList<String>(0); }
		return templateAtt;
	}
	
	public KeyValueTableModel(List<KeyValue> items, String keyColumnName, String valueColumnName, JTable table, boolean b) {
		
		super(items, new String[] {"#",keyColumnName, valueColumnName}, table);
		TableColumnModel columnModel = table.getColumnModel();   
		TableColumn col = columnModel.getColumn(2);   
        col.setCellEditor(new TableEditor()); 
		table.setDefaultRenderer(Object.class, new Renderer());
		setAlignment(JLabel.CENTER);
		pack();
	}
	public boolean isCellEditable (int row, int column){
		
		return column > 1;
        
    }
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		KeyValue keyValue = getItem(rowIndex);
		
		switch(columnIndex) {
			case 0: return rowIndex + 1;
			case 1: return keyValue.getKey();
			case 2: return keyValue.getValue();
		}
		
		return null;
	}
	
	public void setValueAt(Object value, int row, int column) {
		if(row >= getRowCount() || column <=0) return;
		
		KeyValue item = getItem(row);
		item.setValue((String)value);

	}	
	
	private boolean isRowSelected(int row) {
		String cellName = getItems().get(row).getKey().toString();
		rowSelected = false;
		for(int j=0; j<templateAtt.size() ; j++){		
			if((templateAtt.get(j).toString()).equals(cellName))
			{	rowSelected= true;  }
		}
		
		return rowSelected;
    		
	}
	
	private class Renderer extends DefaultTableCellRenderer{
		   
		private static final long serialVersionUID = 1L;
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
			cellRenderer.setHorizontalAlignment(this.getHorizontalAlignment());
			Component comp = 
			   cellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			 rowed = row;
			
			if(isRowSelected(rowed)) {
				comp.setBackground(Color.GREEN);
			}
				
			return comp;
	   }
    }
	
	class TableEditor extends AbstractCellEditor implements TableCellEditor  
	{   
	    JTextField textField;   
	    
	    public TableEditor()   
	    {   
	        textField = new JTextField();   
	    }   
	    
	    public Component getTableCellEditorComponent(JTable table,   
	                                                 Object value,   
	                                                 boolean isSelected,   
	                                                 int row, int col)   
	    {   
	    	attributename = table.getValueAt(row, col-1).toString();
	    	System.out.println("attributeName:"+attributename);
	        textField.setText(String.valueOf(value));   
	        return textField;   
	    }   
	    
	    public Object getCellEditorValue()   
	    {   
	        String s = textField.getText();   
	        return s;
	    }   
	    
	    public boolean stopCellEditing()   
	    {   
	    	int avalue = 0;
	        String f = getCellEditorValue().toString();
	        if(attributesWithLength!= null){
	        	if(attributesWithLength.containsKey(attributename))
	        		avalue= attributesWithLength.get(attributename);
	       
	        	if(!(f.length()== avalue)&& !(String.valueOf(avalue).equalsIgnoreCase("0")))   
	        	{   
	        		MessageDialog.showError("attribute length not matched. It should match value: "+ avalue);
	        		fireEditingCanceled(); 
	        		return false;   
	        	}   
	        }
	        return super.stopCellEditing();   
	    }
	}   
}
