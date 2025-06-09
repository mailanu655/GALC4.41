package com.honda.mfg.stamp.conveyor.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.honda.mfg.schedule.Scheduler;
import com.honda.mfg.stamp.storage.service.clientmgr.StampServiceServerSocketInterface;

public class StorageServiceMain implements Runnable {

	@Override
	public void run() {
		/*
		 * ApplicationContext context = new ClassPathXmlApplicationContext( new String[]
		 * {"META-INF/applicationContext.xml"});
		 */
		ctx = new ClassPathXmlApplicationContext(new String[] { "META-INF/applicationContext.xml" });
		// add a shutdown hook for the above context...
		try {
			org.apache.commons.dbcp.BasicDataSource ds = (BasicDataSource) ctx.getBean("dataSource");
			LOG.info("Using Data Source: " + ds.getUrl());
		} catch (Exception e) {

		}
		ctx.registerShutdownHook();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String mode = null;
		StampStorageShutdown shutdownHook = new StampStorageShutdown();
		if (args != null && args.length > 0) {
			mode = args[0];
		}
		if (mode == null) {
			mode = "";
		}
		if ("start".equals(mode)) {
			LOG.info(StorageServiceRuntimeStats.getStatsAsString());
			Runtime.getRuntime().addShutdownHook(shutdownHook);
			spring = Executors.newSingleThreadExecutor();
			spring.execute(new StorageServiceMain());
			Scheduler.pause(60, TimeUnit.SECONDS);
			try {
				StampServiceServerSocketInterface soServer = (StampServiceServerSocketInterface) ctx
						.getBean("serverSo");
				shutdownHook.setSoServer(soServer);
			} catch (Exception e) {
			}
		}

		else if ("stop".equals(mode)) {
			StampServiceServerSocketInterface soServer = (StampServiceServerSocketInterface) ctx.getBean("serverSo");
			if (soServer != null) {
				soServer.closeSocket();
			}
			spring.shutdownNow();
		}
	}

	public static ExecutorService spring = null;
	public static ConfigurableApplicationContext ctx = null;
	private static final Logger LOG = LoggerFactory.getLogger(StorageServiceMain.class);

}
