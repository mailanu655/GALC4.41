package com.honda.mfg.device.mesmodule;

import com.honda.io.socket.SocketFactory;
import com.honda.io.socket.SocketStreamPair;
import com.honda.mfg.device.mesmodule.messages.GeneralMessage;
import com.honda.mfg.device.watchdog.DevicePing;
import com.honda.mfg.device.watchdog.Watchdog;
import com.honda.mfg.device.watchdog.WatchdogAdapter;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: vcc30690
 * Date: 4/13/11
 */
public class MesDeviceClient {

    private static final Logger LOG = LoggerFactory.getLogger(MesDeviceClient.class);

    String previousValue;

    BasicMesDevice basicMesDevice;
    MesDevice advancedMesDevice;
    private String message;

    private MesInitializer initializer;

    public MesDeviceClient() {
        String host = "10.44.1.6";
        int port = 44445;

        int socketConnectTimeout = 5;
        int initializerInterval = 5;
        int deviceResponseInterval = 2;
        int watchInterval = initializerInterval * 2;

        SocketFactory socketFactory = new SocketFactory(host, port, socketConnectTimeout);
        SocketStreamPair streamPair = new SocketStreamPair(socketFactory);
        MesProcessorPair processorPair = new StreamMesProcessorPair(streamPair);
        this.initializer = new MesInitializer(processorPair, initializerInterval);
        this.basicMesDevice = new BasicMesDevice(processorPair, initializer, deviceResponseInterval);
        DevicePing devicePing = new MesDevicePing(basicMesDevice);
        Watchdog watchDog = new Watchdog(streamPair);
        WatchdogAdapter adapter = new WatchdogAdapter(watchDog, devicePing, watchInterval);

        advancedMesDevice = new AdvancedMesDevice(basicMesDevice, adapter);
        AnnotationProcessor.process(this);
    }

    public static void main(String[] args) {
        MesDeviceClient client = new MesDeviceClient();
        client.go();
    }

    private void go() {
        long cnt = 0;
        while (true) {
            try {
                LOG.debug("initialized? " + initializer.isInitialized());
                basicMesDevice.isHealthy();
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
