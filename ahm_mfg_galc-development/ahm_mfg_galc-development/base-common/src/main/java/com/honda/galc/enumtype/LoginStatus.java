package com.honda.galc.enumtype;

public enum LoginStatus {
	OK(""),
	LIGHT_SECURITY_OK(""),
	CANCEL(""),
	USER_NOT_EXIST("User name does not exist"),
	PASSWORD_INCORRECT("Incorrect password"),
	PASSWORD_EXPIRED("Password expired"),
	PASSWORD_REQUIRED("Password required"),
	NO_ACCESS_PERMISSION("No access permission"),
	AUTHENTICATION_ERROR("LDAP Server error"),
	AD_DISABLED_ACCOUNT("Disabled Account");
	
	private String message;
	
	private LoginStatus(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		 return message;
 	}
}
