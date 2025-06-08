package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.util.ToStringUtil;

/*
 * 
 * @author Anusha Gopalan
 * @date Dec 12, 2017
 */
//Additional fields not in DailyDepartmentSchedule
public class Gal103TailDto{ 

	private String trlRecordId;

	private String trlTransCode;

	private String trlHashTotal;

	private String space7;

	public String getTrlRecordId()  {
		return trlRecordId;
	}

	public void setTrlRecordId(String newTrlRecordId)  {
		trlRecordId=newTrlRecordId;
	}

	public String getTrlTransCode()  {
		return trlTransCode;
	}

	public void setTrlTransCode(String newTrlTransCode)  {
		trlTransCode=newTrlTransCode;
	}

	public String getTrlHashTotal()  {
		return trlHashTotal;
	}

	public void setTrlHashTotal(String newTrlHashTotal)  {
		trlHashTotal=newTrlHashTotal;
	}

	public String getSpace7()  {
		return space7;
	}
	
	public void setSpace7(String newSpace7)  {
		space7=newSpace7;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((trlRecordId == null) ? 0 : trlRecordId.hashCode());
		result = prime * result + ((trlTransCode == null) ? 0 : trlTransCode.hashCode());
		result = prime * result + ((trlHashTotal == null) ? 0 : trlHashTotal.hashCode());
		result = prime * result + ((space7 == null) ? 0 : space7.hashCode());
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
		Gal103TailDto other = (Gal103TailDto) obj;
		if (trlRecordId == null) {
			if (other.trlRecordId != null)
				return false;
		} else if (!trlRecordId.equals(other.trlRecordId))
			return false;
		if (trlTransCode == null) {
			if (other.trlTransCode != null)
				return false;
		} else if (!trlTransCode.equals(other.trlTransCode))
			return false;
		if (trlHashTotal == null) {
			if (other.trlHashTotal != null)
				return false;
		} else if (!trlHashTotal.equals(other.trlHashTotal))
			return false;
		if (space7 == null) {
			if (other.space7 != null)
				return false;
		} else if (!space7.equals(other.space7))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
