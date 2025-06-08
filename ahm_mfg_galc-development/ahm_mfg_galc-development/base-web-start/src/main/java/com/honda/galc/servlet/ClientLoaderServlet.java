package com.honda.galc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.honda.galc.common.logging.LogContext;
import com.honda.galc.common.logging.LogLevel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.logging.ServerSideLoggerConfig;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.entity.conf.WebStartBuild;
import com.honda.galc.entity.conf.WebStartClient;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.webstart.WebStartConfiguration;
import com.honda.galc.webstart.WebStartConstants;
import com.honda.galc.webstart.WsClientConfig;

/**
 * <h3>Class description</h3>
 * This dude pulls configuration information from the database and based on the
 * applications that are listed for this terminal/client, it either builds a web
 * page with a bunch of links to those applications, or if only one application
 * is found, like in the case of a production terminal, it just forwards the
 * response to the jsp page that dynamically generates the appropriate JNLP page
 * to start the correct application
 * 
 * It sets certain values in session attributes so the JNLP generation page can
 * point to the right location, pass in the right parameters, etc.
 * 
 * This servlet works in conjunction with generate_jnlp.jsp
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Updates</h4>
 * Major changes since 1.1 include<br>
 * 1. Database table schema change <br>
 * 2. Refactoring <br>
 * 3. Properties <br>
 * 4. jnlp <br>
 * 5. Code cleanup <br>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dana Cleverdon</TD>
 * <TD>2007</TD>
 * <TD>1.1</TD>
 * <TD>@DC</TD>
 * <TD>now using GALC data tables</TD>
 * </TR>
 *
 * <TR>
 * <TD>Guang Yang</TD>
 * <TD>Dec 21, 2007</TD>
 * <TD>2.0</TD>
 * <TD>@GY 20071221</TD>
 * <TD>Major Change</TD>
 * </TR>
 *
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Apr 18, 2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL018</TD>
 * <TD>Web start fixes: DB tables names and error handling</TD>
 * </TR>
 * 
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Jun 02, 2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL023</TD>
 * <TD>Client configuraiton cache</TD>
 * </TR>
 * 
 * <TR>
 * <TD>Jeffray Huang</TD>
 * <TD>Oct 02, 2010</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL023</TD>
 * <TD>work with JPA</TD>
 * </TR>
 * 
 * </TABLE>
 * @see 
 * @ver 1.1
 * @author Dana Cleverdon
 */

public class ClientLoaderServlet extends HttpServlet {

	private static final long serialVersionUID = -5972731103426461699L;

	private static final String WEB_START = "Web Start";

	private Logger logger;
	
	private final static String V1_JNLP = "generate_jnlp.jsp";
	private final static String V2_JNLP = "/hma_generate_jnlp.jsp";
	private String jnlpFile = V1_JNLP;
	boolean isLinux;
	
	/**
	 * do initial setup items like loading properties
	 */
	public void init(javax.servlet.ServletConfig config) throws ServletException {
		super.init(config);

		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		ApplicationContextProvider.setApplicationContext(ctx); 

		//allow log4j to load the log4j.property dynamically based on the machine host name or preferred suffix
		ServerSideLoggerConfig.configLog4j();
		
		initLogContext();
	}

	private void initLogContext(){
		LogContext context = LogContext.getContext();
		context.setApplicationName(WEB_START);
		context.setApplicationLogLevel(getLogLevel());
		System.out.println("Current Log Level for Web Start " + context.getApplicationLogLevel());
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		  getLogger().info("ClientLoaderServlet", "Received request (" + request.getMethod() + ") :" + request.getRequestURI() + "?" + request.getQueryString());

		  isLinux = request.getHeader("User-Agent").indexOf("JNLP") > -1 || request.getHeader("User-Agent").indexOf("Linux") > -1;
		  
		  if ( request.getRequestURL().indexOf("Production.jnlp") < 0 ) {
			  doGetV1(request,response);
		  } else {
			  doGetV2(request,response);
		  }
	}
	
	
	
	
	
	public void doGetV1(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
		final String logMethod = "doGet():";
		jnlpFile  = V1_JNLP;
		
		PropertyService.refreshComponentProperties("ClientLoaderServlet");

		try {
			getLogger().info(logMethod, "Received request (" + request.getMethod() + ") :" + request.getRequestURI() + "?" + request.getQueryString());
			String remoteAddr = request.getRemoteAddr();

			// get client name
			String hostName = request.getParameter("machine");
			if (hostName==null) hostName = request.getRemoteAddr();
			if (hostName==null) hostName = request.getRemoteHost();

			if (hostName == null) {
				response.getWriter().println(
						"<h2>Error in client lookup</h2><h4>Client name was null.  It should be passed as URL argument:<br>"
						+ this.getThisURLPath(request)
						+ "<br><br>"
						+ this.getServletName()
						+ " <br><br>Please contact ISD.</h4>");
				throw new Exception("Client name was null");
			}

			getLogger().info(logMethod, "Client: remote address: " + remoteAddr + ", host name: " + hostName);

			// always refresh property from database first 
			PropertyService.refreshComponentProperties(hostName);

			WsClientConfig clientConfig = WebStartConfiguration.getInstance().getClientConfig(hostName, isLinux);
			if(clientConfig==null)clientConfig =  WebStartConfiguration.getInstance().getClientConfig(remoteAddr,isLinux);

			WebStartClient aClient = null;
			WebStartBuild targetBuild = null;

			if (clientConfig != null) {
				aClient = clientConfig.getWsClient();
				targetBuild = clientConfig.getBuild();
			}		

			String buildId = null;

			if (aClient != null) {
				hostName = aClient.getHostName();
				if (clientConfig.isDefaultBuild() && targetBuild == null) {
					getLogger().warn(logMethod, "Default build is not defined.");

					response.getWriter().println(
							"<h2>Default build is not defined</h2>"
							+ "<h4><br><br><br>Please contact ISD.</h4>");

					throw new Exception("Default build is not defined.");
				}

				getLogger().info(logMethod, "Client is " + hostName);

				if (targetBuild == null) {
					getLogger().warn(logMethod, "No build data for: " + buildId);
					noDataFound(hostName + buildId, response);
				}
			} else {
				getLogger().warn(logMethod, "No client was found. Default build will be used.");
				response.getWriter().println(
						"<h2>Client was not found and default client is not defined</h2>" +
						"<h4>Please set DEFAULT client or add client with IP " + remoteAddr +
				"<br><br><br>Please contact ISD.</h4>");
				throw new Exception("Neither client nor Default IP are defined");
			}

			setupSessionAttributes(request, clientConfig);

			processResult(request, response);				// Process request

		} catch (Exception ex) {
			getLogger().error(ex,logMethod, "Exception: ");
			handleException(ex, response);
		}

	}

	
	public void doGetV2(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {

		final String logMethod = "doGet():";
		jnlpFile  = V2_JNLP;

		PropertyService.refreshComponentProperties("ClientLoaderServlet");

		try {
			getLogger().info(logMethod, "Received request (" + request.getMethod() + ") :" + request.getRequestURI() + "?" + request.getQueryString());
			String remoteAddr = request.getRemoteAddr();

			getLogger().info(logMethod, "URI" + request.getRequestURI());


			// get client name
			String hostName = request.getRemoteHost();
			String path =request.getRequestURI();
			String parts [] = path.split("/");
			remoteAddr = parts[2];

			getLogger().info(logMethod, "Client: remote address: " + remoteAddr + ", host name: " + hostName);


			// always refresh property from database first 
			PropertyService.refreshComponentProperties(hostName);
			PropertyService.refreshComponentProperties(remoteAddr);
			
		    WsClientConfig clientConfig = WebStartConfiguration.getInstance().getClientConfig(remoteAddr, isLinux);

			WebStartClient aClient = null;
			WebStartBuild targetBuild = null;

			if (clientConfig != null) {
				aClient = clientConfig.getWsClient();
				targetBuild = clientConfig.getBuild();
			}		

			String buildId = null;

			if (aClient != null) {
				hostName = aClient.getHostName();
				if (clientConfig.isDefaultBuild() && targetBuild == null) {
					getLogger().warn(logMethod, "Default build is not defined.");

					response.getWriter().println(
							"<h2>Default build is not defined</h2>"
							+ "<h4><br><br><br>Please contact ISD.</h4>");

					throw new Exception("Default build is not defined.");
				}

				getLogger().info(logMethod, "Client is " + hostName);

				if (targetBuild == null) {
					getLogger().warn(logMethod, "No build data for: " + buildId);
					noDataFound(hostName + buildId, response);
				}



			} else {
				getLogger().warn(logMethod, "No client was found. Default build will be used.");
				response.getWriter().println(
						"<h2>Client was not found and default client is not defined</h2>" +
						"<h4>Please set DEFAULT client or add client with IP " + remoteAddr +
				"<br><br><br>Please contact ISD.</h4>");
				throw new Exception("Neither client nor Default IP are defined");
			}

			setupSessionAttributesV2(request, clientConfig);

			response.setDateHeader("Last-Modified",System.currentTimeMillis());
			response.setDateHeader("Expires",System.currentTimeMillis()+1000);
			response.addHeader("Cache-Control","no-cache");
			response.addHeader("Cache-Control","must-revalidate");
			response.addHeader("Cache-Control","max-age=1");
			response.addHeader("Cache-Control","s-maxage=1");
			response.addHeader("Pragma","no-cache");
			response.addHeader("Retry-After","120");
			response.addHeader("Refresh","5");		
			response.setContentType("application/x-java-jnlp-file");



			processResult(request, response);				// Process request

		} catch (Exception ex) {
			getLogger().error(ex,logMethod, "Exception: ");
			handleException(ex, response);
		}

	}	
	
	/**
	 * process the result of the client lookup, building the necessary links to be
	 * added to the launcher web page if when we finish processing the
	 * configured applications there is only one, then we just forward to the JNLP
	 * generator rather than rendering a web page with a single link (because that
	 * would be dumb)
	 */
	private void processResult(HttpServletRequest request, HttpServletResponse response) {
		final String logMethod = "processResult():";

		RequestDispatcher dispatcher = request.getRequestDispatcher(jnlpFile);
		if (dispatcher != null) {
			try {
				dispatcher.forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
				getLogger().error(e,logMethod, "ServletException: ");
			} catch (IOException e) {
				e.printStackTrace();
				getLogger().error(e,logMethod, "IOException: ");
			}
		}
	}

	/**
	 * add all the required information to session attributes that can be
	 * retrieved by the JNLP generation function
	 */
	private void setupSessionAttributes(HttpServletRequest request, WsClientConfig cf) {
		final String logMethod = "setupSessionAttributes():";

		HttpSession session = request.getSession();

		session.setAttribute(WebStartConstants.JAR_FILES, cf.getJars());

		session.setAttribute(WebStartConstants.LINE_ID, "????");
		session.setAttribute(WebStartConstants.CODEBASE, getThisURLPath(request));
		session.setAttribute(WebStartConstants.MAIN_CLASS, cf.getMainClass());
		session.setAttribute(WebStartConstants.LAST_MODIFIED, cf.getLastModified());
		session.setAttribute(WebStartConstants.TARGET_BUILD, cf.getBuild());
		session.setAttribute(WebStartConstants.JNLP_ARGS, cf.getArgs());
		session.setAttribute(WebStartConstants.INITIAL_HEAP_SIZE, cf.getInitialHeapSize());
		session.setAttribute(WebStartConstants.MAX_HEAP_SIZE, cf.getMaxHeapSize());
		
		getLogger().info(logMethod, "Args =  " + cf.getArgs().toString());
	}
	
	
	  private void setupSessionAttributesV2(HttpServletRequest request, WsClientConfig cf) {
		  final String logMethod = "setupSessionAttributes():";
		  
		  String path =request.getRequestURI();
	      String parts [] = path.split("/");
	      String terminal = parts[2];
	      String application = parts[3];
		  
		
		  HttpSession session = request.getSession();
		  
		  session.setAttribute(WebStartConstants.JAR_FILES, cf.getJars());
		  session.setAttribute(WebStartConstants.LINE_ID, "????");
		  session.setAttribute(WebStartConstants.CODEBASE, getThisURLPath(request));
		  session.setAttribute(WebStartConstants.TERMINAL_ID,terminal);
		  session.setAttribute(WebStartConstants.APPLICATION,application);
		  session.setAttribute(WebStartConstants.MAIN_CLASS, cf.getMainClass());
		  session.setAttribute(WebStartConstants.LAST_MODIFIED, cf.getLastModified());
		  session.setAttribute(WebStartConstants.JNLPNAME, "webstart.jnlp");
		  session.setAttribute(WebStartConstants.TARGET_BUILD, cf.getBuild());
		  session.setAttribute(WebStartConstants.JNLP_ARGS, cf.getArgs());
		  session.setAttribute(WebStartConstants.INITIAL_HEAP_SIZE, cf.getInitialHeapSize());
		  session.setAttribute(WebStartConstants.MAX_HEAP_SIZE, cf.getMaxHeapSize());
		  
		  getLogger().info(logMethod, "Args =  " + cf.getArgs().toString());
	}  
		  
	  

	/*
	 * utility method to get the servlets URL up to the context path (not
	 * including servlet name)
	 */
	private String getThisURLPath(HttpServletRequest request) {
		StringBuilder serverUrl = new StringBuilder();
		serverUrl.append("http://");
		serverUrl.append(request.getServerName());
		if (request.getServerPort() != 80) {
			serverUrl.append(':');
			serverUrl.append(request.getServerPort());
		}
		serverUrl.append(request.getContextPath());
		serverUrl.append('/');
		return serverUrl.toString();
	}

	/**
	 * found no data when retrieving client info from the config database
	 */
	private void noDataFound(String client, HttpServletResponse response) throws IOException {
		showErrorPage("No configuration data found for client : " + client, response);
	}

	/**
	 * we don't support this other than to forward to the doGet method
	 * 
	 * @see javax.servlet.http.HttpServlet#void
	 *      (javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
	}

	/*
	 * just write an error to the browser output
	 */
	private void showErrorPage(String error, HttpServletResponse response) {
		final String logMethod = "showErrorPage():";

		try {
			response.getWriter().println(error);
		} catch (IOException ioex) {
			getLogger().error(ioex,logMethod, "IO Error writing this exception to servlet response.");
		}
	}

	/*
	 * handle any kind of exception
	 */
	private void handleException(Exception ex, HttpServletResponse response) {
		response.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
		showErrorPage("Client lookup returned an exception:<br>" + getStackTraceString(ex), response);
	}

	private String getStackTraceString(Exception ex) {
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	private LogLevel getLogLevel() {
		return LogLevel.DATABASE;
	}

	private Logger getLogger() {
		if(logger == null) logger = Logger.getLogger(WEB_START);
		return logger;
	}
}
