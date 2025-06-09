package com.honda.mfg.device.rfid.etherip.messages;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Date;

public class RfidReadTagIdResponseTest {
    private final int instanceCounter = 0;
    private final int nodeId = 1;
    private final Date date = new Date();

    @Test
    public void buildsValidMinTagIdResponse() {
        String expectedTag = "0000000000000000";
        RfidReadTagIdResponse response =
                new RfidReadTagIdResponse(instanceCounter, nodeId, date, expectedTag);
        Assert.assertEquals(expectedTag, response.getTagId());
    }

    @Test
    public void buildsValidMaxTagIdResponse() {
        String expectedTag = "FFFFFFFFFFFFFFFF";
        RfidReadTagIdResponse response =
                new RfidReadTagIdResponse(instanceCounter, nodeId, date, expectedTag);
        Assert.assertEquals(expectedTag, response.getTagId());
    }
}
