package com.honda.galc.oif.dto;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.OutputData;

/**
 * 
 * <h3>QiCoreMQHeaderDTO.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> POJO class to hold the Core MQ(Market Quality) data header information </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>vcc01417</TD>
 * <TD>April 7, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author vcc01417
 * @created April 7, 2017
 */

public class QiCoreMQHeaderDTO  implements IOutputFormat {

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

	public String getHdrDesc() {
		return StringUtils.trimToEmpty(hdrDesc);
	}

	public void setHdrDesc(String hdrDesc) {
		this.hdrDesc = hdrDesc;
	}

	public String getFiller1() {
		return filler1;
	}

	public void setFiller1(String filler1) {
		this.filler1 = filler1;
	}

	public String getHdrPlant() {
		return StringUtils.trimToEmpty(hdrPlant);
	}

	public void setHdrPlant(String hdrPlant) {
		this.hdrPlant = hdrPlant;
	}

	public String getFiller2() {
		return filler2;
	}

	public void setFiller2(String filler2) {
		this.filler2 = filler2;
	}

	public String getTotalRecsCnt() {
		return StringUtils.trimToEmpty(totalRecsCnt);
	}

	public void setTotalRecsCnt(String totalRecsCnt) {
		this.totalRecsCnt = totalRecsCnt;
	}

	public String getFiller3() {
		return filler3;
	}

	public void setFiller3(String filler3) {
		this.filler3 = filler3;
	}

	public String getCreateTimestamp() {
		return StringUtils.trimToEmpty(createTimestamp);
	}

	public void setCreateTimestamp(String createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public String getFiller4() {
		return filler4;
	}

	public void setFiller4(String filler4) {
		this.filler4 = filler4;
	}

	@Override
	public String toString() {
		return "QiCoreMQHeaderDTO [hdrDesc=" + hdrDesc + ", filler1=" + filler1 + ", hdrPlant=" + hdrPlant
				+ ", filler2=" + filler2 + ", totalRecsCnt=" + totalRecsCnt + ", filler3=" + filler3
				+ ", createTimestamp=" + createTimestamp + ", filler4=" + filler4 + "]";
	}

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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiCoreMQHeaderDTO other = (QiCoreMQHeaderDTO) obj;
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
