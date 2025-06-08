package com.honda.galc.service.datacollection;

import static com.honda.galc.service.ServiceFactory.getDao;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceDataType;
import com.honda.galc.service.KeepAliveService;
import com.honda.galc.service.utils.ServiceUtil;

/**
 * 
 * <h3>KeepAliveServiceImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> KeepAliveServiceImpl description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Jul 26, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Jul 26, 2012
 */

public class KeepAliveServiceImpl implements KeepAliveService {
	private String processPointId;
	private Logger logger;
	
	public DataContainer execute(DataContainer data) {
			Device device = getDao(DeviceDao.class).findByKey(data.getClientID());
			device.populate(data);
			return execute(device).toReplyDataContainer(true);
	}

	public Device execute(Device device) {
		long start = System.currentTimeMillis();
		try {
			this.processPointId = device.getIoProcessPointId();
			processKeepAlive(device);
		} catch (Exception te) {
			getLogger().error(te, "Exception");
		} 
		
		getLogger().info("replyDeviceData:", device.toReplyString(), " time:" + (System.currentTimeMillis() -start));
		
		return device;
	}

	public void processKeepAliveWriteTimeStamp(Device device) {
		HeadlessDataCollectionContext context = new HeadlessDataCollectionContext();
		context.setDevice(device);
		DataCollectionUtil util = new DataCollectionUtil(context);
		util.getCurrentTimeStampBcd();
		context.prepareReply(device);
	}

	public boolean isWriteTimeStamp(Device device) {
		boolean isWritTimeStamp = false;
		if(device.getDeviceFormat(TagNames.CURRENT_TIMESTAMP_FORMAT.name()) != null)
			isWritTimeStamp = true;
		
		return isWritTimeStamp;
	}

	private void processKeepAlive(Device device) {
		StringBuilder sb = new StringBuilder("device:").append(device.getClientId()).append(Delimiter.SPACE);
		sb.append("processPointId:").append(processPointId).append(Delimiter.SPACE);
		for(DeviceFormat df :  device.getDeviceDataFormats()){
			sb.append(df.getTag()).append(Delimiter.COLON).append(df.getValue()).append(Delimiter.SPACE);
		}
				
		getLogger().info(sb.toString());
		
		DeviceFormat replyDeviceFormat = device.getReplyDeviceFormat(TagNames.REPLY_KEEP_ALIVE.name());
		if(replyDeviceFormat.getDeviceDataType() == DeviceDataType.BOOLEAN){
			Object keepAliveBoolean = (Boolean)device.getDeviceFormat(TagNames.KEEP_ALIVE.name()).getValue();
			Boolean isChange = isChange(device);
			Boolean returnValue = isChange ? !(Boolean)keepAliveBoolean : (Boolean)keepAliveBoolean;
			replyDeviceFormat.setValue(returnValue);
		}
	}

	private Boolean isChange(Device device) {
		if(device.getDeviceFormat(TagNames.CHANGE_DATA_FOR_REPLY.name()) == null) return false;
		Object value = device.getDeviceFormat(TagNames.CHANGE_DATA_FOR_REPLY.name()).getValue();
		return Boolean.valueOf(value == null ? "false" : value.toString());
	}
	
	

	private Logger getLogger() {
		if(logger == null)
			logger = ServiceUtil.getLogger(processPointId);

		return logger;
	}
	
}
