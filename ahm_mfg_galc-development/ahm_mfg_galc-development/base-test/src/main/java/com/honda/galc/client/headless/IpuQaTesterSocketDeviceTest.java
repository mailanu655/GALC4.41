/**
 * 
 */
package com.honda.galc.client.headless;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.honda.galc.client.device.ipuqatester.IpuQaTesterSocketDevice;

/**
 * @author Subu Kathiresan
 * @date Feb 8, 2012
 */
public class IpuQaTesterSocketDeviceTest {

	private IpuQaTesterSocketDevice _ipuQaTesterDevice = null;
	
	@Before
	public void before() {
		_ipuQaTesterDevice = new IpuQaTesterSocketDevice(5050);
		_ipuQaTesterDevice.activate();
	}

	@After
	public void after() {
		_ipuQaTesterDevice.deActivate();
	}
	
	
	@Test
	public void receive() {
		

	}
}
