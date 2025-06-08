package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.util.ToStringUtil;

/*
 * 
 * @author Anusha Gopalan
 * @date Dec 12, 2017
 */
//Additional fields not in DailyDepartmentSchedule
public class Gal103HeaderDto{ 

	private String hdrRecordId;

	private String hdrTransCode;

	private String hdrBatchDate;

	private String hdrBatchTime;

	private String space;

	private String hdrBatchSequence;

	private String space1;

	public String getHdrRecordId()  {
		return hdrRecordId;
	}

	public void setHdrRecordId(String newHdrRecordId)  {
		hdrRecordId=newHdrRecordId;
	}

	public String getHdrTransCode()  {
		return hdrTransCode;
	}

	public void setHdrTransCode(String newHdrTransCode)  {
		hdrTransCode=newHdrTransCode;
	}

	public String getHdrBatchDate()  {
		return hdrBatchDate;
	}

	public void setHdrBatchDate(String newHdrBatchDate)  {
		hdrBatchDate=newHdrBatchDate;
	}

	public String getHdrBatchTime()  {
		return hdrBatchTime;
	}

	public void setHdrBatchTime(String newHdrBatchTime)  {
		hdrBatchTime=newHdrBatchTime;
	}

	public String getSpace()  {
		return space;
	}

	public void setSpace(String newSpace)  {
		space=newSpace;
	}

	public String getHdrBatchSequence()  {
		return hdrBatchSequence;
	}

	public void setHdrBatchSequence(String newHdrBatchSequence)  {
		hdrBatchSequence=newHdrBatchSequence;
	}

	public String getSpace1()  {
		return space1;
	}

	public void setSpace1(String newSpace1)  {
		space1=newSpace1;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hdrRecordId == null) ? 0 : hdrRecordId.hashCode());
		result = prime * result + ((hdrTransCode == null) ? 0 : hdrTransCode.hashCode());
		result = prime * result + ((hdrBatchDate == null) ? 0 : hdrBatchDate.hashCode());
		result = prime * result + ((hdrBatchTime == null) ? 0 : hdrBatchTime.hashCode());
		result = prime * result + ((space == null) ? 0 : space.hashCode());
		result = prime * result + ((hdrBatchSequence == null) ? 0 : hdrBatchSequence.hashCode());
		result = prime * result + ((space1 == null) ? 0 : space1.hashCode());
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
		Gal103HeaderDto other = (Gal103HeaderDto) obj;
		if (hdrRecordId == null) {
			if (other.hdrRecordId != null)
				return false;
		} else if (!hdrRecordId.equals(other.hdrRecordId))
			return false;
		if (hdrTransCode == null) {
			if (other.hdrTransCode != null)
				return false;
		} else if (!hdrTransCode.equals(other.hdrTransCode))
			return false;
		if (hdrBatchDate == null) {
			if (other.hdrBatchDate != null)
				return false;
		} else if (!hdrBatchDate.equals(other.hdrBatchDate))
			return false;
		if (hdrBatchTime == null) {
			if (other.hdrBatchTime != null)
				return false;
		} else if (!hdrBatchTime.equals(other.hdrBatchTime))
			return false;
		if (space == null) {
			if (other.space != null)
				return false;
		} else if (!space.equals(other.space))
			return false;
		if (hdrBatchSequence == null) {
			if (other.hdrBatchSequence != null)
				return false;
		} else if (!hdrBatchSequence.equals(other.hdrBatchSequence))
			return false;
		if (space1 == null) {
			if (other.space1 != null)
				return false;
		} else if (!space1.equals(other.space1))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
