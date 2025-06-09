package com.honda.mfg.stamp.conveyor.newfulfillment;

import java.sql.Timestamp;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.OrderFulfillment;
import com.honda.mfg.stamp.conveyor.domain.OrderFulfillmentPk;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.WeldOrder;
import com.honda.mfg.stamp.conveyor.domain.enums.AlarmTypes;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierFulfillmentStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.CommentCategory;
import com.honda.mfg.stamp.conveyor.domain.enums.DieType;
import com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import com.honda.mfg.stamp.conveyor.manager.StorageConfig;
import com.honda.mfg.stamp.conveyor.messages.CarrierStatusMessage;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateMessage;

/**
 * Created by IntelliJ IDEA. User: vcc30690 Date: 11/16/11 Time: 5:04 PM To
 * change this template use File | Settings | File Templates.
 */
public class FulfillmentProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(FulfillmentProcessor.class);

	private Stop inspectionStop;
	private Integer carrierNumber;

	private NewFulfillmentHelper newFulfillmentHelper;

	public FulfillmentProcessor(NewFulfillmentHelper newFulfillmentHelper, String fulfillmentCarrierInspectionStop) {
		this.newFulfillmentHelper = newFulfillmentHelper;
		int value = this.newFulfillmentHelper.getParmValue(fulfillmentCarrierInspectionStop);
		inspectionStop = Stop.findStop(new Long(value));
		AnnotationProcessor.process(this);
	}

	/** Carrier at Release check stop and update carrier fulfillment status */
	@EventSubscriber(eventClass = CarrierStatusMessage.class, referenceStrength = ReferenceStrength.STRONG)
	public void carrierStatusMessageListener(CarrierStatusMessage statusMessage) {

		validateCarrierStatusMessage(statusMessage);
		Stop stop = statusMessage.getCurrentLocationAsStop();

		carrierNumber = Integer.parseInt(statusMessage.getCarrierNumber());
		String carrierText = "Carrier - " + carrierNumber;
		LOG.info(" Received Carrier status for carrier - " + carrierNumber);

		if (!newFulfillmentHelper.isEmpty(statusMessage.getDie())) {
			WeldOrder order = OrderFulfillment.findFulfillingOrderByCarrierForFulfillment(carrierNumber,
					statusMessage.getDie());
			Integer cycle = OrderFulfillment.getMaxCycleCountForOrderByCarrier(order, carrierNumber);
			if (order != null) {
				LOG.debug("carrier is part of Order - " + order.getId());
				if (!order.getOrderStatus().equals(OrderStatus.ManuallyCompleted)
						|| !order.getDeliveryStatus().equals(OrderStatus.ManuallyCompleted)) {
					LOG.debug("carrier is part of active Order - " + order.getId());
					OrderFulfillmentPk pk = new OrderFulfillmentPk(order, carrierNumber, cycle);
					OrderFulfillment fulfillment = OrderFulfillment.findOrderFulfillment(pk);

					if (fulfillment != null) {

						fulfillment.setCurrentLocation(stop);
						fulfillment.setUpdateDate(new Timestamp(System.currentTimeMillis()));

						if ((StopType.RELEASE_CHECK.equals(stop.getStopType())
								|| StopType.RECIRC_TO_ALL_ROWS.equals(stop.getStopType()))
								&& fulfillment.getCarrierFulfillmentStatus()
										.equals(CarrierFulfillmentStatus.RETRIEVED)) {
							LOG.info(carrierText + " Released ");

							fulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.RELEASED);
						}

						// 2013-06-13:VB: , removed references to ST13 and ST13-1, not needed for
						// expansion
						if (stop.getStopArea().equals(StopArea.Q_WELD_LINE_1)
								|| stop.getStopArea().equals(StopArea.Q_WELD_LINE_2)) {
							if (!fulfillment.isStatus(CarrierFulfillmentStatus.QUEUED)
									&& !(fulfillment.isStatus(CarrierFulfillmentStatus.READY_TO_DELIVER))
									&& !(fulfillment.isStatus(CarrierFulfillmentStatus.DELIVERED))
									&& !(fulfillment.isStatus(CarrierFulfillmentStatus.SELECTED_TO_DELIVER))) {
								fulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.QUEUED);
							}
						}

						fulfillment.merge();
					}
				}
			}
		}
	}

	@EventSubscriber(eventClass = CarrierStatusMessage.class, referenceStrength = ReferenceStrength.STRONG)
	public void deliveryCarrierStatusMessageListener(CarrierStatusMessage statusMessage) {

		validateCarrierStatusMessage(statusMessage);
		Stop stop = statusMessage.getCurrentLocationAsStop();
		Stop destination = statusMessage.getDestinationAsStop();
		carrierNumber = Integer.parseInt(statusMessage.getCarrierNumber());
		String carrierText = "Carrier - " + carrierNumber;
		LOG.info(" Received Carrier status for carrier - " + carrierNumber);

		if (stop != null && !newFulfillmentHelper.isEmpty(statusMessage.getDie()) && stop.equals(inspectionStop)) {
			LOG.info(" Inspection stop-" + inspectionStop.getId());

			WeldOrder order = OrderFulfillment.findDeliveringOrderByCarrierForFulfillment(carrierNumber,
					statusMessage.getDie());
			Integer cycle = OrderFulfillment.getMaxCycleCountForOrderByCarrier(order, carrierNumber);
			if (order != null) {
				LOG.debug("carrier is part of Order - " + order.getId());
				if (!order.getDeliveryStatus().equals(OrderStatus.ManuallyCompleted)) {
					LOG.debug("carrier is part of active Order - " + order.getId());
					OrderFulfillmentPk pk = new OrderFulfillmentPk(order, carrierNumber, cycle);
					OrderFulfillment fulfillment = OrderFulfillment.findOrderFulfillment(pk);

					if (fulfillment != null) {

						fulfillment.setCurrentLocation(stop);
						fulfillment.setUpdateDate(new Timestamp(System.currentTimeMillis()));

						CarrierFulfillmentStatus carrierFulfillStatus = fulfillment.getCarrierFulfillmentStatus();
						if (carrierFulfillStatus.equals(CarrierFulfillmentStatus.READY_TO_DELIVER)
								|| carrierFulfillStatus.equals(CarrierFulfillmentStatus.DELIVERED)
								|| carrierFulfillStatus.equals(CarrierFulfillmentStatus.SELECTED_TO_DELIVER)) {

							fulfillment.setCarrierFulfillmentStatus(CarrierFulfillmentStatus.DELIVERED);
							Stop deliveryDestination = order.getOrderMgr().getDeliveryStop();
							if (!fulfillment.getDestination().equals(deliveryDestination)
									&& destination.equals(inspectionStop)) {
								if (statusMessage.getCarrierStatus().equals(CarrierStatus.SHIPPABLE)) {
									LOG.info(carrierText + " is Shippable updating destination to- "
											+ deliveryDestination);
									publishCarrierUpdateMessage(carrierNumber, deliveryDestination);
									fulfillment.setDestination(deliveryDestination);
								} else {
									LOG.info(carrierText + " is Not Shippable Waiting for Inspector to Update ");
									DieType dieType = (order.getLeftDie().equals(statusMessage.getDie())) ? DieType.LEFT
											: DieType.RIGHT;
									newFulfillmentHelper.saveWeldOrderComments(order,
											carrierText + " is Not Shippable Waiting for Inspector to Update ", dieType,
											CommentCategory.DELIVERY);
								}
							} else {
								LOG.info(carrierText + " already has destination to - " + deliveryDestination);
							}
						}
						fulfillment.merge();
					}
				}
			}
		}
	}

	private void validateCarrierStatusMessage(CarrierStatusMessage statusMessage) {
		if (statusMessage.getCurrentLocationAsStop() == null) {
			generateAlarm(AlarmTypes.INVALID_STOP_ALARM);
		}

		if (statusMessage.getDestinationAsStop() == null) {
			generateAlarm(AlarmTypes.INVALID_STOP_ALARM);
		}

		if (statusMessage.getDie() == null) {
			generateAlarm(AlarmTypes.INVALID_DIE_ALARM);
		}
	}

	private void generateAlarm(AlarmTypes alarmType) {
		newFulfillmentHelper.generateAlarm(0, alarmType.type());
	}

	private void publishCarrierUpdateMessage(Integer carrierNumber, Stop destination) {
		// TODO:Publish Carrier Update Message for new destination.
		Carrier newCarrier = new Carrier();
		newCarrier.setCarrierNumber(carrierNumber);
		newCarrier.setDestination(destination);
		newCarrier.setSource(StorageConfig.OHCV_APP_FULFILLMENT);
		CarrierUpdateMessage updateMessage = new CarrierUpdateMessage(newCarrier);
		EventBus.publish(updateMessage);
	}

	Integer getCarrier() {
		return carrierNumber;
	}
}
