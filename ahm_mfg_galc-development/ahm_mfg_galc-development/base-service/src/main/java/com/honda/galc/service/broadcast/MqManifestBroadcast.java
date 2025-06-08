package com.honda.galc.service.broadcast;

import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.mq.MqDevice;
import com.honda.galc.util.ProductManifestDataUtil;

/**
 * 
 * <h3>MqManifestBroadcast Class description</h3>
 * <p> MqManifestBroadcast description </p>
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
 * @author Paul Chou<br>
 * Oct.27, 2020
 *
 *
 */
public class MqManifestBroadcast extends MqBroadcast {

	public MqManifestBroadcast(BroadcastDestination destination, String processPointId, DataContainer dc) {
		super(destination, processPointId, dc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public DataContainer send(DataContainer dc) {

		Device device = ServiceFactory.getDao(DeviceDao.class).findByKey(destination.getDestinationId());
		if(device == null) {
			DataContainerUtil.error(logger, dc, "Could not find the device id " + destination.getDestinationId());
			return dc;
		}

		String manifestDataJsonString = ProductManifestDataUtil.getManifestDataJsonString(dc, device);
		
		/* Send the data to MQ */
		MqDevice mqDevice=new MqDevice(logger);
		mqDevice.sendToMqQueue(device,manifestDataJsonString);
		return dc;
	}


}
