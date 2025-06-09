package com.honda.mfg.device.plc.omron.messages;

import com.honda.mfg.device.messages.MessageResponse;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 20, 2010
 * Time: 3:23:42 PM
 */
public interface FinsResponse extends MessageResponse {

    public String getFinsResponse();

    public FinsCommand getFinsCommand();

    public int getSourceNode();

    public int getDestinationNode();

    public int getServiceId();

    public String getData();
}
