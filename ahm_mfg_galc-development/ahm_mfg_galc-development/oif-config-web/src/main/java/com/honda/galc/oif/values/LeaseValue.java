package com.honda.galc.oif.values;

import java.util.Date;

public class LeaseValue {
	private String owner;
	private Date expire;
	private String disabled;
	
	/**
	 * @param owner
	 * @param expire
	 * @param disabled
	 */
	public LeaseValue(String owner, Date expire, String disabled) {
		this.owner = owner;
		this.expire = expire;
		this.disabled = disabled;
	}

	public String getDisabled() {
		return disabled;
	}

	public Date getExpire() {
		return expire;
	}

	public String getOwner() {
		return owner;
	}
}

