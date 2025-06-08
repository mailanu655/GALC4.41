package com.honda.galc.client.device.plc.omron;

import com.honda.galc.client.device.plc.omron.exceptions.FinsHeaderException;

/**
 * Not to be confused with FINS/TCP header. This object only holds
 * FINS string followed by expected messages length
 */
public class FinsHeaderPrefix {

    public static final String FINS_PREFIX = "FINS";
    public static final int HEADER_PREFIX_LENGTH = 8;
    public static final int MAX_DATA_LENGTH = 2000;

    public String getHeader() {
        return header;
    }

    private String header;

    public FinsHeaderPrefix(int messageLength) {
        assertValidMessageLengthRange(messageLength);
        header = "FINS" + new String(asChars(messageLength));
    }

    public FinsHeaderPrefix(String header) {
        this.header = header;
        assertValidHeaderLength();
        assertValidHeaderStart();
        int messageLength = getMessageLength();
        assertValidMessageLengthRange(messageLength);
    }

    private void assertValidMessageLengthRange(int messageLength) {
        if (messageLength > MAX_DATA_LENGTH) {
            throw new FinsHeaderException("Maximum messages length is" + MAX_DATA_LENGTH + ". Actual messages length " + messageLength);
        }
    }

    private void assertValidHeaderStart() {
        if (!header.startsWith(FINS_PREFIX)) {
            throw new FinsHeaderException("FINS messages has to start with " + FINS_PREFIX + ". Actual header " + header);
        }
    }

    private void assertValidHeaderLength() {
        if (header.length() != HEADER_PREFIX_LENGTH)
            throw new FinsHeaderException("Fins Header Prefix needs to have 8 characters. Actual length " + header.length());
    }


    public int getMessageLength() {
        String lengthString = header.substring(4);
        int messageLength = 0;
        for (int i = 0; i < lengthString.length(); i++) {
            messageLength = messageLength * 256 + lengthString.charAt(i);
        }
        return messageLength;
    }

    private char[] asChars(int length) {
        int remainder = length;
        char[] lengthChars = new char[4];
        for (int i = 0; i < lengthChars.length; i++) {
            lengthChars[3 - i] = (char) (remainder % 256);
            remainder = remainder / 256;
        }
        return lengthChars;
    }
}




