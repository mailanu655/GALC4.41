package com.honda.galc.client.datacollection.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.honda.galc.constant.InstalledPartShipStatus;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;

public class OfflineRepairStatusCellRender extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
	
			 if(value instanceof InstalledPartStatus){
			
				InstalledPartStatus status = (InstalledPartStatus)value;
				if(status == null || status == InstalledPartStatus.BLANK)
					super.setBackground(Color.white);
				else if(status == InstalledPartStatus.NG || status == InstalledPartStatus.REJECT ||
						status == InstalledPartStatus.NM)
					super.setBackground(Color.red);
				else if(status == InstalledPartStatus.NC)
					super.setBackground(Color.orange);
				else if(status == InstalledPartStatus.OK)
					super.setBackground(Color.green); 
			}else{
				super.setBackground(Color.white);
			}
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
		return this;
	}

}
