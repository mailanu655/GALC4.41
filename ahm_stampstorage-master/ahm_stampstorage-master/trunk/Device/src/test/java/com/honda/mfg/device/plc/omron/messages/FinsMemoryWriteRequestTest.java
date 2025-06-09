package com.honda.mfg.device.plc.omron.messages;

import com.honda.mfg.device.plc.omron.FinsMemory;
import com.honda.mfg.device.plc.omron.exceptions.FinsRequestException;
import org.junit.Assert;
import org.junit.Test;


public class FinsMemoryWriteRequestTest {
    private int destinationNode = 100;
    private int sourceNode = 101;
    private int serviceId = 1;
    private FinsMemory memory = new FinsMemory(FinsMemory.BANK.DM, 100, 20);

    @Test
    public void buildsValidMessage() {
        String message = "test";
        FinsMemoryWriteRequest writeRequest = new FinsMemoryWriteRequest(memory, destinationNode, sourceNode, serviceId, message);
        Assert.assertNotNull("Did not create write events", writeRequest.getMessageRequest());
    }

    @Test(expected = FinsRequestException.class)
    public void throwsFinsRequestExceptionWhenPassingNullMessage() {
        new FinsMemoryWriteRequest(memory, destinationNode, sourceNode, serviceId, null);
    }

    @Test(expected = FinsRequestException.class)
    public void throwsFinsRequestExceptionIfMessageIsTooLarge() {
        String message = "test";
        memory = new FinsMemory(FinsMemory.BANK.DM, 100, 1);
        new FinsMemoryWriteRequest(memory, destinationNode, sourceNode, serviceId, message);
    }

    @Test(expected = FinsRequestException.class)
    public void throwsFinsRequestExceptionIfServiceIdIsTooLarge() {
        String message = "test";
        memory = new FinsMemory(FinsMemory.BANK.DM, 100, 20);
        new FinsMemoryWriteRequest(memory, destinationNode, sourceNode, 256, message);
    }

    @Test
    public void paddsOriginalMessageUpToExpectedLength() {
        String message = "test";
        memory = new FinsMemory(FinsMemory.BANK.DM, 100, 3);
        FinsMemoryWriteRequest finsWriteRequest = new FinsMemoryWriteRequest(memory, destinationNode, sourceNode, 255, message);
        Assert.assertEquals("Did not pad the original messages", message + '\u0000' + '\u0000', finsWriteRequest.getPaddedData());
    }

}
