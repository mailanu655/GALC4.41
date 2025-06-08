package com.honda.galc.entity.product;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Mar 25, 2014
 */




@Entity
@Table(name="GAL305TBX")
public class FrameDelay extends AuditEntry {

	@EmbeddedId
	private FrameDelayId id;

	@Column(name="ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;

	@Column(name="PLANT_CODE")
	private String plantCode;
	
	@Column(name="SHIFT")
	private String shift;
	
	@Column(name="PRODUCTION_DATE")
	private Date productionDate;
		
	@Column(name="CURRENT_PRODUCTION_LOT")
	private String currentProductionLot;
	
	@Column(name="CURRENT_DELAYED_FLAG")
	private Integer currentDelayedFlag;


	private static final long serialVersionUID = 1L;

	public FrameDelay() {
		super();
	}
	
	public FrameDelayId getId() {
		return this.id;
	}

	public void setId(FrameDelayId id) {
		this.id = id;
	}

	public FrameDelay(FrameDelayId id, Timestamp actualTimestamp,
			 String plantCode, String shift,
			Date productionDate, String currentProductionLot,
			Integer currentDelayedFlag) {
		super();
		this.id = id;
		this.actualTimestamp = actualTimestamp;
		this.plantCode = plantCode;
		this.shift = shift;
		this.productionDate = productionDate;
		this.currentProductionLot = currentProductionLot;
		this.currentDelayedFlag = currentDelayedFlag;
	}

	public Timestamp getActualTimestamp() {
		return actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	public String getPlantCode() {
		return StringUtils.trimToEmpty(plantCode);
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getShift() {
		return StringUtils.trimToEmpty(shift);
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public String getCurrentProductionLot() {
		return StringUtils.trimToEmpty(currentProductionLot);
	}

	public void setCurrentProductionLot(String currentProductionLot) {
		this.currentProductionLot = currentProductionLot;
	}

	public Integer getCurrentDelayedFlag() {
		return currentDelayedFlag;
	}

	public void setCurrentDelayedFlag(Integer currentDelayedFlag) {
		this.currentDelayedFlag = currentDelayedFlag;
	}

	@Override
	public String toString() {
		return toString(getId().getCurrentVin(), getId().getPpCurrentlyAt(),
				getId().getPpDelayedAt(), getActualTimestamp(),
				getPlantCode(), getProductionDate(),
				getCurrentProductionLot(), getCurrentDelayedFlag().toString());
	}
}