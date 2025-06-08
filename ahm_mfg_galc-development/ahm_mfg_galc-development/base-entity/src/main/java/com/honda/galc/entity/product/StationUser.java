package com.honda.galc.entity.product;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;


/**
 * 
 * <h3>QiDefectResult Class description</h3>
 * <p>
 * StationUser contains the getter and setter of the Users station
 * </p>
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
 * 
 * </TABLE>
 * 
 * @author LnTInfotech<br>
 *        Oct 07, 2016
 * 
 */
@Entity
@Table(name = "STATION_USER_TBX")
public class StationUser extends AuditEntry  {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private StationUserId id;
	
	@Column(name = "LOGIN_TIMESTAMP")
	private Timestamp loginTimestamp;
	

	
	public StationUser() {
		super();
	}

	public StationUser(StationUserId qiStationUserId) {
		this.id = qiStationUserId;
	}

	/**
	 * @return the loginTimestamp
	 */
	public Timestamp getLoginTimestamp() {
		return loginTimestamp;
	}

	/**
	 * @param loginTimestamp the loginTimestamp to set
	 */
	public void setLoginTimestamp(Timestamp loginTimestamp) {
		this.loginTimestamp = loginTimestamp;
	}

	/**
	 * @return the id
	 */
	public StationUserId getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(StationUserId id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((loginTimestamp == null) ? 0 : loginTimestamp.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		StationUser other = (StationUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (loginTimestamp == null) {
			if (other.loginTimestamp != null)
				return false;
		} else if (!loginTimestamp.equals(other.loginTimestamp))
			return false;
		return true;
	}
	
	

}
