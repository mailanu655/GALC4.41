package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.device.IDeviceData;

/**
 * 
 * <h3>HeartBeat</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> HeartBeat description </p>
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
 * Mar. 5, 2019
 *
 */
public class HeartBeat implements IDeviceData, Serializable{
	private static final long serialVersionUID = 1L;
	private String heartBeat;
	public String getHeartBeat() {
		return heartBeat;
	}
	public void setHeartBeat(String heartBeat) {
		this.heartBeat = heartBeat;
	} 
	
}
