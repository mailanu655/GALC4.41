package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name="GAL715TBX")
public class LetInspectionParam extends AuditEntry implements Serializable {
	@Id
	@Column(name="INSPECTION_PARAM_ID")
	private int inspectionParamId;

	@Column(name="INSPECTION_PARAM_NAME")
	private String inspectionParamName;

	private static final long serialVersionUID = 1L;

	public LetInspectionParam() {
		super();
	}

	public int getInspectionParamId() {
		return this.inspectionParamId;
	}

	public void setInspectionParamId(int inspectionParamId) {
		this.inspectionParamId = inspectionParamId;
	}

	public String getInspectionParamName() {
		return StringUtils.trim(this.inspectionParamName);
	}

	public void setInspectionParamName(String inspectionParamName) {
		this.inspectionParamName = inspectionParamName;
	}

	public String getId() {
		return getInspectionParamId() + " " + getInspectionParamName();
	}
	
	@Override
	public String toString() {
		return toString(getInspectionParamId(), getInspectionParamName());
	}
}
