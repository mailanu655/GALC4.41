package com.honda.mfg.stamp.conveyor.processor.messagebuilders;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.honda.mfg.stamp.conveyor.messages.CarrierStatusMessage;
import com.honda.mfg.stamp.conveyor.messages.DeviceMessageType;

/**
 * User: vcc30690 Date: 5/9/11
 */
public class MessageConfig {

	private static Map<String, String> deviceMessageMap;
	private static MessageConfig messageConfig = null;

	public static MessageConfig getInstance() {
		if (messageConfig == null) {
			messageConfig = new MessageConfig();
			init();
		}
		return messageConfig;
	}

	private static void init() {
		deviceMessageMap = new HashMap<String, String>();
		deviceMessageMap.put(DeviceMessageType.CARRIER_STATUS.toString(), CarrierStatusMessage.class.getName());
	}

	public Set<String> getDeviceMessageTypes() {
		return deviceMessageMap.keySet();
	}

	public String getDeviceClass(String msgType) {
		return deviceMessageMap.get(msgType);
	}
}
