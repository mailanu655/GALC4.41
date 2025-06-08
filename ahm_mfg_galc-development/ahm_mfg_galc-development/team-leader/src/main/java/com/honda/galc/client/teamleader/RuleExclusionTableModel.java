/**
 * 
 */
package com.honda.galc.client.teamleader;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.product.RuleExclusion;

/**
 * @author vf031824
 *
 */
public class RuleExclusionTableModel extends SortableTableModel<RuleExclusion> {

	private static final long serialVersionUID = 1L;


	public RuleExclusionTableModel(JTable table, List<RuleExclusion> items) {
		super(items, new String[] {"#", "Part Name", "Product Spec Code", "Create Timestamp"} , table);
		setColumnWidths(new int[]{100, 400, 400, 400});
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		RuleExclusion rule = getItem(rowIndex);

		switch(columnIndex) {
		case 0: return rowIndex + 1;
		case 1: return rule.getId().getPartName();
		case 2: return rule.getId().getProductSpecCode();
		case 3: 
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 'at' hh:mm:ss aaa");
			return format.format(rule.getCreateTimestamp());
		}
		return null;
	}

	public boolean hasExcludedPart(String partName, String productSpecCode) {
		for (RuleExclusion rule : getItems()) {
			if(rule.getId().getPartName().equals(partName) &&
					rule.getId().getProductSpecCode().equals(productSpecCode)) return true;
		}
		return false;
	}
}