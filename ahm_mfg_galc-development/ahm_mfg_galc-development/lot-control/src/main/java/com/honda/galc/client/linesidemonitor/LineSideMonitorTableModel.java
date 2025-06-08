package com.honda.galc.client.linesidemonitor;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;

/**
 * 
 * <h3>LineSideMonitorTableModel Class description</h3>
 * <p> LineSideMonitorTableModel description </p>
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
 * Mar 16, 2011
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
public class LineSideMonitorTableModel extends BaseTableModel<LineSideMonitorData>{

	private static final long serialVersionUID = 1L;
	
	private boolean hasCheckBoxColumn = true;
	
	public LineSideMonitorTableModel(List<LineSideMonitorData> items,
			String[] columnNames,JTable table) {
		super(items, columnNames,table);
	}
	
	public LineSideMonitorTableModel(String[] columnNames,JTable table) {
		
		super(null,columnNames,table);
		
	}
	
	public LineSideMonitorTableModel(String[] columnNames, int[] columnSizes,JTable table) {
	
		super(null,columnNames,table);
		setColumnWidths(columnSizes);
		
	}
	
	public LineSideMonitorTableModel(List<LineSideMonitorData> items,String[] columnNames, int[] columnSizes,JTable table) {
		
		super(items,columnNames,table);
		setColumnWidths(columnSizes);
		
	}	
	
	public boolean isCellEditable (int row, int column){
		
        return hasCheckBoxColumn && column == 0;
        
    }
	
	public Class<?> getColumnClass(int columnIndex) {
    	
		if(hasCheckBoxColumn && columnIndex == 0) return Boolean.class;
    	return Object.class;
    }

	public Object getValueAt(int rowIndex, int columnIndex) {
		
		if(rowIndex >= getRowCount()) return null;
		LineSideMonitorData item = items.get(rowIndex);
		if(hasCheckBoxColumn){
			if(columnIndex == 0) return item.isChecked;
			else return item.getValue(columnIndex -1);
		}else return item.getValue(columnIndex );
		
	}

	public boolean hasCheckBoxColumn() {
		return hasCheckBoxColumn;
	}

	public void setCheckBoxColumn(boolean hasCheckBox) {
		this.hasCheckBoxColumn = hasCheckBox;
	}
	
	
	
	
}
