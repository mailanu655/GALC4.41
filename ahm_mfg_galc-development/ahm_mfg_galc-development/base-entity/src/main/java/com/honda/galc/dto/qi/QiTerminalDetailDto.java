package com.honda.galc.dto.qi;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.ProcessPoint;

public class QiTerminalDetailDto implements IDto, Comparable<QiTerminalDetailDto> {
	
	private static final long serialVersionUID = 1L;

	@DtoTag(outputName = "HOST_NAME")
	private String hostName;
	
	@DtoTag(outputName = "TERMINAL_DESCRIPTION")
	private String terminalDescription;
	
	@DtoTag(outputName = "COLUMN_LOCATION")
	private String columnLocation;
	
	@DtoTag(outputName = "PHONE_EXTENSION")
	private String phoneExtension;
	
	
	public int compareTo(QiTerminalDetailDto otherTerminal)  {
    	if(otherTerminal == null)  return 1;
    	return hostName.compareToIgnoreCase(otherTerminal.getHostName());
    }


	public String getHostName() {
		return hostName;
	}


	public void setHostName(String hostName) {
		this.hostName = hostName;
	}


	public String getTerminalDescription() {
		return terminalDescription;
	}


	public void setTerminalDescription(String terminalDescription) {
		this.terminalDescription = terminalDescription;
	}


	public String getColumnLocation() {
		return columnLocation;
	}


	public void setColumnLocation(String columnLocation) {
		this.columnLocation = columnLocation;
	}


	public String getPhoneExtension() {
		return phoneExtension;
	}


	public void setPhoneExtension(String phoneExtension) {
		this.phoneExtension = phoneExtension;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((columnLocation == null) ? 0 : columnLocation.hashCode());
		result = prime * result + ((hostName == null) ? 0 : hostName.hashCode());
		result = prime * result + ((phoneExtension == null) ? 0 : phoneExtension.hashCode());
		result = prime * result + ((terminalDescription == null) ? 0 : terminalDescription.hashCode());
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
		QiTerminalDetailDto other = (QiTerminalDetailDto) obj;
		if (columnLocation == null) {
			if (other.columnLocation != null)
				return false;
		} else if (!columnLocation.equals(other.columnLocation))
			return false;
		if (hostName == null) {
			if (other.hostName != null)
				return false;
		} else if (!hostName.equals(other.hostName))
			return false;
		if (phoneExtension == null) {
			if (other.phoneExtension != null)
				return false;
		} else if (!phoneExtension.equals(other.phoneExtension))
			return false;
		if (terminalDescription == null) {
			if (other.terminalDescription != null)
				return false;
		} else if (!terminalDescription.equals(other.terminalDescription))
			return false;
		return true;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QiTerminalDetailDto [hostName=" + hostName + ", terminalDescription=" + terminalDescription
				+ ", columnLocation=" + columnLocation + ", phoneExtension=" + phoneExtension + "]";
	}

}
