package com.honda.galc.service.msip.dto.inbound;

import com.honda.galc.util.ToStringUtil;


/**
 * @author Anusha Gopalan
 * @date May 17, 2017
 */

public class Gpp401Dto implements IPlanCodeDto {
	
	private static final long serialVersionUID = 1L;


	private String productionDate;
	private String dayOfWeek;
	private String weekNo;

    public Gpp401Dto() {
    }
    
    public String getId() {
    	return getProductionDate();
    }
	public String getProductionDate() {
		return this.productionDate;
	}

	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}

	public String getDayOfWeek() {
		return this.dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getWeekNo() {
		return this.weekNo;
	}

	public void setWeekNo(String weekNo) {
		this.weekNo = weekNo;
	}

	public String getPlanCode() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime * result + ((dayOfWeek == null) ? 0 : dayOfWeek.hashCode());
		result = prime * result + ((weekNo == null) ? 0 : weekNo.hashCode());
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
		Gpp401Dto other = (Gpp401Dto) obj;
		if (productionDate == null) {
			if (other.productionDate != null)
				return false;
		} else if (!productionDate.equals(other.productionDate))
			return false;
		if (dayOfWeek == null) {
			if (other.dayOfWeek != null)
				return false;
		} else if (!dayOfWeek.equals(other.dayOfWeek))
			return false;
		if (weekNo == null) {
			if (other.weekNo != null)
				return false;
		} else if (!weekNo.equals(other.weekNo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}

}