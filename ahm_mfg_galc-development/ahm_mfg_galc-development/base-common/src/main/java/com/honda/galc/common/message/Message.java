package com.honda.galc.common.message;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>Message</code> is an object which contains message info.
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
 * <TD>Jeffray Huang</TD>
 * <TD>Mar 3, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 */

public class Message implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private MessageType type = MessageType.INFO;
    private String message;
    private String timestamp;
    
    public Message(MessageType type,String message){
        this.type = type;
        this.message = message;
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        this.timestamp = ts.toString();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    
    public static Message logWarning(String message){
        return new Message(MessageType.WARN,message);
    }
    
    public static Message logInformation(String message){
        return new Message(MessageType.INFO,message);
    }
    
    public static Message logEmergency(String message){
        return new Message(MessageType.EMERGENCY,message);
    }
    
    public static Message logError(String message){
        return new Message(MessageType.ERROR,message);
    }
    
  

}
