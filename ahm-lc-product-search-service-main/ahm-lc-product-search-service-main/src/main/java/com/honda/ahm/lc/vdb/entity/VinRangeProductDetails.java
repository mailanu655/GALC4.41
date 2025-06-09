package com.honda.ahm.lc.vdb.entity;

import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Immutable;

import com.honda.ahm.lc.vdb.util.Constants;

@Entity
@Immutable
public class VinRangeProductDetails implements IProductDetails {

	

	@Column(name = "kd_lot_number")
	private String kdLotNumber;

	@Column(name = "production_lot")
	private String productionLot;
	
	@Id
	@Column(name = "product_id")
	private String id;

	@Column(name = "engine_serial_no")
	private String engineSerialNo;
	
	@Column(name = "ENGINE_MTO")
	private String engineMto;
	

	@Column(name = "AF_ON_SEQUENCE_NUMBER")
	private Integer afOnSequenceNumber;

	@Column(name = "product_spec_code")
	private String productSpecCode;
	
	
	
	@Column(name = "SALES_MODEL_TYPE_CODE")
	private String destination;
	
	@Column(name = "PROCESS_POINT_NAME")
	private String lastProcessPoint;
	
	@Transient
	private Timestamp actualTimestamp;
	
	@Column(name = "line_name")
	private String trackingStatus;
	

	@Transient
	private Timestamp trackingStatusDate;
	
	
	@Column(name = "line_id")
	private String lineId;

	@Column(name = "division_name")
	private String divisionName;
	
	@Column(name = "PROCESS_POINT_ID")
	private String lastProcessPointId;
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public VinRangeProductDetails() {
		super();
	}

	public Integer getAfOnSequenceNumber() {
		return afOnSequenceNumber;
	}

	public void setAfOnSequenceNumber(Integer afOnSequenceNumber) {
		this.afOnSequenceNumber = afOnSequenceNumber;
	}

	public String getProductionLot() {
		return StringUtils.trimToEmpty(productionLot);
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	public String getKdLotNumber() {
		return StringUtils.trimToEmpty(kdLotNumber);
	}

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

	public String getTrackingStatus() {
		return StringUtils.trimToEmpty(trackingStatus);
	}

	public void setTrackingStatus(String trackingStatus) {
		this.trackingStatus = trackingStatus;
	}

	public String getProductSpecCode() {
		return StringUtils.trimToEmpty(productSpecCode);
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getDivisionName() {
		return StringUtils.trimToEmpty(divisionName);
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	

	public String getLastProcessPoint() {
		return lastProcessPoint;
	}

	public void setLastProcessPoint(String lastProcessPoint) {
		this.lastProcessPoint = lastProcessPoint;
	}

	public String getLastProcessPointId() {
		return lastProcessPointId;
	}

	public void setLastProcessPointId(String lastProcessPointId) {
		this.lastProcessPointId = lastProcessPointId;
	}

	public String getEngineSerialNo() {
		return engineSerialNo;
	}

	public void setEngineSerialNo(String engineSerialNo) {
		this.engineSerialNo = engineSerialNo;
	}

	public String getUniqueKey() {
		return getKdLotNumber() + "" + Constants.SEPARATOR + "" + getProductionLot();
	}

	public Timestamp getTrackingStatusDate() {
		return trackingStatusDate;
	}

	public void setTrackingStatusDate(Timestamp trackingStatusDate) {
		this.trackingStatusDate = trackingStatusDate;
	}

	public Timestamp getActualTimestamp() {
		return actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getEngineMto() {
		return engineMto;
	}

	public void setEngineMto(String engineMto) {
		this.engineMto = engineMto;
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(actualTimestamp, afOnSequenceNumber, destination, divisionName, engineMto, engineSerialNo,
				id, kdLotNumber, lastProcessPoint, lastProcessPointId, lineId, productSpecCode, productionLot,
				trackingStatus, trackingStatusDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VinRangeProductDetails other = (VinRangeProductDetails) obj;
		return Objects.equals(actualTimestamp, other.actualTimestamp)
				&& Objects.equals(afOnSequenceNumber, other.afOnSequenceNumber)
				&& Objects.equals(destination, other.destination) && Objects.equals(divisionName, other.divisionName)
				&& Objects.equals(engineMto, other.engineMto) && Objects.equals(engineSerialNo, other.engineSerialNo)
				&& Objects.equals(id, other.id) && Objects.equals(kdLotNumber, other.kdLotNumber)
				&& Objects.equals(lastProcessPoint, other.lastProcessPoint)
				&& Objects.equals(lastProcessPointId, other.lastProcessPointId) && Objects.equals(lineId, other.lineId)
				&& Objects.equals(productSpecCode, other.productSpecCode)
				&& Objects.equals(productionLot, other.productionLot)
				&& Objects.equals(trackingStatus, other.trackingStatus)
				&& Objects.equals(trackingStatusDate, other.trackingStatusDate);
	}

	@Override
	public String toString() {
		return "VinRangeProductDetails [id=" + id + ", kdLotNumber=" + kdLotNumber + ", productionLot=" + productionLot
				+ ", engineSerialNo=" + engineSerialNo + ", engineMto=" + engineMto + ", afOnSequenceNumber="
				+ afOnSequenceNumber + ", productSpecCode=" + productSpecCode + ", destination=" + destination
				+ ", lastProcessPoint=" + lastProcessPoint + ", actualTimestamp=" + actualTimestamp
				+ ", trackingStatus=" + trackingStatus + ", trackingStatusDate=" + trackingStatusDate + ", lineId="
				+ lineId + ", divisionName=" + divisionName + ", lastProcessPointId=" + lastProcessPointId + "]";
	}
	
	

	

	

}
