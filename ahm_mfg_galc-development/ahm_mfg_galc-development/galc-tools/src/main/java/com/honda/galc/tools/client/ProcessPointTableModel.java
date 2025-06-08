package com.honda.galc.tools.client;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.conf.ProcessPoint;

public class ProcessPointTableModel extends SortableTableModel<ProcessPoint> {
	private static final long serialVersionUID = 1L;
	private static String[] solumns = {"Process Point Name", "Process Point ID", "Description"};
	public ProcessPointTableModel(List<ProcessPoint> items, JTable table) {
		super(items, solumns, table);
		
	}


	
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= getRowCount()) return null;
		ProcessPoint processPoint = items.get(rowIndex);

		switch(columnIndex){
		case 0: return processPoint.getProcessPointName();
		case 1: return processPoint.getProcessPointId();
		case 2: return processPoint.getProcessPointDescription();
		
		default:
			return super.getValueAt(rowIndex, columnIndex);
		}
	}

}
