package com.honda.galc.let.message;

import com.honda.galc.entity.product.LetMessage;
import com.honda.galc.entity.product.LetSpool;
import com.honda.galc.let.util.LetUtil;
import com.honda.galc.mdb.SocketMessageDrivenBean;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>RestLetMessageHandler</code> is ... .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Jul 17, 2019
 */
public class RestLetMessageHandler extends LetMessageHandler {

    private LetSpool letSpool;
    private String deviceIpAddress;
    private String textMessage;

    public RestLetMessageHandler(LetSpool letSpool, String deviceIpAddress, String textMessage) {
        this.letSpool = letSpool;
        this.deviceIpAddress = deviceIpAddress;
        this.textMessage = textMessage;
    }

    // === overrides === //
    @Override
    public void handleLetMessage() {
        getLogger().info("Entering RestLetMessageHandler.handleMessage()");

        long startTime = System.currentTimeMillis();
        StringBuilder recdStrB = new StringBuilder();
        LetProcessItem processItem = null;

        readMessage(recdStrB);

        if (recdStrB.length() > 1) {

            processItem = LetUtil.createProcessRequest(recdStrB, getNodeName(), getLetSpool());
            LetMessage letMsg = saveMessage(processItem); // save xml to LET_MESSAGE_TBX
            processItem.setMsgId(letMsg.getMessageId());
            if (processItem.isValid()) {
                LetUtil.addMsgToProcessQueue(processItem); // add msg to process queue
            }
            if (processItem.getUnitInTest() == null) {
                logNgMessage(processItem);
            }
            sendBroadcast(processItem, letMsg); // send a broadcast if configured
            logMsgHandlingDetails(startTime, processItem);
        }
        getLogger().info("Exiting RestLetMessageHandler.handleMessage()");
    }

    @Override
    protected void readMessage(StringBuilder recdStrB) {
        recdStrB.append(getTextMessage());
    }

    @Override
    protected LetSpool getLetSpool() {
        return letSpool;
    }

    @Override
    public String getNodeName() {
        return SocketMessageDrivenBean.getNodeName();
    }

    @Override
    protected String getSocketHostAddress() {
        return getDeviceIpAddress();
    }

    @Override
    public void sendReplyMessage(LetProcessItem item) {
    }

    @Override
    public void finalizeSocketMessage(StringBuilder recdStrB) {

    }

    // === get/set === //
    public String getDeviceIpAddress() {
        return deviceIpAddress;
    }

    public void setDeviceIpAddress(String deviceIpAddress) {
        this.deviceIpAddress = deviceIpAddress;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public void setLetSpool(LetSpool letSpool) {
        this.letSpool = letSpool;
    }
}
