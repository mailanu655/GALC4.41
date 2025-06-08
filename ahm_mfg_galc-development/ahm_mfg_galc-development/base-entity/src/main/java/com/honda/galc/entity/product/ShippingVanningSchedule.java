package com.honda.galc.entity.product;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AbstractEntity;

/**
 * 
 * <h3>ShippingVanningSchedule Class description</h3>
 * <p> ShippingVanningSchedule description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Jun 1, 2012
 *
 *
 */
@Entity
@Table(name="VANNING_SCHEDULE_TBX")
public class ShippingVanningSchedule extends AbstractEntity {
	@EmbeddedId
	private ShippingVanningScheduleId id;

	@Column(name="TRAILER_ID")
	private Integer trailerId;

	@Column(name="KD_LOT")
	private String kdLot;

	@Column(name="PRODUCTION_LOT")
	private String productionLot;

	private String ymto;

	@Column(name="SCH_QTY")
	private int schQty;

	@Column(name="ACT_QTY")
	private int actQty;

	@Transient
	private String plantCode;
	
	@Transient
	private String trailerNumber;
	
	private static final long serialVersionUID = 1L;
	
	public static final int VANNING_SEQ_INTERVAL = 100000;

	public ShippingVanningSchedule() {
		super();
	}
	
	public ShippingVanningSchedule(Date productionDate, int vanningSeq) {
		ShippingVanningScheduleId id = new ShippingVanningScheduleId();
		id.setProductionDate(productionDate);
		id.setVanningSeq(vanningSeq);
		this.id = id;
	}
	
	public ShippingVanningScheduleId getId() {
		return this.id;
	}

	public void setId(ShippingVanningScheduleId id) {
		this.id = id;
	}
	
	public Integer getTrailerId() {
		return trailerId;
	}

	public void setTrailerId(Integer trailerId) {
		this.trailerId = trailerId;
	}
	
	public String getTrailerNumber() {
		return StringUtils.trim(trailerNumber);
	}

	public void setTrailerNumber(String trailerNumber) {
		this.trailerNumber = trailerNumber;
	}
	
	public String getKdLot() {
		return StringUtils.trim(this.kdLot);
	}

	public void setKdLot(String kdLot) {
		this.kdLot = kdLot;
	}
	
	public String getKdLotIndex() {
		return getKdLot().substring(getKdLot().length() -1);
	}
	
	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getPlant(){
		return StringUtils.substring(kdLot,0,6);
	}

	public String getProductionLot() {
		return StringUtils.trim(this.productionLot);
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	public String getYmto() {
		return StringUtils.trim(this.ymto);
	}

	public void setYmto(String ymto) {
		this.ymto = ymto;
	}

	public int getSchQty() {
		return this.schQty;
	}

	public void setSchQty(int schQty) {
		this.schQty = schQty;
	}

	public int getActQty() {
		return this.actQty;
	}

	public void setActQty(int actQty) {
		this.actQty = actQty;
	}
	
	public ShippingVanningSchedule clone() {
		ShippingVanningSchedule schedule = new ShippingVanningSchedule();
		ShippingVanningScheduleId id = new ShippingVanningScheduleId();
		id.setProductionDate(this.getId().getProductionDate());
		id.setVanningSeq(this.getId().getVanningSeq());
		schedule.setId(id);
		schedule.setTrailerId(this.getTrailerId());
		schedule.setKdLot(this.getKdLot());
		schedule.setProductionLot(this.getProductionLot());
		schedule.setYmto(this.getYmto());
		schedule.setSchQty(this.getSchQty());
		schedule.setActQty(this.getActQty());
		return schedule;
	}
	
	public String toString(){
		return toString(id.getProductionDate(),id.getVanningSeq(),getKdLot(),getProductionLot(),getYmto(),getTrailerId(),getSchQty(),getActQty());
	}


}
