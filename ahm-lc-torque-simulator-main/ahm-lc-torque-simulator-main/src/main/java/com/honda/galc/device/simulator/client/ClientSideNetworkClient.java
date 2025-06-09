package com.honda.galc.device.simulator.client;

import java.io.IOException;

import com.honda.galc.data.DataContainer;
/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Define the client interface for Client to communicate with Server.
 * <P>
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
 * <TD>K.Sone</TD>
 * <TD>(2001/03/02 22:02:00)</TD>
 * <TD>0.1</TD><TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * <TR>
 * <TD>M.Hayasibe</TD>
 * <TD>(2001/05/10 07:30:00)</TD>
 * <TD>0.2</TD><TD>(none)</TD>
 * <TD>Add Method :<Br>  transmit(String, DataContainer)</TD>
 * </TR>
 * <TR>
 * <TD>K.Sone</TD>
 * <TD>(2001/06/07 13:15:00)</TD>
 * <TD>0.3</TD><TD>(none)</TD>
 * <TD>Modify Method :<Br>  transmit(String, DataContainer)<Br>  It does not throw GALCException.</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 0.3
 * @author M.Hayasibe
 */
public interface ClientSideNetworkClient {

	/**
	 * Transmit DataContainer to Servlet.
	 * @param aData Transmitting DataContainer to Servlet
	 * @return Return from Servlet
	 * @throws IOException When input/output error accrues at 
	 * the time of establishing connection of Host. 
	 * @exception com.honda.global.galc.common.GALCException When
	 * the abnormal accrues in the system.
	 */
	public DataContainer send(DataContainer aData)
		throws IOException;
/**
 * Give the data transmitting function for EquipmentInterface.
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
public DataContainer transmit(
    String aProcessPointID,
    String aClient,
    DataContainer aData)
    throws java.io.IOException;
/**
 * Insert the method's description here.
 * Creation date: (2001/06/05 11:30:23)
 * @return com.honda.global.galc.common.data.DataContainer
 * @param aClient java.lang.String
 * @param aData com.honda.global.galc.common.data.DataContainer
 * @exception java.io.IOException The exception description.
 */
public DataContainer transmitAsync(
    String aProcessPointId,
    String aClient,
    DataContainer aData)
    throws java.io.IOException;
}
