package com.honda.mfg.device.rfid.etherip;

import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.ResponseTimeoutException;
import com.honda.mfg.device.rfid.etherip.messages.RfidClearPendingResponsesResponse;
import com.honda.mfg.device.rfid.etherip.messages.RfidResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RfidResponseReaderTest {

    private RfidResponse rfidResponse = new RfidClearPendingResponsesResponse(1, 1, new Date());
    private BufferedReader reader;

    @Before
    public void after() {
        reader = null;
    }

    @Test
    public void createsCompleteResponseWhenNoGarbageAtTheBeginningOrEnd() {
        RfidResponse clearPendingResponsesResponse = new RfidClearPendingResponsesResponse(1, 1, new Date());
        assertExpectedResponse(clearPendingResponsesResponse.getRfidResponse());
    }

    @Test(expected = ResponseTimeoutException.class)
    public void timesOutWhenResponseIsIncomplete() {
        String response = rfidResponse.getRfidResponse();
        String incompleteResponse = response.substring(0, response.length() - 2);
        getResponse(incompleteResponse);
    }

    @Test
    public void ignoresRandomCharactersBeforeResponseStarts() {
        String message = "abc" + rfidResponse.getRfidResponse();
        assertExpectedResponse(message);
    }

    @Test
    public void ignoresRandomCharactersAfterResponseEnds() {
        String message = rfidResponse.getRfidResponse();
        assertExpectedResponse(message + message);
        assertExpectedResponse("");
    }

    @Test
    public void ignoresRandomCharactersBeforeAndAfterResponse() {
        String message = "abc" + rfidResponse.getRfidResponse() + "xyz";
        assertExpectedResponse(message);
    }

    @Test(expected = CommunicationsException.class)
    public void throwsExceptionWhenThereIsAnInputStreamProblem() throws IOException {
        BufferedReader mockedBufferedReader = mock(BufferedReader.class);
        when(mockedBufferedReader.ready()).thenThrow(new IOException("Mocked IO Exception"));
        getResponse(mockedBufferedReader);
    }

    private void assertExpectedResponse(String message) {
        String response = getResponse(message);
        Assert.assertEquals(rfidResponse.getRfidResponse(), response);
    }

    private String getResponse(String incompleteResponse) {
        if (reader == null) {
            reader = new BufferedReader(new StringReader(incompleteResponse));
        }
        return getResponse(reader);
    }

    private String getResponse(BufferedReader reader) {
        RfidResponseReader rfidResponseReader = new RfidResponseReader(reader);
        return rfidResponseReader.getResponse();
    }
}
