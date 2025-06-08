package com.honda.galc.service.plc;

import java.util.List;

import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.service.DataReceivingService;
import com.honda.galc.service.datacollection.IoServiceBase;
import com.honda.galc.service.wds.WdsBufferedClient;

/**
 * 
 * <h3>DataReceivingTask Class description</h3>
 * <p> DataReceivingTask description </p>
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
 * Dec 17, 2012
 *
 *
 */
public class DataReceivingServiceImpl extends IoServiceBase implements DataReceivingService{

	 
	    
	static final long serialVersionUID = 3206093459760846163L;
	
	public DataReceivingServiceImpl() {
		super();
	}

	
	@Override
	public com.honda.galc.data.DataContainer processData() {
		
		ProcessPoint processPoint = getProcessPoint();
		if(processPoint == null) return null;
		WdsBufferedClient wdsClient = new WdsBufferedClient(getLogger());
		List<DeviceFormat> deviceFormats = getDevice().getDeviceDataFormats();
		for(DeviceFormat item: deviceFormats) {
			String name = processPoint.getDivisionId() + "\\" + "PLC\\" + processPoint.getId()+"\\"+item.getTagName();
            wdsClient.updateValue(name, (String)item.getTagValue());
		}
		wdsClient.flush();
		return dataCollectionComplete(true);
	}
	
}
