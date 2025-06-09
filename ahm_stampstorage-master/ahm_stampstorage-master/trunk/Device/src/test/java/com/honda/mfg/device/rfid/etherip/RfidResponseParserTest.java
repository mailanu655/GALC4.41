package com.honda.mfg.device.rfid.etherip;

import com.honda.mfg.device.messages.MessageBase;
import com.honda.mfg.device.rfid.etherip.messages.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 11, 2010
 * Time: 1:07:39 PM
 */
public class RfidResponseParserTest extends MessageBase {
    private RfidResponse response;
    private RfidResponseParser parser = new RfidResponseParser();


    @Test
    public void parsesUnknownCommand() {
        response = new RfidClearPendingResponsesResponse(1, 1, new Date());
        String responseString = "999999999999999999999999999999";
        RfidResponseParser parser = new RfidResponseParser();
        RfidCommand cmd = parser.getRfidCommand(responseString);
        assertEquals(RfidCommand.UNKNOWN, cmd);
    }

    @Test
    public void parsesInvalidTagValue() {
        response = new RfidClearPendingResponsesResponse(1, 1, new Date());
        String responseString = "999999999999999999999999999999";
        RfidResponseParser parser = new RfidResponseParser();
        String tagValue = parser.getTagValue(responseString);
        assertNull(tagValue);
    }

    @Test
    public void parsesEmptyTagValue() {
        response = new RfidReadTagResponse(1, 2, new Date(), "");
        String resp = response.getRfidResponse();
        resp = resp.substring(0, resp.length() - 1);
        RfidResponseParser parser = new RfidResponseParser();
        String tagValue = parser.getTagValue(resp);
        assertNull(tagValue);
    }

    @Test
    public void parsesInvalidTagId() {
        response = new RfidReadTagIdResponse(1, 2, new Date(), "");
        String resp = response.getRfidResponse();
        resp = resp.substring(0, resp.length() - 1);
        RfidResponseParser parser = new RfidResponseParser();
        String tagValue = parser.getTagIdString(resp);
        assertNull(tagValue);
    }

    @Test
    public void parsesInvalidDate() {
        String resp = "";
        RfidResponseParser parser = new RfidResponseParser();
        Date date = parser.parseDate(resp);
        assertNull(date);
    }

    @Test
    public void parsesRfidClearPendingResponsesResponse() {
        response = new RfidClearPendingResponsesResponse(1, 1, new Date());
        assertParsing(response);
    }

    @Test
    public void parsesRfidGetControllerConfigurationResponse() {
        response = new RfidGetControllerConfigurationResponse(1, 1, new Date());
        assertParsing(response);
    }

    @Test
    public void parsesRfidGetControllerInfoResponse() {
        response = new RfidGetControllerInfoResponse(1, 1, new Date());
        assertParsing(response);
    }

    @Test
    public void parsesValidRfidReadTagIdResponse() {
        String expectedTagId = "0000000000000000";
        response = new RfidReadTagIdResponse(1, 1, new Date(), expectedTagId);
        RfidReadTagIdResponse rfidResponse = (RfidReadTagIdResponse) response;
        assertParsing(response);
        Assert.assertEquals("Invalid tags", expectedTagId, rfidResponse.getTagId());
        Assert.assertEquals("Wrong response command type!", RfidCommand.READ_TAG_ID, rfidResponse.getRfidCommand());
    }

    @Test
    public void parsesValidRfidReadTagIdResponseWithTwoByteValue() {
        String expectedTagId = "100";
        this.response = new RfidReadTagIdResponse(1, 1, new Date(), expectedTagId);
        RfidReadTagIdResponse rfidResponse = (RfidReadTagIdResponse) this.response;
        assertParsing(this.response);
        Assert.assertEquals("Invalid tags", expectedTagId, rfidResponse.getTagId());
        Assert.assertEquals("Wrong response command type!", RfidCommand.READ_TAG_ID, rfidResponse.getRfidCommand());
    }

    @Test
    public void parsesValidRfidReadTagIdResponseWithEightByteValue() {
        String expectedTagId = "FFFFFFFFFFFFFFFF";
        response = new RfidReadTagIdResponse(1, 1, new Date(), expectedTagId);
        RfidReadTagIdResponse rfidResponse = (RfidReadTagIdResponse) response;
        assertParsing(response);
        Assert.assertEquals("Invalid tags", expectedTagId, rfidResponse.getTagId());
        Assert.assertEquals("Wrong response command type!", RfidCommand.READ_TAG_ID, rfidResponse.getRfidCommand());
    }

    @Test
    public void parsesInvalidRfidReadTagIdResponseWithNullTags() {
        String expectedTagId = null;
        response = new RfidReadTagIdResponse(1, 1, new Date(), expectedTagId);
        RfidReadTagIdResponse rfidResponse = (RfidReadTagIdResponse) response;
        assertParsing(response);
        Assert.assertEquals("Invalid tags", expectedTagId, rfidResponse.getTagId());
        Assert.assertEquals("Wrong response command type!", RfidCommand.ERROR, rfidResponse.getRfidCommand());
    }

    @Test
    public void parsesInvalidRfidReadTagIdResponseWithTooLongTagId() {
        String expectedTagId = "12345678901234567";
        response = new RfidReadTagIdResponse(1, 1, new Date(), expectedTagId);
        RfidReadTagIdResponse rfidResponse = (RfidReadTagIdResponse) response;
        assertParsing(response);
        Assert.assertEquals("Invalid tags", expectedTagId, rfidResponse.getTagId());
        Assert.assertEquals("Wrong response command type!", RfidCommand.ERROR, rfidResponse.getRfidCommand());
    }

    @Test
    public void parsesValidDateRanges() {
        int MONTH = 12;
        int DAY = 31;
        Date date;
        StringBuilder r;
        Calendar cal = new GregorianCalendar();
        for (int hr = 0; hr < 24; hr++) {
            for (int min = 0; min < 60; min++) {
                for (int sec = 0; sec < 60; sec++) {
                    r = new StringBuilder();
                    r.append("123456");
                    r.append(((char) MONTH));
                    r.append(((char) DAY));
                    r.append(((char) hr));
                    r.append(((char) min));
                    r.append(((char) sec));
                    r.append(((char) 0));
                    r.append(((char) 0));
                    r.append(((char) 0));
                    r.append(((char) 0));
                    r.append(((char) 0));
                    r.append(((char) 0));
                    r.append(((char) 0));
                    date = parser.parseDate(r.toString());
                    cal.setTime(date);
                    Assert.assertEquals("Error parsing sec.", sec, cal.get(Calendar.SECOND));
                }
                Assert.assertEquals("Error parsing min.", min, cal.get(Calendar.MINUTE));
            }
            Assert.assertEquals("Error parsing hour.", hr, cal.get(Calendar.HOUR_OF_DAY));
        }
    }

    private void assertParsing(RfidResponse rfidResponse) {
        RfidResponseParser parser = new RfidResponseParser();
        String response = rfidResponse.getRfidResponse();
        Assert.assertEquals("Error parsing command", rfidResponse.getRfidCommand(), parser.getRfidCommand(response));
        Assert.assertEquals("Error parsing instance counter", rfidResponse.getInstanceCounter(), parser.getInstanceCounter(response));
        Assert.assertEquals("Error parsing node id", rfidResponse.getNodeId(), parser.getNodeId(response));
        Assert.assertEquals("Error parsing instance counter", rfidResponse.getInstanceCounter(), parser.getInstanceCounter(response));
        Assert.assertEquals("Error parsing date", rfidResponse.getResponseDate().toString(), parser.parseDate(response).toString());
        Assert.assertEquals("Error determining word length of response", rfidResponse.getLengthInWords(), getResponseStringLength(response));
    }

    private int getResponseStringLength(String response) {
        return (response.length() / 2) + (response.length() % 2);
    }
}
