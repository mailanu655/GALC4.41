package com.honda.mfg.device.plc.omron;

import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.ResponseTimeoutException;
import com.honda.mfg.device.plc.omron.exceptions.FinsHeaderException;
import com.honda.mfg.device.plc.omron.messages.FinsMemoryReadResponse;
import com.honda.mfg.device.plc.omron.messages.FinsResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FinsResponseReaderTest {

    private int sourceNode = 100;
    private int destNode = 101;
    private int serviceId = 1;
    private FinsResponse readResponse = new FinsMemoryReadResponse(sourceNode, destNode, serviceId, "somedata");

    @Test
    public void createsCompleteResponseWhenNoGarbageAtTheBeginningOrEnd() {
        assertExpectedResponse(readResponse.getFinsResponse());
    }

    @Test
    public void createsCompleteResponseWhenDataContainsFINS() {
        readResponse = new FinsMemoryReadResponse(sourceNode, destNode, serviceId, "FINS");
        assertExpectedResponse(readResponse.getFinsResponse());
    }

    @Test(expected = ResponseTimeoutException.class)
    public void timesOutWhenResponseIsIncomplete() {
        readResponse = new FinsMemoryReadResponse(sourceNode, destNode, serviceId, "FINS");
        String response = readResponse.getFinsResponse();
        String incompleteResponse = response.substring(0, response.length() - 2);
        getResponse(incompleteResponse);
    }

    @Test
    public void ignoresRandomCharactersBeforeResponseStarts() {
        String message = "abc" + readResponse.getFinsResponse();
        assertExpectedResponse(message);
    }

    @Test
    public void ignoresRandomCharactersAfterResponseEnds() {
        String message = readResponse.getFinsResponse() + "FINS123";
        assertExpectedResponse(message);
    }

    @Test
    public void ignoresRandomCharactersBeforeAndAfterResponse() {
        String message = "abc" + readResponse.getFinsResponse() + "FINS123";
        assertExpectedResponse(message);
    }

    @Test(expected = FinsHeaderException.class)
    public void throwsExceptionForFINSCharactersBeforeResponseStarts() {
        String message = "FINS" + readResponse.getFinsResponse();
        getResponse(message);
    }

    @Test(expected = CommunicationsException.class)
    public void throwsExceptionWhenThereIsAnInputStreamProblem() throws IOException {
        BufferedReader mockedBufferedReader = mock(BufferedReader.class);
        when(mockedBufferedReader.ready()).thenThrow(new IOException("Mocked IO Exception"));
        getResponse(mockedBufferedReader);
    }

    private void assertExpectedResponse(String message) {
        Assert.assertEquals(readResponse.getFinsResponse(), getResponse(message));
    }

    private String getResponse(String incompleteResponse) {
        BufferedReader reader = new BufferedReader(new StringReader(incompleteResponse));
        return getResponse(reader);
    }

    private String getResponse(BufferedReader reader) {
        FinsResponseReader finsReader = new FinsResponseReader(reader);
        return finsReader.getResponse();
    }


}
