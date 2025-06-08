package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.ExcelSheetColumn;

/**
 * <h3>MCViosMasterOperationCheckerId Class description</h3>
 * <p>
 * ID class for galadm.MC_VIOS_MASTER_OP_CHECKER_TBX
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
 *        Oct 4, 2018
 */
@Embeddable
public class MCViosMasterOperationCheckerId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	@Column(name="VIOS_PLATFORM_ID")
	private String viosPlatformId;
	
	@Column(name="UNIT_NO", length=6)
	@ExcelSheetColumn(name = "Unit_No")
	private String unitNo;

	@Column(name="CHECK_POINT", nullable=false)
	@ExcelSheetColumn(name = "CheckPoint")
	private String checkPoint;

	@Column(name="CHECK_SEQ", nullable=false)
	@ExcelSheetColumn(name = "CheckSequence")
	private int checkSeq;
	
	public MCViosMasterOperationCheckerId() {
		super();
	}

	public MCViosMasterOperationCheckerId(String viosPlatformId, String unitNo, String checkPoint, int checkSeq) {
		super();
		this.viosPlatformId = viosPlatformId;
		this.unitNo = unitNo;
		this.checkPoint = checkPoint;
		this.checkSeq = checkSeq;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((checkPoint == null) ? 0 : checkPoint.hashCode());
		result = prime * result + checkSeq;
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
		MCViosMasterOperationCheckerId other = (MCViosMasterOperationCheckerId) obj;
		if (checkPoint == null) {
			if (other.checkPoint != null)
				return false;
		} else if (!checkPoint.equals(other.checkPoint))
			return false;
		if (checkSeq != other.checkSeq)
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