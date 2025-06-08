package com.honda.galc.device;

/**
 * 
 * <h3>DevicePointShort</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DevicePointShort description </p>
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

public class DevicePointShort extends DevicePoint{

	public DevicePointShort(String clientId, String name, Object value) {
		super(clientId, name);
		this.setValue((Short)value);
	}

	@Override
	public Short getValue() {
		return (Short)super.getValue();
	}

	public void setValue(Short value) {
		this.value = value;
	}

}
