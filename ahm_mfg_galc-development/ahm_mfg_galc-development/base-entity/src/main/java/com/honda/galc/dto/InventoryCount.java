package com.honda.galc.dto;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>Inventory Class description</h3>
 * <p> Inventory description </p>
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
 * @author Jeffray Huang<br>
 * Nov 19, 2012
 *
 *
 */
public class InventoryCount {

	public String processPointId = "";
	public String plant = "";
	public int count = 0;
	public int holdCount = 0;
	
	public String getProcessPointId() {
		return StringUtils.trim(processPointId);
	}
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	public String getPlant() {
		return StringUtils.trim(plant);
	}
	public void setPlant(String plant) {
		this.plant = plant;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getHoldCount() {
		return holdCount;
	}
	public void setHoldCount(int holdCount){
		this.holdCount = holdCount;
	}
	
}

