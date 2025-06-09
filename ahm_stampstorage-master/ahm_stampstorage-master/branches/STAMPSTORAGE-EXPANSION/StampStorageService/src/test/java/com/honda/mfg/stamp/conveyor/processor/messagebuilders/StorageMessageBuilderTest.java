package com.honda.mfg.stamp.conveyor.processor.messagebuilders;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import org.junit.Test;

import com.honda.mfg.stamp.conveyor.messages.CarrierStatusMessage;
import com.honda.mfg.stamp.conveyor.messages.DeviceMessageType;
import com.honda.mfg.stamp.conveyor.messages.Message;
import com.honda.mfg.stamp.conveyor.processor.messagebuilders.exceptions.UnknownStorageMessageException;

/**
 * User: vcc30690 Date: 3/23/11
 */
public class StorageMessageBuilderTest {

	@Test
	public void successfullyBuildStopStatusMesResponse() {
		String msg = "{\"GeneralMessage\" : {\n" + "\"messageType\":\"" + DeviceMessageType.CARRIER_STATUS.toString()
				+ "\",\n" + "\"carrierNumber\":\"1234\",\n" + "\"currentLocation\":\"5-12\",\n"
				+ "\"destination\":\"\"\n" + "}}";
		Message response = new StorageMessageBuilder().buildStorageMessage(msg);

		assertEquals(true, response instanceof CarrierStatusMessage);
	}

	@Test
	public void returnsNullMesResponse() {
		String msg = "{\"GeneralMessage\" : {\n" + "\"messageType\":\" Unknown MessageType \",\n"
				+ "\"carrierNumber\":\"103\",\n" + "\"dieNumber\":\"ab12\",\n" + "\"quantity\":\"1\",\n"
				+ "\"productionDate\":\"03/21/2011 13:50:00\",\n" +

				"\"currentLocation\":{" + "\"name\":\"5-12\",\n" + "},\n" + "\"nextLocation\":\"\"\n" + "}}";
		Message response = new StorageMessageBuilder().buildStorageMessage(msg);

		assertNull(response);
	}

	@Test(expected = UnknownStorageMessageException.class)
	public void throwsUnKnownMessageException() {
		String msg = "{\"Message\" : {\n" + "\"messageType\":\"" + DeviceMessageType.CARRIER_STATUS.toString() + "\",\n"
				+ "}}";
		Message response = new StorageMessageBuilder().buildStorageMessage(msg);
	}
}
