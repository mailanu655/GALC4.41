package com.honda.mfg.stamp.conveyor.processor;

import org.bushe.swing.event.EventServiceExistsException;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.ThreadSafeEventService;

/**
 * User: vcc30690 Date: 3/24/11
 */
public abstract class AbstractTestBase {
	static {
		try {
			EventServiceLocator.setEventService(EventServiceLocator.SERVICE_NAME_EVENT_BUS,
					new ThreadSafeEventService());
		} catch (EventServiceExistsException e) {
			e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
		}
	}
}
