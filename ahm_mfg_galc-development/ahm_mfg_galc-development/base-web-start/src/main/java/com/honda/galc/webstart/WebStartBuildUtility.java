/**
 * 
 */
package com.honda.galc.webstart;

import static com.honda.galc.service.ServiceFactory.getDao;

import com.honda.galc.buildlevel.BuildLevelUtility;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.WebStartBuildDao;
import com.honda.galc.dao.conf.WebStartDefaultBuildDao;
import com.honda.galc.entity.conf.WebStartBuild;
import com.honda.galc.entity.conf.WebStartDefaultBuild;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonUtil;

/**
 * @author Subu Kathiresan
 * @date Sep 25, 2012
 *
 */
public class WebStartBuildUtility {
	
	private static final String BUILD_DESCRIPTION = "Created by WebStartBuildUtility";
	
	private static final String BUILD_SEPERATOR = "___";
	
	private static final String DISPATCHER_URL_PATTERN = "BaseWeb/HttpServiceHandler";
	
	private static final String DISPATCHER_URL_REPLACEMENT = "client/";
	
	private static final String LOGGER_NAME = "WebStartBuildUtility";
	
	private static Logger _logger;
	
	private static String _buildNumber;
	
	private static String _buildName;
	
	static {
		try {
			_buildName = (getBuildComment().split(BUILD_SEPERATOR)[0]).trim();
			_buildNumber = (getBuildComment().split(BUILD_SEPERATOR)[1]).trim();
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Could not parse Build information from BuildLevelUtility.properties file: " + getBuildComment());
		}
	}
		
	/**
	 * sets up build, default build for the provided cell if not already set up
	 */
	public static void setupBuild(String cellName, WsClientConfig wsClientConfig) {

		// create new build if needed
		if (getBuildDao().findByKey(getBuildNumber()) == null) {		
			try {
				String dispatcherUrl = getProperty(wsClientConfig.getTerminalTypeName() + "_DISPATCHER_URL"); 
				createNewBuild(dispatcherUrl);
			} catch(Exception ex) {
				ex.printStackTrace();
				getLogger().error("Could not create new build for cell: " + cellName);
			}
		}
		
		// set up build as the default for current cell
		try {		
			setDefaultBuild(cellName);
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error("Could not set up default build for cell: " + cellName);
		}
	}

	/**
	 * creates a new build 
	 */
	private static void createNewBuild(String dispatcherUrl) {
		WebStartBuild build = new WebStartBuild();
		build.setBuildId(getBuildNumber());
		build.setDescription(BUILD_DESCRIPTION);
		build.setUrl(getBuildUrl(dispatcherUrl));
		build.setBuildDate(new java.sql.Date(new java.util.Date().getTime()));
		build.setDefaultBuild("N");
		build.setCreateTimestamp(CommonUtil.getTimestampNow());
		getLogger().info("Creating " + getBuildNumber());
		getBuildDao().insert(build);
	}

	/**
	 * returns the build url based on the dispatcher url
	 * 
	 * @return
	 */
	private static String getBuildUrl(String dispatcherUrl) {
		return dispatcherUrl.replace(DISPATCHER_URL_PATTERN, DISPATCHER_URL_REPLACEMENT + getBuildNumber());
	}
	
	/**
	 * set up default build for current cell
	 * 
	 * @param cellName
	 */
	private static void setDefaultBuild(String cellName) {
		WebStartDefaultBuild defBuild = getDefaultBuild(cellName);
		if (defBuild == null) {
			getLogger().warn("Cell name not found. Creating new WebStartDefaultBuild cell: " + cellName);
			defBuild = new WebStartDefaultBuild(cellName, getBuildNumber(), getBuildName());
			getDao(WebStartDefaultBuildDao.class).save(defBuild);
		}
		
		// and set up default build for the provided cell, if needed
		if (!(defBuild.getDefaultBuildId().equals(getBuildNumber()))) {
			getLogger().info("Setting " + getBuildNumber() + " as the default for cell: " + cellName);
			defBuild.setDefaultBuildId(getBuildNumber());
			getDao(WebStartDefaultBuildDao.class).save(defBuild);
		}
	}
	
	/**
	 * get default build for provided cell
	 * 
	 * @param cellName
	 * @return
	 */
	private static WebStartDefaultBuild getDefaultBuild(String cellName) {
		return getDao(WebStartDefaultBuildDao.class).findByKey(cellName);
	}
	
	private static WebStartBuildDao getBuildDao() {
		return getDao(WebStartBuildDao.class);
	}
	
	private static String getProperty(String propertyKey) {
		return PropertyService.getProperty("ClientLoaderServlet", propertyKey);
	}
	
	private static Logger getLogger() {
		if(_logger == null) 
			_logger = Logger.getLogger(LOGGER_NAME);
		return _logger;
	}
	
	public static String getBuildComment() {
		try {
			return BuildLevelUtility.getBuildComment().trim();
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}
	
	public static String getBuildName() {
		return _buildName;
	}
	
	public static String getBuildNumber() {
		return _buildNumber;
	}
}