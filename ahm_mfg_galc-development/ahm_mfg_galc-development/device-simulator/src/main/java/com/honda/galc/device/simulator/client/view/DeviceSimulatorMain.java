package com.honda.galc.device.simulator.client.view;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.device.simulator.client.ui.GalcWindow;
import com.honda.galc.device.simulator.client.view.cfg.DeviceSimulatorConfigManager;
import com.honda.galc.device.simulator.client.view.cfg.SimulatorConfig;
import com.honda.galc.device.simulator.client.view.data.PropertyFileTagValueSource;
import com.honda.galc.net.HttpServiceProvider;


public class DeviceSimulatorMain {
	GalcWindow window;

	public static void main(String[] args) {
				
		if(args.length > 0) SimulatorConfig.setEnvArgument(args[0]);
		
		if (SimulatorConfig.getInstance().getHostName() == null) {
			System.out.printf("DeviceSimulator : Check your command line arguments, the environment name provided (%s) is not defined in the property file (%s)\n",args[0],SimulatorConfig.getFilePath());
			System.exit(0);
		}

		// Launch Main Window		
		DeviceSimulatorMain main = new DeviceSimulatorMain();
		loadApplicationConfig();
		
		main.initialize();		
	}
	
	private void initialize()
	{
		SimulatorMainPanel mainPanel = new SimulatorMainPanel();
		
		
		// Set tag value source
		mainPanel.setTagValueSource(new PropertyFileTagValueSource());
		
		window = new GalcWindow(mainPanel);
		String env = StringUtils.isEmpty(SimulatorConfig.hostName) ? SimulatorConfig.getServerName() + " - " : "";
		window.setTitle(env + SimulatorConfig.getInstance().getDeviceURL());
		DeviceSimulatorConfigManager.getInstance().setWindow(window);
		window.showWindow();
	}
	
	private static void loadApplicationConfig() {
		HttpServiceProvider.setUrl(SimulatorConfig.getInstance().getServiceURL());
		ApplicationContextProvider.loadFromClassPathXml("application.xml");
	}
}
