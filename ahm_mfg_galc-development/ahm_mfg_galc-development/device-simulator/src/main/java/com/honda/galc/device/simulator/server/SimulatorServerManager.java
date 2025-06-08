package com.honda.galc.device.simulator.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <h3>Class description</h3>
 * This class defines defines manager class for SimulatorTcpServer 
 * It manages the client and server mapping 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Paul Chou</TD>
 * <TD>Aug 13, 2007</TD>
 * <TD>1.0</TD>
 * <TD>""</TD>
 * <TD>Initial Version</TD>
 * </TR>
 * </TABLE>
 * @see 
 * <br>
 * @ver 1.0 
 */

public class SimulatorServerManager {

	/* manage TCP Server only now */	
	private Map<String, SimulatorSocketServer> _servers = Collections.synchronizedMap(new HashMap<String, SimulatorSocketServer>());
	
	/**
	 * Add a server into the server list
	 * @param sId
	 * @param s
	 */
	public void add(String p, SimulatorSocketServer s)
	{
		_servers.put(p, s);
	}
	
	/**
	 * Add a server into the server list
	 * @param sId
	 * @param s
	 */
	public void add(int p, SimulatorSocketServer s)
	{
		String sP = "" + p;
		_servers.put(sP, s);
	}
	
	/**
     * Iterate though the server list to find a server on the port
     * @param p
     * @return
     */
    public SimulatorSocketServer findServerOnPort(int p)
    {    	
    	for(SimulatorSocketServer s : _servers.values())    		
    	{
    		if(p == s.getPort())
    			return s;
    	}
    	return null;
    }
    /**
     * Iterate though the server list to find a server on the port
     * @param p
     * @return
     */
    public SimulatorSocketServer findServerOnPort(String sP)
    {    	
    	int p = Integer.valueOf(sP.trim()).intValue();
    	
    	return findServerOnPort(p);
    } 
    
    
    public boolean isServerRunning(int port) {
        SimulatorSocketServer server = findServerOnPort(port);
        return server != null ? server.isRunning() : false; 
    }
    
    /**
     * Iterate though the server list to find a server on the 
     * same port and has the same usingXML setting
     * @param p
     * @return
     */
    public SimulatorSocketServer findServer(int iP, boolean bXML)
    {    	
    	for(SimulatorSocketServer s : _servers.values())    		
    	{
    		if(iP == s.getPort() && bXML == s.isUseXML())
    			return s;
    	}
    	return null;
    	
    }
    
    /**
     * Iterate though the server list to find a server on the 
     * same port and has the same usingXML setting 
     * @param p
     * @return
     */
    public SimulatorSocketServer findServer(String sP, boolean bXML)
    {    	
    	int p = Integer.valueOf(sP.trim()).intValue();
    	return findServer(p, bXML);
    }
 
	/**
	 * Stop the current selected server
	 *
	 */
	public void stopServer(int port)
	{
		//find the server
		SimulatorSocketServer crrentServer = findServerOnPort(port);		
		
		crrentServer.stopRunning();
		
	}
	
	/**
	 * Stop all current running servers
	 *
	 */
	public void stopAllServers()
	{
		for(SimulatorSocketServer s : _servers.values())    		
    	{    		
    		if(s.isRunning())
    		{
    			s.stopRunning();
    			
    		}
    	}
		
	}
	
	/**
	 * Return the number of servers in the list
	 * @return
	 */
	public int size()
	{
		return _servers.size();
	}

	public Map<String, SimulatorSocketServer> get_servers() {
		return _servers;
	}
	
	public void cleanUp()
	{
		for(SimulatorSocketServer s : _servers.values())    		
    	{    		
    		if(s.isRunning()) s.stopRunning();
    		
    		_servers.remove("" + s.getPort());
    		s = null;
    		
    	}
		
		_servers.clear();
	}
	
}
