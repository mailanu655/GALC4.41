package com.honda.mfg.device.plc.omron;

import com.honda.mfg.device.plc.PlcMemory;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 9, 2010
 * Time: 8:10:44 AM
 */
public class FinsMemory implements PlcMemory {

    public enum BANK {
        CIO(128), LR(128), HR(128), AR(128), TIMER(129), COUNTER(129),
        DM(130), E0(144), E1(145), E2(146), E3(147), E4(148), E5(149),
        E6(150), E7(151);

        private int code;

        BANK(int code) {
            this.code = code;
        }

        public int code() {
            return code;
        }
    }

    private BANK registerBank;
    // Must between 0 and 256 (0h - 0100h)
    private int beginningLocation;
    private int memorySize;

    public FinsMemory(BANK registerBank, int beginningLocation, int memorySize) {
        super();
        if (beginningLocation < 0 || beginningLocation > 65535) {
            throw new IllegalArgumentException("Invalid memory address:  " + beginningLocation + " --> Needs to be between 0 and 65535");
        }
        if (memorySize < 0 || memorySize > 256) {
            throw new IllegalArgumentException("Invalid memory size:  " + memorySize + " --> Needs to be between 0 and 256");
        }
        this.registerBank = registerBank;
        this.beginningLocation = beginningLocation;
        this.memorySize = memorySize;
    }

    public int getBankCode() {
        return registerBank.code();
    }

    public int getMemorySize() {
        return memorySize;
    }

    public int getBeginningLocation() {
        return beginningLocation;
    }

    public String toString() {
        return ("MEMORY:  " + registerBank.name() + ":" + getBeginningLocation() + "[" + getMemorySize() + "]");
    }
}
