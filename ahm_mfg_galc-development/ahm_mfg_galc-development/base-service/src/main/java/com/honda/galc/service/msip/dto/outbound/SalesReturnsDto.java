package com.honda.galc.service.msip.dto.outbound;

import java.util.List;

import com.honda.galc.util.ToStringUtil;

/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class SalesReturnsDto extends BaseOutboundDto implements IMsipOutboundDto{
	private static final long serialVersionUID = 1L;
	private SalesReturnsHeaderDto headerRec;
	private List<SalesReturnsDetGroup> salesReturnsDetGroup;
	private SalesReturnsTailerDto tailerRec;
	private String errorMsg;
	private Boolean isError;
	
	public Boolean getIsError() {
		return isError;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public void setIsError(Boolean isError) {
		this.isError = isError;
		
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
		
	}
	
	public String getVersion() {
		return version;
	}
	
	public SalesReturnsHeaderDto getHeaderRec() {
		return headerRec;
	}
	public void setHeaderRec(SalesReturnsHeaderDto headerRec) {
		this.headerRec = headerRec;
	}
	public List<SalesReturnsDetGroup> getSalesReturnsDetGroup() {
		return salesReturnsDetGroup;
	}
	public void setSalesReturnsDetGroup(List<SalesReturnsDetGroup> salesReturnsDetGroup) {
		this.salesReturnsDetGroup = salesReturnsDetGroup;
	}
	public SalesReturnsTailerDto getTailerRec() {
		return tailerRec;
	}
	public void setTailerRec(SalesReturnsTailerDto tailerRec) {
		this.tailerRec = tailerRec;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((headerRec == null) ? 0 : headerRec.hashCode());
		result = prime * result + ((salesReturnsDetGroup == null) ? 0 : salesReturnsDetGroup.hashCode());
		result = prime * result + ((tailerRec == null) ? 0 : tailerRec.hashCode());
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
		SalesReturnsDto other = (SalesReturnsDto) obj;
		if (headerRec == null) {
			if (other.headerRec != null)
				return false;
		} else if (!headerRec.equals(other.headerRec))
			return false;
		if (salesReturnsDetGroup == null) {
			if (other.salesReturnsDetGroup != null)
				return false;
		} else if (!salesReturnsDetGroup.equals(other.salesReturnsDetGroup))
			return false;
		if (tailerRec == null) {
			if (other.tailerRec != null)
				return false;
		} else if (!tailerRec.equals(other.tailerRec))
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
