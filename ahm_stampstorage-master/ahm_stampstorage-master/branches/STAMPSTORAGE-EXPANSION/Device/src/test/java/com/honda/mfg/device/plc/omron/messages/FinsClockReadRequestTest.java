package com.honda.mfg.device.plc.omron.messages;

import com.honda.mfg.device.plc.omron.exceptions.FinsRequestException;
import org.junit.Assert;
import org.junit.Test;


public class FinsClockReadRequestTest {
    private int destinationNode = 100;
    private int sourceNode = 101;
    private int serviceId = 1;

    @Test
    public void buildsValidMessage() {
        FinsClockReadRequest clockReadRequest = new FinsClockReadRequest(destinationNode, sourceNode, serviceId);
        Assert.assertNotNull("Did not create write events", clockReadRequest.getMessageRequest());
    }

    @Test(expected = FinsRequestException.class)
    public void throwsFinsRequestExceptionIfServiceIdIsTooLarge() {
        String message = "test";
        new FinsClockReadRequest(destinationNode, sourceNode, 256);
    }
}
