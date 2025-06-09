package com.honda.mfg.connection.processor;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.io.socket.SocketFactory;
import com.honda.io.socket.SocketStreamPair;
import com.honda.mfg.connection.processor.messages.GeneralMessage;
import com.honda.mfg.connection.watchdog.DevicePing;
import com.honda.mfg.connection.watchdog.Watchdog;
import com.honda.mfg.connection.watchdog.WatchdogAdapter;
import com.honda.mfg.connection.watchdog.WatchdogAdapterInterface;

/**
 * User: vcc30690 Date: 4/13/11
 */
public class ConnectionDeviceClient {

	private static final Logger LOG = LoggerFactory.getLogger(ConnectionDeviceClient.class);

	String previousValue;

	BasicConnection basicConnection;
	ConnectionDevice advancedMesDevice;
	private String message;

	private ConnectionInitializer initializer;

	public ConnectionDeviceClient() {
		String host = "10.44.1.6";
		int port = 44445;

		int socketConnectTimeout = 5;
		int initializerInterval = 5;
		int deviceResponseInterval = 2;
		int watchInterval = initializerInterval * 2;

		SocketFactory socketFactory = new SocketFactory(host, port, socketConnectTimeout);
		SocketStreamPair streamPair = new SocketStreamPair(socketFactory);
		ConnectionProcessorPair processorPair = new StreamConnectionProcessorPair(streamPair);
		this.initializer = new ConnectionInitializer(processorPair, initializerInterval);
		this.basicConnection = new BasicConnection(processorPair, initializer, deviceResponseInterval);
		DevicePing devicePing = new ConnectionPing(basicConnection);
		Watchdog watchDog = new Watchdog(streamPair);
		WatchdogAdapterInterface adapter = new WatchdogAdapter(watchDog, devicePing, watchInterval);

		advancedMesDevice = new AdvancedConnection(basicConnection, adapter);
		AnnotationProcessor.process(this);
	}

	public static void main(String[] args) {
		ConnectionDeviceClient client = new ConnectionDeviceClient();
		client.go();
	}

	private void go() {
		long cnt = 0;
		while (true) {
			try {
				LOG.debug("initialized? " + initializer.isInitialized());
				basicConnection.isHealthy();
			} catch (Exception e) {
				LOG.debug("Error: " + e.getMessage());
			}
			sleep(1000L);
		}
	}

	private void print(String currentValue) {
		if (currentValue.equals(previousValue)) {
			LOG.error("Current value and previous value are the same!!!");
			LOG.error("  --> Previous:  " + previousValue);
			LOG.error("  --> Current:   " + currentValue);
		}
		LOG.info("VALUE: " + currentValue);
		previousValue = currentValue;
	}

	@EventSubscriber(eventClass = GeneralMessage.class)
	public void readMessage(GeneralMessage response) {
		this.message = response.getMessage();
	}

	private void sleep(long time) {
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start < time) {
			;
		}
	}
}
