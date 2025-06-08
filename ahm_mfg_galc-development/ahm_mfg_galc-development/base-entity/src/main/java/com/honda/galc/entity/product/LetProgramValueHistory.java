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
@Table(name="GAL713TBX")
public class LetProgramValueHistory extends AuditEntry {
	
	@EmbeddedId
	private LetProgramValueHistoryId id;
	
	@Column(name="INSPECTION_PARAM_VALUE")
	private String inspectionParamValue;

	@Column(name="INSPECTION_PARAM_UNIT")
	private String inspectionParamUnit;

	private static final long serialVersionUID = 1L;

	public LetProgramValueHistory() {
		super();
	}

	public void setId(LetProgramValueHistoryId id) {
		this.id = id;
	}

	public LetProgramValueHistoryId getId() {
		
		return id;
	}

	public String getInspectionParamValue() {
		return StringUtils.trimToEmpty(inspectionParamValue);
	}

	public void setInspectionParamValue(String inspectionParamValue) {
		this.inspectionParamValue = inspectionParamValue;
	}

	public String getInspectionParamUnit() {
		return StringUtils.trimToEmpty(inspectionParamUnit);
	}

	public void setInspectionParamUnit(String inspectionParamUnit) {
		this.inspectionParamUnit = inspectionParamUnit;
	}

	@Override
	public String toString() {
		return toString(getId().getProductId(),getId().getTestSeq(),getId().getHistorySeq(),getId().getInspectionPgmId(),getId().getInspectionParamId(),getId().getInspectionParamType());
}
	
}
