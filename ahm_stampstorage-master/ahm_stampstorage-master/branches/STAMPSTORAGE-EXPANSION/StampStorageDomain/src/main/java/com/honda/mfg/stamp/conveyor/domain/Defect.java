package com.honda.mfg.stamp.conveyor.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import com.honda.mfg.stamp.conveyor.domain.enums.DEFECT_TYPE;
import com.honda.mfg.stamp.conveyor.domain.enums.REWORK_METHOD;

@Configurable
@Entity
@Table(name = "DEFECT_TBX")
public class Defect {

	@NotNull
	@Column(name = "CARRIER_NUMBER")
	private Integer carrierNumber;

	@NotNull
	@Column(name = "PRODUCTIONRUNNUMBER")
	private Integer productionRunNo;

	@NotNull
	@Column(name = "DEFECT_TYPE")
	@Enumerated
	private DEFECT_TYPE defectType;

	@NotNull
	@Column(name = "REWORK_METHOD")
	@Enumerated
	private REWORK_METHOD reworkMethod;

	@NotNull
	@Column(name = "X_Area")
	private Integer xArea;

	@NotNull
	@Column(name = "Y_Area")
	private String yArea;

	@Column(name = "DEFECT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "S-")
	private Date defectTimestamp;

	@Column(name = "DEFECT_REPAIRED")
	private Boolean defectRepaired;

	@Column(name = "NOTE")
	private String note;

	@Column(name = "SOURCE")
	private String source;

	public boolean equals(Object obj) {
		if (obj instanceof Defect) {
			if (getId() == null) {
				return false;
			}
			return getId().equals(((Defect) obj).getId());
		} else {
			return false;
		}
	}

	public int hashCode() {
		// LOG.info("hashCode() called. hashcode: " + super.hashCode());
		return super.hashCode(); // To change body of overridden methods use File | Settings | File Templates.
	}

	public Integer getCarrierNumber() {
		return this.carrierNumber;
	}

	public void setCarrierNumber(Integer carrierNumber) {
		this.carrierNumber = carrierNumber;
	}

	public Integer getProductionRunNo() {
		return this.productionRunNo;
	}

	public void setProductionRunNo(Integer productionRunNo) {
		this.productionRunNo = productionRunNo;
	}

	public DEFECT_TYPE getDefectType() {
		return this.defectType;
	}

	public void setDefectType(DEFECT_TYPE defectType) {
		this.defectType = defectType;
	}

	public REWORK_METHOD getReworkMethod() {
		return this.reworkMethod;
	}

	public void setReworkMethod(REWORK_METHOD reworkMethod) {
		this.reworkMethod = reworkMethod;
	}

	public Integer getXArea() {
		return this.xArea;
	}

	public void setXArea(Integer xArea) {
		this.xArea = xArea;
	}

	public String getYArea() {
		return this.yArea;
	}

	public void setYArea(String yArea) {
		this.yArea = yArea;
	}

	public Date getDefectTimestamp() {
		return this.defectTimestamp;
	}

	public void setDefectTimestamp(Date defectTimestamp) {
		this.defectTimestamp = defectTimestamp;
	}

	public Boolean getDefectRepaired() {
		return this.defectRepaired;
	}

	public void setDefectRepaired(Boolean defectRepaired) {
		this.defectRepaired = defectRepaired;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CarrierNumber: ").append(getCarrierNumber()).append(", ");
		sb.append("DefectRepaired: ").append(getDefectRepaired()).append(", ");
		sb.append("DefectTimestamp: ").append(getDefectTimestamp()).append(", ");
		sb.append("DefectType: ").append(getDefectType()).append(", ");
		sb.append("Id: ").append(getId()).append(", ");
		sb.append("ProductionRunNo: ").append(getProductionRunNo()).append(", ");
		sb.append("ReworkMethod: ").append(getReworkMethod()).append(", ");
		sb.append("Version: ").append(getVersion()).append(", ");
		sb.append("XArea: ").append(getXArea()).append(", ");
		sb.append("YArea: ").append(getYArea());
		sb.append("Note: ").append(getNote());
		return sb.toString();
	}

	@PersistenceContext
	transient EntityManager entityManager;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "DEFECT_ID")
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
			Defect attached = Defect.findDefect(this.id);
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
	public Defect merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Defect merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public static EntityManager entityManager() {
		EntityManager em = new Defect().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countDefects() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Defect o", Long.class).getSingleResult();
	}

	public static List<Defect> findAllDefects() {
		return entityManager().createQuery("SELECT o FROM Defect o", Defect.class).getResultList();
	}

	public static Defect findDefect(Long id) {
		if (id == null)
			return null;
		return entityManager().find(Defect.class, id);
	}

	public static List<Defect> findDefectEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM Defect o", Defect.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	public static List<Defect> findDefectsByCarrierNumberAndProductionRunNo(Integer carrierNumber,
			Integer productionRunNo) {
		if (carrierNumber == null)
			return null;
		Query q = null;
		q = entityManager().createQuery(
				"SELECT o FROM Defect o where o.carrierNumber=:carrierNumber and o.productionRunNo=:productionRunNo Order By o.defectTimestamp",
				Defect.class);
		q.setParameter("carrierNumber", carrierNumber);
		q.setParameter("productionRunNo", productionRunNo);

		List<Defect> defects = q.getResultList();
		return defects;
	}

	public static void removeDefectsByCarrierNumberAndProductionRunNo(Integer carrierNumber, Integer productionRunNo) {
		List<Defect> defects = findDefectsByCarrierNumberAndProductionRunNo(carrierNumber, productionRunNo);
		for (Defect defect : defects) {
			defect.remove();
		}
	}
}
