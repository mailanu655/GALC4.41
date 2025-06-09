package com.honda.mfg.stamp.conveyor.newfulfillment;

import java.util.List;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.OrderFulfillment;
import com.honda.mfg.stamp.conveyor.domain.OrderMgr;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.WeldOrder;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierFulfillmentStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.CommentCategory;
import com.honda.mfg.stamp.conveyor.domain.enums.DieType;
import com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus;
import com.honda.mfg.stamp.conveyor.manager.Storage;
import com.honda.mfg.stamp.conveyor.messages.ReloadStorageStateMessage;
import com.honda.mfg.stamp.conveyor.messages.StaleDataMessage;
import com.honda.mfg.stamp.conveyor.release.ReleaseManager;

/**
 * Created by IntelliJ IDEA. User: vcc30690 Date: 11/16/11 Time: 3:50 PM To
 * change this template use File | Settings | File Templates.
 */
public class OrderFulfillmentManager implements Fulfillment {

	private static final Logger LOG = LoggerFactory.getLogger(OrderFulfillmentManager.class);

	private OrderMgr orderMgr;
	private Storage storage;
	private NewFulfillmentHelper newFulfillmentHelper;
	private ReleaseManager releaseManager;
	private String fulfillmentCycleSize;
	private String recirculationCarrierReleaseCount;

	public OrderFulfillmentManager(OrderMgr orderMgr, Storage storage, NewFulfillmentHelper newFulfillmentHelper,
			ReleaseManager releaseManager, String fulfillmentCycleSize, String recirculationCarrierReleaseCount) {
		this.orderMgr = orderMgr;
		this.storage = storage;
		this.newFulfillmentHelper = newFulfillmentHelper;
		this.releaseManager = releaseManager;
		this.fulfillmentCycleSize = fulfillmentCycleSize;
		this.recirculationCarrierReleaseCount = recirculationCarrierReleaseCount;
		AnnotationProcessor.process(this);
	}

	@SuppressWarnings("incomplete-switch")
	public void run() {
		try {
			WeldOrder order = newFulfillmentHelper.getActiveOrder(orderMgr);
			if (order != null) {
				if (!storage.getStorageState().isStale()) {
					OrderStatus orderStatus = order.getOrderStatus();

					order = updateQueuedQty(order);

					switch (orderStatus) {
					case InProcess:
						newFulfillmentHelper.saveWeldOrderComments(order, "None.", DieType.BOTH,
								CommentCategory.FULFILLMENT);
						retrieveCarriers(order, fulfillmentCycleSize, recirculationCarrierReleaseCount);
						break;
					case RetrievingCarriers:
						releaseCarriers(order);
					}
				} else {
					LOG.info("Storage State stale");
					newFulfillmentHelper.saveWeldOrderComments(order, "Storage state stale", DieType.BOTH,
							CommentCategory.FULFILLMENT);
				}
			} else {
				LOG.info("No Active Order found clearing any backOrders that exist for this OrderMgr");

				storage.getStorageState().removeFromBackOrderList(orderMgr);
			}
		} catch (Exception e) {
			LOG.info(e.getMessage());
			e.printStackTrace();
		}
	}

	private void retrieveCarriers(WeldOrder weldOrder, String fulfillmentCycleSize,
			String recirculationCarrierReleaseCount) {
		if (newFulfillmentHelper.noOrderMgrWithOrderStatus(OrderStatus.RetrievingCarriers)) {
			if (weldOrder.getOrderStatus().equals(OrderStatus.InProcess)) {
				if (allCarriersRetrieved(weldOrder)) {
					Integer releaseCycle = newFulfillmentHelper.getMinCycleCountForOrderForFulfillmentStatus(weldOrder,
							CarrierFulfillmentStatus.RELEASED);
					Integer retrieveCycle = newFulfillmentHelper.getMinCycleCountForOrderForFulfillmentStatus(weldOrder,
							CarrierFulfillmentStatus.RETRIEVED);
					if (releaseCycle.intValue() == 0 && retrieveCycle.intValue() == 0) {
						LOG.info("No More carriers required to fulfill updating OrderStatus to AutoComplete for Order-"
								+ weldOrder.getId());
						newFulfillmentHelper.saveWeldOrderStatus(weldOrder, OrderStatus.AutoCompleted);
						storage.reloadStorageState();
					} else {
						LOG.info("Waiting for carriers to queue for Order-" + weldOrder.getId());
						// newFulfillmentHelper.saveWeldOrderComments(weldOrder, "Waiting for carriers
						// to queue", DieType.BOTH,CommentCategory.FULFILLMENT);
					}
				} else {
					Integer releaseCycle = newFulfillmentHelper.getMinCycleCountForOrderForFulfillmentStatus(weldOrder,
							CarrierFulfillmentStatus.RELEASED);
					Integer cycle = newFulfillmentHelper.getMinCycleCountForOrderForFulfillmentStatus(weldOrder,
							CarrierFulfillmentStatus.SELECTED);
					Integer retrievalCycle = newFulfillmentHelper.getMinCycleCountForOrderForFulfillmentStatus(
							weldOrder, CarrierFulfillmentStatus.RETRIEVED);
					if (releaseCycle == 0 && cycle == 0 && retrievalCycle == 0) {
						// newFulfillmentHelper.saveWeldOrderComments(weldOrder, "Retrieving
						// Carriers",DieType.BOTH,CommentCategory.FULFILLMENT);

						RetrieveCycleManager retrieveCycleManager = new RetrieveCycleManager(storage,
								fulfillmentCycleSize, recirculationCarrierReleaseCount);
						retrieveCycleManager.process(weldOrder, newFulfillmentHelper, releaseManager);
					} else {
						LOG.info("Waiting for carriers to enter queue rows for Order-" + weldOrder.getId());
						// newFulfillmentHelper.saveWeldOrderComments(weldOrder, "Waiting for carriers
						// to enter queue rows", DieType.BOTH, CommentCategory.FULFILLMENT);
					}
				}
			}
		} else {
			// newFulfillmentHelper.saveWeldOrderComments(weldOrder, "Another order
			// selecting carriers", DieType.BOTH, CommentCategory.FULFILLMENT);
		}
	}

	private boolean allCarriersRetrieved(WeldOrder order) {
		boolean flag = false;
		List<OrderFulfillment> orderFulfillments = newFulfillmentHelper.getAllOrderFulfillmentsByOrder(order);

		if (orderFulfillments != null) {
			Integer leftOrderedQty = Integer.valueOf(0);
			Integer rightOrderedQty = Integer.valueOf(0);

			for (OrderFulfillment fulfillment : orderFulfillments) {
				if (!fulfillment.isStatus(CarrierFulfillmentStatus.SELECTED)) {

					if (fulfillment.getDie().equals(order.getModel().getLeftDie())) {
						leftOrderedQty = leftOrderedQty + fulfillment.getQuantity();
					}
					if (fulfillment.getDie().equals(order.getModel().getRightDie())) {
						rightOrderedQty = rightOrderedQty + fulfillment.getQuantity();
					}
				}
			}
			LOG.info(" left orderedQty - " + leftOrderedQty.intValue() + " right orderedQty - "
					+ rightOrderedQty.intValue());
			if (leftOrderedQty.intValue() >= order.getLeftQuantity().intValue()
					&& rightOrderedQty.intValue() >= order.getRightQuantity().intValue()) {
				flag = true;
			}
			// 2014-04-02:VB:Note: We are not counting left over carriers queued for
			// previous orders here;
			// Therefore the fulfillment will keep running until the delivery manager starts
			// and makes
			// any extra carriers part of this order's fulfillment.
		}
		return flag;
	}

	/**
	 * update the count of carriers already queued for this order, including those
	 * in DELIVERED and CONSUMED status
	 *
	 * @param order
	 */
	private WeldOrder updateQueuedQty(WeldOrder order) {
		List<OrderFulfillment> orderFulfillments = newFulfillmentHelper.getAllOrderFulfillmentsByOrder(order);

		if (orderFulfillments != null) {
			Integer leftQueuedQty = Integer.valueOf(0);
			Integer rightQueuedQty = Integer.valueOf(0);

			for (OrderFulfillment fulfillment : orderFulfillments) {
				if (!fulfillment.isStatus(CarrierFulfillmentStatus.SELECTED)
						&& !fulfillment.isStatus(CarrierFulfillmentStatus.RETRIEVED)
						&& !fulfillment.isStatus(CarrierFulfillmentStatus.RELEASED)) {

					if (order.isLeftDie(fulfillment.getDie())) {
						leftQueuedQty = leftQueuedQty + fulfillment.getQuantity();
					}
					if (order.isRightDie(fulfillment.getDie())) {
						rightQueuedQty = rightQueuedQty + fulfillment.getQuantity();
					}
				}
			}
			WeldOrder activeDeliveryOrder = newFulfillmentHelper.getActiveOrderForDelivery(order.getOrderMgr());
			if (activeDeliveryOrder != null && activeDeliveryOrder.getId().equals(order.getId())) {
				int lQty = getProductQtyInQueueNotPartOfThisOrder(order, order.getLeftDie(),
						order.getOrderMgr().getLeftQueueStop());
				int rQty = getProductQtyInQueueNotPartOfThisOrder(order, order.getRightDie(),
						order.getOrderMgr().getRightQueueStop());

				leftQueuedQty = leftQueuedQty + lQty;
				rightQueuedQty = rightQueuedQty + rQty;
			}

			WeldOrder tempOrder = WeldOrder.findWeldOrder(order.getId());
			tempOrder.setLeftQueuedQty(leftQueuedQty);
			tempOrder.setRightQueuedQty(rightQueuedQty);
			tempOrder.merge();
		}
		return WeldOrder.findWeldOrder(order.getId());
	}

	private void releaseCarriers(WeldOrder order) {

		// TODO redo this logic only keep the part that releases carriers. ie if
		// (cycle.intValue() != 0)

		if (order.getOrderStatus().equals(OrderStatus.RetrievingCarriers)) {
			Integer cycle = newFulfillmentHelper.getMinCycleCountForOrderForFulfillmentStatus(order,
					CarrierFulfillmentStatus.SELECTED);
			Integer retrievalCycle = newFulfillmentHelper.getMinCycleCountForOrderForFulfillmentStatus(order,
					CarrierFulfillmentStatus.RETRIEVED);
			/*
			 * (selected)cycle - lowest cycle with SELECTED status retrievalCycle - lowest
			 * cycle with RETRIEVED status if(retrievalCycle != 0) -->there is at least 1
			 * carrier with RETRIEVED status do nothing/exit else (implies no carriers with
			 * RETRIEVED status) if(there are SELECTED carriers) release carriers else
			 * (implies no SELECTED or RETRIEVED carriers set the order to InProcess
			 */

			if (cycle.intValue() != 0) {
				LOG.info("Releasing Carriers for Order-" + order.getId());
				DieType dieType = null;
				int countLeft, countRight;
				countLeft = newFulfillmentHelper.getAllOrderFulfillmentsByCarrierFulfillmentStatusAndDie(order,
						CarrierFulfillmentStatus.SELECTED, order.getLeftDie()).size();
				countRight = newFulfillmentHelper.getAllOrderFulfillmentsByCarrierFulfillmentStatusAndDie(order,
						CarrierFulfillmentStatus.SELECTED, order.getRightDie()).size();
				dieType = null;
				if (countLeft > 0 && countRight > 0) {
					dieType = DieType.BOTH;
				} else {
					if (countLeft > 0) {
						dieType = DieType.LEFT;
					}
					if (countRight > 0) {
						dieType = DieType.RIGHT;
					}
				}
				if (dieType != null) {
					newFulfillmentHelper.saveWeldOrderComments(order, "Carriers Selected and Releasing from Rows",
							dieType, CommentCategory.FULFILLMENT);
				}
				ReleaseCycleManager releaseCycleManager = new ReleaseCycleManager();
				releaseCycleManager.process(order, cycle, newFulfillmentHelper, releaseManager);
			} else {
				if (retrievalCycle.intValue() == 0) {
					LOG.info("No More cycles to Release updating OrderStatus to Inprocess for Order-" + order.getId());
					newFulfillmentHelper.saveWeldOrderStatusAndComments(order, OrderStatus.InProcess, "None.",
							DieType.BOTH);
				} else {
					LOG.info("Waiting for carriers to release for Order-" + order.getId());

					int countLeft, countRight;
					countLeft = newFulfillmentHelper.getAllOrderFulfillmentsByCarrierFulfillmentStatusAndDie(order,
							CarrierFulfillmentStatus.RETRIEVED, order.getLeftDie()).size();
					countRight = newFulfillmentHelper.getAllOrderFulfillmentsByCarrierFulfillmentStatusAndDie(order,
							CarrierFulfillmentStatus.RETRIEVED, order.getRightDie()).size();
					countLeft = countLeft
							+ newFulfillmentHelper.getAllOrderFulfillmentsByCarrierFulfillmentStatusAndDie(order,
									CarrierFulfillmentStatus.RELEASED, order.getLeftDie()).size();
					countRight = countRight
							+ newFulfillmentHelper.getAllOrderFulfillmentsByCarrierFulfillmentStatusAndDie(order,
									CarrierFulfillmentStatus.RELEASED, order.getRightDie()).size();

					if (countLeft > 0 && countRight > 0) {
					} else {
						if (countLeft > 0) {
							newFulfillmentHelper.saveWeldOrderComments(order, "Carriers in route to queue.",
									DieType.LEFT, CommentCategory.FULFILLMENT);
						}
						if (countRight > 0) {
							newFulfillmentHelper.saveWeldOrderComments(order, "Carriers in route to queue",
									DieType.RIGHT, CommentCategory.FULFILLMENT);
						}
					}
				}
			}
		}
	}

	@Override
	public OrderMgr getOrderManager() {
		return orderMgr;
	}

	@EventSubscriber(eventClass = StaleDataMessage.class, referenceStrength = ReferenceStrength.STRONG)
	public void healthCheck(StaleDataMessage msg) {
		// StaleDataMessage message = msg;
		LOG.info("Received StaleDataMessage.  Stale? " + msg.isStale());
		try {
			if (msg != null && msg.isStale()) {
				WeldOrder order = newFulfillmentHelper.getActiveOrder(orderMgr);

				if (order != null && order.getOrderStatus().equals(OrderStatus.RetrievingCarriers)) {
					newFulfillmentHelper.deleteAllUnReleasedOrderFulfillments(order);

					newFulfillmentHelper.saveWeldOrderStatusAndComments(order, OrderStatus.InProcess,
							"StorageState Stale", DieType.BOTH);

				}
			}
		} catch (Exception e) {
			LOG.debug(e.getMessage());
		}
	}

	@EventSubscriber(eventClass = ReloadStorageStateMessage.class, referenceStrength = ReferenceStrength.STRONG)
	public void reOrderCarriers(ReloadStorageStateMessage msg) {
		// StaleDataMessage message = msg;
		LOG.info("Received ReloadStorageStateMessage. ");
		WeldOrder order = newFulfillmentHelper.getActiveOrder(orderMgr);

		if (order != null && order.getOrderStatus().equals(OrderStatus.RetrievingCarriers)) {
			newFulfillmentHelper.deleteAllUnReleasedOrderFulfillments(order);
			newFulfillmentHelper.saveWeldOrderStatusAndComments(order, OrderStatus.InProcess,
					msg.getCurrentLocation().getName() + " Out Of Order", DieType.BOTH);
		}
		storage.reloadStorageState();
	}

	Integer getProductQtyInQueueNotPartOfThisOrder(WeldOrder order, Die die, Stop leftQueue) {
		Integer dieNumber = Integer.valueOf(die.getId().toString());
		List<CarrierMes> carriers = CarrierMes
				.findAllCarriersWithCurrentLocationAndDestinationLocationAndDie(leftQueue.getId(), dieNumber);
		Integer qty = 0;
		for (CarrierMes carrier : carriers) {
			if (!newFulfillmentHelper.isCarrierPartOfActiveOrder(order, carrier)) {
				qty = qty + carrier.getQuantity();
			}
		}
		return qty;
	}

}
