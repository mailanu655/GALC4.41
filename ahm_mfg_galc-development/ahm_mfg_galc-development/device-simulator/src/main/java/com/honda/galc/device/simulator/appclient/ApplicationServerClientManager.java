package com.honda.galc.device.simulator.appclient;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.device.simulator.client.EquipmentApplicationServerClient;
import com.honda.galc.device.simulator.data.DataContainerCommonUtil;
import com.honda.galc.device.simulator.data.SimulatorConstants;
import com.honda.galc.device.simulator.utils.DevSimulatorUtil;

public class ApplicationServerClientManager {
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	/**
	 * get server client
	 * @param equtype
	 * @param srvUrl
	 * @param clntype
	 * @return
	 * @throws SystemException
	 */
	public EquipmentApplicationServerClient getOpcApplicationServerClient(int equtype, String srvUrl, int clntype) throws SystemException
	{
		switch(equtype) {
		case SimulatorConstants.DEVICE_TYPE_OPC: 
			IApplicationServerClientFactory factory = new OpcApplicationServerClientFactory();
			return factory.getApplicationServerClient(srvUrl, clntype);
		default: 
			log.info("Invalid equipment type " + equtype);
			return null;
		}
	}
	
	/**
	 * get server client
	 * @param srvUrl
	 * @param clntype
	 * @return
	 * @throws SystemException
	 */
    public EquipmentApplicationServerClient getServerClient(int devType, String srvUrl, int clntype, String[] exlHosts, String[] exlIps){
		
		try {
			//double check to make sure message not connect to excluding hosts
			if (!DevSimulatorUtil.checkUrl(srvUrl, exlHosts, exlIps)) {
				log.error("Connect to: " + srvUrl + " is not allowed!");
				throw new SystemException("Connect to: " + srvUrl + " is not allowed!");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new SystemException("Malformed URL Exception.");
		}  
		
		return getOpcApplicationServerClient(SimulatorConstants.DEVICE_TYPE_OPC, srvUrl, clntype);
    	
	}
	
    /**
     * call application server
     * @param appServerClient
     * @param clnmode
     * @param ppid
     * @param device
     * @param dc
     * @return
     * @throws IOException
     */
	public DataContainer callApplicationServer(EquipmentApplicationServerClient appServerClient, int clnmode, String ppid, String device, DataContainer dc) throws IOException
	{

		DataContainer result = null;
		DataContainerCommonUtil.setEquipmentOpc(dc);

		switch (clnmode) {
		
			case 1: {
				result = appServerClient.transmitStateless(ppid, device, dc);
				break;
			}
			case 2: {
				result = appServerClient.transmitStatelessAsync(ppid, device, dc);
				break;
			}
			case 3: {
				result = appServerClient.transmit(ppid, device, dc);
				break;
			}
			case 4: {
				result = appServerClient.transmitAsync(ppid, device, dc);
				break;
			}
			default: {
				throw new IOException("Unknown server client mode " + clnmode);
			}
		}
		
		if (result.containsKey(DataContainerTag.PROCESS_COMPLETE)) {  
			// If task exception is thrown in GALC.
			log.info("***** Error occured on server. ***** ");
		}
		else
			log.info("Application server call complete.");
		
		return result;
	}

}
