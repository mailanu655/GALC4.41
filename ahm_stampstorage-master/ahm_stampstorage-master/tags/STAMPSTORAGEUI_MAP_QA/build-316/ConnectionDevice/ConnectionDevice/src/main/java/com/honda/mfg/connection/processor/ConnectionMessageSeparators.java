package com.honda.mfg.connection.processor;

/**
 * User: Jeffrey M Lutz
 * Date: 5/24/11
 */
public enum ConnectionMessageSeparators {
    DEFAULT("{END_OF_MESSAGE}"), ASTERISK("*");
    private String separator;

    private ConnectionMessageSeparators(String separator) {
        this.separator = separator;
    }

    public String getSeparator() {
        return separator;
    }
}
