package com.honda.galc.client.dc.processor;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.property.TCUBroadcastPropertyBean;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.dto.BroadcastContext;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class TCUBroadcastMQProcessor extends BroadcastMQProcessor {

	public TCUBroadcastMQProcessor(DataCollectionController controller,
			MCOperationRevision operation) {
		super(controller, operation);
	}

	public BroadcastContext populateBroadcastContext(BroadcastContext context, InputData data) {
		getLogger().info("TCUBroadcastMQProcessor");
		
		TCUBroadcastPropertyBean tCUBroadcastPropertyBean = PropertyService.getPropertyBean(TCUBroadcastPropertyBean.class);
		
		String destinationsString = tCUBroadcastPropertyBean.getDestination();
		if (destinationsString.length() == 0) {
			getLogger().warn("Property DESTINATION of Component TCU is not set up.");
			return null;
		}
		String plantCode = tCUBroadcastPropertyBean.getAutoMfrPlantId();
		if (plantCode.length() == 0) {
			getLogger().warn("Property AUTO_MFR_PLANT_ID of Component TCU is not set up.");
			return null;
		}
		
		String templateName = tCUBroadcastPropertyBean.getTemplateName();
		if (templateName.length() == 0) {
			getLogger().warn("Property TEMPLATE_NAME of Component TCU is not set up.");
			return null;
		}
		
		//default value jms/LotControlMQQueue, which is configured in ibm-web-bnd.xmi of BaseWeb project
		String mqJndiName = tCUBroadcastPropertyBean.getMqJndiName();
		if (mqJndiName.length() == 0) {
			getLogger().warn("Property MQ_JNDI_NAME of Component TCU is not set up.");
			return null;
		}
		
		String productId = getController().getProductModel().getProductId();
		FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
		
		String[] destinations = StringUtils.split(destinationsString, ",");
		String destination = frameDao.getSalesModelTypeCode(productId);
		if (destination == null || destinations == null || destinations.length == 0) {
			getLogger().warn("Sales Model Type Code of " + productId + " is missing.");
			return null;
		}
		destination = destination.trim();
		boolean sendTCU = false;
		for (String str : destinations) {
			if (destination.equals(str.trim())) {
				sendTCU = true;
				break;
			}
		}
		
		if (!sendTCU) {
			getLogger().info("No TCU broadcast for sales model type code: " + destination);
			return null;
		}
		
		FrameSpec productSpecCode = (FrameSpec)getController().getProductModel().getProductSpec();
		
		productId = ProductNumberDef.justifyJapaneseVIN(productId, false); //pad spaces to the left
		
		context.getExtraAttribs().put("PRODUCT_ID", productId);
		context.getExtraAttribs().put("MODEL_ID", productSpecCode.getSalesModelCode());
		context.getExtraAttribs().put("COLOR_CODE", productSpecCode.getSalesExtColorCode());
		context.getExtraAttribs().put("MODEL_TYPE", productSpecCode.getSalesModelTypeCode());
		context.getExtraAttribs().put("ACCESSORY", "");
		context.getExtraAttribs().put("PLANT_ID", plantCode);
		context.getExtraAttribs().put("TCU", ((PartSerialScanData)data).getSerialNumber());
		context.setDeviceMsg(templateName.trim());
		context.setDestination(mqJndiName);
		
		return context;
	}
}
