package com.honda.galc.test.device;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.simulator.data.DataContainerProcessor;
import com.honda.galc.test.common.LoggerFactory;

/**
 * <h3>DeviceSimulator</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <P>
 * 
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Paul Chou</TD>
 * <TD>Feb 10, 2009</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial version</TD>
 * </TR>
 * </TABLE>
 */
public class SocketServerHandler implements DataContainerProcessor, ITestListener{
	private Logger log = LoggerFactory.createLogger(getClass().getSimpleName());
	private BlockingQueue<DataContainer> queue;
	
	public SocketServerHandler() {
		super();
		initialize();
	}

	private void initialize() {
		this.queue = new LinkedBlockingQueue<DataContainer>();
	}

	public DataContainer processDataContainer(DataContainer dc) {
		queue.add(dc);
		
	DataContainer rData= new DefaultDataContainer();
        rData.put(DataContainerTag.DATA, "1");
        rData.put(DataContainerTag.CLIENT_ID, "OPCACK");
        log.info("Received data container: " + dc);
        
        return rData;
	}
	
	public DataContainer waitForData(long milliSeconds)
	{
		DataContainer dc = null;
		try {
			if (milliSeconds < 0) {
				dc = queue.take();
			} else {
				dc = queue.poll(milliSeconds, TimeUnit.MILLISECONDS);
			}			
		} catch (InterruptedException e) {
			// do nothing
		}
		log.info("Received data container: " + dc);
		
		return dc;
	}
	
	public void cleanUpQueue(){
	    queue.clear();
	}
	
        //getters & setters
	public BlockingQueue<DataContainer> getQueue() {
		return queue;
	}

	public void setQueue(BlockingQueue<DataContainer> queue) {
		this.queue = queue;
	}
	
	

}
