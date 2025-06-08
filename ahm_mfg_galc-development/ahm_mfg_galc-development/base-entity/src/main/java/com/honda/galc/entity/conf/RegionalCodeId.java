package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;

@Embeddable
public class RegionalCodeId implements Serializable {
	
    private static final long serialVersionUID = 1L;

    @Column(name = "REGIONAL_CODE_NAME")
    @Auditable(isPartOfPrimaryKey = true, sequence = 1)
    private String regionalCodeName;

    @Column(name = "REGIONAL_VALUE")
    @Auditable(isPartOfPrimaryKey = true, sequence = 2)
    private String regionalValue;

    public RegionalCodeId() {
        super();
    }

    public RegionalCodeId(String regionalCodeName, String regionalValue) {
        this.regionalCodeName = regionalCodeName;
        this.regionalValue = regionalValue;
    }
    
    public String getRegionalCodeName() {
        return StringUtils.trim(regionalCodeName);
    }

    public void setRegionalCodeName(String regionalCodeName) {
        this.regionalCodeName = regionalCodeName;
    }

    public String getRegionalValue() {
        return StringUtils.trim(regionalValue);
    }

    public void setRegionalValue(String regionalValue) {
        this.regionalValue = regionalValue;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((regionalValue == null) ? 0 : regionalValue.hashCode());
		result = prime * result + ((regionalCodeName == null) ? 0 : regionalCodeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		RegionalCodeId other = (RegionalCodeId) obj;
		if (regionalValue == null) {
			if (other.regionalValue != null)
				return false;
		} else if (!regionalValue.equals(other.regionalValue))
			return false;
		if (regionalCodeName == null) {
			if (other.regionalCodeName != null)
				return false;
		} else if (!regionalCodeName.equals(other.regionalCodeName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RegionalCodeId [regionalCodeName=" + regionalCodeName + ", regionalValue=" + regionalValue + "]";
	}
}
