package com.honda.galc.device.simulator.data;

/**
 * <h3>SimulatorConstants</h3>
 * <h4> </h4>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Sep. 23, 2008</TD>
 * <TD>Version 0.1</TD>
 * <TD></TD>
 * <TD>Initial version.</TD>
 * </TR>
 * </TABLE>
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class SimulatorConstants {
	public enum ClientType {HTTPClient, RouterClient, EmbeddedClient, HTTPServiceClient};
	public enum ClientMode {StatelessTransmit, StatelessAsyncTransmit, Tansmit, TansmitAsync};
	public enum EiType {OPC, EI};
	public enum ServerType{Galc_Server, Legacy_Galc_Server, Rest_Service};
	
	/* device types */
	public static final int DEVICE_TYPE_EQUIPMENT = 0;
	public static final int DEVICE_TYPE_PRINTER = 1;
	public static final int DEVICE_TYPE_VENDOR_APPLICATION = 2;
	public static final int DEVICE_TYPE_OPC = 3;
	public final static int STATIC_TAG_TYPE = 1;
	public final static int NONE_TAG_TYPE = 0;
	public static final String PROP_SIMULATOR = "resource/com/honda/galc/device/simulator/DevSim.properties";
	public static final String PROP_OPENPROTOCOL = "resource/com/honda/galc/device/simulator/OpenProtocol.properties";
	
	
}
