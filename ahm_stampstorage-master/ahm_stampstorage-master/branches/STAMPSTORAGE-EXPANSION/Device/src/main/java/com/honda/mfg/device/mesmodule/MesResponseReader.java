package com.honda.mfg.device.mesmodule;

import com.honda.mfg.device.exceptions.CommunicationsException;
import com.honda.mfg.device.exceptions.IncompleteMesMessageException;
import com.honda.mfg.device.exceptions.ResponseTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * User: vcc30690
 * Date: 4/11/11
 */
public class MesResponseReader {
    private static final Logger LOG = LoggerFactory.getLogger(MesResponseReader.class);

    private static final int MAX_RESPONSE_TIMEOUT = 2 * 1000;

    private BufferedReader in;
    private String endOfMessageMarker;
    private MessageValidator msgValidator;

    public MesResponseReader(BufferedReader in) {
        this(in, MesMessageSeparators.DEFAULT.getSeparator());
    }

    public MesResponseReader(BufferedReader in, String endOfMessageMarker) {
        this.in = in;
        this.endOfMessageMarker = endOfMessageMarker;
        this.msgValidator = new MessageValidator();
    }

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
                    String cleanMsg;
                    try {
                        cleanMsg = cleanMessage(response.toString());
                        return cleanMsg;
                    } catch (IncompleteMesMessageException e) {
                        // LOG.debug(e.getLocalizedMessage());
                    } catch(Exception e){
                         e.printStackTrace();
                    }
                }
            } catch (IOException ex) {
                throw new CommunicationsException("Failed to receive a complete response ", ex);
            } catch(Exception e){
                  e.printStackTrace();
            }
        }
    }

    private String cleanMessage(String msg) {
        String cleanMsgWithEndOfMessageMarker = msgValidator.cleanMessage(msg);
        int endOfMessageMarkerIndex = cleanMsgWithEndOfMessageMarker.indexOf(endOfMessageMarker);
        if (endOfMessageMarkerIndex < 0) {
            throw new IncompleteMesMessageException("Unable to locate end of message marker.");
        }
        return cleanMsgWithEndOfMessageMarker.substring(0, endOfMessageMarkerIndex);
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
}
