package com.honda.galc.entity.product;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.RepairMethod;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.Tag;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.MeasurementAngleStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.dto.RestData;
import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

import java.sql.Timestamp;

@Entity
@Table(name = "GAL198TBX")
public class Measurement extends AuditEntry implements QicsResult, IDeviceData{
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MeasurementId id;

    @RestData(key="measurementValue")
    @Column(name = "MEASUREMENT_VALUE")
    @Tag(name="MEASUREMENT_VALUE", alt="VALUE", optional=true)
    private double measurementValue;

    @Column(name = "MEASUREMENT_STATUS")
    @Tag(name="MEASUREMENT_STATUS", alt="RESULT", optional=true)
    private int measurementStatusId;

    @Column(name = "ACTUAL_TIMESTAMP")
    @Tag(name="MEASUREMENT_ACTUAL_TIMESTAMP", alt="ACTUAL_TIMESTAMP", optional = true)
    private Timestamp actualTimestamp;

    @Column(name = "MEASUREMENT_ANGLE")
    @Tag(name="MEASUREMENT_ANGLE", alt="ANGLE", optional=true)
    private double measurementAngle;

    @Column(name = "PART_SERIAL_NUMBER")
    @Tag(name="MEASUREMENT_PART_SERIAL_NUMBER", alt="MEASUREMENT_SN", optional = true)
    private String partSerialNumber;
    
	@Column(name="MEASUREMENT_NAME")
	@Tag(name="MEASUREMENT_NAME", alt="MNAME", optional=true)
	private String measurementName;

	@Column(name="MEASUREMENT_STRING_VALUE")
	@Tag(name="MEASUREMENT_STRING_VALUE", alt="STRING_VALUE", optional=true)
	private String measurementStringValue;
	
	@Column(name="METHOD_ID")
	@Tag(name="METHOD_ID", alt="METHOD", optional=true)
	private String methodId;
	
	@Column(name="METHOD_DESCRIPTION")
	@Tag(name="METHOD_DESCRIPTION", alt="METHOD_DESC", optional=true)
	private String methodDescription;

	@Column(name = "PART_ID", length=5)
    private String partId;
    
    @Column(name = "PART_REV")
    private int partRevision;
    
    @Column(name = "FEATURE_TYPE")
    private String featureType;
    
    @Column(name = "FEATURE_ID")
    private String featureId;
    
    /**
     * this is a unique build result identifier if a QICS defect was created
     * used for all QICS messages
     */
    @Column(name = "DEFECT_REF_ID")
    private long defectRefId;
    
	@Transient
    private long lastTighteningId;

    @Transient
    private int badTorqueCount;

    @Transient
    private int measurementAngleStatusId;

    @Transient
    private int measurementValueStatusId;

    @Transient
    private int lastTighteningStatusId;
    
    @Transient
	private String errorCode;

    public Measurement() {
        super();
        initialize();
    }

    public Measurement(String productId, String partName,int measurementSequenceNumber){
        super();
        id = new MeasurementId(productId,partName,measurementSequenceNumber);
	}
    
    public Measurement(MeasurementId measurementId) {
		this();
		this.id = measurementId;
	}

	private void initialize() {
    	measurementStatusId = -1;
    	partSerialNumber = "";
    	defectRefId=0;
	}

	public MeasurementId getId() {
        return this.id;
    }

    public void setId(MeasurementId id) {
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

    public long getLastTighteningId() {
        return lastTighteningId;
    }

    public void setLastTighteningId(long lastTighteningId) {
        this.lastTighteningId = lastTighteningId;
    }

    public int getBadTorqueCount() {
        return badTorqueCount;
    }

    public void setBadTorqueCount(int badTorqueCount) {
        this.badTorqueCount = badTorqueCount;
    }

    public MeasurementAngleStatus getMeasurementAngleStatus() {
        return MeasurementAngleStatus.getType(measurementAngleStatusId);
    }

    public void setMeasurementAngleStatus(MeasurementAngleStatus status) {
        this.measurementAngleStatusId = status.getId();
    }

    public MeasurementStatus getMeasurementValueStatus() {
        return MeasurementStatus.getType(measurementValueStatusId);
    }

    public void setMeasurementValueStatus(MeasurementStatus status) {
        this.measurementValueStatusId = status.getId();
    }

    public int getMeasurementAngleStatusId() {
        return measurementAngleStatusId;
    }

    public void setMeasurementAngleStatusId(int measurementAngleStatusId) {
        this.measurementAngleStatusId = measurementAngleStatusId;
    }

    public int getMeasurementValueStatusId() {
        return measurementValueStatusId;
    }

    public void setMeasurementValueStatusId(int measurementValueStatusId) {
        this.measurementValueStatusId = measurementValueStatusId;
    }

    public MeasurementStatus getLastTighteningStatus() {
        return MeasurementStatus.getType(lastTighteningStatusId);
    }

    public void setLastTighteningStatus(MeasurementStatus status) {
        this.lastTighteningStatusId = status.getId();
    }

    public int getLastTighteningStatusId() {
        return lastTighteningStatusId;
    }

    public void setLastTighteningStatusId(int lastTighteningStatusId) {
        this.lastTighteningStatusId = lastTighteningStatusId;
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
	
    public boolean isStatus() {
    	return getMeasurementStatus() == MeasurementStatus.OK;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getDefectLocation() {
		return String.valueOf(getId().getMeasurementSequenceNumber());
	}
	
	public String getMeasurementName() {
		return StringUtils.trim(this.measurementName);
	}

	public void setMeasurementName(String measurementName) {
		this.measurementName = measurementName;
	}
	
	public String getMeasurementStringValue() {
		return StringUtils.trim(this.measurementStringValue);
	}

	public void setMeasurementStringValue(String measurementStringValue) {
		this.measurementStringValue = measurementStringValue;
	}
	
	public String getMethodId() {
		return StringUtils.trim(this.methodId);
	}

	public void setMethodId(String methodId) {
		this.methodId = methodId;
		try {
			setMethodDescription(RepairMethod.fromId(methodId).getDescription());
		} catch (Exception e) {
			//ok to fail
			Logger.getLogger().warn("Failed to set measurement method description:", e.getMessage());
		}
	}

	public String getMethodDescription() {
		return StringUtils.trim(this.methodDescription);
	}

	public void setMethodDescription(String methodDescription) {
		this.methodDescription = methodDescription;
	}

	public long getDefectRefId() {
		return defectRefId;
	}

	public void setDefectRefId(long defectRefId) {
		this.defectRefId = defectRefId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result + badTorqueCount;
		result = prime * result
				+ ((errorCode == null) ? 0 : errorCode.hashCode());
		result = prime * result
				+ ((featureId == null) ? 0 : featureId.hashCode());
		result = prime * result
				+ ((featureType == null) ? 0 : featureType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ (int) (lastTighteningId ^ (lastTighteningId >>> 32));
		result = prime * result + lastTighteningStatusId;
		long temp;
		temp = Double.doubleToLongBits(measurementAngle);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + measurementAngleStatusId;
		result = prime * result
				+ ((measurementName == null) ? 0 : measurementName.hashCode());
		result = prime * result + measurementStatusId;
		result = prime
				* result
				+ ((measurementStringValue == null) ? 0
						: measurementStringValue.hashCode());
		temp = Double.doubleToLongBits(measurementValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + measurementValueStatusId;
		result = prime * result + ((partId == null) ? 0 : partId.hashCode());
		result = prime * result + partRevision;
		result = prime
				* result
				+ ((partSerialNumber == null) ? 0 : partSerialNumber.hashCode());
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
		Measurement other = (Measurement) obj;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (badTorqueCount != other.badTorqueCount)
			return false;
		if (errorCode == null) {
			if (other.errorCode != null)
				return false;
		} else if (!errorCode.equals(other.errorCode))
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
		if (lastTighteningId != other.lastTighteningId)
			return false;
		if (lastTighteningStatusId != other.lastTighteningStatusId)
			return false;
		if (Double.doubleToLongBits(measurementAngle) != Double
				.doubleToLongBits(other.measurementAngle))
			return false;
		if (measurementAngleStatusId != other.measurementAngleStatusId)
			return false;
		if (measurementName == null) {
			if (other.measurementName != null)
				return false;
		} else if (!measurementName.equals(other.measurementName))
			return false;
		if (measurementStatusId != other.measurementStatusId)
			return false;
		if (measurementStringValue == null) {
			if (other.measurementStringValue != null)
				return false;
		} else if (!measurementStringValue.equals(other.measurementStringValue))
			return false;
		if (Double.doubleToLongBits(measurementValue) != Double
				.doubleToLongBits(other.measurementValue))
			return false;
		if (measurementValueStatusId != other.measurementValueStatusId)
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
        sb.append(",").append(measurementValue);
        sb.append(",").append(measurementStatusId);
        sb.append(",\"").append(CommonUtil.convertNull(featureId)).append("\"");
        sb.append(",\"").append(CommonUtil.convertNull(featureType)).append("\"");
        sb.append(",\"").append(CommonUtil.format(actualTimestamp)).append("\"");
        sb.append(",").append(measurementAngle);
        sb.append(",\"").append(partSerialNumber).append("\"");
        sb.append(",\"").append(CommonUtil.convertNull(partId)).append("\"");
        sb.append(",\"").append(partRevision).append("\"");
        sb.append(",\"").append(methodId).append("\"");
        sb.append(",\"").append(methodDescription).append("\"");
        sb.append(",\"").append(CommonUtil.format(getCreateTimestamp())).append("\"");
        sb.append(",\"").append(CommonUtil.format(getUpdateTimestamp())).append("\"");
                
        sb.append(", tighteningId:" + lastTighteningId);
        sb.append(", badTorqueCount:" + badTorqueCount);
        return sb.toString();
    }
}
