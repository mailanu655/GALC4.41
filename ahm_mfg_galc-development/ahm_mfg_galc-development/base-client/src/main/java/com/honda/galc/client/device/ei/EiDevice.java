package com.honda.galc.client.device.ei;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.ClientMain;
import com.honda.galc.client.device.AbstractDevice;
import com.honda.galc.common.exception.BaseException;
import com.honda.galc.common.exception.ServiceException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerListener;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.device.DeviceData;
import com.honda.galc.device.DeviceDataConverter;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.DeviceType;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.net.DataContainerSocketSender;
import com.honda.galc.net.JsonClient;
import com.honda.galc.net.SocketRequestDispatcher;
import com.honda.galc.property.DefaultDeviceWisePropertyBean;
import com.honda.galc.property.DevicePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>EiDevice Class description</h3>
 * <p> EiDevice description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Apr 14, 2010
 *
 */

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class EiDevice extends AbstractDevice implements IEiDevice{
	
	public static String NAME = "EI_DEVICE";
	private Map<String, Device> deviceMap = new HashMap<String, Device>();
	public static boolean hasEiDevice() {
		try {
			return getDeviceDataConverter().hasDeviceData();
		}catch(Exception ex) {
			return false;
		}
	}
	
	public EiDevice(String terminalName) {
		setId(NAME);
		setType(DeviceType.EiDevice);
		setClientId(terminalName);
		setEnabled(true);
		setConnected(true);
		getLogger().info("ei device is initialized");
	}
	
	public void setDeviceProperty(DevicePropertyBean devicePropertyBean) {
		// TODO Auto-generated method stub
		
	}
	
	public IDeviceData syncSend(IDeviceData deviceDataFormat) {
		DeviceData deviceData = getDeviceDataConverter().getDeviceDataObject(deviceDataFormat);
		
		if(deviceData == null){
			getLogger().error("error: Can't find device data - syncSend aborted.");
			return deviceDataFormat;
		}
		
		DataContainer dc = deviceData.convert(deviceDataFormat);
		DataContainer returnDC = syncSend(deviceData.getDevice(), dc);
		
		return deviceData.convertReply(returnDC);
		
	}
	
	public DataContainer syncSend(String deviceId, DataContainer dc) {
		Device device = getDevice(deviceId);
		if(device == null){
			device = ServiceFactory.getDao(DeviceDao.class).findByKey(deviceId);
			
			if(device == null){
				getLogger().error("error: Can't find device - syncSend aborted.");
				dc.put(DataContainerTag.DATA, "0");
				return dc;
			}
			
			deviceMap.put(deviceId, device);
		}
		
		DataContainer returnDC = syncSend(device, dc);
		
		return returnDC;
		
	}

	public DataContainer syncSend(Device device, DataContainer dc) {
		DataContainer returnDC = null;
		if(device.isDeviceWiseType()) 
			returnDC = syncSendJson(device, dc);
		else 
			returnDC = syncSend(device.getEifIpAddress().trim(), device.getEifPort(), dc);

		return returnDC;
	}

	private DataContainer syncSendJson(Device device, DataContainer dc) {
		JsonClient jsonClient = createJsonClient(device);
		DataContainer returnDc = null;
		try {
			returnDc = jsonClient.syncSend(dc, getDwPropertyBean(device.getClientId()).isStringValue());
		} catch (Exception e) {
			getLogger().error("Error:", " exception to send by json client:", e.getMessage());
		}
		return returnDc;
	}

	public JsonClient createJsonClient(Device device){
		String url=device.getEifIpAddress().toString();
		JsonClient jsonClient=null;
		
		if (url != null && url.startsWith("http")) {
			DefaultDeviceWisePropertyBean dwPropertyBean = getDwPropertyBean(device.getClientId());
			jsonClient = new JsonClient(url, dwPropertyBean.getDwConnectionTimeout(), dwPropertyBean.getDwReadTimeout());
		}
		if (url == null || url.length() == 0) {
			getLogger().error("Empty host name, CID [" + url + "]");
		}
		return jsonClient;
	}

	private DefaultDeviceWisePropertyBean getDwPropertyBean(String clientId) {
		return PropertyService.getPropertyBean(DefaultDeviceWisePropertyBean.class, clientId);
	}

	private Device getDevice(String deviceId) {
		return deviceMap.get(deviceId);
	}

	private DataContainer syncSend(String ip, int port, DataContainer dc) {
		DataContainer returnDC = null;
		try{
			DataContainerSocketSender socketSender
				= new DataContainerSocketSender(ip, port);
			returnDC = socketSender.syncSend(dc);
			handleException(returnDC);
			setConnected(true);
		}catch(ServiceException e) {
			setConnected(false);
			throw e;
		}
		return returnDC;
	}

	
	private void handleException(DataContainer dc) {
		
		String message = (String)dc.get("TRANSMIT_EXCEPTION");
		if(message != null) throw new SystemException(message);
		
	}
	
	public void send(IDeviceData deviceDataFormat) {
		DeviceData deviceData = getDeviceDataConverter().getDeviceDataObject(deviceDataFormat);

		DataContainer dc = deviceData.convert(deviceDataFormat);
		
		send(deviceData.getDevice(), dc);
		
	}

	private void send(Device device, DataContainer dc) {
		if(device.isDeviceWiseType()) {
			 sendJson(device, dc);
		} else {
			 send(device.getEifIpAddress(),device.getEifPort(), dc);
		}
		
	}
	
	private void sendJson(Device device, DataContainer dc) {
		JsonClient jsonClient = createJsonClient(device);
		try {
			 jsonClient.send(dc, getDwPropertyBean(device.getClientId()).isStringValue());
		} catch (Exception e) {
			getLogger().error("Error:", " exception to send by json client:", e.getMessage());
		}
	}

	private void send(String ipAddress, int port, DataContainer dc) {
		try{
			DataContainerSocketSender socketSender = new DataContainerSocketSender(ipAddress,port);
			socketSender.send(dc);
			setConnected(true);
		}catch(BaseException e) {
			setConnected(false);
			throw e;
		}
		
	}

	public boolean containOutputDeviceData(Class<? extends IDeviceData>deviceDataClass) {
		return getDeviceDataConverter().containOutputDeviceData(deviceDataClass);
	}	
	
	public boolean containInputDeviceData(Class<? extends IDeviceData>deviceDataClass) {
		return getDeviceDataConverter().containInputDeviceData(deviceDataClass);
	}
	
	private static DeviceDataConverter getDeviceDataConverter() {
	
		return DeviceDataConverter.getInstance();
		
	}
	
	private static SocketRequestDispatcher getRequestDispatcher() {
		return ClientMain.getInstance().getApplicationContext().getRequestDispatcher();
	}
	
	public void registerDataContainerListener(DataContainerListener listener){
		getRequestDispatcher().registerListner(listener);
	}
	
	public void registerDeviceListener(DeviceListener listener,List<IDeviceData> deviceDataList){
		getRequestDispatcher().registerListner(listener,deviceDataList);
	}
	
	public void registerDeviceListener(DeviceListener listener,IDeviceData... deviceDataList){
		getRequestDispatcher().registerListner(listener,deviceDataList);
	}
	
	public void reqisterDeviceData(List<IDeviceData> deviceDataList) {
		getDeviceDataConverter().registerDeviceData(deviceDataList);
	}
	
	public void reqisterDeviceData(IDeviceData... deviceDataList) {
		getDeviceDataConverter().registerDeviceData(deviceDataList);
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}

	public boolean isConfigured(Class<? extends IDeviceData>clazz) {
		return getDeviceDataConverter().isConfigured(clazz);
	}
	
}
