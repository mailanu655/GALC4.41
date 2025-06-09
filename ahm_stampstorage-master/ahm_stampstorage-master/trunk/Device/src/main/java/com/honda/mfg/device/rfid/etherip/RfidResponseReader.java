package com.honda.mfg.device.rfid.etherip;

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
public class RfidResponseReader {
    private static final Logger LOG = LoggerFactory.getLogger(RfidResponseReader.class);

    private static final int MAX_RESPONSE_TIMEOUT = 2 * 1000;
    private static final long GET_RESPONSE_SLEEP_TIME = 50L;

    private BufferedReader in;
    private static final int RFID_FIRST_CHAR_CONSTANT = 0;
    private static final int RFID_THIRD_CHAR_CONSTANT = 170;

    public RfidResponseReader(BufferedReader in) {
        this.in = in;
    }

    /**
     * Returns a complete EtherIP response or times out if the response is not completed within the time out period.
     * The timeout period is specified by MAX_RESPONSE_TIMEOUT
     *
     * @return complete EtherIP Response
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
                    String cleanMessage = cleanedUpMessage(response.toString());
                    if (isMessageComplete(cleanMessage)) {
                        return cleanMessage;
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

    private void checkForTimeout(long startTime, String partialResponse) {
        String timeoutMessage = "Timed out after " + MAX_RESPONSE_TIMEOUT +
                " ms. Response received so far:  \"" +
                partialResponse + "\"";
        long delta = System.currentTimeMillis() - startTime;
        if (delta > MAX_RESPONSE_TIMEOUT) {
            LOG.trace(timeoutMessage);
            throw new ResponseTimeoutException(timeoutMessage);
        }
    }

    private StringBuilder readFromStream(StringBuilder partialResponse) throws IOException {
        int chInt = in.read();
        if (!reachedEndOfStream(chInt)) {
            partialResponse.append((char) chInt);
        }
        return partialResponse;
    }

    private Boolean isMessageCompletePerMessageLength(String message) {
        int messageLength = expectedMessageLength(message);
        if (messageLength < RFID_FIRST_CHAR_CONSTANT || message == null || message.length() < messageLength) {
            return false;
        }
        return true;
    }

    private Boolean reachedEndOfStream(int character) {
        return character < RFID_FIRST_CHAR_CONSTANT;
    }

    private boolean containsMessageStartString(String message) {
        String cleanedMessage = cleanedUpMessage(message);
        return cleanedMessage.length() > 2;
    }

    private boolean isMessageComplete(String message) {
        return containsMessageStartString(message) && isMessageCompletePerMessageLength(message);
    }

    private String cleanedUpMessage(String message) {
        int length = message == null ? -1 : message.length();
        if (length < 3) {
            return message;
        }
        for (int i = 2; message != null && i < message.length(); i++) {
            char[] c = message.toCharArray();
            int firstChar = c[i - 2];
            int thirdChar = c[i];
            if (firstChar == RFID_FIRST_CHAR_CONSTANT && thirdChar == RFID_THIRD_CHAR_CONSTANT) {
                return new String(message.substring(i - 2));
            }
        }
        return "";
    }

    private int expectedMessageLength(String message) {
        if (message == null || message.length() < 2) {
            return -1;
        }
        int high = message.getBytes()[RFID_FIRST_CHAR_CONSTANT] * 256; // Shift high order byte by 8 bits.
        int low = message.getBytes()[1];
        return (high + low) * 2;  // byte count = Number of words times 2 bytes per word
    }
}
