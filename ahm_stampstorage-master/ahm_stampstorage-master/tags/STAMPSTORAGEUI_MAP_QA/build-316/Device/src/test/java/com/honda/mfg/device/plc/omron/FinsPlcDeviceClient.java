package com.honda.mfg.device.plc.omron;

import com.honda.eventbus.EventBusConfig;
import com.honda.io.socket.SocketFactory;
import com.honda.io.socket.SocketStreamPair;
import com.honda.mfg.device.plc.PlcDevice;
import com.honda.mfg.device.plc.PlcDevicePing;
import com.honda.mfg.device.plc.omron.messages.FinsMemoryReadResponse;
import com.honda.mfg.device.watchdog.DevicePing;
import com.honda.mfg.device.watchdog.Watchdog;
import com.honda.mfg.device.watchdog.WatchdogAdapter;
import org.bushe.swing.event.EventServiceExistsException;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 27, 2010
 */
public class FinsPlcDeviceClient implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(FinsPlcDeviceClient.class);

    String previousValue;

    BasicFinsPlcDevice basicPlcDevice;
    PlcDevice plcDevice;
    private static final int MEMORY_SIZE = 20;
    private String memoryReadValue;

    private FinsInitializer initializer;

    public FinsPlcDeviceClient() throws EventServiceExistsException {

        EventBusConfig.setSingleThreadMode();

        String host;
        host = "10.44.1.222";
//        host = "xpd32764";

        int socketConnectTimeout = 2;
        int initializerInterval = 5;
        int deviceResponseInterval = 5;
        int watchInterval = initializerInterval * 2;

        SocketFactory socketFactory = new SocketFactory(host, 9600, socketConnectTimeout);
        SocketStreamPair streamPair = new SocketStreamPair(socketFactory);
        FinsProcessorPair processorPair = new StreamFinsProcessorPair(streamPair);
        this.initializer = new FinsInitializer(processorPair, initializerInterval);
        this.basicPlcDevice = new BasicFinsPlcDevice(processorPair, initializer, deviceResponseInterval);
        DevicePing devicePing = new PlcDevicePing(basicPlcDevice);
        Watchdog watchDog = new Watchdog(streamPair);
        WatchdogAdapter adapter = new WatchdogAdapter(watchDog, devicePing, watchInterval);

        plcDevice = new AdvancedFinsPlcDevice(basicPlcDevice, adapter);
        AnnotationProcessor.process(this);
    }

    public static void main(String[] args) throws EventServiceExistsException {
        FinsPlcDeviceClient client = new FinsPlcDeviceClient();
        client.run();
    }

    public void run() {
        FinsMemory memory = new FinsMemory(FinsMemory.BANK.DM, 0, MEMORY_SIZE);
        long cnt = 0;
        while (true) {
            LOG.info("initialized? " + initializer.isInitialized());
            try {
                String clock = plcDevice.readClock();
                print(clock);

                String val = ("                    " + cnt);
                val = val.substring(val.length() - MEMORY_SIZE, val.length());
                plcDevice.writeMemory(memory, val);
                plcDevice.readMemory(memory);
//                while (memoryReadValue == null)
//                    ;
                LOG.info("Read data:  " + memoryReadValue);
                cnt++;
                memoryReadValue = null;
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

    @EventSubscriber(eventClass = FinsMemoryReadResponse.class)
    public void readMemoryResponse(FinsMemoryReadResponse response) {
        this.memoryReadValue = response.getData();
    }

    private void sleep(long time) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < time) {
            ;
        }
    }
}
