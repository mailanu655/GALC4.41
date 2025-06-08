package com.honda.galc.service.broadcast;

import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.service.ServiceFactory;

import com.honda.galc.service.mq.MqDevice;
import com.honda.galc.service.printing.AttributeConvertor;
import com.honda.galc.util.ReflectionUtils;

public class MqBroadcast extends AbstractBroadcast {
	public MqBroadcast(BroadcastDestination destination, String processPointId,
			DataContainer dc) {
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
		
		/* Get assembler name */
		String className = destination.getArgument();
		DataContainer returnDC=new DefaultDataContainer();
		Class<? extends IMqAssembler> serviceClass = IMqAssembler.MQ_ASSEMBLERS.get(className);
		if(serviceClass == null) {
			DataContainerUtil.error(logger, dc, "MQ Assembler for " + serviceClass + " does not exist");
			return dc;
		}
		IMqAssembler service = ReflectionUtils.createInstance(serviceClass);
		if(service == null) {
			DataContainerUtil.error(logger, dc, "Failed to create service classs " + serviceClass.getSimpleName());
			return dc;
		}
		
		returnDC = service.execute(dc);
		if(returnDC==null){
			DataContainerUtil.error(logger, dc, "Failed to create DataContainer");
			return dc;
		}
		/* Send the data to MQ */
		MqDevice mqDevice=new MqDevice(logger);
		mqDevice.sendToMqQueue(device,returnDC);
		return returnDC;
	}
}
