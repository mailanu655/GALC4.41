package com.honda.mfg.stamp.conveyor.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.CarrierRelease;
import com.honda.mfg.stamp.conveyor.domain.OrderMgr;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.WeldOrder;
import com.honda.mfg.stamp.conveyor.domain.enums.OrderStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import com.honda.mfg.stamp.conveyor.helper.AbstractHelperImpl;

/**
 * User: vcc30690 Date: 7/15/11
 */
public class EmptyCarriersServiceHelperImpl extends AbstractHelperImpl implements EmptyCarriersServiceHelper {

	private static final Logger LOG = LoggerFactory.getLogger(EmptyCarriersServiceHelper.class);

	public Stop getOldWeldLineEmptyCarrierDeliveryStop() {
		List<Stop> stops = Stop.findAllStopsByTypeAndArea(StopType.EMPTY_CARRIER_DELIVERY, StopArea.OLD_WELD_LINE);

		if (stops.size() > 0) {
			return stops.get(0);
		}
		return null;
	}

	public Stop getEmptyCarrierDeliveryStop() {
		List<Stop> stops = Stop.findAllStopsByTypeAndArea(StopType.EMPTY_CARRIER_DELIVERY, StopArea.EMPTY_AREA);

		if (stops.size() > 0) {
			return stops.get(0);
		}
		return null;
	}

	@Override
	@Deprecated
	public boolean activeOrderExistsForOrderMgr(OrderMgr orderMgr) {
		WeldOrder activeWeldOrder = WeldOrder.findActiveOrderForOrderMgr(orderMgr);
		if (activeWeldOrder == null) {
			return false;
		}

		return true;
	}

	@Override
	public boolean anyOrderInProcess() {
		// List<WeldOrder> inprocessWeldOrders =
		// WeldOrder.findWeldOrdersByOrderStatus(OrderStatus.InProcess);
		List<WeldOrder> orderingCarriersWeldOrders = WeldOrder
				.findWeldOrdersByOrderStatus(OrderStatus.RetrievingCarriers);
		List<WeldOrder> deliveringCarriersWeldOrders = WeldOrder
				.findWeldOrdersByOrderStatus(OrderStatus.DeliveringCarriers);
		// inprocessWeldOrders.size() > 0 ||
		if (orderingCarriersWeldOrders.size() > 0 || deliveringCarriersWeldOrders.size() > 0) {
			return true;
		}

		return false;
	}

	@Override
	public Integer getCarrierCountForOldWeldLineEmptyArea() {
		List<CarrierMes> carriersAtEmptyDeliveryStop = CarrierMes
				.findAllCarriersWithCurrentLocationAndDestinationLocationAndDie(
						getOldWeldLineEmptyCarrierDeliveryStop().getId(),
						Integer.parseInt(getEmptyDie().getId().toString()));
		Integer carrierCount = carriersAtEmptyDeliveryStop != null && carriersAtEmptyDeliveryStop.size() > 0
				? carriersAtEmptyDeliveryStop.size()
				: 0;
		LOG.debug("Carriers in route to old weld line empty delivery stop: " + carrierCount);
		return carrierCount;
	}

	@Override
	public Integer getCarrierCountForEmptyArea() {
		List<CarrierMes> carriersInEmptyArea2 = CarrierMes
				.findAllCarriersWithCurrentLocationAndDestinationLocationAndDie(getEmptyCarrierDeliveryStop().getId(),
						Integer.parseInt(getEmptyDie().getId().toString()));
		Integer carrierCountAt2003 = carriersInEmptyArea2 != null && carriersInEmptyArea2.size() > 0
				? carriersInEmptyArea2.size()
				: 0;
		LOG.debug("Carriers At empty carrier delivery: " + carrierCountAt2003);
		return Integer.valueOf(carrierCountAt2003.intValue());
	}

	public List<CarrierMes> getCarriersToMoveToEmptyStorageArea(int carrierCount) {
		List<CarrierMes> carriersInEmptyArea = CarrierMes
				.findAllCarriersWithCurrentLocation(getOldWeldLineEmptyCarrierDeliveryStop().getId());

		if (carriersInEmptyArea == null || carriersInEmptyArea.size() == 0) {
			carriersInEmptyArea = CarrierMes
					.findAllCarriersWithCurrentLocation(getOldWeldLineEmptyCarrierDeliveryStop().getId());
		}
		List<CarrierMes> carriersToRelease = new ArrayList<CarrierMes>();
		int i = 0;
		for (CarrierMes carrierMes : carriersInEmptyArea) {
			if (i < carrierCount) {
				if (!isCarrierInRow(carrierMes)) {
					carriersToRelease.add(carrierMes);
				}
			} else {
				break;
			}
			i++;
		}
		return carriersToRelease;
	}

	private boolean isCarrierInRow(CarrierMes carrierMes) {
		Stop stop = Stop.findStop(carrierMes.getCurrentLocation());
		if (stop == null) {
			System.out.println("STOP " + carrierMes.getCurrentLocation() + " Missing");
			return false;
		}
		return stop.isRowStop();
	}

	public boolean anyCarrierSetToReleaseFromRows() {
		List<CarrierMes> carriersMovingFromStorageArea = CarrierMes.findAllMovingCarriers();

		for (CarrierMes carrierMes : carriersMovingFromStorageArea) {
			if (isCarrierInRow(carrierMes)) {
				return true;
			}
		}
		List<CarrierRelease> carrierReleaseList = CarrierRelease.findAllCarrierReleases();
		for (CarrierRelease carrierRelease : carrierReleaseList) {
			Integer carrierNo = Integer.valueOf(carrierRelease.getId().toString());
			CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(carrierNo);
			if (isCarrierInRow(carrierMes)) {
				return true;
			}
		}
		return false;
	}

	public boolean canReleaseCarrier(Integer carrierNo, Stop stop) {

		CarrierMes carrierMes = getCarrier(carrierNo);
		if (carrierMes != null) {
			if (carrierMes.getCurrentLocation().equals(stop.getId()) && carrierMes.getDieNumber().equals(999)) {
				return true;
			} else {
				LOG.info("carrier -" + carrierNo + " is not in the row " + stop.getName() + " yet ");
				return false;
			}
		}
		return false;
	}

	@Override
	public long getCarrierCountInLane(StorageRow lane) {
		return CarrierMes.countCarriersWithCurrentLocationStop(lane.getStop()); // To change body of implemented methods
																				// use File | Settings | File Templates.
	}

	@Override
	public Integer getCarrierCountForBAreaEmptyStorage() {
		List<CarrierMes> carriersAtEmptyDeliveryStop = CarrierMes
				.findAllCarriersWithCurrentLocationAndDestinationLocationAndDie(
						getBAreaEmptyCarrierDeliveryStop().getId(), Integer.parseInt(getEmptyDie().getId().toString()));
		Integer carrierCount = carriersAtEmptyDeliveryStop != null && carriersAtEmptyDeliveryStop.size() > 0
				? carriersAtEmptyDeliveryStop.size()
				: 0;
		LOG.debug("Carriers in route to B_AREA empty delivery stop: " + carrierCount);
		return carrierCount;
	}

	@Override
	public Stop getBAreaEmptyCarrierDeliveryStop() {
		List<Stop> stops = Stop.findAllStopsByTypeAndArea(StopType.EMPTY_CARRIER_DELIVERY, StopArea.B_PRESS);

		if (stops.size() > 0) {
			return stops.get(0);
		}
		return null;
	}

	public CarrierMes getCarrier(Integer carrierNo) {
		return CarrierMes.findCarrierByCarrierNumber(carrierNo);
	}

}
