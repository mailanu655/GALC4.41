package com.honda.mfg.device.mesmodule;

import com.honda.io.socket.SocketFactory;
import com.honda.io.socket.SocketStreamPair;
import com.honda.mfg.device.mesmodule.messages.GeneralMessage;
import com.honda.mfg.device.mesmodule.messages.MesMessage;
import com.honda.mfg.device.watchdog.DevicePing;
import com.honda.mfg.device.watchdog.Watchdog;
import com.honda.mfg.device.watchdog.WatchdogAdapter;
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

public class MesServerTest {
    private static final Logger LOG = LoggerFactory.getLogger(MesServerTest.class);

    private final String GOOD_HOST = "localhost";
    private final int GOOD_PORT = 65535;
    private final int GOOD_CONNECT_TIMEOUT = 5;

    private TestableMesModuleServer server;

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
//        server = new TestableMesModuleServer(GOOD_PORT);
//
//        SocketFactory socketFactory = new SocketFactory(GOOD_HOST, GOOD_PORT, GOOD_CONNECT_TIMEOUT);
//        SocketStreamPair streamPair = new SocketStreamPair(socketFactory);
//        MesProcessorPair processorPair = new StreamMesProcessorPair(streamPair);
//        LOG.info("####### trying to Connect ##########");
//        while (!server.hasAcceptedClientConnection()) {
//            try {
//                Thread.sleep(100L);
//            } catch (Exception e) {
//            }
//        }
//
//        // Perform test
//        MesInitializer initializer = new MesInitializer(processorPair, 1);
//        LOG.info("************** trying to initialize ************");
//        while (!initializer.isInitialized()) {
//            try {
//                Thread.sleep(100L);
//            } catch (Exception e) {
//            }
//        }
//
//        BasicMesDevice mes = new BasicMesDevice(processorPair, initializer, 5);
//        DevicePing devicePing = new MesDevicePing(mes);
//        Watchdog watchDog = new Watchdog(streamPair);
//        WatchdogAdapter adapter = new WatchdogAdapter(watchDog, devicePing, 5);
//
//        AdvancedMesDevice advancedMesDevice = new AdvancedMesDevice(mes, adapter);
//
//        LOG.info("sending message !!!!!!!");
//        GeneralMessage actualMessage = new GeneralMessage("{hello}");
//        MesMessage expectedMessage = advancedMesDevice.sendMessage(actualMessage, 1000);
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