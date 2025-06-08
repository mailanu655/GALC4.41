package com.honda.galc.service.mqmessaging;

import javax.jms.Destination;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.data.DataContainer;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.service.MQMessagingService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.tracking.TrackingServiceExecutor;


/**
 * 
 * <h3>MQMessagingServiceImpl Class description</h3>
 * <p>
 * MQMessagingServiceImpl description
 * </p>
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
 * @author Deshane Joseph<br>
 *         Apr 28, 2013
 * 
 * 
 */

public class MQMessagingServiceImpl implements MQMessagingService {
	private Logger logger;

	public void send(final DataContainer dc) {

		JmsTemplate jmsTemplate = (JmsTemplate) ApplicationContextProvider
				.getBean("jmsTemplate");

		
		logger = Logger.getLogger(this.getClass().getName());

		String productId = (String) dc.get("PRODUCT_ID");
		FrameSpecDao frameSpecDao = ServiceFactory.getDao(FrameSpecDao.class);
		FrameSpec frameSpec = frameSpecDao.findTCUData(productId);

		
		dc.put("AUTO_MODEL_ID", frameSpec.getSalesModelCode());
		dc.put("AUTO_MFR_COLOR_CD", frameSpec.getSalesExtColorCode());
		dc.put("AUTO_MODEL_TYPE_CD", frameSpec.getSalesModelTypeCode());
		
		MessageCreator messageCreator = new MessageCreator() {

			public Message createMessage(Session session) throws JMSException {

				return session.createTextMessage(new XMLBuilder(dc)
						.buildXMLString());
			}
		};
		jmsTemplate.send("java:comp/env/mqQueue",messageCreator);
		
		

		logger.info("Sent MQ message for:" + productId);

	}

}
