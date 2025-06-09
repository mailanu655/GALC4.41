package com.honda.mfg.stamp.storage.service;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;



@Configurable
public class ServiceConnectionConfig {
	
		public ServiceConnectionConfig(int connectionManagerInterval)  {
			this.connectionManagerInterval = connectionManagerInterval;
		}
	
		private int connectionManagerInterval = 0;
		
		public int getConnectionManagerInterval() {
		return connectionManagerInterval;
		}
	
		public void setConnectionManagerInterval(int connectionManagerInterval) {
			this.connectionManagerInterval = connectionManagerInterval;
		}
	
		@Bean(initMethod="init", name="ServiceConnectionManager")
		  public ServiceConnectionManager serviceConnectionManager() {
		      return ServiceConnectionManager.getInstance(connectionManagerInterval);
		  }

		/*
		@Bean(name="serviceSocketFactory")
		  public SocketFactory serviceSocketFactory() {
			  ServiceConnectionManager scm = serviceConnectionManager();
		      return new SocketFactory(scm.getCurrentIp(), scm.getCurrentPort(), 5);
		  }
		
		@Bean(name="serviceStreamPair")
		  public SocketStreamPair serviceStreamPair() {
		      return new SocketStreamPair(serviceSocketFactory());
		  }

		@Bean(name="serviceProcessorPair")
		  public StreamConnectionProcessorPair serviceProcessorPair() {
		      return new StreamConnectionProcessorPair(serviceStreamPair());
		  }

		@Bean(name="serviceInitializer")
		  public ConnectionInitializer serviceInitializer() {
		      return new ConnectionInitializer(serviceProcessorPair(), 5);
		  }

		@Bean(name="basicServiceDevice")
		  public BasicConnection basicServiceDevice() {
		      return new BasicConnection(serviceProcessorPair(), serviceInitializer(), 5);
		  }

		@Bean(name="serviceDevicePing")
		  public ConnectionPing serviceDevicePing() {
		      return new ConnectionPing(basicServiceDevice());
		  }

		@Bean(name="serviceWatchdog")
		  public Watchdog serviceWatchdog() {
		      return new Watchdog(serviceStreamPair());
		  }

		@Bean(name="serviceWatchdogAdapter")
		  public WatchdogAdapter serviceWatchdogAdapter() {
		      return new WatchdogAdapter(serviceWatchdog(), serviceDevicePing(), 20);
		  }

		@Bean(name="serviceDevice")
		  public AdvancedConnection serviceDevice() {
		      return new AdvancedConnection(basicServiceDevice(), serviceWatchdogAdapter());
		  }
		  */

}
