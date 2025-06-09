package com.honda.mfg.device.rfid;

import com.honda.mfg.device.watchdog.DevicePing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Jeffrey M Lutz
 * Date: 4/2/11
 */
public class RfidDevicePing implements DevicePing {
    private static final Logger LOG = LoggerFactory.getLogger(RfidDevicePing.class);

    private RfidDevice rfidDevice;
    private String controllerInfo;

    public RfidDevicePing(RfidDevice rfidDevice) {
        this.rfidDevice = rfidDevice;
    }

    @Override
    public boolean ping() {
        try {
            LOG.debug("PING:   Making call to rfidDevice.readControllerInfo().");
            controllerInfo = rfidDevice.readControllerInfo();
        } catch (RuntimeException e) {
            LOG.trace("Failed reading controller info.  error msg: " + e.getMessage(), e);
            return false;
        }
        LOG.debug("Success reading controller info.  controller info: ");
        return isControllerInfoValid();
    }

    private boolean isControllerInfoValid() {
        return controllerInfo != null && controllerInfo.length() > 0;
    }
}
