package com.honda.mfg.stamp.conveyor.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.persistence.Version;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;

@Configurable
@Entity
@Table(name = "CARRIER_MES_TBX")
/**
 * User: Jeffrey M Lutz Date: 6/18/11
 */

public class CarrierMes {
	private static final Logger LOG = LoggerFactory.getLogger(CarrierMes.class);
	@Column(name = "CARRIERNUMBER")
	private Integer carrierNumber;
	@Column(name = "TAGID")
	private Integer tagId;
	@Column(name = "DIENUMBER")
	private Integer dieNumber;
	@Column(name = "QUANTITY")
	private Integer quantity;
	@Column(name = "UPDATEDATE")
	private Date updateDate;
	@Column(name = "CURRENTLOCATION")
	private Long currentLocation;
	@Column(name = "DESTINATION")
	private Long destination;
	@Column(name = "STATUS")
	private Integer status;
	@Column(name = "ORIGINATIONLOCATION")
	private Integer originationLocation;
	@Column(name = "BUFFER")
	private Integer buffer;
	@Column(name = "PRODUCTIONRUNNUMBER")
	private Integer productionRunNumber;
	@Column(name = "PRODUCTIONRUNDATE")
	private Date productionRunDate;
	@Column(name = "SOURCE")
	private String source;

	@Column(name = "MAINTENANCE_BITS")
	private Integer maintenanceBits;
	@Transient
	boolean maintSetterCalled = false;
	@Transient
	boolean isDirty = false;
	@Transient
	private Integer maintRequired;
	@Transient
	private Integer bit1;
	@Transient
	private Integer bit2;
	@Transient
	private Integer bit3;
	@Transient
	private Integer bit4;
	@Transient
	private Integer bit5;
	@Transient
	private Integer bit6;
	@Transient
	private Integer bit7;
	@Transient
	private Integer bit8;
	@Transient
	private Integer bit9;
	@Transient
	private Integer bit10;
	@Transient
	private Integer bit11;
	@Transient
	private Integer bit12;
	@Transient
	private Integer bit13;
	@Transient
	private Integer bit14;
	@Transient
	private Integer bit15;

	private void calcMaintBits() {
		if (maintRequired == null && bit1 == null && bit2 == null && bit3 == null && bit4 == null && bit5 == null
				&& bit6 == null && bit7 == null && bit8 == null && bit9 == null && bit10 == null && bit11 == null
				&& bit12 == null && bit13 == null && bit14 == null && bit15 == null) {
			maintenanceBits = null;
		} else {
			maintenanceBits = 0;
			maintenanceBits = maintenanceBits + ((maintRequired == null || maintRequired == 0) ? 0 : 1);
			maintenanceBits = maintenanceBits + ((bit1 == null || bit1 == 0) ? 0 : 2);
			maintenanceBits = maintenanceBits + ((bit2 == null || bit2 == 0) ? 0 : 4);
			maintenanceBits = maintenanceBits + ((bit3 == null || bit3 == 0) ? 0 : 8);
			maintenanceBits = maintenanceBits + ((bit4 == null || bit4 == 0) ? 0 : 16);
			maintenanceBits = maintenanceBits + ((bit5 == null || bit5 == 0) ? 0 : 32);
			maintenanceBits = maintenanceBits + ((bit6 == null || bit6 == 0) ? 0 : 64);
			maintenanceBits = maintenanceBits + ((bit7 == null || bit7 == 0) ? 0 : 128);
			maintenanceBits = maintenanceBits + ((bit8 == null || bit8 == 0) ? 0 : 256);
			maintenanceBits = maintenanceBits + ((bit9 == null || bit9 == 0) ? 0 : 512);
			maintenanceBits = maintenanceBits + ((bit10 == null || bit10 == 0) ? 0 : 1024);
			maintenanceBits = maintenanceBits + ((bit11 == null || bit11 == 0) ? 0 : 2048);
			maintenanceBits = maintenanceBits + ((bit12 == null || bit12 == 0) ? 0 : 4096);
			maintenanceBits = maintenanceBits + ((bit13 == null || bit13 == 0) ? 0 : 8192);
			maintenanceBits = maintenanceBits + ((bit14 == null || bit14 == 0) ? 0 : 16384);
			maintenanceBits = maintenanceBits + ((bit15 == null || bit15 == 0) ? 0 : 32768);
		}
	}

	private void setMaintBits() {
		int maintBits = 0;
		if (maintenanceBits == null) {
			maintenanceBits = null;
			maintRequired = null;
			bit1 = null;
			bit2 = null;
			bit3 = null;
			bit4 = null;
			bit5 = null;
			bit6 = null;
			bit7 = null;
			bit8 = null;
			bit9 = null;
			bit10 = null;
			bit11 = null;
			bit12 = null;
			bit13 = null;
			bit14 = null;
			bit15 = null;

		} else {
			maintBits = maintenanceBits;
			maintRequired = maintBits >> 0 & 1;
			bit1 = maintBits >> 1 & 1;
			bit2 = maintBits >> 2 & 1;
			bit3 = maintBits >> 3 & 1;
			bit4 = maintBits >> 4 & 1;
			bit5 = maintBits >> 5 & 1;
			bit6 = maintBits >> 6 & 1;
			bit7 = maintBits >> 7 & 1;
			bit8 = maintBits >> 8 & 1;
			bit9 = maintBits >> 9 & 1;
			bit10 = maintBits >> 10 & 1;
			bit11 = maintBits >> 11 & 1;
			bit12 = maintBits >> 12 & 1;
			bit13 = maintBits >> 13 & 1;
			bit14 = maintBits >> 14 & 1;
			bit15 = maintBits >> 15 & 1;
		}

	}

	public CarrierMes() {
		super();
	}

	public CarrierMes(long id) {
		setId(id);
	}

	public boolean equals(Object obj) {
		if (obj instanceof CarrierMes) {
			if (getId() == null) {
				return false;
			}
			return getId().equals(((CarrierMes) obj).getId());
		} else {
			return false;
		}
	}

	public int hashCode() {
		// LOG.info("hashCode() called. hashcode: " + super.hashCode());
		return super.hashCode(); // To change body of overridden methods use File | Settings | File Templates.
	}

	@PersistenceContext
	transient EntityManager entityManager;

	private static Boolean staleData;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CARRIER_ID")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

//DAO stuff
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
			CarrierMes attached = CarrierMes.findCarrier(this.id);
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
	public CarrierMes merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		CarrierMes merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public static EntityManager entityManager() {
		EntityManager em = new Carrier().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countCarriers() {
		return entityManager().createQuery("SELECT COUNT(o) FROM CarrierMes o", Long.class).getSingleResult();
	}

	public static List<CarrierMes> findAllCarriers() {
		List<CarrierMes> carriers = entityManager().createQuery("SELECT o FROM CarrierMes o", CarrierMes.class)
				.setMaxResults(1000).getResultList();
		return carriers;
	}

	public static List<CarrierMes> findAllMovingCarriers() {
		List<CarrierMes> carriers = entityManager().createQuery(
				"SELECT o FROM CarrierMes o where o.currentLocation != o.destination order by o.destination",
				CarrierMes.class).getResultList();
		return carriers;
	}

	public static long countCarriersWithDestinationStop(Stop stop) {
		Query q = entityManager().createQuery("SELECT COUNT(o) FROM CarrierMes o WHERE o.destination = :stopId",
				Long.class);
		q.setParameter("stopId", stop.getId());
		return (Long) q.getSingleResult();
	}

	public static long countCarriersWithCurrentLocationStop(Stop stop) {
		Query q = entityManager().createQuery("SELECT COUNT(o) FROM CarrierMes o WHERE o.currentLocation = :stopId",
				Long.class);
		q.setParameter("stopId", stop.getId());
		return (Long) q.getSingleResult();
	}

	public static long countCarriersWithCurrentLocationStopAndDie(Stop stop, Die die) {
		Query q = entityManager().createQuery(
				"SELECT COUNT(o) FROM CarrierMes o WHERE o.dieNumber= :die and o.currentLocation = :stopId",
				Long.class);
		q.setParameter("stopId", stop.getId());
		q.setParameter("die", new Integer(die.getId().intValue()));
		return (Long) q.getSingleResult();
	}

	public static long countCarriersWithDestinationStopAndDie(Stop stop, Die die) {
		Query q = entityManager().createQuery(
				"SELECT COUNT(o) FROM CarrierMes o WHERE o.dieNumber= :die and o.destination = :stopId", Long.class);
		q.setParameter("stopId", stop.getId());
		q.setParameter("die", new Integer(die.getId().intValue()));
		return (Long) q.getSingleResult();
	}

	public static long countAllCarriersInLane(Long stopNumber) {
		Query q = entityManager().createQuery(
				"SELECT COUNT(o) FROM CarrierMes o WHERE o.currentLocation = :stopNumber OR destination = :stopNumber ",
				Long.class);
		q.setParameter("stopNumber", stopNumber);

		return (Long) q.getSingleResult();
	}

	public static List<CarrierMes> findAllCarriersWithDestinationIn(List<Long> stopNumbers) {
		Query q = entityManager().createQuery(
				"SELECT o FROM CarrierMes o WHERE o.destination IN (:stopNumbers) and o.destination > 0 order by o.destination",
				CarrierMes.class);
		q.setParameter("stopNumbers", stopNumbers);

		List<CarrierMes> carriers = q.getResultList();

		return carriers;
	}

	public static List<CarrierMes> findAllCarriersWithCurrentLocationAndDestinationLocationAndDie(Long stopNumber,
			Integer die) {
		Query q = entityManager().createQuery(
				"SELECT o FROM CarrierMes o WHERE o.dieNumber= :die and (o.currentLocation = :stopNumber or o.destination = :stopNumber) order by buffer desc, o.updateDate asc",
				CarrierMes.class);
		q.setParameter("stopNumber", stopNumber);
		q.setParameter("die", die);
		List<CarrierMes> carriers = q.getResultList();

		return carriers;
	}

	public static List<CarrierMes> findAllCarriersWithCurrentLocation(Long stopNumber) {
		Query q = entityManager().createQuery(
				"SELECT o FROM CarrierMes o WHERE o.currentLocation = :stopNumber order by buffer desc, o.updateDate asc",
				CarrierMes.class);
		q.setParameter("stopNumber", stopNumber);
		List<CarrierMes> carriers = q.getResultList();

		return carriers;
	}

	public static List<CarrierMes> findAllCarriersWithProductionRunNumber(Integer productionRunNumber, Integer page,
			Integer sizeNo) {
		Query q = entityManager().createQuery(
				"SELECT o FROM CarrierMes o WHERE o.productionRunNumber =:productionRunNumber order by o.productionRunDate desc",
				CarrierMes.class);
		q.setParameter("productionRunNumber", productionRunNumber);
		List<CarrierMes> carriers = q.setFirstResult(page == null ? 0 : (page.intValue() - 1) * sizeNo)
				.setMaxResults(sizeNo).getResultList();

		return carriers;
	}

	public static Long findCountOfCarriersWithProductionRunNumber(Integer productionRunNumber) {
		Query q = entityManager().createQuery(
				"SELECT COUNT(o) FROM CarrierMes o WHERE o.productionRunNumber =:productionRunNumber", Long.class);
		q.setParameter("productionRunNumber", productionRunNumber);

		return (Long) q.getSingleResult();
	}

	public static CarrierMes findCarrier(Long id) {
		if (id == null)
			return null;
		return entityManager().find(CarrierMes.class, id);
	}

	public static List<CarrierMes> findCarrierEntries(int firstResult, int maxResults) {
		List<CarrierMes> carriers = entityManager().createQuery("SELECT o FROM CarrierMes o", CarrierMes.class)
				.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();

		return carriers;
	}

	public static CarrierMes findCarrierByCarrierNumber(Integer carrierNumber) {
		Query q = null;
		if (carrierNumber != null) {
			q = entityManager().createQuery("SELECT o FROM CarrierMes o where o.carrierNumber=:carrierNumber",
					CarrierMes.class);
			q.setParameter("carrierNumber", carrierNumber);

			List<CarrierMes> carriers = q.getResultList();
			if (carriers != null && carriers.size() > 0) {
				return carriers.get(0);
			}
		}
		return null;
	}

	public static List<DieInventory> findPartCountsByDiesByStopArea(StopArea area) {
		List<DieInventory> dieQuantity = new ArrayList<DieInventory>();
		int emptyDieNumber = 999;
		long i = 0;
		String stops = "";

		List<Stop> stopsByArea = Stop.findAllStopsByArea(area);
		if (stopsByArea != null && stopsByArea.size() > 0) {
			for (Stop stop : stopsByArea) {
				if (stops.length() > 0) {
					stops = stops + ",";
				}
				stops = stops + stop.getId().toString();
			}

			Query q1 = entityManager()
					.createQuery("SELECT distinct o.dieNumber FROM CarrierMes o where o.dieNumber !=:emptyDieNumber");
			q1.setParameter("emptyDieNumber", emptyDieNumber);
			List<Integer> dieNumbers = q1.getResultList();
			for (int dieId : dieNumbers) {
				if (dieId != emptyDieNumber) {
					Long qty = 0l;
					Long holdqty = 0l;
					Long inspectionqty = 0l;

					String sql = "SELECT sum(o.quantity) FROM CarrierMes o where o.dieNumber=:dienumber and destination IN ("
							+ stops + ")";
					Query q = entityManager().createQuery(sql);
					q.setParameter("dienumber", dieId);

					String holdsql = "SELECT sum(o.quantity) FROM CarrierMes o where o.dieNumber=:dienumber and o.status=:carrierstatus and destination IN ("
							+ stops + ")";
					Query holdq = entityManager().createQuery(holdsql);
					holdq.setParameter("dienumber", dieId);
					holdq.setParameter("carrierstatus", CarrierStatus.ON_HOLD.type());

					String inspectionsql = "SELECT sum(o.quantity) FROM CarrierMes o where o.dieNumber=:dienumber and o.status=:carrierstatus and destination IN ("
							+ stops + ")";
					Query inspectionq = entityManager().createQuery(inspectionsql);
					inspectionq.setParameter("dienumber", dieId);
					inspectionq.setParameter("carrierstatus", CarrierStatus.INSPECTION_REQUIRED.type());

					if (q.getResultList() != null && q.getResultList().size() > 0) {
						qty = ((Long) q.getSingleResult());
					}

					if (holdq.getResultList() != null && holdq.getResultList().size() > 0) {
						holdqty = ((Long) holdq.getSingleResult());
					}

					if (inspectionq.getResultList() != null && inspectionq.getResultList().size() > 0) {
						inspectionqty = ((Long) inspectionq.getSingleResult());
					}
					if (qty != null) {
						DieInventory die_inventory = new DieInventory(i++);
						Die die = Die.findDie(new Long(dieId));
						String description = die == null ? dieId + "" : die.getDescription();
						die_inventory.setDieName(description);
						die_inventory.setQuantity(qty);
						die_inventory.setHoldQuantity(holdqty);
						die_inventory.setInspectionRequiredQuantity(inspectionqty);
						dieQuantity.add(die_inventory);
					}
				}
			}
			Long qty = CarrierMes.getEmptyCarrierCountByStops(stops);
			if (qty > 0L) {
				DieInventory die_inventory = new DieInventory(i++);
				die_inventory.setDieName("Empty Carrier Count");
				die_inventory.setQuantity(qty);
				die_inventory.setHoldQuantity(0l);
				die_inventory.setInspectionRequiredQuantity(0l);
				dieQuantity.add(die_inventory);
			}
		}

		return dieQuantity;
	}

	private static Long getEmptyCarrierCountByStops(String stops) {
		int emptyDieNumber = 999;
		Long qty = 0L;
		Query q = entityManager().createQuery(
				"SELECT COUNT(o) FROM CarrierMes o where o.dieNumber=:dienumber and destination IN (" + stops + ")");
		q.setParameter("dienumber", emptyDieNumber);
		if (q.getResultList() != null && q.getResultList().size() > 0) {
			qty = (Long) q.getSingleResult();
		}
		return qty;
	}

	public static List<DieInventory> findPartCountsByDies() {
		List<DieInventory> dieQuantity = new ArrayList<DieInventory>();
		int emptyDieNumber = 999;
		long i = 0;

		Query q1 = entityManager()
				.createQuery("SELECT distinct o.dieNumber FROM CarrierMes o where o.dieNumber !=:emptyDieNumber");
		q1.setParameter("emptyDieNumber", emptyDieNumber);
		List<Integer> dieNumbers = q1.getResultList();
		for (int dieId : dieNumbers) {
			if (dieId != emptyDieNumber) {
				Long qty = 0l;
				Long holdqty = 0l;
				Long inspectionqty = 0l;

				String sql = "SELECT sum(o.quantity) FROM CarrierMes o where o.dieNumber=:dienumber ";

				Query q = entityManager().createQuery(sql);
				q.setParameter("dienumber", dieId);

				String holdsql = "SELECT sum(o.quantity) FROM CarrierMes o where o.dieNumber=:dienumber and o.status=:carrierstatus";
				Query holdq = entityManager().createQuery(holdsql);
				holdq.setParameter("dienumber", dieId);
				holdq.setParameter("carrierstatus", CarrierStatus.ON_HOLD.type());

				String inspectionsql = "SELECT sum(o.quantity) FROM CarrierMes o where o.dieNumber=:dienumber and o.status=:carrierstatus";
				Query inspectionq = entityManager().createQuery(inspectionsql);
				inspectionq.setParameter("dienumber", dieId);
				inspectionq.setParameter("carrierstatus", CarrierStatus.INSPECTION_REQUIRED.type());

				if (q.getResultList() != null && q.getResultList().size() > 0) {
					qty = ((Long) q.getSingleResult());
				}

				if (holdq.getResultList() != null && holdq.getResultList().size() > 0) {
					holdqty = ((Long) holdq.getSingleResult());
				}

				if (inspectionq.getResultList() != null && inspectionq.getResultList().size() > 0) {
					inspectionqty = ((Long) inspectionq.getSingleResult());
				}
				if (qty != null) {
					DieInventory die_inventory = new DieInventory(i++);
					Die die = Die.findDie(new Long(dieId));
					String description = die == null ? dieId + "" : die.getDescription();
					die_inventory.setDieName(description);
					die_inventory.setQuantity(qty);
					die_inventory.setHoldQuantity(holdqty);
					die_inventory.setInspectionRequiredQuantity(inspectionqty);
					dieQuantity.add(die_inventory);
				}
			}
		}
		Long qty = CarrierMes.getEmptyCarrierCount();
		if (qty > 0L) {
			DieInventory die_inventory = new DieInventory(i++);
			die_inventory.setDieName("Empty Carriers");
			die_inventory.setQuantity(qty);
			die_inventory.setHoldQuantity(0l);
			die_inventory.setInspectionRequiredQuantity(0l);
			dieQuantity.add(die_inventory);
		}

		return dieQuantity;
	}

	private static Long getEmptyCarrierCount() {
		int emptyDieNumber = 999;
		Long qty = 0L;
		Query q = entityManager().createQuery("SELECT COUNT(o) FROM CarrierMes o where o.dieNumber=:dienumber ");
		q.setParameter("dienumber", emptyDieNumber);
		if (q.getResultList() != null && q.getResultList().size() > 0) {
			qty = (Long) q.getSingleResult();
		}
		return qty;
	}

	public static List<Integer> getProductionRunNumbers() {
		List<Integer> productionRunNumbers = new ArrayList<Integer>();
		Query q = entityManager().createQuery("SELECT distinct o.productionRunNumber FROM CarrierMes o");

		if (q.getResultList() != null && q.getResultList().size() > 0) {
			return (List<Integer>) q.getResultList();
		}
		productionRunNumbers.add(0);
		return productionRunNumbers;
	}

	public static List<CarrierMes> getAllCarriersInLane(Long stopNumber) {
		Query q = entityManager().createQuery(
				"SELECT o FROM CarrierMes o WHERE o.currentLocation = :stopNumber OR destination = :stopNumber order by buffer desc, o.updateDate asc",
				CarrierMes.class);
		q.setParameter("stopNumber", stopNumber);
		List<CarrierMes> carriers = q.getResultList();

		return carriers;
	}

	public static List<CarrierMes> getReleasingCarriersInArea(List<Long> stopNumbers) {
		Query q = entityManager().createQuery(
				"SELECT o FROM CarrierMes o WHERE o.currentLocation IN (:locations) AND o.currentLocation <> o.destination AND buffer = 1 order by o.currentLocation",
				CarrierMes.class);
		q.setParameter("locations", stopNumbers);

		List<CarrierMes> carriers = q.getResultList();

		return carriers;
	}

	public static Die getDieByProductionRunNumber(Integer prodRunNo) {
		Query q = entityManager().createQuery(
				"SELECT distinct o.dieNumber FROM CarrierMes o where o.productionRunNumber=:prodRunNo", Integer.class);
		q.setParameter("prodRunNo", prodRunNo);
		List<Integer> dies = q.getResultList();

		if (dies != null && dies.size() > 0) {
			return Die.findDie(Long.parseLong(dies.get(0).toString()));
		}

		return null;
	}

	public static List<CarrierMes> findCarriersByCarrierNumberAndDieAndCurrentLocationAndCarrierStatusAndPressAndProductionRunNo(
			CarrierFinderCriteria finderCriteria, Integer page, Integer sizeNo) {
		EntityManager em = CarrierMes.entityManager();
		TypedQuery<CarrierMes> q = null;
		String sql = "SELECT o FROM CarrierMes AS o ";
		List<String> criteriaList = new ArrayList<String>();
		q = em.createQuery(sql, CarrierMes.class);

		if (finderCriteria.getCarrierNumber() != null) {
			criteriaList.add("o.carrierNumber = :carrierNumber");
		}
		if (finderCriteria.getDie() != null) {
			criteriaList.add("o.dieNumber = :die");
		}
		if (finderCriteria.getCurrentLocation() != null) {
			criteriaList.add("o.currentLocation = :currentLocation");
		}
		if (finderCriteria.getCarrierStatus() != null) {
			criteriaList.add("o.status = :carrierStatus");
		}
		if (finderCriteria.getPress() != null) {
			criteriaList.add("o.originationLocation = :press");
		}
		if (finderCriteria.getProductionRunNo() != null) {
			criteriaList.add("o.productionRunNumber = :productionRunNo");
		}
		if (finderCriteria.getDestination() != null) {
			criteriaList.add("o.destination = :destination");
		}
		addBitInfoToCriteriaList(criteriaList, finderCriteria.getBitInfo());
		if (criteriaList.size() > 0) {
			sql = sql + " where ";

			String tempsql = "";
			tempsql = criteriaList.get(0);
			for (int i = 1; i < criteriaList.size(); i++) {
				tempsql = tempsql + " AND " + criteriaList.get(i);
			}
			String orderBySql = " order by o.destination,o.buffer desc, o.updateDate asc";
			q = em.createQuery(sql + tempsql + orderBySql, CarrierMes.class);

			if (finderCriteria.getCarrierNumber() != null) {
				q.setParameter("carrierNumber", finderCriteria.getCarrierNumber());
			}
			if (finderCriteria.getDie() != null) {
				q.setParameter("die", Integer.parseInt(finderCriteria.getDie().getId().toString()));
			}
			if (finderCriteria.getCurrentLocation() != null) {
				q.setParameter("currentLocation", finderCriteria.getCurrentLocation().getId());
			}
			if (finderCriteria.getCarrierStatus() != null) {
				q.setParameter("carrierStatus", finderCriteria.getCarrierStatus().type());
			}
			if (finderCriteria.getPress() != null) {
				q.setParameter("press", finderCriteria.getPress().type());
			}
			if (finderCriteria.getProductionRunNo() != null) {
				q.setParameter("productionRunNo", finderCriteria.getProductionRunNo());
			}
			if (finderCriteria.getDestination() != null) {
				q.setParameter("destination", finderCriteria.getDestination().getId());
			}
		}
		List<CarrierMes> carrierMesList = q.getResultList();
		if (page == null || sizeNo == null) {
			return carrierMesList;
		} else {
			return q.setFirstResult(page == null ? 0 : (page.intValue() - 1) * sizeNo).setMaxResults(sizeNo)
					.getResultList();
		}
	}

	public static Long getFindCarrierCount(CarrierFinderCriteria finderCriteria) {
		EntityManager em = CarrierMes.entityManager();
		TypedQuery<Long> q = null;
		String sql = "SELECT count(o) FROM CarrierMes AS o ";
		List<String> criteriaList = new ArrayList<String>();
		addBitInfoToCriteriaList(criteriaList, finderCriteria.getBitInfo());
		if (finderCriteria.getCarrierNumber() != null) {
			criteriaList.add("o.carrierNumber = :carrierNumber");
		}
		if (finderCriteria.getDie() != null) {
			criteriaList.add("o.dieNumber = :die");
		}
		if (finderCriteria.getCurrentLocation() != null) {
			criteriaList.add("o.currentLocation = :currentLocation");
		}
		if (finderCriteria.getCarrierStatus() != null) {
			criteriaList.add("o.status = :carrierStatus");
		}
		if (finderCriteria.getPress() != null) {
			criteriaList.add("o.originationLocation = :press");
		}
		if (finderCriteria.getProductionRunNo() != null) {
			criteriaList.add("o.productionRunNumber = :productionRunNo");
		}
		if (finderCriteria.getDestination() != null) {
			criteriaList.add("o.destination = :destination");
		}
		String tempsql = "";
		if (criteriaList.size() > 0) {
			sql = sql + " where ";

			tempsql = criteriaList.get(0);
			for (int i = 1; i < criteriaList.size(); i++) {
				tempsql = tempsql + " AND " + criteriaList.get(i);
			}
			q = em.createQuery(sql + tempsql, Long.class);
			if (finderCriteria.getCarrierNumber() != null) {
				q.setParameter("carrierNumber", finderCriteria.getCarrierNumber());
			}
			if (finderCriteria.getDie() != null) {
				q.setParameter("die", Integer.parseInt(finderCriteria.getDie().getId().toString()));
			}
			if (finderCriteria.getCurrentLocation() != null) {
				q.setParameter("currentLocation", finderCriteria.getCurrentLocation().getId());
			}
			if (finderCriteria.getCarrierStatus() != null) {
				q.setParameter("carrierStatus", finderCriteria.getCarrierStatus().type());
			}
			if (finderCriteria.getPress() != null) {
				q.setParameter("press", finderCriteria.getPress().type());
			}
			if (finderCriteria.getProductionRunNo() != null) {
				q.setParameter("productionRunNo", finderCriteria.getProductionRunNo());
			}
			if (finderCriteria.getDestination() != null) {
				q.setParameter("destination", finderCriteria.getDestination().getId());
			}

		}
		if (q == null) {
			q = em.createQuery(sql, Long.class);
		}
		Long count = q.getSingleResult();
		return count != null ? count : 0;
	}

	public static List<CarrierMes> findCarriersByRowAndProductionRunNoAndRobotAndProdRunDate(
			GroupHoldFinderCriteria finderCriteria, Integer page, Integer sizeNo) {
		EntityManager em = CarrierMes.entityManager();
		TypedQuery<CarrierMes> q = null;
		String sql = "SELECT o FROM CarrierMes AS o ";
		List<String> criteriaList = new ArrayList<String>();
		q = em.createQuery(sql, CarrierMes.class);
		String orderBySql = "";
		Integer maxResults = null;
		List<CarrierMes> carrierMesList = new ArrayList<CarrierMes>();

		if (finderCriteria.getProductionRunDate() != null && finderCriteria.getNumberAfterRunDate() != null
				&& finderCriteria.getNumberAfterRunDate() > 0) {
			criteriaList.clear();
			if (finderCriteria.getProductionRunNumber() != null) {
				criteriaList.add("o.productionRunNumber = :productionRunNo");
				orderBySql = " order by productionRunDate asc";
			}
			if (finderCriteria.getRobot() != null) {
				criteriaList.add("o.originationLocation = :robot");
				orderBySql = " order by productionRunDate asc";
			}

			criteriaList.add("o.productionRunDate > :productionRunDate");
			orderBySql = " order by productionRunDate asc";
			maxResults = finderCriteria.getNumberAfterRunDate();

			if (finderCriteria.getRow() != null) {
				if (finderCriteria.getRowAndProdRun()) {
					criteriaList.add("o.currentLocation = :currentLocation");
				}
			}
			criteriaList.add("o.dieNumber!=999");
			criteriaList.add("o.quantity > 0");

			if (criteriaList.size() > 0) {
				String tempsql = "";
				int k = 1;
				String fetchsql = "";
				if (maxResults != null && maxResults > 0) {
					fetchsql = " FETCH FIRST " + maxResults + " ROWS ONLY ";
				}
				tempsql = criteriaList.get(0);
				for (int i = k; i < criteriaList.size(); i++) {
					tempsql = tempsql + " AND " + criteriaList.get(i);
				}

				q = em.createQuery(sql + " WHERE " + tempsql + orderBySql + fetchsql, CarrierMes.class);

				if (finderCriteria.getRow() != null) {
					if (finderCriteria.getRowAndProdRun()) {
						q.setParameter("currentLocation", finderCriteria.getRow().getStop().getId());
					}
				}
				if (finderCriteria.getProductionRunNumber() != null) {
					q.setParameter("productionRunNo", finderCriteria.getProductionRunNumber());
				}
				if (finderCriteria.getRobot() != null) {
					q.setParameter("robot", finderCriteria.getRobot().type());
				}
				if (finderCriteria.getProductionRunDate() != null && ((finderCriteria.getNumberAfterRunDate() != null
						&& finderCriteria.getNumberAfterRunDate() > 0)
						|| (finderCriteria.getNumberBeforeRunDate() != null
								&& finderCriteria.getNumberBeforeRunDate() > 0))) {
					q.setParameter("productionRunDate", finderCriteria.getProductionRunDate());
				}
			}

			List<CarrierMes> carrierList = q.getResultList();

			if (carrierList != null && carrierList.size() > 0) {
				for (int i = 0; i < maxResults; i++) {
					if (carrierList.size() > i) {
						carrierMesList.add(carrierList.get(i));
					}
				}
			}

		}
		if (finderCriteria.getProductionRunDate() != null) {
			criteriaList.clear();
			if (finderCriteria.getProductionRunNumber() != null) {
				criteriaList.add("o.productionRunNumber = :productionRunNo");
			}
			if (finderCriteria.getRobot() != null) {
				criteriaList.add("o.originationLocation = :robot");
			}

			criteriaList.add("o.productionRunDate = :productionRunDate");
			orderBySql = " order by productionRunDate asc";

			if (finderCriteria.getRow() != null) {
				if (finderCriteria.getRowAndProdRun()) {
					criteriaList.add("o.currentLocation = :currentLocation");
				}
			}
			criteriaList.add("o.dieNumber!=999");
			criteriaList.add("o.quantity > 0");

			if (criteriaList.size() > 0) {
				String tempsql = "";
				int k = 1;

				tempsql = criteriaList.get(0);
				for (int i = k; i < criteriaList.size(); i++) {
					tempsql = tempsql + " AND " + criteriaList.get(i);
				}

				q = em.createQuery(sql + " WHERE " + tempsql + orderBySql, CarrierMes.class);

				if (finderCriteria.getRow() != null) {
					if (finderCriteria.getRowAndProdRun()) {
						q.setParameter("currentLocation", finderCriteria.getRow().getId());
					}
				}
				if (finderCriteria.getProductionRunNumber() != null) {
					q.setParameter("productionRunNo", finderCriteria.getProductionRunNumber());
				}
				if (finderCriteria.getRobot() != null) {
					q.setParameter("robot", finderCriteria.getRobot().type());
				}
				if (finderCriteria.getProductionRunDate() != null) {
					q.setParameter("productionRunDate", finderCriteria.getProductionRunDate());
					LOG.info(finderCriteria.getProductionRunDate().toString());
				}
			}

			List<CarrierMes> carrierList = q.getResultList();

			if (carrierList != null && carrierList.size() > 0) {
				carrierMesList.add(carrierList.get(0));
			}
		}
		if (finderCriteria.getProductionRunDate() != null && finderCriteria.getNumberBeforeRunDate() != null
				&& finderCriteria.getNumberBeforeRunDate() > 0) {
			criteriaList.clear();
			if (finderCriteria.getProductionRunNumber() != null) {
				criteriaList.add("o.productionRunNumber = :productionRunNo");
			}
			if (finderCriteria.getRobot() != null) {
				criteriaList.add("o.originationLocation = :robot");
			}

			criteriaList.add(" o.productionRunDate < :productionRunDate");
			maxResults = finderCriteria.getNumberBeforeRunDate();
			orderBySql = " order by o.productionRunDate desc";

			if (finderCriteria.getRow() != null) {
				if (finderCriteria.getRowAndProdRun()) {
					criteriaList.add("o.currentLocation = :currentLocation");
				}
			}
			criteriaList.add("o.dieNumber!=999");
			criteriaList.add("o.quantity > 0");

			if (criteriaList.size() > 0) {
				String tempsql = "";
				int k = 1;
				String fetchsql = "";
				if (maxResults != null && maxResults > 0) {
					fetchsql = " FETCH FIRST " + maxResults + " ROWS ONLY ";
				}
				tempsql = criteriaList.get(0);
				for (int i = k; i < criteriaList.size(); i++) {
					tempsql = tempsql + " AND " + criteriaList.get(i);
				}

				q = em.createQuery(sql + " WHERE " + tempsql + orderBySql + fetchsql, CarrierMes.class);

				if (finderCriteria.getRow() != null) {
					if (finderCriteria.getRowAndProdRun()) {
						q.setParameter("currentLocation", finderCriteria.getRow().getStop().getId());
					}
				}
				if (finderCriteria.getProductionRunNumber() != null) {
					q.setParameter("productionRunNo", finderCriteria.getProductionRunNumber());
				}
				if (finderCriteria.getRobot() != null) {
					q.setParameter("robot", finderCriteria.getRobot().type());
				}
				if (finderCriteria.getProductionRunDate() != null && ((finderCriteria.getNumberAfterRunDate() != null
						&& finderCriteria.getNumberAfterRunDate() > 0)
						|| (finderCriteria.getNumberBeforeRunDate() != null
								&& finderCriteria.getNumberBeforeRunDate() > 0))) {
					q.setParameter("productionRunDate", finderCriteria.getProductionRunDate());
				}
			}

			List<CarrierMes> carrierList = q.getResultList();

			if (carrierList != null && carrierList.size() > 0) {
				for (int i = 0; i < maxResults; i++) {
					if (carrierList.size() > i) {
						carrierMesList.add(carrierList.get(i));
					}
				}
			}
		}

		if (finderCriteria.getProductionRunDate() == null) {
			criteriaList.clear();
			if (finderCriteria.getProductionRunNumber() != null) {
				criteriaList.add("o.productionRunNumber = :productionRunNo");
				orderBySql = " order by productionRunDate asc";
			}
			if (finderCriteria.getRobot() != null) {
				criteriaList.add("o.originationLocation = :robot");
				orderBySql = " order by productionRunDate asc";
			}

			if (finderCriteria.getRow() != null) {
				if (finderCriteria.getRowAndProdRun()) {
					criteriaList.add("o.currentLocation = :currentLocation");
				}
			}
			criteriaList.add("o.dieNumber!=999");
			criteriaList.add("o.quantity > 0");

			if (criteriaList.size() > 0) {
				String tempsql = "";
				int k = 1;

				tempsql = criteriaList.get(0);
				for (int i = k; i < criteriaList.size(); i++) {
					tempsql = tempsql + " AND " + criteriaList.get(i);
				}

				q = em.createQuery(sql + " WHERE " + tempsql + orderBySql, CarrierMes.class);

				if (finderCriteria.getRow() != null) {
					if (finderCriteria.getRowAndProdRun()) {
						q.setParameter("currentLocation", finderCriteria.getRow().getStop().getId());
					}
				}
				if (finderCriteria.getProductionRunNumber() != null) {
					q.setParameter("productionRunNo", finderCriteria.getProductionRunNumber());
				}
				if (finderCriteria.getRobot() != null) {
					q.setParameter("robot", finderCriteria.getRobot().type());
				}

			}

			if (page == null || sizeNo == null) {
				return q.getResultList();
			} else {
				return q.setFirstResult(page == null ? 0 : (page.intValue() - 1) * sizeNo).setMaxResults(sizeNo)
						.getResultList();
			}
		}
		if (page == null || sizeNo == null) {
			return carrierMesList;
		} else {
			List<CarrierMes> pagedList = new ArrayList<CarrierMes>();
			int firstresult = (page - 1) * sizeNo;
			int max = page * sizeNo;
			for (int i = firstresult; i < max; i++) {
				if (carrierMesList.size() > i) {
					pagedList.add(carrierMesList.get(i));
				}
			}

			return pagedList;
		}
	}

	public static Long getGroupHoldCarrierCount(GroupHoldFinderCriteria finderCriteria) {
		EntityManager em = CarrierMes.entityManager();
		TypedQuery<CarrierMes> q = null;
		String sql = "SELECT o FROM CarrierMes AS o ";
		List<String> criteriaList = new ArrayList<String>();
		q = em.createQuery(sql, CarrierMes.class);
		String orderBySql = "";
		Integer maxResults = null;
		Long count = 0l;
		List<CarrierMes> carrierMesList = new ArrayList<CarrierMes>();

		if (finderCriteria.getProductionRunDate() != null && finderCriteria.getNumberAfterRunDate() != null
				&& finderCriteria.getNumberAfterRunDate() > 0) {
			criteriaList.clear();
			if (finderCriteria.getProductionRunNumber() != null) {
				criteriaList.add("o.productionRunNumber = :productionRunNo");
				orderBySql = " order by productionRunDate asc";
			}
			if (finderCriteria.getRobot() != null) {
				criteriaList.add("o.originationLocation = :robot");
				orderBySql = " order by productionRunDate asc";
			}

			criteriaList.add("o.productionRunDate > :productionRunDate");
			orderBySql = " order by productionRunDate asc";
			maxResults = finderCriteria.getNumberAfterRunDate();

			if (finderCriteria.getRow() != null) {
				if (finderCriteria.getRowAndProdRun()) {
					criteriaList.add("o.currentLocation = :currentLocation");
				}
			}
			criteriaList.add("o.dieNumber!=999");
			criteriaList.add("o.quantity > 0");

			if (criteriaList.size() > 0) {
				String tempsql = "";
				int k = 1;
				String fetchsql = "";
				if (maxResults != null && maxResults > 0) {
					fetchsql = " FETCH FIRST " + maxResults + " ROWS ONLY ";
				}
				tempsql = criteriaList.get(0);
				for (int i = k; i < criteriaList.size(); i++) {
					tempsql = tempsql + " AND " + criteriaList.get(i);
				}

				q = em.createQuery(sql + " WHERE " + tempsql + orderBySql + fetchsql, CarrierMes.class);

				if (finderCriteria.getRow() != null) {
					if (finderCriteria.getRowAndProdRun()) {
						q.setParameter("currentLocation", finderCriteria.getRow().getId());
					}
				}
				if (finderCriteria.getProductionRunNumber() != null) {
					q.setParameter("productionRunNo", finderCriteria.getProductionRunNumber());
				}
				if (finderCriteria.getRobot() != null) {
					q.setParameter("robot", finderCriteria.getRobot().type());
				}
				if (finderCriteria.getProductionRunDate() != null && ((finderCriteria.getNumberAfterRunDate() != null
						&& finderCriteria.getNumberAfterRunDate() > 0)
						|| (finderCriteria.getNumberBeforeRunDate() != null
								&& finderCriteria.getNumberBeforeRunDate() > 0))) {
					q.setParameter("productionRunDate", finderCriteria.getProductionRunDate());
				}
			}

			List<CarrierMes> carrierList = q.getResultList();

			if (carrierList != null && carrierList.size() > 0) {
				for (int i = 0; i < maxResults; i++) {
					if (carrierList.size() > i) {
						carrierMesList.add(carrierList.get(i));
					}
				}
			}

		}
		if (finderCriteria.getProductionRunDate() != null) {
			criteriaList.clear();
			if (finderCriteria.getProductionRunNumber() != null) {
				criteriaList.add("o.productionRunNumber = :productionRunNo");
			}
			if (finderCriteria.getRobot() != null) {
				criteriaList.add("o.originationLocation = :robot");
			}

			criteriaList.add("o.productionRunDate = :productionRunDate");
			orderBySql = " order by productionRunDate asc";

			if (finderCriteria.getRow() != null) {
				if (finderCriteria.getRowAndProdRun()) {
					criteriaList.add("o.currentLocation = :currentLocation");
				}
			}
			criteriaList.add("o.dieNumber!=999");
			criteriaList.add("o.quantity > 0");

			if (criteriaList.size() > 0) {
				String tempsql = "";
				int k = 1;

				tempsql = criteriaList.get(0);
				for (int i = k; i < criteriaList.size(); i++) {
					tempsql = tempsql + " AND " + criteriaList.get(i);
				}

				q = em.createQuery(sql + " WHERE " + tempsql + orderBySql, CarrierMes.class);

				if (finderCriteria.getRow() != null) {
					if (finderCriteria.getRowAndProdRun()) {
						q.setParameter("currentLocation", finderCriteria.getRow().getStop().getId());
					}
				}
				if (finderCriteria.getProductionRunNumber() != null) {
					q.setParameter("productionRunNo", finderCriteria.getProductionRunNumber());
				}
				if (finderCriteria.getRobot() != null) {
					q.setParameter("robot", finderCriteria.getRobot().type());
				}
				if (finderCriteria.getProductionRunDate() != null) {
					q.setParameter("productionRunDate", finderCriteria.getProductionRunDate());
					LOG.info(finderCriteria.getProductionRunDate().toString());
				}
			}

			List<CarrierMes> carrierList = q.getResultList();

			if (carrierList != null && carrierList.size() > 0) {
				carrierMesList.add(carrierList.get(0));
			}
		}
		if (finderCriteria.getProductionRunDate() != null && finderCriteria.getNumberBeforeRunDate() != null
				&& finderCriteria.getNumberBeforeRunDate() > 0) {
			criteriaList.clear();
			if (finderCriteria.getProductionRunNumber() != null) {
				criteriaList.add("o.productionRunNumber = :productionRunNo");
				orderBySql = " order by productionRunDate asc";
			}
			if (finderCriteria.getRobot() != null) {
				criteriaList.add("o.originationLocation = :robot");
				orderBySql = " order by productionRunDate asc";
			}

			criteriaList.add(" o.productionRunDate < :productionRunDate");
			maxResults = finderCriteria.getNumberBeforeRunDate();
			orderBySql = " order by o.productionRunDate desc";

			if (finderCriteria.getRow() != null) {
				if (finderCriteria.getRowAndProdRun()) {
					criteriaList.add("o.currentLocation = :currentLocation");
				}
			}
			criteriaList.add("o.dieNumber!=999");
			criteriaList.add("o.quantity > 0");

			if (criteriaList.size() > 0) {
				String tempsql = "";
				int k = 1;
				String fetchsql = "";
				if (maxResults != null && maxResults > 0) {
					fetchsql = " FETCH FIRST " + maxResults + " ROWS ONLY ";
				}
				tempsql = criteriaList.get(0);
				for (int i = k; i < criteriaList.size(); i++) {
					tempsql = tempsql + " AND " + criteriaList.get(i);
				}

				q = em.createQuery(sql + " WHERE " + tempsql + orderBySql + fetchsql, CarrierMes.class);

				if (finderCriteria.getRow() != null) {
					if (finderCriteria.getRowAndProdRun()) {
						q.setParameter("currentLocation", finderCriteria.getRow().getStop().getId());
					}
				}
				if (finderCriteria.getProductionRunNumber() != null) {
					q.setParameter("productionRunNo", finderCriteria.getProductionRunNumber());
				}
				if (finderCriteria.getRobot() != null) {
					q.setParameter("robot", finderCriteria.getRobot().type());
				}
				if (finderCriteria.getProductionRunDate() != null && ((finderCriteria.getNumberAfterRunDate() != null
						&& finderCriteria.getNumberAfterRunDate() > 0)
						|| (finderCriteria.getNumberBeforeRunDate() != null
								&& finderCriteria.getNumberBeforeRunDate() > 0))) {
					q.setParameter("productionRunDate", finderCriteria.getProductionRunDate());
				}
			}

			List<CarrierMes> carrierList = q.getResultList();

			if (carrierList != null && carrierList.size() > 0) {
				for (int i = 0; i < maxResults; i++) {
					if (carrierList.size() > i) {
						carrierMesList.add(carrierList.get(i));
					}
				}
			}
		}

		if (finderCriteria.getProductionRunDate() == null) {
			criteriaList.clear();
			if (finderCriteria.getProductionRunNumber() != null) {
				criteriaList.add("o.productionRunNumber = :productionRunNo");
				orderBySql = " order by productionRunDate asc";
			}
			if (finderCriteria.getRobot() != null) {
				criteriaList.add("o.originationLocation = :robot");
				orderBySql = " order by productionRunDate asc";
			}

			if (finderCriteria.getRow() != null) {
				if (finderCriteria.getRowAndProdRun()) {
					criteriaList.add("o.currentLocation = :currentLocation");
				}
			}
			criteriaList.add("o.dieNumber!=999");
			criteriaList.add("o.quantity > 0");

			if (criteriaList.size() > 0) {
				String tempsql = "";
				int k = 1;

				tempsql = criteriaList.get(0);
				for (int i = k; i < criteriaList.size(); i++) {
					tempsql = tempsql + " AND " + criteriaList.get(i);
				}

				q = em.createQuery(sql + " WHERE " + tempsql + orderBySql, CarrierMes.class);

				if (finderCriteria.getRow() != null) {
					if (finderCriteria.getRowAndProdRun()) {
						q.setParameter("currentLocation", finderCriteria.getRow().getStop().getId());
					}
				}
				if (finderCriteria.getProductionRunNumber() != null) {
					q.setParameter("productionRunNo", finderCriteria.getProductionRunNumber());
				}
				if (finderCriteria.getRobot() != null) {
					q.setParameter("robot", finderCriteria.getRobot().type());
				}
			}
			List<CarrierMes> carrierList = q.getResultList();

			if (carrierList != null) {
				carrierMesList.addAll(carrierList);
			}
		}

		count = new Long(carrierMesList.size());
		return count;
	}

	private static void addBitInfoToCriteriaList(List<String> criteriaList, BitInfo bi) {
		if (bi != null) {
			if (bi.getMaintRequired() != null) {
				criteriaList
						.add("bitwise_and(o.maintenanceBits,1)" + ((bi.getMaintRequired()) ? " = " : " <> ") + "1 ");
			}
			if (bi.isShowBit1() != null)
				criteriaList.add("bitwise_and(o.maintenanceBits,2)" + ((bi.isShowBit1()) ? " = " : " <> ") + "2 ");
			if (bi.isShowBit2() != null)
				criteriaList.add("bitwise_and(o.maintenanceBits,4)" + ((bi.isShowBit2()) ? " = " : " <> ") + "4 ");
			if (bi.isShowBit3() != null)
				criteriaList.add("bitwise_and(o.maintenanceBits,8)" + ((bi.isShowBit3()) ? " = " : " <> ") + "8 ");
			if (bi.isShowBit4() != null)
				criteriaList.add("bitwise_and(o.maintenanceBits,16)" + ((bi.isShowBit4()) ? " = " : " <> ") + "16 ");
			if (bi.isShowBit5() != null)
				criteriaList.add("bitwise_and(o.maintenanceBits,32)" + ((bi.isShowBit5()) ? " = " : " <> ") + "32 ");
			if (bi.isShowBit6() != null)
				criteriaList.add("bitwise_and(o.maintenanceBits,64)" + ((bi.isShowBit6()) ? " = " : " <> ") + "64 ");
			if (bi.isShowBit7() != null)
				criteriaList.add("bitwise_and(o.maintenanceBits,128)" + ((bi.isShowBit7()) ? " = " : " <> ") + "128 ");
			if (bi.isShowBit8() != null)
				criteriaList.add("bitwise_and(o.maintenanceBits,256)" + ((bi.isShowBit8()) ? " = " : " <> ") + "256 ");
			if (bi.isShowBit9() != null)
				criteriaList.add("bitwise_and(o.maintenanceBits,512)" + ((bi.isShowBit9()) ? " = " : " <> ") + "512 ");
			if (bi.isShowBit10() != null)
				criteriaList
						.add("bitwise_and(o.maintenanceBits,1024)" + ((bi.isShowBit10()) ? " = " : " <> ") + "1024 ");
			if (bi.isShowBit11() != null)
				criteriaList
						.add("bitwise_and(o.maintenanceBits,2048)" + ((bi.isShowBit11()) ? " = " : " <> ") + "2048 ");
			if (bi.isShowBit12() != null)
				criteriaList
						.add("bitwise_and(o.maintenanceBits,4096)" + ((bi.isShowBit12()) ? " = " : " <> ") + "4096 ");
			if (bi.isShowBit13() != null)
				criteriaList
						.add("bitwise_and(o.maintenanceBits,8192)" + ((bi.isShowBit13()) ? " = " : " <> ") + "8192 ");
			if (bi.isShowBit14() != null)
				criteriaList
						.add("bitwise_and(o.maintenanceBits,16384)" + ((bi.isShowBit14()) ? " = " : " <> ") + "16384 ");
			if (bi.isShowBit15() != null)
				criteriaList
						.add("bitwise_and(o.maintenanceBits,32768)" + ((bi.isShowBit15()) ? " = " : " <> ") + "32768 ");

		}
		LOG.info("Adding Bit Info to Query Criteria: " + criteriaList);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CarrierNumber: ").append(getCarrierNumber()).append(", ");
		sb.append("TagId: ").append(getTagId()).append(", ");
		sb.append("DieNumber: ").append(getDieNumber()).append(", ");
		sb.append("Quantity: ").append(getQuantity()).append(", ");
		sb.append("UpdateDate: ").append(getUpdateDate()).append(", ");
		sb.append("CurrentLocation: ").append(getCurrentLocation()).append(", ");
		sb.append("Destination: ").append(getDestination()).append(", ");
		sb.append("Status: ").append(getStatus()).append(", ");
		sb.append("OriginationLocation: ").append(getOriginationLocation()).append(", ");
		sb.append("Buffer: ").append(getBuffer()).append(", ");
		sb.append("ProductionRunNumber: ").append(getProductionRunNumber()).append(", ");
		sb.append("ProductionRunDate: ").append(getProductionRunDate());

		return sb.toString();
	}

	// Getters & Setters
	public Integer getMaintenanceBits() {
		if (!maintSetterCalled && !isDirty) {
			setMaintBits();
		} else {
			calcMaintBits();
		}
		return maintenanceBits;
	}

	public void setMaintenanceBits(Integer maintenanceBits) {
		this.maintenanceBits = maintenanceBits;
		setMaintBits();
		maintSetterCalled = true;
	}

	public Integer getMaintRequired() {
		return maintRequired;
	}

	public void setMaintRequired(Integer maintRequired) {
		this.maintRequired = maintRequired;
		isDirty = true;
	}

	public Integer getBit1() {
		return bit1;
	}

	public void setBit1(Integer bit1) {
		this.bit1 = bit1;
		isDirty = true;
	}

	public Integer getBit2() {
		return bit2;
	}

	public void setBit2(Integer bit2) {
		this.bit2 = bit2;
		isDirty = true;
	}

	public Integer getBit3() {
		return bit3;
	}

	public void setBit3(Integer bit3) {
		this.bit3 = bit3;
		isDirty = true;
	}

	public Integer getBit4() {
		return bit4;
	}

	public void setBit4(Integer bit4) {
		this.bit4 = bit4;
		isDirty = true;
	}

	public Integer getBit5() {
		return bit5;
	}

	public void setBit5(Integer bit5) {
		this.bit5 = bit5;
		isDirty = true;
	}

	public Integer getBit6() {
		return bit6;
	}

	public void setBit6(Integer bit6) {
		this.bit6 = bit6;
		isDirty = true;
	}

	public Integer getBit7() {
		return bit7;
	}

	public void setBit7(Integer bit7) {
		this.bit7 = bit7;
		isDirty = true;
	}

	public Integer getBit8() {
		return bit8;
	}

	public void setBit8(Integer bit8) {
		this.bit8 = bit8;
		isDirty = true;
	}

	public Integer getBit9() {
		return bit9;
	}

	public void setBit9(Integer bit9) {
		this.bit9 = bit9;
		isDirty = true;
	}

	public Integer getBit10() {
		return bit10;
	}

	public void setBit10(Integer bit10) {
		this.bit10 = bit10;
		isDirty = true;
	}

	public Integer getBit11() {
		return bit11;
	}

	public void setBit11(Integer bit11) {
		this.bit11 = bit11;
		isDirty = true;
	}

	public Integer getBit12() {
		return bit12;
	}

	public void setBit12(Integer bit12) {
		this.bit12 = bit12;
		isDirty = true;
	}

	public Integer getBit13() {
		return bit13;
	}

	public void setBit13(Integer bit13) {
		this.bit13 = bit13;
		isDirty = true;
	}

	public Integer getBit14() {
		return bit14;
	}

	public void setBit14(Integer bit14) {
		this.bit14 = bit14;
		isDirty = true;
	}

	public Integer getBit15() {
		return bit15;
	}

	public void setBit15(Integer bit15) {
		this.bit15 = bit15;
		isDirty = true;
	}

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

	public Integer getCarrierNumber() {
		return this.carrierNumber;
	}

	public void setCarrierNumber(Integer carrierNumber) {
		this.carrierNumber = carrierNumber;
	}

	public Integer getTagId() {
		return this.tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public Integer getDieNumber() {
		return this.dieNumber;
	}

	public void setDieNumber(Integer dieNumber) {
		this.dieNumber = dieNumber;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Long getCurrentLocation() {
		return this.currentLocation;
	}

	public void setCurrentLocation(Long currentLocation) {
		this.currentLocation = currentLocation;
	}

	public Long getDestination() {
		return this.destination;
	}

	public void setDestination(Long destination) {
		this.destination = destination;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOriginationLocation() {
		return this.originationLocation;
	}

	public void setOriginationLocation(Integer originationLocation) {
		this.originationLocation = originationLocation;
	}

	public Integer getBuffer() {
		return this.buffer;
	}

	public void setBuffer(Integer buffer) {
		this.buffer = buffer;
	}

	public Integer getProductionRunNumber() {
		return this.productionRunNumber;
	}

	public void setProductionRunNumber(Integer productionRunNumber) {
		this.productionRunNumber = productionRunNumber;
	}

	public Date getProductionRunDate() {
		return this.productionRunDate;
	}

	public void setProductionRunDate(Date productionRunDate) {
		this.productionRunDate = productionRunDate;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
