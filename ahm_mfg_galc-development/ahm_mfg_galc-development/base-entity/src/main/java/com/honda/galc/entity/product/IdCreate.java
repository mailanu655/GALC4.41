package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name = "GAL266TBX")
public class IdCreate extends AuditEntry implements Serializable {
	@EmbeddedId
	private IdCreateId id;

	private String prefix;

	@Column(name="START_ID")
	private int startId;

	@Column(name="END_ID")
	private int endId;

	@Column(name="CURRENT_ID")
	private int currentId;

	private static final long serialVersionUID = 1L;

	public IdCreate() {
		super();
	}

	public IdCreateId getId() {
		return this.id;
	}

	public void setId(IdCreateId id) {
		this.id = id;
	}

	public String getPrefix() {
		return  StringUtils.trim(this.prefix);
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public int getStartId() {
		return this.startId;
	}

	public void setStartId(int startId) {
		this.startId = startId;
	}

	public int getEndId() {
		return this.endId;
	}

	public void setEndId(int endId) {
		this.endId = endId;
	}

	public int getCurrentId() {
		return this.currentId;
	}

	public void setCurrentId(int currentId) {
		this.currentId = currentId;
	}
	
	@Override
	public String toString() {
		return toString(getId().getTableName(), getId().getColumnName(), getPrefix(), getStartId(), getEndId(), getCurrentId());
	}
}
