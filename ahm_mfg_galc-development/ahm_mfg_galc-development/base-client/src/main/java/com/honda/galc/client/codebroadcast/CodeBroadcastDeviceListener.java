package com.honda.galc.client.codebroadcast;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.ErrorData;
import com.honda.galc.device.dataformat.ErrorDataIdentifier;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.device.dataformat.ProductIdIdentifier;

public class CodeBroadcastDeviceListener implements DeviceListener {

	private volatile static CodeBroadcastDeviceListener _instance;

	private List<CodeBroadcastController> _controllers;
	private CodeBroadcastDeviceListener() {
		super();
		register(getDeviceData());
	}

	public static CodeBroadcastDeviceListener getInstance() {
		if(_instance == null) {
			_instance = new CodeBroadcastDeviceListener();
		}
		return _instance;
	}

	public void registerController(CodeBroadcastController controller) {
		if (_controllers == null) {
			_controllers = new ArrayList<CodeBroadcastController>();
		}
		if (!_controllers.contains(controller)) {
			_controllers.add(controller);
		}
	}

	private void register(List<IDeviceData> dataList) {
		IDevice eiDevice = DeviceManager.getInstance().getDevice(EiDevice.NAME);
		if(eiDevice != null && eiDevice.isEnabled()){
			((EiDevice)eiDevice).registerDeviceListener(this, dataList);
		}
	}

	private List<IDeviceData> getDeviceData() {
		List<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new ErrorData());
		list.add(new ErrorDataIdentifier());
		list.add(new ProductId());
		list.add(new ProductIdIdentifier());
		return list;
	}

	public IDeviceData received(String clientId, IDeviceData deviceData) {
		if (_controllers == null) {
			Logger.getLogger().error(getId() + " has no registered controllers");
		}
		Logger.getLogger().info("Receive " + getReceivedLogString(deviceData) + " from device:" + clientId);
		for (CodeBroadcastController controller : _controllers) {
			controller.received(deviceData);
		}
		return deviceData;
	}

	private String getReceivedLogString(IDeviceData deviceData) {
		StringBuilder sb = new StringBuilder();
		sb.append(deviceData.getClass().getSimpleName()).append(":");

		if (deviceData instanceof ErrorData) {
			sb.append(deviceData.toString());
		} else if (deviceData instanceof ProductId) {
			sb.append(deviceData.toString());
		} else {
			sb.append("unknown");
		}
		return sb.toString();
	}

	public String getId() {
		return this.getClass().getSimpleName();
	}

	public String getApplicationName() {
		return this.getClass().getSimpleName();
	}

	public Integer getDeviceAccessKey(String deviceId) {
		return null;
	}
}
