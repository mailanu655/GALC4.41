package com.honda.galc.client.teamleader;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.honda.galc.constant.InstalledPartShipStatus;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
/**
 * 
 * <h3>ManualLotCtrRepairStatusCellRender</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ManualLotCtrRepairStatusCellRender description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Paul Chou
 * Aug 4, 2010
 *
 */
public class ManualLotCtrRepairStatusCellRender extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int column) {

		//Set default white color
		super.setBackground(Color.white);
		
		if(value instanceof String){
			String shipStatus = (String)value;
			if(InstalledPartShipStatus.NG.toString().equalsIgnoreCase(shipStatus)){
				super.setBackground(Color.red);
			}else if(InstalledPartShipStatus.OK.toString().equalsIgnoreCase(shipStatus)){
				super.setBackground(Color.green); 
			}
		}else if(value instanceof InstalledPartStatus){
			InstalledPartStatus status = (InstalledPartStatus)value;
			if(
				status == InstalledPartStatus.NG 
				|| status == InstalledPartStatus.REJECT 
				|| status == InstalledPartStatus.NM
				|| status == InstalledPartStatus.MISSING
			){
				super.setBackground(Color.red);
			}else if(
				status == InstalledPartStatus.NC 
				|| status == InstalledPartStatus.REPAIRED
				|| status == InstalledPartStatus.PENDING
			){
				super.setBackground(Color.orange);
			}else if(status == InstalledPartStatus.OK || status == InstalledPartStatus.ACCEPT){
				super.setBackground(Color.green); 
			}
		}else if(value instanceof MeasurementStatus){
			MeasurementStatus mstatus = (MeasurementStatus)value;
			if(mstatus == MeasurementStatus.NG){
				super.setBackground(Color.red);
			}else if(mstatus == MeasurementStatus.OK){
				super.setBackground(Color.green);
			}
		}
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
		return this;
	}

}
