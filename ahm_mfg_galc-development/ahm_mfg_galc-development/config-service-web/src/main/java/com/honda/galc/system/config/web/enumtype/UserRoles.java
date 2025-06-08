package com.honda.galc.system.config.web.enumtype;

public enum UserRoles {
	ALL_USERS("AllUsers"),
	ADMINISTRATOR("Administrator"),
	VIEW_PROCESS_TREE("ViewProcessTree"),
	VIEW_PROCESS_POINT("ViewProcessPoint"),
	EDIT_PROCESS_POINT("EditProcessPoint"),
	VIEW_ADMIN("ViewAdmin"),
	EDIT_ADMIN("EditAdmin"),
	VIEW_USERS("ViewUsers"),
	EDIT_USERS("EditUsers"),
	VIEW_TERMINAL("ViewTerminal"),
	EDIT_TERMINAL("EditTerminal"),
	VIEW_DEVICE("ViewDevice"),
	EDIT_DEVICE("EditDevice"),
	VIEW_APPLICATION("ViewApplication"),
	EDIT_APPLICATION("EditApplication"),
	VIEW_PROCESS("ViewProcess"),
	EDIT_PROCESS("EditProcess"),
	VIEW_ACL("ViewACL"),
	EDIT_ACL("EditACL"),
	OIF_ADMINISTRATOR("OifAdministrator");	
	
	private final String name;
	
	
	private UserRoles(String name) {
		this.name = name;
	}
	
	
	public String getName() {
		return name;
	}
}
