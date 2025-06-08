package com.honda.galc.service.broadcast;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.net.JsonClient;
import com.honda.galc.property.DefaultDeviceWisePropertyBean;
import com.honda.galc.service.printing.AttributeConvertor;
import com.honda.galc.service.property.PropertyService;


public class HttpJsonBroadcast extends AbstractDeviceBroadcast{
	String url=null;
	JsonClient jsonClient=null;
	boolean asString=false;
	
	public HttpJsonBroadcast(BroadcastDestination destination, String processPointId,
			DataContainer dc) {
		super(destination, processPointId, dc);
	}

	@Override
	public void send(Device device, DataContainer dc) {
		try{
			createJsonClient(device);
			if(jsonClient!=null){
				jsonClient.send(dc,asString);
				logger.info("sent data : " + dc.toString());
			}else{
				logger.error("Unable to connect to server");
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			logger.error(ex,"Failed to send data " + dc.toString() + " to device " + device );
		}
	}

	@Override
	public DataContainer syncSend(Device device, DataContainer dc) {
		DataContainer returnDC = null;
		try{
			returnDC = new DefaultDataContainer();
			createJsonClient(device);
			if(jsonClient!=null){
				returnDC=jsonClient.syncSend(dc,asString);
				logger.info("sent data : " + dc.toString());
			}else{
				logger.error("Unable to connect to server");
			}
			if(returnDC==null){
				logger.error("Failed to receive data");
				return null;
			}
			returnDC=new AttributeConvertor(logger).convertFromDeviceDataFormat(device.getReplyClientId().toString(), returnDC);
		}catch(Exception ex) {
			ex.printStackTrace();
			logger.error(ex,"Failed to send data " + dc.toString() + " to device " + device );
		}
		return returnDC;
	}
	
	public void createJsonClient(Device device){
		url=device.getEifIpAddress().toString();
		asString=getJsonUseStringValuesSetting(device.getClientId().toString());
		if (url != null && url.startsWith("http")) {
			DefaultDeviceWisePropertyBean defaultDwPropertyBean = getDefaultDeviceWisePropertyBean();
			jsonClient = new JsonClient(url, defaultDwPropertyBean.getDwConnectionTimeout(), defaultDwPropertyBean.getDwReadTimeout());
		}
		if (url == null || url.length() == 0) {
			logger.error("Empty host name, CID [" + url + "], DC is [" + dc + "]");
		}
	}
	
	private boolean getJsonUseStringValuesSetting(String clientID) {
		return PropertyService.getPropertyBoolean(clientID, "STRING_VALUE",false);
	}
	
	private DefaultDeviceWisePropertyBean getDefaultDeviceWisePropertyBean() {
		return PropertyService.getPropertyBean(DefaultDeviceWisePropertyBean.class);
	}
		
}
