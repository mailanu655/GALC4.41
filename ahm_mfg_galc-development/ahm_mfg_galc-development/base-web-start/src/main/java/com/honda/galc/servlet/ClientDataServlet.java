package com.honda.galc.servlet;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.honda.galc.common.logging.LogContext;
import com.honda.galc.common.logging.LogLevel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.logging.ServerSideLoggerConfig;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.conf.WebStartBuildDao;
import com.honda.galc.dao.conf.WebStartClientDao;
import com.honda.galc.dao.conf.WebStartDefaultBuildDao;
import com.honda.galc.dao.product.FeatureDao;
import com.honda.galc.dao.product.FeatureTypeDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.entity.conf.WebStartBuild;
import com.honda.galc.entity.conf.WebStartClient;
import com.honda.galc.entity.conf.WebStartDefaultBuild;
import com.honda.galc.entity.product.Feature;
import com.honda.galc.entity.product.FeatureType;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.util.SortedArrayList;
import com.honda.galc.webstart.WebStartConstants;
import com.honda.galc.webstart.WebStartConstants.Action;
import com.ibm.ejs.ras.RasHelper;


/**
 * <h3>Class description</h3>
 * This class works as the controller of GALC Web Start Maintenance System.
 * It is responsible for redirecting requests to appropriate pages as well
 * as accessing database.
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
 * <TD>Initial Release</TD>
 * </TR>
 *
 * <TR>
 * <TD>Guang Yang</TD>
 * <TD>Dec 21, 2007</TD>
 * <TD>2.0</TD>
 * <TD>@GY 20071221</TD>
 * <TD>Major Change</TD>
 * </TR>
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Apr 18, 2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL018</TD>
 * <TD>Web start fixes: DB tables names and error handling</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 1.1
 * @author Dana Cleverdon
 */
public class ClientDataServlet extends HttpServlet implements Servlet {


	private static final long serialVersionUID = 6848757292550691772L;

	private String jarUrl = null;

	private Logger logger;

	private static final String WEB_START = "Web Start";



	/**
	 * do initial setup items like loading properties
	 */
	public void init(javax.servlet.ServletConfig config) throws ServletException {
		super.init(config);

		//allow log4j to load the log4j.property dynamically based on the machine host name or preffered suffix
		ServerSideLoggerConfig.configLog4j();

		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		ApplicationContextProvider.setApplicationContext(ctx); 


		initLogContext();

	}

	private void initLogContext(){
		LogContext context = LogContext.getContext();
		context.setApplicationName(WEB_START);
		context.setApplicationLogLevel(getLogLevel());
		System.out.println("Current Log Level for Web Start " + context.getApplicationLogLevel());
	}


	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest arg0,
	 *      HttpServletResponse arg1)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		WebStartClientDao clientMgr = getDao(WebStartClientDao.class);
		WebStartBuildDao buildMgr  = getDao(WebStartBuildDao.class);

		WebStartConstants.Action action = getAction(request);

		WebStartClient aClient = null;
		WebStartBuild aBuild = null;
		getLogger().info("doGet:" + action);

		switch (action) {
		case LIST_CLIENTS:
			reloadSessionAttribute(request, response, WebStartConstants.CLIENTS);
			// refresh all clients from db
			forwardTo("web_start_clients.jsp", request, response);
			break;

		case ADD_CLIENT:
			reloadSessionAttribute(request, response,
					WebStartConstants.TERMINALS);
			request.getSession().setAttribute(WebStartConstants.BUILDS,
					getAllBuildsWithDefault());
			request.getSession().setAttribute(WebStartConstants.FEATURETYPES,
					getFeatureTypes());

			response.resetBuffer();
			forwardTo("add_client.jsp", request, response);
			break;

		case EDIT_CLIENT:
			aClient = clientMgr.findByKey(request
					.getParameter(WebStartConstants.IP_ADDRESS));
			if (aClient == null) {
				reloadSessionAttribute(request, response,
						WebStartConstants.CLIENTS);
				reloadSessionAttribute(request, response,
						WebStartConstants.BUILDS);
				forwardTo("web_start_clients.jsp", request, response);
			} else {
				request.getSession().setAttribute(WebStartConstants.BUILDS,
						getAllBuildsWithDefault());
				request.getSession().setAttribute(WebStartConstants.FEATURETYPES,
						getFeatureTypes());
				request.getSession().setAttribute(WebStartConstants.FEATUREIDS,
						getFeatureIds(aClient.getFeatureType()));
				request.getSession().setAttribute(
						WebStartConstants.WEBSTART_CLIENT, aClient);
				reloadSessionAttribute(request, response, WebStartConstants.TERMINALS);
				forwardTo("edit_client.jsp?action=" + action, request, response);
			}
			reloadSessionAttribute(request, response,
					WebStartConstants.AVAILABLE_TERMINALS);
			break;

		case DELETE_CLIENT:
			request
			.getSession()
			.setAttribute(
					WebStartConstants.WEBSTART_CLIENT,
					clientMgr.findByKey(request.getParameter(WebStartConstants.IP_ADDRESS)));
			forwardTo("delete_client.jsp", request, response);
			break;

		case SHOW_CLIENT:
			aClient = clientMgr.findByKey(request
					.getParameter(WebStartConstants.IP_ADDRESS));
			if (aClient == null) {
				reloadSessionAttribute(request, response,
						WebStartConstants.CLIENTS);
				forwardTo("web_start_clients.jsp", request, response);
			} else {
				request.getSession().setAttribute(
						WebStartConstants.WEBSTART_CLIENT, aClient);
				forwardTo("show_client.jsp?", request, response);
			}
			break;

		case LIST_BUILDS:
			reloadSessionAttribute(request, response, WebStartConstants.BUILDS);
			forwardTo("galc_builds.jsp", request, response);
			break;

		case EDIT_BUILD:
			aBuild = buildMgr.findByKey(request.getParameter(WebStartConstants.BUILD_ID));
			if (aBuild == null) {
				reloadSessionAttribute(request, response,
						WebStartConstants.BUILDS);
				forwardTo("galc_builds.jsp", request, response);
			} else {
				request.getSession().setAttribute(
						WebStartConstants.WEBSTART_BUILD, aBuild);
				forwardTo("edit_build.jsp", request, response);
			}
			break;

		case DELETE_BUILD:
			request.getSession().setAttribute(WebStartConstants.WEBSTART_BUILD,
					buildMgr.findByKey(request.getParameter(WebStartConstants.BUILD_ID)));
			forwardTo("delete_build.jsp", request, response);
			break;

		case LIST_TERMINALS:
			reloadSessionAttribute(request, response,
					WebStartConstants.TERMINALS);
			forwardTo("galc_terminals.jsp", request, response);
			break;

		case ADD_BUILD:
			reloadSessionAttribute(request, response,
					WebStartConstants.BUILDS);
			forwardTo("create_new_build.jsp", request, response);
			break;

		case SET_DEFAULT_BUILD:
			reloadSessionAttribute(request, response, WebStartConstants.BUILDS);
			reloadSessionAttribute(request, response,
					WebStartConstants.BUILD_ID);
			reloadSessionAttribute(request, response,
					WebStartConstants.CELL_BUILDS);
			reloadSessionAttribute(request, response,
					WebStartConstants.BUILDS);
			reloadSessionAttribute(request, response,
					WebStartConstants.CURRENT_CELL_ID);
			forwardTo("default_build.jsp", request, response);
			break;

		case HOME:
		default:
			reloadSessionAttribute(request, response, WebStartConstants.LINE_ID);
			reloadSessionAttribute(request, response, WebStartConstants.CLIENTS);
			reloadSessionAttribute(request, response, WebStartConstants.BUILDS);
			reloadSessionAttribute(request, response,
					WebStartConstants.BUILD_ID);
			forwardTo("web_start_home.jsp", request, response);
		}

	}

	private Action getAction(HttpServletRequest request) {
		String ctxPath = request.getContextPath();
		String uri = request.getRequestURI();

		int startActPref = uri.indexOf(WebStartConstants.ACTION_SFX);

		// trim context path from URI
		String actionString = StringUtils.substring(uri, ctxPath.length() + 1, startActPref);

		Action action;
		try {
			action = Action.valueOf(actionString);
		} catch (IllegalArgumentException e) {
			// apply default value
			action = Action.HOME;
		}		

		return action;
	}

	/**
	 * the doPost is called as a result of the user submitting form requests
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		WebStartClientDao clientMgr = getDao(WebStartClientDao.class);
		WebStartBuildDao buildMgr  = getDao(WebStartBuildDao.class);

		WebStartClient aClient;
		String ipAddress;

		WebStartConstants.Action action = getAction(request);

		getLogger().info("doPost: " + action);

		try {
			switch (action) {
			case ADD_CLIENT:
				ipAddress= request.getParameter(WebStartConstants.IP_ADDRESS).trim();
				aClient = clientMgr.findByKey(ipAddress);
				if(aClient != null) {
					request.getSession().setAttribute(
							WebStartConstants.ERROR,
							"Could not add client. IP Address " + ipAddress +
							" has been used by client " + aClient.getHostName());
					forwardTo("web_start_error.jsp", request, response);
				}else {
					clientMgr.insert(createClientFromRequest(request, true));
					reloadSessionAttribute(request, response,
							WebStartConstants.CLIENTS);
					reloadSessionAttribute(request, response,
							WebStartConstants.BUILDS);
					reloadSessionAttribute(request, response,
							WebStartConstants.BUILD_ID);
					forwardTo("web_start_clients.jsp", request, response);
				}
				break;

			case UPDATE_CLIENT:
				String oldIpAddress = request.getParameter(WebStartConstants.OLD_IP_ADDRESS).trim();
				ipAddress = request.getParameter(WebStartConstants.IP_ADDRESS).trim();

				aClient = clientMgr.findByKey(ipAddress);
				clientMgr.removeByKey(oldIpAddress);
				clientMgr.insert(createClientFromRequest(request, true));
				reloadSessionAttribute(request, response, WebStartConstants.BUILDS);
				reloadSessionAttribute(request, response, WebStartConstants.CLIENTS);
				forwardTo("web_start_clients.jsp", request, response);
				break;

			case UPDATE_CLIENTS:
				Enumeration<String> e = request.getParameterNames();
				final int shutdownFlag = request.getParameter(WebStartConstants.SHUTDOWN_CHECKBOX) != null ? 1 : 0;
				while (e.hasMoreElements()) {
					String param = e.nextElement();
					// handle individual updates
					if (param.startsWith(WebStartConstants.SHUTDOWN_FLAG)) {
						String clientId = getClientIdFromRequestParam(param);
						updateClientRestartTime(WebStartConstants.CHECKBOX_ON.equals((request.getParameter(param)).trim()) ? shutdownFlag : -1, clientId);
					}
				}
				reloadSessionAttribute(request, response, WebStartConstants.BUILDS);
				reloadSessionAttribute(request, response, WebStartConstants.CLIENTS);
				forwardTo("web_start_clients.jsp", request, response);
				break;

			case DELETE_CLIENT:
				clientMgr.removeByKey(request.getParameter(WebStartConstants.IP_ADDRESS));
				reloadSessionAttribute(request, response,
						WebStartConstants.CLIENTS);
				forwardTo("web_start_clients.jsp", request, response);
				break;

			case ADD_BUILD:
				buildMgr.insert(createBuildFromRequest(request));
				reloadSessionAttribute(request, response,
						WebStartConstants.BUILDS);
				forwardTo("galc_builds.jsp", request, response);
				break;

			case UPDATE_BUILD:
				buildMgr.update(createBuildFromRequest(request));
				reloadSessionAttribute(request, response,
						WebStartConstants.BUILDS);
				forwardTo("galc_builds.jsp", request, response);
				break;

			case DELETE_BUILD:
				if (isBuildInUse(request
						.getParameter(WebStartConstants.BUILD_ID))) {
					request
					.getSession()
					.setAttribute(WebStartConstants.ERROR,
							"<p>The build is currently in use. It cannot be deleted.</p>");
					forwardTo("web_start_error.jsp", request, response);
				} else {
					buildMgr.removeByKey(request
							.getParameter(WebStartConstants.BUILD_ID));
					reloadSessionAttribute(request, response,
							WebStartConstants.BUILDS);
					forwardTo("galc_builds.jsp", request, response);
				}
				break;

			case SET_DEFAULT_BUILD:
				if (isDelete(request)) {
					getDao(WebStartDefaultBuildDao.class).removeByKey(request.getParameter(WebStartConstants.CELL_ID));
				} else {
					getDao(WebStartDefaultBuildDao.class).save(createDefaultBuildFromRequest(request));
				}				
				reloadSessionAttribute(request, response, WebStartConstants.LINE_ID);
				reloadSessionAttribute(request, response, WebStartConstants.CLIENTS);
				reloadSessionAttribute(request, response, WebStartConstants.BUILDS);
				reloadSessionAttribute(request, response, WebStartConstants.BUILD_ID);
				reloadSessionAttribute(request, response, WebStartConstants.CELL_BUILDS);
				reloadSessionAttribute(request, response, WebStartConstants.CURRENT_CELL_ID);
				forwardTo("default_build.jsp", request, response);
				break;

			default:
				forwardTo("web_start_home.jsp", request, response);
			}
		} catch (Exception e) {
			getLogger().error(e, "Error in doPost: " + e.getMessage());
			handleException(request, response, e);
		}
	}

	private String getClientIdFromRequestParam(String param) {
		return param.replace(WebStartConstants.SHUTDOWN_FLAG + "_", "");
	}

	private WebStartClient updateClientRestartTime(int shutdownFlag, String clientId) {
		WebStartClientDao clientMgr = getDao(WebStartClientDao.class);
		WebStartClient aClient = clientMgr.findByKey(clientId);

		if (aClient != null) {
			if (shutdownFlag == 0 && aClient.isShutdownRequested()) {
				return aClient; // don't remove shutdown request if shutdown flag is on but not requested
			}
			aClient.setShutdownFlag(shutdownFlag);
			clientMgr.update(aClient);
		}
		return aClient;
	}

	private boolean isDelete(HttpServletRequest request) {

		String submit = request.getParameter("actionCmd");

		return submit != null && submit.equalsIgnoreCase("delete");

	}


	private String getCurrentCellName() {

		String cellName = null;

		try {
			cellName = RasHelper.getServerName().split("\\\\", 2)[0];
			getLogger().info("The cell name is: " + cellName);
		} catch (Exception e) {
			getLogger().error(e, "Cannot determine the cell: ");
		}

		return cellName;
	}


	private void reloadSessionAttribute(HttpServletRequest request,
			HttpServletResponse response, String attribute) {

		HttpSession session = request.getSession();
		session.removeAttribute(attribute);

		try {
			if (attribute.equals(WebStartConstants.TERMINALS)) {
				SortedArrayList<Terminal> sortedTerminals = new SortedArrayList<Terminal>(getDao(TerminalDao.class).findAll(),"getHostName");
				session.setAttribute(attribute, sortedTerminals);

			} else if (attribute.equals(WebStartConstants.CLIENTS)) {
				SortedArrayList<WebStartClient> sortedClients = new SortedArrayList<WebStartClient>(getDao(WebStartClientDao.class).findAll(),"getIpAddress");
				session.setAttribute(attribute, sortedClients);

			} else if (attribute.equals(WebStartConstants.BUILDS)) {
				SortedArrayList<WebStartBuild> sortedBuilds = new SortedArrayList<WebStartBuild>(getDao(WebStartBuildDao.class).findAll(),"getCreateTimestamp",true);
				session.setAttribute(attribute, sortedBuilds);

			} else if (attribute.equals(WebStartConstants.AVAILABLE_TERMINALS)) {
				session.setAttribute(attribute, getAvailableTerminals());

			} else if (attribute.equals(WebStartConstants.BUILD_ID)) {
				session.setAttribute(attribute, getDefaultBuildId());

			} else if (attribute.equals(WebStartConstants.LINE_ID)) {
				session.setAttribute(attribute, getLineNum());

			} else if (attribute.equals(WebStartConstants.JAR_URL)) {
				session.setAttribute(attribute, jarUrl);
			} else if (attribute.equals(WebStartConstants.CELL_BUILDS)) {

				session.setAttribute(attribute, getDefaultBuilds());
			} else if (attribute.equals(WebStartConstants.CURRENT_CELL_ID)) {
				session.setAttribute(attribute, getCurrentCellName());
			}
		} catch (Exception e) {
			getLogger().error(e, "Failed to reload Session Attribute : " + attribute + " due to " + e.getMessage());
			forwardTo("web_start_error.jsp", request, response);
		}
	}

	/**
	 * turns the HTML form contents into our java object representation of the
	 * client computer
	 * @param isCreate - is it from scratch or for update
	 */
	private WebStartClient createClientFromRequest(HttpServletRequest request, boolean isCreate)
			throws Exception {
		String ipAddress = request.getParameter(WebStartConstants.IP_ADDRESS).trim();
		String hostName = request.getParameter(WebStartConstants.HOST_NAME).trim();
		String assetNumber = (request.getParameter(WebStartConstants.ASSET_NUMBER)==null) ? "" : request.getParameter(WebStartConstants.ASSET_NUMBER).trim();
		String columnLocation = (request.getParameter(WebStartConstants.COLUMN_LOCATION)==null) ? "" : request.getParameter(WebStartConstants.COLUMN_LOCATION).trim();
		String phoneExtension = (request.getParameter(WebStartConstants.PHONE_EXTENSION)==null) ? "" : request.getParameter(WebStartConstants.PHONE_EXTENSION).trim();
		String featureType = null;
		String featureId = null;
		if(null != request.getParameter(WebStartConstants.FEATURETYPE)) 
			featureType = (request.getParameter(WebStartConstants.FEATURETYPE).trim().equals("NONE")) ? null : request.getParameter(WebStartConstants.FEATURETYPE).trim();

		if(null != request.getParameter(WebStartConstants.FEATUREID))
			featureId = (request.getParameter(WebStartConstants.FEATUREID).trim().equals("NONE")) ? null : request.getParameter(WebStartConstants.FEATUREID).trim();

		Integer shutdownFlag; {
			final String shutdownFlagStr = request.getParameter(WebStartConstants.SHUTDOWN_FLAG);
			if (shutdownFlagStr == null) {
				shutdownFlag = -1;
			} else {
				try {
					shutdownFlag = Integer.parseInt(shutdownFlagStr);
				} catch (NumberFormatException nfe) {
					shutdownFlag = Boolean.parseBoolean(shutdownFlagStr) ? 1 : -1;
				}
			}
		}
		String description = null;

		if (isCreate) {
			Terminal aTerminal = getDao(TerminalDao.class).findByKey(hostName);
			description = aTerminal.getTerminalDescription();
		} 

		WebStartClient client = new WebStartClient(ipAddress, hostName, description, 
				request.getParameter(WebStartConstants.BUILD_ID), assetNumber, columnLocation, shutdownFlag, featureType, featureId);
		client.setCreateTimestamp(CommonUtil.getTimestampNow());
		client.setPhoneExtension(phoneExtension);

		return client;
	}

	/**
	 * put our error message into our session and forward to our error page
	 */
	private void handleException(HttpServletRequest request,
			HttpServletResponse response, Throwable t) throws IOException {

		getLogger().error(t, "Exception in ClientDataServlet: " + t.getMessage());

		request.getSession().setAttribute(
				WebStartConstants.ERROR,
				t.getClass() + "<p>" + t.getMessage()
				+ "<p>Please see server logs for more info.");
		forwardTo("web_start_error.jsp", request, response);
	}

	private boolean isBuildInUse(String buildId) {

		try {
			if (buildId.equals(getDefaultBuildId())) return true;
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {			
			List<WebStartClient> clients = getDao(WebStartClientDao.class).findAll();
			for (WebStartClient client : clients) {
				if (client.getBuildId().equals(buildId)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public List<Terminal> getAvailableTerminals() {

		List<Terminal> availableTerminals = new ArrayList<Terminal>();
		try {
			List<Terminal> allTerminals = getDao(TerminalDao.class).findAll();
			List<WebStartClient> allClients = getDao(WebStartClientDao.class).findAll();

			for (Terminal aTerminal : allTerminals) {
				if (!isWebStartClient(aTerminal, allClients))
					availableTerminals.add(aTerminal);				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return availableTerminals;
	}

	public Map<String,WebStartDefaultBuild> getDefaultBuilds() {

		List<WebStartDefaultBuild> builds= getDao(WebStartDefaultBuildDao.class).findAll();
		Map<String,WebStartDefaultBuild> defaultBuilds =new TreeMap<String,WebStartDefaultBuild>();
		for(WebStartDefaultBuild build : builds) {
			defaultBuilds.put(build.getEnvironment(), build);
		}
		return defaultBuilds;
	}

	public boolean isWebStartClient(Terminal aTerminal,	List<WebStartClient> allClients) {

		for(WebStartClient client : allClients) {
			if (client.getHostName().equals(aTerminal.getHostName())) return true;
		}
		return false;

	}

	/*
	 * Return a list of all builds and a build that represents default.
	 */
	private List<WebStartBuild> getAllBuildsWithDefault() {
		List<WebStartBuild> builds;
		WebStartBuild aBuild = new WebStartBuild();
		aBuild.setBuildId(WebStartConstants.DEFAULT_BUILD_ID);
		aBuild.setDescription("This is the default build.");
		aBuild.setCreateTimestamp(CommonUtil.getTimestampNow());
		try {
			builds = getDao(WebStartBuildDao.class).findAll();
		} catch (Exception ex) {
			builds = new ArrayList<WebStartBuild>();
		}
		//create array list to update 
		List<WebStartBuild> buildList = new SortedArrayList<WebStartBuild>(builds,"getCreateTimestamp",true);
		buildList.add(0, aBuild);
		return buildList;
	}



	private List<String> getFeatureTypes(){
		List<FeatureType> featureTypes;
		String aBuild = "NONE";
		List<String> featureTypes1 = new ArrayList<String>();
		try{
			featureTypes = getDao(FeatureTypeDao.class).findAll();
			for(FeatureType feature : featureTypes) {
				featureTypes1.add(feature.getFeatureType());
			}
		}catch(Exception ex){
			featureTypes = new ArrayList<FeatureType>();
		}

		featureTypes1.add(0, aBuild);
		return featureTypes1;
	}

	private List<String> getFeatureIds(String featureType){
		List<Feature> featureIds;
		String aBuild = "NONE";
		List<String> featureIdList = new ArrayList<String>();
		try{
			featureIds= getDao(FeatureDao.class).findAllFeatures(featureType);
			for(Feature feature : featureIds) {
				featureIdList.add(feature.getFeatureId());
			}
		}catch(Exception ex){
			featureIds = new ArrayList<Feature>();
		}
		featureIdList.add(0, aBuild);
		return featureIdList;

	}

	/*
	 * Create a WebStartBuild from incoming request.
	 */
	private WebStartBuild createBuildFromRequest(HttpServletRequest request)
			throws Exception {
		WebStartBuild aBuild = new WebStartBuild();
		aBuild.setBuildId(request.getParameter(WebStartConstants.BUILD_ID));
		aBuild.setDescription(request
				.getParameter(WebStartConstants.BUILD_DESCRIPTION));
		aBuild.setUrl(request.getParameter(WebStartConstants.JAR_URL));
		aBuild.setBuildDate(Date.valueOf(request
				.getParameter(WebStartConstants.BUILD_DATE)));
		aBuild.setDefaultBuild("N");
		aBuild.setCreateTimestamp(CommonUtil.getTimestampNow());
		return aBuild;
	}

	private WebStartDefaultBuild createDefaultBuildFromRequest(HttpServletRequest request){

		return new WebStartDefaultBuild(
				request.getParameter(WebStartConstants.CELL_ID),
				request.getParameter(WebStartConstants.BUILD_ID), 
				request.getParameter(WebStartConstants.DESCRIPTION));

	}

	private String getDefaultBuildId() {

		WebStartDefaultBuild item = getDao(WebStartDefaultBuildDao.class).findByKey(getCurrentCellName());
		return item == null ? null : item.getDefaultBuildId();

	}

	/*
	 * Forward request to a jsp.
	 */
	private void forwardTo(String aJspName, HttpServletRequest request,
			HttpServletResponse response) {

		getLogger().info("Forwarding request to: " + aJspName);
		RequestDispatcher dispatcher = request.getRequestDispatcher(aJspName);
		if (dispatcher != null) {
			try {
				dispatcher.forward(request, response);
			} catch (ServletException e) {
				getLogger().error(e, "Exception in forwardTo "  + e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				getLogger().error(e, "Exception in forwardTo "  + e.getMessage());
				e.printStackTrace();
			}
		} else {
			try {
				response.sendRedirect("web_start_home.jsp");
			} catch (IOException e) {
				getLogger().error(e, "Unable to redirect response to web_start_home.jsp");
			}
		}
	}

	private Logger getLogger() {
		if(logger == null) logger = Logger.getLogger(WEB_START);
		return logger;
	}

	private LogLevel getLogLevel() { 
		return LogLevel.DATABASE;  
	}

	private String getLineNum() {
		return PropertyService.getSiteName() + " " + PropertyService.getAssemblyLineId();
	}
}