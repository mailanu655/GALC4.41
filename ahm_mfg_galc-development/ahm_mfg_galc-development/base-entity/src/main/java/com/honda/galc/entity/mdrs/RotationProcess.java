package com.honda.galc.entity.mdrs;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * Apr 09, 2014
 */
@Entity
@Table(name="TBLROTATION_PROCESS", schema="VIOS")
public class RotationProcess extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RotationProcessId id;

	@Column(name="ASM_PROC_NAME", nullable=false, length=35)
	private String asmProcName;

	@Column(name="ASM_PROC_NO", nullable=false, length=5)
	private String asmProcNo;

	@Column(name="BODY_LOC_NO", nullable=false, length=4)
	private String bodyLocNo;

	@Column(name="DAYSFLAG", nullable=false, length=1)
	private String daysFlag;

	@Column(name="EFF_BEG_DATE", nullable=false)
	private Date effBegDate;

	@Column(name="EFF_END_DATE", nullable=false)
	private Date effEndDate;

	@Column(name="MODEL_YEAR_DATE", nullable=false, precision=5, scale=1)
	private BigDecimal modelYearDate;

	@Column(name="PROCESS_STATUS", nullable=false)
	private short processStatus;

	@Column(name="PROD_SCH_QTY", nullable=false)
	private int prodSchQty;

	@Column(name="ROTATION_PROCESS_ID", nullable=false)
	private int rotationProcessId;

	@Column(name="VEHICLE_MODEL_CODE", nullable=false, length=1)
	private String vehicleModelCode;

	@Column(name="VMC_DESCRIPTION", nullable=false, length=35)
	private String vmcDescription;

    public RotationProcess() {
    }

	public RotationProcessId getId() {
		return this.id;
	}

	public void setId(RotationProcessId id) {
		this.id = id;
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

	public String getBodyLocNo() {
		return StringUtils.trim(this.bodyLocNo);
	}

	public void setBodyLocNo(String bodyLocNo) {
		this.bodyLocNo = bodyLocNo;
	}


	public String getDaysFlag() {
		return StringUtils.trim(this.daysFlag);
	}

	public void setDaysFlag(String daysflag) {
		this.daysFlag = daysflag;
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

	public BigDecimal getModelYearDate() {
		return this.modelYearDate;
	}

	public void setModelYearDate(BigDecimal modelYearDate) {
		this.modelYearDate = modelYearDate;
	}

	public short getProcessStatus() {
		return this.processStatus;
	}

	public void setProcessStatus(short processStatus) {
		this.processStatus = processStatus;
	}

	public int getProdSchQty() {
		return this.prodSchQty;
	}

	public void setProdSchQty(int prodSchQty) {
		this.prodSchQty = prodSchQty;
	}

	public int getRotationProcessId() {
		return this.rotationProcessId;
	}

	public void setRotationProcessId(int rotationProcessId) {
		this.rotationProcessId = rotationProcessId;
	}

	public String getVehicleModelCode() {
		return StringUtils.trim(this.vehicleModelCode);
	}

	public void setVehicleModelCode(String vehicleModelCode) {
		this.vehicleModelCode = vehicleModelCode;
	}

	public String getVmcDescription() {
		return StringUtils.trim(this.vmcDescription);
	}

	public void setVmcDescription(String vmcDescription) {
		this.vmcDescription = vmcDescription;
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
				+ ((bodyLocNo == null) ? 0 : bodyLocNo.hashCode());
		result = prime * result
				+ ((daysFlag == null) ? 0 : daysFlag.hashCode());
		result = prime * result
				+ ((effBegDate == null) ? 0 : effBegDate.hashCode());
		result = prime * result
				+ ((effEndDate == null) ? 0 : effEndDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((modelYearDate == null) ? 0 : modelYearDate.hashCode());
		result = prime * result + processStatus;
		result = prime * result + prodSchQty;
		result = prime * result + rotationProcessId;
		result = prime
				* result
				+ ((vehicleModelCode == null) ? 0 : vehicleModelCode.hashCode());
		result = prime * result
				+ ((vmcDescription == null) ? 0 : vmcDescription.hashCode());
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
		if (!(obj instanceof RotationProcess)) {
			return false;
		}
		RotationProcess other = (RotationProcess) obj;
		if (asmProcName == null) {
			if (other.asmProcName != null) {
				return false;
			}
		} else if (!asmProcName.equals(other.asmProcName)) {
			return false;
		}
		if (asmProcNo == null) {
			if (other.asmProcNo != null) {
				return false;
			}
		} else if (!asmProcNo.equals(other.asmProcNo)) {
			return false;
		}
		if (bodyLocNo == null) {
			if (other.bodyLocNo != null) {
				return false;
			}
		} else if (!bodyLocNo.equals(other.bodyLocNo)) {
			return false;
		}
		if (daysFlag == null) {
			if (other.daysFlag != null) {
				return false;
			}
		} else if (!daysFlag.equals(other.daysFlag)) {
			return false;
		}
		if (effBegDate == null) {
			if (other.effBegDate != null) {
				return false;
			}
		} else if (!effBegDate.equals(other.effBegDate)) {
			return false;
		}
		if (effEndDate == null) {
			if (other.effEndDate != null) {
				return false;
			}
		} else if (!effEndDate.equals(other.effEndDate)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (modelYearDate == null) {
			if (other.modelYearDate != null) {
				return false;
			}
		} else if (!modelYearDate.equals(other.modelYearDate)) {
			return false;
		}
		if (processStatus != other.processStatus) {
			return false;
		}
		if (prodSchQty != other.prodSchQty) {
			return false;
		}
		if (rotationProcessId != other.rotationProcessId) {
			return false;
		}
		if (vehicleModelCode == null) {
			if (other.vehicleModelCode != null) {
				return false;
			}
		} else if (!vehicleModelCode.equals(other.vehicleModelCode)) {
			return false;
		}
		if (vmcDescription == null) {
			if (other.vmcDescription != null) {
				return false;
			}
		} else if (!vmcDescription.equals(other.vmcDescription)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return toString(getId().getRotateRecNo(), getId().getProcessId(), getAsmProcName(), getAsmProcNo(), 
				getBodyLocNo(), getDaysFlag(), getId().getDeptCode(), getEffBegDate(), getEffEndDate(), getId().getExtractDate(), 
				getModelYearDate(), getId().getPlantLocCode(), getProcessStatus(), getProdSchQty(),
				getRotationProcessId(), getVehicleModelCode(), getVmcDescription() );
	}
	
}