package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;


/**
 * The persistent class for the GAL130TBX database table.
 * 
 */
@Entity
@Table(name = "GAL130TBX")
public class DownloadLotSequence extends AuditEntry {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DownloadLotSequenceId id;

	@Column(name="END_PRODUCTION_LOT")
	private String endProductionLot;

    public DownloadLotSequence() {
    	super();
    }

	public DownloadLotSequenceId getId() {
		return this.id;
	}

	public void setId(DownloadLotSequenceId id) {
		this.id = id;
	}
	
	public String getProcessLocation() {
		return id.getProcessLocation();
	}
	
	public String getProcessPointId() {
		return id.getProcessPointId();
	}

	public String getEndProductionLot() {
		return StringUtils.trim(this.endProductionLot);
	}

	public void setEndProductionLot(String endProductionLot) {
		this.endProductionLot = endProductionLot;
	}
	
	public String toString() {
		return toString(id.getProcessLocation(),id.getProcessPointId(),getEndProductionLot());
	}

}