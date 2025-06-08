package com.honda.galc.entity.qics;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.DefectStatus;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Feb 19, 2015
 */

@Entity
@Table(name="GAL125_HIST_TBX")
public class DefectResultHistory extends AuditEntry {
	@EmbeddedId
	private DefectResultHistoryId id;

	@Column(name="ASSOCIATE_NO")
	private String associateNo;

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
	
	@Column(name="CHANGE_ASSOCIATE")
	private String changeAssociate;
	
	@Column(name="CHANGE_NUMBER")
	private String changeNumber;

	@Column(name="CHANGE_DESCRIPTION")
	private String changeDesc;
	
	@Column(name="SHIFT")
	private String shift;

	@Column(name="DATE")
	private Date date;	
	
	@Column(name="GDP_DEFECT")
	private Integer gdpDefect;
	

	
	private static final long serialVersionUID = 1L;

	public DefectResultHistory() {
		super();
	}
	

	public DefectResultHistory(DefectResultHistoryId id, String associateNo,
			Timestamp actualTimestamp, short defectStatus, String imageName,
			String responsibleDept, String responsibleZone,
			String repairMethodNamePlan, int repairTimePlan,
			String repairAssociateNoPlan, String repairAssociateNo,
			String writeUpDepartment, short twoPartDefectFlag,
			String iqsCategoryName, String iqsItemName, String regressionCode,
			int pointX, int pointY, String entryDept, short outstandingFlag,
			Timestamp repairTimestamp, String responsibleLine,
			String partGroupName, String changeAssociate, String changeNumber,
			String changeDesc,String shift,Date date,Integer gdpDefect) {
		super();
		this.id = id;
		this.associateNo = associateNo;
		this.actualTimestamp = actualTimestamp;
		this.defectStatus = defectStatus;
		this.imageName = imageName;
		this.responsibleDept = responsibleDept;
		this.responsibleZone = responsibleZone;
		this.repairMethodNamePlan = repairMethodNamePlan;
		this.repairTimePlan = repairTimePlan;
		this.repairAssociateNoPlan = repairAssociateNoPlan;
		this.repairAssociateNo = repairAssociateNo;
		this.writeUpDepartment = writeUpDepartment;
		this.twoPartDefectFlag = twoPartDefectFlag;
		this.iqsCategoryName = iqsCategoryName;
		this.iqsItemName = iqsItemName;
		this.regressionCode = regressionCode;
		this.pointX = pointX;
		this.pointY = pointY;
		this.entryDept = entryDept;
		this.outstandingFlag = outstandingFlag;
		this.repairTimestamp = repairTimestamp;
		this.responsibleLine = responsibleLine;
		this.partGroupName = partGroupName;
		this.changeAssociate = changeAssociate;
		this.changeNumber = changeNumber;
		this.changeDesc = changeDesc;
		this.gdpDefect=gdpDefect;
		this.shift=shift;
		this.date=date;
	}


	public DefectResultHistory(DefectResult defectResult,String changeAssociate,String changeNumber,String changeDesc) {	
		super();
		java.util.Date date=new java.util.Date();
		Timestamp currentTimestamp = new Timestamp(date.getTime());
		DefectResultHistoryId id=new DefectResultHistoryId(defectResult.getId().getInspectionPartName(),
				defectResult.getId().getInspectionPartLocationName(), defectResult.getId().getDefectTypeName(),
				defectResult.getId().getSecondaryPartName(), defectResult.getId().getDefectResultId(), defectResult.getId().getApplicationId(),
				defectResult.getId().getProductId(), defectResult.getId().getTwoPartPairPart(), defectResult.getId().getTwoPartPairLocation(),currentTimestamp);		
		this.id = id;
		this.associateNo = defectResult.getAssociateNo();
		this.actualTimestamp = defectResult.getActualTimestamp();
		this.defectStatus = defectResult.getDefectStatusValue();
		this.imageName = defectResult.getImageName();
		this.responsibleDept = defectResult.getResponsibleDept();
		this.responsibleZone = defectResult.getResponsibleZone();
		this.repairMethodNamePlan = defectResult.getRepairMethodNamePlan();
		this.repairTimePlan = defectResult.getRepairTimePlan();
		this.repairAssociateNoPlan = defectResult.getRepairAssociateNoPlan();
		this.repairAssociateNo = defectResult.getRepairAssociateNo();
		this.writeUpDepartment =  defectResult.getWriteUpDepartment();
		this.twoPartDefectFlag = defectResult.getTwoPartDefectFlagVlaue();
		this.iqsCategoryName = defectResult.getIqsCategoryName();
		this.iqsItemName = defectResult.getIqsItemName();
		this.regressionCode = defectResult.getRegressionCode();
		this.pointX = defectResult.getPointX();
		this.pointY =  defectResult.getPointY();
		this.entryDept = defectResult.getEntryDept();
		this.outstandingFlag =  defectResult.getOutstandingFlag();
		this.repairTimestamp =  defectResult.getRepairTimestamp();
		this.responsibleLine = defectResult.getResponsibleLine();
		this.partGroupName = defectResult.getPartGroupName();
		this.changeAssociate = changeAssociate;
		this.changeNumber = changeNumber;
		this.changeDesc = changeDesc;
		this.shift=defectResult.getShift();
		this.date=defectResult.getDate();
		this.gdpDefect=defectResult.getGdpDefect();
	}
	
	public DefectResultHistoryId getId() {
		return this.id;
	}

	public void setId(DefectResultHistoryId id) {
		this.id = id;
	}

	public String getAssociateNo() {
		return StringUtils.trim(this.associateNo);
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
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
	

	public String toShortString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getId().getInspectionPartName())
		       .append(" ")
		       .append(getId().getInspectionPartLocationName())
		       .append(" ").append(getId().getDefectTypeName());
		return builder.toString();
	}
	
	public String toString() {
		return toString(getId().toString(),
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
	
	 
	public Integer getDefectResultHistoryId() {
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
	
	public String getChangeAssociate() {
		return changeAssociate;
	}

	public void setChangeAssociate(String changeAssociate) {
		this.changeAssociate = changeAssociate;
	}

	public String getChangeNumber() {
		return changeNumber;
	}

	public void setChangeNumber(String changeNumber) {
		this.changeNumber = changeNumber;
	}

	public String getChangeDesc() {
		return changeDesc;
	}

	public void setChangeDesc(String changeDesc) {
		this.changeDesc = changeDesc;
	}

	public Timestamp getChangeTimestamp() {		
		return getId() != null ? getId().getChangeTimestamp() : null;
	
	}

	public void setChangeTimestamp(Timestamp changeTimestamp) {		
		if(getId()!=null)
			getId().setChangeTimestamp(changeTimestamp);
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

	public Integer getGdpDefect() {
		return gdpDefect;
	}

	public void setGdpDefect(Integer gdpDefect) {
		this.gdpDefect = gdpDefect;
	}


}
