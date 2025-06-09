package com.honda.mfg.connection.processor;

import com.honda.io.socket.SocketFactory;
import com.honda.io.socket.SocketStreamPair;
import com.honda.mfg.connection.processor.TestableConnectionModuleServer;
import com.honda.mfg.connection.processor.messages.GeneralMessage;
import com.honda.mfg.connection.processor.messages.ConnectionMessage;
import com.honda.mfg.connection.watchdog.DevicePing;
import com.honda.mfg.connection.watchdog.Watchdog;
import com.honda.mfg.connection.watchdog.WatchdogAdapter;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

/**
 * User: vcc30690
 * Date: 4/18/11
 */

public class ConnectionServerTest {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectionServerTest.class);

    private final String GOOD_HOST = "localhost";
    private final int GOOD_PORT = 65535;
    private final int GOOD_CONNECT_TIMEOUT = 5;

    private TestableConnectionModuleServer server;

    @After
    public void after() {
        if (server != null) {
            server.disconnect();
        }
    }

    // TODO Broken test that needs fixed...
    // TODO This test does not seem to tear everything down at the end.
    @Test(timeout = 5000L)
    public void successfullyPingMesModuleServer() throws IOException {
        // Pre-condition
//        server = new TestableConnectionModuleServer(GOOD_PORT);
//
//        SocketFactory socketFactory = new SocketFactory(GOOD_HOST, GOOD_PORT, GOOD_CONNECT_TIMEOUT);
//        SocketStreamPair streamPair = new SocketStreamPair(socketFactory);
//        ConnectionProcessorPair processorPair = new StreamConnectionProcessorPair(streamPair);
//        LOG.info("####### trying to Connect ##########");
//        while (!server.hasAcceptedClientConnection()) {
//            try {
//                Thread.sleep(100L);
//            } catch (Exception e) {
//            }
//        }
//
//        // Perform test
//        ConnectionInitializer initializer = new ConnectionInitializer(processorPair, 1);
//        LOG.info("************** trying to initialize ************");
//        while (!initializer.isInitialized()) {
//            try {
//                Thread.sleep(100L);
//            } catch (Exception e) {
//            }
//        }
//
//        BasicConnection mes = new BasicConnection(processorPair, initializer, 5);
//        DevicePing devicePing = new ConnectionPing(mes);
//        Watchdog watchDog = new Watchdog(streamPair);
//        WatchdogAdapter adapter = new WatchdogAdapter(watchDog, devicePing, 5);
//
//        AdvancedConnection advancedMesDevice = new AdvancedConnection(mes, adapter);
//
//        LOG.info("sending message !!!!!!!");
//        GeneralMessage actualMessage = new GeneralMessage("{hello}");
//        ConnectionMessage expectedMessage = advancedMesDevice.sendMessage(actualMessage, 1000);
//
//        assertEquals(expectedMessage.getMessage(), actualMessage.getMessage());
//
//        LOG.info("is device Healthy????????");
//        while (!advancedMesDevice.isHealthy()) {
//            try {
//                Thread.sleep(100L);
//            } catch (Exception e) {
//            }
//        }
//        LOG.info("ABOUT DONE!!!");
//        assertEquals(true, advancedMesDevice.isHealthy());
//        LOG.info("DONE!!!");
    }
}