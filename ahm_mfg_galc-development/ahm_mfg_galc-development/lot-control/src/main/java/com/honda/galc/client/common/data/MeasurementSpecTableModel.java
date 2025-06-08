package com.honda.galc.client.common.data;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.product.MeasurementSpec;


/**
 * 
 * <h3>MeasurementSpecTableModel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MeasurementSpecTableModel description </p>
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
public class MeasurementSpecTableModel extends BaseTableModel<MeasurementSpec>{
	private static final long serialVersionUID = 1L;

	final static String[] columns =  new String[] {"#", "PART_NAME", "PART_ID ", "MINIMUM_LIMIT", "MAXIMUM_LIMIT", 
		"MAX_ATTEMPTS", "CREATE_TIMESTAMP", "UPDATE_TIMESTAMP"};
	int[] columWidths = {35, 180, 80, 160, 160, 160, 180, 180};
	
	public MeasurementSpecTableModel(List<MeasurementSpec> items, JTable table) {

		super(items, columns,table);
		setColumnWidths(columWidths);
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= getRowCount()) return null;
		MeasurementSpec measurementSpec = items.get(rowIndex);

		switch(columnIndex){
		case 0: return measurementSpec.getId().getMeasurementSeqNum();
		case 1: return measurementSpec.getId().getPartName();
		case 2: return measurementSpec.getId().getPartId();

		default:
			return super.getValueAt(rowIndex, columnIndex);
		}
	}
}
