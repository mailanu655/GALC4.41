package com.honda.eventbus;

import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventServiceExistsException;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.SwingEventService;
import org.bushe.swing.event.ThreadSafeEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Jeffrey M Lutz Date: 4/4/11
 */
public class EventBusConfig {
	private static final Logger LOG = LoggerFactory.getLogger(EventBusConfig.class);
	private static EventService eventService;

	private EventBusConfig() {
	}

	public static void setSwingMode() throws EventServiceExistsException {
		if (isAlreadySet() && isSwingMode()) {
			// Already set, return
			LOG.info("Already set to Swing Mode!");
			return;
		}
		EventService newEventService = new SwingEventService();
		EventServiceLocator.setEventService(EventServiceLocator.SERVICE_NAME_EVENT_BUS, newEventService);

		LOG.warn("****** NEW EVENT SERVICE STARTING - Swing Mode *****************************");
		eventService = newEventService;
	}

	public static void setSingleThreadMode() throws EventServiceExistsException {
		if (isAlreadySet() && isSingleThreadMode()) {
			// Already set, return
			LOG.info("Already set to Single Thread Mode!");
			return;
		}
		EventService newEventService = new ThreadSafeEventService();
		EventServiceLocator.setEventService(EventServiceLocator.SERVICE_NAME_EVENT_BUS, newEventService);

		LOG.warn("****** NEW EVENT SERVICE STARTING - Single Thread Mode *********************");
		eventService = newEventService;
	}

	public static boolean isSwingMode() {
		boolean retVal = eventService instanceof SwingEventService;
		LOG.info("isSwingMode()?  " + retVal);
		return retVal;
	}

	public static boolean isSingleThreadMode() {
		boolean retVal = eventService instanceof ThreadSafeEventService;
		LOG.info("isSingleThreadMode()?  " + retVal);
		return retVal;
	}

	public static boolean isAlreadySet() {
		boolean retVal = eventService != null;
		LOG.info("Checking if Event Service is Initialized: " + retVal);
		return retVal;
	}
}
