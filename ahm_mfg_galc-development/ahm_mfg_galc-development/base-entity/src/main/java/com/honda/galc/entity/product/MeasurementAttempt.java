package com.honda.galc.entity.product;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.util.CommonUtil;

/** * * 
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012
 */
@Entity
@Table(name = "GAL198_HIST_TBX")
public class MeasurementAttempt extends AuditEntry {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MeasurementAttemptId id;

	@Column(name = "MEASUREMENT_VALUE")
	private double measurementValue;

	@Column(name = "MEASUREMENT_STATUS")
	private int measurementStatusId;

	@Column(name = "ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;

	@Column(name = "MEASUREMENT_ANGLE")
	private double measurementAngle;

	@Column(name = "PART_SERIAL_NUMBER")
	private String partSerialNumber;

	@Column(name = "PART_ID", length=5)
	private String partId;

	@Column(name = "PART_REV")
	private int partRevision;
	
	@Column(name = "FEATURE_TYPE")
    private String featureType;
    
    @Column(name = "FEATURE_ID")
    private String featureId;
    
	@Column(name="MEASUREMENT_NAME")
	private String measurementName;

	@Column(name="MEASUREMENT_STRING_VALUE")
	private String measurementStringValue;
    
	@Column(name="METHOD_ID")
	private String methodId;
	
	@Column(name="METHOD_DESCRIPTION")
	private String methodDescription;
    
	public MeasurementAttempt() {
		super();
	}
	
	public MeasurementAttempt(String productId, String partName,int measurementSequenceNumber, int measurementAttempt){
		id = new MeasurementAttemptId(productId,partName,measurementSequenceNumber,measurementAttempt);
	}

	public MeasurementAttempt(MeasurementAttemptId measurementId) {
		this.id = measurementId;
	}
	
	public MeasurementAttemptId getId() {
		return this.id;
	}

	public void setId(MeasurementAttemptId id) {
		this.id = id;
	}

	public double getMeasurementValue() {
		return this.measurementValue;
	}

	public void setMeasurementValue(double measurementValue) {
		this.measurementValue = measurementValue;
	}

	public int getMeasurementStatusId() {
		return measurementStatusId;
	}

	public void setMeasurementStatusId(int measurementStatusId) {
		this.measurementStatusId = measurementStatusId;
	}

	public MeasurementStatus getMeasurementStatus() {
		return MeasurementStatus.getType(measurementStatusId);
	}

	public void setMeasurementStatus(MeasurementStatus status) {
		this.measurementStatusId = status.getId();
	}

	public Timestamp getActualTimestamp() {
		return this.actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	public double getMeasurementAngle() {
		return this.measurementAngle;
	}

	public void setMeasurementAngle(double measurementAngle) {
		this.measurementAngle = measurementAngle;
	}

	public String getPartSerialNumber() {
		return StringUtils.trim(this.partSerialNumber);
	}

	public void setPartSerialNumber(String partSerialNumber) {
		this.partSerialNumber = partSerialNumber;
	}

	public boolean isStatus() {
		return getMeasurementStatus() == MeasurementStatus.OK;
	}

	public String getPartId() {
		return StringUtils.trim(partId);
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}
	
	public int getPartRevision() {
		return partRevision;
	}

	public void setPartRevision(int partRevision) {
		this.partRevision = partRevision;
	}

	public String getFeatureType() {
		return featureType;
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	
	public String getMeasurementName() {
		return StringUtils.trim(measurementName);
	}

	public void setMeasurementName(String measurementName) {
		this.measurementName = measurementName;
	}

	public String getMeasurementStringValue() {
		return StringUtils.trim(measurementStringValue);
	}

	public void setMeasurementStringValue(String measurementStringValue) {
		this.measurementStringValue = measurementStringValue;
	}

	public String getMethodId() {
		return StringUtils.trim(this.methodId);
	}

	public void setMethodId(String methodId) {
		this.methodId = methodId;
	}
	
	public String getMethodDescription() {
		return StringUtils.trim(this.methodDescription);
	}

	public void setMethodDescription(String methodDescription) {
		this.methodDescription = methodDescription;
	}

	public String getDefectLocation() {
		return String.valueOf(getId().getMeasurementSequenceNumber());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result
				+ ((featureId == null) ? 0 : featureId.hashCode());
		result = prime * result
				+ ((featureType == null) ? 0 : featureType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		long temp;
		temp = Double.doubleToLongBits(measurementAngle);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + measurementStatusId;
		temp = Double.doubleToLongBits(measurementValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((partId == null) ? 0 : partId.hashCode());
		result = prime * result + partRevision;
		result = prime
				* result
				+ ((partSerialNumber == null) ? 0 : partSerialNumber.hashCode());
		result = prime * result + ((measurementName == null) ? 0 : measurementName.hashCode());
		result = prime * result + ((measurementStringValue == null) ? 0 : measurementStringValue.hashCode());
		result = prime * result + ((methodId == null) ? 0 : methodId.hashCode());
		result = prime * result + ((methodDescription == null) ? 0 : methodDescription.hashCode());
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
		MeasurementAttempt other = (MeasurementAttempt) obj;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (featureId == null) {
			if (other.featureId != null)
				return false;
		} else if (!featureId.equals(other.featureId))
			return false;
		if (featureType == null) {
			if (other.featureType != null)
				return false;
		} else if (!featureType.equals(other.featureType))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (Double.doubleToLongBits(measurementAngle) != Double
				.doubleToLongBits(other.measurementAngle))
			return false;
		if (measurementStatusId != other.measurementStatusId)
			return false;
		if (Double.doubleToLongBits(measurementValue) != Double
				.doubleToLongBits(other.measurementValue))
			return false;
		if (partId == null) {
			if (other.partId != null)
				return false;
		} else if (!partId.equals(other.partId))
			return false;
		if (partRevision != other.partRevision)
			return false;
		if (partSerialNumber == null) {
			if (other.partSerialNumber != null)
				return false;
		} else if (!partSerialNumber.equals(other.partSerialNumber))
			return false;
		if (measurementName == null) {
			if (other.measurementName != null)
				return false;
		} else if (!measurementName.equals(other.measurementName))
			return false;
		if (measurementStringValue == null) {
			if (other.measurementStringValue != null)
				return false;
		} else if (!measurementStringValue.equals(other.measurementStringValue))
			return false;
		if (methodId == null) {
			if (other.methodId != null)
				return false;
		} else if (!methodId.equals(other.methodId))
			return false;
		if (methodDescription == null) {
			if (other.methodDescription != null)
				return false;
		} else if (!methodDescription.equals(other.methodDescription))
			return false;
		return true;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\"").append(getId().getProductId()).append("\"");
		sb.append(",\"").append(getId().getPartName()).append("\"");
		sb.append(",").append(getId().getMeasurementSequenceNumber());
		sb.append(",").append(getId().getMeasurementAttempt());
		sb.append(",").append(measurementValue);
		sb.append(",").append(measurementStatusId);
		sb.append(",\"").append(CommonUtil.convertNull(featureId)).append("\"");
        sb.append(",\"").append(CommonUtil.convertNull(featureType)).append("\"");
		sb.append(",\"").append(CommonUtil.format(actualTimestamp)).append("\"");
		sb.append(",").append(measurementAngle);
		sb.append(",\"").append(partSerialNumber).append("\"");
		sb.append(",\"").append(CommonUtil.format(getCreateTimestamp())).append("\"");
		sb.append(",\"").append(CommonUtil.format(getUpdateTimestamp())).append("\"");
        sb.append(",\"").append(CommonUtil.convertNull(partId)).append("\"");
        sb.append(",\"").append(partRevision).append("\"");
        sb.append(",\"").append(CommonUtil.convertNull(getMeasurementName())).append("\"");
        sb.append(",\"").append(CommonUtil.convertNull(getMeasurementStringValue())).append("\"");
        sb.append(",\"").append(CommonUtil.convertNull(getMethodId())).append("\"");
        sb.append(",\"").append(CommonUtil.convertNull(getMethodDescription())).append("\"");
		return sb.toString();
	}
}
