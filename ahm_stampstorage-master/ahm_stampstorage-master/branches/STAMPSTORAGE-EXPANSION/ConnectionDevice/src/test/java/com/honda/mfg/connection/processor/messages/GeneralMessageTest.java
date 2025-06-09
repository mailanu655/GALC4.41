package com.honda.mfg.connection.processor.messages;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * User: vcc30690 Date: 4/13/11
 */
public class GeneralMessageTest {

	@Test
	public void testAllMessages() {

		GeneralMessage msg = new GeneralMessage("hello");
		assertNotNull(msg.getMessage());

		PingMessage pingMessage = new PingMessage();
		assertNotNull(pingMessage.getMessage());

		ConnectionRequest request = new ConnectionRequest(msg);
		assertNotNull(request.getMessageRequest());
	}

	@Test
	public void testAnotherMessages() {
		String expectedMsg = "{\"GeneralMessage\":{hello}}";
		GeneralMessage msg = new GeneralMessage("{hello}");
		assertNotNull(msg.getMessage());
		assertEquals(expectedMsg, msg.getMessage());

		PingMessage pingMessage = new PingMessage();
		assertNotNull(pingMessage.getMessage());

		ConnectionRequest request = new ConnectionRequest(msg);
		assertNotNull(request.getMessageRequest());
	}

	@Test
	public void successfullyConstructWrappedMessage() {
		String expectedMsg = "{\"GeneralMessage\":{hello}}";
		GeneralMessage generalMessage = new GeneralMessage(expectedMsg);
		String actualMsg = generalMessage.getMessage();
		assertEquals(expectedMsg, actualMsg);
	}
}
