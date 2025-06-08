package com.honda.galc.service.msip.dto.inbound;

import java.sql.Date;

import com.honda.galc.util.ToStringUtil;


/**
 * @author Anusha Gopalan
 * @date May, 2017
 *
 */
//Additional fields not in DailyDepartmentSchedule
public class Gpp301Dto implements IMsipInboundDto {
	private static final long serialVersionUID = 1L;
	private int capacity;
	private String departmentCode;
	private String onOffFlag;
	private String planCode;
	private String plantCode;
	private String weekNo;
	private String isWork;
	private String lineNo;
	private String processLocation;
	private Date productionDate;
	private String shift;
	private String dayOfWeek;

	public Gpp301Dto() {
	}

// Copy Constructor
	public Gpp301Dto(Gpp301Dto objCapacity) {
		this.capacity = objCapacity.getCapacity();
		this.dayOfWeek = objCapacity.getDayOfWeek();
		this.departmentCode = objCapacity.getDepartmentCode();
		this.isWork = objCapacity.getIsWork();
		this.lineNo = objCapacity.getLineNo();
		this.onOffFlag = objCapacity.getOnOffFlag();
		this.planCode = objCapacity.getPlanCode();
		this.plantCode = objCapacity.getPlantCode();
		this.processLocation = objCapacity.getProcessLocation();
		this.productionDate = objCapacity.getProductionDate();
		this.shift = objCapacity.getShift();
		this.weekNo = objCapacity.getWeekNo();
	}

	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public String getOnOffFlag() {
		return onOffFlag;
	}
	public void setOnOffFlag(String onOffFlag) {
		this.onOffFlag = onOffFlag;
	}
	public String getPlanCode() {
		return planCode;
	}
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	public String getWeekNo() {
		return weekNo;
	}
	public void setWeekNo(String weekNo) {
		this.weekNo = weekNo;
	}
	public String getIsWork() {
		return isWork;
	}
	public void setIsWork(String isWork) {
		this.isWork = isWork;
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
	public Date getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}
	public String getShift() {
		return shift;
	}
	public void setShift(String shift) {
		this.shift = shift;
	}
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}
	public String getPlantCode() {
		return plantCode;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + capacity;
		result = prime * result + ((departmentCode == null) ? 0 : departmentCode.hashCode());
		result = prime * result + ((onOffFlag == null) ? 0 : onOffFlag.hashCode());
		result = prime * result + ((planCode == null) ? 0 : planCode.hashCode());
		result = prime * result + ((plantCode == null) ? 0 : plantCode.hashCode());
		result = prime * result + ((weekNo == null) ? 0 : weekNo.hashCode());
		result = prime * result + ((isWork == null) ? 0 : isWork.hashCode());
		result = prime * result + ((lineNo == null) ? 0 : lineNo.hashCode());
		result = prime * result + ((processLocation == null) ? 0 : processLocation.hashCode());
		result = prime * result + ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime * result + ((shift == null) ? 0 : shift.hashCode());
		result = prime * result + ((dayOfWeek == null) ? 0 : dayOfWeek.hashCode());
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
		Gpp301Dto other = (Gpp301Dto) obj;
		if (capacity != other.capacity)
			return false;
		if (departmentCode == null) {
			if (other.departmentCode != null)
				return false;
		} else if (!departmentCode.equals(other.departmentCode))
			return false;
		if (onOffFlag == null) {
			if (other.onOffFlag != null)
				return false;
		} else if (!onOffFlag.equals(other.onOffFlag))
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
		if (weekNo == null) {
			if (other.weekNo != null)
				return false;
		} else if (!weekNo.equals(other.weekNo))
			return false;
		if (isWork == null) {
			if (other.isWork != null)
				return false;
		} else if (!isWork.equals(other.isWork))
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
		if (productionDate == null) {
			if (other.productionDate != null)
				return false;
		} else if (!productionDate.equals(other.productionDate))
			return false;
		if (shift == null) {
			if (other.shift != null)
				return false;
		} else if (!shift.equals(other.shift))
			return false;
		if (dayOfWeek == null) {
			if (other.dayOfWeek != null)
				return false;
		} else if (!dayOfWeek.equals(other.dayOfWeek))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}