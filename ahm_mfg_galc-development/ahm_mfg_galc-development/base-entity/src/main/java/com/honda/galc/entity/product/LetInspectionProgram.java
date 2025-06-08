package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name="GAL714TBX")
public class LetInspectionProgram extends AuditEntry implements Serializable {
	@Id
	@Column(name="INSPECTION_PGM_ID")
	private int inspectionPgmId;

	@Column(name="INSPECTION_PGM_NAME")
	private String inspectionPgmName;

	private static final long serialVersionUID = 1L;

	public LetInspectionProgram() {
		super();
	}

	public int getInspectionPgmId() {
		return this.inspectionPgmId;
	}

	public void setInspectionPgmId(int inspectionPgmId) {
		this.inspectionPgmId = inspectionPgmId;
	}

	public String getInspectionPgmName() {
		return StringUtils.trim(this.inspectionPgmName);
	}

	public void setInspectionPgmName(String inspectionPgmName) {
		this.inspectionPgmName = inspectionPgmName;
	}

	public String getId() {
		return getInspectionPgmId() + " " + getInspectionPgmName();
	}

	@Override
	public String toString() {
		return toString(getInspectionPgmId(), getInspectionPgmName());
	}
}
