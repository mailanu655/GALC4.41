package com.honda.ahm.lc.vdb.entity;

import java.io.Serializable;

import javax.persistence.Column;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>HistoryId</code> is ... .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Hemant Rajput
 * @created Oct 1, 2022
 */
public class TrackingStatusDetailsId implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Column(name = "division_name")
	private String divisionName;
    
    @Column(name = "line_name")
	private String lineName;
    
    @Column(name = "process_point_name")
	private String processPointName;
	
	public TrackingStatusDetailsId() {
		super();
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getProcessPointName() {
		return processPointName;
	}

	public void setProcessPointName(String processPointName) {
		this.processPointName = processPointName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((divisionName == null) ? 0 : divisionName.hashCode());
		result = prime * result + ((lineName == null) ? 0 : lineName.hashCode());
		result = prime * result + ((processPointName == null) ? 0 : processPointName.hashCode());
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
		TrackingStatusDetailsId other = (TrackingStatusDetailsId) obj;
		if (divisionName == null) {
			if (other.divisionName != null)
				return false;
		} else if (!divisionName.equals(other.divisionName))
			return false;
		if (lineName == null) {
			if (other.lineName != null)
				return false;
		} else if (!lineName.equals(other.lineName))
			return false;
		if (processPointName == null) {
			if (other.processPointName != null)
				return false;
		} else if (!processPointName.equals(other.processPointName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TrackingStatusDetailsId [divisionName=" + divisionName + ", lineName=" + lineName
				+ ", processPointName=" + processPointName + "]";
	}
	
}
