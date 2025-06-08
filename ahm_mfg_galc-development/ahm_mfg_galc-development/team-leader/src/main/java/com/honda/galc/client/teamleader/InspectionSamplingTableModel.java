package com.honda.galc.client.teamleader;

import java.util.List;
import javax.swing.JTable;
import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.product.InspectionSampling;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Oct 01, 2019
 */
public class InspectionSamplingTableModel extends SortableTableModel<InspectionSampling> {

	private static final long serialVersionUID = 1L;

	public InspectionSamplingTableModel(JTable table, List<InspectionSampling> items) {
		super(items, new String[] { "Model", "Type", "Sampling Test Size", "Spec Check Test Size" }, table);
		setColumnWidths(new int[] { 200, 200, 200, 200 });
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		InspectionSampling row = getItem(rowIndex);
		switch (columnIndex) {
		case 0:
			return row.getId().getModelCode();
		case 1:
			return row.getId().getModelTypeCode();
		case 2:
			return new String(row.getEmissionMoleculeMaster() + "/" + row.getEmissionDenominatorMaster());
		case 3:
			return new String(row.getDimensionMoleculeMaster() + "/" + row.getDimensionDenominatorMaster());
		}
		return null;
	}

}
