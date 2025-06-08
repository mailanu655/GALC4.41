package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.ExcelSheetColumn;

/**
 * <h3>MCViosMasterOperationMeasurementCheckerId Class description</h3>
 * <p>
 * ID class for galadm.MC_VIOS_MASTER_OP_MEAS_CHECKER_TBX
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
public class MCViosMasterOperationMeasurementCheckerId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "VIOS_PLATFORM_ID")
	private String viosPlatformId;

	@Column(name = "UNIT_NO", length = 6)
	@ExcelSheetColumn(name="Unit No")
	private String unitNo;

	@Column(name = "OP_MEAS_SEQ_NUM", nullable = false)
	private int measurementSeqNum;

	@Column(name = "CHECK_POINT", nullable = false)
	@ExcelSheetColumn(name = "CheckPoint")
	private String checkPoint;

	@Column(name = "CHECK_SEQ", nullable = false)
	@ExcelSheetColumn(name = "CheckSeq")
	private int checkSeq;

	@Column(name = "CHECK_NAME", nullable = false)
	@ExcelSheetColumn(name = "CheckName")
	private String checkName;

	public MCViosMasterOperationMeasurementCheckerId() {
		super();
	}

	public MCViosMasterOperationMeasurementCheckerId(String viosPlatformId, String unitNo,
			 int measurementSeqNum, String checkPoint, int checkSeq,
			String checkName) {
		super();
		this.viosPlatformId = viosPlatformId;
		this.unitNo = unitNo;
		//this.partNo = partNo;
		//this.partItemNo = partItemNo;
		//this.partSectionCode = partSectionCode;
		this.measurementSeqNum = measurementSeqNum;
		this.checkPoint = checkPoint;
		this.checkSeq = checkSeq;
		this.checkName = checkName;
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

//	public String getPartNo() {
//		return StringUtils.trimToEmpty(partNo);
//	}
//
//	public void setPartNo(String partNo) {
//		this.partNo = partNo;
//	}

//	public String getPartItemNo() {
//		return StringUtils.trimToEmpty(partItemNo);
//	}
//
//	public void setPartItemNo(String partItemNo) {
//		this.partItemNo = partItemNo;
//	}
//
//	public String getPartSectionCode() {
//		return StringUtils.trimToEmpty(partSectionCode);
//	}
//
//	public void setPartSectionCode(String partSectionCode) {
//		this.partSectionCode = partSectionCode;
//	}

	public int getMeasurementSeqNum() {
		return measurementSeqNum;
	}

	public void setMeasurementSeqNum(int measurementSeqNum) {
		this.measurementSeqNum = measurementSeqNum;
	}

	public String getCheckPoint() {
		return StringUtils.trimToEmpty(checkPoint);
	}

	public void setCheckPoint(String checkPoint) {
		this.checkPoint = checkPoint;
	}

	public int getCheckSeq() {
		return checkSeq;
	}

	public void setCheckSeq(int checkSeq) {
		this.checkSeq = checkSeq;
	}

	public String getCheckName() {
		return StringUtils.trimToEmpty(checkName);
	}

	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((checkName == null) ? 0 : checkName.hashCode());
		result = prime * result + ((checkPoint == null) ? 0 : checkPoint.hashCode());
		result = prime * result + checkSeq;
		result = prime * result + measurementSeqNum;
		//result = prime * result + ((partItemNo == null) ? 0 : partItemNo.hashCode());
		//result = prime * result + ((partNo == null) ? 0 : partNo.hashCode());
		//result = prime * result + ((partSectionCode == null) ? 0 : partSectionCode.hashCode());
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
		MCViosMasterOperationMeasurementCheckerId other = (MCViosMasterOperationMeasurementCheckerId) obj;
		if (checkName == null) {
			if (other.checkName != null)
				return false;
		} else if (!checkName.equals(other.checkName))
			return false;
		if (checkPoint == null) {
			if (other.checkPoint != null)
				return false;
		} else if (!checkPoint.equals(other.checkPoint))
			return false;
		if (checkSeq != other.checkSeq)
			return false;
		if (measurementSeqNum != other.measurementSeqNum)
			return false;
		
//		if (partNo == null) {
//			if (other.partNo != null)
//				return false;
//		} else if (!partNo.equals(other.partNo))
//			return false;
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
