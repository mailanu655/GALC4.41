package com.honda.galc.client.audio;

import java.util.List;
import purejavahidapi.HidDevice;
import purejavahidapi.HidDeviceInfo;
import purejavahidapi.PureJavaHidApi;
import com.honda.galc.common.logging.Logger;

/**
 * <h3>LightAlarmManager</h3>
 * <h4>
 * Delcom USB light alarm action.
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
 * <TD>Bernard Leong</TD>
 * <TD>Sep 21, 2017</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Bernard Leong
 */
public class LightAlarmManager {
	private static final short VENDOR_ID = 0x0FC5;  // Delcom Vendor ID
	private static final short PRODUCT_ID = -20352; // Delcom USB Product ID 
	
	protected static volatile LightAlarmManager INSTANCE = null;
	protected static HidDeviceInfo devInfo = null;
	
	public LightAlarmManager() {}
	
	public static LightAlarmManager getInstance() {
		synchronized (LightAlarmManager.class) {
			if (INSTANCE == null)
				INSTANCE = new LightAlarmManager();
		}
		return INSTANCE;
	}

	public static boolean isExist() {
		return INSTANCE != null;
	}
	
	public void raiseAlarm(boolean blink) {
		setAlarm(blink);
	}

	private static HidDevice openDevice() {
		try {
			if (devInfo == null)
				devInfo = findDeviceInfo();
			if (devInfo == null) return null;
			return PureJavaHidApi.openDevice(devInfo);
		} catch (Exception e) {
			Logger.getLogger().warn(e, " Failed to open light alarm device. ");
		}
		return null;
	}

	private static HidDeviceInfo findDeviceInfo() {
		List<HidDeviceInfo> devList = PureJavaHidApi.enumerateDevices();
		for (HidDeviceInfo info : devList) {
			if (info.getVendorId() == VENDOR_ID
					&& info.getProductId() == PRODUCT_ID)
				return info;
		}
		Logger.getLogger().warn("No light alarm device found.");
		return null;
	}

	private static void setAlarm(boolean blink) {
		HidDevice device = openDevice();
		if (device == null)
			return;
		try {
			if (blink)
				setBlinkOn(device);
			else
				setBlinkOff(device);
		} catch (Exception e) {
			Logger.getLogger().warn(e, " Failed to write to light alarm device. ");
		} finally {
			try {
				Thread.sleep(100);
				device.close();
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * Write commands to turn on and blink the light alarm
	 * 
	 * @param device
	 */
	private static void setBlinkOn(HidDevice device) {
		byte[] data = new byte[8];
		data[0] = 101;
		data[1] = 12;
		data[2] = 2;
		data[3] = 0;
		device.setFeatureReport(data, 8);
		data[0] = 101;
		data[1] = 20;
		data[2] = 0;
		data[3] = 2;
		device.setFeatureReport(data, 8);
	}
	
	/**
	 * Write commands to turn off light alarm
	 * 
	 * @param device
	 */
	private static void setBlinkOff(HidDevice device) {	
		byte[] data = new byte[8];
		data[0] = 101;
		data[1] = 20;
		data[2] = 2;
		data[3] = 0;
		device.setFeatureReport(data, 8);
		data[0] = 101;
		data[1] = 12;
		data[2] = 0;
		data[3] = 2;
		device.setFeatureReport(data, 8);
	}
}
