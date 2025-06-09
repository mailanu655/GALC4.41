package com.honda.mfg.stamp.conveyor.processor;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.junit.Test;

import com.honda.mfg.connection.processor.messages.ConnectionMessage;
import com.honda.mfg.connection.processor.messages.GeneralMessage;
import com.honda.mfg.stamp.conveyor.messages.CarrierStatusMessage;
import com.honda.mfg.stamp.conveyor.messages.DeviceMessageType;
import com.honda.mfg.stamp.conveyor.messages.Message;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapper;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapperImpl;

/**
 * User: vcc30690 Date: 3/23/11
 */
public class DeviceReceiveMessageProcessorTest extends AbstractTestBase {

	Message storageMessage;

	@EventSubscriber(eventClass = CarrierStatusMessage.class)
	public void catchStorageEvent(CarrierStatusMessage response) {
		String name = Thread.currentThread().getName();
		storageMessage = response;
	}

	@Test
	public void successfullyPublishStorageMessage() {
		AnnotationProcessor.process(this);
		DeviceReceiveMessageProcessor messageProcessor = new DeviceReceiveMessageProcessor();
		ServiceRoleWrapper sRole = ServiceRoleWrapperImpl.getInstance();
		String msg = "{\"GeneralMessage\" : {\n" + "\"buffer\":\"1\",\n" + "\"carrierNumber\":\"101\",\n"
				+ "\"currentLocation\":\"0\",\n" + "\"destination\":\"503\",\n" + "\"dieNumber\":\"123\","
				+ "\"messageType\":\"" + DeviceMessageType.CARRIER_STATUS.toString() + "\",\n"
				+ "\"originationLocation\":\"0\",\n" + "\"quantity\":\"0\",\n" + "\"status\":\"0\",\n"
				+ "\"tagID\":\"0\",\n" + "\"updateDate\": \"2011-06-17 22:03:09\"\n" + "}}";
		ConnectionMessage message = new GeneralMessage(msg);
		boolean isPassive = sRole.isPassive(); // save original state
		sRole.setPassive(true);
		EventBus.publish(message);
		assertNull(messageProcessor.getGeneralMessage());
		sRole.setPassive(false);
		EventBus.publish(message);
		assertNotNull(messageProcessor.getGeneralMessage());
		messageProcessor.turnoffSubscriber();
		assertNotNull(storageMessage);
		sRole.setPassive(isPassive); // restore original state for subsequent tests
		AnnotationProcessor.unprocess(this);

	}
}
