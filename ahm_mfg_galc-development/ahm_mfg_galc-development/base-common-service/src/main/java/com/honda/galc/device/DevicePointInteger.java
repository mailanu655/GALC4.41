package com.honda.galc.device;

/**
 * 
 * <h3>DevicePointInteger</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DevicePointInteger description </p>
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
 * Nov 5, 2010
 *
 */

public class DevicePointInteger extends DevicePoint{

	public DevicePointInteger(String clientId, String name, Object value) {
		super(clientId, name);
		this.setValue((Integer)value);
	}

	@Override
	public Object getValue() {
		return (Integer)super.getValue();
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	
	

}
