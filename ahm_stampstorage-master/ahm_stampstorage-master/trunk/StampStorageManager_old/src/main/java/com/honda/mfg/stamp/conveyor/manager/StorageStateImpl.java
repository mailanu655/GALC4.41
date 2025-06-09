package com.honda.mfg.stamp.conveyor.manager;

import com.google.common.collect.Iterables;
import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.Press;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import com.honda.mfg.stamp.conveyor.messages.CarrierUpdateMessage;
import org.bushe.swing.event.EventBus;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * User: Adam S. Kendell
 * Date: Mar 1, 2011
 */
public class StorageStateImpl implements StorageState {
    private static final Logger LOG = LoggerFactory.getLogger(StorageStateImpl.class);


    private List<StorageRow> rows;
    private Set<Long> backOrderedParts;
    private boolean stale;


    public StorageStateImpl(List<StorageRow> rows) {
        this.rows = rows;
        this.stale = false;
    }

    public StorageStateImpl(List<StorageRow> rows, Set<Long> backOrderedParts) {
        this.rows = rows;
        this.backOrderedParts = backOrderedParts;
    }

    public Set<Long> getBackOrderedParts() {
        return this.backOrderedParts;
    }

    public void setBackOrderedParts(Set<Long> backOrderedParts) {
        this.backOrderedParts = backOrderedParts;
    }

    public boolean isStale() {
        return stale;
    }

    public void setStale(boolean stale) {
        this.stale = stale;
    }

    public List<StorageRow> getRows() {
        return rows;
    }

    public boolean isCarrierPartsOnBackOrder(Carrier carrier) {
        if (backOrderedParts == null) {
            return false;
        }
        return backOrderedParts.contains(carrier.getDie().getId());
    }

    private void removeFromBackOrderList(Carrier carrier) {
        try {
            if (isCarrierPartsOnBackOrder(carrier)) {
                backOrderedParts.remove(carrier.getDie().getId());
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
            //e.printStackTrace();
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof StorageStateImpl)) {
            return false;
        }
        // LHS = LEFT HAND SIDE, RHS = RIGHT HAND SIDE.  This is equality comparison
        StorageStateImpl lhs = this;
        StorageStateImpl rhs = (StorageStateImpl) obj;
        return areLanesSame(lhs.rows, rhs.rows);
    }

    private boolean areLanesSame(List<StorageRow> lhsLanes, List<StorageRow> rhsLanes) {
        if (lhsLanes == null) {
            return rhsLanes == null;
        }
        if (rhsLanes == null) {
            return false;
        }
        // NOTE: Sequence of lanes is very important.
        // So, left lane number two must be the exact same as right lane number two.
        for (int i = 0; i < lhsLanes.size(); i++) {
            StorageRow lhsLane = lhsLanes.get(i);
            StorageRow rhsLane = rhsLanes.get(i);
            if (!lhsLane.equals(rhsLane)) {
                return false;
            }
        }
        return true;
    }

    public StorageRow queryForRow(Matcher<StorageRow> matcher) {
        List<StorageRow> rows = queryForRows(matcher);
        return rows == null || rows.size() == 0 ? null : rows.get(0);
    }


    public List<StorageRow> queryForRows(Matcher<StorageRow> matcher) {
        Iterable<StorageRow> filteredRows = matcher == null ? rows : Iterables.filter(rows, new PredicateWrapper<StorageRow>(matcher));
        if (filteredRows != null && filteredRows.iterator().hasNext()) {
            List<StorageRow> rows = new ArrayList<StorageRow>();

            for (StorageRow row : filteredRows) {
                rows.add(row);
            }

            return rows;
        }
        return null;
    }


    public List<StorageRow> queryForRows(Matcher<StorageRow> matcher, List<StorageRow> storageRows) {

        if (storageRows != null) {
            Iterable<StorageRow> filteredRows = matcher == null ? storageRows : Iterables.filter(storageRows, new PredicateWrapper<StorageRow>(matcher));

            if (filteredRows != null && filteredRows.iterator().hasNext()) {
                List<StorageRow> rows = new ArrayList<StorageRow>();
                for (StorageRow row : filteredRows) {
                    rows.add(row);
                }
                return rows;
            }
        }
        return null;
    }


    public Carrier storeInLane(Carrier carrier, StorageRow row) {//Lane lane) {

        if (!carrier.getCarrierStatus().equals(CarrierStatus.ON_HOLD)) {
            removeFromBackOrderList(carrier);
        }

        for (StorageRow storageRow : rows) {

            if (storageRow.getStop().equals(row.getStop())) {

                Carrier newCarrier = new Carrier();
                newCarrier.setCarrierNumber(carrier.getCarrierNumber());
                newCarrier.setDestination(storageRow.getStop());
                newCarrier.setSource(StorageConfig.OHCV_APP_STOREIN);
                if (carrier.getCurrentLocation() != null && carrier.getCurrentLocation().getStopType().equals(StopType.REWORK)) {
                    newCarrier.setDie(carrier.getDie());
                    newCarrier.setQuantity(carrier.getQuantity());
                    newCarrier.setCarrierStatus(carrier.getCarrierStatus());
                    newCarrier.setProductionRunNo(carrier.getProductionRunNo());
                    newCarrier.setStampingProductionRunTimestamp(carrier.getStampingProductionRunTimestamp());
                }
                sendCarrierUpdateMessage(newCarrier);
                storageRow.store(carrier);

                break;
            }
        }
        return carrier;
    }


    public Carrier releaseCarrierFromLane(StorageRow row) {
        // clearLastReleasedFlag();
        Carrier carrier = null;
        for (StorageRow storageRow : rows) {
            if (storageRow.getStop().equals(row.getStop()) && !storageRow.isEmpty()) {
                carrier = storageRow.release();

                break;
            }
        }

        return carrier;
    }

    public void sendCarrierUpdateMessage(Carrier carrier) {
        if (carrier.getDestination() != null) {
            LOG.debug("sent carrier update Message" + carrier.getDestination().toString());
        }
        CarrierUpdateMessage carrierUpdateMsg = new CarrierUpdateMessage(carrier);
        EventBus.publish(carrierUpdateMsg);
    }

    @Override
    public void updateCarrier(Carrier carrier) {
        for (StorageRow row : rows) {
            if (row.carrierExistsInRow(carrier)) {
                row.updateCarrier(carrier);
            }
        }
    }

    @Override
    public void releaseCarrierIfExistsAtHeadOfLane(Carrier carrier) {
        if (carrier != null) {
            LOG.debug("releasing carrier from Row --" + carrier.getCarrierNumber());
            for (StorageRow row : rows) {
                if (row.carrierExistsInRow(carrier)) {
                    Carrier carrierOut = row.getCarrierAtRowOut();

                    if (carrier.getCarrierNumber().equals(carrierOut.getCarrierNumber())) {
                        if (!carrier.getDestination().equals(row.getStop())) {
                            LOG.debug("releasing carrier " + carrier.getCarrierNumber() + " from Row --" + row.getRowName());
                            row.release();
                            break;
                        }
                    } else {
                        LOG.debug("Carrier " + carrier.getCarrierNumber() + "not at head of Row" + row.getRowName());
                    }
                }
            }
        }
    }

    @Override
    public void storeInLaneIfDestinationIsALaneAndAlreadyNotExistsInStorageSystem(Carrier carrier) {
        if (!carrierExistsInStorageState(carrier)) {
            for (StorageRow row : rows) {

                if (row.getStop().equals(carrier.getDestination())) {
                    row.store(carrier);
                    break;
                }
            }
        }
    }

    @Override
    public boolean carrierExistsInStorageState(Carrier carrier) {
        boolean exists = false;
        for (StorageRow row : rows) {
            if (row.carrierExistsInRow(carrier)) {
                exists = true;
                break;
            }
        }

        return exists;
    }

    //return position of carrier in lane
    public int getCarrierPositionInLane(Long laneStopConveyorId, Integer carrierNumber) {
        int position = 0;
        for (StorageRow lane : rows) {
            if (lane.getStop().getId().equals(laneStopConveyorId)) {
                LinkedList<Carrier> carrierLinkedList = lane.getCarriersAsLinkedList();
                if (carrierLinkedList != null) {
                    for (int i = 0; i < carrierLinkedList.size(); i++) {
                        if (carrierNumber.equals(carrierLinkedList.get(i).getCarrierNumber())) {
                            position = i + 1;
                            break;
                        }
                    }
                }
            }
        }
        return position;
    }

    //remove Carrier from lane
    public void removeCarrierFromRow(Integer carrierNumber, Long laneStopConveyorId) {

        List<CarrierMes> carrierMesList = CarrierMes.getAllCarriersInLane(laneStopConveyorId);
        List<Carrier> carrierLinkedList = new LinkedList<Carrier>();
        List<Carrier> movingCarrierLinkedList = new LinkedList<Carrier>();

        if (carrierMesList != null) {

            for (CarrierMes carrierMes : carrierMesList) {
                if (!carrierMes.getCarrierNumber().equals(carrierNumber) && carrierMes.getDestination().equals(laneStopConveyorId)) {
                    Carrier carrier = getCarrier(carrierMes);

                    if (carrier.getCurrentLocation().equals(carrier.getDestination())) {
                        carrier.setAlreadyInLane(true);
                        carrierLinkedList.add(carrier);
                    } else {
                        movingCarrierLinkedList.add(carrier);
                        carrier.setAlreadyInLane(false);
                    }
                }
            }

            populateRow(carrierLinkedList, movingCarrierLinkedList, laneStopConveyorId);
        }
    }

    public void reorderCarriersInRow(Long laneStopConveyorId) {
        LOG.debug(" reloading row - " + laneStopConveyorId);
        List<CarrierMes> carrierMesList = CarrierMes.getAllCarriersInLane(laneStopConveyorId);
        List<Carrier> carrierLinkedList = new LinkedList<Carrier>();
        List<Carrier> movingCarrierLinkedList = new LinkedList<Carrier>();

        if (carrierMesList != null) {

            for (CarrierMes carrierMes : carrierMesList) {
                Carrier carrier = getCarrier(carrierMes);
                if (carrierMes.getCurrentLocation().equals(laneStopConveyorId)) {
                    carrier.setAlreadyInLane(true);
                    carrierLinkedList.add(carrier);
                } else {
                    movingCarrierLinkedList.add(carrier);
                    carrier.setAlreadyInLane(false);
                }
            }
            populateRow(carrierLinkedList, movingCarrierLinkedList, laneStopConveyorId);
        }
    }

    public void removeCarrierFromStorageState(Carrier carrier) {
        StorageRow tempRow = null;
        for (StorageRow row : rows) {
            if (row.carrierExistsInRow(carrier)) {
                tempRow = row;
                break;
            }
        }
        if (tempRow != null) {
            removeCarrierFromRow(carrier.getCarrierNumber(), tempRow.getStop().getId());
        }
    }


    //return true if carrier has invalid destination
    public boolean hadValidLaneDestination(Carrier carrier) {
        boolean flag = true;

        if (carrier.getDestination().isRowStop()) {

            if (!carrierExistsInStorageState(carrier)) {
                flag = false;
            } else {
                long countCarriersInDestinationLane = CarrierMes.countCarriersWithCurrentLocationStop(carrier.getDestination());

                if (countCarriersInDestinationLane >= getLaneCapacityByAssociatedStop(carrier.getDestination()).intValue()) {
                    flag = false;
                }
            }
        }

        return flag;
    }

    private Integer getLaneCapacityByAssociatedStop(Stop stop) {
        int capacity = 0;
        for (StorageRow row : rows) {
            if (row.getStop().equals(stop)) {
                capacity = row.getCapacity();
                break;
            }
        }

        return capacity;
    }


    //release all existing carriers from lane set current location and destination to zero if not part of carrier list
    // add List of carriers to Row of associated laneStop
    public void addCarriersToLane(List<Carrier> carriers, Stop lanestop) {
        LOG.info("adding carrier sto lane " + lanestop.getName());
        for (StorageRow lane : rows) {
            if (lane.getStop().equals(lanestop)) {
                lane.setOutOfOrder(true);
                LOG.info("emptying the lane " + lane.getRowName());

                while (!lane.isEmpty()) {
                    Carrier c = lane.release();

                    if (!exists(carriers, c)) {
                        List<Stop> maintenanceStops = Stop.findAllStopsByType(StopType.MAINTENANCE);
                        if (maintenanceStops != null && maintenanceStops.size() > 0) {
                            Carrier newCarrier = new Carrier();
                            newCarrier.setCarrierNumber(c.getCarrierNumber());
                            newCarrier.setCurrentLocation(maintenanceStops.get(0));
                            newCarrier.setDestination(maintenanceStops.get(0));
                            sendCarrierUpdateMessage(newCarrier);
                            LOG.info("moving carrier " + newCarrier.getCarrierNumber() + " to maintenance stop-" + maintenanceStops.get(0).getName());
                        } else {
                            LOG.info("no maintenance stop defined ");
                        }
                    }
                }
                if (lane.isEmpty()) {
                    LOG.info("storing carriers into empty lane " + lane.getRowName());
                    for (Carrier carrier : carriers) {

                        CarrierMes carrierMes = CarrierMes.findCarrierByCarrierNumber(carrier.getCarrierNumber());
                        if (carrierMes != null) {
                            carrier.setQuantity(carrierMes.getQuantity());
                            carrier.setDie(Die.findDie(new Long(carrierMes.getDieNumber().intValue())));
                            carrier.setCarrierStatus(CarrierStatus.findByType(carrierMes.getStatus()));
                            carrier.setPress(Press.findByType(carrierMes.getOriginationLocation()));
                            carrier.setProductionRunNo(carrierMes.getProductionRunNumber());
                            carrier.setStampingProductionRunTimestamp(carrierMes.getProductionRunDate());
                        }
                        carrier.setAlreadyInLane(true);
                        lane.store(carrier);
                    }
                }
                lane.setOutOfOrder(false);
            }
        }
    }


    private boolean exists(List<Carrier> carriers, Carrier c) {
        boolean flag = false;
        for (Carrier carrier : carriers) {
            if (carrier.getCarrierNumber().equals(c.getCarrierNumber())) {
                flag = true;
                break;
            }
        }
        return flag;
    }


    private Carrier getCarrier(CarrierMes carrierMes) {
        Carrier carrier = new Carrier();
        carrier.setCarrierNumber(carrierMes.getCarrierNumber());
        carrier.setQuantity(carrierMes.getQuantity());
        carrier.setDie(Die.findDie(Long.valueOf(carrierMes.getDieNumber().toString())));
        carrier.setCarrierStatus(CarrierStatus.findByType(carrierMes.getStatus()));
        carrier.setCurrentLocation(Stop.findStop(carrierMes.getCurrentLocation()));
        carrier.setDestination(Stop.findStop(carrierMes.getDestination()));
        carrier.setPress(Press.findByType(carrierMes.getOriginationLocation()));
        carrier.setProductionRunNo(carrierMes.getProductionRunNumber());
        carrier.setStampingProductionRunTimestamp(carrierMes.getProductionRunDate());
        carrier.setBuffer(carrierMes.getBuffer());
        carrier.setUpdateDate(carrierMes.getUpdateDate());

        return carrier;
    }

    private void populateRow(List<Carrier> carriersAlreadyInRow, List<Carrier> carriersMovingIntoRow, long laneStopConveyorId) {
        for (StorageRow lane : rows) {
            if (lane.getStop().getId().equals(laneStopConveyorId)) {
                lane.setOutOfOrder(true);
                while (!lane.isEmpty()) {
                    lane.release();
                }
                for (Carrier c : carriersAlreadyInRow) {
                    LOG.debug(" adding carrier already in row - " + c.getCarrierNumber());
                    lane.store(c);
                }
                for (Carrier c : carriersMovingIntoRow) {
                    LOG.debug(" adding carrier moving to row - " + c.getCarrierNumber());
                    lane.store(c);
                }
                lane.setOutOfOrder(false);
            }
        }
    }

    public void updateLane(StorageRow row) {
        LOG.info("updating lane " + row.getRowName());
        for (StorageRow lane : rows) {
            if (lane.getId().equals(row.getId())) {
                lane.setRowName(row.getRowName());
                lane.setAvailability(row.getAvailability());
                lane.setCapacity(row.getCapacity());
                lane.setStorageArea(row.getStorageArea());
                lane.setStop(row.getStop());

                LOG.info("updated Row " + lane.toString());
                break;
            }
        }
    }

    public String toString() {
        String tempString = "";
        for (StorageRow laneImpl : rows) {
            tempString = tempString + "\n" + laneImpl.toString();
            LinkedList<Carrier> carrierLinkedList = laneImpl.getCarriersAsLinkedList();
            String clist = "";
            for (Carrier carrier : carrierLinkedList) {
                clist = clist + "," + carrier.getCarrierNumber();
            }
            tempString = tempString + "\n" + clist;
        }
        return tempString;
    }

    public int hashCode() {
        //LOG.info("hashCode() called.  hashcode: " + super.hashCode());
        return super.hashCode();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
