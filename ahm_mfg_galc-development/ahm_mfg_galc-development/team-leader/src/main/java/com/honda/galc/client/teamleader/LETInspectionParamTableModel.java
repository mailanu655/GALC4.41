/**
 * 
 */
package com.honda.galc.client.teamleader;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.product.LetInspectionParam;

/**
 * @author vf031824
 *
 */
public class LETInspectionParamTableModel extends SortableTableModel<LetInspectionParam> {
	private static final long serialVersionUID = 1L;

	public LETInspectionParamTableModel(JTable table, List<LetInspectionParam> items) {
		super(items, new String[] { "#", "Inspection Param Name"},table);
		this.setAlignment(JLabel.CENTER);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		LetInspectionParam param = getItem(rowIndex);

		switch(columnIndex) {
		case 0 : return rowIndex + 1;
		case 1 : return param.getInspectionParamName();
		}

		return null;
	}
}