package com.honda.galc.device;

/**
 * 
 * <h3>DevicePoint</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DevicePoint description </p>
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

public class DevicePoint implements IDeviceData, IPlcData{
	private String clientId;
	private String name;
    protected Object value;
	
    public DevicePoint(String clientId, String name, Object value) {
		this(clientId, name);
		this.value = value;
	}
    
	public DevicePoint(String clientId, String name) {
		super();
		this.clientId = clientId;
		this.name = name;
	}

	//Getter & Setters
    public String getClientId() {
		return clientId;
	}
    
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Object getValue() {
		return value;
	}
	

	public void setValue(Object value) {
		this.value = value;
	}
	
}
