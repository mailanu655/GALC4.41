package com.honda.galc.dto;

public class BadgeInfoDto implements IDto {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String preferred_name;

	public BadgeInfoDto(String userId, String userName) {
		super();
		this.user_id = userId;
		this.preferred_name = userName;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getPreferred_name() {
		return preferred_name;
	}

	public void setPreferred_name(String preferred_name) {
		this.preferred_name = preferred_name;
	}

	@Override
	public String toString() {
		return "BadgeUser [user_id=" + user_id + ", preferred_name=" + preferred_name + "]";
	}
}
