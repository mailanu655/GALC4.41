package com.honda.galc.client.headless;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.GenericApplicationContext;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.MQApplicationContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.TemplateDao;
import com.honda.galc.data.XmlStringApplicationContext;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.product.Template;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

import com.ibm.mq.jms.MQXAQueueConnectionFactory;

public class HeadlessPrintJobExecutor implements IHeadlessMain {

	private static final String PROP_TEMPLATE_NAME = "templateName";
	private static final String DEFAULT_TEMPLATE_NAME = "MQ_APPLICATION_CONTEXT";

	private String templateName = null;

	public HeadlessPrintJobExecutor() {
		super();
	}

	public void initialize(ApplicationContext appContext, Application application) {
		try {
			String xmlString = getAppContextTemplate(getTemplateName());
			if (!StringUtils.isEmpty(xmlString)) {
				GenericApplicationContext ctx = XmlStringApplicationContext.getApplicationContext(xmlString);
				if (ctx == null) {
					getLogger().error("Application context is null.  Plase check configuration template.");
				} else {
					MQXAQueueConnectionFactory connFactory = (MQXAQueueConnectionFactory) ctx.getBean("connectionFactory");
					getAppContext().setChannel(connFactory.getChannel());
					getAppContext().setPort(connFactory.getPort());
					getAppContext().setHostName(connFactory.getHostName());
					getAppContext().setQueueManager(connFactory.getQueueManager());
				}
			} 
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Unable to initialize HeadlessPrintJobExecutor listeners");
		}
	}

	private String getTemplateName() {
		if (templateName == null) {
			templateName = PropertyService.getProperty(ApplicationContext.getInstance().getHostName(), PROP_TEMPLATE_NAME);
			if (templateName == null) {
				getLogger().info("Template name not configured. Using default: " + DEFAULT_TEMPLATE_NAME);
				templateName = DEFAULT_TEMPLATE_NAME;
			}
		}
		return templateName;
	}

	private String getAppContextTemplate(String templateName) {
		TemplateDao templateDao = ServiceFactory.getDao(TemplateDao.class);
		Template template = templateDao.findByKey(templateName);
		if (template.getTemplateDataBytes() != null) {
			String templateStr = template.getTemplateDataString();
			getLogger().info("MQApplicationContext: " + templateStr);
			return templateStr;
		} else {
			getLogger().info("MQApplicationContext not found");
			return "";
		}
	}

	private MQApplicationContext getAppContext() {
		return MQApplicationContext.getInstance();
	}
	
	private Logger getLogger() {
		return Logger.getLogger();
	}
}
