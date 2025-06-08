package com.honda.galc.entity.product;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.Tag;
import com.honda.galc.dto.RestData;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.util.CommonUtil;

/**
 * 
 * <h3>InstalledPart Class description</h3>
 * <p> InstalledPart description </p>
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
 * Mar 31, 2011
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL185TBX")
public class InstalledPart extends ProductBuildResult {
    @EmbeddedId
    private InstalledPartId id;

    @RestData(key="partSn")
    @Tag(name="SN",alt="PART_SERIAL_NUMBER", optional = true)
    @Column(name = "PART_SERIAL_NUMBER")
    private String partSerialNumber;

    @Column(name = "PASS_TIME")
    private int passTime;

    @Column(name = "INSTALLED_PART_REASON")
    @Tag(name="INSTALLED_PART_REASON", alt="REASON", optional = true)
    private String installedPartReason;

    @Column(name = "FIRST_ALARM")
    private int firstAlarm;

    @Column(name = "SECOND_ALARM")
    private int secondAlarm;
    
    @Column(name = "OP_REV")
    private int operationRevision;

    @RestData()
    @Column(name = "PART_ID", length=5)
    private String partId;
    
    @Column(name = "PART_REV")
    private int partRevision;
  
	@Column(name = "FEATURE_TYPE")
    private String featureType;
    
    @Column(name = "FEATURE_ID")
    private String featureId;
    
    @Column(name = "PRODUCT_TYPE")
    private String productType;
    
    @Column(name = "OVERRIDE_ASSOCIATE_NO")
	private String overrideAssociateNo;
    
    /**
     * this is a unique build result identifier if a QICS defect was created
     * used for all QICS messages
     */
    @Column(name = "DEFECT_REF_ID")
    private long defectRefId;
    
	@Transient
    private boolean validPartSerialNumber = false;
	
    @Transient
    private Timestamp startTimestamp = null;
    
    @Transient
    private int partIndex;
    
    @Transient
    private boolean skipped;
    
    @Transient
    List<Measurement> measurements = new ArrayList<Measurement>();

    private static final long serialVersionUID = 1L;

    public InstalledPart() {
        super();
        initialize();
    }
    
    public InstalledPart(String productId,String partName) {
		id = new InstalledPartId(productId,partName);
		setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		initialize();
		setQicsDefect(false);
	}

    public InstalledPart(InstalledPartId id) {
		this();
		this.id = id;
	}

    private void initialize() {
    	partSerialNumber = "";
    	setAssociateNo("");
    	installedPartReason = "";
    	partId = "";
    	setInstalledPartStatusId(-1);
    	startTimestamp = new Timestamp(System.currentTimeMillis());
    	defectRefId=0;
	}

	public InstalledPartId getId() {
        return this.id;
    }

    public void setId(InstalledPartId id) {
        this.id = id;
    }

    public String getPartSerialNumber() {
        return StringUtils.trim(this.partSerialNumber);
    }

    public void setPartSerialNumber(String partSerialNumber) {
        this.partSerialNumber = StringUtils.trim(partSerialNumber);
    }

    public int getPassTime() {
        return this.passTime;
    }

    public void setPassTime(int passTime) {
        this.passTime = passTime;
    }

    public String getInstalledPartReason() {
        return StringUtils.trim(this.installedPartReason);
    }

    public void setInstalledPartReason(String installedPartReason) {
        this.installedPartReason = installedPartReason;
    }

    public int getFirstAlarm() {
        return this.firstAlarm;
    }

    public void setFirstAlarm(int firstAlarm) {
        this.firstAlarm = firstAlarm;
    }

    public int getSecondAlarm() {
        return this.secondAlarm;
    }

    public void setSecondAlarm(int secondAlarm) {
        this.secondAlarm = secondAlarm;
    }

    public int getOperationRevision() {
        return this.operationRevision;
    }

    public void setOperationRevision(int operationRevision) {
        this.operationRevision = operationRevision;
    }
    
    public String getPartId() {
        return StringUtils.trim(this.partId);
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
	
    public boolean isValidPartSerialNumber() {
        return validPartSerialNumber;
    }

    public void setValidPartSerialNumber(boolean validPartSn) {
        this.validPartSerialNumber = validPartSn;
    }

    public Timestamp getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Timestamp startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public void addMeasurement( String measurementVal,String partName) {
    	boolean measurementFound = false;
    	Measurement measurement = new Measurement(getProductId(), partName, 1);
    	for (Measurement mnt: getMeasurements()) {
    		if (mnt.getId().equals(measurement.getId())) {
    			measurementFound = true;
    			mnt.setMeasurementValue(Double.parseDouble(measurementVal));
    		}
    	}
    	if (!measurementFound) {
    		measurement.setMeasurementValue(Double.parseDouble(measurementVal));
    		getMeasurements().add(measurement);
    	}
    }
    
    public int getPartIndex() {
        return partIndex;
    }

    public void setPartIndex(int partIndex) {
        this.partIndex = partIndex;
    }
    
    public boolean isSkipped() {
		return skipped;
	}

	public void setSkipped(boolean skip) {
		this.skipped = skip;
	}
	
	public boolean isQicsDefect() {
		return isQicsDefect;
	}

	public void setQicsDefect(boolean isQicsDefect) {
		this.isQicsDefect = isQicsDefect;
	}

	public String getFeatureType() {
		return StringUtils.trim(this.featureType);
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	public String getFeatureId() {
		return StringUtils.trim(this.featureId);
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	
	public String getProductType() {
		return StringUtils.trim(productType);
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	

	public String getOverrideAssociateNo() {
		return StringUtils.trim(overrideAssociateNo);
	}

	public void setOverrideAssociateNo(String overrideAssociateNo) {
		this.overrideAssociateNo = overrideAssociateNo;
	}

	public boolean hasData(){
		return !StringUtils.isEmpty(getPartSerialNumber()) || 
		(getMeasurements() != null && getMeasurements().size() > 0) ||
		getInstalledPartStatus() != InstalledPartStatus.BLANK;
	}
	
	public String getResultValue() {
		return getPartSerialNumber();
	}

	public void setResultValue(String resultValue) {
	}

	@Override
	public String getProductId() {
		return id.getProductId();
	}

	@Override
	public void setProductId(String productId) {
		if(id == null) id = new InstalledPartId();
		id.setProductId(productId);
	}

	@Override
	public String getPartName() {
		return id.getPartName();
	}

	@Override
	public void setPartName(String partName) {
		if(id == null) id = new InstalledPartId();
		id.setPartName(partName);
	}
	public boolean isBlank() {
		return getInstalledPartStatus() == InstalledPartStatus.BLANK &&
		StringUtils.isEmpty(getPartSerialNumber()) &&
		(getMeasurements() == null || getMeasurements().size() == 0);
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
		result = prime * result + ((featureId == null) ? 0 : featureId.hashCode());
		result = prime * result + ((featureType == null) ? 0 : featureType.hashCode());
		result = prime * result + firstAlarm;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((installedPartReason == null) ? 0 : installedPartReason.hashCode());
		result = prime * result + ((measurements == null) ? 0 : measurements.hashCode());
		result = prime * result + operationRevision;
		result = prime * result + ((overrideAssociateNo == null) ? 0 : overrideAssociateNo.hashCode());
		result = prime * result + ((partId == null) ? 0 : partId.hashCode());
		result = prime * result + partIndex;
		result = prime * result + partRevision;
		result = prime * result + ((partSerialNumber == null) ? 0 : partSerialNumber.hashCode());
		result = prime * result + passTime;
		result = prime * result + ((productType == null) ? 0 : productType.hashCode());
		result = prime * result + secondAlarm;
		result = prime * result + (skipped ? 1231 : 1237);
		result = prime * result + ((startTimestamp == null) ? 0 : startTimestamp.hashCode());
		result = prime * result + (validPartSerialNumber ? 1231 : 1237);
		result = prime * result + (int)defectRefId;
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
		InstalledPart other = (InstalledPart) obj;
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
		if (firstAlarm != other.firstAlarm)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (installedPartReason == null) {
			if (other.installedPartReason != null)
				return false;
		} else if (!installedPartReason.equals(other.installedPartReason))
			return false;
		if (measurements == null) {
			if (other.measurements != null)
				return false;
		} else if (!measurements.equals(other.measurements))
			return false;
		if (operationRevision != other.operationRevision)
			return false;
		if (overrideAssociateNo == null) {
			if (other.overrideAssociateNo != null)
				return false;
		} else if (!overrideAssociateNo.equals(other.overrideAssociateNo))
			return false;
		if (partId == null) {
			if (other.partId != null)
				return false;
		} else if (!partId.equals(other.partId))
			return false;
		if (partIndex != other.partIndex)
			return false;
		if (partRevision != other.partRevision)
			return false;
		if (partSerialNumber == null) {
			if (other.partSerialNumber != null)
				return false;
		} else if (!partSerialNumber.equals(other.partSerialNumber))
			return false;
		if (passTime != other.passTime)
			return false;
		if (productType == null) {
			if (other.productType != null)
				return false;
		} else if (!productType.equals(other.productType))
			return false;
		if (secondAlarm != other.secondAlarm)
			return false;
		if (skipped != other.skipped)
			return false;
		if (startTimestamp == null) {
			if (other.startTimestamp != null)
				return false;
		} else if (!startTimestamp.equals(other.startTimestamp))
			return false;
		if (validPartSerialNumber != other.validPartSerialNumber)
			return false;
		if (defectRefId != other.defectRefId)
			return false;
		return true;
	}

	public String toShortString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("\"").append(getId().getProductId()).append("\"");
        sb.append(",\"").append(getId().getPartName()).append("\"");
        sb.append(",\"").append(CommonUtil.convertNull(partSerialNumber)).append("\"");
        sb.append(",").append(getInstalledPartStatus());
        sb.append("]");
        return sb.toString();
    }
	
	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"").append(getId().getProductId()).append("\"");
        sb.append(",\"").append(getId().getPartName()).append("\"");
        sb.append(",\"").append(CommonUtil.convertNull(partSerialNumber)).append("\"");
        sb.append(",").append(passTime);
        sb.append(",\"").append(CommonUtil.convertNull(installedPartReason)).append("\"");
        sb.append(",\"").append(CommonUtil.convertNull(getProcessPointId())).append("\""); 
        sb.append(",\"").append(CommonUtil.convertNull(getAssociateNo())).append("\"");
        sb.append(",\"").append(CommonUtil.format(getActualTimestamp())).append("\"");
        sb.append(",").append(getInstalledPartStatus());
        sb.append(",").append(firstAlarm);
        sb.append(",").append(secondAlarm);
        sb.append(",").append(operationRevision);
        sb.append(",\"").append(CommonUtil.convertNull(featureId)).append("\"");
        sb.append(",\"").append(CommonUtil.convertNull(featureType)).append("\"");
        sb.append(",\"").append(CommonUtil.convertNull(partId)).append("\"");
        sb.append(",\"").append(partRevision).append("\"");
        sb.append(",\"").append(CommonUtil.format(getCreateTimestamp())).append("\"");
        sb.append(",\"").append(CommonUtil.format(getUpdateTimestamp())).append("\"");
        sb.append(",\"").append(CommonUtil.convertNull(getOverrideAssociateNo())).append("\"");
        sb.append(",").append(defectRefId);
        
        sb.append(", validPartSn:" + validPartSerialNumber);
        sb.append(", isSkipped:" + isSkipped());
        sb.append(", Measurements: [");
        int numOfMeasurements = measurements.size();
        int i = 0;
        for (Measurement measurement : measurements) {
            if (measurement != null) {
                sb.append(measurement.toString());
            } else {
                sb.append("null");
            }
            if (++i < numOfMeasurements)
            	sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
	
	public void setMeasurementsNg(int count){
		if(count <= 0) return;
		if(getMeasurements() == null) setMeasurements(new ArrayList<Measurement>());
		for(int j = 0; j < count; j++){
			 if(getMeasurements().size() > j) {
				Measurement measurement = getMeasurements().get(j);
				measurement.setMeasurementStatus(MeasurementStatus.NG);
			}else{
				getMeasurements().add(new Measurement(getProductId(), getPartName(), j+1));
			}
		}
	}
}

