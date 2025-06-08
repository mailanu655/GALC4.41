package com.honda.test.util;

import static com.honda.galc.service.ServiceFactory.getDao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.Socket;
import java.util.Map;

import org.h2.tools.Server;

import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.gts.GtsIndicatorDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.gts.GtsIndicator;
import com.honda.galc.entity.gts.GtsIndicatorId;

public class TestUtils {
	
	public static DataContainer prepareGtsIndicatorDataContainer(String area,String indicator, String value) {
		Device device = getDao(DeviceDao.class).findByKey(area + "." + indicator);
		assertNotNull(device);
		
		DeviceFormat df  = device.getDeviceFormat(indicator);
		assertNotNull(df);
	    
		DataContainer dc = new DefaultDataContainer();
		dc.setClientID(area + "." + indicator);
		
		dc.put(df.getTagValue(), value);
		
		return dc;
	}
	
	public static Device prepareDevice(String deviceId,DataContainer data) {
		Device device = getDao(DeviceDao.class).findByKey(deviceId);
		assertNotNull(device);
		assertTrue(device.getDeviceDataFormats().size() > 0);
		
		for(DeviceFormat format : device.getDeviceDataFormats()) {
			if(data.containsKey(format.getTag())) {
				format.setValue(data.get(format.getTag()));
			}
		}
		
		return device;
		
	}
	
	public static DataContainer prepareDataContainer(String deviceId,DataContainer data) {
		Device device = getDao(DeviceDao.class).findByKey(deviceId);
		
		assertNotNull(device);
		
		DataContainer dc = new DefaultDataContainer();
		dc.setClientID(deviceId);
		
		
		for(Map.Entry<Object, Object> entry: data.entrySet()) {
			DeviceFormat df = device.getDeviceFormat((String)entry.getKey());
			
			assertNotNull(df);
		    
			dc.put(df.getTagValue(), data.get(entry.getKey()));
		}
		
		return dc;
		
	}
	
	public static void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void assertIndicator(String area,String indicatorName,int value) {
		GtsIndicatorId id = new GtsIndicatorId();
		id.setTrackingArea(area);
		id.setIndicatorName(indicatorName);
		GtsIndicator indicator= getDao(GtsIndicatorDao.class).findByKey(id);
		
		assertNotNull(indicator);
		assertEquals(value,indicator.getStatus());
	
	}
	
	/*
	 * This allows to access the db from a web browser : 
	 * localhost:8082
	 * 
	 * JDBC URL : jdbc:h2:mem:play;MODE=DB2
	 * user name /pass : sa/sa
	 */
	public static void startWebServer() {
		try {
			
			if(serverListening("localhost", 8082)) return;
			Server webServer = Server.createWebServer("-web","-webAllowOthers","-webPort","8082").start();
//			Server server = Server.createTcpServer("-tcp","-tcpAllowOthers","-tcpPort","9092").start();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	public static boolean serverListening(String host, int port)
	{
	    Socket s = null;
	    try {
	        s = new Socket(host, port);
	        return true;
	    }catch (Exception e){
	        return false;
	    }finally {
	        if(s != null)
	            try {s.close();}
	            catch(Exception e){}
	    }
	}
	
	

}
