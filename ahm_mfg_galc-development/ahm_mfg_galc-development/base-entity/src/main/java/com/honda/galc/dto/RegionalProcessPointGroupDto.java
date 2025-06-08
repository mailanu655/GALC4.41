package com.honda.galc.dto;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * <h3>Class description</h3>
 * 
 * <h4>Description</h4>
 * <p>
 * </p>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Jul. 10, 2018</TD>
 * <TD>1.0</TD>
 * <TD>20180710</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 */
public class RegionalProcessPointGroupDto implements IDto {
	
	private static final long serialVersionUID = -4777601325567833949L;

    @DtoTag(name = "SITE")
    private String site;
    
	@DtoTag(name = "REGIONAL_VALUE")
	private String regionalValue;

	@DtoTag(name = "REGIONAL_VALUE_NAME")
    private String regionalValueName;

    @DtoTag(name = "REGIONAL_VALUE_ABBR")
    private String regionalValueAbbr;

    @DtoTag(name = "REGIONAL_VALUE_DESC")
    private String regionalValueDesc;

    @DtoTag(name = "CATEGORY_CODE")
    private short categoryCode;

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getRegionalValue() {
		return StringUtils.trimToEmpty(regionalValue);
	}

	public void setRegionalValue(String regionalValue) {
		this.regionalValue = regionalValue;
	}

	public String getRegionalValueName() {
		return StringUtils.trimToEmpty(regionalValueName);
	}

	public void setRegionalValueName(String regionalValueName) {
		this.regionalValueName = regionalValueName;
	}

	public String getRegionalValueAbbr() {
		return StringUtils.trimToEmpty(regionalValueAbbr);
	}

	public void setRegionalValueAbbr(String regionalValueAbbr) {
		this.regionalValueAbbr = regionalValueAbbr;
	}

	public String getRegionalValueDesc() {
		return StringUtils.trimToEmpty(regionalValueDesc);
	}

	public void setRegionalValueDesc(String regionalValueDesc) {
		this.regionalValueDesc = regionalValueDesc;
	}
	
	public short getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(short categoryCode) {
		this.categoryCode = categoryCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime + ((regionalValueName == null) ? 0 : getRegionalValueName().hashCode());
		result = prime * result + ((site == null) ? 0 : getSite().hashCode());
		result = prime * result + ((regionalValue == null) ? 0 : getRegionalValue().hashCode());
		result = prime * result + ((regionalValueName == null) ? 0 : getRegionalValueName().hashCode());
		result = prime * result + ((regionalValueAbbr == null) ? 0 : getRegionalValueAbbr().hashCode());
		result = prime * result + ((regionalValueDesc == null) ? 0 : getRegionalValueDesc().hashCode());
		result = prime * result + categoryCode;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		
		RegionalProcessPointGroupDto other = (RegionalProcessPointGroupDto) obj;

		return StringUtils.equals(getSite(), other.getSite())
				&& StringUtils.equals(getRegionalValue(), other.getRegionalValue())
				&& StringUtils.equals(getRegionalValueName(), other.getRegionalValueName())
				&& StringUtils.equals(getRegionalValueAbbr(), other.getRegionalValueAbbr())
				&& StringUtils.equals(getRegionalValueDesc(), other.getRegionalValueDesc())
				&& getCategoryCode() == other.getCategoryCode();
	}
	    
	@Override
	public String toString() {		
	  return StringUtil.toString(getClass().getSimpleName(), getSite(), getRegionalValueName(), getCategoryCode());
	}
 
}
