package com.honda.galc.entity.lcvinbom;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * The persistent class for the LET_CATEGORY_CODE database table.
 * 
 */
@Entity
@Table(name="LET_CATEGORY_CODE", schema="LCVINBOM")
public class LetCategoryCode extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CATEGORY_CODE_ID", unique=true, nullable=false)
	private Long categoryCodeId;
	
	@Column(name="NAME", nullable=false, length=255)
	private String name;

	@Column(name="DESCRIPTION", length=255)
	private String description;
	
	@Column(name="INCLUSIVE", nullable=false, columnDefinition="INT(1)")
	private boolean inclusive;

	public LetCategoryCode() {
		
	}

	public Long getCategoryCodeId() {
		return categoryCodeId;
	}

	public void setCategoryCodeId(Long categoryCodeId) {
		this.categoryCodeId = categoryCodeId;
	}

	public String getName() {
		return StringUtils.trim(this.name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return StringUtils.trim(this.description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getInclusive() {
		return inclusive;
	}

	public void setInclusive(boolean inclusive) {
		this.inclusive = inclusive;
	}
	
	@Override
	public Object getId() {
		return this.categoryCodeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoryCodeId == null) ? 0 : categoryCodeId.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + (inclusive ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		LetCategoryCode other = (LetCategoryCode) obj;
		if (categoryCodeId == null) {
			if (other.categoryCodeId != null)
				return false;
		} else if (!categoryCodeId.equals(other.categoryCodeId))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (inclusive != other.inclusive)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString(getId(), getName(), getDescription(), getInclusive());
	}	
}
