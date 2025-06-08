package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.dto.ExcelSheetColumn;

/**
 * <h3>MCViosMasterOperationId Class description</h3>
 * <p>
 * ID class for galadm.MC_VIOS_MASTER_OP_TBX
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
 *         Oct 4, 2018
 */
@Embeddable
public class MCViosMasterOperationId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "VIOS_PLATFORM_ID")
	private String viosPlatformId;

	@Column(name = "UNIT_NO", length = 6)
	@ExcelSheetColumn(name = "Unit_No")
	private String unitNo;

	public MCViosMasterOperationId() {
		super();
	}

	public MCViosMasterOperationId(String viosPlatformId, String unitNo) {
		super();
		this.viosPlatformId = viosPlatformId;
		this.unitNo = unitNo;
	}

	public String getViosPlatformId() {
		return StringUtils.trimToEmpty(viosPlatformId);
	}

	public void setViosPlatformId(String viosPlatformId) {
		this.viosPlatformId = viosPlatformId;
	}

	public String getUnitNo() {
		return StringUtils.trimToEmpty(unitNo);
	}

	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}

	public String getOperationName() {
		return StringUtils.trimToEmpty(this.unitNo + Delimiter.UNDERSCORE + this.viosPlatformId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((unitNo == null) ? 0 : unitNo.hashCode());
		result = prime * result + ((viosPlatformId == null) ? 0 : viosPlatformId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MCViosMasterOperationId other = (MCViosMasterOperationId) obj;
		if (unitNo == null) {
			if (other.unitNo != null)
				return false;
		} else if (!unitNo.equals(other.unitNo))
			return false;
		if (viosPlatformId == null) {
			if (other.viosPlatformId != null)
				return false;
		} else if (!viosPlatformId.equals(other.viosPlatformId))
			return false;
		return true;
	}

}
