package com.honda.galc.client.datacollection.view.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.LotControlConstants;
import com.honda.galc.client.datacollection.view.ErrorDialogManager;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.lotcontrol.ITorqueDevice;
import com.honda.galc.client.device.lotcontrol.TorqueSocketDevice;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.IDevice;
import com.honda.galc.net.Request;

/**
 * 
 * <h3>TestTorqueButtonAction</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * TestTorqueButtonAction description
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 * 
 * @author Ambica Gawarla Apr 08, 2016
 *
 */
public class TestTorqueButtonAction extends BaseDataCollectionAction {

	private static final long serialVersionUID = 1L;

	public TestTorqueButtonAction(ClientContext context, String name) {
		super(context, name);
	}

	public TestTorqueButtonAction(ClientContext context, String name, boolean fromPopup) {
		super(context, name);
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		String message = "WARNING: WHILE THIS SCREEN IS OPEN THE TOOL(S) WILL BE ENABLED BUT WILL NOT COLLECT DATA";
		String request = "cancel";
		for (IDevice device : DeviceManager.getInstance().getDevices().values()) {
			if (device instanceof ITorqueDevice) {
				TorqueSocketDevice torqueDevice = (TorqueSocketDevice) device;
				Logger.getLogger().info("enabling device - " + torqueDevice.getHostName());
				torqueDevice.enable(context.getProperty().getTestTorquePset()); //WED 20170629 set default test torque Pset on controller
			}
		}
		if (context.getProperty().isShowErrorDialog()) {
			ErrorDialogManager mgr = new ErrorDialogManager();
			request = mgr.showDialog(context.getFrame(),  message, LotControlConstants.TEST_TORQUE,
					context.getProperty());
			disableDevices();
		}else{
			int response = JOptionPane.showOptionDialog(context.getFrame(), message, "Pre Shift Tool Test",
					JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[] { "CLOSE" }, null);
	
			if (response == JOptionPane.OK_OPTION || response == JOptionPane.CLOSED_OPTION) {
				disableDevices();
			}
			
		}
			logInfo();
			runInSeparateThread(new Request(request));
		
	}
	
	private void disableDevices(){
		for (IDevice device : DeviceManager.getInstance().getDevices().values()) {
			if (device instanceof ITorqueDevice) {
				TorqueSocketDevice torqueDevice = (TorqueSocketDevice) device;
				Logger.getLogger().info("disabling device - " + torqueDevice.getHostName());
				torqueDevice.disable();
			}
		}
	}
	
}