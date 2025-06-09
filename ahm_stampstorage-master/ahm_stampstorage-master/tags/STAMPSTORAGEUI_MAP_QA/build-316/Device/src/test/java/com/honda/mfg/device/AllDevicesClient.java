package com.honda.mfg.device;

import com.honda.eventbus.EventBusConfig;
import com.honda.mfg.device.plc.omron.FinsPlcDeviceClient;
import com.honda.mfg.device.rfid.etherip.EtherIpRfidDeviceClient;
import com.honda.mfg.schedule.Scheduler;
import org.bushe.swing.event.EventServiceExistsException;

/**
 * User: Jeffrey M Lutz
 * Date: 4/14/11
 */
public class AllDevicesClient {

    public static void main(String[] args) throws EventServiceExistsException {
        EventBusConfig.setSingleThreadMode();

        FinsPlcDeviceClient plcClient = new FinsPlcDeviceClient();
        EtherIpRfidDeviceClient rfidClient = new EtherIpRfidDeviceClient();

        new Scheduler(plcClient, "PLC Client");
        new Scheduler(rfidClient, "RFID Client");
        while (true) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
