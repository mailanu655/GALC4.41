package com.honda.mfg.stamp.conveyor.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.stamp.conveyor.domain.Carrier;
import com.honda.mfg.stamp.conveyor.domain.CarrierMes;
import com.honda.mfg.stamp.conveyor.domain.Die;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.StorageRow;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.exceptions.NoApplicableRuleFoundException;
import com.honda.mfg.stamp.conveyor.rules.empty.EmptyManagerImpl;

/**
 * User: Jeffrey M Lutz Date: Jan 24, 2011
 */
public class StorageImpl implements Storage {
	private static final Logger LOG = LoggerFactory.getLogger(StorageImpl.class);

	private StoreInManager storeInManager;
	private StoreOutManager storeOutManager;
	private EmptyManagerImpl emptyManager;
	private StorageStateContext storageStateContext;

	public StorageImpl(StoreInManager storeInManager, StoreOutManager storeOutManager, EmptyManagerImpl emptyManager,
			StorageStateContext storageStateContext) {
		this.storeInManager = storeInManager;
		this.storeOutManager = storeOutManager;
		this.emptyManager = emptyManager;
		this.storageStateContext = storageStateContext;
	}

	public void store(Carrier carrier) {
		StorageRow storeInRow = null;
		Stop stop = carrier.getCurrentLocation();
		StopType type = stop.getStopType();
		StopArea area = stop.getStopArea();
		Die die = carrier.getDie();
		if (!die.getActive()) {
			LOG.info("Inactive die----id: " + die.getId());
		} else if ((carrier.getDie().getId().equals(999L))
				&& (type.equals(StopType.STORE_IN_ALL_LANES) || type.equals(StopType.REWORK))) {
			storeInRow = emptyManager.storeEmptyCarrier(carrier);
		} else {
			switch (type) {
			case STORE_IN_ALL_LANES:
				storeInRow = storeInManager.store(carrier);
				break;
			case STORE_IN_C_LOW_LANES:
				storeInRow = storeInManager.subStore(carrier, StorageArea.C_LOW);
				break;
			case STORE_IN_C_HIGH_LANES:
				storeInRow = storeInManager.subStore(carrier, StorageArea.C_HIGH);
				break;
			case RECIRC_TO_ALL_ROWS:
				if ((area.equals(StopArea.STORE_IN_ROUTE) || area.equals(StopArea.STORE_OUT_ROUTE))
						&& storageStateContext.spaceAvailable(stop)) {
					try {
						storeInRow = storeInManager.subStore(carrier, StorageArea.A_AREA);
					} catch (NoApplicableRuleFoundException exception) {
						storeInRow = storeInManager.store(carrier);
					}
				} else {
					storeInRow = storeInManager.store(carrier);
				}
				break;
			case REWORK:
				storeInRow = storeInManager.store(carrier);
				break;
			}

		}
		if (storeInRow != null) {
			LOG.info("Store In Lane Info---- " + storeInRow.getId());
			carrier.setAlreadyInLane(false);
			getStorageState().storeInLane(carrier, storeInRow);
		}
	}

	@Override
	public StorageRow retrieve(Die die) {
		return storeOutManager.retrieve(die);
	}

	@Override
	public StorageRow retrieveEmptyCarrier() {
		return emptyManager.retrieveEmptyCarrier();
	}

	@Override
	public StorageRow retrieveEmptyCarrier(StorageArea area) {
		return emptyManager.retrieveEmptyCarrier(area);
	}

	@Override
	public StorageRow retrieveEmptyCarrierForOldWeldLine() {
		return emptyManager.retrieveEmptyCarrierForOldWeldLineEmptyStorage();
	}

	@Override
	public StorageRow retrieveEmptyCarrierForBAreaEmptyStorage() {
		return emptyManager.retrieveEmptyCarrierForBAreaEmptyStorage();
	}

	@Override
	public void sendCarrierUpdateMessage(Carrier carrier) {
		// release from lane if carrier is updated to be moved out of lann ve
		// if carrier exists in storage state and is at head on the lane then release
		// carrier from lane
		if (!storageStateContext.getStorageState().isStale()) {
			Stop currentLocation = null;
			Stop destination = null;
			CarrierMes carrierMes = storageStateContext.getCarrier(carrier.getCarrierNumber());
			if (carrierMes != null) {
				Carrier carrierToUpdate = new Carrier();
				carrierToUpdate.setCarrierNumber(carrierMes.getCarrierNumber());

				if (carrier.getCurrentLocation() != null) {
					currentLocation = carrier.getCurrentLocation();
					if (!carrierMes.getCurrentLocation().equals(currentLocation.getId())) {
						carrierToUpdate.setCurrentLocation(currentLocation);
					}
				} else {
					currentLocation = Stop.findStop(carrierMes.getCurrentLocation());
				}

				if (carrier.getDestination() != null) {
					destination = carrier.getDestination();
					if (!carrierMes.getDestination().equals(destination.getId())) {
						carrierToUpdate.setDestination(destination);
					}
				} else {
					destination = Stop.findStop(carrierMes.getDestination());
				}

				Long dieId = carrier.getDie() == null ? Long.valueOf(carrierMes.getDieNumber().toString())
						: carrier.getDie().getId();
				Integer quantity = carrier.getQuantity() == null ? carrierMes.getQuantity() : carrier.getQuantity();
				CarrierStatus status = carrier.getCarrierStatus() == null
						? CarrierStatus.findByType(carrierMes.getStatus())
						: carrier.getCarrierStatus();

				if (!quantity.equals(carrierMes.getQuantity())
						|| !status.equals(CarrierStatus.findByType(carrierMes.getStatus()))
						|| !(dieId.equals(Long.valueOf(carrierMes.getDieNumber().toString())))) {
					getStorageState().updateCarrier(carrier);
					LOG.info("updating die, quantity, carrier status for carrier-" + carrier.getCarrierNumber());
					carrierToUpdate.setCarrierStatus(carrier.getCarrierStatus());
					carrierToUpdate.setDie(carrier.getDie());
					carrierToUpdate.setQuantity(carrier.getQuantity());
				}

				carrierToUpdate.setBuffer(carrier.getBuffer());
				carrierToUpdate.setReprocess(carrier.getReprocess());
				carrierToUpdate.setSource(carrier.getSource());
				// MG add Maintenance Bits
				// TODO:CHECK FOR MAINT BITS
				carrierToUpdate.setMaintenanceBits(carrier.getMaintenanceBits());

				getStorageState().sendCarrierUpdateMessage(carrierToUpdate);

				if (!currentLocation.isRowStop() && !destination.isRowStop()) {
					LOG.info("not rows-" + currentLocation + "-" + destination);
					if (getStorageState().carrierExistsInStorageState(carrier)) {
						getStorageState().removeCarrierFromStorageState(carrier);
					}
				} else {
					if (currentLocation.isRowStop()) {
						if (!currentLocation.equals(destination)) {
							LOG.info("releasing carrier: " + carrierToUpdate.getCarrierNumber() + " in row "
									+ currentLocation + " with a destination of: " + destination);
							getStorageState().releaseCarrierIfExistsAtHeadOfLane(carrier);
						} else {
							LOG.info("carrier: " + carrierToUpdate.getCarrierNumber() + " with currentLocation: "
									+ currentLocation + " has a destination of " + destination + "- reordering row.");
							getStorageState().reorderCarriersInRow(currentLocation.getId());
						}
					} else if (destination.isRowStop()) {
						getStorageState().storeInLaneIfDestinationIsALaneAndAlreadyNotExistsInStorageSystem(carrier);
					}

				}
				getStorageState().updateCarrier(carrier);
			} else {
				getStorageState().sendCarrierUpdateMessage(carrier);
			}
		} else {
			LOG.info("StorageState is Stale");
		}
	}

	public StorageState getStorageState() {
		return storageStateContext.getStorageState();
	}

	public void reloadStorageState() {
		List<BackOrder> tempBackOrder = storageStateContext.getStorageState().getBackOrder();

		storageStateContext.reload();
		storageStateContext.getStorageState().setBackOrder(tempBackOrder);
	}

	public void resetStorageStateAndBackOrder() {
		storageStateContext.reload();
	}

	public void recalculateCarrierDestination(Carrier c) {
		CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(c.getCarrierNumber());

		Carrier carrier = storageStateContext.populateCarrier(carrierMes);
		carrier.setDie(c.getDie());

		if (carrier != null) {
			if (carrier.getDestination().isRowStop()) {
				getStorageState().removeCarrierFromRow(c.getCarrierNumber(), carrierMes.getDestination());
			}
			store(carrier);
		}
	}

}
