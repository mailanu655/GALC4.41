/**
 * 
 */
package com.honda.galc.client.teamleader;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.product.LetInspectionProgram;

/**
 * @author vf031824
 *
 */
public class LETInspectionProgramTableModel extends SortableTableModel<LetInspectionProgram> {


	private static final long serialVersionUID = 1L;

	public LETInspectionProgramTableModel(JTable table, List<LetInspectionProgram> items) {
		super(items, new String[] { "#", "Inspection Program Name"},table);
		this.setAlignment(JLabel.CENTER);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		LetInspectionProgram program = getItem(rowIndex);

		switch(columnIndex) {
		case 0 : return rowIndex + 1;
		case 1 : return program.getInspectionPgmName();
		}
		return null;
	}
}
