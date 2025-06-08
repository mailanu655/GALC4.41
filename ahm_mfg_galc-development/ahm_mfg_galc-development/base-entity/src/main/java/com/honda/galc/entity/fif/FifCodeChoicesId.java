package com.honda.galc.entity.fif;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.apache.commons.lang.StringUtils;


/**
 * 
 * <h3>FifCodeChoicesId.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> FifCodeChoicesId.java description </p>
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
 * <TD>Xiaomei Ma</TD>
 * <TD>Feb 17, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 * </TABLE>
 */

@Embeddable
public class FifCodeChoicesId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "PLANT_CD")
	private String plantCd;

	@Column(name = "MODEL_YEAR")
	private String modelYear;

	@Column(name = "MODEL_CD")
	private String modelCd;

	@Column(name = "DEV_SEQ_CD")
	private String devSeqCd;

	@Column(name = "FIF_CODE")
	private String fifCode;

	@Column(name = "FIF_TYPE")
	private String fifType;

	@Column(name = "GROUP_CD")
	private String groupCd;

	@Column(name = "EFCT_BEG_DT")
	private Date efctBegDt;
	
	public FifCodeChoicesId(){
		super();
	}

	public FifCodeChoicesId(String plantCd, String modelYear, String modelCd,
			String fifCode, String fifType, String devSeqCd, String groupCd,
			Date efctBegDt) {
		super();
		this.plantCd = plantCd;
		this.modelYear = modelYear;
		this.modelCd = modelCd;
		this.fifCode = fifCode;
		this.fifType = fifType;
		this.devSeqCd = devSeqCd;
		this.groupCd = groupCd;
		this.efctBegDt = efctBegDt;
	}

	public String getPlantCd() {
		return StringUtils.trim(plantCd);
	}

	public void setPlantCd(String plantCd) {
		this.plantCd = plantCd;
	}

	public String getModelYear() {
		return StringUtils.trim(modelYear);
	}

	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}

	public String getModelCd() {
		return StringUtils.trim(modelCd);
	}

	public void setModelCd(String modelCd) {
		this.modelCd = modelCd;
	}

	public String getDevSeqCd() {
		return StringUtils.trim(devSeqCd);
	}

	public void setDevSeqCd(String devSeqCd) {
		this.devSeqCd = devSeqCd;
	}

	public String getFifCode() {
		return StringUtils.trim(fifCode);
	}

	public void setFifCode(String fifCode) {
		this.fifCode = fifCode;
	}

	public String getFifType() {
		return StringUtils.trim(fifType);
	}

	public void setFifType(String fifType) {
		this.fifType = fifType;
	}

	public String getGroupCd() {
		return StringUtils.trim(groupCd);
	}

	public void setGroupCd(String groupCd) {
		this.groupCd = groupCd;
	}

	public Date getEfctBegDt() {
		return efctBegDt;
	}

	public void setEfctBegDt(Date efctBegDt) {
		this.efctBegDt = efctBegDt;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((plantCd == null) ? 0 : plantCd.hashCode());
		result = prime * result
				+ ((modelYear == null) ? 0 : modelYear.hashCode());
		result = prime * result
				+ ((modelCd == null) ? 0 : modelCd.hashCode());
		result = prime * result
				+ ((devSeqCd == null) ? 0 : devSeqCd.hashCode());
		result = prime * result
				+ ((fifCode == null) ? 0 : fifCode.hashCode());
		result = prime * result
				+ ((fifType == null) ? 0 : fifType.hashCode());
		result = prime * result
				+ ((groupCd == null) ? 0 : groupCd.hashCode());
		result = prime * result
				+ ((efctBegDt == null) ? 0 : efctBegDt.hashCode());
		
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
		FifCodeChoicesId other = (FifCodeChoicesId) obj;
		if (plantCd == null) {
			if (other.plantCd != null)
				return false;
		} else if (!plantCd.equals(other.plantCd))
			return false;
		if (modelYear == null) {
			if (other.modelYear != null)
				return false;
		} else if (!modelYear.equals(other.modelYear))
			return false;
		if (devSeqCd == null) {
			if (other.devSeqCd != null)
				return false;
		} else if (!devSeqCd.equals(other.devSeqCd))
			return false;
		if ( fifCode == null) {
			if (other.fifCode != null)
				return false;
		} else if (!fifCode.equals(other.fifCode))
			return false;
		if (fifType == null) {
			if (other.fifType != null)
				return false;
		} else if (!fifType.equals(other.fifType))
			return false;
		if (groupCd == null) {
			if (other.groupCd != null)
				return false;
		} else if (!groupCd.equals(other.groupCd))
			return false;
		if (efctBegDt == null) {
			if (other.efctBegDt != null)
				return false;
		} else if (!efctBegDt.equals(other.efctBegDt))
			return false;
		if (efctBegDt != other.efctBegDt) {
			return false;
		}
		return true;
	}
	
}

