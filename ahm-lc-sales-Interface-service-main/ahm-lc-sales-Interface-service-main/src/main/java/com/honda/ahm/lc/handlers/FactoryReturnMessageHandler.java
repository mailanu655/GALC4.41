package com.honda.ahm.lc.handlers;

import com.honda.ahm.lc.enums.InstalledPartStatus;
import com.honda.ahm.lc.enums.StatusEnum;
import com.honda.ahm.lc.messages.StatusMessage;
import com.honda.ahm.lc.messages.StatusVehicle;
import com.honda.ahm.lc.model.InRepairArea;
import com.honda.ahm.lc.model.ShippingStatus;
import com.honda.ahm.lc.model.ShippingTransaction;
import com.honda.ahm.lc.service.InRepairAreaService;
import com.honda.ahm.lc.service.InstalledPartService;
import com.honda.ahm.lc.service.NaqDefectAndParkingService;
import com.honda.ahm.lc.service.ParkChangeService;
import com.honda.ahm.lc.service.ShippingStatusService;
import com.honda.ahm.lc.service.ShippingTransactionService;
import com.honda.ahm.lc.util.PropertyUtil;
import org.apache.commons.lang3.StringUtils;
import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component("FactoryReturnMessageHandler")
public class FactoryReturnMessageHandler implements IStatusMessageHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShippingStatusService shippingStatusService;

    @Autowired
    private InstalledPartService installedPartService;

    @Autowired
    private NaqDefectAndParkingService naqDefectAndParkingService;

    @Autowired
    private PropertyUtil propertyUtil;

    @Autowired
    private ShippingTransactionService shippingTransactionService;

    @Autowired
    private InRepairAreaService inRepairAreaService;
    @Autowired
    private ParkChangeService parkChangeService;
    private static final Character SENDED_FLAG = 'R';
    private static final String RESPONSIBLE_DEPARTMENT	= "VQ";

    @Override
    public List<String> handle(StatusMessage statusMessage, StatusEnum status) {
    	List<String> errorMessages = new ArrayList<String>();
    	
        StatusVehicle statusVehicle = (StatusVehicle) statusMessage.getVehicle();
        logger.info("Received " + status.getType() + " Status Message for VIN {}", statusVehicle.getVin());
        String galcUrl = shippingStatusService.getGalcUrl(statusVehicle.getVin(),
                statusMessage.getTransaction().getLine_id());
        if (StringUtils.isBlank(galcUrl)) {
            logger.info("Unable to find the VIN record - " + statusVehicle.getVin());
            errorMessages.add("Unable to find the VIN record - "+statusVehicle.getVin());
            return errorMessages;
        }
        ShippingStatus shippingStatus = shippingStatusService.findByProductId(galcUrl, statusVehicle.getVin());
        if (Objects.isNull(shippingStatus)) {
            logger.warn("ShippingStatus not found for VIN {}", statusVehicle.getVin());
            errorMessages.add("ShippingStatus not found for VIN "+ statusVehicle.getVin());
            return errorMessages;
        }

        if (shippingStatus.getStatus() == StatusEnum.AH_RTN.getStatus()) {
            logger.warn("Ignoring request. ShippingStatus is equal to {}", StatusEnum.AH_RTN.getType());
            errorMessages.add("Ignoring request. ShippingStatus for VIN "+ statusVehicle.getVin()+" is equal to "+ StatusEnum.AH_RTN.getType());
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
        installedPartService.updateInstalledPartStatus(galcUrl, statusVehicle.getVin(), propertyUtil.getBackoutPartList(), InstalledPartStatus.BLANK);
       
        shippingStatusService.findByProductId(galcUrl, statusVehicle.getVin());
        shippingStatus.setStatus(StatusEnum.AH_RTN.getStatus());
        shippingStatus.setActualTimestamp(shippingStatus.getActualTimestamp());
        shippingStatusService.saveShippingStatus(galcUrl, shippingStatus);
        
        shippingStatusService.trackProduct(galcUrl, propertyUtil.getProcessPoint(StatusEnum.AH_RTN.getType()), statusVehicle.getVin());
       
        ShippingTransaction shippingTransaction = shippingTransactionService.findByProductId(galcUrl, statusVehicle.getVin());
        if (shippingTransaction != null) {
            shippingTransaction.setSendFlag(SENDED_FLAG);
            shippingTransactionService.saveShippingTransaction(galcUrl, shippingTransaction);
        }
        parkChangeService.deleteParkChange(galcUrl, statusVehicle.getVin());
        
        String defectName = propertyUtil.getPropertyByDefectName();
        String repairArea = propertyUtil.getPropertyByRepairName();

        InRepairArea inRepairArea = new InRepairArea();
        inRepairArea.setProductId(statusVehicle.getVin());
        inRepairArea.setRepairAreaName(repairArea);
        inRepairArea.setActualTimestamp(shippingStatus.getActualTimestamp());
        inRepairArea.setResponsibleDept(RESPONSIBLE_DEPARTMENT);
        inRepairAreaService.saveinRepairArea(galcUrl,inRepairArea);

        boolean updateNaq = propertyUtil.updateNaqEnable();
        if (updateNaq) {
            naqDefectAndParkingService.createNaqDefectAndParking(
                    galcUrl,
                    statusVehicle.getVin(),
                    defectName,
                    propertyUtil.getProcessPoint(StatusEnum.AH_RTN.getType()),
                    repairArea
            );
        }
        
        return errorMessages;
    }

}
