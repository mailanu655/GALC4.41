/**
 * 
 */
package com.honda.mfg.stamp.conveyor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.honda.mfg.stamp.storage.service.clientmgr.StampServiceServerSocketInterface;

/**
 * @author VCC44349
 *
 */
public class StampStorageShutdown extends Thread {

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOG.info("StampStorageShutdown#run: shutting down...");
		if(soServer != null)  {soServer.closeSocket();}
	}
	
	private StampServiceServerSocketInterface soServer;
	protected StampServiceServerSocketInterface getSoServer() {
		return soServer;
	}
	protected void setSoServer(StampServiceServerSocketInterface soServer) {
		this.soServer = soServer;
	}
    private static final Logger LOG = LoggerFactory.getLogger(StampStorageShutdown.class);
}
