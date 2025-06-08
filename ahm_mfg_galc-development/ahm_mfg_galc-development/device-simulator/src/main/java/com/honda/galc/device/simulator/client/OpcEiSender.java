package com.honda.galc.device.simulator.client;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.OpcConfigEntryDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.device.simulator.appclient.ApplicationServerClientManager;
import com.honda.galc.device.simulator.data.DataContainerCommonUtil;
import com.honda.galc.device.simulator.data.SimulatorConstants;
import com.honda.galc.device.simulator.utils.DevSimulatorUtil;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.OpcConfigEntry;
import com.honda.galc.service.ServiceFactory;

/**
 * <h3>OpcEiSender</h3>
 * <h4> This class is used to simulate OPC EI interface sending data container to server </h4>
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
 * <TD>Oct. 24, 2008</TD>
 * <TD> 0.0.1 </TD>
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
public class OpcEiSender implements IEiSender {
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());
	ApplicationServerClientManager clnMgr = new ApplicationServerClientManager();
	EquipmentApplicationServerClient serverClient;
	private String serverUrl;
	private int clientType;
	private int clientMode;
	String[] exclHosts;
	String[] exclIps;
	String deviceId;
	private String processPointId;
	
	/**
	 * constructor
	 * @param serverUrl
	 * @param clientType
	 * @param clientMode
	 * @param deviceId
	 * @throws Exception 
	 */
	public OpcEiSender(String serverUrl, int clientType, int clientMode, String deviceId) throws Exception {
		super();
		this.serverUrl = serverUrl;
		this.clientType = clientType;
		this.clientMode = clientMode;
		this.deviceId = deviceId;
		initialize();
	}

	
	/**
	 * constructor
	 * @param opcIns
	 * @param devId
	 * @throws Exception 
	 */
	public OpcEiSender(String opcIns, Device device)  {
		super();
		
		List<OpcConfigEntry> opcConfigEntries = getDao(OpcConfigEntryDao.class).findAllByDeviceId(device.getClientId());
		
		if(!opcConfigEntries.isEmpty()){
			OpcConfigEntry opcConfigEntry = opcConfigEntries.get(0);
			this.serverUrl = opcConfigEntry.getServerUrl();
			this.clientType = opcConfigEntry.getServerClientTypeId();
			this.clientMode = opcConfigEntry.getServerClientModeId();
			this.deviceId = device.getClientId();
			this.processPointId = device.getIoProcessPointId();
		}
		else
			log.error("Error: Can not find OPC Instance name or device Id from OPC configuration!");
	
		initialize();
	}
	
	/**
	 * init
	 * @throws Exception 
	 *
	 */
	private void initialize() {

        Device device = ServiceFactory.getDao(DeviceDao.class).findByKey(deviceId);
        processPointId = device.getIoProcessPointId();

		DevSimulatorUtil util = new DevSimulatorUtil();
		Properties props = util.loadProperties(SimulatorConstants.PROP_SIMULATOR);
		String exclHostsStr = (String)props.get("ExcludesDispatcherHosts");
		String exclIpMask = (String)props.get("ExcludesIpMask");

		exclHosts = (exclHostsStr == null) ? null : exclHostsStr.split(",");
		exclIps = (exclIpMask == null) ? null : exclIpMask.split(",");
		serverClient = clnMgr.getServerClient(SimulatorConstants.DEVICE_TYPE_OPC, serverUrl, clientType, exclHosts, exclIps);

		if (serverClient == null)
			log.error("Error: Failed to get application server client.");

		//always set to use XML for OPC
		serverClient.setUseXML(true);


	}
	
	/**
	 * Send data container to GALC server
	 * @param senderDc
	 * @return
	 */
	public DataContainer send(DataContainer dc)
	{
		DataContainer retDc = null;
		try {
			
			DataContainerCommonUtil.setEquipmentOpc(dc);
			log.info("Start call application server. " + serverUrl + "client type:" + clientType + " client mode:" + clientMode);
			log.info("Sender DataContainer: " + dc);
			
			retDc = clnMgr.callApplicationServer(serverClient, clientMode, processPointId, deviceId, dc);
			
			log.info("Reply DataContainer: " + retDc);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error: Failed to get OPC Application Server Client. " + e.getMessage());
		}		
		return retDc;
		
	}

	

}
