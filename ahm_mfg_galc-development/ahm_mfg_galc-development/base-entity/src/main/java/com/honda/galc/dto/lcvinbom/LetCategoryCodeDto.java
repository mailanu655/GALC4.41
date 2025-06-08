package com.honda.galc.dto.lcvinbom;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.IDto;
import com.honda.galc.util.StringUtil;

public class LetCategoryCodeDto implements IDto {
	private static final long serialVersionUID = 1L;
	
	private Long categoryCodeId;
	private String name;
	private String description;
	private boolean inclusive;
	private List<String> inspectionNames;
	
	public LetCategoryCodeDto() {
	}

	public Long getCategoryCodeId() {
		return categoryCodeId;
	}

	public void setCategoryCodeId(Long categoryCodeId) {
		this.categoryCodeId = categoryCodeId;
	}

	public String getName() {
		return StringUtils.trim(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return StringUtils.trim(description);
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

	public List<String> getInspectionNames() {
		return inspectionNames;
	}

	public void setInspectionNames(List<String> inspectionNames) {
		this.inspectionNames = inspectionNames;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoryCodeId == null) ? 0 : categoryCodeId.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + (inclusive ? 1231 : 1237);
		result = prime * result + ((inspectionNames == null) ? 0 : inspectionNames.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		LetCategoryCodeDto other = (LetCategoryCodeDto) obj;
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
		if (inspectionNames == null) {
			if (other.inspectionNames != null)
				return false;
		} else if (!inspectionNames.equals(other.inspectionNames))
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
		return StringUtil.toString(getClass().getSimpleName(), 
				getCategoryCodeId(), getName(), getDescription(), 
				getInclusive(), getInspectionNames());
	}
}