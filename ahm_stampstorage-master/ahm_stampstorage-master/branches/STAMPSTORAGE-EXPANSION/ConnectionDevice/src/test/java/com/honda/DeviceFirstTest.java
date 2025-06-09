package com.honda;

import static junit.framework.Assert.assertTrue;

import org.bushe.swing.event.EventServiceExistsException;
import org.junit.Test;

import com.honda.eventbus.EventBusConfig;

/**
 * User: Jeffrey M Lutz Date: 4/5/11
 */
public class DeviceFirstTest {

	@Test
	public void myFirstTest() throws EventServiceExistsException {
		EventBusConfig.setSingleThreadMode();
		assertTrue(EventBusConfig.isSingleThreadMode());
	}
}
