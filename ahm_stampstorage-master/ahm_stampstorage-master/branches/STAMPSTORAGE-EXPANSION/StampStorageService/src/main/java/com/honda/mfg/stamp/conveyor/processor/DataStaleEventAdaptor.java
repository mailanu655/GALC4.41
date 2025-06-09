package com.honda.mfg.stamp.conveyor.processor;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.connection.processor.messages.ConnectionInitialized;
import com.honda.mfg.connection.processor.messages.ConnectionUninitialized;
import com.honda.mfg.stamp.conveyor.messages.StaleDataMessage;

/**
 * User: Jeffrey M Lutz Date: 6/19/11
 * <p/>
 * The dataStaleEventAdaptor purpose is to detect MES device initialize and
 * uninitialize events and publishes the StaleDataMessage. The StaleDataMessage
 * is used by the CarrierMes_Roo_Entity.aj (aspect) to detect when CarrierMes
 * data in DB2 is to be considered stale or fresh. To properly use this adaptor,
 * simply construct (default constructor) a single instance
 */
public class DataStaleEventAdaptor {
	private static final Logger LOG = LoggerFactory.getLogger(DataStaleEventAdaptor.class);

	public DataStaleEventAdaptor() {
		AnnotationProcessor.process(this);
	}

	@EventSubscriber(eventClass = ConnectionInitialized.class, referenceStrength = ReferenceStrength.STRONG)
	public void processDataFresh(ConnectionInitialized msg) {
		LOG.info("Publishing StaleData  message! ");
		EventBus.publish(new StaleDataMessage(false));
		EventBus.publish(new ConnectionEventMessage(true));
	}

	@EventSubscriber(eventClass = ConnectionUninitialized.class, referenceStrength = ReferenceStrength.STRONG)
	public void processDataStale(ConnectionUninitialized msg) {
		LOG.info("Publishing StaleData message! ");
		EventBus.publish(new StaleDataMessage(true));
		EventBus.publish(new ConnectionEventMessage(false));
	}
}
