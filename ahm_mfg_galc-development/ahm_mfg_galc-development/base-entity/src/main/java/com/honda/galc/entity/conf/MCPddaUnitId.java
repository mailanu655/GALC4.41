package com.honda.galc.entity.conf;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.util.StringUtil;

/**
 * The primary key class for the MC_PDDA_UNIT_TBX database table.
 * 
 */
@Embeddable
public class MCPddaUnitId implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="OPERATION_NAME", unique=true, nullable=false, length=32)
	private String operationName;

	@Column(name="PDDA_PLATFORM_ID", unique=true, nullable=false)
	private int pddaPlatformId;

	@Column(name="UNIT_NO", unique=true, nullable=false, length=6)
	private String unitNo;

	@Column(name="REV_ID", unique=true, nullable=false)
	private long revId;

    public MCPddaUnitId() {
    }
	public String getOperationName() {
		return this.operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public int getPddaPlatformId() {
		return this.pddaPlatformId;
	}
	public void setPddaPlatformId(int pddaPlatformId) {
		this.pddaPlatformId = pddaPlatformId;
	}
	public String getUnitNo() {
		return StringUtils.trim(this.unitNo);
	}
	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}
	public long getRevId() {
		return this.revId;
	}
	public void setRevId(long revId) {
		this.revId = revId;
	}

	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getOperationName(), getUnitNo(), getPddaPlatformId(), getUnitNo());
	}
	
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MCPddaUnitId)) {
			return false;
		}
		MCPddaUnitId castOther = (MCPddaUnitId)other;
		return 
			this.operationName.equals(castOther.operationName)
			&& (this.pddaPlatformId == castOther.pddaPlatformId)
			&& this.unitNo.equals(castOther.unitNo)
			&& (this.revId == castOther.revId);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.operationName.hashCode();
		hash = hash * prime + this.pddaPlatformId;
		hash = hash * prime + this.unitNo.hashCode();
		hash = hash * prime + ((int) (this.revId ^ (this.revId >>> 32)));
		
		return hash;
    }
}