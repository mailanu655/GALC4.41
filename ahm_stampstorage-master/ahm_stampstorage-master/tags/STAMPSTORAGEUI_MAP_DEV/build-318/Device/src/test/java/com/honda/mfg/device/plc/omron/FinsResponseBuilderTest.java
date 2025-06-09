package com.honda.mfg.device.plc.omron;

import com.honda.mfg.device.exceptions.UnknownResponseException;
import com.honda.mfg.device.plc.omron.messages.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * User: Mihail Chirita
 * Date: Sep 24, 2010
 */
public class FinsResponseBuilderTest {

    private int destinationNode = 100;
    private int sourceNode = 101;
    private int serviceId = 102;
    private String data = "testData";


    @Test
    public void buildsFinsInitializeResponse() {
        FinsInitializeResponse expectedResponse = new FinsInitializeResponse(destinationNode, sourceNode);
        String respString = expectedResponse.getFinsResponse();
        FinsResponseBuilder responseBuilder = new FinsResponseBuilder(new FinsResponseParser());
        FinsInitializeResponse actualResponse = (FinsInitializeResponse) responseBuilder.buildFinsResponse(respString);
        Assert.assertEquals("Build wrong type of response", FinsInitializeResponse.class, responseBuilder.buildFinsResponse(respString).getClass());
        Assert.assertEquals(FinsCommand.INITIALIZE, actualResponse.getFinsCommand());
        Assert.assertEquals(sourceNode, actualResponse.getSourceNode());
        Assert.assertEquals(destinationNode, actualResponse.getDestinationNode());
    }

    @Test
    public void buildsFinsReadResponse() {
        FinsMemoryReadResponse finsMemoryReadResponse = new FinsMemoryReadResponse(destinationNode, sourceNode, serviceId, data);
        String finsResponse = finsMemoryReadResponse.getFinsResponse();
        FinsResponseBuilder responseBuilder = new FinsResponseBuilder(new FinsResponseParser());
        Assert.assertEquals("Build wrong type of response", FinsMemoryReadResponse.class, responseBuilder.buildFinsResponse(finsResponse).getClass());
    }

    @Test
    public void buildsFinsWriteResponse() {
        FinsMemoryWriteResponse finsMemoryWriteResponse = new FinsMemoryWriteResponse(destinationNode, sourceNode, serviceId);
        String finsResponse = finsMemoryWriteResponse.getFinsResponse();
        FinsResponseBuilder responseBuilder = new FinsResponseBuilder(new FinsResponseParser());
        Assert.assertEquals("Build wrong type of response", FinsMemoryWriteResponse.class, responseBuilder.buildFinsResponse(finsResponse).getClass());
    }

    @Test
    public void buildsFinsClockReadResponse() {
        FinsClockReadResponse finsClockReadResponse = new FinsClockReadResponse(destinationNode, sourceNode, serviceId, data);
        String finsResponse = finsClockReadResponse.getFinsResponse();
        FinsResponseBuilder responseBuilder = new FinsResponseBuilder(new FinsResponseParser());
        Assert.assertEquals("Build wrong type of response", FinsClockReadResponse.class, responseBuilder.buildFinsResponse(finsResponse).getClass());
    }

    @Test(expected = UnknownResponseException.class)
    public void throwsUnknownResponseExceptionAttemptingToBuildFinsResponse() {
        String finsResponse = "F";
        FinsResponseBuilder responseBuilder = new FinsResponseBuilder(new FinsResponseParser());
        responseBuilder.buildFinsResponse(finsResponse);
    }
}
