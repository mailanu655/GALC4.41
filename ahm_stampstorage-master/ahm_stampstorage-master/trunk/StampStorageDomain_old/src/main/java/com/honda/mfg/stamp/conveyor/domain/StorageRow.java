package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.LaneCondition;
import com.honda.mfg.stamp.conveyor.domain.enums.StopAvailability;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.exceptions.InvalidDieException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.*;

@RooJavaBean
@RooToString
@RooEntity(identifierField = "id", identifierColumn = "Row_ID", table = "Row_TBX")
/**
 * 
 * A physical row used to store carriers, includes carrier queue and lane condition.
 *
 */
public class StorageRow {

    private static final Logger LOG = LoggerFactory.getLogger(StorageRow.class);

    @NotNull
    @Column(name = "ROW_NAME")
    private String rowName;

    @NotNull
    @ManyToOne
    private Stop stop;

    @NotNull
    @Column(name = "CAPACITY")
    private Integer capacity;

    @NotNull
    @Column(name = "STORAGE_AREA")
    @Enumerated
    private StorageArea storageArea;

    @NotNull
    @Column(name = "AVAILABILITY")
    @Enumerated
    private StopAvailability availability;

    @Transient
    private Queue<Carrier> carrierQueue;

    @Transient
    private boolean outOfOrder;
    @Transient
    private LaneCondition laneCondition;
    @Transient
    private int testMode;

    public StorageRow() {
        super();
        this.carrierQueue = new LinkedList<Carrier>();
    }

    public StorageRow(long id, String name, int maxCapacity, int testMode) {
        this.setId(id);
        this.rowName = name;
        this.capacity = maxCapacity;
        this.carrierQueue = new LinkedList<Carrier>();
        this.availability = StopAvailability.AVAILABLE;
        this.testMode = testMode;
    }


    public void store(Carrier carrier) {
        if (carrierQueue != null && carrierQueue.size() == capacity) {
            throw new IllegalStateException("Attempt to store Carrier- " + carrier.getCarrierNumber() + " element into " + rowName + " exceeds queue max capacity: " + capacity);
        }
        if (!carrierExistsInRow(carrier)) {
            carrierQueue.add(carrier);
        } else {
            LOG.debug("cannot add carrier: " + carrier + "-" + carrier.getCarrierNumber());
        }
    }

     public void uiDisplayUseOnlyAddCarrier(Carrier carrier) {

        if (!carrierExistsInRow(carrier)) {
            carrierQueue.add(carrier);
        } else {
            LOG.debug("cannot add carrier: " + carrier + "-" + carrier.getCarrierNumber());
        }
    }

    public Carrier release() {
        return carrierQueue.remove();
    }


    public boolean carrierExistsInRow(Carrier carrier) {
        boolean exists = false;
        if (carrierQueue == null || carrierQueue.isEmpty()) {
            return exists;
        }
        for (Iterator it = carrierQueue.iterator(); it.hasNext(); ) {
            Carrier tempCarrier = (Carrier) it.next();

            if (tempCarrier.getCarrierNumber().equals(carrier.getCarrierNumber())) {
                exists = true;
                break;
            }
        }
        return exists;
    }


    public int getCurrentCarrierCount() {
        return carrierQueue.size();
    }


    public boolean isEmpty() {
        return carrierQueue.isEmpty();
    }


    public boolean isFull() {
        return !(getCurrentCarrierCount() < capacity);
    }

    public void setLaneCondition(LaneCondition laneCondition) {
        this.laneCondition = laneCondition;
    }

    public Carrier getCarrierAtRowOut() {
        return carrierQueue.peek();
    }

    public boolean hasAnyCarrierArrivedInLane() {
        Carrier headCarrier = getCarrierAtRowOut();
        if (headCarrier == null || headCarrier.getCurrentLocation() == null) {
            return false;
        }
        return getStop().getId().equals(headCarrier.getCurrentLocation().getId());
    }

    public Set<Long> getDieNumbersForAllCarriers() {
        Set<Long> partTypes = new HashSet<Long>();
        for (Carrier carrier : carrierQueue) {
            Long dieNumber = null;
            if (carrier.getDie() != null) {
                dieNumber = carrier.getDie().getId();
            }
            partTypes.add(dieNumber);
        }
        return partTypes;
    }

    public Carrier getCarrierAtLaneIn() {
        Carrier lastCarrier = null;

        Iterator<Carrier> it = carrierQueue.iterator();
        while (it.hasNext()) {
            lastCarrier = it.next();
        }
        return lastCarrier;
    }

    public boolean hasCarrierOfProdRunNo(Integer prodRunNo) {
        Iterator<Carrier> it = carrierQueue.iterator();
        boolean hasProdRunNo = false;
        Carrier carrier = null;
        while (it.hasNext()) {
            carrier = it.next();
            if (carrier.getProductionRunNo() != null && carrier.getProductionRunNo().equals(prodRunNo)) {
                hasProdRunNo = true;
                break;
            }
        }
        return hasProdRunNo;
    }

    public boolean carrierAtLaneOutIsNotOnHold() {
        if (carrierQueue == null || carrierQueue.isEmpty()) {
            return false;
        }
        Carrier headCarrier = getCarrierAtRowOut();
        if (headCarrier == null || headCarrier.getCarrierStatus() == null) {
            return false;
        }
        if (headCarrier.getCarrierStatus().equals(CarrierStatus.SHIPPABLE) || headCarrier.getCarrierStatus().equals(CarrierStatus.INSPECTION_REQUIRED)) {
            return true;
        }
        return false;
    }

    public boolean isOutOfOrder() {
        return outOfOrder;
    }

    public void setOutOfOrder(boolean outOfOrder) {
        this.outOfOrder = outOfOrder;
    }

    public String getDie() {
        String dieDescription = null;
        Set<Long> dieNumbers = getDieNumbersForAllCarriers();
        if (dieNumbers.size() > 0 && !dieNumbers.contains(null)) {
            Die die = Die.findDie((Long) dieNumbers.toArray()[0]);
            dieDescription = die.getDescription();
        }

        return dieDescription;
    }

    public LaneCondition getLaneConditionByDie(Die die) {
        if (this.laneCondition == null) {
            if (isEmpty()) {
                return LaneCondition.VACANT;
            } else if (isFull()) {

                if (getDieNumbersForAllCarriers().size() == 1) {
                    if (!getDieNumbersForAllCarriers().contains(null) || !getDieNumbersForAllCarriers().contains(999)) {
                        return LaneCondition.FULL;
                    } else {
                        return LaneCondition.EMPTY;
                    }
                } else {
                    if (getCarrierAtRowOut().getDie().equals(die)) {
                        return LaneCondition.MIXED_FRONT;
                    } else {
                        return LaneCondition.MIXED_BLOCK;
                    }
                }
            } else {
                Set<Long> dieNumbers = getDieNumbersForAllCarriers();
                if (dieNumbers.size() == 1) {
                    if (dieNumbers.toArray()[0] == null) {return LaneCondition.EMPTY;}
                    long dieNumber = (Long) dieNumbers.toArray()[0];
                    if (dieNumber != 999L) {
                        return LaneCondition.PARTIAL;
                    } else {
                        return LaneCondition.PARTIAL;
                    }
                }

                if (dieNumbers.size() == 0) {
                    return LaneCondition.EMPTY;
                }
                if (dieNumbers.size() > 1) {
                    if (getCarrierAtRowOut().getDie().equals(die)) {
                        return LaneCondition.MIXED_FRONT;
                    } else {
                        return LaneCondition.MIXED_BLOCK;
                    }
                }
            }
        }

        return this.laneCondition;
    }

    public LaneCondition getLaneCondition() {
        if (this.laneCondition == null) {
            if (isEmpty()) {
                return LaneCondition.VACANT;
            } else if (isFull()) {

                if (getDieNumbersForAllCarriers().size() == 1) {
                    if (!getDieNumbersForAllCarriers().contains(null) || !getDieNumbersForAllCarriers().contains(999)) {
                        return LaneCondition.FULL;
                    } else {
                        return LaneCondition.EMPTY;
                    }
                } else {
                    return LaneCondition.MIXED;
                }
            } else {
                Set<Long> dieNumbers = getDieNumbersForAllCarriers();
                if (dieNumbers.size() == 1) {
                    if (dieNumbers.toArray()[0] == null) {return LaneCondition.EMPTY;}
                    long dieNumber = (Long) dieNumbers.toArray()[0];
                    if (dieNumber != 999L) {
                        return LaneCondition.PARTIAL;
                    } else {
                        return LaneCondition.EMPTY;
                    }
                }

                if (dieNumbers.size() == 0) {
                    return LaneCondition.EMPTY;
                }
                if (dieNumbers.size() > 1) {
                    return LaneCondition.MIXED;
                }
            }
        }

        return this.laneCondition;
    }

    public LinkedList<Carrier> getCarriersAsLinkedList() {
        LinkedList<Carrier> carrierLinkedList = new LinkedList<Carrier>();

        Iterator<Carrier> it = carrierQueue.iterator();
        while (it.hasNext()) {
            carrierLinkedList.add(it.next());
        }
        return carrierLinkedList;
    }


    public void updateCarrier(Carrier carrier) {
        LinkedList<Carrier> carrierLinkedList = new LinkedList<Carrier>();

        while (!carrierQueue.isEmpty()) {
            Carrier tempCarrier = carrierQueue.remove();
            if (tempCarrier.getCarrierNumber().equals(carrier.getCarrierNumber())) {
                tempCarrier.setDie(carrier.getDie());
                tempCarrier.setQuantity(carrier.getQuantity());
                tempCarrier.setCarrierStatus(carrier.getCarrierStatus());
            }
            carrierLinkedList.add(tempCarrier);
        }

        while (!carrierLinkedList.isEmpty()) {
            carrierQueue.add(carrierLinkedList.remove());
        }
    }

    public boolean hasShippableCarriersWithDieNumber(Long dieNumber) {
        boolean flag = false;
        LinkedList<Carrier> carriers = getCarriersAsLinkedList();
        LinkedList<Carrier> carriersWithMatchingDie = new LinkedList<Carrier>();
        for (Carrier carrier : carriers) {
            if (carrier.getDie().getId().equals(dieNumber)) {
                carriersWithMatchingDie.add(carrier);
            }
        }

        if (carriersWithMatchingDie.size() > 0) {
            if (!carriersWithMatchingDie.get(0).getCarrierStatus().equals(CarrierStatus.ON_HOLD)) {
                flag = true;
            }
        }

        return flag;
    }


    public Integer getCountOfCarriersAtLaneOutEndForDieNumber(Long dieNumber) {

        int count = 0;

        if (getDieNumbersForAllCarriers().contains(dieNumber)) {
            LinkedList<Carrier> carrierLinkedList = getCarriersAsLinkedList();
            while (!carrierLinkedList.isEmpty()) {
                Carrier carrier = carrierLinkedList.remove();
                if (carrier.getDie() != null && !(carrier.getDie().getId().equals(dieNumber))) {
                    break;
                }
                count++;
            }
        }

        return count;
    }


    public Integer getCountOfCarriersAtLaneInEndForDieNumber(Long dieNumber) {

        int count = 0;

        if (getDieNumbersForAllCarriers().contains(dieNumber)) {
            LinkedList<Carrier> carrierLinkedList = getCarriersAsLinkedList();
            while (!carrierLinkedList.isEmpty()) {
                Carrier carrier = carrierLinkedList.remove();
                if (carrier.getDie() == null || carrier.getDie().getId().equals(dieNumber)) {
                    count++;
                }
            }
        }

        return count;
    }


    public Integer getCountOfCarriersBlockingDieNumberAtLaneOutEnd(Long dieNumber) {

        int count = 0;
        if (getDieNumbersForAllCarriers().contains(dieNumber)) {
            LinkedList<Carrier> carrierLinkedList = getCarriersAsLinkedList();
            while (!carrierLinkedList.isEmpty()) {
                Carrier carrier = carrierLinkedList.remove();
                if (carrier.getDie() != null && carrier.getDie().getId().equals(dieNumber)) {
                    break;
                }
                count++;
            }
        }

        return count;
    }

    public boolean isBlocked() {
        return (availability.equals(StopAvailability.BLOCKED));
    }

    public Carrier getOldestProductionRunCarrierForDieNumber(Long dieNumber) {

        Iterator<Carrier> it = carrierQueue.iterator();
        Carrier oldestProductionRunPartCarrier = null;
        Carrier carrier = null;
        if (!isEmpty()) {
            Carrier c = getCarrierAtRowOut();
            if (!c.getCarrierStatus().equals(CarrierStatus.ON_HOLD)) {
                while (it.hasNext()) {
                    carrier = it.next();
                    if (carrier.getDie() == null) {
                        throw new InvalidDieException(carrier.getCarrierNumber() + " has Invalid Die number");
                    }
                    if (!carrier.getCarrierStatus().equals(CarrierStatus.ON_HOLD)
                            && carrier.getDie().getId().equals(dieNumber)
                            && (oldestProductionRunPartCarrier == null
                            || carrier.getStampingProductionRunTimestamp().before(oldestProductionRunPartCarrier.getStampingProductionRunTimestamp()))) {
                        oldestProductionRunPartCarrier = carrier;
                    }
                }
            }
        }
        return oldestProductionRunPartCarrier;
    }


}

