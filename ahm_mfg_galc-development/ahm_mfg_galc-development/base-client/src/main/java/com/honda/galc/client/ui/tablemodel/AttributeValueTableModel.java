/**
 * 
 */
package com.honda.galc.client.ui.tablemodel;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.product.BuildAttribute;

/**
 * 
 * <h3>AttributeListTableModel.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> AttributeListTableModel.java description </p>
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
 * @author KMaharjan - vfc01499
 * Apr 28, 2014
 *
 */

public class AttributeValueTableModel extends SortableTableModel<BuildAttribute> {
	private static final long serialVersionUID = 1L;

	public AttributeValueTableModel(List<BuildAttribute> items, JTable table) {
		super(items, new String[] {"#","Attribute Value","Description"}, table);
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
        
		BuildAttribute item = getItem(rowIndex);
		if (item == null)
			return null;
        
        switch(columnIndex) {
            case 0: return rowIndex + 1;
            case 1: return item.getAttributeValue();
            case 2: return item.getAttributeDescription();
            default: return null;
        }
    }
}
