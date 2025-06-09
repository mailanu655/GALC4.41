package com.honda.mfg.device.plc.omron.messages;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * User: Jeffrey M Lutz
 * Date: 4/10/11
 */
public class FinsInitializeResponseTest {

    @Test
    public void successfullyHandleResponse() {
        FinsInitializeResponse response =
                new FinsInitializeResponse(1, 2);
        assertNotNull(response.getFinsResponse());
        assertEquals("", response.getData());
        assertNotNull(response.toString());
    }
}
