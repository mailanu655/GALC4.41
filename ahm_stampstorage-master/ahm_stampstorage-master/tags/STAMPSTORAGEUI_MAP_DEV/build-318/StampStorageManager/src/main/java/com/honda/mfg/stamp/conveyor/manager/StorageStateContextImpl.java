package com.honda.mfg.stamp.conveyor.manager;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.messages.StaleDataMessage;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.bushe.swing.event.annotation.ReferenceStrength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * User: Jeffrey M Lutz
 * Date: 6/19/11
 */
public class StorageStateContextImpl implements StorageStateContext, StorageLifeCycle {
    private static final Logger LOG = LoggerFactory.getLogger(StorageStateContextImpl.class);

    private StorageStateContextHelper helper;

    private List<CarrierMes> carriersInRow;
    private List<CarrierMes> carriersMovingToRow;

    private List<CarrierMes> carriersWithInvalidDestination;
    private StorageState storageState;

    private List<StorageRow> carrierPopulatedRows;
    private Map<Long, StorageRow> lastUsedRowMap;

    public StorageStateContextImpl(StorageStateContextHelper helper) {
        this.helper = helper;
        AnnotationProcessor.process(this);
        this.lastUsedRowMap = new HashMap<Long, StorageRow>();
    }

    @EventSubscriber(eventClass = StaleDataMessage.class, referenceStrength = ReferenceStrength.STRONG)
    public void healthCheck(StaleDataMessage msg) {
        LOG.info("Received StaleDataMessage.  Stale? " + msg.isStale());
        if (msg != null && msg.isStale() && storageState != null) {
            storageState.setStale(msg.isStale());
        }
        if (msg != null && !msg.isStale()) {
            getStorageState().setStale(msg.isStale());
            reload();
        }
    }

    public void reload() {
        if(storageState != null)  this.storageState.setStale(true);
        initializeState();
        rebuildLanes();

        this.storageState = new StorageStateImpl(carrierPopulatedRows);
         this.storageState.setStale(false);
    }

    @Override
    public StorageState getStorageState() {
        if (storageState == null) {
            reload();
        }

        // recalculateDestinations(carriersWithInvalidDestination) ;
        return storageState;
    }

//    private void recalculateDestinations(List<CarrierMes> carriersWithInvalidDestination) {
//        EventBus.publish(new RecalculateDestinationMessage(true));
//    }

    private void initializeState() {
        this.carriersWithInvalidDestination = new ArrayList<CarrierMes>();

        this.carrierPopulatedRows = new ArrayList<StorageRow>();
        carriersInRow = new ArrayList<CarrierMes>();
        carriersMovingToRow = new ArrayList<CarrierMes>();
    }

    private void rebuildLanes() {
        rebuildActualCarriersInRows();
        StorageArea[] areas = StorageArea.values();
        List<StorageRow> allRows = new ArrayList<StorageRow>();
        for (int i = 0; i < areas.length; i++) {
            if (!areas[i].equals(StorageArea.Q_AREA)) {
                List<StorageRow> rows = helper.findAllStorageRowsByStorageArea(areas[i]);
                allRows.addAll(rows);
            }
        }

        for (StorageRow row : allRows) {
            StorageRow populatedRow = populateRowWithCarriers(row);
            this.carrierPopulatedRows.add(populatedRow);
        }
    }

    public StorageRow populateRowWithCarriers(final StorageRow row) {
        List<CarrierMes> carriersForRow = findAllCarriersForRow(row);
        for (CarrierMes carrierMes : carriersForRow) {
            try {
                row.store(populateCarrier(carrierMes));
            } catch (IllegalStateException e) {
                carriersWithInvalidDestination.add(carrierMes);
            }
        }
        return row;
    }


    private List<CarrierMes> findAllCarriersForRow(StorageRow row) {
        Long rowStopId = row.getStop().getId();
        List<CarrierMes> carriersForRow = new ArrayList<CarrierMes>();
        for (CarrierMes carrierMes : carriersInRow) {
            if (carrierMes.getDestination().equals(rowStopId)) {
                carriersForRow.add(carrierMes);
            }
        }

        for (CarrierMes carrierMes : carriersMovingToRow) {
            if (carrierMes.getDestination().equals(rowStopId)) {
                carriersForRow.add(carrierMes);
            }
        }
        return carriersForRow;
    }

    private void rebuildActualCarriersInRows() {
        List<CarrierMes> allCarriersInStorage = helper.findAllCarriersInStorage();

        for (CarrierMes carrierMes : allCarriersInStorage) {
            if (isCarrierInRow(carrierMes)) {
                carriersInRow.add(carrierMes);
            } else {
                carriersMovingToRow.add(carrierMes);
            }
        }

        sortCarriersLists();
    }

    private void sortCarriersLists() {
        Collections.sort(carriersInRow, new CarrierMesInStorageLanesComparator());
        Collections.sort(carriersMovingToRow, new CarrierMesInStorageLanesComparator());
    }

    private boolean isCarrierInRow(CarrierMes carrierMes) {
        Long currentLocation = carrierMes.getCurrentLocation();
        Long destination = carrierMes.getDestination();
        return currentLocation.equals(destination);
    }

    public List<CarrierMes> getCarriersWithInvalidDestination() {
        return carriersWithInvalidDestination;
    }

    @Override
    public Carrier populateCarrier(CarrierMes carrierMes) {
        return helper.populateCarrier(carrierMes);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public CarrierMes getCarrier(Integer carrierNumber) {
        return CarrierMes.findCarrierByCarrierNumber(carrierNumber);
    }

    public void addDie(Long dieNumber, StorageRow row) {
        LOG.info(" adding Row and Die-" + dieNumber + "---" + row.getRowName());
        lastUsedRowMap.put(dieNumber, row);
    }

    public StorageRow getRow(Die die) {
        LOG.info(" get Row for Die-" + die.getId());
        return lastUsedRowMap.get(die.getId());
    }

    @Override
    public void saveToAuditLog(String nodeId, String message, String source) {
        helper.saveToAuditLog(nodeId, message, source);
    }

    @Override
    public Die getEmptyDie() {
        return helper.getEmptyDie();
    }

    @Override
    public boolean spaceAvailable(Stop stop) {
        return helper.spaceAvailable(stop);  //To change body of implemented methods use File | Settings | File Templates.
    }
}
