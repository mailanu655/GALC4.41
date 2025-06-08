package com.honda.galc.client;

/**
 * 
 * <h3> Constants</h3>
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Alok Ghode</TD>
 * <TD>May 15, 2014</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * 
 * @author Alok Ghode
 * May 15 2014 
 */
public class ClientConstants {
	
	/**
	 * For Login captions
	 */
	public static final String DEFAULT_LOGIN_WITH_SCANNER_CAPTION = "Scan/Log into VIOS system";
	public static final String DEFAULT_LOGIN_CAPTION = "Log into VIOS system";
	
	public static final String LOGIN_MESSAGE = "Please scan again or log in manually (e.g. vf01234)";
	public static final String LOGIN_MANUALLY_MESSAGE = "Please log in manually (e.g. vf01234)";
	
	public static final String KEYBOARD_CSS_PATH = "/resource/com/honda/galc/client/ui/keypad/css/KeyboardButtonStyle.css";
	public static final String KEYBOARD_FONT_URL = "/resource/com/honda/galc/client/ui/keypad/font/FontKeyboardFX.ttf";
	public static final String TEMP_LOGIN_WATCH_FILE_PATH = System.getProperty("java.io.tmpdir") + "\\login.watch"; 
	public static final String VIOS_BADGE_READER_DLL = "VIOSBadgeReader_" + (System.getProperty("sun.arch.data.model").contains("32") ? "32" : "64");
	
	public static final String MAX_CYCLE_TIME = "MAX_CYCLE_TIME";
	public static final String OVER_CYCLE = "OVER_CYCLE";
	public static final String CURRENT_CYCLE_TIME = "CURRENT_CYCLE_TIME";
	public static final String ACCUMULATION_TIME = "ACCUMULATION_TIME";
	public static final String ON_TARGET = "ON_TARGET";
	public static final String KEYBOARD_BUTTON_ID = "KEYBOARD_BUTTON";
	public static final String PRODUCT_ID_BUTTON_ID = "PRODUCT_ID_BUTTON";
	public static final String PRODUCT_ID_TEXT_FIELD_ID = "PRODUCT_ID_TEXT_FIELD";
	public static final String DUNNAGE_BUTTON_ID = "DUNNAGE_BUTTON";
	public static final String TRANSACTIONID_BUTTON_ID = "TRANSACTIONID_BUTTON";
	public static final String DUNNAGE_TEXT_FIELD_ID = "DUNNAGE_TEXT_FIELD";
	public static final String TRANSACTIONID_TEXT_FIELD_ID = "TRANSACTIONID_TEXT_FIELD";
	public static final String PRODUCT_SEARCH_BUTTON_ID = "PRODUCT_SEARCH_BUTTON";
	public static final String KICKOUT_TAB_ID = "KICKOUT_TAB";
	public static final String REMOVE_KICKOUT_TAB = "REMOVE_KICKOUT_TAB";
	public static final String KICKOUT_ALL_ID = "KICKOUT_ALL";
	public static final String KICKOUT_SELECTED_ID = "KICKOUT_SELECTED";
	public static final String DESELECT_KICKOUT_ID = "DESELECT_KICKOUT";
	public static final String REMOVE_ALL_KICKOUT_ID = "REMOVE_KICKOUT";
	public static final String REMOVE_SELECTED_KICKOUT_ID = "REMOVE_SELECTED";
	public static final String RELEASE_ALL_KICKOUT_ID = "RELEASE_ALL_KICKOUT";
	public static final String RELEASE_SELECTED_KICKOUT_ID = "RELEASE_SELECTED_KICKOUT";
	public static final String DESELECT_RELEASE_KICKOUT_ID = "DESELECT_RELEASE_KICKOUT";
	public static final String REMOVE_ALL_RELEASE_KICKOUT_ID = "REMOVE_ALL_RELEASE_KICKOUT";
	public static final String REMOVE_SELECTED_RELEASE_KICKOUT_ID = "REMOVE_SELECTED_RELEASE_KICKOUT";
	public static final String KICKOUT_REASON_PANE_ID = "KICKOUT_REASON_PANE";
	public static final String RELEASE_KICKOUT_REASON_PANE_ID = "RELEASE_KICKOUT_REASON_PANE";
}
