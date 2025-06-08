package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * @date Mar 8, 2016
 */
@Entity
@Table(name="LET_SPOOL_TBX")
public class LetSpool extends AuditEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="SPOOL_ID", nullable=false)
	private Integer spoolId;
	
	@Column(name="ENV_NAME", nullable=false)
	private String envName;
	
	@Column(name="LINE_NAME", nullable=false)
	private String lineName;
	
	@Column(name="PORT_NUMBER", nullable=false)
	private Integer portNumber;
	
	@Column(name="XML_SAVE_LOCATION")
	private String xmlSaveLocation;
	
	@Column(name="EXCEPTION_SAVE_LOCATION")
	private String exceptionSaveLocation;

	public Object getId() {
		return getSpoolId();
	}
	
	public Integer getSpoolId() {
		return spoolId;
	}

	public void setSpoolId(Integer spoolId) {
		this.spoolId = spoolId;
	}

	public String getEnvName() {
		return StringUtils.trim(envName);
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	public String getLineName() {
		return StringUtils.trim(lineName);
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public Integer getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(Integer portNumber) {
		this.portNumber = portNumber;
	}

	public String getXmlSaveLocation() {
		return StringUtils.trim(xmlSaveLocation);
	}

	public void setXmlSaveLocation(String xmlSaveLocation) {
		this.xmlSaveLocation = xmlSaveLocation;
	}

	public String getExceptionSaveLocation() {
		return StringUtils.trim(exceptionSaveLocation);
	}

	public void setExceptionSaveLocation(String exceptionSaveLocation) {
		this.exceptionSaveLocation = exceptionSaveLocation;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((envName == null) ? 0 : envName.hashCode());
		result = prime
				* result
				+ ((exceptionSaveLocation == null) ? 0 : exceptionSaveLocation
						.hashCode());
		result = prime * result
				+ ((lineName == null) ? 0 : lineName.hashCode());
		result = prime * result
				+ ((portNumber == null) ? 0 : portNumber.hashCode());
		result = prime * result + ((spoolId == null) ? 0 : spoolId.hashCode());
		result = prime * result
				+ ((xmlSaveLocation == null) ? 0 : xmlSaveLocation.hashCode());
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
		LetSpool other = (LetSpool) obj;
		if (envName == null) {
			if (other.envName != null)
				return false;
		} else if (!envName.equals(other.envName))
			return false;
		if (exceptionSaveLocation == null) {
			if (other.exceptionSaveLocation != null)
				return false;
		} else if (!exceptionSaveLocation.equals(other.exceptionSaveLocation))
			return false;
		if (lineName == null) {
			if (other.lineName != null)
				return false;
		} else if (!lineName.equals(other.lineName))
			return false;
		if (portNumber == null) {
			if (other.portNumber != null)
				return false;
		} else if (!portNumber.equals(other.portNumber))
			return false;
		if (spoolId == null) {
			if (other.spoolId != null)
				return false;
		} else if (!spoolId.equals(other.spoolId))
			return false;
		if (xmlSaveLocation == null) {
			if (other.xmlSaveLocation != null)
				return false;
		} else if (!xmlSaveLocation.equals(other.xmlSaveLocation))
			return false;
		return true;
	}

	public String toString() {
		return envName+lineName;
	}
}