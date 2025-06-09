package com.honda.ahm.lc.handlers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.honda.ahm.lc.enums.StatusEnum;
import com.honda.ahm.lc.messages.StatusMessage;
import com.honda.ahm.lc.messages.StatusVehicle;
import com.honda.ahm.lc.service.ShippingStatusService;
import com.honda.ahm.lc.util.PropertyUtil;

@Component("SimpleStatusMessageHandler")
public class SimpleStatusMessageHandler  implements IStatusMessageHandler {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
    private ShippingStatusService shippingStatusService;
	
	@Autowired
	private PropertyUtil propertyUtil;


	@Override
	public List<String> handle(StatusMessage statusMessage, StatusEnum status) {
		List<String> errorMessages = new ArrayList<String>();
		
		StatusVehicle statusVehicle = (StatusVehicle) statusMessage.getVehicle();
		 logger.info("Received "+status.getType()+" Status Message for VIN {}", statusVehicle.getVin());
		String galcUrl = shippingStatusService.getGalcUrl(statusVehicle.getVin(),
				statusMessage.getTransaction().getLine_id());
		if(StringUtils.isBlank(galcUrl)) {
			logger.info("Unable to find the VIN record - "+statusVehicle.getVin());
			errorMessages.add("Unable to find the VIN record - "+statusVehicle.getVin());
			return errorMessages;
		}
		logger.info("Tracking - "+status.getType()+" Status Message for VIN {}", statusVehicle.getVin());
		shippingStatusService.trackProduct(galcUrl, propertyUtil.getProcessPoint(status.getType()), statusVehicle.getVin());
		
		return errorMessages;
	}


}
