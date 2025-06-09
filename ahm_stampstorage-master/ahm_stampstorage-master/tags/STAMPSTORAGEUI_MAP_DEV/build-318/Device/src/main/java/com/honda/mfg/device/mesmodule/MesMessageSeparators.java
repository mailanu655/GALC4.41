package com.honda.mfg.device.mesmodule;

/**
 * User: Jeffrey M Lutz
 * Date: 5/24/11
 */
public enum MesMessageSeparators {
    DEFAULT("{END_OF_MESSAGE}"), ASTERISK("*");
    private String separator;

    private MesMessageSeparators(String separator) {
        this.separator = separator;
    }

    public String getSeparator() {
        return separator;
    }
}
