package com.honda.galc.client;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.WebStartClientDao;
import com.honda.galc.entity.conf.WebStartClient;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.GalcStation;
import com.honda.galc.util.TimeTicks;

/**
 * @author Subu Kathiresan
 * @date Sep 25, 2012
 *
 */
public class ClientMonitor extends Thread {

	public static final String HEARTBEAT_ENABLED_PROP_KEY = "HEARTBEAT_ENABLED";

	public static final String HEARTBEAT_INTERVAL_PROP_KEY = "HEARTBEAT_INTERVAL";

	public static final String DEFAULT_COMMONPROPERTIES_KEY = "Default_CommonProperties";

	private static final String LOG_NAME = "ClientMonitor";

	private volatile boolean _shutDownInProgress = false;

	private volatile ScheduledFuture<?> _shutDownProcHandle; 

	private volatile boolean _active = false;

	private volatile static ClientMonitor _instance;

	public static ClientMonitor getInstance() {
		if (_instance == null) {
			_instance = new ClientMonitor();
		}
		return _instance;
	}

	private ClientMonitor() {}

	/**
	 * starts the notification thread which updates WSCLIENTS_TBX to indicate
	 * client is active
	 */
	public void run() {
		init();
		final long interval = getHeartbeatInterval();
		while (isActive()) {
			try {
				WebStartClient client = getWebStartClient();
				if (client != null) {
					handleShutDownRequest(client);
					sendHeartBeat(interval, client);
				} else {
					getLogger().warn(String.format("Client information not found. Could not update heartbeat for Asset %s (%s)", 
							getAssetName(), getAppContext().getTerminalId())); 
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				getLogger().error(ex, String.format("Could not notify client active status for Asset %s: %s", getAssetName(), 
						(ex.getMessage() != null ? ex.getMessage() : "")));
			}
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * sends a heart beat to the server, if heart beat is
	 * enabled for the client
	 * 
	 * @param interval
	 * @param client
	 */
	private void sendHeartBeat(long interval, WebStartClient client) {
		if (isHeartBeatEnabledForTerminal()) {
			Date now = getWebStartClientDao().getDatabaseTimeStamp();
			logClientOfflineWarning(client, now, interval);
			client.setHeartBeatTimestamp(now);
			updateWebStartClient(client);
		}
	}

	/**
	 * initializes client monitor
	 */
	private void init() {
		activate();
		registerClientStartUp();
	}

	/**
	 * update webStartClient in a thread safe method
	 * 
	 * @param client
	 */
	private synchronized void updateWebStartClient(WebStartClient client) {
		getWebStartClientDao().update(client);
	}

	/**
	 * registers client start up
	 * 
	 * @return
	 */
	private void registerClientStartUp() {
		try {
			WebStartClient client = getWebStartClient();	
			if (client != null) {
				client.setShutdownFlag(client.isShutdownFlagOn() ? 0 : -1); // reset SHUTDOWN_FLAG indicating successful start
				updateWebStartClient(client);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, String.format("Could not register client startUp: %s", (ex.getMessage() != null ? ex.getMessage() : "")));
		}
	}

	/**
	 * checks if shutdown should be initiated/canceled for the client
	 * if SHUTDOWN_FLAG > 0 initiate shutdown
	 * if shutdown is in progress and the value is less than 0, cancel shutdown
	 * 
	 * @param client
	 */
	private void handleShutDownRequest(WebStartClient client) {
		if (isShutDownRequested(client)) {
			initiateShutDown(client);
		} else {
			if (isShutDownInProgress())
				cancelShutDown(client);
		}
	}

	/**
	 * check if shutdown was requested for this client
	 * 
	 * @param client
	 * @return
	 */
	private boolean isShutDownRequested(WebStartClient client) {
		return client.isShutdownRequested();
	}

	/**
	 * waits for the shutdownFlag time to expire and 
	 * initiates 'cold' shutdown of the application.
	 * 
	 * @param webStartClient
	 */
	public void initiateShutDown(WebStartClient webStartClient) {
		if (isShutDownInProgress())		// if shutdown is already in progress, do nothing
			return;

		final WebStartClient client = webStartClient;
		try {
			final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
			getLogger().warn(String.format("Asset %s (%s) will shutdown in %d seconds", getAssetName(), 
					getLocation(client), webStartClient.getShutdownFlag()));

			final Runnable shutDownClient = new Runnable() {
				public void run() {
					getLogger().warn(String.format("Shutting down Asset %s (%s)", getAssetName(), getLocation(client)));
					ClientMainFx.getInstance().exitApplication(0);
				}
			};
			setShutDownProcHandle(scheduler.schedule(shutDownClient, webStartClient.getShutdownFlag(), TimeUnit.SECONDS));
			setShutDownInProgress(true); 
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error(String.format("Could not schedule shutdown for Asset %s (%s): %s", getAssetName(), getLocation(client), 
					(ex.getMessage() != null ? ex.getMessage() : "")));
		}
	}

	/**
	 * cancels the process to shutdown the client
	 * 
	 * @param client
	 */
	private void cancelShutDown(WebStartClient client) {
		try {
			getLogger().warn(String.format("Attempting to cancel shutdown process for Asset %s (%s)", getAssetName(), getLocation(client)));
			if (getShutDownProcHandle() != null) {
				getShutDownProcHandle().cancel(true);
				setShutDownInProgress(false);
				setShutDownProcHandle(null);
			}
			getLogger().warn(String.format("Cancelled shutdown process for Asset %s (%s)", getAssetName(), getLocation(client)));
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error(String.format("Failed to cancel shutdown process for Asset %s (%s)", getAssetName(), getLocation(client)));
		}
	}

	/**
	 * returns the location of the client
	 * 
	 * @param client
	 * @return
	 */
	private String getLocation(WebStartClient client) {
		String hostName = StringUtils.stripToEmpty(client.getHostName());
		String columnLocation = StringUtils.stripToEmpty(client.getColumnLocation());
		if (!hostName.equals("") && !columnLocation.equals("")) {
			hostName += " @ "; 
		}

		String location = hostName + columnLocation;
		return location;
	}

	/**
	 * returns a valid WebStartClient object representing
	 * current headless/headed terminal 
	 * 
	 * @return
	 */
	public WebStartClient getWebStartClient() {
		WebStartClient client = null;
		String hostName = getAssetName();
		try {
			if (isHeadless()) {
				// Headless terminals will need to be looked up by terminal ID (instead of asset name)
				// and assetName should be used as column location
				client = getWebStartClientDao().findByKey(getAppContext().getTerminalId());

				// use the first 20 characters of the asset name if its longer than 20 chars - column max length is 20
				client.setColumnLocation(hostName.substring(0, Math.min(hostName.length(), 20)));	
			} else {
				client = getWebStartClientDao().findByKey(hostName);
				if(client == null) {
					client = getWebStartClientDao().findByKey(getLocalIpAddress());
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, String.format("Unable to retrieve Webstart Client information for %s/%s ", 
					(getAppContext() != null ? StringUtils.trimToEmpty(getAppContext().getTerminalId()) : ""), hostName));
		}

		return client;
	}

	/**
	 * returns the client monitor logger
	 * 
	 * @return
	 */
	private Logger getLogger() {
		Logger logger = Logger.getLogger(LOG_NAME);
		logger.getLogContext().setApplicationInfoNeeded(true);
		return logger;
	}

	/**
	 * log client off-line warning if the client has been dormant
	 * 
	 * @param assetName
	 * @param client
	 * @param now
	 * @param interval
	 */
	public void logClientOfflineWarning(WebStartClient client, Date now, long interval) {
		if (client.getHeartBeatTimestamp() != null){
			long timeDiff = now.getTime() - client.getHeartBeatTimestamp().getTime();
			if (timeDiff > interval * 3) {			// threshold is always 3 times the heart beat interval
				getLogger().warn(String.format("Asset %s (%s) has been dormant/offline for %s", getAssetName(), 
						getLocation(client), new TimeTicks(timeDiff).toString()));
			}
		}
	}

	/**
	 * returns the heartbeat interval
	 * @return
	 */
	private long getHeartbeatInterval() {
		long interval = 60000;		// default interval, will try to override with what's specified in Default_CommonProperties

		try {
			interval = Long.parseLong(PropertyService.getProperty(DEFAULT_COMMONPROPERTIES_KEY, HEARTBEAT_INTERVAL_PROP_KEY));
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, String.format("Unable to parse property %s, using default value: 60000", HEARTBEAT_INTERVAL_PROP_KEY));
		}

		return interval;
	}

	private WebStartClientDao getWebStartClientDao() {
		return ServiceFactory.getDao(WebStartClientDao.class);
	}

	private ApplicationContext getAppContext() {
		return ClientMainFx.getInstance().getApplicationContext();
	}

	public boolean isHeartBeatEnabledForTerminal() {
		return Boolean.parseBoolean(PropertyService.getProperty(getAppContext().getTerminalId(), HEARTBEAT_ENABLED_PROP_KEY));
	}

	private boolean isHeadless() {
		return ClientMainFx.getInstance().getApplicationContext().getArguments().isHeadless();
	}

	private String getAssetName() {
		return GalcStation.getHostName(getAppContext().getTerminalId(), LOG_NAME);
	}
	
	private String getLocalIpAddress() {
		return GalcStation.getLocalIpAddress(getAppContext().getTerminalId(), LOG_NAME);
	}

	private void setShutDownInProgress(boolean shutDownInProgress) {
		_shutDownInProgress = shutDownInProgress;
	}

	private boolean isShutDownInProgress() {
		return _shutDownInProgress;
	}

	private void setShutDownProcHandle(ScheduledFuture<?> shutDownProcHandle) {
		_shutDownProcHandle = shutDownProcHandle;
	}

	private ScheduledFuture<?> getShutDownProcHandle() {
		return _shutDownProcHandle;
	}

	public void deActivate() {
		_active = false;
	}

	private void activate() {
		_active = true;
	}

	private boolean isActive() {
		return _active;
	}
}
