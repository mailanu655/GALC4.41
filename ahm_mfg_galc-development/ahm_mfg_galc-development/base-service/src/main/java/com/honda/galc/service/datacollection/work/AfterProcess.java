package com.honda.galc.service.datacollection.work;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceType;
import com.honda.galc.notification.service.IProductPassedNotification;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.datacollection.ProductDataCollectorBase;
import com.honda.galc.service.printing.PrintAttributeConvertor;
/**
 * 
 * <h3>AfterProcess</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> AfterProcess description </p>
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
 * <TD>Mar 12, 2014</TD>
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
 * @since Mar 12, 2014
 */
public class AfterProcess extends CollectorWork{

	public AfterProcess(HeadlessDataCollectionContext context, ProductDataCollectorBase collector) {
		super(context, collector);
	}

	@Override
	void doWork() throws Exception {
		
		doAttributeConvert();
		doBroadcast();
		
		if(getProperty().isNotifyLineSideMonitor()) notifyLSM();
		
	}

	private void doAttributeConvert() throws Exception {

		List<DeviceFormat> deviceLst = context.getDevice().getReplyDeviceDataFormats();
		if ( deviceLst == null ) return;
		
		PrintAttributeConvertor printAttributeConvertor = new PrintAttributeConvertor(getLogger());
		printAttributeConvertor.convertAttributeDeviceFormat(deviceLst,context.getProduct(),context.getProductSpec());
	}
	
	

	private void doBroadcast() throws Exception {
		Broadcast broadCast = new Broadcast(context);
		broadCast.execute();
	}
	
	private void notifyLSM() {
		if(!StringUtils.isEmpty(context.getProductId()) && 
				(getProperty().isValidateProductId()? (Boolean)context.get(TagNames.VALID_PRODUCT_ID.name()) ==true : true)){
			
			ServiceFactory.getNotificationService(IProductPassedNotification.class, context.getProcessPointId()).execute(
					context.getProcessPointId(), context.getProductId());
			
			getLogger().info("Notified product passed event:" + context.getProcessPointId() + ":" + context.getProductId());
		} 
		else 
			getLogger().info("Invalid product for product passed event:" + context.getProductId());
		
		
	}

}
