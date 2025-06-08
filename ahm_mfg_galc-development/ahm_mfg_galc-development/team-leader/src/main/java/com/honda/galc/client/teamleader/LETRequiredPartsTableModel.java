/**
 * 
 */
package com.honda.galc.client.teamleader;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.client.ui.component.ComboBoxCellEditor;
import com.honda.galc.client.ui.component.ComboBoxCellRender;
import com.honda.galc.dto.LetRequiredPartSpecsDto;
import com.honda.galc.entity.enumtype.LetParamType;

/**
 * @author vf031824
 *
 */
public class LETRequiredPartsTableModel extends BaseTableModel<LetRequiredPartSpecsDto> {

	private static final long serialVersionUID = 1L;

	public LETRequiredPartsTableModel(JTable table, List<LetRequiredPartSpecsDto> items) {
		super(items, new String[] {"#", "Part Name", "Part ID", "Seq #", "Description", "Part Mask", "Param Type", "Program Name", "Param Name"}, table);
		this.setAlignment(JLabel.CENTER);
		table.setRowHeight(25);
		setLetParamTypeComboBoxCell();
	}

	public boolean isCellEditable (int row, int column){
		return column == 6;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {

		LetRequiredPartSpecsDto letRequiredPart = getItem(rowIndex);

		switch(columnIndex) {
		case 0: return rowIndex + 1;
		case 1: return letRequiredPart.getPartName();
		case 2: return letRequiredPart.getPartId();
		case 3 :return letRequiredPart.getSequenceNumber(); 
		case 4: return letRequiredPart.getDescription();
		case 5: return letRequiredPart.getPartSerialNumberMask();
		case 6: return letRequiredPart.getLetParamType();
		case 7: return letRequiredPart.getInspectionPgmName();
		case 8: return letRequiredPart.getInspectionParamName();
		}
		return null;		
	}

	public void setValueAt(Object value, int row, int column) {
		super.setValueAt(value, row, column);
		LetRequiredPartSpecsDto letRequiredPart = getItem(row);
		try {
			if(column == 6) letRequiredPart.setParamType(((LetParamType)value).getId());
		}catch(NumberFormatException e) {
			return;
		}
		this.fireTableCellUpdated(row, column);
	}

	public boolean hasRequiredPart(String partName, String partID, String programName, String paramName) {
		for(LetRequiredPartSpecsDto part : getItems()) {
			if(part.getPartName().equals(partName) &&
					part.getPartId().equals(partID) &&
					part.getInspectionPgmName().equals(programName) &&
					part.getInspectionParamName().equals(paramName)) return true;
		}
		return false;
	}
	
	public boolean hasProgramName(String programName) {
		if(getItems().size() == 0) return false;
		for(LetRequiredPartSpecsDto part : getItems()) {
			if(part.getInspectionPgmName().equals(programName)) {
				return false;
			}
		}
		return true;
	}

	private void setLetParamTypeComboBoxCell() {
		Object[] paramTypes = LetParamType.values();
		TableColumn col = table.getColumnModel().getColumn(6);
		col.setCellEditor(new ComboBoxCellEditor(paramTypes,false));
		col.setCellRenderer(new ComboBoxCellRender(paramTypes));
	}
}