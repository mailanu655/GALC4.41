package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;

@Entity
@Table(name = "REGIONAL_CODE_TBX")
public class RegionalCode extends CreateUserAuditEntry {
	
    private static final long serialVersionUID = 1L;

	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true)
	private RegionalCodeId id;
	
    @Column(name = "REGIONAL_VALUE_NAME")
    @Auditable(isPartOfPrimaryKey = false, sequence = 3)
    private String regionalValueName;

    @Column(name = "REGIONAL_VALUE_ABBR")
    @Auditable(isPartOfPrimaryKey = false, sequence = 4)
    private String regionalValueAbbr;
    
    @Column(name = "REGIONAL_VALUE_DESC")
    private String regionalValueDesc;
    
    
    public RegionalCode() {
		super();
	}

	public RegionalCode(RegionalCodeId id) {
    	this.id = id;
    }
    
    public RegionalCodeId getId() {
    	return id;
    }

    public String getRegionalValueName() {
        return StringUtils.trim(regionalValueName);
    }
    
    public void setRegionalValueName(String regionalValueName) {
        this.regionalValueName = regionalValueName;
    }

    public String getRegionalValueAbbr() {
        return StringUtils.trim(regionalValueAbbr);
    }

    public void setRegionalValueAbbr(String regionalValueAbbr) {
        this.regionalValueAbbr = regionalValueAbbr;
    }
    
	public String getRegionalValueDesc() {
		return StringUtils.trim(regionalValueDesc);
	}

	public void setRegionalValueDesc(String regionalValueDesc) {
		this.regionalValueDesc = regionalValueDesc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((regionalValueAbbr == null) ? 0 : regionalValueAbbr.hashCode());
		result = prime * result + ((regionalValueName == null) ? 0 : regionalValueName.hashCode());
		result = prime * result + ((regionalValueDesc == null) ? 0 : regionalValueDesc.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegionalCode other = (RegionalCode) obj;
		if (regionalValueAbbr == null) {
			if (other.regionalValueAbbr != null)
				return false;
		} else if (!regionalValueAbbr.equals(other.regionalValueAbbr))
			return false;
		if (regionalValueName == null) {
			if (other.regionalValueName != null)
				return false;
		} else if (!regionalValueName.equals(other.regionalValueName))
			return false;
		if (regionalValueDesc == null) {
			if (other.regionalValueDesc != null)
				return false;
		} else if (!regionalValueDesc.equals(other.regionalValueDesc))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RegionalCode [" + getId().toString() + ", regionalValueName=" + regionalValueName + 
				", regionalValueAbbr=" + regionalValueAbbr + ", regionalValueDesc=" + regionalValueDesc + "]";
	}
}
