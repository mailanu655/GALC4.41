package com.honda.galc.test.device;

import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.DeviceFormatDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.device.simulator.client.IEiSender;
import com.honda.galc.device.simulator.client.OpcEiSender;
import com.honda.galc.device.simulator.client.SimulatorSenderManager;
import com.honda.galc.device.simulator.server.SimulatorServerManager;
import com.honda.galc.device.simulator.server.SimulatorSocketServer;
import com.honda.galc.device.simulator.utils.DevSimulatorUtil;

import static com.honda.galc.service.ServiceFactory.getDao;

/**
 * <h3>DeviceSimulator</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>
 * 
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
 * <TD>Paul Chou</TD>
 * <TD>Feb 10, 2009</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial version</TD>
 * </TR>
 * </TABLE>
 */
public class DeviceSimulator {
	private static DeviceSimulator instance = null;
	private static boolean useXML = true;
	
	private SimulatorSenderManager _senders;
	private SimulatorServerManager _listeners;
	DeviceDao deviceDao;
	
	private DeviceSimulator() {
		super();
		this.deviceDao = getDao(DeviceDao.class);
		this._senders = new SimulatorSenderManager();
		this._listeners = new SimulatorServerManager();
	}

	/**
	 * find EI Sender, if not exist then create a new one
	 * @param opcins
	 * @param devid
	 * @return
	 */
	public static IEiSender getOpcEiSender(String opcins, String devid)
	{
		IEiSender sender = null;
		DeviceSimulator devsim = getInstance();
		
		sender = devsim.getExistSender(opcins, devid);
		
		if(sender == null)
		{
            //create sender
			try {
				sender = new OpcEiSender(opcins, getDao(DeviceDao.class).findByKey(devid));
			} catch (Exception e) {
				e.printStackTrace();
			}  
			
			devsim.saveSender(opcins, devid, sender);
			
		}
		
		return sender;
	}
	
	/**
	 * find a exist server or create a new one and start it.
	 * @param opcins
	 * @param devid
	 */
	public static void startOpcEiListener(int port)
	{
		SimulatorSocketServer listenerserver = null;
		DeviceSimulator devsim = getInstance();
		listenerserver = devsim.getExistListener(port);
		
		if (listenerserver == null) {
			listenerserver = new SimulatorSocketServer(port, useXML );
	        devsim.saveListener(port, listenerserver);
	        
	        listenerserver.setDcProcessor(new SocketServerHandler());
	        listenerserver.startServer();	
		}
		
		//OK. assume that the server already running
	}
	
	public static DataContainerFixture waitForData(int port, long timeout)
	{
		DeviceSimulator devsim = getInstance();
		SimulatorSocketServer existListener = devsim.getExistListener(port);
		DataContainer dc = ((SocketServerHandler)existListener.getDcProcessor()).waitForData(timeout);
		
		//alway convert keys in data container from device data format tag to tag value
		dc = DevSimulatorUtil.convertDataContainerKey(getDao(DeviceFormatDao.class).findAllByDeviceId(dc.getClientID()),dc, true);
		return new DataContainerFixture(dc);
	}
	
	//Getters & Senders
	private void saveListener(int ei_port, SimulatorSocketServer listenerserver) {
		_listeners.add(ei_port, listenerserver);
		
	}

	private SimulatorSocketServer getExistListener(int port) {
		return _listeners.findServerOnPort(port);
		
	}
	
	private void saveSender(String opcins, String devid, IEiSender sender) {
		_senders.add(opcins + "_" + devid, sender);
		
	}
	private IEiSender getExistSender(String opcins, String devid) {
		return _senders.get(opcins + "_" + devid);
		
	}
	
	private static DeviceSimulator getInstance()
	{
		if(instance == null)
			instance = new DeviceSimulator();
		
		return instance;
	}
	
	private void clear()
	{
		_senders.cleanUp();
		_listeners.cleanUp();
	}
	
	public static void cleanUp()
	{
		getInstance().clear();
	}


	
	public static DataContainerFixture send(String opcInstance, String devId, DataContainer data)
	{
		 DataContainer rdc = getOpcEiSender(opcInstance, devId).send(data);
		
		 //alway convert keys in data container from device data format tag to tag value
		 rdc = DevSimulatorUtil.convertDataContainerKey(getDao(DeviceFormatDao.class).findAllByDeviceId(rdc.getClientID()),rdc, true);
		 
		 return new DataContainerFixture(rdc);
	}

	public static void cleanUpDeviceQueue(Integer port) {
	    DeviceSimulator devsim = getInstance();
	    ((SocketServerHandler)devsim.getExistListener(port).getDcProcessor()).cleanUpQueue();
	    
	}

}
