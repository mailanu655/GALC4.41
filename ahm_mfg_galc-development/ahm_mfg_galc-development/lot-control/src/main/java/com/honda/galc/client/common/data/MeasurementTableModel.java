package com.honda.galc.client.common.data;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.product.Measurement;
/**
 * 
 * <h3>MeasurementTableModel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MeasurementTableModel description </p>
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
 * May 7, 2010
 *
 */
public class MeasurementTableModel extends BaseTableModel<Measurement>{

	private static final long serialVersionUID = 1L;
	final static String[] columns =  new String[] {"#", "PRODUCT_ID", "PART_NAME", 
		"MEASUREMENT_VALUE", "MEASUREMENT_STATUS","MEASUREMENT_ANGLE","PART_SERIAL_NUMBER",
		"ACTUAL_TIMESTAMP",	"CREATE_TIMESTAMP", "UPDATE_TIMESTAMP"};
	
	int[] columWidths = {35, 140, 150, 145, 145, 145, 160, 180, 180, 180};
	
	public MeasurementTableModel(List<Measurement> items, JTable table) {
		
		super(items, columns,table);
		setColumnWidths(columWidths);
	}


	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= getRowCount()) return null;
		Measurement measurement = items.get(rowIndex);
		
        switch(columnIndex){
            case 0: return measurement.getId().getMeasurementSequenceNumber();
            case 1: return measurement.getId().getProductId();
            case 2: return measurement.getId().getPartName();
            
            default:
            	return super.getValueAt(rowIndex, columnIndex);
        }
	}



}
