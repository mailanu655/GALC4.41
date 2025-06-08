package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */

@Entity
@Table(name="GAL711TBX")
public class LetLogHistory extends AuditEntry {
	
	@EmbeddedId
	private LetLogHistoryId id;

	@Column(name="ECU_LOG")
	private byte[] ecuLog;
	
	private static final long serialVersionUID = 1L;

	public LetLogHistory() {
		super();
	}

	public void setId(LetLogHistoryId id) {
		this.id = id;
	}

	public LetLogHistoryId getId() {
		
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
		return toString(getId().getProductId(),getId().getTestSeq(),getId().getHistorySeq(),getId().getEcuName());
}

}
