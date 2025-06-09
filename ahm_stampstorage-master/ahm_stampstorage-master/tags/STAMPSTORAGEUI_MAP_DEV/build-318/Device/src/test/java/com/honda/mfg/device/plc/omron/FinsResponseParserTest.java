package com.honda.mfg.device.plc.omron;

import com.honda.mfg.device.plc.omron.messages.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Mihail Chirita
 * Date: Sep 24, 2010
 * Time: 1:07:39 PM
 */
public class FinsResponseParserTest {

    private int destinationNode = 100;
    private int sourceNode = 101;
    private int serviceId = 102;
    private String data = "testData";


    @Test
    public void parsesFinsInitializeResponse() {
        FinsInitializeResponse finsInitializeResponse = new FinsInitializeResponse(destinationNode, sourceNode);
        assertInitializeParsing(finsInitializeResponse);
    }

    @Test
    public void parsesFinsReadResponse() {
        FinsMemoryReadResponse finsMemoryReadResponse = new FinsMemoryReadResponse(destinationNode, sourceNode, serviceId, data);
        assertParsing(finsMemoryReadResponse);
    }

    @Test
    public void parsesFinsWriteResponse() {
        FinsMemoryWriteResponse finsMemoryWriteResponse = new FinsMemoryWriteResponse(destinationNode, sourceNode, serviceId);
        assertParsing(finsMemoryWriteResponse);
    }

    @Test
    public void parsesFinsClockResponse() {
        FinsClockReadResponse finsClockReadResponse;
        finsClockReadResponse = new FinsClockReadResponse(destinationNode, sourceNode, serviceId, data);
        assertParsing(finsClockReadResponse);
    }

    private void assertInitializeParsing(FinsResponse expectedResponse) {
        FinsResponseParser parser = new FinsResponseParser();
        String finsResponse = expectedResponse.getFinsResponse();
        FinsCommand cmd = parser.parseFinsCommand(finsResponse);
        Assert.assertEquals("Error parsing Initialize command", cmd, parser.parseFinsCommand(finsResponse));
        Assert.assertEquals("Error parsing source node", expectedResponse.getSourceNode(), parser.parseSourceNodeForInitialize(finsResponse));
        Assert.assertEquals("Error parsing destination node", expectedResponse.getDestinationNode(), parser.parseDestinationNodeForInitialize(finsResponse));
    }

    private void assertParsing(FinsResponse response) {
        FinsResponseParser parser = new FinsResponseParser();
        String finsResponse = response.getFinsResponse();
        FinsCommand cmd = parser.parseFinsCommand(finsResponse);
        Assert.assertEquals("Error parsing destination node", response.getDestinationNode(), parser.parseDestinationNode(finsResponse));
        Assert.assertEquals("Error parsing source node", response.getSourceNode(), parser.parseSourceNode(finsResponse));
        Assert.assertEquals("Error parsing request code", response.getFinsCommand().getRequestCode(), parser.parseRequestCode(finsResponse));
        Assert.assertEquals("Error parsing subrequest code", response.getFinsCommand().getSubrequestCode(), parser.parseSubrequestCode(finsResponse));
        Assert.assertEquals("Error parsing service id", response.getServiceId(), parser.parseServiceId(finsResponse));
        Assert.assertEquals("Error parsing data", response.getData(), parser.parseData(finsResponse));
    }
}
