package com.honda.galc.service.device;

import static com.honda.galc.service.ServiceFactory.getDao;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.service.IoService;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.wds.WdsBufferedClient;
/**
 * 
 * <h3>PlcDataReceivingService</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PlcDataReceivingService description </p>
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
 * <TD>Nov 17, 2014</TD>
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
 * @since Nov 17, 2014
 */

public class PlcDataReceivingServiceImpl implements IoService{
	protected HeadlessDataCollectionContext context = new HeadlessDataCollectionContext();
	private long start;
	private WdsBufferedClient wdsClient = null;

	public DataContainer execute(DataContainer data) {
		Device device = getDao(DeviceDao.class).findByKey(data.getClientID());
		device.populate(data);
		
		return execute(device).toReplyDataContainer(true);
	}
	
	public Device execute(Device device) {
		try{
			start = System.currentTimeMillis();
			init(device);
			
			updatePlcData();
			
		} catch(Throwable te){
			context.getLogger().error(te, " Exception to collect data for", this.getClass().getSimpleName());
			context.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
		}

		context.prepareReply(device); 
		context.getLogger().info("replyDeviceData:", device.toReplyString());
		context.getLogger().debug("total process time:" + (System.currentTimeMillis() - start) + " ms.");
		return device;
	}

	private void updatePlcData() {
		for( DeviceFormat df : context.getDevice().getDeviceDataFormats()){
			StringBuilder sb = new StringBuilder(context.getGpcsDivision().getGpcsProcessLocation());
			sb.append("\\PLC\\").append(StringUtils.trimToEmpty(df.getId().getClientId()));
			sb.append("\\").append(StringUtils.trimToEmpty(df.getTag()));
			
			context.getLogger().info("update:", sb.toString() + " " + df.getValue());
			
			wdsClient.updateValue(sb.toString(), "" + df.getValue());
		}
		
		
		wdsClient.flush();
		
	}

	private void init(Device device) {
		context.setDevice(device);
		
		wdsClient = new WdsBufferedClient(context.getLogger());
	}

}
