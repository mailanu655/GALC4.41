package com.honda.mfg.stamp.conveyor.messages;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 2/13/12 Time: 1:35 PM To
 * change this template use File | Settings | File Templates.
 */
public class StatusUpdateMessageTest {

	@Test
	public void successfullyCreateValidMessage() {
		List<Integer> numbers = new ArrayList<Integer>();
		numbers.add(5);
		numbers.add(6);
		StatusUpdateMessage msg = new StatusUpdateMessage(CarrierStatus.ON_HOLD, numbers, "user");

		assertEquals(StorageMessageType.STATUS_UPDATE, msg.getMessageType());
		assertEquals("1", msg.status);
		assertEquals("user", msg.source);
		assertEquals("5,6,0,0,0,0,0,0,0,0", msg.carrierNumbers);
	}

	@Test
	public void successfullyCreateValidMessage2() {

		StatusUpdateMessage msg = new StatusUpdateMessage(null, null, null);

		assertEquals(StorageMessageType.STATUS_UPDATE, msg.getMessageType());
		assertEquals("", msg.status);
		assertEquals("NOT_SET", msg.source);
		assertEquals("0,0,0,0,0,0,0,0,0,0", msg.carrierNumbers);
	}
}
