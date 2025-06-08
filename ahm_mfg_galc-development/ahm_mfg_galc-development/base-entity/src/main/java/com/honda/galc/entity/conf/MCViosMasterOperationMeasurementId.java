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
 * <h3>MCViosMasterOperationMeasurementId Class description</h3>
 * <p>
 * ID class for galadm.MC_VIOS_MASTER_OP_MEAS_TBX
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
 *        Nov 20, 2018
 */
@Embeddable
public class MCViosMasterOperationMeasurementId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "VIOS_PLATFORM_ID")
	private String viosPlatformId;

	@Column(name = "UNIT_NO", length = 6)
	@ExcelSheetColumn(name = "Unit")
	private String unitNo;

	@Column(name = "PART_TYPE", length = 32)
	@Enumerated(EnumType.STRING)
	private PartType partType;

	@Column(name = "OP_MEAS_SEQ_NUM", nullable = false)
	private int measurementSeqNum;
	
	public MCViosMasterOperationMeasurementId() {
		super();
	}

	public MCViosMasterOperationMeasurementId(String viosPlatformId, String unitNo, PartType partType, int measurementSeqNum) {
		super();
		this.viosPlatformId = viosPlatformId;
		this.unitNo = unitNo;
		this.partType = partType;
		this.measurementSeqNum = measurementSeqNum;
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

	public PartType getPartType() {
		return partType;
	}
	
	public String getPartTypeAsString() {
		return partType.name();
	}

	public void setPartType(PartType partType) {
		this.partType = partType;
	}

	public int getMeasurementSeqNum() {
		
		return measurementSeqNum;
	}

	public void setMeasurementSeqNum(int measurementSeqNum) {
		this.measurementSeqNum = measurementSeqNum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + measurementSeqNum;
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
		MCViosMasterOperationMeasurementId other = (MCViosMasterOperationMeasurementId) obj;
		if (measurementSeqNum != other.measurementSeqNum)
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
