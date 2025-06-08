package com.honda.galc.client.common.data;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.product.PartSpec;

/**
 * 
 * <h3>PartSpecTableModel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PartSpecTableModel description </p>
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
 * May 31, 2010
 *
 */
public class PartSpecTableModel extends BaseTableModel<PartSpec>{
	private static final long serialVersionUID = 1L;

	final static String[] columns =  new String[] {"PART_NAME", "PART_ID ", "PART_DESCRIPTION", "PART_SERIAL_NUMBER_MASK", "PART_MAX_ATTEMPTS", 
		"MEASUREMENT_COUNT", "ENTRY_TIMESTAMP", "CREATE_TIMESTAMP", "UPDATE_TIMESTAMP"};
	int[] columWidths = {160, 65, 150, 180, 160, 150, 180, 180, 180};
	
	public PartSpecTableModel(List<PartSpec> items, JTable table) {

		super(items, columns,table);
		setColumnWidths(columWidths);
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= getRowCount()) return null;
		PartSpec partSpec = items.get(rowIndex);
		
		switch(columnIndex){
		case 0: return partSpec.getId().getPartName();
		case 1: return partSpec.getId().getPartId();
		default:
			return super.getValueAt(rowIndex, columnIndex);
		}
	}

}
