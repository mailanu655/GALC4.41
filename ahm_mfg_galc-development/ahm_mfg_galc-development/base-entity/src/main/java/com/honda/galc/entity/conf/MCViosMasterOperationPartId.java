package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.constant.PartType;
import com.honda.galc.dto.ExcelSheetColumn;

/**
 * <h3>MCViosMasterOperationPartId Class description</h3>
 * <p>
 * ID class for galadm.MC_VIOS_MASTER_OP_PART_TBX
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
 *         Nov 20, 2018
 */
@Embeddable
public class MCViosMasterOperationPartId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "VIOS_PLATFORM_ID")
	private String viosPlatformId;

	@Column(name = "UNIT_NO", length = 6)
	@ExcelSheetColumn(name = "Unit")
	private String unitNo;

	@Column(name = "PART_NO", length = 18)
	@ExcelSheetColumn(name = "Part No")
	private String partNo;


	@Column(name = "PART_TYPE", length = 32)
	@Enumerated(EnumType.STRING)
	private PartType partType;

	public MCViosMasterOperationPartId() {
		super();
	}

	public MCViosMasterOperationPartId(String viosPlatformId, String unitNo, String partNo,  PartType partType) {
		super();
		this.viosPlatformId = viosPlatformId;
		this.unitNo = unitNo;
		this.partNo = partNo;
		this.partType = partType;
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

	public String getPartNo() {
		return StringUtils.trimToEmpty(partNo);
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public PartType getPartType() {
		return partType;
	}

	public String getPartTypeAsString() {
		return partType.name();
	}

	public void setPartType(PartType partType) {
		this.partType = partType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((partNo == null) ? 0 : partNo.hashCode());
		result = prime * result + ((partType == null) ? 0 : partType.hashCode());
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
		MCViosMasterOperationPartId other = (MCViosMasterOperationPartId) obj;
		
		if (partNo == null) {
			if (other.partNo != null)
				return false;
		} else if (!partNo.equals(other.partNo))
			return false;
		
		if (partType != other.partType)
			return false;
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
