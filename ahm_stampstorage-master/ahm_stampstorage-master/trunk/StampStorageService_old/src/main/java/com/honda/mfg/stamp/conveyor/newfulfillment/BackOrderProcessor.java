package com.honda.mfg.stamp.conveyor.newfulfillment;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.OrderFulfillment;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.WeldOrder;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierFulfillmentStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus;
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


    public BackOrderProcessor(Storage storage, NewFulfillmentHelper helper, String deliveryCycleSize) {
        AnnotationProcessor.process(this);
        String name = Thread.currentThread().getName();
        LOG.trace("STORAGE THREAD NAME: " + name);
        this.storage = storage;
        this.helper = helper;
        this.deliveryCycleSize = deliveryCycleSize;
    }

/**Clears backorder if enough carriers in the queue or sends update message if not enough available */
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


                    if (storage.getStorageState().isCarrierPartsOnBackOrder(carrier)) {

                        if (carrier.getCarrierStatus().equals(CarrierStatus.ON_HOLD)) {
                            LOG.info(" Storing Carrier as carrier not shippable" + statusMessage.getCarrierNumber());
                            storage.store(carrier);
                        } else {
                            LOG.info(" Queuing Carrier" + statusMessage.getCarrierNumber());
                            List<WeldOrder> orders = helper.getWeldOrdersByOrderStatus(OrderStatus.InProcess);
                            List<WeldOrder> retrievingOrders = helper.getWeldOrdersByOrderStatus(OrderStatus.RetrievingCarriers);
                            orders.addAll(retrievingOrders);

                            for (WeldOrder order : orders) {
                                Integer cycle = OrderFulfillment.getMaxCycleCountForOrder(order) + 1;
                                if (statusMessage.getDie().equals(order.getLeftDie())) {
                                    if (helper.isSpaceAvailableInQueue(order.getLeftQueueStop())) {
                                        if (helper.canClearBackOrder(order.getLeftQueueStop(), order.getLeftDie(), deliveryCycleSize) || !helper.needMoreCarriersWithDie(order, order.getLeftDie())) {
                                            LOG.info(" Have Enough Carriers in Queue clearing back order for -" + order.getLeftDie());
                                            storage.getStorageState().getBackOrderedParts().remove(order.getLeftDie().getId());
                                            storage.store(carrier);
                                        } else {
                                            carrier.setDestination(order.getLeftQueueStop());
                                            helper.saveFulfillment(order, carrier, cycle, CarrierFulfillmentStatus.RELEASED);
                                            publishCarrierUpdate(carrier.getCarrierNumber(), order.getLeftQueueStop());
                                        }
                                        break;
                                    } else {
                                        LOG.info(" No Space in Queue clearing back order for -" + order.getLeftDie());
                                        storage.getStorageState().getBackOrderedParts().remove(order.getLeftDie().getId());
                                        storage.store(carrier);
                                    }

                                } else if (statusMessage.getDie().equals(order.getRightDie())) {
                                    if (helper.isSpaceAvailableInQueue(order.getRightQueueStop())) {

                                        if (helper.canClearBackOrder(order.getRightQueueStop(), order.getRightDie(), deliveryCycleSize)|| !helper.needMoreCarriersWithDie(order, order.getRightDie())) {
                                            LOG.info(" Have Enough Carriers in Queue clearing back order for -" + order.getRightDie());
                                            storage.getStorageState().getBackOrderedParts().remove(order.getRightDie().getId());
                                            storage.store(carrier);
                                        } else {
                                            carrier.setDestination(order.getRightQueueStop());
                                            helper.saveFulfillment(order, carrier, cycle, CarrierFulfillmentStatus.RELEASED);
                                            publishCarrierUpdate(carrier.getCarrierNumber(), order.getRightQueueStop());
                                        }

                                        break;
                                    } else {
                                        LOG.info(" No Space in Queue clearing back order for -" + order.getRightDie());
                                        storage.getStorageState().getBackOrderedParts().remove(order.getRightDie().getId());
                                        storage.store(carrier);
                                    }
                                }

                            }
                        }
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