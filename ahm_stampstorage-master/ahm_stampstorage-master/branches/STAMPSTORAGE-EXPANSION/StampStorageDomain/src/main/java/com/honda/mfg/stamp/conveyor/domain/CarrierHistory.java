package com.honda.mfg.stamp.conveyor.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.Version;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.Press;

@Configurable
@Entity
@Table(name = "CARRIER_MES_ARCHIVE_TBX")
/**
 * User: Adam S. Kendell Date: Feb 02, 2012
 */

public class CarrierHistory {
	private static final Logger LOG = LoggerFactory.getLogger(CarrierHistory.class);

	@Column(name = "BUFFER")
	private Integer buffer;

	@Column(name = "CARRIERNUMBER")
	private Integer carrierNumber;

	@Column(name = "CURRENTLOCATION")
	private Long currentLocation;

	@Column(name = "DESTINATION")
	private Long destination;

	@Column(name = "DIENUMBER")
	private Long dieNumber;

	@Column(name = "ORIGINATIONLOCATION")
	private Integer originationLocation;

	@Column(name = "PRODUCTIONRUNDATE")
	private Timestamp productionRunDate;

	@Column(name = "PRODUCTIONRUNNUMBER")
	private Integer productionRunNumber;

	@Column(name = "QUANTITY")
	private Integer quantity;

	@Column(name = "STATUS")
	@Enumerated
	private CarrierStatus status;

	@Column(name = "TAGID")
	private Long tagId;

	@Column(name = "UPDATEDATE")
	private Timestamp updateDate;

	@Column(name = "SOURCE")
	private String source;

	@Column(name = "CARRIER_MES_ARCHIVE_TSTP")
	private Timestamp carrierMesArchiveTstp;

	public CarrierHistory() {
		super();
	}

	public CarrierHistory(long id) {
		setId(id);
	}

	public boolean equals(Object obj) {
		if (obj instanceof CarrierHistory) {
			if (getId() == null) {
				return false;
			}
			return getId().equals(((CarrierHistory) obj).getId());
		} else {
			return false;
		}
	}

	public int hashCode() {
		// LOG.info("hashCode() called. hashcode: " + super.hashCode());
		return super.hashCode(); // To change body of overridden methods use File | Settings | File Templates.
	}

	public Integer getBuffer() {
		return this.buffer;
	}

	public void setBuffer(Integer buffer) {
		this.buffer = buffer;
	}

	public Integer getCarrierNumber() {
		return this.carrierNumber;
	}

	public void setCarrierNumber(Integer CarrierNumber) {
		this.carrierNumber = CarrierNumber;
	}

	public Stop getCurrentLocation() {
		return Stop.findStop(this.currentLocation);
	}

	public void setCurrentLocation(Long CurrentLocation) {
		this.currentLocation = CurrentLocation;
	}

	public Stop getDestination() {
		return Stop.findStop(this.destination);
	}

	public void setDestination(Long Destination) {
		this.destination = Destination;
	}

	public Die getDieNumber() {
		return Die.findDie(this.dieNumber);
	}

	public void setDieNumber(Long DieNumber) {
		this.dieNumber = DieNumber;
	}

	public Integer getOriginationLocation() {
		return this.originationLocation;
	}

	public void setOriginationLocation(Integer OriginationLocation) {
		this.originationLocation = OriginationLocation;
	}

	public Press getPress() {
		return Press.findByType(this.originationLocation);
	}

	public void setPress(Integer OriginationLocation) {
		this.originationLocation = OriginationLocation;
	}

	public Timestamp getProductionRunDate() {
		return this.productionRunDate;
	}

	public void setProductionRunDate(Timestamp ProductionRunDate) {
		this.productionRunDate = ProductionRunDate;
	}

	public Integer getProductionRunNumber() {
		return this.productionRunNumber;
	}

	public void setProductionRunNumber(Integer ProductionRunNumber) {
		this.productionRunNumber = ProductionRunNumber;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer Quantity) {
		this.quantity = Quantity;
	}

	public CarrierStatus getStatus() {
		return this.status;
	}

	public void setStatus(CarrierStatus Status) {
		this.status = Status;
	}

	public Long getTagId() {
		return this.tagId;
	}

	public void setTagId(Long TagId) {
		this.tagId = TagId;
	}

	public Timestamp getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Timestamp UpdateDate) {
		this.updateDate = UpdateDate;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String Source) {
		this.source = Source;
	}

	public Timestamp getCarrierMesArchiveTstp() {
		return this.carrierMesArchiveTstp;
	}

	public void setCarrierMesArchiveTstp(Timestamp CarrierMesArchiveTstp) {
		this.carrierMesArchiveTstp = CarrierMesArchiveTstp;
	}

	@PersistenceContext
	transient EntityManager entityManager;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CARRIER_ID")
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
			CarrierHistory attached = CarrierHistory.findCarrierHistory(this.id);
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
	public CarrierHistory merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		CarrierHistory merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public static EntityManager entityManager() {
		EntityManager em = new CarrierHistory().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countCarrierHistories() {
		// LOG.debug(entityManager().createQuery("SELECT COUNT(o) FROM CarrierHistory
		// o", Long.class).toString());
		return entityManager().createQuery("SELECT COUNT(o) FROM CarrierHistory o", Long.class).getSingleResult();
	}

	public static List<CarrierHistory> findAllCarrierHistories() {
		return entityManager().createQuery("SELECT o FROM CarrierHistory o ORDER BY o.id desc", CarrierHistory.class)
				.setMaxResults(1000).getResultList();
	}

	public static CarrierHistory findCarrierHistory(Long id) {
		if (id == null)
			return null;
		return entityManager().find(CarrierHistory.class, id);
	}

	public static List<CarrierHistory> findCarrierHistoryEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM CarrierHistory o ORDER BY o.id desc", CarrierHistory.class)
				.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	public static List<CarrierHistory> findCarrierHistoryByCarrierNumber(CarrierHistoryFinderCriteria finderCriteria,
			Integer page, Integer sizeNo) {
		EntityManager em = CarrierHistory.entityManager();
		TypedQuery<CarrierHistory> q = null;
		String sql = "SELECT o FROM CarrierHistory AS o ";
		List<String> criteriaList = new ArrayList<String>();
		q = em.createQuery(sql, CarrierHistory.class);

		if (finderCriteria.getCarrierNumber() != null) {
			criteriaList.add("o.carrierNumber = :carrierNumber");
		}
		if (finderCriteria.getDie() != null) {
			criteriaList.add("o.dieNumber = :die");
		}
		if (finderCriteria.getCurrentLocation() != null) {
			criteriaList.add("o.currentLocation = :currentLocation");
		}
		if (finderCriteria.getDestination() != null) {
			criteriaList.add("o.destination = :destination");
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

		if (criteriaList.size() > 0) {
			sql = sql + " where ";

			String tempsql = "";
			tempsql = criteriaList.get(0);
			for (int i = 1; i < criteriaList.size(); i++) {
				tempsql = tempsql + " AND " + criteriaList.get(i);
			}
			String orderBySql = " order by o.id desc";
			q = em.createQuery(sql + tempsql + orderBySql, CarrierHistory.class);

			if (finderCriteria.getCarrierNumber() != null) {
				q.setParameter("carrierNumber", finderCriteria.getCarrierNumber());
			}
			if (finderCriteria.getDie() != null) {
				q.setParameter("die", finderCriteria.getDie().getId());
			}
			if (finderCriteria.getCurrentLocation() != null) {
				q.setParameter("currentLocation", finderCriteria.getCurrentLocation().getId());
			}
			if (finderCriteria.getProductionRunNo() != null) {
				q.setParameter("productionRunNo", finderCriteria.getProductionRunNo());
			}
			if (finderCriteria.getDestination() != null) {
				q.setParameter("destination", finderCriteria.getDestination().getId());
			}
			if (finderCriteria.getCarrierStatus() != null) {
				q.setParameter("carrierStatus", finderCriteria.getCarrierStatus());
			}
			if (finderCriteria.getPress() != null) {
				q.setParameter("press", finderCriteria.getPress().type());
			}
		}
		if (page == null || sizeNo == null) {
			return q.getResultList();
		} else {
			return q.setFirstResult(page == null ? 0 : (page.intValue() - 1) * sizeNo).setMaxResults(sizeNo)
					.getResultList();
		}
	}

	public static Long getFindCarrierHistoryCount(CarrierHistoryFinderCriteria finderCriteria) {
		EntityManager em = CarrierHistory.entityManager();
		TypedQuery<Long> q = null;
		String sql = "SELECT count(o) FROM CarrierHistory AS o ";
		List<String> criteriaList = new ArrayList<String>();

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

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("buffer: ").append(getBuffer()).append(", ");
		sb.append("carrierMesArchiveTstp: ").append(getCarrierMesArchiveTstp()).append(", ");
		sb.append("CarrierNumber: ").append(getCarrierNumber()).append(", ");
		sb.append("CurrentLocation: ").append(getCurrentLocation()).append(", ");
		sb.append("Destination: ").append(getDestination()).append(", ");
		sb.append("DieNumber: ").append(getDieNumber()).append(", ");
		sb.append("Id: ").append(getId()).append(", ");
		sb.append("OriginationLocation: ").append(getOriginationLocation()).append(", ");
		sb.append("ProductionRunDate: ").append(getProductionRunDate()).append(", ");
		sb.append("ProductionRunNumber: ").append(getProductionRunNumber()).append(", ");
		sb.append("Quantity: ").append(getQuantity()).append(", ");
		sb.append("source: ").append(getSource()).append(", ");
		sb.append("Status: ").append(getStatus()).append(", ");
		sb.append("tagId: ").append(getTagId()).append(", ");
		sb.append("updateDate: ").append(getUpdateDate()).append(", ");
		sb.append("Version: ").append(getVersion());
		return sb.toString();
	}
}
