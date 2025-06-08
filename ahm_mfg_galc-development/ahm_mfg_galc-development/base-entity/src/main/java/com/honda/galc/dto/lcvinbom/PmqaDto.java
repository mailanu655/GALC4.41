package com.honda.galc.dto.lcvinbom;

import java.util.List;

import com.honda.galc.dto.IDto;
import com.honda.galc.util.StringUtil;

public class PmqaDto implements IDto {
	private static final long serialVersionUID = 1L;
	
	private String product_id;
	private int inspection_passing_flag;
	private List<String> defect_items;
	
	public PmqaDto() {
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public int getInspection_passing_flag() {
		return inspection_passing_flag;
	}

	public void setInspection_passing_flag(int inspection_passing_flag) {
		this.inspection_passing_flag = inspection_passing_flag;
	}

	public List<String> getDefect_items() {
		return defect_items;
	}

	public void setDefect_items(List<String> defect_items) {
		this.defect_items = defect_items;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((defect_items == null) ? 0 : defect_items.hashCode());
		result = prime * result + inspection_passing_flag;
		result = prime * result + ((product_id == null) ? 0 : product_id.hashCode());
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
		PmqaDto other = (PmqaDto) obj;
		if (defect_items == null) {
			if (other.defect_items != null)
				return false;
		} else if (!defect_items.equals(other.defect_items))
			return false;
		if (inspection_passing_flag != other.inspection_passing_flag)
			return false;
		if (product_id == null) {
			if (other.product_id != null)
				return false;
		} else if (!product_id.equals(other.product_id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return StringUtil.toString(getClass().getSimpleName(), 
				getProduct_id(), getInspection_passing_flag(), 
				getDefect_items());
	}
}
