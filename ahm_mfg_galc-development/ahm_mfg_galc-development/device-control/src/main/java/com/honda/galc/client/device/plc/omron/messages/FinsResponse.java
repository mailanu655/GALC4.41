package com.honda.galc.client.device.plc.omron.messages;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 20, 2010
 * Time: 3:23:42 PM
 */
public interface FinsResponse extends MessageResponse {

    public StringBuilder getFinsResponse();

    public FinsCommand getFinsCommand();

    public int getSourceNode();

    public int getDestinationNode();

    public int getServiceId();

    public StringBuilder getData();
}
