package com.honda.ahm.lc.task;

import com.honda.ahm.lc.messages.StatusMessage;
import com.honda.ahm.lc.service.IQueueManagerService;
import com.honda.ahm.lc.handlers.StatusMessageHandlerFactory;
import com.honda.ahm.lc.util.EmailSender;
import com.honda.ahm.lc.util.JSONUtil;
import com.honda.ahm.lc.util.PropertyUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ReceivingTransactionTask")
public class ReceivingTransactionTask implements ITransactionTask {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IQueueManagerService queueManagerService;

    @Autowired
    private PropertyUtil propertyUtil;

    @Autowired
    private StatusMessageHandlerFactory statusMessageHandlerFactory;
    
    @Autowired
    private EmailSender emailSender;

    @Override
    public void execute() {
    	List<String> errorMessages = new ArrayList<String>();
        try {
        	
            // Read and parse message received from YMS Queue
            String message = queueManagerService.recv(propertyUtil.getSalesReceivingQueueName());
        	            
            if (Strings.isNotBlank(message)) {
            	logger.info("Message read from Queue-"+message);
                StatusMessage statusMessage = JSONUtil.getStatusMessageFromJSON(message.toLowerCase());
                logger.info("Received YMS message", statusMessage.toString());
                errorMessages.addAll(statusMessageHandlerFactory.handleMessage(statusMessage));
            } else {
                logger.info("No Message in Queue to read");
            }
        } catch (Exception ex) {
            logger.error("Error processing message from YMS", ex.getMessage());
            errorMessages.add("Error processing message from YMS-"+ ex.getMessage());
        }
        
        if(!errorMessages.isEmpty()) {
        	emailSender.sendEmail(getClass().getName()+" : ", errorMessages);
        }
    }

}
