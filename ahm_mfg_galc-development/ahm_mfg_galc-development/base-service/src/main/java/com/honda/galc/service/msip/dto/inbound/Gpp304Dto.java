package com.honda.galc.service.msip.dto.inbound;

import java.sql.Date;
import java.sql.Time;

import com.honda.galc.util.ToStringUtil;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 *
 */
//Additional fields not in DailyDepartmentSchedule
public class Gpp304Dto implements IScheduleDto{
	private static final long serialVersionUID = 1L;
	private String planCode;
	private String plantCode;
	private String lineNo;
	private String processLocation;
	private int onOffFlag;
	private String shift;
	private int lostCapacity;
	private Time startTime;
	private Time endTime;
	private Date productionDate;
	private String comment;
	
	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getProcessLocation() {
		return processLocation;
	}

	public void setProcessLocation(String processLocation) {
		this.processLocation = processLocation;
	}

	public String getShift() {
		return shift;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}	

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public int getOnOffFlag() {
		return onOffFlag;
	}

	public void setOnOffFlag(int onOffFlag) {
		this.onOffFlag = onOffFlag;
	}

	public int getLostCapacity() {
		return lostCapacity;
	}

	public void setLostCapacity(int lostCapacity) {
		this.lostCapacity = lostCapacity;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + onOffFlag;
		result = prime * result + lostCapacity;
		result = prime * result + ((planCode == null) ? 0 : planCode.hashCode());
		result = prime * result + ((plantCode == null) ? 0 : plantCode.hashCode());
		result = prime * result + ((lineNo == null) ? 0 : lineNo.hashCode());
		result = prime * result + ((processLocation == null) ? 0 : processLocation.hashCode());
		result = prime * result + ((shift == null) ? 0 : shift.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
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
		Gpp304Dto other = (Gpp304Dto) obj;
		if (onOffFlag != other.onOffFlag)
			return false;
		if (lostCapacity != other.lostCapacity)
			return false;
		if (planCode == null) {
			if (other.planCode != null)
				return false;
		} else if (!planCode.equals(other.planCode))
			return false;
		if (plantCode == null) {
			if (other.plantCode != null)
				return false;
		} else if (!plantCode.equals(other.plantCode))
			return false;
		if (lineNo == null) {
			if (other.lineNo != null)
				return false;
		} else if (!lineNo.equals(other.lineNo))
			return false;
		if (processLocation == null) {
			if (other.processLocation != null)
				return false;
		} else if (!processLocation.equals(other.processLocation))
			return false;
		if (shift == null) {
			if (other.shift != null)
				return false;
		} else if (!shift.equals(other.shift))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (productionDate == null) {
			if (other.productionDate != null)
				return false;
		} else if (!productionDate.equals(other.productionDate))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}