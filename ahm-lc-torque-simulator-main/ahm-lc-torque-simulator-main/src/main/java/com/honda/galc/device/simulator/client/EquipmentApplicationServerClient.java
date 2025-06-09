package com.honda.galc.device.simulator.client;

import com.honda.galc.data.DataContainer;




/**
 * <h3>EuipmentApplicationServerClient</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>This is the interface that an equipment-interface client can use to send 
 * data containers up to the application server.
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
 * <TD>martinek</TD>
 * <TD>Jun 4, 2007</TD>
 * <TD></TD>
 * <TD>JM200710</TD>
 * <TD>New interface established for OPC Equipment Interface Server</TD>
 * </TR>
 * </TABLE>
 */
public interface EquipmentApplicationServerClient extends ClientSideNetworkClient {

	/**
	 * Instruct the server client to use XML instead of DataContainers
	 * @param useXML
	 */
	public void setUseXML(boolean useXML);

	/**
	 * Determine if the client is using xml to converse with the server.
	 * @return boolean - true means that the client is using XML
	 */
	public boolean isUseXML();

	/**
	 * Set a Java Logger object into the client class for use with tracing
	 * and error logging. 
	 */
	public void setLogger(java.util.logging.Logger newLogger);
	
	/**
	 * 
	 * Give the data transmitting function for EquipmentInterface. This 
	 * implementation will invoke the "stateless" controllers.
	 * <P>
	 * The argument except DataContainer is all put() with
	 * the contents of DataContainerTag as a key to DataContainer of
	 * the argument.
	 * @return 
	 * @param aClient ClientID
	 * @param aData Transmitting DataContainer
	 * @exception java.io.IOException When input/output error accrues at
	 * the time of establishing the connection with Host.
	 */
	public DataContainer transmitStateless(
		String aProcessPointId,
		String aClient,
		DataContainer aData)
		throws java.io.IOException;

	/**
	 * @JM200710
	 * One of the data transmitting functions for EquipmentInterface. This 
	 * implementation will invoke the "stateless" controllers with the async
	 * flag - only an acknowledgement will be returned by the server.
	 * <P>
	 * The argument except DataContainer is all put() with
	 * the contents of DataContainerTag as a key to DataContainer of
	 * the argument.
	 * @return 
	 * @param aClient ClientID
	 * @param aData Transmitting DataContainer
	 * @exception java.io.IOException When input/output error accrues at
	 * the time of establishing the connection with Host.
	 */
	public DataContainer transmitStatelessAsync(
		String aProcessPointId,
		String aClient,
		DataContainer aData)
		throws java.io.IOException;


}
