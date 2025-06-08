package com.honda.galc.entity.qics;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumn;
import org.apache.openjpa.persistence.jdbc.ElementJoinColumns;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.DefectStatus;

/**
 * 
 * <h3>DefectResult Class description</h3>
 * <p> DefectResult description </p>
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
 * Mar 30, 2011
 *
 *
 */
/*   
* @author Gangadhararao Gadde
* jan 17, 2017
* ver 2
*
*
*/
@Entity
@Table(name="GAL125TBX")
public class DefectResult extends AuditEntry {
	@EmbeddedId
	private DefectResultId id;

	@Column(name="ASSOCIATE_NO")
	private String associateNo;

	@Column(name="SHIFT")
	private String shift;

	@Column(name="DATE")
	private Date date;

	@Column(name="ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;

	@Column(name="DEFECT_STATUS")
	private short defectStatus;

	@Column(name="IMAGE_NAME")
	private String imageName;

	@Column(name="RESPONSIBLE_DEPT")
	private String responsibleDept;

	@Column(name="RESPONSIBLE_ZONE")
	private String responsibleZone;

	@Column(name="REPAIR_METHOD_NAME_PLAN")
	private String repairMethodNamePlan;

	@Column(name="REPAIR_TIME_PLAN")
	private int repairTimePlan;

	@Column(name="REPAIR_ASSOCIATE_NO_PLAN")
	private String repairAssociateNoPlan;

	@Column(name="REPAIR_ASSOCIATE_NO")
	private String repairAssociateNo;

	@Column(name="WRITE_UP_DEPARTMENT")
	private String writeUpDepartment;

	@Column(name="TWO_PART_DEFECT_FLAG")
	private short twoPartDefectFlag;

	@Column(name="IQS_CATEGORY_NAME")
	private String iqsCategoryName;

	@Column(name="IQS_ITEM_NAME")
	private String iqsItemName;

	@Column(name="REGRESSION_CODE")
	private String regressionCode;

	@Column(name="POINT_X")
	private int pointX;

	@Column(name="POINT_Y")
	private int pointY;

	@Column(name="ENTRY_DEPT")
	private String entryDept;

	@Column(name="OUTSTANDING_FLAG")
	private short outstandingFlag;

	@Column(name="REPAIR_TIMESTAMP")
	private Timestamp repairTimestamp;

	@Column(name="RESPONSIBLE_LINE")
	private String responsibleLine;
	
	@Column(name="PART_GROUP_NAME")
	private String partGroupName;
	
	@Column(name="GDP_DEFECT")
	private Integer gdpDefect;
	
	@Column(name="NAQ_DEFECTRESULTID")
	private long naqDefectResultId;	

	@OneToMany(cascade={CascadeType.MERGE},targetEntity = DefectRepairResult.class,fetch = FetchType.EAGER)
    @ElementJoinColumns({
    	@ElementJoinColumn(name="DEFECTRESULTID",referencedColumnName="DEFECTRESULTID",updatable=false,insertable=false),
    	@ElementJoinColumn(name="PRODUCT_ID",referencedColumnName="PRODUCT_ID",updatable=false,insertable=false)
    })
    private List<DefectRepairResult> defectRepairResults;
	
		
	@Transient
	private boolean isNewDefect = false;
	
	@Transient
	private boolean isChangeAtRepair = false;
	
	@Transient
	private boolean engineFiring = false;
	
	@Transient
	private String entryStation;
	
	@Transient
	private String errorCode;
	
	@Transient
	private boolean isQicsDefect;
	
	@Transient long defectRefId = 0;
	
	private static final long serialVersionUID = 1L;

	public DefectResult() {
		super();
	}

	public DefectResultId getId() {
		return this.id;
	}

	public void setId(DefectResultId id) {
		this.id = id;
	}

	public String getAssociateNo() {
		return StringUtils.trim(this.associateNo);
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}

	public String getShift() {
		return StringUtils.trim(this.shift);
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Timestamp getActualTimestamp() {
		return this.actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	public short getDefectStatusValue() {
		return this.defectStatus;
	}

	public void setDefectStatusValue(short defectStatus) {
		this.defectStatus = defectStatus;
	}
	
	public DefectStatus getDefectStatus() {
		return DefectStatus.getType(this.defectStatus);
	}
	
	public void setDefectStatus(DefectStatus defectStatus) {
		this.defectStatus = (short)defectStatus.getId();
	}
	
	public boolean isOutstandingStatus() {
		
		return this.defectStatus == DefectStatus.OUTSTANDING.getId();
		
	}

	public boolean isScrapStatus() {
		
		return this.defectStatus == DefectStatus.SCRAP.getId();
		
	}
	
	public boolean isRepairedStatus() {
		
		return this.defectStatus == DefectStatus.REPAIRED.getId();
		
	}
	
	public void setDefectStatus(short defectStatus) {
		this.defectStatus = defectStatus;
	}

	public String getImageName() {
		return StringUtils.trim(this.imageName);
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getResponsibleDept() {
		return StringUtils.trim(this.responsibleDept);
	}

	public void setResponsibleDept(String responsibleDept) {
		this.responsibleDept = responsibleDept;
	}

	public String getResponsibleZone() {
		return StringUtils.trim(this.responsibleZone);
	}

	public void setResponsibleZone(String responsibleZone) {
		this.responsibleZone = responsibleZone;
	}

	public String getRepairMethodNamePlan() {
		return StringUtils.trim(this.repairMethodNamePlan);
	}

	public void setRepairMethodNamePlan(String repairMethodNamePlan) {
		this.repairMethodNamePlan = repairMethodNamePlan;
	}

	public int getRepairTimePlan() {
		return this.repairTimePlan;
	}

	public void setRepairTimePlan(int repairTimePlan) {
		this.repairTimePlan = repairTimePlan;
	}

	public String getRepairAssociateNoPlan() {
		return StringUtils.trim(this.repairAssociateNoPlan);
	}

	public void setRepairAssociateNoPlan(String repairAssociateNoPlan) {
		this.repairAssociateNoPlan = repairAssociateNoPlan;
	}

	public String getRepairAssociateNo() {
		return StringUtils.trim(this.repairAssociateNo);
	}

	public void setRepairAssociateNo(String repairAssociateNo) {
		this.repairAssociateNo = repairAssociateNo;
	}

	public String getWriteUpDepartment() {
		return StringUtils.trim(this.writeUpDepartment);
	}

	public void setWriteUpDepartment(String writeUpDepartment) {
		this.writeUpDepartment = writeUpDepartment;
	}

	public short getTwoPartDefectFlagVlaue() {
		return this.twoPartDefectFlag;
	}

	public void setTwoPartDefectFlagValue(short twoPartDefectFlag) {
		this.twoPartDefectFlag = twoPartDefectFlag;
	}
	
	public boolean getTwoPartDefectFlag() {
		return this.twoPartDefectFlag >= 1;
	}

	public void setTwoPartDefectFlag(boolean twoPartDefectFlagValue) {
		this.twoPartDefectFlag =(short)( twoPartDefectFlagValue ? 1 : 0);
	}

	public String getIqsCategoryName() {
		return StringUtils.trim(this.iqsCategoryName);
	}

	public void setIqsCategoryName(String iqsCategoryName) {
		this.iqsCategoryName = iqsCategoryName;
	}

	public String getIqsItemName() {
		return StringUtils.trim(this.iqsItemName);
	}

	public void setIqsItemName(String iqsItemName) {
		this.iqsItemName = iqsItemName;
	}

	public String getRegressionCode() {
		return StringUtils.trim(this.regressionCode);
	}

	public void setRegressionCode(String regressionCode) {
		this.regressionCode = regressionCode;
	}

	public int getPointX() {
		return this.pointX;
	}

	public void setPointX(int pointX) {
		this.pointX = pointX;
	}

	public int getPointY() {
		return this.pointY;
	}

	public void setPointY(int pointY) {
		this.pointY = pointY;
	}

	public String getEntryDept() {
		return StringUtils.trim(this.entryDept);
	}

	public void setEntryDept(String entryDept) {
		this.entryDept = entryDept;
	}

	public short getOutstandingFlag() {
		return this.outstandingFlag;
	}

	public void setOutstandingFlag(short outstandingFlag) {
		this.outstandingFlag = outstandingFlag;
	}
	
	public void setOutstandingFlag(boolean aFlag) {
		
		this.outstandingFlag =(short)( aFlag ? 1 : 0);
	}

	public Timestamp getRepairTimestamp() {
		return this.repairTimestamp;
	}

	public void setRepairTimestamp(Timestamp repairTimestamp) {
		this.repairTimestamp = repairTimestamp;
	}

	public String getResponsibleLine() {
		return StringUtils.trim(this.responsibleLine);
	}

	public void setResponsibleLine(String responsibleLine) {
		this.responsibleLine = responsibleLine;
	}
	
	
	public DefectRepairResult getDefectRepairResult() {
		if(!hasDefectRepairResult()){
			DefectRepairResult defectRepairResult = createNewDefectRepairResult();
			getDefectRepairResults().add(defectRepairResult);
		}
		return defectRepairResults.get(0);
	}
	
	public List<DefectRepairResult> getDefectRepairResults() {
		if(defectRepairResults == null) defectRepairResults = new ArrayList<DefectRepairResult>();
		return defectRepairResults;
	}
	
	private DefectRepairResult createNewDefectRepairResult() {
		
		DefectRepairResult defectRepairResult = new DefectRepairResult();
		DefectRepairResultId id = new DefectRepairResultId();
		id.setProductId(getId().getProductId());
		id.setDefectResultId(getId().getDefectResultId());
		defectRepairResult.setId(id);
		return defectRepairResult;
	}
	
	public void setDefectRepairResult(DefectRepairResult defectRepairResult) {
		getDefectRepairResults().add(defectRepairResult);
	}

	
	public boolean isNewDefect() {
		return isNewDefect;
	}

	public void setNewDefect(boolean isNewDefect) {
		this.isNewDefect = isNewDefect;
	}
	
	public boolean isChangeAtRepair() {
		return isChangeAtRepair;
	}

	public void setChangeAtRepair(boolean isChangeAtRepair) {
		this.isChangeAtRepair = isChangeAtRepair;
	}

	public boolean hasDefectRepairResult() {
		
		return defectRepairResults != null && !defectRepairResults.isEmpty();
		
	}
	
	public String getDefectTypeName() {
		return id == null ? null : getId().getDefectTypeName();
	}

	public String getInspectionPartLocationName() {
		return id == null ? null : getId().getInspectionPartLocationName();
	}
	
	public String getInspectionPartName() {
		return id == null ? null : getId().getInspectionPartName();
	}
	
	public String getSecondaryPartName() {
		return id == null ? null : getId().getSecondaryPartName();
	}
	
	public boolean isEngineFiring() {
		return engineFiring;
	}

	public void setEngineFiring(boolean engineFiring) {
		this.engineFiring = engineFiring;
	}
	
	public String getEntryStation() {
		return entryStation;
	}

	public void setEntryStation(String entryStation) {
		this.entryStation = entryStation;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String toShortString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getId().getInspectionPartName())
		       .append(" ")
		       .append(getId().getInspectionPartLocationName())
		       .append(" ").append(getId().getDefectTypeName());
		return builder.toString();
	}
	
	public String toString() {
		return toString(getId().getProductId(),
				getDefectTypeName(),
				getInspectionPartName(),
				getInspectionPartLocationName()
				);
	}

	public String getPartGroupName() {
		return partGroupName;
	}

	public void setPartGroupName(String partGroupName) {
		this.partGroupName = partGroupName;
	}
	
	 
	public Integer getDefectResultId() {
		return getId() != null ? getId().getDefectResultId() : null;
	}
	
	public String getApplicationId() {
		return getId() != null ? getId().getApplicationId() : null;
	}
	
	public String getProductId() {
		return getId() != null ? getId().getProductId() : null;
	}
	
	public String getTwoPartPairPart() {
		return getId() != null ? getId().getTwoPartPairPart() : null;
	}
	
	public String getTwoPartPairLocation() {
		return getId() != null ? getId().getTwoPartPairLocation() : null;
	}
	
	public void setInspectionPartName(String inspectionPartName) {
		if(getId()!=null)
			getId().setInspectionPartName(inspectionPartName);
	}

	public void setInspectionPartLocationName(String inspectionPartLocationName) {
		if(getId()!=null)
			getId().setInspectionPartLocationName(inspectionPartLocationName);
	}

	public void setDefectTypeName(String defectTypeName) {
		if(getId()!=null)
			getId().setDefectTypeName(defectTypeName);
	}

	public void setSecondaryPartName(String secondaryPartName) {
		if(getId()!=null)
			getId().setSecondaryPartName(secondaryPartName);
	}

	public void setDefectResultId(int defectResultId) {
		if(getId()!=null)
			getId().setDefectResultId(defectResultId);
	}

	public void setApplicationId(String applicationId) {
		if( getId() != null )
			getId().setApplicationId(applicationId);
	}

	public void setProductId(String productId) {
		if( getId() != null )
			getId().setProductId(productId);
	}

	public void setTwoPartPairPart(String twoPartPairPart) {
		if( getId() != null )
			getId().setTwoPartPairPart(twoPartPairPart);
	
	}

	public void setTwoPartPairLocation(String twoPartPairLocation) {
		if( getId() != null )
			getId().setTwoPartPairLocation(twoPartPairLocation);		
	}	
	
	public Integer getGdpDefect() {
		return gdpDefect;
	}

	public void setGdpDefect(Integer gdpDefect) {
		this.gdpDefect = gdpDefect;
	}
	
	public long getNaqDefectResultId() {
		return naqDefectResultId;
	}

	public void setNaqDefectResultId(long naqDefectResultId) {
		this.naqDefectResultId = naqDefectResultId;
	}

	public boolean isQicsDefect() {
		return isQicsDefect;
	}

	public void setQicsDefect(boolean isQicsDefect) {
		this.isQicsDefect = isQicsDefect;
	}

	public long getDefectRefId() {
		return defectRefId;
	}

	public void setDefectRefId(long defectRefId) {
		this.defectRefId = defectRefId;
	}
}
