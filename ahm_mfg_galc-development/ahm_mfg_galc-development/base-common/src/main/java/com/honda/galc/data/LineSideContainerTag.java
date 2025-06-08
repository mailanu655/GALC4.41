package com.honda.galc.data;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Insert the description here.
 * <h4>Usage and Example</h4>
 * Insert the usage and example here.
 * <h4>Special Notes</h4>
 * Insert the special notes here if any.
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * 
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * 
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>8/11/2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL042</TD>
 * <TD>Re-factoring:  from DataContainerTag to LineSideContainerTag<ul>
 * 	<li>TASK_EXCEPTION -> TASK_ERROR</li>
 *  <li>PART_NAME_MASK, TAG_ECU_PART_MASK, PART_TORQUES</li></ul>
 * </TD>
 * </TR> 
 * 
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>3/28/2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL014</TD>
 * <TD>Process Point Notification - tags for multiple products:<br>
 * VINs and EINs</TD>
 * </TR> 
 * <TR>
 * <TD>B. Carr / D. Jensen</TD>
 * <TD>2/23/2006</TD>
 * <TD></TD>
 * <TD>2CX</TD>
 * <TD> Adding several tags for 2CX</TD>
 * </TR> 
 * <TR>
 * <TD>Raj </TD>
 * <TD>12/19/2005</TD>
 * <TD></TD>
 * <TD>@SR6830</TD>
 * <TD> Add rear suspension mount front and rear suspension back</TD>
 * </TR>
 * 
 * <TR>
 * <TD>B. Carr</TD>
 * <TD>10/14/2005</TD>
 * <TD></TD>
 * <TD></TD>
 * <TD>Added LET_STATUS</TD>
 * </TR>
 * 
 * <TR>
 * <TD>D. Jensen</TD>
 * <TD>12/05/05</TD>
 * <TD>&nbsp;</TD>
 * <TD>@DEJ005</TD>
 * <TD>Add DIECAST_MACHINE_NUMBER constant in support of LPDC Inquiry</TD>
 * </TR> 
 *  
 * <TR>
 * <TD>D. Jensen</TD>
 * <TD>09/07/05</TD>
 * <TD>&nbsp;</TD>
 * <TD>@DEJ004</TD>
 * <TD>Add constants in support of HPDC Inquiry</TD>
 * </TR> 
 * 
 * <TR>
 * <TD>D. Jensen</TD>
 * <TD>07/01/05</TD>
 * <TD>&nbsp;</TD>
 * <TD>@DEJ002</TD>
 * <TD>Add constants in support of SR 6481 - LPDC Inquiry</TD>
 * </TR> 
 *  
 * <TR>
 * <TD>B. Carr</TD>
 * <TD>7/15/2004</TD>
 * <TD></TD>
 * <TD>OIM 71</TD>
 * <TD>Add Shipment Number, Status, and CreateTime for Shipping/Receiving</TD>
 * </TR>
 *
 * <TR>
 * <TD>B. Marks</TD>
 * <TD>(2004/04/29)</TD>
 * <TD>1.4.2.1</TD>
 * <TD>SR4301</TD>
 * <TD>Added defines for duplicate engine sn and time</TD>
 * </TR>
 *  
 * <TR>
 * <TD>J. Martinek</TD>
 * <TD>February 11th, 2004</TD>
 * <TD></TD>
 * <TD>@OIM282</TD>
 * <TD>PropertyServicesFacade Task bean constants.</TD>
 * </TR>
 * 
 * <TR>
 * <TD>S.Ohba</TD>
 * <TD>(2001/07/18 13:32:28)</TD>
 * <TD>0.1.5</TD>
 * <TD>(none)</TD>
 * <TD>Add DUPLI_PART Tag</TD>
 * </TR>
 * 
 * <TR>
 * <TD>Y.Kawada</TD>
 * <TD>(2001/07/17 15:33:40)</TD>
 * <TD>0.1.4</TD>
 * <TD>(none)</TD>
 * <TD>Add REPAIR_FLAG Tag</TD>
 * </TR>
 * 
 * <TR>
 * <TD>T.Naoi</TD>
 * <TD>(2001/06/08 19:23:40)</TD>
 * <TD>0.1.3</TD>
 * <TD>(none)</TD>
 * <TD>Update VQ_ON_RESULT Tag(VQ_ON_RESULT >> VIN)</TD>
 * </TR>
 * 
 * <TR>
 * <TD>T.Naoi</TD>
 * <TD>(2001/06/08 18:45:40)</TD>
 * <TD>0.1.2</TD>
 * <TD>(none)</TD>
 * <TD>Update STAMPING_VIN Tag(PRODUCT_ID >> VIN)</TD>
 * </TR>
 * 
 * <TR>
 * <TD>T.Naoi</TD>
 * <TD>(2001/06/08 18:30:40)</TD>
 * <TD>0.1.1</TD>
 * <TD>(none)</TD>
 * <TD>Add DUPLICATE_RESULT Tag</TD>
 * </TR>
 * 
 * <TR>
 * <TD>M.Kinoshita</TD>
 * <TD>(2001/05/26 10:45:40)</TD>
 * <TD>0.1.0</TD>
 * <TD>(none)</TD>
 * <TD>Add commnet</TD>
 * </TR>
 * 
 * <TR>
 * <TD>T.Naoi</TD>
 * <TD>(2001/03/13 16:31:45)</TD>
 * <TD>0.0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * 
 * </TABLE>
 * @see
 * @ver 0.1
 * @author T.Naoi
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class LineSideContainerTag {
	/**
	 * These Tag is common Tag of LineSide Application and BusinessLogic
	 */
	public final static java.lang.String VIN = "VIN";
	public final static java.lang.String VINS = "VINS";
	public final static java.lang.String PROD_LOT = "PROD_LOT";
	public final static java.lang.String KD_LOT = "KD_LOT";
	public final static java.lang.String MTOC = "MTOC";
	public final static java.lang.String TRACKING_STATUS = "TRACKING_ST";
	public final static java.lang.String LAST_PASSING_PROCESS = "LAST_PASS_PP";
	public final static java.lang.String HOLD_STATUS = "HOLD_ST";
	public final static java.lang.String SHIPHOLD_STATUS = "SHIPHOLD_ST";
	public final static java.lang.String ENGINE_SERIAL_NUMBER = "ENGINE_SN";	
	public final static java.lang.String ENGINE_SERIAL_NUMBERS = "ENGINE_SNS";	
	public final static java.lang.String YMTO = "YMTO";
	public final static java.lang.String FIRING_FLAG = "FIRING_FLG";
	public final static java.lang.String MISSION_SERIAL_NUMBER = "MISSION_SN";
	public final static java.lang.String AFON_CHECK_RESULT = "AFON_CHK_RES";
	public final static java.lang.String SMARTEYE_LABEL_NUMBER = "LABEL_NUMBER";
	public final static java.lang.String CURRENT_TIME = "CURRENT_TIME";
	public final static java.lang.String TIMESTAMP = "TIMESTAMP";
	public final static java.lang.String CHECK_RESULT = "CHECK_RES";
	public final static java.lang.String CLIENT_EVENT = "CLIENT_EVENT";
	public final static java.lang.String DATA_COLLECTION_OK = "DATA_COLLECTION_OK";
	public final static java.lang.String DATA_COLLECTION_STATUS = "DATA_COLLECTION_COMPLETE";
	public final static String QICS_UPDATE_STATUS = "QICS_UPDATE_COMPLETE";
	// Added for LPDC Inspection Client
	public final static java.lang.String DATA_COLLECTION_COMPLETE1 = "DATA_COLLECTION_COMPLETE1";
	public final static java.lang.String DATA_COLLECTION_COMPLETE2 = "DATA_COLLECTION_COMPLETE2";
	public final static java.lang.String DATA_COLLECTION_COMPLETE3 = "DATA_COLLECTION_COMPLETE3";
	public final static java.lang.String DATA_COLLECTION_COMPLETE4 = "DATA_COLLECTION_COMPLETE4";
	
	public final static String RELEASE_INSTRUCTION_CODE = "RELEASE_INSTRUCTION_CODE"; // FJF 7/27/04
	/**
	 * These Tag is  Tag of LineSide(only Frame) Application and BusinessLogic and E/I
	 */
	// WE
	public final static java.lang.String VIN_STAMPING_REQ = "VIN_STAMPING_REQ";
	public final static java.lang.String DOWNLOAD_LOT = "PRODUCTION_LOT";
	public final static java.lang.String STAMPING_VIN = "VIN";
	public final static java.lang.String WEON_RESULT = "RESULT";
	// AF(Common)
	public final static java.lang.String EXPECTED_VIN = "EXPECTED_PRODUCT_ID";
	public final static java.lang.String EXPECTED_MTOC = "EXPECTED_MTOC";
	public final static java.lang.String PART_SERIAL_NO = "PART_SN";
	public final static java.lang.String NEXT_VIN = "NEXT_VIN";
	//	AF(SRS)
	public final static java.lang.String PEAK_TORQUE = "PEAK_TORQUE";
	public final static java.lang.String PEAK_TORQUE_STATUS = "PEAK_TORQUE_STATUS";
	public final static java.lang.String FINAL_ANGLE_STATUS = "FINAL_ANGLE_STATUS";
	public final static java.lang.String FINAL_ANGLE = "FINAL_ANGLE";
	public final static java.lang.String TORQUE_VALUE = "TORQUE_VALUE";
	public final static java.lang.String RESPONSE = "RESPONSE";
	public final static java.lang.String TORQUE_COUNT = "TORQUE_COUNT";
	public final static java.lang.String TIGHTENING_ID = "TIGHTENING_ID";
	public final static java.lang.String TIGHTENING_STATUS = "TIGHTENING_STATUS";
	
	//	AF(Mission&Glass)
	public final static java.lang.String REQUIRED_MISSION = "REQUIRED_MISSION";
	public final static java.lang.String MISSION_TYPE = "MISSION_TYPE";
	public final static java.lang.String REQUIRED_FRONT_GLASS="REQUIRED_FRONT_GLASS";
	public final static java.lang.String REQUIRED_REAR_GLASS="REQUIRED_REAR_GLASS";
	public final static java.lang.String FRONT_GLASS_MODEL_TYPE="FRONT_GLASS_MODEL_TYPE";
	public final static java.lang.String REAR_GLASS_MODEL_TYPE="REAR_GLASS_MODEL_TYPE";
	public final static java.lang.String REQUIRED_GLASS = "REQUIRED_GLASS";
	public final static java.lang.String GLASS_MODEL_TYPE = "GLASS_MODEL_TYPE";
	
	//	AF(AFOFF)
	public final static java.lang.String AFOFF_REQUEST_TYPE = "AFOFF_REQUEST_TYPE";
	public final static java.lang.String SRS_CHECK_RESULT = "SRS_CHECK_RESULT";
	public final static java.lang.String ENGINE_CHECK_RESULT = "ENGINE_CHECK_RESULT";
	public final static java.lang.String PRODUCT_CHECK_RESULT = "PRODUCT_CHECK_RESULT";
	public final static java.lang.String PRODUCT_CHECK_TYPES = "PRODUCT_CHECK_TYPES";
	public final static java.lang.String SAMPLE_CHECK_RESULT = "SAMPLE_CHECK_RESULT";
	// PP @SR7912  Label Hold Check for destination change
	public final static java.lang.String LABELHOLD_CHECK_RESULT = "LABELHOLD_CHECK_RESULT";
	
	//	@SR6830 AF Suspension mount	
	public final static java.lang.String TORQUE_PREFIX = "TORQUE";
	public final static java.lang.String ANGLE_PREFIX  = "ANGLE";
	public final static java.lang.String FINAL_JUDGEMENT_STATUS = "FINAL_JUDGEMENT_STATUS";
	// VQ
	public final static java.lang.String VQ_ON_RESULT = "VIN";
	public final static java.lang.String KEY_NO = "KEY_NO";
	// CN SR3082
	// VQ_ON_CONFIRM -- confirmation ASCII digit for Scan or Alignment Confirmation action
	public final static java.lang.String CONFIRM = "CONFIRM";

	// SRS Repair
	public final static java.lang.String PART_RESULT_DATA = "PART_RESULT_DATA";
	public final static java.lang.String REMOVE_PART_NAME = "REMOVE_PART";
	/**
	 * These Tag is  Tag of LineSide(only Engine) Application and BusinessLogic and E/I
	 */
	// AE
	public final static java.lang.String LAST_DOWNLOAD_LOT = "LAST_DOWNLOADED_PROD_LOT_NO";
	public final static java.lang.String LAST_DOWNLOAD_STARTING_EIN = "LAST_DOWNLOAD_STARTING_EIN";
	public final static java.lang.String CHECK_OVERRIDE = "LOT_NO_CHECK_OVERRIDE";
	public final static java.lang.String DOWNLOAD_LOT_SIZE = "NO_OF_LOTS_TO_BE_DOWNLOADED";
	public final static java.lang.String FIRST_ALARM = "1ST_ALARM";
	public final static java.lang.String SECOND_ALARM = "2ND_ALARM";
	public final static java.lang.String SKIP_INVENTORY_UPDATE = "SKIP_INVENTORY_UPDATE";
	public final static java.lang.String LEAK_AMOUNT = "LEAK_AMOUNT";
	public final static java.lang.String QC_RESULT = "QC_RESULT";
	// Head and Block
	public final static java.lang.String HEAD = "HEAD";
	public final static java.lang.String BLOCK = "BLOCK";
	public final static java.lang.String HEAD_DC_NUMBER = "HEAD_DC";
	public final static java.lang.String HEAD_MC_NUMBER = "HEAD_MC";
	public final static java.lang.String BLOCK_DC_NUMBER = "BLOCK_DC";
	public final static java.lang.String BLOCK_MC_NUMBER = "BLOCK_MC";
	public final static java.lang.String ALMCB_NUMBER = "ALMCB_NUMBER";
	public final static java.lang.String ALMCH_NUMBER = "ALMCH_NUMBER";
	public final static java.lang.String ALMCH_COLOR = "ALMCH_COLOR";
	public final static java.lang.String CART_NUMBER = "CART_NUMBER";
	public final static java.lang.String TRAILER_NUMBER = "TRAILER_NUMBER";
	public final static java.lang.String SHIPMENT_NUMBER = "SHIPMENT_NUMBER";  // BC 7/15/04
	public final static java.lang.String CONTAINER_TYPE = "CONTAINER_TYPE";	// BC 7/26/04
	public final static java.lang.String CONTAINER_ID = "CONTAINER_ID";		//BC 7/26/04
	public final static java.lang.String FIFO_NUMBER = "FIFO_NUMBER";		//
	public final static java.lang.String CONTAINER_COUNT = "CONTAINER_COUNT";
	public final static java.lang.String CONTAINER_TARGET = "CONTAINER_TARGET";
	public final static java.lang.String CART_COUNT = "CART_COUNT";
	public final static java.lang.String CART_TARGET = "CART_TARGET";
	public final static java.lang.String DUPLICATE_RESULT = "DUPLICATE_RESULT";
	public final static java.lang.String PART_TYPE = "PART_TYPE";
	public final static java.lang.String REPAIR_FLAG = "REPAIR_FLAG";
	public final static java.lang.String DUPLI_PART = "DUPLI_PART";
	public final static java.lang.String AFSEQ_VINLIST = "AFSeqVinList";
	public final static java.lang.String AFSEQ_VINLIST_MODE = "AFSeqVinListMode";
	public final static String SKID_NOS = "SKID_NUMBERS";
	public final static String SKID_ENTRIES = "SKID_ENTRIES";
	public final static String CART_NOS = "CART_NUMBERS";
	public final static String CART_ENTRIES = "CART_ENTRIES";
	public final static String RACK_ENTRIES = "RACK_ENTRIES";
	public final static String PALLET_ENTRIES = "PALLET_ENTRIES";	
	public final static String KEYS = "Keys";
	public final static String DESTINATION="DESTINATION";
	public final static String DUP_ENG_SN="DUP_ENG_SN"; // SR4301 - BM
	public final static String DUP_ENG_TIME="DUP_ENG_TIME"; // SR4301 - BM
	/*@SR5103 - 07/15/2004 - KDA : Added code to support Head Type for MCOff*/
	public final static String HEAD_TYPE = "HEAD_TYPE";
	public final static String BLOCK_TYPE = "BLOCK_TYPE";
	public final static String MAX_CONTAINER_COUNT = "MAX_CONTAINER_COUNT";

	public final static String DC_AUTO_GENERATE = "DC_AUTO_GENERATE"; // MC HEAD ON - Added to support autogenerate DC Head #'s BR 11/10/04

	public final static String DC_AUTO_POPULATE = "DC_AUTO_POPULATE"; //SR13007 DKP 8/3/2007
	
	public final static String FAKE_DC_STRING="DC_NOT_ENTERED*"; // SR4301 - BM
	public final static String SHIPPING_STATUS="SHIPPING_STATUS"; // BC 7/19/04
	public final static String CREATE_TIME = "CREATE_TIME"; // BC 7/20/04

	public final static String PENDING_SHIPMENT_DATA = "PENDING_SHIPMENT_DATA"; // BC 7/22/04
	//@SR8855 - JJ
	public final static String SKIP_CAPACITY_CHECK = "SKIP_CAPACITY_CHECK"; //JJ 1/2/06
	public final static String MC_NUMBERS = "MC_NUMBERS"; //JJ 1/2/06
	
	public final static String RACK_NOS = "RACK_NUMBERS";
	public final static String PALLET_NOS = "PALLET_NUMBERS";
	

	// MC Block
	public final static java.lang.String HONE_MACHINE_NUMBER = "HONE_MACHINE_NUMBER";
	public final static java.lang.String DISPLAY_COUNT = "DISPLAY_COUNT";
	public final static java.lang.String COLUMN_ELEMENT_0 = "COLUMN_ELEMENT_0";
	public final static java.lang.String COLUMN_ELEMENT_1 = "COLUMN_ELEMENT_1";
	public final static java.lang.String TRY_COUNT = "TRY_COUNT";
	public final static java.lang.String COMMAND = "COMMAND";
	public final static java.lang.String PARAMS = "PARAMS";
	
	public final static java.lang.String INCORRECT_FIELD = "INCORRECT_FIELD";
	public final static java.lang.String DATA_KEY = "DATA_KEY";

	// LPDC Inquiry Tags		@DEJ002 - SR6481 DJ 05/20/05
	public static final String EXECUTION_TASK_DC = "EXECUTION TYPE";
	public static final String PROCESS_POINT_DC = "PROCESS POINT";
	public static final String STATUS_DC = "STATUS";
	public static final String FROM_DATE_DC = "FROM DATE";
	public static final String TO_DATE_DC = "TO DATE";
	public static final String SQL_DATA_DC = "SQL DATA";
	public static final String FROM_HEAD_NUMBER = "FROM HEAD NUMBER";
	public static final String TO_HEAD_NUMBER = "TO HEAD NUMBER";
	
	//@DEJ005 - 11/09/05 - HEAT581069
	public static final String DIECAST_MACHINE_NUMBER = "DIECAST MACHINE NUMBER";
	
	// HPDC Inquiry Tags		@DEJ004 - 08/26/05
	public static final String FROM_BLOCK_NUMBER = "FROM BLOCK NUMBER";
	public static final String TO_BLOCK_NUMBER = "TO BLOCK NUMBER";

	// Property Services @OIM282
	public final static String PROPERTY_SERVICES_COMMAND = "PROPERTY_SERVICES_COMMAND";
	public final static String PROPERTY_SERVICES_PROPS = "PROPERTY_SERVICES_PROPS";
	public final static String PROPERTY_SERVICES_COMPONENT = "PROPERTY_SERVICES_COMPONENT";
	public final static String PROPERTY_SERVICES_PROP_KEY = "PROPERTY_SERVICES_PROP_KEY";
	public final static String PROPERTY_SERVICES_PROP_VALUE = "PROPERTY_SERVICES_PROP_VALUE";

	// 5-9-05 ComFrame SR6784
	// AF-OFF Counter fields
	public final static String FIRST_SHIFT_CURRENT_COUNT = "FIRST_SHIFT_CURRENT_COUNT"; //SR6784
	public final static String FIRST_SHIFT_PLANNED_COUNT = "FIRST_SHIFT_PLANNED_COUNT"; //SR6784
	public final static String SECOND_SHIFT_CURRENT_COUNT = "SECOND_SHIFT_CURRENT_COUNT"; //SR6784
	public final static String SECOND_SHIFT_PLANNED_COUNT = "SECOND_SHIFT_PLANNED_COUNT"; //SR6784
	
	//10/05/05 LET BCC
	public final static String LET_STATUS = "LET_STATUS";
	public final static String MODEL_TYPE = "MODEL_TYPE";
	public final static String ALC_DATE = "ALC_DATE";
	
	//HCM AE ON
	public final static String MODEL_CODE = "MODEL_CODE";
	
	// 2CX
	public final static String SN_LIST = "SN_LIST";
	public final static String IS_VALID = "IS_VALID";
	public final static String DESCRIPTION = "DESCRIPTION";
	public final static String MACHINE_ID = "MACHINE_ID";
	public final static String MACHINE_NAME = "MACHINE_NAME";
	public final static String MACHINE_LIST = "MACHINE_LIST";
	public final static String PROCESS_POINT_LIST = "PROCESS_POINT_LIST";
	public final static String PROCESS_POINT_TYPES = "PROCESS_POINT_TYPES";
	public final static String DESCRIPTION_LIST = "DESCRIPTION_LIST";
	public final static String SERIAL_NUMBER = "SERIAL_NUMBER";
	public final static String COUNT = "COUNT";
	public final static String COMMENT = "COMMENT";
	public final static String PROCESS_POINTS_MAP_DATA = "PROCESS_POINT_MAP_DATA";
	public final static String HOLD_REASONS_MAP_DATA = "HOLD_REASONS_MAP_DATA";
	public final static String PROCESS_POINT_ID = "PROCESS_POINT_ID";
	public final static String PROCESS_ID = "PROCESS_ID";
	public final static String PROCESS_DESC = "PROCESS_DESC";
	public final static String PROCESS_TYPE = "PROCESS_TYPE";
	public final static String PROCESS_LIST = "PROCESS_LIST";
	public final static String HOLD_REASON_ID = "HOLD_REASON_ID";
	public final static String HOLD_REASON_DESC = "HOLD_REASON_DESC";
	public final static String TRANSACTION_ID = "TRANSACTION_ID";
	public final static String EVENT_RECORD = "EVENT_RECORD";
	public final static String OTHER_DESCRIPTION = "OTHER_DESCRIPTION";
	public final static String NEXT_ACTION = "NEXT_ACTION";
	public final static String REASON_ID = "REASON_ID";
	public final static String REASON_DESC = "REASON_DESC";
	public final static String USER_NAME = "USER_NAME";
	public final static String USER_TYPE = "USER_TYPE";
	public final static String AREA = "AREA";
	public final static String AREA_LIST = "AREA_LIST";
	public final static String ACTION_DETAILS_DC = "ACTION_DETAILS_DC";
	public final static String HISTORY_DETAILS_DC = "HISTORY_DETAILS_DC";
	public final static String STATUS_DETAILS_DC = "STATUS_DETAILS_DC";
	public final static String INVALID_PART_LIST = "INVALID_PART_LIST";
	public final static String TYPE_ID = "TYPE_ID";
	public final static String MACHINES_MAP_DATA = "MACHINES_MAP_DATA"; 
	public final static String HOLD_BY_DATE_SER_NUMS = "HOLD_BY_DATE_SER_NUMS";
	public final static String PROC_PT_ID = "PROCESS_POINT_ID";
	public final static String PROC_PT_TYPE_MAP_DATA = "PROC_PT_TYPE_MAP_DATA";
	public final static String NEXT_PROCESS = "NEXT_PROCESS";
	public final static String ACTION = "ACTION";
	
	public final static String SEARCH_DATE = "SEARCH_DATE";
	public final static String SEARCH_FROM_DATE = "SEARCH_FROM_DATE";
	public final static String SEARCH_TO_DATE = "SEARCH_TO_DATE";
	public final static String SEARCH_FROM_TIME = "SEARCH_FROM_TIME";
	public final static String SEARCH_TO_TIME = "SEARCH_TO_TIME";
	public final static String SEARCH_BY_CONTEXT = "SEARCH_BY_CONTEXT";
	
	/*20070619 jma 2cx on/off number marriage*/
	public static final String PART_ON_SERIAL_NUMBER = "PART_ON_SERIAL_NUMBER";
	public static final String PART_OFF_SERIAL_NUMBER = "PART_OFF_SERIAL_NUMBER";
	public static final String PART_NAME = "PART_NAME";
	public static final String TASK_TYPE = "TASK_TYPE";
	public static final String FORCE_OVERWRITE = "FORCE_OVERWRITE";

	//SR9187 - Defect Display - JJ
	public final static String RESPONSIBLE_DEPT = "RESPONSIBLE_DEPT";
	public final static String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP";
	public final static String TIME_RANGES = "TIME_RANGES";
	public final static String DEFECT_POINTS = "DEFECT_POINTS";
	public final static String DEFECT_COUNTS = "DEFECT_COUNTS";
	public final static String IMAGE_NAME = "IMAGE_NAME";
	public final static String START_TIMESTAMP = "START_TIMESTAMP";
	public final static String END_TIMESTAMP = "END_TIMESTAMP";
	public final static String IMAGE_NAMES = "IMAGE_NAMES";
	public final static String IMAGE_FULL_NAMES = "IMAGE_FULL_NAMES";
	public final static String IMAGES = "IMAGES";
	public final static String PROCESS_POINTS = "PROCESS_POINTS";
	public final static String IMAGE_SECTIONS = "IMAGE_SECTIONS";
	
	public final static String TRANS_INQUIRY_SUMMARY_TABLE_DATA = "TRANS_INQUIRY_SUMMARY_TABLE_DATA";
	public final static String TRANS_INQUIRY_DETAIL_TABLE_DATA = "TRANS_INQUIRY_DETAIL_TABLE_DATA";
	
	// 2CX - PORD
	public final static String PORD_FMC_TABLE_HEADERS = "TABLE_HEADERS";
	public final static String PORD_FMC_TABLE_DATA = "TABLE_DATA";
	public final static String PORD_FMC_CENTERED_COLUMNS = "CENTERED_COLUMNS";
	public final static String PORD_FMC_COLUMN_WIDTHS = "COLUMN_WIDTHS";
	public final static String PORD_FMC_PRODUCTION_DATE = "PRODUCTION_DATE";
	public final static String PORD_FMC_BEGIN_DATE_TIME = "BEGIN_DATE_TIME";
	public final static String PORD_FMC_END_DATE_TIME = "END_DATE_TIME";
	
	public final static java.lang.String CRANK_SERIAL_NUMBER = "CRANKSHAFT";
	public final static java.lang.String CONROD_SERIAL_NUMBER_X = "CONROD %s";
	public final static java.lang.String CONROD_SERIAL_NUMBER_1 = "CONROD 1";
	public final static java.lang.String CONROD_SERIAL_NUMBER_2 = "CONROD 2";
	public final static java.lang.String CONROD_SERIAL_NUMBER_3 = "CONROD 3";
	public final static java.lang.String CONROD_SERIAL_NUMBER_4 = "CONROD 4";
	public final static java.lang.String CONROD_SERIAL_NUMBER_5 = "CONROD 5";
	public final static java.lang.String CONROD_SERIAL_NUMBER_6 = "CONROD 6";
	
	public final static String PART_MCB = "BLOCK MC";
	public static final String PART_CRANK_JOURNAL_PREFIX = "CRANK JOURNAL";
	public static final String CON_ROD_CAPS = "CON-ROD-CAPS";

	//Knuckle Bar install --BCC
	public final static String SPINDLE_SIZE = "SPINDLE_SIZE"; 	
	
	public final static String USER_LIST = "USER_LIST";
	public final static String SEARCH_FOR = "SEARCH_FOR";
	public final static String EXPIRE_DAYS = "EXPIRE_DAYS";
	public final static String USER_GROUPS = "USER_GROUPS";
	public final static String POSSIBLE_GROUPS = "POSSIBLE_GROUPS";
	
	//Tags for BTS
	public final static String BUMPER_ID = "BUMPER_ID";
	public final static String LINE_NO = "LINE_NO";
	
	public final static String PRODUCT_ID = "PRODUCT_ID";
	public final static String PRODUCT = "PRODUCT";
	public final static String CARRIER_ID = "CARRIER_ID";	
	public final static String REPAIR_STATUS = "REPAIR_STATUS";
	public final static String LOCATION_STATUS = "LOCATION_STATUS";
	public final static String INSTALLED_SEALANTS = "INSTALLED_SEALANTS";
	public final static String INSTALLED_PARTS = "INSTALLED_PARTS";
	
	//Tags for MC/DC Data Collection 
	public final static String OVERALL_STATUS = "OVERALL_STATUS";
	public final static String PREHEAT = "PREHEAT";
	public final static String MISSION = "MISSION";
	
	// Tag indicating task error (also caused by TaskException)
	public static final String TASK_ERROR = "TASK_ERROR";
	/**
	 * Tags for Headed DC
	 */
	public final static String PART_NAME_MASK = "PART_NAME_MASK";
	public final static String TAG_ECU_PART_MASK = "ECUPARTMASK";
	public final static String PART_TORQUES = "DATACOLLECTION_PART_TORQUES";
	
	//Tag added for Engine Head mariiage
	public final static String MISSING_REQUIRED_PARTS = "MISSING_REQUIRED_PARTS";
	public final static String SKIP_REQUIRED_PART_CHECK = "SKIP_REQUIRED_PART_CHECK";
	
	//Tag for OPC EI heart beat
	public final static String OPC_INSTANCE_NAME = "OPC_INSTANCE_NAME";
	public final static String KEEP_ALIVE = "KEEP_ALIVE";
    public final static String HEARTBEAT_TAG = "HEARTBEAT_TAG";
	public static final String EXPECTED_PRODUCT_ID = "EXPECTED_PRODUCT_ID";
	public static final String NO_NEXT_EXPECTED_PRODUCT_ID = "NO_NEXT_EXPECTED_PRODUCT_ID";
	public static final String VALID_PRODUCT_NUMBER_FORMAT = "VALID_PRODUCT_NUMBER_FORMAT";
	public static final String PRODUCT_EXISTS = "PRODUCT_EXISTS";
    public static final String ENABLE_HEARTBEAT_MONITOR = "ENABLE_HEARTBEAT_MONITOR";
    public static final String HEARTBEAT_TIMOUT = "HEARTBEAT_TIMOUT";
    public static final String HEARTBEAT_INTERVAL = "HEARTBEAT_INTERVAL";
    public static final String HEARTBEAT_MONITOR_IDS = "HEARTBEAT_MONITOR_IDS";
    public static final String REPLY_KEEP_ALIVE = "REPLY_KEEP_ALIVE";
    public static final String CHANGE_DATA_FOR_REPLY = "CHANGE_DATA_FOR_REPLY";
    
    //Expected product
    public final static String PRODUCT_LIST = "PRODUCT_LIST";
    public static final String TARGET_PROCESS_POINT_ID = "TARGET_PROCESS_POINT_ID";
    public static final String PRODUCT_TYPE = "PRODUCT_TYPE";
    public static final Object ADDITIONAL_INFORMATION = "ADDITIONAL_INFORMATION";
    public static final Object MSG_PART_SN_MISMATCH = "MSG_PART_SN_MISMATCH";
	
	
	
	
/**
 * LineSideContainerTag constructor comment.
 */
public LineSideContainerTag() {
	super();
}
}
