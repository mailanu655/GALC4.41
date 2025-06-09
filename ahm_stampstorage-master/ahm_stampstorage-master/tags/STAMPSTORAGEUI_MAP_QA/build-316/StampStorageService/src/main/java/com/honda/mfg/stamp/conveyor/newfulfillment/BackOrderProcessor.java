package com.honda.mfg.stamp.conveyor.newfulfillment;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.*;
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
 * Created by IntelliJ IDEA. 
 * User: Ambica Gawarla
 * Date: 6/27/13
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class BackOrderProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(BackOrderProcessor.class);

    private Storage storage;
    private NewFulfillmentHelper helper;
    private Message message;
    private String deliveryCycleSize;
    private Boolean clearBackOrder;


    public BackOrderProcessor(Storage storage, NewFulfillmentHelper helper, String deliveryCycleSize) {
        AnnotationProcessor.process(this);
        String name = Thread.currentThread().getName();
        LOG.trace("STORAGE THREAD NAME: " + name);
        this.storage = storage;
        this.helper = helper;
        this.deliveryCycleSize = deliveryCycleSize;
        this.clearBackOrder = false;
    }

    public BackOrderProcessor() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * Clears backorder if enough carriers in the queue or sends update message if not enough available
     */
    @EventSubscriber(eventClass = CarrierStatusMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void carrierStatusMessageListener(CarrierStatusMessage statusMessage) {
        message = statusMessage;
        try {
            LOG.info(" received status Message" + statusMessage.getCarrierNumber());

            Stop currentStop = statusMessage.getCurrentLocationAsStop();
            Stop destinationStop = statusMessage.getDestinationAsStop();
            if (currentStop != null && destinationStop != null) {
                if (currentStop.getStopType().equals(StopType.STORE_IN_ALL_LANES) && destinationStop.getStopType().equals(StopType.STORE_IN_ALL_LANES)) {
                    Carrier carrier = statusMessage.getCarrier();


                    if (storage.getStorageState().isCarrierPartsOnBackOrder(carrier.getDie())) {

                        if (carrier.getCarrierStatus().equals(CarrierStatus.ON_HOLD)) {
                            LOG.info(" Storing Carrier as carrier not shippable" + statusMessage.getCarrierNumber());
                            storage.store(carrier);
                        } else {
                            LOG.info(" Queuing Carrier" + statusMessage.getCarrierNumber());

                            List<WeldOrder> orders = helper.getActiveFulfillmentOrders();


                            for (WeldOrder order : orders) {
                                Integer cycle = OrderFulfillment.getMaxCycleCountForOrder(order) + 1;
                                if (statusMessage.getDie().equals(order.getLeftDie())) {
                                    if (helper.isSpaceAvailableInQueue(order.getLeftQueueStop())) {
                                        if (!helper.needMoreCarriersWithDie(order, order.getLeftDie()) || helper.canClearBackOrder(order.getLeftQueueStop(), order.getLeftDie(), deliveryCycleSize)) {
                                            LOG.info(" Have Enough Carriers in Queue clearing back order for -" + order.getLeftDie() +" - by OrderMgr"+order.getOrderMgr());
                                            storage.getStorageState().removeFromBackOrderList(order.getLeftDie(),order.getOrderMgr());

                                        } else {
                                            carrier.setDestination(order.getLeftQueueStop());
                                            helper.saveFulfillment(order, carrier, cycle, CarrierFulfillmentStatus.RELEASED);
                                            publishCarrierUpdate(carrier.getCarrierNumber(), order.getLeftQueueStop());
                                            break;
                                        }

                                    } else {
                                        LOG.info(" No Space in Queue clearing back order for -" + order.getLeftDie()+" - by OrderMgr"+order.getOrderMgr());
                                        storage.getStorageState().removeFromBackOrderList(order.getLeftDie(),order.getOrderMgr());
                                    }

                                } else if (statusMessage.getDie().equals(order.getRightDie())) {
                                    if (helper.isSpaceAvailableInQueue(order.getRightQueueStop())) {

                                        if (helper.canClearBackOrder(order.getRightQueueStop(), order.getRightDie(), deliveryCycleSize) || !helper.needMoreCarriersWithDie(order, order.getRightDie())) {
                                            LOG.info(" Have Enough Carriers in Queue clearing back order for -" + order.getRightDie()+" - by OrderMgr"+order.getOrderMgr());
                                            storage.getStorageState().removeFromBackOrderList(order.getRightDie(),order.getOrderMgr());
                                        } else {
                                            carrier.setDestination(order.getRightQueueStop());
                                            helper.saveFulfillment(order, carrier, cycle, CarrierFulfillmentStatus.RELEASED);
                                            publishCarrierUpdate(carrier.getCarrierNumber(), order.getRightQueueStop());
                                            break;
                                        }

                                    } else {
                                        LOG.info(" No Space in Queue clearing back order for -" + order.getRightDie()+" - by OrderMgr"+order.getOrderMgr());
                                        storage.getStorageState().removeFromBackOrderList(order.getRightDie(),order.getOrderMgr());
                                    }
                                }

                            }

                            if(!storage.getStorageState().isCarrierPartsOnBackOrder(carrier.getDie())){
                                LOG.info(" No BackOrder Request Exists for Die - "+ carrier.getDie()+" so storing carrier -"+ carrier.getCarrierNumber());
                                 helper.resetAlarm(AlarmTypes.BACK_ORDER_ALARM.type());
                                 helper.resetAlarm(AlarmTypes.UNABLE_TO_UNBLOCK.type());
                                 storage.store(carrier);
                            }else{
                                LOG.info(storage.getStorageState().getBackOrder().toArray().toString());
                                 LOG.info(" BackOrder Request Exists for Die - "+ carrier.getDie()+" so Not storing carrier -"+ carrier.getCarrierNumber());
                            }
                        }
                    }else{
                        LOG.info("Carrier Not on BackOrder");
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void turnOffSubscriber() {
        AnnotationProcessor.unprocess(this);
    }

    void publishCarrierUpdate(Integer carrierNumber, Stop destination) {
        Carrier newCarrier = new Carrier();
        newCarrier.setCarrierNumber(carrierNumber);
        newCarrier.setDestination(destination);
        newCarrier.setSource(StorageConfig.OHCV_APP_BACKORDER_PROCESSOR);
        CarrierUpdateMessage carrierUpdateMessage = new CarrierUpdateMessage(newCarrier);
        EventBus.publish(carrierUpdateMessage);
    }

    public Message getMessage() {
        return message;
    }

}