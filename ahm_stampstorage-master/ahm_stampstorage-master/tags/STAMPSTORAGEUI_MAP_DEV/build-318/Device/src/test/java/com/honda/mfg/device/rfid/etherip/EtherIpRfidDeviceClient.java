package com.honda.mfg.device.rfid.etherip;

import com.honda.eventbus.EventBusConfig;
import com.honda.io.socket.SocketFactory;
import com.honda.io.socket.SocketStreamPair;
import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.ResponseTimeoutException;
import com.honda.mfg.device.rfid.RfidDevice;
import com.honda.mfg.device.rfid.RfidDevicePing;
import com.honda.mfg.device.rfid.etherip.exceptions.TagNotFoundException;
import com.honda.mfg.device.watchdog.DevicePing;
import com.honda.mfg.device.watchdog.Watchdog;
import com.honda.mfg.device.watchdog.WatchdogAdapter;
import org.bushe.swing.event.EventServiceExistsException;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Jeffrey M Lutz
 * Date: Dec 7, 2010
 */
public class EtherIpRfidDeviceClient implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(EtherIpRfidDeviceClient.class);
    private static final int RESPONSE_TIMEOUT_SEC = 666;
    private static final int RFID_RETRY_COUNT = 1;

    private BasicEtherIpRfidDevice basicRfidDevice;
    private SocketFactory socketFactory;
    private SocketStreamPair streamPair;
    private RfidProcessorPair processorPair;
    private EtherIpInitializer initializer;
    private RfidDevice device;

    public EtherIpRfidDeviceClient() {
        try {
            EventBusConfig.setSingleThreadMode();
        } catch (EventServiceExistsException e) {
            e.printStackTrace();
        }
        AnnotationProcessor.process(this);
        String host;
        host = "10.44.1.15";
//        host = "xpd32764";
        int port = 50200;

        int socketConnectTimeout = 5;
        int initializerInterval = 5;
        int deviceResponseInterval = 4;
        int watchInterval = initializerInterval * 4;

        socketFactory = new SocketFactory(host, port, socketConnectTimeout);
        streamPair = new SocketStreamPair(socketFactory);
        processorPair = new StreamRfidProcessorPair(streamPair);
        initializer = new EtherIpInitializer(processorPair, initializerInterval);
        BasicEtherIpRfidDevice basicRfidDevice = new BasicEtherIpRfidDevice(processorPair, initializer, deviceResponseInterval, RFID_RETRY_COUNT);
        DevicePing devicePing = new RfidDevicePing(basicRfidDevice);
        Watchdog watchdog = new Watchdog(streamPair);
        WatchdogAdapter adapter = new WatchdogAdapter(watchdog, devicePing, watchInterval);

        device = new AdvancedEtherIpRfidDevice(basicRfidDevice, adapter);
//        device = basicRfidDevice;
    }


    public static void main(String[] args) {
        EtherIpRfidDeviceClient client = new EtherIpRfidDeviceClient();
        client.run();
    }

    @Override
    public void run() {
        String tagId = "n/a";
        String writeVal = "                                                                                                                ";
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < (10 * 1000L)) {
            ;
        }
        while (true) {
            String config = "n/a";
            try {
                config = convertToHex(device.readControllerInfo());
                tagId = convertToHex(device.readTagId());
//                device.writeTag(new RfidMemory(4, 1), writeVal);
            } catch (TagNotFoundException e) {
                tagId = "<No RFID Tag Found>";
            } catch (CommunicationsException e) {
                LOG.info("Unable to read TAG ID.  Communications error. MSG: " + e.getMessage());
            } catch (ResponseTimeoutException e) {
                LOG.info("Unable to read TAG ID.  Response timeout. MSG: " + e.getMessage());
            } catch (Exception e) {
                LOG.error("Unable to read TAG ID.  General exception. MSG: " + e.getMessage(), e);
            }
            LOG.info("Initialized?  " + initializer.isInitialized() + " --> TAG ID READ: " + tagId + " --> CONFIG: " + config);
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //    @EventSubscriber(eventClass = Object.class, referenceStrength = ReferenceStrength.STRONG)
    public void subscribeMe(Object obj) {
        LOG.debug("Sleeping in subscriber.  OBJ:  " + obj.getClass().getSimpleName());
    }

    private String convertToHex(String string) {
        StringBuilder hexStr = new StringBuilder();
        for (int i = 0; string != null && i < string.length(); i++) {
            String hexVal = Integer.toHexString(string.charAt(i));
            if (i > 0) {
                hexStr.append("-");
            }
            if (hexVal.length() < 2) {
                hexStr.append("0");
            }
            hexStr.append(hexVal);
        }
        return hexStr.toString();
    }
}
