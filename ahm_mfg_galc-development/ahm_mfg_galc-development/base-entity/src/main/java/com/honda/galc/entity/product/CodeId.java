package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

@Embeddable
public class CodeId implements Serializable {

	@Column(name="CODE_TYPE")
	private String codeType;

	@Column(name="CODE")
	private String code;

	private static final long serialVersionUID = 1L;

	public CodeId() {
		super();
	}

	public CodeId(String codeType, String code) {
		super();
		this.codeType = codeType;
		this.code = code;
	}

	public String getCodeType() {
		return StringUtils.trim(this.codeType);
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getCode() {
		return StringUtils.trim(this.code);
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof CodeId)) {
			return false;
		}
		CodeId other = (CodeId) o;
		return this.codeType.equals(other.codeType)
				&& this.code.equals(other.code);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codeType.hashCode();
		hash = hash * prime + this.code.hashCode();
		return hash;
	}

}
