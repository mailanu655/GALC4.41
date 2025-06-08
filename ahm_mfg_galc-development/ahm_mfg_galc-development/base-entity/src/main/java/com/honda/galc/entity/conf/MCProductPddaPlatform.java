package com.honda.galc.entity.conf;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.vios.dto.PddaPlatformDto;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>MCProductPddaPlatform</code> is an entity class for MC_PRODUCT_PDDA_PLATFORM_TBX table.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Hemant Kumar
 * @date 15th Feb, 2018
 */

@Entity
@Table(name = "MC_PRODUCT_PDDA_PLATFORM_TBX")
public class MCProductPddaPlatform extends AuditEntry {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PRODUCT_ID")
	private String productId;

	@Column(name = "PLANT_LOC_CODE", length=1)
	private String plantLocCode;
	
	@Column(name = "DEPT_CODE", length=2)
	private String deptCode;
	
	@Column(name="MODEL_YEAR_DATE", precision=5, scale=1)
	private BigDecimal modelYearDate;
	
	@Column(name="PROD_SCH_QTY", precision=5, scale=1)
	private BigDecimal prodSchQty;
	
	@Column(name = "PROD_ASM_LINE_NO", length=1)
	private String prodAsmLineNo;
	
	@Column(name = "VEHICLE_MODEL_CODE", length=1)
	private String vehicleModelCode;
	
	public MCProductPddaPlatform() {
		super();
	}
	
	public MCProductPddaPlatform(String productId, PddaPlatformDto pddaPlatform) {
		super();
		if (pddaPlatform != null) {
			this.productId = productId;
			this.plantLocCode = pddaPlatform.getPlantLocCode();
			this.deptCode = pddaPlatform.getDeptCode();
			this.modelYearDate = new BigDecimal(Float.toString(pddaPlatform.getModelYearDate()));
			this.prodSchQty = new BigDecimal(Float.toString(pddaPlatform.getProdSchQty()));
			this.prodAsmLineNo = pddaPlatform.getProdAsmLineNo();
			this.vehicleModelCode = pddaPlatform.getVehicleModelCode();
		}
	}
	
	public String getProductId() {
		return StringUtils.trimToEmpty(productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deptCode == null) ? 0 : deptCode.hashCode());
		result = prime * result + ((modelYearDate == null) ? 0 : modelYearDate.hashCode());
		result = prime * result + ((plantLocCode == null) ? 0 : plantLocCode.hashCode());
		result = prime * result + ((prodAsmLineNo == null) ? 0 : prodAsmLineNo.hashCode());
		result = prime * result + ((prodSchQty == null) ? 0 : prodSchQty.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((vehicleModelCode == null) ? 0 : vehicleModelCode.hashCode());
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
		MCProductPddaPlatform other = (MCProductPddaPlatform) obj;
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
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (vehicleModelCode == null) {
			if (other.vehicleModelCode != null)
				return false;
		} else if (!vehicleModelCode.equals(other.vehicleModelCode))
			return false;
		return true;
	}

	public PddaPlatformDto convertToPddaPlatformDto() {
		PddaPlatformDto pddaPlatform = new PddaPlatformDto();
		pddaPlatform.setPlantLocCode(getPlantLocCode());
		pddaPlatform.setDeptCode(getDeptCode());
		pddaPlatform.setModelYearDate((getModelYearDate()!=null) ? getModelYearDate().floatValue() : 0.0f);
		pddaPlatform.setProdSchQty((getProdSchQty()!=null) ? getProdSchQty().floatValue() : 0.0f);
		pddaPlatform.setProdAsmLineNo(getProdAsmLineNo());
		pddaPlatform.setVehicleModelCode(getVehicleModelCode());
		return pddaPlatform;
	}
	
	public Object getId() {
		return productId;
	}
}

