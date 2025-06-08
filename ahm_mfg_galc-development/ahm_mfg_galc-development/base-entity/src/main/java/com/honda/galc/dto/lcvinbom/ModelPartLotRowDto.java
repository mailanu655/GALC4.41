package com.honda.galc.dto.lcvinbom;

import java.util.Date;

import com.honda.galc.dto.IDto;
import com.honda.galc.entity.enumtype.VinBomActiveStatus;
import com.honda.galc.entity.enumtype.VinBomApprovalStatus;
import com.honda.galc.util.StringUtil;

public class ModelPartLotRowDto implements IDto {
	private static final long serialVersionUID = 1L;
	
	private Long modelPartId;
	private String productSpecWildcard;
	private String letSystemName;
	private String dcPartNumber;
	private VinBomActiveStatus active;
	private String dcClass;
	private Date dcEffBegDate;
	private String dcNumber;
	private boolean interchangeable;
	private boolean reflash;
	private boolean scrapParts;
	private String planCode;
	private String startingProductionLot;
	private String model;
	private String modelType;
	private String approveStatus;
	
	
	public ModelPartLotRowDto() {
	}

	public Long getModelPartId() {
		return modelPartId;
	}

	public void setModelPartId(Long modelPartId) {
		this.modelPartId = modelPartId;
	}

	public String getProductSpecWildcard() {
		return productSpecWildcard;
	}

	public void setProductSpecWildcard(String productSpecWildcard) {
		this.productSpecWildcard = productSpecWildcard;
	}

	public String getLetSystemName() {
		return letSystemName;
	}

	public void setLetSystemName(String letSystemName) {
		this.letSystemName = letSystemName;
	}

	public String getDcPartNumber() {
		return dcPartNumber;
	}

	public void setDcPartNumber(String dcPartNumber) {
		this.dcPartNumber = dcPartNumber;
	}

	public VinBomActiveStatus getActive() {
		return active;
	}

	public void setActive(VinBomActiveStatus active) {
		this.active = active;
	}

	public String getDcClass() {
		return dcClass;
	}

	public void setDcClass(String dcClass) {
		this.dcClass = dcClass;
	}

	public Date getDcEffBegDate() {
		return dcEffBegDate;
	}

	public void setDcEffBegDate(Date dcEffBegDate) {
		this.dcEffBegDate = dcEffBegDate;
	}

	public String getDcNumber() {
		return dcNumber;
	}

	public void setDcNumber(String dcNumber) {
		this.dcNumber = dcNumber;
	}

	public boolean getInterchangeable() {
		return interchangeable;
	}

	public void setInterchangeable(boolean interchangeable) {
		this.interchangeable = interchangeable;
	}

	public boolean getReflash() {
		return reflash;
	}

	public void setReflash(boolean reflash) {
		this.reflash = reflash;
	}

	public boolean getScrapParts() {
		return scrapParts;
	}

	public void setScrapParts(boolean scrapParts) {
		this.scrapParts = scrapParts;
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getStartingProductionLot() {
		return startingProductionLot;
	}

	public void setStartingProductionLot(String startingProductionLot) {
		this.startingProductionLot = startingProductionLot;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	
	
	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		
		result = prime * result + ((dcClass == null) ? 0 : dcClass.hashCode());
		result = prime * result + ((dcEffBegDate == null) ? 0 : dcEffBegDate.hashCode());
		result = prime * result + ((dcNumber == null) ? 0 : dcNumber.hashCode());
		result = prime * result + ((dcPartNumber == null) ? 0 : dcPartNumber.hashCode());
		result = prime * result + (interchangeable ? 1231 : 1237);
		result = prime * result + ((letSystemName == null) ? 0 : letSystemName.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((modelPartId == null) ? 0 : modelPartId.hashCode());
		result = prime * result + ((modelType == null) ? 0 : modelType.hashCode());
		result = prime * result + ((planCode == null) ? 0 : planCode.hashCode());
		result = prime * result + ((productSpecWildcard == null) ? 0 : productSpecWildcard.hashCode());
		result = prime * result + (reflash ? 1231 : 1237);
		result = prime * result + (scrapParts ? 1231 : 1237);
		result = prime * result + ((startingProductionLot == null) ? 0 : startingProductionLot.hashCode());
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
		ModelPartLotRowDto other = (ModelPartLotRowDto) obj;
		if (active != other.active)
			return false;
		
		if (dcClass == null) {
			if (other.dcClass != null)
				return false;
		} else if (!dcClass.equals(other.dcClass))
			return false;
		if (dcEffBegDate == null) {
			if (other.dcEffBegDate != null)
				return false;
		} else if (!dcEffBegDate.equals(other.dcEffBegDate))
			return false;
		if (dcNumber == null) {
			if (other.dcNumber != null)
				return false;
		} else if (!dcNumber.equals(other.dcNumber))
			return false;
		if (dcPartNumber == null) {
			if (other.dcPartNumber != null)
				return false;
		} else if (!dcPartNumber.equals(other.dcPartNumber))
			return false;
		if (interchangeable != other.interchangeable)
			return false;
		if (letSystemName == null) {
			if (other.letSystemName != null)
				return false;
		} else if (!letSystemName.equals(other.letSystemName))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (modelPartId == null) {
			if (other.modelPartId != null)
				return false;
		} else if (!modelPartId.equals(other.modelPartId))
			return false;
		if (modelType == null) {
			if (other.modelType != null)
				return false;
		} else if (!modelType.equals(other.modelType))
			return false;
		if (planCode == null) {
			if (other.planCode != null)
				return false;
		} else if (!planCode.equals(other.planCode))
			return false;
		if (productSpecWildcard == null) {
			if (other.productSpecWildcard != null)
				return false;
		} else if (!productSpecWildcard.equals(other.productSpecWildcard))
			return false;
		if (reflash != other.reflash)
			return false;
		if (scrapParts != other.scrapParts)
			return false;
		if (startingProductionLot == null) {
			if (other.startingProductionLot != null)
				return false;
		} else if (!startingProductionLot.equals(other.startingProductionLot))
			return false;
		return true;
	}

	public String toString() {
	    return StringUtil.toString(getClass().getSimpleName(), 
	    		getModelPartId(), getProductSpecWildcard(), getLetSystemName(), 
				getDcPartNumber(), getDcClass(), getDcEffBegDate(), getDcNumber(), 
				getActive(), getInterchangeable(), getReflash(), getScrapParts(), 
				getPlanCode(), getStartingProductionLot());
	}
}
