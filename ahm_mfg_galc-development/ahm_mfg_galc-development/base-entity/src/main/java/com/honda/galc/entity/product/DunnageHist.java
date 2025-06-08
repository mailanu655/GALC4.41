package com.honda.galc.entity.product;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name="DUNNAGE_HIST_TBX")
public class DunnageHist extends AuditEntry {
	
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private DunnageHistId id;

	@Column(name="OFF_TIMESTAMP")
	private Timestamp offTimestamp;


	public DunnageHist() {
		super();
	}

	public DunnageHistId getId() {
		return this.id;
	}

	public void setId(DunnageHistId id) {
		this.id = id;
	}

	public Timestamp getOffTimestamp() {
		return this.offTimestamp;
	}

	public void setOffTimestamp(Timestamp offTimestamp) {
		this.offTimestamp = offTimestamp;
	}

	public String toString() {
        return getId().toString();
    }
	

}
