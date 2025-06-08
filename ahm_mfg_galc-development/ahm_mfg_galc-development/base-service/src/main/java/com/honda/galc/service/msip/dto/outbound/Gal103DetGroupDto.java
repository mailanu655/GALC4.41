package com.honda.galc.service.msip.dto.outbound;

import java.util.List;

import com.honda.galc.util.ToStringUtil;
/*
 * 
 * @author Anusha Gopalan
 * @date Dec 12, 2017
 */
public class Gal103DetGroupDto{
	private List<Gal103DetailDto> gal103DetailRec;
	private Gal103SummaryDto gal103SummaryRec;
	
	public List<Gal103DetailDto> getGal103DetailRec() {
		return gal103DetailRec;
	}
	public void setGal103DetailRec(List<Gal103DetailDto> gal103DetailRec) {
		this.gal103DetailRec = gal103DetailRec;
	}
	public Gal103SummaryDto getGal103SummaryRec() {
		return gal103SummaryRec;
	}
	public void setGal103SummaryRec(Gal103SummaryDto gal103SummaryRec) {
		this.gal103SummaryRec = gal103SummaryRec;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gal103DetailRec == null) ? 0 : gal103DetailRec.hashCode());
		result = prime * result + ((gal103SummaryRec == null) ? 0 : gal103SummaryRec.hashCode());
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
		Gal103DetGroupDto other = (Gal103DetGroupDto) obj;
		if (gal103DetailRec == null) {
			if (other.gal103DetailRec != null)
				return false;
		} else if (!gal103DetailRec.equals(other.gal103DetailRec))
			return false;
		if (gal103SummaryRec == null) {
			if (other.gal103SummaryRec != null)
				return false;
		} else if (!gal103SummaryRec.equals(other.gal103SummaryRec))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
	
}
