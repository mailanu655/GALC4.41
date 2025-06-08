package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ScheduleReplicationId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column( name = "SOURCE_PROC_LOC" )
	private String sourceProcLoc;

	@Column( name = "DEST_PROC_LOC" )
	private String destProcLoc;

	@Column( name = "DEST_SPEC_CODE" )
	private String destSpecCode;

	public String getSourceProcLoc() {
		return sourceProcLoc;
	}

	public void setSourceProcLoc(String sourceProcLoc) {
		this.sourceProcLoc = sourceProcLoc;
	}

	public String getDestProcLoc() {
		return destProcLoc;
	}

	public void setDestProcLoc(String destProcLoc) {
		this.destProcLoc = destProcLoc;
	}

	public String getDestSpecCode() {
		return destSpecCode;
	}

	public void setDestSpecCode(String destSpecCode) {
		this.destSpecCode = destSpecCode;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destProcLoc == null) ? 0 : destProcLoc.hashCode());
		result = prime * result + ((destSpecCode == null) ? 0 : destSpecCode.hashCode());
		result = prime * result + ((sourceProcLoc == null) ? 0 : sourceProcLoc.hashCode());
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
		ScheduleReplicationId other = (ScheduleReplicationId) obj;
		if (destProcLoc == null) {
			if (other.destProcLoc != null)
				return false;
		} else if (!destProcLoc.equals(other.destProcLoc))
			return false;
		if (destSpecCode == null) {
			if (other.destSpecCode != null)
				return false;
		} else if (!destSpecCode.equals(other.destSpecCode))
			return false;
		if (sourceProcLoc == null) {
			if (other.sourceProcLoc != null)
				return false;
		} else if (!sourceProcLoc.equals(other.sourceProcLoc))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ScheduleReplicationId [sourceProcLoc=" + sourceProcLoc + ", destProcLoc=" + destProcLoc
				+ ", destSpecCode=" + destSpecCode + "]";
	}
}
