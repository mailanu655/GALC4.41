package com.honda.mfg.device.plc;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 9, 2010
 * Time: 7:51:30 AM
 */
public interface PlcDevice {

    public String readMemory(PlcMemory memory);

    public void writeMemory(PlcMemory memory, String data);

    public String readClock();
}
