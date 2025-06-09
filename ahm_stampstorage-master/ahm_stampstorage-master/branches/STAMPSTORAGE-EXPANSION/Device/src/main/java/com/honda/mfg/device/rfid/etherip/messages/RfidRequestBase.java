package com.honda.mfg.device.rfid.etherip.messages;

import com.honda.mfg.device.messages.MessageRequest;

/**
 * User: Jeffrey M Lutz
 * Date: Dec 3, 2010
 */
public abstract class RfidRequestBase extends RfidMessageBase implements MessageRequest {
    private static final int MAX_RFID_WRITE_VALUE_LENGTH = 112;

    private int nodeId;
    private int readTimeout;
    private int startAddress;

    private String request;

    RfidRequestBase(RfidCommand command, int nodeId, int readTimeout, int startAddress, String tagValue) {
        this(command, nodeId, readTimeout, startAddress);
        validateTagValue(tagValue);
        process(tagValue);
    }

    RfidRequestBase(RfidCommand command, int nodeId, int readTimeout, int startAddress, int wordSize) {
        this(command, nodeId, readTimeout, startAddress);
        validateWordSize(wordSize);
        process(wordSize);
    }

    RfidRequestBase(RfidCommand command, int nodeId, int readTimeout, int startAddress) {
        validateNodeId(nodeId);
        validateReadTimeout(readTimeout);
        validateStartAddress(startAddress);
        setRfidCommand(command);
        this.nodeId = nodeId;
        this.readTimeout = readTimeout;
        this.startAddress = startAddress;
    }

    private String parseTagValueToMaxLength(String tagValue) {
        int maxAllowableLength = MAX_RFID_WRITE_VALUE_LENGTH - startAddress;
        return tagValue == null || tagValue.length() <= maxAllowableLength ?
                tagValue : tagValue.substring(0, maxAllowableLength);
    }

    private void process(String tag) {
        String limitedTag = parseTagValueToMaxLength(tag);
        StringBuilder b = new StringBuilder();
        // Tag READ:  FF 01 00 06 AA 05 00 01 00 01 00 01 00 03
        b.append(getStart());                               // FF 01
        b.append(getDummyLength());                         // 00 00
        b.append(getCommandString());                       // AA ??
        b.append(getNodeIdString(nodeId));                  // 00 ??
        b.append(getReadTimeoutString(readTimeout));        // ?? ??
        b.append(getStartAddressString(startAddress));      // ?? ??
        b.append(getWordSizeString(limitedTag.length()));   // ?? ??
        b.append(limitedTag);                               // ?? ?? ?? ?? ?? ??
        updateMessageLength(b);
        request = b.toString();
    }

    private void process(int wordSize) {
        StringBuilder b = new StringBuilder();
        // Tag READ:  FF 01 00 06 AA 05 00 01 00 01 00 01 00 03
        b.append(getStart());                           // FF 01
        b.append(getDummyLength());                     // 00 00
        b.append(getCommandString());                   // AA ??
        b.append(getNodeIdString(nodeId));              // 00 ??
        b.append(getReadTimeoutString(readTimeout));    // ?? ??
        if (startAddress != START_ADDRESS_NOT_APPLICABLE) {
            b.append(getStartAddressString(startAddress));  // ?? ??
        }
        if (wordSize != WORD_SIZE_NOT_APPLICABLE) {
            b.append(getWordSizeString(wordSize));          // ?? ??
        }
        updateMessageLength(b);
        request = b.toString();
    }

    public String getMessageRequest() {
        return request;
    }

    void updateMessageLength(final StringBuilder sb) {
        if (sb.length() % 2 == 1) {
            sb.append(asChars(0, 1));
        }
        sb.setCharAt(3, (char) (((sb.length() / 2) + (sb.length() % 2)) - 1));
    }

    String getStart() {
        StringBuilder b = new StringBuilder();
        b.append(asChars(255, 1));
        b.append(asChars(1, 1));
        return b.toString();
    }

    String getDummyLength() {
        StringBuilder b = new StringBuilder();
        b.append(asChars(0, 2));
        return b.toString();
    }

    String getNodeIdString(int nodeId) {
        StringBuilder b = new StringBuilder();
        b.append(asChars(0, 1)); // This is always zero for all commands!
        b.append(asChars(nodeId, 1));
        return b.toString();
    }

    String getReadTimeoutString(int readTimeout) {
        StringBuilder b = new StringBuilder();
        b.append(asChars(readTimeout, 2));
        return b.toString();
    }

    String getStartAddressString(int startAddress) {
        StringBuilder b = new StringBuilder();
        b.append(asChars(startAddress, 2));
        return b.toString();
    }

    String getWordSizeString(int wordSize) {
        StringBuilder b = new StringBuilder();
        b.append(asChars(wordSize, 2));
        return b.toString();
    }

    private void validateNodeId(int nodeId) {
        if (nodeId < NODE_ID_MIN || nodeId > NODE_ID_MAX) {
            throw new IllegalArgumentException("Node ID must be between 0 and 255!");
        }
    }

    private void validateReadTimeout(int readTimeout) {
        if (readTimeout < READ_TIMEOUT_MIN || readTimeout > READ_TIMEOUT_MAX) {
            throw new IllegalArgumentException("Read timeout must be between " + READ_TIMEOUT_MIN + " and " + READ_TIMEOUT_MAX);
        }
    }

    private void validateStartAddress(int startAddress) {
        if ((startAddress < START_ADDRESS_MIN || startAddress > START_ADDRESS_MAX) && startAddress != START_ADDRESS_NOT_APPLICABLE) {
            throw new IllegalArgumentException("Start address must be between " + START_ADDRESS_MIN + " and " + START_ADDRESS_MAX);
        }
    }

    private void validateWordSize(int wordSize) {
        if ((wordSize < WORD_SIZE_MIN || wordSize > WORD_SIZE_MAX) && wordSize != WORD_SIZE_NOT_APPLICABLE) {
            throw new IllegalArgumentException("Word size must be between " + WORD_SIZE_MIN + " and " + WORD_SIZE_MAX);
        }
    }

    private void validateTagValue(String tagValue) {
        if (tagValue == null || tagValue.length() == 0) {
            throw new IllegalArgumentException("Tag value must not be NULL or empty");
        }
    }
}
