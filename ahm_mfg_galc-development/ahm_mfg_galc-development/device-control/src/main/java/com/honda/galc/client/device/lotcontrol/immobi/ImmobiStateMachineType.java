
package com.honda.galc.client.device.lotcontrol.immobi;
/**
 * @author Gangadhararao Gadde, Subu Kathiresan
 * 
 */
public enum ImmobiStateMachineType {
	WITH_KEYSCAN("WITH_KEYSCAN"),
	WITHOUT_KEYSCAN("WITHOUT_KEYSCAN");	
	
	private String _stateMachineType = "";

	ImmobiStateMachineType(String stateMachineType)
	{
		_stateMachineType = stateMachineType;
	}

	public String getStateMachineType()
	{
		return _stateMachineType;
	}
	
}
