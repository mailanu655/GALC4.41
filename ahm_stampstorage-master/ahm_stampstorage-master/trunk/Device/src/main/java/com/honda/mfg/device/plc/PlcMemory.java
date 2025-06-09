package com.honda.mfg.device.plc;

/**
 * User: Jeffrey M Lutz
 * Date: Feb 1, 2011
 */
public interface PlcMemory {
    public int getBankCode();

    public int getBeginningLocation();

    public int getMemorySize();
}
