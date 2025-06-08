package com.honda.galc.entity.conf;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.ExcelSheetColumn;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.vios.dto.PddaPlatformDto;

/**
 * <h3>MCViosMasterPlatform Class description</h3>
 * <p>
 * Entity class for galadm.MC_VIOS_MASTER_PLATFORM_TBX
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
 * @author Hemant Kumar<br>
 *         Aug 28, 2018
 */
@Entity
@Table(name = "MC_VIOS_MASTER_PLATFORM_TBX")
public class MCViosMasterPlatform extends AuditEntry {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "VIOS_PLATFORM_ID")
	private String viosPlatformId;

	@Column(name = "PLANT_LOC_CODE", nullable = false, length = 1)
	@ExcelSheetColumn(name = "Plant")
	private String plantLocCode;

	@Column(name = "DEPT_CODE", nullable = false, length = 2)
	@ExcelSheetColumn(name = "Department")
	private String deptCode;

	@Column(name = "MODEL_YEAR_DATE", nullable = false, precision = 5, scale = 1)
	@ExcelSheetColumn(name = "Model_Year")
	private BigDecimal modelYearDate;

	@Column(name = "PROD_SCH_QTY", nullable = false, precision = 5, scale = 1)
	@ExcelSheetColumn(name = "Production_Rate")
	private BigDecimal prodSchQty;

	@Column(name = "PROD_ASM_LINE_NO", nullable = false, length = 1)
	@ExcelSheetColumn(name = "Line_No")
	private String prodAsmLineNo;

	@Column(name = "VEHICLE_MODEL_CODE", nullable = false, length = 1)
	@ExcelSheetColumn(name = "Vehicle_Model_Code")
	private String vehicleModelCode;

	@Column(name = "ACTIVE")
	@ExcelSheetColumn(name = "Status")
	private short active;

	@Column(name = "DISCARD_YEAR")
	private short discardYear;
	
	@Column(name = "USER_ID")
    private String userId;
	
	public String getUserId() {
		return StringUtils.trim(userId);
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public MCViosMasterPlatform() {
		super();
	}

	public MCViosMasterPlatform(String plantLocCode, String deptCode, BigDecimal modelYearDate, BigDecimal prodSchQty,
			String prodAsmLineNo, String vehicleModelCode) {
		super();
		this.plantLocCode = plantLocCode;
		this.deptCode = deptCode;
		this.modelYearDate = modelYearDate;
		this.prodSchQty = prodSchQty;
		this.prodAsmLineNo = prodAsmLineNo;
		this.vehicleModelCode = vehicleModelCode;
		this.viosPlatformId = getGeneratedId();
		setActive(true);
		setDiscardYear(true);
		

	}

	public MCViosMasterPlatform(String plantLocCode, String deptCode, BigDecimal modelYearDate, BigDecimal prodSchQty,
			String prodAsmLineNo, String vehicleModelCode, boolean isActive, boolean isDiscardYear) {
		super();
		this.plantLocCode = plantLocCode;
		this.deptCode = deptCode;
		this.modelYearDate = modelYearDate;
		this.prodSchQty = prodSchQty;
		this.prodAsmLineNo = prodAsmLineNo;
		this.vehicleModelCode = vehicleModelCode;
		this.viosPlatformId = getGeneratedId();
		setActive(isActive);
		setDiscardYear(isDiscardYear);
	}

	public MCViosMasterPlatform(PddaPlatformDto platformDto) {
		super();
		this.plantLocCode = platformDto.getPlantLocCode();
		this.deptCode = platformDto.getDeptCode();
		this.modelYearDate = new BigDecimal(platformDto.getModelYearDate());
		this.prodSchQty = new BigDecimal(platformDto.getProdSchQty());
		this.prodAsmLineNo = platformDto.getProdAsmLineNo();
		this.vehicleModelCode = platformDto.getVehicleModelCode();
		this.viosPlatformId = getGeneratedId();
		setActive(true);
		setDiscardYear(false);

	}

	public String getViosPlatformId() {
		return StringUtils.trimToEmpty(viosPlatformId);
	}

	public void setViosPlatformId(String viosPlatformId) {
		this.viosPlatformId = viosPlatformId;
	}

	public String getPlantLocCode() {
		return StringUtils.trimToEmpty(plantLocCode);
	}

	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
	}

	public String getDeptCode() {
		return StringUtils.trimToEmpty(deptCode);
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public BigDecimal getModelYearDate() {
		return modelYearDate;
	}

	public void setModelYearDate(BigDecimal modelYearDate) {
		this.modelYearDate = modelYearDate;
	}

	public BigDecimal getProdSchQty() {
		return prodSchQty;
	}

	public void setProdSchQty(BigDecimal prodSchQty) {
		this.prodSchQty = prodSchQty;
	}

	public String getProdAsmLineNo() {
		return StringUtils.trimToEmpty(prodAsmLineNo);
	}

	public void setProdAsmLineNo(String prodAsmLineNo) {
		this.prodAsmLineNo = prodAsmLineNo;
	}

	public String getVehicleModelCode() {
		return StringUtils.trimToEmpty(vehicleModelCode);
	}

	public void setVehicleModelCode(String vehicleModelCode) {
		this.vehicleModelCode = vehicleModelCode;
	}

	public short getActiveValue() {
		return active;
	}

	public boolean isActive() {
		return this.active == (short) 1;
	}

	public void setActive(short active) {
		this.active = active;
	}
	
	public boolean isDiscardYear() {
        return this.discardYear == (short) 1;
    }
	
	public void setActive(boolean active) {
		this.active = (short) (active ? 1 : 0);
	}

	public String getStatus() {
		return this.active == 1 ? "Active" : "Inactive";
	}

	
	public void setDiscardYear(boolean discardYear) {
		this.discardYear = (short) (discardYear ? 1 : 0);
	}
	
	public short getDiscardYear() {
		return discardYear;
	}

	public void setDiscardYear(short discardYear) {
		this.discardYear = discardYear;
	}

	
	public String getDiscardYearS() {
		return this.discardYear == 1 ? "Yes" : "No";
	}


	public String getGeneratedId() {
		DecimalFormat formatter = new DecimalFormat("0000.0");
		return StringUtils.trimToEmpty(
				this.plantLocCode + this.deptCode + formatter.format(this.prodSchQty.floatValue()) + this.prodAsmLineNo
						+ this.vehicleModelCode + formatter.format(this.modelYearDate.floatValue()));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result;
		result = prime * result + discardYear;
		
		result = prime * result + ((deptCode == null) ? 0 : deptCode.hashCode());
		result = prime * result + ((modelYearDate == null) ? 0 : modelYearDate.hashCode());
		result = prime * result + ((plantLocCode == null) ? 0 : plantLocCode.hashCode());
		result = prime * result + ((prodAsmLineNo == null) ? 0 : prodAsmLineNo.hashCode());
		result = prime * result + ((prodSchQty == null) ? 0 : prodSchQty.hashCode());
		result = prime * result + ((vehicleModelCode == null) ? 0 : vehicleModelCode.hashCode());
		result = prime * result + ((viosPlatformId == null) ? 0 : viosPlatformId.hashCode());
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
		MCViosMasterPlatform other = (MCViosMasterPlatform) obj;
		if (active != other.active)
			return false;
		if (discardYear != other.discardYear)
			return false;
		if (deptCode == null) {
			if (other.deptCode != null)
				return false;
		} else if (!deptCode.equals(other.deptCode))
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
		if (vehicleModelCode == null) {
			if (other.vehicleModelCode != null)
				return false;
		} else if (!vehicleModelCode.equals(other.vehicleModelCode))
			return false;
		if (viosPlatformId == null) {
			if (other.viosPlatformId != null)
				return false;
		} else if (!viosPlatformId.equals(other.viosPlatformId))
			return false;
		return true;
	}
	@PrePersist
	private void setId() {
		this.viosPlatformId = getGeneratedId();
	}

	public Object getId() {
		return this.viosPlatformId;
	}

	@Override
	public String toString() {
		return "MCViosMasterPlatform [viosPlatformId=" + getGeneratedId() + ", plantLocCode=" + plantLocCode
				+ ", deptCode=" + deptCode + ", modelYearDate=" + modelYearDate + ", prodSchQty=" + prodSchQty
				+ ", prodAsmLineNo=" + prodAsmLineNo + ", vehicleModelCode=" + vehicleModelCode + ", active=" + active+", Discard year=" + discardYear
				 + "]";
	}
}
