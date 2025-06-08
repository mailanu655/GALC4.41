package com.honda.galc.device.simulator.appclient;

import java.net.MalformedURLException;

import org.apache.log4j.Logger;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.device.simulator.client.EquipmentApplicationServerClient;
import com.honda.galc.device.simulator.client.OPCApplicationServerHTTPClient;
import com.honda.galc.device.simulator.client.OPCApplicationServerHTTPServiceClient;
import com.honda.galc.device.simulator.client.OPCApplicationServerRouterClient;


/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class OpcApplicationServerClientFactory implements IApplicationServerClientFactory 
{
	private Logger log = Logger.getLogger(this.getClass().getName());
	public EquipmentApplicationServerClient getApplicationServerClient(String srvUrl, int clntype) throws SystemException 
	{
		EquipmentApplicationServerClient appServerClient = null;
        // Determine how we connect to the application server
		//			1:  HTTPClient
		//			2:  Router Client
		//			3:  Embedded Cleint
		
		try {
			switch (clntype) {

			case 1: 
				appServerClient = new OPCApplicationServerHTTPClient(srvUrl, null);
				break;

			case 2: 
				appServerClient = new OPCApplicationServerRouterClient(srvUrl, true, false);
				break;

			case 4: {
				// client for new Lot Control 
				appServerClient = new OPCApplicationServerHTTPServiceClient(srvUrl, null);
				break;
			}
			default: 
				// Bad Configuration
				throw new SystemException("Unknown server client type: " + clntype);
			}
			return appServerClient;
		} catch (MalformedURLException e) {
			log.info("IO Exception - Could not connect to address " + srvUrl);
			throw new SystemException("OPCEI0002",e);
		}
	}

}

