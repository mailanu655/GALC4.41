package com.honda.mfg.stamp.storage.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.mfg.connection.processor.ConnectionInitializer;
import com.honda.mfg.connection.watchdog.WatchdogAdapterInterface;

public class DisposableBeanImpl implements DisposableBean {

    private static final Logger LOG = LoggerFactory.getLogger(DisposableBeanImpl.class);
    
	@Autowired
	private WatchdogAdapterInterface mesWatchdogAdapter;
	@Autowired
	private ConnectionInitializer mesInitializer;
	
	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub

		if(mesWatchdogAdapter != null)  {
			LOG.info("DisposableBeanImpl#destroy: shutting down watchdog adapter...");			
			mesWatchdogAdapter.stopRunning();
		}
		if(mesInitializer != null)  {
			mesInitializer.shutdown();
			LOG.info("DisposableBeanImpl#destroy: shutting down connection initializer...");			
		}
	}

}
