package com.honda.ahm.lc.vdb.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;

import com.honda.ahm.lc.vdb.util.Constants;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>HistoryDetails</code> is view class for History.
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Hemant Rajput
 * @created Apr 22, 2021
 */

@Entity
@Immutable
@Subselect(value = "select distinct " + "hist.PRODUCT_ID as id" + ", hist.ACTUAL_TIMESTAMP as actual_timestamp "
		+ ", a.AF_ON_SEQUENCE_NUMBER as af_on_sequence_number " + ", b.SALES_MODEL_TYPE_CODE as destination "
		+ ", d.DIVISION_ID as division_id " + ", d.DIVISION_NAME as division_name " + ", b.ENGINE_MTO as engine_mto "
		+ ", a.ENGINE_SERIAL_NO as engine_serial_no "
		+ ", (b.EXT_COLOR_CODE ||' - '|| b.EXT_COLOR_DESCRIPTION) as exterior_color "
		+ ", histLine.LINE_NAME as tracking_status "
		+ ", (b.INT_COLOR_CODE ||' - '|| b.INT_COLOR_DESCRIPTION) as interior_color "
		+ ", a.KD_LOT_NUMBER as kd_lot_number " + ", pp.PROCESS_POINT_NAME as last_process_point "
		+ ", histLine.LINE_ID as line_id " + ", histLine.LINE_NAME as line_name "
		+ ", histLine.LINE_SEQUENCE_NUMBER as line_sequence_number " + ", b.MODEL_CODE as model_code "
		+ ", b.MODEL_YEAR_CODE ||''|| b.MODEL_CODE as model_description "
		+ ", b.MODEL_OPTION_CODE as model_option_code " + ", b.MODEL_TYPE_CODE as model_type_code "
		+ ", (b.MODEL_YEAR_CODE ||' - '|| b.MODEL_YEAR_DESCRIPTION) as model_year "
		+ ", pr.PARKING_LOCATION as parking_location " + ", p.PLANT_NAME as plant_name "
		+ ", b.PRODUCT_SPEC_CODE as product_spec_code " + ", a.PRODUCTION_LOT as production_lot " + ", case "
		+ "  when d.SEQUENCE_NUMBER IS NULL then 0 " + "  else d.SEQUENCE_NUMBER " + "  end as sequence_number "
		+ ", a.LAST_PASSING_PROCESS_POINT_ID as last_process_point_id "
		+ ", histpp.TRACKING_POINT_FLAG as tracking_point_flag" + " from " + "GAL215TBX hist "
		+ "join GAL143TBX a on a.PRODUCT_ID = hist.PRODUCT_ID "
		+ "join GAL214TBX pp on a.LAST_PASSING_PROCESS_POINT_ID = pp.PROCESS_POINT_ID "
		+ "join GAL214TBX histpp on hist.PROCESS_POINT_ID = histpp.PROCESS_POINT_ID "
		+ "join GAL144TBX b on a.PRODUCT_SPEC_CODE = b.PRODUCT_SPEC_CODE "
		+ "join GAL195TBX c on a.TRACKING_STATUS = c.LINE_ID "
		+ "join GAL195TBX histLine on histpp.LINE_ID = histLine.LINE_ID "
		+ "join GAL128TBX d on histLine.DIVISION_ID = d.DIVISION_ID "
		+ "join GAL211TBX p on d.PLANT_NAME = p.PLANT_NAME " + "left join GAL163TBX pr on a.PRODUCT_ID = pr.VIN "
		+ "order by id")
public class HistoryDetails extends VdbEntity<String> implements IProductDetails {



	@Column(name = "kd_lot_number")
	private String kdLotNumber;

	@Column(name = "production_lot")
	private String productionLot;

	@Column(name = "engine_serial_no")
	private String engineSerialNo;

	@Column(name = "engine_mto")
	private String engineMto;

	@Column(name = "af_on_sequence_number")
	private Integer afOnSequenceNumber;

	@Column(name = "product_spec_code")
	private String productSpecCode;

	@Column(name = "destination")
	private String destination;

	@Column(name = "last_process_point")
	private String lastProcessPoint;

	@Column(name = "actual_timestamp")
	private Timestamp actualTimestamp;

	@Column(name = "tracking_status")
	private String trackingStatus;

	@Transient
	private Timestamp trackingStatusDate;

	@Column(name = "parking_location")
	private String parkingLocation;

	@Column(name = "division_id")
	private String divisionId;

	@Column(name = "last_process_point_id")
	private String lastProcessPointId;

	@Column(name = "tracking_point_flag")
	private Integer trackingPointFlag;

	@Column(name = "division_name")
	private String divisionName;

	@Column(name = "exterior_color")
	private String exteriorColor;

	@Column(name = "interior_color")
	private String interiorColor;

	@Column(name = "line_id")
	private String lineId;

	@Column(name = "line_name")
	private String lineName;

	@Column(name = "line_sequence_number")
	private Integer lineSequenceNumber;

	@Column(name = "model_code")
	private String modelCode;

	@Column(name = "model_description")
	private String modelDescription;

	@Column(name = "model_option_code")
	private String modelOptionCode;

	@Column(name = "model_type_code")
	private String modelTypeCode;

	@Column(name = "model_year")
	private String modelYear;

	@Column(name = "plant_name")
	private String plantName;

	@Column(name = "sequence_number")
	private Integer sequenceNumber;

	public HistoryDetails() {
		super();
	}

	@Override
	public String toString() {

		String str = getClass().getSimpleName() + "{";
		str = str + "id: " + getId();
		str = str + ", actualTimestamp: " + getActualTimestamp();
		str = str + ", afOnSequenceNumber: " + getAfOnSequenceNumber();
		str = str + ", productionLot: " + getProductionLot();
		str = str + ", kdLotNumber: " + getKdLotNumber();
		str = str + ", trackingStatus: " + getTrackingStatus();
		str = str + ", divisionId: " + getDivisionId();
		str = str + ", divisionName: " + getDivisionName();
		str = str + ", sequenceNumber: " + getSequenceNumber();
		str = str + ", lineId: " + getLineId();
		str = str + ", lineName: " + getLineName();
		str = str + ", lineSequenceNumber: " + getLineSequenceNumber();
		str = str + ", productSpecCode: " + getProductSpecCode();
		str = str + ", modelCode: " + getModelCode();
		str = str + ", modelTypeCode: " + getModelTypeCode();
		str = str + ", modelYear: " + getModelYear();
		str = str + ", modelOptionCode: " + getModelOptionCode();
		str = str + ", modelDescription: " + getModelDescription();
		str = str + ", exteriorColor: " + getExteriorColor();
		str = str + ", interiorColor: " + getInteriorColor();
		str = str + ", lastProcessPoint: " + getLastProcessPoint();
		str = str + ", engineSerialNo: " + getEngineSerialNo();
		str = str + ", destination: " + getDestination();
		str = str + ", engineMto: " + getEngineMto();
		str = str + ", parkingLocation: " + getParkingLocation();
		str = str + ", plantName: " + getPlantName();
		str = str + "}";
		return str;

	}

	public Timestamp getActualTimestamp() {
		return actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	public Integer getAfOnSequenceNumber() {
		return afOnSequenceNumber;
	}

	public Timestamp getTrackingStatusDate() {
		return trackingStatusDate;
	}

	public void setTrackingStatusDate(Timestamp trackingStatusDate) {
		this.trackingStatusDate = trackingStatusDate;
	}

	public Integer getTrackingPointFlag() {
		return trackingPointFlag;
	}

	public void setTrackingPointFlag(Integer trackingPointFlag) {
		this.trackingPointFlag = trackingPointFlag;
	}

	public String getLastProcessPointId() {
		return lastProcessPointId;
	}

	public void setLastProcessPointId(String lastProcessPointId) {
		this.lastProcessPointId = lastProcessPointId;
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

	public String getDivisionId() {
		return StringUtils.trimToEmpty(divisionId);
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public String getDivisionName() {
		return StringUtils.trimToEmpty(divisionName);
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getLineId() {
		return StringUtils.trimToEmpty(lineId);
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getLineName() {
		return StringUtils.trimToEmpty(lineName);
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public Integer getLineSequenceNumber() {
		return lineSequenceNumber;
	}

	public void setLineSequenceNumber(Integer lineSequenceNumber) {
		this.lineSequenceNumber = lineSequenceNumber;
	}

	public String getModelCode() {
		return StringUtils.trimToEmpty(modelCode);
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelTypeCode() {
		return StringUtils.trimToEmpty(modelTypeCode);
	}

	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}

	public String getModelYear() {
		return StringUtils.trimToEmpty(modelYear).replaceAll(" +", " ");
	}

	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}

	public String getModelOptionCode() {
		return StringUtils.trimToEmpty(modelOptionCode);
	}

	public void setModelOptionCode(String modelOptionCode) {
		this.modelOptionCode = modelOptionCode;
	}

	public String getModelDescription() {
		return StringUtils.trimToEmpty(modelDescription).replaceAll(" +", " ");
	}

	public void setModelDescription(String modelDescription) {
		this.modelDescription = modelDescription;
	}

	public String getExteriorColor() {
		return StringUtils.trimToEmpty(exteriorColor).replaceAll(" +", " ");
	}

	public void setExteriorColor(String exteriorColor) {
		this.exteriorColor = exteriorColor;
	}

	public String getInteriorColor() {
		return StringUtils.trimToEmpty(interiorColor).replaceAll(" +", " ");
	}

	public void setInteriorColor(String interiorColor) {
		this.interiorColor = interiorColor;
	}

	public String getLastProcessPoint() {
		return StringUtils.trimToEmpty(lastProcessPoint);
	}

	public void setLastProcessPoint(String lastProcessPoint) {
		this.lastProcessPoint = lastProcessPoint;
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

	public String getParkingLocation() {
		return parkingLocation;
	}

	public void setParkingLocation(String parkingLocation) {
		this.parkingLocation = parkingLocation;
	}

	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result + ((afOnSequenceNumber == null) ? 0 : afOnSequenceNumber.hashCode());
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((divisionId == null) ? 0 : divisionId.hashCode());
		result = prime * result + ((divisionName == null) ? 0 : divisionName.hashCode());
		result = prime * result + ((engineMto == null) ? 0 : engineMto.hashCode());
		result = prime * result + ((engineSerialNo == null) ? 0 : engineSerialNo.hashCode());
		result = prime * result + ((exteriorColor == null) ? 0 : exteriorColor.hashCode());
		result = prime * result + ((interiorColor == null) ? 0 : interiorColor.hashCode());
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((lastProcessPoint == null) ? 0 : lastProcessPoint.hashCode());
		result = prime * result + ((lineId == null) ? 0 : lineId.hashCode());
		result = prime * result + ((lineName == null) ? 0 : lineName.hashCode());
		result = prime * result + ((lineSequenceNumber == null) ? 0 : lineSequenceNumber.hashCode());
		result = prime * result + ((modelCode == null) ? 0 : modelCode.hashCode());
		result = prime * result + ((modelDescription == null) ? 0 : modelDescription.hashCode());
		result = prime * result + ((modelOptionCode == null) ? 0 : modelOptionCode.hashCode());
		result = prime * result + ((modelTypeCode == null) ? 0 : modelTypeCode.hashCode());
		result = prime * result + ((modelYear == null) ? 0 : modelYear.hashCode());
		result = prime * result + ((parkingLocation == null) ? 0 : parkingLocation.hashCode());
		result = prime * result + ((plantName == null) ? 0 : plantName.hashCode());
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((productionLot == null) ? 0 : productionLot.hashCode());
		result = prime * result + ((sequenceNumber == null) ? 0 : sequenceNumber.hashCode());
		result = prime * result + ((trackingStatus == null) ? 0 : trackingStatus.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoryDetails other = (HistoryDetails) obj;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (afOnSequenceNumber == null) {
			if (other.afOnSequenceNumber != null)
				return false;
		} else if (!afOnSequenceNumber.equals(other.afOnSequenceNumber))
			return false;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
			return false;
		if (divisionName == null) {
			if (other.divisionName != null)
				return false;
		} else if (!divisionName.equals(other.divisionName))
			return false;
		if (engineMto == null) {
			if (other.engineMto != null)
				return false;
		} else if (!engineMto.equals(other.engineMto))
			return false;
		if (engineSerialNo == null) {
			if (other.engineSerialNo != null)
				return false;
		} else if (!engineSerialNo.equals(other.engineSerialNo))
			return false;
		if (exteriorColor == null) {
			if (other.exteriorColor != null)
				return false;
		} else if (!exteriorColor.equals(other.exteriorColor))
			return false;
		if (interiorColor == null) {
			if (other.interiorColor != null)
				return false;
		} else if (!interiorColor.equals(other.interiorColor))
			return false;
		if (kdLotNumber == null) {
			if (other.kdLotNumber != null)
				return false;
		} else if (!kdLotNumber.equals(other.kdLotNumber))
			return false;
		if (lastProcessPoint == null) {
			if (other.lastProcessPoint != null)
				return false;
		} else if (!lastProcessPoint.equals(other.lastProcessPoint))
			return false;
		if (lineId == null) {
			if (other.lineId != null)
				return false;
		} else if (!lineId.equals(other.lineId))
			return false;
		if (lineName == null) {
			if (other.lineName != null)
				return false;
		} else if (!lineName.equals(other.lineName))
			return false;
		if (lineSequenceNumber == null) {
			if (other.lineSequenceNumber != null)
				return false;
		} else if (!lineSequenceNumber.equals(other.lineSequenceNumber))
			return false;
		if (modelCode == null) {
			if (other.modelCode != null)
				return false;
		} else if (!modelCode.equals(other.modelCode))
			return false;
		if (modelDescription == null) {
			if (other.modelDescription != null)
				return false;
		} else if (!modelDescription.equals(other.modelDescription))
			return false;
		if (modelOptionCode == null) {
			if (other.modelOptionCode != null)
				return false;
		} else if (!modelOptionCode.equals(other.modelOptionCode))
			return false;
		if (modelTypeCode == null) {
			if (other.modelTypeCode != null)
				return false;
		} else if (!modelTypeCode.equals(other.modelTypeCode))
			return false;
		if (modelYear == null) {
			if (other.modelYear != null)
				return false;
		} else if (!modelYear.equals(other.modelYear))
			return false;
		if (parkingLocation == null) {
			if (other.parkingLocation != null)
				return false;
		} else if (!parkingLocation.equals(other.parkingLocation))
			return false;
		if (plantName == null) {
			if (other.plantName != null)
				return false;
		} else if (!plantName.equals(other.plantName))
			return false;
		if (productSpecCode == null) {
			if (other.productSpecCode != null)
				return false;
		} else if (!productSpecCode.equals(other.productSpecCode))
			return false;
		if (productionLot == null) {
			if (other.productionLot != null)
				return false;
		} else if (!productionLot.equals(other.productionLot))
			return false;
		if (sequenceNumber == null) {
			if (other.sequenceNumber != null)
				return false;
		} else if (!sequenceNumber.equals(other.sequenceNumber))
			return false;
		if (trackingStatus == null) {
			if (other.trackingStatus != null)
				return false;
		} else if (!trackingStatus.equals(other.trackingStatus))
			return false;
		return true;
	}

}