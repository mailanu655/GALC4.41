
package com.honda.galc.device.simulator.client.view;

import java.awt.BorderLayout;
import java.util.Properties;


import javax.swing.JSplitPane;
import javax.swing.JPanel;

import com.honda.galc.device.simulator.client.ui.GalcMainPanel;
import com.honda.galc.device.simulator.client.view.cfg.DeviceSimulatorConfigMain;
import com.honda.galc.device.simulator.client.view.cfg.DeviceSimulatorConfigManager;
import com.honda.galc.device.simulator.data.ITagValueSource;
import com.honda.galc.device.simulator.data.SimulatorConstants;
import com.honda.galc.device.simulator.utils.DevSimulatorUtil;

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class SimulatorMainPanel extends GalcMainPanel {

	private static final long serialVersionUID = -8533412296493125610L;
	private JSplitPane mainSplitPane = null;
	private DeviceDataPanel deviceDataPanel = null;
	private DeviceSelectorPanel deviceListTable = null;

	public SimulatorMainPanel() {
		super();
		initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getMainSplitPane(), BorderLayout.CENTER);
		DeviceSimulatorConfigManager cfgMgr = DeviceSimulatorConfigManager.getInstance();
		
		DevSimulatorUtil util = new DevSimulatorUtil();
		Properties props = util.loadProperties(SimulatorConstants.PROP_SIMULATOR);
		String exclHosts = (String)props.get("ExcludesDispatcherHosts");
		cfgMgr.getConfig().setExcludeHosts((exclHosts == null) ? null : exclHosts.split(","));
		
		String exclIpMasks = (String)props.get("ExcludesIpMask");
		cfgMgr.getConfig().setExclIpMasks((exclIpMasks == null) ? null : exclIpMasks.split(","));
		
		//String opcInstanceName = (String)props.get("OPCInstanceName");
		String opcInstanceName = this.getController().getProperty("OPC Instance Name");

		//set as true for now. assume the instance name is valid
		cfgMgr.getConfig().setUseOPCServerClientCfg(true); 
		cfgMgr.getConfig().setOpcInstanceName(opcInstanceName);
		cfgMgr.getConfig().setSenderShowTag(true);
		cfgMgr.getConfig().setReplyShowTag(false);
	}

	/**
	 * This method initializes mainSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getMainSplitPane() {
		if (mainSplitPane == null) {
			mainSplitPane = new JSplitPane();
			mainSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			mainSplitPane.setDividerLocation(500);
			mainSplitPane.setLeftComponent(getDeviceListTable());
			mainSplitPane.setRightComponent(getDeviceDataPanel());			
            deviceListTable.addDeviceSelectListener(deviceDataPanel); 
            deviceDataPanel.setDeviceSelector(deviceListTable); 
            
            deviceListTable.addDeviceSelectListener(deviceDataPanel.getSimulatorServerPanel());
		}
		return mainSplitPane;
	}
	
	/**
	 * This method initializes deviceListPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JPanel getDeviceDataPanel() {
		if (deviceDataPanel == null) {
			deviceDataPanel = new DeviceDataPanel();
		}
		return deviceDataPanel;
	}

	/**
	 * This method initializes deviceListTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	public DeviceSelectorPanel getDeviceListTable() {
		if (deviceListTable == null) {
			deviceListTable = new DeviceSelectorPanel();            
		}
		return deviceListTable;
	}
	
	public void setTagValueSource(ITagValueSource tagValueSource)
	{
		deviceDataPanel.setTagValueSource(tagValueSource);
	}

	public void configuration()
	{
		java.awt.Container parentScreen = this.getRootPane().getParent();
		DeviceSimulatorConfigMain cfg = new DeviceSimulatorConfigMain();
		cfg.setLocationRelativeTo(parentScreen);
		cfg.setVisible(true);
	}
}
