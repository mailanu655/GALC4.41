package com.honda.galc.client.datacollection.view;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.datacollection.BasePartResult;


public class OfflineRepairTableModel extends BaseTableModel<BasePartResult> {
	
	private static final long serialVersionUID = 1L;
	
	private boolean editable = true;
	
	public OfflineRepairTableModel(JTable table,List<BasePartResult> items, boolean editable, boolean autoSize) {
		super(items, new String[] {"Part Name", "Status"},table);
		this.setAlignment(JLabel.CENTER);
		if(!autoSize)
			this.setColumnWidths(new int[] {150,80});
		
		this.editable = editable;
		setColumnRenders();
	}
	
	
	public OfflineRepairTableModel(JTable table,List<BasePartResult> items) {
		this(table,items,true);
	}
	
	public OfflineRepairTableModel(JTable table,List<BasePartResult> items,boolean editable) {
		super(items, new String[] {"Part Name", "Status"},table);
		this.setAlignment(JLabel.CENTER);
		this.setColumnWidths(new int[] {150,80});
		this.editable = editable;
		setColumnRenders();
	}	
		
	private void setColumnRenders() {
		table.getColumnModel().getColumn(1).setCellRenderer(new OfflineRepairStatusCellRender());
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
        
		BasePartResult partResult = getItem(rowIndex);
        
        switch(columnIndex) {
            case 0: return partResult.getLotControlRule().getPartName().getPartName();
            case 1: return partResult.getInstalledPart().getInstalledPartStatus();
          
          }
        return null;
    }
	
	public void setValueAt(Object value, int row, int column) {
		if(row >= getRowCount() || column == 0) return;
		super.setValueAt(value, row, column);
		this.fireTableCellUpdated(row, column);
	}    

	
}
