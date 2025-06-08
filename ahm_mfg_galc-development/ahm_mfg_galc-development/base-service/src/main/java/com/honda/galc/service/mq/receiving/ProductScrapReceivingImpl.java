package com.honda.galc.service.mq.receiving;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerJSONUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.defect.ScrapService;

public class ProductScrapReceivingImpl implements MessageListener {
	
	public ProductScrapReceivingImpl() {
		getLogger().info("ProductScrapReceivingImpl Listener started");
	}

	@Override
	public void onMessage(Message message) {
		String messageId = getMessageId(message);
		getLogger().info(messageId + " Listener received message");
		
		if(message instanceof TextMessage) {
			try {
				
				String msg = ((TextMessage)message).getText();
				getLogger().info("Receiving Text Msg " + msg);
				
				scrapProduct(msg);
				
			}catch(JMSException ex) {
				getLogger().error(ex,"Unable to scrap due to " + ex.getMessage());
			}
		}
	}
	
	private void scrapProduct(String msg) {
		DefaultDataContainer requestDc = new DefaultDataContainer();
		
		requestDc = (DefaultDataContainer) DataContainerJSONUtil.convertFromJSON(requestDc, msg);
		
		ScrapService scrapService = ServiceFactory.getService(ScrapService.class);
		DataContainer returnDC =  scrapService.scrapProduct(requestDc);
		
		getLogger().debug("Scrap request for productId(s) :"+requestDc.get(TagNames.PRODUCT_ID.name())+" completed with result code :"+returnDC.get(TagNames.REQUEST_RESULT.name()));
		
	}
	
	
	private Logger getLogger(){
		return Logger.getLogger(this.getClass().getSimpleName());
	}
	protected String getMessageId(Message message) {
		try {
			if (message.getJMSMessageID().length() > 3) {
				return StringUtils.trimToEmpty(message.getJMSMessageID().substring(3)); //remove "ID:"
			}
			return message.getJMSMessageID();
		} catch(Exception ex) {
			getLogger().error(ex, "Unable to retrieve message Id");
			return "";
		}
	}

}