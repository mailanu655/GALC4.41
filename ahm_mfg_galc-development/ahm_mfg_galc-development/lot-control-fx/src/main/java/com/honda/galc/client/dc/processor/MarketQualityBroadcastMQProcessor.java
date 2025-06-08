package com.honda.galc.client.dc.processor;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.property.MarketQualityBroadcastPropertyBean;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.dto.BroadcastContext;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.service.property.PropertyService;

public class MarketQualityBroadcastMQProcessor extends InstalledPartBroadcastMQProcessor {

	public MarketQualityBroadcastMQProcessor(DataCollectionController controller,
			MCOperationRevision operation) {
		super(controller, operation);
	}

	@Override
	public BroadcastContext populateBroadcastContext(BroadcastContext context, InputData data) {
		getLogger().info("Populating Market Quality Broadcast Context");
		
		MarketQualityBroadcastPropertyBean mrktQltyBroadcastPropertyBean = PropertyService.getPropertyBean(MarketQualityBroadcastPropertyBean.class);
		String templateName = mrktQltyBroadcastPropertyBean.getTemplateName();
		if (StringUtils.isBlank(templateName)) {
			getLogger().warn("Property TEMPLATE_NAME of Component MARKET_QUALITY is not set up.");
			return null;
		}
		
		//default value jms/LotControlMQQueue, which is configured in ibm-web-bnd.xmi of BaseWeb project
		String mqJndiName = mrktQltyBroadcastPropertyBean.getMqJndiName();
		if (mqJndiName.length() == 0) {
			getLogger().warn("Property MQ_JNDI_NAME of Component MARKET_QUALITY is not set up.");
			return null;
		}
		context.setId(StringUtils.trimToEmpty(mrktQltyBroadcastPropertyBean.getTextId()));
		context.setDeviceMsg(templateName.trim());
		context.setDestination(mqJndiName);
		context.getExtraAttribs().put("TIMESTAMP", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
		
		return context;
	}
}
