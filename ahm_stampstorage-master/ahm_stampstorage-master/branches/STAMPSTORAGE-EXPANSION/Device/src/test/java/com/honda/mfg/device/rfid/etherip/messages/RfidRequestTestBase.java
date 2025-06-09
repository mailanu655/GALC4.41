package com.honda.mfg.device.rfid.etherip.messages;

import com.honda.mfg.device.messages.MessageRequest;
import org.junit.Assert;

/**
 * User: Jeffrey M Lutz
 * Date: Dec 8, 2010
 */
public class RfidRequestTestBase {
    static final int NODE_ID_GOOD = 1;
    static final int NODE_ID_BAD = -1;
    static final int READ_TIMEOUT_GOOD = 1000;
    static final int READ_TIMEOUT_BAD = -1;
    static final int START_ADDRESS_GOOD = 0;
    static final int START_ADDRESS_BAD = -2;
    static final int WORD_SIZE_GOOD = 10;
    static final int WORD_SIZE_BAD = -2;
    static final String TAG_VALUE_GOOD = "HELLO";
    static final String TAG_VALUE_BAD = null;

    int getWordLengthDefinedInMessage(MessageRequest messageRequest) {
        String request = messageRequest.getMessageRequest();
        int msb = request.charAt(2) * 256;
        int lsb = request.charAt(3);
        return msb + lsb;
    }

    int getWordLengthActual(MessageRequest messageRequest) {
        String request = messageRequest.getMessageRequest();
        int len = request.length();
        return ((len / 2) + (len % 2)) - 1;
    }

    int getCommandCode(MessageRequest request) {
        String msg = request.getMessageRequest();
        return msg.charAt(5);
    }

    void assertRequestConditions(RfidCommand cmd, RfidRequestBase request) {
        Assert.assertEquals("Invalid word length", getWordLengthDefinedInMessage(request), getWordLengthActual(request));
        Assert.assertEquals("Wrong command", cmd.getCode(), getCommandCode(request));
    }
}
