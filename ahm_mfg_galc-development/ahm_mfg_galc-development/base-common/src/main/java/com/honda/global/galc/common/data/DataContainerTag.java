package com.honda.global.galc.common.data;


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
public class DataContainerTag {
	/**
	 * @sm
	 * Straggler indicator
	 * <P>If key exists in the Datacontainerand value is TRUE then the body is a straggler.
	 */
	public static final String STRAGGLER_FLAG = "STRAGGLER_FLAG";
	
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

    public static final Object BROADCAST_HEARTBEAT_TARGET_HOST = "BROADCAST_HEARTBEAT_TARGET_HOST";
	
	/**
	 * Don't let anyone instantiate this class.
	 */
	private DataContainerTag() {
		super();
	}
}
