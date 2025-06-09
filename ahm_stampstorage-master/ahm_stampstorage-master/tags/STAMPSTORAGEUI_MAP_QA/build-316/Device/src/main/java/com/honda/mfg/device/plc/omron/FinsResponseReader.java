package com.honda.mfg.device.plc.omron;

import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.ResponseTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 22, 2010
 * Time: 3:40:35 PM
 */
public class FinsResponseReader {
    private static final Logger LOG = LoggerFactory.getLogger(FinsResponseReader.class);

    private static final String MESSAGE_START_STRING = FinsHeaderPrefix.FINS_PREFIX;
    private static final int HEADER_PREFIX_LENGTH = FinsHeaderPrefix.HEADER_PREFIX_LENGTH;
    private static final int MAX_RESPONSE_TIMEOUT = 2 * 1000;
    private static final long GET_RESPONSE_SLEEP_TIME = 100L;

    private BufferedReader in;

    public FinsResponseReader(BufferedReader in) {
        this.in = in;
    }

    /**
     * Returns a complete FINS response or times out if the response is not completed within the time out period.
     * The timeout period is specified by MAX_RESPONSE_TIMEOUT
     *
     * @return complete FINS Response
     */
    public String getResponse() {
        StringBuilder response = new StringBuilder();
        long startTimeMs = System.currentTimeMillis();
        while (true) {
            checkForTimeout(startTimeMs, response.toString());
            try {
                if (in.ready()) {
                    int beforeLength = response.length();
                    response = readFromStream(response);
                    int afterLength = response.length();
                    if (afterLength > beforeLength) {
                        startTimeMs = System.currentTimeMillis();
                    }
                    if (isMessageComplete(response.toString())) {
                        return cleanedUpMessage(response.toString());
                    }
                } else {
                    try {
                        Thread.sleep(GET_RESPONSE_SLEEP_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException ex) {
                throw new CommunicationsException("Failed to receive a complete response ", ex);
            }
        }
    }

    private StringBuilder readFromStream(StringBuilder partialResponse) throws IOException {
        int chInt = in.read();
        if (!reachedEndOfStream(chInt)) {
            partialResponse.append((char) chInt);
        }
        return partialResponse;
    }

    private Boolean reachedEndOfStream(int character) {
        return character < 0;
    }

    private void checkForTimeout(long startTime, String partialResponse) {
        String timeoutMessage = "Timed out after " + MAX_RESPONSE_TIMEOUT +
                " ms. Response received so far:  \"" +
                partialResponse + "\"";
        long delta = System.currentTimeMillis() - startTime;
        if (delta > MAX_RESPONSE_TIMEOUT) {
            throw new ResponseTimeoutException(timeoutMessage);
        }
    }

    private boolean containsMessageStartString(String message) {
        int length = message.length() < 5 ? message.length() : 4;
        return message.contains(MESSAGE_START_STRING.substring(0, length));
    }

    private boolean isMessageComplete(String message) {
        if (!containsMessageStartString(message)) return false;
        String removedPrefix = cleanedUpMessage(message);
        return removedPrefix.length() >= HEADER_PREFIX_LENGTH && removedPrefix.length() == expectedMessageLength(removedPrefix);
    }

    private String cleanedUpMessage(String message) {
        int length = message.length() < 5 ? message.length() : 4;
        int startIndex = message.indexOf(MESSAGE_START_STRING.substring(0, length));
        int endIndex = message.length();
        return message.substring(startIndex, endIndex);
    }

    private int expectedMessageLength(String message) {
        String cleanedMessage = cleanedUpMessage(message);
        FinsHeaderPrefix finsPrefix = new FinsHeaderPrefix(cleanedMessage.substring(0, HEADER_PREFIX_LENGTH));
        return finsPrefix.getMessageLength() + HEADER_PREFIX_LENGTH;
    }
}
