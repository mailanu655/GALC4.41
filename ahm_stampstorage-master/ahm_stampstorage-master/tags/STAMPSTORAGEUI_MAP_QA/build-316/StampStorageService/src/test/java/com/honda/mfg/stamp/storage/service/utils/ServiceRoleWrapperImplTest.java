package com.honda.mfg.stamp.storage.service.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.bushe.swing.event.EventBus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import com.honda.mfg.connection.processor.messages.MissedPingPassive;
import com.honda.mfg.connection.watchdog.WatchdogAdapterInterface;
import com.honda.mfg.stamp.conveyor.domain.ServiceRole;
import com.honda.mfg.stamp.conveyor.processor.ConnectionEventMessage;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")

public class ServiceRoleWrapperImplTest {

	@Mock ServiceRole role1;
	@Mock ServiceRole role2;
	@Mock ServiceRole role3;
	@Mock WatchdogAdapterInterface wd;
	

	@Test
	public void testRun() {
		ServiceRoleWrapper servWrapper = ServiceRoleWrapperImpl.getInstance(getSocketProperties());
		assertNotNull(servWrapper);
		servWrapper.run();
	}

	@Test
	public void testInit() {
		ServiceRoleWrapper servWrapper = ServiceRoleWrapperImpl.getInstance();
		servWrapper.setSocketProperties(getSocketProperties());		
		servWrapper.init();
		assertNotNull(servWrapper.getLatch());
	}

	@Test
	public void testGetInstance() {
		Properties p = getSocketProperties();
		ServiceRoleWrapper servWrapper = ServiceRoleWrapperImpl.getInstance();
		servWrapper.setSocketProperties(p);
		assertNotNull(servWrapper);
	}

	@Test
	public void testGetInstanceProperties() {
		Properties p = getSocketProperties();
		ServiceRoleWrapper servWrapper = ServiceRoleWrapperImpl.getInstance(p);
		assertNotNull(servWrapper);
	}
	
	@Test
	public void testPassiveMissedPing() {
		Properties p = getSocketProperties();
		ServiceRoleWrapper servWrapper = ServiceRoleWrapperImpl.getInstance(p);
		EventBus.publish(new MissedPingPassive(2, 2));
		assertTrue(servWrapper.isPassive());
	}
	
	@Test
	public void testConnectionEvent()  {
		MockitoAnnotations.initMocks(this);
		Properties p = getSocketProperties();
		ServiceRoleWrapperImpl servWrapper = ServiceRoleWrapperImpl.getInstanceImpl(p);
		//WatchdogAdapterInterface wd = mock(WatchdogAdapter.class);
		servWrapper.setServiceWatchdogAdapter(wd);
		EventBus.publish(new ConnectionEventMessage(true));
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.schedule(
				new Runnable() {					
					@Override
					public void run() {
						//do nothing						
					}
				}, 2, TimeUnit.SECONDS);
		try {
			exec.awaitTermination(1, TimeUnit.SECONDS);
		} catch (Exception e) {
		}
		Mockito.verify(wd, Mockito.times(1)).resetMaxWaitCount();		
		assertFalse(servWrapper.isMissedPing());
	}
	
	@Test
	public void testNextInLine()  {
		MockitoAnnotations.initMocks(this);
		Properties p = getSocketProperties();
		ServiceRoleWrapperImpl servWrapper = ServiceRoleWrapperImpl.getInstanceImpl(p);
		//WatchdogAdapterInterface wd = mock(WatchdogAdapter.class);
		servWrapper.setServiceWatchdogAdapter(wd);
		
		List<ServiceRole> srList = createServiceRoleList(5);
		srList.get(0).setFailoverOrder(0);
		srList.get(1).setCurrentActive(true);
		boolean isNext = servWrapper.nextInLine(srList, srList.get(0), srList.get(1));
		assertFalse(isNext);
				
		resetServiceRole(srList.get(0));
		isNext = servWrapper.nextInLine(srList, srList.get(0), srList.get(0));
		assertTrue(isNext);

		isNext = servWrapper.nextInLine(srList, srList.get(2), srList.get(1));
		assertTrue(isNext);
		
		resetServiceRole(srList.get(1));
		srList.get(4).setCurrentActive(true);
		isNext = servWrapper.nextInLine(srList, srList.get(0), srList.get(4));
		assertTrue(isNext);
		
		Mockito.when(wd.isAdaptiveWaitCount()).thenReturn(true, false);
		isNext = servWrapper.nextInLine(srList, srList.get(2), srList.get(4));
		assertTrue(isNext);
		isNext = servWrapper.nextInLine(srList, srList.get(2), srList.get(4));
		assertFalse(isNext);
		Mockito.verify(wd, Mockito.times(1)).incrementWaitCount(Mockito.anyInt());
		assertFalse(servWrapper.isMissedPing());
		
	}

	public Properties getSocketProperties()  {
		Properties p = new Properties();
		p.put("MaxNumConnections", 1000);
		p.put("MaxNumTimeouts", 2);
		p.put("ServerPort", 44449);
		p.put("PreWriteDelay", 1);
		p.put("PreReadDelay", 1);
		return p;
	}
	
	public ServiceRole createServiceRole(long serviceId)  {
		ServiceRole role = new ServiceRole();
		role.setId(serviceId);
		role.setDesignatedPrimary(false);
		role.setCurrentActive(false);
		role.setFailoverOrder((int)serviceId);
		role.setHostName("HOST_" + serviceId);
		role.setIp("192.168.0." + serviceId);
		role.setPort(44449);
		role.setServiceName("LOCAL_" + serviceId);
		role.setVersion(1);	
		return role;
	}
	
	public ServiceRole resetServiceRole(ServiceRole role)  {
		role.setDesignatedPrimary(false);
		role.setCurrentActive(false);
		long fOrder = role.getId();
		role.setFailoverOrder((int)fOrder);
		return role;
	}
	
	public List createServiceRoleList(int howMany)  {
		List<ServiceRole> serviceList = new ArrayList<ServiceRole>();
		for(int i = 1; i <= howMany; i++)  {
			serviceList.add(createServiceRole(i));
		}
		return serviceList;
	}
}
