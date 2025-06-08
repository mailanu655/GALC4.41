package com.honda.galc.service.broadcast;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.TemplateDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.dto.BroadcastContext;
import com.honda.galc.entity.product.Template;
import com.honda.galc.service.BroadcastMQService;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>BroadcastServiceImpl Class description</h3>
 * <p> BroadcastServiceImpl description </p>
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
 * @author LK <br>
 * May 6, 2015
 *
 *
 */
public class BroadcastMQServiceImpl implements BroadcastMQService {

	private static Logger logger = Logger.getLogger("BroadcastMQServiceImpl");
	
	public void broadcast(BroadcastContext context) {
		
		String templateName = context.getDeviceMsg();
		Template template = ServiceFactory.getDao(TemplateDao.class).findByKey(templateName);
		Map<String, String> pMap = context.getAllAttributes();
		final String strMessage = template.getPopulatedTemplate(pMap);
		MessageCreator messageCreator = new MessageCreator() {
			public Message createMessage(Session session) {
				TextMessage msg = null;
				try {
					msg = session.createTextMessage(strMessage);
				} catch (JMSException e) {
					logger.error(e);
				}
				return msg;
			}
		};
		JmsTemplate jmsTemplate = (JmsTemplate) ApplicationContextProvider.getBean("jmsTemplate");
		try {
			jmsTemplate.send(context.getDestination(), messageCreator);
		} catch (JmsException e) {
			logger.error(e);
		}
	}

}
