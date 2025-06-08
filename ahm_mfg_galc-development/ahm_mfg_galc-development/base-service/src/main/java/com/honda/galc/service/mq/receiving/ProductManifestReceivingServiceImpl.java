package com.honda.galc.service.mq.receiving;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import com.honda.galc.util.ProductManifestDataUtil;
/**
 * 
 * 
 * <h3>ProductManifestReceivingServiceImpl Class description</h3>
 * <p> ProductManifestReceivingServiceImpl description </p>
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
 *
 *
 */
public class ProductManifestReceivingServiceImpl extends AbstractMessageReceivingServiceImpl {
	
	public ProductManifestReceivingServiceImpl() {
		super("PRODUCT_MANIFEST_QUEUE");
	}

	@Override
	public void onMessage(Message message) {
		if(message instanceof TextMessage) {
			try {
				
				String msg = ((TextMessage)message).getText();
				getLogger().info("Receiving Text Msg " + msg);
				ProductManifestDataUtil.saveManifestDataFromJsonString(msg, getLogger());
				
			}catch(JMSException ex) {
				getLogger().error(ex,"Unable to save product data due to " + ex.getMessage());
			}
		}
	}
}
