package com.honda.galc.data;


/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Define System Function in DataContainer and 
 * Keys using in Client,Task common.
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Sudhir Malhotra</TD>
 * <TD>(2003/06/16 14:51:00)</TD>
 * <TD>SR1977</TD><TD>(none)</TD>
 * <TD>Added key tag STRAGGLER_FLAG, retrieves Boolean value from the Datacontainer</TD>
 * </TR>
 * <TR>
 * <TD>K.Sone</TD>
 * <TD>(2001/03/01 14:51:00)</TD>
 * <TD>0.1</TD><TD>(none)</TD>
 * <TD>Initial Release.</TD>
 * </TR>
 * <TR>
 * <TD>K.Sone</TD>
 * <TD>(2001/03/29 14:13:00)</TD>
 * <TD>0.2</TD><TD>(none)</TD>
 * <TD>Deleted the REQUEST_ID of field.</TD>
 * </TR>
 * <TR>
 * <TD>K.Sone</TD>
 * <TD>(2001/04/09 11:09:00)</TD>
 * <TD>0.2.1</TD><TD>(none)</TD>
 * <TD>Undeleted the REQUEST_ID of field.</TD>
 * </TR>
 * <TR>
 * <TD>M.Hayashibe</TD>
 * <TD>(2001/09/18 11:09:00)</TD>
 * <TD>hma-2001/09/18</TD><TD>VER_2001_09_18</TD>
 * <TD>Add NEW_PASSWORD, CONFIRM_PASSWORD field.</TD>
 * </TR>
 * <TR>
 * <TD>J.Martinek</TD>
 * <TD>2004.03.17</TD>
 * <TD></TD>
 * <TD>OIM50</TD>
 * <TD>Session establishment and management</TD>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>May 28, 2004</TD>
 * <TD>1.0</TD>
 * <TD>OIM70</TD>
 * <TD>Support for DONT_KEEP_IN_STATE</TD>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Sep 15, 2004</TD>
 * <TD>1.0</TD>
 * <TD>JM016</TD>
 * <TD>Additional message info tag</TD>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Aug 2, 2005</TD>
 * <TD></TD>
 * <TD>@JM074</TD>
 * <TD>XML request directives</TD>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Nov 17, 2005</TD>
 * <TD></TD>
 * <TD>@JM073</TD>
 * <TD>New data tags that support the session persistence infrastructure</TD>
 * </TR>
 * <TR>
 * <TD>lasenko</TD>
 * <TD>Dec 04, 2007</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL010</TD>
 * <TD>Introduced DataContainerTag.EQUIPMENT_TYPE</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 0.2.1
 * @author K.Sone
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */

public class DataContainerTag {
	/**
	 * @sm
	 * Straggler indicator
	 * <P>If key exists in the Datacontainerand value is TRUE then the body is a straggler.
	 */
	public static final String STRAGGLER_FLAG = "STRAGGLER_FLAG";
	
	public static final String ATTRIBUTES_FLAG = "ATTRIBUTES_FLAG";
	
	public static final String MEASUREMENTS_FLAG = "MEASUREMENTS_FLAG";
	
	/**
	 * ProcessPointID inputting key Input ProcessPointID.
	 * <P>
	 * The String which taken by this key from DataContainer is the same as
	 * DataContainerTag.APPLICATION_ID. APPLICATION_ID cannot be intuitive
	 * with PROCESS_POINT_ID, setup the tag clearly.<Br>
	 * When Client calls init(), execute() by using HttpClient, automatically,
	 * put() aApplicationID of the argument with this tag to DataContainer
	 */
	public static final String PROCESS_POINT_ID = "PROCESS_POINT_ID";
	
	public static final String PROCESS_POINT = "PROCESS_POINT";
	
	public static final String DIVISION_ID = "DIVISION_ID";
	
	public static final String LINE_ID = "LINE_ID";
	
	/**
	 * Application ID inputting key. Input starting up application ID.
	 * <P>
	 * When request from Terminal, input ApplicationID to identify Controller.
	 * ApplicationID is decided when select Application in GUI.
	 */	
	public static final String APPLICATION_ID = "APPLICATION_ID";

	/**
	 * Password, inputting key.
	 * <P>
	 * When Client calls login() by using HttpClient, automatically,
	 * put() aPassword of the argument with this tag to DataContainer.
	 */
	public static final String PASSWORD = "PASSWORD";
	public static final String OVERRIDE_PASSWORD = "OVERRIDE_PASSWD";
	public static final String OVERRIDE_USERID = "OVERRIDE_USERID";
	public static final String GROUP_ID = "GROUP_ID";	
	public static final String HAS_BLOCK = "HAS_BLOCK";	
	public static final String HAS_HEAD = "HAS_HEAD";
	/**
	 * Client name, inputting key.
	 * <P>
	 * When Client is Terminal, input hostName attribution of Terminal. 
	 * When Client is Equipment, input EquipmentID(ClientID).
	 */
	public static final String CLIENT_ID = "CLIENT_ID";
	public static final String DEVICE_ID = "DEVICE_ID";



	/**
	 * The argument of Task when execution, inputting key.
	 * <P>
	 * As for Configration function of ProcessPoint,
	 * the execution argument of Task is setup.
	 * It is the key to input the execution argument of Task.<Br>
	 * When init(), execute() of Task are called,
	 * the execution argument is setup by using this Key.
	 */
	public static final String TASK_ARGUMENT = "TASK_ARGUMENT";
	public static final String TASK_NAME = "TASK_NAME";

	/**
	 * Inputting key of TaskStatus which is returned after execution of
	 * Task.init(DataContainer), Task.execute(DataContainer).
	 * <P>
	 * Task must put() TaskStatus Class to DataContainer using this key after
	 * execution of init(),execute() process.
	 */
	public static final String TASK_STATUS = "TASK_STATUS";

	/**
	 * The independent Task is carried out
	 * if DataContainerTag.EXECUTE_TASK is contained in DataContainer.
	 */
	public static final String EXECUTE_TASK = "EXECUTE_TASK";

	/**
	 * Client ID of the terminal being connected at present, inputting key.
	 * <P>
	 * The terminal which did init() at the end is stored.
	 */
	public static final String CONNECTED_TERMINAL = "CONNECTED_TERMINAL";

	/**
	 * The feed master of the DataContainer, inputting key.
	 * <P>
	 * Value is DataContainerTag.EQUIPMENT or DataContainerTag.TERMINAL.
	 */
	public static final String SENDER = "SENDER";

	/**
	 * When feed master of the DataContainer is equipment,
	 * It becomes the value of DataContainerTag.SENDER.
	 */
	public static final String EQUIPMENT = "EQUIPMENT";

	/**
	 * When feed master of the DataContainer is application (process point),
	 * It becomes the value of DataContainerTag.SENDER.
	 */
	public static final String APPLICATION = "APPLICATION";

	/**
	 * 
	 * The source type of the DataContainer when sent from EQUIPMENT<br>
	 * (when DataContainerTag.SENDER is set to DataContainerTag.EQUIPMENT)
	 * <p>
	 * The values are:<ul>
	 * <li>DataContainerTag.EQUIPMENT_TYPE_EI 
     * <li>DataContainerTag.EQUIPMENT_TYPE_OPC
     * </ul>
	 */
	public static final String EQUIPMENT_TYPE = "EQUIPMENT_TYPE"; // @RL101
	
	/**
	 * When feed master of the DataContainer is OPC Client,
	 * It becomes the value of DataContainerTag.EQUIPMENT_TYPE.
	 */
	public static final String EQUIPMENT_TYPE_OPC = "OPC"; // @RL101
	public static final String EQUIPMENT_TYPE_JSON = "JSON";
	
	/**
	 * When feed master of the DataContainer is equipment interface,
	 * It becomes the value of DataContainerTag.EQUIPMENT_TYPE.
	 */
	public static final String EQUIPMENT_TYPE_EI = "EI";  // @RL101
	
	/**
	 * When feed master of the DataContainer is terminal,
	 * It becomes the value of DataContainerTag.SENDER.
	 */
	public static final String TERMINAL = "TERMINAL";
	
	/**
	 * The data of equipment, inputting key.
	 * <P>
	 * BOC from equipment is stored.
	 */
	public static final String DATA = "DATA";

	/**
	 * Message ID for Log, inputting key.
	 * <P>
	 * The key to acquire a message ID without using Exception.
	 */
	public static final String MESSAGE_ID = "MESSAGE_ID";
	
	
	/**
	 * @JM016
	 * This string is used to retrieve additional message information.
	 */
	public static final String ADDITIONAL_MESSAGE_INFO = "ADDITIONAL_MESSAGE_INFO";

	/**
	 * User ID, inputting key
	 * <P>
	 * When Client calls login(), logout() by using HttpClient, automatically,
	 * put() aUser of the argument with this tag to DataContainer. 
	 */
	public static final String USER_ID = "USER_ID";

	/**
	 * User New Password, inputting key
	 * <P>
	 * VER_2001_09_18
	 * When Client calls updatePassword by using HttpClient, automatically,
	 * put() aUser of the argument with this tag to DataContainer.
	 */
	public static final String NEW_PASSWORD = "NEW_PASSWORD";

	/**
	 * User New Password, inputting key
	 * <P>
	 * VER_2001_09_18
	 * When Client calls updatePassword by using HttpClient, automatically,
	 * put() aUser of the argument with this tag to DataContainer.
	 */
	public static final String CONFIRM_PASSWORD = "CONFIRM_PASSWORD";

	/**
	 * Request identification ID for facility, inputting key.
	 * <P>
	 * When Client calls transmit() by using HttpClient, automatically,
	 * put() aRequestID of the argument with this tag to DataContainer.
	 */
	public static final String REQUEST_ID = "REQUEST_ID";

	public static final String EI_SYNC_MODE = "EI_SYNC_MODE";
	public static final String EI_SYNC = "EI_SYNC";
	public static final String EI_ASYNC = "EI_ASYNC";
	
	// @migration - Moved this definition to Core package class
	public final static String	QICSMethod	= "QICSMethod";
	/* @Migration - move this definition for Broadcast class */
	//@KM
	public final static String	DEFECT_REPAIRED= "DEFECT_REPAIRED";
	public final static String  HAS_DEFECT="HAS_DEFECT";
	
	public final static String	ProductSpecCode = "PRODUCT_SPEC_CODE";
	/**
     * @OIM70
     * 
     * This value referenced by this tag should be a space-separated list of tag values
     * that should not be stored as part of the controller beans state.  The tags in the
     * list are returned to the client, but they are stripped from the DataContainer
     * stored by the Controller bean.
     * 
     * A task would use this attribute to keep large results from being stored in the state.
     * 
     */
    public static final String DONT_KEEP_IN_STATE = "CONTROLLER.DONT_KEEP";

	/**
	 * @OIM50
	 * The presence of this tag indicates the the request processor servlet should
	 * create a session if one does not exist.
	 */
	public final static String HTTP_SESSION_REQUIRED = "HTTP_SESSION_REQUIRED";
	
	
	/**
	 * @OIM50
	 * The presence of this tag indicates that the HTTPSession should be destroyed.
	 */
	public final static String HTTP_SESSION_DESTROY = "HTTP_SESSION_DESTROY";

    
	/**
	 * @OIM50
	 * The presence of this tag indicates that the server should validate the
	 * HTTP Session.  A new session will be created only if the HTTP_SESSION_REQUIRED
	 * tag is also present.  If the session is not there and no new session is to
	 * be created,then the server will not process the request, instead throwing an
	 * exception.
	 */	
	public final static String HTTP_SESSION_VALIDATE = "HTTP_SESSION_VALIDATE";
	
	/**
	 * @OIM50
	 * This tag is used to store an embedded data container in the response from 
	 * a transmit call.  On the client side, the embedded container will be processed.
	 */
	public final static String ROUTER_FILTER_DATA_CONTAINER = "ROUTER_FILTER_DATA_CONTAINER";
	
	
	/**
	 * @JM074
	 * This instructional tag is used to format the response to an xml application.
	 * If the value is set to "true", then the data type names returned in the response
	 * will not be fully qualified Java classes. The package name will be removed. 
	 */
	public static final String XML_USE_GENERIC_TYPE_NAMES = "XML_USE_GENERIC_TYPE_NAMES";

	/**
	 * @JM074
	 * This instructional tag is used to format the response to an xml application.
	 * If the value is set to "true", then String data will be returned as the value
	 * text instead of as a typed value format. 
	 */	
	public static final String XML_USE_SIMPLE_STRING_DATA_FORMAT = "XML_USE_SIMPLE_STRING_DATA_FORMAT";
	
	/**
	 * @JM074
	 * This instructional tag is used to format the response to an xml application.
	 * If the value is set to "true", then the data container elements will be returned 
	 * with the key values as the XML tag values, instead of the generic keyValue format. 
	 */	
	public static final String XML_USE_KEYS_AS_TAGS = "XML_USE_KEYS_AS_TAGS";
	
	
	/**
	 * @JM073
	 * This tag is used to store the TaskSessionData object in the DataContainer
	 * that is passed to a task.
	 */
	public static final String TASK_SESSION_DATA = "TASK_SESSION_DATA";             	

    /**
     * @JM073
     * This tag is used to pass the requested session ID obtained from the 
     * HttpRequest object to the Controller bean.
     */
	public static final String SESSION_KEY = "SESSION_KEY";
	
	/**
	 * @JM073
	 * If this flag is in the DataContainer, the controller will try to restore
	 * its state.
	 */
	public static final String RECOVER_SESSION = "RECOVER_SESSION";            	


	public static final String PROCESS_COMPLETE = "PROCESS_COMPLETE";
	
	/**
	 * Tags for read from OPC
	 */
	public static final String EI_OP_MODE = "EI_OP_MODE";
	public static final String EI_OP_MODE_READ = "EI_OP_MODE_READ";
	
	
	/**
	 * @JM200723
	 * The OPC converter will allow the object referenced with this object to 
	 * pass through unconverted so that the client side filter can pull it out.
	 */
	public static final String CLIENT_FILTER_DATA = "CLIENT_FILTER_DATA";

	/**
	 * @PC80115
	 * The Equipment Data Convertor will add tag list which has the same sequence as
	 * in device data format into data container.
	 */
	public static final String TAG_LIST = "TAG_LIST";
	
	/**
	 * Tag to hold product obj
	 */
	public static final String PRODUCT = "PRODUCT";
	public static final String PRODUCT_ID = "PRODUCT_ID";
	public static final String PRODUCTION_LOT = "PRODUCTION_LOT";
	public static final String PRODUCT_TYPE = "PRODUCT_TYPE";
	public final static String Product = "Product";
	
	public static final String INSTALLED_PART_NAME = "INSTALLED_PART_NAME";
	
	/**
	 * Tag to hold product spec object
	 */
	public static final String PRODUCT_SPEC = "PRODUCT_SPEC";
	
	public static final String PRODUCT_SPEC_CODE = "PRODUCT_SPEC_CODE";
	
	public static final String PRINTER_TYPE = "PRINTER_TYPE";
	
	public static final String PRINTER_DESTINATION = "PRINTER_DESTINATION";
	
	public static final String PRINT_FORMAT = "PRINT_FORMAT";
	
	public static final String PRINT_PDF = "PRINT_PDF";
	
	public static final String PRINT_POSTSCRIPT = "PRINT_POSTSCRIPT";
	
	public static final String FORM_ID = "FORM_ID";
	
	public static final String QUEUE_NAME = "QUEUE_NAME";
	
	public static final String TEMPLATE_NAME = "TEMPLATE_NAME";
	
	public static final String PRINTER_NAME = "PRINTER_NAME";
	
	public static final String INSTALLED_PART = "INSTALLED_PART";
	
	public static final String TEXT_ID = "TEXT_ID";
	
	public static final String JASPER_REPORT_PAGE_TYPE = "JASPER_REPORT_PAGE_TYPE";
	
	public static final String JASPER_REPORT_PAGE_ORIENTATION = "JASPER_REPORT_PAGE_ORIENTATION";  //page orientation
	
	public static final String JASPER_REPORT_DUPLEX_TUMBLE = "JASPER_REPORT_DUPLEX_TUMBLE";
	
	public static final String DATA_COLLECTION_COMPLETE = "DATA_COLLECTION_COMPLETE";
	public static final String DATA_COLLECTION_STATUS = "DATA_COLLECTION_COMPLETE";
	
	public static final String CHANNEL_NAME = "CHANNEL_NAME";
	
	public static final String QUEUE_MANAGER_NAME = "QUEUE_MANAGER_NAME";
	
	public static final String PRINT_QUANTITY = "PRINT_QUANTITY";
	
	public static final String PART_INFO_LIST = "PART_INFO_LIST";
	
	public static final String NO_GOOD_PARTS_LIST = "NO_GOOD_PARTS_LIST";
	public static final String PART_COUNT = "PART_COUNT";
	public static final String PRODUCT_TYPE_NAME = "PRODUCT_TYPE_NAME";
	public static final String DUNNAGE_NUMBER = "DUNNAGE_NUMBER";

	/**
	 * Tags for AF On Web service
	 */
	public static final String PRODUCT_SEQUENCE = "PRODUCT_SEQUENCE";
	
	public static final String NEXT_PRODUCT_ID = "NEXT_PRODUCT_ID";

	public static final String PREV_PRODUCT_ID = "PREV_PRODUCT_ID";
	
	public static final String ERROR_MESSAGE = "ERROR_MESSAGE";
	
	public static final String ERROR_CODE = "ERROR_CODE";
	
	public static final String EXCEPTION = "EXCEPTION";
	
	public static final String TRANSACTION_ID = "TRANSACTION_ID";
	
	public static final String FIXED_DEFECT_FLAG = "FIXED_DEFECT_FLAG";
	
	/**
	 * Tags for the jasper printer
	 */
	public static final String READY_FOR_AF_FLAG = "READY_FOR_AF_FLAG";
	
	public static final String READY_FOR_AF_TIME = "AF_READY";
	
	/**
	 * Tags for Weld Tracking
	 */
	public static final String CURRENT_LOT = "CURRENT_LOT";
	
	public static final String ACTUAL_TIME = "ACTUAL_TIME";
	
	public static final String MACHINE_TYPE = "MACHINE_TYPE";
	
	/**
	 * Tags for VinStampingService
	 */
	public static final String ATTRIBUTE_VALUES = "ATTRIBUTE_VALUES";
	
	public static final String ATTRIBUTES = "ATTRIBUTES";
	
	public static final String BOUNDARY_MARK = "BOUNDARY_MARK";
	
	public static final String COMPONENT_ID = "TERMINAL_ID";
	
	public static final String EXPECTED_VIN = "EXPECTED_VIN";
	
	public static final String EXT_COLOR = "EXT_COLOR";
	
	public static final String INFO_CODE = "INFO_CODE";
	
	public static final String INFO_MESSAGE = "INFO_MESSAGE";
	
	public static final String INT_COLOR = "INT_COLOR";
	
	public static final String KD_LOT = "KD_LOT";
	
	public static final String LAST_VIN = "LAST_VIN";
	
	public static final String MISSION_TYPE = "MISSION_TYPE";
	
	public static final String MODEL = "MODEL";
	
	public static final String NEXT_VIN = "NEXT_VIN";
	
	public static final String OPTION = "OPTION";
	
	public static final String STAMPED_VIN = "STAMPED_VIN";
	
	public static final String STAMPED_TIME = "STAMPED_TIME";
	
	public static final String TYPE = "TYPE";
	
	public static final String UPDATE = "UPDATE";
	
	/**
	 * Tags for ProductionLotBackoutService
	 */
	public static final String LOT_DATE = "LOT_DATE";
	
	public static final String LOT_PREFIX = "LOT_PREFIX";
	
	/**
	 * Tags for Reprint Ticket
	 */
	public static final String REPRINT_TICKET = "REPRINT_TICKET";
	
	public static final String LOT = "LOT";
	
	public static final String ROW_VALUE = "ROW_VALUE";
	
	public static final String SPACE_VALUE = "SPACE_VALUE";
		
	public static final String SHORT_VIN = "SHORT_VIN";
	
	public static final String COLOR_CODE = "COLOR_CODE";
	
	public static final String EXT_COLOR_CODE = "EXT_COLOR_CODE";
	
	public static final String GRADE_CODE = "GRADE_CODE";
	
	public static final String REPAIR_AREA = "REPAIR_AREA";
	
	public static final String IS_ASSIGN_AND_PRINT="IS_ASSIGN_AND_PRINT";
	
	public static final String DEFECT_RESULT_ID="DEFECT_RESULT_ID";
	
	public static final String KEY_VALUE_PAIR = "KEY_VALUE_PAIR";
	
	/*
	 * Tags for findByFieldRanges
	 */
	public static final String LINE_NO = "LINE_NO";
	
	public static final String AF_ON_SEQUENCE_NUMBER = "AF_ON_SEQUENCE_NUMBER";
	
	public static final String VIN_SERIAL = "VIN_SERIAL";
	
	public static final String TRACKING_STATUS = "TRACKING_STATUS";
	
	public static final String MODEL_YEAR_CODE = "MODEL_YEAR_CODE";
	
	public static final String MODEL_CODE = "MODEL_CODE";
	
	public static final String MODEL_TYPE_CODE = "MODEL_TYPE_CODE";
	
	public static final String MODEL_OPTION_CODE = "MODEL_OPTION_CODE";
	
	public static final String INT_COLOR_CODE = "INT_COLOR_CODE";
	
	public static final String ENGINE_SERIAL_NO = "ENGINE_SERIAL_NO";
	
	public static final String ENGINE_MTO = "ENGINE_MTO";
	
	public static final String KD_LOT_NUMBER = "KD_LOT_NUMBER";
	
	public static final String ACTUAL_TIMESTAMP = "ACTUAL_TIMESTAMP";
	
	public static final String PARKING_LOCATION = "PARKING_LOCATION";
	
	public static final String UNIT_RELEASE = "UNIT_RELEASE";
	
	public static final String IS_ERROR = "IS_ERROR";
	
	public static final String CARRIER_ID = "CARRIER_ID";
	
	public static final String COMBINATION_CODE = "COMBINATION_CODE";
	
	public static final String DELIMITER = "DELIMITER";
	
	/**
	 * Tags for Exceptional Outgoing
	 */
	public static final String ASSOCIATE_NO = "ASSOCIATE_NO";
	
	public static final String EXCEPTIONAL_OUT_COMMENT = "EXCEPTIONAL_OUT_COMMENT";
	
	/**
	 * Tags for hold
	 */
	public static final String HOLD_COMMENT = "HOLD_COMMENT";
	
	/**
	 * Tags for hold
	 */
	public static final String RELEASE_COMMENT = "RELEASE_COMMENT";
	
	
	/**
	 * Tags for print dunnage
	 */
	public static final String DATA_SOURCE = "DATA_SOURCE";

	public static final String DATA_STRING = "DATA_STRING";
	
	
	/**
	 * Tags for buck load service
	 */
	public static final String PLAN_CODE = "PLAN_CODE";
	public static final String VIN = "VIN";
	public static final String ALC_INFO_CODE = "ALC_INFO_CODE";
	public static final String FIF_CODES = "FIF_CODES";
	
	public static final String REQUEST_RESULT = "REQUEST_RESULT";
	
	public static final String KICKOUT_PRODUCTS = "KICKOUT_PRODUCTS";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String KICKOUT_COMMENT = "KICKOUT_COMMENT";
	
	/**
	 * Tags for engine load
	 */
	
	public static final String ENGINE_SN_STATUS = "ENGINE_SN_STATUS";
	
	public static final String ENGINE_LOADED = "ENGINE_LOADED";
	
	public static final String VIN_LOADED = "VIN_LOADED";
	
	public static final String ENGINE_FIRED = "ENGINE_FIRED";
	
	public static final String COMMIT_READY = "COMMIT_READY";
	
	public static final String FRAME_YMTOC = "FRAME_YMTOC";

	public static final String PART_SERIAL_NUMBER = "PART_SERIAL_NUMBER";
	public static final String PART_SN = "PART_SN";

	public static final String STATUS = "STATUS";

	public static final String STATUS_OK = "STATUS_OK";
	
	public static final String PART_MASK = "PART_MASK";
	public static final String PART_MASKS = "PART_MASKS";
	public static final String INSTRUCTION_CODE ="INSTRUCTION_CODE";
	public static final String TORQUE_COUNT="TORQUE_COUNT";
	public static final String MIN_LIMIT="MIN_LIMIT";
	public static final String MAX_LIMIT="MAX_LIMIT";
	public static final String PART_NUMBER = "PART_NUMBER";
	
	/**
	 * Don't let anyone instantiate this class.
	 */
	private DataContainerTag() {
		super();
	}
}
