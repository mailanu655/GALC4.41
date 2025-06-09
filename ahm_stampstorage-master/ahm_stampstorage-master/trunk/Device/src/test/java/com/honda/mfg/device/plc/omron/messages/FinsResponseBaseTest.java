package com.honda.mfg.device.plc.omron.messages;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * User: Jeffrey M Lutz
 * Date: 4/10/11
 */
public class FinsResponseBaseTest {

    @Test
    public void successfullyTestToString() {
        String data = "123";
        FinsResponseBase base = new FinsResponseBase(1, 2, 3, FinsCommand.CLOCK_READ, data);
        assertNotNull(base.toString());
    }
}
