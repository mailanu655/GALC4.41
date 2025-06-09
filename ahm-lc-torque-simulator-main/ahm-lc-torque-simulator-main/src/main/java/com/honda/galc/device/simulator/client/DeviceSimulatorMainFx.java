package com.honda.galc.device.simulator.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.device.simulator.torque.TorqueSimulator;
import com.honda.galc.device.simulator.utils.Environment;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.property.PropertyService;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author Subu Kathiresan
 * @date Jan 6, 2015
 */
public class DeviceSimulatorMainFx extends Application implements EventHandler<WindowEvent> {

	private ArrayList<IDeviceSimulator> simulators = new ArrayList<IDeviceSimulator>();
	
	private static String terminalName = "LOT_CONTROL_SIM_2";
	private Stage stage = null;
	private BorderPane borderPane = null;
	private TabPane tabPane = null;
	
	public static void main (String args[] ) {
		launch(args);
	}
		
	public void start(Stage stage) throws Exception {
		this.setStage(stage);
		initialize();
		borderPane = new BorderPane();
		tabPane = new TabPane();
		borderPane.setLeft(tabPane);
		startDeviceSimulators(borderPane);
		
		Scene scene = new Scene(borderPane);
		stage.setScene(scene);
		stage.show();
	}
	
	@Override
	public void stop(){
	    System.out.println("Stage is closing");
	    for (IDeviceSimulator simulator: simulators) {
	    	simulator.stop();
	    }
	    System.exit(0);
	}
	
	private void initialize() {
		ApplicationContextProvider.loadFromClassPathXml("application.xml");
		final Parameters params = getParameters();
        final List<String> parameters = params.getRaw();
        String url = getEnvironment(parameters.get(0));
        HttpServiceProvider.setUrl(url);
        setTerminalName(parameters.get(1));
		System.out.println("Server URL : " + url);	
		System.out.println("Terminal   : " + getTerminalName());	
	}
	
	private void startDeviceSimulators(BorderPane borderPane) {
		
		TorqueSimulator torqueSim = new TorqueSimulator();
		addSimulator(torqueSim);	// Torque simulator
		//addSimulator(new PlcSimulator());		// PLC simulator
	}

	public void addSimulator(IDeviceSimulator iDeviceSimulator) {
		tabPane.getTabs().add(iDeviceSimulator.getDeviceSimTab());
		simulators.add(iDeviceSimulator);
	}
	
	public void handle(WindowEvent windowEvent) {
		if (windowEvent.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
			if (isExitConfirmed()) {
				System.exit(0);
			} else {
				windowEvent.consume();
			}
		}
	}

	public boolean isExitConfirmed() {
		return MessageDialog.confirm(null, "Are you sure that you want to exit?");
	}
	
	public static List<ComponentProperty> getProperties(String componentId, String regex) {
		return PropertyService.getProperties(componentId, regex);
	}

	
	public String getEnvironment(String environment) {
		try {
			environment = Enum.valueOf(Environment.class, environment).getUrl();
		} catch (Exception ex) {
			System.out.println("Environment is not specified, using url " + environment);
		}
		return environment;
	}
	
	public static void setTerminalName(String terminal) {
		terminal = StringUtils.trimToNull(terminal);
		if (terminal != null) {
			terminalName = terminal;
		}
	}
	
	public static String getTerminalName() {
		return terminalName;
	}
	
	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
