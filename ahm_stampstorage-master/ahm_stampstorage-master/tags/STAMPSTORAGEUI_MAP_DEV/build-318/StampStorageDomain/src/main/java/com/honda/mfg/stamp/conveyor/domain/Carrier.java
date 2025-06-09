package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.Press;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Configurable
@Entity
@Table(name = "CARRIER_TBX")
/**
 * User: Jeffrey M Lutz
 * Date: Jan 24, 2011
 */
public class Carrier {
	private static final Logger LOG = LoggerFactory.getLogger(Carrier.class);

	@NotNull
	@Column(name = "Carrier_Number")
	private Integer carrierNumber;

	@NotNull
	@Column(name = "QUANTITY")
	private Integer quantity;

	@ManyToOne
	private Die die;

	@OneToOne
	private Stop currentLocation;

	@OneToOne
	private Stop destination;

	@NotNull
	@Column(name = "Carrier_STATUS")
	@Enumerated
	private CarrierStatus carrierStatus;

	@NotNull
	@Column(name = "PRESS_INFO")
	@Enumerated
	private Press press;

	@Column(name = "LOAD_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "SS")
	private Date loadTimestamp;

	@Column(name = "UNLOAD_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "SS")
	private Date unloadTimestamp;

	@Column(name = "PRODUCTION_RUN_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "SS")
	private Date stampingProductionRunTimestamp;

	@Column(name = "PRODUCTION_RUN_NO")
	private Integer productionRunNo;

	private Integer buffer;

	private Date updateDate;

	private boolean reprocess;

	private Integer positionInLane;

	private boolean alreadyInLane;

	private boolean movingOutOfLane = false;

	private String source;

	private boolean submitToReleaseManager;

	private boolean inQueueToMoveOutOfLane = false;

	private boolean movingInToLane = false;


	@Transient
	private Integer maintenanceBits;
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


	private void calcMaintBits(){
		if(maintRequired==null && bit1==null && bit2==null && bit3==null && bit4==null && bit5==null && bit6==null && bit7==null && bit8==null &&
				bit9==null && bit10==null && bit11==null && bit12==null && bit13==null && bit14==null && bit15==null){
			maintenanceBits =null;
		}else{
			maintenanceBits= 0;
			maintenanceBits= maintenanceBits +  ((maintRequired==null||maintRequired==0)?0:1);
			maintenanceBits= maintenanceBits +  ((bit1==null||bit1==0)?0:2);
			maintenanceBits= maintenanceBits +  ((bit2==null||bit2==0)?0:4);
			maintenanceBits= maintenanceBits +  ((bit3==null||bit3==0)?0:8);
			maintenanceBits= maintenanceBits +  ((bit4==null||bit4==0)?0:16);
			maintenanceBits= maintenanceBits +  ((bit5==null||bit5==0)?0:32);
			maintenanceBits= maintenanceBits +  ((bit6==null||bit6==0)?0:64);
			maintenanceBits= maintenanceBits +  ((bit7==null||bit7==0)?0:128);
			maintenanceBits= maintenanceBits +  ((bit8==null||bit8==0)?0:256);
			maintenanceBits= maintenanceBits +  ((bit9==null||bit9==0)?0:512);
			maintenanceBits= maintenanceBits +  ((bit10==null||bit10==0)?0:1024);
			maintenanceBits= maintenanceBits +  ((bit11==null||bit11==0)?0:2048);
			maintenanceBits= maintenanceBits +  ((bit12==null||bit12==0)?0:4096);
			maintenanceBits= maintenanceBits +  ((bit13==null||bit13==0)?0:8192);
			maintenanceBits= maintenanceBits +  ((bit14==null||bit14==0)?0:16384);
			maintenanceBits= maintenanceBits +  ((bit15==null||bit15==0)?0:32768);
		}
	}
	private void setMaintBits(){
		int maintBits =0;
		if(maintenanceBits==null) {
			maintenanceBits=null;
			maintRequired  = null;
			bit1=null;
			bit2=null;
			bit3=null;
			bit4=null;
			bit5=null;
			bit6=null;
			bit7=null;
			bit8=null;
			bit9=null;
			bit10=null;
			bit11=null;
			bit12=null;
			bit13=null;
			bit14=null;
			bit15=null;
			
		}else{
			maintBits = maintenanceBits;
			maintRequired = maintBits>>0 & 1;
			bit1 = maintBits>>1 & 1;
			bit2 = maintBits>>2 & 1;
			bit3 = maintBits>>3 & 1;
			bit4 = maintBits>>4 & 1;
			bit5 = maintBits>>5 & 1;
			bit6 = maintBits>>6 & 1;
			bit7 = maintBits>>7 & 1;
			bit8 = maintBits>>8 & 1;
			bit9 = maintBits>>9 & 1;
			bit10 = maintBits>>10 & 1;
			bit11 = maintBits>>11 & 1;
			bit12 = maintBits>>12 & 1;
			bit13 = maintBits>>13 & 1;
			bit14 = maintBits>>14 & 1;
			bit15 = maintBits>>15 & 1;
		}

	}
	public Carrier() {
	}

	public Carrier(long id) {
		this(id, null, 0, null, null);
	}

	public Carrier(long id, Timestamp stampingProductionRunTimestamp, Integer productionRunNo, Stop currentLocation, Stop destination, CarrierStatus carrierStatus, Integer carrierNumber, Die die) {
		this(id, die, Integer.valueOf(10), stampingProductionRunTimestamp, productionRunNo);

		this.currentLocation = currentLocation;
		this.destination = destination;
		this.carrierStatus = carrierStatus;
		this.carrierNumber = carrierNumber;
	}

	public Carrier(long id, Die die, int quantity, Timestamp stampingProductionRunTimestamp, Integer productionRunNo) {
		this.setId(id);
		this.productionRunNo = productionRunNo;
		this.stampingProductionRunTimestamp = stampingProductionRunTimestamp;
		this.die = die;
		this.quantity = quantity;
		this.carrierNumber = (int) id;

	}

	public PartProductionVolume getPartProductionVolume() {
		if (die != null) {
			return die.getPartProductionVolume();
		}
		return null;
	}

	//    public boolean equals(Object obj, boolean storageStateCompare) {
	//
	//        if (storageStateCompare) {
	//            if (obj instanceof Carrier) {
	//                Carrier tempCarrier = (Carrier) obj;
	//                LOG.trace(tempCarrier.getQuantity() + "-" + getQuantity() + "," + tempCarrier.getDie() + "-" + getDie());
	//                if (tempCarrier.getDie() == null && getDie() == null) {
	//                    return true;
	//                }
	//
	//                boolean flag1 = tempCarrier.getQuantity().equals(quantity) && tempCarrier.getDie().equals(getDie());
	//                boolean flag2 = tempCarrier.getCarrierStatus().equals(getCarrierStatus()) && tempCarrier.getProductionRunNo().equals(getProductionRunNo()) && tempCarrier.getStampingProductionRunTimestamp().equals(getStampingProductionRunTimestamp());
	//                boolean flag3 = tempCarrier.getId() == getId() && tempCarrier.getCarrierNumber().equals(carrierNumber);
	//                boolean flag4 = tempCarrier.getCurrentLocation().equals(getCurrentLocation(), storageStateCompare) && tempCarrier.getDestination().equals(getDestination(), storageStateCompare);
	//                if (flag1 && flag2 && flag3 && flag4) {
	//                    return true;
	//                } else {
	//                    return false;
	//                }
	//
	//            } else {
	//                return false;
	//            }
	//        }
	//        return ((Carrier) obj).getId().longValue() == getId().longValue();
	//    }

	public boolean isMoving() {
		if (currentLocation != null) {
			if (currentLocation.equals(destination)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		LOG.info("hashCode() called.  hashcode: " + super.hashCode());
		return super.hashCode();    //To change body of overridden methods use File | Settings | File Templates.
	}

	public boolean isLoadedLike(Carrier rhs) {
		Carrier lhs = this;
		if (lhs.getCarrierNumber() != null) {
			if (!lhs.getCarrierNumber().equals(rhs.getCarrierNumber())) {
				return false;
			}
		} else if (rhs.getCarrierNumber() != null) {
			return false;
		}
		if (lhs.getQuantity() != null) {
			if (!lhs.getQuantity().equals(rhs.getQuantity())) {
				return false;
			}
		} else if (rhs.getQuantity() != null) {
			return false;
		}
		if (lhs.getDie() != null) {
			if (!lhs.getDie().equals(rhs.getDie())) {
				return false;
			}
		} else if (rhs.getDie() != null) {
			return false;
		}
		return true;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Carrier ) {
			if (getId() == null) {
				return false;
			}
			return getId().equals(((Carrier) obj).getId());
		}
		return false;
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


	//	@Transactional
	//	public void persist() {
	//		if (this.entityManager == null) this.entityManager = entityManager();
	//		this.entityManager.persist(this);
	//	}
	//
	//	@Transactional
	//	public void remove() {
	//		if (this.entityManager == null) this.entityManager = entityManager();
	//		if (this.entityManager.contains(this)) {
	//			this.entityManager.remove(this);
	//		} else {
	//			Carrier attached = Carrier.findCarrier(this.id);
	//			this.entityManager.remove(attached);
	//		}
	//	}
	//
	//	@Transactional
	//	public void flush() {
	//		if (this.entityManager == null) this.entityManager = entityManager();
	//		this.entityManager.flush();
	//	}
	//
	//	@Transactional
	//	public void clear() {
	//		if (this.entityManager == null) this.entityManager = entityManager();
	//		this.entityManager.clear();
	//	}
	//
	//	@Transactional
	//	public Carrier merge() {
	//		if (this.entityManager == null) this.entityManager = entityManager();
	//		Carrier merged = this.entityManager.merge(this);
	//		this.entityManager.flush();
	//		return merged;
	//	}
	//
	//	public static final EntityManager entityManager() {
	//		EntityManager em = new Carrier().entityManager;
	//		if (em == null)
	//			throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
	//		return em;
	//	}
	//
	//	public static long countCarriers() {
	//		return entityManager().createQuery("SELECT COUNT(o) FROM Carrier o", Long.class).getSingleResult();
	//	}
	//
	//	public static List<Carrier> findAllCarriers() {
	//		return entityManager().createQuery("SELECT o FROM Carrier o", Carrier.class).getResultList();
	//	}
	//
	//	public static Carrier findCarrier(Long id) {
	//		if (id == null) return null;
	//		return entityManager().find(Carrier.class, id);
	//	}
	//
	//	public static List<Carrier> findCarrierEntries(int firstResult, int maxResults) {
	//		return entityManager().createQuery("SELECT o FROM Carrier o", Carrier.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	//	}
	//
	//	public static TypedQuery<Carrier> findCarriersByCarrierNumberAndDieAndCurrentLocationAndCarrierStatusAndPressAndProductionRunNo(Integer carrierNumber, Die die, Stop currentLocation, CarrierStatus carrierStatus, Press press, Integer productionRunNo) {
	//		if (carrierNumber == null) throw new IllegalArgumentException("The carrierNumber argument is required");
	//		if (die == null) throw new IllegalArgumentException("The die argument is required");
	//		if (currentLocation == null) throw new IllegalArgumentException("The currentLocation argument is required");
	//		if (carrierStatus == null) throw new IllegalArgumentException("The carrierStatus argument is required");
	//		if (press == null) throw new IllegalArgumentException("The press argument is required");
	//		if (productionRunNo == null) throw new IllegalArgumentException("The productionRunNo argument is required");
	//		EntityManager em = Carrier.entityManager();
	//		TypedQuery<Carrier> q = em.createQuery("SELECT o FROM Carrier AS o WHERE o.carrierNumber = :carrierNumber AND o.die = :die AND o.currentLocation = :currentLocation AND o.carrierStatus = :carrierStatus AND o.press = :press AND o.productionRunNo = :productionRunNo", Carrier.class);
	//		q.setParameter("carrierNumber", carrierNumber);
	//		q.setParameter("die", die);
	//		q.setParameter("currentLocation", currentLocation);
	//		q.setParameter("carrierStatus", carrierStatus);
	//		q.setParameter("press", press);
	//		q.setParameter("productionRunNo", productionRunNo);
	//		return q;
	//	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CarrierNumber: ").append(getCarrierNumber()).append(", ");
		sb.append("Die: ").append(getDie()).append(", ");
		sb.append("Quantity: ").append(getQuantity()).append(", ");
		sb.append("ProductionRunTimestamp: ").append(getStampingProductionRunTimestamp()).append(", ");
		sb.append("CarrierStatus: ").append(getCarrierStatus()).append(", ");
		sb.append("CurrentLocation: ").append(getCurrentLocation()).append(", ");
		sb.append("DestinationLocation: ").append(getDestination()).append(", ");
		sb.append("Die: ").append(getDie()).append(", ");
		sb.append("Id: ").append(getId()).append(", ");
		sb.append("LoadTimestamp: ").append(getLoadTimestamp()).append(", ");
		sb.append("Press: ").append(getPress()).append(", ");
		sb.append("UnloadTimestamp: ").append(getUnloadTimestamp()).append(", ");
		sb.append("Version: ").append(getVersion());
		return sb.toString();
	}
	//Getters & Setters
	public Integer getMaintenanceBits() {
		calcMaintBits();
		return maintenanceBits;
	}

	public void setMaintenanceBits(Integer maintenanceBits) {
		this.maintenanceBits = maintenanceBits;
		setMaintBits();
	}
	public Integer getMaintRequired() {
		return maintRequired;
	}
	public void setMaintRequired(Integer maintRequired) {
		this.maintRequired = maintRequired;
	}
	public Integer getBit1() {
		return bit1;
	}
	public void setBit1(Integer bit1) {
		this.bit1 = bit1;
	}
	public Integer getBit2() {
		return bit2;
	}
	public void setBit2(Integer bit2) {
		this.bit2 = bit2;
	}
	public Integer getBit3() {
		return bit3;
	}
	public void setBit3(Integer bit3) {
		this.bit3 = bit3;
	}
	public Integer getBit4() {
		return bit4;
	}
	public void setBit4(Integer bit4) {
		this.bit4 = bit4;
	}
	public Integer getBit5() {
		return bit5;
	}
	public void setBit5(Integer bit5) {
		this.bit5 = bit5;
	}
	public Integer getBit6() {
		return bit6;
	}
	public void setBit6(Integer bit6) {
		this.bit6 = bit6;
	}
	public Integer getBit7() {
		return bit7;
	}
	public void setBit7(Integer bit7) {
		this.bit7 = bit7;
	}
	public Integer getBit8() {
		return bit8;
	}
	public void setBit8(Integer bit8) {
		this.bit8 = bit8;
	}
	public Integer getBit9() {
		return bit9;
	}
	public void setBit9(Integer bit9) {
		this.bit9 = bit9;
	}
	public Integer getBit10() {
		return bit10;
	}
	public void setBit10(Integer bit10) {
		this.bit10 = bit10;
	}
	public Integer getBit11() {
		return bit11;
	}
	public void setBit11(Integer bit11) {
		this.bit11 = bit11;
	}
	public Integer getBit12() {
		return bit12;
	}
	public void setBit12(Integer bit12) {
		this.bit12 = bit12;
	}
	public Integer getBit13() {
		return bit13;
	}
	public void setBit13(Integer bit13) {
		this.bit13 = bit13;
	}
	public Integer getBit14() {
		return bit14;
	}
	public void setBit14(Integer bit14) {
		this.bit14 = bit14;
	}
	public Integer getBit15() {
		return bit15;
	}
	public void setBit15(Integer bit15) {
		this.bit15 = bit15;
	}
	public Integer getCarrierNumber() {
		return this.carrierNumber;
	}

	public void setCarrierNumber(Integer carrierNumber) {
		this.carrierNumber = carrierNumber;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Die getDie() {
		return this.die;
	}

	public void setDie(Die die) {
		this.die = die;
	}

	public Stop getCurrentLocation() {
		return this.currentLocation;
	}

	public void setCurrentLocation(Stop currentLocation) {
		this.currentLocation = currentLocation;
	}

	public Stop getDestination() {
		return this.destination;
	}

	public void setDestination(Stop destinationLocation) {
		this.destination = destinationLocation;
	}

	public CarrierStatus getCarrierStatus() {
		return this.carrierStatus;
	}

	public void setCarrierStatus(CarrierStatus carrierStatus) {
		this.carrierStatus = carrierStatus;
	}

	public Press getPress() {
		return this.press;
	}

	public void setPress(Press press) {
		this.press = press;
	}

	public Date getLoadTimestamp() {
		return this.loadTimestamp;
	}

	public void setLoadTimestamp(Date loadTimestamp) {
		this.loadTimestamp = loadTimestamp;
	}

	public Date getUnloadTimestamp() {
		return this.unloadTimestamp;
	}

	public void setUnloadTimestamp(Date unloadTimestamp) {
		this.unloadTimestamp = unloadTimestamp;
	}

	public Integer getProductionRunNo() {
		return this.productionRunNo;
	}

	public void setProductionRunNo(Integer productionRunNo) {
		this.productionRunNo = productionRunNo;
	}

	public Date getStampingProductionRunTimestamp() {
		return this.stampingProductionRunTimestamp;
	}

	public void setStampingProductionRunTimestamp(Date stampingProductionRunTimestamp) {
		this.stampingProductionRunTimestamp = stampingProductionRunTimestamp;
	}

	public Integer getBuffer() {
		return buffer;
	}

	public void setBuffer(Integer buffer) {
		this.buffer = buffer;
	}

	public boolean getReprocess() {
		return this.reprocess;
	}

	public void setReprocess(boolean reprocess) {
		this.reprocess = reprocess;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public int getPositionInLane() {
		return this.positionInLane;
	}

	public void setPositionInLane(int positionInLane) {
		this.positionInLane = positionInLane;
	}

	public boolean isAlreadyInLane() {
		return alreadyInLane;
	}

	public void setAlreadyInLane(boolean alreadyInLane) {
		this.alreadyInLane = alreadyInLane;
	}

	public boolean isMovingOutOfLane() {
		return movingOutOfLane;
	}

	public void setMovingOutOfLane(boolean movingOutOfLane) {
		this.movingOutOfLane = movingOutOfLane;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public boolean getSubmitToReleaseManager() {
		return this.submitToReleaseManager;
	}

	public void setSubmitToReleaseManager(boolean submitToReleaseManager) {
		this.submitToReleaseManager = submitToReleaseManager;
	}

	public boolean isInQueueToMoveOutOfLane() {
		return inQueueToMoveOutOfLane;
	}

	public void setInQueueToMoveOutOfLane(boolean inQueueToMoveOutOfLane) {
		this. inQueueToMoveOutOfLane =  inQueueToMoveOutOfLane;
	}

	public boolean isMovingInToLane() {
		return movingInToLane;
	}

	public void setMovingInToLane(boolean movingInToLane) {
		this.movingInToLane = movingInToLane;
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
}