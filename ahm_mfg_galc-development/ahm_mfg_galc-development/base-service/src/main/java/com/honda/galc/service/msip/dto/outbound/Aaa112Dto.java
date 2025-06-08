package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.util.ToStringUtil;

/*
 * 
 * @author Anusha Gopalan
 * @date Dec 12, 2017
*/

public class Aaa112Dto extends BaseOutboundDto implements IMsipOutboundDto {

	private static final long serialVersionUID = 1L;
	private String accountBudget;
	private String event;
	private String time;
	private String company;
	private String costCenter;
	private String profitCenter;
	private String product;
	private String sldToDestination;
	private String interCompany;
	private String varianceDetail;
	private String auditTrail;
	private String rptCurrency;
	private String measures;
	private int quantity = 0;
	private String endMark;	
	private boolean isError;
	private String errorMsg;	
	
	public String getAccountBudget() {
		return accountBudget;
	}
	public void setAccountBudget(String accountBudget) {
		this.accountBudget = accountBudget;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCostCenter() {
		return costCenter;
	}
	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}
	public String getProfitCenter() {
		return profitCenter;
	}
	public void setProfitCenter(String profitCenter) {
		this.profitCenter = profitCenter;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getSldToDestination() {
		return sldToDestination;
	}
	public void setSldToDestination(String sldToDestination) {
		this.sldToDestination = sldToDestination;
	}
	public String getInterCompany() {
		return interCompany;
	}
	public void setInterCompany(String interCompany) {
		this.interCompany = interCompany;
	}
	public String getVarianceDetail() {
		return varianceDetail;
	}
	public void setVarianceDetail(String varianceDetail) {
		this.varianceDetail = varianceDetail;
	}
	public String getAuditTrail() {
		return auditTrail;
	}
	public void setAuditTrail(String auditTrail) {
		this.auditTrail = auditTrail;
	}
	public String getRptCurrency() {
		return rptCurrency;
	}
	public void setRptCurrency(String rptCurrency) {
		this.rptCurrency = rptCurrency;
	}
	public String getMeasures() {
		return measures;
	}
	public void setMeasures(String measures) {
		this.measures = measures;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getEndMark() {
		return endMark;
	}

	public void setEndMark(String endMark) {
		this.endMark = endMark;
	}
	@Override
	public String getSiteName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlantName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProcessPointId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getIsError() {
		// TODO Auto-generated method stub
		return isError;
	}

	@Override
	public String getErrorMsg() {
		// TODO Auto-generated method stub
		return errorMsg;
	}

	@Override
	public void setIsError(Boolean isError) {
		// TODO Auto-generated method stub
		this.isError = isError;
		
	}

	@Override
	public void setErrorMsg(String errorMsg) {
		// TODO Auto-generated method stub
		this.errorMsg = errorMsg;
	
	}
	public String getVersion() {
		return version;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + quantity;
		result = prime * result + ((accountBudget == null) ? 0 : accountBudget.hashCode());
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((costCenter == null) ? 0 : costCenter.hashCode());
		result = prime * result + ((profitCenter == null) ? 0 : profitCenter.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + ((sldToDestination == null) ? 0 : sldToDestination.hashCode());
		result = prime * result + ((interCompany == null) ? 0 : interCompany.hashCode());
		result = prime * result + ((varianceDetail == null) ? 0 : varianceDetail.hashCode());
		result = prime * result + ((auditTrail == null) ? 0 : auditTrail.hashCode());
		result = prime * result + ((rptCurrency == null) ? 0 : rptCurrency.hashCode());
		result = prime * result + ((measures == null) ? 0 : measures.hashCode());
		result = prime * result + ((endMark == null) ? 0 : endMark.hashCode());
		result = prime * result + ((errorMsg == null) ? 0 : errorMsg.hashCode());
		result = prime * result + (isError ? 1231 : 1237);
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
		Aaa112Dto other = (Aaa112Dto) obj;
		if (accountBudget == null) {
			if (other.accountBudget != null)
				return false;
		} else if (!accountBudget.equals(other.accountBudget))
			return false;
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (costCenter == null) {
			if (other.costCenter != null)
				return false;
		} else if (!costCenter.equals(other.costCenter))
			return false;
		if (profitCenter == null) {
			if (other.profitCenter != null)
				return false;
		} else if (!profitCenter.equals(other.profitCenter))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (sldToDestination == null) {
			if (other.sldToDestination != null)
				return false;
		} else if (!sldToDestination.equals(other.sldToDestination))
			return false;
		if (interCompany == null) {
			if (other.interCompany != null)
				return false;
		} else if (!interCompany.equals(other.interCompany))
			return false;
		if (varianceDetail == null) {
			if (other.varianceDetail != null)
				return false;
		} else if (!varianceDetail.equals(other.varianceDetail))
			return false;
		if (auditTrail == null) {
			if (other.auditTrail != null)
				return false;
		} else if (!auditTrail.equals(other.auditTrail))
			return false;
		if (rptCurrency == null) {
			if (other.rptCurrency != null)
				return false;
		} else if (!rptCurrency.equals(other.rptCurrency))
			return false;
		if (measures == null) {
			if (other.measures != null)
				return false;
		} else if (!measures.equals(other.measures))
			return false;
		if (endMark == null) {
			if (other.endMark != null)
				return false;
		} else if (!endMark.equals(other.endMark))
			return false;
		if (errorMsg == null) {
			if (other.errorMsg != null)
				return false;
		} else if (!errorMsg.equals(other.errorMsg))
			return false;
		if (!isError != other.isError)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
