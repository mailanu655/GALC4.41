package com.honda.mfg.device.rfid.etherip;

import com.honda.io.StreamPair;
import com.honda.io.socket.SocketFactory;
import com.honda.io.socket.SocketStreamPair;
import com.honda.mfg.device.rfid.etherip.messages.RfidResponse;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Jeffrey M Lutz
 * Date: Dec 9, 2010
 */
public class RfidDeviceListener {
    private static final Logger LOG = LoggerFactory.getLogger(BasicEtherIpRfidDevice.class);

    public RfidDeviceListener() {
        super();
        AnnotationProcessor.process(this);
        SocketFactory socketFactory = new SocketFactory("10.72.85.15", 50200, 5);
        StreamPair streamPair = new SocketStreamPair(socketFactory);
        RfidProcessorPair processorPair = new StreamRfidProcessorPair(streamPair);
        LOG.info("Initialized!");
    }

    public static void main(String[] args) {
        RfidDeviceListener l = new RfidDeviceListener();
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @EventSubscriber(eventClass = RfidResponse.class, referenceStrength = ReferenceStrength.STRONG)
    public void listener(RfidResponse rfidResponse) {
        LOG.info("Received:  " + rfidResponse);
    }
}
