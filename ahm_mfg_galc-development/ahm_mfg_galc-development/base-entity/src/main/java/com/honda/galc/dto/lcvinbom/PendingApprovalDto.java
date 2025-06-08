package com.honda.galc.dto.lcvinbom;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.IDto;
import com.honda.galc.util.StringUtil;

public class PendingApprovalDto implements IDto {
	private static final long serialVersionUID = 1L;
	
	private String requestor;
	private String model;
	private String dcNumber;
	private String letSystemName;
	private String dcEffBegDate;
	
	public PendingApprovalDto() {
	}

	public String getRequestor() {
		return StringUtils.trimToEmpty(requestor);
	}

	public void setRequestor(String requestor) {
		this.requestor = requestor;
	}

	public String getModel() {
		return StringUtils.trimToEmpty(model);
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getDcNumber() {
		return StringUtils.trimToEmpty(dcNumber);
	}

	public void setDcNumber(String dcNumber) {
		this.dcNumber = dcNumber;
	}

	public String getLetSystemName() {
		return StringUtils.trimToEmpty(letSystemName);
	}

	public void setLetSystemName(String letSystemName) {
		this.letSystemName = letSystemName;
	}

	public String getDcEffBegDate() {
		return StringUtils.trimToEmpty(dcEffBegDate);
	}

	public void setDcEffBegDate(String dcEffBegDate) {
		this.dcEffBegDate = dcEffBegDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dcEffBegDate == null) ? 0 : dcEffBegDate.hashCode());
		result = prime * result + ((dcNumber == null) ? 0 : dcNumber.hashCode());
		result = prime * result + ((letSystemName == null) ? 0 : letSystemName.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((requestor == null) ? 0 : requestor.hashCode());
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
		PendingApprovalDto other = (PendingApprovalDto) obj;
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
		if (requestor == null) {
			if (other.requestor != null)
				return false;
		} else if (!requestor.equals(other.requestor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return StringUtil.toString(getClass().getSimpleName(), 
				getRequestor(), getModel(), getDcNumber(), getLetSystemName(), 
				getDcEffBegDate());
	}
	
}
