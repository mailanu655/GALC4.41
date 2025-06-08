package com.honda.galc.entity.product;

import java.sql.Timestamp;

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
@Table(name="GAL709TBX")
public class LetModifyHistory extends AuditEntry {
	
	@EmbeddedId
	private LetModifyHistoryId id;

	@Column(name="USER_ID")
	private String userId;

	@Column(name="MODIFY_TYPE")
	private String modifyType;

	@Column(name="MODIFY_TIMESTAMP")
	private Timestamp modifyTimestamp;
	
	private static final long serialVersionUID = 1L;

	public LetModifyHistory() {
		super();
	}

	public void setId(LetModifyHistoryId id) {
		this.id = id;
	}

	public LetModifyHistoryId getId() {
		
		return id;
	}

	public String getUserId() {
		return StringUtils.trimToEmpty(userId);
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getModifyType() {
		return StringUtils.trimToEmpty(modifyType);
	}

	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}

	public Timestamp getModifyTimestamp() {
		return modifyTimestamp;
	}

	public void setModifyTimestamp(Timestamp modifyTimestamp) {
		this.modifyTimestamp = modifyTimestamp;
	}
	
	
	@Override
	public String toString() {
		return toString(getId().getProductId(),getId().getHistorySeq());
}

}
