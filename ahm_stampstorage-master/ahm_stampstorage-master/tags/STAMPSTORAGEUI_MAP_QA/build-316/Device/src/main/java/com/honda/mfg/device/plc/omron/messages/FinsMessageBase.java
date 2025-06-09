package com.honda.mfg.device.plc.omron.messages;

import com.honda.mfg.device.messages.MessageBase;

/**
 * Created by IntelliJ IDEA.
 * User: Mihail Chirita
 * Date: Sep 28, 2010
 * Time: 1:28:39 PM
 */
public abstract class FinsMessageBase extends MessageBase {

    protected static final String FINS = "FINS";
    protected static final int TCP_HEADER_COMMAND = 2;
    protected static final int ERROR_CODE = 0;
    protected static final int TCP_HEADER_COMMAND_LEGTH = 4;
    protected static final int ERROR_CODE_LENGTH = 4;
    protected static final int FINS_MESSAGE_LENGTH = 4;
    protected static final int ICF_REQUEST = 128;
    protected static final int ICF_RESPONSE = 192;
    protected static final int RSV = 0;
    protected static final int GCT = 2;
    protected static final int DNA = 0;
    protected static final int DA2 = 0;
    protected static final int SNA = 0;
    protected static final int SA2 = 0;
    protected static final int DONT_KNOW = 0;

    protected static final int END_CODE = 0;

    public static final String NO_DATA = "";
    public static final int NO_SERVICE_ID = 0;
    public static final int NO_SUBREQUEST_CODE = 0;

    private String finsMessage;

    private int sourceNode;
    private int destinationNode;
    private int serviceId;
    private FinsCommand finsCommand;

    protected FinsMessageBase(int destinationNode, int sourceNode, int serviceId, FinsCommand finsCommand) {
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
        this.serviceId = serviceId;
        this.finsCommand = finsCommand;
    }

    public FinsCommand getFinsCommand() {
        return finsCommand;
    }

    public int getSourceNode() {
        return sourceNode;
    }

    public int getDestinationNode() {
        return destinationNode;
    }

    public int getServiceId() {
        return serviceId;
    }

    protected void setFinsMessage(String finsMessage) {
        this.finsMessage = finsMessage;
    }

    protected String getFinsMessage() {
        return finsMessage;
    }
}
