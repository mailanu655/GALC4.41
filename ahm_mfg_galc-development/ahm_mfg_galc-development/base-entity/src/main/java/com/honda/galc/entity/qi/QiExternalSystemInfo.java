package com.honda.galc.entity.qi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.honda.galc.entity.CreateUserAuditEntry;

/**
 * 
 * <h3>QiDepartmentId Class description</h3>
 * <p>
 * QiDepartmentId contains the getter and setter of the Department composite key properties and maps this class with database and these columns .
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
 * @author vcc44349<br>
 * Oct 22, 2019
 * 
 */
@Entity
@Table(name = "QI_EXTERNAL_SYSTEM_INFO_TBX")
public class QiExternalSystemInfo extends CreateUserAuditEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "EXTERNAL_SYSTEM_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private short externalSystemId;
		
	@Column(name = "EXTERNAL_SYSTEM_NAME")
	private String externalSystemName;

	@Column(name = "EXTERNAL_SYSTEM_DESC")
	private String externalSystemDesc;

	public short getExternalSystemId() {
		return externalSystemId;
	}

	public void setExternalSystemId(short externalSystemId) {
		this.externalSystemId = externalSystemId;
	}

	public String getExternalSystemName() {
		return externalSystemName;
	}

	public void setExternalSystemName(String externalSystemName) {
		this.externalSystemName = externalSystemName;
	}

	public String getExternalSystemDesc() {
		return externalSystemDesc;
	}

	public void setExternalSystemDesc(String externalSystemDesc) {
		this.externalSystemDesc = externalSystemDesc;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public Object getId() {
		return getExternalSystemId();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((externalSystemDesc == null) ? 0 : externalSystemDesc.hashCode());
		result = prime * result + externalSystemId;
		result = prime * result + ((externalSystemName == null) ? 0 : externalSystemName.hashCode());
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
		QiExternalSystemInfo other = (QiExternalSystemInfo) obj;
		if (externalSystemDesc == null) {
			if (other.externalSystemDesc != null)
				return false;
		} else if (!externalSystemDesc.equals(other.externalSystemDesc))
			return false;
		if (externalSystemId != other.externalSystemId)
			return false;
		if (externalSystemName == null) {
			if (other.externalSystemName != null)
				return false;
		} else if (!externalSystemName.equals(other.externalSystemName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QiExternalSystem [externalSystemId=" + externalSystemId + ", externalSystemName=" + externalSystemName
				+ ", externalSystemDesc=" + externalSystemDesc + "]";
	}


}
