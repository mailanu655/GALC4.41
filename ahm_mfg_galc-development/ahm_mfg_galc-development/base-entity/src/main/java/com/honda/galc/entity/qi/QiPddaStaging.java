package com.honda.galc.entity.qi;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;


import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>QiPddaStaging Class description</h3>
 * <p>
 * QiPddaStaging contains the getter and setter of the responsible
 * associate and maps this class with database and these columns .
 * </p>
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
 * @author LnTInfotech<br>
 *         May 15, 2017
 * 
 */
@Entity
@Table(name = "PALSQ1", schema="VIOS")
public class QiPddaStaging extends AuditEntry {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "PCL_TO_QICS_SEQ")
	private Integer pclToQicsSeq;
	
	@Column(name = "COMPANY_CODE")
	private String companyCode; 
	
	@Column(name = "PLANT_LOC_CODE")
	private String plantLocationCode;
	
	@Column(name = "DEPT_CODE")
	private String departmentCode;
	
	@Column(name = "TEAM_NO")
	private String teamNumber;
	
	@Column(name = "TEAM_DESC")
	private String teamDescription;
	
	@Column(name = "PROD_SCH_QTY")
	private BigDecimal prodSchQuantity;
	
	@Column(name = "MODEL_YEAR_DATE")
	private BigDecimal modelYearDate; 
	
	@Column(name = "VEHICLE_MODEL_CODE")
	private String vehicleModelCode;
	
	@Column(name = "PROD_ASM_LINE_NO")
	private String prodAsmLineNo;
	
	@Column(name = "ASM_PROC_NO")
	private String asmProcNo; 

	@Column(name = "ASM_PROC_NAME")
	private String asmProcName;
	
	@Column(name = "UNIT_NO")
	private String unitNumber;
	
	@Column(name = "UNIT_RANK")
	private String unitRank;

	@Column(name = "PROD_UNIT_ID_NO")
	private String prodUnitIdNo;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "BASE_PART_NO")
	private String basePartNumber;

	@Column(name = "DATA_COL_BY_QICS")
	private String dataColByQics;
	
	@Column(name = "UNIT_OP_DESC_TEXT")
	private String unitDescription;
	
	@Column(name = "INSERT_LOGON_NO")
	private String createUser;
	
	@Column(name = "UPDATE_LOGON_NO")
	private String updateUser;

	public Integer getPclToQicsSeq() {
		return pclToQicsSeq;
	}

	public void setPclToQicsSeq(Integer pclToQicsSeq) {
		this.pclToQicsSeq = pclToQicsSeq;
	}

	public String getCompanyCode() {
		return StringUtils.trimToEmpty(companyCode);
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getPlantLocationCode() {
		return StringUtils.trimToEmpty(plantLocationCode);
	}

	public void setPlantLocationCode(String plantLocationCode) {
		this.plantLocationCode = plantLocationCode;
	}

	public String getDepartmentCode() {
		return StringUtils.trimToEmpty(departmentCode);
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getTeamNumber() {
		return StringUtils.trimToEmpty(teamNumber);
	}

	public void setTeamNumber(String teamNumber) {
		this.teamNumber = teamNumber;
	}

	public String getTeamDescription() {
		return StringUtils.trimToEmpty(teamDescription);
	}

	public void setTeamDescription(String teamDescription) {
		this.teamDescription = teamDescription;
	}

	public BigDecimal getProdSchQuantity() {
		return prodSchQuantity;
	}

	public void setProdSchQuantity(BigDecimal prodSchQuantity) {
		this.prodSchQuantity = prodSchQuantity;
	}

	public BigDecimal getModelYearDate() {
		return modelYearDate;
	}

	public void setModelYearDate(BigDecimal modelYear) {
		this.modelYearDate = modelYear;
	}

	public String getVehicleModelCode() {
		return StringUtils.trimToEmpty(vehicleModelCode);
	}

	public void setVehicleModelCode(String vehicleModelCode) {
		this.vehicleModelCode = vehicleModelCode;
	}

	public String getProdAsmLineNo() {
		return StringUtils.trimToEmpty(prodAsmLineNo);
	}

	public void setProdAsmLineNo(String prodAsmLineNo) {
		this.prodAsmLineNo = prodAsmLineNo;
	}

	public String getAsmProcNo() {
		return StringUtils.trimToEmpty(asmProcNo);
	}

	public void setAsmProcNo(String asmProcNo) {
		this.asmProcNo = asmProcNo;
	}

	public String getAsmProcName() {
		return StringUtils.trimToEmpty(asmProcName);
	}

	public void setAsmProcName(String asmProcName) {
		this.asmProcName = asmProcName;
	}

	public String getUnitNumber() {
		return StringUtils.trimToEmpty(unitNumber);
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	public String getUnitRank() {
		return StringUtils.trimToEmpty(unitRank);
	}

	public void setUnitRank(String unitRank) {
		this.unitRank = unitRank;
	}

	public String getProdUnitIdNo() {
		return StringUtils.trimToEmpty(prodUnitIdNo);
	}

	public void setProdUnitIdNo(String prodUnitIdNo) {
		this.prodUnitIdNo = prodUnitIdNo;
	}

	public String getStatus() {
		return StringUtils.trimToEmpty(status);
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBasePartNumber() {
		return StringUtils.trimToEmpty(basePartNumber);
	}

	public void setBasePartNumber(String basePartNumber) {
		this.basePartNumber = basePartNumber;
	}

	public String getDataColByQics() {
		return StringUtils.trimToEmpty(dataColByQics);
	}

	public void setDataColByQics(String dataColByQics) {
		this.dataColByQics = dataColByQics;
	}

	public String getUnitDescription() {
		return StringUtils.trimToEmpty(unitDescription);
	}

	public void setUnitDescription(String unitDescription) {
		this.unitDescription = unitDescription;
	}
	
	public String getCreateUser() {
		return StringUtils.trimToEmpty(createUser);
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return StringUtils.trimToEmpty(updateUser);
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
	public Object getId() {
		return getPclToQicsSeq();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((asmProcName == null) ? 0 : asmProcName.hashCode());
		result = prime * result
				+ ((asmProcNo == null) ? 0 : asmProcNo.hashCode());
		result = prime * result
				+ ((basePartNumber == null) ? 0 : basePartNumber.hashCode());
		result = prime * result
				+ ((companyCode == null) ? 0 : companyCode.hashCode());
		result = prime * result
				+ ((dataColByQics == null) ? 0 : dataColByQics.hashCode());
		result = prime * result
				+ ((departmentCode == null) ? 0 : departmentCode.hashCode());
		result = prime * result
				+ ((modelYearDate == null) ? 0 : modelYearDate.hashCode());
		result = prime * result
				+ ((pclToQicsSeq == null) ? 0 : pclToQicsSeq.hashCode());
		result = prime
				* result
				+ ((plantLocationCode == null) ? 0 : plantLocationCode
						.hashCode());
		result = prime * result
				+ ((prodAsmLineNo == null) ? 0 : prodAsmLineNo.hashCode());
		result = prime * result
				+ ((prodSchQuantity == null) ? 0 : prodSchQuantity.hashCode());
		result = prime * result
				+ ((prodUnitIdNo == null) ? 0 : prodUnitIdNo.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((teamDescription == null) ? 0 : teamDescription.hashCode());
		result = prime * result
				+ ((teamNumber == null) ? 0 : teamNumber.hashCode());
		result = prime * result
				+ ((unitDescription == null) ? 0 : unitDescription.hashCode());
		result = prime * result
				+ ((unitNumber == null) ? 0 : unitNumber.hashCode());
		result = prime * result
				+ ((unitRank == null) ? 0 : unitRank.hashCode());
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
		QiPddaStaging other = (QiPddaStaging) obj;
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
		if (basePartNumber == null) {
			if (other.basePartNumber != null)
				return false;
		} else if (!basePartNumber.equals(other.basePartNumber))
			return false;
		if (companyCode == null) {
			if (other.companyCode != null)
				return false;
		} else if (!companyCode.equals(other.companyCode))
			return false;
		if (dataColByQics == null) {
			if (other.dataColByQics != null)
				return false;
		} else if (!dataColByQics.equals(other.dataColByQics))
			return false;
		if (departmentCode == null) {
			if (other.departmentCode != null)
				return false;
		} else if (!departmentCode.equals(other.departmentCode))
			return false;
		if (modelYearDate == null) {
			if (other.modelYearDate != null)
				return false;
		} else if (!modelYearDate.equals(other.modelYearDate))
			return false;
		if (pclToQicsSeq == null) {
			if (other.pclToQicsSeq != null)
				return false;
		} else if (!pclToQicsSeq.equals(other.pclToQicsSeq))
			return false;
		if (plantLocationCode == null) {
			if (other.plantLocationCode != null)
				return false;
		} else if (!plantLocationCode.equals(other.plantLocationCode))
			return false;
		if (prodAsmLineNo == null) {
			if (other.prodAsmLineNo != null)
				return false;
		} else if (!prodAsmLineNo.equals(other.prodAsmLineNo))
			return false;
		if (prodSchQuantity == null) {
			if (other.prodSchQuantity != null)
				return false;
		} else if (!prodSchQuantity.equals(other.prodSchQuantity))
			return false;
		if (prodUnitIdNo == null) {
			if (other.prodUnitIdNo != null)
				return false;
		} else if (!prodUnitIdNo.equals(other.prodUnitIdNo))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (teamDescription == null) {
			if (other.teamDescription != null)
				return false;
		} else if (!teamDescription.equals(other.teamDescription))
			return false;
		if (teamNumber == null) {
			if (other.teamNumber != null)
				return false;
		} else if (!teamNumber.equals(other.teamNumber))
			return false;
		if (unitDescription == null) {
			if (other.unitDescription != null)
				return false;
		} else if (!unitDescription.equals(other.unitDescription))
			return false;
		if (unitNumber == null) {
			if (other.unitNumber != null)
				return false;
		} else if (!unitNumber.equals(other.unitNumber))
			return false;
		if (unitRank == null) {
			if (other.unitRank != null)
				return false;
		} else if (!unitRank.equals(other.unitRank))
			return false;
		if (vehicleModelCode == null) {
			if (other.vehicleModelCode != null)
				return false;
		} else if (!vehicleModelCode.equals(other.vehicleModelCode))
			return false;
		return true;
	}
	

}
