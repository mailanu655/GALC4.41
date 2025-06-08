package com.honda.galc.client.teamleader;

import java.util.List;
import javax.swing.JTable;
import com.honda.galc.client.ui.component.SortableTableModel;

/**
 * 
 * <h3>ProductIdMaskListTableModel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> table model for product Id Mask</p>
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
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Kamlesh Maharjan
 * March 05, 2016
 * 
 */
public class ProductIdMaskListTableModel extends SortableTableModel<String> {
	
	private static final long serialVersionUID = 1L;

	public ProductIdMaskListTableModel(JTable table,List<String> items,boolean partNameOnly) {
		super(items, new String[] {"#","Product ID Mask"},table);
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {

		switch(columnIndex) {
            case 0: return rowIndex + 1;
            case 1: return getItem(rowIndex);
        }
        return null;
    }

}
