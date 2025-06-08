package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

/**
 * The primary key class for the GAL130TBX database table.
 * 
 */
@Embeddable
public class DownloadLotSequenceId implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="PROCESS_LOCATION")
	private String processLocation;

	@Column(name="PROCESS_POINT_ID")
	private String processPointId;

    public DownloadLotSequenceId(String processLocation,String processPointId){
    	this.processLocation = processLocation;
    	this.processPointId = processPointId;
    }
    
	public DownloadLotSequenceId() {
    }
	
	public String getProcessLocation() {
		return StringUtils.trim(this.processLocation);
	}
	public void setProcessLocation(String processLocation) {
		this.processLocation = processLocation;
	}
	public String getProcessPointId() {
		return StringUtils.trim(this.processPointId);
	}
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof DownloadLotSequenceId)) {
			return false;
		}
		DownloadLotSequenceId castOther = (DownloadLotSequenceId)other;
		return 
			this.processLocation.equals(castOther.processLocation)
			&& this.processPointId.equals(castOther.processPointId);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.processLocation.hashCode();
		hash = hash * prime + this.processPointId.hashCode();
		
		return hash;
    }
}