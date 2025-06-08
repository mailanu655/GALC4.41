package com.honda.galc.device.simulator.client;

/**
 * @migration - Moved constants from HTTPRequestDispatcher servlet in order to 
 * prevent dependencies on web application.
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>
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
 * <TD>martinek</TD>
 * <TD>Mar 18, 2004</TD>
 * <TD>1.0</TD>
 * <TD>OIM50</TD>
 * <TD>R_TYPE_PING and R_TYPE_TERMINATE</TD>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Jul 15, 2004</TD>
 * <TD>1.0</TD>
 * <TD>JM001</TD>
 * <TD>R_TYPE_EXECUTE_STATELESS, R_TYPE_TRANSMIT_STATELESS support</TD>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Feb 16, 2005</TD>
 * <TD></TD>
 * <TD>@JM040</TD>
 * <TD>START_RESULT_REQUEST_ROUTER_PORT</TD>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Feb 13, 2006</TD>
 * <TD></TD>
 * <TD>@JM089</TD>
 * <TD>TerminalInit support</TD>
 * </TR>
 * <TR>
 * <TD>martinek</TD>
 * <TD>Jul 24, 2007</TD>
 * <TD></TD>
 * <TD>@JM200723</TD>
 * <TD>TERMINAL_INIT_TERMINAL_PROPERTIES</TD>
 * </TR>
 * </TABLE>
 * 
 */
public interface HTTPRequestDispatcherTags { 
    /**
     * The request type of connect.
     */
    public static final String R_TYPE_CONNECT = "R_TYPE_CONNECT";
    /**
     * The request type of login.
     */
    public static final String R_TYPE_LOGIN = "R_TYPE_LOGIN";
    /**
     * The request type of execute.
     */
    public static final String R_TYPE_EXECUTE = "R_TYPE_EXECUTE";
    
    /**
     * @JM001
     * The request type of execute stateless controller.  This call
     * does not result in any state being stored on the server. 
     */
    public static final String R_TYPE_EXECUTE_STATELESS = "R_TYPE_EXECUTE_STATELESS";
    
    /**
     * The request type of init.
     */
    public static final String R_TYPE_INIT = "R_TYPE_INIT";

    /**
     * The request type of transmit.
     */
    public static final String R_TYPE_TRANSMIT = "R_TYPE_TRANSMIT";

	/**
	 * @JM001
	 * The request type of transmit - but use a stateless controller.
	 */
	public static final String R_TYPE_TRANSMIT_STATELESS = "R_TYPE_TRANSMIT_STATELESS";
    
    /**
     * The request type of disconnect.
     */
    public static final String R_TYPE_DISCONNECT = "R_TYPE_DISCONNECT";
    
    /**
     * The request type of start.
     */
    public static final String R_TYPE_START = "R_TYPE_START";
    
    /**
     * @OIM50
     * The request type of terminate. This is different from disconnect, which
     * is a process-point definition.  Terminate indicates that the client is 
     * exiting completely. Primarily used to clear HTTP session, but can also
     * be used for other "end of client" activities that can't be handled in
     * disconnect.
     */
    public static final String R_TYPE_TERMINATE = "R_TYPE_TERMINATE";
    
    
    /**
     * @OIM50
     * This request type is a no-op, but may be used to have other processing done, such
     * as establishing or closing sessions with the HTTP_SESSION_REQUIRED or HTTP_SESSION_DESTROY
     * tags.
     */
    public static final String R_TYPE_PING = "R_TYPE_PING";
    
    /**
     * @JM089
     * This request type is used prior to client login to download terminal settings that must
     * be loaded prior to any screen creation.
     */
    public static final String R_TYPE_TERMINAL_INIT = "R_TYPE_TERMINAL_INIT";
    
    /**
     * The key of DataContainer that
     * between HTTPRequestDispatcher and HTTPClient is local. <Br>
     * A exception, inputting key.
     */
    public static final String EXCEPTION = "EXCEPTION";
    
    /**
     * The key of DataContainer element that contains exceptions that 
     * are otherwise swallowed during transmit processing.
     * @oim50.1
     */
    public static final String TRANSMIT_EXCEPTION= "TRANSMIT_EXCEPTION";
    
    /**
     * The key of DataContainer that
     * between HTTPRequestDispatcher and HTTPClient is local. <Br>
     * The request type, inputting key.
     */
    public static final String HTTP_REQUEST_TYPE = "HTTP_REQUEST_TYPE";
    /**
     * The key of DataContainer that
     * between HTTPRequestDispatcher and HTTPClient is local. <Br>
     * The result of login, inputting key.
     */
    public static final String LOGIN_RESULT = "LOGIN_RESULT";
    /**
     * The key of DataContainer that
     * between HTTPRequestDispatcher and HTTPClient is local. <Br>
     * The result of start, inputting key.
     */
    public static final String START_RESULT = "START_RESULT";
    /**
     * The key of DataContainer that
     * between HTTPRequestDispatcher and HTTPClient is local. <Br>
     * The ApplicationInfo of start() method, inputting key.
     */
    public final static java.lang.String START_RESULT_APPINFO =
        "START_RESULT_APPINFO";
    /**
     * The key of DataContainer that
     * between HTTPRequestDispatcher and HTTPClient is local. <Br>
     * The MenuNode infomation of start() method, inputting key.
     */
    public final static java.lang.String START_RESULT_NODEINFO =
        "START_RESULT_NODEINFO";
        
    /**
     * @JM040
     * Key for Integer object that contains the port number.  May not
     * always be in start results.
     */
    public final static String START_RESULT_REQUEST_ROUTER_PORT = "START_RESULT_REQUEST_ROUTER_PORT";
    
    /**
     * @JM089
     * This tag is used to obtain some of the results of the terminalInit call.
     */
    public final static String TERMINAL_INIT_RESULT_RECEIVER_PORT = "INIT_TERMINAL_RESULT_RECEIVER_PORT";


    /**
     * @JM200723
     * This tag is used to obtain the Terminal Property List, which is in the results
     * of the terminalInit call.
     * 
     */
    public final static String TERMINAL_INIT_TERMINAL_PROPERTIES = "TERMINAL_INIT_TERMINAL_PROPERTIES";
}
