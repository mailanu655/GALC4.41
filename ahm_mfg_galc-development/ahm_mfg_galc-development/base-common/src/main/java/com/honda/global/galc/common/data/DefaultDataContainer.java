package com.honda.global.galc.common.data;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * <h3>DefaultDataContainer is the default implementation class of DataContainer interface.</h3>
 * <h4>Description</h4>
 * DefaultDataContainer has one Hashtable which maps keys to values.
 * <h4>Usage and Example</h4>
 * // How to constructs DataContainer
 * DataContainer dc = new DefaultDataContainer();
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
 * <TD>(2001/02/05 13:10:06)</TD>
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
 * <TD>(2001/11/02 15:00:00)</TD>
 * <TD>0.2</TD><TD>(none)</TD>
 * <TD>Revise Javadoc</TD>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>May 28, 2004</TD>
 * <TD>1.0</TD>
 * <TD>OIM70</TD>
 * <TD>Make a reasonable guess at buffer size for toString().</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 0.2
 * @author K.Sone
 */
public class DefaultDataContainer
	implements DataContainer, Serializable, Cloneable {

	/**
	 * Hashtable in which data is inputted.
	 */
	protected Hashtable data = new Hashtable();
	/**
	 * Constructs a new DefaultDataContainer object.
	 */
	public DefaultDataContainer() {
	}
	/**
	 *  Clears own hashtable so that it contains no keys.
	 */
	public void clear() {
		data.clear();
	}
	/**
	 * Deprecated, not in use.
	 * Clears own hashtable without DataContainerTag.CLIENT_ID and DataContainerTag.REQUEST_ID.
	 */ 
	public void clearHash() {
		String clientId = (String)get(DataContainerTag.CLIENT_ID);
		String requestId = (String)get(DataContainerTag.REQUEST_ID);
		this.clear();
		if(clientId != null) {
			put(DataContainerTag.CLIENT_ID, clientId);	
		}
		if(requestId != null) {
			put(DataContainerTag.REQUEST_ID, requestId);
		}
	}
	/**
	 * Constructs the copy of DefaultDataContainer.
	 * But Keys or Values is not copied. not deep clone.
	 * @return Clone of DefaultDataContainer.
	 */
	public Object clone() {
		Hashtable newData = (Hashtable) data.clone();

		DefaultDataContainer d;
		try {
			d = (DefaultDataContainer) super.clone();
		}
		catch (CloneNotSupportedException e) {
			// this shouldn't happen, since we are Cloneable
			throw new InternalError();
		}
		d.data = newData;
		return d;
	}
	/**
	 * Returns true if own Hashtable maps one or more keys to this value.
	 * @param aValue Retrieving value.
	 * @return When a key is mapping with the value argument of Hashtable
	 * which decided by equals method, it is true, when not, it is false.
	 */
	public boolean contains(Object aValue) {
		return data.contains(aValue);
	}
	/**
	 * Tests if the specified object is a key in own hashtable.
	 * @param aKey inspecting key.
	 * @return When specified Object is the key of this Hashtable,
	 * it is true, and when not, it is false.
	 */
	public boolean containsKey(Object aKye) {
		return data.containsKey(aKye);
	}
	/**
	 * Returns an enumeration of the values in own hashtable. 
	 * @return List of Value.
	 */
	public Enumeration elements() {
		return data.elements();
	}
	/**
	 * Return the value mapped to the specified key.
	 * @param aKey Key.
	 * @return Value mapped to Key.
	 */
	public Object get(Object aKey) {
		return data.get(aKey);
	}
	/**
	 * Gets EI ClientID.
	 * @return java.lang.String ClientID
	 */
	public String getClientID() {
		return (String)get(DataContainerTag.CLIENT_ID);	
	}
	/**
	 * Return all the list of registered keys.
	 * @return List of key.
	 */
	public Enumeration keys() {
		return data.keys();
	}
	/**
	 * Map the specified key to the specified value. 
	 * Key or Value should not be null.
	 * @param aKey Key.
	 * @param aValue Value.
	 */
	public Object put(Object aKey, Object aValue) {
		return data.put(aKey, aValue);
	}
	/**
	 * Delete the key (and value corresponding to it). 
	 * When the key does not exist, do not do anything.
	 * @param aKey deleting key.
	 * @return Value mapped to the key.
	 */
	public Object remove(Object aKey) {
		return data.remove(aKey);
	}
	/**
	 * Sets EI ClientID.
	 * @param aClientId EI ClientID.
	 */
	public void setClientID(String aClientId) {
		put(DataContainerTag.CLIENT_ID, aClientId);	
	}
	/**
	 * Return number of keys in DataContainer. 
	 * @return Number of keys in DataContainer.
	 */
	public int size() {
		return data.size();
	}
/**
 * Returns a string representation of the object.
 * @return a string representation of the object.
 */
public String toString()
{
    StringBuffer buf = new StringBuffer((data.size()+1)* 36); //@OIM70
    //System.out.println("DefaultDataContainer start at: "+buf.capacity());

    buf.append("__{\n");
    // @WAS61
    for (Enumeration myenum = data.keys(); myenum.hasMoreElements();)
    {
        Object key = myenum.nextElement();
        buf.append(key.toString());
        buf.append(" : \n");  // @JJM-Memory
        buf.append("\t[");
        String upperCaseKey = key.toString().toUpperCase();
        if(upperCaseKey.endsWith("PASSWORD")) {
        	StringBuilder aBuilder = new StringBuilder();
        	for(int i = 0; i < data.get(key).toString().length(); i++) 
        		aBuilder.append("*");
        	buf.append(aBuilder.toString());
        } else {
        	buf.append(data.get(key).toString());
        }
        buf.append("]\n");
        
		//System.out.println("DefaultDataContainer: "+buf.capacity());
    }
    buf.append("}__");
    
	//System.out.println("DefaultDataContainer final: "+buf.capacity()+" actual:"+buf.length()+"\n");
	
	if (buf.capacity() > 100000)
	{
		String ppid = (String)data.get(DataContainerTag.PROCESS_POINT_ID);
			
		String client_id = (String)data.get(DataContainerTag.CLIENT_ID);
		
		if (ppid == null)
		{
			ppid = "";
		}
		
		if (client_id == null)
		{
			client_id = "";
		}
			
		System.out.println("LARGE DATA CONTAINER toString() BUFFER: DefaultDataContainer final: "+buf.capacity()+" actual:"+buf.length()+
                           " client,pp= [" + client_id + ","+ppid+"] \n");
	
	}
    return buf.toString();
}

}
