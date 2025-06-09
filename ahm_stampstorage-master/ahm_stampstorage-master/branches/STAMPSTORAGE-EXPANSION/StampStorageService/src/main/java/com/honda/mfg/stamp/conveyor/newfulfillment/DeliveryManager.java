package com.honda.mfg.stamp.conveyor.newfulfillment;

import java.util.ArrayList;
import java.util.List;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.OrderFulfillment;
import com.honda.mfg.stamp.conveyor.domain.OrderMgr;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.WeldOrder;
import com.honda.mfg.stamp.conveyor.domain.enums.AlarmTypes;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierFulfillmentStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.CommentCategory;
import com.honda.mfg.stamp.conveyor.domain.enums.DieType;
import com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus;
import com.honda.mfg.stamp.conveyor.exceptions.WrongDieInQueueException;
import com.honda.mfg.stamp.conveyor.manager.StorageConfig;
import com.honda.mfg.stamp.conveyor.release.ReleaseManager;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 6/10/13 Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeliveryManager implements Fulfillment {

	private static final Logger LOG = LoggerFactory.getLogger(DeliveryManager.class);
	private OrderMgr orderMgr;
	private NewFulfillmentHelper newFulfillmentHelper;
	private ReleaseManager releaseManager;
	private String deliveryCarrierCycleSize;
	private String fulfillmentCarrierInspectionStop;

	public DeliveryManager(OrderMgr orderMgr, NewFulfillmentHelper newFulfillmentHelper, ReleaseManager releaseManager,
			String deliveryCycleSize, String fulfillmentCarrierInspectionStop) {
		this.orderMgr = orderMgr;
		this.newFulfillmentHelper = newFulfillmentHelper;
		this.releaseManager = releaseManager;
		this.deliveryCarrierCycleSize = deliveryCycleSize;
		this.fulfillmentCarrierInspectionStop = fulfillmentCarrierInspectionStop;
		AnnotationProcessor.process(this);
	}

	/**
	 * If enough carriers in the queue and no other order is delivering, change
	 * order status to delivering carriers
	 */
	@Override
	public void run() {
		try {
			WeldOrder order = newFulfillmentHelper.getActiveOrderForDelivery(orderMgr);

			if (order != null) {
				LOG.info(" running Delivery Manager for Order-" + order.getId());
				order = updateDeliveredQty(order);
				if (order.getDeliveryStatus().equals(OrderStatus.InProcess)) {

					if (order.getLeftDeliveredQuantity() >= order.getLeftQuantity()
							&& order.getRightDeliveredQuantity() >= order.getRightQuantity()) {
						LOG.info(
								"No More carriers required to deliver updating deliveryStatus to AutoComplete for Order-"
										+ order.getId());
						newFulfillmentHelper.saveWeldDeliveryStatus(order, OrderStatus.AutoCompleted);
					} else {
						addCarriersInQueueNotPartOfActiveOrderToOrder(order, order.getRemainingLeftQuantity(),
								order.getLeftDie(), order.getLeftQueueStop());
						addCarriersInQueueNotPartOfActiveOrderToOrder(order, order.getRemainingRightQuantity(),
								order.getRightDie(), order.getRightQueueStop());
						int deliveryCarrierReleaseCount = newFulfillmentHelper.getParmValue(deliveryCarrierCycleSize);
						if (newFulfillmentHelper.isSpaceAvailableToDeliver(orderMgr, deliveryCarrierReleaseCount)) {
							if (newFulfillmentHelper.noOrderWithDeliveryStatus(OrderStatus.DeliveringCarriers)) {

								if (queuesHveReqCntOfCarriersWithCorrectDieToDeliver(orderMgr, order,
										deliveryCarrierReleaseCount)) {
									LOG.info("Space Available - " + order.getId());
									List<OrderFulfillment> leftFulfillments = getCarriersToDeliverFromQueueRow(order,
											orderMgr.getLeftQueueStop(), order.getLeftDie(),
											order.getRemainingLeftQuantity(), deliveryCarrierReleaseCount);

									List<OrderFulfillment> rightFulfillments = getCarriersToDeliverFromQueueRow(order,
											orderMgr.getRightQueueStop(), order.getRightDie(),
											order.getRemainingRightQuantity(), deliveryCarrierReleaseCount);

									ArrayList<OrderFulfillment> selectedFulfillments = new ArrayList<OrderFulfillment>();
									selectedFulfillments.addAll(leftFulfillments);
									selectedFulfillments.addAll(rightFulfillments);

									for (OrderFulfillment tempFulfillment : selectedFulfillments) {
										newFulfillmentHelper.updateOrderFulfillmentStatus(tempFulfillment,
												CarrierFulfillmentStatus.SELECTED_TO_DELIVER, null);
									}
									// List<OrderFulfillment> selectedToDeliver =
									// newFulfillmentHelper.getAllOrderFulfillmentsByOrderAndCarrierFulfillmentStatus(order,
									// CarrierFulfillmentStatus.SELECTED_TO_DELIVER);

									List<OrderFulfillment> readyToDeliver = newFulfillmentHelper
											.getAllOrderFulfillmentsByOrderAndCarrierFulfillmentStatus(order,
													CarrierFulfillmentStatus.READY_TO_DELIVER);
									if (selectedFulfillments.size() > 0 || readyToDeliver.size() > 0) {
										DieType dieType;
										if (leftFulfillments.size() > 0 && rightFulfillments.size() > 0) {
											dieType = DieType.BOTH;
										} else {
											dieType = (leftFulfillments.size() > 0) ? DieType.LEFT : DieType.RIGHT;
										}
										newFulfillmentHelper.saveWeldDeliveryStatusAndComments(order,
												OrderStatus.DeliveringCarriers, "Delivering to weld", dieType);
									}
								} else {
									LOG.info("Cannot Deliver Carriers from Queue ");
									// newFulfillmentHelper.saveWeldDeliveryStatus(order, OrderStatus.InProcess);
									newFulfillmentHelper.saveWeldDeliveryStatusAndComments(order, OrderStatus.InProcess,
											"Cannot deliver carriers now - Not enough Carriers in Queue to Deliver",
											DieType.BOTH);
								}
							} else {
								LOG.info("Another Order Delivering Carriers ");
								// newFulfillmentHelper.saveWeldDeliveryStatusAndComments(order,
								// OrderStatus.InProcess, "Cannot deliver carriers now - Another Order
								// Delivering Carriers ", DieType.BOTH);
							}
						} else {
							LOG.info("No Space Available to deliver Carriers ");

							newFulfillmentHelper.saveWeldDeliveryStatusAndComments(order, OrderStatus.InProcess,
									"No space available to deliver carriers", DieType.BOTH);
						}
					}

				} else if (order.getDeliveryStatus().equals(OrderStatus.DeliveringCarriers)) {
					deliverCarriers(order);
				}
			}
		} catch (WrongDieInQueueException exception) {
			// newFulfillmentHelper.saveWeldDeliveryComments(order, "Wrong die in queue.
			// Please place delivery on hold, manually correct, and place back in process.",
			// DieType.BOTH);
			newFulfillmentHelper.generateAlarm(0, AlarmTypes.WRONG_DIE_IN_QUEUE.type());

		} catch (Exception e) {
			LOG.info(e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * Used when carriers are left over in the queue
	 */
	private void addCarriersInQueueNotPartOfActiveOrderToOrder(WeldOrder order, Integer remainingQty, Die die,
			Stop queueStop) {
		List<CarrierMes> queuedCarriers = CarrierMes.findAllCarriersWithCurrentLocation(queueStop.getId());
		int partQty = 0;
		if (queuedCarriers.size() > 0) {

			for (int i = 0; i < queuedCarriers.size(); i++) {
				if (partQty < remainingQty) {
					if (carrierHasDie(queuedCarriers.get(i), die)) {

						if (!newFulfillmentHelper.isCarrierPartOfActiveOrder(order, queuedCarriers.get(i))) {

							LOG.info(" Carrier - " + queuedCarriers.get(i).getCarrierNumber()
									+ "  not part of active Order - ");
							Integer carrierNumber = queuedCarriers.get(i).getCarrierNumber();
							Carrier carrier = newFulfillmentHelper
									.populateCarrier(CarrierMes.findCarrierByCarrierNumber(carrierNumber));
							newFulfillmentHelper.saveFulfillment(order, carrier, 999, CarrierFulfillmentStatus.QUEUED);
							partQty = partQty + queuedCarriers.get(i).getQuantity();
						} else {
							LOG.info(" carrier - " + queuedCarriers.get(i).getCarrierNumber()
									+ " part of active order - " + order.getId());
						}

					} else {
						LOG.info(" Carrier -" + queuedCarriers.get(i).getCarrierNumber() + " in queue stop - "
								+ queueStop.getId() + " does not have the required Die-" + die.getDescription());
						DieType dieType = (order.getLeftDie().equals(die)) ? DieType.LEFT : DieType.RIGHT;
						newFulfillmentHelper.saveWeldOrderComments(order,
								"Wrong die in queue. Please place delivery on hold, manually correct queue row, manually reset alarm, and place back in process.",
								dieType, CommentCategory.DELIVERY);
						throw new WrongDieInQueueException("Wrong die in Queue");
					}
				} else {
					LOG.info(" QueuePartQty " + partQty + "   remainingQty - " + remainingQty);
					break;
				}

			}
		} else {
			LOG.info(" Queue Stop - " + queueStop.getId() + " is empty cannot deliver carriers at this time");
		}

	}

	/**
	 * Check the quantity of the product on carriers with correct die
	 */
	private boolean queuesHveReqCntOfCarriersWithCorrectDieToDeliver(OrderMgr orderMgr, WeldOrder order,
			int deliveryReleaseCount) {
		boolean leftFlag = false, rightFlag = false;
		long cntOfCarriersInLeftQ = CarrierMes.countCarriersWithCurrentLocationStopAndDie(orderMgr.getLeftQueueStop(),
				order.getLeftDie());
		long cntOfCarriersInRightQ = CarrierMes.countCarriersWithCurrentLocationStopAndDie(orderMgr.getRightQueueStop(),
				order.getRightDie());

		if (order.getRemainingLeftQuantity() > 0 && order.getRemainingRightQuantity() > 0) {
			if (cntOfCarriersInLeftQ > 0 && cntOfCarriersInRightQ > 0) {
				if (deliveryReleaseCount > cntOfCarriersInLeftQ) {
					Integer qty = quantityOfProductInQueue(orderMgr.getLeftQueueStop());
					if (qty < order.getRemainingLeftQuantity()) {
						leftFlag = true;
						newFulfillmentHelper.saveWeldOrderComments(order,
								"Not enough Carriers in Left Queue to Deliver", DieType.LEFT, CommentCategory.DELIVERY);
						LOG.info("Not enough Carriers in Left Queue to Deliver");

					}
				}
				if (!carrierAtHeadHasRequiredDie(orderMgr.getLeftQueueStop(), order.getLeftDie())) {
					leftFlag = true;
					newFulfillmentHelper.saveWeldOrderComments(order, "Carrier At The head Has incorrect Left Die",
							DieType.LEFT, CommentCategory.DELIVERY);

					LOG.info("Carrier At The head Has incorrect Left Die");
					throw new WrongDieInQueueException("Carrier At The head Has incorrect Left Die");
				}
				if (deliveryReleaseCount > cntOfCarriersInRightQ) {

					Integer qty = quantityOfProductInQueue(orderMgr.getRightQueueStop());
					if (qty < order.getRemainingRightQuantity()) {
						rightFlag = true;
						newFulfillmentHelper.saveWeldOrderComments(order,
								"Not enough Carriers in Right Queue to Deliver", DieType.RIGHT,
								CommentCategory.DELIVERY);
						LOG.info("Not enough Carriers in Right Queue to Deliver");
					}
				}
				if (!carrierAtHeadHasRequiredDie(orderMgr.getRightQueueStop(), order.getRightDie())) {
					rightFlag = true;
					LOG.info("Carrier At The head Has incorrect Right Die");
					newFulfillmentHelper.saveWeldOrderComments(order, "Carrier At The head Has incorrect Right Die",
							DieType.RIGHT, CommentCategory.DELIVERY);

					throw new WrongDieInQueueException("Carrier At The head Has incorrect Right Die");
				}

			} else {
				newFulfillmentHelper.saveWeldOrderComments(order, "Not enough Carriers in Both Queues to Deliver",
						DieType.BOTH, CommentCategory.DELIVERY);
				LOG.info("Not enough Carriers in Queues to Deliver");
				return false;
			}
		}

		return !leftFlag && !rightFlag;
	}

	private Integer quantityOfProductInQueue(Stop stop) {
		Integer qty = 0;

		List<CarrierMes> carriers = CarrierMes.findAllCarriersWithCurrentLocation(stop.getId());

		for (CarrierMes carrierMes : carriers) {
			qty = qty + carrierMes.getQuantity();
		}

		return qty;
	}

	private boolean carrierHasDie(CarrierMes carrier, Die die) {
		if (carrier.getDieNumber().longValue() == die.getId().longValue()) {
			return true;
		}
		return false;

	}

	private boolean carrierAtHeadHasRequiredDie(Stop stop, Die die) {
		List<CarrierMes> carriers = CarrierMes.findAllCarriersWithCurrentLocation(stop.getId());
		return carrierHasDie(carriers.get(0), die);

	}

	private void deliverCarriers(WeldOrder order) {
		List<OrderFulfillment> readyToDeliver = newFulfillmentHelper
				.getAllOrderFulfillmentsByOrderAndCarrierFulfillmentStatus(order,
						CarrierFulfillmentStatus.READY_TO_DELIVER);
		List<OrderFulfillment> selectedToDeliver = newFulfillmentHelper
				.getAllOrderFulfillmentsByOrderAndCarrierFulfillmentStatus(order,
						CarrierFulfillmentStatus.SELECTED_TO_DELIVER);
		if (readyToDeliver.size() == 0) {
			// newFulfillmentHelper.saveWeldOrderComments(order, "Carriers are not
			// delivering.", DieType.BOTH, CommentCategory.DELIVERY);
			if (selectedToDeliver.size() > 0) {
				LOG.info("Get carriers to deliver to ST 13");
				Stop deliveryStop = getCarrierDeliveryDestination();
				List<OrderFulfillment> leftOrderFulfillments = newFulfillmentHelper
						.getAllOrderFulfillmentsByCarrierFulfillmentStatusAndDie(order,
								CarrierFulfillmentStatus.SELECTED_TO_DELIVER, order.getLeftDie());

				if (leftOrderFulfillments.size() > 0) {
					saveAndReleaseCarriers(leftOrderFulfillments, deliveryStop,
							CarrierFulfillmentStatus.READY_TO_DELIVER);
					newFulfillmentHelper.saveWeldOrderComments(order, "Carriers selected to deliver for Left die.",
							DieType.LEFT, CommentCategory.DELIVERY);
				} else {
					List<OrderFulfillment> rightOrderFulfillments = newFulfillmentHelper
							.getAllOrderFulfillmentsByCarrierFulfillmentStatusAndDie(order,
									CarrierFulfillmentStatus.SELECTED_TO_DELIVER, order.getRightDie());
					saveAndReleaseCarriers(rightOrderFulfillments, deliveryStop,
							CarrierFulfillmentStatus.READY_TO_DELIVER);
					if (rightOrderFulfillments.size() > 0) {
						newFulfillmentHelper.saveWeldOrderComments(order, "Carriers selected to deliver for Right die.",
								DieType.RIGHT, CommentCategory.DELIVERY);
					}
				}
			} else {
				newFulfillmentHelper.saveWeldDeliveryStatus(order, OrderStatus.InProcess);
			}
		} else {
			// determine which side is waiting to be delivered.
			DieType dieType = null;
			for (int i = 0; i < readyToDeliver.size(); i++) {
				if (readyToDeliver.get(i).getDie().equals(order.getLeftDie())) {
					if (dieType == DieType.RIGHT) {
						dieType = DieType.BOTH;
						break;
					} else {
						dieType = DieType.LEFT;
					}
				} else {
					if (dieType == DieType.LEFT) {
						dieType = DieType.BOTH;
						break;
					} else {
						dieType = DieType.RIGHT;
					}
				}
			}
			if (dieType == null)
				dieType = DieType.BOTH;
			LOG.info("Waiting for carriers to release to deliver for Order-" + order.getId());
			newFulfillmentHelper.saveWeldOrderComments(order, "Carriers are being released for final delivery.",
					dieType, CommentCategory.DELIVERY);
		}
	}

	private Stop getCarrierDeliveryDestination() {
		int value = this.newFulfillmentHelper.getParmValue(fulfillmentCarrierInspectionStop);
		return Stop.findStop(new Long(value));
	}

	private void saveAndReleaseCarriers(List<OrderFulfillment> orderFulfillments, Stop deliveryStop,
			CarrierFulfillmentStatus carrierFulfillmentStatus) {
		for (OrderFulfillment orderFulfillment : orderFulfillments) {
			newFulfillmentHelper.updateOrderFulfillmentStatus(orderFulfillment, carrierFulfillmentStatus, deliveryStop);
			Integer carrierNumber = orderFulfillment.getId().getCarrierNumber();
			LOG.info("issuing carrier update for carrier-" + carrierNumber + " to move to ST 13");
			releaseManager.releaseCarrier(carrierNumber, deliveryStop, StorageConfig.OHCV_APP_FULFILLMENT, true);
		}
	}

	@Override
	public OrderMgr getOrderManager() {
		return orderMgr;
	}

	private synchronized WeldOrder updateDeliveredQty(WeldOrder order) {
		List<OrderFulfillment> orderFulfillments = newFulfillmentHelper.getAllOrderFulfillmentsByOrder(order);

		if (orderFulfillments != null) {
			Integer leftDeliveredQty = Integer.valueOf(0);
			Integer rightDeliveredQty = Integer.valueOf(0);

			for (OrderFulfillment fulfillment : orderFulfillments) {
				if (fulfillment.getCarrierFulfillmentStatus().equals(CarrierFulfillmentStatus.DELIVERED)
						|| fulfillment.getCarrierFulfillmentStatus().equals(CarrierFulfillmentStatus.CONSUMED)) {
					if (fulfillment.getDie().equals(order.getModel().getLeftDie())) {
						leftDeliveredQty = leftDeliveredQty + fulfillment.getQuantity();
					}
					if (fulfillment.getDie().equals(order.getModel().getRightDie())) {
						rightDeliveredQty = rightDeliveredQty + fulfillment.getQuantity();
					}
				}
			}

			WeldOrder tempOrder = WeldOrder.findWeldOrder(order.getId());
			tempOrder.setLeftDeliveredQuantity(leftDeliveredQty);
			tempOrder.setRightDeliveredQuantity(rightDeliveredQty);
			tempOrder.merge();
		}
		return WeldOrder.findWeldOrder(order.getId());
	}

	public List<OrderFulfillment> getCarriersToDeliverFromQueueRow(WeldOrder order, Stop queueStop, Die die,
			Integer remainingQty, int deliveryCycleSize) {
		List<CarrierMes> queuedCarriers = CarrierMes.findAllCarriersWithCurrentLocation(queueStop.getId());
		int partQty = 0;
		List<OrderFulfillment> selectedFulfillments = new ArrayList<OrderFulfillment>();
		if (queuedCarriers.size() > 0) {

			for (int i = 0; i < deliveryCycleSize; i++) {
				if (partQty < remainingQty) {
					if (i > queuedCarriers.size() - 1) {
						break;
					}
					if (carrierHasDie(queuedCarriers.get(i), die)) {

						List<OrderFulfillment> orderFulfillments = OrderFulfillment
								.findAllOrderFulfillmentsByCarrierWithCarrierFulfillmentStatus(
										queuedCarriers.get(i).getCarrierNumber(), CarrierFulfillmentStatus.QUEUED);

						if (orderFulfillments.size() > 0) {
							for (OrderFulfillment fulfillment : orderFulfillments) {

								WeldOrder tempOrder = fulfillment.getId().getWeldOrder();

								if (tempOrder.getId().equals(order.getId())) {
									LOG.info(" carrier - " + queuedCarriers.get(i).getCarrierNumber()
											+ "  queued for Order - " + tempOrder.getId());
									selectedFulfillments.add(fulfillment);
									// newFulfillmentHelper.updateOrderFulfillmentStatus(fulfillment,
									// CarrierFulfillmentStatus.SELECTED_TO_DELIVER, null);
									partQty = partQty + queuedCarriers.get(i).getQuantity();
									break;
								} else {
									LOG.info(" carrier - " + queuedCarriers.get(i).getCarrierNumber()
											+ "  queued for other Order - " + tempOrder.getId());
								}
							}
						} else {
							LOG.info(" No Order Fulfillment Records for carrier - "
									+ queuedCarriers.get(i).getCarrierNumber() + " with Queued Status");
						}
					} else {
						LOG.info(" Carrier -" + queuedCarriers.get(i).getCarrierNumber() + " in queue stop - "
								+ queueStop.getId() + " does not have the required Die-" + die.getDescription());
						/** GSA 20140529 - Unable to Unblock alarm **/
						selectedFulfillments.clear();
						DieType dieType = (order.getLeftDie().equals(die)) ? DieType.LEFT : DieType.RIGHT;
						newFulfillmentHelper.saveWeldOrderComments(order,
								"Wrong die in queue. Please place delivery on hold, manually correct queue row, manually reset alarm, and place back in process.",
								dieType, CommentCategory.DELIVERY);
						throw new WrongDieInQueueException("Wrong die in Queue");
					}
				} else {
					LOG.info(" SelectedToDeliverPartQty " + partQty + "   remainingQty - " + remainingQty);
					break;
				}

			}

		} else {
			LOG.info(" Queue Stop - " + queueStop.getId() + " is empty cannot deliver carriers at this time");
		}
		return selectedFulfillments;
	}
}
