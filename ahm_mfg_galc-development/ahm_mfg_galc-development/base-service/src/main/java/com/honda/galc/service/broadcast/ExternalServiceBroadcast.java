package com.honda.galc.service.broadcast;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.service.printing.AttributeConvertor;
import com.honda.galc.util.ReflectionUtils;

/**
 * 
 * <h3>ExternalServiceBroadcast Class description</h3>
 * <p> ExternalServiceBroadcast description </p>
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
 * Aug 20, 2013
 *
 *
 */
public class ExternalServiceBroadcast extends AbstractBroadcast{

	public ExternalServiceBroadcast(BroadcastDestination destination,String processPointId,
			DataContainer dc) {
		super(destination, processPointId, dc);
	}
	
	@Override
	public DataContainer calculateAttributes(DataContainer dc) {
		return new AttributeConvertor(logger).convertFromDeviceDataFormat(destination.getRequestId(), dc);
	}
	
	@Override
	public DataContainer send(DataContainer dc) {
		
		String serviceName  = destination.getDestinationId();
		
		if(!IExternalService.EXT_SERVICIES.containsKey(serviceName)){
			DataContainerUtil.error(logger, dc, "External Service " + serviceName + " is not defined in GALC");
			return dc;
		}
		
		String methodDisplayName = destination.getArgument();
		
		Class<? extends IExternalService> serviceClass  = IExternalService.EXT_SERVICIES.get(serviceName);
		if(serviceClass == null) {
			DataContainerUtil.error(logger, dc, "External service for " + serviceName + " does not exist");
			return dc;
		}
		
		IExternalService service = ReflectionUtils.createInstance(serviceClass);
		
		if(service == null) {
			DataContainerUtil.error(logger, dc, "Failed to create service classs " + serviceClass.getSimpleName());
			return dc;
		}
		
		DataContainer returnDC = service.execute(methodDisplayName, dc);
		return returnDC;
	}

}
