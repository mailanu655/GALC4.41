package com.honda.galc.client.ui.tablemodel;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.data.MbpnDef;
import com.honda.galc.entity.product.Mbpn;

public class MbpnMaintenanceTableModel extends SortableTableModel<Mbpn> {

	private static final long serialVersionUID = 1L;
	private static String[] mbpnHeader = {"#","Main No","Class No", "ProtoType Code", "Type No","Supplementary No", "Target No", "Hes Color", "Mask Id", "Description", "Disabled"};

	public MbpnMaintenanceTableModel(JTable table, List<Mbpn> items) {
		super(items, mbpnHeader, table);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Mbpn item = getItem(rowIndex);
		return getMbpnValue(rowIndex, columnIndex, item);
	}

	private Object getMbpnValue(int rowIndex, int columnIndex, Mbpn item) {
		switch(columnIndex) {
		case 0: return rowIndex + 1;
		case 1: return MbpnDef.MAIN_NO.getValue(item.getProductSpecCode());
		case 2: return MbpnDef.CLASS_NO.getValue(item.getProductSpecCode());
		case 3: return MbpnDef.PROTOTYPE_CODE.getValue(item.getProductSpecCode());
		case 4: return MbpnDef.TYPE_NO.getValue(item.getProductSpecCode());
		case 5: return MbpnDef.SUPPLEMENTARY_NO.getValue(item.getProductSpecCode());
		case 6: return MbpnDef.TARGET_NO.getValue(item.getProductSpecCode());
		case 7: return MbpnDef.HES_COLOR.getValue(item.getProductSpecCode());
		case 8: return item.getMaskId();
		case 9: return item.getDescription();
		case 10: return item.isDisabled();
		}
		return null;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex ==  10) {
			return Boolean.class;
		} else {
			return super.getColumnClass(columnIndex);
		}
	}

}
