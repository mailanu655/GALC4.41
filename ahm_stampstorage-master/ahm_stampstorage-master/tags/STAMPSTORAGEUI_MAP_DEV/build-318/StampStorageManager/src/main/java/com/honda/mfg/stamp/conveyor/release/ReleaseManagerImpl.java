package com.honda.mfg.stamp.conveyor.release;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.CarrierRelease;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateMessage;
import org.bushe.swing.event.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: vcc30690
 * Date: 1/16/12
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReleaseManagerImpl implements ReleaseManager {
     private static final Logger LOG = LoggerFactory.getLogger(ReleaseManagerImpl.class);
     private ReleaseManagerHelper releaseManagerHelper;

    public ReleaseManagerImpl(ReleaseManagerHelper releaseManagerHelper) {
        this.releaseManagerHelper = releaseManagerHelper;
    }
    /**
     * If Carrier is at head of row (buffer=1), and no other carriers releasing from this row, Publish an update message.
     * @param carrierNumber
     * @param destination
     * @param source
     * @param releaseManager
     */
    public void releaseCarrier(Integer carrierNumber, Stop destination, String source, boolean releaseMgr) {
        CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(carrierNumber);
        Stop currentLocation = Stop.findStop(carrierMes.getCurrentLocation());
        if (releaseMgr) {
            if (carrierMes.getBuffer().equals(Integer.valueOf(1))) {
                  LOG.info("Carrier -"+ carrierNumber+" at head of row");
                if (releaseManagerHelper.anyCarriersReleasingInStorageArea(currentLocation)) {
                    LOG.info("Carriers Releasing in storage area saving to carrier release table");
                    saveToCarrierReleaseTable(carrierNumber, currentLocation,destination,source);
                } else {
                    LOG.info("No Carriers Releasing in storage area publishing carrier update for carrier-"+ carrierNumber);
                    //  creating a copy of carrier to make sure we send only carrier number and destination information in carrier update
                  //TODO:CHECK FOR MAINT BITS
                    Carrier newCarrier = new Carrier();
                    newCarrier.setCarrierNumber(carrierNumber);
                    newCarrier.setDestination(destination);
                    newCarrier.setSource(source);
                    CarrierUpdateMessage updateMessage = new CarrierUpdateMessage(newCarrier);
                    EventBus.publish(updateMessage);
                }
            } else {
                LOG.info("Carrier -"+ carrierNumber+"not at head of stop saving to carrier release table");
               saveToCarrierReleaseTable(carrierNumber, currentLocation, destination,source);
            }
        } else {
             LOG.info("Release Manager false publishing carrier update for carrier-"+ carrierNumber);
           //TODO:CHECK FOR MAINT BITS
            Carrier newCarrier = new Carrier();
            newCarrier.setCarrierNumber(carrierNumber);
            newCarrier.setDestination(destination);
            newCarrier.setSource(source);
            CarrierUpdateMessage updateMessage = new CarrierUpdateMessage(newCarrier);
            EventBus.publish(updateMessage);
        }
    }

    private void saveToCarrierReleaseTable(Integer carrierNumber,Stop currentLocation, Stop destination, String source){
           Long id = Long.parseLong(carrierNumber.toString());
           CarrierRelease.saveCarrierRelease(id, currentLocation, destination, source);
    }

}
