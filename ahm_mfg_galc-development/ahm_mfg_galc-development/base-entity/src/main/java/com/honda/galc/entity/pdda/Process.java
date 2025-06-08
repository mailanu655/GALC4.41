package com.honda.galc.entity.pdda;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 */
@Entity
@Table(name="PVPMX1", schema="VIOS")
public class Process extends AuditEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="MAINTENANCE_ID", nullable=false)
	private int maintenanceId;

	@Column(name="ACTION", nullable=false, length=1)
	private String action;

	@Column(name="ADD_INFO_NUMBER", nullable=false, length=18)
	private String addInfoNumber;

	@Column(name="ADDITIONAL_COMMENT", nullable=false, length=254)
	private String additionalComment;

	@Column(name="ASM_PROC_NAME", nullable=false, length=35)
	private String asmProcName;

	@Column(name="ASM_PROC_NO", nullable=false, length=5)
	private String asmProcNo;

	@Column(name="ASM_PROC_SEQ_NO", nullable=false)
	private short asmProcSeqNo;

	@Column(name="BODY_LOC_NO", nullable=false, length=4)
	private String bodyLocNo;

	@Column(name="DEPT_CODE", nullable=false, length=2)
	private String deptCode;

	@Column(name="EFF_BEG_DATE", nullable=false)
	private Date effBegDate;

	@Column(name="EFF_END_DATE", nullable=false)
	private Date effEndDate;

	@Column(name="EXTRACT_DATE", nullable=false)
	private Date extractDate;

	@Column(name="MAINT_DATE", nullable=false)
	private Date maintDate;

	@Column(name="MAINT_LOGON_ID", nullable=false, length=8)
	private String maintLogonId;

	@Column(name="MODEL_YEAR_DATE", nullable=false, precision=5, scale=1)
	private BigDecimal modelYearDate;

	@Column(name="NUM_ASSOCIATES", nullable=false)
	private short NumAssociates;

	@Column(name="PLANT_LOC_CODE", nullable=false, length=1)
	private String plantLocCode;

	@Column(name="PROCESS_LOCATION", length=4)
	private String processLocation;

	@Column(name="PROD_ASM_LINE_NO", nullable=false, length=1)
	private String prodAsmLineNo;

	@Column(name="PROD_SCH_QTY", nullable=false, precision=5, scale=1)
	private BigDecimal prodSchQty;

	@Column(name="REASON_CODE_TYPE", nullable=false, length=18)
	private String reasonCodeType;

	@Column(name="REASON_FOR_CHANGE", nullable=false, length=254)
	private String reasonForChange;

	@Column(name="ROTATION", nullable=false, length=1)
	private String rotation;

	@Column(name="TEAM_DESC", nullable=false, length=30)
	private String teamDesc;

	@Column(name="TEAM_NO", nullable=false, length=4)
	private String teamNo;

	@Column(name="TEAM_SEQ_NO", nullable=false)
	private int teamSeqNo;

	@Column(name="VEHICLE_MODEL_CODE", nullable=false, length=1)
	private String vehicleModelCode;

    public Process() {}
    
	public Integer getId() {
		return getMaintenanceId();
	}
	
	public void setId(int id) {
		setMaintenanceId(id);
	}

	public int getMaintenanceId() {
		return this.maintenanceId;
	}

	public void setMaintenanceId(int maintenanceId) {
		this.maintenanceId = maintenanceId;
	}

	public String getAction() {
		return StringUtils.trim(this.action);
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAddInfoNumber() {
		return StringUtils.trim(this.addInfoNumber);
	}

	public void setAddInfoNumber(String addInfoNumber) {
		this.addInfoNumber = addInfoNumber;
	}

	public String getAdditionalComment() {
		return StringUtils.trim(this.additionalComment);
	}

	public void setAdditionalComment(String additionalComment) {
		this.additionalComment = additionalComment;
	}

	public String getAsmProcName() {
		return StringUtils.trim(this.asmProcName);
	}

	public void setAsmProcName(String asmProcName) {
		this.asmProcName = asmProcName;
	}

	public String getAsmProcNo() {
		return StringUtils.trim(this.asmProcNo);
	}

	public void setAsmProcNo(String asmProcNo) {
		this.asmProcNo = asmProcNo;
	}

	public short getAsmProcSeqNo() {
		return this.asmProcSeqNo;
	}

	public void setAsmProcSeqNo(short asmProcSeqNo) {
		this.asmProcSeqNo = asmProcSeqNo;
	}

	public String getBodyLocNo() {
		return StringUtils.trim(this.bodyLocNo);
	}

	public void setBodyLocNo(String bodyLocNo) {
		this.bodyLocNo = bodyLocNo;
	}

	public String getDeptCode() {
		return StringUtils.trim(this.deptCode);
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public Date getEffBegDate() {
		return this.effBegDate;
	}

	public void setEffBegDate(Date effBegDate) {
		this.effBegDate = effBegDate;
	}

	public Date getEffEndDate() {
		return this.effEndDate;
	}

	public void setEffEndDate(Date effEndDate) {
		this.effEndDate = effEndDate;
	}

	public Date getExtractDate() {
		return this.extractDate;
	}

	public void setExtractDate(Date extractDate) {
		this.extractDate = extractDate;
	}

	public Date getMaintDate() {
		return this.maintDate;
	}

	public void setMaintDate(Date maintDate) {
		this.maintDate = maintDate;
	}

	public String getMaintLogonId() {
		return StringUtils.trim(this.maintLogonId);
	}

	public void setMaintLogonId(String maintLogonId) {
		this.maintLogonId = maintLogonId;
	}

	public BigDecimal getModelYearDate() {
		return this.modelYearDate;
	}

	public void setModelYearDate(BigDecimal modelYearDate) {
		this.modelYearDate = modelYearDate;
	}

	public short getNumAssociates() {
		return this.NumAssociates;
	}

	public void setNumAssociates(short NumAssociates) {
		this.NumAssociates = NumAssociates;
	}

	public String getPlantLocCode() {
		return StringUtils.trim(this.plantLocCode);
	}

	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
	}

	public String getProcessLocation() {
		return StringUtils.trim(this.processLocation);
	}

	public void setProcessLocation(String processLocation) {
		this.processLocation = processLocation;
	}

	public String getProdAsmLineNo() {
		return StringUtils.trim(this.prodAsmLineNo);
	}

	public void setProdAsmLineNo(String prodAsmLineNo) {
		this.prodAsmLineNo = prodAsmLineNo;
	}

	public BigDecimal getProdSchQty() {
		return this.prodSchQty;
	}

	public void setProdSchQty(BigDecimal prodSchQty) {
		this.prodSchQty = prodSchQty;
	}

	public String getReasonCodeType() {
		return StringUtils.trim(this.reasonCodeType);
	}

	public void setReasonCodeType(String reasonCodeType) {
		this.reasonCodeType = reasonCodeType;
	}

	public String getReasonForChange() {
		return StringUtils.trim(this.reasonForChange);
	}

	public void setReasonForChange(String reasonForChange) {
		this.reasonForChange = reasonForChange;
	}

	public String getRotation() {
		return StringUtils.trim(this.rotation);
	}

	public void setRotation(String rotation) {
		this.rotation = rotation;
	}

	public String getTeamDesc() {
		return StringUtils.trim(this.teamDesc);
	}

	public void setTeamDesc(String teamDesc) {
		this.teamDesc = teamDesc;
	}

	public String getTeamNo() {
		return StringUtils.trim(this.teamNo);
	}

	public void setTeamNo(String teamNo) {
		this.teamNo = teamNo;
	}

	public int getTeamSeqNo() {
		return this.teamSeqNo;
	}

	public void setTeamSeqNo(int teamSeqNo) {
		this.teamSeqNo = teamSeqNo;
	}

	public String getVehicleModelCode() {
		return StringUtils.trim(this.vehicleModelCode);
	}

	public void setVehicleModelCode(String vehicleModelCode) {
		this.vehicleModelCode = vehicleModelCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + NumAssociates;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result
				+ ((addInfoNumber == null) ? 0 : addInfoNumber.hashCode());
		result = prime
				* result
				+ ((additionalComment == null) ? 0 : additionalComment
						.hashCode());
		result = prime * result
				+ ((asmProcName == null) ? 0 : asmProcName.hashCode());
		result = prime * result
				+ ((asmProcNo == null) ? 0 : asmProcNo.hashCode());
		result = prime * result + asmProcSeqNo;
		result = prime * result
				+ ((bodyLocNo == null) ? 0 : bodyLocNo.hashCode());
		result = prime * result
				+ ((deptCode == null) ? 0 : deptCode.hashCode());
		result = prime * result
				+ ((effBegDate == null) ? 0 : effBegDate.hashCode());
		result = prime * result
				+ ((effEndDate == null) ? 0 : effEndDate.hashCode());
		result = prime * result
				+ ((extractDate == null) ? 0 : extractDate.hashCode());
		result = prime * result
				+ ((maintDate == null) ? 0 : maintDate.hashCode());
		result = prime * result
				+ ((maintLogonId == null) ? 0 : maintLogonId.hashCode());
		result = prime * result + maintenanceId;
		result = prime * result
				+ ((modelYearDate == null) ? 0 : modelYearDate.hashCode());
		result = prime * result
				+ ((plantLocCode == null) ? 0 : plantLocCode.hashCode());
		result = prime * result
				+ ((processLocation == null) ? 0 : processLocation.hashCode());
		result = prime * result
				+ ((prodAsmLineNo == null) ? 0 : prodAsmLineNo.hashCode());
		result = prime * result
				+ ((prodSchQty == null) ? 0 : prodSchQty.hashCode());
		result = prime * result
				+ ((reasonCodeType == null) ? 0 : reasonCodeType.hashCode());
		result = prime * result
				+ ((reasonForChange == null) ? 0 : reasonForChange.hashCode());
		result = prime * result
				+ ((rotation == null) ? 0 : rotation.hashCode());
		result = prime * result
				+ ((teamDesc == null) ? 0 : teamDesc.hashCode());
		result = prime * result + ((teamNo == null) ? 0 : teamNo.hashCode());
		result = prime * result + teamSeqNo;
		result = prime
				* result
				+ ((vehicleModelCode == null) ? 0 : vehicleModelCode.hashCode());
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
		Process other = (Process) obj;
		if (NumAssociates != other.NumAssociates)
			return false;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (addInfoNumber == null) {
			if (other.addInfoNumber != null)
				return false;
		} else if (!addInfoNumber.equals(other.addInfoNumber))
			return false;
		if (additionalComment == null) {
			if (other.additionalComment != null)
				return false;
		} else if (!additionalComment.equals(other.additionalComment))
			return false;
		if (asmProcName == null) {
			if (other.asmProcName != null)
				return false;
		} else if (!asmProcName.equals(other.asmProcName))
			return false;
		if (asmProcNo == null) {
			if (other.asmProcNo != null)
				return false;
		} else if (!asmProcNo.equals(other.asmProcNo))
			return false;
		if (asmProcSeqNo != other.asmProcSeqNo)
			return false;
		if (bodyLocNo == null) {
			if (other.bodyLocNo != null)
				return false;
		} else if (!bodyLocNo.equals(other.bodyLocNo))
			return false;
		if (deptCode == null) {
			if (other.deptCode != null)
				return false;
		} else if (!deptCode.equals(other.deptCode))
			return false;
		if (effBegDate == null) {
			if (other.effBegDate != null)
				return false;
		} else if (!effBegDate.equals(other.effBegDate))
			return false;
		if (effEndDate == null) {
			if (other.effEndDate != null)
				return false;
		} else if (!effEndDate.equals(other.effEndDate))
			return false;
		if (extractDate == null) {
			if (other.extractDate != null)
				return false;
		} else if (!extractDate.equals(other.extractDate))
			return false;
		if (maintDate == null) {
			if (other.maintDate != null)
				return false;
		} else if (!maintDate.equals(other.maintDate))
			return false;
		if (maintLogonId == null) {
			if (other.maintLogonId != null)
				return false;
		} else if (!maintLogonId.equals(other.maintLogonId))
			return false;
		if (maintenanceId != other.maintenanceId)
			return false;
		if (modelYearDate == null) {
			if (other.modelYearDate != null)
				return false;
		} else if (!modelYearDate.equals(other.modelYearDate))
			return false;
		if (plantLocCode == null) {
			if (other.plantLocCode != null)
				return false;
		} else if (!plantLocCode.equals(other.plantLocCode))
			return false;
		if (processLocation == null) {
			if (other.processLocation != null)
				return false;
		} else if (!processLocation.equals(other.processLocation))
			return false;
		if (prodAsmLineNo == null) {
			if (other.prodAsmLineNo != null)
				return false;
		} else if (!prodAsmLineNo.equals(other.prodAsmLineNo))
			return false;
		if (prodSchQty == null) {
			if (other.prodSchQty != null)
				return false;
		} else if (!prodSchQty.equals(other.prodSchQty))
			return false;
		if (reasonCodeType == null) {
			if (other.reasonCodeType != null)
				return false;
		} else if (!reasonCodeType.equals(other.reasonCodeType))
			return false;
		if (reasonForChange == null) {
			if (other.reasonForChange != null)
				return false;
		} else if (!reasonForChange.equals(other.reasonForChange))
			return false;
		if (rotation == null) {
			if (other.rotation != null)
				return false;
		} else if (!rotation.equals(other.rotation))
			return false;
		if (teamDesc == null) {
			if (other.teamDesc != null)
				return false;
		} else if (!teamDesc.equals(other.teamDesc))
			return false;
		if (teamNo == null) {
			if (other.teamNo != null)
				return false;
		} else if (!teamNo.equals(other.teamNo))
			return false;
		if (teamSeqNo != other.teamSeqNo)
			return false;
		if (vehicleModelCode == null) {
			if (other.vehicleModelCode != null)
				return false;
		} else if (!vehicleModelCode.equals(other.vehicleModelCode))
			return false;
		return true;
	}
		
	@Override
	public String toString() {
		return toString(getId(), getMaintenanceId(), getAction(), getAddInfoNumber(), 
				getAdditionalComment(), getAsmProcName(), getAsmProcNo(), getAsmProcSeqNo(), 
				getBodyLocNo(), getDeptCode(), getEffBegDate(), getEffEndDate(), 
				getExtractDate(), getMaintDate(), getMaintLogonId(), getModelYearDate(), 
				getNumAssociates(), getPlantLocCode(), getProcessLocation(), getProdAsmLineNo(), 
				getProdSchQty(), getReasonCodeType(), getReasonForChange(), getRotation(), 
				getTeamDesc(), getTeamNo(), getTeamSeqNo(), getVehicleModelCode());
	}
}