package com.honda.mfg.device.rfid.etherip.messages;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 16, 2010
 */
public abstract class RfidResponseBase extends RfidMessageBase implements RfidResponse {
    private int instanceCounter;
    private int nodeId;
    private Date responseDate;
    private String response;

    public RfidResponseBase(RfidCommand rfidCommand, int instanceCounter, int nodeId, Date responseDate) {
        if (instanceCounter < INSTANCE_COUNTER_MIN || instanceCounter > INSTANCE_COUNTER_MAX) {
            throw new IllegalArgumentException("Invalid instance counter!");
        }
        setRfidCommand(rfidCommand);
        this.instanceCounter = instanceCounter;
        this.nodeId = nodeId;
        this.responseDate = responseDate;
    }

    void initializeResponse() {
        StringBuilder b = new StringBuilder();
        // Start char and bogus length char
        b.append(getStart());
        // Command chars. Typically 0xAA,0x??
        b.append(getCommand());
        // Get Instance Counter
        b.append(getInstanceCounterString());
        // Get Node Id
        b.append(getNodeIdString());
        // Get Response Date String
        b.append(getResponseDateString());
        // Get Suffix
        b.append(getSuffix());
        // Update word length char at position #2
        setResponseLength(b);
        this.response = b.toString();
    }

    String getStart() {
        return new String(asChars(0, 2));
    }

    String getCommand() {
        StringBuffer b = new StringBuffer();
        b.append(asChars(170, 1));
        b.append(asChars(getRfidCommand().getCode(), 1));
        return b.toString();
    }

    abstract String getSuffix();

    void setResponseLength(StringBuilder sb) {
        sb.setCharAt(WORD_LENGTH_INDEX, (char) ((sb.length() / 2) + (sb.length() % 2)));
    }

    @Override
    public int getLengthInWords() {
        return response.charAt(WORD_LENGTH_INDEX);
    }

    @Override
    public String getRfidResponse() {
        return response;
    }

    @Override
    public int getInstanceCounter() {
        return instanceCounter;
    }

    @Override
    public int getNodeId() {
        return nodeId;
    }

    @Override
    public Date getResponseDate() {
        return responseDate;
    }

    String getInstanceCounterString() {
        return new String(asChars(getInstanceCounter(), 1));
    }

    String getNodeIdString() {
        return new String(asChars(getNodeId(), 1));
    }

    String getResponseDateString() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(responseDate);
        StringBuilder b = new StringBuilder();
        b.append(asChars(cal.get(Calendar.MONTH) + 1, 1));      // 0x0B
        b.append(asChars(cal.get(Calendar.DAY_OF_MONTH), 1));   // 0x09
        b.append(asChars(cal.get(Calendar.HOUR_OF_DAY), 1));    // 0x0C
        b.append(asChars(cal.get(Calendar.MINUTE), 1));         // 0x2A
        b.append(asChars(cal.get(Calendar.SECOND), 1));
        return b.toString();
    }

}
