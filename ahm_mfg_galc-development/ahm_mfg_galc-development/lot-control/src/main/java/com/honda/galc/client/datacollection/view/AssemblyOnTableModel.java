package com.honda.galc.client.datacollection.view;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.util.StringUtil;

public class AssemblyOnTableModel extends BaseTableModel<AssemblyOnModel>{

private static final long serialVersionUID = 1L;
	
	private boolean editable = false;
	public AssemblyOnTableModel(JTable table,List<AssemblyOnModel> items, String[] columns, boolean editable, boolean autoSize) {
		super(items,columns ,table);
		this.setAlignment(JLabel.CENTER);
		if(!autoSize)
			this.setColumnWidths(new int[] {250,250,250,250});
		this.editable = editable;
	}
	
	
	public AssemblyOnTableModel(JTable table,String[] columns,List<AssemblyOnModel> items) {
		this(table,items,columns,false);
	}
	
	public AssemblyOnTableModel(JTable table,List<AssemblyOnModel> items,String[] columns,boolean editable) {
		super(items, columns,table);
		this.setAlignment(JLabel.CENTER);
		this.setColumnWidths(new int[] {250,250,250,250});
		this.editable = editable;
	}	
	
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isCellEditable (int row, int column){
		return editable && column > 0;
    }
	
	public Object getValueAt(int rowIndex, int columnIndex) {
        
		AssemblyOnModel assemblyOn = getItem(rowIndex);
		String column = this.getColumnName(columnIndex);
		if(column.equalsIgnoreCase("AF ON SEQUENCE") || column.equalsIgnoreCase("PA OFF SEQUENCE") || column.equalsIgnoreCase("SEQUENCE") ){
			String text = StringUtil.padLeft(String.valueOf(assemblyOn.getSequence()),5,'0');
			return text;
		}else if(column.equalsIgnoreCase("VIN")){
			return assemblyOn.getVin();
		}else if(column.equalsIgnoreCase("MTOCI")){
			return assemblyOn.getMtoci();
		}else if(column.equalsIgnoreCase("KD LOT")){
			return assemblyOn.getKdLot();
		}else if(column.equalsIgnoreCase("COMMENTS")){
			return assemblyOn.getComments();
		}
       
        return null;
    }
	
	public void setValueAt(Object value, int row, int column) {
		if(row >= getRowCount() || column == 0) return;
		super.setValueAt(value, row, column);
		this.fireTableCellUpdated(row, column);
	} 
}
