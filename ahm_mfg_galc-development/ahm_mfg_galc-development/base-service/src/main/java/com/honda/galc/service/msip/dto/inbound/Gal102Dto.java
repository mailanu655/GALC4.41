package com.honda.galc.service.msip.dto.inbound;

import com.honda.galc.util.ToStringUtil;


public class Gal102Dto implements IMsipInboundDto{
	private static final long serialVersionUID = 1L;
	private Gal102DetGroup[] gal102DetGroup;
	
	public Gal102DetGroup[] getGal102DetGroup() {
		return gal102DetGroup;
	}
	public void setGal102DetGroup(Gal102DetGroup[] gal102DetGroup) {
		this.gal102DetGroup = gal102DetGroup;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gal102DetGroup == null) ? 0 : gal102DetGroup.hashCode());
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
		Gal102Dto other = (Gal102Dto) obj;
		if (gal102DetGroup == null) {
			if (other.gal102DetGroup != null)
				return false;
		} else if (!gal102DetGroup.equals(other.gal102DetGroup))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
