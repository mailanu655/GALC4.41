package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name="DUNNAGE_CONTENT_TBX")
public class DunnageContent extends AuditEntry {
	
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private DunnageContentId id;

	@Column(name="DUNNAGE_ROW")
	private String dunnageRow;

	@Column(name="DUNNAGE_COLUMN")
	private String dunnageColumn;
	
	@Column(name="DUNNAGE_LAYER")
	private String dunnageLayer;


	public DunnageContent() {
		super();
	}

	public DunnageContentId getId() {
		return this.id;
	}

	public void setId(DunnageContentId id) {
		this.id = id;
	}	
	
	public String getDunnageRow() {
		return StringUtils.trim(dunnageRow);
	}

	public void setDunnageRow(String dunnageRow) {
		this.dunnageRow = dunnageRow;
	}

	public String getDunnageColumn() {
		return StringUtils.trim(dunnageColumn);
	}

	public void setDunnageColumn(String dunnageColumn) {
		this.dunnageColumn = dunnageColumn;
	}

	public String getDunnageLayer() {
		return StringUtils.trim(dunnageLayer);
	}

	public void setDunnageLayer(String dunnageLayer) {
		this.dunnageLayer = dunnageLayer;
	}

	public String toString() {
        return getId().toString();
    }
	

}
