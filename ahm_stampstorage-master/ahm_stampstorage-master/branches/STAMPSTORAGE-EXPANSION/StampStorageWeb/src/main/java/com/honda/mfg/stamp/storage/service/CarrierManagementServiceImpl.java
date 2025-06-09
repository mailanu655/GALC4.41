package com.honda.mfg.stamp.storage.service;

import java.util.ArrayList;
import java.util.List;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.stamp.conveyor.domain.AlarmDefinition;
import com.honda.mfg.stamp.conveyor.domain.AlarmEvent;
import com.honda.mfg.stamp.conveyor.domain.BitInfo;
import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.GroupHoldFinderCriteria;
import com.honda.mfg.stamp.conveyor.domain.OrderFulfillment;
import com.honda.mfg.stamp.conveyor.domain.ParmSetting;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.ValidDestination;
import com.honda.mfg.stamp.conveyor.domain.WeldOrder;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.Press;

/**
 * User: vcc30690 Date: 5/25/11
 */
public class CarrierManagementServiceImpl implements CarrierManagementService {
	private static final Logger LOG = LoggerFactory.getLogger(CarrierManagementServiceImpl.class);

	public CarrierManagementServiceImpl() {
		super();
		AnnotationProcessor.process(this);
		setConnected(true);
	}

	@Override
	public Integer getPositionInLane(Long currentLocation, Integer carrierNumber) {

		int position = 0;
		List<StorageRow> lanes = StorageRow.findAllStorageRows();

		for (StorageRow lane : lanes) {
			if (lane.getStop().getId().equals(currentLocation)) {
				List<CarrierMes> carrierMesList = CarrierMes.getAllCarriersInLane(lane.getStop().getId());
				if (carrierMesList != null) {
					for (int i = 0; i < carrierMesList.size(); i++) {
						if (carrierNumber.equals(carrierMesList.get(i).getCarrierNumber())) {
							position = i + 1;
							break;
						}
					}
				}
			}
		}
		return position;
	}

	public List<Stop> getManualOrderCarrierDeliveryStops() {
		List<Stop> stops = new ArrayList<Stop>();
		stops.add(Stop.findStop(500L));
		stops.add(Stop.findStop(800L));
		stops.add(Stop.findStop(10100L));

		stops.add(Stop.findStop(704L));
		stops.add(Stop.findStop(5200L));

		stops.add(Stop.findStop(13400L));

		stops.add(Stop.findStop(3003L));
		stops.add(Stop.findStop(3004L));
		stops.add(Stop.findStop(3018L));
		stops.add(Stop.findStop(905L));

		return stops;
	}

	public List<Stop> getValidDestinationStops(Stop stop) {
		return ValidDestination.findValidDestinationsForGivenStop(stop);
	}

	public void reloadStorageState() {
		// 2013-02-01:VB:TBD: storage.reloadStorageState();
	}

	public Carrier getCarrier(CarrierMes carrierMes) {
		Carrier carrier = new Carrier();

		carrier.setId(carrierMes.getId());
		carrier.setCarrierNumber(carrierMes.getCarrierNumber());
		carrier.setQuantity(carrierMes.getQuantity());
		carrier.setDie(Die.findDie(Long.valueOf(carrierMes.getDieNumber())));
		carrier.setCurrentLocation(Stop.findStop(carrierMes.getCurrentLocation()));
		carrier.setDestination(Stop.findStop(carrierMes.getDestination()));
		carrier.setPress(Press.findByType(carrierMes.getOriginationLocation()));
		carrier.setCarrierStatus(CarrierStatus.findByType(carrierMes.getStatus()));
		carrier.setProductionRunNo(carrierMes.getProductionRunNumber());
		carrier.setStampingProductionRunTimestamp(carrierMes.getProductionRunDate());
		carrier.setBuffer(carrierMes.getBuffer());
		carrier.setReprocess(false);
		carrier.setUpdateDate(carrierMes.getUpdateDate());
		carrier.setSource(carrierMes.getSource());
		// TODO:CHECK FOR MAINT BITS Does carrier Manager need Maint Bits? YES
		carrier.setMaintenanceBits(carrierMes.getMaintenanceBits());

		if (carrierMes.getCurrentLocation() > 1200 && carrierMes.getCurrentLocation() < 1246) {
			carrier.setPositionInLane(
					getPositionInLane(carrierMes.getCurrentLocation(), carrierMes.getCarrierNumber()));
		} else {
			carrier.setPositionInLane(0);
		}
		return carrier;
	}

	private Integer zeroForNull(Integer i) {
		return (i == null) ? 0 : i;
	}

	@Override
	public List<Carrier> getGroupHoldCarriers(GroupHoldFinderCriteria finderCriteria, Integer page, Integer size) {
		List<CarrierMes> carrierMeslist = CarrierMes
				.findCarriersByRowAndProductionRunNoAndRobotAndProdRunDate(finderCriteria, page, size);
		List<Carrier> carrierList = new ArrayList<Carrier>();
		for (CarrierMes carrierMes : carrierMeslist) {
			carrierList.add(getCarrier(carrierMes));
		}

		return carrierList;
	}

	public AlarmEvent getAlarmEventToDisplay() {
		AlarmEvent alarmEventToDisplay = null;
		List<AlarmEvent> alarmEvents = AlarmEvent.findCurrentUnClearedAlarmsForAppNotify(50);
		LOG.info("uncleared alarms found......." + alarmEvents.size());
		if (alarmEvents != null && alarmEvents.size() > 0) {
			LOG.info("uncleared alarms found.......");
			for (AlarmEvent alarmEvent : alarmEvents) {
				LOG.info(" alarm event- " + alarmEvent.getId());
				AlarmDefinition alarmDefinition = AlarmDefinition
						.findNotificationRequiredAlarmsByAlarmNumberAndLocation(alarmEvent.getAlarmNumber(),
								alarmEvent.getLocation());

				if (alarmDefinition != null) {
					LOG.info("uncleared notification required alarm found.......");
					alarmEventToDisplay = alarmEvent;
					break;
				}
			}
		}

		return alarmEventToDisplay;
	}

	@Override
	public List<OrderFulfillment> getOrderFulfillmentsByOrder(WeldOrder order) {

		List<OrderFulfillment> fulfillments = OrderFulfillment.findAllOrderFulfillmentsByOrder(order);

		List<OrderFulfillment> fulfillmentList = new ArrayList<OrderFulfillment>();
		for (OrderFulfillment fulfillment : fulfillments) {
			CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(fulfillment.getId().getCarrierNumber());
			Stop currentLocation = carrierMes == null ? null : Stop.findStop(carrierMes.getCurrentLocation());
			fulfillment.setCurrentLocation(currentLocation);

			fulfillmentList.add(fulfillment);
		}
		return fulfillmentList;
	}

//    @Override
//    public List<LaneImpl> getStorageRowsForDetailedInventory() {
//        return StorageConfig.getStorageRowsForDetailedInventory();
//    }

	private volatile boolean connected = false;

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public boolean isDisconnected() {
		return !connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	@EventSubscriber(eventClass = ConnectionEventMessage.class, referenceStrength = ReferenceStrength.STRONG)
	public void setConnectionState(ConnectionEventMessage msg) {
		LOG.info("received connection event: " + msg.isConnected());
		setConnected(msg.isConnected());
	}

	@Override
	public BitInfo setBitInfo(List<ParmSetting> parms) {
		BitInfo bi = new BitInfo();
		for (int i = 1; i < 16; i++) {
			boolean show = false;
			switch (i) {
			case 1:
				bi.setShowBit1(show);
				break;
			case 2:
				bi.setShowBit2(show);
				break;
			case 3:
				bi.setShowBit3(show);
				break;
			case 4:
				bi.setShowBit4(show);
				break;
			case 5:
				bi.setShowBit5(show);
				break;
			case 6:
				bi.setShowBit6(show);
				break;
			case 7:
				bi.setShowBit7(show);
				break;
			case 8:
				bi.setShowBit8(show);
				break;
			case 9:
				bi.setShowBit9(show);
				break;
			case 10:
				bi.setShowBit10(show);
				break;
			case 11:
				bi.setShowBit11(show);
				break;
			case 12:
				bi.setShowBit12(show);
				break;
			case 13:
				bi.setShowBit13(show);
				break;
			case 14:
				bi.setShowBit14(show);
				break;
			case 15:
				bi.setShowBit15(show);
				break;
			}
		}
		for (ParmSetting p : parms) {
			for (int i = 1; i < 16; i++) {
				if (("Bit" + i + "Description").equalsIgnoreCase(p.getFieldname())) {
					boolean show = !(p.getFieldvalue() == null || p.getFieldvalue().isEmpty());
					switch (i) {
					case 1:
						bi.setShowBit1(show);
						bi.setBit1Label(p.getFieldvalue());
						break;
					case 2:
						bi.setShowBit2(show);
						bi.setBit2Label(p.getFieldvalue());
						break;
					case 3:
						bi.setShowBit3(show);
						bi.setBit3Label(p.getFieldvalue());
						break;
					case 4:
						bi.setShowBit4(show);
						bi.setBit4Label(p.getFieldvalue());
						break;
					case 5:
						bi.setShowBit5(show);
						bi.setBit5Label(p.getFieldvalue());
						break;
					case 6:
						bi.setShowBit6(show);
						bi.setBit6Label(p.getFieldvalue());
						break;
					case 7:
						bi.setShowBit7(show);
						bi.setBit7Label(p.getFieldvalue());
						break;
					case 8:
						bi.setShowBit8(show);
						bi.setBit8Label(p.getFieldvalue());
						break;
					case 9:
						bi.setShowBit9(show);
						bi.setBit9Label(p.getFieldvalue());
						break;
					case 10:
						bi.setShowBit10(show);
						bi.setBit10Label(p.getFieldvalue());
						break;
					case 11:
						bi.setShowBit11(show);
						bi.setBit11Label(p.getFieldvalue());
						break;
					case 12:
						bi.setShowBit12(show);
						bi.setBit12Label(p.getFieldvalue());
						break;
					case 13:
						bi.setShowBit13(show);
						bi.setBit13Label(p.getFieldvalue());
						break;
					case 14:
						bi.setShowBit14(show);
						bi.setBit14Label(p.getFieldvalue());
						break;
					case 15:
						bi.setShowBit15(show);
						bi.setBit15Label(p.getFieldvalue());
						break;
					}
					break;
				}
			}

		}
		return bi;
	}

}
