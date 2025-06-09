package com.honda.mfg.device.rfid;

/**
 * User: Jeffrey M Lutz
 * Date: Dec 13, 2010
 */
public class RfidMemory {
    private int startAddress;
    private int wordLength;

    public RfidMemory(int startAddress, int wordLength) {
        this.startAddress = startAddress;
        this.wordLength = wordLength;
    }

    public int getStartAddress() {
        return startAddress;
    }

    public int getWordLength() {
        return wordLength;
    }
}
