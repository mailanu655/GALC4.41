package com.honda.global.galc.common.data;

import java.util.Enumeration;
import java.io.Serializable;

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
public interface DataContainer extends Serializable, Cloneable {

	/**
	 * Removes all mappings from this DataContainer.
	 */
	public void clear();
	/**
	 * not in use.
	 */
	public void clearHash();
	/**
	 * DataContainer extends Cloneable interface.
	 * Clones DataContainer.
	 * @return Cloned object
	 */
	public Object clone();
	/**
	 * Returns true if DataContainer maps one or more keys to the specified value.
	 * @param aValue value whose presence in this map is to be tested.
	 * @return true if this map maps one or more keys to the specified value.
	 */
	public boolean contains(Object aValue);
	/**
	 * Returns true if this map contains a mapping for the specified key.
	 * @param aKey key whose presence in this DataContainer is to be tested.
	 * @return true if this map contains a mapping for the specified key.
	 */
	public boolean containsKey(Object aKye);
	/**
	 * Returns an enumeration of the values in this DataContainer.
	 * @return an enumeration of the values.
	 */
	public Enumeration elements();
	/**
	 * Returns the value to which this DataContainer maps the specified key.
	 * @param aKey Key.
	 * @return the value to which this DataContainer maps the specified key, or null if the map contains no mapping for this key.
	 */
	public Object get(Object aKey);
	/**
	 * Gets EI ClientID.
	 * @return java.lang.String
	 */
	public String getClientID();
	/**
	 * Returns an enumeration of the keys.
	 * @return an enumeration of keys.
	 */
	public Enumeration keys();
	/**
	 * Maps the specified key to the specified value in this DataContainer.
	 * Key or Value should not be null.
	 * @param aKey Key.
	 * @param aValue Value.
	 */
	public Object put(Object aKey, Object aValue);
	/**
	 * Removes the mapping for this key from DataContainer.
	 * When the key does not exist return null.
	 * @param aKey key whose mapping is to be removed from the DataContainer.
	 * @return Value mapped to the key.
	 */
	public Object remove(Object aKey);
	/**
	 * Sets EI ClientID.
	 * @param aClientId java.lang.String
	 */
	public void setClientID(String aClientId);
	/**
	 * Returns the number of key-value mappings.
	 * @return Number of key-value.
	 */
	public int size();
}
