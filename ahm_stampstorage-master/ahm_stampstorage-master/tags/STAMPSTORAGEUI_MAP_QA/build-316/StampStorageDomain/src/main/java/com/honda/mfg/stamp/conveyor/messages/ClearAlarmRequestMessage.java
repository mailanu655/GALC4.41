package com.honda.mfg.stamp.conveyor.messages;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 6/2/14
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClearAlarmRequestMessage extends ServiceRequest implements ServiceRequestMessage{

    /**
     * @param targetOp
     * @param user
     */
     String alarmNumber;
    public ClearAlarmRequestMessage(CarrierUpdateOperations targetOp, String user, String alarmNumber) {
        super(targetOp, user);
        this.alarmNumber = alarmNumber;
    }

    public String getAlarmNumber(){
        return alarmNumber;
    }
}
