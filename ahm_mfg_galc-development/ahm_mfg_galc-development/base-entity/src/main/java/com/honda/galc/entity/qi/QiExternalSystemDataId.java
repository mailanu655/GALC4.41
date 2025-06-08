package com.honda.galc.entity.qi;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.ToStringUtil;

/**
 * 
 * <h3>QiExternalSystemDataId Class description</h3>
 * <p>
 * QiExternalSystemDataId contains the getter and setter of the QiExternalSystemData composite key and maps this class with database and these columns .
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
 * @author LnTInfotech<br>
 * 
 */

@Embeddable
public class QiExternalSystemDataId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "EXTERNAL_SYSTEM_NAME")
	private String externalSystemName;

	@Column(name = "EXTERNAL_PART_CODE")
	private String externalPartCode;

	@Column(name = "EXTERNAL_DEFECT_CODE")
	private String externalDefectCode;

	@Column(name = "PRODUCT_ID")
	private String productId;

	@Column(name = "PROCESS_POINT_ID")
	private String processPointId;
	
	@Column(name = "ENTRY_TIMESTAMP")
	private Timestamp entryTimestamp;

	public QiExternalSystemDataId() {
		super();
	}

	public QiExternalSystemDataId(String externalSystemName,String externalPartCode,String externalDefectCode,String productId,String processPointId,Timestamp entryTimestamp) {
		this.setExternalSystemName(externalSystemName);
		this.setExternalPartCode(externalPartCode);
		this.setExternalDefectCode(externalDefectCode);
		this.setProductId(productId);
		this.setProcessPointId(processPointId);
		this.setEntryTimestamp(entryTimestamp);
	}


	public String getExternalSystemName() {
		return StringUtils.trimToEmpty(this.externalSystemName);
	}

	public void setExternalSystemName(String externalSystemName) {
		this.externalSystemName = externalSystemName;
	}

	public String getExternalPartCode() {
		return StringUtils.trimToEmpty(this.externalPartCode);
	}

	public void setExternalPartCode(String externalPartCode) {
		this.externalPartCode = externalPartCode;
	}

	public String getExternalDefectCode() {
		return StringUtils.trimToEmpty(this.externalDefectCode);
	}

	public void setExternalDefectCode(String externalDefectCode) {
		this.externalDefectCode = externalDefectCode;
	}

	public String getProductId() {
		return StringUtils.trimToEmpty(this.productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProcessPointId() {
		return StringUtils.trimToEmpty(this.processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public Timestamp getEntryTimestamp() {
		return entryTimestamp;
	}

	public void setEntryTimestamp(Timestamp entryTimestamp) {
		this.entryTimestamp = entryTimestamp;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((entryTimestamp == null) ? 0 : entryTimestamp.hashCode());
		result = prime
				* result
				+ ((externalDefectCode == null) ? 0 : externalDefectCode
						.hashCode());
		result = prime
				* result
				+ ((externalPartCode == null) ? 0 : externalPartCode.hashCode());
		result = prime
				* result
				+ ((externalSystemName == null) ? 0 : externalSystemName
						.hashCode());
		result = prime * result
				+ ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
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
		QiExternalSystemDataId other = (QiExternalSystemDataId) obj;
		if (entryTimestamp == null) {
			if (other.entryTimestamp != null)
				return false;
		} else if (!entryTimestamp.equals(other.entryTimestamp))
			return false;
		if (externalDefectCode == null) {
			if (other.externalDefectCode != null)
				return false;
		} else if (!externalDefectCode.equals(other.externalDefectCode))
			return false;
		if (externalPartCode == null) {
			if (other.externalPartCode != null)
				return false;
		} else if (!externalPartCode.equals(other.externalPartCode))
			return false;
		if (externalSystemName == null) {
			if (other.externalSystemName != null)
				return false;
		} else if (!externalSystemName.equals(other.externalSystemName))
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		return true;
	}
}
