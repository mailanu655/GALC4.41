package com.honda.galc.entity.lcvinbom;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * The primary key class for the LET_PARTIAL_CHECK database table.
 * 
 */
@Embeddable
public class LetPartialCheckId implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="CATEGORY_CODE_ID", unique=true, nullable=false)
	private Long categoryCodeId;

	@Column(name="LET_INSPECTION_NAME", unique=true, nullable=false, length=255)
	private String letInspectionName;

	public LetPartialCheckId() {
	}
	
	public Long getCategoryCodeId() {
		return categoryCodeId;
	}
	
	public void setCategoryCodeId(Long categoryCodeId) {
		this.categoryCodeId = categoryCodeId;
	}

	public String getLetInspectionName() {
		return StringUtils.trim(this.letInspectionName);
	}

	public void setLetInspectionName(String letInspectionName) {
		this.letInspectionName = letInspectionName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoryCodeId == null) ? 0 : categoryCodeId.hashCode());
		result = prime * result + ((letInspectionName == null) ? 0 : letInspectionName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LetPartialCheckId other = (LetPartialCheckId) obj;
		if (categoryCodeId == null) {
			if (other.categoryCodeId != null)
				return false;
		} else if (!categoryCodeId.equals(other.categoryCodeId))
			return false;
		if (letInspectionName == null) {
			if (other.letInspectionName != null)
				return false;
		} else if (!letInspectionName.equals(other.letInspectionName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(),
				getCategoryCodeId(), getLetInspectionName());
	}
	
	
}