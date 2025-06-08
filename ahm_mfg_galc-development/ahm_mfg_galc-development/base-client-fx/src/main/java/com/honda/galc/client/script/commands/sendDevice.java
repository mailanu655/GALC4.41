package com.honda.galc.client.script.commands;

import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.device.dataformat.DataCollectionComplete;

import bsh.CallStack;
import bsh.Interpreter;

/**
 * 
 * <h3>sendDevice</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> sendDevice description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Apr 13, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Apr 13, 2012
 */
public class sendDevice {
	public static String usage() {
		return "usage: sendDevice( String | DataContainer | Device )";
	}

	/**
	 * Implement sendDevice command.
	 * @param env
	 * @param callstack
	 */

	public static void invoke(Interpreter env, CallStack callstack, String status ) 
	{
		try {
			EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
			eiDevice.syncSend(new DataCollectionComplete(status));
		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed to write status: " + status);
		}
	}

	public static void invoke(Interpreter env, CallStack callstack, DataContainer dc ) 
	{
		try {
			EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
			eiDevice.syncSend(dc.getClientID(), dc);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed to write data: " + dc);
		}
	}
}
