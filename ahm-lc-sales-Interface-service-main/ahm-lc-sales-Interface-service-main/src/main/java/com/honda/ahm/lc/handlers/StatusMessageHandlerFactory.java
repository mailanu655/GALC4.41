package com.honda.ahm.lc.handlers;

import com.honda.ahm.lc.enums.StatusEnum;
import com.honda.ahm.lc.messages.StatusMessage;
import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class StatusMessageHandlerFactory {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Qualifier("AhParkingChangeMessageHandler")
    @Autowired
    private IStatusMessageHandler ahParkingChangeMessageHandler;

    @Qualifier("AhReceiveMessageHandler")
    @Autowired
    private IStatusMessageHandler ahReceiveMessageHandler;

    @Qualifier("DealerAssignMessageHandler")
    @Autowired
    private IStatusMessageHandler dealerAssignMessageHandler;

    @Qualifier("FactoryReturnMessageHandler")
    @Autowired
    private IStatusMessageHandler factoryReturnMessageHandler;

    @Qualifier("ShipmentConfirmMessageHandler")
    @Autowired
    private IStatusMessageHandler shipmentConfirmMessageHandler;
    
    @Qualifier("SimpleStatusMessageHandler")
    @Autowired
    private IStatusMessageHandler simpleStatusMessageHandler;

    public List<String> handleMessage(StatusMessage statusMessage) {
    	List<String> errorMessages = new ArrayList<String>();
    
        StatusEnum statusEnum = StatusEnum.getStatusByType(statusMessage.getTransaction().getTransaction_code());
        if (Objects.isNull(statusEnum)) {
            logger.error("Invalid status received from YMS Interface");
            return errorMessages;
        }
        try {
            switch (statusEnum) {
                case AH_RCVD:
                	errorMessages = ahReceiveMessageHandler.handle(statusMessage,statusEnum);
                    break;
                case AH_PCHG:
                	errorMessages = ahParkingChangeMessageHandler.handle(statusMessage,statusEnum);
                    break;
                case DLR_ASGN:
                	errorMessages = dealerAssignMessageHandler.handle(statusMessage,statusEnum);
                    break;
                case AH_RTN:
                	errorMessages = factoryReturnMessageHandler.handle(statusMessage,statusEnum);
                    break;
                case AH_SHIP:
                	errorMessages = shipmentConfirmMessageHandler.handle(statusMessage,statusEnum);
                    break;
                case PPO_ON:
                	errorMessages = simpleStatusMessageHandler.handle(statusMessage,statusEnum);
                	 break;
                case PPO_OFF:
                	errorMessages = simpleStatusMessageHandler.handle(statusMessage,statusEnum);
                	 break;
                case ON_TRN:
                	errorMessages = simpleStatusMessageHandler.handle(statusMessage,statusEnum);
                	 break;
                case SHIPPER:
                	errorMessages = simpleStatusMessageHandler.handle(statusMessage,statusEnum);
                	break;
                case DLR_RCPT:
                	errorMessages = simpleStatusMessageHandler.handle(statusMessage,statusEnum);
                	 break;
                case DLR_RTN:
                	errorMessages = simpleStatusMessageHandler.handle(statusMessage,statusEnum);
                	 break;
                default:
                    logger.warn("Unknown status message {}", statusMessage.toString());
                    errorMessages.add("Unknown status message "+ statusMessage.toString());
            }
        } catch (Exception e) {
        	logger.error(e.getMessage());
        	 errorMessages.add("status message: "+ statusMessage.toString()+" - "+e.getMessage());
        }
        
        return errorMessages;
    }

}
