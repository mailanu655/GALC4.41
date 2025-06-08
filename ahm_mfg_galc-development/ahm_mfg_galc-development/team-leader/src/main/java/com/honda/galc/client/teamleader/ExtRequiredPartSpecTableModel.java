/**
 * 
 */
package com.honda.galc.client.teamleader;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.client.ui.component.ComboBoxCellEditor;
import com.honda.galc.client.ui.component.ComboBoxCellRender;
import com.honda.galc.dto.ExtRequiredPartSpecDto;
import com.honda.galc.entity.enumtype.ParseStrategyType;
import com.honda.galc.util.CommonPartUtility;

/**
 * @author vf031824
 *
 */
public class ExtRequiredPartSpecTableModel extends BaseTableModel<ExtRequiredPartSpecDto> {

	private static final long serialVersionUID = 1L;

	public ExtRequiredPartSpecTableModel(JTable table,List<ExtRequiredPartSpecDto> items, List<String> partGroups) {
		super(items, new String[] {"#","Part Name","Part Id", "Description","Part Mask","Part Group","Parser","Parse Info"},table);
		this.setAlignment(JLabel.CENTER);
		this.setColumnWidths(new int[] {30,150,80,100,150,150,150,200,120,150});

		setParseStrategyComboCell();
		setPartGroupComboCell(partGroups);
	}

	public ExtRequiredPartSpecTableModel(JTable table,List<ExtRequiredPartSpecDto> items, boolean partNameOnly) {
		super(items, new String[] {"#","Part Name","Part Id", "Description","Part Mask"},table);
		this.setAlignment(JLabel.CENTER);
		this.setColumnWidths(new int[] {30,150,80,100,100});
	}

	public Object getValueAt(int rowIndex, int columnIndex) {

		ExtRequiredPartSpecDto partSpec = getItem(rowIndex);

		switch(columnIndex) {
		case 0: return rowIndex + 1;
		case 1: return partSpec.getPartName();
		case 2: return partSpec.getPartId();
		case 3: return partSpec.getPartDescription();
		case 4: return getPartSerialNumberMaskDisplayName(partSpec.getPartSerialNumberMask());
		case 5: return partSpec.getPartGroup();
		case 6: return getStrategyDisplayName(partSpec.getParseStrategy());
		case 7: return partSpec.getParserInformation();
		}
		return null;
	}

	public void setValueAt(Object value, int row, int column) {
		if(row >= getRowCount() || column <5) return;
		super.setValueAt(value, row, column);
		ExtRequiredPartSpecDto partSpec = getItem(row);
		String valueString = value == null ? null : value.toString().trim();
		if(column == 5) {
			partSpec.setPartGroup(parseString(valueString,64));
		}else if(column == 6) {
			partSpec.setParseStragety(ParseStrategyType.getName(parseString(valueString,32)));
		}else if (column == 7) {
			partSpec.setParserInformation(parseString(valueString,64));
		}
		this.fireTableCellUpdated(row, column);
	}

	public boolean isCellEditable (int row, int column){
		return column >= 5;
	}

	private void setParseStrategyComboCell() {
		Object[] parseStrategys = getParseStrategyTypes().toArray();
		TableColumn col = table.getColumnModel().getColumn(6);
		col.setCellEditor(new ComboBoxCellEditor(parseStrategys,false));
		col.setCellRenderer(new ComboBoxCellRender(parseStrategys));
	}
	
	private void setPartGroupComboCell(List<String> partGroups) {
		Object[] objPartGroups = partGroups.toArray();
		TableColumn col = table .getColumnModel().getColumn(5);
		col.setCellEditor(new ComboBoxCellEditor(objPartGroups,false));
		col.setCellRenderer(new ComboBoxCellRender(objPartGroups));
	}

	private ArrayList<String> getParseStrategyTypes() {
		ParseStrategyType[] types = ParseStrategyType.values();
		List<String> asList = new ArrayList<String>();
		for (ParseStrategyType type : types) {
			asList.add(type.name());
		}
		return ParseStrategyType.getComboDisplayNames(asList);
	}

	private String getPartSerialNumberMaskDisplayName(String partSerialNumberMask){

		String amask = StringUtils.replace(partSerialNumberMask,"<<"+CommonPartUtility.WILD_CARD_MULTI_CHARS+">>",String.valueOf(CommonPartUtility.WILD_CARD_MULTI_CHARS));
		amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_ALPHANUMERIC+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_ALPHANUMERIC));
		amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_CHAR+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_CHAR));
		amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_NUMBER+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_NUMBER));
		amask = StringUtils.replace(amask,"<<"+CommonPartUtility.WILD_CARD_ONE_ANYTHING+">>",String.valueOf(CommonPartUtility.WILD_CARD_ONE_ANYTHING));
		return amask;
	}	

	private Object getStrategyDisplayName(String strategy) {
		String strategyName = StringUtils.trimToEmpty(strategy);
		if (strategyName.equals(""))
			return "";

		ParseStrategyType type = null;
		try {
			type = ParseStrategyType.valueOf(strategyName);
		} catch (Exception ex) {}

		if (type != null) return type.getDisplayName();
		else return "";
	}

	private String parseString(String valueString, int length) {
		if ( valueString != null && valueString.length() > length) {
			MessageDialog.showError(getParentComponent(),String.format("'%.10s...' value must not exceed %d characters in length",valueString, length));
			throw new IllegalArgumentException();
		} else {
			return valueString;
		}
	}
	
	public boolean hadRequiredPart(String partName, String partID) {
		for(ExtRequiredPartSpecDto part : getItems()) {
			if(part.getPartName().equals(partName) &&
					part.getPartId().equals(partID)) return true;
		}
		return false;
	}
}