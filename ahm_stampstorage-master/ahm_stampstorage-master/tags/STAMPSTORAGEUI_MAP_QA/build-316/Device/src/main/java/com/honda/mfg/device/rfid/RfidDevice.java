package com.honda.mfg.device.rfid;

/**
 * User: Jeffrey M Lutz
 * Date: Dec 7, 2010
 */
public interface RfidDevice {
    public String readTagId();

    public String readTag(RfidMemory memory);

    public void writeTag(RfidMemory memory, String tagValue);

    public String readControllerInfo();
}
