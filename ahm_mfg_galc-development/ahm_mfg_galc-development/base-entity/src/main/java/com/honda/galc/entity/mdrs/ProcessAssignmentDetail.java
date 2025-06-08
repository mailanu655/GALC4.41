package com.honda.galc.entity.mdrs;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;


/**
 * The persistent class for the PROCESS_ASSIGNMENT_DETAIL database table.
 * 
 */
@Entity
@Table(name="PROCESS_ASSIGNMENT_DETAIL", schema="VIOS")
public class ProcessAssignmentDetail extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProcessAssignmentDetailId id;

	@Column(name="BODY_LOC_NO", nullable=false, length=4)
	private String bodyLocNo;

	@Column(name="DAYS_FLAG", nullable=false, length=1)
	private String daysFlag;

	@Column(name="EFF_BEGIN_DATE", nullable=false)
	private Timestamp effBeginDate;

	@Column(name="EFF_END_DATE", nullable=false)
	private Timestamp effEndDate;

	@Column(name="LAST_UPDATED_BY", nullable=false, length=7)
	private String lastUpdatedBy;

	@Column(name="MODEL_YEAR", nullable=false, length=4)
	private String modelYear;

	@Column(name="PDDA_PROCESS_ID")
	private Long pddaProcessId;

	@Column(name="PROCESS_NAME", nullable=false, length=35)
	private String processName;

	@Column(name="PROCESS_NO", nullable=false, length=5)
	private String processNo;

	@Column(name="PRODUCTION_SCHEDULE_QTY", nullable=false)
	private int productionScheduleQty;

	@Column(nullable=false, length=1)
	private String vmc;

	@Column(name="VMC_DESC", nullable=false, length=35)
	private String vmcDesc;

	public ProcessAssignmentDetail() {
	}

	public ProcessAssignmentDetailId getId() {
		return this.id;
	}

	public void setId(ProcessAssignmentDetailId id) {
		this.id = id;
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

	public void setDaysFlag(String daysFlag) {
		this.daysFlag = daysFlag;
	}

	public Timestamp getEffBeginDate() {
		return this.effBeginDate;
	}

	public void setEffBeginDate(Timestamp effBeginDate) {
		this.effBeginDate = effBeginDate;
	}

	public Timestamp getEffEndDate() {
		return this.effEndDate;
	}

	public void setEffEndDate(Timestamp effEndDate) {
		this.effEndDate = effEndDate;
	}

	public String getLastUpdatedBy() {
		return StringUtils.trim(this.lastUpdatedBy);
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getModelYear() {
		return StringUtils.trim(this.modelYear);
	}

	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}

	public Long getPddaProcessId() {
		return this.pddaProcessId;
	}

	public void setPddaProcessId(Long pddaProcessId) {
		this.pddaProcessId = pddaProcessId;
	}

	public String getProcessName() {
		return StringUtils.trim(this.processName);
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getProcessNo() {
		return StringUtils.trim(this.processNo);
	}

	public void setProcessNo(String processNo) {
		this.processNo = processNo;
	}

	public int getProductionScheduleQty() {
		return this.productionScheduleQty;
	}

	public void setProductionScheduleQty(int productionScheduleQty) {
		this.productionScheduleQty = productionScheduleQty;
	}

	public String getVmc() {
		return StringUtils.trim(this.vmc);
	}

	public void setVmc(String vmc) {
		this.vmc = vmc;
	}

	public String getVmcDesc() {
		return StringUtils.trim(this.vmcDesc);
	}

	public void setVmcDesc(String vmcDesc) {
		this.vmcDesc = vmcDesc;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((bodyLocNo == null) ? 0 : bodyLocNo.hashCode());
		result = prime * result
				+ ((daysFlag == null) ? 0 : daysFlag.hashCode());
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((effBeginDate == null) ? 0 : effBeginDate.hashCode());
		result = prime * result
				+ ((effEndDate == null) ? 0 : effEndDate.hashCode());
		result = prime * result
				+ ((lastUpdatedBy == null) ? 0 : lastUpdatedBy.hashCode());
		result = prime * result
				+ ((modelYear == null) ? 0 : modelYear.hashCode());
		result = prime * result
				+ ((pddaProcessId  == null) ? 0 : ((int) (pddaProcessId ^ (pddaProcessId >>> 32))));
		result = prime * result
				+ ((processName == null) ? 0 : processName.hashCode());
		result = prime * result
				+ ((processNo == null) ? 0 : processNo.hashCode());
		result = prime * result + productionScheduleQty ;
		result = prime * result
				+ ((vmc == null) ? 0 : vmc.hashCode());
		result = prime * result
				+ ((vmcDesc == null) ? 0 : vmcDesc.hashCode());
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
		ProcessAssignmentDetail other = (ProcessAssignmentDetail) obj;
		if (bodyLocNo == null) {
			if (other.bodyLocNo != null)
				return false;
		} else if (!bodyLocNo.equals(other.bodyLocNo))
			return false;
		if (daysFlag == null) {
			if (other.daysFlag != null)
				return false;
		} else if (!daysFlag.equals(other.daysFlag))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (effBeginDate == null) {
			if (other.effBeginDate != null)
				return false;
		} else if (!effBeginDate.equals(other.effBeginDate))
			return false;
		if (effEndDate == null) {
			if (other.effEndDate != null)
				return false;
		} else if (!effEndDate.equals(other.effEndDate))
			return false;
		if (lastUpdatedBy == null) {
			if (other.lastUpdatedBy != null)
				return false;
		} else if (!lastUpdatedBy.equals(other.lastUpdatedBy))
			return false;
		if (modelYear == null) {
			if (other.modelYear != null)
				return false;
		} else if (!modelYear.equals(other.modelYear))
			return false;
		if (pddaProcessId == null) {
			if (other.pddaProcessId != null)
				return false;
		} else if (!pddaProcessId.equals(other.pddaProcessId))
			return false;
		if (processName == null) {
			if (other.processName != null)
				return false;
		} else if (!processName.equals(other.processName))
			return false;
		if (processNo == null) {
			if (other.processNo != null)
				return false;
		} else if (!processNo.equals(other.processNo))
			return false;
		if (productionScheduleQty != other.productionScheduleQty)
			return false;
		if (vmc == null) {
			if (other.vmc != null)
				return false;
		} else if (!vmc.equals(other.vmc))
			return false;
		if (vmcDesc == null) {
			if (other.vmcDesc != null)
				return false;
		} else if (!vmcDesc.equals(other.vmcDesc))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return toString(getId().getManpowerAssignmentId(), getId().getProcessId(), getId().getPlantLocCode()
				, getId().getDeptCode(), getId().getLastUpdatedDate(), getId().getExtractDate(), getBodyLocNo()
				, getDaysFlag(), getEffBeginDate(), getEffEndDate(), getLastUpdatedBy(), getModelYear()
				, getPddaProcessId(), getProcessName(), getProcessNo(), getProductionScheduleQty()
				, getVmc(), getVmcDesc());
	}

}