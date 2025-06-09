package com.honda.mfg.stamp.conveyor.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 9/5/13
 * Time: 10:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class PlcAlarmMessage implements Message {

    private static final Logger LOG = LoggerFactory.getLogger(PlcAlarmMessage.class);

    StorageMessageType messageType = StorageMessageType.PLC_ALARM;
    String alarmNumber;
    String plcRegister;
    String description;


    public PlcAlarmMessage(String alarmNumber, String plcRegister, String description){
        this.alarmNumber = alarmNumber;
        this.plcRegister = plcRegister;
        this.description = description;
    }
}
