package com.honda.galc.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

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
public class DefaultDataContainer extends HashMap<Object,Object>
	implements DataContainer {

	
    private static final long serialVersionUID = 1L;

    /**
	 * Constructs a new DefaultDataContainer object.
	 */
	public DefaultDataContainer() {
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
	 * Gets EI ClientID.
	 * @return java.lang.String ClientID
	 */
	public String getClientID() {
		return (String)get(DataContainerTag.CLIENT_ID);	
	}
	
	
	
	/**
	 * Sets EI ClientID.
	 * @param aClientId EI ClientID.
	 */
	public void setClientID(String aClientId) {
		put(DataContainerTag.CLIENT_ID, aClientId);	
	}
	
/**
 * Returns a string representation of the object.
 * @return a string representation of the object.
 */
public String toString()
{
    StringBuffer buf = new StringBuffer((size()+1)* 36); //@OIM70
    //System.out.println("DefaultDataContainer start at: "+buf.capacity());

    buf.append("__{\n");
    // @WAS61
    
    Iterator<?> iter = keySet().iterator();
    while(iter.hasNext()) {
    
        Object key = iter.next();
        buf.append(key.toString());
        buf.append(" : \n");  // @JJM-Memory
        buf.append("\t[");
        String upperCaseKey = key.toString().toUpperCase();
        if(upperCaseKey.endsWith("PASSWORD")) {
        	StringBuilder aBuilder = new StringBuilder();
        	if(null!=get(key)){
	        	for(int i = 0; i < get(key).toString().length(); i++) 
	        		aBuilder.append("*");
        	}
        	buf.append(aBuilder.toString());
        } else {
        	if(null!=get(key)){
        		buf.append(get(key).toString());
        	}
        }
        buf.append("]\n");
        
		//System.out.println("DefaultDataContainer: "+buf.capacity());
    }
    buf.append("}__");
    
	//System.out.println("DefaultDataContainer final: "+buf.capacity()+" actual:"+buf.length()+"\n");
	
	if (buf.capacity() > 100000)
	{
		String ppid = (String)get(DataContainerTag.PROCESS_POINT_ID);
			
		String client_id = (String)get(DataContainerTag.CLIENT_ID);
		
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

public void putAll(DataContainer dataContainer) {
    
    Iterator<Object> iter = dataContainer.keySet().iterator();
    while(iter.hasNext()) {
        Object key = iter.next();
        put(key,dataContainer.get(key));
    }
    
}

public String getString(String key) {
	
	return ObjectUtils.toString(super.get(key));
	
}

	public void addErrorMsg(String msg) {
		if (StringUtils.isBlank(msg)) {
			return;
		}
		List<String> msgs = getErrorMessages();
		if (msgs == null) {
			msgs = new ArrayList<String>();
			put(DataContainerTag.ERROR_MESSAGE, msgs);
		}
		msgs.add (0,  msg);
	}

	@SuppressWarnings("unchecked")
	public List<String> getErrorMessages() {
		Object obj = get(DataContainerTag.ERROR_MESSAGE);
		if (obj instanceof List<?>) {
			return (List<String>) obj;
		} 
		return null;
	}

	public void putException(Exception e) {
		if (e != null) {
			put(DataContainerTag.EXCEPTION, e);
		}
	}

	public Exception getException() {
		Object o = get(DataContainerTag.EXCEPTION);
		if (o instanceof Exception) {
			return (Exception) o;
		}
		return null;
	}
}
