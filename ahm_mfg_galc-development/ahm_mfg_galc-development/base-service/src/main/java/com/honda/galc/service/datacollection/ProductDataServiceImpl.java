package com.honda.galc.service.datacollection;

import static com.honda.galc.service.ServiceFactory.getDao;

import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.service.ProductDataService;
import com.honda.galc.util.ProductManifestDataUtil;

public class ProductDataServiceImpl implements ProductDataService {

	@Override
	public Device execute(Device device) {
		Device replyDevice = getDao(DeviceDao.class).findByKey(device.getReplyClientId());
		DataContainer returnDc = execute(device.toDataContainer(), replyDevice);
		device.populateReply(returnDc);
		return device;
	}

	@Override
	public DataContainer execute(DataContainer data) {
		Device device = getDao(DeviceDao.class).findByKey(data.getClientID());
		Device replyDevice = getDao(DeviceDao.class).findByKey(device.getReplyClientId());
		
		return execute(data, replyDevice);
	}

	private DataContainer execute(DataContainer data, Device device) {
		return ProductManifestDataUtil.getManifestDataContainer(data, device);
	}

}
