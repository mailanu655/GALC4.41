package com.honda.ahm.lc.handlers;

import com.honda.ahm.lc.enums.StatusEnum;
import com.honda.ahm.lc.messages.StatusMessage;
import com.honda.ahm.lc.messages.StatusVehicle;
import com.honda.ahm.lc.model.ShippingStatus;
import com.honda.ahm.lc.service.ShippingStatusService;
import com.honda.ahm.lc.util.PropertyUtil;

import org.apache.commons.lang3.StringUtils;
import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component("AhReceiveMessageHandler")
public class AhReceiveMessageHandler implements IStatusMessageHandler {

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
        ShippingStatus shippingStatus = shippingStatusService.findByProductId(galcUrl, statusVehicle.getVin());
        if (Objects.isNull(shippingStatus)) {
            logger.error("Shipping Status not found for VIN {}", statusVehicle.getVin());
            errorMessages.add("Shipping Status not found for VIN "+ statusVehicle.getVin());
            return errorMessages;
        }

        if (shippingStatus.getStatus() == StatusEnum.VQ_SHIP.getStatus()) {
            // Update VIN Status
            shippingStatus.setStatus(StatusEnum.AH_RCVD.getStatus());
            shippingStatusService.saveShippingStatus(galcUrl, shippingStatus);

            // Track VIN update
            logger.info("Tracking - "+status.getType()+" Status Message for VIN {}", statusVehicle.getVin());
            shippingStatusService.trackProduct(galcUrl, propertyUtil.getProcessPoint(StatusEnum.AH_RCVD.getType()), statusVehicle.getVin());

           
            logger.info("Updated ShippingStatus for VIN "+statusVehicle.getVin()+" to status "+StatusEnum.AH_RCVD);
        } else {
            logger.warn("Status for VIN "+statusVehicle.getVin()+" is not "+StatusEnum.VQ_SHIP+ ", ignoring status update");
        }
        
        return errorMessages;
    }

	

}
