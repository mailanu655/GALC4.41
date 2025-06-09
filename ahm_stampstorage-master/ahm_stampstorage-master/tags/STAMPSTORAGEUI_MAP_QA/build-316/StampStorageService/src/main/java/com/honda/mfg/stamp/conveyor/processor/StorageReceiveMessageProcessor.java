package com.honda.mfg.stamp.conveyor.processor;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.Defect;
import com.honda.mfg.stamp.conveyor.domain.OrderFulfillment;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierFulfillmentStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import com.honda.mfg.stamp.conveyor.manager.Storage;
import com.honda.mfg.stamp.conveyor.manager.StorageConfig;
import com.honda.mfg.stamp.conveyor.messages.CarrierStatusMessage;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateMessage;
import com.honda.mfg.stamp.conveyor.messages.Message;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * User: vcc30690
 * Date: 4/28/11
 */
public class StorageReceiveMessageProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(StorageReceiveMessageProcessor.class);

    private Storage storage;
    private Message message;
    private int testMode;

    public StorageReceiveMessageProcessor(Storage storage) {
        AnnotationProcessor.process(this);
        String name = Thread.currentThread().getName();
        LOG.trace("STORAGE THREAD NAME: " + name);
        this.storage = storage;
        this.testMode = 0;
    }

    StorageReceiveMessageProcessor(Storage storage, int testMode) {
        AnnotationProcessor.process(this);
        String name = Thread.currentThread().getName();
        LOG.trace("STORAGE THREAD NAME: " + name);
        this.storage = storage;
        this.testMode = testMode;
    }


    @EventSubscriber(eventClass = CarrierStatusMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void carrierStatusMessageListener(CarrierStatusMessage statusMessage) {
        message = statusMessage;
        try {
            LOG.info(" received status Message" + statusMessage.getCarrierNumber());
            Carrier carrier = statusMessage.getCarrier();
            Stop currentStop = carrier.getCurrentLocation();
            Stop destinationStop = carrier.getDestination();
            if (currentStop != null && destinationStop != null) {
                if ((isStoreInStop(currentStop) && isStoreInStop(destinationStop)) ||(isRecircStop(currentStop) && isRecircStop(destinationStop))){

                    if (!storage.getStorageState().isCarrierPartsOnBackOrder(carrier.getDie())) {
                        LOG.info(" Storing Carrier" + statusMessage.getCarrierNumber());
                        storage.store(carrier);
                    } else {
                        LOG.info(" carrier " + statusMessage.getCarrierNumber() + " has back order parts" + statusMessage.getDie());

                    }

//                    else if (destinationStop.isRowStop()) {
////                        if (!storage.getStorageState().hadValidLaneDestination(carrier)) {
////                            LOG.info(" Carrier Has invalid Lane Destination  recalculating destination Storing Carrier" + statusMessage.getCarrierNumber());
////                            storage.store(carrier);
////                        }
//                    } else {
//                        if (currentStop.equals(carrier.getDestination())) {
//                            LOG.info(" Storing Carrier" + statusMessage.getCarrierNumber());
//                            if (!storage.getStorageState().isCarrierPartsOnBackOrder(carrier)) {
//                                storage.store(carrier);
//                            }
//                        }
//                    }
                } else if (currentStop.isRowStop() && destinationStop.isRowStop()) {
                    if (!currentStop.equals(destinationStop)) {
                        LOG.info("Carrier " + statusMessage.getCarrierNumber() + " moved in to wrong lane: currentLocation = " + currentStop.getId() + " destination=" + destinationStop.getId());
                        LOG.info("Reloading storage state");
                        storage.reloadStorageState();
                        //StaleDataMessage staleDataMessage = new StaleDataMessage(false);
                        //EventBus.publish(staleDataMessage);
                    } else {
                        LOG.info("Carrier " + statusMessage.getCarrierNumber() + " moved in to lane: currentLocation = " + currentStop.getId() + " destination=" + destinationStop.getId());
                        storage.getStorageState().reorderCarriersInRow(destinationStop.getId());
                    }


                } else if ((currentStop.getStopType().equals(StopType.RELEASE_CHECK) || currentStop.getStopType().equals(StopType.EMPTY_CARRIER_DELIVERY))) {
                    LOG.info("Carrier " + statusMessage.getCarrierNumber() + " moved out of lane: currentLocation = " + currentStop.getId() + " destination=" + destinationStop.getId());

                    if (!destinationStop.isRowStop()) {

                        if (storage.getStorageState().carrierExistsInStorageState(carrier)) {
                            LOG.info("Removing Carrier: " + carrier.getCarrierNumber() + " from storage state ");
                            storage.getStorageState().removeCarrierFromStorageState(carrier);
                        }
                    } else {
                        storage.getStorageState().reorderCarriersInRow(destinationStop.getId());
                    }

                } else if (destinationStop.getStopType().equals(StopType.FULL_CARRIER_DELIVERY) && currentStop.getStopType().equals(StopType.FULL_CARRIER_DELIVERY)) {
                    if (currentStop.getStopArea().equals(StopArea.WELD_LINE_1)) {
                        LOG.info("Moving Carrier " + carrier.getCarrierNumber() + " with currentLocation " + currentStop.getDescription() + " and destination " + destinationStop.getDescription() + " to weldline 1 robotStop 3700");
                        publishCarrierUpdate(carrier.getCarrierNumber(), getConsumptionStopByStopArea(StopArea.WELD_LINE_1));
                    } else if (currentStop.getStopArea().equals(StopArea.WELD_LINE_2)) {
                        LOG.info("Moving Carrier " + carrier.getCarrierNumber() + " with currentLocation " + currentStop.getDescription() + " and destination " + destinationStop.getDescription() + " to weldline 2 robotStop 10900");
                        publishCarrierUpdate(carrier.getCarrierNumber(), getConsumptionStopByStopArea(StopArea.WELD_LINE_2));
                    }
//                    else if (currentStop.getStopArea().equals(StopArea.KD_LINE)) {
//                        LOG.info("Moving Carrier " + carrier.getCarrierNumber() + " with currentLocation " + currentStop.getDescription() + " and destination " + destinationStop.getDescription() + " to KD consumption Stop 800");
//                        publishCarrierUpdate(carrier.getCarrierNumber(), getConsumptionStopByStopArea(StopArea.KD_LINE));
//                    }
                    markCarrierConsumed(carrier);
                } else if (currentStop.getStopType().equals(StopType.FULL_CARRIER_CONSUMPTION)) {
                    removeDefects(carrier);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void markCarrierConsumed(Carrier carrier) {
        if (testMode == 0) {
            List<OrderFulfillment> orderFulfillments = OrderFulfillment.findAllNotConsumedOrderFulfillmentsByCarrier(carrier);

            for (OrderFulfillment orderFulfillment : orderFulfillments) {
                orderFulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.CONSUMED);
                orderFulfillment.merge();
            }
        }
    }

    private boolean isStoreInStop(Stop stop) {
        if (stop != null) {

            StopType type = stop.getStopType();

            if (type.equals(StopType.STORE_IN_ALL_LANES) || type.equals(StopType.STORE_IN_C_LOW_LANES) ||
                    type.equals(StopType.STORE_IN_C_HIGH_LANES)) {
                return true;
            }
        }
        return false;
    }

      private boolean isRecircStop(Stop stop) {
        if (stop != null) {

            StopType type = stop.getStopType();

            if (type.equals(StopType.RECIRC_TO_ALL_ROWS) ) {
                return true;
            }
        }
        return false;
    }


    void turnOffSubscriber() {
        AnnotationProcessor.unprocess(this);
    }

    Stop getConsumptionStopByStopArea(StopArea stopArea) {
        Stop stop = null;
        if (testMode == 0) {

            List<Stop> stops = Stop.findAllStopsByTypeAndArea(StopType.FULL_CARRIER_CONSUMPTION, stopArea);
            if (stops != null && stops.size() > 0) {
                return stops.get(0);
            }
        }

        return stop;
    }

    void removeDefects(Carrier carrier) {
        if (testMode == 0) {
            Integer carrierNumber = carrier.getCarrierNumber();
            Integer prodRunNumber = carrier.getProductionRunNo();
            Defect.removeDefectsByCarrierNumberAndProductionRunNo(carrierNumber, prodRunNumber);
        }
    }

    void publishCarrierUpdate(Integer carrierNumber, Stop destination) {
    	//TODO:publish destination update.
        Carrier newCarrier = new Carrier();
        newCarrier.setCarrierNumber(carrierNumber);
        newCarrier.setDestination(destination);
        newCarrier.setSource(StorageConfig.OHCV_APP_STORAGE_PROCESSOR);
        CarrierUpdateMessage carrierUpdateMessage = new CarrierUpdateMessage(newCarrier);
        EventBus.publish(carrierUpdateMessage);
    }

    public Message getMessage() {
        return message;
    }
}
