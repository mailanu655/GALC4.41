package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.util.ToStringUtil;

/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class SalesReturnsDetGroup{
	private SalesReturnsDetailDto detailRec;
	private SalesReturnsAmtDto amtRec;
	
	public SalesReturnsDetailDto getDetailRec() {
		return detailRec;
	}
	public void setDetailRec(SalesReturnsDetailDto detailRec) {
		this.detailRec = detailRec;
	}
	public SalesReturnsAmtDto getAmtRec() {
		return amtRec;
	}
	public void setAmtRec(SalesReturnsAmtDto amtRec) {
		this.amtRec = amtRec;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((detailRec == null) ? 0 : detailRec.hashCode());
		result = prime * result + ((amtRec == null) ? 0 : amtRec.hashCode());
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
		SalesReturnsDetGroup other = (SalesReturnsDetGroup) obj;
		if (detailRec == null) {
			if (other.detailRec != null)
				return false;
		} else if (!detailRec.equals(other.detailRec))
			return false;
		if (amtRec == null) {
			if (other.amtRec != null)
				return false;
		} else if (!amtRec.equals(other.amtRec))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
