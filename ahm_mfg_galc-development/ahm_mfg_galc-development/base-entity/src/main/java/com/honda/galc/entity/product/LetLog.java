package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.honda.galc.entity.AuditEntry;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */

@Entity
@Table(name="GAL702TBX")
public class LetLog extends AuditEntry {
	
	@EmbeddedId
	private LetLogId id;

	@Column(name="ECU_LOG")
	private byte[] ecuLog;

	private static final long serialVersionUID = 1L;

	public LetLog() {
		super();
	}

	public void setId(LetLogId id) {
		this.id = id;
	}

	public LetLogId getId() {
		
		return id;
	}
	
	public byte[] getEcuLog() {
		return ecuLog;
	}

	public void setEcuLog(byte[] ecuLog) {
		this.ecuLog = ecuLog;
	}
	
	@Override
	public String toString() {
		return toString(getId().getProductId(),getId().getTestSeq(),getId().getEcuName());
}

}
