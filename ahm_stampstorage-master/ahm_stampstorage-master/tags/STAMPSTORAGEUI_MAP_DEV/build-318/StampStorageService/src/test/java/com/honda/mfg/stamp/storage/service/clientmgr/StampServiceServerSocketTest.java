package com.honda.mfg.stamp.storage.service.clientmgr;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.honda.io.socket.SocketFactory;
import com.honda.mfg.schedule.Scheduler;
import com.honda.mfg.stamp.conveyor.service.StorageStateUpdateService;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapper;
import com.honda.mfg.stamp.storage.service.utils.ServiceRoleWrapperImpl;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@Transactional
public class StampServiceServerSocketTest {

	@Autowired
	ApplicationContext context;
	Properties props_ = null;
	ServiceRoleWrapper servRoleWrapper = null;
	@Before
	public void setUp() throws Exception {
		
		/*
		FileInputStream fis = new FileInputStream("src/test/resources/stamp-service-socket.properties");
		props_ = new Properties();
		props_.load(fis);
		*/
		props_ = (Properties)context.getBean("socketProperties");
		servRoleWrapper = ServiceRoleWrapperImpl.getInstance(props_);

	}

	@Test
	public void testStampServiceServerSocketString() {
        AnnotationProcessor.process(this);
		StampServiceServerSocket soServer = null;
		//ServiceRoleWrapper servRoleWrapper = ServiceRoleWrapperImpl.getInstance(props_);
		try  {
		  //soServer = new StampServiceServerSocket("src/test/resources/stamp-service-socket.properties");
		  soServer = new StampServiceServerSocket(props_);
		}
		catch(Exception ex)  {
			ex.printStackTrace();
		}
        AnnotationProcessor.unprocess(this);

		assertNotNull(soServer);
	}

	@Test
	public void testStampServiceServerSocketProperties() {
		StampServiceServerSocket soServer = null;
		//ServiceRoleWrapper servRoleWrapper = ServiceRoleWrapperImpl.getInstance(props_);
		try  {
		  soServer = new StampServiceServerSocket(props_);
		}
		catch(Exception ex)  {
			ex.printStackTrace();
		}
		assertNotNull(soServer);
	}

	@Test
	public void testInit() {
		StampServiceServerSocket soServer = null;
		try  {
		  soServer = new StampServiceServerSocket(props_);
		  soServer.init();
		}
		catch(Exception ex)  {
			ex.printStackTrace();
		}
		assertNotNull(soServer);
		assertTrue(soServer.isbInitialized());
		
	}

	@Test
	public void testStart() {
		StampServiceServerSocket soServer = new StampServiceServerSocket(props_);

		final SocketFactory clientSocket = (SocketFactory)context.getBean("serviceSocketFactory");
		//create a separate thread to launch server socket
		ExecutorService exec = Executors.newSingleThreadExecutor();
		ExecutorService exec1 = Executors.newSingleThreadExecutor();
		
		class ServerSoRunner implements Runnable {
			ServerSoRunner(StampServiceServerSocket newSo)  {so = newSo;}
			StampServiceServerSocket so;
			StampServiceServerSocket getSo()  { return so; }
			void close()  {if(so != null)  so.closeSocket();}
			@Override
			public void run() {
					try {
						if(so != null)so.start();
					} catch (Exception e) {
						e.printStackTrace();
						if(so != null) so.closeSocket();
					}
			}
		}
		servRoleWrapper = ServiceRoleWrapperImpl.getInstance(props_);
		servRoleWrapper.init();
		ServerSoRunner sOx = new ServerSoRunner(soServer);
		try {
			exec.execute(sOx);
			servRoleWrapper.getLatch().countDown();
			try {
				exec1.execute(
					new Runnable() {					
						@Override
						public void run() {
							try {
								clientSocket.createSocket();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				);
			} catch (Exception e) {
				if(exec1 != null)  exec1.shutdown();
				e.printStackTrace();
			}

		} catch(Exception ex)  {
			if(exec != null)  {
				exec.shutdown();
				if(sOx != null)  sOx.close();
			}
			ex.printStackTrace();
		}

		
		Scheduler.pause(5, TimeUnit.SECONDS);
		assertEquals(1, soServer.getNumConnections());
		assertEquals(soServer.isbInitialized(), true);
		assertNotNull(soServer.getSocketMap());
		assertEquals(soServer.getSocketMap().size(), 1);
		sOx.close();
		exec.shutdown();
		exec1.shutdown();
	}

	@Test
	public void testRun() {
		StampServiceServerSocket soServer = new StampServiceServerSocket(props_);

		final SocketFactory clientSocket = (SocketFactory)context.getBean("serviceSocketFactory");
		//create a separate thread to launch server socket
		ExecutorService exec = Executors.newSingleThreadExecutor();
		ExecutorService exec1 = Executors.newSingleThreadExecutor();
		
		servRoleWrapper = ServiceRoleWrapperImpl.getInstance(props_);
		servRoleWrapper.init();
		try {
			exec.execute(soServer);
			servRoleWrapper.getLatch().countDown();
			try {
				exec1.execute(
					new Runnable() {					
						@Override
						public void run() {
							try {
								clientSocket.createSocket();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				);
			} catch (Exception e) {
				if(exec1 != null)  exec1.shutdown();
				e.printStackTrace();
			}
		}
		catch(Exception ex)  {
			if(soServer != null)  soServer.closeSocket();
			if(exec != null)  exec.shutdown();
			ex.printStackTrace();
		}
		
		Scheduler.pause(5, TimeUnit.SECONDS);
		assertEquals(1, soServer.getNumConnections());
		assertEquals(soServer.isbInitialized(), true);
		assertNotNull(soServer.getSocketMap());
		assertEquals(soServer.getSocketMap().size(), 1);
		
		soServer.closeSocket();
		exec.shutdown();
		exec1.shutdown();
	}

	@Test
	public void testCloseAndWait() {
		Map<String, StampServiceSocketConnectionInterface> socketMap = Mockito.mock(Map.class);
		Map<String, StampServiceSocketConnectionInterface> realMap = new HashMap<String, StampServiceSocketConnectionInterface>();
		StampServiceSocketConnectionInterface mockClient = mock(StampServiceSocketConnectionInterface.class);
		when(socketMap.isEmpty()).thenReturn(false, true);
		when(socketMap.values()).thenReturn(Arrays.asList(mockClient));
		
		StampServiceServerSocket soServer = new StampServiceServerSocket(props_);
		soServer.setSocketMap(socketMap);
		
		soServer.closeAndWait();
		verify(mockClient).closeSocket();
		
	}

	@Test
	public void testGetServerSocketTimeout() {
		StampServiceServerSocket soServer = new StampServiceServerSocket(props_);
		int soSocketTimeout = (new Integer(
				props_.getProperty(StampServiceSocketConnectionInterface.SERVER_SOCKET_TIMEOUT_PROPERTY_KEY)))
				.intValue();
		assertEquals(0, soServer.getServerSocketTimeout());
		try {
			soServer.init();
			assertEquals(soSocketTimeout, soServer.getServerSocketTimeout());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Test
	public void testGetNumTimeOuts() {
		//create a separate thread to launch server socket
		ExecutorService exec = Executors.newSingleThreadExecutor();
		props_.setProperty(StampServiceSocketConnectionInterface.SERVER_SOCKET_TIMEOUT_PROPERTY_KEY, "5000");
		StampServiceServerSocket soServer = new StampServiceServerSocket(props_);
		
		servRoleWrapper = ServiceRoleWrapperImpl.getInstance(props_);
		servRoleWrapper.init();
		try {
			exec.execute(soServer);
			servRoleWrapper.getLatch().countDown();
			Scheduler.pause(22, TimeUnit.SECONDS);
			soServer.setDone(true);
			assertEquals(true, soServer.isDone());
			assertTrue(soServer.getNumTimeOuts() > 3 && soServer.getNumTimeOuts() < 6);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(soServer != null)  {
				soServer.enableMaxTimeOuts(false);
				soServer.closeSocket();
			}
			if(exec != null) exec.shutdown();			
			props_.setProperty(StampServiceSocketConnectionInterface.SERVER_SOCKET_TIMEOUT_PROPERTY_KEY, String.valueOf(0));
		}
	}

	@Test
	public void testIsEnableMaxTimeOuts() {
		StampServiceServerSocket soServer = new StampServiceServerSocket(props_);
		soServer.enableMaxTimeOuts(true);
		assertEquals(true, soServer.isEnableMaxTimeOuts());
		soServer.enableMaxTimeOuts(false);
	}

	@Test
	public void testEnableMaxTimeOutsBoolean() {
		//create a separate thread to launch server socket
		ExecutorService exec = Executors.newSingleThreadExecutor();
		String oldValue = props_.getProperty(StampServiceSocketConnectionInterface.SERVER_SOCKET_TIMEOUT_PROPERTY_KEY);
		props_.setProperty(StampServiceSocketConnectionInterface.SERVER_SOCKET_TIMEOUT_PROPERTY_KEY, "10000");
		StampServiceServerSocket soServer = new StampServiceServerSocket(props_);
		
		servRoleWrapper = ServiceRoleWrapperImpl.getInstance(props_);
		servRoleWrapper.init();
		try {
			soServer.enableMaxTimeOuts(true);
			exec.execute(soServer);
			servRoleWrapper.getLatch().countDown();
			assertEquals(false, soServer.isDone());
			
			Scheduler.pause(24, TimeUnit.SECONDS);
			assertEquals(true, soServer.isDone());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(soServer != null)  {
				soServer.enableMaxTimeOuts(false);
				soServer.closeSocket();
			}
			if(exec != null) exec.shutdown();
			props_.setProperty(StampServiceSocketConnectionInterface.SERVER_SOCKET_TIMEOUT_PROPERTY_KEY, String.valueOf(0));
		}
	}

	//@Test
	public void testIsDone() {
		StampServiceServerSocket soServer = new StampServiceServerSocket(props_);
		assertEquals(false, soServer.isDone());
	}

	@Test
	public void testSetDone() {
		//create a separate thread to launch server socket
		ExecutorService exec = Executors.newSingleThreadExecutor();
		props_.setProperty(StampServiceSocketConnectionInterface.SERVER_SOCKET_TIMEOUT_PROPERTY_KEY, "10000");
		StampServiceServerSocket soServer = new StampServiceServerSocket(props_);
		
		servRoleWrapper = ServiceRoleWrapperImpl.getInstance(props_);
		servRoleWrapper.init();
		try {
			exec.execute(soServer);
			servRoleWrapper.getLatch().countDown();
			Scheduler.pause(12, TimeUnit.SECONDS);
			assertEquals(false, soServer.isDone());
			soServer.setDone(true);
			Scheduler.pause(12, TimeUnit.SECONDS);
			assertEquals(true, soServer.isDone());
			assertTrue(soServer.getNumTimeOuts() > 1 && soServer.getNumTimeOuts() < 4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(soServer != null)  {
				soServer.enableMaxTimeOuts(false);
				soServer.closeSocket();
			}
			if(exec != null) exec.shutdown();			
			props_.setProperty(StampServiceSocketConnectionInterface.SERVER_SOCKET_TIMEOUT_PROPERTY_KEY, String.valueOf(0));
		}
	}

	@Test
	public void testGetCarrierManager() {
		StorageStateUpdateService mockManager = mock(StorageStateUpdateService.class);
		StampServiceServerSocket soServer = new StampServiceServerSocket(props_);
		soServer.setCarrierManager(mockManager);
		assertNotNull(soServer.getCarrierManager());
		assertEquals(mockManager, soServer.getCarrierManager());
	}

	@Test
	public void testGetSocketMap() {
		Map<String, StampServiceSocketConnectionInterface> socketMap = Mockito.mock(Map.class);
		StampServiceServerSocket soServer = new StampServiceServerSocket(props_);
		soServer.setSocketMap(socketMap);
		assertNotNull(soServer.getSocketMap());
	}

	@Test
	public void testSetSocketMap() {
		Map<String, StampServiceSocketConnectionInterface> socketMap = Mockito.mock(Map.class);
		StampServiceServerSocket soServer = new StampServiceServerSocket(props_);
		soServer.setSocketMap(socketMap);
		soServer.roleChange(true);
		verify(socketMap).isEmpty();
	}

	@Test
	public void testRoleChange() {
		Map<String, StampServiceSocketConnectionInterface> socketMap = Mockito.mock(Map.class);
		Map<String, StampServiceSocketConnectionInterface> realMap = new HashMap<String, StampServiceSocketConnectionInterface>();
		StampServiceSocketConnectionInterface mockClient = mock(StampServiceSocketConnectionInterface.class);
		when(socketMap.isEmpty()).thenReturn(false, true);
		when(socketMap.values()).thenReturn(Arrays.asList(mockClient));
		
		StampServiceServerSocket soServer = new StampServiceServerSocket(props_);
		soServer.setSocketMap(socketMap);
		
		soServer.roleChange(false);
		verifyZeroInteractions(mockClient);
		soServer.roleChange(true);
		verify(mockClient).closeSocket();
		
	}

}
