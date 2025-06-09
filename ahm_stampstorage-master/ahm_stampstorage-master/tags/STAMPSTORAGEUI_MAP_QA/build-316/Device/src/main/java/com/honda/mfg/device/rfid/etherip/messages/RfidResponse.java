package com.honda.mfg.device.rfid.etherip.messages;

import com.honda.mfg.device.messages.MessageResponse;

import java.util.Date;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 10, 2010
 */
public interface RfidResponse extends MessageResponse {
    public String getRfidResponse();

    public int getLengthInWords();

    public int getInstanceCounter();

    public int getNodeId();

    public Date getResponseDate();

    public RfidCommand getRfidCommand();
}
