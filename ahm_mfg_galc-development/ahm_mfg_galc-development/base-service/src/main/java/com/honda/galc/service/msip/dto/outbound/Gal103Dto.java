package com.honda.galc.service.msip.dto.outbound;

import java.util.List;

import com.honda.galc.util.ToStringUtil;

/*
 * 
 * @author Anusha Gopalan
 * @date Dec 12, 2017
 */
public class Gal103Dto extends BaseOutboundDto implements IMsipOutboundDto{
	private static final long serialVersionUID = 1L;
	private Gal103HeaderDto gal103HeaderRec;
	private List<Gal103DetGroupDto> gal103DetGroup;
	private Gal103TailDto gal103TailerRec;
	private boolean isError;
	private String errorMsg;
	
	public Gal103HeaderDto getGal103HeaderRec() {
		return gal103HeaderRec;
	}
	public void setGal103HeaderRec(Gal103HeaderDto gal103HeaderRec) {
		this.gal103HeaderRec = gal103HeaderRec;
	}
	public List<Gal103DetGroupDto> getGal103DetGroup() {
		return gal103DetGroup;
	}
	public void setGal103DetGroup(List<Gal103DetGroupDto> gal103DetGroup) {
		this.gal103DetGroup = gal103DetGroup;
	}
	public Gal103TailDto getGal103TailerRec() {
		return gal103TailerRec;
	}
	public void setGal103TailerRec(Gal103TailDto gal103TailerRec) {
		this.gal103TailerRec = gal103TailerRec;
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
		result = prime * result + ((gal103HeaderRec == null) ? 0 : gal103HeaderRec.hashCode());
		result = prime * result + ((gal103DetGroup == null) ? 0 : gal103DetGroup.hashCode());
		result = prime * result + ((gal103TailerRec == null) ? 0 : gal103TailerRec.hashCode());
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
		Gal103Dto other = (Gal103Dto) obj;
		if (gal103HeaderRec == null) {
			if (other.gal103HeaderRec != null)
				return false;
		} else if (!gal103HeaderRec.equals(other.gal103HeaderRec))
			return false;
		if (gal103DetGroup == null) {
			if (other.gal103DetGroup != null)
				return false;
		} else if (!gal103DetGroup.equals(other.gal103DetGroup))
			return false;
		if (gal103TailerRec == null) {
			if (other.gal103TailerRec != null)
				return false;
		} else if (!gal103TailerRec.equals(other.gal103TailerRec))
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
