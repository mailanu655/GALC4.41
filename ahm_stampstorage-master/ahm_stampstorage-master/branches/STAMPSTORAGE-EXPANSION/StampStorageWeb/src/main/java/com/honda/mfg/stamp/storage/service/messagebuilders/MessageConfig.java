package com.honda.mfg.stamp.storage.service.messagebuilders;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
//        deviceMessageMap.put(DeviceMessageType.STORAGE_STATE_RESPONSE.toString(), StorageStateRefreshRequestMessage.class.getName());
//        deviceMessageMap.put(DeviceMessageType.STOP_INFO_REQUEST.toString(), StopInfoMessage.class.getName());
//2013-02-01:VB:        deviceMessageMap.put(DeviceMessageType.CARRIER_STATUS.toString(), CarrierStatusMessage.class.getName());
//        deviceMessageMap.put(DeviceMessageType.CARRIER_MOVE_ERROR.toString(), ErrorMessage.class.getName());
//        deviceMessageMap.put(DeviceMessageType.STORAGE_STATE_VERIFY.toString(), StorageStateRefreshRequestMessage.class.getName());

	}

	public Set<String> getDeviceMessageTypes() {
		return deviceMessageMap.keySet();
	}

	public String getDeviceClass(String msgType) {
		return deviceMessageMap.get(msgType);
	}
}
