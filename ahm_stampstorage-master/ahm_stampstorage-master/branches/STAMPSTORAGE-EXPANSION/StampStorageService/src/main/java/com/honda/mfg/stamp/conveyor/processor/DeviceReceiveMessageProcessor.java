package com.honda.mfg.stamp.conveyor.processor;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.connection.processor.messages.GeneralMessage;
import com.honda.mfg.stamp.conveyor.messages.Message;
import com.honda.mfg.stamp.conveyor.processor.messagebuilders.StorageMessageBuilder;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapper;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapperImpl;

/**
 * User: vcc30690 Date: 3/23/11
 */
public class DeviceReceiveMessageProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(DeviceReceiveMessageProcessor.class);

	private StorageMessageBuilder builder;
	private GeneralMessage message;
	private ServiceRoleWrapper serviceRole = null;

	public DeviceReceiveMessageProcessor() {
		AnnotationProcessor.process(this);
		// String name = Thread.currentThread().getName();
		builder = new StorageMessageBuilder();
		serviceRole = ServiceRoleWrapperImpl.getInstance();

	}

	@EventSubscriber(eventClass = GeneralMessage.class, referenceStrength = ReferenceStrength.STRONG)
	public void mesMessageListener(GeneralMessage message) {
		if (serviceRole.isPassive()) {
			return;
		}
		this.message = message;
		LOG.info("Received MSG: " + message.getMessage());
		if (message.getMessage() != null) {
			Message storageMessage = builder.buildStorageMessage(message.getMessage());
			LOG.info("Built message to be published:  " + storageMessage);
			if (storageMessage != null) {
				LOG.debug("publishing msg to EventBus.");
				EventBus.publish(storageMessage);
			}
		}
	}

//    public void clearMessage()  {
//    	message = null;
//    }

	GeneralMessage getGeneralMessage() {
		return message;
	}

	public void turnoffSubscriber() {
		AnnotationProcessor.unprocess(this);
	}

}
