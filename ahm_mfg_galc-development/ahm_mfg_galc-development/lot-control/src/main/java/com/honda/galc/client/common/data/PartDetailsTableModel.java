
package com.honda.galc.client.common.data;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.entity.product.DevicePartDetails;

public class PartDetailsTableModel extends BaseTableModel<DevicePartDetails> {
	private static final long serialVersionUID = 1L;
	final static String[] columns =  new String[] {"PART_NAME", "PART_SERIAL_NUMBER_MASK", "INSTRUCTION_CODE", "PART_MAX_ATTEMPTS", "MEASUREMENT_SEQ_NUM", "MINIMUM_LIMIT", "MAXIMUM_LIMIT", "MAX_ATTEMPTS"};
	int[] columWidths = {140, 150, 65, 160, 150, 65, 160, 80, 65, 65, 180, 180, 180};
	
	public PartDetailsTableModel(List<DevicePartDetails> devicePartDetailsList, JTable table) {
		super(devicePartDetailsList, columns,table);
		setColumnWidths(columWidths);
	}
}
