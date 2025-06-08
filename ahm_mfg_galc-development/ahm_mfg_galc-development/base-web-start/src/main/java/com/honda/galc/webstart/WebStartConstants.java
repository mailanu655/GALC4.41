package com.honda.galc.webstart;

public abstract class WebStartConstants {
	public static final String ACTION_SFX = ".act";
	
	public static final String CODEBASE = "codebase";
	public static final String DISPATCHER_URL = "dispatcher_url";
	public static final String JNLP_ARGS = "jnlp_args";
	public static final String LINE_ID = "line_id";
	public static final String JNLP = "jnlp";
	public static final String HOME_DIR = "home_dir";
	public static final String TARGET_BUILD = "target";
	public static final String TERMINALS = "terminals";
	public static final String AVAILABLE_TERMINALS = "available_terminals";
	public static final String WEBSTART_CLIENT = "webstart_client";
	public static final String CLIENTS = "clients";
	public static final String IP_ADDRESS = "ip_address";
	public static final String OLD_IP_ADDRESS = "old_ip_address";
	public static final String DEFAULT_IP_ADDRESS = "DEFAULT";
	public static final String HOST_NAME = "host_name";
	public static final String CLIENT_DESCRIPTION = "client_description";
	public static final String ERROR = "ERROR";
	public static final String BUILDS = "builds";
	public static final String WEBSTART_BUILD = "webstart_build";
	public static final String BUILD_ID = "build_id";
	public static final String BUILD_DESCRIPTION = "build_description";
	public static final String DESCRIPTION = "description";
	public static final String JAR_URL = "jar_url";
	public static final String JAR_FILES = "jar_files";
	public static final String BUILD_DATE = "build_date";
	public static final String DEFAULT_BUILD_ID = "DEFAULT";
	public static final String MAIN_CLASS = "main_class";
	public static final String LOOK_AND_FEEL = "look_and_feel";
	public static final String LAST_MODIFIED = "last_modified";
	public static final String CELL_BUILDS = "cell_builds";
	public static final String DEF_BUILDS = "def_builds";
	public static final String CELL_ID = "cell_id";
	public static final String CURRENT_CELL_ID = "current_cell_id";
	public static final String IS_NEW_CELL = "is_new_cell";
	public static final String ASSET_NUMBER = "asset_number";
	public static final String COLUMN_LOCATION = "column_location";
	public static final String SHUTDOWN_FLAG = "shutdown_flag";
	public static final String SHUTDOWN_CHECKBOX = "shutdown_checkbox";
	public static final String CHECKBOX_ON = "on";
	public static final String TERMINAL_ID = "terminal_id";
	public static final String APPLICATION = "application";
	public static final String JNLPNAME="jnlpname";
	public static final String FEATURETYPES = "featuretypes";
	public static final String FEATUREIDS = "featureids";
	public static final String FEATURETYPE = "feature_type";
	public static final String FEATUREID = "feature_id";
	public static final String PHONE_EXTENSION = "phone_extension";
	public static final String INITIAL_HEAP_SIZE = "initial_heap_size";
	public static final String MAX_HEAP_SIZE = "max_heap_size";
	

	public enum Action { ADD_CLIENT, EDIT_CLIENT, DELETE_CLIENT, UPDATE_CLIENT, UPDATE_CLIENTS,
        LIST_CLIENTS, SHOW_CLIENT, ADD_BUILD, EDIT_BUILD, DELETE_BUILD,
        UPDATE_BUILD, LIST_BUILDS, SHOW_BUILD, 
        SET_DEFAULT_BUILD, LIST_TERMINALS, HOME;
			
		public String action() {
			return name() + ACTION_SFX;
		}
	}
}