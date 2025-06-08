package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date Sep 30, 2014
 */
@Embeddable
public class MCAppCheckerId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="APPLICATION_ID", nullable=false, length=16)
	private String applicationId;

	@Column(name="CHECK_POINT", nullable=false)
	private String checkPoint;

	@Column(name="CHECK_SEQ", nullable=false)
	private int checkSeq;
	
	public MCAppCheckerId() {}

	public MCAppCheckerId(String applicationId, String checkPoint, int checkSeq) {
		this.applicationId = applicationId;
		this.checkPoint = checkPoint;
		this.checkSeq = checkSeq;
	}
	
	public String getApplicationId() {
		return StringUtils.trim(applicationId);
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getCheckPoint() {
		return StringUtils.trim(checkPoint);
	}

	public void setCheckPoint(String checkPoint) {
		this.checkPoint = checkPoint;
	}

	public int getCheckSeq() {
		return checkSeq;
	}

	public void setCheckSeq(int checkSeq) {
		this.checkSeq = checkSeq;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((applicationId == null) ? 0 : applicationId.hashCode());
		result = prime * result
				+ ((checkPoint == null) ? 0 : checkPoint.hashCode());
		result = prime * result + checkSeq;
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
		MCAppCheckerId other = (MCAppCheckerId) obj;
		if (applicationId == null) {
			if (other.applicationId != null)
				return false;
		} else if (!applicationId.equals(other.applicationId))
			return false;
		if (checkPoint == null) {
			if (other.checkPoint != null)
				return false;
		} else if (!checkPoint.equals(other.checkPoint))
			return false;
		if (checkSeq != other.checkSeq)
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return StringUtil.toString(this.getClass().getSimpleName(), getApplicationId(), 
				getCheckPoint(), getCheckSeq());
	}
}