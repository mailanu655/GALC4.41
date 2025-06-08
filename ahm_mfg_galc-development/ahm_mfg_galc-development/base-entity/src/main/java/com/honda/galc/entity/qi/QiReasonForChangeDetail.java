package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.util.ToStringUtil;

/**
 * 
 * <h3>QiReasonForChangeDetail Class description</h3>
 * <p>
 * QiReasonForChangeDetail description
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
 * @author Justin Jiang<br>
 *         November 3, 2020
 *
 */

@Entity
@Table(name = "QI_REASON_FOR_CHANGE_DETAIL_TBX")
public class QiReasonForChangeDetail extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "DETAIL_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private int detailId;
	
	@Auditable
	@Column(name = "CATEGORY_ID", nullable=false)
	private int categoryId;
	
	@Auditable
	@Column(name = "DETAIL", nullable=false)
	private String detail;

	public QiReasonForChangeDetail() {
		super();
	}

	public Object getId() {
		return getDetailId();
	}

	public int getDetailId() {
		return detailId;
	}

	public void setDetailId(int detailId) {
		this.detailId = detailId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getDetail() {
		return StringUtils.trimToEmpty(detail);
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + categoryId;
		result = prime * result + ((detail == null) ? 0 : detail.hashCode());
		result = prime * result + detailId;
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
		QiReasonForChangeDetail other = (QiReasonForChangeDetail) obj;
		if (categoryId != other.categoryId)
			return false;
		if (detail == null) {
			if (other.detail != null)
				return false;
		} else if (!detail.equals(other.detail))
			return false;
		if (detailId != other.detailId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}
}
