package com.honda.mfg.device.plc.omron.messages;

import com.honda.mfg.device.messages.MessageBase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 * User: Jeffrey M Lutz
 * Date: 4/11/11
 */
public class FinsClockReadResponseTest extends MessageBase {
    private static final Logger LOG = LoggerFactory.getLogger(FinsClockReadResponseTest.class);

    @Test
    public void successfullyReadsClock() {
        int dest = 1;
        int src = 2;
        int service = 3;
        String expectedClock = "11-01-02 01:23:45_12";
        String clock = getClockValue();
        FinsClockReadResponse response =
                new FinsClockReadResponse(dest, src, service, clock);
        String actualClock = response.clockReading();
        LOG.info("EXPECTED CLOCK: " + expectedClock);
        LOG.info("ACTUAL CLOCK:   " + actualClock);
        assertEquals("Unable to compute correct clock reading",
                expectedClock, actualClock);
    }

    private String getClockValue() {
        StringBuilder sb = new StringBuilder();
        sb.append(asChars(17, 1));
        sb.append(asChars(1, 1));
        sb.append(asChars(2, 1));
        sb.append(asChars(1, 1));
        sb.append(asChars(35, 1));
        sb.append(asChars(69, 1));
        sb.append(asChars(18, 1));
        return sb.toString();
    }
}
