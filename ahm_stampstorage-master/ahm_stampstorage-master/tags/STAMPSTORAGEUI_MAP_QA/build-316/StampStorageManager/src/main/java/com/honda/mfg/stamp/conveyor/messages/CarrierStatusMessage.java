package com.honda.mfg.stamp.conveyor.messages;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.Press;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: vcc30690
 * Date: 4/28/11
 */
public class CarrierStatusMessage implements Message {

    private DeviceMessageType messageType = DeviceMessageType.CARRIER_STATUS;


    private String buffer;
    private String carrierNumber;
    private String quantity;
    private String dieNumber;
    private String currentLocation;
    private String destination;
    private String status;
    private String originationLocation;
    private String id;
    private String productionRunNumber;
    private String productionRunTimestamp;
    private String tagID;
    private String updateDate;

    Carrier carrier;

    public CarrierStatusMessage() {
    }

    public DeviceMessageType getMessageType() {
        return messageType;
    }

    public String getCarrierNumber() {
        return carrierNumber;
    }

    public void setCarrierNumber(String carrierNumber) {
        this.carrierNumber = carrierNumber;
    }

    public Carrier getCarrier() {
        if (carrier == null) {
            Carrier carrier = new Carrier();
            carrier.setCarrierNumber(Integer.parseInt(carrierNumber));
            carrier.setQuantity(Integer.parseInt(quantity));
            Die die = Die.findDie(Long.parseLong(dieNumber));
            carrier.setDie(die);
            Stop currentStop = currentLocation != null ? Stop.findStop(Long.valueOf(currentLocation)) : null;
            carrier.setCurrentLocation(currentStop);
            Stop destinationStop = destination != null ? Stop.findStop(Long.valueOf(destination)) : null;
            carrier.setDestination(destinationStop);
            carrier.setCarrierStatus(CarrierStatus.findByType(Integer.parseInt(status)));
            carrier.setPress(Press.findByType(Integer.parseInt(originationLocation)));
            carrier.setProductionRunNo(Integer.parseInt(productionRunNumber));
            carrier.setBuffer(Integer.parseInt(buffer));

          //TODO:CHECK FOR MAINT BITS
            DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.sss");
            try {
                carrier.setStampingProductionRunTimestamp(new Date(formatter.parse(productionRunTimestamp).getTime()));
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


            return carrier;
        }

        return carrier;
    }

    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setDieNumber(String dieNumber) {
        this.dieNumber = dieNumber;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public Stop getCurrentLocationAsStop() {
       return  currentLocation != null ? Stop.findStop(Long.valueOf(currentLocation)) : null;
    }

      public Stop getDestinationAsStop() {
        return destination != null && destination.trim().length() > 0 ? Stop.findStop(Long.valueOf(destination)) : null;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOriginationLocation(String originationLocation) {
        this.originationLocation = originationLocation;
    }

    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }

    public void setProductionRunNo(String productionRunNo) {
        this.productionRunNumber = productionRunNo;
    }

    public void setStampingProductionRunTimestamp(String stampingProductionRunTimestamp) {
        this.productionRunTimestamp = stampingProductionRunTimestamp;
    }

    public void setTagID(String tagID) {
        this.tagID = tagID;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getBuffer(){
        return buffer;
    }

    public Die getDie(){
       return Die.findDie(Long.parseLong(dieNumber));
    }

    public CarrierStatus getCarrierStatus(){
        return CarrierStatus.findByType(Integer.parseInt(status));
    }

}
