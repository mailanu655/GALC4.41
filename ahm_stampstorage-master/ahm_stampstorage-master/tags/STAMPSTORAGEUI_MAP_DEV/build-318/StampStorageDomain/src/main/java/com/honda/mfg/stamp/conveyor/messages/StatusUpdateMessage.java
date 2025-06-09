package com.honda.mfg.stamp.conveyor.messages;

import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 2/13/12
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class StatusUpdateMessage implements Message {
    private static final Logger LOG = LoggerFactory.getLogger(StatusUpdateMessage.class);
    StorageMessageType messageType = StorageMessageType.STATUS_UPDATE;

    String status;
    String carrierNumbers;
    String source;

    public StorageMessageType getMessageType() {
        return messageType;
    }

    public StatusUpdateMessage(CarrierStatus carrierStatus, List<Integer> carriers, String user) {

        String str = carrierStatus == null ? "" : "" + carrierStatus.type();
        this.status = str;

        this.source = user == null ? "NOT_SET" : user;

        String nos = carriers!= null && carriers.size() > 0 ? carriers.get(0).intValue() + "," : "0,";
        String temp = "";
        for (int i = 1; i < 9; i++) {
            temp = carriers!= null && carriers.size() > i ? carriers.get(i).intValue() + "," : "0,";
            nos = nos + temp;
        }
        temp = carriers!= null && carriers.size() > 9 ? carriers.get(9).intValue() + "" : "0";
        nos = nos + temp;

        this.carrierNumbers = nos;
    }
}
