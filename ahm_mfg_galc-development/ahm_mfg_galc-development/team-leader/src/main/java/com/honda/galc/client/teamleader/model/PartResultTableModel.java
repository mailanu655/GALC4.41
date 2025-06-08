package com.honda.galc.client.teamleader.model;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.teamleader.ManualLotCtrRepairStatusCellRender;
import com.honda.galc.client.ui.component.SortableTableModel;
/**
 * 
 * <h3>PartResultTableModel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PartResultTableModel description </p>
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
 * @author Paul Chou
 * Aug 4, 2010
 *
 */
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 03, 2016
 */
public class PartResultTableModel extends SortableTableModel<PartResult>{
	private static final long serialVersionUID = 1L;
    private String[] columns;
	public PartResultTableModel(List<PartResult> items, String[] columns, JTable table) {
		super(items, columns,table);
		this.columns = columns;
		setColumnRenders();
	}
	
	private void setColumnRenders() {
		for(int i=0;i<this.columns.length;i++){
			String column = this.columns[i];
			if(column.equalsIgnoreCase("SHIP_STATUS") || column.equalsIgnoreCase("STATUS") || column.equalsIgnoreCase("STATUS_MEASURE")){
				table.getColumnModel().getColumn(i).setCellRenderer(new ManualLotCtrRepairStatusCellRender());
			}
		}
		
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= getRowCount()) return null;
		PartResult partResult = items.get(rowIndex);
		String column = this.getColumnName(columnIndex);
		if(column.equalsIgnoreCase("PROCESS_POINT_NAME")){
			return partResult.getProcessPoint().getProcessPointName();	
		}else if(column.equalsIgnoreCase("PART_NAME")){
			return partResult.getPartName();
		}else if(column.equalsIgnoreCase("MEASUREMENT_RESULT")){
			//Not show the measurement data if it is defined as quick fix for head less data collection
			//-- the same as current AE function
			return (partResult.isHeadLess() && partResult.isQuickFix()) ? null : partResult.getMeasurementResult();
		}else if(column.equalsIgnoreCase("PROCESS_POINT")){
			return partResult.getProcessPointId();
		}else if(column.equalsIgnoreCase("SHIP_STATUS")){
			return partResult.getShipStatus();
		}else{
			return super.getValueAt(rowIndex, columnIndex);
		}
	}

}
