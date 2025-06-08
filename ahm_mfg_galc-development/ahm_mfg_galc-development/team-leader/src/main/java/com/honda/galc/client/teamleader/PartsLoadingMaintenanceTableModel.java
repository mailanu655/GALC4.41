package com.honda.galc.client.teamleader;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.product.PartsLoadingMaintenance;

public class PartsLoadingMaintenanceTableModel extends SortableTableModel<PartsLoadingMaintenance> {

	private static final long serialVersionUID = 1L;


	public PartsLoadingMaintenanceTableModel(JTable table,List<PartsLoadingMaintenance> items) {
		super(items, new String[] {"#","Bin Name","Part Name","Part Spec"},table);
		setColumnWidths(new int[] {50,300,300,100});
	}

	public Object getValueAt(int rowIndex, int columnIndex) {

		PartsLoadingMaintenance bin = getItem(rowIndex);

		switch(columnIndex) {
		case 0: return rowIndex + 1;
		case 1: return bin.getBinName();
		case 2: return bin.getPartName();
		case 3: return bin.getPartSpecId();
		}
		return null;
	}

	public boolean hasBinName(String name) {
		for(PartsLoadingMaintenance binName : getItems()) {
			if(binName.getBinName().equals(name))
				return true;
		}
		return false;
	}

	public boolean isPartSpecAddedToBin(PartsLoadingMaintenance partsLoadingMaintenance) {
		for(PartsLoadingMaintenance binName : getItems()) {

			if(binName.getPartName().equals(partsLoadingMaintenance.getPartName()) &&
					binName.getPartSpecId().equals(partsLoadingMaintenance.getPartSpecId())){
				return true;
			}
		}
		return false;
	}
}
