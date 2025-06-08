package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name="CODE_TBX")
public class Code extends AuditEntry {

	@EmbeddedId
	private CodeId id;

	@Column(name="CODE_DESCRIPTION")
	private String codeDescription;

	@Column(name="DIVISION_ID")
	private String divisionId;

	private static final long serialVersionUID = 1L;

	public Code() {
		super();
	}

	public Code(String codeType, String code) {
		this(codeType, code, null, null);
	}

	public Code(String codeType, String code, String codeDescription) {
		this(codeType, code, codeDescription, null);
	}

	public Code(String codeType, String code, String codeDescription, String divisionId) {
		this.id = new CodeId(codeType, code);
		this.codeDescription = codeDescription;
		this.divisionId = divisionId;
	}

	public CodeId getId() {
		return this.id;
	}

	public String getCodeType() {
		return this.id.getCodeType();
	}

	public void setCodeType(String codeType) {
		this.id.setCodeType(codeType);
	}

	public String getCode() {
		return this.id.getCode();
	}

	public void setCode(String code) {
		this.id.setCode(code);
	}

	public String getCodeDescription() {
		return StringUtils.trim(this.codeDescription);
	}

	public void setCodeDescription(String codeDescription) {
		this.codeDescription = codeDescription;
	}

	public String getDivisionId() {
		return StringUtils.trim(this.divisionId);
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	@Override
	public String toString() {
		return toString(getCodeType(),
				getCode(),
				getCodeDescription(),
				getDivisionId());
	}
}
