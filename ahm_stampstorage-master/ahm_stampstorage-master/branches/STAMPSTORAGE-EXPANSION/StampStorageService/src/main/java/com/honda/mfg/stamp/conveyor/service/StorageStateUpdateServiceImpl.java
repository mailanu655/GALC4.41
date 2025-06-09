package com.honda.mfg.stamp.conveyor.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.stamp.conveyor.domain.AlarmDefinition;
import com.honda.mfg.stamp.conveyor.domain.AlarmEvent;
import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.CarrierRelease;
import com.honda.mfg.stamp.conveyor.domain.OrderFulfillment;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.WeldOrder;
import com.honda.mfg.stamp.conveyor.domain.enums.AlarmTypes;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.manager.Storage;
import com.honda.mfg.stamp.conveyor.manager.StorageConfig;
import com.honda.mfg.stamp.conveyor.manager.StorageState;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateMessage;
import com.honda.mfg.stamp.conveyor.messages.StaleDataMessage;
import com.honda.mfg.stamp.conveyor.messages.StatusUpdateMessage;
import com.honda.mfg.stamp.conveyor.processor.ConnectionEventMessage;
import com.honda.mfg.stamp.conveyor.release.ReleaseManager;

/**
 * User: vcc30690 Date: 5/25/11
 */
public class StorageStateUpdateServiceImpl implements StorageStateUpdateService {
	private static final Logger LOG = LoggerFactory.getLogger(StorageStateUpdateServiceImpl.class);

	private Storage storage;
	private ReleaseManager releaseManager;
	private AlarmServiceHelper helper;

	public StorageStateUpdateServiceImpl(Storage storage, ReleaseManager releaseManager,
			AlarmServiceHelper alarmServiceHelper) {
		this.storage = storage;
		this.releaseManager = releaseManager;
		this.helper = alarmServiceHelper;
		AnnotationProcessor.process(this);
	}

	public void saveCarrier(Carrier carrier) {
		LOG.info("Saving carrier:  " + carrier);
		CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(carrier.getCarrierNumber());
		if (carrier.getSubmitToReleaseManager() && carrierMes.getBuffer().intValue() == 0
				&& !carrierMes.getDestination().equals(carrier.getDestination().getId())
				&& !carrierMes.getCurrentLocation().equals(carrier.getDestination().getId())) {
			Long carrierNumber = Long.parseLong(carrier.getCarrierNumber().toString());
			LOG.info(" Adding Carrier-" + carrierNumber + " to release table");
			CarrierRelease.saveCarrierRelease(carrierNumber, carrier.getCurrentLocation(), carrier.getDestination(),
					carrier.getSource());
		} else {
			if (carrierMes != null && carrier.getDestination() != null) {

				String carrierNumber = carrier.getCarrierNumber().toString();
				CarrierRelease carrierRelease = CarrierRelease.findCarrierRelease(Long.parseLong(carrierNumber));

				if (carrierRelease != null) {
					if (!carrierRelease.getDestination().equals(carrier.getDestination())) {
						carrierRelease.remove();
					}
				}
			}
			Carrier c = new Carrier();
			c.setCarrierNumber(carrier.getCarrierNumber());
			c.setDestination(carrier.getDestination());
			c.setDie(carrier.getDie());
			c.setQuantity(carrier.getQuantity());
			c.setCarrierStatus(carrier.getCarrierStatus());
			c.setCurrentLocation(carrier.getCurrentLocation());
			c.setBuffer(carrier.getBuffer());
			c.setReprocess(carrier.getReprocess());
			c.setSource(carrier.getSource());
			// TODO:CHECK FOR MAINT BITS Does Update Touch This? YES
			// MG add Maint Bits
			c.setMaintenanceBits(carrier.getMaintenanceBits());
			storage.sendCarrierUpdateMessage(c);
		}
	}

	public void storeReworkCarrier(Carrier carrier) {
		CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(carrier.getCarrierNumber());
		if (carrierMes != null) {
			Stop currentLocation = Stop.findStop(carrierMes.getCurrentLocation());
			carrier.setCurrentLocation(currentLocation);
		}
		storage.store(carrier);
	}

	public StorageState getStorageState() {
		return storage.getStorageState();
	}

	public void saveCarriersInToRow(List<Carrier> carriers, Stop laneStop) {
		// int count = 1;
		// release all carriers from lane to make it empty
		int index = 0;
		List<Carrier> carrierList = new ArrayList<Carrier>();
		if (!getStorageState().isStale()) {

			for (Carrier c : carriers) {

				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.SECOND, 1);

				if (index == 0) {
					carriers.get(index).setBuffer(Integer.valueOf(1));
					c.setBuffer(1);
				} else {
					c.setBuffer(0);
				}
				Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
				c.setUpdateDate(timestamp);
				c.setSource(StorageConfig.OHCV_APP_RESEQUENCE);
				LOG.info("Carrier - " + c.getCarrierNumber() + "-" + c.getUpdateDate().toString());
				CarrierUpdateMessage carrierUpdateMessage = new CarrierUpdateMessage(c);
				EventBus.publish(carrierUpdateMessage);
				carrierList.add(c);
//                count++;
				index++;

				helper.pause(2);
			}
			getStorageState().addCarriersToLane(carrierList, laneStop);
		}
	}

	public void removeCarrierFromRow(Integer carrierNumber) {

		List<Stop> maintenanceStops = Stop.findAllStopsByType(StopType.MAINTENANCE);
		if (maintenanceStops != null && maintenanceStops.size() > 0) {
			Carrier c = new Carrier();
			c.setCarrierNumber(carrierNumber);
			c.setCurrentLocation(maintenanceStops.get(0));
			c.setDestination(maintenanceStops.get(0));
			c.setBuffer(Integer.valueOf(0));
			c.setSource(StorageConfig.OHCV_APP_RESEQUENCE);
			getStorageState().removeCarrierFromStorageState(c);
			storage.getStorageState().sendCarrierUpdateMessage(c);
		}

	}

	public void addCarrierToRow(Integer carrierNumber, Integer position, Stop laneStop) {

		List<StorageRow> lanes = getStorageState().getRows();
		List<Carrier> carriersList = new LinkedList<Carrier>();
		for (StorageRow lane : lanes) {
			if (lane.getStop().equals(laneStop)) {
				carriersList = lane.getCarriersAsLinkedList();
			}
		}

		Integer carrierPosition = Integer.valueOf(1);
		List<Carrier> newCarriersList = new ArrayList<Carrier>();
		Carrier newCarrier = new Carrier();
		// TODO:Add Carrier To row.
		newCarrier.setCarrierNumber(carrierNumber);
		newCarrier.setCurrentLocation(laneStop);
		newCarrier.setDestination(laneStop);
		if (carriersList.size() > 0) {
			for (Carrier c : carriersList) {
				if (carrierPosition.equals(position)) {
					newCarriersList.add(newCarrier);
				}
				newCarriersList.add(c);
				carrierPosition++;
			}
			if (position > carriersList.size()) {
				newCarriersList.add(newCarrier);
			}
		} else {
			newCarriersList.add(newCarrier);
		}
		saveCarriersInToRow(newCarriersList, laneStop);
	}

	@Override
	public void recalculateCarrierDestination(Carrier carrier) {
		storage.recalculateCarrierDestination(carrier);
	}

	@Override
	public void releaseCarriers(Stop rowStop, int count, Stop destination, String source) {

		int i = 0;
		LOG.debug("releasing carrier from row with associated stop --" + rowStop.getName());
		if (rowStop.getStopArea().equals(StopArea.Q_WELD_LINE_1)
				|| rowStop.getStopArea().equals(StopArea.Q_WELD_LINE_2)) {
			List<CarrierMes> carriers = CarrierMes.findAllCarriersWithCurrentLocation(rowStop.getId());
			for (CarrierMes carrier : carriers) {
				if (i < count) {
					releaseManager.releaseCarrier(carrier.getCarrierNumber(), destination,
							StorageConfig.OHCV_APP_MANUAL_ORDER + "-" + source, true);
				} else {
					break;
				}
				i++;
			}

		} else {
			List<StorageRow> lanes = getStorageState().getRows();
			for (StorageRow tempLane : lanes) {
				if (tempLane.getStop().equals(rowStop)) {
					StorageRow lane = tempLane;
					LOG.debug("releasing carrier from row --" + lane.getRowName());
					while (i < count) {
						if (!lane.isEmpty()) {
							try {
								Carrier carrier = lane.release();
								Integer carrierNo = carrier.getCarrierNumber();
								if (carrierNotSelectedForAnyOrder(carrier)) {

									CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(carrierNo);
									if (carrierMes != null) {
										if (carrierMes.getCurrentLocation().equals(lane.getStop().getId())) {
											releaseManager.releaseCarrier(carrierNo, destination,
													StorageConfig.OHCV_APP_MANUAL_ORDER + "-" + source, true);
											storage.getStorageState().releaseCarrierIfExistsAtHeadOfLane(carrier);
										} else {

											LOG.info("carrier -" + carrierNo + " is not in the row " + lane.getRowName()
													+ " yet ");
										}
									}
									i++;
								} else {
									LOG.info(carrierNo
											+ " carrier is part of an active order so cannot select for manual order");
								}
							} catch (Exception e) {
								LOG.debug(e.getMessage());
							}
						} else {
							break;
						}
					}
				}
			}
		}
	}

	private boolean carrierNotSelectedForAnyOrder(Carrier carrier) {
		WeldOrder order = OrderFulfillment.findOrderByCarrierInRows(carrier.getCarrierNumber());
		if (order == null) {
			return true;
		}
		return false;
	}

	@Override
	public void reorderCarriersInRow(Long stopId) {
		getStorageState().reorderCarriersInRow(stopId);
	}

	public void releaseEmptyCarriersFromRows(StorageArea area, boolean releaseMgr, String source) {
		// PartProductionVolume volume =
		// StorageConfig.getStorageAreaPartProductionVolumeMap().get(area);
		Stop emptyCarrierDeliveryStop = null;

		try {
			List<Stop> stops = Stop.findAllStopsByTypeAndArea(StopType.EMPTY_CARRIER_DELIVERY, StopArea.OLD_WELD_LINE);

			if (stops.size() > 0) {
				emptyCarrierDeliveryStop = stops.get(0);
			}

			if (emptyCarrierDeliveryStop != null) {
				LOG.info("StorageStateUpdate--" + area.name());
				StorageRow lane = storage.retrieveEmptyCarrier(area);

				if (lane != null) {

					int count = lane.getCurrentCarrierCount();
					int i = 0;
					while (i < count) {
						if (!lane.isEmpty()) {
							Carrier carrier = lane.release();
							carrier.setDestination(emptyCarrierDeliveryStop);
							Integer carrierNo = carrier.getCarrierNumber();
							CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(carrierNo);
							if (carrierMes != null) {
								if (carrierMes.getCurrentLocation().equals(lane.getStop().getId())
										&& carrierMes.getDieNumber().equals(Integer.valueOf(999))) {
									releaseManager.releaseCarrier(carrier.getCarrierNumber(), emptyCarrierDeliveryStop,
											StorageConfig.OHCV_APP_MANUAL_EMPTY + "-" + source, releaseMgr);
									storage.getStorageState().releaseCarrierIfExistsAtHeadOfLane(carrier);
								} else {
									LOG.info("carrier -" + carrierNo + " is not in the row " + lane.getRowName()
											+ " yet or is not an Empty Carrier ");
									break;
								}
							}
						} else {
							break;
						}
						i++;
					}
				} else {
					LOG.info("No Row With Empty Carriers Found in " + area);
				}
			} else {
				LOG.info("No Empty Carrier Delivery Set For" + area);
			}

		} catch (Exception e) {
			LOG.debug(e.getMessage());
		}
	}

	public void reloadStorageState() {
		LOG.info(" BEFORE REFRESH \n " + storage.getStorageState().toString());
		storage.reloadStorageState();
		LOG.info(" AFTER REFRESH \n " + storage.getStorageState().toString());
	}

	public void updateStorageRow(Long stopId) {
		StorageRow row = StorageRow.findStorageRowsByStop(Stop.findStop(stopId));
		storage.getStorageState().updateLane(row);
		reloadStorageState();
	}

	@Override
	public void clearAlarm(Integer alarmNumber) {
		AlarmDefinition alarmDefinition = AlarmDefinition.findAlarmByAlarmNumberAndLocation(alarmNumber, 0);

		if (alarmDefinition.getQpcNotificationRequired()) {
			helper.resetAlarm(alarmNumber);

			LOG.info(" Published Alarm Message:");
		}
	}

	@Override
	public void resetStorageState() {
		LOG.info(" BEFORE RESET \n " + storage.getStorageState().toString() + "\n BackOrder -"
				+ storage.getStorageState().getBackOrder().toArray().toString());
		storage.resetStorageStateAndBackOrder();
		LOG.info(" AFTER RESET \n " + storage.getStorageState().toString() + "\n BackOrder -"
				+ storage.getStorageState().getBackOrder().toArray().toString());
	}

	@Override
	public void sendBulkCarrierStatusUpdate(List<Carrier> carriers, CarrierStatus status, String user) {
		List<List<Integer>> carrierList = new ArrayList<List<Integer>>();

		int i = 0;
		List<Integer> numbers = new ArrayList<Integer>();
		for (Carrier carrier : carriers) {
			numbers.add(carrier.getCarrierNumber());
			i++;
			if (i > 9) {
				carrierList.add(numbers);
				i = 0;
				numbers = new ArrayList<Integer>(10);
			}
		}
		if (i < 9 && i > 0) {
			carrierList.add(numbers);
		}

		for (List<Integer> list : carrierList) {
			StatusUpdateMessage statusUpdateMessage = new StatusUpdateMessage(status, list, user);
			EventBus.publish(statusUpdateMessage);

			helper.pause(10);
		}

		EventBus.publish(new StaleDataMessage(false));
	}

	private volatile boolean qpcConnected = true;

	public boolean isQpcConnected() {
		return qpcConnected;
	}

	public void setQpcConnected(boolean qpcConnected) {
		this.qpcConnected = qpcConnected;
	}

	@EventSubscriber(eventClass = ConnectionEventMessage.class, referenceStrength = ReferenceStrength.STRONG)
	public void setConnectionState(ConnectionEventMessage msg) {
		LOG.info("received connection event: " + msg.isConnected());
		setQpcConnected(msg.isConnected());

		List<AlarmEvent> alarmEvents = helper.getUnclearedAlarmsByType(AlarmTypes.MES_CONNECTION_UNHEALTHY.type());
		try {
			if (!isQpcConnected()) { // disconnected, or MesUninitialized

				if (alarmEvents == null || alarmEvents.size() == 0) {
					LOG.debug("StorageStateUpdateService#setConnectionState(): inserting MES Unhealthy alarm");

					helper.generateAlarm(0, AlarmTypes.MES_CONNECTION_UNHEALTHY.type());

				} else if (alarmEvents != null && alarmEvents.size() > 0) {
					LOG.debug(
							"StorageStateUpdateService#setConnectionState(): uncleared MES Unhealthy alarm found - not inserting");
				}

			} else if (alarmEvents != null) { // connected, or MesInitialized

				if (alarmEvents.size() == 0) {
					LOG.debug("StorageStateUpdateService#setConnectionState(): no MES unhealthy alarms to delete");
				} else {
					LOG.debug(
							"StorageStateUpdateService#setConnectionState(): uncleared MES Unhealthy alarm found - clearing and archiving");
					for (AlarmEvent alarmEvent : alarmEvents) {
						helper.archiveAlarm(alarmEvent.getId(), "StorageStateUpdateService");
					}
				}
			}
		} catch (Exception e) {
			LOG.debug(e.getMessage());
			e.printStackTrace();
		}
	}
}
