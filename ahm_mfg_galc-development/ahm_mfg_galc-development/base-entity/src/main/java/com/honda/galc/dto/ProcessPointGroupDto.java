package com.honda.galc.dto;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * <h3>Class description</h3>
 * 
 * <h4>Description</h4>
 * <p>
 * </p>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Jul. 10, 2018</TD>
 * <TD>1.0</TD>
 * <TD>20180710</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 */
public class ProcessPointGroupDto implements IDto {
	
	private static final long serialVersionUID = -4777601325567833949L;

	@DtoTag(name = "REGIONAL_VALUE_NAME")
	private String regionalValueName;
	
	@DtoTag(name = "PROCESS_POINT_GROUP_NAME")
	private String processPointGroupName;

	public String getRegionalValueName() {
		return StringUtils.trimToEmpty(regionalValueName);
	}

	public void setRegionalValueName(String regionalValueName) {
		this.regionalValueName = regionalValueName;
	}

	public String getProcessPointGroupName() {
		return StringUtils.trimToEmpty(processPointGroupName);
	}

	public void setProcessPointGroupName(String processPointGroupName) {
		this.processPointGroupName = processPointGroupName;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime + ((regionalValueName == null) ? 0 : getRegionalValueName().hashCode());
		result = prime * result + ((processPointGroupName == null) ? 0 : getProcessPointGroupName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		
		ProcessPointGroupDto other = (ProcessPointGroupDto) obj;

		return StringUtils.equals(getRegionalValueName(), other.getRegionalValueName())
				&& StringUtils.equals(getProcessPointGroupName(), other.getProcessPointGroupName());
	}
	    
	@Override
	public String toString() {		
	  return StringUtil.toString(getClass().getSimpleName(), getRegionalValueName(), getProcessPointGroupName());
	}
 
}
