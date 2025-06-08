package com.honda.galc.service.broadcast;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.printing.AttributeConvertor;

public abstract class AbstractDeviceBroadcast extends AbstractBroadcast{

	public AbstractDeviceBroadcast(BroadcastDestination destination,
			String processPointId, DataContainer dc) {
		super(destination, processPointId, dc);
	}

	@Override
	public DataContainer calculateAttributes(DataContainer dc) {
		return new AttributeConvertor(logger).convertFromDeviceDataFormat(destination.getDestinationId(), dc);
	}
		
	@Override
	public DataContainer send(DataContainer dc) {		
		Device device = ServiceFactory.getDao(DeviceDao.class).findByKey(destination.getDestinationId());
		if(device == null) {
			DataContainerUtil.error(logger, dc, "Could not find the device id " + destination.getDestinationId());
			return dc;
		}
		dc.setClientID(device.getClientId());
		DataContainer outDC = createOutputDataContainer(dc);
		if(StringUtils.isEmpty(device.getReplyClientId())) {
			send(device,outDC);
			return outDC;
		} else {
			return syncSend(device,outDC);
	    }
	}
	
	@SuppressWarnings("unchecked")
	protected DataContainer createOutputDataContainer(DataContainer dc){
		DataContainer outDC = new DefaultDataContainer();
		outDC.setClientID(dc.getClientID());
		List<String> tags = (List<String>) dc.get(DataContainerTag.TAG_LIST);
		for(String tag : tags){
			outDC.put(tag, dc.get(tag));
		}
		return outDC;
	}
	
	public abstract void send(Device device, DataContainer dc);
	public abstract DataContainer syncSend(Device device,DataContainer dc);
	
}
