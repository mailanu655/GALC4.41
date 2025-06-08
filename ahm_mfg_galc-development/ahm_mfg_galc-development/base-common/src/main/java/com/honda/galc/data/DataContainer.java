package com.honda.galc.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <h3>DataContainer is the general interface which the data to be used GALC all functions.</h3>
 * <h4>Description</h4>
 * This interface is used as the communication data between GALCClient and GALCServer,<Br>
 * An implemented class of DataContainer interface will be able to map keys to values. <Br>
 * but objects as keys and values data must implement interface java.io.Serializable.
 * A DataContainer cannot contain duplicate keys and, key and value as null.
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
 * <TD>(2001/03/01 14:48:00)</TD>
 * <TD>0.1</TD><TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * <TR>
 * <TD>K.Sone</TD>
 * <TD>(2001/05/07 15:32:00)</TD>
 * <TD>0.2</TD><TD>(none)</TD>
 * <TD>Add Methods:<Br>  clearHash()<Br>  getClientID()<Br>  setClientID(String)</TD>
 * </TR>
 * <TR>
 * <TD>M.Hayashibe</TD>
 * <TD>(2001/10/29 15:00:00)</TD>
 * <TD>0.2</TD><TD>(none)</TD>
 * <TD>Revise Javadoc</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 0.2
 * @author K.Sone
 */
public interface DataContainer extends Map<Object,Object>,Serializable, Cloneable {

	/**
	 * Gets EI ClientID.
	 * @return java.lang.String
	 */
	public String getClientID();
	/**
	 * Returns an enumeration of the keys.
	 * @return an enumeration of keys.
	 */

	public void setClientID(String aClientId);
	
	public String getString(String key);
	
	public void putAll(DataContainer dataContainer);
	
	public void addErrorMsg(String msg);
	
	public List<String> getErrorMessages();
	
	public void putException(Exception e);
	
	public Exception getException();
}
