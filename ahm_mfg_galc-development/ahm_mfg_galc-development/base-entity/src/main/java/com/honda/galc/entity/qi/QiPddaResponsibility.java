package com.honda.galc.entity.qi;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;

/**
 * 
 * <h3>QiPddaResponsibility Class description</h3>
 * <p> QiPddaResponsibility description </p>
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
 *        Oct 06, 2016
 * 
 */
@Entity
@Table(name = "QI_PDDA_RESPONSIBILITY_TBX")
public class QiPddaResponsibility extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PDDA_RESPONSIBILITY_ID")
	@Auditable(isPartOfPrimaryKey=true)
	private Integer pddaResponsibilityId;
	
	@Auditable
	@Column(name = "MODEL_YEAR")
	private BigDecimal modelYear; 
	
	@Auditable
	@Column(name = "VEHICLE_MODEL_CODE")
	private String vehicleModelCode;
	
	@Auditable
	@Column(name = "PROCESS_NUMBER")
	private String processNumber;
	
	@Auditable
	@Column(name = "PROCESS_NAME")
	private String processName;
	
	@Auditable
	@Column(name = "UNIT_NUMBER")
	private String unitNumber;
	
	@Auditable
	@Column(name = "UNIT_DESC")
	private String unitDescription;
	
	@Auditable
	@Column(name = "PROD_SCH_QTY")
	private BigDecimal prodSchQty; 
	
	@Auditable
	@Column(name = "UNIT_RANK")
	private String unitRank;
	
	@Auditable
	@Column(name = "SHORT_PLANT_CODE")
	private String shortPlantCode;
	
	@Auditable
	@Column(name = "PCL_TO_QICS_SEQ_KEY")
	private Integer pclToQicsSeqKey; 

	@Auditable
	@Column(name = "RESP_COMPANY")
	private String responsibleCompany;
	
	@Auditable
	@Column(name = "RESP_SITE")
	private String responsibleSite;
	
	@Auditable
	@Column(name = "RESP_PLANT")
	private String responsiblePlant;

	@Auditable
	@Column(name = "RESP_DEPT")
	private String responsibleDept;
	
	@Auditable
	@Column(name = "ENTRY_SITE")
	private String entrySite;
	
	@Auditable
	@Column(name = "ENTRY_PLANT")
	private String entryPlant;

	@Auditable
	@Column(name = "PRODUCT_TYPE")
	private String productType;
	
	@Auditable
	@Column(name = "ROW_TYPE")
	private String rowType;
	
	@Auditable
	@Column(name = "RESP_LEVEL1")
	private String responsibleLevel1;
	
	@Auditable
	@Column(name = "RESP_LEVEL2")
	private String responsibleLevel2;
	
	@Auditable
	@Column(name = "PDDA_LINE")
	private String pddaLine;
	
	@Auditable
	@Column(name = "RESP_LEVEL1_DESC")
	private String responsibleLevel1Desc;
	
	@Auditable
	@Column(name = "PDDA_DEPT_CODE")
	private String pddaDeptCode;
	
	@Auditable
	@Column(name = "BASE_PART_NO")
	private String basePartNumber;

	public Integer getPddaResponsibilityId() {
		return pddaResponsibilityId;
	}

	public void setPddaResponsibilityId(Integer pddaResponsibilityId) {
		this.pddaResponsibilityId = pddaResponsibilityId;
	}

	public String getVehicleModelCode() {
		return StringUtils.trimToEmpty(this.vehicleModelCode);
	}

	public void setVehicleModelCode(String vehicleModelCode) {
		this.vehicleModelCode = vehicleModelCode;
	}

	public String getProcessNumber() {
		return StringUtils.trimToEmpty(this.processNumber);
	}

	public void setProcessNumber(String processNumber) {
		this.processNumber = processNumber;
	}

	public String getProcessName() {
		return StringUtils.trimToEmpty(this.processName);
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getUnitNumber() {
		return StringUtils.trimToEmpty(this.unitNumber);
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	public String getUnitDescription() {
		return StringUtils.trimToEmpty(this.unitDescription);
	}

	public void setUnitDescription(String unitDescription) {
		this.unitDescription = unitDescription;
	}


	public String getUnitRank() {
		return StringUtils.trimToEmpty(this.unitRank);
	}

	public void setUnitRank(String unitRank) {
		this.unitRank = unitRank;
	}

	public String getShortPlantCode() {
		return StringUtils.trimToEmpty(this.shortPlantCode);
	}

	public void setShortPlantCode(String shortPlantCode) {
		this.shortPlantCode = shortPlantCode;
	}


	public String getResponsibleCompany() {
		return StringUtils.trimToEmpty(responsibleCompany);
	}

	public void setResponsibleCompany(String responsibleCompany) {
		this.responsibleCompany = responsibleCompany;
	}

	public String getResponsibleSite() {
		return StringUtils.trimToEmpty(responsibleSite);
	}

	public void setResponsibleSite(String responsibleSite) {
		this.responsibleSite = responsibleSite;
	}

	public String getResponsiblePlant() {
		return StringUtils.trimToEmpty(responsiblePlant);
	}

	public void setResponsiblePlant(String responsiblePlant) {
		this.responsiblePlant = responsiblePlant;
	}

	public String getResponsibleDept() {
		return StringUtils.trimToEmpty(responsibleDept);
	}

	public void setResponsibleDept(String responsibleDept) {
		this.responsibleDept = responsibleDept;
	}

	public String getEntrySite() {
		return StringUtils.trimToEmpty(entrySite);
	}

	public void setEntrySite(String entrySite) {
		this.entrySite = entrySite;
	}

	public String getEntryPlant() {
		return StringUtils.trimToEmpty(entryPlant);
	}

	public void setEntryPlant(String entryPlant) {
		this.entryPlant = entryPlant;
	}

	public String getProductType() {
		return StringUtils.trimToEmpty(productType);
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getRowType() {
		return StringUtils.trimToEmpty(rowType);
	}

	public void setRowType(String rowType) {
		this.rowType = rowType;
	}

	public String getResponsibleLevel1() {
		return StringUtils.trimToEmpty(responsibleLevel1);
	}

	public void setResponsibleLevel1(String responsibleLevel1) {
		this.responsibleLevel1 = responsibleLevel1;
	}

	public String getResponsibleLevel2() {
		return StringUtils.trimToEmpty(responsibleLevel2);
	}

	public void setResponsibleLevel2(String responsibleLevel2) {
		this.responsibleLevel2 = responsibleLevel2;
	}

	public String getPddaLine() {
		return StringUtils.trimToEmpty(pddaLine);
	}

	public void setPddaLine(String pddaLine) {
		this.pddaLine = pddaLine;
	}

	public String getResponsibleLevel1Desc() {
		return StringUtils.trimToEmpty(responsibleLevel1Desc);
	}

	public void setResponsibleLevel1Desc(String responsibleLevel1Desc) {
		this.responsibleLevel1Desc = responsibleLevel1Desc;
	}


	public String getPddaDeptCode() {
		return StringUtils.trimToEmpty(pddaDeptCode);
	}

	public void setPddaDeptCode(String pddaDeptCode) {
		this.pddaDeptCode = pddaDeptCode;
	}

	public String getBasePartNumber() {
		return StringUtils.trimToEmpty(basePartNumber);
	}

	public void setBasePartNumber(String basePartNumber) {
		this.basePartNumber = basePartNumber;
	}

	public BigDecimal getModelYear() {
		return modelYear;
	}

	public void setModelYear(BigDecimal modelYear) {
		this.modelYear = modelYear;
	}

	public BigDecimal getProdSchQty() {
		return prodSchQty;
	}

	public void setProdSchQty(BigDecimal prodSchQty) {
		this.prodSchQty = prodSchQty;
	}

	public Integer getPclToQicsSeqKey() {
		return pclToQicsSeqKey;
	}

	public void setPclToQicsSeqKey(Integer pclToQicsSeqKey) {
		this.pclToQicsSeqKey = pclToQicsSeqKey;
	}
	
	public Object getId() {
		return getPddaResponsibilityId();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((basePartNumber == null) ? 0 : basePartNumber.hashCode());
		result = prime * result
				+ ((entryPlant == null) ? 0 : entryPlant.hashCode());
		result = prime * result
				+ ((entrySite == null) ? 0 : entrySite.hashCode());
		result = prime * result
				+ ((modelYear == null) ? 0 : modelYear.hashCode());
		result = prime * result
				+ ((pclToQicsSeqKey == null) ? 0 : pclToQicsSeqKey.hashCode());
		result = prime * result
				+ ((pddaDeptCode == null) ? 0 : pddaDeptCode.hashCode());
		result = prime * result
				+ ((pddaLine == null) ? 0 : pddaLine.hashCode());
		result = prime
				* result
				+ ((pddaResponsibilityId == null) ? 0 : pddaResponsibilityId
						.hashCode());
		result = prime * result
				+ ((processName == null) ? 0 : processName.hashCode());
		result = prime * result
				+ ((processNumber == null) ? 0 : processNumber.hashCode());
		result = prime * result
				+ ((prodSchQty == null) ? 0 : prodSchQty.hashCode());
		result = prime * result
				+ ((productType == null) ? 0 : productType.hashCode());
		result = prime
				* result
				+ ((responsibleCompany == null) ? 0 : responsibleCompany
						.hashCode());
		result = prime * result
				+ ((responsibleDept == null) ? 0 : responsibleDept.hashCode());
		result = prime
				* result
				+ ((responsibleLevel1 == null) ? 0 : responsibleLevel1
						.hashCode());
		result = prime
				* result
				+ ((responsibleLevel1Desc == null) ? 0 : responsibleLevel1Desc
						.hashCode());
		result = prime
				* result
				+ ((responsibleLevel2 == null) ? 0 : responsibleLevel2
						.hashCode());
		result = prime
				* result
				+ ((responsiblePlant == null) ? 0 : responsiblePlant.hashCode());
		result = prime * result
				+ ((responsibleSite == null) ? 0 : responsibleSite.hashCode());
		result = prime * result + ((rowType == null) ? 0 : rowType.hashCode());
		result = prime * result
				+ ((shortPlantCode == null) ? 0 : shortPlantCode.hashCode());
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
		QiPddaResponsibility other = (QiPddaResponsibility) obj;
		if (basePartNumber == null) {
			if (other.basePartNumber != null)
				return false;
		} else if (!basePartNumber.equals(other.basePartNumber))
			return false;
		if (entryPlant == null) {
			if (other.entryPlant != null)
				return false;
		} else if (!entryPlant.equals(other.entryPlant))
			return false;
		if (entrySite == null) {
			if (other.entrySite != null)
				return false;
		} else if (!entrySite.equals(other.entrySite))
			return false;
		if (modelYear == null) {
			if (other.modelYear != null)
				return false;
		} else if (!modelYear.equals(other.modelYear))
			return false;
		if (pclToQicsSeqKey == null) {
			if (other.pclToQicsSeqKey != null)
				return false;
		} else if (!pclToQicsSeqKey.equals(other.pclToQicsSeqKey))
			return false;
		if (pddaDeptCode == null) {
			if (other.pddaDeptCode != null)
				return false;
		} else if (!pddaDeptCode.equals(other.pddaDeptCode))
			return false;
		if (pddaLine == null) {
			if (other.pddaLine != null)
				return false;
		} else if (!pddaLine.equals(other.pddaLine))
			return false;
		if (pddaResponsibilityId == null) {
			if (other.pddaResponsibilityId != null)
				return false;
		} else if (!pddaResponsibilityId.equals(other.pddaResponsibilityId))
			return false;
		if (processName == null) {
			if (other.processName != null)
				return false;
		} else if (!processName.equals(other.processName))
			return false;
		if (processNumber == null) {
			if (other.processNumber != null)
				return false;
		} else if (!processNumber.equals(other.processNumber))
			return false;
		if (prodSchQty == null) {
			if (other.prodSchQty != null)
				return false;
		} else if (!prodSchQty.equals(other.prodSchQty))
			return false;
		if (productType == null) {
			if (other.productType != null)
				return false;
		} else if (!productType.equals(other.productType))
			return false;
		if (responsibleCompany == null) {
			if (other.responsibleCompany != null)
				return false;
		} else if (!responsibleCompany.equals(other.responsibleCompany))
			return false;
		if (responsibleDept == null) {
			if (other.responsibleDept != null)
				return false;
		} else if (!responsibleDept.equals(other.responsibleDept))
			return false;
		if (responsibleLevel1 == null) {
			if (other.responsibleLevel1 != null)
				return false;
		} else if (!responsibleLevel1.equals(other.responsibleLevel1))
			return false;
		if (responsibleLevel1Desc == null) {
			if (other.responsibleLevel1Desc != null)
				return false;
		} else if (!responsibleLevel1Desc.equals(other.responsibleLevel1Desc))
			return false;
		if (responsibleLevel2 == null) {
			if (other.responsibleLevel2 != null)
				return false;
		} else if (!responsibleLevel2.equals(other.responsibleLevel2))
			return false;
		if (responsiblePlant == null) {
			if (other.responsiblePlant != null)
				return false;
		} else if (!responsiblePlant.equals(other.responsiblePlant))
			return false;
		if (responsibleSite == null) {
			if (other.responsibleSite != null)
				return false;
		} else if (!responsibleSite.equals(other.responsibleSite))
			return false;
		if (rowType == null) {
			if (other.rowType != null)
				return false;
		} else if (!rowType.equals(other.rowType))
			return false;
		if (shortPlantCode == null) {
			if (other.shortPlantCode != null)
				return false;
		} else if (!shortPlantCode.equals(other.shortPlantCode))
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
