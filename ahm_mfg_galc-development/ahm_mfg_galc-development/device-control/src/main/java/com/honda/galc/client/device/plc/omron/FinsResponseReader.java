package com.honda.galc.client.device.plc.omron;

import java.io.BufferedReader;
import java.io.IOException;

import com.honda.galc.client.device.exceptions.CommunicationsException;
import com.honda.galc.client.device.exceptions.ResponseTimeoutException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.util.StringUtil;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 22, 2010
 * Time: 3:40:35 PM
 */
public class FinsResponseReader {

    private static final String MESSAGE_START_STRING = FinsHeaderPrefix.FINS_PREFIX;
    private static final int HEADER_PREFIX_LENGTH = FinsHeaderPrefix.HEADER_PREFIX_LENGTH;
    private static final long GET_RESPONSE_SLEEP_TIME = 1L;

    private BufferedReader in;
    private Logger logger;
    
    public FinsResponseReader(BufferedReader in, Logger logger) {
        this.in = in;
        this.logger = logger;
    }

    /**
     * Returns a complete FINS response or times out if the response is not completed within the time out period.
     * The timeout period is specified by responseTimeout
     *
     * @return complete FINS Response
     */
    public StringBuilder getResponse(boolean logFinsTraffic, int responseTimeout) {
    	
        long endTime = System.currentTimeMillis() + responseTimeout;
        StringBuilder response = new StringBuilder();
        while (true) {
            try {
                if (in != null && in.ready()) {
                    response = readFromStream(response);
                    if (isMessageComplete(response)) {
                    	if (logFinsTraffic)
                    		getLogger().info("<<" + StringUtil.toHexString(response) + "<<");
                        return cleanedUpMessage(response);
                    }
                } else {
                    if (System.currentTimeMillis() > endTime) {
                		getLogger().warn("Did not receive FINS response before timout period: " + responseTimeout + " ms");
                    	throw new ResponseTimeoutException("Exceeded response timeout period " + responseTimeout + " ms");
                    }	
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

    private boolean containsMessageStartString(String message) {
        int length = message.length() < 5 ? message.length() : 4;
        return message.contains(MESSAGE_START_STRING.substring(0, length));
    }

    private boolean isMessageComplete(StringBuilder message) {
        if (!containsMessageStartString(message.toString())) return false;
        StringBuilder removedPrefix = cleanedUpMessage(message);
        return removedPrefix.length() >= HEADER_PREFIX_LENGTH && removedPrefix.length() == expectedMessageLength(removedPrefix);
    }

    private StringBuilder cleanedUpMessage(StringBuilder message) {
        int length = message.length() < 5 ? message.length() : 4;
        int startIndex = message.indexOf(MESSAGE_START_STRING.substring(0, length));
        int endIndex = message.length();
        return new StringBuilder(message.substring(startIndex, endIndex));
    }

    private int expectedMessageLength(StringBuilder message) {
        StringBuilder cleanedMessage = cleanedUpMessage(message);
        FinsHeaderPrefix finsPrefix = new FinsHeaderPrefix(cleanedMessage.substring(0, HEADER_PREFIX_LENGTH));
        return finsPrefix.getMessageLength() + HEADER_PREFIX_LENGTH;
    }
    
    private Logger getLogger() {
    	if (logger == null) {
    		logger = Logger.getLogger();
    	}
    	return logger;
    }
    
    public void setLogger(Logger logger) {
    	this.logger = logger;
    }
}
