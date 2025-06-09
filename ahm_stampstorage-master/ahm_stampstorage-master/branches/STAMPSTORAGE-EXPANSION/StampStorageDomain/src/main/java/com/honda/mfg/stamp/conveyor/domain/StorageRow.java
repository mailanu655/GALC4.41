package com.honda.mfg.stamp.conveyor.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.LaneCondition;
import com.honda.mfg.stamp.conveyor.domain.enums.StopAvailability;
import com.honda.mfg.stamp.conveyor.domain.enums.StorageArea;
import com.honda.mfg.stamp.conveyor.exceptions.InvalidDieException;

@Entity
@Table(name = "Row_TBX")
@Configurable
/**
 * 
 * A physical row used to store carriers, includes carrier queue and lane
 * condition.
 *
 */
public class StorageRow {

	private static final Logger LOG = LoggerFactory.getLogger(StorageRow.class);

	@NotNull
	@Column(name = "ROW_NAME")
	private String rowName;

	@NotNull
	@ManyToOne
	@JoinColumn(name="STOP", referencedColumnName="STOP_ID")
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
		synchronized (carrierQueue) { // MG 06-27-2016 Fix concurrency issues by locking the carrier queue
			if (carrierQueue != null && carrierQueue.size() == capacity) {
				throw new IllegalStateException("Attempt to store Carrier- " + carrier.getCarrierNumber()
						+ " element into " + rowName + " exceeds queue max capacity: " + capacity);
			}
			if (!carrierExistsInRow(carrier)) {
				carrierQueue.add(carrier);
			} else {
				LOG.debug("cannot add carrier: " + carrier + "-" + carrier.getCarrierNumber());
			}
		}

	}

	public void uiDisplayUseOnlyAddCarrier(Carrier carrier) {
		synchronized (carrierQueue) { // MG 06-27-2016 Fix concurrency issues by locking the carrier queue
			if (!carrierExistsInRow(carrier)) {
				carrierQueue.add(carrier);
			} else {
				LOG.debug("cannot add carrier: " + carrier + "-" + carrier.getCarrierNumber());
			}
		}
	}

	public Carrier release() {
		synchronized (carrierQueue) {
			return carrierQueue.remove();
		}
	}

	public boolean carrierExistsInRow(Carrier carrier) {
		synchronized (carrierQueue) { // MG 06-27-2016 Fix concurrency issues by locking the carrier queue
			boolean exists = false;
			if (carrierQueue == null || carrierQueue.isEmpty()) {
				return exists;
			}
			for (Iterator it = carrierQueue.iterator(); it.hasNext();) {
				Carrier tempCarrier = (Carrier) it.next();

				if (tempCarrier.getCarrierNumber().equals(carrier.getCarrierNumber())) {
					exists = true;
					break;
				}
			}
			return exists;
		}
	}

	public int getCurrentCarrierCount() {
		synchronized (carrierQueue) { // MG 06-27-2016 Fix concurrency issues by locking the carrier queue
			return carrierQueue.size();
		}
	}

	public boolean isEmpty() {
		synchronized (carrierQueue) { // MG 06-27-2016 Fix concurrency issues by locking the carrier queue
			return carrierQueue.isEmpty();
		}
	}

	public boolean isFull() {
		return !(getCurrentCarrierCount() < capacity);
	}

	public void setLaneCondition(LaneCondition laneCondition) {
		this.laneCondition = laneCondition;
	}

	public Carrier getCarrierAtRowOut() {
		synchronized (carrierQueue) { // MG 06-27-2016 Fix concurrency issues by locking the carrier queue
			return carrierQueue.peek();
		}
	}

	public boolean hasAnyCarrierArrivedInLane() {
		Carrier headCarrier = getCarrierAtRowOut();
		if (headCarrier == null || headCarrier.getCurrentLocation() == null) {
			return false;
		}
		return getStop().getId().equals(headCarrier.getCurrentLocation().getId());
	}

	public Set<Long> getDieNumbersForAllCarriers() {
		synchronized (carrierQueue) { // MG 06-27-2016 Fix concurrency issues by locking the carrier queue
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
	}

	public Carrier getCarrierAtLaneIn() {
		synchronized (carrierQueue) { // MG 06-27-2016 Fix concurrency issues by locking the carrier queue
			Carrier lastCarrier = null;
			Iterator<Carrier> it = carrierQueue.iterator();
			while (it.hasNext()) {
				lastCarrier = it.next();
			}
			return lastCarrier;
		}
	}

	public boolean hasCarrierOfProdRunNo(Integer prodRunNo) {
		synchronized (carrierQueue) { // MG 06-27-2016 Fix concurrency issues by locking the carrier queue
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
	}

	public boolean carrierAtLaneOutIsNotOnHold() {
		synchronized (carrierQueue) { // MG 06-27-2016 Fix concurrency issues by locking the carrier queue
			if (carrierQueue == null || carrierQueue.isEmpty()) {
				return false;
			}
		}
		Carrier headCarrier = getCarrierAtRowOut();
		if (headCarrier == null || headCarrier.getCarrierStatus() == null) {
			return false;
		}
		if (!headCarrier.getCarrierStatus().equals(CarrierStatus.ON_HOLD)) {
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
					// Only one die in this row.
					if (!getDieNumbersForAllCarriers().contains(null) || !getDieNumbersForAllCarriers().contains(999)) {
						return LaneCondition.FULL;
					} else {
						// What if die is null still call it empty?
						return LaneCondition.EMPTY;
					}
				} else { // More than one die in row. Added check to see if the lane out is on hold.
					if (getCarrierAtRowOut().getDie().equals(die) && carrierAtLaneOutIsNotOnHold()) {
						return LaneCondition.MIXED_FRONT;
					} else {
						// So far we have not checked whether the row contains the die or not.
						return LaneCondition.MIXED_BLOCK;
					}
				}
			} else { // not full row.
				Set<Long> dieNumbers = getDieNumbersForAllCarriers();
				if (dieNumbers.size() == 1) { // Only One Die in this row.
					if (dieNumbers.toArray()[0] == null) {
						return LaneCondition.EMPTY;
					}
					long dieNumber = (Long) dieNumbers.toArray()[0];
					if (dieNumber != 999L) {
						return LaneCondition.PARTIAL;
					} else {
						// Should this be empty if the die is empty?
						return LaneCondition.PARTIAL;
					}
				}

				if (dieNumbers.size() == 0) {
					return LaneCondition.EMPTY;
				}
				if (dieNumbers.size() > 1) {// Added check to see if the lane out is on hold.
					if (getCarrierAtRowOut().getDie().equals(die) && carrierAtLaneOutIsNotOnHold()) {
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
					if (dieNumbers.toArray()[0] == null) {
						return LaneCondition.EMPTY;
					}
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
		synchronized (carrierQueue) { // MG 06-27-2016 Fix concurrency issues by locking the carrier queue
			LinkedList<Carrier> carrierLinkedList = new LinkedList<Carrier>();
			Iterator<Carrier> it = carrierQueue.iterator();
			while (it.hasNext()) {
				carrierLinkedList.add(it.next());
			}
			return carrierLinkedList;
		}
	}

	public void updateCarrier(Carrier carrier) {
		synchronized (carrierQueue) { // MG 06-27-2016 Fix concurrency issues by locking the carrier queue
			// MG 06-28-2016 Do not rebuild the queue, rather just change the one element.
			Carrier tempCarrier;
			Iterator<Carrier> it = carrierQueue.iterator();
			boolean found = false;
			while (it.hasNext() && !found) {
				tempCarrier = it.next();
				if (tempCarrier.getCarrierNumber().equals(carrier.getCarrierNumber())) {
					tempCarrier.setDie(carrier.getDie());
					tempCarrier.setQuantity(carrier.getQuantity());
					tempCarrier.setCarrierStatus(carrier.getCarrierStatus());
					found = true;
				}
			}
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
		synchronized (carrierQueue) { // MG 06-27-2016 Fix concurrency issues by locking the carrier queue
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
										|| carrier.getStampingProductionRunTimestamp().before(
												oldestProductionRunPartCarrier.getStampingProductionRunTimestamp()))) {
							oldestProductionRunPartCarrier = carrier;
						}
					}
				}
			}
			return oldestProductionRunPartCarrier;
		}
	}

	public boolean isPhysicalSpaceAvailable() {
		if (testMode == 1) {
			return (capacity - getCurrentCarrierCount()) > 0;
		}
		Long carrierCnt = getCarrierCountInRow(stop);
		return (capacity - carrierCnt.intValue()) > 0;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		// sb.append("Availability: ").append(getAvailability()).append(", ");
		// sb.append("Capacity: ").append(getCapacity()).append(", ");
		// sb.append("Id: ").append(getId()).append(", ");
		sb.append(getRowName());
		// sb.append("Stop: ").append(getStop()).append(", ");
		// sb.append("StorageArea: ").append(getStorageArea()).append(", ");
		// sb.append("Version: ").append(getVersion());
		return sb.toString();
	}

	@PersistenceContext
	transient EntityManager entityManager;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Row_ID")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Transactional
	public void persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.persist(this);
	}

	@Transactional
	public void remove() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			StorageRow attached = StorageRow.findStorageRow(this.id);
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void flush() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.flush();
	}

	@Transactional
	public void clear() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.clear();
	}

	@Transactional
	public StorageRow merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		StorageRow merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public static EntityManager entityManager() {
		EntityManager em = new StorageRow().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countStorageRows() {
		return entityManager().createQuery("SELECT COUNT(o) FROM StorageRow o", Long.class).getSingleResult();
	}

	public static List<StorageRow> findAllStorageRows() {
		return entityManager().createQuery("SELECT o FROM StorageRow o", StorageRow.class).getResultList();
	}

	public static StorageRow findStorageRow(Long id) {
		if (id == null)
			return null;
		return entityManager().find(StorageRow.class, id);
	}

	public static List<StorageRow> findStorageRowEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM StorageRow o", StorageRow.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	public static List<StorageRow> findStorageRowsByArea(StorageArea area) {
		Query q = entityManager().createQuery("SELECT o FROM StorageRow o where o.storageArea=:area", StorageRow.class);
		q.setParameter("area", area);

		return q.getResultList();
	}

	public static StorageRow findStorageRowsByStop(Stop currentLocation) {
		Query q = entityManager().createQuery("SELECT o FROM StorageRow o where o.stop=:stop", StorageRow.class);
		q.setParameter("stop", currentLocation);

		List<StorageRow> rows = q.getResultList();
		if (!rows.isEmpty()) {
			return rows.get(0);
		} else {
			return null;
		}
	}

	public static long getCarrierCountInRow(Stop currentLocation) {
		long count = CarrierMes.countAllCarriersInLane(currentLocation.getId());

		return count;
	}



	public String getRowName() {
		return this.rowName;
	}

	public void setRowName(String rowName) {
		this.rowName = rowName;
	}

	public Stop getStop() {
		return this.stop;
	}

	public void setStop(Stop stop) {
		this.stop = stop;
	}

	public Integer getCapacity() {
		return this.capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public StorageArea getStorageArea() {
		return this.storageArea;
	}

	public void setStorageArea(StorageArea storageArea) {
		this.storageArea = storageArea;
	}

	public StopAvailability getAvailability() {
		return this.availability;
	}

	public void setAvailability(StopAvailability availability) {
		this.availability = availability;
	}
}
