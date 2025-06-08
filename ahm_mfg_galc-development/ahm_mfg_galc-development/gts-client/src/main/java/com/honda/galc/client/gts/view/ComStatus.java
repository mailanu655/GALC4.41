package com.honda.galc.client.gts.view;

import java.sql.Timestamp;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ComStatus</code> is ...
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
 * <TD>Mar 14, 2008</TD>
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

public class ComStatus {
    
    String name = "";
    
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    
    boolean isOK = true;
    
    int expiredTime = 1000;
    
    
    public ComStatus(String name){
        this.name = name;
    }
    
    public ComStatus(String name, int time){
        this.name = name;
        this.expiredTime = time;
    }
    
    public boolean isOk() {
        
        return isOK;

    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

     public int getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(int expiredTime) {
        this.expiredTime = expiredTime;
    }
    
    public boolean isHeartbeatLost(){
        
        isOK = System.currentTimeMillis() - timestamp.getTime() < expiredTime;
        System.out.println("name=" + name + ",time = " + (System.currentTimeMillis() - timestamp.getTime()));

        return !isOK;
    }
    
    public void heatbeatReceived(){
        
        timestamp = new Timestamp(System.currentTimeMillis());
        
    }
    
}
