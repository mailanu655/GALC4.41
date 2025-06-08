package com.honda.galc.client.common.data;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.product.InstalledPart;
/**
 * 
 * <h3>InstalledPartTableModel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> InstalledPartTableModel description </p>
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
public class InstalledPartTableModel extends BaseTableModel<InstalledPart> {
	private static final long serialVersionUID = 1L;
	final static String[] columns =  new String[] {"PRODUCT_ID", "PART_NAME ", "PART_ID", "PART_SERIAL_NUMBER", "INSTALLED_PART_STATUS", 
			"PASS_TIME", "INSTALLED_PART_REASON", "ASSOCIATE_NO", "FIRST_ALARM", "SECOND_ALARM", "ACTUAL_TIMESTAMP", 
			"CREATE_TIMESTAMP", "UPDATE_TIMESTAMP"};
	
	int[] columWidths = {140, 150, 65, 160, 150, 65, 160, 80, 65, 65, 180, 180, 180};
	
	public InstalledPartTableModel(List<InstalledPart> items, JTable table) {
		
		super(items, columns,table);
		setColumnWidths(columWidths);
	}


	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= getRowCount()) return null;
		InstalledPart installedPart = items.get(rowIndex);
		
        switch(columnIndex){
            case 0: return installedPart.getId().getProductId();
            case 1: return installedPart.getId().getPartName();
            default:
            	return super.getValueAt(rowIndex, columnIndex);
        }
	}

}
