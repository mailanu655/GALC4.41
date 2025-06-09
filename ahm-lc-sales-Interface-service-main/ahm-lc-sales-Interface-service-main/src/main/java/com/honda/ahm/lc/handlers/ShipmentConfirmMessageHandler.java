package com.honda.ahm.lc.handlers;

import com.honda.ahm.lc.enums.StatusEnum;
import com.honda.ahm.lc.messages.StatusMessage;
import com.honda.ahm.lc.messages.StatusVehicle;
import com.honda.ahm.lc.model.ShippingStatus;
import com.honda.ahm.lc.service.FrameShipConfirmationService;
import com.honda.ahm.lc.service.InProcessProductService;
import com.honda.ahm.lc.service.ParkChangeService;
import com.honda.ahm.lc.service.ShippingStatusService;
import com.honda.ahm.lc.service.ShippingTransactionService;
import com.honda.ahm.lc.util.PropertyUtil;

import org.apache.commons.lang3.StringUtils;
import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component("ShipmentConfirmMessageHandler")
public class ShipmentConfirmMessageHandler implements IStatusMessageHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShippingStatusService shippingStatusService;

    @Autowired
    private PropertyUtil propertyUtil;

    @Autowired
    private ShippingTransactionService shippingTransactionService;

    @Autowired
    private ParkChangeService parkChangeService;

    @Autowired
    private InProcessProductService inProcessProductService;

    @Autowired
    private FrameShipConfirmationService frameShipConfirmationService;

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
        LocalDateTime localDateTime = LocalDateTime.parse(statusVehicle.getTimestamp());
        String shipDate = DateTimeFormatter.ofPattern("yyMMdd").format(localDateTime);
        String shipTime = DateTimeFormatter.ofPattern("hhmmss").format(localDateTime);
        ShippingStatus shippingStatus = shippingStatusService.findByProductId(galcUrl, statusVehicle.getVin());

        if (Objects.isNull(shippingStatus)) {
            logger.warn("ShippingStatus not found for VIN {}", statusVehicle.getVin());
            errorMessages.add("ShippingStatus not found for VIN "+ statusVehicle.getVin());
            return errorMessages;
        }

        if (shippingStatus.getStatus() > StatusEnum.AH_SHIP.getStatus()) {
            logger.warn("Ignoring request. ShippingStatus is greater than {}", StatusEnum.AH_SHIP.getType());
            errorMessages.add("Ignoring request. ShippingStatus for VIN "+ statusVehicle.getVin()+" is greater than "+ StatusEnum.AH_SHIP.getType());
            return errorMessages;
        }

        if (shippingStatus.getStatus() == StatusEnum.INIT.getStatus()) {
            logger.warn("Ignoring request. ShippingStatus is equal to {}", StatusEnum.INIT.getType());
            errorMessages.add("Ignoring request. ShippingStatus for VIN "+ statusVehicle.getVin()+" is equal to "+ StatusEnum.INIT.getType());
            return errorMessages;
        }
        logger.info("Updating ShippingStatus to - "+status.getType()+"  for VIN {}", statusVehicle.getVin());
        shippingStatus.setStatus(StatusEnum.AH_SHIP.getStatus());
        shippingStatusService.saveShippingStatus(galcUrl, shippingStatus);

        shippingTransactionService.deleteShippingTransaction(galcUrl, statusVehicle.getVin());
        parkChangeService.deleteParkChange(galcUrl, statusVehicle.getVin());
        inProcessProductService.deleteInProcess(galcUrl, statusVehicle.getVin());
        
        errorMessages.addAll(frameShipConfirmationService.processFrameShipConfirmation(
        		galcUrl,
                statusVehicle.getVin(), //
                propertyUtil.getProcessPoint(StatusEnum.AH_SHIP.getType()),
                shipDate,
                shipTime, StatusEnum.AH_SHIP
        ));
        logger.info("Tracking - "+status.getType()+" Status Message for VIN {}", statusVehicle.getVin());
        shippingStatusService.trackProduct(galcUrl, propertyUtil.getProcessPoint(StatusEnum.AH_SHIP.getType()), statusVehicle.getVin()); //process point id
        
        return errorMessages;
    }
}
