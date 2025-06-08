package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.service.msip.util.OutputData;
/*
 * 
 * @author Parthasarathy Palanisamy
 * @date Nov 17, 2017
 */

public class QicsDeftHeaderDto {

	@OutputData("HDR_DESC")
	private String hdrDesc;

	@OutputData("HDR_FILLER1")
	private String filler1;

	@OutputData("HDR_PLANT")
	private String hdrPlant;

	@OutputData("HDR_FILLER2")
	private String filler2;

	@OutputData("HDR_TOTAL_RECS_CNT")
	private String totalRecsCnt;

	@OutputData("HDR_FILLER3")
	private String filler3;

	@OutputData("HDR_CREATE_TIMESTAMP")
	private String createTimestamp;

	@OutputData("HDR_FILLER4")
	private String filler4;

	private int totalRocordsSize;
	
	
	public int getTotalRocordsSize() {
		return totalRocordsSize;
	}

	public void setTotalRocordsSize(int totalRocordsSize) {
		this.totalRocordsSize = totalRocordsSize;
	}

	public String getHdrDesc()  {
		return hdrDesc;
	}

	public void setHdrDesc(String newHdrDesc)  {
		hdrDesc=newHdrDesc;
	}

	public String getFiller1()  {
		return filler1;
	}

	public void setFiller1(String newFiller1)  {
		filler1=newFiller1;
	}

	public String getHdrPlant()  {
		return hdrPlant;
	}

	public void setHdrPlant(String newHdrPlant)  {
		hdrPlant=newHdrPlant;
	}

	public String getFiller2()  {
		return filler2;
	}

	public void setFiller2(String newFiller2)  {
		filler2=newFiller2;
	}

	public String getTotalRecsCnt()  {
		return totalRecsCnt;
	}

	public void setTotalRecsCnt(String newTotalRecsCnt)  {
		totalRecsCnt=newTotalRecsCnt;
	}

	public String getFiller3()  {
		return filler3;
	}

	public void setFiller3(String newFiller3)  {
		filler3=newFiller3;
	}

	public String getCreateTimestamp()  {
		return createTimestamp;
	}

	public void setCreateTimestamp(String newCreateTimestamp)  {
		createTimestamp=newCreateTimestamp;
	}

	public String getFiller4()  {
		return filler4;
	}

	public void setFiller4(String newFiller4)  {
		filler4=newFiller4;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CoreMQHeaderDTO [hdrDesc=").append(hdrDesc).append(", filler1=").append(filler1)
				.append(", hdrPlant=").append(hdrPlant).append(", filler2=").append(filler2).append(", totalRecsCnt=")
				.append(totalRecsCnt).append(", filler3=").append(filler3).append(", createTimestamp=")
				.append(createTimestamp).append(", filler4=").append(filler4).append("]");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createTimestamp == null) ? 0 : createTimestamp.hashCode());
		result = prime * result + ((filler1 == null) ? 0 : filler1.hashCode());
		result = prime * result + ((filler2 == null) ? 0 : filler2.hashCode());
		result = prime * result + ((filler3 == null) ? 0 : filler3.hashCode());
		result = prime * result + ((filler4 == null) ? 0 : filler4.hashCode());
		result = prime * result + ((hdrDesc == null) ? 0 : hdrDesc.hashCode());
		result = prime * result + ((hdrPlant == null) ? 0 : hdrPlant.hashCode());
		result = prime * result + ((totalRecsCnt == null) ? 0 : totalRecsCnt.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof QicsDeftHeaderDto))
			return false;
		QicsDeftHeaderDto other = (QicsDeftHeaderDto) obj;
		if (createTimestamp == null) {
			if (other.createTimestamp != null)
				return false;
		} else if (!createTimestamp.equals(other.createTimestamp))
			return false;
		if (filler1 == null) {
			if (other.filler1 != null)
				return false;
		} else if (!filler1.equals(other.filler1))
			return false;
		if (filler2 == null) {
			if (other.filler2 != null)
				return false;
		} else if (!filler2.equals(other.filler2))
			return false;
		if (filler3 == null) {
			if (other.filler3 != null)
				return false;
		} else if (!filler3.equals(other.filler3))
			return false;
		if (filler4 == null) {
			if (other.filler4 != null)
				return false;
		} else if (!filler4.equals(other.filler4))
			return false;
		if (hdrDesc == null) {
			if (other.hdrDesc != null)
				return false;
		} else if (!hdrDesc.equals(other.hdrDesc))
			return false;
		if (hdrPlant == null) {
			if (other.hdrPlant != null)
				return false;
		} else if (!hdrPlant.equals(other.hdrPlant))
			return false;
		if (totalRecsCnt == null) {
			if (other.totalRecsCnt != null)
				return false;
		} else if (!totalRecsCnt.equals(other.totalRecsCnt))
			return false;
		return true;
	}
}
