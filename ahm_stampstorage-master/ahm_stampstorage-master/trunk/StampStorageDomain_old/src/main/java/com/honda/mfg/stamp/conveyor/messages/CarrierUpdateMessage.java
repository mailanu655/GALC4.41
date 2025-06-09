package com.honda.mfg.stamp.conveyor.messages;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

/**
 * User: Jeffrey M Lutz
 * Date: 5/18/11
 */
public class CarrierUpdateMessage implements Message {
    private static final Logger LOG = LoggerFactory.getLogger(CarrierUpdateMessage.class);
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");

    StorageMessageType messageType = StorageMessageType.CARRIER_UPDATE;
    String carrierNumber;
    String dieNumber;
    String quantity;
    String currentLocation;
    String destination;
    String status;
    String productionRunTimestamp;
    String productionRunNumber;

    String originationLocation;
    String tagID;
    String buffer;
    String reprocess;
    String updateDate;
    String source;

    StorageMessageType getMessageType() {
        return messageType;
    }


    public CarrierUpdateMessage(Carrier c) {
        Stop currentLoc = c.getCurrentLocation();
        Long currentLocId = currentLoc == null ? null : currentLoc.getId();

        LOG.debug("Creating CarrierUpdateMessage with Carrier: " + c);
        LOG.debug("Creating update msg for carrierNumber: " + c.getCarrierNumber());
        LOG.debug("currentLocationId: " + currentLocId);

        String str;
        this.carrierNumber = c.getCarrierNumber() == null ? "" : "" + c.getCarrierNumber();
        this.currentLocation = currentLocId == null ? "" : "" + currentLocId;
        str = c.getDestination() == null ? "" : "" + c.getDestination().getId();
        this.destination = str;
        str = c.getDie() == null ? "" : "" + c.getDie().getId();
        this.dieNumber = str;
        str = c.getQuantity() == null ? "" : "" + c.getQuantity();
        this.quantity = str;
        str = c.getCarrierStatus() == null ? "" : "" + c.getCarrierStatus().type();
        this.status = str;

        str = c.getStampingProductionRunTimestamp() == null ? "" : SDF.format(c.getStampingProductionRunTimestamp());
        this.productionRunTimestamp = str;
        this.productionRunNumber = c.getProductionRunNo() == null ? "" : "" + c.getProductionRunNo();
        this.buffer = c.getBuffer() == null ? "" : "" + c.getBuffer();

        if(c.getReprocess()){
            this.reprocess="1";
        }else{
            this.reprocess="0";
        }
        this.originationLocation = "";
        this.tagID = "";
        str = c.getUpdateDate() == null ? "" : SDF.format(c.getUpdateDate());

        this.updateDate =  str;

        str = c.getSource() == null ?"NOT_SET": c.getSource();
        this.source = str;
    }

    public String getCarrierNumber() {
        return carrierNumber;
    }

    public String getDestination() {
        return destination;
    }

}
