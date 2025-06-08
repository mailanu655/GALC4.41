package com.honda.galc.test.device;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.Arguments;
import com.honda.galc.client.CachedApplicationContext;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.TorqueData;
import com.honda.galc.net.SocketRequestDispatcher;

public class DeviceTest implements DeviceListener {

	
	public DeviceTest() {

	    ApplicationContextProvider.loadFromClassPathXml("application.xml");

		start();
	}
	
	public void start() {
		String[] args = {"logs", "http://localhost:9080/BaseWeb/HttpServiceHandler", "AE0EN12501"};
		ApplicationContext context = CachedApplicationContext.create(Arguments.create(Arrays.asList(args)));

		SocketRequestDispatcher dispatcher = new SocketRequestDispatcher(3030, context);
		try {
			dispatcher.accept();
			dispatcher.registerListner(this, this.createDeviceDataList());
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private List<IDeviceData> createDeviceDataList() {
		
		List<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new TorqueData());
		return list;
	}



	public IDeviceData received(String clientId, IDeviceData deviceData) {
		TorqueData data = (TorqueData) deviceData;
		if(data != null && data.isTighteningStatus()) return DataCollectionComplete.OK();
		else return DataCollectionComplete.NG();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new DeviceTest();

	}

}
