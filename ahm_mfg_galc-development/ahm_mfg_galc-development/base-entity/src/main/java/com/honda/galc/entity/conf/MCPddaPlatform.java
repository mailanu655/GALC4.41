package com.honda.galc.entity.conf;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.entity.AuditEntry;

/**
 * The persistent class for the MC_PDDA_PLATFORM_TBX database table.
 * 
 */
@Entity
@Table(name="MC_PDDA_PLATFORM_TBX")
public class MCPddaPlatform extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PDDA_PLATFORM_ID")
	private int pddaPlatformId;

	@Column(name="APPROVED")
	private Timestamp approved;

	@Column(name="APPROVER_NO", length=11)
	private String approverNo;

	@Column(name="ASM_PROC_NO", nullable=false, length=5)
	private String asmProcNo;

	@Column(name="DEPRECATED")
	private Timestamp deprecated;

	@Column(name="DEPRECATED_REV_ID")
	private long deprecatedRevId;

	@Column(name="DEPRECATER_NO", length=11)
	private String deprecaterNo;

	@Column(name="DEPT_CODE", nullable=false, length=2)
	private String deptCode;

	@Column(name="MODEL_YEAR_DATE", nullable=false, precision=5, scale=1)
	private BigDecimal modelYearDate;

	@Column(name="PLANT_LOC_CODE", nullable=false, length=1)
	private String plantLocCode;

	@Column(name="PROD_ASM_LINE_NO", nullable=false, length=1)
	private String prodAsmLineNo;

	@Column(name="PROD_SCH_QTY", nullable=false, precision=5, scale=1)
	private BigDecimal prodSchQty;

	@Column(name="REV_ID", nullable=false)
	private long revId;

	@Column(name="VEHICLE_MODEL_CODE", nullable=false, length=1)
	private String vehicleModelCode;

    public MCPddaPlatform() {
    }

	public Integer getPddaPlatformId() {
		return this.pddaPlatformId;
	}

	public void setPddaPlatformId(int pddaPlatformId) {
		this.pddaPlatformId = pddaPlatformId;
	}

	public Timestamp getApproved() {
		return this.approved;
	}

	public void setApproved(Timestamp approved) {
		this.approved = approved;
	}

	public String getApproverNo() {
		return StringUtils.trim(this.approverNo);
	}

	public void setApproverNo(String approverNo) {
		this.approverNo = approverNo;
	}

	public String getAsmProcessNo() {
		return StringUtils.trim(this.asmProcNo);
	}

	public void setAsmProcessNo(String asmProcNo) {
		this.asmProcNo = asmProcNo;
	}

	public Timestamp getDeprecated() {
		return this.deprecated;
	}

	public void setDeprecated(Timestamp deprecated) {
		this.deprecated = deprecated;
	}

	public long getDeprecatedRevId() {
		return this.deprecatedRevId;
	}

	public void setDeprecatedRevId(long deprecatedRevId) {
		this.deprecatedRevId = deprecatedRevId;
	}

	public String getDeprecaterNo() {
		return StringUtils.trim(this.deprecaterNo);
	}

	public void setDeprecaterNo(String deprecaterNo) {
		this.deprecaterNo = deprecaterNo;
	}

	public String getDeptCode() {
		return StringUtils.trim(this.deptCode);
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public BigDecimal getModelYearDate() {
		return this.modelYearDate;
	}

	public void setModelYearDate(BigDecimal modelYearDate) {
		this.modelYearDate = modelYearDate;
	}

	public String getPlantLocCode() {
		return StringUtils.trim(this.plantLocCode);
	}

	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
	}

	public String getProductAsmLineNo() {
		return StringUtils.trim(this.prodAsmLineNo);
	}

	public void setProductAsmLineNo(String prodAsmLineNo) {
		this.prodAsmLineNo = prodAsmLineNo;
	}

	public BigDecimal getProductScheduleQty() {
		return this.prodSchQty;
	}

	public void setProductScheduleQty(BigDecimal prodSchQty) {
		this.prodSchQty = prodSchQty;
	}

	public long getRevId() {
		return this.revId;
	}

	public void setRevId(long revId) {
		this.revId = revId;
	}

	public String getVehicleModelCode() {
		return StringUtils.trim(this.vehicleModelCode);
	}

	public void setVehicleModelCode(String vehicleModelCode) {
		this.vehicleModelCode = vehicleModelCode;
	}

	public Object getId() {
		return this.pddaPlatformId;
	}

	@Override
	public String toString() {
		return toString(getId(), getPlantLocCode(), getDeptCode(), getModelYearDate(), 
				getProductScheduleQty(), getProductAsmLineNo(), getVehicleModelCode(), 
				getAsmProcessNo(), getApproverNo(), 
				getApproved(), getDeprecatedRevId(), getDeprecaterNo(), getDeprecated());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((approved == null) ? 0 : approved.hashCode());
		result = prime * result
				+ ((approverNo == null) ? 0 : approverNo.hashCode());
		result = prime * result
				+ ((asmProcNo == null) ? 0 : asmProcNo.hashCode());
		result = prime * result
				+ ((deprecated == null) ? 0 : deprecated.hashCode());
		result = prime * result
				+ (int) (deprecatedRevId ^ (deprecatedRevId >>> 32));
		result = prime * result
				+ ((deprecaterNo == null) ? 0 : deprecaterNo.hashCode());
		result = prime * result
				+ ((deptCode == null) ? 0 : deptCode.hashCode());
		result = prime * result
				+ ((modelYearDate == null) ? 0 : modelYearDate.hashCode());
		result = prime * result + pddaPlatformId;
		result = prime * result
				+ ((plantLocCode == null) ? 0 : plantLocCode.hashCode());
		result = prime * result
				+ ((prodAsmLineNo == null) ? 0 : prodAsmLineNo.hashCode());
		result = prime * result
				+ ((prodSchQty == null) ? 0 : prodSchQty.hashCode());
		result = prime * result + (int) (revId ^ (revId >>> 32));
		result = prime
				* result
				+ ((vehicleModelCode == null) ? 0 : vehicleModelCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MCPddaPlatform other = (MCPddaPlatform) obj;
		if (approved == null) {
			if (other.approved != null) {
				return false;
			}
		} else if (!approved.equals(other.approved)) {
			return false;
		}
		if (approverNo == null) {
			if (other.approverNo != null) {
				return false;
			}
		} else if (!approverNo.equals(other.approverNo)) {
			return false;
		}
		if (asmProcNo == null) {
			if (other.asmProcNo != null) {
				return false;
			}
		} else if (!asmProcNo.equals(other.asmProcNo)) {
			return false;
		}
		if (deprecated == null) {
			if (other.deprecated != null) {
				return false;
			}
		} else if (!deprecated.equals(other.deprecated)) {
			return false;
		}
		if (deprecatedRevId != other.deprecatedRevId) {
			return false;
		}
		if (deprecaterNo == null) {
			if (other.deprecaterNo != null) {
				return false;
			}
		} else if (!deprecaterNo.equals(other.deprecaterNo)) {
			return false;
		}
		if (deptCode == null) {
			if (other.deptCode != null) {
				return false;
			}
		} else if (!deptCode.equals(other.deptCode)) {
			return false;
		}
		if (modelYearDate == null) {
			if (other.modelYearDate != null) {
				return false;
			}
		} else if (!modelYearDate.equals(other.modelYearDate)) {
			return false;
		}
		if (pddaPlatformId != other.pddaPlatformId) {
			return false;
		}
		if (plantLocCode == null) {
			if (other.plantLocCode != null) {
				return false;
			}
		} else if (!plantLocCode.equals(other.plantLocCode)) {
			return false;
		}
		if (prodAsmLineNo == null) {
			if (other.prodAsmLineNo != null) {
				return false;
			}
		} else if (!prodAsmLineNo.equals(other.prodAsmLineNo)) {
			return false;
		}
		if (prodSchQty == null) {
			if (other.prodSchQty != null) {
				return false;
			}
		} else if (!prodSchQty.equals(other.prodSchQty)) {
			return false;
		}
		if (revId != other.revId) {
			return false;
		}
		if (vehicleModelCode == null) {
			if (other.vehicleModelCode != null) {
				return false;
			}
		} else if (!vehicleModelCode.equals(other.vehicleModelCode)) {
			return false;
		}
		return true;
	}

}
