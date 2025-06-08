package com.honda.galc.device.simulator.client.view.cfg;

import com.honda.galc.device.simulator.client.ui.GalcWindow;
import com.honda.galc.device.simulator.client.view.data.ConfigValueObject;

/**
 * <h3>DeviceSimulatorConfigManager</h3>
 * <h4> </h4>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>P.Chou</TD>
 * <TD>Sep. 23, 2008</TD>
 * <TD>Version 0.2</TD>
 * <TD></TD>
 * <TD>Initial version.</TD>
 * </TR>
 * </TABLE>
 */

public class DeviceSimulatorConfigManager {
	private static volatile DeviceSimulatorConfigManager INSTANCE;
	GalcWindow window;
	private ConfigValueObject config = new ConfigValueObject();
	String instanceName;
	protected DeviceSimulatorConfigManager() {
	}

	public static DeviceSimulatorConfigManager getInstance() {
		synchronized(DeviceSimulatorConfigManager.class) {
			if (INSTANCE == null)
				INSTANCE = new DeviceSimulatorConfigManager();
		}
		return INSTANCE;
	}

	/**
	 * @return the config
	 */
	public ConfigValueObject getConfig() {
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(ConfigValueObject config) {
		this.config = config;
	}

	/**
	 * @return the window
	 */
	public GalcWindow getWindow() {
		return window;
	}

	/**
	 * @param window the window to set
	 */
	public void setWindow(GalcWindow window) {
		this.window = window;
	}

}
