package com.honda.galc.service.msip.dto.inbound;

import com.honda.galc.util.ToStringUtil;

public class Gal102DetGroup implements IMsipInboundDto{
	private static final long serialVersionUID = 1L;
	private Gal102DetailRecord[] gal102DetailRec;
	private Gal102HeaderRecord gal102HeaderRec;
	
	public Gal102DetailRecord[] getGal102DetailRec() {
		return gal102DetailRec;
	}
	public void setGal102DetailRec(Gal102DetailRecord[] gal102DetailRec) {
		this.gal102DetailRec = gal102DetailRec;
	}
	public Gal102HeaderRecord getGal102HeaderRec() {
		return gal102HeaderRec;
	}
	public void setGal102HeaderRec(Gal102HeaderRecord gal102HeaderRec) {
		this.gal102HeaderRec = gal102HeaderRec;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gal102DetailRec == null) ? 0 : gal102DetailRec.hashCode());
		result = prime * result + ((gal102HeaderRec == null) ? 0 : gal102HeaderRec.hashCode());
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
		Gal102DetGroup other = (Gal102DetGroup) obj;
		if (gal102DetailRec == null) {
			if (other.gal102DetailRec != null)
				return false;
		} else if (!gal102DetailRec.equals(other.gal102DetailRec))
			return false;
		if (gal102HeaderRec == null) {
			if (other.gal102HeaderRec != null)
				return false;
		} else if (!gal102HeaderRec.equals(other.gal102HeaderRec))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
	
}
