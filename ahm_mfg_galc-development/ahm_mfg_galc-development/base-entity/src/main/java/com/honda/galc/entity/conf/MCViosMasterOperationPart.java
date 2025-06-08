package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.constant.PartType;
import com.honda.galc.dto.ExcelSheetColumn;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.PartCheck;

/**
 * <h3>MCViosMasterOperationPart Class description</h3>
 * <p>
 * Entity class for galadm.MC_VIOS_MASTER_OP_PART_TBX
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
@Entity
@Table(name="MC_VIOS_MASTER_OP_PART_TBX")
public class MCViosMasterOperationPart extends AuditEntry {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private MCViosMasterOperationPartId id;
	
	@Column(name="PART_DESC", length=255)
	private String partDesc;
	
	@Column(name="PART_VIEW", length=255)
	private String partView;
	
	@Column(name="PART_PROCESSOR", length=255)
	private String partProcessor;
	
	@Column(name="DEVICE_ID", length=32)
	private String deviceId;

	@Column(name="DEVICE_MSG", length=32)
	private String deviceMsg;
	
	@Column(name="PART_MASK", length=255)
	@ExcelSheetColumn(name="Part Mask")
	private String partMask;
	
	@Column(name="PART_MARK", length=32)
	private String partMark;
	
	@Column(name="MEASUREMENT_COUNT")
	private int measCount;
	
	@Column(name="PART_CHECK")
	@ExcelSheetColumn(name="Part Check")
	@Enumerated(EnumType.ORDINAL)
	private PartCheck partCheck;
	
	
	@Column(name = "USER_ID")
    private String userId;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public MCViosMasterOperationPart() {
		super();
	}

	public MCViosMasterOperationPart(String viosPlatformId, String unitNo, String partNo, 
			 PartType partType, String partDesc, String partView,
			String partProcessor, String deviceId, String deviceMsg, String partMask, String partMark, int measCount,
			PartCheck partCheck) {
		super();
		this.id = new MCViosMasterOperationPartId(viosPlatformId, unitNo, partNo,  partType);
		this.partDesc = partDesc;
		this.partView = partView;
		this.partProcessor = partProcessor;
		this.deviceId = deviceId;
		this.deviceMsg = deviceMsg;
		this.partMask = partMask;
		this.partMark = partMark;
		this.measCount = measCount;
		this.partCheck = partCheck;
	}

	public String getPartDesc() {
		return StringUtils.trimToEmpty(partDesc);
	}

	public void setPartDesc(String partDesc) {
		this.partDesc = partDesc;
	}

	public String getPartView() {
		return StringUtils.trimToEmpty(partView);
	}

	public void setPartView(String partView) {
		this.partView = partView;
	}

	public String getPartProcessor() {
		return StringUtils.trimToEmpty(partProcessor);
	}

	public void setPartProcessor(String partProcessor) {
		this.partProcessor = partProcessor;
	}

	public String getDeviceId() {
		return StringUtils.trimToEmpty(deviceId);
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceMsg() {
		return StringUtils.trimToEmpty(deviceMsg);
	}

	public void setDeviceMsg(String deviceMsg) {
		this.deviceMsg = deviceMsg;
	}

	public String getPartMask() {
		return StringUtils.trimToEmpty(partMask);
	}

	public void setPartMask(String partMask) {
		this.partMask = partMask;
	}

	public String getPartMark() {
		return StringUtils.trimToEmpty(partMark);
	}

	public void setPartMark(String partMark) {
		this.partMark = partMark;
	}

	public int getMeasCount() {
		return measCount;
	}

	public void setMeasCount(int measCount) {
		this.measCount = measCount;
	}

	public PartCheck getPartCheck() {
		return partCheck;
	}
	
	public String getPartCheckAsString() {
		return partCheck.name();
	}

	public void setPartCheck(PartCheck partCheck) {
		this.partCheck = partCheck;
	}

	public MCViosMasterOperationPartId getId() {
		return id;
	}

	public void setId(MCViosMasterOperationPartId id) {
		this.id = id;
	}
	
	public String getOperationName() {
		return getId().getUnitNo()
				+Delimiter.UNDERSCORE
				+getId().getViosPlatformId();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
		result = prime * result + ((deviceMsg == null) ? 0 : deviceMsg.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + measCount;
		result = prime * result + ((partCheck == null) ? 0 : partCheck.hashCode());
		result = prime * result + ((partDesc == null) ? 0 : partDesc.hashCode());
		result = prime * result + ((partMark == null) ? 0 : partMark.hashCode());
		result = prime * result + ((partMask == null) ? 0 : partMask.hashCode());
		result = prime * result + ((partProcessor == null) ? 0 : partProcessor.hashCode());
		result = prime * result + ((partView == null) ? 0 : partView.hashCode());
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
		MCViosMasterOperationPart other = (MCViosMasterOperationPart) obj;
		if (deviceId == null) {
			if (other.deviceId != null)
				return false;
		} else if (!deviceId.equals(other.deviceId))
			return false;
		if (deviceMsg == null) {
			if (other.deviceMsg != null)
				return false;
		} else if (!deviceMsg.equals(other.deviceMsg))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (measCount != other.measCount)
			return false;
		if (partCheck != other.partCheck)
			return false;
		if (partDesc == null) {
			if (other.partDesc != null)
				return false;
		} else if (!partDesc.equals(other.partDesc))
			return false;
		if (partMark == null) {
			if (other.partMark != null)
				return false;
		} else if (!partMark.equals(other.partMark))
			return false;
		if (partMask == null) {
			if (other.partMask != null)
				return false;
		} else if (!partMask.equals(other.partMask))
			return false;
		if (partProcessor == null) {
			if (other.partProcessor != null)
				return false;
		} else if (!partProcessor.equals(other.partProcessor))
			return false;
		if (partView == null) {
			if (other.partView != null)
				return false;
		} else if (!partView.equals(other.partView))
			return false;
		return true;
	}

}
