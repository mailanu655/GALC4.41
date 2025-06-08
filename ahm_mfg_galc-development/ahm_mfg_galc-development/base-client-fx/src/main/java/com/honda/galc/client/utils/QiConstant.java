package com.honda.galc.client.utils;

/**
 * 
 * <h3>QIConstant</h3> <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * QIConstant description
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
 * @author LnT Infotech April 20 , 2016
 * 
 */

public class QiConstant {
	
	public final static String CREATE = "Create";
	public final static String UPDATE = "Update";
	public final static String REACTIVATE = "Reactivate";
	public final static String INACTIVATE = "Inactivate";
	public final static String BROWSE_IMAGE = "Image File Name";
	public final static String REPLACE_IMAGE = "Replace Image";
	public final static String RESET = "Reset";
	public final static String CANCEL = "Cancel";
	public final static String SKIP = "Skip";
	public final static String ADD_TO_GROUP = "Add To Group";
	public final static String SEARCH = "Search";
	public final static String TEST_LINK = "Test Link";
	
	public final static boolean VALIDATION_FLAG_FALSE = false;
	public final static boolean VALIDATION_FLAG_TRUE = true;
	public final static String TIME_STAMP_FORMAT = "yyyy-MM-dd HH.mm.ss";
	public final static String ASSOCIATION_PART_LOCATION_COMB_MESSAGE = "This part is associated with PartLocationCombination ,Are you sure want to inactivate Part?";
	public final static String PRODUCT_KIND= "PRODUCT_KIND";
	public final static String AUTOMOBILE= "AUTOMOBILE";
	public final static String MATCH_PATTERN_REGEX="^[A-Z0-9\\s]*$";
	public final static int IMAGE_DIMENSION=500;
	public final static String PARENT_SCREEN = "parentScreen";
	public final static int NUMBER_OF_GRIDLINES=10;
	public final static double ZOOM_FACTOR = 1.2;
	public final static int LINE_WIDTH = 5;
	public final static String CSS_PATH = "/resource/css/QiMainCss.css";
	public final static String CSS_PATH_TOUCH_SCREEN_DEVICE = "/resource/css/QiTouchScreenCss.css";
	public final static String ALL = "All";
	public final static String ACTIVE = "Active";
	public final static String INACTIVE = "Inactive";
	public final static String PRIMARY = "Primary";
	public final static String SECONDARY = "Secondary";
	public final static String REG_EXP_FOR_DATE_FORMAT = "[0-9][0-9][0-9][0-9]|[0-9][0-9][0-9][0-9]-[0-9][0-9]|[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]";
	public final static String DRAW = "Draw";
	public final static String SAVE = "Save";
	public final static String DELETE = "Delete";
	public final static String REFRESH = "Refresh";
	public final static String SELECT = "Select";
	
	public final static String ASSIGNED = "Assigned";
	public final static String NOT_ASSIGNED = "Not Assigned";
	public final static String ASSIGNED_ALL = "Assigned All";
	public final static String UPDATE_ATTRIBUTE = "Update Attribute";
	public final static String ASSIGN_ATTRIBUTE = "Assign Attribute";
	public final static String REPORTABLE = "Reportable";
	public final static String NON_REPORTABLE = "Non Reportable";
	public final static String DEFAULT_REPORTABLE = "Default Regional";
	public final static String PART_NAME = "Part Name";
	public final static String IQS = "IQS";
	public final static String THEME = "Theme";

	public final static String EMPTY = "";
	public final static String REAL_PROBLEM = "Real Problem";
	public final static String SYMPTOM = "Symptom";
	public final static String INFORMATIONAL = "Informational";
	public final static String DEFAULT_CATEGORY = "Default Regional";
	
	public static final String CREATE_MNEU = "Create Menu";
	public static final String DELETE_MNEU = "Delete Menu";
	public static final String UPDATE_MNEU = "Update Menu";
	public static final String NO = "No";
	public static final String YES = "Yes";
	public static final String USED = "Used";
	public static final String NOT_USED ="Not Used";
	
	public final static String ASSIGN = "Assign"; 
	public final static String DEASSIGN = "Deassign"; 
	public final static String ASSOCIATE = "Associate";
	public final static String VALID = "VALID";
	public final static String INVALID = "INVALID";
	public final static String DUPLICATE = "DUPLICATE";
	public final static String IMPORT = "Import";
    public final static String CLEAR_REPAIR_AREA = "Clear Repair Area";

	
	public final static String COMPANY_NODE = "Company "; 
	public final static String SITE_NODE = "Site "; 
	public final static String PLANT_NODE = "Plant "; 
	public final static String DEPARTMENT_NODE = "Department "; 
	public final static String RESPONSIBILITY_NODE = "Responsible "; 
	public final static int COMPANY_NAME_TEXT_BOX_LEN = 32;
	public final static int COMPANY_DESC_TEXT_AREA_LEN = 64;
	public final static int SITE_NAME_TEXT_BOX_LEN = 16;
	public final static int SITE_DESC_TEXT_AREA_LEN = 64;
	public final static int PLANT_NAME_TEXT_BOX_LEN = 16;
	public final static int PLANT_DESC_TEXT_AREA_LEN = 32;
	public final static int PDDA_PLANT_CODE_TEXT_BOX_LEN = 1;
	public final static int PLANT_ENTRY_SITE_TEXT_BOX_LEN = 16;
	public final static int PLANT_ENTRY_PLANT_TEXT_BOX_LEN = 16;
	public final static int PLANT_PRODUCT_LINE_NO_TEXT_BOX_LEN = 5;
	public final static int PDDA_LINE_TEXT_BOX_LEN = 1;
	public final static int DEPARTMENT_ABBR_NAME_TEXT_BOX_LEN = 16;
	public final static int DEPARTMENT_NAME_TEXT_BOX_LEN = 32;
	public final static int DEPARTMENT_PDDA_CODE_TEXT_BOX_LEN = 2;
	public final static int DEPARTMENT_DESC_TEXT_AREA_LEN = 128;
	public final static int RESPONSIBLE_LEVEL_NAME_TEXT_BOX_LEN = 32;
	public final static int RESPONSIBLE_LEVEL_DESC_TEXT_AREA_LEN = 32;
	public final static int MOST_FREQ_USED_LIST_SZ = 0;
	public static final int MOST_FREQ_USED_DURATION = 0; //hours
	
	public final static String UPDATE_REASON_FOR_AUDIT = "Entity has been hard deleted.";
	public final static String ASSIGN_REASON_FOR_AUDIT = "Image is assigned to Entry Screen.";
	public final static String ASSIGN_REASON_REGIONAL_ATTRIBUTE_FOR_AUDIT = "Assigned Attribute to Part Defect Combination.";
	public final static String SAVE_REASON_FOR_MTC_TO_ENTRY_MODEL_AUDIT = "MTC Model removed from Entry Model.";
	public final static String SAVE_REASON_FOR_QICS_STN_REPAIR_METHOD_AUDIT = "Repair Method removed from QICS Station.";
	public final static String SAVE_REASON_FOR_PDC_TO_ENTRY_SCREEN_AUDIT = "PDC removed from Entry screen.";
	public final static String SAVE_REASON_FOR_STATION_RESPONSIBILITY_AUDIT = "Responsible level deassigned from Process Point";
	public final static String SAVE_REASON_FOR_REGIONAL_PROCESS_POINT_GROUP_AUDIT = "Regional Process Point Group deassigned.";
	public static final String IMAGE="Image";
	public static final String TEXT="Text";	
	public static final String NEW = "New";
	public static final String LOGOUT = "Logout";
	public static final String SEND = "Send";
	public static final String UPLOAD_IMAGE_VIDEO = "Upload Image/Video";
	public final static String VOID_ALL = "Void All";
	public final static String CLOSE = "Close";
	public final static String APPLY = "Apply";
	public final static String ACCEPT = "Accept";
	public final static String VOID = "Void";
	public final static String CHANGE_RESPONSIBLE = "Change Responsible";
	
	public final static String RESPONSIBLE_LEVEL3_NODE = "Responsible Level 3 ";
	public final static String RESPONSIBLE_LEVEL2_NODE = "Responsible Level 2 ";
	public final static String RESPONSIBLE_LEVEL1_NODE = "Responsible Level 1 ";

	public static final String REPAIRED = "Repaired";
	public static final String NOT_REPAIRED = "Not Repaired";
	public static final String NON_REPAIRABLE = "Non Repairable/Scrap";
	public static final String UPDATE_ENTRY_STATION_STATUS = "Update Entry Station Status";
	public static final String NONE = "None";
	public static final String ENTRY_STATION_DEFAULT_DEFECT_STATUS = "Default Defect Status";
	public static final String ENTRY_STATION_AVAILABLE_DEFECT_STATUS = "Available Defect Status";
	public static final String SAVE_REASON_FOR_ENTRY_STATION_COFIGURATION_AUDIT = "Entry Station Audit";
	public final static String SAVE_REASON_FOR_QICS_STATION_DOCUMENT_AUDIT = "Document deassigned from QICS Station.";

	//Clone station check boxes
	public static final String CLONESTN_ENTRY_DEPT = "Entry Depts";
	public static final String CLONESTN_WRITEUP_DEPT = "Writeup Dept";
	public static final String CLONESTN_ENTRY_MODEL_SCREEN = "Entry Models & Screens";
	public static final String CLONESTN_DEFECT_STATUS = "Defect Status";
	public static final String CLONESTN_SETTINGS = "Settings";
	public static final String CLONESTN_PREV_DEFECT_VISIBLE = "Previous Defect Visible";
	public static final String CLONESTN_LIMITED_RESP = "Limited Responsibility";
	public static final String CLONESTN_UPC = "UPC";
	public static final String CLONESTN_REPLACE_DIALOG_TITLE = "Replace existing configuration";
	public static final Short NON_REPORTABLE_DEFECT_ENTRY_SCREEN = 3;
	public static final String ADD_REPAIR_METHOD = "Add Repair Method";
	public static final String DONE = "Done";
	public static final String REPEAT = "Repeat";
	public static final String SPACE_ASSIGNMENT = "Space Assignment";
	public static final String ADD = "Add";
	public static final String RETURN_TO_HOME_SCREEN = "Return To Home Screen";
	public static final String RETURN_TO_REPAIR_SCREEN= "Return to Repair Screen";
	public static final String PRODUCT_ALREADY_SCRAPED = "Product has been scrapped";
	public final static String KEYBOARD_CSS_PATH = "/resource/com/honda/galc/client/ui/keypad/css/KeyboardButtonStyle.css";
	public final static String CREATE_ROW = "Create Row";
	public final static String UPDATE_ROW = "Update Row";
	public final static String DELETE_ROW = "Delete Row";
	public static final String CREATE_SPACE = "Create Space";
	public static final String DELETE_SPACE = "Delete Space";
	public static final String REPEAT_SPACE = "Repeat Space";
	public final static String ASSIGN_REASON_EXTERNAL_SYSTEM_FOR_AUDIT = "Received defect data from external system already exist in Defect Result table";
	public final static String CLEAR_SPACE = "Clear Space";
	public final static String REPEAT_ROW = "Repeat Row";
	public final static String OK = "Ok";
	public final static String REPAIR_PARKING_STATION="Repair Parking Station";
	public final static String VOID_LAST = "Void Last";
	public final static String CHANGE_TO_FIXED ="Change To Fixed";
	public static final String NO_PROBLEM_FOUND = "No Problem Found";
	public static final String COPY_FROM = "Copy From";
	public static final String COPY_MENU = "Copy Menu";
	public static final String COPY_PDC = "Copy PDC";
	public static final String MOVE_PDC = "Move PDC";
	public static final String RESPONSIBILITY = "Responsibility";
	public final static String SCRAP_REASON = "Reason for Scrap is required";
	public static final String FIXED = "Fixed";
	public static final String CONFIRMED = "Confirmed";
	public static final String MARK_AS_CONFIRMED = "Mark as confirmed";
	public static final int MAX_UPC_COUNT = 9999;
	public static final int MIN_UPC_COUNT = 0;
	public static final String ENTRY_DEPT_SELECTED = "ENTRY_DEPT_SELECTED";
	public static final String ENTRY_DEPT2_SELECTED = "ENTRY_DEPT2_SELECTED";
	public static final String ASSOCIATE_ID_SELECTED = "ASSOCIATE_ID_SELECTED";
	public final static String DEFECT_TYPE = "DefectType";
	public final static String REPAIR_TYPE = "RepairType";
	public final static String DATA_CORRECTION = "DATA_CORRECTION";
	public final static String DEFECT_TAGGING = "DEFECT_TAGGING";
	public static final String CHANGE = "Change";
	public final static String CRITICAL = "CRITICAL";
	public static final String COMPLEX = "COMPLEX";
	public static final String TYPE_OF_CHANGE_UPDATE_ATTRIBUTE = "Update Attributes";
	public static final String TYPE_OF_CHANGE_CHANGE_DEFECT = "Change Defects";
	public static final String TYPE_OF_CHANGE_DELETE_DEFECT = "Delete Defects";
	public static final String TYPE_OF_CHANGE_UPDATE_ACTUAL_PROBLEM_ATTRIBUTE = "Update Actual Problem Attributes";
	public static final String TYPE_OF_CHANGE_CHANGE_ACTUAL_PROBLEM = "Change Actual Problem";
	public static final String TYPE_OF_CHANGE_DELETE_ACTUAL_PROBLEM = "Delete Actual Problem";
	public static final String DATA_CORRECTION_QUALIFY_BY_DATE = "Qualify By Date Only";
	public static final String DATA_CORRECTION_QUALIFY_BY_DATE_TIME = "Qualify By Date And Time";
	public static final String TRAINING_MODE_ON = "TRAINING_MODE_ON";
	public static final String TRAINING_MODE_OFF = "TRAINING_MODE_OFF";
	public static final String NO_LOCAL_SITES_CONFIGURED ="NoLocalSitesConfigured"; 
	public static final String LOCAL_SITES_IMPACTED ="LocalSitesImpacted"; 
	public static final String RESPONSIBLE_LEVEL_3 = "Responsible Level 3";
	public static final String RESPONSIBLE_LEVEL_2 = "Responsible Level 2";
	public static final String RESPONSIBLE_LEVEL_1 = "Responsible Level 1";
	public static final String PRODUCTION_DATE = "Production Date"; 
	public static final String ENTRY_TIMESTAMP ="Entry Timestamp"; 
	public static final String DATE_FORMAT = "yyyy-MM-dd"; 
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"; 
	public static final String SET_SEQUENCE = "Set Sequence";
	public static final String SET_ANGLE = "Set Angle";
	public final static String CREATE_VERSION="Create Pending Entry Model"; 
	public final static String APPLY_VERSION="Replace Current With Pending Entry Model";
	public final static char INSIDE_LOCATION = 'I';
	public final static char OUTSIDE_LOCATION = 'O';
	public final static char IN_TRANSIT_LOCATION = 'T';
	public static final String ENTER_TRAINING_MODE = "Enter Training Mode";
	public static final String EXIT_TRAINING_MODE = "Exit Training Mode";
	public static final String COMMENT = "Comment";
	public static final String REFRESH_CACHE = "Refresh Cache";
	public static final CharSequence TRAINING_MODE = "TRAINING MODE";
	//Training mode cached defect types 
	public static final Integer ADD_PARENT_DEFECT_FOR_TM = 1;
	public static final Integer ADD_CHILD_DEFECT_FOR_TM = 2;
	public static final Integer UPDATE_PARENT_DEFECT_FOR_TM = 3;
	public static final Integer UPDATE_CHILD_DEFECT_FOR_TM = 4;
	public static final Integer DELETE_PARENT_DEFECT_FOR_TM = 5;
	public static final Integer DELETE_CHILD_DEFECT_FOR_TM = 6;	
	
	public static final String POST_SCRAP_ACCEPT_DEFECT_ERROR_MSG = "The Product is already scrapped. Cannot further accept New Defects.";

	public static final String DISPLAY_QR_CODE = "Display QR Code";
	public static final String EXPORT = "Export";
	public static final String SET_SCAN ="Set Scan"; 
	public static final String UNSET_SCAN ="Unset Scan";
	public final static String CHANGE_TO_NOT_FIXED ="Change To Not Fixed";
	public final static String CANNOT_BE_INACTIVATED_MSG ="Cannot be inactivated as it is assigned to a Process Point or a Part Defect Combination.";
	public final static String CLEAR_TEXT_SYMBOL ="X";
	public static final String CONCURRENT_UPDATE_MSG_TMPL = " has been modified by another user, please refresh screen and try again.";
	public static final String CONCURRENT_UPDATE_MSG = "Data " + CONCURRENT_UPDATE_MSG_TMPL;
	
	public static final String RFID_STATION_PROP_KEY = "RFID_STATION";
	public static final String IS_AUTO_SEQ_KEY = "IS_AUTO_SEQUENCE";
	public static final String AUTO_INPUT_FREQ = "AUTO_INPUT_FREQ";

	public static final String BTN_CONTINUE_DEFECT_ENTRY = "Continue Entering Defects";
	public static final String BTN_DONE = "Done With Product";
	public static final String BTN_DIRECT_PASS = "Direct Pass Product";
	public static final String BTN_CANCEL = "Do Not report on this Product(Cancel)";
	public static final String SUBMIT_PRODUCTS = "Submit Products";
	public static final String PROCESS_ALL_PRODUCTS = "Process ALL Products";
	public static final String CLEAR_PRODUCTS_TABLE = "Clear Products Table";
	public static final String EXPORT_TABLE = "Export Table";
	public static final String IMPORT_PRODUCT_LIST = "Import Products";
	public static final String TARGET_PLANT = "Plant";
	public static final String TARGET_DEPARTMENT = "Department";
	public static final String TARGET_THEME = "Theme";
	public static final String TARGET_LOCAL_THEME = "Local Theme";
	public static final String TARGET_DEPT_RESP_LEVEL = "Dept Resp Level";
	public static final String TARGET_RESPONSIBLE_LEVEL_1 = "RespLevel1";	
	public static final String TARGET_RESPONSIBLE_LEVEL_2 = "RespLevel2";
	public static final String TARGET_RESPONSIBLE_LEVEL_3 = "RespLevel3";
	public static final String SEPARATOR = "-\n";
	public static final String HISTORY = "History";
	public static final int PRODUCT_ID_COLUMN = 1;
	public static final int PRODUCT_SPEC_CODE_COLUMN = 2;
	public static final int TRACKING_STATUS_COLUMN = 3;
	public static final int DEFECT_STATUS_COLUMN = 6;
	public static final int KICKOUT_STATUS_COLUMN = 5;
	public static final int SEARCH_PROCESS_POINT_COLUMN = 7;
	public static final int SEARCH_PROCESS_POINT_TIMESTAMP_COLUMN = 8;
	public static final int MC_SERIAL_NUMBER_COLUMN = 1;
	public static final int DC_SERIAL_NUMBER_COLUMN = 2;
	public static final int PRODUCT_SPEC_CODE_COLUMN_DC = 3;
	public static final int TRACKING_STATUS_COLUMN_DC = 4;
	public static final int DEFECT_STATUS_COLUMN_DC = 5;
	public static final int KICKOUT_STATUS_COLUMN_DC = 6;
	public static final int SEARCH_PROCESS_POINT_COLUMN_DC = 8;
	public static final int SEARCH_PROCESS_POINT_TIMESTAMP_COLUMN_DC = 9;	
	
	public final static String CREATE_CATEGORY = "Create Category";
	public final static String UPDATE_CATEGORY = "Update Category";
	public final static String DELETE_CATEGORY = "Delete Category";
	public final static String CREATE_DETAIL = "Create Detail";
	public final static String UPDATE_DETAIL = "Update Detail";
	public final static String DELETE_DETAIL = "Delete Detail";
	
	public static final String SAVE_REPAIR_COMMENT ="Save Comment";
	public static final String ASSIGN_RESP = "ASSIGN RESPONSIBLE";

}
