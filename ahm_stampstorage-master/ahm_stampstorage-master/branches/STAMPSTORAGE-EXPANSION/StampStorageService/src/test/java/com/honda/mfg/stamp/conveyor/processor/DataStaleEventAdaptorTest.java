package com.honda.mfg.stamp.conveyor.processor;

import static org.junit.Assert.assertEquals;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.junit.Test;

import com.honda.mfg.connection.processor.messages.ConnectionInitialized;
import com.honda.mfg.connection.processor.messages.ConnectionUninitialized;
import com.honda.mfg.stamp.conveyor.messages.StaleDataMessage;

/**
 * User: VCC30690 Date: 9/21/11
 */
public class DataStaleEventAdaptorTest extends AbstractTestBase {

	boolean reloadStorageState;
	int count = 0;

	@Test(timeout = 5000)
	public void successfullyPublishStaleDataMessage() {
		AnnotationProcessor.process(this);

		DataStaleEventAdaptor adaptor = new DataStaleEventAdaptor();
		ConnectionInitialized connectionInitialized = new ConnectionInitialized();
		EventBus.publish(connectionInitialized);

		while (!reloadStorageState) {

		}
		assertEquals(1, count);
		ConnectionUninitialized connectionUninitialized = new ConnectionUninitialized();
		EventBus.publish(connectionUninitialized);

		while (reloadStorageState) {
		}
		assertEquals(2, count);
		AnnotationProcessor.unprocess(this);
	}

	@EventSubscriber(eventClass = StaleDataMessage.class, referenceStrength = ReferenceStrength.STRONG)
	public void catchStorageEvent(StaleDataMessage message) {
		reloadStorageState = !message.isStale();
		count++;
	}

}
