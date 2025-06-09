package com.honda.mfg.device.plc;

import com.honda.mfg.device.watchdog.DevicePing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Jeffrey M Lutz
 * Date: 4/2/11
 */
public class PlcDevicePing implements DevicePing {
    private static final Logger LOG = LoggerFactory.getLogger(PlcDevicePing.class);

    private PlcDevice plcDevice;

    public PlcDevicePing(PlcDevice plcDevice) {
        this.plcDevice = plcDevice;
    }

    @Override
    public boolean ping() {
        String clockReading;
        try {
            clockReading = plcDevice.readClock();
        } catch (RuntimeException e) {
            LOG.debug("Failed reading plc clock.  error msg: " + e.getMessage(), e);
            return false;
        }
        boolean validReading = isClockReadingValid(clockReading);
        LOG.debug("Success reading plc clock.  valid reading? " + validReading + "    --> clock: " + clockReading);
        return validReading;
    }

    private boolean isClockReadingValid(String clockReading) {
        return clockReading != null && clockReading.length() > 0;
    }
}
