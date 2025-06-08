package com.honda.galc.entity.qi;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.enumtype.QiActiveStatus;

/**
 * 
 * <h3>QiRepairAreaSpace Class description</h3>
 * <p> QiRepairAreaSpace description </p>
 * 
 * <h4>Repair Area</h4>
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
 *        Mar 1, 2017
 * 
 */

@Entity
@Table(name = "QI_REPAIR_AREA_SPACE_TBX")
public class QiRepairAreaSpace extends QiAuditEntryTimestamp {

	private static final long serialVersionUID = 1L;

	@Id
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private QiRepairAreaSpaceId id;
	
	@Column(name = "TARGET_RESP_DEPT")
	@Auditable(isPartOfPrimaryKey = true, sequence = 2)
	private String targetRespDept;
	
	@Column(name = "TARGET_REPAIR_AREA")
	@Auditable(isPartOfPrimaryKey = true, sequence = 3)
	private String targetRepairArea;
	
	@Column(name = "PRODUCT_ID")
	@Auditable(isPartOfPrimaryKey = true, sequence = 4)
	private String productId;
	
	@Column(name = "DEFECTRESULTID")
	@Auditable(isPartOfPrimaryKey = true, sequence = 5)
	private Long defectResultId;
	
	@Column(name = "ACTIVE")
	@Auditable
	private short active;
	
	@Column(name = "ACTUAL_TIMESTAMP")
	private Date actualTimestamp;

	public QiRepairAreaSpace() {
	}

	public QiRepairAreaSpace(QiRepairAreaSpaceId id, String targetRespDept, String targetRepairArea, String productId,
			Long defectResultId) {
		super();
		this.id = id;
		this.targetRespDept = targetRespDept;
		this.targetRepairArea = targetRepairArea;
		this.productId = productId;
		this.defectResultId = defectResultId;
	}

	public QiRepairAreaSpaceId getId() {
		return id;
	}

	public String getTargetRespDept() {
		return StringUtils.trimToEmpty(targetRespDept);
	}

	public String getTargetRepairArea() {
		return StringUtils.trimToEmpty(targetRepairArea);
	}

	public String getProductId() {
		return StringUtils.trimToEmpty(productId);
	}

	public Long getDefectResultId() {
		return defectResultId;
	}

	public void setId(QiRepairAreaSpaceId id) {
		this.id = id;
	}

	public void setTargetRespDept(String targetRespDept) {
		this.targetRespDept = targetRespDept;
	}

	public void setTargetRepairArea(String targetRepairArea) {
		this.targetRepairArea = targetRepairArea;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setDefectResultId(Long defectResultId) {
		this.defectResultId = defectResultId;
	}
	
	public short getActiveValue() {
		return active;
	}
	
	public void setActiveValue(short active) {
        this.active = active;
    }
	
	public boolean isActive() {
        return this.active ==(short) 1;
    }

	public void setActive(boolean active) {
		this.active =(short)( active ? 1 : 0);
	}
	
	public Date getActualTimestamp() {
		return actualTimestamp;
	}

	public void setActualTimestamp(Date actualtimestamp) {
		this.actualTimestamp = actualtimestamp;
	}

	public String getStatus() {
		return QiActiveStatus.getType(active).getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((defectResultId == null) ? 0 : defectResultId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((targetRepairArea == null) ? 0 : targetRepairArea.hashCode());
		result = prime * result + ((targetRespDept == null) ? 0 : targetRespDept.hashCode());
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
		QiRepairAreaSpace other = (QiRepairAreaSpace) obj;
		if (defectResultId == null) {
			if (other.defectResultId != null)
				return false;
		} else if (!defectResultId.equals(other.defectResultId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (targetRepairArea == null) {
			if (other.targetRepairArea != null)
				return false;
		} else if (!targetRepairArea.equals(other.targetRepairArea))
			return false;
		if (targetRespDept == null) {
			if (other.targetRespDept != null)
				return false;
		} else if (!targetRespDept.equals(other.targetRespDept))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QiRepairAreaSpace [id=" + id + ", targetRespDept=" + targetRespDept + ", targetRepairArea="
				+ targetRepairArea + ", productId=" + productId + ", defectResultId=" + defectResultId + "]";
	}


}
