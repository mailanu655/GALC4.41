package com.honda.galc.entity.product;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.Tag;
import com.honda.galc.dto.RestData;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.InstalledPartStatus;

/**
 * 
 * <h3>ProductBuildResult Class description</h3>
 * <p> ProductBuildResult description </p>
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
 * Mar 2, 2011
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@MappedSuperclass()
public abstract class ProductBuildResult extends AuditEntry implements Cloneable, QicsResult, IDeviceData{
	private static final long serialVersionUID = 1L;

	@RestData(key="partStatus")
	@Tag(name="INSTALLED_PART_STATUS", alt="STATUS", optional=true)
	@Column(name = "INSTALLED_PART_STATUS")
	private Integer installedPartStatusId;
	
    @RestData(key="installedAtProcessPoint")
	@Column(name = "PROCESS_POINT_ID")	//add Process Point ID column
	private String processPointId;
	
    @RestData(key="associateId")
    @Tag(name="ASSOCIATE_NO",alt="USER_ID", optional = true)
	@Column(name = "ASSOCIATE_NO")
	private String associateNo;
	
    @RestData(key="installTime")
    @Tag(name="INSTALLED_PART_ACTUAL_TIMESTAMP", alt="ACTUAL_TIMESTAMP", optional = true)
	@Column(name = "ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;

	@Transient
	@Tag(name="DEFECT_NAME", alt="DEFECT_TYPE_NAME", optional=true)
	private String defectName = null;
	
	@Transient
	private Integer defectStatus = null; 
	
	@Transient
	private String defectLocation = null;
	
	@Transient
	@Tag(name="POINT_X", optional=true)
	private int pointX;
	
	@Transient
	@Tag(name="POINT_Y", optional=true)
	private int pointY;
	
	@Transient
	private String errorCode;
	
    @Transient
    protected boolean isQicsDefect;
    
    public static final int ASSOCIATE_NO_LENGTH = 11;	
	
	// === get/set === //
	public Timestamp getActualTimestamp() {
		return actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	public String getProcessPointId() {
		return StringUtils.trim(processPointId);	
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;	
	} 
	
	public String getAssociateNo() {
		return associateNo;
	}

	// In some cases, Process_Point_Id (16) is set to Associate_No (11). 
	// Truncate Process_Point_Id if it is longer than 11
	public void setAssociateNo(String associateNo) {
		this.associateNo = StringUtils.substring(StringUtils.trim(associateNo),
				0, ProductBuildResult.ASSOCIATE_NO_LENGTH);
	} 

	public abstract String getProductId();

	public abstract void setProductId(String productId);
	
	// ----- methods added to support the generic manual lot control repair ---
	public abstract List<Measurement> getMeasurements();

	public abstract String getInstalledPartReason();

	public abstract String getPartSerialNumber();

	public Integer getInstalledPartStatusId() {
		return installedPartStatusId;
	}

	public void setInstalledPartStatusId(Integer installedPartStatus) {
		this.installedPartStatusId = installedPartStatus;
	}
	
	public InstalledPartStatus getInstalledPartStatus() {
		return InstalledPartStatus.getType(installedPartStatusId);
	}

	public void setInstalledPartStatus(InstalledPartStatus status) {
		this.installedPartStatusId = status.getId();
	}
	
	public boolean isStatusOk() {
		return installedPartStatusId != null && InstalledPartStatus.OK.getId() == installedPartStatusId;
	}

	public String getDefectName() {
		return defectName;
	}

	public void setDefectName(String defectName) {
		this.defectName = defectName;
	}

	public Integer getDefectStatus() {
		return defectStatus;
	}

	public void setDefectStatus(Integer defectStatus) {
		this.defectStatus = defectStatus;
	}
	
	public String getDefectLocation() {
		return defectLocation;
	}

	public void setDefectLocation(String defectLocation) {
		this.defectLocation = defectLocation;
	}
	
	public int getPointX() {
		return pointX;
	}

	public void setPointX(int pointX) {
		this.pointX = pointX;
	}

	public int getPointY() {
		return pointY;
	}

	public void setPointY(int pointY) {
		this.pointY = pointY;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public boolean isQicsDefect() {
		return isQicsDefect;
	}

	public void setQicsDefect(boolean isQicsDefect) {
		this.isQicsDefect = isQicsDefect;
	}

	public abstract String getPartName();

	public abstract void setPartName(String partName);

	public abstract String getResultValue();

	public abstract void setResultValue(String resultValue);

	public abstract long getDefectRefId() ;
	public abstract void setDefectRefId(long defectRefId);

	public ProductBuildResult clone() {
		ProductBuildResult result = null;
		try {
			result = (ProductBuildResult) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName()).append(":");
		builder.append("[productId:").append(getProductId()).append(", ");
		builder.append("partName:").append(getPartName()).append(", ");
		if(getInstalledPartStatusId() == null)
			builder.append("status:").append("null").append(", ");
		else
			builder.append("status:").append(getInstalledPartStatus()).append(", ");
		builder.append("resultValue:").append(getResultValue()).append("]");
		return builder.toString();
	}
	
	public static String toLogString(List<? extends ProductBuildResult> list) {
		StringBuilder sb = new StringBuilder();
		for(ProductBuildResult result : list){
			sb.append(result == null ? "null" : result.toString()).append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}
}
